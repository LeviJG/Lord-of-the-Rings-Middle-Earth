package net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity;

import java.util.Set;
import java.util.HashSet;
import net.minecraft.world.item.BlockItem;
import net.minecraft.core.Direction;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.CrossbowItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRThrowingAxeItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRSlingItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRFoodItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMaterialItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMiscItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRToolItems;
import java.util.List;
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
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
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
 *
 * <p>A BaseContainerBlockEntity for the custom name, and a WorldlyContainer for
 * LOTRTileEntityForgeBase's hopper rules.
 */
public class LOTRUnsmelteryBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer {

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
     * getEquipmentMaterial. The original asked tools, swords (every melee
     * weapon was one), crossbows, throwing axes, armour and mount armour for
     * their material's repair item, and knew a handful of odds and ends by
     * name. Bows -- the blowgun among them -- and the sling were none of
     * those, so nothing ever unsmelted them.
     *
     * <p>The material is the first entry of the item's REPAIRABLE component,
     * which is where the port keeps the material's repair item.
     */
    public static ItemStack getEquipmentMaterial(ItemStack stack) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        ItemStack named = namedMaterial(stack);
        if (!named.isEmpty()) {
            return named;
        }
        if (!isEquipment(stack)) {
            return ItemStack.EMPTY;
        }
        Repairable repairable = stack.get(DataComponents.REPAIRABLE);
        if (repairable == null) {
            return ItemStack.EMPTY;
        }
        for (Holder<Item> holder : repairable.items()) {
            return new ItemStack(holder.value());
        }
        return ItemStack.EMPTY;
    }

    /** ItemTool, ItemSword, LOTRItemCrossbow, LOTRItemThrowingAxe, ItemArmor, LOTRItemMountArmor. */
    private static boolean isEquipment(ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof CrossbowItem || item instanceof LOTRThrowingAxeItem) {
            return true;
        }
        if (item instanceof ProjectileWeaponItem || item instanceof LOTRSlingItem) {
            return false;
        }
        // Every 1.7.10 tool and sword carried the shared weapon-damage modifier,
        // which is what LOTRWeaponStats.isMeleeWeapon looked for too.
        if (stack.has(DataComponents.TOOL) || stack.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS,
                ItemAttributeModifiers.EMPTY).modifiers().stream()
                .anyMatch(entry -> entry.modifier().is(Item.BASE_ATTACK_DAMAGE_ID))) {
            return true;
        }
        Equippable equippable = stack.get(DataComponents.EQUIPPABLE);
        return equippable != null && equippable.slot().isArmor();
    }

    /** The end of getEquipmentMaterial: the bucket, the rings and the goblets. */
    private static ItemStack namedMaterial(ItemStack stack) {
        if (stack.is(Items.BUCKET)) {
            return new ItemStack(Items.IRON_INGOT);
        }
        if (stack.is(LOTRMiscItems.SILVER_RING)) {
            return new ItemStack(LOTRMaterialItems.SILVER_NUGGET);
        }
        if (stack.is(LOTRMiscItems.GOLD_RING)) {
            return new ItemStack(Items.GOLD_NUGGET);
        }
        if (stack.is(LOTRMiscItems.MITHRIL_RING)) {
            return new ItemStack(LOTRMaterialItems.MITHRIL_NUGGET);
        }
        if (stack.is(LOTRFoodItems.GOLDEN_GOBLET)) {
            return new ItemStack(Items.GOLD_INGOT);
        }
        if (stack.is(LOTRFoodItems.SILVER_GOBLET)) {
            return new ItemStack(LOTRMaterialItems.SILVER_INGOT);
        }
        if (stack.is(LOTRFoodItems.COPPER_GOBLET)) {
            return new ItemStack(LOTRMaterialItems.BRONZE_INGOT);
        }
        return ItemStack.EMPTY;
    }

    private static @Nullable Map<Item, List<Item>> uncraftable;

    /**
     * LOTRRecipes.uncraftableUnsmeltingRecipes: gear no table makes -- found in
     * ruins, dropped, or given -- with the grid it would have taken, so the
     * unsmeltery still knows what is in it. Only the material in each grid is
     * listed; the sticks and string beside it never come back anyway.
     */
    private static Map<Item, List<Item>> uncraftable() {
        if (uncraftable == null) {
            Map<Item, List<Item>> map = new HashMap<>();
            Item iron = Items.IRON_INGOT;
            Item elf = LOTRMaterialItems.ELVEN_STEEL_INGOT;
            Item orc = LOTRMaterialItems.ORC_STEEL_INGOT;
            Item gold = Items.GOLD_INGOT;
            for (Object[] entry : new Object[][] {
                    {LOTRCombatItems.BARROW_BLADE, iron, 1},
                    {LOTRCombatItems.ARNOR_HELMET, iron, 5}, {LOTRCombatItems.ARNOR_CHESTPLATE, iron, 8},
                    {LOTRCombatItems.ARNOR_LEGGINGS, iron, 7}, {LOTRCombatItems.ARNOR_BOOTS, iron, 4},
                    {LOTRCombatItems.ARNOR_SWORD, iron, 2}, {LOTRCombatItems.ARNOR_DAGGER, iron, 1},
                    {LOTRCombatItems.ARNOR_SPEAR, iron, 1},
                    {LOTRCombatItems.BLACK_NUMENOREAN_HELMET, iron, 5}, {LOTRCombatItems.BLACK_NUMENOREAN_CHESTPLATE, iron, 8},
                    {LOTRCombatItems.BLACK_NUMENOREAN_LEGGINGS, iron, 7}, {LOTRCombatItems.BLACK_NUMENOREAN_BOOTS, iron, 4},
                    {LOTRCombatItems.BLACK_NUMENOREAN_SWORD, iron, 2}, {LOTRCombatItems.BLACK_NUMENOREAN_DAGGER, iron, 1},
                    {LOTRCombatItems.BLACK_NUMENOREAN_SPEAR, iron, 1}, {LOTRCombatItems.BLACK_NUMENOREAN_MACE, iron, 4},
                    {LOTRCombatItems.GONDOLIN_HELMET, elf, 5}, {LOTRCombatItems.GONDOLIN_CHESTPLATE, elf, 8},
                    {LOTRCombatItems.GONDOLIN_LEGGINGS, elf, 7}, {LOTRCombatItems.GONDOLIN_BOOTS, elf, 4},
                    {LOTRCombatItems.GONDOLIN_SWORD, elf, 2},
                    {LOTRCombatItems.GOLDEN_TAURETHRIM_HELMET, gold, 5}, {LOTRCombatItems.GOLDEN_TAURETHRIM_CHESTPLATE, gold, 8},
                    {LOTRCombatItems.GOLDEN_TAURETHRIM_LEGGINGS, gold, 7}, {LOTRCombatItems.GOLDEN_TAURETHRIM_BOOTS, gold, 4},
                    {LOTRCombatItems.BLACKSMITH_HAMMER, iron, 4},
                    {LOTRCombatItems.OLD_HARADRIC_SACRIFICIAL_DAGGER, iron, 1},
                    {LOTRCombatItems.UTUMNO_HELMET, orc, 5}, {LOTRCombatItems.UTUMNO_CHESTPLATE, orc, 8},
                    {LOTRCombatItems.UTUMNO_LEGGINGS, orc, 7}, {LOTRCombatItems.UTUMNO_BOOTS, orc, 4},
                    {LOTRCombatItems.UTUMNO_SWORD, orc, 2}, {LOTRCombatItems.UTUMNO_DAGGER, orc, 1},
                    {LOTRCombatItems.UTUMNO_SPEAR, orc, 1}, {LOTRCombatItems.UTUMNO_BATTLEAXE, orc, 5},
                    {LOTRCombatItems.UTUMNO_WARHAMMER, orc, 4}, {LOTRToolItems.UTUMNO_PICKAXE, orc, 3}}) {
                map.put((Item) entry[0], java.util.Collections.nCopies((Integer) entry[2], (Item) entry[1]));
            }
            // The Utumno bow was on the list too, but as a bow it was never
            // offered to the unsmeltery in the first place.
            uncraftable = map;
        }
        return uncraftable;
    }

    /**
     * canBeUnsmelted. Refuses anything whose material would BURN -- the
     * original checked getItemBurnTime on the material, so a wooden sword
     * cannot be turned back into planks by a fire that would consume them --
     * and any block item whose block burns (Material.getCanBurn).
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
        if (stack.getItem() instanceof BlockItem blockItem
                && blockItem.getBlock().defaultBlockState().ignitedByLava()) {
            return false;
        }
        return resourcesUsed(stack, material) > 0;
    }

    /**
     * determineResourcesUsed. How many of {@code material} the item's crafting
     * recipe consumes -- an iron chestplate is eight ingots, an iron sword two
     * (one ingot, one stick, of which one matches).
     *
     * <p>An ingredient that is not the material itself is followed into ITS
     * recipe (countMatchingIngredients' recursion), so something crafted from
     * iron blocks counts nine ingots a block. A recipe is followed at most once
     * per question, which is what stops a cycle. For an ingredient that
     * accepts several items (a tag, 1.7.10's ore-dictionary list) every
     * alternative's count is added, as the original's loop did.
     *
     * <p>Where several recipes make the item, the largest count is kept; the
     * original took the first it came to in its recipe lists, an order the
     * modern recipe manager does not share.
     *
     * <p>Gear with no crafting recipe at all falls back on
     * {@link #uncraftable()}, as the original searched its uncraftable list last.
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
        int count = level instanceof ServerLevel serverLevel
                ? resourcesUsed(serverLevel, stack.getItem(), material, new HashSet<>())
                : 0;
        if (count == 0) {
            List<Item> grid = uncraftable().get(stack.getItem());
            if (grid != null) {
                count = (int) grid.stream().filter(material::is).count();
            }
        }
        RESOURCE_COUNTS.put(key, count);
        return count;
    }

    // Ingredient.items() is deprecated but is the only way 26.2 offers to list
    // an ingredient's alternatives, which the recursion has to follow.
    @SuppressWarnings("deprecation")
    private static int resourcesUsed(ServerLevel level, Item item, ItemStack material,
                                     Set<RecipeHolder<?>> checked) {
        int best = 0;
        for (RecipeHolder<?> holder : level.recipeAccess().getRecipes()) {
            if (checked.contains(holder) || !(holder.value() instanceof NormalCraftingRecipe crafting)) {
                continue;
            }
            ItemStack result = resultOf(crafting);
            if (result.isEmpty() || !result.is(item)) {
                continue;
            }
            checked.add(holder);
            int matches = 0;
            for (Ingredient ingredient : crafting.placementInfo().ingredients()) {
                if (ingredient.test(material)) {
                    matches++;
                    continue;
                }
                for (Holder<Item> alternative : ingredient.items().toList()) {
                    matches += resourcesUsed(level, alternative.value(), material, checked);
                }
            }
            // Per crafted item, not per recipe: a recipe making four of
            // something spreads its ingredients across all four.
            matches /= Math.max(1, result.getCount());
            best = Math.max(best, matches);
        }
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
    protected NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> newItems) {
        for (int i = 0; i < SLOT_COUNT; ++i) {
            items.set(i, i < newItems.size() ? newItems.get(i) : ItemStack.EMPTY);
        }
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

    private static final int[] SLOTS_DOWN = {OUTPUT_SLOT, FUEL_SLOT};
    private static final int[] SLOTS_UP = {INPUT_SLOT};
    private static final int[] SLOTS_SIDE = {FUEL_SLOT};

    /** getAccessibleSlotsFromSide: output and fuel below, input above, fuel at the sides. */
    @Override
    public int[] getSlotsForFace(Direction side) {
        return switch (side) {
            case DOWN -> SLOTS_DOWN;
            case UP -> SLOTS_UP;
            default -> SLOTS_SIDE;
        };
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
