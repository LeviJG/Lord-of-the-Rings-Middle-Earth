package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory.LOTRHiredFarmerInventoryMenu;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory.LOTRHiredWarriorInventoryMenu;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCommandHornItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCommandSwordItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRHiredPayloads;

import net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

import org.jspecify.annotations.Nullable;

/**
 * The server's side of the hired-unit screens: LOTRPacketHiredUnitInteract,
 * LOTRPacketHiredUnitCommand, LOTRPacketHiredUnitDismiss and
 * LOTRPacketNPCSquadron, each taken only from the unit's own player.
 */
public final class LOTRHiredNetworking {

    private LOTRHiredNetworking() {
    }

    public static void init() {
        PayloadTypeRegistry.clientboundPlay().register(LOTRHiredPayloads.HiredGui.TYPE, LOTRHiredPayloads.HiredGui.STREAM_CODEC);
        PayloadTypeRegistry.serverboundPlay().register(LOTRHiredPayloads.Interact.TYPE, LOTRHiredPayloads.Interact.STREAM_CODEC);
        PayloadTypeRegistry.serverboundPlay().register(LOTRHiredPayloads.Command.TYPE, LOTRHiredPayloads.Command.STREAM_CODEC);
        PayloadTypeRegistry.serverboundPlay().register(LOTRHiredPayloads.Dismiss.TYPE, LOTRHiredPayloads.Dismiss.STREAM_CODEC);
        PayloadTypeRegistry.serverboundPlay().register(LOTRHiredPayloads.Squadron.TYPE, LOTRHiredPayloads.Squadron.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(LOTRHiredPayloads.SwordCommandFX.TYPE,
                LOTRHiredPayloads.SwordCommandFX.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(LOTRHiredPayloads.OpenSquadronItem.TYPE,
                LOTRHiredPayloads.OpenSquadronItem.STREAM_CODEC);
        PayloadTypeRegistry.serverboundPlay().register(LOTRHiredPayloads.ItemSquadron.TYPE,
                LOTRHiredPayloads.ItemSquadron.STREAM_CODEC);

        // LOTRPacketItemSquadron: the company named on the command sword or horn in hand.
        ServerPlayNetworking.registerGlobalReceiver(LOTRHiredPayloads.ItemSquadron.TYPE, (payload, context) -> {
            ItemStack stack = context.player().getMainHandItem();
            if (isSquadronItem(stack)) {
                LOTRCommandHornItem.setSquadron(stack, payload.squadron().isEmpty() ? ""
                        : LOTRCommandHornItem.checkAcceptableLength(payload.squadron()));
            }
        });

        // LOTRPacketHiredUnitInteract: talk, or open the unit's own screen.
        ServerPlayNetworking.registerGlobalReceiver(LOTRHiredPayloads.Interact.TYPE, (payload, context) -> {
            ServerPlayer player = context.player();
            LOTRNPCEntity hiredNPC = ownUnit(player, payload.entityId());
            if (hiredNPC == null) {
                return;
            }
            boolean closeScreen = false;
            if (payload.action() == 0) {
                hiredNPC.npcTalkTick = hiredNPC.getNPCTalkInterval();
                closeScreen = hiredNPC.speakTo(player);
            } else if (payload.action() == 1) {
                hiredNPC.hiredNPCInfo.sendClientPacket(true);
            }
            if (closeScreen) {
                player.closeContainer();
            }
        });

        // LOTRPacketHiredUnitCommand.
        ServerPlayNetworking.registerGlobalReceiver(LOTRHiredPayloads.Command.TYPE, (payload, context) -> {
            ServerPlayer player = context.player();
            LOTRNPCEntity hiredNPC = ownUnit(player, payload.entityId());
            if (hiredNPC == null) {
                return;
            }
            LOTRHiredNPCInfo info = hiredNPC.hiredNPCInfo;
            int page = payload.page();
            int action = payload.action();
            int value = payload.value();
            if (action == -1) {
                info.isGuiOpen = false;
                return;
            }
            LOTRHiredTask task = info.getTask();
            if (task == LOTRHiredTask.WARRIOR) {
                if (page == 0) {
                    player.openMenu(provider(hiredNPC, Component.translatable("lotr.gui.warrior.openInv"),
                            (id, inv) -> new LOTRHiredWarriorInventoryMenu(id, inv, hiredNPC)));
                } else if (page == 1) {
                    switch (action) {
                        case 0 -> info.teleportAutomatically = !info.teleportAutomatically;
                        case 1 -> info.setGuardMode(!info.isGuardMode());
                        case 2 -> info.setGuardRange(value);
                        default -> {
                        }
                    }
                }
            } else if (task == LOTRHiredTask.FARMER) {
                switch (action) {
                    case 0 -> info.setGuardMode(!info.isGuardMode());
                    case 1 -> info.setGuardRange(value);
                    case 2 -> player.openMenu(provider(hiredNPC, hiredNPC.getDisplayName(),
                            (id, inv) -> new LOTRHiredFarmerInventoryMenu(id, inv, hiredNPC)));
                    default -> {
                    }
                }
            }
            info.sendClientPacket(false);
        });

        // LOTRPacketHiredUnitDismiss: the unit, and a mount or rider of the same player's with it.
        ServerPlayNetworking.registerGlobalReceiver(LOTRHiredPayloads.Dismiss.TYPE, (payload, context) -> {
            ServerPlayer player = context.player();
            LOTRNPCEntity hiredNPC = ownUnit(player, payload.entityId());
            if (hiredNPC == null || payload.action() != 0) {
                return;
            }
            hiredNPC.hiredNPCInfo.dismissUnit(false);
            dismissIfOwn(hiredNPC.getVehicle(), player);
            dismissIfOwn(hiredNPC.getFirstPassenger(), player);
            player.closeContainer();
        });

        // LOTRPacketNPCSquadron.
        ServerPlayNetworking.registerGlobalReceiver(LOTRHiredPayloads.Squadron.TYPE, (payload, context) -> {
            LOTRNPCEntity hiredNPC = ownUnit(context.player(), payload.entityId());
            if (hiredNPC != null) {
                hiredNPC.hiredNPCInfo.setSquadron(payload.squadron().isEmpty() ? ""
                        : LOTRCommandHornItem.checkAcceptableLength(payload.squadron()));
            }
        });
    }

    /** LOTRSquadrons.SquadronItem: the command sword and the horn of command. */
    public static boolean isSquadronItem(ItemStack stack) {
        return stack.getItem() instanceof LOTRCommandSwordItem || stack.getItem() instanceof LOTRCommandHornItem;
    }

    /** The unit, if it is active in this player's service. */
    private static @Nullable LOTRNPCEntity ownUnit(ServerPlayer player, int entityId) {
        if (player.level().getEntity(entityId) instanceof LOTRNPCEntity npc && npc.hiredNPCInfo.isActive
                && npc.hiredNPCInfo.getHiringPlayer() == player) {
            return npc;
        }
        return null;
    }

    private static void dismissIfOwn(@Nullable Entity entity, Player player) {
        if (entity instanceof LOTRNPCEntity npc && npc.hiredNPCInfo.isActive && npc.hiredNPCInfo.getHiringPlayer() == player) {
            npc.hiredNPCInfo.dismissUnit(false);
        }
    }

    private interface MenuFactory {
        AbstractContainerMenu create(int containerId, Inventory inventory);
    }

    private static ExtendedMenuProvider<Integer> provider(LOTRNPCEntity npc, Component title, MenuFactory factory) {
        return new ExtendedMenuProvider<>() {
            @Override
            public Integer getScreenOpeningData(ServerPlayer player) {
                return npc.getId();
            }

            @Override
            public Component getDisplayName() {
                return title;
            }

            @Override
            public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
                return factory.create(containerId, inventory);
            }
        };
    }
}
