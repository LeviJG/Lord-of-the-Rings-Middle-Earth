package net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRPouchItem;

import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import org.apache.commons.lang3.StringUtils;

/**
 * LOTRContainerPouch: the pouch's rows above the player's inventory. The pouch itself cannot be
 * moved while open (nor a pouch put in a pouch), and the window closes if it is no longer there.
 */
public class LOTRPouchMenu extends AbstractContainerMenu {

    public final int pouchSlot;
    public final int capacity;
    public final Container pouchInventory;
    private final Inventory playerInventory;
    private final ItemStack pouchStack;

    /** The pouch's slots, the player's inventory under them (as LOTRContainerPouch laid them out). */
    public LOTRPouchMenu(int containerId, Inventory inventory, Integer slot) {
        super(LOTRMenus.POUCH, containerId);
        this.pouchSlot = slot;
        this.playerInventory = inventory;
        this.pouchStack = inventory.getItem(slot);
        this.capacity = LOTRPouchItem.getCapacity(this.pouchStack);
        this.pouchInventory = new LOTRPouchContainer(() -> inventory.getItem(slot), this.capacity,
                !inventory.player.level().isClientSide());
        int rows = this.capacity / 9;
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlot(new PouchSlot(this.pouchInventory, j + i * 9, 8 + j * 18, 30 + i * 18));
            }
        }
        addStandardInventorySlots(inventory, 8, 98);
    }

    public Component getDisplayName() {
        return this.playerInventory.getItem(this.pouchSlot).getHoverName();
    }

    /** renamePouch: a blank name takes the custom name off. */
    public void renamePouch(String name) {
        ItemStack pouch = this.playerInventory.getItem(this.pouchSlot);
        if (!LOTRPouchItem.isPouch(pouch)) {
            return;
        }
        if (StringUtils.isBlank(name)) {
            pouch.remove(net.minecraft.core.component.DataComponents.CUSTOM_NAME);
        } else {
            pouch.set(net.minecraft.core.component.DataComponents.CUSTOM_NAME, Component.literal(name));
        }
    }

    /** canInteractWith: the same pouch is still in its slot. */
    public boolean pouchStillThere() {
        return this.playerInventory.getItem(this.pouchSlot) == this.pouchStack && LOTRPouchItem.isPouch(this.pouchStack);
    }

    @Override
    public boolean stillValid(Player player) {
        return pouchStillThere();
    }

    /** isPouchSlot: the player's slot the pouch is in. */
    public static boolean isPouchSlot(AbstractContainerMenu menu, int slotIndex, Inventory inventory, int pouchSlot) {
        if (slotIndex >= 0 && slotIndex < menu.slots.size()) {
            Slot slot = menu.slots.get(slotIndex);
            return slot.container == inventory && slot.getContainerSlot() == pouchSlot;
        }
        return false;
    }

    @Override
    public void clicked(int slotIndex, int button, ContainerInput input, Player player) {
        if (isPouchSlot(this, slotIndex, this.playerInventory, this.pouchSlot)
                || input == ContainerInput.SWAP && button == this.pouchSlot) {
            return;
        }
        super.clicked(slotIndex, button, input, player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = this.slots.get(index);
        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = slot.getItem();
        ItemStack original = stack.copy();
        if (index < this.capacity) {
            if (!moveItemStackTo(stack, this.capacity, this.capacity + 36, true)) {
                return ItemStack.EMPTY;
            }
        } else if (LOTRPouchItem.isPouch(stack) || !moveItemStackTo(stack, 0, this.capacity, false)) {
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

    /** LOTRSlotPouch: no pouch goes in a pouch. */
    public static class PouchSlot extends Slot {
        public PouchSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return !LOTRPouchItem.isPouch(stack) && super.mayPlace(stack);
        }
    }
}
