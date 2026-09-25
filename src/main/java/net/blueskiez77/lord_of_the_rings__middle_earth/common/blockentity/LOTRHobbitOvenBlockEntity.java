package net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity;

import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.util.Mth;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.Direction;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRDecorationBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRHobbitOvenBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory.LOTRHobbitOvenMenu;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItems;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.FuelValues;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import org.jspecify.annotations.Nullable;

// Hobbit oven: nineteen slots, nine parallel cook lanes on one shared timer.
//
//   slots 0-8    input
//   slots 9-17   output  (lane i feeds lane i+9)
//   slot  18     fuel
//
// Ported from LOTRTileEntityHobbitOven. Same shape as the forge, three
// differences worth knowing:
//
//  * Cook time is 400 ticks, not 200.
//  * It only cooks FOOD. isCookResultAcceptable gates on the RESULT, not the
//    input: the smelting result must be edible, or pipeweed, or dried reeds.
//    Put an iron ore in and nothing happens.
//  * The lit state is metadata bit 8 in the original, flipped by
//    setOvenActive. Here that is the LIT blockstate property, which also
//    carries the light level of 13.
//
// A BaseContainerBlockEntity for the custom name, and a WorldlyContainer for
// getAccessibleSlotsFromSide's hopper rules.
public class LOTRHobbitOvenBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer {

    public static final int SLOT_COUNT = 19;
    public static final int INPUT_START = 0;
    public static final int OUTPUT_START = 9;
    public static final int FUEL_SLOT = 18;
    public static final int LANES = 9;

    /** currentCookTime hits 400, not the furnace's 200. */
    public static final int COOK_DURATION = 400;

    public static final int DATA_COOKING = 0;
    public static final int DATA_LIT = 1;
    public static final int DATA_LIT_TOTAL = 2;
    public static final int DATA_COUNT = 3;

    private final NonNullList<ItemStack> items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);

    /** currentCookTime */
    private int cookingTimer;
    /** ovenCookTime -- ticks of fuel left */
    private int litTimeRemaining;
    /** currentItemFuelValue */
    private int litTotalTime;

    /** Cooking experience waiting in the outputs; SlotFurnace paid it as they were taken. */
    private float storedExperience;

    private final RecipeManager.CachedCheck<SingleRecipeInput, ? extends AbstractCookingRecipe> quickCheck =
            RecipeManager.createCheck(RecipeType.SMELTING);

    protected final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int dataId) {
            return switch (dataId) {
                case DATA_COOKING -> cookingTimer;
                case DATA_LIT -> litTimeRemaining;
                case DATA_LIT_TOTAL -> litTotalTime;
                default -> 0;
            };
        }

        @Override
        public void set(int dataId, int value) {
            switch (dataId) {
                case DATA_COOKING -> cookingTimer = value;
                case DATA_LIT -> litTimeRemaining = value;
                case DATA_LIT_TOTAL -> litTotalTime = value;
                default -> {
                }
            }
        }

        @Override
        public int getCount() {
            return DATA_COUNT;
        }
    };

    public LOTRHobbitOvenBlockEntity(BlockPos pos, BlockState state) {
        super(LOTRBlockEntities.HOBBIT_OVEN, pos, state);
    }

    // ---------------------------------------------------------- what it cooks

    /**
     * LOTRTileEntityHobbitOven.isCookResultAcceptable. The gate is on the
     * RESULT: food, pipeweed, or dried reeds. This is what stops the oven
     * being a second furnace.
     */
    public static boolean isCookResultAcceptable(ItemStack result) {
        if (result.isEmpty()) {
            return false;
        }
        if (result.has(DataComponents.FOOD)) {
            return true;
        }
        Item item = result.getItem();
        return item == LOTRItems.PIPEWEED || item == LOTRDecorationBlocks.DRIED_REEDS.asItem();
    }

    // ---------------------------------------------------------------- ticking

    public static void serverTick(ServerLevel level, BlockPos pos, BlockState state, LOTRHobbitOvenBlockEntity oven) {
        boolean changed = false;
        boolean wasLit = oven.litTimeRemaining > 0;

        if (oven.litTimeRemaining > 0) {
            --oven.litTimeRemaining;
        }
        boolean isLit = oven.litTimeRemaining > 0;

        if (!isLit && oven.canCookAnyItem(level)) {
            ItemStack fuel = oven.items.get(FUEL_SLOT);
            int burn = oven.getBurnDuration(level.fuelValues(), fuel);
            if (burn > 0) {
                oven.litTimeRemaining = burn;
                oven.litTotalTime = burn;
                isLit = true;
                changed = true;
                consumeFuel(oven.items, fuel);
            }
        }

        if (isLit && oven.canCookAnyItem(level)) {
            ++oven.cookingTimer;
            if (oven.cookingTimer >= COOK_DURATION) {
                oven.cookingTimer = 0;
                for (int lane = 0; lane < LANES; ++lane) {
                    oven.cookItem(level, lane);
                }
                changed = true;
            }
        } else {
            oven.cookingTimer = 0;
        }

        if (wasLit != isLit) {
            changed = true;
            // setOvenActive: metadata bit 8 in the original, LIT here.
            state = state.setValue(LOTRHobbitOvenBlock.LIT, isLit);
            level.setBlock(pos, state, Block.UPDATE_ALL);
        }

        if (changed) {
            setChanged(level, pos, state);
        }
    }

    private static void consumeFuel(NonNullList<ItemStack> items, ItemStack fuel) {
        if (fuel.isEmpty()) {
            return;
        }
        Item fuelItem = fuel.getItem();
        fuel.shrink(1);
        if (fuel.isEmpty()) {
            ItemStackTemplate remainder = fuelItem.getCraftingRemainder();
            items.set(FUEL_SLOT, remainder != null ? remainder.create() : ItemStack.EMPTY);
        }
    }

    protected int getBurnDuration(FuelValues fuelValues, ItemStack itemStack) {
        return itemStack.isEmpty() ? 0 : fuelValues.burnDuration(itemStack);
    }

    private boolean canCookAnyItem(ServerLevel level) {
        for (int lane = 0; lane < LANES; ++lane) {
            if (canCook(level, lane)) {
                return true;
            }
        }
        return false;
    }

    /** LOTRTileEntityHobbitOven.canCook(i). */
    private boolean canCook(ServerLevel level, int lane) {
        ItemStack input = items.get(INPUT_START + lane);
        if (input.isEmpty()) {
            return false;
        }
        ItemStack result = getCookResult(level, input);
        if (!isCookResultAcceptable(result)) {
            return false;
        }
        ItemStack output = items.get(OUTPUT_START + lane);
        if (output.isEmpty()) {
            return true;
        }
        if (!ItemStack.isSameItemSameComponents(output, result)) {
            return false;
        }
        int total = output.getCount() + result.getCount();
        return total <= getMaxStackSize() && total <= result.getMaxStackSize();
    }

    private void cookItem(ServerLevel level, int lane) {
        if (!canCook(level, lane)) {
            return;
        }
        ItemStack input = items.get(INPUT_START + lane);
        ItemStack result = getCookResult(level, input);
        storedExperience += quickCheck.getRecipeFor(new SingleRecipeInput(input), level)
                .map(recipe -> recipe.value().experience()).orElse(0.0F);
        ItemStack output = items.get(OUTPUT_START + lane);
        if (output.isEmpty()) {
            items.set(OUTPUT_START + lane, result.copy());
        } else {
            output.grow(result.getCount());
        }
        input.shrink(1);
    }

    private ItemStack getCookResult(ServerLevel level, ItemStack input) {
        if (input.isEmpty()) {
            return ItemStack.EMPTY;
        }
        SingleRecipeInput recipeInput = new SingleRecipeInput(input);
        RecipeHolder<? extends AbstractCookingRecipe> recipe =
                quickCheck.getRecipeFor(recipeInput, level).orElse(null);
        return recipe == null ? ItemStack.EMPTY : recipe.value().assemble(recipeInput);
    }

    public boolean isFuel(ItemStack stack) {
        return level != null && level.fuelValues().isFuel(stack);
    }

    /** isItemValidForSlot for the inputs: it would cook into something acceptable. */
    public boolean isCookable(ItemStack stack) {
        return level instanceof ServerLevel serverLevel && isCookResultAcceptable(getCookResult(serverLevel, stack));
    }

    /**
     * SlotFurnace.onCrafting's payout: the whole part of the stored
     * experience, plus one more with the fractional part as its chance.
     */
    public void popExperience(ServerPlayer player) {
        int whole = Mth.floor(storedExperience);
        float frac = Mth.frac(storedExperience);
        if (frac != 0.0F && player.getRandom().nextFloat() < frac) {
            ++whole;
        }
        storedExperience = 0.0F;
        if (whole > 0) {
            ExperienceOrb.award(player.level(), player.position(), whole);
        }
        setChanged();
    }

    // ------------------------------------------------------------ persistence

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        items.clear();
        ContainerHelper.loadAllItems(input, items);
        cookingTimer = input.getShortOr("CookTime", (short) 0);
        litTimeRemaining = input.getShortOr("BurnTime", (short) 0);
        litTotalTime = input.getShortOr("BurnTimeTotal", (short) 0);
        storedExperience = input.getFloatOr("StoredExperience", 0.0F);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        ContainerHelper.saveAllItems(output, items);
        output.putShort("CookTime", (short) cookingTimer);
        output.putShort("BurnTime", (short) litTimeRemaining);
        output.putShort("BurnTimeTotal", (short) litTotalTime);
        output.putFloat("StoredExperience", storedExperience);
    }

    // ------------------------------------------------- Container / hoppers

    @Override
    public int getContainerSize() {
        return SLOT_COUNT;
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> newItems) {
        for (int i = 0; i < SLOT_COUNT; ++i) {
            items.set(i, i < newItems.size() ? newItems.get(i) : ItemStack.EMPTY);
        }
    }

    /**
     * isItemValidForSlot, which hoppers go through: inputs take what would
     * cook into food, pipeweed or dried reeds, the fuel slot takes fuel, and the
     * outputs take nothing.
     */
    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if (slot < OUTPUT_START) {
            return isCookable(stack);
        }
        if (slot == FUEL_SLOT) {
            return isFuel(stack);
        }
        return false;
    }

    private static final int[] SLOTS_DOWN = {9, 10, 11, 12, 13, 14, 15, 16, 17, FUEL_SLOT};
    private static final int[] SLOTS_SIDE = {FUEL_SLOT};

    /**
     * getAccessibleSlotsFromSide: outputs and fuel from below, fuel from the
     * sides, the nine inputs from above emptiest-first (LOTRSlotStackSize).
     */
    @Override
    public int[] getSlotsForFace(Direction side) {
        if (side == Direction.DOWN) {
            return SLOTS_DOWN;
        }
        if (side == Direction.UP) {
            Integer[] inputs = {0, 1, 2, 3, 4, 5, 6, 7, 8};
            java.util.Arrays.sort(inputs, java.util.Comparator
                    .comparingInt((Integer slot) -> items.get(slot).getCount())
                    .thenComparingInt(slot -> slot));
            return java.util.Arrays.stream(inputs).mapToInt(Integer::intValue).toArray();
        }
        return SLOTS_SIDE;
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction side) {
        return canPlaceItem(slot, stack);
    }

    /** canExtractItem: from below, the fuel slot only gives up an empty bucket. */
    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction side) {
        return side != Direction.DOWN || slot != FUEL_SLOT || stack.is(Items.BUCKET);
    }

    // ----------------------------------------------------------- MenuProvider

    @Override
    protected Component getDefaultName() {
        return getBlockState().getBlock().getName();
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new LOTRHobbitOvenMenu(containerId, inventory, this, dataAccess);
    }
}
