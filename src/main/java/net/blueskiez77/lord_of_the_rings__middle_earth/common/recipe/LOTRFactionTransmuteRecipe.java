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
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.TransmuteRecipe;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.level.Level;

/**
 * LOTRRecipePoisonWeapon, at a faction table: one item plus one catalyst makes
 * a different item that keeps everything the first one had -- its wear, its
 * name, its enchantments. That is exactly vanilla's transmute recipe, so this
 * is one of those carrying the table it belongs to, with the per-table recipe
 * type the faction menu looks recipes up by.
 */
public class LOTRFactionTransmuteRecipe implements CraftingRecipe {
    public static final MapCodec<LOTRFactionTransmuteRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
                    TransmuteRecipe.MAP_CODEC.forGetter(o -> o.delegate),
                    LOTRCraftingTable.CODEC.fieldOf("table").forGetter(o -> o.table))
            .apply(i, LOTRFactionTransmuteRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, LOTRFactionTransmuteRecipe> STREAM_CODEC =
            StreamCodec.composite(
                    TransmuteRecipe.STREAM_CODEC, o -> o.delegate,
                    LOTRCraftingTable.STREAM_CODEC, o -> o.table,
                    LOTRFactionTransmuteRecipe::new);

    public static final RecipeSerializer<LOTRFactionTransmuteRecipe> SERIALIZER =
            new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

    private final TransmuteRecipe delegate;
    private final LOTRCraftingTable table;

    public LOTRFactionTransmuteRecipe(TransmuteRecipe delegate, LOTRCraftingTable table) {
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
    public RecipeSerializer<LOTRFactionTransmuteRecipe> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public RecipeType<CraftingRecipe> getType() {
        return LOTRRecipeTypes.forTable(table);
    }
}
