package net.blueskiez77.lord_of_the_rings__middle_earth.common.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRDrinkItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItems;
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
        Set<Item> bone = of(Items.BONE, LOTRItems.WARG_BONE, LOTRItems.ORC_BONE, LOTRItems.ELF_BONE,
                LOTRItems.DWARF_BONE, LOTRItems.HOBBIT_BONE, LOTRItems.TROLL_BONE);
        Set<Item> apple = of(Items.APPLE, LOTRItems.GREEN_APPLE);
        Item wheat = Items.WHEAT;
        add(list, LOTRItems.ALE, wheat, wheat, wheat, wheat, wheat, wheat);
        add(list, LOTRItems.MIRUVOR, LOTRItems.MALLORN_NUT, LOTRItems.MALLORN_NUT, LOTRItems.MALLORN_NUT,
                LOTRBlocks.ELANOR, LOTRBlocks.NIPHREDIL, Items.SUGAR);
        add(list, LOTRItems.ORC_DRAUGHT, LOTRBlocks.MORGUL_SHROOM, LOTRBlocks.MORGUL_SHROOM, LOTRBlocks.MORGUL_SHROOM,
                bone, bone, bone);
        add(list, LOTRItems.MEAD, Items.SUGAR, Items.SUGAR, Items.SUGAR, Items.SUGAR, Items.SUGAR, Items.SUGAR);
        add(list, LOTRItems.CIDER, apple, apple, apple, apple, apple, apple);
        add(list, LOTRItems.PERRY, LOTRItems.PEAR, LOTRItems.PEAR, LOTRItems.PEAR, LOTRItems.PEAR, LOTRItems.PEAR,
                LOTRItems.PEAR);
        add(list, LOTRItems.CHERRY_LIQUEUR, LOTRItems.CHERRIES, LOTRItems.CHERRIES, LOTRItems.CHERRIES,
                LOTRItems.CHERRIES, LOTRItems.CHERRIES, LOTRItems.CHERRIES);
        add(list, LOTRItems.RUM, Items.SUGAR_CANE, Items.SUGAR_CANE, Items.SUGAR_CANE, Items.SUGAR_CANE,
                Items.SUGAR_CANE, Items.SUGAR_CANE);
        add(list, LOTRItems.ATHELAS_BREW, LOTRBlocks.ATHELAS, LOTRBlocks.ATHELAS, LOTRBlocks.ATHELAS,
                LOTRBlocks.ATHELAS, LOTRBlocks.ATHELAS, LOTRBlocks.ATHELAS);
        add(list, LOTRItems.DWARVEN_TONIC, wheat, wheat, wheat, LOTRBlocks.DWARF_HERB, LOTRBlocks.DWARF_HERB,
                LOTRItems.MITHRIL_NUGGET);
        add(list, LOTRItems.DWARVEN_ALE, wheat, wheat, wheat, wheat, LOTRBlocks.DWARF_HERB, LOTRBlocks.DWARF_HERB);
        add(list, LOTRItems.VODKA, Items.POTATO, Items.POTATO, Items.POTATO, Items.POTATO, Items.POTATO,
                Items.POTATO);
        add(list, LOTRItems.MAPLE_BEER, wheat, wheat, wheat, wheat, LOTRItems.MAPLE_SYRUP, LOTRItems.MAPLE_SYRUP);
        add(list, LOTRItems.ARAK, LOTRItems.DATE, LOTRItems.DATE, LOTRItems.DATE, LOTRItems.DATE, LOTRItems.DATE,
                LOTRItems.DATE);
        add(list, LOTRItems.CARROT_WINE, Items.CARROT, Items.CARROT, Items.CARROT, Items.CARROT, Items.CARROT,
                Items.CARROT);
        add(list, LOTRItems.BANANA_BEER, LOTRItems.BANANA, LOTRItems.BANANA, LOTRItems.BANANA, LOTRItems.BANANA,
                LOTRItems.BANANA, LOTRItems.BANANA);
        add(list, LOTRItems.MELON_LIQUEUR, Items.MELON_SLICE, Items.MELON_SLICE, Items.MELON_SLICE,
                Items.MELON_SLICE, Items.MELON_SLICE, Items.MELON_SLICE);
        add(list, LOTRItems.CACTUS_LIQUEUR, Items.CACTUS, Items.CACTUS, Items.CACTUS, Items.CACTUS, Items.CACTUS,
                Items.CACTUS);
        add(list, LOTRItems.TERMITE_TEQUILA, Items.CACTUS, Items.CACTUS, Items.CACTUS, Items.CACTUS, Items.CACTUS,
                LOTRItems.EXPLODING_TERMITE);
        add(list, LOTRItems.TOROG_DRAUGHT, Items.SUGAR_CANE, Items.SUGAR_CANE, Items.ROTTEN_FLESH,
                Items.ROTTEN_FLESH, Items.DIRT, LOTRItems.RHINO_HORN);
        add(list, LOTRItems.LEMON_LIQUEUR, LOTRItems.LEMON, LOTRItems.LEMON, LOTRItems.LEMON, LOTRItems.LEMON,
                LOTRItems.LEMON, LOTRItems.LEMON);
        add(list, LOTRItems.LIME_LIQUEUR, LOTRItems.LIME, LOTRItems.LIME, LOTRItems.LIME, LOTRItems.LIME,
                LOTRItems.LIME, LOTRItems.LIME);
        add(list, LOTRItems.CORN_LIQUOR, LOTRItems.CORN, LOTRItems.CORN, LOTRItems.CORN, LOTRItems.CORN,
                LOTRItems.CORN, LOTRItems.CORN);
        add(list, LOTRItems.RED_WINE, LOTRItems.RED_GRAPES, LOTRItems.RED_GRAPES, LOTRItems.RED_GRAPES,
                LOTRItems.RED_GRAPES, LOTRItems.RED_GRAPES, LOTRItems.RED_GRAPES);
        add(list, LOTRItems.WHITE_WINE, LOTRItems.GREEN_GRAPES, LOTRItems.GREEN_GRAPES, LOTRItems.GREEN_GRAPES,
                LOTRItems.GREEN_GRAPES, LOTRItems.GREEN_GRAPES, LOTRItems.GREEN_GRAPES);
        add(list, LOTRItems.MORGUL_DRAUGHT, LOTRBlocks.MORGUL_FLOWER, LOTRBlocks.MORGUL_FLOWER,
                LOTRBlocks.MORGUL_FLOWER, bone, bone, bone);
        add(list, LOTRItems.PLUM_KVASS, wheat, wheat, wheat, LOTRItems.PLUM, LOTRItems.PLUM, LOTRItems.PLUM);
        add(list, LOTRItems.SOURED_MILK, Items.MILK_BUCKET, Items.MILK_BUCKET, Items.MILK_BUCKET,
                Items.MILK_BUCKET, Items.MILK_BUCKET, Items.MILK_BUCKET);
        add(list, LOTRItems.POMEGRANATE_WINE, LOTRItems.POMEGRANATE, LOTRItems.POMEGRANATE, LOTRItems.POMEGRANATE,
                LOTRItems.POMEGRANATE, LOTRItems.POMEGRANATE, LOTRItems.POMEGRANATE);
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
