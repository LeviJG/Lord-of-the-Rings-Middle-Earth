package net.blueskiez77.lord_of_the_rings__middle_earth.common.recipe;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBuildingBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRFoodItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMaterialItems;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.ItemLike;

import org.jspecify.annotations.Nullable;

/**
 * LOTRMillstoneRecipes. What the millstone turns into what, and how often.
 *
 * <p>The original keeps this as a plain Java map built once at startup rather
 * than as datapack recipes, and so does this -- a millstone recipe is an
 * item plus a chance, which no vanilla recipe type expresses.
 *
 * <p>Each entry is one input item and one {@link Result}: the stack produced
 * and the probability of producing it. A failed roll still consumes the input,
 * which is what makes gravel-to-flint a gamble rather than a conversion.
 *
 * <h2>Deviations from the original</h2>
 *
 * <p><b>Inputs are keyed by item, not by item plus damage.</b> The 1.7.10 list
 * was full of {@code new ItemStack(LOTRMod.brick, 1, 11)} entries because a
 * single block id held sixteen bricks in its metadata. Those are all separate
 * blocks now, so the damage value has nothing left to select and the linear
 * scan in {@code getMillingResult} becomes a map lookup.
 *
 * <p><b>Red sandstone is vanilla's.</b> {@code LOTRMod.redSandstone} existed
 * because 1.7.10 had no red sandstone; modern Minecraft does, so it grinds to
 * vanilla red sand.
 *
 * <p><b>{@code addCrackedBricks} also registered a furnace recipe</b>
 * ({@code GameRegistry.addSmelting(itemstack, result, 0.1f)}). Those are
 * emitted by LOTRTranscribedRecipes from {@link #crackedBricks()}.
 */
public final class LOTRMillstoneRecipes {

    /**
     * What a successful grind yields, and how often the grind succeeds.
     *
     * <p>Held as an item and a count rather than as an ItemStack because the
     * recipe list is built during mod init, before item components are bound --
     * constructing a stack that early throws "Components not bound yet". The
     * stack is made on demand instead, which also keeps callers from sharing
     * and mutating one prototype.
     */
    public record Result(ItemLike item, int count, float chance) {

        public ItemStack resultItem() {
            return new ItemStack(item, count);
        }
    }

    private static final Map<Item, Result> RECIPES = new HashMap<>();

    private LOTRMillstoneRecipes() {
    }

    private static void addRecipe(ItemLike input, ItemLike result, int count, float chance) {
        RECIPES.put(input.asItem(), new Result(result, count, chance));
    }

    private static void addRecipe(ItemLike input, ItemLike result) {
        addRecipe(input, result, 1, 1.0f);
    }

    public static @Nullable Result getMillingResult(ItemStack itemstack) {
        return itemstack.isEmpty() ? null : RECIPES.get(itemstack.getItem());
    }

    public static boolean canMill(ItemStack itemstack) {
        return getMillingResult(itemstack) != null;
    }

    /** Called once from LOTRMod's initialiser, after the blocks are registered. */
    public static void createRecipes() {
        RECIPES.clear();

        addRecipe(Blocks.STONE, Blocks.COBBLESTONE);
        addRecipe(Blocks.COBBLESTONE, Blocks.GRAVEL, 1, 0.75f);
        addRecipe(LOTRBuildingBlocks.MORDOR_ROCK, LOTRBuildingBlocks.MORDOR_GRAVEL, 1, 0.75f);

        // Gravel is the gamble: three times in four you grind it away to
        // nothing, which is why flint is worth having a millstone for.
        addRecipe(Blocks.GRAVEL, Items.FLINT, 1, 0.25f);
        addRecipe(LOTRBuildingBlocks.MORDOR_GRAVEL, Items.FLINT, 1, 0.25f);
        addRecipe(LOTRBuildingBlocks.OBSIDIAN_GRAVEL, LOTRMaterialItems.OBSIDIAN_SHARD);
        addRecipe(LOTRBuildingBlocks.SALT_ORE, LOTRFoodItems.SALT);

        addRecipe(Blocks.SANDSTONE, Blocks.SAND, 2, 1.0f);
        addRecipe(Blocks.RED_SANDSTONE, Blocks.RED_SAND, 2, 1.0f);
        addRecipe(LOTRBuildingBlocks.WHITE_SANDSTONE, LOTRBuildingBlocks.WHITE_SAND, 2, 1.0f);

        crackedBricks().forEach(LOTRMillstoneRecipes::addRecipe);
    }

    /**
     * The original's addCrackedBricks list, in its order. Each pair was BOTH a
     * certain millstone grind and a furnace recipe (0.1 xp); the furnace half is
     * emitted by the recipe datagen from this same map.
     *
     * <p>Explicit rather than derived from names: the port's cracked blocks are
     * not all named cracked_&lt;x&gt; (dale_cracked_brick, rhun_cracked_brick,
     * ...), and a name rule also swept in slabs, stairs and walls, which the
     * original never cracked.
     */
    public static Map<ItemLike, ItemLike> crackedBricks() {
        Map<ItemLike, ItemLike> pairs = new LinkedHashMap<>();
        pairs.put(Blocks.BRICKS, LOTRBuildingBlocks.RED_BRICK_CRACKED);
        pairs.put(Blocks.STONE_BRICKS, Blocks.CRACKED_STONE_BRICKS);
        pairs.put(LOTRBuildingBlocks.MORDOR_BRICK, LOTRBuildingBlocks.CRACKED_MORDOR_BRICK);
        pairs.put(LOTRBuildingBlocks.GONDOR_BRICK, LOTRBuildingBlocks.CRACKED_GONDOR_BRICK);
        pairs.put(LOTRBuildingBlocks.DWARVEN_BRICK, LOTRBuildingBlocks.CRACKED_DWARVEN_BRICK);
        pairs.put(LOTRBuildingBlocks.GALADHRIM_BRICK, LOTRBuildingBlocks.CRACKED_GALADHRIM_BRICK);
        pairs.put(LOTRBuildingBlocks.NEAR_HARAD_BRICK, LOTRBuildingBlocks.NEAR_HARAD_CRACKED_BRICK);
        pairs.put(LOTRBuildingBlocks.ANGMAR_BRICK, LOTRBuildingBlocks.CRACKED_ANGMAR_BRICK);
        pairs.put(LOTRBuildingBlocks.ARNOR_BRICK, LOTRBuildingBlocks.CRACKED_ARNOR_BRICK);
        pairs.put(LOTRBuildingBlocks.DOL_GULDUR_BRICK, LOTRBuildingBlocks.CRACKED_DOL_GULDUR_BRICK);
        pairs.put(LOTRBuildingBlocks.HIGH_ELVEN_BRICK, LOTRBuildingBlocks.CRACKED_HIGH_ELVEN_BRICK);
        pairs.put(LOTRBuildingBlocks.WOOD_ELVEN_BRICK, LOTRBuildingBlocks.CRACKED_WOOD_ELVEN_BRICK);
        pairs.put(LOTRBuildingBlocks.MORWAITH_BRICK, LOTRBuildingBlocks.CRACKED_MORWAITH_BRICK);
        pairs.put(LOTRBuildingBlocks.NEAR_HARAD_RED_BRICK, LOTRBuildingBlocks.NEAR_HARAD_RED_CRACKED_BRICK);
        pairs.put(LOTRBuildingBlocks.TAUREDAIN_BRICK, LOTRBuildingBlocks.TAUREDAIN_CRACKED_BRICK);
        pairs.put(LOTRBuildingBlocks.DALE_BRICK, LOTRBuildingBlocks.DALE_CRACKED_BRICK);
        pairs.put(LOTRBuildingBlocks.DORWINION_BRICK, LOTRBuildingBlocks.CRACKED_DORWINION_BRICK);
        pairs.put(LOTRBuildingBlocks.GONDOR_COBBLEBRICK, LOTRBuildingBlocks.CRACKED_GONDOR_COBBLEBRICK);
        pairs.put(LOTRBuildingBlocks.RHUN_BRICK, LOTRBuildingBlocks.RHUN_CRACKED_BRICK);
        pairs.put(LOTRBuildingBlocks.UMBAR_BRICK, LOTRBuildingBlocks.CRACKED_UMBAR_BRICK);
        pairs.put(LOTRBuildingBlocks.DWARVEN_PILLAR, LOTRBuildingBlocks.CRACKED_DWARVEN_PILLAR);
        pairs.put(LOTRBuildingBlocks.GALADHRIM_PILLAR, LOTRBuildingBlocks.CRACKED_GALADHRIM_PILLAR);
        pairs.put(LOTRBuildingBlocks.HIGH_ELVEN_PILLAR, LOTRBuildingBlocks.CRACKED_HIGH_ELVEN_PILLAR);
        pairs.put(LOTRBuildingBlocks.WOOD_ELVEN_PILLAR, LOTRBuildingBlocks.CRACKED_WOOD_ELVEN_PILLAR);
        pairs.put(LOTRBuildingBlocks.ARNOR_PILLAR, LOTRBuildingBlocks.CRACKED_ARNOR_PILLAR);
        return pairs;
    }
}
