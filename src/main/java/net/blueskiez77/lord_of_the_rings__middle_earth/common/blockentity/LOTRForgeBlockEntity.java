package net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity;

import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.util.Mth;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.Direction;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBuildingBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRFoodBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRUtilityBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory.LOTRForgeMenu;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRFoodItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMaterialItems;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipePropertySet;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.FuelValues;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import org.jspecify.annotations.Nullable;

// The forge: thirteen slots, four parallel smelt lanes, one shared fuel slot.
//
// This CANNOT extend AbstractFurnaceBlockEntity -- that class is fixed at three
// slots and four data values, and AbstractFurnaceMenu hard-calls
// checkContainerSize(container, 3) / checkContainerDataCount(data, 4). So the
// tick loop is ported from LOTRTileEntityForgeBase and
// LOTRTileEntityAlloyForgeBase, with the 26.2 idioms taken verbatim from
// AbstractFurnaceBlockEntity.serverTick.
//
//   slots 0-3   alloy   (lane i pairs alloy i with input i+4)
//   slots 4-7   input
//   slots 8-11  output
//   slot  12    fuel
//
// The four lanes advance on ONE shared timer, exactly as in 1.7.10: the forge
// burns while any lane can smelt, and when the timer reaches 200 every eligible
// lane produces at once. That is why the cook time is the original's flat 200
// rather than the recipe's cookingTime() -- one timer cannot honour four
// different per-recipe durations.
//
// One block entity serves all four forges. What differed between
// LOTRTileEntityDwarvenForge, ElvenForge, OrcForge and AlloyForge was only
// their alloy pairs and a few special smelts, so those are chosen here by the
// forge block this entity sits in.
//
// Like the original, a forge is not a general furnace: it only smelts stone,
// sand and clay blocks, the clay items, and wood (getSmeltingResult's
// Material test, here the pickaxe and shovel tags).
//
// A BaseContainerBlockEntity for the custom name (an anvil-named forge keeps
// its name, as setSpecialForgeName did) and a WorldlyContainer for
// getAccessibleSlotsFromSide's hopper rules.
public class LOTRForgeBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer {

    public static final int SLOT_COUNT = 13;
    public static final int ALLOY_START = 0;
    public static final int INPUT_START = 4;
    public static final int OUTPUT_START = 8;
    public static final int FUEL_SLOT = 12;
    public static final int LANES = 4;

    /** LOTRTileEntityAlloyForgeBase.getSmeltingDuration(). */
    public static final int SMELT_DURATION = 200;

    public static final int DATA_COOKING = 0;
    public static final int DATA_LIT = 1;
    public static final int DATA_LIT_TOTAL = 2;
    public static final int DATA_COUNT = 3;

    private final NonNullList<ItemStack> items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);

    /** currentSmeltTime */
    private int cookingTimer;
    /** forgeSmeltTime -- ticks of fuel left */
    private int litTimeRemaining;
    /** currentItemFuelValue -- burn time of the fuel currently alight */
    private int litTotalTime;

    /**
     * Smelting experience waiting in the output slots. SlotFurnace paid it out
     * by the output item as it was taken; here each smelt adds its share and
     * the output slot pays the total (see ResultSlot).
     */
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

    public LOTRForgeBlockEntity(BlockPos pos, BlockState state) {
        super(LOTRBlockEntities.FORGE, pos, state);
    }

    // ---------------------------------------------------------------- ticking

    public static void serverTick(ServerLevel level, BlockPos pos, BlockState state, LOTRForgeBlockEntity forge) {
        boolean changed = false;
        boolean wasLit = forge.litTimeRemaining > 0;

        if (forge.litTimeRemaining > 0) {
            --forge.litTimeRemaining;
        }

        boolean isLit = forge.litTimeRemaining > 0;

        if (!isLit && forge.canDoSmelting(level)) {
            ItemStack fuel = forge.items.get(FUEL_SLOT);
            int burn = forge.getBurnDuration(level.fuelValues(), fuel);
            if (burn > 0) {
                forge.litTimeRemaining = burn;
                forge.litTotalTime = burn;
                isLit = true;
                changed = true;
                consumeFuel(forge.items, fuel);
            }
        }

        if (isLit && forge.canDoSmelting(level)) {
            ++forge.cookingTimer;
            if (forge.cookingTimer >= SMELT_DURATION) {
                forge.cookingTimer = 0;
                for (int lane = 0; lane < LANES; ++lane) {
                    forge.smeltLane(level, lane);
                }
                changed = true;
            }
        } else {
            forge.cookingTimer = 0;
        }

        if (wasLit != isLit) {
            changed = true;
            state = state.setValue(AbstractFurnaceBlock.LIT, isLit);
            level.setBlock(pos, state, 3);
        }

        if (changed) {
            setChanged(level, pos, state);
        }
    }

    // AbstractFurnaceBlockEntity.consumeFuel verbatim, only the slot differs.
    // getCraftingRemainder() returns an ItemStackTemplate and may be null.
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

    private boolean canDoSmelting(ServerLevel level) {
        for (int lane = 0; lane < LANES; ++lane) {
            if (canSmelt(level, lane)) {
                return true;
            }
        }
        return false;
    }

    private boolean canSmelt(ServerLevel level, int lane) {
        ItemStack input = items.get(INPUT_START + lane);
        if (input.isEmpty()) {
            return false;
        }
        ItemStack output = items.get(OUTPUT_START + lane);

        ItemStack alloy = getAlloyResult(input, items.get(ALLOY_START + lane));
        if (!alloy.isEmpty() && canBurn(output, alloy)) {
            return true;
        }

        ItemStack result = getSmeltingResult(level, input);
        return !result.isEmpty() && canBurn(output, result);
    }

    // AbstractFurnaceBlockEntity.canBurn, against an explicit output stack.
    private boolean canBurn(ItemStack output, ItemStack result) {
        if (output.isEmpty()) {
            return true;
        }
        if (!ItemStack.isSameItemSameComponents(output, result)) {
            return false;
        }
        int total = output.getCount() + result.getCount();
        return total <= Math.min(getMaxStackSize(), result.getMaxStackSize());
    }

    private void smeltLane(ServerLevel level, int lane) {
        int inputSlot = INPUT_START + lane;
        int alloySlot = ALLOY_START + lane;
        int outputSlot = OUTPUT_START + lane;

        ItemStack input = items.get(inputSlot);
        if (input.isEmpty()) {
            return;
        }
        ItemStack alloyItem = items.get(alloySlot);
        ItemStack output = items.get(outputSlot);

        ItemStack alloyResult = getAlloyResult(input, alloyItem);
        if (!alloyResult.isEmpty() && canBurn(output, alloyResult)) {
            storedExperience += experienceFor(level, input, alloyResult);
            grow(outputSlot, alloyResult);
            input.shrink(1);
            alloyItem.shrink(1);
            return;
        }

        ItemStack result = getSmeltingResult(level, input);
        if (!result.isEmpty() && canBurn(output, result)) {
            storedExperience += experienceFor(level, input, result);
            grow(outputSlot, result);
            input.shrink(1);
        }
    }

    private void grow(int slot, ItemStack result) {
        ItemStack output = items.get(slot);
        if (output.isEmpty()) {
            items.set(slot, result.copy());
        } else {
            output.grow(result.getCount());
        }
    }

    /**
     * FurnaceRecipes.getSmeltingExperience for what came out: the furnace
     * recipe's own experience when this was an ordinary smelt, or the values
     * LOTRRecipes.addSmeltingXPForItem gave the mod's alloys and forge-only
     * results.
     */
    private float experienceFor(ServerLevel level, ItemStack input, ItemStack result) {
        Float special = LOTR_SMELT_XP.get(result.getItem());
        if (special != null) {
            return special;
        }
        return quickCheck.getRecipeFor(new SingleRecipeInput(input), level)
                .filter(recipe -> ItemStack.isSameItem(recipe.value().assemble(new SingleRecipeInput(input)), result))
                .map(recipe -> recipe.value().experience())
                .orElse(0.0F);
    }

    private static final java.util.Map<Item, Float> LOTR_SMELT_XP = java.util.Map.ofEntries(
            java.util.Map.entry(LOTRMaterialItems.BRONZE_INGOT, 0.7F),
            java.util.Map.entry(LOTRItems.MITHRIL, 1.0F),
            java.util.Map.entry(LOTRMaterialItems.ORC_STEEL_INGOT, 0.7F),
            java.util.Map.entry(LOTRMaterialItems.DWARVEN_STEEL_INGOT, 0.7F),
            java.util.Map.entry(LOTRMaterialItems.GALVORN_INGOT, 0.8F),
            java.util.Map.entry(LOTRMaterialItems.URUK_STEEL_INGOT, 0.7F),
            java.util.Map.entry(LOTRMaterialItems.MORGUL_STEEL_INGOT, 0.8F),
            java.util.Map.entry(LOTRMaterialItems.BLUE_DWARVEN_STEEL_INGOT, 0.7F),
            java.util.Map.entry(LOTRMaterialItems.BLACK_URUK_STEEL_INGOT, 0.7F),
            java.util.Map.entry(LOTRMaterialItems.ELVEN_STEEL_INGOT, 0.7F),
            java.util.Map.entry(LOTRMaterialItems.ITHILDIN, 0.8F),
            java.util.Map.entry(LOTRMaterialItems.GILDED_IRON_INGOT, 0.7F));

    /**
     * SlotFurnace.onCrafting's payout: the whole part of the experience, plus
     * one more with the fractional part as its chance, dropped at the player.
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

    // ------------------------------------------------------------- recipe API

    /**
     * getAlloySmeltingResult: an input in the lower slot and an alloy in the
     * slot above it. Each forge's own pairs come first, then the bronze every
     * forge could make.
     */
    protected ItemStack getAlloyResult(ItemStack input, ItemStack alloyItem) {
        if (input.isEmpty() || alloyItem.isEmpty()) {
            return ItemStack.EMPTY;
        }
        Block forge = getBlockState().getBlock();
        if (forge == LOTRUtilityBlocks.ORC_FORGE) {
            if (isIron(input) && isCoal(alloyItem)) {
                return new ItemStack(LOTRMaterialItems.URUK_STEEL_INGOT);
            }
            if (isOrcSteel(input) && alloyItem.is(LOTRMaterialItems.GULDURIL)) {
                return new ItemStack(LOTRMaterialItems.MORGUL_STEEL_INGOT);
            }
            if (isOrcSteel(input) && alloyItem.is(LOTRMaterialItems.DURNOR)) {
                return new ItemStack(LOTRMaterialItems.BLACK_URUK_STEEL_INGOT);
            }
        } else if (forge == LOTRUtilityBlocks.DWARVEN_FORGE) {
            if (isIron(input) && isCoal(alloyItem)) {
                return new ItemStack(LOTRMaterialItems.DWARVEN_STEEL_INGOT);
            }
            if (isIron(input) && alloyItem.is(LOTRMaterialItems.EDHELVIR)) {
                return new ItemStack(LOTRMaterialItems.GALVORN_INGOT);
            }
            if (isIron(input) && alloyItem.is(LOTRBuildingBlocks.BLUE_ROCK.asItem())) {
                return new ItemStack(LOTRMaterialItems.BLUE_DWARVEN_STEEL_INGOT);
            }
        } else if (forge == LOTRUtilityBlocks.ELVEN_FORGE) {
            if (isIron(input) && isCoal(alloyItem)) {
                return new ItemStack(LOTRMaterialItems.ELVEN_STEEL_INGOT);
            }
            if (isSilver(input) && alloyItem.is(LOTRMaterialItems.MITHRIL_NUGGET)) {
                return new ItemStack(LOTRMaterialItems.ITHILDIN);
            }
        } else if (forge == LOTRUtilityBlocks.ALLOY_FORGE) {
            if (isIron(input) && alloyItem.is(Items.GOLD_NUGGET)) {
                return new ItemStack(LOTRMaterialItems.GILDED_IRON_INGOT);
            }
        }
        if (isCopper(input) && isTin(alloyItem) || isTin(input) && isCopper(alloyItem)) {
            return new ItemStack(LOTRMaterialItems.BRONZE_INGOT, 2);
        }
        return ItemStack.EMPTY;
    }

    /**
     * The smelts only one forge knew: the orc forge chars wood, makes orc steel
     * of Morgul iron and spoils meat; the dwarven forge alone smelts mithril.
     */
    private ItemStack getForgeSmeltingResult(ItemStack input) {
        Block forge = getBlockState().getBlock();
        if (forge == LOTRUtilityBlocks.ORC_FORGE) {
            if (input.is(ItemTags.LOGS) && !input.is(LOTRBuildingBlocks.CHARRED_LOG.asItem())) {
                return new ItemStack(LOTRBuildingBlocks.CHARRED_LOG);
            }
            if (input.is(LOTRBuildingBlocks.MORGUL_IRON_ORE.asItem())) {
                return new ItemStack(LOTRMaterialItems.ORC_STEEL_INGOT);
            }
            // isWolfsFavoriteMeat: whatever a wolf eats, the mod's own meats included.
            if (input.is(ItemTags.WOLF_FOOD)) {
                return new ItemStack(Items.ROTTEN_FLESH);
            }
        } else if (forge == LOTRUtilityBlocks.DWARVEN_FORGE) {
            if (input.is(LOTRBuildingBlocks.MITHRIL_ORE.asItem())) {
                return new ItemStack(LOTRItems.MITHRIL);
            }
        }
        return ItemStack.EMPTY;
    }

    protected ItemStack getSmeltingResult(ServerLevel level, ItemStack input) {
        if (input.isEmpty()) {
            return ItemStack.EMPTY;
        }
        ItemStack special = getForgeSmeltingResult(input);
        if (!special.isEmpty()) {
            return special;
        }
        if (!isForgeMaterial(input)) {
            return ItemStack.EMPTY;
        }
        SingleRecipeInput recipeInput = new SingleRecipeInput(input);
        RecipeHolder<? extends AbstractCookingRecipe> recipe =
                quickCheck.getRecipeFor(recipeInput, level).orElse(null);
        return recipe == null ? ItemStack.EMPTY : recipe.value().assemble(recipeInput);
    }

    /**
     * LOTRTileEntityAlloyForgeBase.getSmeltingResult's filter: blocks of rock,
     * sand or clay, the clay balls and unfired clay vessels, and wood. Raw ore
     * items are what 1.7.10's ore blocks became, so they count as rock.
     */
    private static boolean isForgeMaterial(ItemStack stack) {
        if (stack.is(ItemTags.LOGS)) {
            return true;
        }
        if (stack.getItem() instanceof BlockItem blockItem) {
            BlockState state = blockItem.getBlock().defaultBlockState();
            return state.is(BlockTags.MINEABLE_WITH_PICKAXE) || state.is(BlockTags.MINEABLE_WITH_SHOVEL);
        }
        return stack.is(Items.CLAY_BALL) || stack.is(Items.RAW_IRON) || stack.is(Items.RAW_COPPER)
                || stack.is(Items.RAW_GOLD) || stack.is(LOTRFoodBlocks.STONEWARE_PLATE.asItem()) || stack.is(LOTRFoodItems.CLAY_MUG) || stack.is(LOTRFoodItems.CLAY_PLATE) || stack.is(LOTRMaterialItems.RED_CLAY_BALL);
    }

    private static boolean isCoal(ItemStack stack) {
        return stack.is(Items.COAL) || stack.is(Items.CHARCOAL);
    }

    private static boolean isCopper(ItemStack stack) {
        return stack.is(Items.COPPER_INGOT) || stack.is(ItemTags.COPPER_ORES) || stack.is(Items.RAW_COPPER);
    }

    private static boolean isIron(ItemStack stack) {
        return stack.is(Items.IRON_INGOT) || stack.is(ItemTags.IRON_ORES) || stack.is(Items.RAW_IRON);
    }

    private static boolean isTin(ItemStack stack) {
        return stack.is(LOTRMaterialItems.TIN_INGOT) || stack.is(LOTRBuildingBlocks.TIN_ORE.asItem());
    }

    private static boolean isSilver(ItemStack stack) {
        return stack.is(LOTRMaterialItems.SILVER_INGOT) || stack.is(LOTRBuildingBlocks.SILVER_ORE.asItem());
    }

    private static boolean isOrcSteel(ItemStack stack) {
        return stack.is(LOTRMaterialItems.ORC_STEEL_INGOT) || stack.is(LOTRBuildingBlocks.MORGUL_IRON_ORE.asItem());
    }

    /**
     * canMachineInsertInput: getSmeltingResult(stack) != null. An ingot that
     * only ever alloys (iron, tin, silver) has no smelting result of its own,
     * so neither hoppers nor shift-click put it in an input slot -- the player
     * places it by hand. Client-safe: it asks the synced furnace input set.
     */
    public boolean hasSmeltingResult(ItemStack stack) {
        if (stack.isEmpty() || level == null) {
            return false;
        }
        if (!getForgeSmeltingResult(stack).isEmpty()) {
            return true;
        }
        return isForgeMaterial(stack) && level.recipeAccess().propertySet(RecipePropertySet.FURNACE_INPUT).test(stack);
    }

    public boolean isFuel(ItemStack stack) {
        return level != null && level.fuelValues().isFuel(stack);
    }

    // ------------------------------------------------------------ persistence

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        items.clear();
        ContainerHelper.loadAllItems(input, items);
        cookingTimer = input.getShortOr("cooking_time_spent", (short) 0);
        litTimeRemaining = input.getShortOr("lit_time_remaining", (short) 0);
        litTotalTime = input.getShortOr("lit_total_time", (short) 0);
        storedExperience = input.getFloatOr("stored_experience", 0.0F);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putShort("cooking_time_spent", (short) cookingTimer);
        output.putShort("lit_time_remaining", (short) litTimeRemaining);
        output.putShort("lit_total_time", (short) litTotalTime);
        output.putFloat("stored_experience", storedExperience);
        ContainerHelper.saveAllItems(output, items);
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
     * isItemValidForSlot, which hoppers (canInsertItem) go through: input
     * slots take what the forge can smelt, the fuel slot takes fuel, and
     * nothing is ever put into an alloy or output slot this way.
     */
    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if (slot >= INPUT_START && slot < OUTPUT_START) {
            return hasSmeltingResult(stack);
        }
        if (slot == FUEL_SLOT) {
            return isFuel(stack);
        }
        return false;
    }

    private static final int[] SLOTS_DOWN = {OUTPUT_START, OUTPUT_START + 1, OUTPUT_START + 2, OUTPUT_START + 3, FUEL_SLOT};
    private static final int[] SLOTS_SIDE = {FUEL_SLOT};

    /**
     * getAccessibleSlotsFromSide: outputs and fuel from below, fuel from the
     * sides, and from above the four inputs emptiest-first (LOTRSlotStackSize),
     * so a hopper spreads its load across the lanes.
     */
    @Override
    public int[] getSlotsForFace(Direction side) {
        if (side == Direction.DOWN) {
            return SLOTS_DOWN;
        }
        if (side == Direction.UP) {
            Integer[] inputs = {INPUT_START, INPUT_START + 1, INPUT_START + 2, INPUT_START + 3};
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

    /** "Dwarven Forge", "Elven Forge", ...: the block's own name. */
    @Override
    protected Component getDefaultName() {
        return getBlockState().getBlock().getName();
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new LOTRForgeMenu(containerId, inventory, this, dataAccess);
    }
}
