package net.blueskiez77.lord_of_the_rings__middle_earth.common.recipe;

import com.mojang.serialization.MapCodec;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRBannerItem;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

/**
 * LOTRRecipesBanners: a banner carrying protection, alone in the grid, comes
 * out bare -- everything on it wiped, as the original's setTagCompound(null)
 * did; with plain banners of the same kind beside it, they all come out
 * carrying its protection.
 */
public class LOTRBannerProtectionRecipe extends CustomRecipe {
    public static final LOTRBannerProtectionRecipe INSTANCE = new LOTRBannerProtectionRecipe();
    public static final MapCodec<LOTRBannerProtectionRecipe> MAP_CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, LOTRBannerProtectionRecipe> STREAM_CODEC =
            StreamCodec.unit(INSTANCE);
    public static final RecipeSerializer<LOTRBannerProtectionRecipe> SERIALIZER =
            new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

    @Override
    public boolean matches(CraftingInput input, Level level) {
        return !assemble(input).isEmpty();
    }

    @Override
    public ItemStack assemble(CraftingInput input) {
        ItemStack baseBanner = null;
        int emptyBanners = 0;
        for (int pass = 0; pass < 2; ++pass) {
            for (ItemStack stack : input.items()) {
                if (stack.isEmpty()) {
                    continue;
                }
                if (!(stack.getItem() instanceof LOTRBannerItem)) {
                    return ItemStack.EMPTY;
                }
                boolean hasData = LOTRBannerItem.hasProtectionData(stack);
                if (pass == 0) {
                    if (hasData) {
                        if (baseBanner != null) {
                            return ItemStack.EMPTY;
                        }
                        baseBanner = stack;
                    }
                    continue;
                }
                if (baseBanner == null || !stack.is(baseBanner.getItem())) {
                    return ItemStack.EMPTY;
                }
                if (!hasData) {
                    ++emptyBanners;
                }
            }
        }
        if (baseBanner == null) {
            return ItemStack.EMPTY;
        }
        if (emptyBanners > 0) {
            return baseBanner.copyWithCount(emptyBanners + 1);
        }
        return new ItemStack(baseBanner.getItem());
    }

    @Override
    public RecipeSerializer<LOTRBannerProtectionRecipe> getSerializer() {
        return SERIALIZER;
    }
}
