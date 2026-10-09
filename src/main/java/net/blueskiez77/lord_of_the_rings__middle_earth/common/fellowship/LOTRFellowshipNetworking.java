package net.blueskiez77.lord_of_the_rings__middle_earth.common.fellowship;

import java.util.UUID;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.config.LOTRConfig;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.NameAndId;
import net.minecraft.world.item.ItemStack;

/** The server's side of the fellowships screen's packets (LOTRPacketFellowshipCreate and the rest). */
final class LOTRFellowshipNetworking {

    private LOTRFellowshipNetworking() {
    }

    static void init() {
        PayloadTypeRegistry.clientboundPlay().register(LOTRFellowshipPayloads.Sync.TYPE, LOTRFellowshipPayloads.Sync.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(LOTRFellowshipPayloads.Remove.TYPE, LOTRFellowshipPayloads.Remove.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(LOTRFellowshipPayloads.AcceptResult.TYPE,
                LOTRFellowshipPayloads.AcceptResult.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(LOTRFellowshipPayloads.Notification.TYPE,
                LOTRFellowshipPayloads.Notification.STREAM_CODEC);
        PayloadTypeRegistry.serverboundPlay().register(LOTRFellowshipPayloads.Create.TYPE, LOTRFellowshipPayloads.Create.STREAM_CODEC);
        PayloadTypeRegistry.serverboundPlay().register(LOTRFellowshipPayloads.Do.TYPE, LOTRFellowshipPayloads.Do.STREAM_CODEC);
        PayloadTypeRegistry.serverboundPlay().register(LOTRFellowshipPayloads.DoPlayer.TYPE, LOTRFellowshipPayloads.DoPlayer.STREAM_CODEC);
        PayloadTypeRegistry.serverboundPlay().register(LOTRFellowshipPayloads.InvitePlayer.TYPE,
                LOTRFellowshipPayloads.InvitePlayer.STREAM_CODEC);
        PayloadTypeRegistry.serverboundPlay().register(LOTRFellowshipPayloads.Rename.TYPE, LOTRFellowshipPayloads.Rename.STREAM_CODEC);
        PayloadTypeRegistry.serverboundPlay().register(LOTRFellowshipPayloads.RespondInvite.TYPE,
                LOTRFellowshipPayloads.RespondInvite.STREAM_CODEC);

        ServerPlayNetworking.registerGlobalReceiver(LOTRFellowshipPayloads.Create.TYPE, (payload, context) ->
                LOTRFellowships.createFellowship(context.player().getUUID(), payload.name().trim(), true));

        ServerPlayNetworking.registerGlobalReceiver(LOTRFellowshipPayloads.Do.TYPE, (payload, context) -> {
            ServerPlayer player = context.player();
            UUID uuid = player.getUUID();
            LOTRFellowship fs = LOTRFellowships.getActiveFellowship(payload.fellowshipID());
            if (fs == null) {
                return;
            }
            switch (payload.action()) {
                case DISBAND -> LOTRFellowships.disbandFellowship(uuid, fs, player.getName().getString());
                case LEAVE -> LOTRFellowships.leaveFellowship(uuid, fs);
                case SET_ICON -> {
                    ItemStack held = player.getMainHandItem();
                    LOTRFellowships.setFellowshipIcon(uuid, fs, held.isEmpty() ? null : held.copyWithCount(1));
                }
                case TOGGLE_PVP -> LOTRFellowships.setFellowshipPreventPVP(uuid, fs, !fs.getPreventPVP());
                case TOGGLE_HIRED_FF -> LOTRFellowships.setFellowshipPreventHiredFF(uuid, fs, !fs.getPreventHiredFriendlyFire());
                case TOGGLE_MAP_SHOW -> LOTRFellowships.setFellowshipShowMapLocations(uuid, fs, !fs.getShowMapLocations());
                default -> {
                }
            }
        });

        ServerPlayNetworking.registerGlobalReceiver(LOTRFellowshipPayloads.DoPlayer.TYPE, (payload, context) -> {
            ServerPlayer player = context.player();
            String playerName = player.getName().getString();
            LOTRFellowship fs = LOTRFellowships.getActiveFellowship(payload.fellowshipID());
            if (fs == null) {
                return;
            }
            UUID subject = payload.subject();
            switch (payload.function()) {
                case REMOVE -> LOTRFellowships.removePlayerFromFellowship(player.getUUID(), fs, subject, playerName);
                case TRANSFER -> LOTRFellowships.transferFellowship(player.getUUID(), fs, subject, playerName);
                case OP -> LOTRFellowships.setFellowshipAdmin(player.getUUID(), fs, subject, true, playerName);
                case DEOP -> LOTRFellowships.setFellowshipAdmin(player.getUUID(), fs, subject, false, playerName);
                default -> {
                }
            }
        });

        ServerPlayNetworking.registerGlobalReceiver(LOTRFellowshipPayloads.InvitePlayer.TYPE, (payload, context) -> {
            ServerPlayer player = context.player();
            LOTRFellowship fs = LOTRFellowships.getActiveFellowship(payload.fellowshipID());
            if (fs == null) {
                return;
            }
            int limit = LOTRConfig.fellowshipMaxSize;
            if (limit >= 0 && fs.getPlayerCount() >= limit) {
                LOTRMod.LOGGER.warn("Player {} tried to invite a player with username {} to fellowship {}, but fellowship size {} is already >= the maximum of {}",
                        player.getName().getString(), payload.username(), fs.getName(), fs.getPlayerCount(), limit);
                return;
            }
            UUID invited = player.level().getServer().services().nameToIdCache().get(payload.username().trim())
                    .map(NameAndId::id).orElse(null);
            if (invited != null) {
                LOTRFellowships.invitePlayerToFellowship(player.getUUID(), fs, invited, player.getName().getString());
            } else {
                LOTRMod.LOGGER.warn("Player {} tried to invite a player with username {} to fellowship {}, but couldn't find the invited player's UUID",
                        player.getName().getString(), payload.username(), fs.getName());
            }
        });

        ServerPlayNetworking.registerGlobalReceiver(LOTRFellowshipPayloads.Rename.TYPE, (payload, context) -> {
            LOTRFellowship fs = LOTRFellowships.getActiveFellowship(payload.fellowshipID());
            if (fs != null && !payload.name().isBlank()) {
                LOTRFellowships.renameFellowship(context.player().getUUID(), fs, payload.name().trim());
            }
        });

        ServerPlayNetworking.registerGlobalReceiver(LOTRFellowshipPayloads.RespondInvite.TYPE, (payload, context) -> {
            ServerPlayer player = context.player();
            LOTRFellowship fs = LOTRFellowships.getFellowship(payload.fellowshipID());
            if (fs == null) {
                LOTRFellowships.sendNonexistent(player, payload.fellowshipID());
            } else if (payload.accept()) {
                LOTRFellowships.acceptFellowshipInvite(player.getUUID(), fs, true);
            } else {
                LOTRFellowships.rejectFellowshipInvite(player.getUUID(), fs);
            }
        });
    }
}
