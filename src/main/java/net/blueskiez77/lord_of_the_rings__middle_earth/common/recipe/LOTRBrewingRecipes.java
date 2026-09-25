package net.blueskiez77.lord_of_the_rings__middle_earth.common.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRDecorationBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRFoodBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRDrinkItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRFoodItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMaterialItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMiscItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

/**
 * LOTRBrewingRecipes: what a barrel turns six ingredients and three buckets of
 * water into. Every recipe makes a full barrel, sixteen weak drinks.
 *
 * <p>findMatchingRecipe, transcribed: all three bucket slots must hold water,
 * then each non-empty ingredient slot must use up one ingredient of the recipe
 * (first match wins) and nothing of the recipe may be left over. Ore-dictionary
 * "bone" is any bone -- vanilla's or one of the six LOTR bones -- and "apple"
 * the red apple or the green one.
 *
 */
public final class LOTRBrewingRecipes {
    public static final int BARREL_CAPACITY = 16;

    private record Recipe(Item result, List<Set<Item>> ingredients) {
    }

    private static List<Recipe> recipes;

    private LOTRBrewingRecipes() {
    }

    private static Set<Item> of(ItemLike... items) {
        Set<Item> set = new java.util.HashSet<>();
        for (ItemLike item : items) {
            set.add(item.asItem());
        }
        return set;
    }

    private static void add(List<Recipe> list, Item result, Object... ingredients) {
        if (ingredients.length != 6) {
            throw new IllegalArgumentException("Brewing recipes must contain exactly 6 items");
        }
        List<Set<Item>> sets = new ArrayList<>();
        for (Object ingredient : ingredients) {
            sets.add(ingredient instanceof Set<?> set ? castSet(set) : of((ItemLike) ingredient));
        }
        list.add(new Recipe(result, sets));
    }

    @SuppressWarnings("unchecked")
    private static Set<Item> castSet(Set<?> set) {
        return (Set<Item>) set;
    }

    /** createBrewingRecipes, in its order. Built on first use, once every item exists. */
    private static List<Recipe> recipes() {
        if (recipes != null) {
            return recipes;
        }
        List<Recipe> list = new ArrayList<>();
        Set<Item> bone = of(Items.BONE, LOTRMaterialItems.WARG_BONE, LOTRMaterialItems.ORC_BONE, LOTRMaterialItems.ELF_BONE,
                LOTRMaterialItems.DWARF_BONE, LOTRMaterialItems.HOBBIT_BONE, LOTRMaterialItems.TROLL_BONE);
        Set<Item> apple = of(Items.APPLE, LOTRFoodItems.GREEN_APPLE);
        Item wheat = Items.WHEAT;
        add(list, LOTRFoodItems.ALE, wheat, wheat, wheat, wheat, wheat, wheat);
        add(list, LOTRFoodItems.MIRUVOR, LOTRFoodItems.MALLORN_NUT, LOTRFoodItems.MALLORN_NUT, LOTRFoodItems.MALLORN_NUT,
                LOTRDecorationBlocks.ELANOR, LOTRDecorationBlocks.NIPHREDIL, Items.SUGAR);
        add(list, LOTRFoodItems.ORC_DRAUGHT, LOTRFoodBlocks.MORGUL_SHROOM, LOTRFoodBlocks.MORGUL_SHROOM, LOTRFoodBlocks.MORGUL_SHROOM,
                bone, bone, bone);
        add(list, LOTRFoodItems.MEAD, Items.SUGAR, Items.SUGAR, Items.SUGAR, Items.SUGAR, Items.SUGAR, Items.SUGAR);
        add(list, LOTRFoodItems.CIDER, apple, apple, apple, apple, apple, apple);
        add(list, LOTRFoodItems.PERRY, LOTRFoodItems.PEAR, LOTRFoodItems.PEAR, LOTRFoodItems.PEAR, LOTRFoodItems.PEAR, LOTRFoodItems.PEAR,
                LOTRFoodItems.PEAR);
        add(list, LOTRFoodItems.CHERRY_LIQUEUR, LOTRFoodItems.CHERRIES, LOTRFoodItems.CHERRIES, LOTRFoodItems.CHERRIES,
                LOTRFoodItems.CHERRIES, LOTRFoodItems.CHERRIES, LOTRFoodItems.CHERRIES);
        add(list, LOTRFoodItems.RUM, Items.SUGAR_CANE, Items.SUGAR_CANE, Items.SUGAR_CANE, Items.SUGAR_CANE,
                Items.SUGAR_CANE, Items.SUGAR_CANE);
        add(list, LOTRFoodItems.ATHELAS_BREW, LOTRDecorationBlocks.ATHELAS, LOTRDecorationBlocks.ATHELAS, LOTRDecorationBlocks.ATHELAS,
                LOTRDecorationBlocks.ATHELAS, LOTRDecorationBlocks.ATHELAS, LOTRDecorationBlocks.ATHELAS);
        add(list, LOTRFoodItems.DWARVEN_TONIC, wheat, wheat, wheat, LOTRDecorationBlocks.DWARF_HERB, LOTRDecorationBlocks.DWARF_HERB,
                LOTRMaterialItems.MITHRIL_NUGGET);
        add(list, LOTRFoodItems.DWARVEN_ALE, wheat, wheat, wheat, wheat, LOTRDecorationBlocks.DWARF_HERB, LOTRDecorationBlocks.DWARF_HERB);
        add(list, LOTRFoodItems.VODKA, Items.POTATO, Items.POTATO, Items.POTATO, Items.POTATO, Items.POTATO,
                Items.POTATO);
        add(list, LOTRFoodItems.MAPLE_BEER, wheat, wheat, wheat, wheat, LOTRFoodItems.MAPLE_SYRUP, LOTRFoodItems.MAPLE_SYRUP);
        add(list, LOTRFoodItems.ARAK, LOTRFoodItems.DATE, LOTRFoodItems.DATE, LOTRFoodItems.DATE, LOTRFoodItems.DATE, LOTRFoodItems.DATE,
                LOTRFoodItems.DATE);
        add(list, LOTRFoodItems.CARROT_WINE, Items.CARROT, Items.CARROT, Items.CARROT, Items.CARROT, Items.CARROT,
                Items.CARROT);
        add(list, LOTRFoodItems.BANANA_BEER, LOTRFoodItems.BANANA, LOTRFoodItems.BANANA, LOTRFoodItems.BANANA, LOTRFoodItems.BANANA,
                LOTRFoodItems.BANANA, LOTRFoodItems.BANANA);
        add(list, LOTRFoodItems.MELON_LIQUEUR, Items.MELON_SLICE, Items.MELON_SLICE, Items.MELON_SLICE,
                Items.MELON_SLICE, Items.MELON_SLICE, Items.MELON_SLICE);
        add(list, LOTRFoodItems.CACTUS_LIQUEUR, Items.CACTUS, Items.CACTUS, Items.CACTUS, Items.CACTUS, Items.CACTUS,
                Items.CACTUS);
        add(list, LOTRFoodItems.TERMITE_TEQUILA, Items.CACTUS, Items.CACTUS, Items.CACTUS, Items.CACTUS, Items.CACTUS,
                LOTRMiscItems.EXPLODING_TERMITE);
        add(list, LOTRFoodItems.TOROG_DRAUGHT, Items.SUGAR_CANE, Items.SUGAR_CANE, Items.ROTTEN_FLESH,
                Items.ROTTEN_FLESH, Items.DIRT, LOTRMaterialItems.RHINO_HORN);
        add(list, LOTRFoodItems.LEMON_LIQUEUR, LOTRFoodItems.LEMON, LOTRFoodItems.LEMON, LOTRFoodItems.LEMON, LOTRFoodItems.LEMON,
                LOTRFoodItems.LEMON, LOTRFoodItems.LEMON);
        add(list, LOTRFoodItems.LIME_LIQUEUR, LOTRFoodItems.LIME, LOTRFoodItems.LIME, LOTRFoodItems.LIME, LOTRFoodItems.LIME,
                LOTRFoodItems.LIME, LOTRFoodItems.LIME);
        add(list, LOTRFoodItems.CORN_LIQUOR, LOTRFoodItems.CORN, LOTRFoodItems.CORN, LOTRFoodItems.CORN, LOTRFoodItems.CORN,
                LOTRFoodItems.CORN, LOTRFoodItems.CORN);
        add(list, LOTRFoodItems.RED_WINE, LOTRFoodItems.RED_GRAPES, LOTRFoodItems.RED_GRAPES, LOTRFoodItems.RED_GRAPES,
                LOTRFoodItems.RED_GRAPES, LOTRFoodItems.RED_GRAPES, LOTRFoodItems.RED_GRAPES);
        add(list, LOTRFoodItems.WHITE_WINE, LOTRFoodItems.GREEN_GRAPES, LOTRFoodItems.GREEN_GRAPES, LOTRFoodItems.GREEN_GRAPES,
                LOTRFoodItems.GREEN_GRAPES, LOTRFoodItems.GREEN_GRAPES, LOTRFoodItems.GREEN_GRAPES);
        add(list, LOTRFoodItems.MORGUL_DRAUGHT, LOTRDecorationBlocks.MORGUL_FLOWER, LOTRDecorationBlocks.MORGUL_FLOWER,
                LOTRDecorationBlocks.MORGUL_FLOWER, bone, bone, bone);
        add(list, LOTRFoodItems.PLUM_KVASS, wheat, wheat, wheat, LOTRFoodItems.PLUM, LOTRFoodItems.PLUM, LOTRFoodItems.PLUM);
        add(list, LOTRFoodItems.SOURED_MILK, Items.MILK_BUCKET, Items.MILK_BUCKET, Items.MILK_BUCKET,
                Items.MILK_BUCKET, Items.MILK_BUCKET, Items.MILK_BUCKET);
        add(list, LOTRFoodItems.POMEGRANATE_WINE, LOTRFoodItems.POMEGRANATE, LOTRFoodItems.POMEGRANATE, LOTRFoodItems.POMEGRANATE,
                LOTRFoodItems.POMEGRANATE, LOTRFoodItems.POMEGRANATE, LOTRFoodItems.POMEGRANATE);
        recipes = list;
        return list;
    }

    /** isWaterSource. */
    public static boolean isWaterSource(ItemStack stack) {
        return stack.is(Items.WATER_BUCKET);
    }

    /**
     * findMatchingRecipe over the barrel's slots: 0-5 ingredients, 6-8 water.
     * Returns a full barrel of weak drink, or EMPTY.
     */
    public static ItemStack findMatchingRecipe(List<ItemStack> barrel) {
        for (int i = 6; i < 9; ++i) {
            if (!isWaterSource(barrel.get(i))) {
                return ItemStack.EMPTY;
            }
        }
        recipes:
        for (Recipe recipe : recipes()) {
            List<Set<Item>> remaining = new ArrayList<>(recipe.ingredients());
            for (int i = 0; i < 6; ++i) {
                ItemStack stack = barrel.get(i);
                if (stack.isEmpty()) {
                    continue;
                }
                boolean inRecipe = false;
                for (int j = 0; j < remaining.size(); ++j) {
                    if (remaining.get(j).contains(stack.getItem())) {
                        remaining.remove(j);
                        inRecipe = true;
                        break;
                    }
                }
                if (!inRecipe) {
                    continue recipes;
                }
            }
            if (remaining.isEmpty()) {
                ItemStack result = LOTRDrinkItem.stack(recipe.result(), 0);
                result.setCount(BARREL_CAPACITY);
                return result;
            }
        }
        return ItemStack.EMPTY;
    }
}
