package net.blueskiez77.lord_of_the_rings__middle_earth.common.recipe;

import java.util.EnumMap;
import java.util.Map;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;

// One RecipeType per table. A single shared type would force the menu to filter after lookup, but getRecipeFor returns only the first match, so a recipe from another table could mask a valid one.
public final class LOTRRecipeTypes {

    private static final Map<LOTRCraftingTable, RecipeType<CraftingRecipe>> TYPES =
            new EnumMap<>(LOTRCraftingTable.class);

    private LOTRRecipeTypes() {
    }

    public static RecipeType<CraftingRecipe> forTable(LOTRCraftingTable table) {
        RecipeType<CraftingRecipe> type = TYPES.get(table);
        if (type == null) {
            throw new IllegalStateException("Recipe type requested before LOTRRecipeTypes.init(): " + table);
        }
        return type;
    }

    public static void init() {
        for (LOTRCraftingTable table : LOTRCraftingTable.values()) {
            RecipeType<CraftingRecipe> type = new RecipeType<>() {
                @Override
                public String toString() {
                    return table.id().toString();
                }
            };
            Registry.register(BuiltInRegistries.RECIPE_TYPE, table.id(), type);
            TYPES.put(table, type);
        }
        Registry.register(BuiltInRegistries.RECIPE_SERIALIZER,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "faction_crafting"),
                LOTRFactionCraftingRecipe.SERIALIZER);
    }
}