package net.blueskiez77.lord_of_the_rings__middle_earth.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFactionRelations;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRAlignmentZonesPayload;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRFactionRelationsPayload;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import org.jspecify.annotations.Nullable;

/**
 * The world-wide state LOTRLevelData and faction_relations.dat kept: whether
 * control zones ("alignment areas of influence") are on, and the relations a
 * /facRelations has overridden, and the Grey Wanderers abroad
 * (LOTRGreyWandererTracker). All stay in the statics the rest of the mod
 * reads, as in the original; this saves them with the world and tells
 * clients -- the whole set on login, again on every change.
 */
public final class LOTRLevelData extends SavedData {

    /** LOTRPacketDate: the Shire Reckoning's day, for the red book. */
    public record LOTRDatePayload(int shireDate) implements net.minecraft.network.protocol.common.custom.CustomPacketPayload {
        public static final Type<LOTRDatePayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "date"));
        public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, LOTRDatePayload> STREAM_CODEC =
                net.minecraft.network.codec.StreamCodec.composite(net.minecraft.network.codec.ByteBufCodecs.VAR_INT,
                        LOTRDatePayload::shireDate, LOTRDatePayload::new);

        @java.lang.Override
        public Type<? extends net.minecraft.network.protocol.common.custom.CustomPacketPayload> type() {
            return TYPE;
        }
    }


    /** enableAlignmentZones: on unless a save says otherwise. */
    public static boolean enableAlignmentZones = true;
    /** gollumSpawned: whether Gollum is abroad in the world, so that LOTRGollumSpawner makes only one. */
    public static boolean gollumSpawned;
    /** structuresBanned: 1 when /banStructures has turned structure spawners off for everyone. */
    public static int structuresBanned;

    private static @Nullable LOTRLevelData instance;
    private static @Nullable MinecraftServer server;

    private record Override(LOTRFaction fac1, LOTRFaction fac2, LOTRFactionRelations.Relation relation) {
        static final Codec<Override> CODEC = RecordCodecBuilder.create(i -> i.group(
                LOTRFaction.CODEC.fieldOf("FacPair1").forGetter(Override::fac1),
                LOTRFaction.CODEC.fieldOf("FacPair2").forGetter(Override::fac2),
                Codec.STRING.xmap(LOTRFactionRelations.Relation::forName, LOTRFactionRelations.Relation::codeName)
                        .fieldOf("Rel").forGetter(Override::relation)).apply(i, Override::new));
    }

    private static final Codec<LOTRLevelData> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.BOOL.optionalFieldOf("AlignmentZones", true).forGetter(d -> enableAlignmentZones),
            Override.CODEC.listOf().optionalFieldOf("Overrides", List.of()).forGetter(d -> currentOverrides()),
            LOTRGreyWandererTracker.Entry.CODEC.listOf().optionalFieldOf("GreyWanderers", List.of())
                    .forGetter(d -> LOTRGreyWandererTracker.save()),
            Codec.INT.optionalFieldOf("GWSpawnTick", 2400).forGetter(d -> LOTRGreyWandererTracker.spawnCooldown),
            Codec.BOOL.optionalFieldOf("GollumSpawned", false).forGetter(d -> gollumSpawned),
            Codec.INT.optionalFieldOf("StructuresBanned", 0).forGetter(d -> structuresBanned),
            // LOTRDate.saveDates: {"Dates": {"ShireDate": day}}.
            Codec.INT.fieldOf("ShireDate").codec().optionalFieldOf("Dates", 0)
                    .forGetter(d -> LOTRDate.ShireReckoning.currentDay)
    ).apply(i, (zones, overrides, wanderers, gwSpawnTick, gollum, banned, shireDate) -> {
        LOTRDate.ShireReckoning.currentDay = shireDate;
        gollumSpawned = gollum;
        structuresBanned = banned;
        LOTRGreyWandererTracker.load(wanderers, gwSpawnTick);
        enableAlignmentZones = zones;
        LOTRFactionRelations.overrideMap.clear();
        for (Override o : overrides) {
            if (o.relation() != null) {
                LOTRFactionRelations.overrideMap.put(new LOTRFactionRelations.FactionPair(o.fac1(), o.fac2()), o.relation());
            }
        }
        return new LOTRLevelData();
    }));

    private static final SavedDataType<LOTRLevelData> TYPE = new SavedDataType<>(
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "level_data"), LOTRLevelData::fresh, CODEC, null);

    private static LOTRLevelData fresh() {
        enableAlignmentZones = true;
        LOTRFactionRelations.overrideMap.clear();
        LOTRGreyWandererTracker.reset();
        gollumSpawned = false;
        structuresBanned = 0;
        LOTRDate.ShireReckoning.currentDay = 0;
        return new LOTRLevelData();
    }

    private static List<Override> currentOverrides() {
        List<Override> list = new ArrayList<>();
        for (Map.Entry<LOTRFactionRelations.FactionPair, LOTRFactionRelations.Relation> e
                : LOTRFactionRelations.overrideMap.entrySet()) {
            list.add(new Override(e.getKey().getLeft(), e.getKey().getRight(), e.getValue()));
        }
        return list;
    }

    public static void init() {
        net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry.clientboundPlay().register(LOTRDatePayload.TYPE,
                LOTRDatePayload.STREAM_CODEC);
        ServerLifecycleEvents.SERVER_STARTED.register(s -> {
            server = s;
            instance = s.getDataStorage().computeIfAbsent(TYPE);
        });
        ServerLifecycleEvents.SERVER_STOPPED.register(s -> {
            server = null;
            instance = null;
        });
        ServerPlayConnectionEvents.JOIN.register((handler, sender, s) -> {
            ServerPlayNetworking.send(handler.player, new LOTRAlignmentZonesPayload(enableAlignmentZones));
            ServerPlayNetworking.send(handler.player, relationsPayload());
            ServerPlayNetworking.send(handler.player, new LOTRDatePayload(LOTRDate.ShireReckoning.currentDay));
        });
    }

    private static LOTRFactionRelationsPayload relationsPayload() {
        return new LOTRFactionRelationsPayload(Map.copyOf(LOTRFactionRelations.overrideMap));
    }

    static void markDirty() {
        if (instance != null) {
            instance.setDirty();
        }
    }

    public static boolean structuresBanned() {
        return structuresBanned == 1;
    }

    public static void setStructuresBanned(boolean banned) {
        structuresBanned = banned ? 1 : 0;
        markDirty();
    }

    public static void setGollumSpawned(boolean flag) {
        gollumSpawned = flag;
        markDirty();
    }

    /** setEnableAlignmentZones: saved, and every player told. */
    public static void setEnableAlignmentZones(boolean flag) {
        enableAlignmentZones = flag;
        markDirty();
        if (server != null) {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                ServerPlayNetworking.send(player, new LOTRAlignmentZonesPayload(flag));
            }
        }
    }

    /** After an override or reset: saved, and every player sent the new set. */
    public static void onRelationsChanged() {
        markDirty();
        if (server != null) {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                ServerPlayNetworking.send(player, relationsPayload());
            }
        }
    }
}
