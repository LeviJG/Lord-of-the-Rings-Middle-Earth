package net.blueskiez77.lord_of_the_rings__middle_earth.common.recipe;

import java.util.List;

import com.mojang.serialization.MapCodec;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRLeatherHatItem;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.DyeRecipe;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.level.Level;

/**
 * LOTRRecipeLeatherHatDye: vanilla's dye recipe (the 1.7.10 armour blend), but
 * a hat that already has a feather in it will not take dye -- getFeatherColor
 * above -1 refused the match.
 */
public class LOTRLeatherHatDyeRecipe implements CraftingRecipe {

    public static final MapCodec<LOTRLeatherHatDyeRecipe> MAP_CODEC =
            DyeRecipe.MAP_CODEC.xmap(LOTRLeatherHatDyeRecipe::new, r -> r.delegate);
    public static final StreamCodec<RegistryFriendlyByteBuf, LOTRLeatherHatDyeRecipe> STREAM_CODEC =
            DyeRecipe.STREAM_CODEC.map(LOTRLeatherHatDyeRecipe::new, r -> r.delegate);
    public static final RecipeSerializer<LOTRLeatherHatDyeRecipe> SERIALIZER =
            new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

    private final DyeRecipe delegate;

    public LOTRLeatherHatDyeRecipe(DyeRecipe delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        for (ItemStack stack : input.items()) {
            if (LOTRLeatherHatItem.hasFeather(stack)) {
                return false;
            }
        }
        return delegate.matches(input, level);
    }

    @Override
    public ItemStack assemble(CraftingInput input) {
        return delegate.assemble(input);
    }

    @Override
    public boolean showNotification() {
        return delegate.showNotification();
    }

    @Override
    public String group() {
        return delegate.group();
    }

    @Override
    public CraftingBookCategory category() {
        return delegate.category();
    }

    @Override
    public PlacementInfo placementInfo() {
        return delegate.placementInfo();
    }

    @Override
    public List<RecipeDisplay> display() {
        return delegate.display();
    }

    @Override
    public RecipeSerializer<LOTRLeatherHatDyeRecipe> getSerializer() {
        return SERIALIZER;
    }
}
