package net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRUnsmelteryBlockEntity;

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

// LOTRContainerUnsmeltery, pixel for pixel -- the vanilla furnace layout:
//   0  input   56, 17
//   1  fuel    56, 53
//   2  output 116, 35   (LOTRSlotUnsmeltResult, i.e. a FurnaceResultSlot)
//   player inventory at y 84, hotbar at y 142 -- which is exactly what
//   addStandardInventorySlots(inv, 8, 84) lays out.
public class LOTRUnsmelteryMenu extends AbstractContainerMenu {

    public static final int SLOT_COUNT = LOTRUnsmelteryBlockEntity.SLOT_COUNT;
    private static final int INV_START = SLOT_COUNT;          // 3
    private static final int HOTBAR_START = INV_START + 27;   // 30
    private static final int HOTBAR_END = HOTBAR_START + 9;   // 39

    private final Container container;
    private final ContainerData data;

    public LOTRUnsmelteryMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, new SimpleContainer(SLOT_COUNT),
                new SimpleContainerData(LOTRUnsmelteryBlockEntity.DATA_COUNT));
    }

    public LOTRUnsmelteryMenu(int containerId, Inventory inventory, Container container, ContainerData data) {
        super(LOTRMenus.UNSMELTERY, containerId);
        checkContainerSize(container, SLOT_COUNT);
        checkContainerDataCount(data, LOTRUnsmelteryBlockEntity.DATA_COUNT);
        this.container = container;
        this.data = data;

        addSlot(new Slot(container, LOTRUnsmelteryBlockEntity.INPUT_SLOT, 56, 17) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return container.canPlaceItem(LOTRUnsmelteryBlockEntity.INPUT_SLOT, stack);
            }
        });
        addSlot(new Slot(container, LOTRUnsmelteryBlockEntity.FUEL_SLOT, 56, 53) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return container.canPlaceItem(LOTRUnsmelteryBlockEntity.FUEL_SLOT, stack);
            }
        });
        addSlot(new FurnaceResultSlot(inventory.player, container,
                LOTRUnsmelteryBlockEntity.OUTPUT_SLOT, 116, 35));

        addStandardInventorySlots(inventory, 8, 84);
        addDataSlots(data);
    }

    public boolean isLit() {
        return data.get(LOTRUnsmelteryBlockEntity.DATA_LIT) > 0;
    }

    /** Flame height, as a fraction of the fuel left. */
    public int getLitProgress(int pixels) {
        int total = data.get(LOTRUnsmelteryBlockEntity.DATA_LIT_TOTAL);
        if (total == 0) {
            total = LOTRUnsmelteryBlockEntity.SMELT_DURATION;
        }
        return data.get(LOTRUnsmelteryBlockEntity.DATA_LIT) * pixels / total;
    }

    public int getSmeltProgress(int pixels) {
        int cooking = data.get(LOTRUnsmelteryBlockEntity.DATA_COOKING);
        return cooking * pixels / LOTRUnsmelteryBlockEntity.SMELT_DURATION;
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
        } else if (!moveItemStackTo(stack, LOTRUnsmelteryBlockEntity.INPUT_SLOT,
                LOTRUnsmelteryBlockEntity.INPUT_SLOT + 1, false)
                && !moveItemStackTo(stack, LOTRUnsmelteryBlockEntity.FUEL_SLOT,
                LOTRUnsmelteryBlockEntity.FUEL_SLOT + 1, false)) {
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
