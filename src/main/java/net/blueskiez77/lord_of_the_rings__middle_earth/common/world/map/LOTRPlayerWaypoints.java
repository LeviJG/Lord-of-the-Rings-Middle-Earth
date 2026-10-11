package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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

import org.jspecify.annotations.Nullable;

/**
 * LOTRPlayerData's waypoints: the regions whose waypoints the player has unlocked by going there
 * ("UnlockedFTRegions"), how often they have travelled to each waypoint, which lengthens the wait
 * less each time ("WPUses"), and the waypoint last travelled to ("LastWP"). Kept with the player
 * and sent to them for the map (LOTRPacketWaypointRegion, LOTRPacketWaypointUseCount).
 */
public record LOTRPlayerWaypoints(Set<LOTRWaypoint.Region> unlockedFTRegions, Map<LOTRWaypoint, Integer> wpUseCounts,
                                  Optional<LOTRWaypoint> lastWaypoint) {

    public static final LOTRPlayerWaypoints EMPTY = new LOTRPlayerWaypoints(Set.of(), Map.of(), Optional.empty());

    private static final Codec<LOTRWaypoint.Region> REGION_CODEC = Codec.STRING.xmap(
            name -> {
                LOTRWaypoint.Region region = LOTRWaypoint.regionForName(name);
                return region == null ? LOTRWaypoint.Region.OCEAN : region;
            }, LOTRWaypoint.Region::name);
    private static final Codec<LOTRWaypoint> WAYPOINT_CODEC = Codec.STRING.xmap(
            name -> {
                LOTRWaypoint wp = LOTRWaypoint.waypointForName(name);
                return wp == null ? LOTRWaypoint.values()[0] : wp;
            }, LOTRWaypoint::getCodeName);

    /** {"Name": region}. */
    private record RegionEntry(String name) {
        static final Codec<RegionEntry> CODEC = RecordCodecBuilder.create(i -> i.group(
                Codec.STRING.fieldOf("Name").forGetter(RegionEntry::name)).apply(i, RegionEntry::new));
    }

    /** {"WPName": waypoint, "Count": uses}. */
    private record UseEntry(String name, int count) {
        static final Codec<UseEntry> CODEC = RecordCodecBuilder.create(i -> i.group(
                Codec.STRING.fieldOf("WPName").forGetter(UseEntry::name),
                Codec.INT.fieldOf("Count").forGetter(UseEntry::count)).apply(i, UseEntry::new));
    }

    public static final Codec<LOTRPlayerWaypoints> CODEC = RecordCodecBuilder.create(i -> i.group(
            RegionEntry.CODEC.listOf().optionalFieldOf("UnlockedFTRegions", List.of()).forGetter(d ->
                    d.unlockedFTRegions().stream().map(r -> new RegionEntry(r.name())).toList()),
            UseEntry.CODEC.listOf().optionalFieldOf("WPUses", List.of()).forGetter(d ->
                    d.wpUseCounts().entrySet().stream().map(e -> new UseEntry(e.getKey().getCodeName(), e.getValue())).toList()),
            Codec.STRING.optionalFieldOf("LastWP").forGetter(d -> d.lastWaypoint().map(LOTRWaypoint::getCodeName))
    ).apply(i, (regions, uses, last) -> {
        Set<LOTRWaypoint.Region> unlocked = EnumSet.noneOf(LOTRWaypoint.Region.class);
        for (RegionEntry entry : regions) {
            LOTRWaypoint.Region region = LOTRWaypoint.regionForName(entry.name());
            if (region != null) {
                unlocked.add(region);
            }
        }
        Map<LOTRWaypoint, Integer> counts = new EnumMap<>(LOTRWaypoint.class);
        for (UseEntry entry : uses) {
            LOTRWaypoint wp = LOTRWaypoint.waypointForName(entry.name());
            if (wp != null) {
                counts.put(wp, entry.count());
            }
        }
        return new LOTRPlayerWaypoints(Set.copyOf(unlocked), Map.copyOf(counts),
                last.map(LOTRWaypoint::waypointForName));
    }));

    public static final StreamCodec<RegistryFriendlyByteBuf, LOTRPlayerWaypoints> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.fromCodec(REGION_CODEC).apply(ByteBufCodecs.list()), d -> List.copyOf(d.unlockedFTRegions()),
            ByteBufCodecs.map(java.util.HashMap::new, ByteBufCodecs.fromCodec(WAYPOINT_CODEC), ByteBufCodecs.VAR_INT),
            d -> new java.util.HashMap<>(d.wpUseCounts()),
            ByteBufCodecs.optional(ByteBufCodecs.fromCodec(WAYPOINT_CODEC)), LOTRPlayerWaypoints::lastWaypoint,
            (regions, counts, last) -> new LOTRPlayerWaypoints(Set.copyOf(regions), Map.copyOf(counts), last));

    public static final AttachmentType<LOTRPlayerWaypoints> WAYPOINTS = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "waypoints"),
            builder -> builder.initializer(() -> EMPTY).persistent(CODEC).copyOnDeath()
                    .syncWith(STREAM_CODEC, AttachmentSyncPredicate.targetOnly()));

    private static LOTRPlayerWaypoints get(Player player) {
        return player.getAttachedOrCreate(WAYPOINTS);
    }

    public static boolean isFTRegionUnlocked(Player player, LOTRWaypoint.Region region) {
        return get(player).unlockedFTRegions().contains(region);
    }

    /** unlockFTRegion. */
    public static void unlockFTRegion(Player player, LOTRWaypoint.Region region) {
        LOTRPlayerWaypoints d = get(player);
        if (!d.unlockedFTRegions().contains(region)) {
            Set<LOTRWaypoint.Region> regions = EnumSet.noneOf(LOTRWaypoint.Region.class);
            regions.addAll(d.unlockedFTRegions());
            regions.add(region);
            player.setAttached(WAYPOINTS, new LOTRPlayerWaypoints(Set.copyOf(regions), d.wpUseCounts(), d.lastWaypoint()));
        }
    }

    /** lockFTRegion. */
    public static void lockFTRegion(Player player, LOTRWaypoint.Region region) {
        LOTRPlayerWaypoints d = get(player);
        if (d.unlockedFTRegions().contains(region)) {
            Set<LOTRWaypoint.Region> regions = EnumSet.noneOf(LOTRWaypoint.Region.class);
            regions.addAll(d.unlockedFTRegions());
            regions.remove(region);
            player.setAttached(WAYPOINTS, new LOTRPlayerWaypoints(Set.copyOf(regions), d.wpUseCounts(), d.lastWaypoint()));
        }
    }

    /** getWPUseCount, for a fixed waypoint (custom ones keep their own counts). */
    public static int getWPUseCount(Player player, LOTRWaypoint wp) {
        return get(player).wpUseCounts().getOrDefault(wp, 0);
    }

    public static void setWPUseCount(Player player, LOTRWaypoint wp, int count) {
        LOTRPlayerWaypoints d = get(player);
        Map<LOTRWaypoint, Integer> counts = new EnumMap<>(LOTRWaypoint.class);
        counts.putAll(d.wpUseCounts());
        counts.put(wp, count);
        player.setAttached(WAYPOINTS, new LOTRPlayerWaypoints(d.unlockedFTRegions(), Map.copyOf(counts), d.lastWaypoint()));
    }

    /** getLastKnownWaypoint. */
    public static @Nullable LOTRWaypoint getLastKnownWaypoint(Player player) {
        return get(player).lastWaypoint().orElse(null);
    }

    public static void setLastWaypoint(Player player, LOTRWaypoint wp) {
        LOTRPlayerWaypoints d = get(player);
        player.setAttached(WAYPOINTS, new LOTRPlayerWaypoints(d.unlockedFTRegions(), d.wpUseCounts(), Optional.of(wp)));
    }

    public static void init() {
    }
}
