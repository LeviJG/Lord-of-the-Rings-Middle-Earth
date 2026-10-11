package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRLevelData;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.command.LOTRMiscCommands;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.config.LOTRConfig;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.character.LOTRGollumEntity;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

import org.jspecify.annotations.Nullable;

/**
 * LOTRPlayerData's fast travel. Each player's clock counts the ticks since they last travelled
 * ("FTSince"); a waypoint may be travelled to once the clock has passed its wait, which shrinks the
 * more it is used and grows with the distance (getWaypointFTTime). Asked from the map (the Fast
 * Travel key on a chosen waypoint, LOTRPacketFastTravel), the server counts ten seconds down -- to be
 * spent standing still, out of bed and not under attack -- then, through the client
 * (LOTRPacketFTBounceClient/Server), moves the player there, with their mount, the hired units
 * following them, Gollum, their pets and whatever they lead; the mount is ridden again half a second
 * later. The client is shown the journey (LOTRPacketFTScreen) and the clock (LOTRPacketFTTimer).
 * Away from a multiplayer server, a player's clock goes on a tenth as fast. Custom waypoints, one's
 * own and those shared, are travelled to as the fixed ones are (LOTRCustomWaypoints).
 */
public final class LOTRFastTravel {

    /** ticksUntilFT_max: the countdown, ten seconds. */
    public static final int TICKS_UNTIL_FT_MAX = 200;

    /** What is kept: "FTSince", "MountUUID", "MountUUIDTime", "LastOnlineTime". */
    public record Data(int ftSinceTick, Optional<UUID> uuidToMount, int uuidToMountTime, long lastOnlineTime) {
        static final Data EMPTY = new Data(0, Optional.empty(), 0, -1L);
        static final Codec<Data> CODEC = RecordCodecBuilder.create(i -> i.group(
                Codec.INT.optionalFieldOf("FTSince", 0).forGetter(Data::ftSinceTick),
                Codec.STRING.xmap(UUID::fromString, UUID::toString).optionalFieldOf("MountUUID").forGetter(Data::uuidToMount),
                Codec.INT.optionalFieldOf("MountUUIDTime", 0).forGetter(Data::uuidToMountTime),
                Codec.LONG.optionalFieldOf("LastOnlineTime", -1L).forGetter(Data::lastOnlineTime)
        ).apply(i, Data::new));
    }

    public static final AttachmentType<Data> DATA = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "fast_travel"),
            builder -> builder.initializer(() -> Data.EMPTY).persistent(Data.CODEC).copyOnDeath());

    /** targetFTWaypoint and ticksUntilFT, which the original did not keep. */
    private record Pending(LOTRAbstractWaypoint waypoint, int ticksUntilFT) {
    }

    private static final Map<UUID, Pending> PENDING = new HashMap<>();

    private LOTRFastTravel() {
    }

    // ---------------------------------------------------------------- payloads

    /** LOTRPacketFastTravel: travel to this waypoint, a custom one (with the player sharing it) or not. */
    public record FastTravelPayload(boolean isCustom, int wpID, Optional<UUID> sharingPlayer) implements CustomPacketPayload {
        public static final Type<FastTravelPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "fast_travel"));
        public static final StreamCodec<RegistryFriendlyByteBuf, FastTravelPayload> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.BOOL, FastTravelPayload::isCustom, ByteBufCodecs.VAR_INT, FastTravelPayload::wpID,
                ByteBufCodecs.optional(UUIDUtil.STREAM_CODEC), FastTravelPayload::sharingPlayer, FastTravelPayload::new);

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    /** LOTRPacketFTTimer: the player's clock. */
    public record TimerPayload(int timer) implements CustomPacketPayload {
        public static final Type<TimerPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "ft_timer"));
        public static final StreamCodec<RegistryFriendlyByteBuf, TimerPayload> STREAM_CODEC =
                StreamCodec.composite(ByteBufCodecs.VAR_INT, TimerPayload::timer, TimerPayload::new);

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    /** LOTRPacketFTCooldown: the waypoints' longest and shortest wait, in seconds. */
    public record CooldownPayload(int max, int min) implements CustomPacketPayload {
        public static final Type<CooldownPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "ft_cooldown"));
        public static final StreamCodec<RegistryFriendlyByteBuf, CooldownPayload> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, CooldownPayload::max, ByteBufCodecs.VAR_INT, CooldownPayload::min, CooldownPayload::new);

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    /** LOTRPacketFTBounceClient: the countdown is done; the client answers to be moved. */
    public record BounceClientPayload() implements CustomPacketPayload {
        public static final Type<BounceClientPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "ft_bounce_client"));
        public static final StreamCodec<RegistryFriendlyByteBuf, BounceClientPayload> STREAM_CODEC = StreamCodec.unit(new BounceClientPayload());

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    /** LOTRPacketFTBounceServer: the client's answer. */
    public record BounceServerPayload() implements CustomPacketPayload {
        public static final Type<BounceServerPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "ft_bounce_server"));
        public static final StreamCodec<RegistryFriendlyByteBuf, BounceServerPayload> STREAM_CODEC = StreamCodec.unit(new BounceServerPayload());

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    /** LOTRPacketFTScreen: show the journey, to this waypoint from here. */
    public record ScreenPayload(boolean isCustom, int wpID, Optional<UUID> sharingPlayer, int startX, int startZ)
            implements CustomPacketPayload {
        public static final Type<ScreenPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "ft_screen"));
        public static final StreamCodec<RegistryFriendlyByteBuf, ScreenPayload> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.BOOL, ScreenPayload::isCustom, ByteBufCodecs.VAR_INT, ScreenPayload::wpID,
                ByteBufCodecs.optional(UUIDUtil.STREAM_CODEC), ScreenPayload::sharingPlayer,
                ByteBufCodecs.VAR_INT, ScreenPayload::startX, ByteBufCodecs.VAR_INT, ScreenPayload::startZ, ScreenPayload::new);

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public static void init() {
        LOTRPlayerWaypoints.init();
        LOTRCustomWaypoints.init();
        LOTRMapPlayerData.init();
        LOTRLastKnownLocation.init();
        PayloadTypeRegistry.serverboundPlay().register(FastTravelPayload.TYPE, FastTravelPayload.STREAM_CODEC);
        PayloadTypeRegistry.serverboundPlay().register(BounceServerPayload.TYPE, BounceServerPayload.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(TimerPayload.TYPE, TimerPayload.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(CooldownPayload.TYPE, CooldownPayload.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(BounceClientPayload.TYPE, BounceClientPayload.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(ScreenPayload.TYPE, ScreenPayload.STREAM_CODEC);
        ServerPlayNetworking.registerGlobalReceiver(FastTravelPayload.TYPE, (payload, context) -> requestFastTravel(context.player(), payload));
        ServerPlayNetworking.registerGlobalReceiver(BounceServerPayload.TYPE, (payload, context) -> receiveFTBouncePacket(context.player()));
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayer player = handler.player;
            ServerPlayNetworking.send(player, new CooldownPayload(LOTRLevelData.getWaypointCooldownMax(),
                    LOTRLevelData.getWaypointCooldownMin()));
            setTimeSinceFT(player, getTimeSinceFT(player), true);
            updateFastTravelClockFromLastOnlineTime(player, server);
        });
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> PENDING.remove(handler.player.getUUID()));
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            long onlineTime = getCurrentOnlineTime(server);
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                handleFastTravel(player);
                handleMounting(player);
                Data d = data(player);
                player.setAttached(DATA, new Data(d.ftSinceTick(), d.uuidToMount(), d.uuidToMountTime(), onlineTime));
            }
        });
    }

    private static Data data(Player player) {
        return player.getAttachedOrCreate(DATA);
    }

    // ---------------------------------------------------------------- the clock

    public static int getTimeSinceFT(Player player) {
        return data(player).ftSinceTick();
    }

    /** setTimeSinceFT: the clock, sent on when it turns over, every quarter second, or when asked. */
    public static void setTimeSinceFT(ServerPlayer player, int i, boolean forceUpdate) {
        Data d = data(player);
        int preTick = d.ftSinceTick();
        int ftSinceTick = Math.max(0, i);
        player.setAttached(DATA, new Data(ftSinceTick, d.uuidToMount(), d.uuidToMountTime(), d.lastOnlineTime()));
        boolean bigChange = (ftSinceTick == 0 || preTick == 0) && ftSinceTick != preTick || preTick < 0;
        if (bigChange || ftSinceTick % 5 == 0 || forceUpdate) {
            ServerPlayNetworking.send(player, new TimerPayload(ftSinceTick));
        }
    }

    /** setTimeSinceFTWithUpdate. */
    public static void setTimeSinceFTWithUpdate(ServerPlayer player, int i) {
        setTimeSinceFT(player, i, true);
    }

    /** getCurrentOnlineTime: the overworld's age. */
    private static long getCurrentOnlineTime(MinecraftServer server) {
        return server.overworld().getGameTime();
    }

    /** updateFastTravelClockFromLastOnlineTime: on a multiplayer server, a tenth of the time away. */
    private static void updateFastTravelClockFromLastOnlineTime(ServerPlayer player, MinecraftServer server) {
        long lastOnlineTime = data(player).lastOnlineTime();
        if (lastOnlineTime <= 0L || server.isSingleplayer()) {
            return;
        }
        int diff = (int) (getCurrentOnlineTime(server) - lastOnlineTime);
        int ftClockIncrease = (int) (diff * 0.1);
        if (ftClockIncrease > 0) {
            setTimeSinceFTWithUpdate(player, getTimeSinceFT(player) + ftClockIncrease);
            player.sendSystemMessage(Component.translatable("chat.lotr.ft.offlineTick",
                    LOTRMiscCommands.hmsTime(diff), LOTRMiscCommands.hmsTime(ftClockIncrease)));
        }
    }

    // ---------------------------------------------------------------- the wait

    /** getWPUseCount: a fixed waypoint's, kept with the player, or a custom one's, kept with the world's. */
    public static int getWPUseCount(Player player, LOTRAbstractWaypoint wp) {
        if (wp instanceof LOTRWaypoint fixed) {
            return LOTRPlayerWaypoints.getWPUseCount(player, fixed);
        }
        if (wp instanceof LOTRCustomWaypoint cwp) {
            return player.level().isClientSide() ? cwp.useCount : LOTRCustomWaypoints.getUseCount(player.getUUID(), cwp);
        }
        return 0;
    }

    private static void incrementWPUseCount(ServerPlayer player, LOTRAbstractWaypoint wp) {
        if (wp instanceof LOTRWaypoint fixed) {
            LOTRPlayerWaypoints.setWPUseCount(player, fixed, LOTRPlayerWaypoints.getWPUseCount(player, fixed) + 1);
        } else if (wp instanceof LOTRCustomWaypoint cwp) {
            LOTRCustomWaypoints.incrementUseCount(player, cwp);
        }
    }

    /**
     * getWaypointFTTime: the shortest wait, plus what is between it and the longest shrunk by a tenth
     * for each journey there; lengthened by the distance past some eighty thousand blocks. In ticks.
     */
    public static int getWaypointFTTime(LOTRAbstractWaypoint wp, Player player, int cooldownMax, int cooldownMin) {
        int useCount = getWPUseCount(player, wp);
        double dist = Math.sqrt(player.distanceToSqr(wp.getXCoord() + 0.5, wp.getYCoordSaved(), wp.getZCoord() + 0.5));
        double time = cooldownMin;
        double added = (cooldownMax - cooldownMin) * Math.pow(0.9, useCount);
        time += added;
        int seconds = (int) Math.round(time * Math.max(1.0, dist * 1.2E-5));
        seconds = Math.max(seconds, 0);
        return seconds * 20;
    }

    public static int getWaypointFTTime(LOTRAbstractWaypoint wp, ServerPlayer player) {
        return getWaypointFTTime(wp, player, LOTRLevelData.getWaypointCooldownMax(), LOTRLevelData.getWaypointCooldownMin());
    }

    // ---------------------------------------------------------------- asking and waiting

    /** LOTRPacketFastTravel's handler. */
    private static void requestFastTravel(ServerPlayer player, FastTravelPayload payload) {
        if (!LOTRConfig.enableFastTravel) {
            player.sendSystemMessage(Component.translatable("chat.lotr.ftDisabled"));
            return;
        }
        LOTRAbstractWaypoint waypoint = null;
        if (payload.isCustom()) {
            waypoint = payload.sharingPlayer().isPresent()
                    ? LOTRCustomWaypoints.getSharedCustomWaypointByID(player.getUUID(), payload.sharingPlayer().get(), payload.wpID())
                    : LOTRCustomWaypoints.getCustomWaypointByID(player.getUUID(), payload.wpID());
        } else {
            int id = payload.wpID();
            if (id >= 0 && id < LOTRWaypoint.values().length) {
                waypoint = LOTRWaypoint.values()[id];
            }
        }
        if (waypoint == null || !waypoint.hasPlayerUnlocked(player)) {
            return;
        }
        if (getTimeSinceFT(player) < getWaypointFTTime(waypoint, player)) {
            player.closeContainer();
            player.sendSystemMessage(Component.translatable("lotr.fastTravel.moreTime", waypoint.getDisplayName()));
        } else if (!canFastTravel(player)) {
            player.closeContainer();
            player.sendSystemMessage(Component.translatable("lotr.fastTravel.underAttack"));
        } else if (player.isSleeping()) {
            player.closeContainer();
            player.sendSystemMessage(Component.translatable("lotr.fastTravel.inBed"));
        } else {
            setTargetFTWaypoint(player, waypoint);
        }
    }

    /** canFastTravel: in creative, or with nothing within sixteen blocks set on them. */
    public static boolean canFastTravel(ServerPlayer player) {
        if (!player.getAbilities().instabuild) {
            for (Mob mob : player.level().getEntitiesOfClass(Mob.class, player.getBoundingBox().inflate(16.0))) {
                if (mob.getTarget() == player) {
                    return false;
                }
            }
        }
        return true;
    }

    public static @Nullable LOTRAbstractWaypoint getTargetFTWaypoint(Player player) {
        Pending pending = PENDING.get(player.getUUID());
        return pending == null ? null : pending.waypoint();
    }

    /** setTargetFTWaypoint: the countdown begins, or is dropped. */
    public static void setTargetFTWaypoint(Player player, @Nullable LOTRAbstractWaypoint wp) {
        if (wp != null) {
            PENDING.put(player.getUUID(), new Pending(wp, TICKS_UNTIL_FT_MAX));
        } else {
            PENDING.remove(player.getUUID());
        }
    }

    /** cancelFastTravel: on moving, or being robbed. */
    public static void cancelFastTravel(Player player) {
        if (PENDING.remove(player.getUUID()) != null && !player.level().isClientSide()) {
            player.sendSystemMessage(Component.translatable("lotr.fastTravel.motion"));
        }
    }

    /** handleFastTravel: the clock on a tick, and the countdown, told at its start and its last five seconds. */
    private static void handleFastTravel(ServerPlayer player) {
        setTimeSinceFT(player, getTimeSinceFT(player) + 1, false);
        Pending pending = PENDING.get(player.getUUID());
        if (pending == null) {
            return;
        }
        if (player.isSleeping()) {
            player.sendSystemMessage(Component.translatable("lotr.fastTravel.inBed"));
            setTargetFTWaypoint(player, null);
        } else if (pending.ticksUntilFT() > 0) {
            int ticksUntilFT = pending.ticksUntilFT();
            int seconds = ticksUntilFT / 20;
            if (ticksUntilFT == TICKS_UNTIL_FT_MAX) {
                player.sendSystemMessage(Component.translatable("lotr.fastTravel.travelTicksStart", seconds));
            } else if (ticksUntilFT % 20 == 0 && seconds <= 5) {
                player.sendSystemMessage(Component.translatable("lotr.fastTravel.travelTicks", seconds));
            }
            PENDING.put(player.getUUID(), new Pending(pending.waypoint(), ticksUntilFT - 1));
        } else {
            ServerPlayNetworking.send(player, new BounceClientPayload());
        }
    }

    /** receiveFTBouncePacket. */
    private static void receiveFTBouncePacket(ServerPlayer player) {
        Pending pending = PENDING.get(player.getUUID());
        if (pending != null && pending.ticksUntilFT() <= 0) {
            fastTravelTo(player, pending.waypoint());
            setTargetFTWaypoint(player, null);
        }
    }

    // ---------------------------------------------------------------- the journey

    /** fastTravelTo. */
    public static void fastTravelTo(ServerPlayer player, LOTRAbstractWaypoint waypoint) {
        ServerLevel level = player.level();
        int startX = Mth.floor(player.getX());
        int startZ = Mth.floor(player.getZ());
        double range = 256.0;
        Set<Mob> entitiesToTransport = new HashSet<>();
        for (Mob entity : level.getEntitiesOfClass(Mob.class, player.getBoundingBox().inflate(range))) {
            if (entity instanceof LOTRNPCEntity npc) {
                if (npc.hiredNPCInfo.isActive && npc.hiredNPCInfo.getHiringPlayer() == player && npc.hiredNPCInfo.shouldFollowPlayer()) {
                    entitiesToTransport.add(npc);
                    continue;
                }
                if (npc instanceof LOTRGollumEntity gollum && gollum.getGollumOwner() == player && !gollum.isGollumSitting()) {
                    entitiesToTransport.add(gollum);
                    continue;
                }
            }
            if (entity instanceof TamableAnimal pet && pet.getOwner() == player && !pet.isOrderedToSit()) {
                entitiesToTransport.add(pet);
                continue;
            }
            if (entity.getLeashHolder() == player) {
                entitiesToTransport.add(entity);
            }
        }
        // A mount whose rider goes too travels under them.
        entitiesToTransport.removeIf(entity -> entity.getFirstPassenger() instanceof Mob rider && entitiesToTransport.contains(rider));
        int i = waypoint.getXCoord();
        int k = waypoint.getZCoord();
        level.getChunk(i >> 4, k >> 4);
        int j = waypoint.getYCoord(level, i, k);
        Entity playerMount = player.getVehicle();
        player.stopRiding();
        player.teleportTo(level, i + 0.5, j, k + 0.5, Set.of(), player.getYRot(), player.getXRot(), true);
        player.resetFallDistance();
        if (playerMount instanceof Mob mob) {
            playerMount = fastTravelEntity(level, mob, i, j, k);
        }
        if (playerMount != null) {
            setUUIDToMount(player, playerMount.getUUID());
        }
        for (Mob entity : List.copyOf(entitiesToTransport)) {
            Entity mount = entity.getVehicle();
            entity.stopRiding();
            Mob moved = fastTravelEntity(level, entity, i, j, k);
            if (mount instanceof Mob mountMob) {
                Mob movedMount = fastTravelEntity(level, mountMob, i, j, k);
                moved.startRiding(movedMount, true, false);
            }
        }
        boolean custom = waypoint instanceof LOTRCustomWaypoint;
        Optional<UUID> sharer = waypoint instanceof LOTRCustomWaypoint cwp ? Optional.ofNullable(cwp.getSharingPlayerID()) : Optional.empty();
        ServerPlayNetworking.send(player, new ScreenPayload(custom, waypoint.getID(), sharer, startX, startZ));
        setTimeSinceFTWithUpdate(player, 0);
        incrementWPUseCount(player, waypoint);
        if (waypoint instanceof LOTRWaypoint fixed) {
            LOTRPlayerWaypoints.setLastWaypoint(player, fixed);
        }
        if (waypoint instanceof LOTRCustomWaypoint cwp) {
            LOTRCustomWaypoints.logTravel(player, cwp);
        }
    }

    /** fastTravelEntity: there, unharmed by the fall, with no path or quarry. */
    private static Mob fastTravelEntity(ServerLevel level, Mob entity, int i, int j, int k) {
        entity.teleportTo(level, i + 0.5, j, k + 0.5, Set.of(), entity.getYRot(), entity.getXRot(), true);
        entity.resetFallDistance();
        entity.getNavigation().stop();
        entity.setTarget(null);
        return entity;
    }

    private static void setUUIDToMount(ServerPlayer player, @Nullable UUID uuid) {
        Data d = data(player);
        player.setAttached(DATA, new Data(d.ftSinceTick(), Optional.ofNullable(uuid), uuid != null ? 10 : 0, d.lastOnlineTime()));
    }

    /** handleMounting: the mount, found within 32 blocks half a second on, ridden again. */
    private static void handleMounting(ServerPlayer player) {
        Data d = data(player);
        if (d.uuidToMount().isEmpty()) {
            return;
        }
        if (d.uuidToMountTime() > 0) {
            player.setAttached(DATA, new Data(d.ftSinceTick(), d.uuidToMount(), d.uuidToMountTime() - 1, d.lastOnlineTime()));
            return;
        }
        UUID uuid = d.uuidToMount().get();
        for (LivingEntity entity : player.level().getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(32.0))) {
            if (entity.getUUID().equals(uuid)) {
                player.startRiding(entity);
                break;
            }
        }
        setUUIDToMount(player, null);
    }
}
