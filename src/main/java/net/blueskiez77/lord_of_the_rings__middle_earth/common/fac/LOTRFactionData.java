package net.blueskiez77.lord_of_the_rings__middle_earth.common.fac;

import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

/**
 * LOTRFactionData: a player's record with each faction -- its NPCs killed,
 * its enemies killed, trades and hires made, mini-quests done, conquest
 * earned, and whether its conquest horn has been taken. Saved under the
 * original's keys and sent to the player (LOTRPacketFactionData) for the
 * factions screen.
 *
 * <p>NOT ported yet: conquest earned (conquest, D14) -- it stays at nothing
 * until then.
 */
public record LOTRFactionData(int npcsKilled, int enemiesKilled, int tradeCount, int hireCount,
                              int miniQuestsCompleted, float conquestEarned, boolean hasConquestHorn) {

    public static final LOTRFactionData EMPTY = new LOTRFactionData(0, 0, 0, 0, 0, 0.0f, false);

    public static final Codec<LOTRFactionData> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.INT.optionalFieldOf("NPCKill", 0).forGetter(LOTRFactionData::npcsKilled),
            Codec.INT.optionalFieldOf("EnemyKill", 0).forGetter(LOTRFactionData::enemiesKilled),
            Codec.INT.optionalFieldOf("Trades", 0).forGetter(LOTRFactionData::tradeCount),
            Codec.INT.optionalFieldOf("Hired", 0).forGetter(LOTRFactionData::hireCount),
            Codec.INT.optionalFieldOf("MiniQuests", 0).forGetter(LOTRFactionData::miniQuestsCompleted),
            Codec.FLOAT.optionalFieldOf("Conquest", 0.0f).forGetter(LOTRFactionData::conquestEarned),
            Codec.BOOL.optionalFieldOf("ConquestHorn", false).forGetter(LOTRFactionData::hasConquestHorn)
    ).apply(i, LOTRFactionData::new));

    private static final StreamCodec<RegistryFriendlyByteBuf, LOTRFactionData> ENTRY_STREAM_CODEC = StreamCodec.of(
            (buf, d) -> {
                buf.writeVarInt(d.npcsKilled);
                buf.writeVarInt(d.enemiesKilled);
                buf.writeVarInt(d.tradeCount);
                buf.writeVarInt(d.hireCount);
                buf.writeVarInt(d.miniQuestsCompleted);
                buf.writeFloat(d.conquestEarned);
                buf.writeBoolean(d.hasConquestHorn);
            },
            buf -> new LOTRFactionData(buf.readVarInt(), buf.readVarInt(), buf.readVarInt(), buf.readVarInt(),
                    buf.readVarInt(), buf.readFloat(), buf.readBoolean()));

    private static final StreamCodec<RegistryFriendlyByteBuf, LOTRFaction> FACTION_STREAM_CODEC =
            ByteBufCodecs.STRING_UTF8.map(LOTRFaction::forName, LOTRFaction::codeName).cast();

    public static final AttachmentType<Map<LOTRFaction, LOTRFactionData>> FACTION_DATA = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "faction_data"),
            builder -> builder.initializer(Map::of)
                    .persistent(Codec.unboundedMap(LOTRFaction.CODEC, CODEC))
                    .copyOnDeath()
                    .syncWith(ByteBufCodecs.<RegistryFriendlyByteBuf, LOTRFaction, LOTRFactionData, Map<LOTRFaction, LOTRFactionData>>map(
                            HashMap::new, FACTION_STREAM_CODEC, ENTRY_STREAM_CODEC), AttachmentSyncPredicate.targetOnly()));

    public static LOTRFactionData get(Player player, LOTRFaction faction) {
        return player.getAttachedOrCreate(FACTION_DATA).getOrDefault(faction, EMPTY);
    }

    private static void update(Player player, LOTRFaction faction, UnaryOperator<LOTRFactionData> change) {
        Map<LOTRFaction, LOTRFactionData> map = new HashMap<>(player.getAttachedOrCreate(FACTION_DATA));
        map.put(faction, change.apply(map.getOrDefault(faction, EMPTY)));
        player.setAttached(FACTION_DATA, Map.copyOf(map));
    }

    public static void addNPCKill(Player player, LOTRFaction faction) {
        update(player, faction, d -> new LOTRFactionData(d.npcsKilled + 1, d.enemiesKilled, d.tradeCount, d.hireCount,
                d.miniQuestsCompleted, d.conquestEarned, d.hasConquestHorn));
    }

    public static void addEnemyKill(Player player, LOTRFaction faction) {
        update(player, faction, d -> new LOTRFactionData(d.npcsKilled, d.enemiesKilled + 1, d.tradeCount, d.hireCount,
                d.miniQuestsCompleted, d.conquestEarned, d.hasConquestHorn));
    }

    public static void addTrade(Player player, LOTRFaction faction) {
        update(player, faction, d -> new LOTRFactionData(d.npcsKilled, d.enemiesKilled, d.tradeCount + 1, d.hireCount,
                d.miniQuestsCompleted, d.conquestEarned, d.hasConquestHorn));
    }

    public static void addHire(Player player, LOTRFaction faction) {
        update(player, faction, d -> new LOTRFactionData(d.npcsKilled, d.enemiesKilled, d.tradeCount, d.hireCount + 1,
                d.miniQuestsCompleted, d.conquestEarned, d.hasConquestHorn));
    }

    public static void completeMiniQuest(Player player, LOTRFaction faction) {
        update(player, faction, d -> new LOTRFactionData(d.npcsKilled, d.enemiesKilled, d.tradeCount, d.hireCount,
                d.miniQuestsCompleted + 1, d.conquestEarned, d.hasConquestHorn));
    }

    public static void takeConquestHorn(Player player, LOTRFaction faction) {
        update(player, faction, d -> new LOTRFactionData(d.npcsKilled, d.enemiesKilled, d.tradeCount, d.hireCount,
                d.miniQuestsCompleted, d.conquestEarned, true));
    }

    public static void init() {
    }
}
