package net.blueskiez77.lord_of_the_rings__middle_earth.common.network;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRViewingFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFactionData;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRGuiMessageTypes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRPlayerNPCOptions;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRRankOptions;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.server.level.ServerPlayer;

/**
 * The server's side of the Options screen (LOTRPacketSetOption) and the
 * one-time messages.
 *
 * <p>NOT ported yet: options 3 (show map location, with the map, D13) and 5
 * (conquest kills, with conquest, D14).
 */
public final class LOTRMenuNetworking {

    public static final int FRIENDLY_FIRE = 0;
    public static final int HIRED_DEATH_MESSAGES = 1;
    public static final int SHOW_ALIGNMENT = 2;
    public static final int SHOW_MAP_LOCATION = 3;
    public static final int FEM_RANK = 4;
    public static final int CONQUEST = 5;

    private LOTRMenuNetworking() {
    }

    public static void init() {
        LOTRGuiMessageTypes.init();
        LOTRRankOptions.init();
        LOTRFactionData.init();
        LOTRViewingFaction.init();
        PayloadTypeRegistry.serverboundPlay().register(LOTRMenuPayloads.ClientInfo.TYPE, LOTRMenuPayloads.ClientInfo.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(LOTRMenuPayloads.AlignmentSee.TYPE, LOTRMenuPayloads.AlignmentSee.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(LOTRRespawnerPayloads.Open.TYPE, LOTRRespawnerPayloads.Open.STREAM_CODEC);
        PayloadTypeRegistry.serverboundPlay().register(LOTRRespawnerPayloads.Edit.TYPE, LOTRRespawnerPayloads.Edit.STREAM_CODEC);
        // LOTRPacketEditNPCRespawner: the settings only from a creative player; the destroy from anyone at its screen.
        ServerPlayNetworking.registerGlobalReceiver(LOTRRespawnerPayloads.Edit.TYPE, (payload, context) -> {
            ServerPlayer player = context.player();
            if (player.level().getEntity(payload.entityId())
                    instanceof net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRNPCRespawnerEntity spawner) {
                if (player.getAbilities().instabuild) {
                    spawner.readSpawnerData(payload.data());
                }
                if (payload.destroy()) {
                    spawner.onBreak();
                }
            }
        });
        PayloadTypeRegistry.clientboundPlay().register(LOTRAlignmentHudPayloads.EnvironmentOverlay.TYPE,
                LOTRAlignmentHudPayloads.EnvironmentOverlay.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(LOTRAlignmentHudPayloads.AlignDrain.TYPE,
                LOTRAlignmentHudPayloads.AlignDrain.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(LOTRAlignmentHudPayloads.AlignmentBonus.TYPE,
                LOTRAlignmentHudPayloads.AlignmentBonus.STREAM_CODEC);
        // LOTRPacketClientInfo.
        ServerPlayNetworking.registerGlobalReceiver(LOTRMenuPayloads.ClientInfo.TYPE, (payload, context) -> {
            ServerPlayer player = context.player();
            LOTRViewingFaction.setViewingFaction(player, payload.viewingFaction());
            payload.changedRegionMap().forEach((region, fac) -> LOTRViewingFaction.setRegionLastViewedFaction(player, region, fac));
        });
        PayloadTypeRegistry.serverboundPlay().register(LOTRMenuPayloads.SetOption.TYPE, LOTRMenuPayloads.SetOption.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(LOTRMenuPayloads.Message.TYPE, LOTRMenuPayloads.Message.STREAM_CODEC);

        ServerPlayNetworking.registerGlobalReceiver(LOTRMenuPayloads.SetOption.TYPE, (payload, context) -> {
            ServerPlayer player = context.player();
            switch (payload.option()) {
                case FRIENDLY_FIRE -> LOTRPlayerNPCOptions.setFriendlyFire(player, !LOTRPlayerNPCOptions.getFriendlyFire(player));
                case HIRED_DEATH_MESSAGES -> LOTRPlayerNPCOptions.setEnableHiredDeathMessages(player,
                        !LOTRPlayerNPCOptions.getEnableHiredDeathMessages(player));
                case SHOW_ALIGNMENT -> LOTRPlayerAlignments.setHideAlignment(player, !LOTRPlayerAlignments.getHideAlignment(player));
                case FEM_RANK -> LOTRRankOptions.setFemRankOverride(player, !LOTRRankOptions.getFemRankOverride(player));
                default -> {
                }
            }
        });
    }
}
