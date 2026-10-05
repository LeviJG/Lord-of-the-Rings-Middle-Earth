package net.blueskiez77.lord_of_the_rings__middle_earth.common.recipe;

import java.util.List;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRHaradTurbanItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMiscItems;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.level.Level;

/**
 * LOTRRecipeHaradTurbanOrnament, at the Near Harad, Umbar and Gulf tables: a
 * turban with no ornament and one gold nugget, and nothing else, give the
 * turban back set with a gold ornament.
 */
public class LOTRTurbanOrnamentRecipe implements CraftingRecipe {

    public static final MapCodec<LOTRTurbanOrnamentRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
                    LOTRCraftingTable.CODEC.fieldOf("table").forGetter(o -> o.table))
            .apply(i, LOTRTurbanOrnamentRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, LOTRTurbanOrnamentRecipe> STREAM_CODEC =
            StreamCodec.composite(LOTRCraftingTable.STREAM_CODEC, o -> o.table, LOTRTurbanOrnamentRecipe::new);

    public static final RecipeSerializer<LOTRTurbanOrnamentRecipe> SERIALIZER =
            new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

    private final LOTRCraftingTable table;

    public LOTRTurbanOrnamentRecipe(LOTRCraftingTable table) {
        this.table = table;
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        return !assemble(input).isEmpty();
    }

    @Override
    public ItemStack assemble(CraftingInput input) {
        ItemStack turban = null;
        ItemStack gold = null;
        for (ItemStack stack : input.items()) {
            if (stack.isEmpty()) {
                continue;
            }
            if (stack.is(LOTRMiscItems.HARAD_TURBAN) && !LOTRHaradTurbanItem.hasOrnament(stack)) {
                if (turban != null) {
                    return ItemStack.EMPTY;
                }
                turban = stack;
                continue;
            }
            // "nuggetGold": vanilla's gold nugget.
            if (stack.is(Items.GOLD_NUGGET)) {
                if (gold != null) {
                    return ItemStack.EMPTY;
                }
                gold = stack;
                continue;
            }
            return ItemStack.EMPTY;
        }
        if (turban == null || gold == null) {
            return ItemStack.EMPTY;
        }
        return LOTRHaradTurbanItem.setHasOrnament(turban.copyWithCount(1), true);
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public String group() {
        return "";
    }

    @Override
    public CraftingBookCategory category() {
        return CraftingBookCategory.MISC;
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public List<RecipeDisplay> display() {
        return List.of();
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return LOTRRecipeTypes.FACTION_CRAFTING;
    }

    @Override
    public RecipeSerializer<LOTRTurbanOrnamentRecipe> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public RecipeType<CraftingRecipe> getType() {
        return LOTRRecipeTypes.forTable(this.table);
    }
}
