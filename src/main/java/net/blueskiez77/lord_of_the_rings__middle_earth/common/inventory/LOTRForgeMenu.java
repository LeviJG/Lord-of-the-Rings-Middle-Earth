package net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory;

import net.minecraft.world.item.crafting.RecipePropertySet;
import net.minecraft.server.level.ServerPlayer;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRForgeBlockEntity;

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

// The forge menu. Extends AbstractContainerMenu directly, NOT
// AbstractFurnaceMenu: that class hard-calls checkContainerSize(container, 3)
// and checkContainerDataCount(data, 4) in its constructor.
//
// Slot layout is LOTRContainerAlloyForge's, pixel for pixel:
//   0-3   alloy   x = 53 + i*18, y = 21
//   4-7   input   x = 53 + i*18, y = 39
//   8-11  output  x = 53 + i*18, y = 85   (FurnaceResultSlot)
//   12    fuel    x = 80,        y = 129
//   13-39 player inventory, 40-48 hotbar. addStandardInventorySlots(inv, 8, 151)
//         puts the three rows at 151/169/187 and the hotbar at 209, which is
//         exactly the original's y values.
//
// Note the fuel slot is a plain Slot with a mayPlace override, NOT
// FurnaceFuelSlot -- that class's constructor takes an AbstractFurnaceMenu,
// which this menu deliberately is not.
public class LOTRForgeMenu extends AbstractContainerMenu {

    public static final int SLOT_COUNT = LOTRForgeBlockEntity.SLOT_COUNT;
    private static final int CONTAINER_END = SLOT_COUNT;      // 13
    private static final int INV_START = CONTAINER_END;       // 13
    private static final int HOTBAR_START = INV_START + 27;   // 40
    private static final int HOTBAR_END = HOTBAR_START + 9;   // 49

    private final Container container;
    private final ContainerData data;

    /** Client-side constructor, used by the MenuType factory. */
    public LOTRForgeMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, new SimpleContainer(SLOT_COUNT),
                new SimpleContainerData(LOTRForgeBlockEntity.DATA_COUNT));
    }

    public LOTRForgeMenu(int containerId, Inventory inventory, Container container, ContainerData data) {
        super(LOTRMenus.FORGE, containerId);
        checkContainerSize(container, SLOT_COUNT);
        checkContainerDataCount(data, LOTRForgeBlockEntity.DATA_COUNT);
        this.container = container;
        this.data = data;

        for (int i = 0; i < 4; ++i) {
            addSlot(new Slot(container, LOTRForgeBlockEntity.ALLOY_START + i, 53 + i * 18, 21));
        }
        // LOTRContainerAlloyForge: plain Slots for alloy, input and fuel -- a
        // player may put anything there by hand -- and SlotFurnace outputs,
        // which pay out the smelting experience.
        for (int i = 0; i < 4; ++i) {
            addSlot(new Slot(container, LOTRForgeBlockEntity.INPUT_START + i, 53 + i * 18, 39));
        }
        for (int i = 0; i < 4; ++i) {
            addSlot(new FurnaceResultSlot(inventory.player, container,
                    LOTRForgeBlockEntity.OUTPUT_START + i, 53 + i * 18, 85) {
                @Override
                protected void checkTakeAchievements(ItemStack stack) {
                    super.checkTakeAchievements(stack);
                    if (inventory.player instanceof ServerPlayer player
                            && container instanceof LOTRForgeBlockEntity forge) {
                        forge.popExperience(player);
                    }
                }
            });
        }
        addSlot(new Slot(container, LOTRForgeBlockEntity.FUEL_SLOT, 80, 129));

        addStandardInventorySlots(inventory, 8, 151);
        addDataSlots(data);
    }

    @Override
    public boolean stillValid(Player player) {
        return container.stillValid(player);
    }

    /** 0..pixels for the upward smelt arrow. */
    public int getSmeltProgress(int pixels) {
        int cooking = data.get(LOTRForgeBlockEntity.DATA_COOKING);
        return Mth.clamp(cooking * pixels / LOTRForgeBlockEntity.SMELT_DURATION, 0, pixels);
    }

    /** 0..pixels for the flame. */
    public int getLitProgress(int pixels) {
        int total = data.get(LOTRForgeBlockEntity.DATA_LIT_TOTAL);
        if (total == 0) {
            total = LOTRForgeBlockEntity.SMELT_DURATION;
        }
        return Mth.clamp(data.get(LOTRForgeBlockEntity.DATA_LIT) * pixels / total, 0, pixels);
    }

    public boolean isLit() {
        return data.get(LOTRForgeBlockEntity.DATA_LIT) > 0;
    }

    // LOTRContainerAlloyForge.transferStackInSlot, untangled from its nested
    // ternary and with the ranges shifted to the 0-48 numbering above.
    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        ItemStack clicked = ItemStack.EMPTY;
        Slot slot = slots.get(slotIndex);
        if (slot == null || !slot.hasItem()) {
            return clicked;
        }
        ItemStack stack = slot.getItem();
        clicked = stack.copy();

        if (slotIndex >= LOTRForgeBlockEntity.OUTPUT_START && slotIndex < LOTRForgeBlockEntity.FUEL_SLOT) {
            // Output -> player, reversed so it fills the hotbar first.
            if (!moveItemStackTo(stack, INV_START, HOTBAR_END, true)) {
                return ItemStack.EMPTY;
            }
            slot.onQuickCraft(stack, clicked);
        } else if (slotIndex < CONTAINER_END) {
            // Alloy, input or fuel -> player.
            if (!moveItemStackTo(stack, INV_START, HOTBAR_END, false)) {
                return ItemStack.EMPTY;
            }
        } else if (hasSmeltingResult(player, stack)) {
            if (!moveItemStackTo(stack, LOTRForgeBlockEntity.INPUT_START,
                    LOTRForgeBlockEntity.OUTPUT_START, false)) {
                return ItemStack.EMPTY;
            }
        } else if (player.level().fuelValues().isFuel(stack)) {
            if (!moveItemStackTo(stack, LOTRForgeBlockEntity.FUEL_SLOT, CONTAINER_END, false)) {
                return ItemStack.EMPTY;
            }
        } else if (slotIndex < HOTBAR_START) {
            if (!moveItemStackTo(stack, HOTBAR_START, HOTBAR_END, false)) {
                return ItemStack.EMPTY;
            }
        } else if (!moveItemStackTo(stack, INV_START, HOTBAR_START, false)) {
            return ItemStack.EMPTY;
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

    /**
     * theForge.getSmeltingResult(stack) != null, asked of the real forge on the
     * server and of the synced furnace inputs on the client (whose container
     * is a stand-in that does not know which forge it is).
     */
    private boolean hasSmeltingResult(Player player, ItemStack stack) {
        if (container instanceof LOTRForgeBlockEntity forge) {
            return forge.hasSmeltingResult(stack);
        }
        return player.level().recipeAccess().propertySet(RecipePropertySet.FURNACE_INPUT).test(stack);
    }
}
