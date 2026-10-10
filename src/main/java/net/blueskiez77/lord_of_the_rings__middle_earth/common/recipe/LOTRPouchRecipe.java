package net.blueskiez77.lord_of_the_rings__middle_earth.common.recipe;

import java.util.ArrayList;
import java.util.List;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRPouchItem;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import org.jspecify.annotations.Nullable;

/**
 * LOTRRecipesPouch: pouches and dyes, anywhere in the grid. One pouch and some dyes give the pouch
 * back dyed: the average of its own colour (its brown, if undyed) and the dyes', brightened as
 * leather armour's dyeing was. Two or more pouches, if their slots add up to no more than a large
 * pouch's, give one pouch of the combined size holding all their goods, coloured the same way if any
 * of them was dyed or a dye went in. At a faction's table (the `faction_pouch` form) one pouch alone
 * will do, and the result takes the faction's colour whatever else is in the grid.
 *
 * <p>The dyes' colours are vanilla's dye colours of today, where the original used 1.7.10's fleece
 * colours.
 */
public class LOTRPouchRecipe extends CustomRecipe {
    public static final LOTRPouchRecipe INSTANCE = new LOTRPouchRecipe(null);
    public static final MapCodec<LOTRPouchRecipe> MAP_CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, LOTRPouchRecipe> STREAM_CODEC = StreamCodec.unit(INSTANCE);
    public static final RecipeSerializer<LOTRPouchRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

    public static final MapCodec<LOTRPouchRecipe> FACTION_MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            LOTRCraftingTable.CODEC.fieldOf("table").forGetter(o -> o.table)).apply(i, LOTRPouchRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, LOTRPouchRecipe> FACTION_STREAM_CODEC =
            LOTRCraftingTable.STREAM_CODEC.map(LOTRPouchRecipe::new, o -> o.table).cast();
    public static final RecipeSerializer<LOTRPouchRecipe> FACTION_SERIALIZER =
            new RecipeSerializer<>(FACTION_MAP_CODEC, FACTION_STREAM_CODEC);

    private final @Nullable LOTRCraftingTable table;

    public LOTRPouchRecipe(@Nullable LOTRCraftingTable table) {
        this.table = table;
    }

    private boolean hasOverrideColor() {
        return this.table != null;
    }

    private static int combinedSize(List<ItemStack> pouches) {
        int size = 0;
        for (ItemStack pouch : pouches) {
            size += ((LOTRPouchItem) pouch.getItem()).size() + 1;
        }
        return size - 1;
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        List<ItemStack> pouches = new ArrayList<>();
        int dyes = 0;
        for (ItemStack stack : input.items()) {
            if (stack.isEmpty()) {
                continue;
            }
            if (LOTRPouchItem.isPouch(stack)) {
                pouches.add(stack);
            } else if (stack.has(DataComponents.DYE)) {
                ++dyes;
            } else {
                return false;
            }
        }
        if (pouches.isEmpty()) {
            return false;
        }
        if (pouches.size() == 1) {
            return hasOverrideColor() || dyes > 0;
        }
        return LOTRPouchItem.capacityForSize(combinedSize(pouches)) <= LOTRPouchItem.getMaxPouchCapacity();
    }

    @Override
    public ItemStack assemble(CraftingInput input) {
        List<ItemStack> pouches = new ArrayList<>();
        int[] rgb = new int[3];
        int totalColor = 0;
        int coloredItems = 0;
        boolean anyDye = false;
        for (ItemStack stack : input.items()) {
            if (stack.isEmpty()) {
                continue;
            }
            int color;
            if (LOTRPouchItem.isPouch(stack)) {
                pouches.add(stack);
                color = LOTRPouchItem.getPouchColor(stack);
                anyDye |= LOTRPouchItem.isPouchDyed(stack);
            } else {
                DyeColor dye = stack.get(DataComponents.DYE);
                if (dye == null) {
                    return ItemStack.EMPTY;
                }
                color = dye.getTextureDiffuseColor() & 0xFFFFFF;
                anyDye = true;
            }
            int r = color >> 16 & 0xFF;
            int g = color >> 8 & 0xFF;
            int b = color & 0xFF;
            totalColor += Math.max(r, Math.max(g, b));
            rgb[0] += r;
            rgb[1] += g;
            rgb[2] += b;
            ++coloredItems;
        }
        if (pouches.isEmpty()) {
            return ItemStack.EMPTY;
        }
        ItemStack pouch;
        if (pouches.size() == 1) {
            pouch = pouches.getFirst().copyWithCount(1);
        } else {
            int size = combinedSize(pouches);
            pouch = new ItemStack(LOTRPouchItem.ofSize(size));
            List<ItemStack> contents = new ArrayList<>();
            for (ItemStack craftingPouch : pouches) {
                for (ItemStack item : LOTRPouchItem.getContents(craftingPouch)) {
                    if (!item.isEmpty()) {
                        contents.add(item.copy());
                    }
                }
            }
            LOTRPouchItem.setContents(pouch, contents);
        }
        if (this.table != null) {
            LOTRPouchItem.setPouchColor(pouch, this.table.faction().getFactionColor());
        } else if (anyDye && coloredItems > 0) {
            int r = rgb[0] / coloredItems;
            int g = rgb[1] / coloredItems;
            int b = rgb[2] / coloredItems;
            float averageColor = (float) totalColor / coloredItems;
            float maxColor = Math.max(r, Math.max(g, b));
            r = (int) (r * averageColor / maxColor);
            g = (int) (g * averageColor / maxColor);
            b = (int) (b * averageColor / maxColor);
            LOTRPouchItem.setPouchColor(pouch, (r << 16) + (g << 8) + b);
        }
        return pouch;
    }

    @Override
    public boolean showNotification() {
        return this.table == null && super.showNotification();
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return this.table != null ? LOTRRecipeTypes.FACTION_CRAFTING : super.recipeBookCategory();
    }

    @Override
    @SuppressWarnings("unchecked")
    public RecipeType<CraftingRecipe> getType() {
        return this.table != null ? LOTRRecipeTypes.forTable(this.table) : super.getType();
    }

    @Override
    public RecipeSerializer<LOTRPouchRecipe> getSerializer() {
        return this.table != null ? FACTION_SERIALIZER : SERIALIZER;
    }
}
