package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.mojang.serialization.Codec;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRLevelData;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRPlayerAchievements;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.config.LOTRConfig;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFactionRank;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRWorldGen;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiomes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.genlayer.LOTRGenLayerWorld;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import org.jspecify.annotations.Nullable;

/**
 * LOTRConquestGrid: Middle-earth's conquest, a grid over the map of zones eight map pixels a side
 * (LOTRConquestZone). A pledged player's kills of their faction's enemies raise its strength in the
 * zones about them, or first wear down the enemy's own there; a zone's strengths draw more of that
 * faction's NPCs to it (LOTRBiomeSpawnList), and an enemy waypoint in a zone held strongly enough
 * opens to fast travel (LOTRWaypoint).
 *
 * <p>The original kept each zone in a file of its own in the LOTR folder ("conquest_zones"); here
 * they are one SavedData, "lotr:conquest_zones", each zone under the original's keys. The players'
 * waypoints opened by conquest are worked out on the server and sent to them for the map.
 */
public final class LOTRConquestGrid {

    public static final int MAP_GRID_SCALE = 8;
    public static final float MAX_CONQUEST_SET = 100000.0f;
    private static final Map<GridCoordPair, LOTRConquestZone> zoneMap = new HashMap<>();
    private static final LOTRConquestZone dummyZone = new LOTRConquestZone(-999, -999).setDummyZone();
    private static final Collection<GridCoordPair> dirtyZones = new HashSet<>();
    private static final Map<GridCoordPair, List<LOTRFaction>> cachedZoneFactions = new HashMap<>();
    private static @Nullable Storage storage;

    /** LOTRPlayerData's ConquestKills: whether the player's kills count towards conquest. */
    public static final AttachmentType<Boolean> CONQUEST_KILLS = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "conquest_kills"),
            builder -> builder.initializer(() -> true).persistent(Codec.BOOL).copyOnDeath()
                    .syncWith(ByteBufCodecs.BOOL, AttachmentSyncPredicate.targetOnly()));

    /** The waypoints (by ordinal) the player has opened by conquest, worked out on the server for their map. */
    public static final AttachmentType<List<Integer>> CONQUEST_WAYPOINTS = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "conquest_waypoints"),
            builder -> builder.initializer(List::of)
                    .syncWith(ByteBufCodecs.VAR_INT.apply(ByteBufCodecs.list()), AttachmentSyncPredicate.targetOnly()));

    /** LOTRPacketConquestNotification: a zone the player stands in passed a multiple of fifty. */
    public record NotificationPayload(LOTRFaction faction, float conquest, boolean cleansing) implements CustomPacketPayload {
        public static final Type<NotificationPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "conquest_notification"));
        public static final StreamCodec<RegistryFriendlyByteBuf, NotificationPayload> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT.map(LOTRFaction::forID, LOTRFaction::ordinal), NotificationPayload::faction,
                ByteBufCodecs.FLOAT, NotificationPayload::conquest,
                ByteBufCodecs.BOOL, NotificationPayload::cleansing,
                NotificationPayload::new);

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    /** LOTRPacketConquestGridRequest: the conquest map asks for a faction's grid. */
    public record GridRequestPayload(LOTRFaction faction) implements CustomPacketPayload {
        public static final Type<GridRequestPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "conquest_grid_request"));
        public static final StreamCodec<RegistryFriendlyByteBuf, GridRequestPayload> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT.map(LOTRFaction::forID, LOTRFaction::ordinal), GridRequestPayload::faction,
                GridRequestPayload::new);

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    /** One zone in LOTRPacketConquestGrid: where it is, when it last changed, and the faction's raw strength. */
    public record GridZone(short gridX, short gridZ, long time, float strength) {
        public static final StreamCodec<RegistryFriendlyByteBuf, GridZone> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.SHORT, GridZone::gridX, ByteBufCodecs.SHORT, GridZone::gridZ,
                ByteBufCodecs.LONG, GridZone::time, ByteBufCodecs.FLOAT, GridZone::strength, GridZone::new);

        /** The zone as the client keeps it, holding the faction's strength alone. */
        public LOTRConquestZone toClientZone(LOTRFaction faction) {
            LOTRConquestZone zone = new LOTRConquestZone(this.gridX, this.gridZ).setClientSide();
            zone.setLastChangeTime(this.time);
            zone.setConquestStrengthRaw(faction, this.strength);
            return zone;
        }
    }

    /** LOTRPacketConquestGrid: the faction's zones where it still holds any strength. */
    public record GridPayload(LOTRFaction faction, List<GridZone> zones) implements CustomPacketPayload {
        public static final Type<GridPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "conquest_grid"));
        public static final StreamCodec<RegistryFriendlyByteBuf, GridPayload> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT.map(LOTRFaction::forID, LOTRFaction::ordinal), GridPayload::faction,
                GridZone.STREAM_CODEC.apply(ByteBufCodecs.list()), GridPayload::zones,
                GridPayload::new);

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    /** sendClientsideGUI 60: the Table of Command opens the conquest map. */
    public record OpenMapPayload() implements CustomPacketPayload {
        public static final Type<OpenMapPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "open_conquest_map"));
        public static final StreamCodec<RegistryFriendlyByteBuf, OpenMapPayload> STREAM_CODEC = StreamCodec.unit(new OpenMapPayload());

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    /** The zones as saved: each non-empty zone's compound, as the original's zone files held it. */
    private static final class Storage extends SavedData {
        static final Codec<Storage> CODEC = CompoundTag.CODEC.listOf().optionalFieldOf("Zones", List.of()).codec()
                .xmap(Storage::load, Storage::save);
        static final SavedDataType<Storage> TYPE = new SavedDataType<>(
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "conquest_zones"), Storage::new, CODEC, null);

        private static Storage load(List<CompoundTag> zones) {
            zoneMap.clear();
            dirtyZones.clear();
            for (CompoundTag nbt : zones) {
                try {
                    LOTRConquestZone zone = LOTRConquestZone.readFromNBT(nbt);
                    if (!zone.isEmpty()) {
                        zoneMap.put(GridCoordPair.forZone(zone), zone);
                    }
                } catch (RuntimeException e) {
                    LOTRMod.LOGGER.error("Error loading LOTR conquest zone", e);
                }
            }
            LOTRMod.LOGGER.info("LOTR: Loaded {} conquest zones", zoneMap.size());
            return new Storage();
        }

        private static List<CompoundTag> save(Storage unused) {
            List<CompoundTag> zones = new ArrayList<>();
            for (LOTRConquestZone zone : zoneMap.values()) {
                if (zone.isEmpty()) {
                    continue;
                }
                CompoundTag nbt = new CompoundTag();
                zone.writeToNBT(nbt);
                zones.add(nbt);
            }
            return zones;
        }
    }

    private LOTRConquestGrid() {
    }

    public static void init() {
        PayloadTypeRegistry.clientboundPlay().register(NotificationPayload.TYPE, NotificationPayload.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(GridPayload.TYPE, GridPayload.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(OpenMapPayload.TYPE, OpenMapPayload.STREAM_CODEC);
        PayloadTypeRegistry.serverboundPlay().register(GridRequestPayload.TYPE, GridRequestPayload.STREAM_CODEC);
        ServerPlayNetworking.registerGlobalReceiver(GridRequestPayload.TYPE, (payload, context) -> {
            LOTRFaction fac = payload.faction();
            if (fac != null && canPlayerViewConquest(context.player(), fac).result() == ConquestViewable.CAN_VIEW) {
                sendConquestGridTo(context.player(), fac);
            }
        });
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            zoneMap.clear();
            dirtyZones.clear();
            storage = server.overworld().getDataStorage().computeIfAbsent(Storage.TYPE);
        });
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
            storage = null;
            zoneMap.clear();
            dirtyZones.clear();
        });
        ServerTickEvents.END_SERVER_TICK.register(LOTRConquestGrid::serverTick);
    }

    /** LOTRTickHandlerServer: the changed zones saved (the empty ones let go), the zones checked, the waypoints sent. */
    private static void serverTick(MinecraftServer server) {
        if (anyChangedZones()) {
            saveChangedZones();
        }
        ServerLevel level = server.getLevel(LOTRWorldGen.MIDDLE_EARTH);
        if (level != null) {
            updateZones(level, server.getTickCount());
            if (server.getTickCount() % 40 == 0) {
                for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                    updateConquestWaypoints(player);
                }
            }
        }
    }

    public static boolean anyChangedZones() {
        return !dirtyZones.isEmpty();
    }

    private static void saveChangedZones() {
        if (storage != null) {
            storage.setDirty();
        }
        for (GridCoordPair key : dirtyZones) {
            LOTRConquestZone zone = zoneMap.get(key);
            if (zone != null && zone.isEmpty()) {
                zoneMap.remove(key);
            }
        }
        dirtyZones.clear();
    }

    static void markZoneDirty(LOTRConquestZone zone) {
        GridCoordPair key = GridCoordPair.forZone(zone);
        if (zoneMap.containsKey(key)) {
            dirtyZones.add(key);
        }
    }

    public static boolean conquestEnabled(Level level) {
        return LOTRConfig.enableConquest && !LOTRWorldGen.classic;
    }

    public static boolean getEnableConquestKills(Player player) {
        return player.getAttachedOrCreate(CONQUEST_KILLS);
    }

    public static void setEnableConquestKills(Player player, boolean flag) {
        player.setAttached(CONQUEST_KILLS, flag);
    }

    public static ConquestViewableQuery canPlayerViewConquest(Player player, LOTRFaction fac) {
        LOTRFaction pledged = LOTRPlayerAlignments.get(player).pledgeFaction();
        if (pledged != null) {
            if (fac == pledged) {
                return ConquestViewableQuery.canView();
            }
            float align = LOTRPlayerAlignments.getAlignment(player, pledged);
            LOTRFactionRank pledgeRank = pledged.getPledgeRank();
            if (fac.isAlly(pledged) || fac.isBadRelation(pledged)) {
                return ConquestViewableQuery.canView();
            }
            LOTRFactionRank higherRank = pledged.getRankNAbove(pledgeRank, 2);
            if (align >= higherRank.alignment) {
                return ConquestViewableQuery.canView();
            }
            return new ConquestViewableQuery(ConquestViewable.NEED_RANK, higherRank);
        }
        return new ConquestViewableQuery(ConquestViewable.UNPLEDGED, null);
    }

    /** sendConquestGridTo: the faction's grid; surveying one's own faction's earns factionConquest. */
    public static void sendConquestGridTo(ServerPlayer player, LOTRFaction fac) {
        long worldTime = player.level().getGameTime();
        List<GridZone> zones = new ArrayList<>();
        for (LOTRConquestZone zone : zoneMap.values()) {
            if (zone.getConquestStrength(fac, worldTime) <= 0.0f) {
                continue;
            }
            zones.add(new GridZone((short) zone.gridX, (short) zone.gridZ, zone.getLastChangeTime(), zone.getConquestStrengthRaw(fac)));
        }
        ServerPlayNetworking.send(player, new GridPayload(fac, zones));
        if (fac == LOTRPlayerAlignments.get(player).pledgeFaction()) {
            LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.FACTION_CONQUEST);
        }
    }

    /** checkNotifyConquest: the players nearby in the zone, of the faction (or its enemies, when cleansed), told as it passes a fifty. */
    private static void checkNotifyConquest(LOTRConquestZone zone, Player originPlayer, LOTRFaction faction, float newConq,
                                            float prevConq, boolean isCleansing) {
        if (Mth.floor(newConq / 50.0f) == Mth.floor(prevConq / 50.0f) && (newConq != 0.0f || prevConq == newConq)) {
            return;
        }
        for (Player other : originPlayer.level().players()) {
            if (!(other instanceof ServerPlayer player) || player.distanceToSqr(originPlayer) > 40000.0
                    || getZoneByEntityCoords(player) != zone) {
                continue;
            }
            boolean playerApplicable;
            if (isCleansing) {
                LOTRFaction pledgeFac = LOTRPlayerAlignments.get(player).pledgeFaction();
                playerApplicable = pledgeFac != null && pledgeFac.isBadRelation(faction);
            } else {
                playerApplicable = LOTRPlayerAlignments.isPledgedTo(player, faction);
            }
            if (playerApplicable) {
                ServerPlayNetworking.send(player, new NotificationPayload(faction, newConq, isCleansing));
            }
        }
    }

    /**
     * doRadialConquest: the zones within three and a half of the central one, each by its nearness --
     * cleansing the enemy's strength where it has any, raising the pledged faction's elsewhere. What
     * the central zone gained (or the enemy lost there) is returned.
     */
    public static float doRadialConquest(Level level, LOTRConquestZone centralZone, @Nullable Player killingPlayer,
                                         @Nullable LOTRFaction pledgeFaction, @Nullable LOTRFaction enemyFaction,
                                         float conqGain, float conqCleanse) {
        if (centralZone.isDummyZone) {
            return 0.0f;
        }
        float centralConqBonus = 0.0f;
        for (int i1 = -3; i1 <= 3; ++i1) {
            for (int k1 = -3; k1 <= 3; ++k1) {
                int distSq = i1 * i1 + k1 * k1;
                if (distSq > 12.25f) {
                    continue;
                }
                float frac = 1.0f - Mth.sqrt(distSq) / 3.5f;
                float conqGainHere = frac * conqGain;
                float conqCleanseHere = frac * conqCleanse;
                LOTRConquestZone zone = getZoneByGridCoords(centralZone.gridX + i1, centralZone.gridZ + k1);
                if (zone.isDummyZone) {
                    continue;
                }
                float enemyConq;
                if (enemyFaction != null && (enemyConq = zone.getConquestStrength(enemyFaction, level)) > 0.0f) {
                    zone.addConquestStrength(enemyFaction, -conqCleanseHere, level);
                    float newEnemyConq = zone.getConquestStrength(enemyFaction, level);
                    if (zone == centralZone) {
                        centralConqBonus = newEnemyConq - enemyConq;
                    }
                    if (killingPlayer != null) {
                        checkNotifyConquest(zone, killingPlayer, enemyFaction, newEnemyConq, enemyConq, true);
                    }
                    continue;
                }
                if (pledgeFaction == null) {
                    continue;
                }
                float prevZoneConq = zone.getConquestStrength(pledgeFaction, level);
                zone.addConquestStrength(pledgeFaction, conqGainHere, level);
                float newZoneConq = zone.getConquestStrength(pledgeFaction, level);
                if (zone == centralZone) {
                    centralConqBonus = newZoneConq - prevZoneConq;
                }
                if (killingPlayer != null) {
                    checkNotifyConquest(zone, killingPlayer, pledgeFaction, newZoneConq, prevZoneConq, false);
                }
            }
        }
        return centralConqBonus;
    }

    /**
     * getConquestEffectIn: whether the faction's conquest counts in the zone -- it does where any of
     * the zone's biomes on the map has the faction's NPCs, it only aids allies where those have
     * theirs, and otherwise does nothing. Each zone's factions are worked out once.
     */
    public static ConquestEffective getConquestEffectIn(Level level, LOTRConquestZone zone, LOTRFaction theFaction) {
        return getConquestEffectIn(level, zone.gridX, zone.gridZ, theFaction);
    }

    /** getConquestEffectIn by the zone's grid coordinates, making no zone (the client's map asks it too). */
    public static ConquestEffective getConquestEffectIn(Level level, int gridX, int gridZ, LOTRFaction theFaction) {
        if (!LOTRGenLayerWorld.loadedBiomeImage()) {
            new LOTRGenLayerWorld();
        }
        GridCoordPair gridCoords = new GridCoordPair(gridX, gridZ);
        List<LOTRFaction> cachedFacs;
        synchronized (cachedZoneFactions) {
            cachedFacs = cachedZoneFactions.get(gridCoords);
        }
        if (cachedFacs == null) {
            cachedFacs = new ArrayList<>();
            List<LOTRBiome> includedBiomes = new ArrayList<>();
            for (int i = gridToMapCoord(gridX); i < gridToMapCoord(gridX + 1); ++i) {
                for (int k = gridToMapCoord(gridZ); k < gridToMapCoord(gridZ + 1); ++k) {
                    LOTRBiome biome = LOTRGenLayerWorld.getBiomeOrOcean(i, k);
                    if (!includedBiomes.contains(biome)) {
                        includedBiomes.add(biome);
                    }
                }
            }
            facs:
            for (LOTRFaction fac : LOTRFaction.getPlayableAlignmentFactions()) {
                for (LOTRBiome biome : includedBiomes) {
                    if (biome.npcSpawnList.isFactionPresent(level, fac)) {
                        cachedFacs.add(fac);
                        continue facs;
                    }
                }
            }
            synchronized (cachedZoneFactions) {
                cachedZoneFactions.put(gridCoords, cachedFacs);
            }
        }
        if (cachedFacs.contains(theFaction)) {
            return ConquestEffective.EFFECTIVE;
        }
        for (LOTRFaction allyFac : theFaction.getConquestBoostRelations()) {
            if (cachedFacs.contains(allyFac)) {
                return ConquestEffective.ALLY_BOOST;
            }
        }
        return ConquestEffective.NO_EFFECT;
    }

    /** getConquestGainRate: the biome the player stands in's rate (LOTRBiomeSpawnList.conquestGainRate). */
    public static float getConquestGainRate(Player player) {
        LOTRBiome biome = LOTRBiomes.of(player.level().getBiome(player.blockPosition()));
        return biome != null ? biome.npcSpawnList.conquestGainRate : 1.0f;
    }

    public static int[] getMaxCoordsOnMap(LOTRConquestZone zone) {
        return new int[]{gridToMapCoord(zone.gridX + 1), gridToMapCoord(zone.gridZ + 1)};
    }

    public static int[] getMinCoordsOnMap(LOTRConquestZone zone) {
        return new int[]{gridToMapCoord(zone.gridX), gridToMapCoord(zone.gridZ)};
    }

    public static LOTRConquestZone getZoneByEntityCoords(Entity entity) {
        return getZoneByWorldCoords(Mth.floor(entity.getX()), Mth.floor(entity.getZ()));
    }

    /** getZoneByGridCoords: the zone, made fresh if it has nothing yet; outside the map, the dummy zone. */
    public static LOTRConquestZone getZoneByGridCoords(int i, int k) {
        if (!LOTRGenLayerWorld.loadedBiomeImage()) {
            new LOTRGenLayerWorld();
        }
        if (i < 0 || i >= Mth.ceil((float) LOTRGenLayerWorld.getImageWidth() / MAP_GRID_SCALE)
                || k < 0 || k >= Mth.ceil((float) LOTRGenLayerWorld.getImageHeight() / MAP_GRID_SCALE)) {
            return dummyZone;
        }
        return zoneMap.computeIfAbsent(new GridCoordPair(i, k), key -> new LOTRConquestZone(key.gridX, key.gridZ));
    }

    public static LOTRConquestZone getZoneByWorldCoords(int i, int k) {
        return getZoneByGridCoords(worldToGridX(i), worldToGridZ(k));
    }

    public static int gridToMapCoord(int i) {
        return i << 3;
    }

    /**
     * onConquestKill: in Middle-earth, with conquest on and the player's kills counting, the bonus
     * (by the world's conquest rate and the biome's gain rate) spread about the zone the player is in.
     */
    public static float onConquestKill(Player player, LOTRFaction pledgeFaction, LOTRFaction enemyFaction, float alignBonus) {
        Level level = player.level();
        if (!level.isClientSide() && conquestEnabled(level) && getEnableConquestKills(player)
                && level.dimension() == LOTRWorldGen.MIDDLE_EARTH) {
            LOTRConquestZone centralZone = getZoneByEntityCoords(player);
            float conqAmount = alignBonus * LOTRLevelData.getConquestRate();
            float conqGain = conqAmount * getConquestGainRate(player);
            float conqCleanse = conqAmount * getConquestGainRate(player);
            return doRadialConquest(level, centralZone, player, pledgeFaction, enemyFaction, conqGain, conqCleanse);
        }
        return 0.0f;
    }

    /** updateZones: each zone checked, once every five minutes, for strengths all worn away. */
    private static void updateZones(Level level, int tick) {
        if (!conquestEnabled(level)) {
            return;
        }
        int interval = 6000;
        for (LOTRConquestZone zone : new ArrayList<>(zoneMap.values())) {
            if (zone.isEmpty() || Math.floorMod(tick, interval) != Math.floorMod(zone.hashCode(), interval)) {
                continue;
            }
            zone.checkForEmptiness(level);
        }
    }

    /** The waypoints the player has opened by conquest, sent to them when they change. */
    private static void updateConquestWaypoints(ServerPlayer player) {
        List<Integer> unlocked = new ArrayList<>();
        for (LOTRWaypoint wp : LOTRWaypoint.values()) {
            if (wp.isUnlockedByConquest(player)) {
                unlocked.add(wp.ordinal());
            }
        }
        if (!unlocked.equals(player.getAttachedOrCreate(CONQUEST_WAYPOINTS))) {
            player.setAttached(CONQUEST_WAYPOINTS, List.copyOf(unlocked));
        }
    }

    public static int worldToGridX(int i) {
        int mapX = i >> 7;
        return mapX + 810 >> 3;
    }

    public static int worldToGridZ(int k) {
        int mapZ = k >> 7;
        return mapZ + 730 >> 3;
    }

    public enum ConquestEffective {
        EFFECTIVE, ALLY_BOOST, NO_EFFECT
    }

    public enum ConquestViewable {
        UNPLEDGED, CAN_VIEW, NEED_RANK
    }

    public record ConquestViewableQuery(ConquestViewable result, @Nullable LOTRFactionRank needRank) {
        public static ConquestViewableQuery canView() {
            return new ConquestViewableQuery(ConquestViewable.CAN_VIEW, null);
        }
    }

    public record GridCoordPair(int gridX, int gridZ) {
        public static GridCoordPair forZone(LOTRConquestZone zone) {
            return new GridCoordPair(zone.gridX, zone.gridZ);
        }

        @Override
        public int hashCode() {
            int i = 1664525 * this.gridX + 1013904223;
            int j = 1664525 * (this.gridZ ^ 0xDEADBEEF) + 1013904223;
            return i ^ j;
        }
    }
}
