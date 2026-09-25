package net.blueskiez77.lord_of_the_rings__middle_earth.common.recipe;

import com.mojang.serialization.MapCodec;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMiscItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRPoisonedDrinks;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRDrinkItem;

/**
 * LOTRRecipesPoisonDrinks: one full drink and one bottle of poison, alone in
 * the grid, give the drink back poisoned. The bottle comes back empty through
 * its craft remainder.
 *
 * <p>The original dug the crafting player out of the grid by reflection to
 * record them as the poisoner. A recipe cannot see the player here, so
 * {@link LOTRDrinkItem#onCraftedBy}
 * records them instead as the result is taken.
 */
public class LOTRPoisonDrinkRecipe extends CustomRecipe {
    public static final LOTRPoisonDrinkRecipe INSTANCE = new LOTRPoisonDrinkRecipe();
    public static final MapCodec<LOTRPoisonDrinkRecipe> MAP_CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, LOTRPoisonDrinkRecipe> STREAM_CODEC =
            StreamCodec.unit(INSTANCE);
    public static final RecipeSerializer<LOTRPoisonDrinkRecipe> SERIALIZER =
            new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

    @Override
    public boolean matches(CraftingInput input, Level level) {
        return !findDrink(input).isEmpty();
    }

    @Override
    public ItemStack assemble(CraftingInput input) {
        ItemStack drink = findDrink(input);
        if (drink.isEmpty()) {
            return ItemStack.EMPTY;
        }
        ItemStack result = drink.copyWithCount(1);
        LOTRPoisonedDrinks.poison(result, null);
        return result;
    }

    @Override
    public RecipeSerializer<LOTRPoisonDrinkRecipe> getSerializer() {
        return SERIALIZER;
    }

    // The drink, if the grid is exactly one drink and one poison.
    private static ItemStack findDrink(CraftingInput input) {
        ItemStack drink = ItemStack.EMPTY;
        boolean poison = false;
        for (ItemStack stack : input.items()) {
            if (stack.isEmpty()) {
                continue;
            }
            if (LOTRPoisonedDrinks.canPoison(stack)) {
                if (!drink.isEmpty()) {
                    return ItemStack.EMPTY;
                }
                drink = stack;
                continue;
            }
            if (stack.is(LOTRMiscItems.BOTTLE_OF_POISON)) {
                if (poison) {
                    return ItemStack.EMPTY;
                }
                poison = true;
                continue;
            }
            return ItemStack.EMPTY;
        }
        return poison ? drink : ItemStack.EMPTY;
    }
}
