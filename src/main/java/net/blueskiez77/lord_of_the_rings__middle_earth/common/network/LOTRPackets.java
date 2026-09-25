package net.blueskiez77.lord_of_the_rings__middle_earth.common.network;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRBeaconBlockEntity;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCommandHornItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRCarvedSignBlockEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory.LOTRAnvilMenu;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory.LOTRBarrelMenu;

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
        PayloadTypeRegistry.clientboundPlay()
                .register(LOTROpenSignEditorPayload.TYPE, LOTROpenSignEditorPayload.STREAM_CODEC);
        PayloadTypeRegistry.serverboundPlay()
                .register(LOTRSignEditPayload.TYPE, LOTRSignEditPayload.STREAM_CODEC);

        // LOTRPacketEditSign: only the player whose chisel carved the sign may
        // letter it, and each line must be fifteen allowed characters or fewer
        // -- anything else is written as "!?", as the original did.
        ServerPlayNetworking.registerGlobalReceiver(LOTRSignEditPayload.TYPE, (payload, context) -> {
            ServerPlayer player = context.player();
            context.server().execute(() -> {
                net.minecraft.core.BlockPos pos = payload.pos();
                if (player.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) > 64.0
                        || !(player.level().getBlockEntity(pos)
                                instanceof LOTRCarvedSignBlockEntity sign)
                        || !sign.canBeEditedBy(player)) {
                    LOTRMod.LOGGER.warn("Player {} just tried to change non-editable LOTR sign",
                            player.getName().getString());
                    return;
                }
                java.util.List<String> lines = new java.util.ArrayList<>();
                for (int l = 0; l < LOTRCarvedSignBlockEntity.NUM_LINES; ++l) {
                    String line = l < payload.lines().size() ? payload.lines().get(l) : "";
                    boolean valid = line.length() <= LOTRCarvedSignBlockEntity.MAX_LINE_LENGTH
                            && line.codePoints().allMatch(net.minecraft.util.StringUtil::isAllowedChatCharacter);
                    lines.add(valid ? line : "!?");
                }
                sign.applyEdit(lines);
            });
        });

        PayloadTypeRegistry.serverboundPlay()
                .register(LOTRAnvilRenamePayload.TYPE, LOTRAnvilRenamePayload.STREAM_CODEC);

        // LOTRPacketAnvilRename: the name for the LOTR anvil the player has open.
        ServerPlayNetworking.registerGlobalReceiver(LOTRAnvilRenamePayload.TYPE, (payload, context) -> {
            ServerPlayer player = context.player();
            context.server().execute(() -> {
                if (player.containerMenu instanceof LOTRAnvilMenu menu
                        && menu.stillValid(player)) {
                    // A blank name clears it; one over 30 characters is ignored
                    // (the box takes 40, the handler only ever accepted 30).
                    String rename = payload.name();
                    if (rename == null || rename.isBlank()) {
                        menu.setItemName("");
                    } else if (rename.length() <= 30) {
                        menu.setItemName(rename);
                    }
                }
            });
        });

        PayloadTypeRegistry.clientboundPlay()
                .register(LOTRBrokenPledgePayload.TYPE, LOTRBrokenPledgePayload.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay()
                .register(LOTRAlignmentZonesPayload.TYPE, LOTRAlignmentZonesPayload.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay()
                .register(LOTRFactionRelationsPayload.TYPE, LOTRFactionRelationsPayload.STREAM_CODEC);
        PayloadTypeRegistry.serverboundPlay()
                .register(LOTRPledgeSetPayload.TYPE, LOTRPledgeSetPayload.STREAM_CODEC);

        // LOTRPacketPledgeSet: the faction screen's pledge or unpledge.
        ServerPlayNetworking.registerGlobalReceiver(LOTRPledgeSetPayload.TYPE, (payload, context) -> {
            ServerPlayer player = context.player();
            context.server().execute(() -> net.blueskiez77.lord_of_the_rings__middle_earth.common.fac
                    .LOTRPlayerAlignments.handlePledgeRequest(player, payload.faction()));
        });

        PayloadTypeRegistry.serverboundPlay()
                .register(LOTRBrewingButtonPayload.TYPE, LOTRBrewingButtonPayload.STREAM_CODEC);

        // LOTRPacketBrewingButton: start or stop the barrel the player has open.
        ServerPlayNetworking.registerGlobalReceiver(LOTRBrewingButtonPayload.TYPE, (payload, context) -> {
            ServerPlayer player = context.player();
            context.server().execute(() -> {
                if (player.containerMenu instanceof LOTRBarrelMenu menu
                        && menu.barrel() != null && menu.stillValid(player)) {
                    menu.barrel().handleBrewingButtonPress();
                }
            });
        });

        PayloadTypeRegistry.serverboundPlay()
                .register(LOTRBeaconEditPayload.TYPE, LOTRBeaconEditPayload.STREAM_CODEC);
        PayloadTypeRegistry.serverboundPlay()
                .register(LOTRHornModePayload.TYPE, LOTRHornModePayload.STREAM_CODEC);

        // LOTRPacketHornSelect and LOTRPacketItemSquadron in one: both acted on
        // the item in the main hand only, and the mode is only chosen for a
        // plain horn (item damage 0, now Mode.SELECT).
        ServerPlayNetworking.registerGlobalReceiver(LOTRHornModePayload.TYPE, (payload, context) -> {
            ServerPlayer player = context.player();
            context.server().execute(() -> {
                LOTRCommandHornItem.Mode[] modes = LOTRCommandHornItem.Mode.values();
                if (payload.mode() < 0 || payload.mode() >= modes.length) {
                    return;
                }
                ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
                if (stack.getItem() instanceof LOTRCommandHornItem) {
                    if (LOTRCommandHornItem.getMode(stack) == LOTRCommandHornItem.Mode.SELECT) {
                        LOTRCommandHornItem.setMode(stack, modes[payload.mode()]);
                    }
                    LOTRCommandHornItem.setSquadron(stack, payload.squadron());
                }
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(LOTRBeaconEditPayload.TYPE, (payload, context) -> {
            ServerPlayer player = context.player();
            // Handlers run on the netty thread; touching the world has to
            // happen on the server thread. Context.server() hands it over
            // directly -- ServerPlayer.server is private.
            context.server().execute(() -> {
                // LOTRPacketBeaconEdit: only from a player who opened this
                // beacon's dialog, who is then released (releasePlayer).
                if (player.level().getBlockEntity(payload.pos())
                        instanceof LOTRBeaconBlockEntity beacon && beacon.isPlayerEditing(player)) {
                    beacon.releaseEditingPlayer(player);
                    // StringUtils.isBlank in the original; String.isBlank is
                    // the same test without pulling in commons-lang3.
                    beacon.setBeaconName(blankToNull(payload.beaconName()));
                    beacon.setFellowshipName(blankToNull(payload.fellowshipName()));
                }
            });
        });
    }
}