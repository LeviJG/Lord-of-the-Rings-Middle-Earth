package net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory;

import io.netty.buffer.ByteBuf;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRPouchItem;

import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * LOTRContainerChestWithPouch: a chest (or a chest minecart, or the ender chest) with the pouch
 * that was used on it, and the player's inventory -- shift-clicking moves goods between chest and
 * pouch, and from the player's inventory into the chest.
 */
public class LOTRChestWithPouchMenu extends AbstractContainerMenu {

    /** The pouch's slot and how many rows the chest has. */
    public record OpeningData(int pouchSlot, int chestRows) {
        public static final StreamCodec<ByteBuf, OpeningData> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, OpeningData::pouchSlot, ByteBufCodecs.VAR_INT, OpeningData::chestRows,
                OpeningData::new);
    }

    public final Container chestInv;
    public final LOTRPouchContainer pouchInv;
    public final int pouchSlot;
    public final int numChestRows;
    public final int numPouchRows;
    private final Inventory playerInventory;
    private final ItemStack pouchStack;

    public LOTRChestWithPouchMenu(int containerId, Inventory inventory, OpeningData data) {
        this(containerId, inventory, data.pouchSlot(), new SimpleContainer(data.chestRows() * 9));
    }

    public LOTRChestWithPouchMenu(int containerId, Inventory inventory, int pouchSlot, Container chest) {
        super(LOTRMenus.CHEST_WITH_POUCH, containerId);
        this.chestInv = chest;
        this.pouchSlot = pouchSlot;
        this.playerInventory = inventory;
        this.pouchStack = inventory.getItem(pouchSlot);
        int capacity = LOTRPouchItem.getCapacity(this.pouchStack);
        this.pouchInv = new LOTRPouchContainer(() -> inventory.getItem(pouchSlot), capacity,
                !inventory.player.level().isClientSide());
        this.numChestRows = chest.getContainerSize() / 9;
        this.numPouchRows = capacity / 9;
        chest.startOpen(inventory.player);
        for (int j = 0; j < this.numChestRows; ++j) {
            for (int i = 0; i < 9; ++i) {
                addSlot(new Slot(chest, i + j * 9, 8 + i * 18, 18 + j * 18));
            }
        }
        int pouchSlotsY = 103 + (this.numChestRows - 4) * 18;
        for (int j = 0; j < this.numPouchRows; ++j) {
            for (int i = 0; i < 9; ++i) {
                addSlot(new LOTRPouchMenu.PouchSlot(this.pouchInv, i + j * 9, 8 + i * 18, pouchSlotsY + j * 18));
            }
        }
        addStandardInventorySlots(inventory, 8, pouchSlotsY + 67);
    }

    @Override
    public boolean stillValid(Player player) {
        return this.chestInv.stillValid(player) && this.playerInventory.getItem(this.pouchSlot) == this.pouchStack
                && LOTRPouchItem.isPouch(this.pouchStack);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.chestInv.stopOpen(player);
    }

    @Override
    public void clicked(int slotIndex, int button, ContainerInput input, Player player) {
        if (LOTRPouchMenu.isPouchSlot(this, slotIndex, this.playerInventory, this.pouchSlot)
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
        int chestEnd = this.numChestRows * 9;
        if (index < chestEnd) {
            if (LOTRPouchItem.isPouch(stack) || !moveItemStackTo(stack, chestEnd, chestEnd + this.numPouchRows * 9, true)) {
                return ItemStack.EMPTY;
            }
        } else if (!moveItemStackTo(stack, 0, chestEnd, false)) {
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
