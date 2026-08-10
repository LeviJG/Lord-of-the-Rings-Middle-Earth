package net.blueskiez77.lord_of_the_rings__middle_earth.common.recipe;

import java.util.List;
import java.util.Optional;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.NormalCraftingRecipe;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapedCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;

// Mirrors vanilla ShapedRecipe exactly, plus a table field. getType() returns a per-table RecipeType so the faction menu's getRecipeFor lookup finds only that table's recipes -- no post-filtering, which matters because getRecipeFor returns the FIRST match and would otherwise silently drop recipes.
public class LOTRFactionCraftingRecipe extends NormalCraftingRecipe {

    public static final MapCodec<LOTRFactionCraftingRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
                    Recipe.CommonInfo.MAP_CODEC.forGetter(o -> o.commonInfo),
                    CraftingRecipe.CraftingBookInfo.MAP_CODEC.forGetter(o -> o.bookInfo),
                    LOTRCraftingTable.CODEC.fieldOf("table").forGetter(o -> o.table),
                    ShapedRecipePattern.MAP_CODEC.forGetter(o -> o.pattern),
                    ItemStackTemplate.CODEC.fieldOf("result").forGetter(o -> o.result))
            .apply(i, LOTRFactionCraftingRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, LOTRFactionCraftingRecipe> STREAM_CODEC =
            StreamCodec.composite(
                    Recipe.CommonInfo.STREAM_CODEC, o -> o.commonInfo,
                    CraftingRecipe.CraftingBookInfo.STREAM_CODEC, o -> o.bookInfo,
                    LOTRCraftingTable.STREAM_CODEC, o -> o.table,
                    ShapedRecipePattern.STREAM_CODEC, o -> o.pattern,
                    ItemStackTemplate.STREAM_CODEC, o -> o.result,
                    LOTRFactionCraftingRecipe::new);

    public static final RecipeSerializer<LOTRFactionCraftingRecipe> SERIALIZER =
            new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

    private final LOTRCraftingTable table;
    private final ShapedRecipePattern pattern;
    private final ItemStackTemplate result;

    public LOTRFactionCraftingRecipe(Recipe.CommonInfo commonInfo, CraftingRecipe.CraftingBookInfo bookInfo,
                                     LOTRCraftingTable table, ShapedRecipePattern pattern, ItemStackTemplate result) {
        super(commonInfo, bookInfo);
        this.table = table;
        this.pattern = pattern;
        this.result = result;
    }

    public LOTRCraftingTable table() {
        return table;
    }

    @Override
    public RecipeSerializer<LOTRFactionCraftingRecipe> getSerializer() {
        return SERIALIZER;
    }

    // Must return RecipeType<CraftingRecipe>, matching CraftingRecipe.getType() exactly -- a narrower type parameter is a compile error, not a covariant override.
    @Override
    public RecipeType<CraftingRecipe> getType() {
        return LOTRRecipeTypes.forTable(table);
    }

    public List<Optional<Ingredient>> getIngredients() {
        return pattern.ingredients();
    }

    @Override
    protected PlacementInfo createPlacementInfo() {
        return PlacementInfo.createFromOptionals(pattern.ingredients());
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        return pattern.matches(input);
    }

    @Override
    public ItemStack assemble(CraftingInput input) {
        return result.create();
    }

    public int getWidth() {
        return pattern.width();
    }

    public int getHeight() {
        return pattern.height();
    }

    // The icon is the faction table, not the vanilla one, so the recipe book shows where it can be made.
    @Override
    public List<RecipeDisplay> display() {
        return List.of(new ShapedCraftingRecipeDisplay(
                pattern.width(), pattern.height(),
                pattern.ingredients().stream()
                        .map(e -> (SlotDisplay) e.map(Ingredient::display).orElse(SlotDisplay.Empty.INSTANCE))
                        .toList(),
                new SlotDisplay.ItemStackSlotDisplay(result),
                new SlotDisplay.ItemSlotDisplay(table.block().asItem() == Items.AIR
                        ? Items.CRAFTING_TABLE : table.block().asItem())));
    }
}