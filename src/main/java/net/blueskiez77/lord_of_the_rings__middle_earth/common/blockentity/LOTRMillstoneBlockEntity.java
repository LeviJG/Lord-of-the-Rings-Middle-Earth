package net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRMillstoneBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory.LOTRMillstoneMenu;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.recipe.LOTRMillstoneRecipes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import org.jspecify.annotations.Nullable;

/**
 * LOTRTileEntityMillstone. A redstone-driven grinder: two slots and one timer,
 * and no fuel at all.
 *
 * <p>What makes it unlike a furnace is that the player does not start it. It
 * runs for exactly as long as the block is receiving redstone power, and while
 * it runs it grinds whatever is in the input slot once every
 * {@value #MILL_DURATION} ticks. Cut the power and the part-ground item resets
 * to zero progress -- the original discards {@code currentMillTime} on both
 * edges rather than banking it.
 *
 * <p>Milling can also fail: {@link LOTRMillstoneRecipes.Result#chance} is rolled
 * per grind, and the input is consumed whether or not the roll succeeds. That is
 * how cobblestone-to-gravel loses a quarter of its input and gravel-to-flint
 * three quarters of its own.
 */
public class LOTRMillstoneBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer {

    public static final int SLOT_COUNT = 2;
    public static final int INPUT_SLOT = 0;
    public static final int OUTPUT_SLOT = 1;

    /** getMillProgressScaled divides by 200, and updateEntity fires at 200. */
    public static final int MILL_DURATION = 200;

    public static final int DATA_MILL_TIME = 0;
    public static final int DATA_MILLING = 1;
    public static final int DATA_COUNT = 2;

    /** Hoppers feed the input from any side and pull the output from below. */
    private static final int[] SLOTS_DOWN = {OUTPUT_SLOT};
    private static final int[] SLOTS_SIDE = {INPUT_SLOT};

    private NonNullList<ItemStack> items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);

    private boolean milling;
    private int currentMillTime;

    private final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case DATA_MILL_TIME -> currentMillTime;
                case DATA_MILLING -> milling ? 1 : 0;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case DATA_MILL_TIME -> currentMillTime = value;
                case DATA_MILLING -> milling = value > 0;
                default -> {
                }
            }
        }

        @Override
        public int getCount() {
            return DATA_COUNT;
        }
    };

    public LOTRMillstoneBlockEntity(BlockPos pos, BlockState state) {
        super(LOTRBlockEntities.MILLSTONE, pos, state);
    }

    // ---- ticking ----------------------------------------------------------

    /**
     * updateEntity. Note the two power edges toggle the block's ACTIVE state,
     * which is what swaps in the animated running texture and starts the smoke;
     * the milling itself only happens while there is something grindable.
     */
    public static void serverTick(ServerLevel level, BlockPos pos, BlockState state,
                                  LOTRMillstoneBlockEntity millstone) {
        boolean changed = false;
        boolean powered = level.hasNeighborSignal(pos);

        if (powered != millstone.milling) {
            millstone.milling = powered;
            millstone.currentMillTime = 0;
            changed = true;
            level.setBlock(pos, state.setValue(LOTRMillstoneBlock.ACTIVE, powered), Block.UPDATE_ALL);
        }

        if (millstone.milling && millstone.canMill()) {
            ++millstone.currentMillTime;
            if (millstone.currentMillTime == MILL_DURATION) {
                millstone.currentMillTime = 0;
                millstone.millItem();
                changed = true;
            }
        } else if (millstone.currentMillTime != 0) {
            millstone.currentMillTime = 0;
            changed = true;
        }

        if (changed) {
            millstone.setChanged();
        }
    }

    /**
     * canMill. There must be an input with a recipe, and room in the output for
     * what that recipe would make.
     */
    private boolean canMill() {
        LOTRMillstoneRecipes.Result result = LOTRMillstoneRecipes.getMillingResult(items.get(INPUT_SLOT));
        if (result == null) {
            return false;
        }

        ItemStack output = items.get(OUTPUT_SLOT);
        if (output.isEmpty()) {
            return true;
        }
        if (!ItemStack.isSameItemSameComponents(output, result.resultItem())) {
            return false;
        }

        int total = output.getCount() + result.resultItem().getCount();
        return total <= getMaxStackSize() && total <= output.getMaxStackSize();
    }

    /**
     * millItem. The input is spent either way -- the chance roll only decides
     * whether anything comes out the other side.
     */
    private void millItem() {
        LOTRMillstoneRecipes.Result result = LOTRMillstoneRecipes.getMillingResult(items.get(INPUT_SLOT));
        if (result == null || level == null) {
            return;
        }

        if (level.getRandom().nextFloat() < result.chance()) {
            ItemStack output = items.get(OUTPUT_SLOT);
            if (output.isEmpty()) {
                items.set(OUTPUT_SLOT, result.resultItem().copy());
            } else {
                output.grow(result.resultItem().getCount());
            }
        }

        items.get(INPUT_SLOT).shrink(1);
    }

    // ---- container --------------------------------------------------------

    @Override
    public int getContainerSize() {
        return SLOT_COUNT;
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.lotr.millstone");
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new LOTRMillstoneMenu(containerId, inventory, this, dataAccess);
    }

    /** isItemValidForSlot: only the input slot, and only things it can grind. */
    @Override
    public boolean canPlaceItem(int slot, ItemStack itemstack) {
        return slot == INPUT_SLOT && LOTRMillstoneRecipes.canMill(itemstack);
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return side == Direction.DOWN ? SLOTS_DOWN : SLOTS_SIDE;
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack itemstack, @Nullable Direction side) {
        return canPlaceItem(slot, itemstack);
    }

    /** canExtractItem returned true unconditionally in the original. */
    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack itemstack, Direction side) {
        return true;
    }

    // ---- persistence ------------------------------------------------------

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(input, items);
        milling = input.getBooleanOr("Milling", false);
        currentMillTime = input.getIntOr("MillTime", 0);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        ContainerHelper.saveAllItems(output, items);
        output.putBoolean("Milling", milling);
        output.putInt("MillTime", currentMillTime);
    }
}
