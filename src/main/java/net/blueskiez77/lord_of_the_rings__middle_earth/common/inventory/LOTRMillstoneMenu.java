package net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRMillstoneBlockEntity;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.FurnaceResultSlot;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

// LOTRContainerMillstone, pixel for pixel. The layout is taller than a
// furnace's and stacked vertically, input above output, because the millstone
// grinds downward:
//   0  input   84, 25
//   1  output  84, 71   (LOTRSlotMillstone, i.e. a result slot)
//   player inventory at y 100, hotbar at y 158 -- which is what
//   addStandardInventorySlots(inv, 8, 100) lays out.
public class LOTRMillstoneMenu extends AbstractContainerMenu {

    public static final int SLOT_COUNT = LOTRMillstoneBlockEntity.SLOT_COUNT;
    private static final int INV_START = SLOT_COUNT;          // 2
    private static final int HOTBAR_START = INV_START + 27;   // 29
    private static final int HOTBAR_END = HOTBAR_START + 9;   // 38

    private final Container container;
    private final ContainerData data;

    public LOTRMillstoneMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, new SimpleContainer(SLOT_COUNT),
                new SimpleContainerData(LOTRMillstoneBlockEntity.DATA_COUNT));
    }

    public LOTRMillstoneMenu(int containerId, Inventory inventory, Container container, ContainerData data) {
        super(LOTRMenus.MILLSTONE, containerId);
        checkContainerSize(container, SLOT_COUNT);
        checkContainerDataCount(data, LOTRMillstoneBlockEntity.DATA_COUNT);
        this.container = container;
        this.data = data;

        addSlot(new Slot(container, LOTRMillstoneBlockEntity.INPUT_SLOT, 84, 25) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return container.canPlaceItem(LOTRMillstoneBlockEntity.INPUT_SLOT, stack);
            }
        });
        addSlot(new FurnaceResultSlot(inventory.player, container,
                LOTRMillstoneBlockEntity.OUTPUT_SLOT, 84, 71));

        addStandardInventorySlots(inventory, 8, 100);
        addDataSlots(data);
    }

    public boolean isMilling() {
        return data.get(LOTRMillstoneBlockEntity.DATA_MILLING) > 0;
    }

    /** getMillProgressScaled: how far down the 14px chute the meal has fallen. */
    public int getMillProgress(int pixels) {
        return data.get(LOTRMillstoneBlockEntity.DATA_MILL_TIME) * pixels
                / LOTRMillstoneBlockEntity.MILL_DURATION;
    }

    @Override
    public boolean stillValid(Player player) {
        return container.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = slots.get(index);
        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = slot.getItem();
        ItemStack original = stack.copy();

        if (index < SLOT_COUNT) {
            // Machine -> player.
            if (!moveItemStackTo(stack, INV_START, HOTBAR_END, true)) {
                return ItemStack.EMPTY;
            }
            slot.onQuickCraft(stack, original);
        } else if (!moveItemStackTo(stack, LOTRMillstoneBlockEntity.INPUT_SLOT,
                LOTRMillstoneBlockEntity.INPUT_SLOT + 1, false)) {
            // Player -> machine failed; fall back to moving between the
            // inventory and the hotbar, as vanilla containers do.
            if (index < HOTBAR_START) {
                if (!moveItemStackTo(stack, HOTBAR_START, HOTBAR_END, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!moveItemStackTo(stack, INV_START, HOTBAR_START, false)) {
                return ItemStack.EMPTY;
            }
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
