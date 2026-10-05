package net.blueskiez77.lord_of_the_rings__middle_earth.common.enchant;

import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRSmithsScrollItem;

import net.minecraft.world.item.ItemStack;

import org.jspecify.annotations.Nullable;

import static net.blueskiez77.lord_of_the_rings__middle_earth.common.enchant.LOTRModifier.*;

/**
 * LOTREnchantmentCombining: two smith's scrolls of the same modifier, taken
 * to a smith, make one scroll of the next grade up, for coins.
 */
public final class LOTRModifierCombining {

    public record CombineRecipe(LOTRModifier inputMod, LOTRModifier outputMod, int cost) {
        public ItemStack createOutputItem() {
            return LOTRSmithsScrollItem.of(this.outputMod);
        }
    }

    private static final List<CombineRecipe> ALL_COMBINE_RECIPES = List.of(
            new CombineRecipe(STRONG_1, STRONG_2, 200),
            new CombineRecipe(STRONG_2, STRONG_3, 800),
            new CombineRecipe(STRONG_3, STRONG_4, 1600),
            new CombineRecipe(DURABLE_1, DURABLE_2, 300),
            new CombineRecipe(DURABLE_2, DURABLE_3, 1500),
            new CombineRecipe(KNOCKBACK_1, KNOCKBACK_2, 2500),
            new CombineRecipe(TOOL_SPEED_1, TOOL_SPEED_2, 200),
            new CombineRecipe(TOOL_SPEED_2, TOOL_SPEED_3, 800),
            new CombineRecipe(TOOL_SPEED_3, TOOL_SPEED_4, 1500),
            new CombineRecipe(LOOTING_1, LOOTING_2, 400),
            new CombineRecipe(LOOTING_2, LOOTING_3, 1500),
            new CombineRecipe(PROTECT_1, PROTECT_2, 2000),
            new CombineRecipe(PROTECT_FIRE_1, PROTECT_FIRE_2, 400),
            new CombineRecipe(PROTECT_FIRE_2, PROTECT_FIRE_3, 1500),
            new CombineRecipe(PROTECT_FALL_1, PROTECT_FALL_2, 400),
            new CombineRecipe(PROTECT_FALL_2, PROTECT_FALL_3, 1500),
            new CombineRecipe(PROTECT_RANGED_1, PROTECT_RANGED_2, 400),
            new CombineRecipe(PROTECT_RANGED_2, PROTECT_RANGED_3, 1500),
            new CombineRecipe(RANGED_STRONG_1, RANGED_STRONG_2, 400),
            new CombineRecipe(RANGED_STRONG_2, RANGED_STRONG_3, 1500),
            new CombineRecipe(RANGED_KNOCKBACK_1, RANGED_KNOCKBACK_2, 2500));

    private LOTRModifierCombining() {
    }

    public static @Nullable CombineRecipe getCombinationResult(ItemStack item1, ItemStack item2) {
        if (item1.isEmpty() || item2.isEmpty() || !(item1.getItem() instanceof LOTRSmithsScrollItem)
                || !(item2.getItem() instanceof LOTRSmithsScrollItem)) {
            return null;
        }
        LOTRModifier mod1 = LOTRSmithsScrollItem.getModifier(item1);
        if (mod1 == null || mod1 != LOTRSmithsScrollItem.getModifier(item2)) {
            return null;
        }
        for (CombineRecipe recipe : ALL_COMBINE_RECIPES) {
            if (recipe.inputMod() == mod1) {
                return recipe;
            }
        }
        return null;
    }
}
