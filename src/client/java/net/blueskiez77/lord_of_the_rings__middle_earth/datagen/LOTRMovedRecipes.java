package net.blueskiez77.lord_of_the_rings__middle_earth.datagen;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.recipe.LOTRCraftingTable;

import net.minecraft.data.recipes.RecipeCategory;

/**
 * Recipes the port first wrote as JSON under src/main/resources, before
 * LOTRTranscribedRecipes existed, now generated like the rest. Each keeps the
 * id and recipe-book category its JSON file had. Parity with LOTRRecipes.java is tracked for both by
 * tools/parity_recipes.py.
 *
 * <p>Still hand-written (see docs/hand_written_resources.md): the drinks, whose
 * results carry drink components; recipes with ingredient alternatives; and
 * the special recipe types.
 */
final class LOTRMovedRecipes {

    private LOTRMovedRecipes() {
    }

    static void build(LOTRTranscribedRecipes r) {
        // block
        r.in(RecipeCategory.BUILDING_BLOCKS).shaped(null, "block/termite_mound", "lotr:termite_mound", 1, LOTRTranscribedRecipes.rows(" X ", "XYX", " X "), 'X', "lotr:exploding_termite", 'Y', "minecraft:stone");
        // combat
        r.in(RecipeCategory.COMBAT).shaped(null, "combat/bronze_battleaxe", "lotr:bronze_battleaxe", 1, LOTRTranscribedRecipes.rows("XXX", "XYX", " Y "), 'X', "lotr:bronze_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(null, "combat/bronze_boots", "lotr:bronze_boots", 1, LOTRTranscribedRecipes.rows("X X", "X X"), 'X', "lotr:bronze_ingot");
        r.in(RecipeCategory.COMBAT).shaped(null, "combat/bronze_chestplate", "lotr:bronze_chestplate", 1, LOTRTranscribedRecipes.rows("X X", "XXX", "XXX"), 'X', "lotr:bronze_ingot");
        r.in(RecipeCategory.COMBAT).shaped(null, "combat/bronze_crossbow", "lotr:bronze_crossbow", 1, LOTRTranscribedRecipes.rows("XXY", "ZYX", "YZX"), 'X', "lotr:bronze_ingot", 'Y', "#lotr:sticks", 'Z', "minecraft:string");
        r.in(RecipeCategory.COMBAT).shaped(null, "combat/bronze_dagger", "lotr:bronze_dagger", 1, LOTRTranscribedRecipes.rows("X", "Y"), 'X', "lotr:bronze_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(null, "combat/bronze_helmet", "lotr:bronze_helmet", 1, LOTRTranscribedRecipes.rows("XXX", "X X"), 'X', "lotr:bronze_ingot");
        r.in(RecipeCategory.COMBAT).shaped(null, "combat/bronze_leggings", "lotr:bronze_leggings", 1, LOTRTranscribedRecipes.rows("XXX", "X X", "X X"), 'X', "lotr:bronze_ingot");
        r.in(RecipeCategory.COMBAT).shaped(null, "combat/bronze_spear", "lotr:bronze_spear", 1, LOTRTranscribedRecipes.rows("  X", " Y ", "Y  "), 'X', "lotr:bronze_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(null, "combat/bronze_sword", "lotr:bronze_sword", 1, LOTRTranscribedRecipes.rows("X", "X", "Y"), 'X', "lotr:bronze_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(null, "combat/bronze_throwing_axe", "lotr:bronze_throwing_axe", 1, LOTRTranscribedRecipes.rows(" X ", " YX", "Y  "), 'X', "lotr:bronze_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(null, "combat/command_horn", "lotr:command_horn", 1, LOTRTranscribedRecipes.rows("XYX"), 'X', "lotr:bronze_ingot", 'Y', "#lotr:horns");
        r.in(RecipeCategory.COMBAT).shaped(null, "combat/command_sword", "lotr:command_sword", 1, LOTRTranscribedRecipes.rows("X", "Y", "Z"), 'X', "minecraft:iron_ingot", 'Y', "lotr:bronze_ingot", 'Z', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(null, "combat/diamond_horse_armor", "lotr:diamond_horse_armor", 1, LOTRTranscribedRecipes.rows("X  ", "XYX", "XXX"), 'X', "minecraft:diamond", 'Y', "minecraft:leather");
        r.in(RecipeCategory.COMBAT).shaped(null, "combat/fur_boots", "lotr:fur_boots", 1, LOTRTranscribedRecipes.rows("X X", "X X"), 'X', "lotr:fur");
        r.in(RecipeCategory.COMBAT).shaped(null, "combat/fur_hat", "lotr:fur_hat", 1, LOTRTranscribedRecipes.rows("XXX", "X X"), 'X', "lotr:fur");
        r.in(RecipeCategory.COMBAT).shaped(null, "combat/fur_leggings", "lotr:fur_leggings", 1, LOTRTranscribedRecipes.rows("XXX", "X X", "X X"), 'X', "lotr:fur");
        r.in(RecipeCategory.COMBAT).shaped(null, "combat/fur_tunic", "lotr:fur_tunic", 1, LOTRTranscribedRecipes.rows("X X", "XXX", "XXX"), 'X', "lotr:fur");
        r.in(RecipeCategory.COMBAT).shaped(null, "combat/gemsbok_hide_boots", "lotr:gemsbok_hide_boots", 1, LOTRTranscribedRecipes.rows("X X", "X X"), 'X', "lotr:gemsbok_hide");
        r.in(RecipeCategory.COMBAT).shaped(null, "combat/gemsbok_hide_chestplate", "lotr:gemsbok_hide_chestplate", 1, LOTRTranscribedRecipes.rows("X X", "XXX", "XXX"), 'X', "lotr:gemsbok_hide");
        r.in(RecipeCategory.COMBAT).shaped(null, "combat/gemsbok_hide_helmet", "lotr:gemsbok_hide_helmet", 1, LOTRTranscribedRecipes.rows("Y Y", "XXX", "X X"), 'X', "lotr:gemsbok_hide", 'Y', "lotr:gemsbok_horn");
        r.in(RecipeCategory.COMBAT).shaped(null, "combat/gemsbok_hide_leggings", "lotr:gemsbok_hide_leggings", 1, LOTRTranscribedRecipes.rows("XXX", "X X", "X X"), 'X', "lotr:gemsbok_hide");
        r.in(RecipeCategory.COMBAT).shaped(null, "combat/golden_horse_armor", "minecraft:golden_horse_armor", 1, LOTRTranscribedRecipes.rows("X  ", "XYX", "XXX"), 'X', "minecraft:gold_ingot", 'Y', "minecraft:leather");
        r.in(RecipeCategory.COMBAT).shaped(null, "combat/iron_horse_armor", "minecraft:iron_horse_armor", 1, LOTRTranscribedRecipes.rows("X  ", "XYX", "XXX"), 'X', "minecraft:iron_ingot", 'Y', "minecraft:leather");
        r.in(RecipeCategory.COMBAT).shaped(null, "combat/iron_spear", "lotr:iron_spear", 1, LOTRTranscribedRecipes.rows("  X", " Y ", "Y  "), 'X', "minecraft:iron_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(null, "combat/mithril_boots", "lotr:mithril_boots", 1, LOTRTranscribedRecipes.rows("X X", "X X"), 'X', "lotr:mithril_mail");
        r.in(RecipeCategory.COMBAT).shaped(null, "combat/mithril_chestplate", "lotr:mithril_chestplate", 1, LOTRTranscribedRecipes.rows("X X", "XXX", "XXX"), 'X', "lotr:mithril_mail");
        r.in(RecipeCategory.COMBAT).shaped(null, "combat/mithril_halberd", "lotr:mithril_halberd", 1, LOTRTranscribedRecipes.rows(" XX", " YX", "Y  "), 'X', "lotr:mithril", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(null, "combat/mithril_helmet", "lotr:mithril_helmet", 1, LOTRTranscribedRecipes.rows("XXX", "X X"), 'X', "lotr:mithril_mail");
        r.in(RecipeCategory.COMBAT).shaped(null, "combat/mithril_horse_armor", "lotr:mithril_horse_armor", 1, LOTRTranscribedRecipes.rows("X  ", "XYX", "XXX"), 'X', "lotr:mithril_mail", 'Y', "minecraft:leather");
        r.in(RecipeCategory.COMBAT).shaped(null, "combat/mithril_leggings", "lotr:mithril_leggings", 1, LOTRTranscribedRecipes.rows("XXX", "X X", "X X"), 'X', "lotr:mithril_mail");
        r.in(RecipeCategory.COMBAT).shaped(null, "combat/rolling_pin", "lotr:rolling_pin", 1, LOTRTranscribedRecipes.rows("XYX"), 'X', "#lotr:sticks", 'Y', "#minecraft:planks");
        r.in(RecipeCategory.COMBAT).shaped(null, "combat/stone_spear", "lotr:stone_spear", 1, LOTRTranscribedRecipes.rows("  X", " Y ", "Y  "), 'X', "minecraft:cobblestone", 'Y', "#lotr:sticks");
        // decoration
        r.in(RecipeCategory.MISC).shaped(null, "decoration/bronze_bars", "lotr:bronze_bars", 16, LOTRTranscribedRecipes.rows("XXX", "XXX"), 'X', "lotr:bronze_ingot");
        r.in(RecipeCategory.MISC).shaped(null, "decoration/bronze_bird_cage", "lotr:bronze_bird_cage", 1, LOTRTranscribedRecipes.rows("YYY", "Y Y", "XXX"), 'X', "lotr:bronze_ingot", 'Y', "lotr:bronze_bars");
        r.in(RecipeCategory.MISC).shaped(null, "decoration/bronze_chandelier", "lotr:bronze_chandelier", 2, LOTRTranscribedRecipes.rows(" X ", "YZY"), 'X', "#lotr:sticks", 'Y', "minecraft:torch", 'Z', "lotr:bronze_ingot");
        r.in(RecipeCategory.MISC).shaped(null, "decoration/silver_bars", "lotr:silver_bars", 16, LOTRTranscribedRecipes.rows("XXX", "XXX"), 'X', "lotr:silver_ingot");
        r.in(RecipeCategory.MISC).shaped(null, "decoration/silver_bird_cage", "lotr:silver_bird_cage", 1, LOTRTranscribedRecipes.rows("YYY", "Y Y", "XXX"), 'X', "lotr:silver_ingot", 'Y', "lotr:silver_bars");
        r.in(RecipeCategory.MISC).shaped(null, "decoration/silver_chandelier", "lotr:silver_chandelier", 2, LOTRTranscribedRecipes.rows(" X ", "YZY"), 'X', "#lotr:sticks", 'Y', "minecraft:torch", 'Z', "lotr:silver_ingot");
        r.in(RecipeCategory.MISC).shapeless(null, "decoration/treasure_copper", "lotr:treasure_copper", 1, "lotr:treasure_copper_carpet", "lotr:treasure_copper_carpet", "lotr:treasure_copper_carpet", "lotr:treasure_copper_carpet", "lotr:treasure_copper_carpet", "lotr:treasure_copper_carpet", "lotr:treasure_copper_carpet", "lotr:treasure_copper_carpet");
        r.in(RecipeCategory.MISC).shaped(null, "decoration/treasure_copper_carpet", "lotr:treasure_copper_carpet", 8, LOTRTranscribedRecipes.rows("XX", "XX"), 'X', "minecraft:copper_ingot");
        r.in(RecipeCategory.MISC).shapeless(null, "decoration/treasure_gold", "lotr:treasure_gold", 1, "lotr:treasure_gold_carpet", "lotr:treasure_gold_carpet", "lotr:treasure_gold_carpet", "lotr:treasure_gold_carpet", "lotr:treasure_gold_carpet", "lotr:treasure_gold_carpet", "lotr:treasure_gold_carpet", "lotr:treasure_gold_carpet");
        r.in(RecipeCategory.MISC).shaped(null, "decoration/treasure_gold_carpet", "lotr:treasure_gold_carpet", 8, LOTRTranscribedRecipes.rows("XX", "XX"), 'X', "minecraft:gold_ingot");
        r.in(RecipeCategory.MISC).shapeless(null, "decoration/treasure_silver", "lotr:treasure_silver", 1, "lotr:treasure_silver_carpet", "lotr:treasure_silver_carpet", "lotr:treasure_silver_carpet", "lotr:treasure_silver_carpet", "lotr:treasure_silver_carpet", "lotr:treasure_silver_carpet", "lotr:treasure_silver_carpet", "lotr:treasure_silver_carpet");
        r.in(RecipeCategory.MISC).shaped(null, "decoration/treasure_silver_carpet", "lotr:treasure_silver_carpet", 8, LOTRTranscribedRecipes.rows("XX", "XX"), 'X', "lotr:silver_ingot");
        // drink/apple_juice
        r.in(RecipeCategory.MISC).shapeless(null, "drink/apple_juice/mug", "lotr:apple_juice", 1, "lotr:mug", "#lotr:apples", "#lotr:apples");
        // drink/blackberry_juice
        r.in(RecipeCategory.MISC).shapeless(null, "drink/blackberry_juice/mug", "lotr:blackberry_juice", 1, "lotr:mug", "lotr:blackberries", "lotr:blackberries", "lotr:blackberries");
        // drink/blueberry_juice
        r.in(RecipeCategory.MISC).shapeless(null, "drink/blueberry_juice/mug", "lotr:blueberry_juice", 1, "lotr:mug", "lotr:blueberries", "lotr:blueberries", "lotr:blueberries");
        // drink/cranberry_juice
        r.in(RecipeCategory.MISC).shapeless(null, "drink/cranberry_juice/mug", "lotr:cranberry_juice", 1, "lotr:mug", "lotr:cranberries", "lotr:cranberries", "lotr:cranberries");
        // drink/elderberry_juice
        r.in(RecipeCategory.MISC).shapeless(null, "drink/elderberry_juice/mug", "lotr:elderberry_juice", 1, "lotr:mug", "lotr:elderberries", "lotr:elderberries", "lotr:elderberries");
        // drink/green_grape_juice
        r.in(RecipeCategory.MISC).shapeless(null, "drink/green_grape_juice/mug", "lotr:green_grape_juice", 1, "lotr:mug", "lotr:green_grapes", "lotr:green_grapes", "lotr:green_grapes");
        // drink/mango_juice
        r.in(RecipeCategory.MISC).shapeless(null, "drink/mango_juice/mug", "lotr:mango_juice", 1, "lotr:mug", "lotr:mango", "lotr:mango");
        // drink/orange_juice
        r.in(RecipeCategory.MISC).shapeless(null, "drink/orange_juice/mug", "lotr:orange_juice", 1, "lotr:mug", "lotr:orange", "lotr:orange");
        // drink/pomegranate_juice
        r.in(RecipeCategory.MISC).shapeless(null, "drink/pomegranate_juice/mug", "lotr:pomegranate_juice", 1, "lotr:mug", "lotr:pomegranate", "lotr:pomegranate");
        // drink/raspberry_juice
        r.in(RecipeCategory.MISC).shapeless(null, "drink/raspberry_juice/mug", "lotr:raspberry_juice", 1, "lotr:mug", "lotr:raspberries", "lotr:raspberries", "lotr:raspberries");
        // drink/red_grape_juice
        r.in(RecipeCategory.MISC).shapeless(null, "drink/red_grape_juice/mug", "lotr:red_grape_juice", 1, "lotr:mug", "lotr:red_grapes", "lotr:red_grapes", "lotr:red_grapes");
        // faction/angmar/block
        r.in(RecipeCategory.BUILDING_BLOCKS).shaped(LOTRCraftingTable.ANGMAR, "faction/angmar/block/orc_plating_iron", "lotr:orc_plating_iron", 8, LOTRTranscribedRecipes.rows("XX", "XX"), 'X', "lotr:orc_steel_ingot");
        // faction/angmar/combat
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.ANGMAR, "faction/angmar/combat/angmar_battleaxe", "lotr:angmar_battleaxe", 1, LOTRTranscribedRecipes.rows("XXX", "XYX", " Y "), 'X', "lotr:orc_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.ANGMAR, "faction/angmar/combat/angmar_boots", "lotr:angmar_boots", 1, LOTRTranscribedRecipes.rows("X X", "X X"), 'X', "lotr:orc_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.ANGMAR, "faction/angmar/combat/angmar_chestplate", "lotr:angmar_chestplate", 1, LOTRTranscribedRecipes.rows("X X", "XXX", "XXX"), 'X', "lotr:orc_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.ANGMAR, "faction/angmar/combat/angmar_dagger", "lotr:angmar_dagger", 1, LOTRTranscribedRecipes.rows("X", "Y"), 'X', "lotr:orc_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.ANGMAR, "faction/angmar/combat/angmar_helmet", "lotr:angmar_helmet", 1, LOTRTranscribedRecipes.rows("XXX", "X X"), 'X', "lotr:orc_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.ANGMAR, "faction/angmar/combat/angmar_leggings", "lotr:angmar_leggings", 1, LOTRTranscribedRecipes.rows("XXX", "X X", "X X"), 'X', "lotr:orc_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.ANGMAR, "faction/angmar/combat/angmar_poleaxe", "lotr:angmar_poleaxe", 1, LOTRTranscribedRecipes.rows(" XX", " YX", "Y  "), 'X', "lotr:orc_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.ANGMAR, "faction/angmar/combat/angmar_spear", "lotr:angmar_spear", 1, LOTRTranscribedRecipes.rows("  X", " Y ", "Y  "), 'X', "lotr:orc_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.ANGMAR, "faction/angmar/combat/angmar_sword", "lotr:angmar_sword", 1, LOTRTranscribedRecipes.rows("X", "X", "Y"), 'X', "lotr:orc_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.ANGMAR, "faction/angmar/combat/angmar_warg_armor", "lotr:angmar_warg_armor", 1, LOTRTranscribedRecipes.rows("X  ", "XYX", "XXX"), 'X', "lotr:orc_steel_ingot", 'Y', "minecraft:leather");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.ANGMAR, "faction/angmar/combat/angmar_warhammer", "lotr:angmar_warhammer", 1, LOTRTranscribedRecipes.rows("XYX", "XYX", " Y "), 'X', "lotr:orc_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shapeless(LOTRCraftingTable.ANGMAR, "faction/angmar/combat/double_strength_orc_bomb", "lotr:double_strength_orc_bomb", 1, "lotr:orc_bomb", "minecraft:gunpowder", "lotr:orc_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.ANGMAR, "faction/angmar/combat/morgul_blade", "lotr:morgul_blade", 1, LOTRTranscribedRecipes.rows("X", "X", "Y"), 'X', "lotr:morgul_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.ANGMAR, "faction/angmar/combat/morgul_boots", "lotr:morgul_boots", 1, LOTRTranscribedRecipes.rows("X X", "X X"), 'X', "lotr:morgul_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.ANGMAR, "faction/angmar/combat/morgul_chestplate", "lotr:morgul_chestplate", 1, LOTRTranscribedRecipes.rows("X X", "XXX", "XXX"), 'X', "lotr:morgul_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.ANGMAR, "faction/angmar/combat/morgul_helmet", "lotr:morgul_helmet", 1, LOTRTranscribedRecipes.rows("XXX", "X X"), 'X', "lotr:morgul_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.ANGMAR, "faction/angmar/combat/morgul_horse_armor", "lotr:morgul_horse_armor", 1, LOTRTranscribedRecipes.rows("X  ", "XYX", "XXX"), 'X', "lotr:morgul_steel_ingot", 'Y', "minecraft:leather");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.ANGMAR, "faction/angmar/combat/morgul_leggings", "lotr:morgul_leggings", 1, LOTRTranscribedRecipes.rows("XXX", "X X", "X X"), 'X', "lotr:morgul_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.ANGMAR, "faction/angmar/combat/orc_bomb", "lotr:orc_bomb", 4, LOTRTranscribedRecipes.rows("XYX", "YXY", "XYX"), 'X', "minecraft:gunpowder", 'Y', "lotr:orc_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.ANGMAR, "faction/angmar/combat/orc_bow", "lotr:orc_bow", 1, LOTRTranscribedRecipes.rows(" XY", "X Y", " XY"), 'X', "lotr:orc_steel_ingot", 'Y', "minecraft:string");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.ANGMAR, "faction/angmar/combat/poisoned_arrow", "lotr:poisoned_arrow", 4, LOTRTranscribedRecipes.rows(" X ", "XYX", " X "), 'X', "minecraft:arrow", 'Y', "lotr:bottle_of_poison");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.ANGMAR, "faction/angmar/combat/poisoned_crossbow_bolt", "lotr:poisoned_crossbow_bolt", 4, LOTRTranscribedRecipes.rows(" X ", "XYX", " X "), 'X', "lotr:crossbow_bolt", 'Y', "lotr:bottle_of_poison");
        r.in(RecipeCategory.COMBAT).shapeless(LOTRCraftingTable.ANGMAR, "faction/angmar/combat/triple_strength_orc_bomb", "lotr:triple_strength_orc_bomb", 1, "lotr:double_strength_orc_bomb", "minecraft:gunpowder", "lotr:orc_steel_ingot");
        // faction/angmar/decoration
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.ANGMAR, "faction/angmar/decoration/morgul_chandelier", "lotr:morgul_chandelier", 2, LOTRTranscribedRecipes.rows(" X ", "YZY"), 'X', "#lotr:sticks", 'Y', "lotr:morgul_torch", 'Z', "lotr:morgul_steel_ingot");
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.ANGMAR, "faction/angmar/decoration/orc_chandelier", "lotr:orc_chandelier", 2, LOTRTranscribedRecipes.rows(" X ", "YZY"), 'X', "#lotr:sticks", 'Y', "lotr:orc_torch", 'Z', "lotr:orc_steel_ingot");
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.ANGMAR, "faction/angmar/decoration/orc_steel_bars", "lotr:orc_steel_bars", 16, LOTRTranscribedRecipes.rows("XXX", "XXX"), 'X', "lotr:orc_steel_ingot");
        // faction/angmar/food
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.ANGMAR, "faction/angmar/food/maggoty_bread_angmar", "lotr:maggoty_bread", 1, LOTRTranscribedRecipes.rows("XXX"), 'X', "minecraft:wheat");
        // faction/angmar/misc
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.ANGMAR, "faction/angmar/misc/mechanism", "lotr:mechanism", 1, LOTRTranscribedRecipes.rows(" X ", "YZY", " X "), 'X', "minecraft:copper_ingot", 'Y', "minecraft:flint", 'Z', "lotr:orc_steel_ingot");
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.ANGMAR, "faction/angmar/misc/orc_gate", "lotr:orc_gate", 4, LOTRTranscribedRecipes.rows("YYY", "YXY", "YYY"), 'X', "lotr:gate_gears", 'Y', "lotr:orc_steel_ingot");
        // faction/angmar/tool
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.ANGMAR, "faction/angmar/tool/angmar_axe", "lotr:angmar_axe", 1, LOTRTranscribedRecipes.rows("XX", "XY", " Y"), 'X', "lotr:orc_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.ANGMAR, "faction/angmar/tool/angmar_hoe", "lotr:angmar_hoe", 1, LOTRTranscribedRecipes.rows("XX", " Y", " Y"), 'X', "lotr:orc_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.ANGMAR, "faction/angmar/tool/angmar_pickaxe", "lotr:angmar_pickaxe", 1, LOTRTranscribedRecipes.rows("XXX", " Y ", " Y "), 'X', "lotr:orc_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.ANGMAR, "faction/angmar/tool/angmar_shovel", "lotr:angmar_shovel", 1, LOTRTranscribedRecipes.rows("X", "Y", "Y"), 'X', "lotr:orc_steel_ingot", 'Y', "#lotr:sticks");
        // faction/angmar/utility
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.ANGMAR, "faction/angmar/utility/orc_chain", "lotr:orc_chain", 8, LOTRTranscribedRecipes.rows("X", "X", "X"), 'X', "lotr:orc_steel_ingot");
        // faction/blue_dwarven/block
        r.in(RecipeCategory.BUILDING_BLOCKS).shaped(LOTRCraftingTable.BLUE_DWARVEN, "faction/blue_dwarven/block/dwarven_mithril_brick", "lotr:dwarven_mithril_brick", 1, LOTRTranscribedRecipes.rows(" X ", "XYX", " X "), 'X', "lotr:mithril_nugget", 'Y', "lotr:dwarven_brick");
        r.in(RecipeCategory.BUILDING_BLOCKS).shaped(LOTRCraftingTable.BLUE_DWARVEN, "faction/blue_dwarven/block/dwarven_silver_brick", "lotr:dwarven_silver_brick", 1, LOTRTranscribedRecipes.rows(" X ", "XYX", " X "), 'X', "lotr:silver_nugget", 'Y', "lotr:dwarven_brick");
        r.in(RecipeCategory.BUILDING_BLOCKS).shaped(LOTRCraftingTable.BLUE_DWARVEN, "faction/blue_dwarven/block/obsidian_dwarven_brick", "lotr:obsidian_dwarven_brick", 4, LOTRTranscribedRecipes.rows("XX", "XX"), 'X', "lotr:obsidian_shard");
        // faction/blue_dwarven/combat
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.BLUE_DWARVEN, "faction/blue_dwarven/combat/blue_dwarven_battleaxe", "lotr:blue_dwarven_battleaxe", 1, LOTRTranscribedRecipes.rows("XXX", "XYX", " Y "), 'X', "lotr:blue_dwarven_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.BLUE_DWARVEN, "faction/blue_dwarven/combat/blue_dwarven_boar_armor", "lotr:blue_dwarven_boar_armor", 1, LOTRTranscribedRecipes.rows("X  ", "XYX", "XXX"), 'X', "lotr:blue_dwarven_steel_ingot", 'Y', "minecraft:leather");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.BLUE_DWARVEN, "faction/blue_dwarven/combat/blue_dwarven_boots", "lotr:blue_dwarven_boots", 1, LOTRTranscribedRecipes.rows("X X", "X X"), 'X', "lotr:blue_dwarven_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.BLUE_DWARVEN, "faction/blue_dwarven/combat/blue_dwarven_chestplate", "lotr:blue_dwarven_chestplate", 1, LOTRTranscribedRecipes.rows("X X", "XXX", "XXX"), 'X', "lotr:blue_dwarven_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.BLUE_DWARVEN, "faction/blue_dwarven/combat/blue_dwarven_dagger", "lotr:blue_dwarven_dagger", 1, LOTRTranscribedRecipes.rows("X", "Y"), 'X', "lotr:blue_dwarven_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.BLUE_DWARVEN, "faction/blue_dwarven/combat/blue_dwarven_helmet", "lotr:blue_dwarven_helmet", 1, LOTRTranscribedRecipes.rows("XXX", "X X"), 'X', "lotr:blue_dwarven_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.BLUE_DWARVEN, "faction/blue_dwarven/combat/blue_dwarven_leggings", "lotr:blue_dwarven_leggings", 1, LOTRTranscribedRecipes.rows("XXX", "X X", "X X"), 'X', "lotr:blue_dwarven_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.BLUE_DWARVEN, "faction/blue_dwarven/combat/blue_dwarven_pike", "lotr:blue_dwarven_pike", 1, LOTRTranscribedRecipes.rows("  X", " YX", "Y  "), 'X', "lotr:blue_dwarven_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.BLUE_DWARVEN, "faction/blue_dwarven/combat/blue_dwarven_spear", "lotr:blue_dwarven_spear", 1, LOTRTranscribedRecipes.rows("  X", " Y ", "Y  "), 'X', "lotr:blue_dwarven_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.BLUE_DWARVEN, "faction/blue_dwarven/combat/blue_dwarven_sword", "lotr:blue_dwarven_sword", 1, LOTRTranscribedRecipes.rows("X", "X", "Y"), 'X', "lotr:blue_dwarven_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.BLUE_DWARVEN, "faction/blue_dwarven/combat/blue_dwarven_throwing_axe", "lotr:blue_dwarven_throwing_axe", 1, LOTRTranscribedRecipes.rows(" X ", " YX", "Y  "), 'X', "lotr:blue_dwarven_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.BLUE_DWARVEN, "faction/blue_dwarven/combat/blue_dwarven_warhammer", "lotr:blue_dwarven_warhammer", 1, LOTRTranscribedRecipes.rows("XYX", "XYX", " Y "), 'X', "lotr:blue_dwarven_steel_ingot", 'Y', "#lotr:sticks");
        // faction/blue_dwarven/decoration
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.BLUE_DWARVEN, "faction/blue_dwarven/decoration/blue_dwarf_bars", "lotr:blue_dwarf_bars", 16, LOTRTranscribedRecipes.rows("XXX", "XXX"), 'X', "lotr:blue_dwarven_steel_ingot");
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.BLUE_DWARVEN, "faction/blue_dwarven/decoration/blue_dwarven_chandelier", "lotr:blue_dwarven_chandelier", 2, LOTRTranscribedRecipes.rows(" X ", "YZY"), 'X', "#lotr:sticks", 'Y', "minecraft:torch", 'Z', "lotr:blue_dwarven_steel_ingot");
        // faction/blue_dwarven/misc
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.BLUE_DWARVEN, "faction/blue_dwarven/misc/dwarven_gate", "lotr:dwarven_gate", 4, LOTRTranscribedRecipes.rows("ZYZ", "YXY", "ZYZ"), 'X', "lotr:gate_gears", 'Y', "lotr:dwarven_brick", 'Z', "lotr:blue_dwarven_steel_ingot");
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.BLUE_DWARVEN, "faction/blue_dwarven/misc/mechanism", "lotr:mechanism", 1, LOTRTranscribedRecipes.rows(" X ", "YZY", " X "), 'X', "minecraft:copper_ingot", 'Y', "minecraft:flint", 'Z', "lotr:blue_dwarven_steel_ingot");
        // faction/blue_dwarven/tool
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.BLUE_DWARVEN, "faction/blue_dwarven/tool/blue_dwarven_axe", "lotr:blue_dwarven_axe", 1, LOTRTranscribedRecipes.rows("XX", "XY", " Y"), 'X', "lotr:blue_dwarven_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.BLUE_DWARVEN, "faction/blue_dwarven/tool/blue_dwarven_hoe", "lotr:blue_dwarven_hoe", 1, LOTRTranscribedRecipes.rows("XX", " Y", " Y"), 'X', "lotr:blue_dwarven_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.BLUE_DWARVEN, "faction/blue_dwarven/tool/blue_dwarven_mattock", "lotr:blue_dwarven_mattock", 1, LOTRTranscribedRecipes.rows("XXX", "XY ", " Y "), 'X', "lotr:blue_dwarven_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.BLUE_DWARVEN, "faction/blue_dwarven/tool/blue_dwarven_pickaxe", "lotr:blue_dwarven_pickaxe", 1, LOTRTranscribedRecipes.rows("XXX", " Y ", " Y "), 'X', "lotr:blue_dwarven_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.BLUE_DWARVEN, "faction/blue_dwarven/tool/blue_dwarven_shovel", "lotr:blue_dwarven_shovel", 1, LOTRTranscribedRecipes.rows("X", "Y", "Y"), 'X', "lotr:blue_dwarven_steel_ingot", 'Y', "#lotr:sticks");
        // faction/blue_dwarven/utility
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.BLUE_DWARVEN, "faction/blue_dwarven/utility/ithildin_dwarven_door", "lotr:ithildin_dwarven_door", 1, LOTRTranscribedRecipes.rows("XX", "XY", "XX"), 'X', "minecraft:stone", 'Y', "lotr:ithildin");
        // faction/dale/combat
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DALE, "faction/dale/combat/dale_battleaxe", "lotr:dale_battleaxe", 1, LOTRTranscribedRecipes.rows("XXX", "XYX", " Y "), 'X', "minecraft:iron_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DALE, "faction/dale/combat/dale_boots", "lotr:dale_boots", 1, LOTRTranscribedRecipes.rows("X X", "X X"), 'X', "minecraft:iron_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DALE, "faction/dale/combat/dale_bow", "lotr:dale_bow", 1, LOTRTranscribedRecipes.rows(" XY", "X Y", " XY"), 'X', "#lotr:sticks", 'Y', "minecraft:string");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DALE, "faction/dale/combat/dale_chestplate", "lotr:dale_chestplate", 1, LOTRTranscribedRecipes.rows("X X", "XXX", "XXX"), 'X', "minecraft:iron_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DALE, "faction/dale/combat/dale_dagger", "lotr:dale_dagger", 1, LOTRTranscribedRecipes.rows("X", "Y"), 'X', "minecraft:iron_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DALE, "faction/dale/combat/dale_gambeson", "lotr:dale_gambeson", 1, LOTRTranscribedRecipes.rows("X X", "XXX", "XXX"), 'X', "#minecraft:wool");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DALE, "faction/dale/combat/dale_helmet", "lotr:dale_helmet", 1, LOTRTranscribedRecipes.rows("XXX", "X X"), 'X', "minecraft:iron_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DALE, "faction/dale/combat/dale_horse_armor", "lotr:dale_horse_armor", 1, LOTRTranscribedRecipes.rows("X  ", "XYX", "XXX"), 'X', "minecraft:iron_ingot", 'Y', "minecraft:leather");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DALE, "faction/dale/combat/dale_leggings", "lotr:dale_leggings", 1, LOTRTranscribedRecipes.rows("XXX", "X X", "X X"), 'X', "minecraft:iron_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DALE, "faction/dale/combat/dale_pitchfork", "lotr:dale_pitchfork", 1, LOTRTranscribedRecipes.rows("  X", " YX", "Y  "), 'X', "minecraft:iron_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DALE, "faction/dale/combat/dale_spear", "lotr:dale_spear", 1, LOTRTranscribedRecipes.rows("  X", " Y ", "Y  "), 'X', "minecraft:iron_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DALE, "faction/dale/combat/dale_sword", "lotr:dale_sword", 1, LOTRTranscribedRecipes.rows("X", "X", "Y"), 'X', "minecraft:iron_ingot", 'Y', "#lotr:sticks");
        // faction/dale/food
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.DALE, "faction/dale/food/cram", "lotr:cram", 1, LOTRTranscribedRecipes.rows("XYX"), 'X', "minecraft:wheat", 'Y', "lotr:salt");
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.DALE, "faction/dale/food/dalish_pastry", "lotr:dalish_pastry", 1, LOTRTranscribedRecipes.rows("ABA", "CDC", "EEE"), 'A', "lotr:maple_syrup", 'B', "minecraft:milk_bucket", 'C', "lotr:raisins", 'D', "minecraft:egg", 'E', "minecraft:wheat");
        // faction/dol_amroth/combat
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DOL_AMROTH, "faction/dol_amroth/combat/dol_amroth_chaps", "lotr:dol_amroth_chaps", 1, LOTRTranscribedRecipes.rows("XXX", "X X", "X X"), 'X', "#minecraft:wool");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DOL_AMROTH, "faction/dol_amroth/combat/dol_amroth_chestplate", "lotr:dol_amroth_chestplate", 1, LOTRTranscribedRecipes.rows("YXY", "XXX", "XXX"), 'X', "minecraft:iron_ingot", 'Y', "lotr:swan_feather");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DOL_AMROTH, "faction/dol_amroth/combat/dol_amroth_dagger", "lotr:dol_amroth_dagger", 1, LOTRTranscribedRecipes.rows("X", "Y"), 'X', "minecraft:iron_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DOL_AMROTH, "faction/dol_amroth/combat/dol_amroth_gambeson", "lotr:dol_amroth_gambeson", 1, LOTRTranscribedRecipes.rows("X X", "XXX", "XXX"), 'X', "#minecraft:wool");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DOL_AMROTH, "faction/dol_amroth/combat/dol_amroth_helmet", "lotr:dol_amroth_helmet", 1, LOTRTranscribedRecipes.rows("Y Y", "XXX", "X X"), 'X', "minecraft:iron_ingot", 'Y', "lotr:swan_feather");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DOL_AMROTH, "faction/dol_amroth/combat/dol_amroth_longspear", "lotr:dol_amroth_longspear", 1, LOTRTranscribedRecipes.rows("  X", " YX", "Y  "), 'X', "minecraft:iron_ingot", 'Y', "#lotr:sticks");
        // faction/dol_amroth/misc
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.DOL_AMROTH, "faction/dol_amroth/misc/dol_amroth_gate", "lotr:dol_amroth_gate", 4, LOTRTranscribedRecipes.rows("ZYZ", "YXY", "ZYZ"), 'X', "lotr:gate_gears", 'Y', "#minecraft:planks", 'Z', "minecraft:iron_ingot");
        // faction/dol_guldur/block
        r.in(RecipeCategory.BUILDING_BLOCKS).shaped(LOTRCraftingTable.DOL_GULDUR, "faction/dol_guldur/block/orc_plating_iron", "lotr:orc_plating_iron", 8, LOTRTranscribedRecipes.rows("XX", "XX"), 'X', "lotr:orc_steel_ingot");
        // faction/dol_guldur/combat
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DOL_GULDUR, "faction/dol_guldur/combat/dol_guldur_battleaxe", "lotr:dol_guldur_battleaxe", 1, LOTRTranscribedRecipes.rows("XXX", "XYX", " Y "), 'X', "lotr:orc_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DOL_GULDUR, "faction/dol_guldur/combat/dol_guldur_boots", "lotr:dol_guldur_boots", 1, LOTRTranscribedRecipes.rows("X X", "X X"), 'X', "lotr:orc_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DOL_GULDUR, "faction/dol_guldur/combat/dol_guldur_chestplate", "lotr:dol_guldur_chestplate", 1, LOTRTranscribedRecipes.rows("X X", "XXX", "XXX"), 'X', "lotr:orc_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DOL_GULDUR, "faction/dol_guldur/combat/dol_guldur_dagger", "lotr:dol_guldur_dagger", 1, LOTRTranscribedRecipes.rows("X", "Y"), 'X', "lotr:orc_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DOL_GULDUR, "faction/dol_guldur/combat/dol_guldur_helmet", "lotr:dol_guldur_helmet", 1, LOTRTranscribedRecipes.rows("XXX", "X X"), 'X', "lotr:orc_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DOL_GULDUR, "faction/dol_guldur/combat/dol_guldur_leggings", "lotr:dol_guldur_leggings", 1, LOTRTranscribedRecipes.rows("XXX", "X X", "X X"), 'X', "lotr:orc_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DOL_GULDUR, "faction/dol_guldur/combat/dol_guldur_spear", "lotr:dol_guldur_spear", 1, LOTRTranscribedRecipes.rows("  X", " Y ", "Y  "), 'X', "lotr:orc_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DOL_GULDUR, "faction/dol_guldur/combat/dol_guldur_spike", "lotr:dol_guldur_spike", 1, LOTRTranscribedRecipes.rows("  X", " YX", "Y  "), 'X', "lotr:orc_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DOL_GULDUR, "faction/dol_guldur/combat/dol_guldur_sword", "lotr:dol_guldur_sword", 1, LOTRTranscribedRecipes.rows("X", "X", "Y"), 'X', "lotr:orc_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DOL_GULDUR, "faction/dol_guldur/combat/dol_guldur_warhammer", "lotr:dol_guldur_warhammer", 1, LOTRTranscribedRecipes.rows("XYX", "XYX", " Y "), 'X', "lotr:orc_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shapeless(LOTRCraftingTable.DOL_GULDUR, "faction/dol_guldur/combat/double_strength_orc_bomb", "lotr:double_strength_orc_bomb", 1, "lotr:orc_bomb", "minecraft:gunpowder", "lotr:orc_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DOL_GULDUR, "faction/dol_guldur/combat/morgul_blade", "lotr:morgul_blade", 1, LOTRTranscribedRecipes.rows("X", "X", "Y"), 'X', "lotr:morgul_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DOL_GULDUR, "faction/dol_guldur/combat/morgul_boots", "lotr:morgul_boots", 1, LOTRTranscribedRecipes.rows("X X", "X X"), 'X', "lotr:morgul_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DOL_GULDUR, "faction/dol_guldur/combat/morgul_chestplate", "lotr:morgul_chestplate", 1, LOTRTranscribedRecipes.rows("X X", "XXX", "XXX"), 'X', "lotr:morgul_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DOL_GULDUR, "faction/dol_guldur/combat/morgul_helmet", "lotr:morgul_helmet", 1, LOTRTranscribedRecipes.rows("XXX", "X X"), 'X', "lotr:morgul_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DOL_GULDUR, "faction/dol_guldur/combat/morgul_horse_armor", "lotr:morgul_horse_armor", 1, LOTRTranscribedRecipes.rows("X  ", "XYX", "XXX"), 'X', "lotr:morgul_steel_ingot", 'Y', "minecraft:leather");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DOL_GULDUR, "faction/dol_guldur/combat/morgul_leggings", "lotr:morgul_leggings", 1, LOTRTranscribedRecipes.rows("XXX", "X X", "X X"), 'X', "lotr:morgul_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DOL_GULDUR, "faction/dol_guldur/combat/orc_bomb", "lotr:orc_bomb", 4, LOTRTranscribedRecipes.rows("XYX", "YXY", "XYX"), 'X', "minecraft:gunpowder", 'Y', "lotr:orc_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DOL_GULDUR, "faction/dol_guldur/combat/orc_bow", "lotr:orc_bow", 1, LOTRTranscribedRecipes.rows(" XY", "X Y", " XY"), 'X', "lotr:orc_steel_ingot", 'Y', "minecraft:string");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DOL_GULDUR, "faction/dol_guldur/combat/poisoned_arrow", "lotr:poisoned_arrow", 4, LOTRTranscribedRecipes.rows(" X ", "XYX", " X "), 'X', "minecraft:arrow", 'Y', "lotr:bottle_of_poison");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DOL_GULDUR, "faction/dol_guldur/combat/poisoned_crossbow_bolt", "lotr:poisoned_crossbow_bolt", 4, LOTRTranscribedRecipes.rows(" X ", "XYX", " X "), 'X', "lotr:crossbow_bolt", 'Y', "lotr:bottle_of_poison");
        r.in(RecipeCategory.COMBAT).shapeless(LOTRCraftingTable.DOL_GULDUR, "faction/dol_guldur/combat/triple_strength_orc_bomb", "lotr:triple_strength_orc_bomb", 1, "lotr:double_strength_orc_bomb", "minecraft:gunpowder", "lotr:orc_steel_ingot");
        // faction/dol_guldur/decoration
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.DOL_GULDUR, "faction/dol_guldur/decoration/morgul_chandelier", "lotr:morgul_chandelier", 2, LOTRTranscribedRecipes.rows(" X ", "YZY"), 'X', "#lotr:sticks", 'Y', "lotr:morgul_torch", 'Z', "lotr:morgul_steel_ingot");
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.DOL_GULDUR, "faction/dol_guldur/decoration/orc_chandelier", "lotr:orc_chandelier", 2, LOTRTranscribedRecipes.rows(" X ", "YZY"), 'X', "#lotr:sticks", 'Y', "lotr:orc_torch", 'Z', "lotr:orc_steel_ingot");
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.DOL_GULDUR, "faction/dol_guldur/decoration/orc_steel_bars", "lotr:orc_steel_bars", 16, LOTRTranscribedRecipes.rows("XXX", "XXX"), 'X', "lotr:orc_steel_ingot");
        // faction/dol_guldur/food
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.DOL_GULDUR, "faction/dol_guldur/food/maggoty_bread_dol_guldur", "lotr:maggoty_bread", 1, LOTRTranscribedRecipes.rows("XXX"), 'X', "minecraft:wheat");
        // faction/dol_guldur/misc
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.DOL_GULDUR, "faction/dol_guldur/misc/mechanism", "lotr:mechanism", 1, LOTRTranscribedRecipes.rows(" X ", "YZY", " X "), 'X', "minecraft:copper_ingot", 'Y', "minecraft:flint", 'Z', "lotr:orc_steel_ingot");
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.DOL_GULDUR, "faction/dol_guldur/misc/orc_gate", "lotr:orc_gate", 4, LOTRTranscribedRecipes.rows("YYY", "YXY", "YYY"), 'X', "lotr:gate_gears", 'Y', "lotr:orc_steel_ingot");
        // faction/dol_guldur/tool
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DOL_GULDUR, "faction/dol_guldur/tool/dol_guldur_axe", "lotr:dol_guldur_axe", 1, LOTRTranscribedRecipes.rows("XX", "XY", " Y"), 'X', "lotr:orc_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DOL_GULDUR, "faction/dol_guldur/tool/dol_guldur_hoe", "lotr:dol_guldur_hoe", 1, LOTRTranscribedRecipes.rows("XX", " Y", " Y"), 'X', "lotr:orc_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DOL_GULDUR, "faction/dol_guldur/tool/dol_guldur_pickaxe", "lotr:dol_guldur_pickaxe", 1, LOTRTranscribedRecipes.rows("XXX", " Y ", " Y "), 'X', "lotr:orc_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DOL_GULDUR, "faction/dol_guldur/tool/dol_guldur_shovel", "lotr:dol_guldur_shovel", 1, LOTRTranscribedRecipes.rows("X", "Y", "Y"), 'X', "lotr:orc_steel_ingot", 'Y', "#lotr:sticks");
        // faction/dol_guldur/utility
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.DOL_GULDUR, "faction/dol_guldur/utility/orc_chain", "lotr:orc_chain", 8, LOTRTranscribedRecipes.rows("X", "X", "X"), 'X', "lotr:orc_steel_ingot");
        // faction/dorwinion/combat
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DORWINION, "faction/dorwinion/combat/bladorthin_spear", "lotr:bladorthin_spear", 1, LOTRTranscribedRecipes.rows("  X", " Y ", "Y  "), 'X', "lotr:dwarven_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DORWINION, "faction/dorwinion/combat/dorwinion_boots", "lotr:dorwinion_boots", 1, LOTRTranscribedRecipes.rows("X X", "X X"), 'X', "minecraft:iron_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DORWINION, "faction/dorwinion/combat/dorwinion_chestplate", "lotr:dorwinion_chestplate", 1, LOTRTranscribedRecipes.rows("X X", "XXX", "XXX"), 'X', "minecraft:iron_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DORWINION, "faction/dorwinion/combat/dorwinion_elven_boots", "lotr:dorwinion_elven_boots", 1, LOTRTranscribedRecipes.rows("X X", "X X"), 'X', "lotr:elven_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DORWINION, "faction/dorwinion/combat/dorwinion_elven_bow", "lotr:dorwinion_elven_bow", 1, LOTRTranscribedRecipes.rows(" XY", "X Y", " XY"), 'X', "lotr:elven_steel_ingot", 'Y', "minecraft:string");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DORWINION, "faction/dorwinion/combat/dorwinion_elven_chestplate", "lotr:dorwinion_elven_chestplate", 1, LOTRTranscribedRecipes.rows("X X", "XXX", "XXX"), 'X', "lotr:elven_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DORWINION, "faction/dorwinion/combat/dorwinion_elven_dagger", "lotr:dorwinion_elven_dagger", 1, LOTRTranscribedRecipes.rows("X", "Y"), 'X', "lotr:elven_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DORWINION, "faction/dorwinion/combat/dorwinion_elven_helmet", "lotr:dorwinion_elven_helmet", 1, LOTRTranscribedRecipes.rows("XXX", "X X"), 'X', "lotr:elven_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DORWINION, "faction/dorwinion/combat/dorwinion_elven_leggings", "lotr:dorwinion_elven_leggings", 1, LOTRTranscribedRecipes.rows("XXX", "X X", "X X"), 'X', "lotr:elven_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DORWINION, "faction/dorwinion/combat/dorwinion_elven_sword", "lotr:dorwinion_elven_sword", 1, LOTRTranscribedRecipes.rows("X", "X", "Y"), 'X', "lotr:elven_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DORWINION, "faction/dorwinion/combat/dorwinion_helmet", "lotr:dorwinion_helmet", 1, LOTRTranscribedRecipes.rows("XXX", "X X"), 'X', "minecraft:iron_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DORWINION, "faction/dorwinion/combat/dorwinion_leggings", "lotr:dorwinion_leggings", 1, LOTRTranscribedRecipes.rows("XXX", "X X", "X X"), 'X', "minecraft:iron_ingot");
        // faction/dwarven/block
        r.in(RecipeCategory.BUILDING_BLOCKS).shaped(LOTRCraftingTable.DWARVEN, "faction/dwarven/block/dwarven_mithril_brick", "lotr:dwarven_mithril_brick", 1, LOTRTranscribedRecipes.rows(" X ", "XYX", " X "), 'X', "lotr:mithril_nugget", 'Y', "lotr:dwarven_brick");
        r.in(RecipeCategory.BUILDING_BLOCKS).shaped(LOTRCraftingTable.DWARVEN, "faction/dwarven/block/dwarven_silver_brick", "lotr:dwarven_silver_brick", 1, LOTRTranscribedRecipes.rows(" X ", "XYX", " X "), 'X', "lotr:silver_nugget", 'Y', "lotr:dwarven_brick");
        r.in(RecipeCategory.BUILDING_BLOCKS).shaped(LOTRCraftingTable.DWARVEN, "faction/dwarven/block/obsidian_dwarven_brick", "lotr:obsidian_dwarven_brick", 4, LOTRTranscribedRecipes.rows("XX", "XX"), 'X', "lotr:obsidian_shard");
        // faction/dwarven/combat
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DWARVEN, "faction/dwarven/combat/dwarven_battleaxe", "lotr:dwarven_battleaxe", 1, LOTRTranscribedRecipes.rows("XXX", "XYX", " Y "), 'X', "lotr:dwarven_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DWARVEN, "faction/dwarven/combat/dwarven_boar_armor", "lotr:dwarven_boar_armor", 1, LOTRTranscribedRecipes.rows("X  ", "XYX", "XXX"), 'X', "lotr:dwarven_steel_ingot", 'Y', "minecraft:leather");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DWARVEN, "faction/dwarven/combat/dwarven_boots", "lotr:dwarven_boots", 1, LOTRTranscribedRecipes.rows("X X", "X X"), 'X', "lotr:dwarven_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DWARVEN, "faction/dwarven/combat/dwarven_chestplate", "lotr:dwarven_chestplate", 1, LOTRTranscribedRecipes.rows("X X", "XXX", "XXX"), 'X', "lotr:dwarven_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DWARVEN, "faction/dwarven/combat/dwarven_dagger", "lotr:dwarven_dagger", 1, LOTRTranscribedRecipes.rows("X", "Y"), 'X', "lotr:dwarven_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DWARVEN, "faction/dwarven/combat/dwarven_helmet", "lotr:dwarven_helmet", 1, LOTRTranscribedRecipes.rows("XXX", "X X"), 'X', "lotr:dwarven_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DWARVEN, "faction/dwarven/combat/dwarven_leggings", "lotr:dwarven_leggings", 1, LOTRTranscribedRecipes.rows("XXX", "X X", "X X"), 'X', "lotr:dwarven_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DWARVEN, "faction/dwarven/combat/dwarven_pike", "lotr:dwarven_pike", 1, LOTRTranscribedRecipes.rows("  X", " YX", "Y  "), 'X', "lotr:dwarven_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DWARVEN, "faction/dwarven/combat/dwarven_spear", "lotr:dwarven_spear", 1, LOTRTranscribedRecipes.rows("  X", " Y ", "Y  "), 'X', "lotr:dwarven_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DWARVEN, "faction/dwarven/combat/dwarven_sword", "lotr:dwarven_sword", 1, LOTRTranscribedRecipes.rows("X", "X", "Y"), 'X', "lotr:dwarven_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DWARVEN, "faction/dwarven/combat/dwarven_throwing_axe", "lotr:dwarven_throwing_axe", 1, LOTRTranscribedRecipes.rows(" X ", " YX", "Y  "), 'X', "lotr:dwarven_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DWARVEN, "faction/dwarven/combat/dwarven_warhammer", "lotr:dwarven_warhammer", 1, LOTRTranscribedRecipes.rows("XYX", "XYX", " Y "), 'X', "lotr:dwarven_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DWARVEN, "faction/dwarven/combat/mithril_trimmed_dwarven_boots", "lotr:mithril_trimmed_dwarven_boots", 1, LOTRTranscribedRecipes.rows("XXX", "XYX", "XXX"), 'X', "lotr:mithril_nugget", 'Y', "lotr:dwarven_boots");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DWARVEN, "faction/dwarven/combat/mithril_trimmed_dwarven_chestplate", "lotr:mithril_trimmed_dwarven_chestplate", 1, LOTRTranscribedRecipes.rows("XXX", "XYX", "XXX"), 'X', "lotr:mithril_nugget", 'Y', "lotr:dwarven_chestplate");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DWARVEN, "faction/dwarven/combat/mithril_trimmed_dwarven_helmet", "lotr:mithril_trimmed_dwarven_helmet", 1, LOTRTranscribedRecipes.rows("XXX", "XYX", "XXX"), 'X', "lotr:mithril_nugget", 'Y', "lotr:dwarven_helmet");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DWARVEN, "faction/dwarven/combat/mithril_trimmed_dwarven_leggings", "lotr:mithril_trimmed_dwarven_leggings", 1, LOTRTranscribedRecipes.rows("XXX", "XYX", "XXX"), 'X', "lotr:mithril_nugget", 'Y', "lotr:dwarven_leggings");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DWARVEN, "faction/dwarven/combat/silver_trimmed_dwarven_boots", "lotr:silver_trimmed_dwarven_boots", 1, LOTRTranscribedRecipes.rows("XXX", "XYX", "XXX"), 'X', "lotr:silver_nugget", 'Y', "lotr:dwarven_boots");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DWARVEN, "faction/dwarven/combat/silver_trimmed_dwarven_chestplate", "lotr:silver_trimmed_dwarven_chestplate", 1, LOTRTranscribedRecipes.rows("XXX", "XYX", "XXX"), 'X', "lotr:silver_nugget", 'Y', "lotr:dwarven_chestplate");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DWARVEN, "faction/dwarven/combat/silver_trimmed_dwarven_helmet", "lotr:silver_trimmed_dwarven_helmet", 1, LOTRTranscribedRecipes.rows("XXX", "XYX", "XXX"), 'X', "lotr:silver_nugget", 'Y', "lotr:dwarven_helmet");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DWARVEN, "faction/dwarven/combat/silver_trimmed_dwarven_leggings", "lotr:silver_trimmed_dwarven_leggings", 1, LOTRTranscribedRecipes.rows("XXX", "XYX", "XXX"), 'X', "lotr:silver_nugget", 'Y', "lotr:dwarven_leggings");
        // faction/dwarven/decoration
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.DWARVEN, "faction/dwarven/decoration/dwarf_bars", "lotr:dwarf_bars", 16, LOTRTranscribedRecipes.rows("XXX", "XXX"), 'X', "lotr:dwarven_steel_ingot");
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.DWARVEN, "faction/dwarven/decoration/dwarven_chandelier", "lotr:dwarven_chandelier", 2, LOTRTranscribedRecipes.rows(" X ", "YZY"), 'X', "#lotr:sticks", 'Y', "minecraft:torch", 'Z', "lotr:dwarven_steel_ingot");
        // faction/dwarven/misc
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.DWARVEN, "faction/dwarven/misc/dwarven_gate", "lotr:dwarven_gate", 4, LOTRTranscribedRecipes.rows("ZYZ", "YXY", "ZYZ"), 'X', "lotr:gate_gears", 'Y', "lotr:dwarven_brick", 'Z', "lotr:dwarven_steel_ingot");
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.DWARVEN, "faction/dwarven/misc/mechanism", "lotr:mechanism", 1, LOTRTranscribedRecipes.rows(" X ", "YZY", " X "), 'X', "minecraft:copper_ingot", 'Y', "minecraft:flint", 'Z', "lotr:dwarven_steel_ingot");
        // faction/dwarven/tool
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DWARVEN, "faction/dwarven/tool/dwarven_axe", "lotr:dwarven_axe", 1, LOTRTranscribedRecipes.rows("XX", "XY", " Y"), 'X', "lotr:dwarven_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DWARVEN, "faction/dwarven/tool/dwarven_hoe", "lotr:dwarven_hoe", 1, LOTRTranscribedRecipes.rows("XX", " Y", " Y"), 'X', "lotr:dwarven_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DWARVEN, "faction/dwarven/tool/dwarven_mattock", "lotr:dwarven_mattock", 1, LOTRTranscribedRecipes.rows("XXX", "XY ", " Y "), 'X', "lotr:dwarven_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DWARVEN, "faction/dwarven/tool/dwarven_pickaxe", "lotr:dwarven_pickaxe", 1, LOTRTranscribedRecipes.rows("XXX", " Y ", " Y "), 'X', "lotr:dwarven_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.DWARVEN, "faction/dwarven/tool/dwarven_shovel", "lotr:dwarven_shovel", 1, LOTRTranscribedRecipes.rows("X", "Y", "Y"), 'X', "lotr:dwarven_steel_ingot", 'Y', "#lotr:sticks");
        // faction/dwarven/utility
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.DWARVEN, "faction/dwarven/utility/ithildin_dwarven_door", "lotr:ithildin_dwarven_door", 1, LOTRTranscribedRecipes.rows("XX", "XY", "XX"), 'X', "minecraft:stone", 'Y', "lotr:ithildin");
        // faction/elven/block
        r.in(RecipeCategory.BUILDING_BLOCKS).shaped(LOTRCraftingTable.ELVEN, "faction/elven/block/galadhrim_silver_brick", "lotr:galadhrim_silver_brick", 1, LOTRTranscribedRecipes.rows(" X ", "XYX", " X "), 'X', "lotr:silver_nugget", 'Y', "lotr:galadhrim_brick");
        // faction/elven/combat
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.ELVEN, "faction/elven/combat/galadhrim_battlestaff", "lotr:galadhrim_battlestaff", 1, LOTRTranscribedRecipes.rows("  X", " Y ", "X  "), 'X', "lotr:elven_steel_ingot", 'Y', "lotr:mallorn_stick");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.ELVEN, "faction/elven/combat/galadhrim_boots", "lotr:galadhrim_boots", 1, LOTRTranscribedRecipes.rows("X X", "X X"), 'X', "lotr:elven_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.ELVEN, "faction/elven/combat/galadhrim_bow", "lotr:galadhrim_bow", 1, LOTRTranscribedRecipes.rows(" XY", "X Y", " XY"), 'X', "lotr:elven_steel_ingot", 'Y', "minecraft:string");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.ELVEN, "faction/elven/combat/galadhrim_chestplate", "lotr:galadhrim_chestplate", 1, LOTRTranscribedRecipes.rows("X X", "XXX", "XXX"), 'X', "lotr:elven_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.ELVEN, "faction/elven/combat/galadhrim_cloak_boots", "lotr:galadhrim_cloak_boots", 1, LOTRTranscribedRecipes.rows("X X", "X X"), 'X', "lotr:hithlain");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.ELVEN, "faction/elven/combat/galadhrim_cloak_hood", "lotr:galadhrim_cloak_hood", 1, LOTRTranscribedRecipes.rows("XXX", "X X"), 'X', "lotr:hithlain");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.ELVEN, "faction/elven/combat/galadhrim_cloak_leggings", "lotr:galadhrim_cloak_leggings", 1, LOTRTranscribedRecipes.rows("XXX", "X X", "X X"), 'X', "lotr:hithlain");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.ELVEN, "faction/elven/combat/galadhrim_cloak_tunic", "lotr:galadhrim_cloak_tunic", 1, LOTRTranscribedRecipes.rows("X X", "XXX", "XXX"), 'X', "lotr:hithlain");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.ELVEN, "faction/elven/combat/galadhrim_dagger", "lotr:galadhrim_dagger", 1, LOTRTranscribedRecipes.rows("X", "Y"), 'X', "lotr:elven_steel_ingot", 'Y', "lotr:mallorn_stick");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.ELVEN, "faction/elven/combat/galadhrim_helmet", "lotr:galadhrim_helmet", 1, LOTRTranscribedRecipes.rows("XXX", "X X"), 'X', "lotr:elven_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.ELVEN, "faction/elven/combat/galadhrim_horse_armor", "lotr:galadhrim_horse_armor", 1, LOTRTranscribedRecipes.rows("X  ", "XYX", "XXX"), 'X', "lotr:elven_steel_ingot", 'Y', "minecraft:leather");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.ELVEN, "faction/elven/combat/galadhrim_leggings", "lotr:galadhrim_leggings", 1, LOTRTranscribedRecipes.rows("XXX", "X X", "X X"), 'X', "lotr:elven_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.ELVEN, "faction/elven/combat/galadhrim_longspear", "lotr:galadhrim_longspear", 1, LOTRTranscribedRecipes.rows("  X", " YX", "Y  "), 'X', "lotr:elven_steel_ingot", 'Y', "lotr:mallorn_stick");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.ELVEN, "faction/elven/combat/galadhrim_spear", "lotr:galadhrim_spear", 1, LOTRTranscribedRecipes.rows("  X", " Y ", "Y  "), 'X', "lotr:elven_steel_ingot", 'Y', "lotr:mallorn_stick");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.ELVEN, "faction/elven/combat/galadhrim_sword", "lotr:galadhrim_sword", 1, LOTRTranscribedRecipes.rows("X", "X", "Y"), 'X', "lotr:elven_steel_ingot", 'Y', "lotr:mallorn_stick");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.ELVEN, "faction/elven/combat/galvorn_boots", "lotr:galvorn_boots", 1, LOTRTranscribedRecipes.rows("X X", "X X"), 'X', "lotr:galvorn_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.ELVEN, "faction/elven/combat/galvorn_chestplate", "lotr:galvorn_chestplate", 1, LOTRTranscribedRecipes.rows("X X", "XXX", "XXX"), 'X', "lotr:galvorn_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.ELVEN, "faction/elven/combat/galvorn_helmet", "lotr:galvorn_helmet", 1, LOTRTranscribedRecipes.rows("XXX", "X X"), 'X', "lotr:galvorn_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.ELVEN, "faction/elven/combat/galvorn_leggings", "lotr:galvorn_leggings", 1, LOTRTranscribedRecipes.rows("XXX", "X X", "X X"), 'X', "lotr:galvorn_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.ELVEN, "faction/elven/combat/mallorn_bow", "lotr:mallorn_bow", 1, LOTRTranscribedRecipes.rows(" XY", "X Y", " XY"), 'X', "lotr:mallorn_stick", 'Y', "minecraft:string");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.ELVEN, "faction/elven/combat/mallorn_sword", "lotr:mallorn_sword", 1, LOTRTranscribedRecipes.rows("X", "X", "Y"), 'X', "lotr:mallorn_planks", 'Y', "lotr:mallorn_stick");
        // faction/elven/decoration
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.ELVEN, "faction/elven/decoration/blue_mallorn_chandelier", "lotr:blue_mallorn_chandelier", 2, LOTRTranscribedRecipes.rows(" X ", "YZY"), 'X', "lotr:mallorn_stick", 'Y', "lotr:blue_mallorn_torch", 'Z', "lotr:mallorn_planks");
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.ELVEN, "faction/elven/decoration/galadhrim_bars", "lotr:galadhrim_bars", 16, LOTRTranscribedRecipes.rows("XXX", "XXX"), 'X', "lotr:elven_steel_ingot");
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.ELVEN, "faction/elven/decoration/galadhrim_wood_bars", "lotr:galadhrim_wood_bars", 8, LOTRTranscribedRecipes.rows("XXX", "XXX"), 'X', "lotr:mallorn_stick");
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.ELVEN, "faction/elven/decoration/green_mallorn_chandelier", "lotr:green_mallorn_chandelier", 2, LOTRTranscribedRecipes.rows(" X ", "YZY"), 'X', "lotr:mallorn_stick", 'Y', "lotr:green_mallorn_torch", 'Z', "lotr:mallorn_planks");
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.ELVEN, "faction/elven/decoration/hithlain_ladder", "lotr:hithlain_ladder", 3, LOTRTranscribedRecipes.rows("X", "X", "X"), 'X', "lotr:hithlain");
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.ELVEN, "faction/elven/decoration/lothlorien_banner", "lotr:lothlorien_banner", 1, LOTRTranscribedRecipes.rows("X", "Y", "Z"), 'X', "#minecraft:wool", 'Y', "lotr:mallorn_stick", 'Z', "lotr:mallorn_planks");
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.ELVEN, "faction/elven/decoration/mallorn_fence", "lotr:mallorn_fence", 3, LOTRTranscribedRecipes.rows("XYX", "XYX"), 'X', "lotr:mallorn_planks", 'Y', "lotr:mallorn_stick");
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.ELVEN, "faction/elven/decoration/mallorn_gold_chandelier", "lotr:mallorn_gold_chandelier", 2, LOTRTranscribedRecipes.rows(" X ", "YZY"), 'X', "lotr:mallorn_stick", 'Y', "lotr:mallorn_gold_torch", 'Z', "lotr:mallorn_planks");
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.ELVEN, "faction/elven/decoration/mallorn_ladder", "lotr:mallorn_ladder", 3, LOTRTranscribedRecipes.rows("X X", "XXX", "X X"), 'X', "lotr:mallorn_stick");
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.ELVEN, "faction/elven/decoration/silver_mallorn_chandelier", "lotr:silver_mallorn_chandelier", 2, LOTRTranscribedRecipes.rows(" X ", "YZY"), 'X', "lotr:mallorn_stick", 'Y', "lotr:mallorn_torch", 'Z', "lotr:mallorn_planks");
        // faction/elven/material
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.ELVEN, "faction/elven/material/hithlain", "lotr:hithlain", 3, LOTRTranscribedRecipes.rows("XXX"), 'X', "minecraft:string");
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.ELVEN, "faction/elven/material/mallorn_stick", "lotr:mallorn_stick", 4, LOTRTranscribedRecipes.rows("X", "X"), 'X', "lotr:mallorn_planks");
        // faction/elven/misc
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.ELVEN, "faction/elven/misc/elven_gate", "lotr:elven_gate", 4, LOTRTranscribedRecipes.rows("ZYZ", "YXY", "ZYZ"), 'X', "lotr:gate_gears", 'Y', "lotr:mallorn_planks", 'Z', "lotr:elven_steel_ingot");
        // faction/elven/tool
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.ELVEN, "faction/elven/tool/galadhrim_axe", "lotr:galadhrim_axe", 1, LOTRTranscribedRecipes.rows("XX", "XY", " Y"), 'X', "lotr:elven_steel_ingot", 'Y', "lotr:mallorn_stick");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.ELVEN, "faction/elven/tool/galadhrim_hoe", "lotr:galadhrim_hoe", 1, LOTRTranscribedRecipes.rows("XX", " Y", " Y"), 'X', "lotr:elven_steel_ingot", 'Y', "lotr:mallorn_stick");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.ELVEN, "faction/elven/tool/galadhrim_pickaxe", "lotr:galadhrim_pickaxe", 1, LOTRTranscribedRecipes.rows("XXX", " Y ", " Y "), 'X', "lotr:elven_steel_ingot", 'Y', "lotr:mallorn_stick");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.ELVEN, "faction/elven/tool/galadhrim_shovel", "lotr:galadhrim_shovel", 1, LOTRTranscribedRecipes.rows("X", "Y", "Y"), 'X', "lotr:elven_steel_ingot", 'Y', "lotr:mallorn_stick");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.ELVEN, "faction/elven/tool/mallorn_axe", "lotr:mallorn_axe", 1, LOTRTranscribedRecipes.rows("XX", "XY", " Y"), 'X', "lotr:mallorn_planks", 'Y', "lotr:mallorn_stick");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.ELVEN, "faction/elven/tool/mallorn_hoe", "lotr:mallorn_hoe", 1, LOTRTranscribedRecipes.rows("XX", " Y", " Y"), 'X', "lotr:mallorn_planks", 'Y', "lotr:mallorn_stick");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.ELVEN, "faction/elven/tool/mallorn_pickaxe", "lotr:mallorn_pickaxe", 1, LOTRTranscribedRecipes.rows("XXX", " Y ", " Y "), 'X', "lotr:mallorn_planks", 'Y', "lotr:mallorn_stick");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.ELVEN, "faction/elven/tool/mallorn_shovel", "lotr:mallorn_shovel", 1, LOTRTranscribedRecipes.rows("X", "Y", "Y"), 'X', "lotr:mallorn_planks", 'Y', "lotr:mallorn_stick");
        // faction/elven/utility
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.ELVEN, "faction/elven/utility/galadhrim_bed", "lotr:galadhrim_bed", 1, LOTRTranscribedRecipes.rows("XXX", "YYY"), 'X', "lotr:hithlain", 'Y', "lotr:mallorn_planks");
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.ELVEN, "faction/elven/utility/mallorn_fence_gate", "lotr:mallorn_fence_gate", 1, LOTRTranscribedRecipes.rows("XYX", "XYX"), 'X', "lotr:mallorn_stick", 'Y', "lotr:mallorn_planks");
        // faction/gondorian/combat
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GONDORIAN, "faction/gondorian/combat/blackroot_bow", "lotr:blackroot_bow", 1, LOTRTranscribedRecipes.rows(" XY", "Z Y", " XY"), 'X', "lotr:blackroot_stick", 'Y', "minecraft:string", 'Z', "lotr:blackroot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GONDORIAN, "faction/gondorian/combat/blackroot_vale_boots", "lotr:blackroot_vale_boots", 1, LOTRTranscribedRecipes.rows("XYX", "X X"), 'X', "minecraft:iron_ingot", 'Y', "#c:dyes/black");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GONDORIAN, "faction/gondorian/combat/blackroot_vale_chestplate", "lotr:blackroot_vale_chestplate", 1, LOTRTranscribedRecipes.rows("XYX", "XXX", "XXX"), 'X', "minecraft:iron_ingot", 'Y', "#c:dyes/black");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GONDORIAN, "faction/gondorian/combat/blackroot_vale_helmet", "lotr:blackroot_vale_helmet", 1, LOTRTranscribedRecipes.rows("XXX", "XYX"), 'X', "minecraft:iron_ingot", 'Y', "#c:dyes/black");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GONDORIAN, "faction/gondorian/combat/blackroot_vale_leggings", "lotr:blackroot_vale_leggings", 1, LOTRTranscribedRecipes.rows("XXX", "XYX", "X X"), 'X', "minecraft:iron_ingot", 'Y', "#c:dyes/black");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GONDORIAN, "faction/gondorian/combat/gondor_gambeson", "lotr:gondor_gambeson", 1, LOTRTranscribedRecipes.rows("X X", "XXX", "XXX"), 'X', "#minecraft:wool");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GONDORIAN, "faction/gondorian/combat/gondor_lance", "lotr:gondor_lance", 1, LOTRTranscribedRecipes.rows("  X", " X ", "Y  "), 'X', "minecraft:iron_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GONDORIAN, "faction/gondorian/combat/gondor_pike", "lotr:gondor_pike", 1, LOTRTranscribedRecipes.rows("  X", " YX", "Y  "), 'X', "minecraft:iron_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GONDORIAN, "faction/gondorian/combat/ithilien_ranger_boots", "lotr:ithilien_ranger_boots", 1, LOTRTranscribedRecipes.rows("X X", "X X"), 'X', "minecraft:leather");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GONDORIAN, "faction/gondorian/combat/ithilien_ranger_hood", "lotr:ithilien_ranger_hood", 1, LOTRTranscribedRecipes.rows("XXX", "X X"), 'X', "minecraft:leather");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GONDORIAN, "faction/gondorian/combat/ithilien_ranger_leggings", "lotr:ithilien_ranger_leggings", 1, LOTRTranscribedRecipes.rows("XXX", "X X", "X X"), 'X', "minecraft:leather");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GONDORIAN, "faction/gondorian/combat/ithilien_ranger_tunic", "lotr:ithilien_ranger_tunic", 1, LOTRTranscribedRecipes.rows("X X", "XXX", "XXX"), 'X', "minecraft:leather");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GONDORIAN, "faction/gondorian/combat/lamedon_boots", "lotr:lamedon_boots", 1, LOTRTranscribedRecipes.rows("YZY", "X X"), 'X', "minecraft:iron_ingot", 'Y', "minecraft:leather", 'Z', "#c:dyes/blue");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GONDORIAN, "faction/gondorian/combat/lamedon_chestplate", "lotr:lamedon_chestplate", 1, LOTRTranscribedRecipes.rows("XZX", "YYY", "XXX"), 'X', "minecraft:iron_ingot", 'Y', "minecraft:leather", 'Z', "#c:dyes/blue");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GONDORIAN, "faction/gondorian/combat/lamedon_helmet", "lotr:lamedon_helmet", 1, LOTRTranscribedRecipes.rows("XXX", "YZY"), 'X', "minecraft:iron_ingot", 'Y', "minecraft:leather", 'Z', "#c:dyes/blue");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GONDORIAN, "faction/gondorian/combat/lamedon_horse_armor", "lotr:lamedon_horse_armor", 1, LOTRTranscribedRecipes.rows("XZ ", "XYX", "XXX"), 'X', "minecraft:iron_ingot", 'Y', "minecraft:leather", 'Z', "#c:dyes/blue");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GONDORIAN, "faction/gondorian/combat/lamedon_jacket", "lotr:lamedon_jacket", 1, LOTRTranscribedRecipes.rows("XYX", "XXX", "XXX"), 'X', "minecraft:leather", 'Y', "#c:dyes/blue");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GONDORIAN, "faction/gondorian/combat/lamedon_leggings", "lotr:lamedon_leggings", 1, LOTRTranscribedRecipes.rows("XXX", "YZY", "X X"), 'X', "minecraft:iron_ingot", 'Y', "minecraft:leather", 'Z', "#c:dyes/blue");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GONDORIAN, "faction/gondorian/combat/lebennin_gambeson", "lotr:lebennin_gambeson", 1, LOTRTranscribedRecipes.rows("XYX", "XXX", "XXX"), 'X', "#minecraft:wool", 'Y', "#c:dyes/light_blue");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GONDORIAN, "faction/gondorian/combat/lossarnach_battleaxe", "lotr:lossarnach_battleaxe", 1, LOTRTranscribedRecipes.rows("XXX", "XYX", " Y "), 'X', "minecraft:iron_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GONDORIAN, "faction/gondorian/combat/lossarnach_boots", "lotr:lossarnach_boots", 1, LOTRTranscribedRecipes.rows("Y Y", "X X"), 'X', "minecraft:iron_ingot", 'Y', "minecraft:leather");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GONDORIAN, "faction/gondorian/combat/lossarnach_chestplate", "lotr:lossarnach_chestplate", 1, LOTRTranscribedRecipes.rows("X X", "YYY", "XXX"), 'X', "minecraft:iron_ingot", 'Y', "minecraft:leather");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GONDORIAN, "faction/gondorian/combat/lossarnach_helmet", "lotr:lossarnach_helmet", 1, LOTRTranscribedRecipes.rows("XXX", "Y Y"), 'X', "minecraft:iron_ingot", 'Y', "minecraft:leather");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GONDORIAN, "faction/gondorian/combat/lossarnach_leggings", "lotr:lossarnach_leggings", 1, LOTRTranscribedRecipes.rows("XXX", "Y Y", "X X"), 'X', "minecraft:iron_ingot", 'Y', "minecraft:leather");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GONDORIAN, "faction/gondorian/combat/lossarnach_throwing_axe", "lotr:lossarnach_throwing_axe", 1, LOTRTranscribedRecipes.rows(" X ", " YX", "Y  "), 'X', "minecraft:iron_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GONDORIAN, "faction/gondorian/combat/pelargir_boots", "lotr:pelargir_boots", 1, LOTRTranscribedRecipes.rows("XYX", "X X"), 'X', "minecraft:iron_ingot", 'Y', "#c:dyes/cyan");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GONDORIAN, "faction/gondorian/combat/pelargir_chestplate", "lotr:pelargir_chestplate", 1, LOTRTranscribedRecipes.rows("XYX", "XXX", "XXX"), 'X', "minecraft:iron_ingot", 'Y', "#c:dyes/cyan");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GONDORIAN, "faction/gondorian/combat/pelargir_eket", "lotr:pelargir_eket", 1, LOTRTranscribedRecipes.rows(" X", "X ", "Y "), 'X', "minecraft:iron_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GONDORIAN, "faction/gondorian/combat/pelargir_helmet", "lotr:pelargir_helmet", 1, LOTRTranscribedRecipes.rows("XXX", "XYX"), 'X', "minecraft:iron_ingot", 'Y', "#c:dyes/cyan");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GONDORIAN, "faction/gondorian/combat/pelargir_leggings", "lotr:pelargir_leggings", 1, LOTRTranscribedRecipes.rows("XXX", "XYX", "X X"), 'X', "minecraft:iron_ingot", 'Y', "#c:dyes/cyan");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GONDORIAN, "faction/gondorian/combat/pelargir_trident", "lotr:pelargir_trident", 1, LOTRTranscribedRecipes.rows(" XX", " YX", "Y  "), 'X', "minecraft:iron_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GONDORIAN, "faction/gondorian/combat/pinnath_gelin_boots", "lotr:pinnath_gelin_boots", 1, LOTRTranscribedRecipes.rows("YZY", "X X"), 'X', "minecraft:iron_ingot", 'Y', "minecraft:leather", 'Z', "#c:dyes/green");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GONDORIAN, "faction/gondorian/combat/pinnath_gelin_chestplate", "lotr:pinnath_gelin_chestplate", 1, LOTRTranscribedRecipes.rows("XZX", "YYY", "XXX"), 'X', "minecraft:iron_ingot", 'Y', "minecraft:leather", 'Z', "#c:dyes/green");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GONDORIAN, "faction/gondorian/combat/pinnath_gelin_helmet", "lotr:pinnath_gelin_helmet", 1, LOTRTranscribedRecipes.rows("XXX", "YZY"), 'X', "minecraft:iron_ingot", 'Y', "minecraft:leather", 'Z', "#c:dyes/green");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GONDORIAN, "faction/gondorian/combat/pinnath_gelin_leggings", "lotr:pinnath_gelin_leggings", 1, LOTRTranscribedRecipes.rows("XXX", "YZY", "X X"), 'X', "minecraft:iron_ingot", 'Y', "minecraft:leather", 'Z', "#c:dyes/green");
        // faction/gondorian/decoration
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.GONDORIAN, "faction/gondorian/decoration/ithilien_banner", "lotr:ithilien_banner", 1, LOTRTranscribedRecipes.rows("XA", "Y ", "Z "), 'A', "lotr:silver_nugget", 'X', "#minecraft:wool", 'Y', "#lotr:sticks", 'Z', "#minecraft:planks");
        // faction/gondorian/misc
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.GONDORIAN, "faction/gondorian/misc/gondor_gate", "lotr:gondor_gate", 4, LOTRTranscribedRecipes.rows("ZYZ", "YXY", "ZYZ"), 'X', "lotr:gate_gears", 'Y', "#minecraft:planks", 'Z', "minecraft:iron_ingot");
        // faction/gondorian/utility
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.GONDORIAN, "faction/gondorian/utility/lebethron_casket", "lotr:lebethron_casket", 1, LOTRTranscribedRecipes.rows("XXX", "XYX", "XXX"), 'X', "lotr:lebethron_planks", 'Y', "lotr:silver_nugget");
        // faction/gulf/block
        r.in(RecipeCategory.BUILDING_BLOCKS).shaped(LOTRCraftingTable.GULF, "faction/gulf/block/near_harad_red_brick", "lotr:near_harad_red_brick", 4, LOTRTranscribedRecipes.rows("XX", "XX"), 'X', "minecraft:red_sandstone");
        r.in(RecipeCategory.BUILDING_BLOCKS).shaped(LOTRCraftingTable.GULF, "faction/gulf/block/near_harad_red_pillar", "lotr:near_harad_red_pillar", 3, LOTRTranscribedRecipes.rows("X", "X", "X"), 'X', "minecraft:red_sandstone");
        // faction/gulf/combat
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GULF, "faction/gulf/combat/gulfen_boots", "lotr:gulfen_boots", 1, LOTRTranscribedRecipes.rows("Y Y", "X X"), 'X', "lotr:bronze_ingot", 'Y', "lotr:dried_reeds");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GULF, "faction/gulf/combat/gulfen_chestplate", "lotr:gulfen_chestplate", 1, LOTRTranscribedRecipes.rows("X X", "YYY", "XXX"), 'X', "lotr:bronze_ingot", 'Y', "lotr:dried_reeds");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GULF, "faction/gulf/combat/gulfen_helmet", "lotr:gulfen_helmet", 1, LOTRTranscribedRecipes.rows("XXX", "Y Y"), 'X', "lotr:bronze_ingot", 'Y', "lotr:dried_reeds");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GULF, "faction/gulf/combat/gulfen_khopesh", "lotr:gulfen_khopesh", 1, LOTRTranscribedRecipes.rows(" X", "X ", "Y "), 'X', "lotr:bronze_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GULF, "faction/gulf/combat/gulfen_leggings", "lotr:gulfen_leggings", 1, LOTRTranscribedRecipes.rows("XXX", "Y Y", "X X"), 'X', "lotr:bronze_ingot", 'Y', "lotr:dried_reeds");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GULF, "faction/gulf/combat/haradric_dagger", "lotr:haradric_dagger", 1, LOTRTranscribedRecipes.rows("X", "Y"), 'X', "lotr:bronze_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GULF, "faction/gulf/combat/haradric_pike", "lotr:haradric_pike", 1, LOTRTranscribedRecipes.rows("  X", " YX", "Y  "), 'X', "lotr:bronze_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GULF, "faction/gulf/combat/haradric_spear", "lotr:haradric_spear", 1, LOTRTranscribedRecipes.rows("  X", " Y ", "Y  "), 'X', "lotr:bronze_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GULF, "faction/gulf/combat/haradric_sword", "lotr:haradric_sword", 1, LOTRTranscribedRecipes.rows("X", "X", "Y"), 'X', "lotr:bronze_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GULF, "faction/gulf/combat/nomad_cap", "lotr:nomad_cap", 1, LOTRTranscribedRecipes.rows("XXX", "X X"), 'X', "lotr:dried_reeds");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GULF, "faction/gulf/combat/nomad_leggings", "lotr:nomad_leggings", 1, LOTRTranscribedRecipes.rows("XXX", "X X", "X X"), 'X', "lotr:dried_reeds");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GULF, "faction/gulf/combat/nomad_shoes", "lotr:nomad_shoes", 1, LOTRTranscribedRecipes.rows("X X", "X X"), 'X', "lotr:dried_reeds");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GULF, "faction/gulf/combat/nomad_tunic", "lotr:nomad_tunic", 1, LOTRTranscribedRecipes.rows("X X", "XXX", "XXX"), 'X', "lotr:dried_reeds");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GULF, "faction/gulf/combat/poisoned_arrow", "lotr:poisoned_arrow", 4, LOTRTranscribedRecipes.rows(" X ", "XYX", " X "), 'X', "minecraft:arrow", 'Y', "lotr:bottle_of_poison");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GULF, "faction/gulf/combat/poisoned_crossbow_bolt", "lotr:poisoned_crossbow_bolt", 4, LOTRTranscribedRecipes.rows(" X ", "XYX", " X "), 'X', "lotr:crossbow_bolt", 'Y', "lotr:bottle_of_poison");
        // faction/gulf/misc
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.GULF, "faction/gulf/misc/near_harad_gate", "lotr:near_harad_gate", 4, LOTRTranscribedRecipes.rows("ZYZ", "YXY", "ZYZ"), 'X', "lotr:gate_gears", 'Y', "#minecraft:planks", 'Z', "lotr:bronze_ingot");
        // faction/gundabad/block
        r.in(RecipeCategory.BUILDING_BLOCKS).shaped(LOTRCraftingTable.GUNDABAD, "faction/gundabad/block/orc_plating_iron", "lotr:orc_plating_iron", 8, LOTRTranscribedRecipes.rows("XX", "XX"), 'X', "lotr:orc_steel_ingot");
        // faction/gundabad/combat
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GUNDABAD, "faction/gundabad/combat/gundabad_uruk_bludgeon", "lotr:gundabad_uruk_bludgeon", 1, LOTRTranscribedRecipes.rows("XYX", "XYX", " Y "), 'X', "lotr:uruk_steel_ingot", 'Y', "#lotr:bones");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GUNDABAD, "faction/gundabad/combat/gundabad_uruk_boots", "lotr:gundabad_uruk_boots", 1, LOTRTranscribedRecipes.rows("X X", "X X"), 'X', "lotr:uruk_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GUNDABAD, "faction/gundabad/combat/gundabad_uruk_bow", "lotr:gundabad_uruk_bow", 1, LOTRTranscribedRecipes.rows(" XY", "X Y", " XY"), 'X', "lotr:uruk_steel_ingot", 'Y', "minecraft:string");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GUNDABAD, "faction/gundabad/combat/gundabad_uruk_chestplate", "lotr:gundabad_uruk_chestplate", 1, LOTRTranscribedRecipes.rows("X X", "XXX", "XXX"), 'X', "lotr:uruk_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GUNDABAD, "faction/gundabad/combat/gundabad_uruk_cleaver", "lotr:gundabad_uruk_cleaver", 1, LOTRTranscribedRecipes.rows("X", "X", "Y"), 'X', "lotr:uruk_steel_ingot", 'Y', "#lotr:bones");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GUNDABAD, "faction/gundabad/combat/gundabad_uruk_dagger", "lotr:gundabad_uruk_dagger", 1, LOTRTranscribedRecipes.rows("X", "Y"), 'X', "lotr:uruk_steel_ingot", 'Y', "#lotr:bones");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GUNDABAD, "faction/gundabad/combat/gundabad_uruk_helmet", "lotr:gundabad_uruk_helmet", 1, LOTRTranscribedRecipes.rows("XXX", "X X"), 'X', "lotr:uruk_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GUNDABAD, "faction/gundabad/combat/gundabad_uruk_leggings", "lotr:gundabad_uruk_leggings", 1, LOTRTranscribedRecipes.rows("XXX", "X X", "X X"), 'X', "lotr:uruk_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GUNDABAD, "faction/gundabad/combat/gundabad_uruk_pike", "lotr:gundabad_uruk_pike", 1, LOTRTranscribedRecipes.rows("  X", " YX", "Y  "), 'X', "lotr:uruk_steel_ingot", 'Y', "#lotr:bones");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GUNDABAD, "faction/gundabad/combat/gundabad_uruk_spear", "lotr:gundabad_uruk_spear", 1, LOTRTranscribedRecipes.rows("  X", " Y ", "Y  "), 'X', "lotr:uruk_steel_ingot", 'Y', "#lotr:bones");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GUNDABAD, "faction/gundabad/combat/gundabad_uruk_waraxe", "lotr:gundabad_uruk_waraxe", 1, LOTRTranscribedRecipes.rows("XXX", "XYX", " Y "), 'X', "lotr:uruk_steel_ingot", 'Y', "#lotr:bones");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GUNDABAD, "faction/gundabad/combat/orc_bow", "lotr:orc_bow", 1, LOTRTranscribedRecipes.rows(" XY", "X Y", " XY"), 'X', "lotr:orc_steel_ingot", 'Y', "minecraft:string");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GUNDABAD, "faction/gundabad/combat/poisoned_arrow", "lotr:poisoned_arrow", 4, LOTRTranscribedRecipes.rows(" X ", "XYX", " X "), 'X', "minecraft:arrow", 'Y', "lotr:bottle_of_poison");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.GUNDABAD, "faction/gundabad/combat/poisoned_crossbow_bolt", "lotr:poisoned_crossbow_bolt", 4, LOTRTranscribedRecipes.rows(" X ", "XYX", " X "), 'X', "lotr:crossbow_bolt", 'Y', "lotr:bottle_of_poison");
        // faction/gundabad/decoration
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.GUNDABAD, "faction/gundabad/decoration/orc_chandelier", "lotr:orc_chandelier", 2, LOTRTranscribedRecipes.rows(" X ", "YZY"), 'X', "#lotr:sticks", 'Y', "lotr:orc_torch", 'Z', "lotr:orc_steel_ingot");
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.GUNDABAD, "faction/gundabad/decoration/orc_steel_bars", "lotr:orc_steel_bars", 16, LOTRTranscribedRecipes.rows("XXX", "XXX"), 'X', "lotr:orc_steel_ingot");
        // faction/gundabad/food
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.GUNDABAD, "faction/gundabad/food/maggoty_bread_gundabad", "lotr:maggoty_bread", 1, LOTRTranscribedRecipes.rows("XXX"), 'X', "minecraft:wheat");
        // faction/gundabad/misc
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.GUNDABAD, "faction/gundabad/misc/mechanism", "lotr:mechanism", 1, LOTRTranscribedRecipes.rows(" X ", "YZY", " X "), 'X', "minecraft:copper_ingot", 'Y', "minecraft:flint", 'Z', "lotr:orc_steel_ingot");
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.GUNDABAD, "faction/gundabad/misc/orc_gate", "lotr:orc_gate", 4, LOTRTranscribedRecipes.rows("YYY", "YXY", "YYY"), 'X', "lotr:gate_gears", 'Y', "lotr:orc_steel_ingot");
        // faction/gundabad/utility
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.GUNDABAD, "faction/gundabad/utility/orc_chain", "lotr:orc_chain", 8, LOTRTranscribedRecipes.rows("X", "X", "X"), 'X', "lotr:orc_steel_ingot");
        // faction/half_troll/block
        r.in(RecipeCategory.BUILDING_BLOCKS).shaped(LOTRCraftingTable.HALF_TROLL, "faction/half_troll/block/orc_plating_iron", "lotr:orc_plating_iron", 8, LOTRTranscribedRecipes.rows("XX", "XX"), 'X', "lotr:orc_steel_ingot");
        // faction/half_troll/combat
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.HALF_TROLL, "faction/half_troll/combat/half_troll_boots", "lotr:half_troll_boots", 1, LOTRTranscribedRecipes.rows("Y Y", "X X"), 'X', "lotr:gemsbok_hide", 'Y', "minecraft:string");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.HALF_TROLL, "faction/half_troll/combat/half_troll_chestplate", "lotr:half_troll_chestplate", 1, LOTRTranscribedRecipes.rows("X X", "XYX", "XYX"), 'X', "lotr:gemsbok_hide", 'Y', "minecraft:string");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.HALF_TROLL, "faction/half_troll/combat/half_troll_helmet", "lotr:half_troll_helmet", 1, LOTRTranscribedRecipes.rows("XXX", "Y Y"), 'X', "lotr:gemsbok_hide", 'Y', "minecraft:string");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.HALF_TROLL, "faction/half_troll/combat/half_troll_leggings", "lotr:half_troll_leggings", 1, LOTRTranscribedRecipes.rows("XXX", "Y Y", "X X"), 'X', "lotr:gemsbok_hide", 'Y', "minecraft:string");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.HALF_TROLL, "faction/half_troll/combat/half_troll_rhino_armor", "lotr:half_troll_rhino_armor", 1, LOTRTranscribedRecipes.rows("X  ", "XYX", "XXX"), 'X', "lotr:gemsbok_hide", 'Y', "minecraft:string");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.HALF_TROLL, "faction/half_troll/combat/poisoned_arrow", "lotr:poisoned_arrow", 4, LOTRTranscribedRecipes.rows(" X ", "XYX", " X "), 'X', "minecraft:arrow", 'Y', "lotr:bottle_of_poison");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.HALF_TROLL, "faction/half_troll/combat/poisoned_crossbow_bolt", "lotr:poisoned_crossbow_bolt", 4, LOTRTranscribedRecipes.rows(" X ", "XYX", " X "), 'X', "lotr:crossbow_bolt", 'Y', "lotr:bottle_of_poison");
        // faction/half_troll/food
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.HALF_TROLL, "faction/half_troll/food/maggoty_bread_half_troll", "lotr:maggoty_bread", 1, LOTRTranscribedRecipes.rows("XXX"), 'X', "minecraft:wheat");
        r.in(RecipeCategory.MISC).shapeless(LOTRCraftingTable.HALF_TROLL, "faction/half_troll/food/torog_stew", "lotr:torog_stew", 1, "minecraft:bowl", "minecraft:rotten_flesh", "#lotr:bones", "minecraft:dirt");
        // faction/half_troll/utility
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.HALF_TROLL, "faction/half_troll/utility/orc_chain", "lotr:orc_chain", 8, LOTRTranscribedRecipes.rows("X", "X", "X"), 'X', "lotr:orc_steel_ingot");
        // faction/high_elven/block
        r.in(RecipeCategory.BUILDING_BLOCKS).shaped(LOTRCraftingTable.HIGH_ELVEN, "faction/high_elven/block/high_elven_silver_brick", "lotr:high_elven_silver_brick", 1, LOTRTranscribedRecipes.rows(" X ", "XYX", " X "), 'X', "lotr:silver_nugget", 'Y', "lotr:high_elven_brick");
        // faction/high_elven/combat
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.HIGH_ELVEN, "faction/high_elven/combat/galvorn_boots", "lotr:galvorn_boots", 1, LOTRTranscribedRecipes.rows("X X", "X X"), 'X', "lotr:galvorn_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.HIGH_ELVEN, "faction/high_elven/combat/galvorn_chestplate", "lotr:galvorn_chestplate", 1, LOTRTranscribedRecipes.rows("X X", "XXX", "XXX"), 'X', "lotr:galvorn_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.HIGH_ELVEN, "faction/high_elven/combat/galvorn_helmet", "lotr:galvorn_helmet", 1, LOTRTranscribedRecipes.rows("XXX", "X X"), 'X', "lotr:galvorn_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.HIGH_ELVEN, "faction/high_elven/combat/galvorn_leggings", "lotr:galvorn_leggings", 1, LOTRTranscribedRecipes.rows("XXX", "X X", "X X"), 'X', "lotr:galvorn_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.HIGH_ELVEN, "faction/high_elven/combat/lindon_battlestaff", "lotr:lindon_battlestaff", 1, LOTRTranscribedRecipes.rows("  X", " Y ", "X  "), 'X', "lotr:elven_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.HIGH_ELVEN, "faction/high_elven/combat/lindon_boots", "lotr:lindon_boots", 1, LOTRTranscribedRecipes.rows("X X", "X X"), 'X', "lotr:elven_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.HIGH_ELVEN, "faction/high_elven/combat/lindon_bow", "lotr:lindon_bow", 1, LOTRTranscribedRecipes.rows(" XY", "X Y", " XY"), 'X', "lotr:elven_steel_ingot", 'Y', "minecraft:string");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.HIGH_ELVEN, "faction/high_elven/combat/lindon_chestplate", "lotr:lindon_chestplate", 1, LOTRTranscribedRecipes.rows("X X", "XXX", "XXX"), 'X', "lotr:elven_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.HIGH_ELVEN, "faction/high_elven/combat/lindon_dagger", "lotr:lindon_dagger", 1, LOTRTranscribedRecipes.rows("X", "Y"), 'X', "lotr:elven_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.HIGH_ELVEN, "faction/high_elven/combat/lindon_helmet", "lotr:lindon_helmet", 1, LOTRTranscribedRecipes.rows("XXX", "X X"), 'X', "lotr:elven_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.HIGH_ELVEN, "faction/high_elven/combat/lindon_horse_armor", "lotr:lindon_horse_armor", 1, LOTRTranscribedRecipes.rows("X  ", "XYX", "XXX"), 'X', "lotr:elven_steel_ingot", 'Y', "minecraft:leather");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.HIGH_ELVEN, "faction/high_elven/combat/lindon_leggings", "lotr:lindon_leggings", 1, LOTRTranscribedRecipes.rows("XXX", "X X", "X X"), 'X', "lotr:elven_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.HIGH_ELVEN, "faction/high_elven/combat/lindon_longspear", "lotr:lindon_longspear", 1, LOTRTranscribedRecipes.rows("  X", " YX", "Y  "), 'X', "lotr:elven_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.HIGH_ELVEN, "faction/high_elven/combat/lindon_spear", "lotr:lindon_spear", 1, LOTRTranscribedRecipes.rows("  X", " Y ", "Y  "), 'X', "lotr:elven_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.HIGH_ELVEN, "faction/high_elven/combat/lindon_sword", "lotr:lindon_sword", 1, LOTRTranscribedRecipes.rows("X", "X", "Y"), 'X', "lotr:elven_steel_ingot", 'Y', "#lotr:sticks");
        // faction/high_elven/decoration
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.HIGH_ELVEN, "faction/high_elven/decoration/high_elf_bars", "lotr:high_elf_bars", 16, LOTRTranscribedRecipes.rows("XXX", "XXX"), 'X', "lotr:elven_steel_ingot");
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.HIGH_ELVEN, "faction/high_elven/decoration/high_elven_chandelier", "lotr:high_elven_chandelier", 2, LOTRTranscribedRecipes.rows(" X ", "YZY"), 'X', "#lotr:sticks", 'Y', "lotr:high_elven_torch", 'Z', "lotr:elven_steel_ingot");
        // faction/high_elven/misc
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.HIGH_ELVEN, "faction/high_elven/misc/high_elven_gate", "lotr:high_elven_gate", 4, LOTRTranscribedRecipes.rows("ZYZ", "YXY", "ZYZ"), 'X', "lotr:gate_gears", 'Y', "#minecraft:planks", 'Z', "lotr:elven_steel_ingot");
        // faction/high_elven/tool
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.HIGH_ELVEN, "faction/high_elven/tool/lindon_axe", "lotr:lindon_axe", 1, LOTRTranscribedRecipes.rows("XX", "XY", " Y"), 'X', "lotr:elven_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.HIGH_ELVEN, "faction/high_elven/tool/lindon_hoe", "lotr:lindon_hoe", 1, LOTRTranscribedRecipes.rows("XX", " Y", " Y"), 'X', "lotr:elven_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.HIGH_ELVEN, "faction/high_elven/tool/lindon_pickaxe", "lotr:lindon_pickaxe", 1, LOTRTranscribedRecipes.rows("XXX", " Y ", " Y "), 'X', "lotr:elven_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.HIGH_ELVEN, "faction/high_elven/tool/lindon_shovel", "lotr:lindon_shovel", 1, LOTRTranscribedRecipes.rows("X", "Y", "Y"), 'X', "lotr:elven_steel_ingot", 'Y', "#lotr:sticks");
        // faction/hobbit/food
        r.in(RecipeCategory.MISC).shapeless(LOTRCraftingTable.HOBBIT, "faction/hobbit/food/hobbit_pancake", "lotr:hobbit_pancake", 1, "minecraft:wheat", "minecraft:egg", "minecraft:milk_bucket");
        // faction/hobbit/misc
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.HOBBIT, "faction/hobbit/misc/blue_hobbit_gate", "lotr:blue_hobbit_gate", 4, LOTRTranscribedRecipes.rows("YYY", "ZXZ", "YYY"), 'X', "lotr:gate_gears", 'Y', "#minecraft:planks", 'Z', "#c:dyes/blue");
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.HOBBIT, "faction/hobbit/misc/green_hobbit_gate", "lotr:green_hobbit_gate", 4, LOTRTranscribedRecipes.rows("YYY", "ZXZ", "YYY"), 'X', "lotr:gate_gears", 'Y', "#minecraft:planks", 'Z', "#c:dyes/green");
        r.in(RecipeCategory.MISC).shapeless(LOTRCraftingTable.HOBBIT, "faction/hobbit/misc/hobbit_pancake_with_maple_syrup_1", "lotr:hobbit_pancake_with_maple_syrup", 1, "lotr:maple_syrup", "lotr:hobbit_pancake");
        r.in(RecipeCategory.MISC).shapeless(LOTRCraftingTable.HOBBIT, "faction/hobbit/misc/hobbit_pancake_with_maple_syrup_2", "lotr:hobbit_pancake_with_maple_syrup", 2, "lotr:maple_syrup", "lotr:hobbit_pancake", "lotr:hobbit_pancake");
        r.in(RecipeCategory.MISC).shapeless(LOTRCraftingTable.HOBBIT, "faction/hobbit/misc/hobbit_pancake_with_maple_syrup_3", "lotr:hobbit_pancake_with_maple_syrup", 3, "lotr:maple_syrup", "lotr:hobbit_pancake", "lotr:hobbit_pancake", "lotr:hobbit_pancake");
        r.in(RecipeCategory.MISC).shapeless(LOTRCraftingTable.HOBBIT, "faction/hobbit/misc/hobbit_pancake_with_maple_syrup_4", "lotr:hobbit_pancake_with_maple_syrup", 4, "lotr:maple_syrup", "lotr:hobbit_pancake", "lotr:hobbit_pancake", "lotr:hobbit_pancake", "lotr:hobbit_pancake");
        r.in(RecipeCategory.MISC).shapeless(LOTRCraftingTable.HOBBIT, "faction/hobbit/misc/hobbit_pancake_with_maple_syrup_5", "lotr:hobbit_pancake_with_maple_syrup", 5, "lotr:maple_syrup", "lotr:hobbit_pancake", "lotr:hobbit_pancake", "lotr:hobbit_pancake", "lotr:hobbit_pancake", "lotr:hobbit_pancake");
        r.in(RecipeCategory.MISC).shapeless(LOTRCraftingTable.HOBBIT, "faction/hobbit/misc/hobbit_pancake_with_maple_syrup_6", "lotr:hobbit_pancake_with_maple_syrup", 6, "lotr:maple_syrup", "lotr:hobbit_pancake", "lotr:hobbit_pancake", "lotr:hobbit_pancake", "lotr:hobbit_pancake", "lotr:hobbit_pancake", "lotr:hobbit_pancake");
        r.in(RecipeCategory.MISC).shapeless(LOTRCraftingTable.HOBBIT, "faction/hobbit/misc/hobbit_pancake_with_maple_syrup_7", "lotr:hobbit_pancake_with_maple_syrup", 7, "lotr:maple_syrup", "lotr:hobbit_pancake", "lotr:hobbit_pancake", "lotr:hobbit_pancake", "lotr:hobbit_pancake", "lotr:hobbit_pancake", "lotr:hobbit_pancake", "lotr:hobbit_pancake");
        r.in(RecipeCategory.MISC).shapeless(LOTRCraftingTable.HOBBIT, "faction/hobbit/misc/hobbit_pancake_with_maple_syrup_8", "lotr:hobbit_pancake_with_maple_syrup", 8, "lotr:maple_syrup", "lotr:hobbit_pancake", "lotr:hobbit_pancake", "lotr:hobbit_pancake", "lotr:hobbit_pancake", "lotr:hobbit_pancake", "lotr:hobbit_pancake", "lotr:hobbit_pancake", "lotr:hobbit_pancake");
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.HOBBIT, "faction/hobbit/misc/red_hobbit_gate", "lotr:red_hobbit_gate", 4, LOTRTranscribedRecipes.rows("YYY", "ZXZ", "YYY"), 'X', "lotr:gate_gears", 'Y', "#minecraft:planks", 'Z', "#c:dyes/red");
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.HOBBIT, "faction/hobbit/misc/yellow_hobbit_gate", "lotr:yellow_hobbit_gate", 4, LOTRTranscribedRecipes.rows("YYY", "ZXZ", "YYY"), 'X', "lotr:gate_gears", 'Y', "#minecraft:planks", 'Z', "#c:dyes/yellow");
        // faction/moredain/combat
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.MOREDAIN, "faction/moredain/combat/morwaith_battleaxe", "lotr:morwaith_battleaxe", 1, LOTRTranscribedRecipes.rows("XXX", "XYX", " Y "), 'X', "lotr:rhino_horn", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.MOREDAIN, "faction/moredain/combat/morwaith_boots", "lotr:morwaith_boots", 1, LOTRTranscribedRecipes.rows("X X", "X X"), 'X', "lotr:gemsbok_hide");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.MOREDAIN, "faction/moredain/combat/morwaith_chestplate", "lotr:morwaith_chestplate", 1, LOTRTranscribedRecipes.rows("X X", "XXX", "XXX"), 'X', "lotr:gemsbok_hide");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.MOREDAIN, "faction/moredain/combat/morwaith_chieftain_boots", "lotr:morwaith_chieftain_boots", 1, LOTRTranscribedRecipes.rows("X X", "X X"), 'X', "lotr:lion_fur");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.MOREDAIN, "faction/moredain/combat/morwaith_chieftain_chestplate", "lotr:morwaith_chieftain_chestplate", 1, LOTRTranscribedRecipes.rows("X X", "XXX", "XXX"), 'X', "lotr:lion_fur");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.MOREDAIN, "faction/moredain/combat/morwaith_chieftain_helmet", "lotr:morwaith_chieftain_helmet", 1, LOTRTranscribedRecipes.rows("XXX", "X X"), 'X', "lotr:lion_fur");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.MOREDAIN, "faction/moredain/combat/morwaith_chieftain_leggings", "lotr:morwaith_chieftain_leggings", 1, LOTRTranscribedRecipes.rows("XXX", "X X", "X X"), 'X', "lotr:lion_fur");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.MOREDAIN, "faction/moredain/combat/morwaith_club", "lotr:morwaith_club", 1, LOTRTranscribedRecipes.rows("X", "X", "X"), 'X', "#minecraft:planks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.MOREDAIN, "faction/moredain/combat/morwaith_dagger", "lotr:morwaith_dagger", 1, LOTRTranscribedRecipes.rows("X", "Y"), 'X', "lotr:rhino_horn", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.MOREDAIN, "faction/moredain/combat/morwaith_helmet", "lotr:morwaith_helmet", 1, LOTRTranscribedRecipes.rows("XXX", "X X"), 'X', "lotr:gemsbok_hide");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.MOREDAIN, "faction/moredain/combat/morwaith_leggings", "lotr:morwaith_leggings", 1, LOTRTranscribedRecipes.rows("XXX", "X X", "X X"), 'X', "lotr:gemsbok_hide");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.MOREDAIN, "faction/moredain/combat/morwaith_spear", "lotr:morwaith_spear", 1, LOTRTranscribedRecipes.rows("  X", " X ", "X  "), 'X', "lotr:gemsbok_horn");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.MOREDAIN, "faction/moredain/combat/morwaith_sword", "lotr:morwaith_sword", 1, LOTRTranscribedRecipes.rows("X", "X", "Y"), 'X', "lotr:bronze_ingot", 'Y', "#lotr:sticks");
        // faction/morgul/block
        r.in(RecipeCategory.BUILDING_BLOCKS).shaped(LOTRCraftingTable.MORGUL, "faction/morgul/block/orc_plating_iron", "lotr:orc_plating_iron", 8, LOTRTranscribedRecipes.rows("XX", "XX"), 'X', "lotr:orc_steel_ingot");
        // faction/morgul/combat
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.MORGUL, "faction/morgul/combat/black_uruk_battleaxe", "lotr:black_uruk_battleaxe", 1, LOTRTranscribedRecipes.rows("XXX", "XYX", " Y "), 'X', "lotr:black_uruk_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.MORGUL, "faction/morgul/combat/black_uruk_boots", "lotr:black_uruk_boots", 1, LOTRTranscribedRecipes.rows("X X", "X X"), 'X', "lotr:black_uruk_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.MORGUL, "faction/morgul/combat/black_uruk_bow", "lotr:black_uruk_bow", 1, LOTRTranscribedRecipes.rows(" XY", "X Y", " XY"), 'X', "lotr:black_uruk_steel_ingot", 'Y', "minecraft:string");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.MORGUL, "faction/morgul/combat/black_uruk_chestplate", "lotr:black_uruk_chestplate", 1, LOTRTranscribedRecipes.rows("X X", "XXX", "XXX"), 'X', "lotr:black_uruk_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.MORGUL, "faction/morgul/combat/black_uruk_cleaver", "lotr:black_uruk_cleaver", 1, LOTRTranscribedRecipes.rows("X", "X", "Y"), 'X', "lotr:black_uruk_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.MORGUL, "faction/morgul/combat/black_uruk_dagger", "lotr:black_uruk_dagger", 1, LOTRTranscribedRecipes.rows("X", "Y"), 'X', "lotr:black_uruk_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.MORGUL, "faction/morgul/combat/black_uruk_helmet", "lotr:black_uruk_helmet", 1, LOTRTranscribedRecipes.rows("XXX", "X X"), 'X', "lotr:black_uruk_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.MORGUL, "faction/morgul/combat/black_uruk_leggings", "lotr:black_uruk_leggings", 1, LOTRTranscribedRecipes.rows("XXX", "X X", "X X"), 'X', "lotr:black_uruk_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.MORGUL, "faction/morgul/combat/black_uruk_spear", "lotr:black_uruk_spear", 1, LOTRTranscribedRecipes.rows("  X", " Y ", "Y  "), 'X', "lotr:black_uruk_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.MORGUL, "faction/morgul/combat/black_uruk_warhammer", "lotr:black_uruk_warhammer", 1, LOTRTranscribedRecipes.rows("XYX", "XYX", " Y "), 'X', "lotr:black_uruk_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shapeless(LOTRCraftingTable.MORGUL, "faction/morgul/combat/double_strength_orc_bomb", "lotr:double_strength_orc_bomb", 1, "lotr:orc_bomb", "minecraft:gunpowder", "lotr:orc_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.MORGUL, "faction/morgul/combat/mordor_battleaxe", "lotr:mordor_battleaxe", 1, LOTRTranscribedRecipes.rows("XXX", "XYX", " Y "), 'X', "lotr:orc_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.MORGUL, "faction/morgul/combat/mordor_boots", "lotr:mordor_boots", 1, LOTRTranscribedRecipes.rows("X X", "X X"), 'X', "lotr:orc_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.MORGUL, "faction/morgul/combat/mordor_chestplate", "lotr:mordor_chestplate", 1, LOTRTranscribedRecipes.rows("X X", "XXX", "XXX"), 'X', "lotr:orc_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.MORGUL, "faction/morgul/combat/mordor_dagger", "lotr:mordor_dagger", 1, LOTRTranscribedRecipes.rows("X", "Y"), 'X', "lotr:orc_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.MORGUL, "faction/morgul/combat/mordor_helmet", "lotr:mordor_helmet", 1, LOTRTranscribedRecipes.rows("XXX", "X X"), 'X', "lotr:orc_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.MORGUL, "faction/morgul/combat/mordor_leggings", "lotr:mordor_leggings", 1, LOTRTranscribedRecipes.rows("XXX", "X X", "X X"), 'X', "lotr:orc_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.MORGUL, "faction/morgul/combat/mordor_scimitar", "lotr:mordor_scimitar", 1, LOTRTranscribedRecipes.rows("X", "X", "Y"), 'X', "lotr:orc_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.MORGUL, "faction/morgul/combat/mordor_spear", "lotr:mordor_spear", 1, LOTRTranscribedRecipes.rows("  X", " Y ", "Y  "), 'X', "lotr:orc_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.MORGUL, "faction/morgul/combat/mordor_warg_armor", "lotr:mordor_warg_armor", 1, LOTRTranscribedRecipes.rows("X  ", "XYX", "XXX"), 'X', "lotr:orc_steel_ingot", 'Y', "minecraft:leather");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.MORGUL, "faction/morgul/combat/mordor_warhammer", "lotr:mordor_warhammer", 1, LOTRTranscribedRecipes.rows("XYX", "XYX", " Y "), 'X', "lotr:orc_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.MORGUL, "faction/morgul/combat/mordor_warscythe", "lotr:mordor_warscythe", 1, LOTRTranscribedRecipes.rows(" XX", " YX", "Y  "), 'X', "lotr:orc_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.MORGUL, "faction/morgul/combat/morgul_blade", "lotr:morgul_blade", 1, LOTRTranscribedRecipes.rows("X", "X", "Y"), 'X', "lotr:morgul_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.MORGUL, "faction/morgul/combat/morgul_boots", "lotr:morgul_boots", 1, LOTRTranscribedRecipes.rows("X X", "X X"), 'X', "lotr:morgul_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.MORGUL, "faction/morgul/combat/morgul_chestplate", "lotr:morgul_chestplate", 1, LOTRTranscribedRecipes.rows("X X", "XXX", "XXX"), 'X', "lotr:morgul_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.MORGUL, "faction/morgul/combat/morgul_helmet", "lotr:morgul_helmet", 1, LOTRTranscribedRecipes.rows("XXX", "X X"), 'X', "lotr:morgul_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.MORGUL, "faction/morgul/combat/morgul_horse_armor", "lotr:morgul_horse_armor", 1, LOTRTranscribedRecipes.rows("X  ", "XYX", "XXX"), 'X', "lotr:morgul_steel_ingot", 'Y', "minecraft:leather");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.MORGUL, "faction/morgul/combat/morgul_leggings", "lotr:morgul_leggings", 1, LOTRTranscribedRecipes.rows("XXX", "X X", "X X"), 'X', "lotr:morgul_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.MORGUL, "faction/morgul/combat/orc_bomb", "lotr:orc_bomb", 4, LOTRTranscribedRecipes.rows("XYX", "YXY", "XYX"), 'X', "minecraft:gunpowder", 'Y', "lotr:orc_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.MORGUL, "faction/morgul/combat/orc_bow", "lotr:orc_bow", 1, LOTRTranscribedRecipes.rows(" XY", "X Y", " XY"), 'X', "lotr:orc_steel_ingot", 'Y', "minecraft:string");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.MORGUL, "faction/morgul/combat/poisoned_arrow", "lotr:poisoned_arrow", 4, LOTRTranscribedRecipes.rows(" X ", "XYX", " X "), 'X', "minecraft:arrow", 'Y', "lotr:bottle_of_poison");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.MORGUL, "faction/morgul/combat/poisoned_crossbow_bolt", "lotr:poisoned_crossbow_bolt", 4, LOTRTranscribedRecipes.rows(" X ", "XYX", " X "), 'X', "lotr:crossbow_bolt", 'Y', "lotr:bottle_of_poison");
        r.in(RecipeCategory.COMBAT).shapeless(LOTRCraftingTable.MORGUL, "faction/morgul/combat/triple_strength_orc_bomb", "lotr:triple_strength_orc_bomb", 1, "lotr:double_strength_orc_bomb", "minecraft:gunpowder", "lotr:orc_steel_ingot");
        // faction/morgul/decoration
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.MORGUL, "faction/morgul/decoration/black_uruk_banner", "lotr:black_uruk_banner", 1, LOTRTranscribedRecipes.rows("XA", "Y ", "Z "), 'A', "lotr:black_uruk_steel_ingot", 'X', "#minecraft:wool", 'Y', "#lotr:sticks", 'Z', "#minecraft:planks");
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.MORGUL, "faction/morgul/decoration/morgul_chandelier", "lotr:morgul_chandelier", 2, LOTRTranscribedRecipes.rows(" X ", "YZY"), 'X', "#lotr:sticks", 'Y', "lotr:morgul_torch", 'Z', "lotr:morgul_steel_ingot");
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.MORGUL, "faction/morgul/decoration/orc_chandelier", "lotr:orc_chandelier", 2, LOTRTranscribedRecipes.rows(" X ", "YZY"), 'X', "#lotr:sticks", 'Y', "lotr:orc_torch", 'Z', "lotr:orc_steel_ingot");
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.MORGUL, "faction/morgul/decoration/orc_steel_bars", "lotr:orc_steel_bars", 16, LOTRTranscribedRecipes.rows("XXX", "XXX"), 'X', "lotr:orc_steel_ingot");
        // faction/morgul/food
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.MORGUL, "faction/morgul/food/maggoty_bread_morgul", "lotr:maggoty_bread", 1, LOTRTranscribedRecipes.rows("XXX"), 'X', "minecraft:wheat");
        // faction/morgul/misc
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.MORGUL, "faction/morgul/misc/mechanism", "lotr:mechanism", 1, LOTRTranscribedRecipes.rows(" X ", "YZY", " X "), 'X', "minecraft:copper_ingot", 'Y', "minecraft:flint", 'Z', "lotr:orc_steel_ingot");
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.MORGUL, "faction/morgul/misc/orc_gate", "lotr:orc_gate", 4, LOTRTranscribedRecipes.rows("YYY", "YXY", "YYY"), 'X', "lotr:gate_gears", 'Y', "lotr:orc_steel_ingot");
        // faction/morgul/tool
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.MORGUL, "faction/morgul/tool/mordor_axe", "lotr:mordor_axe", 1, LOTRTranscribedRecipes.rows("XX", "XY", " Y"), 'X', "lotr:orc_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.MORGUL, "faction/morgul/tool/mordor_hoe", "lotr:mordor_hoe", 1, LOTRTranscribedRecipes.rows("XX", " Y", " Y"), 'X', "lotr:orc_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.MORGUL, "faction/morgul/tool/mordor_pickaxe", "lotr:mordor_pickaxe", 1, LOTRTranscribedRecipes.rows("XXX", " Y ", " Y "), 'X', "lotr:orc_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.MORGUL, "faction/morgul/tool/mordor_shovel", "lotr:mordor_shovel", 1, LOTRTranscribedRecipes.rows("X", "Y", "Y"), 'X', "lotr:orc_steel_ingot", 'Y', "#lotr:sticks");
        // faction/morgul/utility
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.MORGUL, "faction/morgul/utility/orc_chain", "lotr:orc_chain", 8, LOTRTranscribedRecipes.rows("X", "X", "X"), 'X', "lotr:orc_steel_ingot");
        // faction/near_harad/block
        r.in(RecipeCategory.BUILDING_BLOCKS).shaped(LOTRCraftingTable.NEAR_HARAD, "faction/near_harad/block/near_harad_red_brick", "lotr:near_harad_red_brick", 4, LOTRTranscribedRecipes.rows("XX", "XX"), 'X', "minecraft:red_sandstone");
        r.in(RecipeCategory.BUILDING_BLOCKS).shaped(LOTRCraftingTable.NEAR_HARAD, "faction/near_harad/block/near_harad_red_pillar", "lotr:near_harad_red_pillar", 3, LOTRTranscribedRecipes.rows("X", "X", "X"), 'X', "minecraft:red_sandstone");
        // faction/near_harad/combat
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.NEAR_HARAD, "faction/near_harad/combat/coast_southron_boots", "lotr:coast_southron_boots", 1, LOTRTranscribedRecipes.rows("X X", "X X"), 'X', "lotr:bronze_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.NEAR_HARAD, "faction/near_harad/combat/coast_southron_chestplate", "lotr:coast_southron_chestplate", 1, LOTRTranscribedRecipes.rows("X X", "XXX", "XXX"), 'X', "lotr:bronze_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.NEAR_HARAD, "faction/near_harad/combat/coast_southron_helmet", "lotr:coast_southron_helmet", 1, LOTRTranscribedRecipes.rows("XXX", "X X"), 'X', "lotr:bronze_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.NEAR_HARAD, "faction/near_harad/combat/coast_southron_horse_armor", "lotr:coast_southron_horse_armor", 1, LOTRTranscribedRecipes.rows("X  ", "XYX", "XXX"), 'X', "lotr:bronze_ingot", 'Y', "minecraft:leather");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.NEAR_HARAD, "faction/near_harad/combat/coast_southron_leggings", "lotr:coast_southron_leggings", 1, LOTRTranscribedRecipes.rows("XXX", "X X", "X X"), 'X', "lotr:bronze_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.NEAR_HARAD, "faction/near_harad/combat/haradric_dagger", "lotr:haradric_dagger", 1, LOTRTranscribedRecipes.rows("X", "Y"), 'X', "lotr:bronze_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.NEAR_HARAD, "faction/near_harad/combat/haradric_pike", "lotr:haradric_pike", 1, LOTRTranscribedRecipes.rows("  X", " YX", "Y  "), 'X', "lotr:bronze_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.NEAR_HARAD, "faction/near_harad/combat/haradric_spear", "lotr:haradric_spear", 1, LOTRTranscribedRecipes.rows("  X", " Y ", "Y  "), 'X', "lotr:bronze_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.NEAR_HARAD, "faction/near_harad/combat/haradric_sword", "lotr:haradric_sword", 1, LOTRTranscribedRecipes.rows("X", "X", "Y"), 'X', "lotr:bronze_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.NEAR_HARAD, "faction/near_harad/combat/harnennor_boots", "lotr:harnennor_boots", 1, LOTRTranscribedRecipes.rows("Y Y", "X X"), 'X', "lotr:bronze_ingot", 'Y', "minecraft:leather");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.NEAR_HARAD, "faction/near_harad/combat/harnennor_chestplate", "lotr:harnennor_chestplate", 1, LOTRTranscribedRecipes.rows("X X", "YYY", "XXX"), 'X', "lotr:bronze_ingot", 'Y', "minecraft:leather");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.NEAR_HARAD, "faction/near_harad/combat/harnennor_helmet", "lotr:harnennor_helmet", 1, LOTRTranscribedRecipes.rows("XXX", "Y Y"), 'X', "lotr:bronze_ingot", 'Y', "minecraft:leather");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.NEAR_HARAD, "faction/near_harad/combat/harnennor_leggings", "lotr:harnennor_leggings", 1, LOTRTranscribedRecipes.rows("XXX", "Y Y", "X X"), 'X', "lotr:bronze_ingot", 'Y', "minecraft:leather");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.NEAR_HARAD, "faction/near_harad/combat/nomad_cap", "lotr:nomad_cap", 1, LOTRTranscribedRecipes.rows("XXX", "X X"), 'X', "lotr:dried_reeds");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.NEAR_HARAD, "faction/near_harad/combat/nomad_leggings", "lotr:nomad_leggings", 1, LOTRTranscribedRecipes.rows("XXX", "X X", "X X"), 'X', "lotr:dried_reeds");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.NEAR_HARAD, "faction/near_harad/combat/nomad_shoes", "lotr:nomad_shoes", 1, LOTRTranscribedRecipes.rows("X X", "X X"), 'X', "lotr:dried_reeds");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.NEAR_HARAD, "faction/near_harad/combat/nomad_tunic", "lotr:nomad_tunic", 1, LOTRTranscribedRecipes.rows("X X", "XXX", "XXX"), 'X', "lotr:dried_reeds");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.NEAR_HARAD, "faction/near_harad/combat/poisoned_arrow", "lotr:poisoned_arrow", 4, LOTRTranscribedRecipes.rows(" X ", "XYX", " X "), 'X', "minecraft:arrow", 'Y', "lotr:bottle_of_poison");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.NEAR_HARAD, "faction/near_harad/combat/poisoned_crossbow_bolt", "lotr:poisoned_crossbow_bolt", 4, LOTRTranscribedRecipes.rows(" X ", "XYX", " X "), 'X', "lotr:crossbow_bolt", 'Y', "lotr:bottle_of_poison");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.NEAR_HARAD, "faction/near_harad/combat/southron_champion_helmet", "lotr:southron_champion_helmet", 1, LOTRTranscribedRecipes.rows("XYX", " Z "), 'X', "#lotr:sticks", 'Y', "minecraft:leather", 'Z', "lotr:coast_southron_helmet");
        // faction/near_harad/misc
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.NEAR_HARAD, "faction/near_harad/misc/near_harad_gate", "lotr:near_harad_gate", 4, LOTRTranscribedRecipes.rows("ZYZ", "YXY", "ZYZ"), 'X', "lotr:gate_gears", 'Y', "#minecraft:planks", 'Z', "lotr:bronze_ingot");
        // faction/ranger/combat
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.RANGER, "faction/ranger/combat/ranger_bow", "lotr:ranger_bow", 1, LOTRTranscribedRecipes.rows(" XY", "X Y", " XY"), 'X', "#lotr:sticks", 'Y', "minecraft:string");
        // faction/rhun/combat
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.RHUN, "faction/rhun/combat/golden_rhunic_boots", "lotr:golden_rhunic_boots", 1, LOTRTranscribedRecipes.rows("X X", "X X"), 'X', "lotr:gilded_iron_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.RHUN, "faction/rhun/combat/golden_rhunic_chestplate", "lotr:golden_rhunic_chestplate", 1, LOTRTranscribedRecipes.rows("X X", "XXX", "XXX"), 'X', "lotr:gilded_iron_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.RHUN, "faction/rhun/combat/golden_rhunic_helmet", "lotr:golden_rhunic_helmet", 1, LOTRTranscribedRecipes.rows("XXX", "X X"), 'X', "lotr:gilded_iron_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.RHUN, "faction/rhun/combat/golden_rhunic_leggings", "lotr:golden_rhunic_leggings", 1, LOTRTranscribedRecipes.rows("XXX", "X X", "X X"), 'X', "lotr:gilded_iron_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.RHUN, "faction/rhun/combat/khamuls_fire_jar", "lotr:khamuls_fire_jar", 1, LOTRTranscribedRecipes.rows("XYX", "YZY", "XYX"), 'X', "lotr:gilded_iron_ingot", 'Y', "minecraft:gunpowder", 'Z', "lotr:durnor");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.RHUN, "faction/rhun/combat/rhunic_bardiche", "lotr:rhunic_bardiche", 1, LOTRTranscribedRecipes.rows(" XX", " YX", "Y  "), 'X', "minecraft:iron_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.RHUN, "faction/rhun/combat/rhunic_battleaxe", "lotr:rhunic_battleaxe", 1, LOTRTranscribedRecipes.rows("XXX", "XYX", " Y "), 'X', "minecraft:iron_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.RHUN, "faction/rhun/combat/rhunic_boots", "lotr:rhunic_boots", 1, LOTRTranscribedRecipes.rows("Y Y", "X X"), 'X', "minecraft:iron_ingot", 'Y', "minecraft:leather");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.RHUN, "faction/rhun/combat/rhunic_bow", "lotr:rhunic_bow", 1, LOTRTranscribedRecipes.rows(" XY", "X Y", " XY"), 'X', "#lotr:sticks", 'Y', "minecraft:string");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.RHUN, "faction/rhun/combat/rhunic_chestplate", "lotr:rhunic_chestplate", 1, LOTRTranscribedRecipes.rows("X X", "YYY", "XXX"), 'X', "minecraft:iron_ingot", 'Y', "minecraft:leather");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.RHUN, "faction/rhun/combat/rhunic_dagger", "lotr:rhunic_dagger", 1, LOTRTranscribedRecipes.rows("X", "Y"), 'X', "minecraft:iron_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.RHUN, "faction/rhun/combat/rhunic_fire_pot", "lotr:rhunic_fire_pot", 4, LOTRTranscribedRecipes.rows("Z", "Y", "X"), 'X', "lotr:gilded_iron_ingot", 'Y', "minecraft:gunpowder", 'Z', "lotr:durnor");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.RHUN, "faction/rhun/combat/rhunic_helmet", "lotr:rhunic_helmet", 1, LOTRTranscribedRecipes.rows("XXX", "Y Y"), 'X', "minecraft:iron_ingot", 'Y', "minecraft:leather");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.RHUN, "faction/rhun/combat/rhunic_horse_armor", "lotr:rhunic_horse_armor", 1, LOTRTranscribedRecipes.rows("X  ", "XYX", "XXX"), 'X', "lotr:gilded_iron_ingot", 'Y', "minecraft:leather");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.RHUN, "faction/rhun/combat/rhunic_leggings", "lotr:rhunic_leggings", 1, LOTRTranscribedRecipes.rows("XXX", "Y Y", "X X"), 'X', "minecraft:iron_ingot", 'Y', "minecraft:leather");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.RHUN, "faction/rhun/combat/rhunic_pike", "lotr:rhunic_pike", 1, LOTRTranscribedRecipes.rows("  X", " YX", "Y  "), 'X', "minecraft:iron_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.RHUN, "faction/rhun/combat/rhunic_spear", "lotr:rhunic_spear", 1, LOTRTranscribedRecipes.rows("  X", " Y ", "Y  "), 'X', "minecraft:iron_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.RHUN, "faction/rhun/combat/rhunic_sword", "lotr:rhunic_sword", 1, LOTRTranscribedRecipes.rows("X", "X", "Y"), 'X', "minecraft:iron_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.RHUN, "faction/rhun/combat/rhunic_warlord_helmet", "lotr:rhunic_warlord_helmet", 1, LOTRTranscribedRecipes.rows("XYX"), 'X', "lotr:kine_of_araw_horn", 'Y', "lotr:golden_rhunic_helmet");
        // faction/rhun/misc
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.RHUN, "faction/rhun/misc/rhun_gate", "lotr:rhun_gate", 4, LOTRTranscribedRecipes.rows("ZYZ", "YXY", "ZYZ"), 'X', "lotr:gate_gears", 'Y', "#minecraft:planks", 'Z', "lotr:gilded_iron_ingot");
        // faction/rivendell/block
        r.in(RecipeCategory.BUILDING_BLOCKS).shaped(LOTRCraftingTable.RIVENDELL, "faction/rivendell/block/high_elven_silver_brick", "lotr:high_elven_silver_brick", 1, LOTRTranscribedRecipes.rows(" X ", "XYX", " X "), 'X', "lotr:silver_nugget", 'Y', "lotr:high_elven_brick");
        // faction/rivendell/combat
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.RIVENDELL, "faction/rivendell/combat/galvorn_boots", "lotr:galvorn_boots", 1, LOTRTranscribedRecipes.rows("X X", "X X"), 'X', "lotr:galvorn_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.RIVENDELL, "faction/rivendell/combat/galvorn_chestplate", "lotr:galvorn_chestplate", 1, LOTRTranscribedRecipes.rows("X X", "XXX", "XXX"), 'X', "lotr:galvorn_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.RIVENDELL, "faction/rivendell/combat/galvorn_helmet", "lotr:galvorn_helmet", 1, LOTRTranscribedRecipes.rows("XXX", "X X"), 'X', "lotr:galvorn_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.RIVENDELL, "faction/rivendell/combat/galvorn_leggings", "lotr:galvorn_leggings", 1, LOTRTranscribedRecipes.rows("XXX", "X X", "X X"), 'X', "lotr:galvorn_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.RIVENDELL, "faction/rivendell/combat/rivendell_battlestaff", "lotr:rivendell_battlestaff", 1, LOTRTranscribedRecipes.rows("  X", " Y ", "X  "), 'X', "lotr:elven_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.RIVENDELL, "faction/rivendell/combat/rivendell_boots", "lotr:rivendell_boots", 1, LOTRTranscribedRecipes.rows("X X", "X X"), 'X', "lotr:elven_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.RIVENDELL, "faction/rivendell/combat/rivendell_bow", "lotr:rivendell_bow", 1, LOTRTranscribedRecipes.rows(" XY", "X Y", " XY"), 'X', "lotr:elven_steel_ingot", 'Y', "minecraft:string");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.RIVENDELL, "faction/rivendell/combat/rivendell_chestplate", "lotr:rivendell_chestplate", 1, LOTRTranscribedRecipes.rows("X X", "XXX", "XXX"), 'X', "lotr:elven_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.RIVENDELL, "faction/rivendell/combat/rivendell_dagger", "lotr:rivendell_dagger", 1, LOTRTranscribedRecipes.rows("X", "Y"), 'X', "lotr:elven_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.RIVENDELL, "faction/rivendell/combat/rivendell_helmet", "lotr:rivendell_helmet", 1, LOTRTranscribedRecipes.rows("XXX", "X X"), 'X', "lotr:elven_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.RIVENDELL, "faction/rivendell/combat/rivendell_horse_armor", "lotr:rivendell_horse_armor", 1, LOTRTranscribedRecipes.rows("X  ", "XYX", "XXX"), 'X', "lotr:elven_steel_ingot", 'Y', "minecraft:leather");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.RIVENDELL, "faction/rivendell/combat/rivendell_leggings", "lotr:rivendell_leggings", 1, LOTRTranscribedRecipes.rows("XXX", "X X", "X X"), 'X', "lotr:elven_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.RIVENDELL, "faction/rivendell/combat/rivendell_longspear", "lotr:rivendell_longspear", 1, LOTRTranscribedRecipes.rows("  X", " YX", "Y  "), 'X', "lotr:elven_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.RIVENDELL, "faction/rivendell/combat/rivendell_spear", "lotr:rivendell_spear", 1, LOTRTranscribedRecipes.rows("  X", " Y ", "Y  "), 'X', "lotr:elven_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.RIVENDELL, "faction/rivendell/combat/rivendell_sword", "lotr:rivendell_sword", 1, LOTRTranscribedRecipes.rows("X", "X", "Y"), 'X', "lotr:elven_steel_ingot", 'Y', "#lotr:sticks");
        // faction/rivendell/decoration
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.RIVENDELL, "faction/rivendell/decoration/high_elf_bars", "lotr:high_elf_bars", 16, LOTRTranscribedRecipes.rows("XXX", "XXX"), 'X', "lotr:elven_steel_ingot");
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.RIVENDELL, "faction/rivendell/decoration/high_elven_chandelier", "lotr:high_elven_chandelier", 2, LOTRTranscribedRecipes.rows(" X ", "YZY"), 'X', "#lotr:sticks", 'Y', "lotr:high_elven_torch", 'Z', "lotr:elven_steel_ingot");
        // faction/rivendell/misc
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.RIVENDELL, "faction/rivendell/misc/high_elven_gate", "lotr:high_elven_gate", 4, LOTRTranscribedRecipes.rows("ZYZ", "YXY", "ZYZ"), 'X', "lotr:gate_gears", 'Y', "#minecraft:planks", 'Z', "lotr:elven_steel_ingot");
        // faction/rivendell/tool
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.RIVENDELL, "faction/rivendell/tool/rivendell_axe", "lotr:rivendell_axe", 1, LOTRTranscribedRecipes.rows("XX", "XY", " Y"), 'X', "lotr:elven_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.RIVENDELL, "faction/rivendell/tool/rivendell_hoe", "lotr:rivendell_hoe", 1, LOTRTranscribedRecipes.rows("XX", " Y", " Y"), 'X', "lotr:elven_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.RIVENDELL, "faction/rivendell/tool/rivendell_pickaxe", "lotr:rivendell_pickaxe", 1, LOTRTranscribedRecipes.rows("XXX", " Y ", " Y "), 'X', "lotr:elven_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.RIVENDELL, "faction/rivendell/tool/rivendell_shovel", "lotr:rivendell_shovel", 1, LOTRTranscribedRecipes.rows("X", "Y", "Y"), 'X', "lotr:elven_steel_ingot", 'Y', "#lotr:sticks");
        // faction/rohirric/combat
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.ROHIRRIC, "faction/rohirric/combat/rohirric_lance", "lotr:rohirric_lance", 1, LOTRTranscribedRecipes.rows("  X", " X ", "Y  "), 'X', "minecraft:iron_ingot", 'Y', "#lotr:sticks");
        // faction/rohirric/misc
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.ROHIRRIC, "faction/rohirric/misc/rohan_gate", "lotr:rohan_gate", 4, LOTRTranscribedRecipes.rows("ZYZ", "YXY", "ZYZ"), 'X', "lotr:gate_gears", 'Y', "#minecraft:planks", 'Z', "minecraft:iron_ingot");
        // faction/tauredain/block
        r.in(RecipeCategory.BUILDING_BLOCKS).shaped(LOTRCraftingTable.TAUREDAIN, "faction/tauredain/block/taur_obsidian_pillar", "lotr:taur_obsidian_pillar", 3, LOTRTranscribedRecipes.rows("X", "X", "X"), 'X', "lotr:obsidian_shard");
        r.in(RecipeCategory.BUILDING_BLOCKS).shaped(LOTRCraftingTable.TAUREDAIN, "faction/tauredain/block/tauredain_obsidian_brick", "lotr:tauredain_obsidian_brick", 4, LOTRTranscribedRecipes.rows("XX", "XX"), 'X', "lotr:obsidian_shard");
        // faction/tauredain/combat
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.TAUREDAIN, "faction/tauredain/combat/poisoned_taurethrim_dart", "lotr:poisoned_taurethrim_dart", 4, LOTRTranscribedRecipes.rows(" X ", "XYX", " X "), 'X', "lotr:taurethrim_dart", 'Y', "lotr:bottle_of_poison");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.TAUREDAIN, "faction/tauredain/combat/taurethrim_battleaxe", "lotr:taurethrim_battleaxe", 1, LOTRTranscribedRecipes.rows("XXX", "XYX", " Y "), 'X', "lotr:obsidian_shard", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.TAUREDAIN, "faction/tauredain/combat/taurethrim_bludgeon", "lotr:taurethrim_bludgeon", 1, LOTRTranscribedRecipes.rows("XYX", "XYX", " Y "), 'X', "lotr:obsidian_shard", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.TAUREDAIN, "faction/tauredain/combat/taurethrim_boots", "lotr:taurethrim_boots", 1, LOTRTranscribedRecipes.rows("X X", "X X"), 'X', "lotr:bronze_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.TAUREDAIN, "faction/tauredain/combat/taurethrim_chestplate", "lotr:taurethrim_chestplate", 1, LOTRTranscribedRecipes.rows("X X", "XXX", "XXX"), 'X', "lotr:bronze_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.TAUREDAIN, "faction/tauredain/combat/taurethrim_chieftain_helmet", "lotr:taurethrim_chieftain_helmet", 1, LOTRTranscribedRecipes.rows("X", "Y"), 'X', "lotr:flame_of_harad", 'Y', "lotr:taurethrim_helmet");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.TAUREDAIN, "faction/tauredain/combat/taurethrim_dagger", "lotr:taurethrim_dagger", 1, LOTRTranscribedRecipes.rows("X", "Y"), 'X', "lotr:obsidian_shard", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.TAUREDAIN, "faction/tauredain/combat/taurethrim_dart", "lotr:taurethrim_dart", 4, LOTRTranscribedRecipes.rows("X", "Y", "Z"), 'X', "lotr:obsidian_shard", 'Y', "#lotr:sticks", 'Z', "#lotr:feathers");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.TAUREDAIN, "faction/tauredain/combat/taurethrim_helmet", "lotr:taurethrim_helmet", 1, LOTRTranscribedRecipes.rows("XXX", "X X"), 'X', "lotr:bronze_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.TAUREDAIN, "faction/tauredain/combat/taurethrim_leggings", "lotr:taurethrim_leggings", 1, LOTRTranscribedRecipes.rows("XXX", "X X", "X X"), 'X', "lotr:bronze_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.TAUREDAIN, "faction/tauredain/combat/taurethrim_pike", "lotr:taurethrim_pike", 1, LOTRTranscribedRecipes.rows("  X", " YX", "Y  "), 'X', "lotr:obsidian_shard", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.TAUREDAIN, "faction/tauredain/combat/taurethrim_spear", "lotr:taurethrim_spear", 1, LOTRTranscribedRecipes.rows("  X", " Y ", "Y  "), 'X', "lotr:obsidian_shard", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.TAUREDAIN, "faction/tauredain/combat/taurethrim_sword", "lotr:taurethrim_sword", 1, LOTRTranscribedRecipes.rows("XZX", "XZX", " Y "), 'X', "lotr:obsidian_shard", 'Y', "#lotr:sticks", 'Z', "#minecraft:planks");
        // faction/tauredain/misc
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.TAUREDAIN, "faction/tauredain/misc/tauredain_gate", "lotr:tauredain_gate", 4, LOTRTranscribedRecipes.rows("ZYZ", "YXY", "ZYZ"), 'X', "lotr:gate_gears", 'Y', "#minecraft:planks", 'Z', "minecraft:gold_ingot");
        // faction/tauredain/tool
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.TAUREDAIN, "faction/tauredain/tool/taurethrim_axe", "lotr:taurethrim_axe", 1, LOTRTranscribedRecipes.rows("XX", "XY", " Y"), 'X', "lotr:obsidian_shard", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.TAUREDAIN, "faction/tauredain/tool/taurethrim_hoe", "lotr:taurethrim_hoe", 1, LOTRTranscribedRecipes.rows("XX", " Y", " Y"), 'X', "lotr:obsidian_shard", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.TAUREDAIN, "faction/tauredain/tool/taurethrim_pickaxe", "lotr:taurethrim_pickaxe", 1, LOTRTranscribedRecipes.rows("XXX", " Y ", " Y "), 'X', "lotr:obsidian_shard", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.TAUREDAIN, "faction/tauredain/tool/taurethrim_shovel", "lotr:taurethrim_shovel", 1, LOTRTranscribedRecipes.rows("X", "Y", "Y"), 'X', "lotr:obsidian_shard", 'Y', "#lotr:sticks");
        // faction/umbar/block
        r.in(RecipeCategory.BUILDING_BLOCKS).shaped(LOTRCraftingTable.UMBAR, "faction/umbar/block/near_harad_red_brick", "lotr:near_harad_red_brick", 4, LOTRTranscribedRecipes.rows("XX", "XX"), 'X', "minecraft:red_sandstone");
        r.in(RecipeCategory.BUILDING_BLOCKS).shaped(LOTRCraftingTable.UMBAR, "faction/umbar/block/near_harad_red_pillar", "lotr:near_harad_red_pillar", 3, LOTRTranscribedRecipes.rows("X", "X", "X"), 'X', "minecraft:red_sandstone");
        // faction/umbar/combat
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.UMBAR, "faction/umbar/combat/corsair_battleaxe", "lotr:corsair_battleaxe", 1, LOTRTranscribedRecipes.rows("XXX", "XYX", " YA"), 'A', "minecraft:string", 'X', "minecraft:iron_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.UMBAR, "faction/umbar/combat/corsair_boots", "lotr:corsair_boots", 1, LOTRTranscribedRecipes.rows("Y Y", "X X"), 'X', "lotr:bronze_ingot", 'Y', "minecraft:leather");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.UMBAR, "faction/umbar/combat/corsair_chestplate", "lotr:corsair_chestplate", 1, LOTRTranscribedRecipes.rows("X X", "YYY", "XXX"), 'X', "lotr:bronze_ingot", 'Y', "minecraft:leather");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.UMBAR, "faction/umbar/combat/corsair_dagger", "lotr:corsair_dagger", 1, LOTRTranscribedRecipes.rows("X ", "YA"), 'A', "minecraft:string", 'X', "minecraft:iron_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.UMBAR, "faction/umbar/combat/corsair_eket", "lotr:corsair_eket", 1, LOTRTranscribedRecipes.rows("X ", "X ", "YA"), 'A', "minecraft:string", 'X', "minecraft:iron_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.UMBAR, "faction/umbar/combat/corsair_harpoon", "lotr:corsair_harpoon", 1, LOTRTranscribedRecipes.rows("  X", " Y ", "YA "), 'A', "minecraft:string", 'X', "minecraft:iron_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.UMBAR, "faction/umbar/combat/corsair_helmet", "lotr:corsair_helmet", 1, LOTRTranscribedRecipes.rows("XXX", "Y Y"), 'X', "lotr:bronze_ingot", 'Y', "minecraft:leather");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.UMBAR, "faction/umbar/combat/corsair_leggings", "lotr:corsair_leggings", 1, LOTRTranscribedRecipes.rows("XXX", "Y Y", "X X"), 'X', "lotr:bronze_ingot", 'Y', "minecraft:leather");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.UMBAR, "faction/umbar/combat/haradric_dagger", "lotr:haradric_dagger", 1, LOTRTranscribedRecipes.rows("X", "Y"), 'X', "lotr:bronze_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.UMBAR, "faction/umbar/combat/haradric_pike", "lotr:haradric_pike", 1, LOTRTranscribedRecipes.rows("  X", " YX", "Y  "), 'X', "lotr:bronze_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.UMBAR, "faction/umbar/combat/haradric_spear", "lotr:haradric_spear", 1, LOTRTranscribedRecipes.rows("  X", " Y ", "Y  "), 'X', "lotr:bronze_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.UMBAR, "faction/umbar/combat/haradric_sword", "lotr:haradric_sword", 1, LOTRTranscribedRecipes.rows("X", "X", "Y"), 'X', "lotr:bronze_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.UMBAR, "faction/umbar/combat/nomad_cap", "lotr:nomad_cap", 1, LOTRTranscribedRecipes.rows("XXX", "X X"), 'X', "lotr:dried_reeds");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.UMBAR, "faction/umbar/combat/nomad_leggings", "lotr:nomad_leggings", 1, LOTRTranscribedRecipes.rows("XXX", "X X", "X X"), 'X', "lotr:dried_reeds");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.UMBAR, "faction/umbar/combat/nomad_shoes", "lotr:nomad_shoes", 1, LOTRTranscribedRecipes.rows("X X", "X X"), 'X', "lotr:dried_reeds");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.UMBAR, "faction/umbar/combat/nomad_tunic", "lotr:nomad_tunic", 1, LOTRTranscribedRecipes.rows("X X", "XXX", "XXX"), 'X', "lotr:dried_reeds");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.UMBAR, "faction/umbar/combat/poisoned_arrow", "lotr:poisoned_arrow", 4, LOTRTranscribedRecipes.rows(" X ", "XYX", " X "), 'X', "minecraft:arrow", 'Y', "lotr:bottle_of_poison");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.UMBAR, "faction/umbar/combat/poisoned_crossbow_bolt", "lotr:poisoned_crossbow_bolt", 4, LOTRTranscribedRecipes.rows(" X ", "XYX", " X "), 'X', "lotr:crossbow_bolt", 'Y', "lotr:bottle_of_poison");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.UMBAR, "faction/umbar/combat/umbaric_boots", "lotr:umbaric_boots", 1, LOTRTranscribedRecipes.rows("X X", "X X"), 'X', "minecraft:iron_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.UMBAR, "faction/umbar/combat/umbaric_chestplate", "lotr:umbaric_chestplate", 1, LOTRTranscribedRecipes.rows("X X", "XXX", "XXX"), 'X', "minecraft:iron_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.UMBAR, "faction/umbar/combat/umbaric_helmet", "lotr:umbaric_helmet", 1, LOTRTranscribedRecipes.rows("XXX", "X X"), 'X', "minecraft:iron_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.UMBAR, "faction/umbar/combat/umbaric_leggings", "lotr:umbaric_leggings", 1, LOTRTranscribedRecipes.rows("XXX", "X X", "X X"), 'X', "minecraft:iron_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.UMBAR, "faction/umbar/combat/umbaric_pike", "lotr:umbaric_pike", 1, LOTRTranscribedRecipes.rows("  X", " YX", "Y  "), 'X', "minecraft:iron_ingot", 'Y', "#lotr:sticks");
        // faction/umbar/misc
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.UMBAR, "faction/umbar/misc/near_harad_gate", "lotr:near_harad_gate", 4, LOTRTranscribedRecipes.rows("ZYZ", "YXY", "ZYZ"), 'X', "lotr:gate_gears", 'Y', "#minecraft:planks", 'Z', "lotr:bronze_ingot");
        // faction/uruk/block
        r.in(RecipeCategory.BUILDING_BLOCKS).shaped(LOTRCraftingTable.URUK, "faction/uruk/block/orc_plating_iron", "lotr:orc_plating_iron", 8, LOTRTranscribedRecipes.rows("XX", "XX"), 'X', "lotr:orc_steel_ingot");
        // faction/uruk/combat
        r.in(RecipeCategory.COMBAT).shapeless(LOTRCraftingTable.URUK, "faction/uruk/combat/double_strength_orc_bomb", "lotr:double_strength_orc_bomb", 1, "lotr:orc_bomb", "minecraft:gunpowder", "lotr:uruk_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.URUK, "faction/uruk/combat/isengard_warg_armor", "lotr:isengard_warg_armor", 1, LOTRTranscribedRecipes.rows("X  ", "XYX", "XXX"), 'X', "lotr:uruk_steel_ingot", 'Y', "minecraft:leather");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.URUK, "faction/uruk/combat/orc_bomb", "lotr:orc_bomb", 4, LOTRTranscribedRecipes.rows("XYX", "YXY", "XYX"), 'X', "minecraft:gunpowder", 'Y', "lotr:uruk_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.URUK, "faction/uruk/combat/poisoned_arrow", "lotr:poisoned_arrow", 4, LOTRTranscribedRecipes.rows(" X ", "XYX", " X "), 'X', "minecraft:arrow", 'Y', "lotr:bottle_of_poison");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.URUK, "faction/uruk/combat/poisoned_crossbow_bolt", "lotr:poisoned_crossbow_bolt", 4, LOTRTranscribedRecipes.rows(" X ", "XYX", " X "), 'X', "lotr:crossbow_bolt", 'Y', "lotr:bottle_of_poison");
        r.in(RecipeCategory.COMBAT).shapeless(LOTRCraftingTable.URUK, "faction/uruk/combat/triple_strength_orc_bomb", "lotr:triple_strength_orc_bomb", 1, "lotr:double_strength_orc_bomb", "minecraft:gunpowder", "lotr:uruk_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.URUK, "faction/uruk/combat/uruk_battleaxe", "lotr:uruk_battleaxe", 1, LOTRTranscribedRecipes.rows("XXX", "XYX", " Y "), 'X', "lotr:uruk_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.URUK, "faction/uruk/combat/uruk_berserker_cleaver", "lotr:uruk_berserker_cleaver", 1, LOTRTranscribedRecipes.rows("XXX", " X ", " Y "), 'X', "lotr:uruk_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.URUK, "faction/uruk/combat/uruk_berserker_helmet", "lotr:uruk_berserker_helmet", 1, LOTRTranscribedRecipes.rows("XYX", " Z "), 'X', "lotr:uruk_steel_ingot", 'Y', "#c:dyes/white", 'Z', "lotr:uruk_helmet");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.URUK, "faction/uruk/combat/uruk_boots", "lotr:uruk_boots", 1, LOTRTranscribedRecipes.rows("X X", "X X"), 'X', "lotr:uruk_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.URUK, "faction/uruk/combat/uruk_chestplate", "lotr:uruk_chestplate", 1, LOTRTranscribedRecipes.rows("X X", "XXX", "XXX"), 'X', "lotr:uruk_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.URUK, "faction/uruk/combat/uruk_cleaver", "lotr:uruk_cleaver", 1, LOTRTranscribedRecipes.rows("X", "X", "Y"), 'X', "lotr:uruk_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.URUK, "faction/uruk/combat/uruk_crossbow", "lotr:uruk_crossbow", 1, LOTRTranscribedRecipes.rows("XXY", "ZYX", "YZX"), 'X', "lotr:uruk_steel_ingot", 'Y', "#lotr:sticks", 'Z', "minecraft:string");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.URUK, "faction/uruk/combat/uruk_dagger", "lotr:uruk_dagger", 1, LOTRTranscribedRecipes.rows("X", "Y"), 'X', "lotr:uruk_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.URUK, "faction/uruk/combat/uruk_helmet", "lotr:uruk_helmet", 1, LOTRTranscribedRecipes.rows("XXX", "X X"), 'X', "lotr:uruk_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.URUK, "faction/uruk/combat/uruk_leggings", "lotr:uruk_leggings", 1, LOTRTranscribedRecipes.rows("XXX", "X X", "X X"), 'X', "lotr:uruk_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.URUK, "faction/uruk/combat/uruk_pike", "lotr:uruk_pike", 1, LOTRTranscribedRecipes.rows("  X", " YX", "Y  "), 'X', "lotr:uruk_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.URUK, "faction/uruk/combat/uruk_spear", "lotr:uruk_spear", 1, LOTRTranscribedRecipes.rows("  X", " Y ", "Y  "), 'X', "lotr:uruk_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.URUK, "faction/uruk/combat/uruk_warhammer", "lotr:uruk_warhammer", 1, LOTRTranscribedRecipes.rows("XYX", "XYX", " Y "), 'X', "lotr:uruk_steel_ingot", 'Y', "#lotr:sticks");
        // faction/uruk/decoration
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.URUK, "faction/uruk/decoration/uruk_bars", "lotr:uruk_bars", 16, LOTRTranscribedRecipes.rows("XXX", "XXX"), 'X', "lotr:uruk_steel_ingot");
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.URUK, "faction/uruk/decoration/uruk_chandelier", "lotr:uruk_chandelier", 2, LOTRTranscribedRecipes.rows(" X ", "YZY"), 'X', "#lotr:sticks", 'Y', "lotr:orc_torch", 'Z', "lotr:uruk_steel_ingot");
        // faction/uruk/food
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.URUK, "faction/uruk/food/maggoty_bread_uruk", "lotr:maggoty_bread", 1, LOTRTranscribedRecipes.rows("XXX"), 'X', "minecraft:wheat");
        // faction/uruk/misc
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.URUK, "faction/uruk/misc/mechanism", "lotr:mechanism", 1, LOTRTranscribedRecipes.rows(" X ", "YZY", " X "), 'X', "minecraft:copper_ingot", 'Y', "minecraft:flint", 'Z', "lotr:uruk_steel_ingot");
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.URUK, "faction/uruk/misc/uruk_gate", "lotr:uruk_gate", 4, LOTRTranscribedRecipes.rows("YYY", "YXY", "YYY"), 'X', "lotr:gate_gears", 'Y', "lotr:uruk_steel_ingot");
        // faction/uruk/tool
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.URUK, "faction/uruk/tool/uruk_axe", "lotr:uruk_axe", 1, LOTRTranscribedRecipes.rows("XX", "XY", " Y"), 'X', "lotr:uruk_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.URUK, "faction/uruk/tool/uruk_hoe", "lotr:uruk_hoe", 1, LOTRTranscribedRecipes.rows("XX", " Y", " Y"), 'X', "lotr:uruk_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.URUK, "faction/uruk/tool/uruk_pickaxe", "lotr:uruk_pickaxe", 1, LOTRTranscribedRecipes.rows("XXX", " Y ", " Y "), 'X', "lotr:uruk_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.URUK, "faction/uruk/tool/uruk_shovel", "lotr:uruk_shovel", 1, LOTRTranscribedRecipes.rows("X", "Y", "Y"), 'X', "lotr:uruk_steel_ingot", 'Y', "#lotr:sticks");
        // faction/uruk/utility
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.URUK, "faction/uruk/utility/orc_chain", "lotr:orc_chain", 8, LOTRTranscribedRecipes.rows("X", "X", "X"), 'X', "lotr:orc_steel_ingot");
        // faction/wood_elven/block
        r.in(RecipeCategory.BUILDING_BLOCKS).shaped(LOTRCraftingTable.WOOD_ELVEN, "faction/wood_elven/block/wood_elven_silver_brick", "lotr:wood_elven_silver_brick", 1, LOTRTranscribedRecipes.rows(" X ", "XYX", " X "), 'X', "lotr:silver_nugget", 'Y', "lotr:wood_elven_brick");
        // faction/wood_elven/combat
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.WOOD_ELVEN, "faction/wood_elven/combat/galvorn_boots", "lotr:galvorn_boots", 1, LOTRTranscribedRecipes.rows("X X", "X X"), 'X', "lotr:galvorn_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.WOOD_ELVEN, "faction/wood_elven/combat/galvorn_chestplate", "lotr:galvorn_chestplate", 1, LOTRTranscribedRecipes.rows("X X", "XXX", "XXX"), 'X', "lotr:galvorn_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.WOOD_ELVEN, "faction/wood_elven/combat/galvorn_helmet", "lotr:galvorn_helmet", 1, LOTRTranscribedRecipes.rows("XXX", "X X"), 'X', "lotr:galvorn_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.WOOD_ELVEN, "faction/wood_elven/combat/galvorn_leggings", "lotr:galvorn_leggings", 1, LOTRTranscribedRecipes.rows("XXX", "X X", "X X"), 'X', "lotr:galvorn_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.WOOD_ELVEN, "faction/wood_elven/combat/wood_elven_battlestaff", "lotr:wood_elven_battlestaff", 1, LOTRTranscribedRecipes.rows("  X", " Y ", "X  "), 'X', "lotr:elven_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.WOOD_ELVEN, "faction/wood_elven/combat/wood_elven_boots", "lotr:wood_elven_boots", 1, LOTRTranscribedRecipes.rows("X X", "X X"), 'X', "lotr:elven_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.WOOD_ELVEN, "faction/wood_elven/combat/wood_elven_chestplate", "lotr:wood_elven_chestplate", 1, LOTRTranscribedRecipes.rows("X X", "XXX", "XXX"), 'X', "lotr:elven_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.WOOD_ELVEN, "faction/wood_elven/combat/wood_elven_dagger", "lotr:wood_elven_dagger", 1, LOTRTranscribedRecipes.rows("X", "Y"), 'X', "lotr:elven_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.WOOD_ELVEN, "faction/wood_elven/combat/wood_elven_elk_armor", "lotr:wood_elven_elk_armor", 1, LOTRTranscribedRecipes.rows("X  ", "XYX", "XXX"), 'X', "lotr:elven_steel_ingot", 'Y', "minecraft:leather");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.WOOD_ELVEN, "faction/wood_elven/combat/wood_elven_helmet", "lotr:wood_elven_helmet", 1, LOTRTranscribedRecipes.rows("XXX", "X X"), 'X', "lotr:elven_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.WOOD_ELVEN, "faction/wood_elven/combat/wood_elven_leggings", "lotr:wood_elven_leggings", 1, LOTRTranscribedRecipes.rows("XXX", "X X", "X X"), 'X', "lotr:elven_steel_ingot");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.WOOD_ELVEN, "faction/wood_elven/combat/wood_elven_longspear", "lotr:wood_elven_longspear", 1, LOTRTranscribedRecipes.rows("  X", " YX", "Y  "), 'X', "lotr:elven_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.WOOD_ELVEN, "faction/wood_elven/combat/wood_elven_spear", "lotr:wood_elven_spear", 1, LOTRTranscribedRecipes.rows("  X", " Y ", "Y  "), 'X', "lotr:elven_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.WOOD_ELVEN, "faction/wood_elven/combat/wood_elven_sword", "lotr:wood_elven_sword", 1, LOTRTranscribedRecipes.rows("X", "X", "Y"), 'X', "lotr:elven_steel_ingot", 'Y', "#lotr:sticks");
        // faction/wood_elven/decoration
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.WOOD_ELVEN, "faction/wood_elven/decoration/wood_elf_bars", "lotr:wood_elf_bars", 16, LOTRTranscribedRecipes.rows("XXX", "XXX"), 'X', "lotr:elven_steel_ingot");
        // faction/wood_elven/misc
        r.in(RecipeCategory.MISC).shaped(LOTRCraftingTable.WOOD_ELVEN, "faction/wood_elven/misc/wood_elven_gate", "lotr:wood_elven_gate", 4, LOTRTranscribedRecipes.rows("ZYZ", "YXY", "ZYZ"), 'X', "lotr:gate_gears", 'Y', "#minecraft:planks", 'Z', "lotr:elven_steel_ingot");
        // faction/wood_elven/tool
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.WOOD_ELVEN, "faction/wood_elven/tool/wood_elven_axe", "lotr:wood_elven_axe", 1, LOTRTranscribedRecipes.rows("XX", "XY", " Y"), 'X', "lotr:elven_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.WOOD_ELVEN, "faction/wood_elven/tool/wood_elven_hoe", "lotr:wood_elven_hoe", 1, LOTRTranscribedRecipes.rows("XX", " Y", " Y"), 'X', "lotr:elven_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.WOOD_ELVEN, "faction/wood_elven/tool/wood_elven_pickaxe", "lotr:wood_elven_pickaxe", 1, LOTRTranscribedRecipes.rows("XXX", " Y ", " Y "), 'X', "lotr:elven_steel_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(LOTRCraftingTable.WOOD_ELVEN, "faction/wood_elven/tool/wood_elven_shovel", "lotr:wood_elven_shovel", 1, LOTRTranscribedRecipes.rows("X", "Y", "Y"), 'X', "lotr:elven_steel_ingot", 'Y', "#lotr:sticks");
        // food
        r.in(RecipeCategory.MISC).shaped(null, "food/apple_crumble", "lotr:apple_crumble", 1, LOTRTranscribedRecipes.rows("AAA", "BCB", "DDD"), 'A', "minecraft:milk_bucket", 'B', "#lotr:apples", 'D', "minecraft:wheat", 'C', "minecraft:sugar");
        r.in(RecipeCategory.MISC).shaped(null, "food/banana_bread", "lotr:banana_bread", 1, LOTRTranscribedRecipes.rows("XYX"), 'X', "minecraft:wheat", 'Y', "lotr:banana");
        r.in(RecipeCategory.MISC).shaped(null, "food/banana_cake", "lotr:banana_cake", 1, LOTRTranscribedRecipes.rows("AAA", "BCB", "DDD"), 'A', "minecraft:milk_bucket", 'B', "lotr:banana", 'D', "minecraft:wheat", 'C', "minecraft:egg");
        r.in(RecipeCategory.MISC).shaped(null, "food/berry_pie", "lotr:berry_pie", 1, LOTRTranscribedRecipes.rows("AAA", "BBB", "CCC"), 'A', "minecraft:milk_bucket", 'B', "#lotr:berries", 'C', "minecraft:wheat");
        r.in(RecipeCategory.MISC).shaped(null, "food/cherry_pie", "lotr:cherry_pie", 1, LOTRTranscribedRecipes.rows("AAA", "BCB", "DDD"), 'A', "minecraft:milk_bucket", 'B', "lotr:cherries", 'D', "minecraft:wheat", 'C', "minecraft:sugar");
        r.in(RecipeCategory.MISC).shapeless(null, "food/chocolate_marchpane", "lotr:chocolate_marchpane", 1, "lotr:marchpane", "minecraft:cocoa_beans");
        r.in(RecipeCategory.MISC).shaped(null, "food/corn_bread", "lotr:corn_bread", 1, LOTRTranscribedRecipes.rows("XXX"), 'X', "lotr:corn");
        r.in(RecipeCategory.MISC).shapeless(null, "food/gammon", "lotr:gammon", 1, "minecraft:cooked_porkchop", "lotr:salt");
        r.in(RecipeCategory.MISC).shapeless(null, "food/leek_soup", "lotr:leek_soup", 1, "minecraft:bowl", "lotr:leek", "lotr:leek", "minecraft:potato");
        r.in(RecipeCategory.MISC).shaped(null, "food/lemon_cake", "lotr:lemon_cake", 1, LOTRTranscribedRecipes.rows("AAA", "BCB", "DDD"), 'A', "minecraft:milk_bucket", 'B', "lotr:lemon", 'D', "minecraft:wheat", 'C', "minecraft:sugar");
        r.in(RecipeCategory.MISC).shapeless(null, "food/maple_syrup", "lotr:maple_syrup", 1, "lotr:maple_log", "minecraft:bowl");
        r.in(RecipeCategory.MISC).shapeless(null, "food/marchpane", "lotr:marchpane", 1, "lotr:almond", "lotr:almond", "minecraft:sugar");
        r.in(RecipeCategory.MISC).shaped(null, "food/marchpane_block", "lotr:marchpane_block", 1, LOTRTranscribedRecipes.rows("XXX"), 'X', "lotr:marchpane");
        r.in(RecipeCategory.MISC).shapeless(null, "food/melon_soup", "lotr:melon_soup", 1, "minecraft:bowl", "minecraft:melon_slice", "minecraft:melon_slice", "minecraft:sugar");
        r.in(RecipeCategory.MISC).shapeless(null, "food/mushroom_pie", "lotr:mushroom_pie", 1, "minecraft:egg", "minecraft:red_mushroom", "minecraft:brown_mushroom");
        r.in(RecipeCategory.MISC).shaped(null, "food/olive_bread", "lotr:olive_bread", 1, LOTRTranscribedRecipes.rows("XYX"), 'X', "minecraft:wheat", 'Y', "lotr:olives");
        r.in(RecipeCategory.MISC).shapeless(null, "food/rabbit_stew", "lotr:rabbit_stew", 1, "minecraft:bowl", "minecraft:cooked_rabbit", "minecraft:potato", "minecraft:potato");
        r.in(RecipeCategory.MISC).shapeless(null, "food/suspicious_meat", "lotr:suspicious_meat", 1, "minecraft:rotten_flesh", "lotr:salt");
        // material
        r.in(RecipeCategory.MISC).shapeless(null, "material/blackroot_stick", "lotr:blackroot_stick", 2, "lotr:blackroot");
        r.in(RecipeCategory.MISC).shapeless(null, "material/bronze_ingot", "lotr:bronze_ingot", 1, "minecraft:copper_ingot", "lotr:tin_ingot");
        r.in(RecipeCategory.MISC).shapeless(null, "material/copper_ingot", "minecraft:copper_ingot", 4, "lotr:treasure_copper");
        // material/dye
        r.in(RecipeCategory.MISC).shapeless(null, "material/dye/bluebell_blue_from_bluebell", "lotr:bluebell_blue", 1, "lotr:bluebell");
        r.in(RecipeCategory.MISC).shapeless(null, "material/dye/bluebell_blue_from_rhun_flower_chrys_blue", "lotr:bluebell_blue", 1, "lotr:rhun_flower_chrys_blue");
        r.in(RecipeCategory.MISC).shapeless(null, "material/dye/brown_dye_from_fangorn_plant_brown", "lotr:brown_dye", 1, "lotr:fangorn_plant_brown");
        r.in(RecipeCategory.MISC).shapeless(null, "material/dye/brown_dye_from_orange_black", "lotr:brown_dye", 2, "#c:dyes/orange", "#c:dyes/black");
        r.in(RecipeCategory.MISC).shapeless(null, "material/dye/brown_dye_from_red_yellow_black", "lotr:brown_dye", 3, "#c:dyes/red", "#c:dyes/yellow", "#c:dyes/black");
        r.in(RecipeCategory.MISC).shapeless(null, "material/dye/charcoal_dust", "lotr:charcoal_dust", 1, "minecraft:charcoal");
        r.in(RecipeCategory.MISC).shapeless(null, "material/dye/clover_green_from_clover", "lotr:clover_green", 1, "lotr:clover");
        r.in(RecipeCategory.MISC).shapeless(null, "material/dye/magenta_dye_from_hibiscus", "minecraft:magenta_dye", 2, "lotr:hibiscus");
        r.in(RecipeCategory.MISC).shapeless(null, "material/dye/purple_dye_from_black_iris", "minecraft:purple_dye", 2, "lotr:black_iris");
        r.in(RecipeCategory.MISC).shapeless(null, "material/dye/red_dye_from_flame_of_harad", "minecraft:red_dye", 2, "lotr:flame_of_harad");
        r.in(RecipeCategory.MISC).shapeless(null, "material/dye/white_dye_from_asphodel", "lotr:white_dye", 1, "lotr:asphodel");
        r.in(RecipeCategory.MISC).shapeless(null, "material/dye/white_dye_from_niphredil", "lotr:white_dye", 1, "lotr:niphredil");
        r.in(RecipeCategory.MISC).shapeless(null, "material/dye/white_dye_from_rhun_flower_chrys_white", "lotr:white_dye", 1, "lotr:rhun_flower_chrys_white");
        r.in(RecipeCategory.MISC).shapeless(null, "material/dye/white_dye_from_simbelmyne", "lotr:white_dye", 1, "lotr:simbelmyne");
        r.in(RecipeCategory.MISC).shapeless(null, "material/dye/yellow_dye_from_elanor", "lotr:yellow_dye", 1, "lotr:elanor");
        r.in(RecipeCategory.MISC).shapeless(null, "material/dye/yellow_dye_from_fangorn_plant_gold", "lotr:yellow_dye", 1, "lotr:fangorn_plant_gold");
        r.in(RecipeCategory.MISC).shapeless(null, "material/dye/yellow_dye_from_fangorn_plant_yellow", "lotr:yellow_dye", 1, "lotr:fangorn_plant_yellow");
        r.in(RecipeCategory.MISC).shapeless(null, "material/dye/yellow_dye_from_rhun_flower_chrys_yellow", "lotr:yellow_dye", 1, "lotr:rhun_flower_chrys_yellow");
        r.in(RecipeCategory.MISC).shapeless(null, "material/dye/yellow_dye_from_yellow_harad_flower", "lotr:yellow_dye", 1, "lotr:yellow_harad_flower");
        r.in(RecipeCategory.MISC).shapeless(null, "material/dye/yellow_dye_from_yellow_iris", "lotr:yellow_dye", 2, "lotr:yellow_iris");
        // material
        r.in(RecipeCategory.MISC).shaped(null, "material/gate_gears", "lotr:gate_gears", 4, LOTRTranscribedRecipes.rows(" X ", "XYX", " X "), 'X', "minecraft:iron_ingot", 'Y', "#minecraft:planks");
        r.in(RecipeCategory.MISC).shapeless(null, "material/gunpowder_from_termite", "minecraft:gunpowder", 2, "lotr:exploding_termite");
        r.in(RecipeCategory.MISC).shaped(null, "material/mithril_from_mithril_mail", "lotr:mithril", 2, LOTRTranscribedRecipes.rows("XX", "XX"), 'X', "lotr:mithril_mail");
        r.in(RecipeCategory.MISC).shaped(null, "material/mithril_mail", "lotr:mithril_mail", 8, LOTRTranscribedRecipes.rows("XX", "XX"), 'X', "lotr:mithril");
        // material/nugget
        r.in(RecipeCategory.MISC).shaped(null, "material/nugget/mithril_from_nuggets", "lotr:mithril", 1, LOTRTranscribedRecipes.rows("XXX", "XXX", "XXX"), 'X', "lotr:mithril_nugget");
        r.in(RecipeCategory.MISC).shapeless(null, "material/nugget/mithril_nugget", "lotr:mithril_nugget", 9, "lotr:mithril");
        r.in(RecipeCategory.MISC).shaped(null, "material/nugget/silver_ingot_from_nuggets", "lotr:silver_ingot", 1, LOTRTranscribedRecipes.rows("XXX", "XXX", "XXX"), 'X', "lotr:silver_nugget");
        r.in(RecipeCategory.MISC).shapeless(null, "material/nugget/silver_nugget", "lotr:silver_nugget", 9, "lotr:silver_ingot");
        // material
        r.in(RecipeCategory.MISC).shapeless(null, "material/obsidian_shard", "lotr:obsidian_shard", 9, "minecraft:obsidian");
        r.in(RecipeCategory.MISC).shaped(null, "material/red_clay", "lotr:red_clay", 1, LOTRTranscribedRecipes.rows("XX", "XX"), 'X', "lotr:red_clay_ball");
        // material/seeds
        r.in(RecipeCategory.MISC).shapeless(null, "material/seeds/flax_seeds_from_flax_plant", "lotr:flax_seeds", 2, "lotr:flax_plant");
        r.in(RecipeCategory.MISC).shapeless(null, "material/seeds/green_grape_seeds", "lotr:green_grape_seeds", 1, "lotr:green_grapes");
        r.in(RecipeCategory.MISC).shapeless(null, "material/seeds/pipeweed_seeds_from_pipeweed_leaf", "lotr:pipeweed_seeds", 1, "lotr:pipeweed_leaf");
        r.in(RecipeCategory.MISC).shapeless(null, "material/seeds/pipeweed_seeds_from_pipeweed_plant", "lotr:pipeweed_seeds", 2, "lotr:pipeweed_plant");
        r.in(RecipeCategory.MISC).shapeless(null, "material/seeds/red_grape_seeds", "lotr:red_grape_seeds", 1, "lotr:red_grapes");
        // material
        r.in(RecipeCategory.MISC).shapeless(null, "material/silver_ingot", "lotr:silver_ingot", 4, "lotr:treasure_silver");
        // material/storage/black_uruk_steel_ingot
        r.in(RecipeCategory.MISC).shaped(null, "material/storage/black_uruk_steel_ingot/black_uruk_steel_block", "lotr:black_uruk_steel_block", 1, LOTRTranscribedRecipes.rows("XXX", "XXX", "XXX"), 'X', "lotr:black_uruk_steel_ingot");
        r.in(RecipeCategory.MISC).shapeless(null, "material/storage/black_uruk_steel_ingot/black_uruk_steel_ingot_from_black_uruk_steel_block", "lotr:black_uruk_steel_ingot", 9, "lotr:black_uruk_steel_block");
        // material/storage/blue_dwarven_steel_ingot
        r.in(RecipeCategory.MISC).shaped(null, "material/storage/blue_dwarven_steel_ingot/blue_dwarf_steel_block", "lotr:blue_dwarf_steel_block", 1, LOTRTranscribedRecipes.rows("XXX", "XXX", "XXX"), 'X', "lotr:blue_dwarven_steel_ingot");
        r.in(RecipeCategory.MISC).shapeless(null, "material/storage/blue_dwarven_steel_ingot/blue_dwarven_steel_ingot_from_blue_dwarf_steel_block", "lotr:blue_dwarven_steel_ingot", 9, "lotr:blue_dwarf_steel_block");
        // material/storage/bronze_ingot
        r.in(RecipeCategory.MISC).shaped(null, "material/storage/bronze_ingot/bronze_block", "lotr:bronze_block", 1, LOTRTranscribedRecipes.rows("XXX", "XXX", "XXX"), 'X', "lotr:bronze_ingot");
        r.in(RecipeCategory.MISC).shapeless(null, "material/storage/bronze_ingot/bronze_ingot_from_bronze_block", "lotr:bronze_ingot", 9, "lotr:bronze_block");
        // material/storage/dwarven_steel_ingot
        r.in(RecipeCategory.MISC).shaped(null, "material/storage/dwarven_steel_ingot/dwarf_steel_block", "lotr:dwarf_steel_block", 1, LOTRTranscribedRecipes.rows("XXX", "XXX", "XXX"), 'X', "lotr:dwarven_steel_ingot");
        r.in(RecipeCategory.MISC).shapeless(null, "material/storage/dwarven_steel_ingot/dwarven_steel_ingot_from_dwarf_steel_block", "lotr:dwarven_steel_ingot", 9, "lotr:dwarf_steel_block");
        // material/storage/elven_steel_ingot
        r.in(RecipeCategory.MISC).shaped(null, "material/storage/elven_steel_ingot/elf_steel_block", "lotr:elf_steel_block", 1, LOTRTranscribedRecipes.rows("XXX", "XXX", "XXX"), 'X', "lotr:elven_steel_ingot");
        r.in(RecipeCategory.MISC).shapeless(null, "material/storage/elven_steel_ingot/elven_steel_ingot_from_elf_steel_block", "lotr:elven_steel_ingot", 9, "lotr:elf_steel_block");
        // material/storage/galvorn_ingot
        r.in(RecipeCategory.MISC).shaped(null, "material/storage/galvorn_ingot/galvorn_block", "lotr:galvorn_block", 1, LOTRTranscribedRecipes.rows("XXX", "XXX", "XXX"), 'X', "lotr:galvorn_ingot");
        r.in(RecipeCategory.MISC).shapeless(null, "material/storage/galvorn_ingot/galvorn_ingot_from_galvorn_block", "lotr:galvorn_ingot", 9, "lotr:galvorn_block");
        // material/storage/gilded_iron_ingot
        r.in(RecipeCategory.MISC).shaped(null, "material/storage/gilded_iron_ingot/gilded_iron_block", "lotr:gilded_iron_block", 1, LOTRTranscribedRecipes.rows("XXX", "XXX", "XXX"), 'X', "lotr:gilded_iron_ingot");
        r.in(RecipeCategory.MISC).shapeless(null, "material/storage/gilded_iron_ingot/gilded_iron_ingot_from_gilded_iron_block", "lotr:gilded_iron_ingot", 9, "lotr:gilded_iron_block");
        // material/storage/morgul_steel_ingot
        r.in(RecipeCategory.MISC).shaped(null, "material/storage/morgul_steel_ingot/morgul_steel_block", "lotr:morgul_steel_block", 1, LOTRTranscribedRecipes.rows("XXX", "XXX", "XXX"), 'X', "lotr:morgul_steel_ingot");
        r.in(RecipeCategory.MISC).shapeless(null, "material/storage/morgul_steel_ingot/morgul_steel_ingot_from_morgul_steel_block", "lotr:morgul_steel_ingot", 9, "lotr:morgul_steel_block");
        // material/storage/orc_steel_ingot
        r.in(RecipeCategory.MISC).shaped(null, "material/storage/orc_steel_ingot/orc_steel_block", "lotr:orc_steel_block", 1, LOTRTranscribedRecipes.rows("XXX", "XXX", "XXX"), 'X', "lotr:orc_steel_ingot");
        r.in(RecipeCategory.MISC).shapeless(null, "material/storage/orc_steel_ingot/orc_steel_ingot_from_orc_steel_block", "lotr:orc_steel_ingot", 9, "lotr:orc_steel_block");
        // material/storage/silver_ingot
        r.in(RecipeCategory.MISC).shaped(null, "material/storage/silver_ingot/silver_block", "lotr:silver_block", 1, LOTRTranscribedRecipes.rows("XXX", "XXX", "XXX"), 'X', "lotr:silver_ingot");
        r.in(RecipeCategory.MISC).shapeless(null, "material/storage/silver_ingot/silver_ingot_from_silver_block", "lotr:silver_ingot", 9, "lotr:silver_block");
        // material/storage/tin_ingot
        r.in(RecipeCategory.MISC).shaped(null, "material/storage/tin_ingot/tin_block", "lotr:tin_block", 1, LOTRTranscribedRecipes.rows("XXX", "XXX", "XXX"), 'X', "lotr:tin_ingot");
        r.in(RecipeCategory.MISC).shapeless(null, "material/storage/tin_ingot/tin_ingot_from_tin_block", "lotr:tin_ingot", 9, "lotr:tin_block");
        // material/storage/uruk_steel_ingot
        r.in(RecipeCategory.MISC).shaped(null, "material/storage/uruk_steel_ingot/uruk_steel_block", "lotr:uruk_steel_block", 1, LOTRTranscribedRecipes.rows("XXX", "XXX", "XXX"), 'X', "lotr:uruk_steel_ingot");
        r.in(RecipeCategory.MISC).shapeless(null, "material/storage/uruk_steel_ingot/uruk_steel_ingot_from_uruk_steel_block", "lotr:uruk_steel_ingot", 9, "lotr:uruk_steel_block");
        // misc
        r.in(RecipeCategory.MISC).shaped(null, "misc/ancient_boots", "lotr:ancient_boots", 1, LOTRTranscribedRecipes.rows("X X", "X X"), 'X', "lotr:ancient_armor_plate");
        r.in(RecipeCategory.MISC).shaped(null, "misc/ancient_chestplate", "lotr:ancient_chestplate", 1, LOTRTranscribedRecipes.rows("X X", "XXX", "XXX"), 'X', "lotr:ancient_armor_plate");
        r.in(RecipeCategory.MISC).shaped(null, "misc/ancient_dagger", "lotr:ancient_dagger", 1, LOTRTranscribedRecipes.rows("X", "Y"), 'X', "lotr:ancient_sword_tip", 'Y', "lotr:ancient_sword_hilt");
        r.in(RecipeCategory.MISC).shaped(null, "misc/ancient_helmet", "lotr:ancient_helmet", 1, LOTRTranscribedRecipes.rows("XXX", "X X"), 'X', "lotr:ancient_armor_plate");
        r.in(RecipeCategory.MISC).shaped(null, "misc/ancient_leggings", "lotr:ancient_leggings", 1, LOTRTranscribedRecipes.rows("XXX", "X X", "X X"), 'X', "lotr:ancient_armor_plate");
        r.in(RecipeCategory.MISC).shaped(null, "misc/ancient_sword", "lotr:ancient_sword", 1, LOTRTranscribedRecipes.rows("X", "Y", "Z"), 'X', "lotr:ancient_sword_tip", 'Y', "lotr:ancient_sword_blade", 'Z', "lotr:ancient_sword_hilt");
        r.in(RecipeCategory.MISC).shapeless(null, "misc/anvil_from_lotr_anvil", "minecraft:anvil", 1, "lotr:anvil");
        r.in(RecipeCategory.MISC).shapeless(null, "misc/blue_rock_button", "lotr:blue_rock_button", 1, "lotr:blue_rock");
        r.in(RecipeCategory.MISC).shaped(null, "misc/blue_rock_pressure_plate", "lotr:blue_rock_pressure_plate", 1, LOTRTranscribedRecipes.rows("XX"), 'X', "lotr:blue_rock");
        r.in(RecipeCategory.MISC).shapeless(null, "misc/bone_meal", "minecraft:bone_meal", 4, "lotr:warg_bone");
        r.in(RecipeCategory.MISC).shapeless(null, "misc/bone_meal_2", "minecraft:bone_meal", 3, "lotr:orc_bone");
        r.in(RecipeCategory.MISC).shapeless(null, "misc/bone_meal_3", "minecraft:bone_meal", 3, "lotr:elf_bone");
        r.in(RecipeCategory.MISC).shapeless(null, "misc/bone_meal_4", "minecraft:bone_meal", 2, "lotr:dwarf_bone");
        r.in(RecipeCategory.MISC).shapeless(null, "misc/bone_meal_5", "minecraft:bone_meal", 2, "lotr:hobbit_bone");
        r.in(RecipeCategory.MISC).shapeless(null, "misc/bone_meal_6", "minecraft:bone_meal", 6, "lotr:troll_bone");
        r.in(RecipeCategory.MISC).shapeless(null, "misc/bottle_of_poison", "lotr:bottle_of_poison", 1, "minecraft:glass_bottle", "lotr:wildberries");
        r.in(RecipeCategory.MISC).shapeless(null, "misc/chalk_button", "lotr:chalk_button", 1, "lotr:chalk");
        r.in(RecipeCategory.MISC).shaped(null, "misc/chalk_pressure_plate", "lotr:chalk_pressure_plate", 1, LOTRTranscribedRecipes.rows("XX"), 'X', "lotr:chalk");
        r.in(RecipeCategory.MISC).shaped(null, "misc/gate_bronze_bars", "lotr:gate_bronze_bars", 4, LOTRTranscribedRecipes.rows("YYY", "YXY", "YYY"), 'X', "lotr:gate_gears", 'Y', "lotr:bronze_ingot");
        r.in(RecipeCategory.MISC).shaped(null, "misc/gate_iron_bars", "lotr:gate_iron_bars", 4, LOTRTranscribedRecipes.rows("YYY", "YXY", "YYY"), 'X', "lotr:gate_gears", 'Y', "minecraft:iron_ingot");
        r.in(RecipeCategory.MISC).shaped(null, "misc/gate_wooden_cross", "lotr:gate_wooden_cross", 4, LOTRTranscribedRecipes.rows("YYY", "YXY", "YYY"), 'X', "lotr:gate_gears", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.MISC).shaped(null, "misc/gold_gate", "lotr:gold_gate", 4, LOTRTranscribedRecipes.rows("YYY", "YXY", "YYY"), 'X', "lotr:gate_gears", 'Y', "minecraft:gold_ingot");
        r.in(RecipeCategory.MISC).shapeless(null, "misc/gold_ingot", "minecraft:gold_ingot", 4, "lotr:treasure_gold");
        r.in(RecipeCategory.MISC).shaped(null, "misc/gold_ring", "lotr:gold_ring", 1, LOTRTranscribedRecipes.rows("XXX", "X X", "XXX"), 'X', "minecraft:gold_nugget");
        r.in(RecipeCategory.MISC).shapeless(null, "misc/gondor_rock_button", "lotr:gondor_rock_button", 1, "lotr:gondor_rock");
        r.in(RecipeCategory.MISC).shaped(null, "misc/gondor_rock_pressure_plate", "lotr:gondor_rock_pressure_plate", 1, LOTRTranscribedRecipes.rows("XX"), 'X', "lotr:gondor_rock");
        r.in(RecipeCategory.MISC).shaped(null, "misc/item_frame", "minecraft:item_frame", 1, LOTRTranscribedRecipes.rows("XXX", "XYX", "XXX"), 'X', "#lotr:sticks", 'Y', "lotr:fur");
        r.in(RecipeCategory.MISC).shaped(null, "misc/item_frame_2", "minecraft:item_frame", 1, LOTRTranscribedRecipes.rows("XXX", "XYX", "XXX"), 'X', "#lotr:sticks", 'Y', "lotr:gemsbok_hide");
        r.in(RecipeCategory.MISC).shapeless(null, "misc/key_of_ice", "lotr:key_of_ice", 1, "lotr:ice_key_handle", "lotr:ice_key_shaft", "lotr:ice_key_pin");
        r.in(RecipeCategory.MISC).shapeless(null, "misc/key_of_obsidian", "lotr:key_of_obsidian", 1, "lotr:obsidian_key_handle", "lotr:obsidian_key_shaft", "lotr:obsidian_key_pin");
        r.in(RecipeCategory.MISC).shaped(null, "misc/leather_hat", "lotr:leather_hat", 1, LOTRTranscribedRecipes.rows(" X ", "XXX"), 'X', "minecraft:leather");
        r.in(RecipeCategory.MISC).shapeless(null, "misc/lotr_anvil", "lotr:anvil", 1, "minecraft:anvil");
        r.in(RecipeCategory.MISC).shaped(null, "misc/mithril_gate", "lotr:mithril_gate", 4, LOTRTranscribedRecipes.rows("YYY", "YXY", "YYY"), 'X', "lotr:gate_gears", 'Y', "lotr:mithril");
        r.in(RecipeCategory.MISC).shaped(null, "misc/mithril_ring", "lotr:mithril_ring", 1, LOTRTranscribedRecipes.rows("XXX", "X X", "XXX"), 'X', "lotr:mithril_nugget");
        r.in(RecipeCategory.MISC).shapeless(null, "misc/mordor_rock_button", "lotr:mordor_rock_button", 1, "lotr:mordor_rock");
        r.in(RecipeCategory.MISC).shaped(null, "misc/mordor_rock_pressure_plate", "lotr:mordor_rock_pressure_plate", 1, LOTRTranscribedRecipes.rows("XX"), 'X', "lotr:mordor_rock");
        r.in(RecipeCategory.MISC).shaped(null, "misc/obsidian", "minecraft:obsidian", 1, LOTRTranscribedRecipes.rows("XXX", "XXX", "XXX"), 'X', "lotr:obsidian_shard");
        r.in(RecipeCategory.MISC).shapeless(null, "misc/red_book", "lotr:red_book", 1, "minecraft:book", "minecraft:red_dye", "minecraft:gold_nugget");
        r.in(RecipeCategory.MISC).shapeless(null, "misc/red_rock_button", "lotr:red_rock_button", 1, "lotr:red_rock");
        r.in(RecipeCategory.MISC).shaped(null, "misc/red_rock_pressure_plate", "lotr:red_rock_pressure_plate", 1, LOTRTranscribedRecipes.rows("XX"), 'X', "lotr:red_rock");
        r.in(RecipeCategory.MISC).shapeless(null, "misc/rohan_rock_button", "lotr:rohan_rock_button", 1, "lotr:rohan_rock");
        r.in(RecipeCategory.MISC).shaped(null, "misc/rohan_rock_pressure_plate", "lotr:rohan_rock_pressure_plate", 1, LOTRTranscribedRecipes.rows("XX"), 'X', "lotr:rohan_rock");
        r.in(RecipeCategory.MISC).shaped(null, "misc/silver_coin", "lotr:silver_coin", 4, LOTRTranscribedRecipes.rows("XX", "XX"), 'X', "lotr:silver_nugget");
        r.in(RecipeCategory.MISC).shaped(null, "misc/silver_gate", "lotr:silver_gate", 4, LOTRTranscribedRecipes.rows("YYY", "YXY", "YYY"), 'X', "lotr:gate_gears", 'Y', "lotr:silver_ingot");
        r.in(RecipeCategory.MISC).shaped(null, "misc/silver_ring", "lotr:silver_ring", 1, LOTRTranscribedRecipes.rows("XXX", "X X", "XXX"), 'X', "lotr:silver_nugget");
        r.in(RecipeCategory.MISC).shapeless(null, "misc/string", "minecraft:string", 1, "lotr:flax");
        r.in(RecipeCategory.MISC).shaped(null, "misc/wooden_gate", "lotr:wooden_gate", 4, LOTRTranscribedRecipes.rows("ZYZ", "YXY", "ZYZ"), 'X', "lotr:gate_gears", 'Y', "#minecraft:planks", 'Z', "minecraft:iron_ingot");
        // smelting/food
        r.in(RecipeCategory.FOOD).smelting("smelting/food/cooked_camel", "lotr:cooked_camel", "lotr:raw_camel", 0.35f);
        r.in(RecipeCategory.FOOD).smelting("smelting/food/cooked_corn", "lotr:cooked_corn", "lotr:corn", 0.3f);
        r.in(RecipeCategory.FOOD).smelting("smelting/food/cooked_lion", "lotr:cooked_lion", "lotr:raw_lion", 0.35f);
        r.in(RecipeCategory.FOOD).smelting("smelting/food/cooked_mutton", "lotr:cooked_mutton", "lotr:raw_mutton", 0.35f);
        r.in(RecipeCategory.FOOD).smelting("smelting/food/cooked_rhino", "lotr:cooked_rhino", "lotr:raw_rhino", 0.35f);
        r.in(RecipeCategory.FOOD).smelting("smelting/food/cooked_venison", "lotr:cooked_venison", "lotr:raw_venison", 0.35f);
        r.in(RecipeCategory.FOOD).smelting("smelting/food/cooked_zebra", "lotr:cooked_zebra", "lotr:raw_zebra", 0.35f);
        r.in(RecipeCategory.FOOD).smelting("smelting/food/raisins_from_green_grapes", "lotr:raisins", "lotr:green_grapes", 0.3f);
        r.in(RecipeCategory.FOOD).smelting("smelting/food/raisins_from_red_grapes", "lotr:raisins", "lotr:red_grapes", 0.3f);
        r.in(RecipeCategory.FOOD).smelting("smelting/food/roast_chestnut", "lotr:roast_chestnut", "lotr:conker", 0.3f);
        r.in(RecipeCategory.FOOD).smelting("smelting/food/roast_turnip", "lotr:roast_turnip", "lotr:turnip", 0.3f);
        r.in(RecipeCategory.FOOD).smelting("smelting/food/roast_yam", "lotr:roast_yam", "lotr:yam", 0.3f);
        // smelting/material
        r.in(RecipeCategory.MISC).smelting("smelting/material/brick_from_red_clay_ball", "minecraft:brick", "lotr:red_clay_ball", 0.3f);
        r.in(RecipeCategory.MISC).smelting("smelting/material/silver_ingot", "lotr:silver_ingot", "lotr:silver_ore", 0.8f);
        r.in(RecipeCategory.MISC).smelting("smelting/material/sulfur", "lotr:sulfur", "lotr:sulfur_ore", 1.0f);
        r.in(RecipeCategory.MISC).smelting("smelting/material/tin_ingot", "lotr:tin_ingot", "lotr:tin_ore", 0.35f);
        // smelting/misc
        r.in(RecipeCategory.MISC).smelting("smelting/misc/pipeweed_from_pipeweed_leaf", "lotr:pipeweed", "lotr:pipeweed_leaf", 0.25f);
        // smelting/tableware
        r.in(RecipeCategory.MISC).smelting("smelting/tableware/ceramic_mug", "lotr:ceramic_mug", "lotr:clay_mug", 0.3f);
        r.in(RecipeCategory.MISC).smelting("smelting/tableware/fine_plate", "lotr:fine_plate", "lotr:stoneware_plate", 0.3f);
        r.in(RecipeCategory.MISC).smelting("smelting/tableware/stoneware_plate", "lotr:stoneware_plate", "lotr:clay_plate", 0.3f);
        // tableware
        r.in(RecipeCategory.MISC).shaped(null, "tableware/ale_horn", "lotr:ale_horn", 1, LOTRTranscribedRecipes.rows("X", "Y"), 'X', "#lotr:horns", 'Y', "lotr:tin_ingot");
        r.in(RecipeCategory.MISC).shaped(null, "tableware/barrel", "lotr:barrel", 1, LOTRTranscribedRecipes.rows("XXX", "YZY", "XXX"), 'X', "#minecraft:planks", 'Y', "minecraft:iron_ingot", 'Z', "minecraft:bucket");
        r.in(RecipeCategory.MISC).shaped(null, "tableware/clay_mug", "lotr:clay_mug", 2, LOTRTranscribedRecipes.rows("X", "Y", "X"), 'X', "lotr:tin_ingot", 'Y', "#lotr:clay_balls");
        r.in(RecipeCategory.MISC).shaped(null, "tableware/clay_plate", "lotr:clay_plate", 2, LOTRTranscribedRecipes.rows("XX"), 'X', "#lotr:clay_balls");
        r.in(RecipeCategory.MISC).shaped(null, "tableware/copper_goblet", "lotr:copper_goblet", 2, LOTRTranscribedRecipes.rows("X X", " X ", " X "), 'X', "minecraft:copper_ingot");
        r.in(RecipeCategory.MISC).shaped(null, "tableware/golden_ale_horn", "lotr:golden_ale_horn", 1, LOTRTranscribedRecipes.rows("X", "Y"), 'X', "#lotr:horns", 'Y', "minecraft:gold_ingot");
        r.in(RecipeCategory.MISC).shaped(null, "tableware/golden_goblet", "lotr:golden_goblet", 2, LOTRTranscribedRecipes.rows("X X", " X ", " X "), 'X', "minecraft:gold_ingot");
        r.in(RecipeCategory.MISC).shaped(null, "tableware/mug", "lotr:mug", 2, LOTRTranscribedRecipes.rows("X", "Y", "X"), 'X', "lotr:tin_ingot", 'Y', "#minecraft:planks");
        r.in(RecipeCategory.MISC).shaped(null, "tableware/silver_goblet", "lotr:silver_goblet", 2, LOTRTranscribedRecipes.rows("X X", " X ", " X "), 'X', "lotr:silver_ingot");
        r.in(RecipeCategory.MISC).shaped(null, "tableware/skull_cup", "lotr:skull_cup", 1, LOTRTranscribedRecipes.rows("X", "Y"), 'X', "minecraft:skeleton_skull", 'Y', "lotr:tin_ingot");
        r.in(RecipeCategory.MISC).shaped(null, "tableware/waterskin", "lotr:waterskin", 2, LOTRTranscribedRecipes.rows(" Y ", "X X", " X "), 'X', "minecraft:leather", 'Y', "minecraft:string");
        r.in(RecipeCategory.MISC).shaped(null, "tableware/waterskin_from_fur", "lotr:waterskin", 2, LOTRTranscribedRecipes.rows(" Y ", "X X", " X "), 'X', "lotr:fur", 'Y', "minecraft:string");
        r.in(RecipeCategory.MISC).shaped(null, "tableware/waterskin_from_gemsbok_hide", "lotr:waterskin", 2, LOTRTranscribedRecipes.rows(" Y ", "X X", " X "), 'X', "lotr:gemsbok_hide", 'Y', "minecraft:string");
        r.in(RecipeCategory.MISC).shaped(null, "tableware/waterskin_from_lion_fur", "lotr:waterskin", 2, LOTRTranscribedRecipes.rows(" Y ", "X X", " X "), 'X', "lotr:lion_fur", 'Y', "minecraft:string");
        r.in(RecipeCategory.MISC).shaped(null, "tableware/wine_glass", "lotr:wine_glass", 2, LOTRTranscribedRecipes.rows("X X", " X ", " X "), 'X', "minecraft:glass");
        r.in(RecipeCategory.MISC).shaped(null, "tableware/wooden_cup", "lotr:wooden_cup", 2, LOTRTranscribedRecipes.rows("X X", " X ", " X "), 'X', "#minecraft:planks");
        r.in(RecipeCategory.MISC).shaped(null, "tableware/wooden_plate", "lotr:wooden_plate", 2, LOTRTranscribedRecipes.rows("XX"), 'X', "#minecraft:logs");
        // tool
        r.in(RecipeCategory.COMBAT).shaped(null, "tool/bronze_axe", "lotr:bronze_axe", 1, LOTRTranscribedRecipes.rows("XX", "XY", " Y"), 'X', "lotr:bronze_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(null, "tool/bronze_hoe", "lotr:bronze_hoe", 1, LOTRTranscribedRecipes.rows("XX", " Y", " Y"), 'X', "lotr:bronze_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(null, "tool/bronze_pickaxe", "lotr:bronze_pickaxe", 1, LOTRTranscribedRecipes.rows("XXX", " Y ", " Y "), 'X', "lotr:bronze_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(null, "tool/bronze_shovel", "lotr:bronze_shovel", 1, LOTRTranscribedRecipes.rows("X", "Y", "Y"), 'X', "lotr:bronze_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(null, "tool/chisel", "lotr:chisel", 1, LOTRTranscribedRecipes.rows("XY"), 'X', "minecraft:iron_ingot", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(null, "tool/mithril_axe", "lotr:mithril_axe", 1, LOTRTranscribedRecipes.rows("XX", "XY", " Y"), 'X', "lotr:mithril", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(null, "tool/mithril_hoe", "lotr:mithril_hoe", 1, LOTRTranscribedRecipes.rows("XX", " Y", " Y"), 'X', "lotr:mithril", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(null, "tool/mithril_mattock", "lotr:mithril_mattock", 1, LOTRTranscribedRecipes.rows("XXX", "XY ", " Y "), 'X', "lotr:mithril", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(null, "tool/mithril_pickaxe", "lotr:mithril_pickaxe", 1, LOTRTranscribedRecipes.rows("XXX", " Y ", " Y "), 'X', "lotr:mithril", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(null, "tool/mithril_shovel", "lotr:mithril_shovel", 1, LOTRTranscribedRecipes.rows("X", "Y", "Y"), 'X', "lotr:mithril", 'Y', "#lotr:sticks");
        r.in(RecipeCategory.COMBAT).shaped(null, "tool/sulfur_match", "lotr:sulfur_match", 4, LOTRTranscribedRecipes.rows("X", "Y"), 'X', "lotr:sulfur", 'Y', "#lotr:sticks");
        // utility
        r.in(RecipeCategory.MISC).shaped(null, "utility/fur_bed", "lotr:fur_bed", 1, LOTRTranscribedRecipes.rows("XXX", "YYY"), 'X', "lotr:fur", 'Y', "#minecraft:planks");
        r.in(RecipeCategory.MISC).shaped(null, "utility/lion_fur_bed", "lotr:lion_fur_bed", 1, LOTRTranscribedRecipes.rows("XXX", "YYY"), 'X', "lotr:lion_fur", 'Y', "#minecraft:planks");
        r.in(RecipeCategory.MISC).shaped(null, "utility/millstone", "lotr:millstone", 1, LOTRTranscribedRecipes.rows("XYX", "XZX", "XXX"), 'X', "minecraft:cobblestone", 'Y', "minecraft:iron_ingot", 'Z', "#lotr:sticks");
        r.in(RecipeCategory.MISC).shaped(null, "utility/table_of_command", "lotr:table_of_command", 1, LOTRTranscribedRecipes.rows("XXX", "YYY", "ZZZ"), 'X', "minecraft:paper", 'Y', "#minecraft:planks", 'Z', "lotr:bronze_ingot");
    }
}
