package net.blueskiez77.lord_of_the_rings__middle_earth.common.recipe;

import java.util.List;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.DyeRecipe;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.level.Level;

/**
 * LOTRRecipeHaradRobesDye, at a faction table: one robe piece and any dyes
 * give the piece back in the blended colour. The blend is the 1.7.10 armour dye
 * one, which vanilla's dye recipe still uses, so this is one of those carrying
 * the table -- the Near Harad tables for the robes and turban, Rhûn for the
 * kaftan (the original's {@code LOTRMaterial.KAFTAN} instance).
 */
public class LOTRFactionDyeRecipe implements CraftingRecipe {
    public static final MapCodec<LOTRFactionDyeRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
                    DyeRecipe.MAP_CODEC.forGetter(o -> o.delegate),
                    LOTRCraftingTable.CODEC.fieldOf("table").forGetter(o -> o.table))
            .apply(i, LOTRFactionDyeRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, LOTRFactionDyeRecipe> STREAM_CODEC =
            StreamCodec.composite(
                    DyeRecipe.STREAM_CODEC, o -> o.delegate,
                    LOTRCraftingTable.STREAM_CODEC, o -> o.table,
                    LOTRFactionDyeRecipe::new);

    public static final RecipeSerializer<LOTRFactionDyeRecipe> SERIALIZER =
            new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

    private final DyeRecipe delegate;
    private final LOTRCraftingTable table;

    public LOTRFactionDyeRecipe(DyeRecipe delegate, LOTRCraftingTable table) {
        this.delegate = delegate;
        this.table = table;
    }

    public LOTRCraftingTable table() {
        return table;
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
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
    public RecipeBookCategory recipeBookCategory() {
        return LOTRRecipeTypes.FACTION_CRAFTING;
    }

    @Override
    public RecipeSerializer<LOTRFactionDyeRecipe> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public RecipeType<CraftingRecipe> getType() {
        return LOTRRecipeTypes.forTable(table);
    }
}
