package net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRHobbitOvenBlockEntity;

import net.minecraft.util.Mth;
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

// LOTRContainerHobbitOven, pixel for pixel:
//   0-8    input   x = 8 + i*18, y = 21
//   9-17   output  x = 8 + i*18, y = 67   (FurnaceResultSlot)
//   18     fuel    x = 80,       y = 111
//   19-45  player inventory at y 133/151/169, 46-54 hotbar at y 191
//          -- addStandardInventorySlots(inv, 8, 133) gives exactly that.
public class LOTRHobbitOvenMenu extends AbstractContainerMenu {

    public static final int SLOT_COUNT = LOTRHobbitOvenBlockEntity.SLOT_COUNT;
    private static final int CONTAINER_END = SLOT_COUNT;      // 19
    private static final int INV_START = CONTAINER_END;       // 19
    private static final int HOTBAR_START = INV_START + 27;   // 46
    private static final int HOTBAR_END = HOTBAR_START + 9;   // 55

    private final Container container;
    private final ContainerData data;

    public LOTRHobbitOvenMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, new SimpleContainer(SLOT_COUNT),
                new SimpleContainerData(LOTRHobbitOvenBlockEntity.DATA_COUNT));
    }

    public LOTRHobbitOvenMenu(int containerId, Inventory inventory, Container container, ContainerData data) {
        super(LOTRMenus.HOBBIT_OVEN, containerId);
        checkContainerSize(container, SLOT_COUNT);
        checkContainerDataCount(data, LOTRHobbitOvenBlockEntity.DATA_COUNT);
        this.container = container;
        this.data = data;

        for (int i = 0; i < 9; ++i) {
            final int inputSlot = LOTRHobbitOvenBlockEntity.INPUT_START + i;
            addSlot(new Slot(container, inputSlot, 8 + i * 18, 21) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return container.canPlaceItem(inputSlot, stack);
                }
            });
        }
        for (int i = 0; i < 9; ++i) {
            addSlot(new FurnaceResultSlot(inventory.player, container,
                    LOTRHobbitOvenBlockEntity.OUTPUT_START + i, 8 + i * 18, 67));
        }
        addSlot(new Slot(container, LOTRHobbitOvenBlockEntity.FUEL_SLOT, 80, 111) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return container.canPlaceItem(LOTRHobbitOvenBlockEntity.FUEL_SLOT, stack);
            }
        });

        addStandardInventorySlots(inventory, 8, 133);
        addDataSlots(data);
    }

    @Override
    public boolean stillValid(Player player) {
        return container.stillValid(player);
    }

    public int getCookProgress(int pixels) {
        return Mth.clamp(data.get(LOTRHobbitOvenBlockEntity.DATA_COOKING) * pixels
                / LOTRHobbitOvenBlockEntity.COOK_DURATION, 0, pixels);
    }

    public int getLitProgress(int pixels) {
        int total = data.get(LOTRHobbitOvenBlockEntity.DATA_LIT_TOTAL);
        if (total == 0) {
            total = LOTRHobbitOvenBlockEntity.COOK_DURATION;
        }
        return Mth.clamp(data.get(LOTRHobbitOvenBlockEntity.DATA_LIT) * pixels / total, 0, pixels);
    }

    public boolean isCooking() {
        return data.get(LOTRHobbitOvenBlockEntity.DATA_LIT) > 0;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        ItemStack clicked = ItemStack.EMPTY;
        Slot slot = slots.get(slotIndex);
        if (slot == null || !slot.hasItem()) {
            return clicked;
        }
        ItemStack stack = slot.getItem();
        clicked = stack.copy();

        if (slotIndex >= LOTRHobbitOvenBlockEntity.OUTPUT_START
                && slotIndex < LOTRHobbitOvenBlockEntity.FUEL_SLOT) {
            if (!moveItemStackTo(stack, INV_START, HOTBAR_END, true)) {
                return ItemStack.EMPTY;
            }
            slot.onQuickCraft(stack, clicked);
        } else if (slotIndex < CONTAINER_END) {
            if (!moveItemStackTo(stack, INV_START, HOTBAR_END, false)) {
                return ItemStack.EMPTY;
            }
        } else if (container.canPlaceItem(LOTRHobbitOvenBlockEntity.FUEL_SLOT, stack)) {
            // Fuel is tested BEFORE the input slots here, the reverse of the
            // forge: the oven's input test cannot run client-side (it needs a
            // recipe lookup), so anything would otherwise land in the inputs.
            if (!moveItemStackTo(stack, LOTRHobbitOvenBlockEntity.FUEL_SLOT, CONTAINER_END, false)) {
                return ItemStack.EMPTY;
            }
        } else if (!moveItemStackTo(stack, LOTRHobbitOvenBlockEntity.INPUT_START,
                LOTRHobbitOvenBlockEntity.OUTPUT_START, false)) {
            if (slotIndex < HOTBAR_START) {
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
        if (stack.getCount() == clicked.getCount()) {
            return ItemStack.EMPTY;
        }
        slot.onTake(player, stack);
        return clicked;
    }
}