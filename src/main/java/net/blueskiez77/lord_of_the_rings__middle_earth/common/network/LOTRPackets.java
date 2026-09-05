package net.blueskiez77.lord_of_the_rings__middle_earth.common.network;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRBeaconBlockEntity;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCommandHornItem;

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
        PayloadTypeRegistry.serverboundPlay()
                .register(LOTRHornModePayload.TYPE, LOTRHornModePayload.STREAM_CODEC);

        // LOTRGuiHornSelect's choice, applied to whichever hand holds a horn.
        ServerPlayNetworking.registerGlobalReceiver(LOTRHornModePayload.TYPE, (payload, context) -> {
            ServerPlayer player = context.player();
            context.server().execute(() -> {
                LOTRCommandHornItem.Mode[] modes = LOTRCommandHornItem.Mode.values();
                if (payload.mode() < 0 || payload.mode() >= modes.length) {
                    return;
                }
                for (InteractionHand hand : InteractionHand.values()) {
                    ItemStack stack = player.getItemInHand(hand);
                    if (stack.getItem() instanceof LOTRCommandHornItem) {
                        LOTRCommandHornItem.setMode(stack, modes[payload.mode()]);
                        LOTRCommandHornItem.setSquadron(stack, payload.squadron());
                        return;
                    }
                }
            });
        });

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