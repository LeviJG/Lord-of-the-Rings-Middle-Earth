package net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity;

import java.util.HashMap;
import java.util.Map;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory.LOTRUnsmelteryMenu;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.NormalCraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.enchantment.Repairable;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.FuelValues;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import org.jspecify.annotations.Nullable;

/**
 * LOTRTileEntityUnsmeltery. A furnace run backwards: feed it a tool, a weapon
 * or a piece of armour and it gives you back some of the metal it was made
 * from.
 *
 * <p>Three slots and one timer, like a vanilla furnace -- input 0, fuel 1,
 * output 2 -- but the "recipe" is computed rather than looked up:
 *
 * <ol>
 *   <li>What is this item repaired with? That is its material.</li>
 *   <li>How many of that material does its crafting recipe use? That is the
 *       most you could ever get back.</li>
 *   <li>Scale that down: 80% always, then by how undamaged the item is, then by
 *       a random 70-100%. So a pristine iron sword made from one ingot usually
 *       returns nothing, and a full set of armour returns a useful handful.</li>
 * </ol>
 *
 * <p>The 400-tick duration is twice a furnace's -- unsmelting is slow work.
 */
public class LOTRUnsmelteryBlockEntity extends BlockEntity implements Container, MenuProvider {

    public static final int SLOT_COUNT = 3;
    public static final int INPUT_SLOT = 0;
    public static final int FUEL_SLOT = 1;
    public static final int OUTPUT_SLOT = 2;

    /** LOTRTileEntityUnsmeltery.getSmeltingDuration(). */
    public static final int SMELT_DURATION = 400;

    /** getRandomUnsmeltingResult: you never get all of it back. */
    private static final float YIELD = 0.8f;
    private static final float YIELD_RANDOM_MIN = 0.7f;

    public static final int DATA_COOKING = 0;
    public static final int DATA_LIT = 1;
    public static final int DATA_LIT_TOTAL = 2;
    public static final int DATA_COUNT = 3;

    /**
     * unsmeltableCraftingCounts. Walking the recipe list is not cheap and the
     * answer never changes within a session, so it is worked out once per
     * (item, material) pair. Cleared on datapack reload by
     * {@link #clearRecipeCache()}.
     */
    private static final Map<ItemPair, Integer> RESOURCE_COUNTS = new HashMap<>();

    private record ItemPair(Item item, Item material) {
    }

    private final NonNullList<ItemStack> items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);

    private int cookingProgress;
    private int litTime;
    private int litTotalTime;

    private @Nullable Component customName;

    private final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case DATA_COOKING -> cookingProgress;
                case DATA_LIT -> litTime;
                case DATA_LIT_TOTAL -> litTotalTime;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case DATA_COOKING -> cookingProgress = value;
                case DATA_LIT -> litTime = value;
                case DATA_LIT_TOTAL -> litTotalTime = value;
                default -> { }
            }
        }

        @Override
        public int getCount() {
            return DATA_COUNT;
        }
    };

    public LOTRUnsmelteryBlockEntity(BlockPos pos, BlockState state) {
        super(LOTRBlockEntities.UNSMELTERY, pos, state);
    }

    public static void clearRecipeCache() {
        RESOURCE_COUNTS.clear();
    }

    // ------------------------------------------------------------ unsmelting

    /**
     * getEquipmentMaterial. The original branched over ItemTool, ItemSword,
     * ItemArmor, LOTRItemCrossbow, LOTRItemThrowingAxe, LOTRItemMountArmor and
     * then a hand-written list of odds and ends (buckets, rings, goblets).
     *
     * <p>All of that collapses into one component now: an item that can be
     * repaired carries {@link DataComponents#REPAIRABLE}, listing what repairs
     * it. That covers every vanilla tool and armour piece AND anything the port
     * adds later, with no list to maintain.
     */
    public static ItemStack getEquipmentMaterial(ItemStack stack) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        Repairable repairable = stack.get(DataComponents.REPAIRABLE);
        if (repairable == null) {
            return ItemStack.EMPTY;
        }
        // The first entry is the canonical one -- iron ingots for iron gear.
        for (Holder<Item> holder : repairable.items()) {
            return new ItemStack(holder.value());
        }
        return ItemStack.EMPTY;
    }

    /**
     * canBeUnsmelted. Refuses anything whose material would BURN -- the
     * original checked getItemBurnTime on the material, so a wooden sword
     * cannot be turned back into planks by a fire that would consume them.
     */
    public boolean canBeUnsmelted(ItemStack stack) {
        if (level == null || stack.isEmpty()) {
            return false;
        }
        ItemStack material = getEquipmentMaterial(stack);
        if (material.isEmpty()) {
            return false;
        }
        FuelValues fuel = level.fuelValues();
        if (fuel.isFuel(material)) {
            return false;
        }
        return resourcesUsed(stack, material) > 0;
    }

    /**
     * determineResourcesUsed. How many of {@code material} the item's crafting
     * recipe consumes -- an iron chestplate is eight ingots, an iron sword two
     * (one ingot, one stick, of which one matches).
     *
     * <p>SIMPLIFIED from the original, which also recursed into sub-recipes so
     * that an item crafted from iron BLOCKS counted nine ingots each. This
     * counts only direct ingredient matches. See docs/TODO-unsmeltery.md.
     */
    public int resourcesUsed(ItemStack stack, ItemStack material) {
        if (level == null || stack.isEmpty() || material.isEmpty()) {
            return 0;
        }
        ItemPair key = new ItemPair(stack.getItem(), material.getItem());
        Integer cached = RESOURCE_COUNTS.get(key);
        if (cached != null) {
            return cached;
        }

        int best = 0;
        if (level instanceof ServerLevel serverLevel) {
            for (RecipeHolder<?> holder : serverLevel.recipeAccess().getRecipes()) {
                Recipe<?> recipe = holder.value();
                if (!(recipe instanceof NormalCraftingRecipe crafting)) {
                    continue;
                }
                ItemStack result = resultOf(crafting);
                if (result.isEmpty() || !result.is(stack.getItem())) {
                    continue;
                }
                int matches = 0;
                for (Ingredient ingredient : crafting.placementInfo().ingredients()) {
                    if (ingredient.test(material)) {
                        matches++;
                    }
                }
                // Per crafted item, not per recipe: a recipe making four of
                // something spreads its ingredients across all four.
                matches /= Math.max(1, result.getCount());
                best = Math.max(best, matches);
            }
        }
        RESOURCE_COUNTS.put(key, best);
        return best;
    }

    /**
     * 26.2 removed Recipe.getResultItem -- a recipe's output can depend on its
     * input now, so there is no context-free getter. Shaped and shapeless
     * recipes still ignore their input when assembling, so an empty one is
     * enough to read the result off them; anything that does not cooperate is
     * skipped rather than crashing the scan.
     */
    private static ItemStack resultOf(NormalCraftingRecipe recipe) {
        try {
            return recipe.assemble(CraftingInput.EMPTY);
        } catch (RuntimeException e) {
            return ItemStack.EMPTY;
        }
    }

    /** getLargestUnsmeltingResult: the ceiling, before wear and luck. */
    public ItemStack largestResult(ItemStack stack) {
        if (!canBeUnsmelted(stack)) {
            return ItemStack.EMPTY;
        }
        ItemStack material = getEquipmentMaterial(stack);
        return new ItemStack(material.getItem(), resourcesUsed(stack, material));
    }

    /**
     * getRandomUnsmeltingResult. Eighty percent, then scaled by remaining
     * durability, then a random 70-100%. A worn-out tool gives back nothing.
     */
    public ItemStack rollResult(ItemStack stack, RandomSource random) {
        ItemStack largest = largestResult(stack);
        if (largest.isEmpty()) {
            return ItemStack.EMPTY;
        }
        float count = largest.getCount() * YIELD;
        if (stack.isDamageableItem()) {
            count *= (float) (stack.getMaxDamage() - stack.getDamageValue()) / stack.getMaxDamage();
        }
        count *= Mth.randomBetween(random, YIELD_RANDOM_MIN, 1.0f);

        int rolled = Math.round(count);
        return rolled <= 0 ? ItemStack.EMPTY : new ItemStack(largest.getItem(), rolled);
    }

    // ---------------------------------------------------------------- ticking

    private boolean canSmelt() {
        ItemStack input = items.get(INPUT_SLOT);
        if (input.isEmpty()) {
            return false;
        }
        // Tested against the LARGEST result, not a fresh roll: whether the
        // machine can run must not depend on a die throw.
        ItemStack result = largestResult(input);
        if (result.isEmpty()) {
            return false;
        }
        ItemStack output = items.get(OUTPUT_SLOT);
        if (output.isEmpty()) {
            return true;
        }
        if (!ItemStack.isSameItemSameComponents(output, result)) {
            return false;
        }
        return output.getCount() + result.getCount() <= output.getMaxStackSize();
    }

    private void smelt(RandomSource random) {
        ItemStack input = items.get(INPUT_SLOT);
        ItemStack result = rollResult(input, random);

        if (!result.isEmpty()) {
            ItemStack output = items.get(OUTPUT_SLOT);
            if (output.isEmpty()) {
                items.set(OUTPUT_SLOT, result);
            } else if (ItemStack.isSameItemSameComponents(output, result)) {
                output.grow(result.getCount());
            }
        }
        // The input is consumed even when the roll came up empty -- the metal
        // is lost, which is the point of the 80% and the durability scaling.
        input.shrink(1);
    }

    public static void serverTick(ServerLevel level, BlockPos pos, BlockState state,
                                  LOTRUnsmelteryBlockEntity unsmeltery) {
        boolean wasLit = unsmeltery.isLit();
        boolean changed = false;

        if (unsmeltery.isLit()) {
            --unsmeltery.litTime;
        }

        ItemStack fuel = unsmeltery.items.get(FUEL_SLOT);
        boolean canSmelt = unsmeltery.canSmelt();

        if (!unsmeltery.isLit() && canSmelt && !fuel.isEmpty()) {
            int burn = level.fuelValues().burnDuration(fuel);
            if (burn > 0) {
                unsmeltery.litTime = burn;
                unsmeltery.litTotalTime = burn;
                // AbstractFurnaceBlockEntity.consumeFuel: getCraftingRemainder
                // hands back an ItemStackTemplate, and may be null.
                Item fuelItem = fuel.getItem();
                fuel.shrink(1);
                if (fuel.isEmpty()) {
                    ItemStackTemplate remainder = fuelItem.getCraftingRemainder();
                    unsmeltery.items.set(FUEL_SLOT,
                            remainder != null ? remainder.create() : ItemStack.EMPTY);
                }
                changed = true;
            }
        }

        if (unsmeltery.isLit() && canSmelt) {
            ++unsmeltery.cookingProgress;
            if (unsmeltery.cookingProgress >= SMELT_DURATION) {
                unsmeltery.cookingProgress = 0;
                unsmeltery.smelt(level.getRandom());
                changed = true;
            }
        } else {
            unsmeltery.cookingProgress = 0;
        }

        if (wasLit != unsmeltery.isLit()) {
            changed = true;
            state = state.setValue(AbstractFurnaceBlock.LIT, unsmeltery.isLit());
            level.setBlock(pos, state, 3);
        }
        if (changed) {
            unsmeltery.setChanged();
        }
    }

    public boolean isLit() {
        return litTime > 0;
    }

    // ---------------------------------------------------------------- rocking

    /** LOTRTileEntityUnsmeltery: the cauldron sways while it works. */
    private float rocking;
    private float prevRocking;
    private float rockingPhase = (float) (Math.random() * Math.PI * 2.0);
    private float prevRockingPhase;

    /**
     * The original synced an "Active" boolean of its own to drive this. It does
     * not need to: LIT already lives on the blockstate and reaches the client
     * for free, so the client reads that instead and one packet type goes away.
     */
    public static void clientTick(Level level, BlockPos pos, BlockState state,
                                  LOTRUnsmelteryBlockEntity unsmeltery) {
        unsmeltery.prevRocking = unsmeltery.rocking;
        unsmeltery.prevRockingPhase = unsmeltery.rockingPhase;
        unsmeltery.rockingPhase += 0.1f;

        // Swings up over about half a second and dies away far more slowly.
        unsmeltery.rocking += state.getValue(AbstractFurnaceBlock.LIT) ? 0.05f : -0.01f;
        unsmeltery.rocking = Mth.clamp(unsmeltery.rocking, 0.0f, 1.0f);
    }

    /** getRockingAmount: magnitude times the sine of the phase, so it sways. */
    public float getRockingAmount(float partialTick) {
        float magnitude = Mth.lerp(partialTick, prevRocking, rocking);
        float phase = Mth.lerp(partialTick, prevRockingPhase, rockingPhase);
        return magnitude * Mth.sin(phase);
    }

    // -------------------------------------------------------------- container

    @Override
    public int getContainerSize() {
        return SLOT_COUNT;
    }

    @Override
    public boolean isEmpty() {
        return items.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getItem(int slot) {
        return items.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        return ContainerHelper.removeItem(items, slot, amount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(items, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        items.set(slot, stack);
        stack.limitSize(getMaxStackSize(stack));
        setChanged();
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return switch (slot) {
            case OUTPUT_SLOT -> false;
            // canMachineInsertInput: only things that would actually yield
            // something may be dropped in.
            case INPUT_SLOT -> canBeUnsmelted(stack);
            case FUEL_SLOT -> level != null && level.fuelValues().isFuel(stack);
            default -> false;
        };
    }

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
        return customName != null ? customName : getBlockState().getBlock().getName();
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new LOTRUnsmelteryMenu(containerId, inventory, this, dataAccess);
    }

    // ------------------------------------------------------------ persistence

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        items.clear();
        ContainerHelper.loadAllItems(input, items);
        cookingProgress = input.getIntOr("CookTime", 0);
        litTime = input.getIntOr("BurnTime", 0);
        litTotalTime = input.getIntOr("BurnTimeTotal", 0);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        ContainerHelper.saveAllItems(output, items);
        output.putInt("CookTime", cookingProgress);
        output.putInt("BurnTime", litTime);
        output.putInt("BurnTimeTotal", litTotalTime);
    }
}
