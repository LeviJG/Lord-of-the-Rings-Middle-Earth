package net.blueskiez77.lord_of_the_rings__middle_earth.common.network;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRBeaconBlockEntity;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.server.level.ServerPlayer;

/**
 * The port's first networking. Payload types must be registered on BOTH sides
 * and BEFORE any handler, per PayloadTypeRegistry's contract, so this is called
 * from LOTRMod.onInitialize -- which runs on the client too.
 */
public final class LOTRPackets {

    private LOTRPackets() {
    }

    private static String blankToNull(String s) {
        return s == null || s.isBlank() ? null : s;
    }

    public static void init() {
        PayloadTypeRegistry.serverboundPlay()
                .register(LOTRBeaconEditPayload.TYPE, LOTRBeaconEditPayload.STREAM_CODEC);

        ServerPlayNetworking.registerGlobalReceiver(LOTRBeaconEditPayload.TYPE, (payload, context) -> {
            ServerPlayer player = context.player();
            // Handlers run on the netty thread; touching the world has to
            // happen on the server thread. Context.server() hands it over
            // directly -- ServerPlayer.server is private.
            context.server().execute(() -> {
                // The original checked isPlayerEditing(). Without fellowships
                // the only thing to guard is that the player is actually at the
                // block -- LOTRGuiBeacon closed itself past 8 blocks (64 sq).
                if (player.distanceToSqr(payload.pos().getX() + 0.5,
                        payload.pos().getY() + 0.5, payload.pos().getZ() + 0.5) > 64.0) {
                    return;
                }
                if (player.level().getBlockEntity(payload.pos())
                        instanceof LOTRBeaconBlockEntity beacon) {
                    // StringUtils.isBlank in the original; String.isBlank is
                    // the same test without pulling in commons-lang3.
                    beacon.setBeaconName(blankToNull(payload.beaconName()));
                    beacon.setFellowshipName(blankToNull(payload.fellowshipName()));
                }
            });
        });
    }
}