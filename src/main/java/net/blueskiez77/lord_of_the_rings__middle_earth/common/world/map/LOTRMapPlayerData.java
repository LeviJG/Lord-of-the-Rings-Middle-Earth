package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.mojang.authlib.GameProfile;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.config.LOTRConfig;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fellowship.LOTRFellowship;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fellowship.LOTRFellowships;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRWorldGen;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.commands.Commands;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.NameAndId;
import net.minecraft.world.entity.player.Player;

/**
 * The map's share of LOTRPlayerData, under its keys: whether the player hides their place on others'
 * maps ("HideOnMap", the Options screen's Show Map Location), whether an operator in creative has hidden
 * it from all but operators ("AdminHideMap", /opHideMap, lifted on leaving creative or losing op), and
 * which waypoints their map shows ("ShowWP", "ShowCWP", "ShowHiddenSWP", the map's toggles). Kept through
 * death and sent to the player, whose map reads its toggles from it.
 *
 * <p>Also LOTRLevelData.sendPlayerLocationsToPlayer (LOTRPacketUpdatePlayerLocations), each second, to
 * each player in Middle-earth: where the others there stand -- those not hiding, or hiding but in a
 * fellowship with them that shows its members on the map, or anyone to an operator in creative; never
 * an operator hiding from a non-operator; all or none if the config forces it -- and the operators'
 * teleport from the map (LOTRPacketMapTp), with the map's question whether the player is one
 * (LOTRPacketIsOpRequest).
 */
public final class LOTRMapPlayerData {

    public record Options(boolean hideOnMap, boolean adminHideMap, boolean showWP, boolean showCWP, boolean showHiddenSWP) {
        public static final Options DEFAULT = new Options(false, false, true, true, false);
        public static final Codec<Options> CODEC = RecordCodecBuilder.create(i -> i.group(
                Codec.BOOL.optionalFieldOf("HideOnMap", false).forGetter(Options::hideOnMap),
                Codec.BOOL.optionalFieldOf("AdminHideMap", false).forGetter(Options::adminHideMap),
                Codec.BOOL.optionalFieldOf("ShowWP", true).forGetter(Options::showWP),
                Codec.BOOL.optionalFieldOf("ShowCWP", true).forGetter(Options::showCWP),
                Codec.BOOL.optionalFieldOf("ShowHiddenSWP", false).forGetter(Options::showHiddenSWP)
        ).apply(i, Options::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, Options> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.BOOL, Options::hideOnMap, ByteBufCodecs.BOOL, Options::adminHideMap, ByteBufCodecs.BOOL, Options::showWP,
                ByteBufCodecs.BOOL, Options::showCWP, ByteBufCodecs.BOOL, Options::showHiddenSWP, Options::new);

        public Options withToggles(boolean wp, boolean cwp, boolean hiddenSWP) {
            return new Options(this.hideOnMap, this.adminHideMap, wp, cwp, hiddenSWP);
        }
    }

    public static final AttachmentType<Options> OPTIONS = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "map_options"),
            builder -> builder.initializer(() -> Options.DEFAULT).persistent(Options.CODEC).copyOnDeath()
                    .syncWith(Options.STREAM_CODEC, AttachmentSyncPredicate.targetOnly()));

    /** LOTRPacketClientInfo's waypoint toggles, sent by the map as they change. */
    public record TogglesPayload(boolean showWP, boolean showCWP, boolean showHiddenSWP) implements CustomPacketPayload {
        public static final Type<TogglesPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "map_toggles"));
        public static final StreamCodec<RegistryFriendlyByteBuf, TogglesPayload> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.BOOL, TogglesPayload::showWP, ByteBufCodecs.BOOL, TogglesPayload::showCWP,
                ByteBufCodecs.BOOL, TogglesPayload::showHiddenSWP, TogglesPayload::new);

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public record PlayerLocation(GameProfile profile, double posX, double posZ) {
        public static final StreamCodec<RegistryFriendlyByteBuf, PlayerLocation> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.GAME_PROFILE, PlayerLocation::profile, ByteBufCodecs.DOUBLE, PlayerLocation::posX,
                ByteBufCodecs.DOUBLE, PlayerLocation::posZ, PlayerLocation::new);
    }

    /** LOTRPacketUpdatePlayerLocations: the players the map shows, in place of those it showed. */
    public record LocationsPayload(List<PlayerLocation> locations) implements CustomPacketPayload {
        public static final Type<LocationsPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "player_locations"));
        public static final StreamCodec<RegistryFriendlyByteBuf, LocationsPayload> STREAM_CODEC = StreamCodec.composite(
                PlayerLocation.STREAM_CODEC.apply(ByteBufCodecs.list()), LocationsPayload::locations, LocationsPayload::new);

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    /** LOTRPacketMapTp: an operator's teleport to the top of this column. */
    public record TeleportPayload(int xCoord, int zCoord) implements CustomPacketPayload {
        public static final Type<TeleportPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "map_tp"));
        public static final StreamCodec<RegistryFriendlyByteBuf, TeleportPayload> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, TeleportPayload::xCoord, ByteBufCodecs.VAR_INT, TeleportPayload::zCoord, TeleportPayload::new);

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    /** LOTRPacketIsOpRequest: whether the player is an operator, asked by the map for its teleport. */
    public record IsOpRequestPayload() implements CustomPacketPayload {
        public static final Type<IsOpRequestPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "is_op_request"));
        public static final StreamCodec<RegistryFriendlyByteBuf, IsOpRequestPayload> STREAM_CODEC = StreamCodec.unit(new IsOpRequestPayload());

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    /** LOTRPacketIsOpResponse. */
    public record IsOpResponsePayload(boolean isOp) implements CustomPacketPayload {
        public static final Type<IsOpResponsePayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "is_op_response"));
        public static final StreamCodec<RegistryFriendlyByteBuf, IsOpResponsePayload> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.BOOL, IsOpResponsePayload::isOp, IsOpResponsePayload::new);

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    private LOTRMapPlayerData() {
    }

    public static Options get(Player player) {
        return player.getAttachedOrCreate(OPTIONS);
    }

    public static boolean getHideMapLocation(Player player) {
        return get(player).hideOnMap();
    }

    public static void setHideMapLocation(Player player, boolean flag) {
        Options o = get(player);
        player.setAttached(OPTIONS, new Options(flag, o.adminHideMap(), o.showWP(), o.showCWP(), o.showHiddenSWP()));
    }

    public static boolean getAdminHideMap(Player player) {
        return get(player).adminHideMap();
    }

    public static void setAdminHideMap(Player player, boolean flag) {
        Options o = get(player);
        player.setAttached(OPTIONS, new Options(o.hideOnMap(), flag, o.showWP(), o.showCWP(), o.showHiddenSWP()));
    }

    public static boolean isOp(ServerPlayer player) {
        return player.level().getServer().getPlayerList().isOp(new NameAndId(player.getGameProfile()));
    }

    public static void init() {
        PayloadTypeRegistry.serverboundPlay().register(TogglesPayload.TYPE, TogglesPayload.STREAM_CODEC);
        PayloadTypeRegistry.serverboundPlay().register(TeleportPayload.TYPE, TeleportPayload.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(LocationsPayload.TYPE, LocationsPayload.STREAM_CODEC);
        PayloadTypeRegistry.serverboundPlay().register(IsOpRequestPayload.TYPE, IsOpRequestPayload.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(IsOpResponsePayload.TYPE, IsOpResponsePayload.STREAM_CODEC);
        ServerPlayNetworking.registerGlobalReceiver(IsOpRequestPayload.TYPE,
                (payload, context) -> context.responseSender().sendPacket(new IsOpResponsePayload(isOp(context.player()))));
        ServerPlayNetworking.registerGlobalReceiver(TogglesPayload.TYPE, (payload, context) -> {
            ServerPlayer player = context.player();
            player.setAttached(OPTIONS, get(player).withToggles(payload.showWP(), payload.showCWP(), payload.showHiddenSWP()));
        });
        ServerPlayNetworking.registerGlobalReceiver(TeleportPayload.TYPE, (payload, context) -> {
            ServerPlayer player = context.player();
            if (isOp(player)) {
                ServerLevel level = player.level();
                int i = payload.xCoord();
                int k = payload.zCoord();
                int j = LOTRWorldGenUtil.getTopSolidOrLiquidBlock(level, i, k);
                level.getServer().getCommands().performPrefixedCommand(player.createCommandSourceStack(), "tp " + i + " " + j + " " + k);
            }
        });
        ServerTickEvents.END_SERVER_TICK.register(LOTRMapPlayerData::serverTick);
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(Commands.literal("opHideMap")
                        .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                        .executes(ctx -> {
                            ServerPlayer player = ctx.getSource().getPlayerOrException();
                            if (!isOp(player)) {
                                ctx.getSource().sendFailure(Component.translatable("commands.lotr.opHideMap.notOp"));
                                return 0;
                            }
                            if (!player.getAbilities().instabuild) {
                                ctx.getSource().sendFailure(Component.translatable("commands.lotr.opHideMap.notCreative"));
                                return 0;
                            }
                            setAdminHideMap(player, true);
                            ctx.getSource().sendSuccess(() -> Component.translatable("commands.lotr.opHideMap.hiding"), true);
                            return 1;
                        })));
    }

    private static void serverTick(MinecraftServer server) {
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            // checkBiomeAndAdminHideMap: hidden only while an operator in creative.
            if (getAdminHideMap(player) && (!player.getAbilities().instabuild || !isOp(player))) {
                setAdminHideMap(player, false);
                player.sendSystemMessage(Component.translatable("commands.lotr.opHideMap.unhide"));
            }
        }
        ServerLevel level = server.getLevel(LOTRWorldGen.MIDDLE_EARTH);
        if (level != null && level.getGameTime() % 20L == 0L) {
            for (ServerPlayer player : level.players()) {
                sendPlayerLocationsToPlayer(player, level);
            }
        }
    }

    /** LOTRLevelData.sendPlayerLocationsToPlayer. (The original also hid fellows during a siege, which nothing here starts.) */
    private static void sendPlayerLocationsToPlayer(ServerPlayer sendPlayer, ServerLevel level) {
        boolean isOp = isOp(sendPlayer);
        boolean creative = sendPlayer.getAbilities().instabuild;
        Collection<LOTRFellowship> fellowshipsMapShow = new ArrayList<>();
        for (LOTRFellowship fs : LOTRFellowships.getFellowships(sendPlayer.getUUID())) {
            if (fs.getShowMapLocations()) {
                fellowshipsMapShow.add(fs);
            }
        }
        List<PlayerLocation> locations = new ArrayList<>();
        for (ServerPlayer otherPlayer : level.players()) {
            if (otherPlayer == sendPlayer) {
                continue;
            }
            boolean show = !getHideMapLocation(otherPlayer);
            if (!isOp && getAdminHideMap(otherPlayer) || LOTRConfig.forceMapLocations == 1) {
                show = false;
            } else if (LOTRConfig.forceMapLocations == 2) {
                show = true;
            } else if (!show) {
                if (isOp && creative) {
                    show = true;
                } else {
                    UUID otherID = otherPlayer.getUUID();
                    for (LOTRFellowship fs : fellowshipsMapShow) {
                        if (fs.containsPlayer(otherID)) {
                            show = true;
                            break;
                        }
                    }
                }
            }
            if (show) {
                locations.add(new PlayerLocation(otherPlayer.getGameProfile(), otherPlayer.getX(), otherPlayer.getZ()));
            }
        }
        ServerPlayNetworking.send(sendPlayer, new LocationsPayload(locations));
    }
}
