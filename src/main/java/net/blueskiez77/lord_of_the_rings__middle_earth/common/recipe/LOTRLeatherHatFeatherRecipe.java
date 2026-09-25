package net.blueskiez77.lord_of_the_rings__middle_earth.common.recipe;

import com.mojang.serialization.MapCodec;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRDataComponents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItemTags;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRLeatherHatItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMiscItems;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

/**
 * LOTRRecipeLeatherHatFeather: a leather hat with no feather yet, and one
 * feather -- plain (the "feather" ore name) or dyed -- and nothing else. The
 * hat comes back with the feather in its band, white or the feather's colour.
 */
public class LOTRLeatherHatFeatherRecipe extends CustomRecipe {

    public static final LOTRLeatherHatFeatherRecipe INSTANCE = new LOTRLeatherHatFeatherRecipe();
    public static final MapCodec<LOTRLeatherHatFeatherRecipe> MAP_CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, LOTRLeatherHatFeatherRecipe> STREAM_CODEC =
            StreamCodec.unit(INSTANCE);
    public static final RecipeSerializer<LOTRLeatherHatFeatherRecipe> SERIALIZER =
            new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

    @Override
    public boolean matches(CraftingInput input, Level level) {
        return !assemble(input).isEmpty();
    }

    @Override
    public ItemStack assemble(CraftingInput input) {
        ItemStack hat = null;
        ItemStack feather = null;
        for (ItemStack stack : input.items()) {
            if (stack.isEmpty()) {
                continue;
            }
            if (stack.is(LOTRMiscItems.LEATHER_HAT) && !LOTRLeatherHatItem.hasFeather(stack)) {
                if (hat != null) {
                    return ItemStack.EMPTY;
                }
                hat = stack;
                continue;
            }
            if (stack.is(LOTRItemTags.FEATHERS) || stack.is(LOTRItems.FEATHER_DYED)) {
                if (feather != null) {
                    return ItemStack.EMPTY;
                }
                feather = stack;
                continue;
            }
            return ItemStack.EMPTY;
        }
        if (hat == null || feather == null) {
            return ItemStack.EMPTY;
        }
        int colour = LOTRLeatherHatItem.FEATHER_WHITE;
        if (feather.is(LOTRItems.FEATHER_DYED)) {
            DyedItemColor dyed = feather.get(DataComponents.DYED_COLOR);
            if (dyed != null) {
                colour = dyed.rgb();
            }
        }
        ItemStack result = hat.copyWithCount(1);
        result.set(LOTRDataComponents.HAT_FEATHER, colour);
        return result;
    }

    @Override
    public RecipeSerializer<LOTRLeatherHatFeatherRecipe> getSerializer() {
        return SERIALIZER;
    }
}
