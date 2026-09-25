package net.blueskiez77.lord_of_the_rings__middle_earth.common.recipe;

import com.mojang.serialization.MapCodec;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRDataComponents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRSmokingPipeItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

/**
 * LOTRRecipeHobbitPipe: a smoking pipe and a dye, anywhere in the grid, give
 * the pipe back with that colour of smoke. A mithril nugget in place of the dye
 * gives the magic smoke. Any number of dyes may go in; the last one in slot
 * order wins, as it did in the original's loop.
 *
 * <p>BlockColored.func_150031_c turned the dye's metadata into a wool colour,
 * which is what DyeColor's id is, so the component takes the id directly.
 */
public class LOTRSmokingPipeDyeRecipe extends CustomRecipe {
    public static final LOTRSmokingPipeDyeRecipe INSTANCE = new LOTRSmokingPipeDyeRecipe();
    public static final MapCodec<LOTRSmokingPipeDyeRecipe> MAP_CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, LOTRSmokingPipeDyeRecipe> STREAM_CODEC =
            StreamCodec.unit(INSTANCE);
    public static final RecipeSerializer<LOTRSmokingPipeDyeRecipe> SERIALIZER =
            new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

    @Override
    public boolean matches(CraftingInput input, Level level) {
        return smokeColor(input) >= 0;
    }

    @Override
    public ItemStack assemble(CraftingInput input) {
        int color = smokeColor(input);
        if (color < 0) {
            return ItemStack.EMPTY;
        }
        // A new pipe carrying over only the wear: the original built a fresh
        // stack, so a name or anything else on the old pipe did not survive.
        ItemStack pipe = new ItemStack(LOTRItems.SMOKING_PIPE);
        pipe.setDamageValue(findPipe(input).getDamageValue());
        pipe.set(LOTRDataComponents.SMOKE_COLOR, color);
        return pipe;
    }

    @Override
    public RecipeSerializer<LOTRSmokingPipeDyeRecipe> getSerializer() {
        return SERIALIZER;
    }

    // The colour the grid would give, or -1 if it is not exactly one pipe and
    // at least one dye with nothing else beside them.
    private static int smokeColor(CraftingInput input) {
        boolean hasPipe = false;
        int color = -1;
        for (ItemStack stack : input.items()) {
            if (stack.isEmpty()) {
                continue;
            }
            if (stack.is(LOTRItems.SMOKING_PIPE)) {
                if (hasPipe) {
                    return -1;
                }
                hasPipe = true;
                continue;
            }
            if (stack.is(LOTRItems.MITHRIL_NUGGET)) {
                color = LOTRSmokingPipeItem.MAGIC_COLOR;
                continue;
            }
            DyeColor dye = stack.get(DataComponents.DYE);
            if (dye == null) {
                return -1;
            }
            color = dye.getId();
        }
        return hasPipe ? color : -1;
    }

    private static ItemStack findPipe(CraftingInput input) {
        for (ItemStack stack : input.items()) {
            if (stack.is(LOTRItems.SMOKING_PIPE)) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }
}
