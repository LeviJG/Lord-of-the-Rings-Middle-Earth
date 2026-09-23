package net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory.LOTRForgeMenu;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItems;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
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
import net.minecraft.world.level.block.entity.BlockEntity;
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
public class LOTRForgeBlockEntity extends BlockEntity implements Container, MenuProvider {

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

    private @Nullable Component customName;

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
            grow(outputSlot, alloyResult);
            input.shrink(1);
            alloyItem.shrink(1);
            return;
        }

        ItemStack result = getSmeltingResult(level, input);
        if (!result.isEmpty() && canBurn(output, result)) {
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
        if (forge == LOTRBlocks.ORC_FORGE) {
            if (isIron(input) && isCoal(alloyItem)) {
                return new ItemStack(LOTRItems.URUK_STEEL_INGOT);
            }
            if (isOrcSteel(input) && alloyItem.is(LOTRItems.GULDURIL)) {
                return new ItemStack(LOTRItems.MORGUL_STEEL_INGOT);
            }
            if (isOrcSteel(input) && alloyItem.is(LOTRItems.DURNOR)) {
                return new ItemStack(LOTRItems.BLACK_URUK_STEEL_INGOT);
            }
        } else if (forge == LOTRBlocks.DWARVEN_FORGE) {
            if (isIron(input) && isCoal(alloyItem)) {
                return new ItemStack(LOTRItems.DWARVEN_STEEL_INGOT);
            }
            if (isIron(input) && alloyItem.is(LOTRItems.EDHELVIR)) {
                return new ItemStack(LOTRItems.GALVORN_INGOT);
            }
            if (isIron(input) && alloyItem.is(LOTRBlocks.BLUE_ROCK.asItem())) {
                return new ItemStack(LOTRItems.BLUE_DWARVEN_STEEL_INGOT);
            }
        } else if (forge == LOTRBlocks.ELVEN_FORGE) {
            if (isIron(input) && isCoal(alloyItem)) {
                return new ItemStack(LOTRItems.ELVEN_STEEL_INGOT);
            }
            if (isSilver(input) && alloyItem.is(LOTRItems.MITHRIL_NUGGET)) {
                return new ItemStack(LOTRItems.ITHILDIN);
            }
        } else if (forge == LOTRBlocks.ALLOY_FORGE) {
            if (isIron(input) && alloyItem.is(Items.GOLD_NUGGET)) {
                return new ItemStack(LOTRItems.GILDED_IRON_INGOT);
            }
        }
        if (isCopper(input) && isTin(alloyItem) || isTin(input) && isCopper(alloyItem)) {
            return new ItemStack(LOTRItems.BRONZE_INGOT, 2);
        }
        return ItemStack.EMPTY;
    }

    /**
     * The smelts only one forge knew: the orc forge chars wood, makes orc steel
     * of Morgul iron and spoils meat; the dwarven forge alone smelts mithril.
     */
    private ItemStack getForgeSmeltingResult(ItemStack input) {
        Block forge = getBlockState().getBlock();
        if (forge == LOTRBlocks.ORC_FORGE) {
            if (input.is(ItemTags.LOGS) && !input.is(LOTRBlocks.CHARRED_LOG.asItem())) {
                return new ItemStack(LOTRBlocks.CHARRED_LOG);
            }
            if (input.is(LOTRBlocks.MORGUL_IRON_ORE.asItem())) {
                return new ItemStack(LOTRItems.ORC_STEEL_INGOT);
            }
            // isWolfsFavoriteMeat: whatever a wolf eats, the mod's own meats included.
            if (input.is(ItemTags.WOLF_FOOD)) {
                return new ItemStack(Items.ROTTEN_FLESH);
            }
        } else if (forge == LOTRBlocks.DWARVEN_FORGE) {
            if (input.is(LOTRBlocks.MITHRIL_ORE.asItem())) {
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
                || stack.is(Items.RAW_GOLD) || stack.is(LOTRBlocks.STONEWARE_PLATE.asItem()) || stack.is(LOTRItems.CLAY_MUG) || stack.is(LOTRItems.CLAY_PLATE) || stack.is(LOTRItems.RED_CLAY_BALL);
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
        return stack.is(LOTRItems.TIN_INGOT) || stack.is(LOTRBlocks.TIN_ORE.asItem());
    }

    private static boolean isSilver(ItemStack stack) {
        return stack.is(LOTRItems.SILVER_INGOT) || stack.is(LOTRBlocks.SILVER_ORE.asItem());
    }

    private static boolean isOrcSteel(ItemStack stack) {
        return stack.is(LOTRItems.ORC_STEEL_INGOT) || stack.is(LOTRBlocks.MORGUL_IRON_ORE.asItem());
    }

    /** Client-safe input test, for slot validation and quick-move. */
    public boolean isSmeltable(ItemStack stack) {
        if (stack.isEmpty() || level == null) {
            return false;
        }
        if (!getForgeSmeltingResult(stack).isEmpty()
                || isIron(stack) || isCopper(stack) || isTin(stack) || isSilver(stack) || isOrcSteel(stack)) {
            return true;
        }
        return isForgeMaterial(stack) && level.recipeAccess().propertySet(RecipePropertySet.FURNACE_INPUT).test(stack);
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
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putShort("cooking_time_spent", (short) cookingTimer);
        output.putShort("lit_time_remaining", (short) litTimeRemaining);
        output.putShort("lit_total_time", (short) litTotalTime);
        ContainerHelper.saveAllItems(output, items);
    }

    // -------------------------------------------------------------- Container

    @Override
    public int getContainerSize() {
        return SLOT_COUNT;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : items) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return items.get(slot);
    }

    // Written out rather than delegated, so nothing here depends on a
    // ContainerHelper overload I have not seen.
    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack stack = items.get(slot);
        if (stack.isEmpty() || amount <= 0) {
            return ItemStack.EMPTY;
        }
        ItemStack split = stack.split(amount);
        if (!split.isEmpty()) {
            setChanged();
        }
        return split;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack stack = items.get(slot);
        items.set(slot, ItemStack.EMPTY);
        return stack;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        items.set(slot, stack);
        stack.limitSize(getMaxStackSize(stack));
        setChanged();
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if (slot >= OUTPUT_START && slot < FUEL_SLOT) {
            return false;
        }
        if (slot == FUEL_SLOT) {
            // Vanilla's rule: fuel, or an empty bucket to catch the lava one.
            ItemStack fuelSlot = items.get(FUEL_SLOT);
            return (level != null && level.fuelValues().isFuel(stack))
                    || (stack.is(Items.BUCKET) && !fuelSlot.is(Items.BUCKET));
        }
        if (slot >= INPUT_START) {
            return isSmeltable(stack);
        }
        // Alloy slots take anything; the recipe hook decides what pairs.
        return true;
    }

    // LOTRTileEntityForgeBase.isUseableByPlayer, written out to avoid depending
    // on a Container static helper I have not seen the signature of.
    @Override
    public boolean stillValid(Player player) {
        return level != null
                && level.getBlockEntity(worldPosition) == this
                && player.distanceToSqr(worldPosition.getX() + 0.5,
                worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5) <= 64.0;
    }

    @Override
    public void clearContent() {
        items.clear();
    }

    /** For Containers.dropContents when the block breaks. */
    public NonNullList<ItemStack> getItems() {
        return items;
    }

    // ----------------------------------------------------------- MenuProvider

    public void setCustomName(Component name) {
        this.customName = name;
    }

    @Override
    public Component getDisplayName() {
        if (customName != null) {
            return customName;
        }
        // "Dwarven Forge" / "Elven Forge" / ... straight off the block, so the
        // four forges need no container.* lang keys of their own.
        return getBlockState().getBlock().getName();
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new LOTRForgeMenu(containerId, inventory, this, dataAccess);
    }
}