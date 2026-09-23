package net.blueskiez77.lord_of_the_rings__middle_earth.common.recipe;

import java.util.List;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapelessCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;

/**
 * The shapeless half of a faction table's recipe list -- LOTRRecipes added
 * ShapelessOreRecipes to those lists as well as shaped ones (the hobbit
 * pancakes, the Torog stew). Vanilla's shapeless matching, plus the table, and
 * the same per-table recipe type as {@link LOTRFactionCraftingRecipe}.
 */
public class LOTRFactionShapelessRecipe extends ShapelessRecipe {
    public static final MapCodec<LOTRFactionShapelessRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
                    Recipe.CommonInfo.MAP_CODEC.forGetter(o -> o.commonInfo),
                    CraftingRecipe.CraftingBookInfo.MAP_CODEC.forGetter(o -> o.bookInfo),
                    LOTRCraftingTable.CODEC.fieldOf("table").forGetter(o -> o.table),
                    ItemStackTemplate.CODEC.fieldOf("result").forGetter(o -> o.result),
                    Ingredient.CODEC.listOf(1, 9).fieldOf("ingredients").forGetter(o -> o.ingredients))
            .apply(i, LOTRFactionShapelessRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, LOTRFactionShapelessRecipe> STREAM_CODEC =
            StreamCodec.composite(
                    Recipe.CommonInfo.STREAM_CODEC, o -> o.commonInfo,
                    CraftingRecipe.CraftingBookInfo.STREAM_CODEC, o -> o.bookInfo,
                    LOTRCraftingTable.STREAM_CODEC, o -> o.table,
                    ItemStackTemplate.STREAM_CODEC, o -> o.result,
                    Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), o -> o.ingredients,
                    LOTRFactionShapelessRecipe::new);

    public static final RecipeSerializer<LOTRFactionShapelessRecipe> SERIALIZER =
            new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

    private final LOTRCraftingTable table;
    private final ItemStackTemplate result;
    private final List<Ingredient> ingredients;

    public LOTRFactionShapelessRecipe(Recipe.CommonInfo commonInfo, CraftingRecipe.CraftingBookInfo bookInfo,
                                      LOTRCraftingTable table, ItemStackTemplate result, List<Ingredient> ingredients) {
        super(commonInfo, bookInfo, result, ingredients);
        this.table = table;
        this.result = result;
        this.ingredients = ingredients;
    }

    public LOTRCraftingTable table() {
        return table;
    }

    @Override
    public RecipeSerializer<ShapelessRecipe> getSerializer() {
        @SuppressWarnings("unchecked")
        RecipeSerializer<ShapelessRecipe> serializer = (RecipeSerializer<ShapelessRecipe>) (RecipeSerializer<?>) SERIALIZER;
        return serializer;
    }

    @Override
    public RecipeType<CraftingRecipe> getType() {
        return LOTRRecipeTypes.forTable(table);
    }

    // As the shaped faction recipe does: the recipe book shows the faction table.
    @Override
    public List<RecipeDisplay> display() {
        return List.of(new ShapelessCraftingRecipeDisplay(
                ingredients.stream().map(Ingredient::display).toList(),
                new SlotDisplay.ItemStackSlotDisplay(result),
                new SlotDisplay.ItemSlotDisplay(table.block().asItem() == Items.AIR
                        ? Items.CRAFTING_TABLE : table.block().asItem())));
    }
}
