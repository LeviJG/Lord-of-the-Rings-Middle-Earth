package net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRBarrelBlockEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.recipe.LOTRBrewingRecipes;
import net.minecraft.resources.Identifier;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

/**
 * LOTRContainerBarrel, pixel for pixel:
 *
 * <pre>
 *   0-8   3x3 at x = 14 + j*18, y = 34 + i*18   (the bottom row takes only water)
 *   9     the brew at 108, 52                   (look, don't touch)
 *   10-45 player inventory at x 25, y 139, hotbar at y 197
 * </pre>
 *
 * <p>LOTRSlotBarrel refused everything once the barrel was brewing or full.
 */
public class LOTRBarrelMenu extends AbstractContainerMenu {
    private static final Identifier EMPTY_BUCKET_SLOT =
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "container/slot/barrel_bucket");
    private static final Identifier EMPTY_MUG_SLOT =
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "container/slot/barrel_mug");

    private static final int INV_START = LOTRBarrelBlockEntity.SLOT_COUNT;
    private static final int HOTBAR_START = INV_START + 27;
    private static final int INV_END = HOTBAR_START + 9;

    private final Container container;
    private final ContainerData data;

    public LOTRBarrelMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, new SimpleContainer(LOTRBarrelBlockEntity.SLOT_COUNT),
                new SimpleContainerData(LOTRBarrelBlockEntity.DATA_COUNT));
    }

    public LOTRBarrelMenu(int containerId, Inventory inventory, Container container, ContainerData data) {
        super(LOTRMenus.BARREL, containerId);
        checkContainerSize(container, LOTRBarrelBlockEntity.SLOT_COUNT);
        checkContainerDataCount(data, LOTRBarrelBlockEntity.DATA_COUNT);
        this.container = container;
        this.data = data;
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                addSlot(new BarrelSlot(container, j + i * 3, 14 + j * 18, 34 + i * 18, i == 2));
            }
        }
        addSlot(new ResultSlot(container, LOTRBarrelBlockEntity.BARREL_SLOT, 108, 52));
        addStandardInventorySlots(inventory, 25, 139);
        addDataSlots(data);
    }

    public int barrelMode() {
        return data.get(LOTRBarrelBlockEntity.DATA_MODE);
    }

    public int brewingTime() {
        return data.get(LOTRBarrelBlockEntity.DATA_BREWING_TIME);
    }

    public ItemStack brewingItem() {
        return container.getItem(LOTRBarrelBlockEntity.BARREL_SLOT);
    }

    /** The barrel itself, on the server; null on the client. */
    public @Nullable LOTRBarrelBlockEntity barrel() {
        return container instanceof LOTRBarrelBlockEntity barrel ? barrel : null;
    }

    @Override
    public boolean stillValid(Player player) {
        return container.stillValid(player);
    }

    /** transferStackInSlot. */
    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = slots.get(index);
        if (slot == null || !slot.hasItem()) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = slot.getItem();
        ItemStack original = stack.copy();
        if (index < LOTRBarrelBlockEntity.BARREL_SLOT) {
            if (!moveItemStackTo(stack, INV_START, INV_END, true)) {
                return ItemStack.EMPTY;
            }
        } else if (index != LOTRBarrelBlockEntity.BARREL_SLOT) {
            boolean moved = false;
            if (slots.get(0).mayPlace(stack)) {
                moved = LOTRBrewingRecipes.isWaterSource(stack)
                        ? moveItemStackTo(stack, 6, 9, false)
                        : moveItemStackTo(stack, 0, 6, false);
            }
            if (!moved && (index < HOTBAR_START
                    ? !moveItemStackTo(stack, HOTBAR_START, INV_END, false)
                    : !moveItemStackTo(stack, INV_START, HOTBAR_START, false))) {
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

    /** LOTRSlotBarrel. */
    private class BarrelSlot extends Slot {
        private final boolean water;

        BarrelSlot(Container container, int index, int x, int y, boolean water) {
            super(container, index, x, y);
            this.water = water;
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            if (barrelMode() != LOTRBarrelBlockEntity.EMPTY) {
                return false;
            }
            return !water || LOTRBrewingRecipes.isWaterSource(stack);
        }

        @Override
        public @Nullable Identifier getNoItemIcon() {
            return water ? EMPTY_BUCKET_SLOT : null;
        }
    }

    /** LOTRSlotBarrelResult. */
    private static class ResultSlot extends Slot {
        ResultSlot(Container container, int index, int x, int y) {
            super(container, index, x, y);
        }

        @Override
        public boolean mayPickup(Player player) {
            return false;
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }

        @Override
        public Identifier getNoItemIcon() {
            return EMPTY_MUG_SLOT;
        }
    }
}
