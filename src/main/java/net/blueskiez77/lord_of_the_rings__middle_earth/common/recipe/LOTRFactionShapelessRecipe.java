package net.blueskiez77.lord_of_the_rings__middle_earth.common.recipe;

import java.util.List;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
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
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapelessCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;

// The ShapelessOreRecipe half of the faction lists: mirrors vanilla ShapelessRecipe exactly, plus a table field, for the same reason LOTRFactionCraftingRecipe mirrors ShapedRecipe. The 1.7.10 lists held both kinds side by side (e.g. mossy bricks: brick + vine), so a table needs both.
public class LOTRFactionShapelessRecipe extends NormalCraftingRecipe {

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
        super(commonInfo, bookInfo);
        this.table = table;
        this.result = result;
        this.ingredients = ingredients;
    }

    public LOTRCraftingTable table() {
        return table;
    }

    @Override
    public RecipeSerializer<LOTRFactionShapelessRecipe> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public RecipeType<CraftingRecipe> getType() {
        return LOTRRecipeTypes.forTable(table);
    }

    @Override
    protected PlacementInfo createPlacementInfo() {
        return PlacementInfo.create(ingredients);
    }

    // Same shape as ShapelessRecipe.matches: a one-ingredient fast path, then the stacked-contents solver.
    @Override
    public boolean matches(CraftingInput input, Level level) {
        if (input.ingredientCount() != ingredients.size()) {
            return false;
        }
        if (input.size() == 1 && ingredients.size() == 1) {
            return ingredients.getFirst().test(input.getItem(0));
        }
        return input.stackedContents().canCraft(this, null);
    }

    @Override
    public ItemStack assemble(CraftingInput input) {
        return result.create();
    }

    // The icon is the faction table, not the vanilla one, as in LOTRFactionCraftingRecipe.
    @Override
    public List<RecipeDisplay> display() {
        return List.of(new ShapelessCraftingRecipeDisplay(
                ingredients.stream().map(Ingredient::display).toList(),
                new SlotDisplay.ItemStackSlotDisplay(result),
                new SlotDisplay.ItemSlotDisplay(table.block().asItem() == Items.AIR
                        ? Items.CRAFTING_TABLE : table.block().asItem())));
    }
}
