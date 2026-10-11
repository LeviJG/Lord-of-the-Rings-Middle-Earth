package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRPlayerAchievements;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.banner.LOTRBannerProtection;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.config.LOTRConfig;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fellowship.LOTRFellowship;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fellowship.LOTRFellowships;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRWorldGen;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.world.level.storage.LevelResource;

import org.jspecify.annotations.Nullable;

/**
 * LOTRPlayerData's custom waypoints, for every player: each one's own waypoints ("CustomWaypoints",
 * numbered from "NextCWPID"), how often they have travelled to them ("CWPUses"), and of those their
 * fellows share with them, which they have unlocked ("CWPSharedUnlocked"), hidden
 * ("CWPSharedHidden") and travelled to ("CWPSharedUses"). Kept for the whole world, like the
 * fellowships, since sharing reads the waypoints of fellows who may be away.
 *
 * <p>A player may keep five, and one more for every five achievements. Shared waypoints are those
 * of the player's fellows that are shared with a fellowship both are in; the original cached copies
 * of them with each player, here they are worked out from the fellowships when sent.
 *
 * <p>Each change is sent to the player (and to fellows it touches) as the whole list
 * (LOTRPacketCreateCWPClient and the rest, in one). The server's create checks the config's lowest
 * height, the limit, and banner protection (its message shown on the map, LOTRPacketCWPProtectionMessage);
 * with "Custom Waypoint logging", each creation, deletion, renaming and journey is written to
 * {@code lotr_cwp_logs/<month-day>.csv} in the world (LOTRCustomWaypointLogger).
 */
public final class LOTRCustomWaypoints extends SavedData {

    /** A shared waypoint: whose, and which. */
    public record SharedKey(UUID sharingPlayer, int id) {
        static final Codec<SharedKey> CODEC = RecordCodecBuilder.create(i -> i.group(
                UUIDUtil.STRING_CODEC.fieldOf("SharingPlayer").forGetter(SharedKey::sharingPlayer),
                Codec.INT.fieldOf("CustomID").forGetter(SharedKey::id)).apply(i, SharedKey::new));
    }

    private record SharedUse(SharedKey key, int count) {
        static final Codec<SharedUse> CODEC = RecordCodecBuilder.create(i -> i.group(
                UUIDUtil.STRING_CODEC.fieldOf("SharingPlayer").forGetter(u -> u.key().sharingPlayer()),
                Codec.INT.fieldOf("CustomID").forGetter(u -> u.key().id()),
                Codec.INT.fieldOf("Count").forGetter(SharedUse::count)).apply(i, (p, id, c) -> new SharedUse(new SharedKey(p, id), c)));
    }

    private record Use(int id, int count) {
        static final Codec<Use> CODEC = RecordCodecBuilder.create(i -> i.group(
                Codec.INT.fieldOf("CustomID").forGetter(Use::id),
                Codec.INT.fieldOf("Count").forGetter(Use::count)).apply(i, Use::new));
    }

    /** One player's. */
    public static final class PlayerWaypoints {
        final List<LOTRCustomWaypoint> customWaypoints = new ArrayList<>();
        int nextCwpID = 20000;
        final Map<Integer, Integer> cwpUseCounts = new HashMap<>();
        final Set<SharedKey> cwpSharedUnlocked = new HashSet<>();
        final Set<SharedKey> cwpSharedHidden = new HashSet<>();
        final Map<SharedKey, Integer> cwpSharedUseCounts = new HashMap<>();

        static final Codec<PlayerWaypoints> CODEC = RecordCodecBuilder.create(i -> i.group(
                LOTRCustomWaypoint.CODEC.listOf().optionalFieldOf("CustomWaypoints", List.of()).forGetter(p -> List.copyOf(p.customWaypoints)),
                Codec.INT.optionalFieldOf("NextCWPID", 20000).forGetter(p -> p.nextCwpID),
                Use.CODEC.listOf().optionalFieldOf("CWPUses", List.of()).forGetter(p ->
                        p.cwpUseCounts.entrySet().stream().map(e -> new Use(e.getKey(), e.getValue())).toList()),
                SharedKey.CODEC.listOf().optionalFieldOf("CWPSharedUnlocked", List.of()).forGetter(p -> List.copyOf(p.cwpSharedUnlocked)),
                SharedKey.CODEC.listOf().optionalFieldOf("CWPSharedHidden", List.of()).forGetter(p -> List.copyOf(p.cwpSharedHidden)),
                SharedUse.CODEC.listOf().optionalFieldOf("CWPSharedUses", List.of()).forGetter(p ->
                        p.cwpSharedUseCounts.entrySet().stream().map(e -> new SharedUse(e.getKey(), e.getValue())).toList())
        ).apply(i, (cwps, next, uses, unlocked, hidden, sharedUses) -> {
            PlayerWaypoints p = new PlayerWaypoints();
            p.customWaypoints.addAll(cwps);
            p.nextCwpID = next;
            uses.forEach(u -> p.cwpUseCounts.put(u.id(), u.count()));
            p.cwpSharedUnlocked.addAll(unlocked);
            p.cwpSharedHidden.addAll(hidden);
            sharedUses.forEach(u -> p.cwpSharedUseCounts.put(u.key(), u.count()));
            return p;
        }));
    }

    private static final Codec<LOTRCustomWaypoints> CODEC = Codec.unboundedMap(Codec.STRING, PlayerWaypoints.CODEC)
            .xmap(map -> {
                LOTRCustomWaypoints data = new LOTRCustomWaypoints();
                map.forEach((uuid, p) -> data.players.put(UUID.fromString(uuid), p));
                return data;
            }, data -> {
                Map<String, PlayerWaypoints> map = new LinkedHashMap<>();
                data.players.forEach((uuid, p) -> {
                    p.customWaypoints.forEach(cwp -> cwp.validateFellowshipIDs(uuid));
                    map.put(uuid.toString(), p);
                });
                return map;
            });

    private static final SavedDataType<LOTRCustomWaypoints> TYPE = new SavedDataType<>(
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "custom_waypoints"), LOTRCustomWaypoints::new, CODEC, null);

    private static @Nullable LOTRCustomWaypoints instance;
    private static @Nullable MinecraftServer server;

    private final Map<UUID, PlayerWaypoints> players = new HashMap<>();

    // ---------------------------------------------------------------- payloads

    /** The player's own waypoints and those shared with them, and the server's lowest height for one. */
    public record SyncPayload(List<LOTRCustomWaypoint> own, List<LOTRCustomWaypoint> shared, int minY) implements CustomPacketPayload {
        public static final Type<SyncPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "cwp_sync"));
        public static final StreamCodec<RegistryFriendlyByteBuf, SyncPayload> STREAM_CODEC = StreamCodec.composite(
                LOTRCustomWaypoint.STREAM_CODEC.apply(ByteBufCodecs.list()), SyncPayload::own,
                LOTRCustomWaypoint.STREAM_CODEC.apply(ByteBufCodecs.list()), SyncPayload::shared,
                ByteBufCodecs.VAR_INT, SyncPayload::minY, SyncPayload::new);

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    /** LOTRPacketCWPProtectionMessage: why the land would not take a waypoint. */
    public record ProtectionPayload(Component message) implements CustomPacketPayload {
        public static final Type<ProtectionPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "cwp_protection"));
        public static final StreamCodec<RegistryFriendlyByteBuf, ProtectionPayload> STREAM_CODEC =
                StreamCodec.composite(ComponentSerialization.STREAM_CODEC, ProtectionPayload::message, ProtectionPayload::new);

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    /** LOTRPacketCreateCWP. */
    public record CreatePayload(String name) implements CustomPacketPayload {
        public static final Type<CreatePayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "cwp_create"));
        public static final StreamCodec<RegistryFriendlyByteBuf, CreatePayload> STREAM_CODEC =
                StreamCodec.composite(ByteBufCodecs.STRING_UTF8, CreatePayload::name, CreatePayload::new);

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    /** LOTRPacketDeleteCWP. */
    public record DeletePayload(int id) implements CustomPacketPayload {
        public static final Type<DeletePayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "cwp_delete"));
        public static final StreamCodec<RegistryFriendlyByteBuf, DeletePayload> STREAM_CODEC =
                StreamCodec.composite(ByteBufCodecs.VAR_INT, DeletePayload::id, DeletePayload::new);

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    /** LOTRPacketRenameCWP. */
    public record RenamePayload(int id, String name) implements CustomPacketPayload {
        public static final Type<RenamePayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "cwp_rename"));
        public static final StreamCodec<RegistryFriendlyByteBuf, RenamePayload> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, RenamePayload::id, ByteBufCodecs.STRING_UTF8, RenamePayload::name, RenamePayload::new);

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    /** LOTRPacketShareCWP: share with, or stop sharing with, the fellowship of that name. */
    public record SharePayload(int id, String fsName, boolean adding) implements CustomPacketPayload {
        public static final Type<SharePayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "cwp_share"));
        public static final StreamCodec<RegistryFriendlyByteBuf, SharePayload> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, SharePayload::id, ByteBufCodecs.STRING_UTF8, SharePayload::fsName,
                ByteBufCodecs.BOOL, SharePayload::adding, SharePayload::new);

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    /** LOTRPacketCWPSharedHide. */
    public record SharedHidePayload(UUID sharingPlayer, int id, boolean hide) implements CustomPacketPayload {
        public static final Type<SharedHidePayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "cwp_shared_hide"));
        public static final StreamCodec<RegistryFriendlyByteBuf, SharedHidePayload> STREAM_CODEC = StreamCodec.composite(
                UUIDUtil.STREAM_CODEC, SharedHidePayload::sharingPlayer, ByteBufCodecs.VAR_INT, SharedHidePayload::id,
                ByteBufCodecs.BOOL, SharedHidePayload::hide, SharedHidePayload::new);

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public static void init() {
        PayloadTypeRegistry.clientboundPlay().register(SyncPayload.TYPE, SyncPayload.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(ProtectionPayload.TYPE, ProtectionPayload.STREAM_CODEC);
        PayloadTypeRegistry.serverboundPlay().register(CreatePayload.TYPE, CreatePayload.STREAM_CODEC);
        PayloadTypeRegistry.serverboundPlay().register(DeletePayload.TYPE, DeletePayload.STREAM_CODEC);
        PayloadTypeRegistry.serverboundPlay().register(RenamePayload.TYPE, RenamePayload.STREAM_CODEC);
        PayloadTypeRegistry.serverboundPlay().register(SharePayload.TYPE, SharePayload.STREAM_CODEC);
        PayloadTypeRegistry.serverboundPlay().register(SharedHidePayload.TYPE, SharedHidePayload.STREAM_CODEC);
        ServerLifecycleEvents.SERVER_STARTED.register(s -> {
            server = s;
            instance = s.getDataStorage().computeIfAbsent(TYPE);
        });
        ServerLifecycleEvents.SERVER_STOPPED.register(s -> {
            server = null;
            instance = null;
        });
        ServerPlayConnectionEvents.JOIN.register((handler, sender, s) -> sync(handler.player));
        ServerPlayNetworking.registerGlobalReceiver(CreatePayload.TYPE, (p, ctx) -> create(ctx.player(), p.name()));
        ServerPlayNetworking.registerGlobalReceiver(DeletePayload.TYPE, (p, ctx) -> {
            LOTRCustomWaypoint cwp = getCustomWaypointByID(ctx.player().getUUID(), p.id());
            if (cwp != null) {
                removeCustomWaypoint(ctx.player(), cwp);
            }
        });
        ServerPlayNetworking.registerGlobalReceiver(RenamePayload.TYPE, (p, ctx) -> {
            LOTRCustomWaypoint cwp = getCustomWaypointByID(ctx.player().getUUID(), p.id());
            String wpName = LOTRCustomWaypoint.validateCustomName(p.name());
            if (cwp != null && wpName != null) {
                renameCustomWaypoint(ctx.player(), cwp, wpName);
            }
        });
        ServerPlayNetworking.registerGlobalReceiver(SharePayload.TYPE, (p, ctx) -> {
            ServerPlayer player = ctx.player();
            LOTRCustomWaypoint cwp = getCustomWaypointByID(player.getUUID(), p.id());
            LOTRFellowship fs = LOTRFellowships.getFellowshipByName(player.getUUID(), p.fsName());
            if (cwp != null && fs != null) {
                if (p.adding()) {
                    cwp.addSharedFellowship(fs.getFellowshipID());
                } else {
                    cwp.removeSharedFellowship(fs.getFellowshipID());
                }
                markDirty();
                syncFellowship(fs);
                sync(player);
            }
        });
        ServerPlayNetworking.registerGlobalReceiver(SharedHidePayload.TYPE, (p, ctx) -> {
            ServerPlayer player = ctx.player();
            if (getSharedCustomWaypointByID(player.getUUID(), p.sharingPlayer(), p.id()) != null) {
                SharedKey key = new SharedKey(p.sharingPlayer(), p.id());
                if (p.hide()) {
                    of(player.getUUID()).cwpSharedHidden.add(key);
                } else {
                    of(player.getUUID()).cwpSharedHidden.remove(key);
                }
                markDirty();
                sync(player);
            }
        });
        // unlockSharedCustomWaypoints: once a second, in Middle-earth, those within reach.
        ServerTickEvents.END_SERVER_TICK.register(s -> {
            if (s.getTickCount() % 20 != 0) {
                return;
            }
            for (ServerPlayer player : s.getPlayerList().getPlayers()) {
                if (player.level().dimension() != LOTRWorldGen.MIDDLE_EARTH) {
                    continue;
                }
                boolean unlocked = false;
                for (LOTRCustomWaypoint waypoint : getSharedCustomWaypoints(player.getUUID())) {
                    if (!waypoint.isSharedUnlocked() && waypoint.canUnlockShared(player)) {
                        of(player.getUUID()).cwpSharedUnlocked.add(new SharedKey(waypoint.getSharingPlayerID(), waypoint.getID()));
                        unlocked = true;
                    }
                }
                if (unlocked) {
                    markDirty();
                    sync(player);
                }
            }
        });
    }

    private static LOTRCustomWaypoints data() {
        if (instance == null) {
            throw new IllegalStateException("LOTR custom waypoint data used with no server running");
        }
        return instance;
    }

    private static void markDirty() {
        data().setDirty();
    }

    private static PlayerWaypoints of(UUID player) {
        return data().players.computeIfAbsent(player, p -> new PlayerWaypoints());
    }

    // ---------------------------------------------------------------- lookups

    public static List<LOTRCustomWaypoint> getCustomWaypoints(UUID player) {
        return of(player).customWaypoints;
    }

    public static @Nullable LOTRCustomWaypoint getCustomWaypointByID(UUID player, int id) {
        for (LOTRCustomWaypoint waypoint : getCustomWaypoints(player)) {
            if (waypoint.getID() == id) {
                return waypoint;
            }
        }
        return null;
    }

    /** getMaxCustomWaypoints: five, and one more for every five achievements. */
    public static int getMaxCustomWaypoints(ServerPlayer player) {
        return 5 + LOTRPlayerAchievements.countEarned(player) / 5;
    }

    /** The waypoints the player's fellows share with a fellowship both are in, as the player stands with them. */
    public static List<LOTRCustomWaypoint> getSharedCustomWaypoints(UUID player) {
        Map<SharedKey, LOTRCustomWaypoint> shared = new LinkedHashMap<>();
        PlayerWaypoints mine = of(player);
        for (LOTRFellowship fs : LOTRFellowships.getFellowships(player)) {
            if (fs.isDisbanded()) {
                continue;
            }
            for (UUID fellow : fs.getAllPlayerUUIDs()) {
                if (fellow.equals(player)) {
                    continue;
                }
                PlayerWaypoints theirs = data().players.get(fellow);
                if (theirs == null) {
                    continue;
                }
                for (LOTRCustomWaypoint waypoint : theirs.customWaypoints) {
                    SharedKey key = new SharedKey(fellow, waypoint.getID());
                    if (!waypoint.hasSharedFellowship(fs.getFellowshipID()) || shared.containsKey(key)) {
                        continue;
                    }
                    LOTRCustomWaypoint copy = waypoint.createCopyOfShared(fellow, LOTRFellowships.usernameOf(fellow));
                    copy.validateFellowshipIDs(fellow);
                    copy.sharedUnlocked = mine.cwpSharedUnlocked.contains(key);
                    copy.sharedHidden = mine.cwpSharedHidden.contains(key);
                    shared.put(key, copy);
                }
            }
        }
        return new ArrayList<>(shared.values());
    }

    public static @Nullable LOTRCustomWaypoint getSharedCustomWaypointByID(UUID player, UUID sharer, int id) {
        for (LOTRCustomWaypoint waypoint : getSharedCustomWaypoints(player)) {
            if (sharer.equals(waypoint.getSharingPlayerID()) && waypoint.getID() == id) {
                return waypoint;
            }
        }
        return null;
    }

    // ---------------------------------------------------------------- use counts

    public static int getUseCount(UUID player, LOTRCustomWaypoint cwp) {
        PlayerWaypoints p = of(player);
        if (cwp.isShared()) {
            return p.cwpSharedUseCounts.getOrDefault(new SharedKey(cwp.getSharingPlayerID(), cwp.getID()), 0);
        }
        return p.cwpUseCounts.getOrDefault(cwp.getID(), 0);
    }

    public static void incrementUseCount(ServerPlayer player, LOTRCustomWaypoint cwp) {
        PlayerWaypoints p = of(player.getUUID());
        if (cwp.isShared()) {
            p.cwpSharedUseCounts.merge(new SharedKey(cwp.getSharingPlayerID(), cwp.getID()), 1, Integer::sum);
        } else {
            p.cwpUseCounts.merge(cwp.getID(), 1, Integer::sum);
        }
        markDirty();
        sync(player);
    }

    // ---------------------------------------------------------------- changes

    /** LOTRPacketCreateCWP's handler: not too low, within the limit, on land that takes it, with a name. */
    private static void create(ServerPlayer player, String name) {
        int minY = LOTRConfig.customWaypointMinY;
        if (minY >= 0 && player.getBoundingBox().minY < minY) {
            LOTRMod.LOGGER.warn("Player {} tried to create a custom waypoint below the config file minimum of {}!",
                    player.getName().getString(), minY);
            return;
        }
        // The original checked the count with <=, letting one past the limit through; the map never asks for it.
        if (getCustomWaypoints(player.getUUID()).size() >= getMaxCustomWaypoints(player)) {
            return;
        }
        Component[] protectionMessage = new Component[1];
        if (LOTRBannerProtection.isProtected(player.level(), player, LOTRBannerProtection.forPlayerReturnMessage(player,
                LOTRBannerProtection.Permission.FULL, protectionMessage), true)) {
            if (protectionMessage[0] != null) {
                ServerPlayNetworking.send(player, new ProtectionPayload(protectionMessage[0]));
            }
            return;
        }
        String wpName = LOTRCustomWaypoint.validateCustomName(name);
        if (wpName != null) {
            PlayerWaypoints p = of(player.getUUID());
            LOTRCustomWaypoint cwp = LOTRCustomWaypoint.createAt(player, wpName, p.nextCwpID);
            ++p.nextCwpID;
            p.customWaypoints.add(cwp);
            markDirty();
            sync(player);
            log("CREATE", player, cwp);
        }
    }

    private static void removeCustomWaypoint(ServerPlayer player, LOTRCustomWaypoint cwp) {
        if (getCustomWaypoints(player.getUUID()).remove(cwp)) {
            markDirty();
            sync(player);
            syncSharedWith(player.getUUID(), cwp);
            log("DELETE", player, cwp);
        }
    }

    private static void renameCustomWaypoint(ServerPlayer player, LOTRCustomWaypoint cwp, String newName) {
        cwp.rename(newName);
        markDirty();
        sync(player);
        syncSharedWith(player.getUUID(), cwp);
        log("RENAME", player, cwp);
    }

    /** A journey to one of them, logged. */
    public static void logTravel(ServerPlayer player, LOTRCustomWaypoint cwp) {
        log("TRAVEL", player, cwp);
    }

    /**
     * unshareFellowshipFromOwnCustomWaypoints and checkCustomWaypointsSharedFromFellowships: a player
     * gone from a fellowship stops sharing their waypoints with it, and is sent what they still see.
     */
    public static void onLeftFellowship(UUID player, LOTRFellowship fs) {
        if (instance == null) {
            return;
        }
        boolean changed = false;
        for (LOTRCustomWaypoint cwp : getCustomWaypoints(player)) {
            if (cwp.hasSharedFellowship(fs.getFellowshipID())) {
                cwp.removeSharedFellowship(fs.getFellowshipID());
                changed = true;
            }
        }
        if (changed) {
            markDirty();
        }
        ServerPlayer online = onlinePlayer(player);
        if (online != null) {
            sync(online);
        }
        syncFellowship(fs);
    }

    /** Every member of the fellowship who is online, sent what they see now. */
    public static void syncFellowship(LOTRFellowship fs) {
        if (instance == null) {
            return;
        }
        for (UUID member : fs.getAllPlayerUUIDs()) {
            ServerPlayer online = onlinePlayer(member);
            if (online != null) {
                sync(online);
            }
        }
    }

    /** The fellows of the owner who might see the waypoint, sent what they see now. */
    private static void syncSharedWith(UUID owner, LOTRCustomWaypoint cwp) {
        Set<UUID> sent = new HashSet<>();
        for (UUID fsID : cwp.getSharedFellowshipIDs()) {
            LOTRFellowship fs = LOTRFellowships.getActiveFellowship(fsID);
            if (fs == null) {
                continue;
            }
            for (UUID member : fs.getAllPlayerUUIDs()) {
                ServerPlayer online = onlinePlayer(member);
                if (!member.equals(owner) && online != null && sent.add(member)) {
                    sync(online);
                }
            }
        }
    }

    private static @Nullable ServerPlayer onlinePlayer(UUID player) {
        return server == null ? null : server.getPlayerList().getPlayer(player);
    }

    public static void sync(ServerPlayer player) {
        if (instance == null) {
            return;
        }
        UUID uuid = player.getUUID();
        List<LOTRCustomWaypoint> own = getCustomWaypoints(uuid);
        own.forEach(cwp -> {
            cwp.validateFellowshipIDs(uuid);
            cwp.useCount = getUseCount(uuid, cwp);
        });
        List<LOTRCustomWaypoint> shared = getSharedCustomWaypoints(uuid);
        shared.forEach(cwp -> cwp.useCount = getUseCount(uuid, cwp));
        ServerPlayNetworking.send(player, new SyncPayload(List.copyOf(own), shared, LOTRConfig.customWaypointMinY));
    }

    // ---------------------------------------------------------------- the log

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MM-dd", Locale.ROOT);
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.ROOT);

    /** LOTRCustomWaypointLogger.log: one line of the day's file, with the fellowships it is shared in that hold the player. */
    private static void log(String function, ServerPlayer player, LOTRCustomWaypoint cwp) {
        if (!LOTRConfig.cwpLog || server == null) {
            return;
        }
        try {
            LocalDateTime now = LocalDateTime.now();
            StringBuilder logLine = new StringBuilder(String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
                    now.format(DATE_FORMAT), now.format(TIME_FORMAT), function, player.getName().getString(), player.getUUID(),
                    cwp.getCodeName(), cwp.getXCoord(), cwp.getYCoordSaved(), cwp.getZCoord(), cwp.isShared(),
                    cwp.isShared() ? cwp.getSharingPlayerName() : "N/A", cwp.isShared() ? cwp.getSharingPlayerID() : "N/A"));
            if (cwp.isShared()) {
                for (UUID id : cwp.getSharedFellowshipIDs()) {
                    LOTRFellowship fellowship = LOTRFellowships.getActiveFellowship(id);
                    if (fellowship != null && fellowship.containsPlayer(player.getUUID())) {
                        logLine.append(",").append(fellowship.getName());
                    }
                }
            }
            Path dir = server.getWorldPath(LevelResource.ROOT).resolve("lotr_cwp_logs");
            Files.createDirectories(dir);
            Path logFile = dir.resolve(now.format(DATE_FORMAT) + ".csv");
            if (!Files.exists(logFile)) {
                Files.writeString(logFile, "date,time,function,username,UUID,wp_name,x,y,z,shared,sharer_name,sharer_UUID,common_fellowships"
                        + System.lineSeparator(), StandardCharsets.UTF_8);
            }
            Files.writeString(logFile, logLine.append(System.lineSeparator()), StandardCharsets.UTF_8, StandardOpenOption.APPEND);
        } catch (IOException e) {
            LOTRMod.LOGGER.warn("Error logging custom waypoint activities", e);
        }
    }
}
