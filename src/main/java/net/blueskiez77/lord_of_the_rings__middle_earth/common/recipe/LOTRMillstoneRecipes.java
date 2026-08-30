package net.blueskiez77.lord_of_the_rings__middle_earth.common.recipe;

import java.util.HashMap;
import java.util.Map;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
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
 * <p><b>The cracked-brick entries are derived, not listed.</b> The original
 * spells out thirty-odd {@code addCrackedBricks} calls by metadata. Every one
 * of them is the same rule -- a brick grinds into its own cracked form -- so
 * this walks the block registry instead and pairs {@code lotr:x_brick} with
 * {@code lotr:cracked_x_brick} (and the same for pillars) wherever both exist.
 * New brick families are then covered the moment they are registered, and no
 * entry can name a block that has not been ported yet.
 *
 * <p><b>Red sandstone is vanilla's.</b> {@code LOTRMod.redSandstone} existed
 * because 1.7.10 had no red sandstone; modern Minecraft does, so it grinds to
 * vanilla red sand.
 *
 * <p><b>{@code addCrackedBricks} also registered a furnace recipe</b>
 * ({@code GameRegistry.addSmelting(itemstack, result, 0.1f)}) so bricks could
 * be cracked in a furnace as well as a millstone. That belongs in the recipe
 * datagen, not here -- see docs/TODO-millstone.md.
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

        // Gravel is the gamble: three times in four you grind it away to
        // nothing, which is why flint is worth having a millstone for.
        addRecipe(Blocks.GRAVEL, Items.FLINT, 1, 0.25f);
        addRecipe(LOTRBlocks.MORDOR_GRAVEL, Items.FLINT, 1, 0.25f);

        addRecipe(Blocks.SANDSTONE, Blocks.SAND, 2, 1.0f);
        addRecipe(Blocks.RED_SANDSTONE, Blocks.RED_SAND, 2, 1.0f);
        addRecipe(LOTRBlocks.WHITE_SANDSTONE, LOTRBlocks.WHITE_SAND, 2, 1.0f);

        addRecipe(Blocks.STONE_BRICKS, Blocks.CRACKED_STONE_BRICKS);

        addCrackedPairs();
    }

    /**
     * Pair every {@code lotr:<name>} with {@code lotr:cracked_<name>}, which
     * covers the original's whole addCrackedBricks list plus the pillars.
     */
    private static void addCrackedPairs() {
        for (Identifier id : BuiltInRegistries.BLOCK.keySet()) {
            if (!"lotr".equals(id.getNamespace()) || id.getPath().startsWith("cracked_")) {
                continue;
            }
            Identifier crackedId =
                    Identifier.fromNamespaceAndPath(id.getNamespace(), "cracked_" + id.getPath());
            BuiltInRegistries.BLOCK.getOptional(crackedId)
                    .ifPresent(cracked -> addRecipe(BuiltInRegistries.BLOCK.getValue(id), cracked));
        }
    }
}
