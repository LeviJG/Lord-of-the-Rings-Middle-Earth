package net.blueskiez77.lord_of_the_rings__middle_earth.common.fac;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRDimension;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

/**
 * LOTRPlayerData's viewingFaction ("CurrentFaction") -- the faction whose
 * alignment the player is looking at, on the factions screen and the
 * alignment bar -- and the faction last viewed in each region
 * ("PrevRegionFactions"). The client picks; the server keeps and sends it
 * back (LOTRPacketClientInfo, LOTRPacketUpdateViewingFaction).
 */
public record LOTRViewingFaction(LOTRFaction viewingFaction, Map<LOTRDimension.DimensionRegion, LOTRFaction> prevRegionFactions) {

    public static final LOTRViewingFaction DEFAULT = new LOTRViewingFaction(LOTRFaction.HOBBIT, Map.of());

    private static final Codec<LOTRDimension.DimensionRegion> REGION_CODEC =
            Codec.STRING.xmap(LOTRDimension.DimensionRegion::forName, LOTRDimension.DimensionRegion::codeName);

    public static final Codec<LOTRViewingFaction> CODEC = RecordCodecBuilder.create(i -> i.group(
            LOTRFaction.CODEC.optionalFieldOf("CurrentFaction", LOTRFaction.HOBBIT).forGetter(LOTRViewingFaction::viewingFaction),
            Codec.unboundedMap(REGION_CODEC, LOTRFaction.CODEC).optionalFieldOf("PrevRegionFactions", Map.of())
                    .forGetter(LOTRViewingFaction::prevRegionFactions)
    ).apply(i, LOTRViewingFaction::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, LOTRFaction> FACTION_STREAM_CODEC =
            ByteBufCodecs.STRING_UTF8.map(LOTRFaction::forName, LOTRFaction::codeName).cast();
    public static final StreamCodec<RegistryFriendlyByteBuf, LOTRDimension.DimensionRegion> REGION_STREAM_CODEC =
            ByteBufCodecs.STRING_UTF8.map(LOTRDimension.DimensionRegion::forName, LOTRDimension.DimensionRegion::codeName).cast();

    public static final StreamCodec<RegistryFriendlyByteBuf, LOTRViewingFaction> STREAM_CODEC = StreamCodec.composite(
            FACTION_STREAM_CODEC, LOTRViewingFaction::viewingFaction,
            ByteBufCodecs.map(HashMap::new, REGION_STREAM_CODEC, FACTION_STREAM_CODEC), v -> new HashMap<>(v.prevRegionFactions()),
            LOTRViewingFaction::new);

    public static final AttachmentType<LOTRViewingFaction> VIEWING_FACTION = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "viewing_faction"),
            builder -> builder.initializer(() -> DEFAULT).persistent(CODEC).copyOnDeath()
                    .syncWith(STREAM_CODEC, AttachmentSyncPredicate.targetOnly()));

    public static LOTRFaction getViewingFaction(Player player) {
        return player.getAttachedOrCreate(VIEWING_FACTION).viewingFaction();
    }

    public static void setViewingFaction(Player player, LOTRFaction faction) {
        if (faction != null) {
            LOTRViewingFaction v = player.getAttachedOrCreate(VIEWING_FACTION);
            player.setAttached(VIEWING_FACTION, new LOTRViewingFaction(faction, v.prevRegionFactions()));
        }
    }

    /** getRegionLastViewedFaction: the region's first faction if none yet. */
    public static LOTRFaction getRegionLastViewedFaction(Player player, LOTRDimension.DimensionRegion region) {
        LOTRFaction fac = player.getAttachedOrCreate(VIEWING_FACTION).prevRegionFactions().get(region);
        return fac != null ? fac : region.factionList.get(0);
    }

    public static void setRegionLastViewedFaction(Player player, LOTRDimension.DimensionRegion region, LOTRFaction fac) {
        if (region.factionList.contains(fac)) {
            LOTRViewingFaction v = player.getAttachedOrCreate(VIEWING_FACTION);
            Map<LOTRDimension.DimensionRegion, LOTRFaction> map = new HashMap<>(v.prevRegionFactions());
            map.put(region, fac);
            player.setAttached(VIEWING_FACTION, new LOTRViewingFaction(v.viewingFaction(), Map.copyOf(map)));
        }
    }

    public static void init() {
    }
}
