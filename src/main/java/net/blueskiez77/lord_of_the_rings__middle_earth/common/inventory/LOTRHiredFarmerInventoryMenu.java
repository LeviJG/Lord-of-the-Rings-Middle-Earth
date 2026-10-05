package net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRFarmGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRHiredTask;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import org.jspecify.annotations.Nullable;

/**
 * LOTRContainerHiredFarmerInventory: a hired farmer's seeds, the two slots
 * its harvest goes into (to be taken, not filled), and its bone meal.
 */
public class LOTRHiredFarmerInventoryMenu extends AbstractContainerMenu {

    public final @Nullable LOTRNPCEntity theNPC;

    public LOTRHiredFarmerInventoryMenu(int containerId, Inventory inventory, Integer entityId) {
        this(containerId, inventory, inventory.player.level().getEntity(entityId) instanceof LOTRNPCEntity npc ? npc : null);
    }

    public LOTRHiredFarmerInventoryMenu(int containerId, Inventory inventory, @Nullable LOTRNPCEntity npc) {
        super(LOTRMenus.HIRED_FARMER_INVENTORY, containerId);
        this.theNPC = npc;
        // The client may not know the farmer's inventory: a stand-in of the same size.
        Container inv = npc != null && npc.hiredNPCInfo.getHiredInventory() != null
                ? npc.hiredNPCInfo.getHiredInventory() : new SimpleContainer(4);
        addSlot(new Slot(inv, 0, 80, 21) {
            /** LOTRSlotSeeds: crop seeds. */
            @Override
            public boolean mayPlace(ItemStack stack) {
                return LOTRFarmGoal.getPlant(stack.getItem()) != null;
            }
        });
        for (int i = 0; i < 2; ++i) {
            addSlot(new Slot(inv, i + 1, 71 + i * 18, 47) {
                /** LOTRSlotProtected. */
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return false;
                }
            });
        }
        addSlot(new Slot(inv, 3, 123, 34) {
            /** LOTRSlotBonemeal. */
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(Items.BONE_MEAL);
            }
        });
        addStandardInventorySlots(inventory, 8, 79);
    }

    public static boolean isOpenOn(Player player, LOTRNPCEntity npc) {
        return player.containerMenu instanceof LOTRHiredFarmerInventoryMenu menu && menu.theNPC == npc;
    }

    @Override
    public boolean stillValid(Player player) {
        LOTRNPCEntity npc = this.theNPC;
        return npc != null && npc.isAlive() && npc.hiredNPCInfo.isActive && npc.hiredNPCInfo.getHiringPlayer() == player
                && npc.hiredNPCInfo.getTask() == LOTRHiredTask.FARMER && player.distanceToSqr(npc) <= 144.0;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        if (!player.level().isClientSide() && this.theNPC != null) {
            this.theNPC.hiredNPCInfo.sendClientPacket(true);
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = slots.get(index);
        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = slot.getItem();
        ItemStack original = stack.copy();
        if (index < 4) {
            if (!moveItemStackTo(stack, 4, 40, true)) {
                return ItemStack.EMPTY;
            }
        } else if (slots.get(0).mayPlace(stack) && !moveItemStackTo(stack, 0, 1, false)
                || slots.get(3).mayPlace(stack) && !moveItemStackTo(stack, 3, 4, false)) {
            return ItemStack.EMPTY;
        }
        if (stack.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        if (stack.getCount() == original.getCount()) {
            return ItemStack.EMPTY;
        }
        slot.onTake(player, stack);
        return original;
    }
}
