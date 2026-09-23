package net.blueskiez77.lord_of_the_rings__middle_earth.datagen;

import static net.blueskiez77.lord_of_the_rings__middle_earth.common.recipe.LOTRCraftingTable.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.recipe.LOTRCraftingTable;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.recipe.LOTRFactionCraftingRecipe;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.recipe.LOTRFactionShapelessRecipe;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.recipe.LOTRMillstoneRecipes;

import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.RecipeUnlockAdvancementBuilder;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;

// Recipes transcribed one-for-one from LOTRRecipes.java, grouped under the create*Recipes method each came from, so the two files can be read side by side. LOTRRecipeProvider keeps the mechanical families (planks, beams, slabs, stairs, walls, smooth stone); this class holds everything else the port currently has the items for.
//
// A null table is GameRegistry.addRecipe / addShapelessRecipe -- the vanilla crafting table. A named table is that faction list; a recipe added through addRecipeTo(common*Recipes, ...) is repeated once per table in the group, exactly as LOTRCraftingTable.Groups lists them.
//
// Ingredients are ids, "#namespace:path" for tags. Ore dictionary names became tags where 26.2 has an equivalent (plankWood -> #minecraft:planks, logWood -> #minecraft:logs, stickWood -> #lotr:sticks, dyeBlue -> #c:dyes/blue, ...); Blocks.wool with no metadata was a wildcard and is #minecraft:wool.
//
// NOT here, because the port does not have what they need yet: every recipe that uses a steel or alloy ingot (orc, uruk, elven, dwarven, blue dwarven, black uruk, morgul steel, bronze, copper, tin, silver, gilded iron, galvorn), mallorn sticks, gate gears, food and drink, the vessel/pouch/banner-pattern/dye/poison special recipe classes, and the recipes whose result is a block or item not yet ported.
final class LOTRTranscribedRecipes extends RecipeProvider {

    // Results the 1.7.10 mod could ONLY craft at a faction table -- every recipe for them lived in a faction list, none in GameRegistry -- limited to those whose recipes all resolve in the port. LOTRRecipeProvider's mechanical families skip these so the vanilla table does not offer them; their faction-table recipes are transcribed below with the rest.
    static final Set<Identifier> FACTION_ONLY = Set.of(
            Identifier.parse("lotr:angmar_banner"),
            Identifier.parse("lotr:angmar_brick"),
            Identifier.parse("lotr:angmar_brick_slab"),
            Identifier.parse("lotr:angmar_brick_stairs"),
            Identifier.parse("lotr:angmar_brick_wall"),
            Identifier.parse("lotr:angmar_crafting_table"),
            Identifier.parse("lotr:angmar_pillar"),
            Identifier.parse("lotr:angmar_pillar_slab"),
            Identifier.parse("lotr:angmar_snow_brick"),
            Identifier.parse("lotr:angmar_snow_brick_slab"),
            Identifier.parse("lotr:angmar_snow_brick_stairs"),
            Identifier.parse("lotr:angmar_snow_brick_wall"),
            Identifier.parse("lotr:anorien_banner"),
            Identifier.parse("lotr:arnor_brick"),
            Identifier.parse("lotr:arnor_brick_slab"),
            Identifier.parse("lotr:arnor_brick_stairs"),
            Identifier.parse("lotr:arnor_brick_wall"),
            Identifier.parse("lotr:arnor_pillar"),
            Identifier.parse("lotr:arnor_pillar_slab"),
            Identifier.parse("lotr:beacon_of_gondor"),
            Identifier.parse("lotr:blackroot_vale_banner"),
            Identifier.parse("lotr:blue_dwarven_crafting_table"),
            Identifier.parse("lotr:blue_mountains_banner"),
            Identifier.parse("lotr:bree_banner"),
            Identifier.parse("lotr:bree_crafting_table"),
            Identifier.parse("lotr:carved_arnor_brick"),
            Identifier.parse("lotr:carved_black_umbar_brick"),
            Identifier.parse("lotr:carved_dol_guldur_brick"),
            Identifier.parse("lotr:carved_dorwinion_brick"),
            Identifier.parse("lotr:carved_dwarven_brick"),
            Identifier.parse("lotr:carved_galadhrim_brick"),
            Identifier.parse("lotr:carved_gondor_brick"),
            Identifier.parse("lotr:carved_high_elven_brick"),
            Identifier.parse("lotr:carved_mordor_brick"),
            Identifier.parse("lotr:carved_rohan_brick"),
            Identifier.parse("lotr:carved_umbar_brick"),
            Identifier.parse("lotr:carved_wood_elven_brick"),
            Identifier.parse("lotr:cracked_angmar_brick_slab"),
            Identifier.parse("lotr:cracked_angmar_brick_stairs"),
            Identifier.parse("lotr:cracked_angmar_brick_wall"),
            Identifier.parse("lotr:cracked_arnor_brick_slab"),
            Identifier.parse("lotr:cracked_arnor_brick_stairs"),
            Identifier.parse("lotr:cracked_arnor_brick_wall"),
            Identifier.parse("lotr:cracked_arnor_pillar_slab"),
            Identifier.parse("lotr:cracked_dol_guldur_brick_slab"),
            Identifier.parse("lotr:cracked_dol_guldur_brick_stairs"),
            Identifier.parse("lotr:cracked_dol_guldur_brick_wall"),
            Identifier.parse("lotr:cracked_dorwinion_brick_slab"),
            Identifier.parse("lotr:cracked_dorwinion_brick_stairs"),
            Identifier.parse("lotr:cracked_dorwinion_brick_wall"),
            Identifier.parse("lotr:cracked_dwarven_brick_slab"),
            Identifier.parse("lotr:cracked_dwarven_brick_stairs"),
            Identifier.parse("lotr:cracked_dwarven_brick_wall"),
            Identifier.parse("lotr:cracked_dwarven_pillar_slab"),
            Identifier.parse("lotr:cracked_galadhrim_brick_slab"),
            Identifier.parse("lotr:cracked_galadhrim_brick_stairs"),
            Identifier.parse("lotr:cracked_galadhrim_brick_wall"),
            Identifier.parse("lotr:cracked_galadhrim_pillar_slab"),
            Identifier.parse("lotr:cracked_gondor_brick_slab"),
            Identifier.parse("lotr:cracked_gondor_brick_stairs"),
            Identifier.parse("lotr:cracked_gondor_brick_wall"),
            Identifier.parse("lotr:cracked_gondor_cobblebrick_slab"),
            Identifier.parse("lotr:cracked_gondor_cobblebrick_stairs"),
            Identifier.parse("lotr:cracked_gondor_cobblebrick_wall"),
            Identifier.parse("lotr:cracked_high_elven_brick_slab"),
            Identifier.parse("lotr:cracked_high_elven_brick_stairs"),
            Identifier.parse("lotr:cracked_high_elven_brick_wall"),
            Identifier.parse("lotr:cracked_high_elven_pillar_slab"),
            Identifier.parse("lotr:cracked_mordor_brick_slab"),
            Identifier.parse("lotr:cracked_mordor_brick_stairs"),
            Identifier.parse("lotr:cracked_mordor_brick_wall"),
            Identifier.parse("lotr:cracked_morwaith_brick_slab"),
            Identifier.parse("lotr:cracked_morwaith_brick_stairs"),
            Identifier.parse("lotr:cracked_morwaith_brick_wall"),
            Identifier.parse("lotr:cracked_umbar_brick_slab"),
            Identifier.parse("lotr:cracked_umbar_brick_stairs"),
            Identifier.parse("lotr:cracked_umbar_brick_wall"),
            Identifier.parse("lotr:cracked_wood_elven_brick_slab"),
            Identifier.parse("lotr:cracked_wood_elven_brick_stairs"),
            Identifier.parse("lotr:cracked_wood_elven_brick_wall"),
            Identifier.parse("lotr:cracked_wood_elven_pillar_slab"),
            Identifier.parse("lotr:dale_banner"),
            Identifier.parse("lotr:dale_brick"),
            Identifier.parse("lotr:dale_brick_slab"),
            Identifier.parse("lotr:dale_brick_stairs"),
            Identifier.parse("lotr:dale_brick_wall"),
            Identifier.parse("lotr:dale_carved_brick"),
            Identifier.parse("lotr:dale_cracked_brick_slab"),
            Identifier.parse("lotr:dale_cracked_brick_stairs"),
            Identifier.parse("lotr:dale_cracked_brick_wall"),
            Identifier.parse("lotr:dale_crafting_table"),
            Identifier.parse("lotr:dale_mossy_brick"),
            Identifier.parse("lotr:dale_mossy_brick_slab"),
            Identifier.parse("lotr:dale_mossy_brick_stairs"),
            Identifier.parse("lotr:dale_mossy_brick_wall"),
            Identifier.parse("lotr:dale_pillar"),
            Identifier.parse("lotr:dale_pillar_slab"),
            Identifier.parse("lotr:dol_amroth_banner"),
            Identifier.parse("lotr:dol_amroth_boots"),
            Identifier.parse("lotr:dol_amroth_brick"),
            Identifier.parse("lotr:dol_amroth_brick_slab"),
            Identifier.parse("lotr:dol_amroth_brick_stairs"),
            Identifier.parse("lotr:dol_amroth_brick_wall"),
            Identifier.parse("lotr:dol_amroth_crafting_table"),
            Identifier.parse("lotr:dol_amroth_horse_armor"),
            Identifier.parse("lotr:dol_amroth_lance"),
            Identifier.parse("lotr:dol_amroth_leggings"),
            Identifier.parse("lotr:dol_amroth_sword"),
            Identifier.parse("lotr:dol_guldur_banner"),
            Identifier.parse("lotr:dol_guldur_brick"),
            Identifier.parse("lotr:dol_guldur_brick_slab"),
            Identifier.parse("lotr:dol_guldur_brick_stairs"),
            Identifier.parse("lotr:dol_guldur_brick_wall"),
            Identifier.parse("lotr:dol_guldur_crafting_table"),
            Identifier.parse("lotr:dol_guldur_pillar"),
            Identifier.parse("lotr:dol_guldur_pillar_slab"),
            Identifier.parse("lotr:dorwinion_banner"),
            Identifier.parse("lotr:dorwinion_brick"),
            Identifier.parse("lotr:dorwinion_brick_slab"),
            Identifier.parse("lotr:dorwinion_brick_stairs"),
            Identifier.parse("lotr:dorwinion_brick_wall"),
            Identifier.parse("lotr:dorwinion_crafting_table"),
            Identifier.parse("lotr:dorwinion_flowers_brick"),
            Identifier.parse("lotr:dorwinion_flowers_brick_slab"),
            Identifier.parse("lotr:dorwinion_flowers_brick_stairs"),
            Identifier.parse("lotr:dorwinion_flowers_brick_wall"),
            Identifier.parse("lotr:dorwinion_pillar"),
            Identifier.parse("lotr:dorwinion_pillar_slab"),
            Identifier.parse("lotr:double_strength_orc_fire_bomb"),
            Identifier.parse("lotr:dunland_banner"),
            Identifier.parse("lotr:dunlending_boots"),
            Identifier.parse("lotr:dunlending_chestplate"),
            Identifier.parse("lotr:dunlending_club"),
            Identifier.parse("lotr:dunlending_crafting_table"),
            Identifier.parse("lotr:dunlending_helmet"),
            Identifier.parse("lotr:dunlending_leggings"),
            Identifier.parse("lotr:dunlending_trident"),
            Identifier.parse("lotr:durin_banner"),
            Identifier.parse("lotr:dwarven_bed"),
            Identifier.parse("lotr:dwarven_brick"),
            Identifier.parse("lotr:dwarven_brick_slab"),
            Identifier.parse("lotr:dwarven_brick_stairs"),
            Identifier.parse("lotr:dwarven_brick_wall"),
            Identifier.parse("lotr:dwarven_crafting_table"),
            Identifier.parse("lotr:dwarven_door"),
            Identifier.parse("lotr:dwarven_forge"),
            Identifier.parse("lotr:dwarven_gold_brick"),
            Identifier.parse("lotr:dwarven_pillar"),
            Identifier.parse("lotr:dwarven_pillar_slab"),
            Identifier.parse("lotr:elven_crafting_table"),
            Identifier.parse("lotr:elven_forge"),
            Identifier.parse("lotr:esgaroth_banner"),
            Identifier.parse("lotr:galadhrim_brick"),
            Identifier.parse("lotr:galadhrim_brick_slab"),
            Identifier.parse("lotr:galadhrim_brick_stairs"),
            Identifier.parse("lotr:galadhrim_brick_wall"),
            Identifier.parse("lotr:galadhrim_gold_brick"),
            Identifier.parse("lotr:galadhrim_pillar"),
            Identifier.parse("lotr:galadhrim_pillar_slab"),
            Identifier.parse("lotr:glowing_dwarven_brick"),
            Identifier.parse("lotr:gold_tauredain_dart_trap"),
            Identifier.parse("lotr:gold_trimmed_dwarven_boots"),
            Identifier.parse("lotr:gold_trimmed_dwarven_chestplate"),
            Identifier.parse("lotr:gold_trimmed_dwarven_helmet"),
            Identifier.parse("lotr:gold_trimmed_dwarven_leggings"),
            Identifier.parse("lotr:gondor_banner"),
            Identifier.parse("lotr:gondor_boots"),
            Identifier.parse("lotr:gondor_bow"),
            Identifier.parse("lotr:gondor_brick"),
            Identifier.parse("lotr:gondor_brick_slab"),
            Identifier.parse("lotr:gondor_brick_stairs"),
            Identifier.parse("lotr:gondor_brick_wall"),
            Identifier.parse("lotr:gondor_chestplate"),
            Identifier.parse("lotr:gondor_cobblebrick"),
            Identifier.parse("lotr:gondor_cobblebrick_slab"),
            Identifier.parse("lotr:gondor_cobblebrick_stairs"),
            Identifier.parse("lotr:gondor_cobblebrick_wall"),
            Identifier.parse("lotr:gondor_dagger"),
            Identifier.parse("lotr:gondor_helmet"),
            Identifier.parse("lotr:gondor_horse_armor"),
            Identifier.parse("lotr:gondor_leggings"),
            Identifier.parse("lotr:gondor_pillar"),
            Identifier.parse("lotr:gondor_pillar_slab"),
            Identifier.parse("lotr:gondor_rock_slab"),
            Identifier.parse("lotr:gondor_rock_stairs"),
            Identifier.parse("lotr:gondor_rock_wall"),
            Identifier.parse("lotr:gondor_spear"),
            Identifier.parse("lotr:gondor_steward_banner"),
            Identifier.parse("lotr:gondor_sword"),
            Identifier.parse("lotr:gondor_warhammer"),
            Identifier.parse("lotr:gondor_winged_helmet"),
            Identifier.parse("lotr:gondorian_crafting_table"),
            Identifier.parse("lotr:gulf_crafting_table"),
            Identifier.parse("lotr:gundabad_banner"),
            Identifier.parse("lotr:gundabad_crafting_table"),
            Identifier.parse("lotr:half_troll_banner"),
            Identifier.parse("lotr:half_troll_battleaxe"),
            Identifier.parse("lotr:half_troll_crafting_table"),
            Identifier.parse("lotr:half_troll_dagger"),
            Identifier.parse("lotr:half_troll_mace"),
            Identifier.parse("lotr:half_troll_pike"),
            Identifier.parse("lotr:half_troll_scimitar"),
            Identifier.parse("lotr:half_troll_warhammer"),
            Identifier.parse("lotr:harad_bow"),
            Identifier.parse("lotr:harad_gulf_banner"),
            Identifier.parse("lotr:harad_nomad_banner"),
            Identifier.parse("lotr:high_elf_banner"),
            Identifier.parse("lotr:high_elf_wood_bars"),
            Identifier.parse("lotr:high_elven_bed"),
            Identifier.parse("lotr:high_elven_brick"),
            Identifier.parse("lotr:high_elven_brick_slab"),
            Identifier.parse("lotr:high_elven_brick_stairs"),
            Identifier.parse("lotr:high_elven_brick_wall"),
            Identifier.parse("lotr:high_elven_crafting_table"),
            Identifier.parse("lotr:high_elven_gold_brick"),
            Identifier.parse("lotr:high_elven_pillar"),
            Identifier.parse("lotr:high_elven_pillar_slab"),
            Identifier.parse("lotr:high_elven_torch"),
            Identifier.parse("lotr:hobbit_banner"),
            Identifier.parse("lotr:hobbit_crafting_table"),
            Identifier.parse("lotr:hobbit_oven"),
            Identifier.parse("lotr:isengard_banner"),
            Identifier.parse("lotr:kebab_stand"),
            Identifier.parse("lotr:lamedon_banner"),
            Identifier.parse("lotr:lebennin_banner"),
            Identifier.parse("lotr:lossarnach_banner"),
            Identifier.parse("lotr:mallorn_beam"),
            Identifier.parse("lotr:mallorn_box"),
            Identifier.parse("lotr:mallorn_door"),
            Identifier.parse("lotr:mallorn_planks"),
            Identifier.parse("lotr:mallorn_slab"),
            Identifier.parse("lotr:mallorn_stairs"),
            Identifier.parse("lotr:mallorn_trapdoor"),
            Identifier.parse("lotr:minas_morgul_banner"),
            Identifier.parse("lotr:mirkwood_banner"),
            Identifier.parse("lotr:mirkwood_bow"),
            Identifier.parse("lotr:mordor_banner"),
            Identifier.parse("lotr:mordor_brick"),
            Identifier.parse("lotr:mordor_brick_slab"),
            Identifier.parse("lotr:mordor_brick_stairs"),
            Identifier.parse("lotr:mordor_brick_wall"),
            Identifier.parse("lotr:mordor_pillar"),
            Identifier.parse("lotr:mordor_pillar_slab"),
            Identifier.parse("lotr:mordor_rock_slab"),
            Identifier.parse("lotr:mordor_rock_stairs"),
            Identifier.parse("lotr:mordor_rock_wall"),
            Identifier.parse("lotr:moredain_banner"),
            Identifier.parse("lotr:moredain_crafting_table"),
            Identifier.parse("lotr:morgul_crafting_table"),
            Identifier.parse("lotr:morgul_torch"),
            Identifier.parse("lotr:morwaith_brick"),
            Identifier.parse("lotr:morwaith_brick_slab"),
            Identifier.parse("lotr:morwaith_brick_stairs"),
            Identifier.parse("lotr:morwaith_brick_wall"),
            Identifier.parse("lotr:mossy_arnor_brick"),
            Identifier.parse("lotr:mossy_arnor_brick_slab"),
            Identifier.parse("lotr:mossy_arnor_brick_stairs"),
            Identifier.parse("lotr:mossy_arnor_brick_wall"),
            Identifier.parse("lotr:mossy_dol_guldur_brick"),
            Identifier.parse("lotr:mossy_dol_guldur_brick_slab"),
            Identifier.parse("lotr:mossy_dol_guldur_brick_stairs"),
            Identifier.parse("lotr:mossy_dol_guldur_brick_wall"),
            Identifier.parse("lotr:mossy_dorwinion_brick"),
            Identifier.parse("lotr:mossy_dorwinion_brick_slab"),
            Identifier.parse("lotr:mossy_dorwinion_brick_stairs"),
            Identifier.parse("lotr:mossy_dorwinion_brick_wall"),
            Identifier.parse("lotr:mossy_dorwinion_pillar"),
            Identifier.parse("lotr:mossy_dorwinion_pillar_slab"),
            Identifier.parse("lotr:mossy_galadhrim_brick"),
            Identifier.parse("lotr:mossy_galadhrim_brick_slab"),
            Identifier.parse("lotr:mossy_galadhrim_brick_stairs"),
            Identifier.parse("lotr:mossy_galadhrim_brick_wall"),
            Identifier.parse("lotr:mossy_gondor_brick"),
            Identifier.parse("lotr:mossy_gondor_brick_slab"),
            Identifier.parse("lotr:mossy_gondor_brick_stairs"),
            Identifier.parse("lotr:mossy_gondor_brick_wall"),
            Identifier.parse("lotr:mossy_gondor_cobblebrick"),
            Identifier.parse("lotr:mossy_gondor_cobblebrick_slab"),
            Identifier.parse("lotr:mossy_gondor_cobblebrick_stairs"),
            Identifier.parse("lotr:mossy_gondor_cobblebrick_wall"),
            Identifier.parse("lotr:mossy_high_elven_brick"),
            Identifier.parse("lotr:mossy_high_elven_brick_slab"),
            Identifier.parse("lotr:mossy_high_elven_brick_stairs"),
            Identifier.parse("lotr:mossy_high_elven_brick_wall"),
            Identifier.parse("lotr:mossy_wood_elven_brick"),
            Identifier.parse("lotr:mossy_wood_elven_brick_slab"),
            Identifier.parse("lotr:mossy_wood_elven_brick_stairs"),
            Identifier.parse("lotr:mossy_wood_elven_brick_wall"),
            Identifier.parse("lotr:nan_ungol_banner"),
            Identifier.parse("lotr:near_harad_banner"),
            Identifier.parse("lotr:near_harad_brick"),
            Identifier.parse("lotr:near_harad_brick_slab"),
            Identifier.parse("lotr:near_harad_brick_stairs"),
            Identifier.parse("lotr:near_harad_brick_wall"),
            Identifier.parse("lotr:near_harad_carved_brick"),
            Identifier.parse("lotr:near_harad_cracked_brick_slab"),
            Identifier.parse("lotr:near_harad_cracked_brick_stairs"),
            Identifier.parse("lotr:near_harad_cracked_brick_wall"),
            Identifier.parse("lotr:near_harad_crafting_table"),
            Identifier.parse("lotr:near_harad_lapis_brick"),
            Identifier.parse("lotr:near_harad_pillar"),
            Identifier.parse("lotr:near_harad_pillar_slab"),
            Identifier.parse("lotr:near_harad_red_brick_slab"),
            Identifier.parse("lotr:near_harad_red_brick_stairs"),
            Identifier.parse("lotr:near_harad_red_brick_wall"),
            Identifier.parse("lotr:near_harad_red_carved_brick"),
            Identifier.parse("lotr:near_harad_red_cracked_brick_slab"),
            Identifier.parse("lotr:near_harad_red_cracked_brick_stairs"),
            Identifier.parse("lotr:near_harad_red_cracked_brick_wall"),
            Identifier.parse("lotr:near_harad_red_pillar_slab"),
            Identifier.parse("lotr:numenorean_brick"),
            Identifier.parse("lotr:numenorean_brick_slab"),
            Identifier.parse("lotr:numenorean_brick_stairs"),
            Identifier.parse("lotr:numenorean_brick_wall"),
            Identifier.parse("lotr:numenorean_pillar"),
            Identifier.parse("lotr:numenorean_pillar_slab"),
            Identifier.parse("lotr:obsidian_dwarven_brick_slab"),
            Identifier.parse("lotr:obsidian_dwarven_brick_stairs"),
            Identifier.parse("lotr:obsidian_dwarven_brick_wall"),
            Identifier.parse("lotr:obsidian_tauredain_dart_trap"),
            Identifier.parse("lotr:orc_bed"),
            Identifier.parse("lotr:orc_fire_bomb"),
            Identifier.parse("lotr:orc_forge"),
            Identifier.parse("lotr:orc_plating_rust"),
            Identifier.parse("lotr:orc_skull_staff"),
            Identifier.parse("lotr:orc_torch"),
            Identifier.parse("lotr:pelargir_banner"),
            Identifier.parse("lotr:pinnath_gelin_banner"),
            Identifier.parse("lotr:ranger_banner"),
            Identifier.parse("lotr:ranger_boots"),
            Identifier.parse("lotr:ranger_crafting_table"),
            Identifier.parse("lotr:ranger_hood"),
            Identifier.parse("lotr:ranger_leggings"),
            Identifier.parse("lotr:ranger_tunic"),
            Identifier.parse("lotr:rhudaur_banner"),
            Identifier.parse("lotr:rhun_banner"),
            Identifier.parse("lotr:rhun_brick_slab"),
            Identifier.parse("lotr:rhun_brick_stairs"),
            Identifier.parse("lotr:rhun_brick_wall"),
            Identifier.parse("lotr:rhun_carved_brick"),
            Identifier.parse("lotr:rhun_cracked_brick_slab"),
            Identifier.parse("lotr:rhun_cracked_brick_stairs"),
            Identifier.parse("lotr:rhun_cracked_brick_wall"),
            Identifier.parse("lotr:rhun_crafting_table"),
            Identifier.parse("lotr:rhun_flowers_brick"),
            Identifier.parse("lotr:rhun_flowers_brick_slab"),
            Identifier.parse("lotr:rhun_flowers_brick_stairs"),
            Identifier.parse("lotr:rhun_flowers_brick_wall"),
            Identifier.parse("lotr:rhun_gold_brick"),
            Identifier.parse("lotr:rhun_mossy_brick"),
            Identifier.parse("lotr:rhun_mossy_brick_slab"),
            Identifier.parse("lotr:rhun_mossy_brick_stairs"),
            Identifier.parse("lotr:rhun_mossy_brick_wall"),
            Identifier.parse("lotr:rhun_pillar_slab"),
            Identifier.parse("lotr:rhun_red_brick"),
            Identifier.parse("lotr:rhun_red_brick_slab"),
            Identifier.parse("lotr:rhun_red_brick_stairs"),
            Identifier.parse("lotr:rhun_red_brick_wall"),
            Identifier.parse("lotr:rhun_red_carved_brick"),
            Identifier.parse("lotr:rhun_red_pillar"),
            Identifier.parse("lotr:rhun_red_pillar_slab"),
            Identifier.parse("lotr:rivendell_banner"),
            Identifier.parse("lotr:rivendell_crafting_table"),
            Identifier.parse("lotr:rohan_banner"),
            Identifier.parse("lotr:rohan_beam"),
            Identifier.parse("lotr:rohan_brick"),
            Identifier.parse("lotr:rohan_brick_slab"),
            Identifier.parse("lotr:rohan_brick_stairs"),
            Identifier.parse("lotr:rohan_brick_wall"),
            Identifier.parse("lotr:rohan_gold_beam"),
            Identifier.parse("lotr:rohan_pillar"),
            Identifier.parse("lotr:rohan_pillar_slab"),
            Identifier.parse("lotr:rohan_rock_slab"),
            Identifier.parse("lotr:rohan_rock_stairs"),
            Identifier.parse("lotr:rohan_rock_wall"),
            Identifier.parse("lotr:rohirric_battleaxe"),
            Identifier.parse("lotr:rohirric_boots"),
            Identifier.parse("lotr:rohirric_bow"),
            Identifier.parse("lotr:rohirric_coif"),
            Identifier.parse("lotr:rohirric_crafting_table"),
            Identifier.parse("lotr:rohirric_dagger"),
            Identifier.parse("lotr:rohirric_hauberk"),
            Identifier.parse("lotr:rohirric_horse_armor"),
            Identifier.parse("lotr:rohirric_leggings"),
            Identifier.parse("lotr:rohirric_marshal_boots"),
            Identifier.parse("lotr:rohirric_marshal_chestplate"),
            Identifier.parse("lotr:rohirric_marshal_helmet"),
            Identifier.parse("lotr:rohirric_marshal_leggings"),
            Identifier.parse("lotr:rohirric_spear"),
            Identifier.parse("lotr:rohirric_sword"),
            Identifier.parse("lotr:smooth_gondor_rock"),
            Identifier.parse("lotr:smooth_gondor_rock_slab"),
            Identifier.parse("lotr:smooth_mordor_rock"),
            Identifier.parse("lotr:smooth_mordor_rock_slab"),
            Identifier.parse("lotr:smooth_rohan_rock"),
            Identifier.parse("lotr:smooth_rohan_rock_slab"),
            Identifier.parse("lotr:taur_gold_pillar"),
            Identifier.parse("lotr:taur_gold_pillar_slab"),
            Identifier.parse("lotr:taur_obsidian_pillar_slab"),
            Identifier.parse("lotr:tauredain_banner"),
            Identifier.parse("lotr:tauredain_brick"),
            Identifier.parse("lotr:tauredain_brick_slab"),
            Identifier.parse("lotr:tauredain_brick_stairs"),
            Identifier.parse("lotr:tauredain_brick_wall"),
            Identifier.parse("lotr:tauredain_cracked_brick_slab"),
            Identifier.parse("lotr:tauredain_cracked_brick_stairs"),
            Identifier.parse("lotr:tauredain_cracked_brick_wall"),
            Identifier.parse("lotr:tauredain_crafting_table"),
            Identifier.parse("lotr:tauredain_dart_trap"),
            Identifier.parse("lotr:tauredain_double_torch"),
            Identifier.parse("lotr:tauredain_gold_brick"),
            Identifier.parse("lotr:tauredain_gold_brick_slab"),
            Identifier.parse("lotr:tauredain_gold_brick_stairs"),
            Identifier.parse("lotr:tauredain_gold_brick_wall"),
            Identifier.parse("lotr:tauredain_mossy_brick"),
            Identifier.parse("lotr:tauredain_mossy_brick_slab"),
            Identifier.parse("lotr:tauredain_mossy_brick_stairs"),
            Identifier.parse("lotr:tauredain_mossy_brick_wall"),
            Identifier.parse("lotr:tauredain_obsidian_brick_slab"),
            Identifier.parse("lotr:tauredain_obsidian_brick_stairs"),
            Identifier.parse("lotr:tauredain_obsidian_brick_wall"),
            Identifier.parse("lotr:tauredain_pillar"),
            Identifier.parse("lotr:tauredain_pillar_slab"),
            Identifier.parse("lotr:taurethrim_blowgun"),
            Identifier.parse("lotr:triple_strength_orc_fire_bomb"),
            Identifier.parse("lotr:umbar_banner"),
            Identifier.parse("lotr:umbar_brick"),
            Identifier.parse("lotr:umbar_brick_slab"),
            Identifier.parse("lotr:umbar_brick_stairs"),
            Identifier.parse("lotr:umbar_brick_wall"),
            Identifier.parse("lotr:umbar_crafting_table"),
            Identifier.parse("lotr:umbar_pillar"),
            Identifier.parse("lotr:umbar_pillar_slab"),
            Identifier.parse("lotr:umbaric_dagger"),
            Identifier.parse("lotr:umbaric_horse_armor"),
            Identifier.parse("lotr:umbaric_mace"),
            Identifier.parse("lotr:umbaric_poleaxe"),
            Identifier.parse("lotr:umbaric_scimitar"),
            Identifier.parse("lotr:umbaric_spear"),
            Identifier.parse("lotr:uruk_brick"),
            Identifier.parse("lotr:uruk_brick_slab"),
            Identifier.parse("lotr:uruk_brick_stairs"),
            Identifier.parse("lotr:uruk_brick_wall"),
            Identifier.parse("lotr:uruk_crafting_table"),
            Identifier.parse("lotr:uruk_pillar"),
            Identifier.parse("lotr:uruk_pillar_slab"),
            Identifier.parse("lotr:wood_elf_wood_bars"),
            Identifier.parse("lotr:wood_elven_bed"),
            Identifier.parse("lotr:wood_elven_brick"),
            Identifier.parse("lotr:wood_elven_brick_slab"),
            Identifier.parse("lotr:wood_elven_brick_stairs"),
            Identifier.parse("lotr:wood_elven_brick_wall"),
            Identifier.parse("lotr:wood_elven_chandelier"),
            Identifier.parse("lotr:wood_elven_crafting_table"),
            Identifier.parse("lotr:wood_elven_gold_brick"),
            Identifier.parse("lotr:wood_elven_pillar"),
            Identifier.parse("lotr:wood_elven_pillar_slab"),
            Identifier.parse("lotr:wood_elven_scout_boots"),
            Identifier.parse("lotr:wood_elven_scout_hood"),
            Identifier.parse("lotr:wood_elven_scout_leggings"),
            Identifier.parse("lotr:wood_elven_scout_tunic"),
            Identifier.parse("lotr:wood_elven_torch"));

    LOTRTranscribedRecipes(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    public void buildRecipes() {
        restoredRecipes();
        // LOTRRecipes.createAngmarRecipes
        shaped(ANGMAR, "angmar/angmar_brick", "lotr:angmar_brick", 4, rows("XX", "XX"), 'X', "minecraft:stone");
        shaped(ANGMAR, "angmar/angmar_crafting_table", "lotr:angmar_crafting_table", 1, rows("XX", "YY"), 'X', "#minecraft:planks", 'Y', "lotr:angmar_brick");
        shaped(ANGMAR, "angmar/angmar_brick_slab", "lotr:angmar_brick_slab", 6, rows("XXX"), 'X', "lotr:angmar_brick");
        shaped(ANGMAR, "angmar/angmar_brick_stairs", "lotr:angmar_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:angmar_brick");
        shaped(ANGMAR, "angmar/angmar_brick_wall", "lotr:angmar_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:angmar_brick");
        shaped(ANGMAR, "angmar/orc_torch", "lotr:orc_torch", 2, rows("X", "Y", "Y"), 'X', "#minecraft:coals", 'Y', "#lotr:sticks");
        shaped(ANGMAR, "angmar/cracked_angmar_brick_slab", "lotr:cracked_angmar_brick_slab", 6, rows("XXX"), 'X', "lotr:cracked_angmar_brick");
        shaped(ANGMAR, "angmar/cracked_angmar_brick_stairs", "lotr:cracked_angmar_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:cracked_angmar_brick");
        shaped(ANGMAR, "angmar/cracked_angmar_brick_wall", "lotr:cracked_angmar_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:cracked_angmar_brick");
        shaped(ANGMAR, "angmar/orc_forge", "lotr:orc_forge", 1, rows("XXX", "X X", "XXX"), 'X', "lotr:angmar_brick");
        shaped(ANGMAR, "angmar/angmar_banner", "lotr:angmar_banner", 1, rows("X", "Y", "Z"), 'X', "#minecraft:wool", 'Y', "#lotr:sticks", 'Z', "#minecraft:planks");
        shaped(ANGMAR, "angmar/angmar_pillar", "lotr:angmar_pillar", 3, rows("X", "X", "X"), 'X', "minecraft:stone");
        shaped(ANGMAR, "angmar/angmar_pillar_slab", "lotr:angmar_pillar_slab", 6, rows("XXX"), 'X', "lotr:angmar_pillar");
        shaped(ANGMAR, "angmar/rhudaur_banner", "lotr:rhudaur_banner", 1, rows("XA", "Y ", "Z "), 'X', "#minecraft:wool", 'Y', "#lotr:sticks", 'Z', "#minecraft:planks", 'A', "#lotr:sticks");
        shapeless(ANGMAR, "angmar/angmar_snow_brick", "lotr:angmar_snow_brick", 1, "lotr:angmar_brick", "minecraft:snowball");
        shaped(ANGMAR, "angmar/angmar_snow_brick_slab", "lotr:angmar_snow_brick_slab", 6, rows("XXX"), 'X', "lotr:angmar_snow_brick");
        shaped(ANGMAR, "angmar/angmar_snow_brick_stairs", "lotr:angmar_snow_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:angmar_snow_brick");
        shaped(ANGMAR, "angmar/angmar_snow_brick_wall", "lotr:angmar_snow_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:angmar_snow_brick");
        // LOTRRecipes.createBlueMountainsRecipes
        shaped(BLUE_DWARVEN, "blue_dwarven/blue_dwarven_crafting_table", "lotr:blue_dwarven_crafting_table", 1, rows("XX", "YY"), 'X', "#minecraft:planks", 'Y', "lotr:blue_rock_brick");
        shaped(BLUE_DWARVEN, "blue_dwarven/blue_mountains_banner", "lotr:blue_mountains_banner", 1, rows("X", "Y", "Z"), 'X', "#minecraft:wool", 'Y', "#lotr:sticks", 'Z', "#minecraft:planks");
        // LOTRRecipes.createBreeRecipes
        shaped(BREE, "bree/bree_crafting_table", "lotr:bree_crafting_table", 1, rows("XX", "XX"), 'X', "#minecraft:planks");
        shaped(BREE, "bree/bree_banner", "lotr:bree_banner", 1, rows("X", "Y", "Z"), 'X', "#minecraft:wool", 'Y', "#lotr:sticks", 'Z', "#minecraft:planks");
        // LOTRRecipes.createCommonDwarfRecipes
        shaped(DWARVEN, "dwarven/dwarven_brick", "lotr:dwarven_brick", 4, rows("XX", "XX"), 'X', "minecraft:stone");
        shaped(BLUE_DWARVEN, "blue_dwarven/dwarven_brick", "lotr:dwarven_brick", 4, rows("XX", "XX"), 'X', "minecraft:stone");
        shaped(DWARVEN, "dwarven/dwarven_brick_stairs", "lotr:dwarven_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:dwarven_brick");
        shaped(BLUE_DWARVEN, "blue_dwarven/dwarven_brick_stairs", "lotr:dwarven_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:dwarven_brick");
        shaped(DWARVEN, "dwarven/dwarven_brick_slab", "lotr:dwarven_brick_slab", 6, rows("XXX"), 'X', "lotr:dwarven_brick");
        shaped(BLUE_DWARVEN, "blue_dwarven/dwarven_brick_slab", "lotr:dwarven_brick_slab", 6, rows("XXX"), 'X', "lotr:dwarven_brick");
        shaped(DWARVEN, "dwarven/dwarven_brick_wall", "lotr:dwarven_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:dwarven_brick");
        shaped(BLUE_DWARVEN, "blue_dwarven/dwarven_brick_wall", "lotr:dwarven_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:dwarven_brick");
        shaped(DWARVEN, "dwarven/dwarven_pillar", "lotr:dwarven_pillar", 3, rows("X", "X", "X"), 'X', "minecraft:stone");
        shaped(BLUE_DWARVEN, "blue_dwarven/dwarven_pillar", "lotr:dwarven_pillar", 3, rows("X", "X", "X"), 'X', "minecraft:stone");
        shaped(DWARVEN, "dwarven/dwarven_forge", "lotr:dwarven_forge", 1, rows("XXX", "X X", "XXX"), 'X', "lotr:dwarven_brick");
        shaped(BLUE_DWARVEN, "blue_dwarven/dwarven_forge", "lotr:dwarven_forge", 1, rows("XXX", "X X", "XXX"), 'X', "lotr:dwarven_brick");
        shaped(DWARVEN, "dwarven/dwarven_pillar_slab", "lotr:dwarven_pillar_slab", 6, rows("XXX"), 'X', "lotr:dwarven_pillar");
        shaped(BLUE_DWARVEN, "blue_dwarven/dwarven_pillar_slab", "lotr:dwarven_pillar_slab", 6, rows("XXX"), 'X', "lotr:dwarven_pillar");
        shaped(DWARVEN, "dwarven/dwarven_door", "lotr:dwarven_door", 1, rows("XX", "XX", "XX"), 'X', "minecraft:stone");
        shaped(BLUE_DWARVEN, "blue_dwarven/dwarven_door", "lotr:dwarven_door", 1, rows("XX", "XX", "XX"), 'X', "minecraft:stone");
        shaped(DWARVEN, "dwarven/dwarven_bed", "lotr:dwarven_bed", 1, rows("XXX", "YYY"), 'X', "#minecraft:wool", 'Y', "#minecraft:planks");
        shaped(BLUE_DWARVEN, "blue_dwarven/dwarven_bed", "lotr:dwarven_bed", 1, rows("XXX", "YYY"), 'X', "#minecraft:wool", 'Y', "#minecraft:planks");
        shaped(DWARVEN, "dwarven/dwarven_gold_brick", "lotr:dwarven_gold_brick", 1, rows(" X ", "XYX", " X "), 'X', "minecraft:gold_nugget", 'Y', "lotr:dwarven_brick");
        shaped(BLUE_DWARVEN, "blue_dwarven/dwarven_gold_brick", "lotr:dwarven_gold_brick", 1, rows(" X ", "XYX", " X "), 'X', "minecraft:gold_nugget", 'Y', "lotr:dwarven_brick");
        shaped(DWARVEN, "dwarven/carved_dwarven_brick", "lotr:carved_dwarven_brick", 1, rows("XX", "XX"), 'X', "lotr:dwarven_brick");
        shaped(BLUE_DWARVEN, "blue_dwarven/carved_dwarven_brick", "lotr:carved_dwarven_brick", 1, rows("XX", "XX"), 'X', "lotr:dwarven_brick");
        shaped(DWARVEN, "dwarven/glowing_dwarven_brick", "lotr:glowing_dwarven_brick", 1, rows(" X ", "XYX", " X "), 'X', "minecraft:glowstone_dust", 'Y', "lotr:dwarven_brick");
        shaped(BLUE_DWARVEN, "blue_dwarven/glowing_dwarven_brick", "lotr:glowing_dwarven_brick", 1, rows(" X ", "XYX", " X "), 'X', "minecraft:glowstone_dust", 'Y', "lotr:dwarven_brick");
        shaped(DWARVEN, "dwarven/cracked_dwarven_brick_stairs", "lotr:cracked_dwarven_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:cracked_dwarven_brick");
        shaped(BLUE_DWARVEN, "blue_dwarven/cracked_dwarven_brick_stairs", "lotr:cracked_dwarven_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:cracked_dwarven_brick");
        shaped(DWARVEN, "dwarven/cracked_dwarven_brick_slab", "lotr:cracked_dwarven_brick_slab", 6, rows("XXX"), 'X', "lotr:cracked_dwarven_brick");
        shaped(BLUE_DWARVEN, "blue_dwarven/cracked_dwarven_brick_slab", "lotr:cracked_dwarven_brick_slab", 6, rows("XXX"), 'X', "lotr:cracked_dwarven_brick");
        shaped(DWARVEN, "dwarven/cracked_dwarven_brick_wall", "lotr:cracked_dwarven_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:cracked_dwarven_brick");
        shaped(BLUE_DWARVEN, "blue_dwarven/cracked_dwarven_brick_wall", "lotr:cracked_dwarven_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:cracked_dwarven_brick");
        shaped(DWARVEN, "dwarven/cracked_dwarven_pillar_slab", "lotr:cracked_dwarven_pillar_slab", 6, rows("XXX"), 'X', "lotr:cracked_dwarven_pillar");
        shaped(BLUE_DWARVEN, "blue_dwarven/cracked_dwarven_pillar_slab", "lotr:cracked_dwarven_pillar_slab", 6, rows("XXX"), 'X', "lotr:cracked_dwarven_pillar");
        shaped(DWARVEN, "dwarven/obsidian_dwarven_brick_stairs", "lotr:obsidian_dwarven_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:obsidian_dwarven_brick");
        shaped(BLUE_DWARVEN, "blue_dwarven/obsidian_dwarven_brick_stairs", "lotr:obsidian_dwarven_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:obsidian_dwarven_brick");
        shaped(DWARVEN, "dwarven/obsidian_dwarven_brick_slab", "lotr:obsidian_dwarven_brick_slab", 6, rows("XXX"), 'X', "lotr:obsidian_dwarven_brick");
        shaped(BLUE_DWARVEN, "blue_dwarven/obsidian_dwarven_brick_slab", "lotr:obsidian_dwarven_brick_slab", 6, rows("XXX"), 'X', "lotr:obsidian_dwarven_brick");
        shaped(DWARVEN, "dwarven/obsidian_dwarven_brick_wall", "lotr:obsidian_dwarven_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:obsidian_dwarven_brick");
        shaped(BLUE_DWARVEN, "blue_dwarven/obsidian_dwarven_brick_wall", "lotr:obsidian_dwarven_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:obsidian_dwarven_brick");
        // LOTRRecipes.createCommonHighElfRecipes
        shaped(HIGH_ELVEN, "high_elven/high_elven_torch", "lotr:high_elven_torch", 4, rows("X", "Y"), 'X', "lotr:edhelvir", 'Y', "#lotr:sticks");
        shaped(RIVENDELL, "rivendell/high_elven_torch", "lotr:high_elven_torch", 4, rows("X", "Y"), 'X', "lotr:edhelvir", 'Y', "#lotr:sticks");
        shaped(HIGH_ELVEN, "high_elven/high_elven_bed", "lotr:high_elven_bed", 1, rows("XXX", "YYY"), 'X', "#minecraft:wool", 'Y', "#minecraft:planks");
        shaped(RIVENDELL, "rivendell/high_elven_bed", "lotr:high_elven_bed", 1, rows("XXX", "YYY"), 'X', "#minecraft:wool", 'Y', "#minecraft:planks");
        shaped(HIGH_ELVEN, "high_elven/high_elven_brick", "lotr:high_elven_brick", 4, rows("XX", "XX"), 'X', "minecraft:stone");
        shaped(RIVENDELL, "rivendell/high_elven_brick", "lotr:high_elven_brick", 4, rows("XX", "XX"), 'X', "minecraft:stone");
        shapeless(HIGH_ELVEN, "high_elven/mossy_high_elven_brick", "lotr:mossy_high_elven_brick", 1, "lotr:high_elven_brick", "#lotr:vines");
        shapeless(RIVENDELL, "rivendell/mossy_high_elven_brick", "lotr:mossy_high_elven_brick", 1, "lotr:high_elven_brick", "#lotr:vines");
        shaped(HIGH_ELVEN, "high_elven/high_elven_brick_slab", "lotr:high_elven_brick_slab", 6, rows("XXX"), 'X', "lotr:high_elven_brick");
        shaped(RIVENDELL, "rivendell/high_elven_brick_slab", "lotr:high_elven_brick_slab", 6, rows("XXX"), 'X', "lotr:high_elven_brick");
        shaped(HIGH_ELVEN, "high_elven/mossy_high_elven_brick_slab", "lotr:mossy_high_elven_brick_slab", 6, rows("XXX"), 'X', "lotr:mossy_high_elven_brick");
        shaped(RIVENDELL, "rivendell/mossy_high_elven_brick_slab", "lotr:mossy_high_elven_brick_slab", 6, rows("XXX"), 'X', "lotr:mossy_high_elven_brick");
        shaped(HIGH_ELVEN, "high_elven/cracked_high_elven_brick_slab", "lotr:cracked_high_elven_brick_slab", 6, rows("XXX"), 'X', "lotr:cracked_high_elven_brick");
        shaped(RIVENDELL, "rivendell/cracked_high_elven_brick_slab", "lotr:cracked_high_elven_brick_slab", 6, rows("XXX"), 'X', "lotr:cracked_high_elven_brick");
        shaped(HIGH_ELVEN, "high_elven/high_elven_brick_stairs", "lotr:high_elven_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:high_elven_brick");
        shaped(RIVENDELL, "rivendell/high_elven_brick_stairs", "lotr:high_elven_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:high_elven_brick");
        shaped(HIGH_ELVEN, "high_elven/mossy_high_elven_brick_stairs", "lotr:mossy_high_elven_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:mossy_high_elven_brick");
        shaped(RIVENDELL, "rivendell/mossy_high_elven_brick_stairs", "lotr:mossy_high_elven_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:mossy_high_elven_brick");
        shaped(HIGH_ELVEN, "high_elven/cracked_high_elven_brick_stairs", "lotr:cracked_high_elven_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:cracked_high_elven_brick");
        shaped(RIVENDELL, "rivendell/cracked_high_elven_brick_stairs", "lotr:cracked_high_elven_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:cracked_high_elven_brick");
        shaped(HIGH_ELVEN, "high_elven/high_elven_brick_wall", "lotr:high_elven_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:high_elven_brick");
        shaped(RIVENDELL, "rivendell/high_elven_brick_wall", "lotr:high_elven_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:high_elven_brick");
        shaped(HIGH_ELVEN, "high_elven/mossy_high_elven_brick_wall", "lotr:mossy_high_elven_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:mossy_high_elven_brick");
        shaped(RIVENDELL, "rivendell/mossy_high_elven_brick_wall", "lotr:mossy_high_elven_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:mossy_high_elven_brick");
        shaped(HIGH_ELVEN, "high_elven/cracked_high_elven_brick_wall", "lotr:cracked_high_elven_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:cracked_high_elven_brick");
        shaped(RIVENDELL, "rivendell/cracked_high_elven_brick_wall", "lotr:cracked_high_elven_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:cracked_high_elven_brick");
        shaped(HIGH_ELVEN, "high_elven/high_elven_pillar", "lotr:high_elven_pillar", 3, rows("X", "X", "X"), 'X', "minecraft:stone");
        shaped(RIVENDELL, "rivendell/high_elven_pillar", "lotr:high_elven_pillar", 3, rows("X", "X", "X"), 'X', "minecraft:stone");
        shaped(HIGH_ELVEN, "high_elven/high_elven_pillar_slab", "lotr:high_elven_pillar_slab", 6, rows("XXX"), 'X', "lotr:high_elven_pillar");
        shaped(RIVENDELL, "rivendell/high_elven_pillar_slab", "lotr:high_elven_pillar_slab", 6, rows("XXX"), 'X', "lotr:high_elven_pillar");
        shaped(HIGH_ELVEN, "high_elven/cracked_high_elven_pillar_slab", "lotr:cracked_high_elven_pillar_slab", 6, rows("XXX"), 'X', "lotr:cracked_high_elven_pillar");
        shaped(RIVENDELL, "rivendell/cracked_high_elven_pillar_slab", "lotr:cracked_high_elven_pillar_slab", 6, rows("XXX"), 'X', "lotr:cracked_high_elven_pillar");
        shaped(HIGH_ELVEN, "high_elven/carved_high_elven_brick", "lotr:carved_high_elven_brick", 1, rows("XX", "XX"), 'X', "lotr:high_elven_brick");
        shaped(RIVENDELL, "rivendell/carved_high_elven_brick", "lotr:carved_high_elven_brick", 1, rows("XX", "XX"), 'X', "lotr:high_elven_brick");
        shaped(HIGH_ELVEN, "high_elven/elven_forge", "lotr:elven_forge", 1, rows("XXX", "X X", "XXX"), 'X', "lotr:high_elven_brick");
        shaped(RIVENDELL, "rivendell/elven_forge", "lotr:elven_forge", 1, rows("XXX", "X X", "XXX"), 'X', "lotr:high_elven_brick");
        shaped(HIGH_ELVEN, "high_elven/high_elf_wood_bars", "lotr:high_elf_wood_bars", 8, rows("XXX", "XXX"), 'X', "#lotr:sticks");
        shaped(RIVENDELL, "rivendell/high_elf_wood_bars", "lotr:high_elf_wood_bars", 8, rows("XXX", "XXX"), 'X', "#lotr:sticks");
        shaped(HIGH_ELVEN, "high_elven/high_elven_gold_brick", "lotr:high_elven_gold_brick", 1, rows(" X ", "XYX", " X "), 'X', "minecraft:gold_nugget", 'Y', "lotr:high_elven_brick");
        shaped(RIVENDELL, "rivendell/high_elven_gold_brick", "lotr:high_elven_gold_brick", 1, rows(" X ", "XYX", " X "), 'X', "minecraft:gold_nugget", 'Y', "lotr:high_elven_brick");
        // LOTRRecipes.createCommonHobbitRecipes
        shaped(HOBBIT, "hobbit/hobbit_oven", "lotr:hobbit_oven", 1, rows("XXX", "X X", "XXX"), 'X', "minecraft:bricks");
        shaped(BREE, "bree/hobbit_oven", "lotr:hobbit_oven", 1, rows("XXX", "X X", "XXX"), 'X', "minecraft:bricks");
        // LOTRRecipes.createCommonMorgulRecipes
        shaped(MORGUL, "morgul/morgul_torch", "lotr:morgul_torch", 4, rows("X", "Y"), 'X', "lotr:gulduril", 'Y', "#lotr:sticks");
        shaped(ANGMAR, "angmar/morgul_torch", "lotr:morgul_torch", 4, rows("X", "Y"), 'X', "lotr:gulduril", 'Y', "#lotr:sticks");
        shaped(DOL_GULDUR, "dol_guldur/morgul_torch", "lotr:morgul_torch", 4, rows("X", "Y"), 'X', "lotr:gulduril", 'Y', "#lotr:sticks");
        // LOTRRecipes.createCommonNearHaradRecipes
        shaped(NEAR_HARAD, "near_harad/near_harad_brick", "lotr:near_harad_brick", 4, rows("XX", "XX"), 'X', "minecraft:sandstone");
        shaped(UMBAR, "umbar/near_harad_brick", "lotr:near_harad_brick", 4, rows("XX", "XX"), 'X', "minecraft:sandstone");
        shaped(GULF, "gulf/near_harad_brick", "lotr:near_harad_brick", 4, rows("XX", "XX"), 'X', "minecraft:sandstone");
        shaped(NEAR_HARAD, "near_harad/near_harad_brick_slab", "lotr:near_harad_brick_slab", 6, rows("XXX"), 'X', "lotr:near_harad_brick");
        shaped(UMBAR, "umbar/near_harad_brick_slab", "lotr:near_harad_brick_slab", 6, rows("XXX"), 'X', "lotr:near_harad_brick");
        shaped(GULF, "gulf/near_harad_brick_slab", "lotr:near_harad_brick_slab", 6, rows("XXX"), 'X', "lotr:near_harad_brick");
        shaped(NEAR_HARAD, "near_harad/near_harad_brick_stairs", "lotr:near_harad_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:near_harad_brick");
        shaped(UMBAR, "umbar/near_harad_brick_stairs", "lotr:near_harad_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:near_harad_brick");
        shaped(GULF, "gulf/near_harad_brick_stairs", "lotr:near_harad_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:near_harad_brick");
        shaped(NEAR_HARAD, "near_harad/near_harad_brick_wall", "lotr:near_harad_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:near_harad_brick");
        shaped(UMBAR, "umbar/near_harad_brick_wall", "lotr:near_harad_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:near_harad_brick");
        shaped(GULF, "gulf/near_harad_brick_wall", "lotr:near_harad_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:near_harad_brick");
        shaped(NEAR_HARAD, "near_harad/near_harad_pillar", "lotr:near_harad_pillar", 3, rows("X", "X", "X"), 'X', "minecraft:sandstone");
        shaped(UMBAR, "umbar/near_harad_pillar", "lotr:near_harad_pillar", 3, rows("X", "X", "X"), 'X', "minecraft:sandstone");
        shaped(GULF, "gulf/near_harad_pillar", "lotr:near_harad_pillar", 3, rows("X", "X", "X"), 'X', "minecraft:sandstone");
        shaped(NEAR_HARAD, "near_harad/near_harad_pillar_slab", "lotr:near_harad_pillar_slab", 6, rows("XXX"), 'X', "lotr:near_harad_pillar");
        shaped(UMBAR, "umbar/near_harad_pillar_slab", "lotr:near_harad_pillar_slab", 6, rows("XXX"), 'X', "lotr:near_harad_pillar");
        shaped(GULF, "gulf/near_harad_pillar_slab", "lotr:near_harad_pillar_slab", 6, rows("XXX"), 'X', "lotr:near_harad_pillar");
        shaped(NEAR_HARAD, "near_harad/near_harad_carved_brick", "lotr:near_harad_carved_brick", 1, rows("XX", "XX"), 'X', "lotr:near_harad_brick");
        shaped(UMBAR, "umbar/near_harad_carved_brick", "lotr:near_harad_carved_brick", 1, rows("XX", "XX"), 'X', "lotr:near_harad_brick");
        shaped(GULF, "gulf/near_harad_carved_brick", "lotr:near_harad_carved_brick", 1, rows("XX", "XX"), 'X', "lotr:near_harad_brick");
        shaped(NEAR_HARAD, "near_harad/near_harad_cracked_brick_slab", "lotr:near_harad_cracked_brick_slab", 6, rows("XXX"), 'X', "lotr:near_harad_cracked_brick");
        shaped(UMBAR, "umbar/near_harad_cracked_brick_slab", "lotr:near_harad_cracked_brick_slab", 6, rows("XXX"), 'X', "lotr:near_harad_cracked_brick");
        shaped(GULF, "gulf/near_harad_cracked_brick_slab", "lotr:near_harad_cracked_brick_slab", 6, rows("XXX"), 'X', "lotr:near_harad_cracked_brick");
        shaped(NEAR_HARAD, "near_harad/near_harad_cracked_brick_stairs", "lotr:near_harad_cracked_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:near_harad_cracked_brick");
        shaped(UMBAR, "umbar/near_harad_cracked_brick_stairs", "lotr:near_harad_cracked_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:near_harad_cracked_brick");
        shaped(GULF, "gulf/near_harad_cracked_brick_stairs", "lotr:near_harad_cracked_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:near_harad_cracked_brick");
        shaped(NEAR_HARAD, "near_harad/near_harad_cracked_brick_wall", "lotr:near_harad_cracked_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:near_harad_cracked_brick");
        shaped(UMBAR, "umbar/near_harad_cracked_brick_wall", "lotr:near_harad_cracked_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:near_harad_cracked_brick");
        shaped(GULF, "gulf/near_harad_cracked_brick_wall", "lotr:near_harad_cracked_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:near_harad_cracked_brick");
        shaped(NEAR_HARAD, "near_harad/near_harad_red_brick_slab", "lotr:near_harad_red_brick_slab", 6, rows("XXX"), 'X', "lotr:near_harad_red_brick");
        shaped(UMBAR, "umbar/near_harad_red_brick_slab", "lotr:near_harad_red_brick_slab", 6, rows("XXX"), 'X', "lotr:near_harad_red_brick");
        shaped(GULF, "gulf/near_harad_red_brick_slab", "lotr:near_harad_red_brick_slab", 6, rows("XXX"), 'X', "lotr:near_harad_red_brick");
        shaped(NEAR_HARAD, "near_harad/near_harad_red_brick_stairs", "lotr:near_harad_red_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:near_harad_red_brick");
        shaped(UMBAR, "umbar/near_harad_red_brick_stairs", "lotr:near_harad_red_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:near_harad_red_brick");
        shaped(GULF, "gulf/near_harad_red_brick_stairs", "lotr:near_harad_red_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:near_harad_red_brick");
        shaped(NEAR_HARAD, "near_harad/near_harad_red_brick_wall", "lotr:near_harad_red_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:near_harad_red_brick");
        shaped(UMBAR, "umbar/near_harad_red_brick_wall", "lotr:near_harad_red_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:near_harad_red_brick");
        shaped(GULF, "gulf/near_harad_red_brick_wall", "lotr:near_harad_red_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:near_harad_red_brick");
        shaped(NEAR_HARAD, "near_harad/near_harad_red_carved_brick", "lotr:near_harad_red_carved_brick", 1, rows("XX", "XX"), 'X', "lotr:near_harad_red_brick");
        shaped(UMBAR, "umbar/near_harad_red_carved_brick", "lotr:near_harad_red_carved_brick", 1, rows("XX", "XX"), 'X', "lotr:near_harad_red_brick");
        shaped(GULF, "gulf/near_harad_red_carved_brick", "lotr:near_harad_red_carved_brick", 1, rows("XX", "XX"), 'X', "lotr:near_harad_red_brick");
        shaped(NEAR_HARAD, "near_harad/near_harad_red_cracked_brick_slab", "lotr:near_harad_red_cracked_brick_slab", 6, rows("XXX"), 'X', "lotr:near_harad_red_cracked_brick");
        shaped(UMBAR, "umbar/near_harad_red_cracked_brick_slab", "lotr:near_harad_red_cracked_brick_slab", 6, rows("XXX"), 'X', "lotr:near_harad_red_cracked_brick");
        shaped(GULF, "gulf/near_harad_red_cracked_brick_slab", "lotr:near_harad_red_cracked_brick_slab", 6, rows("XXX"), 'X', "lotr:near_harad_red_cracked_brick");
        shaped(NEAR_HARAD, "near_harad/near_harad_red_cracked_brick_stairs", "lotr:near_harad_red_cracked_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:near_harad_red_cracked_brick");
        shaped(UMBAR, "umbar/near_harad_red_cracked_brick_stairs", "lotr:near_harad_red_cracked_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:near_harad_red_cracked_brick");
        shaped(GULF, "gulf/near_harad_red_cracked_brick_stairs", "lotr:near_harad_red_cracked_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:near_harad_red_cracked_brick");
        shaped(NEAR_HARAD, "near_harad/near_harad_red_cracked_brick_wall", "lotr:near_harad_red_cracked_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:near_harad_red_cracked_brick");
        shaped(UMBAR, "umbar/near_harad_red_cracked_brick_wall", "lotr:near_harad_red_cracked_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:near_harad_red_cracked_brick");
        shaped(GULF, "gulf/near_harad_red_cracked_brick_wall", "lotr:near_harad_red_cracked_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:near_harad_red_cracked_brick");
        shaped(NEAR_HARAD, "near_harad/near_harad_red_pillar_slab", "lotr:near_harad_red_pillar_slab", 6, rows("XXX"), 'X', "lotr:near_harad_red_pillar");
        shaped(UMBAR, "umbar/near_harad_red_pillar_slab", "lotr:near_harad_red_pillar_slab", 6, rows("XXX"), 'X', "lotr:near_harad_red_pillar");
        shaped(GULF, "gulf/near_harad_red_pillar_slab", "lotr:near_harad_red_pillar_slab", 6, rows("XXX"), 'X', "lotr:near_harad_red_pillar");
        shaped(NEAR_HARAD, "near_harad/near_harad_lapis_brick", "lotr:near_harad_lapis_brick", 1, rows(" X ", "XYX", " X "), 'X', "minecraft:lapis_lazuli", 'Y', "lotr:near_harad_brick");
        shaped(UMBAR, "umbar/near_harad_lapis_brick", "lotr:near_harad_lapis_brick", 1, rows(" X ", "XYX", " X "), 'X', "minecraft:lapis_lazuli", 'Y', "lotr:near_harad_brick");
        shaped(GULF, "gulf/near_harad_lapis_brick", "lotr:near_harad_lapis_brick", 1, rows(" X ", "XYX", " X "), 'X', "minecraft:lapis_lazuli", 'Y', "lotr:near_harad_brick");
        shaped(NEAR_HARAD, "near_harad/kebab_stand", "lotr:kebab_stand", 1, rows(" X ", " Y ", "ZZZ"), 'X', "#minecraft:planks", 'Y', "#lotr:sticks", 'Z', "minecraft:cobblestone");
        shaped(UMBAR, "umbar/kebab_stand", "lotr:kebab_stand", 1, rows(" X ", " Y ", "ZZZ"), 'X', "#minecraft:planks", 'Y', "#lotr:sticks", 'Z', "minecraft:cobblestone");
        shaped(GULF, "gulf/kebab_stand", "lotr:kebab_stand", 1, rows(" X ", " Y ", "ZZZ"), 'X', "#minecraft:planks", 'Y', "#lotr:sticks", 'Z', "minecraft:cobblestone");
        shaped(NEAR_HARAD, "near_harad/harad_bow", "lotr:harad_bow", 1, rows(" XY", "X Y", " XY"), 'X', "#lotr:sticks", 'Y', "minecraft:string");
        shaped(UMBAR, "umbar/harad_bow", "lotr:harad_bow", 1, rows(" XY", "X Y", " XY"), 'X', "#lotr:sticks", 'Y', "minecraft:string");
        shaped(GULF, "gulf/harad_bow", "lotr:harad_bow", 1, rows(" XY", "X Y", " XY"), 'X', "#lotr:sticks", 'Y', "minecraft:string");
        // LOTRRecipes.createCommonNumenoreanRecipes
        shaped(GONDORIAN, "gondorian/numenorean_brick", "lotr:numenorean_brick", 4, rows("XX", "XX"), 'X', "lotr:mordor_rock");
        shaped(DOL_AMROTH, "dol_amroth/numenorean_brick", "lotr:numenorean_brick", 4, rows("XX", "XX"), 'X', "lotr:mordor_rock");
        shaped(UMBAR, "umbar/numenorean_brick", "lotr:numenorean_brick", 4, rows("XX", "XX"), 'X', "lotr:mordor_rock");
        shaped(GONDORIAN, "gondorian/numenorean_brick_slab", "lotr:numenorean_brick_slab", 6, rows("XXX"), 'X', "lotr:numenorean_brick");
        shaped(DOL_AMROTH, "dol_amroth/numenorean_brick_slab", "lotr:numenorean_brick_slab", 6, rows("XXX"), 'X', "lotr:numenorean_brick");
        shaped(UMBAR, "umbar/numenorean_brick_slab", "lotr:numenorean_brick_slab", 6, rows("XXX"), 'X', "lotr:numenorean_brick");
        shaped(GONDORIAN, "gondorian/numenorean_brick_stairs", "lotr:numenorean_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:numenorean_brick");
        shaped(DOL_AMROTH, "dol_amroth/numenorean_brick_stairs", "lotr:numenorean_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:numenorean_brick");
        shaped(UMBAR, "umbar/numenorean_brick_stairs", "lotr:numenorean_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:numenorean_brick");
        shaped(GONDORIAN, "gondorian/numenorean_brick_wall", "lotr:numenorean_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:numenorean_brick");
        shaped(DOL_AMROTH, "dol_amroth/numenorean_brick_wall", "lotr:numenorean_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:numenorean_brick");
        shaped(UMBAR, "umbar/numenorean_brick_wall", "lotr:numenorean_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:numenorean_brick");
        shaped(GONDORIAN, "gondorian/numenorean_pillar", "lotr:numenorean_pillar", 3, rows("X", "X", "X"), 'X', "lotr:mordor_rock");
        shaped(DOL_AMROTH, "dol_amroth/numenorean_pillar", "lotr:numenorean_pillar", 3, rows("X", "X", "X"), 'X', "lotr:mordor_rock");
        shaped(UMBAR, "umbar/numenorean_pillar", "lotr:numenorean_pillar", 3, rows("X", "X", "X"), 'X', "lotr:mordor_rock");
        shaped(GONDORIAN, "gondorian/numenorean_pillar_slab", "lotr:numenorean_pillar_slab", 6, rows("XXX"), 'X', "lotr:numenorean_pillar");
        shaped(DOL_AMROTH, "dol_amroth/numenorean_pillar_slab", "lotr:numenorean_pillar_slab", 6, rows("XXX"), 'X', "lotr:numenorean_pillar");
        shaped(UMBAR, "umbar/numenorean_pillar_slab", "lotr:numenorean_pillar_slab", 6, rows("XXX"), 'X', "lotr:numenorean_pillar");
        // LOTRRecipes.createCommonOrcRecipes
        shaped(MORGUL, "morgul/orc_bed", "lotr:orc_bed", 1, rows("XXX", "YYY"), 'X', "#minecraft:wool", 'Y', "#minecraft:planks");
        shaped(URUK, "uruk/orc_bed", "lotr:orc_bed", 1, rows("XXX", "YYY"), 'X', "#minecraft:wool", 'Y', "#minecraft:planks");
        shaped(ANGMAR, "angmar/orc_bed", "lotr:orc_bed", 1, rows("XXX", "YYY"), 'X', "#minecraft:wool", 'Y', "#minecraft:planks");
        shaped(DOL_GULDUR, "dol_guldur/orc_bed", "lotr:orc_bed", 1, rows("XXX", "YYY"), 'X', "#minecraft:wool", 'Y', "#minecraft:planks");
        shaped(GUNDABAD, "gundabad/orc_bed", "lotr:orc_bed", 1, rows("XXX", "YYY"), 'X', "#minecraft:wool", 'Y', "#minecraft:planks");
        shaped(HALF_TROLL, "half_troll/orc_bed", "lotr:orc_bed", 1, rows("XXX", "YYY"), 'X', "#minecraft:wool", 'Y', "#minecraft:planks");
        shapeless(MORGUL, "morgul/orc_fire_bomb", "lotr:orc_fire_bomb", 1, "lotr:orc_bomb", "minecraft:lava_bucket");
        shapeless(URUK, "uruk/orc_fire_bomb", "lotr:orc_fire_bomb", 1, "lotr:orc_bomb", "minecraft:lava_bucket");
        shapeless(ANGMAR, "angmar/orc_fire_bomb", "lotr:orc_fire_bomb", 1, "lotr:orc_bomb", "minecraft:lava_bucket");
        shapeless(DOL_GULDUR, "dol_guldur/orc_fire_bomb", "lotr:orc_fire_bomb", 1, "lotr:orc_bomb", "minecraft:lava_bucket");
        shapeless(GUNDABAD, "gundabad/orc_fire_bomb", "lotr:orc_fire_bomb", 1, "lotr:orc_bomb", "minecraft:lava_bucket");
        shapeless(HALF_TROLL, "half_troll/orc_fire_bomb", "lotr:orc_fire_bomb", 1, "lotr:orc_bomb", "minecraft:lava_bucket");
        shapeless(MORGUL, "morgul/double_strength_orc_fire_bomb", "lotr:double_strength_orc_fire_bomb", 1, "lotr:double_strength_orc_bomb", "minecraft:lava_bucket");
        shapeless(URUK, "uruk/double_strength_orc_fire_bomb", "lotr:double_strength_orc_fire_bomb", 1, "lotr:double_strength_orc_bomb", "minecraft:lava_bucket");
        shapeless(ANGMAR, "angmar/double_strength_orc_fire_bomb", "lotr:double_strength_orc_fire_bomb", 1, "lotr:double_strength_orc_bomb", "minecraft:lava_bucket");
        shapeless(DOL_GULDUR, "dol_guldur/double_strength_orc_fire_bomb", "lotr:double_strength_orc_fire_bomb", 1, "lotr:double_strength_orc_bomb", "minecraft:lava_bucket");
        shapeless(GUNDABAD, "gundabad/double_strength_orc_fire_bomb", "lotr:double_strength_orc_fire_bomb", 1, "lotr:double_strength_orc_bomb", "minecraft:lava_bucket");
        shapeless(HALF_TROLL, "half_troll/double_strength_orc_fire_bomb", "lotr:double_strength_orc_fire_bomb", 1, "lotr:double_strength_orc_bomb", "minecraft:lava_bucket");
        shapeless(MORGUL, "morgul/triple_strength_orc_fire_bomb", "lotr:triple_strength_orc_fire_bomb", 1, "lotr:triple_strength_orc_bomb", "minecraft:lava_bucket");
        shapeless(URUK, "uruk/triple_strength_orc_fire_bomb", "lotr:triple_strength_orc_fire_bomb", 1, "lotr:triple_strength_orc_bomb", "minecraft:lava_bucket");
        shapeless(ANGMAR, "angmar/triple_strength_orc_fire_bomb", "lotr:triple_strength_orc_fire_bomb", 1, "lotr:triple_strength_orc_bomb", "minecraft:lava_bucket");
        shapeless(DOL_GULDUR, "dol_guldur/triple_strength_orc_fire_bomb", "lotr:triple_strength_orc_fire_bomb", 1, "lotr:triple_strength_orc_bomb", "minecraft:lava_bucket");
        shapeless(GUNDABAD, "gundabad/triple_strength_orc_fire_bomb", "lotr:triple_strength_orc_fire_bomb", 1, "lotr:triple_strength_orc_bomb", "minecraft:lava_bucket");
        shapeless(HALF_TROLL, "half_troll/triple_strength_orc_fire_bomb", "lotr:triple_strength_orc_fire_bomb", 1, "lotr:triple_strength_orc_bomb", "minecraft:lava_bucket");
        shaped(MORGUL, "morgul/orc_skull_staff", "lotr:orc_skull_staff", 1, rows("X", "Y", "Y"), 'X', "minecraft:skeleton_skull", 'Y', "#lotr:sticks");
        shaped(URUK, "uruk/orc_skull_staff", "lotr:orc_skull_staff", 1, rows("X", "Y", "Y"), 'X', "minecraft:skeleton_skull", 'Y', "#lotr:sticks");
        shaped(ANGMAR, "angmar/orc_skull_staff", "lotr:orc_skull_staff", 1, rows("X", "Y", "Y"), 'X', "minecraft:skeleton_skull", 'Y', "#lotr:sticks");
        shaped(DOL_GULDUR, "dol_guldur/orc_skull_staff", "lotr:orc_skull_staff", 1, rows("X", "Y", "Y"), 'X', "minecraft:skeleton_skull", 'Y', "#lotr:sticks");
        shaped(GUNDABAD, "gundabad/orc_skull_staff", "lotr:orc_skull_staff", 1, rows("X", "Y", "Y"), 'X', "minecraft:skeleton_skull", 'Y', "#lotr:sticks");
        shaped(HALF_TROLL, "half_troll/orc_skull_staff", "lotr:orc_skull_staff", 1, rows("X", "Y", "Y"), 'X', "minecraft:skeleton_skull", 'Y', "#lotr:sticks");
        shaped(MORGUL, "morgul/orc_plating_rust", "lotr:orc_plating_rust", 8, rows("XXX", "XYX", "XXX"), 'X', "lotr:orc_plating_iron", 'Y', "minecraft:water_bucket");
        shaped(URUK, "uruk/orc_plating_rust", "lotr:orc_plating_rust", 8, rows("XXX", "XYX", "XXX"), 'X', "lotr:orc_plating_iron", 'Y', "minecraft:water_bucket");
        shaped(ANGMAR, "angmar/orc_plating_rust", "lotr:orc_plating_rust", 8, rows("XXX", "XYX", "XXX"), 'X', "lotr:orc_plating_iron", 'Y', "minecraft:water_bucket");
        shaped(DOL_GULDUR, "dol_guldur/orc_plating_rust", "lotr:orc_plating_rust", 8, rows("XXX", "XYX", "XXX"), 'X', "lotr:orc_plating_iron", 'Y', "minecraft:water_bucket");
        shaped(GUNDABAD, "gundabad/orc_plating_rust", "lotr:orc_plating_rust", 8, rows("XXX", "XYX", "XXX"), 'X', "lotr:orc_plating_iron", 'Y', "minecraft:water_bucket");
        shaped(HALF_TROLL, "half_troll/orc_plating_rust", "lotr:orc_plating_rust", 8, rows("XXX", "XYX", "XXX"), 'X', "lotr:orc_plating_iron", 'Y', "minecraft:water_bucket");
        // LOTRRecipes.createDaleRecipes
        shaped(DALE, "dale/dale_crafting_table", "lotr:dale_crafting_table", 1, rows("XX", "XX"), 'X', "#minecraft:planks");
        shaped(DALE, "dale/dale_banner", "lotr:dale_banner", 1, rows("X", "Y", "Z"), 'X', "#minecraft:wool", 'Y', "#lotr:sticks", 'Z', "#minecraft:planks");
        shaped(DALE, "dale/dale_brick", "lotr:dale_brick", 4, rows("XX", "XX"), 'X', "minecraft:stone");
        shaped(DALE, "dale/dale_brick_slab", "lotr:dale_brick_slab", 6, rows("XXX"), 'X', "lotr:dale_brick");
        shaped(DALE, "dale/dale_brick_stairs", "lotr:dale_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:dale_brick");
        shaped(DALE, "dale/dale_brick_wall", "lotr:dale_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:dale_brick");
        shaped(DALE, "dale/dale_pillar", "lotr:dale_pillar", 3, rows("X", "X", "X"), 'X', "minecraft:stone");
        shaped(DALE, "dale/dale_pillar_slab", "lotr:dale_pillar_slab", 6, rows("XXX"), 'X', "lotr:dale_pillar");
        shaped(DALE, "dale/esgaroth_banner", "lotr:esgaroth_banner", 1, rows("XA", "Y ", "Z "), 'X', "#minecraft:wool", 'Y', "#lotr:sticks", 'Z', "#minecraft:planks", 'A', "minecraft:cod");
        shapeless(DALE, "dale/dale_mossy_brick", "lotr:dale_mossy_brick", 1, "lotr:dale_brick", "#lotr:vines");
        shaped(DALE, "dale/dale_mossy_brick_slab", "lotr:dale_mossy_brick_slab", 6, rows("XXX"), 'X', "lotr:dale_mossy_brick");
        shaped(DALE, "dale/dale_mossy_brick_stairs", "lotr:dale_mossy_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:dale_mossy_brick");
        shaped(DALE, "dale/dale_mossy_brick_wall", "lotr:dale_mossy_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:dale_mossy_brick");
        shaped(DALE, "dale/dale_cracked_brick_slab", "lotr:dale_cracked_brick_slab", 6, rows("XXX"), 'X', "lotr:dale_cracked_brick");
        shaped(DALE, "dale/dale_cracked_brick_stairs", "lotr:dale_cracked_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:dale_cracked_brick");
        shaped(DALE, "dale/dale_cracked_brick_wall", "lotr:dale_cracked_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:dale_cracked_brick");
        shaped(DALE, "dale/dale_carved_brick", "lotr:dale_carved_brick", 1, rows("XX", "XX"), 'X', "lotr:dale_brick");
        // LOTRRecipes.createDolAmrothRecipes
        shaped(DOL_AMROTH, "dol_amroth/dol_amroth_crafting_table", "lotr:dol_amroth_crafting_table", 1, rows("XX", "YY"), 'X', "#minecraft:planks", 'Y', "lotr:gondor_rock");
        shaped(DOL_AMROTH, "dol_amroth/dol_amroth_banner", "lotr:dol_amroth_banner", 1, rows("X", "Y", "Z"), 'X', "#minecraft:wool", 'Y', "#lotr:sticks", 'Z', "#minecraft:planks");
        shaped(DOL_AMROTH, "dol_amroth/dol_amroth_leggings", "lotr:dol_amroth_leggings", 1, rows("XXX", "X X", "X X"), 'X', "minecraft:iron_ingot");
        shaped(DOL_AMROTH, "dol_amroth/dol_amroth_boots", "lotr:dol_amroth_boots", 1, rows("X X", "X X"), 'X', "minecraft:iron_ingot");
        shaped(DOL_AMROTH, "dol_amroth/dol_amroth_sword", "lotr:dol_amroth_sword", 1, rows("X", "X", "Y"), 'X', "minecraft:iron_ingot", 'Y', "#lotr:sticks");
        shaped(DOL_AMROTH, "dol_amroth/dol_amroth_horse_armor", "lotr:dol_amroth_horse_armor", 1, rows("X  ", "XYX", "XXX"), 'X', "minecraft:iron_ingot", 'Y', "minecraft:leather");
        shaped(DOL_AMROTH, "dol_amroth/dol_amroth_brick", "lotr:dol_amroth_brick", 4, rows("XX", "XX"), 'X', "lotr:gondor_rock");
        shaped(DOL_AMROTH, "dol_amroth/dol_amroth_brick_slab", "lotr:dol_amroth_brick_slab", 6, rows("XXX"), 'X', "lotr:dol_amroth_brick");
        shaped(DOL_AMROTH, "dol_amroth/dol_amroth_brick_stairs", "lotr:dol_amroth_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:dol_amroth_brick");
        shaped(DOL_AMROTH, "dol_amroth/dol_amroth_brick_wall", "lotr:dol_amroth_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:dol_amroth_brick");
        shaped(DOL_AMROTH, "dol_amroth/dol_amroth_lance", "lotr:dol_amroth_lance", 1, rows("  X", " X ", "Y  "), 'X', "minecraft:iron_ingot", 'Y', "#lotr:sticks");
        shaped(DOL_AMROTH, "dol_amroth/gondor_bow", "lotr:gondor_bow", 1, rows(" XY", "X Y", " XY"), 'X', "#lotr:sticks", 'Y', "minecraft:string");
        // LOTRRecipes.createDolGuldurRecipes
        shaped(DOL_GULDUR, "dol_guldur/dol_guldur_brick", "lotr:dol_guldur_brick", 4, rows("XX", "XX"), 'X', "minecraft:stone");
        shaped(DOL_GULDUR, "dol_guldur/dol_guldur_crafting_table", "lotr:dol_guldur_crafting_table", 1, rows("XX", "YY"), 'X', "#minecraft:planks", 'Y', "lotr:dol_guldur_brick");
        shaped(DOL_GULDUR, "dol_guldur/dol_guldur_brick_slab", "lotr:dol_guldur_brick_slab", 6, rows("XXX"), 'X', "lotr:dol_guldur_brick");
        shaped(DOL_GULDUR, "dol_guldur/dol_guldur_brick_stairs", "lotr:dol_guldur_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:dol_guldur_brick");
        shaped(DOL_GULDUR, "dol_guldur/dol_guldur_brick_wall", "lotr:dol_guldur_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:dol_guldur_brick");
        shaped(DOL_GULDUR, "dol_guldur/orc_torch", "lotr:orc_torch", 2, rows("X", "Y", "Y"), 'X', "#minecraft:coals", 'Y', "#lotr:sticks");
        shaped(DOL_GULDUR, "dol_guldur/cracked_dol_guldur_brick_slab", "lotr:cracked_dol_guldur_brick_slab", 6, rows("XXX"), 'X', "lotr:cracked_dol_guldur_brick");
        shaped(DOL_GULDUR, "dol_guldur/cracked_dol_guldur_brick_stairs", "lotr:cracked_dol_guldur_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:cracked_dol_guldur_brick");
        shaped(DOL_GULDUR, "dol_guldur/cracked_dol_guldur_brick_wall", "lotr:cracked_dol_guldur_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:cracked_dol_guldur_brick");
        shaped(DOL_GULDUR, "dol_guldur/orc_forge", "lotr:orc_forge", 1, rows("XXX", "X X", "XXX"), 'X', "lotr:dol_guldur_brick");
        shaped(DOL_GULDUR, "dol_guldur/dol_guldur_banner", "lotr:dol_guldur_banner", 1, rows("X", "Y", "Z"), 'X', "#minecraft:wool", 'Y', "#lotr:sticks", 'Z', "#minecraft:planks");
        shapeless(DOL_GULDUR, "dol_guldur/mossy_dol_guldur_brick", "lotr:mossy_dol_guldur_brick", 1, "lotr:dol_guldur_brick", "#lotr:vines");
        shaped(DOL_GULDUR, "dol_guldur/carved_dol_guldur_brick", "lotr:carved_dol_guldur_brick", 1, rows("XX", "XX"), 'X', "lotr:dol_guldur_brick");
        shaped(DOL_GULDUR, "dol_guldur/mossy_dol_guldur_brick_slab", "lotr:mossy_dol_guldur_brick_slab", 6, rows("XXX"), 'X', "lotr:mossy_dol_guldur_brick");
        shaped(DOL_GULDUR, "dol_guldur/mossy_dol_guldur_brick_stairs", "lotr:mossy_dol_guldur_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:mossy_dol_guldur_brick");
        shaped(DOL_GULDUR, "dol_guldur/mossy_dol_guldur_brick_wall", "lotr:mossy_dol_guldur_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:mossy_dol_guldur_brick");
        shaped(DOL_GULDUR, "dol_guldur/dol_guldur_pillar", "lotr:dol_guldur_pillar", 3, rows("X", "X", "X"), 'X', "minecraft:stone");
        shaped(DOL_GULDUR, "dol_guldur/dol_guldur_pillar_slab", "lotr:dol_guldur_pillar_slab", 6, rows("XXX"), 'X', "lotr:dol_guldur_pillar");
        // LOTRRecipes.createDorwinionRecipes
        shaped(DORWINION, "dorwinion/dorwinion_crafting_table", "lotr:dorwinion_crafting_table", 1, rows("XX", "YY"), 'X', "#minecraft:planks", 'Y', "lotr:chalk");
        shaped(DORWINION, "dorwinion/dorwinion_banner", "lotr:dorwinion_banner", 1, rows("X", "Y", "Z"), 'X', "#minecraft:wool", 'Y', "#lotr:sticks", 'Z', "#minecraft:planks");
        shaped(DORWINION, "dorwinion/dorwinion_brick", "lotr:dorwinion_brick", 4, rows("XX", "XX"), 'X', "lotr:chalk");
        shaped(DORWINION, "dorwinion/dorwinion_brick_slab", "lotr:dorwinion_brick_slab", 6, rows("XXX"), 'X', "lotr:dorwinion_brick");
        shaped(DORWINION, "dorwinion/dorwinion_brick_stairs", "lotr:dorwinion_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:dorwinion_brick");
        shaped(DORWINION, "dorwinion/dorwinion_brick_wall", "lotr:dorwinion_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:dorwinion_brick");
        shaped(DORWINION, "dorwinion/elven_forge", "lotr:elven_forge", 1, rows("XXX", "X X", "XXX"), 'X', "lotr:dorwinion_brick");
        shapeless(DORWINION, "dorwinion/mossy_dorwinion_brick", "lotr:mossy_dorwinion_brick", 1, "lotr:dorwinion_brick", "#lotr:vines");
        shaped(DORWINION, "dorwinion/mossy_dorwinion_brick_slab", "lotr:mossy_dorwinion_brick_slab", 6, rows("XXX"), 'X', "lotr:mossy_dorwinion_brick");
        shaped(DORWINION, "dorwinion/mossy_dorwinion_brick_stairs", "lotr:mossy_dorwinion_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:mossy_dorwinion_brick");
        shaped(DORWINION, "dorwinion/mossy_dorwinion_brick_wall", "lotr:mossy_dorwinion_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:mossy_dorwinion_brick");
        shaped(DORWINION, "dorwinion/cracked_dorwinion_brick_slab", "lotr:cracked_dorwinion_brick_slab", 6, rows("XXX"), 'X', "lotr:cracked_dorwinion_brick");
        shaped(DORWINION, "dorwinion/cracked_dorwinion_brick_stairs", "lotr:cracked_dorwinion_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:cracked_dorwinion_brick");
        shaped(DORWINION, "dorwinion/cracked_dorwinion_brick_wall", "lotr:cracked_dorwinion_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:cracked_dorwinion_brick");
        shapeless(DORWINION, "dorwinion/dorwinion_flowers_brick", "lotr:dorwinion_flowers_brick", 1, "lotr:dorwinion_brick", "lotr:rhun_flower_chrys_pink");
        shaped(DORWINION, "dorwinion/dorwinion_flowers_brick_slab", "lotr:dorwinion_flowers_brick_slab", 6, rows("XXX"), 'X', "lotr:dorwinion_flowers_brick");
        shaped(DORWINION, "dorwinion/dorwinion_flowers_brick_stairs", "lotr:dorwinion_flowers_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:dorwinion_flowers_brick");
        shaped(DORWINION, "dorwinion/dorwinion_flowers_brick_wall", "lotr:dorwinion_flowers_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:dorwinion_flowers_brick");
        shaped(DORWINION, "dorwinion/carved_dorwinion_brick", "lotr:carved_dorwinion_brick", 1, rows("XX", "XX"), 'X', "lotr:dorwinion_brick");
        shaped(DORWINION, "dorwinion/dorwinion_pillar", "lotr:dorwinion_pillar", 3, rows("X", "X", "X"), 'X', "lotr:chalk");
        shaped(DORWINION, "dorwinion/dorwinion_pillar_slab", "lotr:dorwinion_pillar_slab", 6, rows("XXX"), 'X', "lotr:dorwinion_pillar");
        shapeless(DORWINION, "dorwinion/mossy_dorwinion_pillar", "lotr:mossy_dorwinion_pillar", 1, "lotr:dorwinion_pillar", "#lotr:vines");
        shaped(DORWINION, "dorwinion/mossy_dorwinion_pillar_slab", "lotr:mossy_dorwinion_pillar_slab", 6, rows("XXX"), 'X', "lotr:mossy_dorwinion_pillar");
        // LOTRRecipes.createDunlendingRecipes
        shaped(DUNLENDING, "dunlending/dunlending_crafting_table", "lotr:dunlending_crafting_table", 1, rows("XX", "YY"), 'X', "#minecraft:planks", 'Y', "minecraft:cobblestone");
        shaped(DUNLENDING, "dunlending/dunlending_helmet", "lotr:dunlending_helmet", 1, rows("XXX", "Y Y"), 'X', "minecraft:iron_ingot", 'Y', "minecraft:leather");
        shaped(DUNLENDING, "dunlending/dunlending_chestplate", "lotr:dunlending_chestplate", 1, rows("X X", "YYY", "XXX"), 'X', "minecraft:iron_ingot", 'Y', "minecraft:leather");
        shaped(DUNLENDING, "dunlending/dunlending_leggings", "lotr:dunlending_leggings", 1, rows("XXX", "Y Y", "X X"), 'X', "minecraft:iron_ingot", 'Y', "minecraft:leather");
        shaped(DUNLENDING, "dunlending/dunlending_boots", "lotr:dunlending_boots", 1, rows("Y Y", "X X"), 'X', "minecraft:iron_ingot", 'Y', "minecraft:leather");
        shaped(DUNLENDING, "dunlending/dunlending_club", "lotr:dunlending_club", 1, rows("X", "X", "X"), 'X', "#minecraft:planks");
        shaped(DUNLENDING, "dunlending/dunlending_trident", "lotr:dunlending_trident", 1, rows(" XX", " YX", "Y  "), 'X', "minecraft:iron_ingot", 'Y', "#lotr:sticks");
        shaped(DUNLENDING, "dunlending/dunland_banner", "lotr:dunland_banner", 1, rows("X", "Y", "Z"), 'X', "#minecraft:wool", 'Y', "#lotr:sticks", 'Z', "#minecraft:planks");
        // LOTRRecipes.createDwarvenRecipes
        shaped(DWARVEN, "dwarven/dwarven_crafting_table", "lotr:dwarven_crafting_table", 1, rows("XX", "YY"), 'X', "#minecraft:planks", 'Y', "lotr:dwarven_brick");
        shaped(DWARVEN, "dwarven/durin_banner", "lotr:durin_banner", 1, rows("X", "Y", "Z"), 'X', "#minecraft:wool", 'Y', "#lotr:sticks", 'Z', "#minecraft:planks");
        shaped(DWARVEN, "dwarven/gold_trimmed_dwarven_helmet", "lotr:gold_trimmed_dwarven_helmet", 1, rows("XXX", "XYX", "XXX"), 'X', "minecraft:gold_nugget", 'Y', "lotr:dwarven_helmet");
        shaped(DWARVEN, "dwarven/gold_trimmed_dwarven_chestplate", "lotr:gold_trimmed_dwarven_chestplate", 1, rows("XXX", "XYX", "XXX"), 'X', "minecraft:gold_nugget", 'Y', "lotr:dwarven_chestplate");
        shaped(DWARVEN, "dwarven/gold_trimmed_dwarven_leggings", "lotr:gold_trimmed_dwarven_leggings", 1, rows("XXX", "XYX", "XXX"), 'X', "minecraft:gold_nugget", 'Y', "lotr:dwarven_leggings");
        shaped(DWARVEN, "dwarven/gold_trimmed_dwarven_boots", "lotr:gold_trimmed_dwarven_boots", 1, rows("XXX", "XYX", "XXX"), 'X', "minecraft:gold_nugget", 'Y', "lotr:dwarven_boots");
        // LOTRRecipes.createElvenRecipes
        shapeless(ELVEN, "elven/mallorn_planks", "lotr:mallorn_planks", 4, "lotr:mallorn_log");
        shaped(ELVEN, "elven/mallorn_slab", "lotr:mallorn_slab", 6, rows("XXX"), 'X', "lotr:mallorn_planks");
        shaped(ELVEN, "elven/mallorn_stairs", "lotr:mallorn_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:mallorn_planks");
        shaped(ELVEN, "elven/elven_crafting_table", "lotr:elven_crafting_table", 1, rows("XX", "XX"), 'X', "lotr:mallorn_planks");
        shaped(ELVEN, "elven/galadhrim_brick", "lotr:galadhrim_brick", 4, rows("XX", "XX"), 'X', "minecraft:stone");
        shapeless(ELVEN, "elven/mossy_galadhrim_brick", "lotr:mossy_galadhrim_brick", 1, "lotr:galadhrim_brick", "#lotr:vines");
        shaped(ELVEN, "elven/galadhrim_brick_slab", "lotr:galadhrim_brick_slab", 6, rows("XXX"), 'X', "lotr:galadhrim_brick");
        shaped(ELVEN, "elven/mossy_galadhrim_brick_slab", "lotr:mossy_galadhrim_brick_slab", 6, rows("XXX"), 'X', "lotr:mossy_galadhrim_brick");
        shaped(ELVEN, "elven/cracked_galadhrim_brick_slab", "lotr:cracked_galadhrim_brick_slab", 6, rows("XXX"), 'X', "lotr:cracked_galadhrim_brick");
        shaped(ELVEN, "elven/galadhrim_brick_stairs", "lotr:galadhrim_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:galadhrim_brick");
        shaped(ELVEN, "elven/mossy_galadhrim_brick_stairs", "lotr:mossy_galadhrim_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:mossy_galadhrim_brick");
        shaped(ELVEN, "elven/cracked_galadhrim_brick_stairs", "lotr:cracked_galadhrim_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:cracked_galadhrim_brick");
        shaped(ELVEN, "elven/galadhrim_brick_wall", "lotr:galadhrim_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:galadhrim_brick");
        shaped(ELVEN, "elven/mossy_galadhrim_brick_wall", "lotr:mossy_galadhrim_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:mossy_galadhrim_brick");
        shaped(ELVEN, "elven/cracked_galadhrim_brick_wall", "lotr:cracked_galadhrim_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:cracked_galadhrim_brick");
        shaped(ELVEN, "elven/galadhrim_pillar", "lotr:galadhrim_pillar", 3, rows("X", "X", "X"), 'X', "minecraft:stone");
        shaped(ELVEN, "elven/galadhrim_pillar_slab", "lotr:galadhrim_pillar_slab", 6, rows("XXX"), 'X', "lotr:galadhrim_pillar");
        shaped(ELVEN, "elven/cracked_galadhrim_pillar_slab", "lotr:cracked_galadhrim_pillar_slab", 6, rows("XXX"), 'X', "lotr:cracked_galadhrim_pillar");
        shaped(ELVEN, "elven/carved_galadhrim_brick", "lotr:carved_galadhrim_brick", 1, rows("XX", "XX"), 'X', "lotr:galadhrim_brick");
        shaped(ELVEN, "elven/elven_forge", "lotr:elven_forge", 1, rows("XXX", "X X", "XXX"), 'X', "lotr:galadhrim_brick");
        shaped(ELVEN, "elven/mallorn_beam", "lotr:mallorn_beam", 3, rows("X", "X", "X"), 'X', "lotr:mallorn_log");
        shaped(ELVEN, "elven/galadhrim_gold_brick", "lotr:galadhrim_gold_brick", 1, rows(" X ", "XYX", " X "), 'X', "minecraft:gold_nugget", 'Y', "lotr:galadhrim_brick");
        // The original pattern is "XXX", "XYX", "XXX" but never defines Y; ShapedOreRecipe read an undefined symbol as an empty slot.
        shaped(ELVEN, "elven/mallorn_box", "lotr:mallorn_box", 1, rows("XXX", "X X", "XXX"), 'X', "lotr:mallorn_planks");
        shaped(ELVEN, "elven/mallorn_door", "lotr:mallorn_door", 1, rows("XX", "XX", "XX"), 'X', "lotr:mallorn_planks");
        shaped(ELVEN, "elven/mallorn_trapdoor", "lotr:mallorn_trapdoor", 2, rows("XXX", "XXX"), 'X', "lotr:mallorn_planks");
        // LOTRRecipes.createGondorianRecipes
        shaped(GONDORIAN, "gondorian/gondorian_crafting_table", "lotr:gondorian_crafting_table", 1, rows("XX", "YY"), 'X', "#minecraft:planks", 'Y', "lotr:gondor_rock");
        shaped(GONDORIAN, "gondorian/beacon_of_gondor", "lotr:beacon_of_gondor", 1, rows("XXX", "XXX", "YYY"), 'X', "#minecraft:logs", 'Y', "minecraft:cobblestone");
        shaped(GONDORIAN, "gondorian/gondor_rock_slab", "lotr:gondor_rock_slab", 6, rows("XXX"), 'X', "lotr:gondor_rock");
        shaped(GONDORIAN, "gondorian/smooth_gondor_rock", "lotr:smooth_gondor_rock", 2, rows("X", "X"), 'X', "lotr:gondor_rock");
        shaped(GONDORIAN, "gondorian/smooth_gondor_rock_slab", "lotr:smooth_gondor_rock_slab", 6, rows("XXX"), 'X', "lotr:smooth_gondor_rock");
        shaped(GONDORIAN, "gondorian/gondor_brick", "lotr:gondor_brick", 4, rows("XX", "XX"), 'X', "lotr:gondor_rock");
        shaped(GONDORIAN, "gondorian/gondor_brick_slab", "lotr:gondor_brick_slab", 6, rows("XXX"), 'X', "lotr:gondor_brick");
        shaped(GONDORIAN, "gondorian/gondor_brick_stairs", "lotr:gondor_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:gondor_brick");
        shaped(GONDORIAN, "gondorian/gondor_rock_wall", "lotr:gondor_rock_wall", 6, rows("XXX", "XXX"), 'X', "lotr:gondor_rock");
        shaped(GONDORIAN, "gondorian/gondor_brick_wall", "lotr:gondor_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:gondor_brick");
        shapeless(GONDORIAN, "gondorian/mossy_gondor_brick", "lotr:mossy_gondor_brick", 1, "lotr:gondor_brick", "#lotr:vines");
        shaped(GONDORIAN, "gondorian/mossy_gondor_brick_slab", "lotr:mossy_gondor_brick_slab", 6, rows("XXX"), 'X', "lotr:mossy_gondor_brick");
        shaped(GONDORIAN, "gondorian/mossy_gondor_brick_stairs", "lotr:mossy_gondor_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:mossy_gondor_brick");
        shaped(GONDORIAN, "gondorian/mossy_gondor_brick_wall", "lotr:mossy_gondor_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:mossy_gondor_brick");
        shaped(GONDORIAN, "gondorian/cracked_gondor_brick_slab", "lotr:cracked_gondor_brick_slab", 6, rows("XXX"), 'X', "lotr:cracked_gondor_brick");
        shaped(GONDORIAN, "gondorian/cracked_gondor_brick_stairs", "lotr:cracked_gondor_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:cracked_gondor_brick");
        shaped(GONDORIAN, "gondorian/cracked_gondor_brick_wall", "lotr:cracked_gondor_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:cracked_gondor_brick");
        shaped(GONDORIAN, "gondorian/carved_gondor_brick", "lotr:carved_gondor_brick", 1, rows("XX", "XX"), 'X', "lotr:gondor_brick");
        shaped(GONDORIAN, "gondorian/gondor_helmet", "lotr:gondor_helmet", 1, rows("XXX", "X X"), 'X', "minecraft:iron_ingot");
        shaped(GONDORIAN, "gondorian/gondor_chestplate", "lotr:gondor_chestplate", 1, rows("X X", "XXX", "XXX"), 'X', "minecraft:iron_ingot");
        shaped(GONDORIAN, "gondorian/gondor_leggings", "lotr:gondor_leggings", 1, rows("XXX", "X X", "X X"), 'X', "minecraft:iron_ingot");
        shaped(GONDORIAN, "gondorian/gondor_boots", "lotr:gondor_boots", 1, rows("X X", "X X"), 'X', "minecraft:iron_ingot");
        shaped(GONDORIAN, "gondorian/gondor_sword", "lotr:gondor_sword", 1, rows("X", "X", "Y"), 'X', "minecraft:iron_ingot", 'Y', "#lotr:sticks");
        shaped(GONDORIAN, "gondorian/gondor_spear", "lotr:gondor_spear", 1, rows("  X", " Y ", "Y  "), 'X', "minecraft:iron_ingot", 'Y', "#lotr:sticks");
        shaped(GONDORIAN, "gondorian/gondor_dagger", "lotr:gondor_dagger", 1, rows("X", "Y"), 'X', "minecraft:iron_ingot", 'Y', "#lotr:sticks");
        shaped(GONDORIAN, "gondorian/gondor_warhammer", "lotr:gondor_warhammer", 1, rows("XYX", "XYX", " Y "), 'X', "minecraft:iron_ingot", 'Y', "#lotr:sticks");
        shaped(GONDORIAN, "gondorian/gondor_bow", "lotr:gondor_bow", 1, rows(" XY", "X Y", " XY"), 'X', "#lotr:sticks", 'Y', "minecraft:string");
        shaped(GONDORIAN, "gondorian/gondor_winged_helmet", "lotr:gondor_winged_helmet", 1, rows("XYX"), 'X', "#lotr:feathers", 'Y', "lotr:gondor_helmet");
        shaped(GONDORIAN, "gondorian/gondor_banner", "lotr:gondor_banner", 1, rows("X", "Y", "Z"), 'X', "#minecraft:wool", 'Y', "#lotr:sticks", 'Z', "#minecraft:planks");
        shaped(GONDORIAN, "gondorian/gondor_horse_armor", "lotr:gondor_horse_armor", 1, rows("X  ", "XYX", "XXX"), 'X', "minecraft:iron_ingot", 'Y', "minecraft:leather");
        shaped(GONDORIAN, "gondorian/gondor_pillar", "lotr:gondor_pillar", 3, rows("X", "X", "X"), 'X', "lotr:gondor_rock");
        shaped(GONDORIAN, "gondorian/gondor_pillar_slab", "lotr:gondor_pillar_slab", 6, rows("XXX"), 'X', "lotr:gondor_pillar");
        shaped(GONDORIAN, "gondorian/gondor_rock_stairs", "lotr:gondor_rock_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:gondor_rock");
        shaped(GONDORIAN, "gondorian/anorien_banner", "lotr:anorien_banner", 1, rows("XA", "Y ", "Z "), 'X', "#minecraft:wool", 'Y', "#lotr:sticks", 'Z', "#minecraft:planks", 'A', "minecraft:gold_nugget");
        shaped(GONDORIAN, "gondorian/lossarnach_banner", "lotr:lossarnach_banner", 1, rows("XA", "Y ", "Z "), 'X', "#minecraft:wool", 'Y', "#lotr:sticks", 'Z', "#minecraft:planks", 'A', "minecraft:rose_bush");
        shaped(GONDORIAN, "gondorian/pinnath_gelin_banner", "lotr:pinnath_gelin_banner", 1, rows("XA", "Y ", "Z "), 'X', "#minecraft:wool", 'Y', "#lotr:sticks", 'Z', "#minecraft:planks", 'A', "#c:dyes/green");
        shaped(GONDORIAN, "gondorian/lebennin_banner", "lotr:lebennin_banner", 1, rows("XA", "Y ", "Z "), 'X', "#minecraft:wool", 'Y', "#lotr:sticks", 'Z', "#minecraft:planks", 'A', "#c:dyes/light_blue");
        shaped(GONDORIAN, "gondorian/pelargir_banner", "lotr:pelargir_banner", 1, rows("XA", "Y ", "Z "), 'X', "#minecraft:wool", 'Y', "#lotr:sticks", 'Z', "#minecraft:planks", 'A', "#c:dyes/cyan");
        shaped(GONDORIAN, "gondorian/blackroot_vale_banner", "lotr:blackroot_vale_banner", 1, rows("XA", "Y ", "Z "), 'X', "#minecraft:wool", 'Y', "#lotr:sticks", 'Z', "#minecraft:planks", 'A', "lotr:blackroot");
        shaped(GONDORIAN, "gondorian/lamedon_banner", "lotr:lamedon_banner", 1, rows("XA", "Y ", "Z "), 'X', "#minecraft:wool", 'Y', "#lotr:sticks", 'Z', "#minecraft:planks", 'A', "#c:dyes/blue");
        shaped(GONDORIAN, "gondorian/gondor_cobblebrick", "lotr:gondor_cobblebrick", 4, rows("XY", "YX"), 'X', "lotr:gondor_rock", 'Y', "minecraft:cobblestone");
        shaped(GONDORIAN, "gondorian/gondor_cobblebrick_slab", "lotr:gondor_cobblebrick_slab", 6, rows("XXX"), 'X', "lotr:gondor_cobblebrick");
        shaped(GONDORIAN, "gondorian/gondor_cobblebrick_stairs", "lotr:gondor_cobblebrick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:gondor_cobblebrick");
        shaped(GONDORIAN, "gondorian/gondor_cobblebrick_wall", "lotr:gondor_cobblebrick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:gondor_cobblebrick");
        shapeless(GONDORIAN, "gondorian/mossy_gondor_cobblebrick", "lotr:mossy_gondor_cobblebrick", 1, "lotr:gondor_cobblebrick", "#lotr:vines");
        shaped(GONDORIAN, "gondorian/mossy_gondor_cobblebrick_slab", "lotr:mossy_gondor_cobblebrick_slab", 6, rows("XXX"), 'X', "lotr:mossy_gondor_cobblebrick");
        shaped(GONDORIAN, "gondorian/mossy_gondor_cobblebrick_stairs", "lotr:mossy_gondor_cobblebrick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:mossy_gondor_cobblebrick");
        shaped(GONDORIAN, "gondorian/mossy_gondor_cobblebrick_wall", "lotr:mossy_gondor_cobblebrick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:mossy_gondor_cobblebrick");
        shaped(GONDORIAN, "gondorian/cracked_gondor_cobblebrick_slab", "lotr:cracked_gondor_cobblebrick_slab", 6, rows("XXX"), 'X', "lotr:cracked_gondor_cobblebrick");
        shaped(GONDORIAN, "gondorian/cracked_gondor_cobblebrick_stairs", "lotr:cracked_gondor_cobblebrick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:cracked_gondor_cobblebrick");
        shaped(GONDORIAN, "gondorian/cracked_gondor_cobblebrick_wall", "lotr:cracked_gondor_cobblebrick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:cracked_gondor_cobblebrick");
        shaped(GONDORIAN, "gondorian/gondor_steward_banner", "lotr:gondor_steward_banner", 1, rows("XA", "Y ", "Z "), 'X', "#minecraft:wool", 'Y', "#lotr:sticks", 'Z', "#minecraft:planks", 'A', "#c:dyes/white");
        // LOTRRecipes.createGulfRecipes
        shaped(GULF, "gulf/gulf_crafting_table", "lotr:gulf_crafting_table", 1, rows("XX", "YY"), 'X', "#minecraft:planks", 'Y', "minecraft:sandstone");
        shaped(GULF, "gulf/harad_gulf_banner", "lotr:harad_gulf_banner", 1, rows("X", "Y", "Z"), 'X', "#minecraft:wool", 'Y', "#lotr:sticks", 'Z', "#minecraft:planks");
        // LOTRRecipes.createGundabadRecipes
        shaped(GUNDABAD, "gundabad/gundabad_crafting_table", "lotr:gundabad_crafting_table", 1, rows("XX", "YY"), 'X', "#minecraft:planks", 'Y', "minecraft:cobblestone");
        shaped(GUNDABAD, "gundabad/orc_torch", "lotr:orc_torch", 2, rows("X", "Y", "Y"), 'X', "#minecraft:coals", 'Y', "#lotr:sticks");
        shaped(GUNDABAD, "gundabad/orc_forge", "lotr:orc_forge", 1, rows("XXX", "X X", "XXX"), 'X', "minecraft:cobblestone");
        shaped(GUNDABAD, "gundabad/gundabad_banner", "lotr:gundabad_banner", 1, rows("X", "Y", "Z"), 'X', "#minecraft:wool", 'Y', "#lotr:sticks", 'Z', "#minecraft:planks");
        // LOTRRecipes.createHalfTrollRecipes
        shaped(HALF_TROLL, "half_troll/half_troll_crafting_table", "lotr:half_troll_crafting_table", 1, rows("XX", "YY"), 'X', "#minecraft:planks", 'Y', "minecraft:cobblestone");
        shaped(HALF_TROLL, "half_troll/half_troll_banner", "lotr:half_troll_banner", 1, rows("X", "Y", "Z"), 'X', "#minecraft:wool", 'Y', "#lotr:sticks", 'Z', "#minecraft:planks");
        shaped(HALF_TROLL, "half_troll/half_troll_scimitar", "lotr:half_troll_scimitar", 1, rows("X", "X", "Y"), 'X', "minecraft:flint", 'Y', "#lotr:sticks");
        shaped(HALF_TROLL, "half_troll/half_troll_battleaxe", "lotr:half_troll_battleaxe", 1, rows("XXX", "XYX", " Y "), 'X', "minecraft:flint", 'Y', "#lotr:sticks");
        shaped(HALF_TROLL, "half_troll/half_troll_dagger", "lotr:half_troll_dagger", 1, rows("X", "Y"), 'X', "minecraft:flint", 'Y', "#lotr:sticks");
        shaped(HALF_TROLL, "half_troll/half_troll_warhammer", "lotr:half_troll_warhammer", 1, rows("XYX", "XYX", " Y "), 'X', "minecraft:flint", 'Y', "#lotr:sticks");
        shaped(HALF_TROLL, "half_troll/half_troll_mace", "lotr:half_troll_mace", 1, rows(" XX", " XX", "Y  "), 'X', "minecraft:flint", 'Y', "#lotr:sticks");
        shaped(HALF_TROLL, "half_troll/half_troll_pike", "lotr:half_troll_pike", 1, rows("  X", " YX", "Y  "), 'X', "minecraft:flint", 'Y', "#lotr:sticks");
        // LOTRRecipes.createHighElvenRecipes
        shaped(HIGH_ELVEN, "high_elven/high_elven_crafting_table", "lotr:high_elven_crafting_table", 1, rows("XX", "XX"), 'X', "#minecraft:planks");
        shaped(HIGH_ELVEN, "high_elven/high_elf_banner", "lotr:high_elf_banner", 1, rows("X", "Y", "Z"), 'X', "#minecraft:wool", 'Y', "#lotr:sticks", 'Z', "#minecraft:planks");
        // LOTRRecipes.createHobbitRecipes
        shaped(HOBBIT, "hobbit/hobbit_crafting_table", "lotr:hobbit_crafting_table", 1, rows("XX", "YY"), 'X', "#minecraft:wool", 'Y', "#minecraft:planks");
        shaped(HOBBIT, "hobbit/hobbit_banner", "lotr:hobbit_banner", 1, rows("X", "Y", "Z"), 'X', "#minecraft:wool", 'Y', "#lotr:sticks", 'Z', "#minecraft:planks");
        // LOTRRecipes.createMoredainRecipes
        shaped(MOREDAIN, "moredain/moredain_crafting_table", "lotr:moredain_crafting_table", 1, rows("XX", "YY"), 'X', "#minecraft:planks", 'Y', "lotr:red_clay");
        shaped(MOREDAIN, "moredain/moredain_banner", "lotr:moredain_banner", 1, rows("X", "Y", "Z"), 'X', "#minecraft:wool", 'Y', "#lotr:sticks", 'Z', "#minecraft:planks");
        shaped(MOREDAIN, "moredain/morwaith_brick", "lotr:morwaith_brick", 4, rows("XX", "XX"), 'X', "lotr:red_clay");
        shaped(MOREDAIN, "moredain/morwaith_brick_slab", "lotr:morwaith_brick_slab", 6, rows("XXX"), 'X', "lotr:morwaith_brick");
        shaped(MOREDAIN, "moredain/morwaith_brick_stairs", "lotr:morwaith_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:morwaith_brick");
        shaped(MOREDAIN, "moredain/morwaith_brick_wall", "lotr:morwaith_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:morwaith_brick");
        shaped(MOREDAIN, "moredain/cracked_morwaith_brick_slab", "lotr:cracked_morwaith_brick_slab", 6, rows("XXX"), 'X', "lotr:cracked_morwaith_brick");
        shaped(MOREDAIN, "moredain/cracked_morwaith_brick_stairs", "lotr:cracked_morwaith_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:cracked_morwaith_brick");
        shaped(MOREDAIN, "moredain/cracked_morwaith_brick_wall", "lotr:cracked_morwaith_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:cracked_morwaith_brick");
        // LOTRRecipes.createMorgulRecipes
        shaped(MORGUL, "morgul/mordor_brick", "lotr:mordor_brick", 4, rows("XX", "XX"), 'X', "lotr:mordor_rock");
        shaped(MORGUL, "morgul/morgul_crafting_table", "lotr:morgul_crafting_table", 1, rows("XX", "YY"), 'X', "#minecraft:planks", 'Y', "lotr:mordor_rock");
        shaped(MORGUL, "morgul/mordor_rock_slab", "lotr:mordor_rock_slab", 6, rows("XXX"), 'X', "lotr:mordor_rock");
        shaped(MORGUL, "morgul/smooth_mordor_rock", "lotr:smooth_mordor_rock", 2, rows("X", "X"), 'X', "lotr:mordor_rock");
        shaped(MORGUL, "morgul/smooth_mordor_rock_slab", "lotr:smooth_mordor_rock_slab", 6, rows("XXX"), 'X', "lotr:smooth_mordor_rock");
        shaped(MORGUL, "morgul/mordor_brick_slab", "lotr:mordor_brick_slab", 6, rows("XXX"), 'X', "lotr:mordor_brick");
        shaped(MORGUL, "morgul/mordor_brick_stairs", "lotr:mordor_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:mordor_brick");
        shaped(MORGUL, "morgul/mordor_rock_wall", "lotr:mordor_rock_wall", 6, rows("XXX", "XXX"), 'X', "lotr:mordor_rock");
        shaped(MORGUL, "morgul/mordor_brick_wall", "lotr:mordor_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:mordor_brick");
        shaped(MORGUL, "morgul/orc_torch", "lotr:orc_torch", 2, rows("X", "Y", "Y"), 'X', "lotr:durnor", 'Y', "#lotr:sticks");
        shaped(MORGUL, "morgul/cracked_mordor_brick_slab", "lotr:cracked_mordor_brick_slab", 6, rows("XXX"), 'X', "lotr:cracked_mordor_brick");
        shaped(MORGUL, "morgul/cracked_mordor_brick_stairs", "lotr:cracked_mordor_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:cracked_mordor_brick");
        shaped(MORGUL, "morgul/cracked_mordor_brick_wall", "lotr:cracked_mordor_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:cracked_mordor_brick");
        shaped(MORGUL, "morgul/orc_forge", "lotr:orc_forge", 1, rows("XXX", "X X", "XXX"), 'X', "lotr:mordor_brick");
        shaped(MORGUL, "morgul/mordor_banner", "lotr:mordor_banner", 1, rows("X", "Y", "Z"), 'X', "#minecraft:wool", 'Y', "#lotr:sticks", 'Z', "#minecraft:planks");
        shaped(MORGUL, "morgul/mordor_pillar", "lotr:mordor_pillar", 3, rows("X", "X", "X"), 'X', "lotr:mordor_rock");
        shaped(MORGUL, "morgul/mordor_pillar_slab", "lotr:mordor_pillar_slab", 6, rows("XXX"), 'X', "lotr:mordor_pillar");
        shaped(MORGUL, "morgul/carved_mordor_brick", "lotr:carved_mordor_brick", 1, rows("XX", "XX"), 'X', "lotr:mordor_brick");
        shaped(MORGUL, "morgul/mordor_rock_stairs", "lotr:mordor_rock_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:mordor_rock");
        shaped(MORGUL, "morgul/minas_morgul_banner", "lotr:minas_morgul_banner", 1, rows("XA", "Y ", "Z "), 'X', "#minecraft:wool", 'Y', "#lotr:sticks", 'Z', "#minecraft:planks", 'A', "minecraft:skeleton_skull");
        shaped(MORGUL, "morgul/nan_ungol_banner", "lotr:nan_ungol_banner", 1, rows("XA", "Y ", "Z "), 'X', "#minecraft:wool", 'Y', "#lotr:sticks", 'Z', "#minecraft:planks", 'A', "minecraft:string");
        // LOTRRecipes.createNearHaradRecipes
        shaped(NEAR_HARAD, "near_harad/near_harad_crafting_table", "lotr:near_harad_crafting_table", 1, rows("XX", "YY"), 'X', "#minecraft:planks", 'Y', "minecraft:sandstone");
        shaped(NEAR_HARAD, "near_harad/near_harad_banner", "lotr:near_harad_banner", 1, rows("X", "Y", "Z"), 'X', "#minecraft:wool", 'Y', "#lotr:sticks", 'Z', "#minecraft:planks");
        shaped(NEAR_HARAD, "near_harad/harad_nomad_banner", "lotr:harad_nomad_banner", 1, rows("XA", "Y ", "Z "), 'X', "#minecraft:wool", 'Y', "#lotr:sticks", 'Z', "#minecraft:planks", 'A', "minecraft:sand");
        // LOTRRecipes.createRangerRecipes
        shaped(RANGER, "ranger/ranger_crafting_table", "lotr:ranger_crafting_table", 1, rows("XX", "XX"), 'X', "#minecraft:planks");
        shaped(RANGER, "ranger/ranger_banner", "lotr:ranger_banner", 1, rows("X", "Y", "Z"), 'X', "#minecraft:wool", 'Y', "#lotr:sticks", 'Z', "#minecraft:planks");
        shaped(RANGER, "ranger/arnor_brick", "lotr:arnor_brick", 4, rows("XX", "XX"), 'X', "minecraft:stone");
        shaped(RANGER, "ranger/arnor_brick_slab", "lotr:arnor_brick_slab", 6, rows("XXX"), 'X', "lotr:arnor_brick");
        shaped(RANGER, "ranger/arnor_brick_stairs", "lotr:arnor_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:arnor_brick");
        shaped(RANGER, "ranger/arnor_brick_wall", "lotr:arnor_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:arnor_brick");
        shaped(RANGER, "ranger/carved_arnor_brick", "lotr:carved_arnor_brick", 1, rows("XX", "XX"), 'X', "lotr:arnor_brick");
        shapeless(RANGER, "ranger/mossy_arnor_brick", "lotr:mossy_arnor_brick", 1, "lotr:arnor_brick", "#lotr:vines");
        shaped(RANGER, "ranger/mossy_arnor_brick_slab", "lotr:mossy_arnor_brick_slab", 6, rows("XXX"), 'X', "lotr:mossy_arnor_brick");
        shaped(RANGER, "ranger/mossy_arnor_brick_stairs", "lotr:mossy_arnor_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:mossy_arnor_brick");
        shaped(RANGER, "ranger/mossy_arnor_brick_wall", "lotr:mossy_arnor_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:mossy_arnor_brick");
        shaped(RANGER, "ranger/cracked_arnor_brick_slab", "lotr:cracked_arnor_brick_slab", 6, rows("XXX"), 'X', "lotr:cracked_arnor_brick");
        shaped(RANGER, "ranger/cracked_arnor_brick_stairs", "lotr:cracked_arnor_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:cracked_arnor_brick");
        shaped(RANGER, "ranger/cracked_arnor_brick_wall", "lotr:cracked_arnor_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:cracked_arnor_brick");
        shaped(RANGER, "ranger/ranger_hood", "lotr:ranger_hood", 1, rows("XXX", "X X"), 'X', "minecraft:leather");
        shaped(RANGER, "ranger/ranger_tunic", "lotr:ranger_tunic", 1, rows("X X", "YYY", "XXX"), 'X', "minecraft:leather", 'Y', "minecraft:iron_ingot");
        shaped(RANGER, "ranger/ranger_leggings", "lotr:ranger_leggings", 1, rows("XXX", "Y Y", "X X"), 'X', "minecraft:leather", 'Y', "minecraft:iron_ingot");
        shaped(RANGER, "ranger/ranger_boots", "lotr:ranger_boots", 1, rows("X X", "X X"), 'X', "minecraft:leather");
        shaped(RANGER, "ranger/arnor_pillar", "lotr:arnor_pillar", 3, rows("X", "X", "X"), 'X', "minecraft:stone");
        shaped(RANGER, "ranger/arnor_pillar_slab", "lotr:arnor_pillar_slab", 6, rows("XXX"), 'X', "lotr:arnor_pillar");
        shaped(RANGER, "ranger/cracked_arnor_pillar_slab", "lotr:cracked_arnor_pillar_slab", 6, rows("XXX"), 'X', "lotr:cracked_arnor_pillar");
        // LOTRRecipes.createRhunRecipes
        shaped(RHUN, "rhun/rhun_crafting_table", "lotr:rhun_crafting_table", 1, rows("XX", "YY"), 'X', "#minecraft:planks", 'Y', "lotr:rhun_brick");
        shaped(RHUN, "rhun/rhun_banner", "lotr:rhun_banner", 1, rows("X", "Y", "Z"), 'X', "#minecraft:wool", 'Y', "#lotr:sticks", 'Z', "#minecraft:planks");
        shaped(RHUN, "rhun/rhun_brick_slab", "lotr:rhun_brick_slab", 6, rows("XXX"), 'X', "lotr:rhun_brick");
        shaped(RHUN, "rhun/rhun_brick_stairs", "lotr:rhun_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:rhun_brick");
        shaped(RHUN, "rhun/rhun_brick_wall", "lotr:rhun_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:rhun_brick");
        shaped(RHUN, "rhun/rhun_carved_brick", "lotr:rhun_carved_brick", 1, rows("XX", "XX"), 'X', "lotr:rhun_brick");
        shaped(RHUN, "rhun/rhun_pillar_slab", "lotr:rhun_pillar_slab", 6, rows("XXX"), 'X', "lotr:rhun_pillar");
        shapeless(RHUN, "rhun/rhun_mossy_brick", "lotr:rhun_mossy_brick", 1, "lotr:rhun_brick", "#lotr:vines");
        shaped(RHUN, "rhun/rhun_mossy_brick_slab", "lotr:rhun_mossy_brick_slab", 6, rows("XXX"), 'X', "lotr:rhun_mossy_brick");
        shaped(RHUN, "rhun/rhun_mossy_brick_stairs", "lotr:rhun_mossy_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:rhun_mossy_brick");
        shaped(RHUN, "rhun/rhun_mossy_brick_wall", "lotr:rhun_mossy_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:rhun_mossy_brick");
        shaped(RHUN, "rhun/rhun_cracked_brick_slab", "lotr:rhun_cracked_brick_slab", 6, rows("XXX"), 'X', "lotr:rhun_cracked_brick");
        shaped(RHUN, "rhun/rhun_cracked_brick_stairs", "lotr:rhun_cracked_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:rhun_cracked_brick");
        shaped(RHUN, "rhun/rhun_cracked_brick_wall", "lotr:rhun_cracked_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:rhun_cracked_brick");
        shapeless(RHUN, "rhun/rhun_flowers_brick", "lotr:rhun_flowers_brick", 1, "lotr:rhun_brick", "lotr:rhun_flower_chrys_orange");
        shaped(RHUN, "rhun/rhun_flowers_brick_slab", "lotr:rhun_flowers_brick_slab", 6, rows("XXX"), 'X', "lotr:rhun_flowers_brick");
        shaped(RHUN, "rhun/rhun_flowers_brick_stairs", "lotr:rhun_flowers_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:rhun_flowers_brick");
        shaped(RHUN, "rhun/rhun_flowers_brick_wall", "lotr:rhun_flowers_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:rhun_flowers_brick");
        shaped(RHUN, "rhun/rhun_gold_brick", "lotr:rhun_gold_brick", 1, rows(" X ", "XYX", " X "), 'X', "minecraft:gold_nugget", 'Y', "lotr:rhun_brick");
        shaped(RHUN, "rhun/rhun_red_brick", "lotr:rhun_red_brick", 4, rows("XX", "XX"), 'X', "lotr:red_rock");
        shaped(RHUN, "rhun/rhun_red_brick_slab", "lotr:rhun_red_brick_slab", 6, rows("XXX"), 'X', "lotr:rhun_red_brick");
        shaped(RHUN, "rhun/rhun_red_brick_stairs", "lotr:rhun_red_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:rhun_red_brick");
        shaped(RHUN, "rhun/rhun_red_brick_wall", "lotr:rhun_red_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:rhun_red_brick");
        shaped(RHUN, "rhun/rhun_red_carved_brick", "lotr:rhun_red_carved_brick", 1, rows("XX", "XX"), 'X', "lotr:rhun_red_brick");
        shaped(RHUN, "rhun/rhun_red_pillar", "lotr:rhun_red_pillar", 3, rows("X", "X", "X"), 'X', "lotr:red_rock");
        shaped(RHUN, "rhun/rhun_red_pillar_slab", "lotr:rhun_red_pillar_slab", 6, rows("XXX"), 'X', "lotr:rhun_red_pillar");
        // LOTRRecipes.createRivendellRecipes
        shaped(RIVENDELL, "rivendell/rivendell_crafting_table", "lotr:rivendell_crafting_table", 1, rows("XX", "XX"), 'X', "#minecraft:planks");
        shaped(RIVENDELL, "rivendell/rivendell_banner", "lotr:rivendell_banner", 1, rows("X", "Y", "Z"), 'X', "#minecraft:wool", 'Y', "#lotr:sticks", 'Z', "#minecraft:planks");
        // LOTRRecipes.createRohirricRecipes
        shaped(ROHIRRIC, "rohirric/rohirric_crafting_table", "lotr:rohirric_crafting_table", 1, rows("XX", "XX"), 'X', "#minecraft:planks");
        shaped(ROHIRRIC, "rohirric/rohan_rock_slab", "lotr:rohan_rock_slab", 6, rows("XXX"), 'X', "lotr:rohan_rock");
        shaped(ROHIRRIC, "rohirric/smooth_rohan_rock", "lotr:smooth_rohan_rock", 2, rows("X", "X"), 'X', "lotr:rohan_rock");
        shaped(ROHIRRIC, "rohirric/smooth_rohan_rock_slab", "lotr:smooth_rohan_rock_slab", 6, rows("XXX"), 'X', "lotr:smooth_rohan_rock");
        shaped(ROHIRRIC, "rohirric/rohan_brick", "lotr:rohan_brick", 4, rows("XX", "XX"), 'X', "lotr:rohan_rock");
        shaped(ROHIRRIC, "rohirric/rohan_brick_slab", "lotr:rohan_brick_slab", 6, rows("XXX"), 'X', "lotr:rohan_brick");
        shaped(ROHIRRIC, "rohirric/rohan_brick_stairs", "lotr:rohan_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:rohan_brick");
        shaped(ROHIRRIC, "rohirric/rohan_rock_wall", "lotr:rohan_rock_wall", 6, rows("XXX", "XXX"), 'X', "lotr:rohan_rock");
        shaped(ROHIRRIC, "rohirric/rohan_brick_wall", "lotr:rohan_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:rohan_brick");
        shaped(ROHIRRIC, "rohirric/rohirric_sword", "lotr:rohirric_sword", 1, rows("X", "X", "Y"), 'X', "minecraft:iron_ingot", 'Y', "#lotr:sticks");
        shaped(ROHIRRIC, "rohirric/rohirric_dagger", "lotr:rohirric_dagger", 1, rows("X", "Y"), 'X', "minecraft:iron_ingot", 'Y', "#lotr:sticks");
        shaped(ROHIRRIC, "rohirric/rohirric_spear", "lotr:rohirric_spear", 1, rows("  X", " Y ", "Y  "), 'X', "minecraft:iron_ingot", 'Y', "#lotr:sticks");
        shaped(ROHIRRIC, "rohirric/rohirric_coif", "lotr:rohirric_coif", 1, rows("XXX", "Y Y"), 'X', "minecraft:iron_ingot", 'Y', "minecraft:leather");
        shaped(ROHIRRIC, "rohirric/rohirric_hauberk", "lotr:rohirric_hauberk", 1, rows("X X", "YYY", "XXX"), 'X', "minecraft:iron_ingot", 'Y', "minecraft:leather");
        shaped(ROHIRRIC, "rohirric/rohirric_leggings", "lotr:rohirric_leggings", 1, rows("XXX", "Y Y", "X X"), 'X', "minecraft:iron_ingot", 'Y', "minecraft:leather");
        shaped(ROHIRRIC, "rohirric/rohirric_boots", "lotr:rohirric_boots", 1, rows("Y Y", "X X"), 'X', "minecraft:iron_ingot", 'Y', "minecraft:leather");
        shaped(ROHIRRIC, "rohirric/rohan_banner", "lotr:rohan_banner", 1, rows("X", "Y", "Z"), 'X', "#minecraft:wool", 'Y', "#lotr:sticks", 'Z', "#minecraft:planks");
        shaped(ROHIRRIC, "rohirric/rohirric_battleaxe", "lotr:rohirric_battleaxe", 1, rows("XXX", "XYX", " Y "), 'X', "minecraft:iron_ingot", 'Y', "#lotr:sticks");
        shaped(ROHIRRIC, "rohirric/rohirric_horse_armor", "lotr:rohirric_horse_armor", 1, rows("X  ", "XYX", "XXX"), 'X', "minecraft:iron_ingot", 'Y', "minecraft:leather");
        shaped(ROHIRRIC, "rohirric/rohan_pillar", "lotr:rohan_pillar", 3, rows("X", "X", "X"), 'X', "lotr:rohan_rock");
        shaped(ROHIRRIC, "rohirric/rohan_pillar_slab", "lotr:rohan_pillar_slab", 6, rows("XXX"), 'X', "lotr:rohan_pillar");
        shaped(ROHIRRIC, "rohirric/rohirric_bow", "lotr:rohirric_bow", 1, rows(" XY", "X Y", " XY"), 'X', "#lotr:sticks", 'Y', "minecraft:string");
        shaped(ROHIRRIC, "rohirric/rohirric_marshal_helmet", "lotr:rohirric_marshal_helmet", 1, rows(" X ", "YAY", " X "), 'X', "minecraft:gold_nugget", 'Y', "minecraft:leather", 'A', "lotr:rohirric_coif");
        shaped(ROHIRRIC, "rohirric/rohirric_marshal_chestplate", "lotr:rohirric_marshal_chestplate", 1, rows(" X ", "YAY", " X "), 'X', "minecraft:gold_nugget", 'Y', "minecraft:leather", 'A', "lotr:rohirric_hauberk");
        shaped(ROHIRRIC, "rohirric/rohirric_marshal_leggings", "lotr:rohirric_marshal_leggings", 1, rows(" X ", "YAY", " X "), 'X', "minecraft:gold_nugget", 'Y', "minecraft:leather", 'A', "lotr:rohirric_leggings");
        shaped(ROHIRRIC, "rohirric/rohirric_marshal_boots", "lotr:rohirric_marshal_boots", 1, rows(" X ", "YAY", " X "), 'X', "minecraft:gold_nugget", 'Y', "minecraft:leather", 'A', "lotr:rohirric_boots");
        shaped(ROHIRRIC, "rohirric/rohan_rock_stairs", "lotr:rohan_rock_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:rohan_rock");
        shaped(ROHIRRIC, "rohirric/carved_rohan_brick", "lotr:carved_rohan_brick", 1, rows("XX", "XX"), 'X', "lotr:rohan_brick");
        shaped(ROHIRRIC, "rohirric/rohan_beam", "lotr:rohan_beam", 3, rows("X", "X", "X"), 'X', "#minecraft:logs");
        shaped(ROHIRRIC, "rohirric/rohan_gold_beam", "lotr:rohan_gold_beam", 1, rows(" X ", "XYX", " X "), 'X', "minecraft:gold_nugget", 'Y', "lotr:rohan_beam");
        // LOTRRecipes.createSmeltingRecipes
        smelting("scorched_stone_from_smelting", "lotr:scorched_stone", "minecraft:stone", 0.1f);
        smelting("glass_from_smelting", "minecraft:glass", "lotr:white_sand", 0.1f);
        smelting("naurite_from_smelting", "lotr:durnor", "lotr:naurite_ore", 1.0f);
        smelting("quendite_crystal_from_smelting", "lotr:edhelvir", "lotr:quendite_ore", 1.0f);
        smelting("glowstone_dust_from_smelting", "minecraft:glowstone_dust", "lotr:glowstone_ore", 1.0f);
        smelting("sulfur_from_smelting", "lotr:sulfur", "lotr:sulfur_ore", 1.0f);
        smelting("saltpeter_from_smelting", "lotr:niter", "lotr:saltpeter_ore", 1.0f);
        smelting("salt_from_smelting", "lotr:salt", "lotr:salt_ore", 1.0f);
        smelting("terracotta_from_smelting", "minecraft:terracotta", "lotr:red_clay", 0.35f);
        smelting("dried_reeds_from_smelting", "lotr:dried_reeds", "lotr:reeds", 0.25f);
        // LOTRRecipes.createStandardRecipes
        shaped(null, "saddle_from_fur", "minecraft:saddle", 1, rows("XXX", "Y Y"), 'X', "lotr:fur", 'Y', "minecraft:iron_ingot");
        shaped(null, "saddle_from_gemsbok_hide", "minecraft:saddle", 1, rows("XXX", "Y Y"), 'X', "lotr:gemsbok_hide", 'Y', "minecraft:iron_ingot");
        shapeless(null, "magenta_dye", "minecraft:magenta_dye", 1, "lotr:shire_heather");
        shapeless(null, "mithril", "lotr:mithril", 9, "lotr:mithril_block");
        shaped(null, "mithril_block", "lotr:mithril_block", 1, rows("XXX", "XXX", "XXX"), 'X', "lotr:mithril");
        shaped(null, "iron_chandelier", "lotr:iron_chandelier", 2, rows(" X ", "YZY"), 'X', "#lotr:sticks", 'Y', "minecraft:torch", 'Z', "minecraft:iron_ingot");
        shaped(null, "gold_chandelier", "lotr:gold_chandelier", 2, rows(" X ", "YZY"), 'X', "#lotr:sticks", 'Y', "minecraft:torch", 'Z', "minecraft:gold_ingot");
        shaped(null, "mithril_sword", "lotr:mithril_sword", 1, rows("X", "X", "Y"), 'X', "lotr:mithril", 'Y', "#lotr:sticks");
        shaped(null, "mithril_spear", "lotr:mithril_spear", 1, rows("  X", " Y ", "Y  "), 'X', "lotr:mithril", 'Y', "#lotr:sticks");
        shaped(null, "mithril_chandelier", "lotr:mithril_chandelier", 2, rows(" X ", "YZY"), 'X', "#lotr:sticks", 'Y', "minecraft:torch", 'Z', "lotr:mithril");
        shaped(null, "hearth", "lotr:hearth", 3, rows("XXX", "YYY"), 'X', "#minecraft:coals", 'Y', "minecraft:brick");
        shaped(null, "iron_dagger", "lotr:iron_dagger", 1, rows("X", "Y"), 'X', "minecraft:iron_ingot", 'Y', "#lotr:sticks");
        shaped(null, "mithril_dagger", "lotr:mithril_dagger", 1, rows("X", "Y"), 'X', "lotr:mithril", 'Y', "#lotr:sticks");
        shaped(null, "mithril_battleaxe", "lotr:mithril_battleaxe", 1, rows("XXX", "XYX", " Y "), 'X', "lotr:mithril", 'Y', "#lotr:sticks");
        shaped(null, "mithril_warhammer", "lotr:mithril_warhammer", 1, rows("XYX", "XYX", " Y "), 'X', "lotr:mithril", 'Y', "#lotr:sticks");
        shaped(null, "crossbow_bolt", "lotr:crossbow_bolt", 4, rows("X", "Y", "Z"), 'X', "minecraft:iron_ingot", 'Y', "#lotr:sticks", 'Z', "#lotr:feathers");
        shaped(null, "crossbow_bolt_from_bronze", "lotr:crossbow_bolt", 4, rows("X", "Y", "Z"), 'X', "lotr:bronze_ingot", 'Y', "#lotr:sticks", 'Z', "#lotr:feathers");
        shaped(null, "iron_crossbow", "lotr:iron_crossbow", 1, rows("XXY", "ZYX", "YZX"), 'X', "minecraft:iron_ingot", 'Y', "#lotr:sticks", 'Z', "minecraft:string");
        shaped(null, "mithril_crossbow", "lotr:mithril_crossbow", 1, rows("XXY", "ZYX", "YZX"), 'X', "lotr:mithril", 'Y', "#lotr:sticks", 'Z', "minecraft:string");
        shapeless(null, "pebble", "lotr:pebble", 4, "minecraft:gravel");
        shaped(null, "sling", "lotr:sling", 1, rows("XYX", "XZX", " X "), 'X', "#lotr:sticks", 'Y', "minecraft:leather", 'Z', "minecraft:string");
        shapeless(null, "naurite", "lotr:durnor", 9, "lotr:naurite_block");
        shaped(null, "naurite_block", "lotr:naurite_block", 1, rows("XXX", "XXX", "XXX"), 'X', "lotr:durnor");
        shapeless(null, "gulduril_crystal", "lotr:gulduril", 9, "lotr:gulduril_block");
        shaped(null, "gulduril_block", "lotr:gulduril_block", 1, rows("XXX", "XXX", "XXX"), 'X', "lotr:gulduril");
        shapeless(null, "quendite_crystal", "lotr:edhelvir", 9, "lotr:quendite_block");
        shaped(null, "quendite_block", "lotr:quendite_block", 1, rows("XXX", "XXX", "XXX"), 'X', "lotr:edhelvir");
        shaped(null, "blue_rock_brick", "lotr:blue_rock_brick", 4, rows("XX", "XX"), 'X', "lotr:blue_rock");
        shaped(null, "shire_pine_fence", "lotr:shire_pine_fence", 3, rows("XYX", "XYX"), 'X', "lotr:shire_pine_planks", 'Y', "#lotr:sticks");
        shaped(null, "mirk_oak_fence", "lotr:mirk_oak_fence", 3, rows("XYX", "XYX"), 'X', "lotr:mirk_oak_planks", 'Y', "#lotr:sticks");
        shaped(null, "charred_fence", "lotr:charred_fence", 3, rows("XYX", "XYX"), 'X', "lotr:charred_planks", 'Y', "#lotr:sticks");
        shaped(null, "apple_fence", "lotr:apple_fence", 3, rows("XYX", "XYX"), 'X', "lotr:apple_planks", 'Y', "#lotr:sticks");
        shaped(null, "pear_fence", "lotr:pear_fence", 3, rows("XYX", "XYX"), 'X', "lotr:pear_planks", 'Y', "#lotr:sticks");
        shaped(null, "cherry_fence", "lotr:cherry_fence", 3, rows("XYX", "XYX"), 'X', "lotr:cherry_planks", 'Y', "#lotr:sticks");
        shaped(null, "mango_fence", "lotr:mango_fence", 3, rows("XYX", "XYX"), 'X', "lotr:mango_planks", 'Y', "#lotr:sticks");
        shaped(null, "lebethron_fence", "lotr:lebethron_fence", 3, rows("XYX", "XYX"), 'X', "lotr:lebethron_planks", 'Y', "#lotr:sticks");
        shaped(null, "beech_fence", "lotr:beech_fence", 3, rows("XYX", "XYX"), 'X', "lotr:beech_planks", 'Y', "#lotr:sticks");
        shaped(null, "holly_fence", "lotr:holly_fence", 3, rows("XYX", "XYX"), 'X', "lotr:holly_planks", 'Y', "#lotr:sticks");
        shaped(null, "banana_fence", "lotr:banana_fence", 3, rows("XYX", "XYX"), 'X', "lotr:banana_planks", 'Y', "#lotr:sticks");
        shaped(null, "maple_fence", "lotr:maple_fence", 3, rows("XYX", "XYX"), 'X', "lotr:maple_planks", 'Y', "#lotr:sticks");
        shaped(null, "larch_fence", "lotr:larch_fence", 3, rows("XYX", "XYX"), 'X', "lotr:larch_planks", 'Y', "#lotr:sticks");
        shaped(null, "date_palm_fence", "lotr:date_palm_fence", 3, rows("XYX", "XYX"), 'X', "lotr:date_palm_planks", 'Y', "#lotr:sticks");
        shaped(null, "mangrove_fence", "lotr:mangrove_fence", 3, rows("XYX", "XYX"), 'X', "lotr:mangrove_planks", 'Y', "#lotr:sticks");
        shaped(null, "blue_rock_pillar", "lotr:blue_rock_pillar", 3, rows("X", "X", "X"), 'X', "lotr:blue_rock");
        shapeless(null, "gunpowder", "minecraft:gunpowder", 2, "lotr:sulfur", "lotr:niter", "minecraft:charcoal");
        shapeless(null, "bone_meal", "minecraft:bone_meal", 2, "lotr:niter", "minecraft:dirt");
        shaped(null, "sulfur_block", "lotr:sulfur_block", 1, rows("XXX", "XXX", "XXX"), 'X', "lotr:sulfur");
        shaped(null, "saltpeter_block", "lotr:saltpeter_block", 1, rows("XXX", "XXX", "XXX"), 'X', "lotr:niter");
        shapeless(null, "sulfur", "lotr:sulfur", 9, "lotr:sulfur_block");
        shapeless(null, "saltpeter", "lotr:niter", 9, "lotr:saltpeter_block");
        shaped(null, "red_rock_brick", "lotr:red_rock_brick", 4, rows("XX", "XX"), 'X', "lotr:red_rock");
        shaped(null, "red_rock_pillar", "lotr:red_rock_pillar", 3, rows("X", "X", "X"), 'X', "lotr:red_rock");
        shaped(null, "thatch_thatch", "lotr:thatch_thatch", 6, rows("XYX", "YXY", "XYX"), 'X', "minecraft:wheat", 'Y', "minecraft:dirt");
        shaped(null, "straw_bed", "lotr:straw_bed", 1, rows("XXX", "YYY"), 'X', "minecraft:wheat", 'Y', "#minecraft:planks");
        shaped(null, "thatch_floor", "lotr:thatch_floor", 3, rows("XX"), 'X', "lotr:thatch_thatch");
        shapeless(null, "red_dye", "minecraft:red_dye", 1, "lotr:red_harad_flower");
        shapeless(null, "pink_dye", "minecraft:pink_dye", 1, "lotr:harad_flower_daisy");
        shapeless(null, "magenta_dye_2", "minecraft:magenta_dye", 1, "lotr:pink_harad_flower");
        shaped(null, "chestnut_fence", "lotr:chestnut_fence", 3, rows("XYX", "XYX"), 'X', "lotr:chestnut_planks", 'Y', "#lotr:sticks");
        shaped(null, "baobab_fence", "lotr:baobab_fence", 3, rows("XYX", "XYX"), 'X', "lotr:baobab_planks", 'Y', "#lotr:sticks");
        shaped(null, "cedar_fence", "lotr:cedar_fence", 3, rows("XYX", "XYX"), 'X', "lotr:cedar_planks", 'Y', "#lotr:sticks");
        shaped(null, "fir_fence", "lotr:fir_fence", 3, rows("XYX", "XYX"), 'X', "lotr:fir_planks", 'Y', "#lotr:sticks");
        shaped(null, "pine_fence", "lotr:pine_fence", 3, rows("XYX", "XYX"), 'X', "lotr:pine_planks", 'Y', "#lotr:sticks");
        shaped(null, "lemon_fence", "lotr:lemon_fence", 3, rows("XYX", "XYX"), 'X', "lotr:lemon_planks", 'Y', "#lotr:sticks");
        shaped(null, "orange_fence", "lotr:orange_fence", 3, rows("XYX", "XYX"), 'X', "lotr:orange_planks", 'Y', "#lotr:sticks");
        shaped(null, "lime_fence", "lotr:lime_fence", 3, rows("XYX", "XYX"), 'X', "lotr:lime_planks", 'Y', "#lotr:sticks");
        shaped(null, "mahogany_fence", "lotr:mahogany_fence", 3, rows("XYX", "XYX"), 'X', "lotr:mahogany_planks", 'Y', "#lotr:sticks");
        shaped(null, "willow_fence", "lotr:willow_fence", 3, rows("XYX", "XYX"), 'X', "lotr:willow_planks", 'Y', "#lotr:sticks");
        shaped(null, "cypress_fence", "lotr:cypress_fence", 3, rows("XYX", "XYX"), 'X', "lotr:cypress_planks", 'Y', "#lotr:sticks");
        shaped(null, "olive_fence", "lotr:olive_fence", 3, rows("XYX", "XYX"), 'X', "lotr:olive_planks", 'Y', "#lotr:sticks");
        shaped(null, "aspen_fence", "lotr:aspen_fence", 3, rows("XYX", "XYX"), 'X', "lotr:aspen_planks", 'Y', "#lotr:sticks");
        shaped(null, "green_oak_fence", "lotr:green_oak_fence", 3, rows("XYX", "XYX"), 'X', "lotr:green_oak_planks", 'Y', "#lotr:sticks");
        shaped(null, "lairelosse_fence", "lotr:lairelosse_fence", 3, rows("XYX", "XYX"), 'X', "lotr:lairelosse_planks", 'Y', "#lotr:sticks");
        shaped(null, "almond_fence", "lotr:almond_fence", 3, rows("XYX", "XYX"), 'X', "lotr:almond_planks", 'Y', "#lotr:sticks");
        shaped(null, "iron_battleaxe", "lotr:iron_battleaxe", 1, rows("XXX", "XYX", " Y "), 'X', "minecraft:iron_ingot", 'Y', "#lotr:sticks");
        shaped(null, "butterfly_jar", "lotr:butterfly_jar", 1, rows("X", "Y"), 'X', "#minecraft:planks", 'Y', "minecraft:glass");
        shaped(null, "blue_carved_brick", "lotr:blue_carved_brick", 1, rows("XX", "XX"), 'X', "lotr:blue_rock_brick");
        shaped(null, "red_carved_brick", "lotr:red_carved_brick", 1, rows("XX", "XX"), 'X', "lotr:red_rock_brick");
        shaped(null, "bone_helmet", "lotr:bone_helmet", 1, rows("XXX", "X X"), 'X', "#lotr:bones");
        shaped(null, "bone_chestplate", "lotr:bone_chestplate", 1, rows("X X", "XXX", "XXX"), 'X', "#lotr:bones");
        shaped(null, "bone_leggings", "lotr:bone_leggings", 1, rows("XXX", "X X", "X X"), 'X', "#lotr:bones");
        shaped(null, "bone_boots", "lotr:bone_boots", 1, rows("X X", "X X"), 'X', "#lotr:bones");
        shaped(null, "unsmeltery", "lotr:unsmeltery", 1, rows("X X", "YXY", "ZZZ"), 'X', "minecraft:iron_ingot", 'Y', "#lotr:sticks", 'Z', "minecraft:cobblestone");
        shaped(null, "iron_throwing_axe", "lotr:iron_throwing_axe", 1, rows(" X ", " YX", "Y  "), 'X', "minecraft:iron_ingot", 'Y', "#lotr:sticks");
        shaped(null, "gold_bars", "lotr:gold_bars", 16, rows("XXX", "XXX"), 'X', "minecraft:gold_ingot");
        shaped(null, "mithril_bars", "lotr:mithril_bars", 16, rows("XXX", "XXX"), 'X', "lotr:mithril");
        shaped(null, "rotten_fence", "lotr:rotten_fence", 3, rows("XYX", "XYX"), 'X', "lotr:rotten_planks", 'Y', "#lotr:sticks");
        shaped(null, "alloy_forge", "lotr:alloy_forge", 1, rows("XXX", "X X", "XXX"), 'X', "#minecraft:stone_bricks");
        shaped(null, "thatch_reed", "lotr:thatch_reed", 4, rows("XX", "XX"), 'X', "lotr:dried_reeds");
        shaped(null, "iron_pike", "lotr:iron_pike", 1, rows("  X", " YX", "Y  "), 'X', "minecraft:iron_ingot", 'Y', "#lotr:sticks");
        shaped(null, "reed_bars", "lotr:reed_bars", 16, rows("XXX", "XXX"), 'X', "lotr:thatch_reed");
        shaped(null, "paper", "minecraft:paper", 3, rows("XXX"), 'X', "lotr:reeds");
        shaped(null, "paper_2", "minecraft:paper", 3, rows("XXX"), 'X', "lotr:corn_stalk");
        shaped(null, "weapon_rack", "lotr:weapon_rack", 1, rows("X X", "YYY"), 'X', "#lotr:sticks", 'Y', "#minecraft:planks");
        shaped(null, "waste_block", "lotr:waste_block", 4, rows("XY", "YZ"), 'X', "minecraft:rotten_flesh", 'Y', "minecraft:dirt", 'Z', "#lotr:bones");
        shaped(null, "chalk_brick", "lotr:chalk_brick", 4, rows("XX", "XX"), 'X', "lotr:chalk");
        shaped(null, "chalk_pillar", "lotr:chalk_pillar", 3, rows("X", "X", "X"), 'X', "lotr:chalk");
        shaped(null, "clay_tile", "lotr:clay_tile", 4, rows("XX", "XX"), 'X', "minecraft:terracotta");
        shaped(null, "clay_tile_dyed_white", "lotr:clay_tile_dyed_white", 8, rows("XXX", "XYX", "XXX"), 'X', "lotr:clay_tile", 'Y', "#c:dyes/white");
        shaped(null, "clay_tile_dyed_white_2", "lotr:clay_tile_dyed_white", 4, rows("XX", "XX"), 'X', "minecraft:white_terracotta");
        shaped(null, "clay_tile_dyed_orange", "lotr:clay_tile_dyed_orange", 8, rows("XXX", "XYX", "XXX"), 'X', "lotr:clay_tile", 'Y', "#c:dyes/orange");
        shaped(null, "clay_tile_dyed_orange_2", "lotr:clay_tile_dyed_orange", 4, rows("XX", "XX"), 'X', "minecraft:orange_terracotta");
        shaped(null, "clay_tile_dyed_magenta", "lotr:clay_tile_dyed_magenta", 8, rows("XXX", "XYX", "XXX"), 'X', "lotr:clay_tile", 'Y', "#c:dyes/magenta");
        shaped(null, "clay_tile_dyed_magenta_2", "lotr:clay_tile_dyed_magenta", 4, rows("XX", "XX"), 'X', "minecraft:magenta_terracotta");
        shaped(null, "clay_tile_dyed_light_blue", "lotr:clay_tile_dyed_light_blue", 8, rows("XXX", "XYX", "XXX"), 'X', "lotr:clay_tile", 'Y', "#c:dyes/light_blue");
        shaped(null, "clay_tile_dyed_light_blue_2", "lotr:clay_tile_dyed_light_blue", 4, rows("XX", "XX"), 'X', "minecraft:light_blue_terracotta");
        shaped(null, "clay_tile_dyed_yellow", "lotr:clay_tile_dyed_yellow", 8, rows("XXX", "XYX", "XXX"), 'X', "lotr:clay_tile", 'Y', "#c:dyes/yellow");
        shaped(null, "clay_tile_dyed_yellow_2", "lotr:clay_tile_dyed_yellow", 4, rows("XX", "XX"), 'X', "minecraft:yellow_terracotta");
        shaped(null, "clay_tile_dyed_lime", "lotr:clay_tile_dyed_lime", 8, rows("XXX", "XYX", "XXX"), 'X', "lotr:clay_tile", 'Y', "#c:dyes/lime");
        shaped(null, "clay_tile_dyed_lime_2", "lotr:clay_tile_dyed_lime", 4, rows("XX", "XX"), 'X', "minecraft:lime_terracotta");
        shaped(null, "clay_tile_dyed_pink", "lotr:clay_tile_dyed_pink", 8, rows("XXX", "XYX", "XXX"), 'X', "lotr:clay_tile", 'Y', "#c:dyes/pink");
        shaped(null, "clay_tile_dyed_pink_2", "lotr:clay_tile_dyed_pink", 4, rows("XX", "XX"), 'X', "minecraft:pink_terracotta");
        shaped(null, "clay_tile_dyed_gray", "lotr:clay_tile_dyed_gray", 8, rows("XXX", "XYX", "XXX"), 'X', "lotr:clay_tile", 'Y', "#c:dyes/gray");
        shaped(null, "clay_tile_dyed_gray_2", "lotr:clay_tile_dyed_gray", 4, rows("XX", "XX"), 'X', "minecraft:gray_terracotta");
        shaped(null, "clay_tile_dyed_silver", "lotr:clay_tile_dyed_silver", 8, rows("XXX", "XYX", "XXX"), 'X', "lotr:clay_tile", 'Y', "#c:dyes/light_gray");
        shaped(null, "clay_tile_dyed_silver_2", "lotr:clay_tile_dyed_silver", 4, rows("XX", "XX"), 'X', "minecraft:light_gray_terracotta");
        shaped(null, "clay_tile_dyed_cyan", "lotr:clay_tile_dyed_cyan", 8, rows("XXX", "XYX", "XXX"), 'X', "lotr:clay_tile", 'Y', "#c:dyes/cyan");
        shaped(null, "clay_tile_dyed_cyan_2", "lotr:clay_tile_dyed_cyan", 4, rows("XX", "XX"), 'X', "minecraft:cyan_terracotta");
        shaped(null, "clay_tile_dyed_purple", "lotr:clay_tile_dyed_purple", 8, rows("XXX", "XYX", "XXX"), 'X', "lotr:clay_tile", 'Y', "#c:dyes/purple");
        shaped(null, "clay_tile_dyed_purple_2", "lotr:clay_tile_dyed_purple", 4, rows("XX", "XX"), 'X', "minecraft:purple_terracotta");
        shaped(null, "clay_tile_dyed_blue", "lotr:clay_tile_dyed_blue", 8, rows("XXX", "XYX", "XXX"), 'X', "lotr:clay_tile", 'Y', "#c:dyes/blue");
        shaped(null, "clay_tile_dyed_blue_2", "lotr:clay_tile_dyed_blue", 4, rows("XX", "XX"), 'X', "minecraft:blue_terracotta");
        shaped(null, "clay_tile_dyed_brown", "lotr:clay_tile_dyed_brown", 8, rows("XXX", "XYX", "XXX"), 'X', "lotr:clay_tile", 'Y', "#c:dyes/brown");
        shaped(null, "clay_tile_dyed_brown_2", "lotr:clay_tile_dyed_brown", 4, rows("XX", "XX"), 'X', "minecraft:brown_terracotta");
        shaped(null, "clay_tile_dyed_green", "lotr:clay_tile_dyed_green", 8, rows("XXX", "XYX", "XXX"), 'X', "lotr:clay_tile", 'Y', "#c:dyes/green");
        shaped(null, "clay_tile_dyed_green_2", "lotr:clay_tile_dyed_green", 4, rows("XX", "XX"), 'X', "minecraft:green_terracotta");
        shaped(null, "clay_tile_dyed_red", "lotr:clay_tile_dyed_red", 8, rows("XXX", "XYX", "XXX"), 'X', "lotr:clay_tile", 'Y', "#c:dyes/red");
        shaped(null, "clay_tile_dyed_red_2", "lotr:clay_tile_dyed_red", 4, rows("XX", "XX"), 'X', "minecraft:red_terracotta");
        shaped(null, "clay_tile_dyed_black", "lotr:clay_tile_dyed_black", 8, rows("XXX", "XYX", "XXX"), 'X', "lotr:clay_tile", 'Y', "#c:dyes/black");
        shaped(null, "clay_tile_dyed_black_2", "lotr:clay_tile_dyed_black", 4, rows("XX", "XX"), 'X', "minecraft:black_terracotta");
        shaped(null, "stone_pillar", "lotr:stone_pillar", 3, rows("X", "X", "X"), 'X', "minecraft:stone");
        shapeless(null, "light_blue_dye", "minecraft:light_blue_dye", 1, "lotr:dwarf_herb");
        shapeless(null, "lime_dye", "minecraft:lime_dye", 1, "lotr:fangorn_plant_green");
        shapeless(null, "orange_dye", "minecraft:orange_dye", 1, "lotr:fangorn_plant_red");
        shapeless(null, "light_gray_dye", "minecraft:light_gray_dye", 1, "lotr:fangorn_plant_silver");
        shaped(null, "brick_pillar", "lotr:brick_pillar", 3, rows("X", "X", "X"), 'X', "minecraft:bricks");
        shaped(null, "mud_brick", "lotr:mud_brick", 4, rows("XX", "XX"), 'X', "lotr:mud");
        shapeless(null, "red_brick_mossy", "lotr:red_brick_mossy", 1, "minecraft:bricks", "#lotr:vines");
        shaped(null, "grapevine", "lotr:grapevine", 1, rows("X", "X", "X"), 'X', "#lotr:sticks");
        shaped(null, "plum_fence", "lotr:plum_fence", 3, rows("XYX", "XYX"), 'X', "lotr:plum_planks", 'Y', "#lotr:sticks");
        shaped(null, "redwood_fence", "lotr:redwood_fence", 3, rows("XYX", "XYX"), 'X', "lotr:redwood_planks", 'Y', "#lotr:sticks");
        shaped(null, "pomegranate_fence", "lotr:pomegranate_fence", 3, rows("XYX", "XYX"), 'X', "lotr:pomegranate_planks", 'Y', "#lotr:sticks");
        shaped(null, "palm_fence", "lotr:palm_fence", 3, rows("XYX", "XYX"), 'X', "lotr:palm_planks", 'Y', "#lotr:sticks");
        shaped(null, "dragon_fence", "lotr:dragon_fence", 3, rows("XYX", "XYX"), 'X', "lotr:dragon_planks", 'Y', "#lotr:sticks");
        shaped(null, "kanuka_fence", "lotr:kanuka_fence", 3, rows("XYX", "XYX"), 'X', "lotr:kanuka_planks", 'Y', "#lotr:sticks");
        shaped(null, "white_sandstone", "lotr:white_sandstone", 1, rows("XX", "XX"), 'X', "lotr:white_sand");
        shaped(null, "reed_basket", "lotr:reed_basket", 1, rows("XXX", "X X", "XXX"), 'X', "lotr:dried_reeds");
        shapeless(null, "orange_dye_2", "minecraft:orange_dye", 1, "lotr:marigold");
        shapeless(null, "orange_dye_3", "minecraft:orange_dye", 1, "lotr:rhun_flower_chrys_orange");
        shapeless(null, "pink_dye_2", "minecraft:pink_dye", 1, "lotr:rhun_flower_chrys_pink");
        shaped(null, "glowstone_ore", "lotr:glowstone_ore", 1, rows("XXX", "XYX", "XXX"), 'X', "minecraft:glowstone_dust", 'Y', "minecraft:stone");
        shaped(null, "iron_bird_cage", "lotr:iron_bird_cage", 1, rows("YYY", "Y Y", "XXX"), 'X', "minecraft:iron_ingot", 'Y', "minecraft:iron_bars");
        shaped(null, "gold_bird_cage", "lotr:gold_bird_cage", 1, rows("YYY", "Y Y", "XXX"), 'X', "minecraft:gold_ingot", 'Y', "lotr:gold_bars");
        shaped(null, "wooden_bird_cage", "lotr:wooden_bird_cage", 1, rows("YYY", "Y Y", "XXX"), 'X', "#minecraft:planks", 'Y', "#lotr:sticks");
        shaped(null, "daub", "lotr:daub", 4, rows("XYX", "YXY", "XYX"), 'X', "#lotr:sticks", 'Y', "minecraft:dirt");
        shaped(null, "daub_2", "lotr:daub", 4, rows("XYX", "YXY", "XYX"), 'X', "lotr:dried_reeds", 'Y', "minecraft:dirt");
        shaped(null, "kebab_block", "lotr:kebab_block", 1, rows("XXX", "XXX", "XXX"), 'X', "lotr:kebab");
        shapeless(null, "kebab", "lotr:kebab", 9, "lotr:kebab_block");
        shaped(null, "salt_block", "lotr:salt_block", 1, rows("XXX", "XXX", "XXX"), 'X', "lotr:salt");
        shapeless(null, "salt", "lotr:salt", 9, "lotr:salt_block");
        shaped(null, "shire_pine_fence_gate", "lotr:shire_pine_fence_gate", 1, rows("XYX", "XYX"), 'X', "#lotr:sticks", 'Y', "lotr:shire_pine_planks");
        shaped(null, "mirk_oak_fence_gate", "lotr:mirk_oak_fence_gate", 1, rows("XYX", "XYX"), 'X', "#lotr:sticks", 'Y', "lotr:mirk_oak_planks");
        shaped(null, "charred_fence_gate", "lotr:charred_fence_gate", 1, rows("XYX", "XYX"), 'X', "#lotr:sticks", 'Y', "lotr:charred_planks");
        shaped(null, "apple_fence_gate", "lotr:apple_fence_gate", 1, rows("XYX", "XYX"), 'X', "#lotr:sticks", 'Y', "lotr:apple_planks");
        shaped(null, "pear_fence_gate", "lotr:pear_fence_gate", 1, rows("XYX", "XYX"), 'X', "#lotr:sticks", 'Y', "lotr:pear_planks");
        shaped(null, "cherry_fence_gate", "lotr:cherry_fence_gate", 1, rows("XYX", "XYX"), 'X', "#lotr:sticks", 'Y', "lotr:cherry_planks");
        shaped(null, "mango_fence_gate", "lotr:mango_fence_gate", 1, rows("XYX", "XYX"), 'X', "#lotr:sticks", 'Y', "lotr:mango_planks");
        shaped(null, "lebethron_fence_gate", "lotr:lebethron_fence_gate", 1, rows("XYX", "XYX"), 'X', "#lotr:sticks", 'Y', "lotr:lebethron_planks");
        shaped(null, "beech_fence_gate", "lotr:beech_fence_gate", 1, rows("XYX", "XYX"), 'X', "#lotr:sticks", 'Y', "lotr:beech_planks");
        shaped(null, "holly_fence_gate", "lotr:holly_fence_gate", 1, rows("XYX", "XYX"), 'X', "#lotr:sticks", 'Y', "lotr:holly_planks");
        shaped(null, "banana_fence_gate", "lotr:banana_fence_gate", 1, rows("XYX", "XYX"), 'X', "#lotr:sticks", 'Y', "lotr:banana_planks");
        shaped(null, "maple_fence_gate", "lotr:maple_fence_gate", 1, rows("XYX", "XYX"), 'X', "#lotr:sticks", 'Y', "lotr:maple_planks");
        shaped(null, "larch_fence_gate", "lotr:larch_fence_gate", 1, rows("XYX", "XYX"), 'X', "#lotr:sticks", 'Y', "lotr:larch_planks");
        shaped(null, "date_palm_fence_gate", "lotr:date_palm_fence_gate", 1, rows("XYX", "XYX"), 'X', "#lotr:sticks", 'Y', "lotr:date_palm_planks");
        shaped(null, "mangrove_fence_gate", "lotr:mangrove_fence_gate", 1, rows("XYX", "XYX"), 'X', "#lotr:sticks", 'Y', "lotr:mangrove_planks");
        shaped(null, "chestnut_fence_gate", "lotr:chestnut_fence_gate", 1, rows("XYX", "XYX"), 'X', "#lotr:sticks", 'Y', "lotr:chestnut_planks");
        shaped(null, "baobab_fence_gate", "lotr:baobab_fence_gate", 1, rows("XYX", "XYX"), 'X', "#lotr:sticks", 'Y', "lotr:baobab_planks");
        shaped(null, "cedar_fence_gate", "lotr:cedar_fence_gate", 1, rows("XYX", "XYX"), 'X', "#lotr:sticks", 'Y', "lotr:cedar_planks");
        shaped(null, "fir_fence_gate", "lotr:fir_fence_gate", 1, rows("XYX", "XYX"), 'X', "#lotr:sticks", 'Y', "lotr:fir_planks");
        shaped(null, "pine_fence_gate", "lotr:pine_fence_gate", 1, rows("XYX", "XYX"), 'X', "#lotr:sticks", 'Y', "lotr:pine_planks");
        shaped(null, "lemon_fence_gate", "lotr:lemon_fence_gate", 1, rows("XYX", "XYX"), 'X', "#lotr:sticks", 'Y', "lotr:lemon_planks");
        shaped(null, "orange_fence_gate", "lotr:orange_fence_gate", 1, rows("XYX", "XYX"), 'X', "#lotr:sticks", 'Y', "lotr:orange_planks");
        shaped(null, "lime_fence_gate", "lotr:lime_fence_gate", 1, rows("XYX", "XYX"), 'X', "#lotr:sticks", 'Y', "lotr:lime_planks");
        shaped(null, "mahogany_fence_gate", "lotr:mahogany_fence_gate", 1, rows("XYX", "XYX"), 'X', "#lotr:sticks", 'Y', "lotr:mahogany_planks");
        shaped(null, "willow_fence_gate", "lotr:willow_fence_gate", 1, rows("XYX", "XYX"), 'X', "#lotr:sticks", 'Y', "lotr:willow_planks");
        shaped(null, "cypress_fence_gate", "lotr:cypress_fence_gate", 1, rows("XYX", "XYX"), 'X', "#lotr:sticks", 'Y', "lotr:cypress_planks");
        shaped(null, "olive_fence_gate", "lotr:olive_fence_gate", 1, rows("XYX", "XYX"), 'X', "#lotr:sticks", 'Y', "lotr:olive_planks");
        shaped(null, "aspen_fence_gate", "lotr:aspen_fence_gate", 1, rows("XYX", "XYX"), 'X', "#lotr:sticks", 'Y', "lotr:aspen_planks");
        shaped(null, "green_oak_fence_gate", "lotr:green_oak_fence_gate", 1, rows("XYX", "XYX"), 'X', "#lotr:sticks", 'Y', "lotr:green_oak_planks");
        shaped(null, "lairelosse_fence_gate", "lotr:lairelosse_fence_gate", 1, rows("XYX", "XYX"), 'X', "#lotr:sticks", 'Y', "lotr:lairelosse_planks");
        shaped(null, "almond_fence_gate", "lotr:almond_fence_gate", 1, rows("XYX", "XYX"), 'X', "#lotr:sticks", 'Y', "lotr:almond_planks");
        shaped(null, "rotten_fence_gate", "lotr:rotten_fence_gate", 1, rows("XYX", "XYX"), 'X', "#lotr:sticks", 'Y', "lotr:rotten_planks");
        shaped(null, "plum_fence_gate", "lotr:plum_fence_gate", 1, rows("XYX", "XYX"), 'X', "#lotr:sticks", 'Y', "lotr:plum_planks");
        shaped(null, "redwood_fence_gate", "lotr:redwood_fence_gate", 1, rows("XYX", "XYX"), 'X', "#lotr:sticks", 'Y', "lotr:redwood_planks");
        shaped(null, "pomegranate_fence_gate", "lotr:pomegranate_fence_gate", 1, rows("XYX", "XYX"), 'X', "#lotr:sticks", 'Y', "lotr:pomegranate_planks");
        shaped(null, "millstone", "lotr:millstone", 1, rows("XYX", "XZX", "XXX"), 'X', "minecraft:cobblestone", 'Y', "lotr:bronze_ingot", 'Z', "#lotr:sticks");
        shapeless(null, "topaz", "lotr:topaz", 9, "lotr:topaz_block");
        shaped(null, "topaz_block", "lotr:topaz_block", 1, rows("XXX", "XXX", "XXX"), 'X', "lotr:topaz");
        shapeless(null, "amethyst", "lotr:amethyst", 9, "lotr:amethyst_block");
        shaped(null, "amethyst_block", "lotr:amethyst_block", 1, rows("XXX", "XXX", "XXX"), 'X', "lotr:amethyst");
        shapeless(null, "sapphire", "lotr:sapphire", 9, "lotr:sapphire_block");
        shaped(null, "sapphire_block", "lotr:sapphire_block", 1, rows("XXX", "XXX", "XXX"), 'X', "lotr:sapphire");
        shapeless(null, "ruby", "lotr:ruby", 9, "lotr:ruby_block");
        shaped(null, "ruby_block", "lotr:ruby_block", 1, rows("XXX", "XXX", "XXX"), 'X', "lotr:ruby");
        shapeless(null, "amber", "lotr:amber", 9, "lotr:amber_block");
        shaped(null, "amber_block", "lotr:amber_block", 1, rows("XXX", "XXX", "XXX"), 'X', "lotr:amber");
        shapeless(null, "diamond", "lotr:diamond", 9, "lotr:diamond_block");
        shaped(null, "diamond_block", "lotr:diamond_block", 1, rows("XXX", "XXX", "XXX"), 'X', "lotr:diamond");
        shapeless(null, "pearl", "lotr:pearl", 9, "lotr:pearl_block");
        shaped(null, "pearl_block", "lotr:pearl_block", 1, rows("XXX", "XXX", "XXX"), 'X', "lotr:pearl");
        shapeless(null, "opal", "lotr:opal", 9, "lotr:opal_block");
        shaped(null, "opal_block", "lotr:opal_block", 1, rows("XXX", "XXX", "XXX"), 'X', "lotr:opal");
        shapeless(null, "coral", "lotr:coral", 4, "lotr:coral_block");
        shaped(null, "coral_block", "lotr:coral_block", 1, rows("XX", "XX"), 'X', "lotr:coral");
        shapeless(null, "emerald", "lotr:emerald", 9, "lotr:emerald_block");
        shaped(null, "emerald_block", "lotr:emerald_block", 1, rows("XXX", "XXX", "XXX"), 'X', "lotr:emerald");
        shaped(null, "glass", "lotr:glass", 4, rows("XX", "XX"), 'X', "minecraft:glass");
        shaped(null, "glass_pane", "lotr:glass_pane", 16, rows("XXX", "XXX"), 'X', "lotr:glass");
        shaped(null, "white_stained_glass", "lotr:white_stained_glass", 4, rows("XX", "XX"), 'X', "minecraft:white_stained_glass");
        shaped(null, "white_stained_glass_2", "lotr:white_stained_glass", 8, rows("XXX", "XYX", "XXX"), 'X', "lotr:glass", 'Y', "#c:dyes/white");
        shaped(null, "white_stained_glass_pane", "lotr:white_stained_glass_pane", 16, rows("XXX", "XXX"), 'X', "lotr:white_stained_glass");
        shaped(null, "orange_stained_glass", "lotr:orange_stained_glass", 4, rows("XX", "XX"), 'X', "minecraft:orange_stained_glass");
        shaped(null, "orange_stained_glass_2", "lotr:orange_stained_glass", 8, rows("XXX", "XYX", "XXX"), 'X', "lotr:glass", 'Y', "#c:dyes/orange");
        shaped(null, "orange_stained_glass_pane", "lotr:orange_stained_glass_pane", 16, rows("XXX", "XXX"), 'X', "lotr:orange_stained_glass");
        shaped(null, "magenta_stained_glass", "lotr:magenta_stained_glass", 4, rows("XX", "XX"), 'X', "minecraft:magenta_stained_glass");
        shaped(null, "magenta_stained_glass_2", "lotr:magenta_stained_glass", 8, rows("XXX", "XYX", "XXX"), 'X', "lotr:glass", 'Y', "#c:dyes/magenta");
        shaped(null, "magenta_stained_glass_pane", "lotr:magenta_stained_glass_pane", 16, rows("XXX", "XXX"), 'X', "lotr:magenta_stained_glass");
        shaped(null, "light_blue_stained_glass", "lotr:light_blue_stained_glass", 4, rows("XX", "XX"), 'X', "minecraft:light_blue_stained_glass");
        shaped(null, "light_blue_stained_glass_2", "lotr:light_blue_stained_glass", 8, rows("XXX", "XYX", "XXX"), 'X', "lotr:glass", 'Y', "#c:dyes/light_blue");
        shaped(null, "light_blue_stained_glass_pane", "lotr:light_blue_stained_glass_pane", 16, rows("XXX", "XXX"), 'X', "lotr:light_blue_stained_glass");
        shaped(null, "yellow_stained_glass", "lotr:yellow_stained_glass", 4, rows("XX", "XX"), 'X', "minecraft:yellow_stained_glass");
        shaped(null, "yellow_stained_glass_2", "lotr:yellow_stained_glass", 8, rows("XXX", "XYX", "XXX"), 'X', "lotr:glass", 'Y', "#c:dyes/yellow");
        shaped(null, "yellow_stained_glass_pane", "lotr:yellow_stained_glass_pane", 16, rows("XXX", "XXX"), 'X', "lotr:yellow_stained_glass");
        shaped(null, "lime_stained_glass", "lotr:lime_stained_glass", 4, rows("XX", "XX"), 'X', "minecraft:lime_stained_glass");
        shaped(null, "lime_stained_glass_2", "lotr:lime_stained_glass", 8, rows("XXX", "XYX", "XXX"), 'X', "lotr:glass", 'Y', "#c:dyes/lime");
        shaped(null, "lime_stained_glass_pane", "lotr:lime_stained_glass_pane", 16, rows("XXX", "XXX"), 'X', "lotr:lime_stained_glass");
        shaped(null, "pink_stained_glass", "lotr:pink_stained_glass", 4, rows("XX", "XX"), 'X', "minecraft:pink_stained_glass");
        shaped(null, "pink_stained_glass_2", "lotr:pink_stained_glass", 8, rows("XXX", "XYX", "XXX"), 'X', "lotr:glass", 'Y', "#c:dyes/pink");
        shaped(null, "pink_stained_glass_pane", "lotr:pink_stained_glass_pane", 16, rows("XXX", "XXX"), 'X', "lotr:pink_stained_glass");
        shaped(null, "gray_stained_glass", "lotr:gray_stained_glass", 4, rows("XX", "XX"), 'X', "minecraft:gray_stained_glass");
        shaped(null, "gray_stained_glass_2", "lotr:gray_stained_glass", 8, rows("XXX", "XYX", "XXX"), 'X', "lotr:glass", 'Y', "#c:dyes/gray");
        shaped(null, "gray_stained_glass_pane", "lotr:gray_stained_glass_pane", 16, rows("XXX", "XXX"), 'X', "lotr:gray_stained_glass");
        shaped(null, "silver_stained_glass", "lotr:silver_stained_glass", 4, rows("XX", "XX"), 'X', "minecraft:light_gray_stained_glass");
        shaped(null, "silver_stained_glass_2", "lotr:silver_stained_glass", 8, rows("XXX", "XYX", "XXX"), 'X', "lotr:glass", 'Y', "#c:dyes/light_gray");
        shaped(null, "silver_stained_glass_pane", "lotr:silver_stained_glass_pane", 16, rows("XXX", "XXX"), 'X', "lotr:silver_stained_glass");
        shaped(null, "cyan_stained_glass", "lotr:cyan_stained_glass", 4, rows("XX", "XX"), 'X', "minecraft:cyan_stained_glass");
        shaped(null, "cyan_stained_glass_2", "lotr:cyan_stained_glass", 8, rows("XXX", "XYX", "XXX"), 'X', "lotr:glass", 'Y', "#c:dyes/cyan");
        shaped(null, "cyan_stained_glass_pane", "lotr:cyan_stained_glass_pane", 16, rows("XXX", "XXX"), 'X', "lotr:cyan_stained_glass");
        shaped(null, "purple_stained_glass", "lotr:purple_stained_glass", 4, rows("XX", "XX"), 'X', "minecraft:purple_stained_glass");
        shaped(null, "purple_stained_glass_2", "lotr:purple_stained_glass", 8, rows("XXX", "XYX", "XXX"), 'X', "lotr:glass", 'Y', "#c:dyes/purple");
        shaped(null, "purple_stained_glass_pane", "lotr:purple_stained_glass_pane", 16, rows("XXX", "XXX"), 'X', "lotr:purple_stained_glass");
        shaped(null, "blue_stained_glass", "lotr:blue_stained_glass", 4, rows("XX", "XX"), 'X', "minecraft:blue_stained_glass");
        shaped(null, "blue_stained_glass_2", "lotr:blue_stained_glass", 8, rows("XXX", "XYX", "XXX"), 'X', "lotr:glass", 'Y', "#c:dyes/blue");
        shaped(null, "blue_stained_glass_pane", "lotr:blue_stained_glass_pane", 16, rows("XXX", "XXX"), 'X', "lotr:blue_stained_glass");
        shaped(null, "brown_stained_glass", "lotr:brown_stained_glass", 4, rows("XX", "XX"), 'X', "minecraft:brown_stained_glass");
        shaped(null, "brown_stained_glass_2", "lotr:brown_stained_glass", 8, rows("XXX", "XYX", "XXX"), 'X', "lotr:glass", 'Y', "#c:dyes/brown");
        shaped(null, "brown_stained_glass_pane", "lotr:brown_stained_glass_pane", 16, rows("XXX", "XXX"), 'X', "lotr:brown_stained_glass");
        shaped(null, "green_stained_glass", "lotr:green_stained_glass", 4, rows("XX", "XX"), 'X', "minecraft:green_stained_glass");
        shaped(null, "green_stained_glass_2", "lotr:green_stained_glass", 8, rows("XXX", "XYX", "XXX"), 'X', "lotr:glass", 'Y', "#c:dyes/green");
        shaped(null, "green_stained_glass_pane", "lotr:green_stained_glass_pane", 16, rows("XXX", "XXX"), 'X', "lotr:green_stained_glass");
        shaped(null, "red_stained_glass", "lotr:red_stained_glass", 4, rows("XX", "XX"), 'X', "minecraft:red_stained_glass");
        shaped(null, "red_stained_glass_2", "lotr:red_stained_glass", 8, rows("XXX", "XYX", "XXX"), 'X', "lotr:glass", 'Y', "#c:dyes/red");
        shaped(null, "red_stained_glass_pane", "lotr:red_stained_glass_pane", 16, rows("XXX", "XXX"), 'X', "lotr:red_stained_glass");
        shaped(null, "black_stained_glass", "lotr:black_stained_glass", 4, rows("XX", "XX"), 'X', "minecraft:black_stained_glass");
        shaped(null, "black_stained_glass_2", "lotr:black_stained_glass", 8, rows("XXX", "XYX", "XXX"), 'X', "lotr:glass", 'Y', "#c:dyes/black");
        shaped(null, "black_stained_glass_pane", "lotr:black_stained_glass_pane", 16, rows("XXX", "XXX"), 'X', "lotr:black_stained_glass");
        shaped(null, "rope", "lotr:rope", 3, rows("X", "X", "X"), 'X', "minecraft:string");
        shaped(null, "palm_fence_gate", "lotr:palm_fence_gate", 1, rows("XYX", "XYX"), 'X', "#lotr:sticks", 'Y', "lotr:palm_planks");
        shaped(null, "dragon_fence_gate", "lotr:dragon_fence_gate", 1, rows("XYX", "XYX"), 'X', "#lotr:sticks", 'Y', "lotr:dragon_planks");
        shapeless(null, "red_dye_2", "minecraft:red_dye", 4, "lotr:dragon_log", "lotr:dragon_log");
        shaped(null, "shire_pine_door", "lotr:shire_pine_door", 1, rows("XX", "XX", "XX"), 'X', "lotr:shire_pine_planks");
        shaped(null, "mirk_oak_door", "lotr:mirk_oak_door", 1, rows("XX", "XX", "XX"), 'X', "lotr:mirk_oak_planks");
        shaped(null, "charred_door", "lotr:charred_door", 1, rows("XX", "XX", "XX"), 'X', "lotr:charred_planks");
        shaped(null, "apple_door", "lotr:apple_door", 1, rows("XX", "XX", "XX"), 'X', "lotr:apple_planks");
        shaped(null, "pear_door", "lotr:pear_door", 1, rows("XX", "XX", "XX"), 'X', "lotr:pear_planks");
        shaped(null, "cherry_door", "lotr:cherry_door", 1, rows("XX", "XX", "XX"), 'X', "lotr:cherry_planks");
        shaped(null, "mango_door", "lotr:mango_door", 1, rows("XX", "XX", "XX"), 'X', "lotr:mango_planks");
        shaped(null, "lebethron_door", "lotr:lebethron_door", 1, rows("XX", "XX", "XX"), 'X', "lotr:lebethron_planks");
        shaped(null, "beech_door", "lotr:beech_door", 1, rows("XX", "XX", "XX"), 'X', "lotr:beech_planks");
        shaped(null, "holly_door", "lotr:holly_door", 1, rows("XX", "XX", "XX"), 'X', "lotr:holly_planks");
        shaped(null, "banana_door", "lotr:banana_door", 1, rows("XX", "XX", "XX"), 'X', "lotr:banana_planks");
        shaped(null, "maple_door", "lotr:maple_door", 1, rows("XX", "XX", "XX"), 'X', "lotr:maple_planks");
        shaped(null, "larch_door", "lotr:larch_door", 1, rows("XX", "XX", "XX"), 'X', "lotr:larch_planks");
        shaped(null, "date_palm_door", "lotr:date_palm_door", 1, rows("XX", "XX", "XX"), 'X', "lotr:date_palm_planks");
        shaped(null, "mangrove_door", "lotr:mangrove_door", 1, rows("XX", "XX", "XX"), 'X', "lotr:mangrove_planks");
        shaped(null, "chestnut_door", "lotr:chestnut_door", 1, rows("XX", "XX", "XX"), 'X', "lotr:chestnut_planks");
        shaped(null, "baobab_door", "lotr:baobab_door", 1, rows("XX", "XX", "XX"), 'X', "lotr:baobab_planks");
        shaped(null, "cedar_door", "lotr:cedar_door", 1, rows("XX", "XX", "XX"), 'X', "lotr:cedar_planks");
        shaped(null, "fir_door", "lotr:fir_door", 1, rows("XX", "XX", "XX"), 'X', "lotr:fir_planks");
        shaped(null, "pine_door", "lotr:pine_door", 1, rows("XX", "XX", "XX"), 'X', "lotr:pine_planks");
        shaped(null, "lemon_door", "lotr:lemon_door", 1, rows("XX", "XX", "XX"), 'X', "lotr:lemon_planks");
        shaped(null, "orange_door", "lotr:orange_door", 1, rows("XX", "XX", "XX"), 'X', "lotr:orange_planks");
        shaped(null, "lime_door", "lotr:lime_door", 1, rows("XX", "XX", "XX"), 'X', "lotr:lime_planks");
        shaped(null, "mahogany_door", "lotr:mahogany_door", 1, rows("XX", "XX", "XX"), 'X', "lotr:mahogany_planks");
        shaped(null, "willow_door", "lotr:willow_door", 1, rows("XX", "XX", "XX"), 'X', "lotr:willow_planks");
        shaped(null, "cypress_door", "lotr:cypress_door", 1, rows("XX", "XX", "XX"), 'X', "lotr:cypress_planks");
        shaped(null, "olive_door", "lotr:olive_door", 1, rows("XX", "XX", "XX"), 'X', "lotr:olive_planks");
        shaped(null, "aspen_door", "lotr:aspen_door", 1, rows("XX", "XX", "XX"), 'X', "lotr:aspen_planks");
        shaped(null, "green_oak_door", "lotr:green_oak_door", 1, rows("XX", "XX", "XX"), 'X', "lotr:green_oak_planks");
        shaped(null, "lairelosse_door", "lotr:lairelosse_door", 1, rows("XX", "XX", "XX"), 'X', "lotr:lairelosse_planks");
        shaped(null, "almond_door", "lotr:almond_door", 1, rows("XX", "XX", "XX"), 'X', "lotr:almond_planks");
        shaped(null, "plum_door", "lotr:plum_door", 1, rows("XX", "XX", "XX"), 'X', "lotr:plum_planks");
        shaped(null, "redwood_door", "lotr:redwood_door", 1, rows("XX", "XX", "XX"), 'X', "lotr:redwood_planks");
        shaped(null, "pomegranate_door", "lotr:pomegranate_door", 1, rows("XX", "XX", "XX"), 'X', "lotr:pomegranate_planks");
        shaped(null, "palm_door", "lotr:palm_door", 1, rows("XX", "XX", "XX"), 'X', "lotr:palm_planks");
        shaped(null, "dragon_door", "lotr:dragon_door", 1, rows("XX", "XX", "XX"), 'X', "lotr:dragon_planks");
        shaped(null, "rotten_door", "lotr:rotten_door", 1, rows("XX", "XX", "XX"), 'X', "lotr:rotten_planks");
        shapeless(null, "dirt", "minecraft:dirt", 1, "minecraft:coarse_dirt", "minecraft:wheat_seeds");
        shaped(null, "bone_block", "lotr:bone_block", 1, rows("XX", "XX"), 'X', "#lotr:bones");
        shapeless(null, "bone_meal_2", "minecraft:bone_meal", 8, "lotr:bone_block");
        shaped(null, "kanuka_fence_gate", "lotr:kanuka_fence_gate", 1, rows("XYX", "XYX"), 'X', "#lotr:sticks", 'Y', "lotr:kanuka_planks");
        shaped(null, "kanuka_door", "lotr:kanuka_door", 1, rows("XX", "XX", "XX"), 'X', "lotr:kanuka_planks");
        shaped(null, "barren_jungle_mud", "lotr:barren_jungle_mud", 4, rows("XY", "YX"), 'X', "lotr:mud", 'Y', "minecraft:gravel");
        shapeless(null, "mud", "lotr:mud", 1, "lotr:barren_jungle_mud", "minecraft:wheat_seeds");
        shaped(null, "drystone", "lotr:drystone", 4, rows("XX", "XX"), 'X', "minecraft:cobblestone");
        shaped(null, "shire_pine_trapdoor", "lotr:shire_pine_trapdoor", 2, rows("XXX", "XXX"), 'X', "lotr:shire_pine_planks");
        shaped(null, "mirk_oak_trapdoor", "lotr:mirk_oak_trapdoor", 2, rows("XXX", "XXX"), 'X', "lotr:mirk_oak_planks");
        shaped(null, "charred_trapdoor", "lotr:charred_trapdoor", 2, rows("XXX", "XXX"), 'X', "lotr:charred_planks");
        shaped(null, "apple_trapdoor", "lotr:apple_trapdoor", 2, rows("XXX", "XXX"), 'X', "lotr:apple_planks");
        shaped(null, "pear_trapdoor", "lotr:pear_trapdoor", 2, rows("XXX", "XXX"), 'X', "lotr:pear_planks");
        shaped(null, "cherry_trapdoor", "lotr:cherry_trapdoor", 2, rows("XXX", "XXX"), 'X', "lotr:cherry_planks");
        shaped(null, "mango_trapdoor", "lotr:mango_trapdoor", 2, rows("XXX", "XXX"), 'X', "lotr:mango_planks");
        shaped(null, "lebethron_trapdoor", "lotr:lebethron_trapdoor", 2, rows("XXX", "XXX"), 'X', "lotr:lebethron_planks");
        shaped(null, "beech_trapdoor", "lotr:beech_trapdoor", 2, rows("XXX", "XXX"), 'X', "lotr:beech_planks");
        shaped(null, "holly_trapdoor", "lotr:holly_trapdoor", 2, rows("XXX", "XXX"), 'X', "lotr:holly_planks");
        shaped(null, "banana_trapdoor", "lotr:banana_trapdoor", 2, rows("XXX", "XXX"), 'X', "lotr:banana_planks");
        shaped(null, "maple_trapdoor", "lotr:maple_trapdoor", 2, rows("XXX", "XXX"), 'X', "lotr:maple_planks");
        shaped(null, "larch_trapdoor", "lotr:larch_trapdoor", 2, rows("XXX", "XXX"), 'X', "lotr:larch_planks");
        shaped(null, "date_palm_trapdoor", "lotr:date_palm_trapdoor", 2, rows("XXX", "XXX"), 'X', "lotr:date_palm_planks");
        shaped(null, "mangrove_trapdoor", "lotr:mangrove_trapdoor", 2, rows("XXX", "XXX"), 'X', "lotr:mangrove_planks");
        shaped(null, "chestnut_trapdoor", "lotr:chestnut_trapdoor", 2, rows("XXX", "XXX"), 'X', "lotr:chestnut_planks");
        shaped(null, "baobab_trapdoor", "lotr:baobab_trapdoor", 2, rows("XXX", "XXX"), 'X', "lotr:baobab_planks");
        shaped(null, "cedar_trapdoor", "lotr:cedar_trapdoor", 2, rows("XXX", "XXX"), 'X', "lotr:cedar_planks");
        shaped(null, "fir_trapdoor", "lotr:fir_trapdoor", 2, rows("XXX", "XXX"), 'X', "lotr:fir_planks");
        shaped(null, "pine_trapdoor", "lotr:pine_trapdoor", 2, rows("XXX", "XXX"), 'X', "lotr:pine_planks");
        shaped(null, "lemon_trapdoor", "lotr:lemon_trapdoor", 2, rows("XXX", "XXX"), 'X', "lotr:lemon_planks");
        shaped(null, "orange_trapdoor", "lotr:orange_trapdoor", 2, rows("XXX", "XXX"), 'X', "lotr:orange_planks");
        shaped(null, "lime_trapdoor", "lotr:lime_trapdoor", 2, rows("XXX", "XXX"), 'X', "lotr:lime_planks");
        shaped(null, "mahogany_trapdoor", "lotr:mahogany_trapdoor", 2, rows("XXX", "XXX"), 'X', "lotr:mahogany_planks");
        shaped(null, "willow_trapdoor", "lotr:willow_trapdoor", 2, rows("XXX", "XXX"), 'X', "lotr:willow_planks");
        shaped(null, "cypress_trapdoor", "lotr:cypress_trapdoor", 2, rows("XXX", "XXX"), 'X', "lotr:cypress_planks");
        shaped(null, "olive_trapdoor", "lotr:olive_trapdoor", 2, rows("XXX", "XXX"), 'X', "lotr:olive_planks");
        shaped(null, "aspen_trapdoor", "lotr:aspen_trapdoor", 2, rows("XXX", "XXX"), 'X', "lotr:aspen_planks");
        shaped(null, "green_oak_trapdoor", "lotr:green_oak_trapdoor", 2, rows("XXX", "XXX"), 'X', "lotr:green_oak_planks");
        shaped(null, "lairelosse_trapdoor", "lotr:lairelosse_trapdoor", 2, rows("XXX", "XXX"), 'X', "lotr:lairelosse_planks");
        shaped(null, "almond_trapdoor", "lotr:almond_trapdoor", 2, rows("XXX", "XXX"), 'X', "lotr:almond_planks");
        shaped(null, "plum_trapdoor", "lotr:plum_trapdoor", 2, rows("XXX", "XXX"), 'X', "lotr:plum_planks");
        shaped(null, "redwood_trapdoor", "lotr:redwood_trapdoor", 2, rows("XXX", "XXX"), 'X', "lotr:redwood_planks");
        shaped(null, "pomegranate_trapdoor", "lotr:pomegranate_trapdoor", 2, rows("XXX", "XXX"), 'X', "lotr:pomegranate_planks");
        shaped(null, "palm_trapdoor", "lotr:palm_trapdoor", 2, rows("XXX", "XXX"), 'X', "lotr:palm_planks");
        shaped(null, "dragon_trapdoor", "lotr:dragon_trapdoor", 2, rows("XXX", "XXX"), 'X', "lotr:dragon_planks");
        shaped(null, "kanuka_trapdoor", "lotr:kanuka_trapdoor", 2, rows("XXX", "XXX"), 'X', "lotr:kanuka_planks");
        shaped(null, "rotten_trapdoor", "lotr:rotten_trapdoor", 2, rows("XXX", "XXX"), 'X', "lotr:rotten_planks");
        shapeless(null, "purple_dye", "minecraft:purple_dye", 1, "lotr:lavender");
        shaped(null, "chainmail_helmet", "minecraft:chainmail_helmet", 1, rows("XXX", "Y Y"), 'X', "minecraft:iron_ingot", 'Y', "minecraft:iron_nugget");
        shaped(null, "chainmail_chestplate", "minecraft:chainmail_chestplate", 1, rows("X X", "YYY", "XXX"), 'X', "minecraft:iron_ingot", 'Y', "minecraft:iron_nugget");
        shaped(null, "chainmail_leggings", "minecraft:chainmail_leggings", 1, rows("XXX", "Y Y", "X X"), 'X', "minecraft:iron_ingot", 'Y', "minecraft:iron_nugget");
        shaped(null, "chainmail_boots", "minecraft:chainmail_boots", 1, rows("Y Y", "X X"), 'X', "minecraft:iron_ingot", 'Y', "minecraft:iron_nugget");
        // LOTRRecipes.createTauredainRecipes
        shaped(TAUREDAIN, "tauredain/tauredain_crafting_table", "lotr:tauredain_crafting_table", 1, rows("XX", "YY"), 'X', "#minecraft:planks", 'Y', "lotr:tauredain_brick");
        shaped(TAUREDAIN, "tauredain/tauredain_banner", "lotr:tauredain_banner", 1, rows("X", "Y", "Z"), 'X', "#minecraft:wool", 'Y', "#lotr:sticks", 'Z', "#minecraft:planks");
        shaped(TAUREDAIN, "tauredain/tauredain_brick", "lotr:tauredain_brick", 4, rows("XX", "XX"), 'X', "minecraft:stone");
        shaped(TAUREDAIN, "tauredain/tauredain_brick_slab", "lotr:tauredain_brick_slab", 6, rows("XXX"), 'X', "lotr:tauredain_brick");
        shaped(TAUREDAIN, "tauredain/tauredain_brick_stairs", "lotr:tauredain_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:tauredain_brick");
        shaped(TAUREDAIN, "tauredain/tauredain_brick_wall", "lotr:tauredain_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:tauredain_brick");
        shapeless(TAUREDAIN, "tauredain/tauredain_mossy_brick", "lotr:tauredain_mossy_brick", 1, "lotr:tauredain_brick", "#lotr:vines");
        shaped(TAUREDAIN, "tauredain/tauredain_mossy_brick_slab", "lotr:tauredain_mossy_brick_slab", 6, rows("XXX"), 'X', "lotr:tauredain_mossy_brick");
        shaped(TAUREDAIN, "tauredain/tauredain_mossy_brick_stairs", "lotr:tauredain_mossy_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:tauredain_mossy_brick");
        shaped(TAUREDAIN, "tauredain/tauredain_mossy_brick_wall", "lotr:tauredain_mossy_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:tauredain_mossy_brick");
        shaped(TAUREDAIN, "tauredain/tauredain_cracked_brick_slab", "lotr:tauredain_cracked_brick_slab", 6, rows("XXX"), 'X', "lotr:tauredain_cracked_brick");
        shaped(TAUREDAIN, "tauredain/tauredain_cracked_brick_stairs", "lotr:tauredain_cracked_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:tauredain_cracked_brick");
        shaped(TAUREDAIN, "tauredain/tauredain_cracked_brick_wall", "lotr:tauredain_cracked_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:tauredain_cracked_brick");
        shaped(TAUREDAIN, "tauredain/tauredain_gold_brick", "lotr:tauredain_gold_brick", 4, rows("XX", "XX"), 'X', "minecraft:gold_ingot");
        shaped(TAUREDAIN, "tauredain/tauredain_gold_brick_slab", "lotr:tauredain_gold_brick_slab", 6, rows("XXX"), 'X', "lotr:tauredain_gold_brick");
        shaped(TAUREDAIN, "tauredain/tauredain_gold_brick_stairs", "lotr:tauredain_gold_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:tauredain_gold_brick");
        shaped(TAUREDAIN, "tauredain/tauredain_gold_brick_wall", "lotr:tauredain_gold_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:tauredain_gold_brick");
        shaped(TAUREDAIN, "tauredain/tauredain_obsidian_brick_slab", "lotr:tauredain_obsidian_brick_slab", 6, rows("XXX"), 'X', "lotr:tauredain_obsidian_brick");
        shaped(TAUREDAIN, "tauredain/tauredain_obsidian_brick_stairs", "lotr:tauredain_obsidian_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:tauredain_obsidian_brick");
        shaped(TAUREDAIN, "tauredain/tauredain_obsidian_brick_wall", "lotr:tauredain_obsidian_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:tauredain_obsidian_brick");
        shaped(TAUREDAIN, "tauredain/taurethrim_blowgun", "lotr:taurethrim_blowgun", 1, rows("XYY"), 'X', "#lotr:sticks", 'Y', "lotr:reeds");
        shaped(TAUREDAIN, "tauredain/tauredain_dart_trap", "lotr:tauredain_dart_trap", 1, rows("XXX", "XYX", "XXX"), 'X', "lotr:tauredain_brick", 'Y', "lotr:taurethrim_blowgun");
        shaped(TAUREDAIN, "tauredain/tauredain_pillar", "lotr:tauredain_pillar", 3, rows("X", "X", "X"), 'X', "minecraft:stone");
        shaped(TAUREDAIN, "tauredain/tauredain_pillar_slab", "lotr:tauredain_pillar_slab", 6, rows("XXX"), 'X', "lotr:tauredain_pillar");
        shaped(TAUREDAIN, "tauredain/tauredain_double_torch", "lotr:tauredain_double_torch", 2, rows("X", "Y", "Y"), 'X', "#minecraft:coals", 'Y', "#lotr:sticks");
        shaped(TAUREDAIN, "tauredain/gold_tauredain_dart_trap", "lotr:gold_tauredain_dart_trap", 1, rows("XXX", "XYX", "XXX"), 'X', "lotr:tauredain_gold_brick", 'Y', "lotr:taurethrim_blowgun");
        shaped(TAUREDAIN, "tauredain/obsidian_tauredain_dart_trap", "lotr:obsidian_tauredain_dart_trap", 1, rows("XXX", "XYX", "XXX"), 'X', "lotr:tauredain_obsidian_brick", 'Y', "lotr:taurethrim_blowgun");
        shaped(TAUREDAIN, "tauredain/taur_gold_pillar", "lotr:taur_gold_pillar", 3, rows("X", "X", "X"), 'X', "minecraft:gold_ingot");
        shaped(TAUREDAIN, "tauredain/taur_gold_pillar_slab", "lotr:taur_gold_pillar_slab", 6, rows("XXX"), 'X', "lotr:taur_gold_pillar");
        shaped(TAUREDAIN, "tauredain/taur_obsidian_pillar_slab", "lotr:taur_obsidian_pillar_slab", 6, rows("XXX"), 'X', "lotr:taur_obsidian_pillar");
        // LOTRRecipes.createUmbarRecipes
        shaped(UMBAR, "umbar/umbar_crafting_table", "lotr:umbar_crafting_table", 1, rows("XX", "YY"), 'X', "#minecraft:planks", 'Y', "minecraft:sandstone");
        shaped(UMBAR, "umbar/umbar_banner", "lotr:umbar_banner", 1, rows("X", "Y", "Z"), 'X', "#minecraft:wool", 'Y', "#lotr:sticks", 'Z', "#minecraft:planks");
        shaped(UMBAR, "umbar/umbaric_scimitar", "lotr:umbaric_scimitar", 1, rows("X", "X", "Y"), 'X', "minecraft:iron_ingot", 'Y', "#lotr:sticks");
        shaped(UMBAR, "umbar/umbaric_dagger", "lotr:umbaric_dagger", 1, rows("X", "Y"), 'X', "minecraft:iron_ingot", 'Y', "#lotr:sticks");
        shaped(UMBAR, "umbar/umbaric_spear", "lotr:umbaric_spear", 1, rows("  X", " Y ", "Y  "), 'X', "minecraft:iron_ingot", 'Y', "#lotr:sticks");
        shaped(UMBAR, "umbar/umbaric_mace", "lotr:umbaric_mace", 1, rows(" XX", " XX", "Y  "), 'X', "minecraft:iron_ingot", 'Y', "#lotr:sticks");
        shaped(UMBAR, "umbar/umbaric_poleaxe", "lotr:umbaric_poleaxe", 1, rows(" XX", " YX", "Y  "), 'X', "minecraft:iron_ingot", 'Y', "#lotr:sticks");
        shaped(UMBAR, "umbar/umbar_brick", "lotr:umbar_brick", 4, rows("XX", "XX"), 'X', "minecraft:stone");
        shaped(UMBAR, "umbar/umbar_brick_slab", "lotr:umbar_brick_slab", 6, rows("XXX"), 'X', "lotr:umbar_brick");
        shaped(UMBAR, "umbar/umbar_brick_stairs", "lotr:umbar_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:umbar_brick");
        shaped(UMBAR, "umbar/umbar_brick_wall", "lotr:umbar_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:umbar_brick");
        shaped(UMBAR, "umbar/cracked_umbar_brick_slab", "lotr:cracked_umbar_brick_slab", 6, rows("XXX"), 'X', "lotr:cracked_umbar_brick");
        shaped(UMBAR, "umbar/cracked_umbar_brick_stairs", "lotr:cracked_umbar_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:cracked_umbar_brick");
        shaped(UMBAR, "umbar/cracked_umbar_brick_wall", "lotr:cracked_umbar_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:cracked_umbar_brick");
        shaped(UMBAR, "umbar/carved_umbar_brick", "lotr:carved_umbar_brick", 1, rows("XX", "XX"), 'X', "lotr:umbar_brick");
        shaped(UMBAR, "umbar/carved_black_umbar_brick", "lotr:carved_black_umbar_brick", 1, rows("XX", "XX"), 'X', "lotr:numenorean_brick");
        shaped(UMBAR, "umbar/umbar_pillar", "lotr:umbar_pillar", 3, rows("X", "X", "X"), 'X', "minecraft:stone");
        shaped(UMBAR, "umbar/umbar_pillar_slab", "lotr:umbar_pillar_slab", 6, rows("XXX"), 'X', "lotr:umbar_pillar");
        shaped(UMBAR, "umbar/umbaric_horse_armor", "lotr:umbaric_horse_armor", 1, rows("X  ", "XYX", "XXX"), 'X', "minecraft:iron_ingot", 'Y', "minecraft:leather");
        // LOTRRecipes.createUrukRecipes
        shaped(URUK, "uruk/uruk_crafting_table", "lotr:uruk_crafting_table", 1, rows("XX", "YY"), 'X', "#minecraft:planks", 'Y', "lotr:uruk_brick");
        shaped(URUK, "uruk/orc_torch", "lotr:orc_torch", 2, rows("X", "Y", "Y"), 'X', "#minecraft:coals", 'Y', "#lotr:sticks");
        shaped(URUK, "uruk/orc_forge", "lotr:orc_forge", 1, rows("XXX", "X X", "XXX"), 'X', "lotr:uruk_brick");
        shaped(URUK, "uruk/isengard_banner", "lotr:isengard_banner", 1, rows("X", "Y", "Z"), 'X', "#minecraft:wool", 'Y', "#lotr:sticks", 'Z', "#minecraft:planks");
        shaped(URUK, "uruk/uruk_brick", "lotr:uruk_brick", 4, rows("XX", "XX"), 'X', "minecraft:stone");
        shaped(URUK, "uruk/uruk_brick_slab", "lotr:uruk_brick_slab", 6, rows("XXX"), 'X', "lotr:uruk_brick");
        shaped(URUK, "uruk/uruk_brick_stairs", "lotr:uruk_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:uruk_brick");
        shaped(URUK, "uruk/uruk_brick_wall", "lotr:uruk_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:uruk_brick");
        shaped(URUK, "uruk/uruk_pillar", "lotr:uruk_pillar", 3, rows("X", "X", "X"), 'X', "minecraft:stone");
        shaped(URUK, "uruk/uruk_pillar_slab", "lotr:uruk_pillar_slab", 6, rows("XXX"), 'X', "lotr:uruk_pillar");
        // LOTRRecipes.createWoodElvenRecipes
        shaped(WOOD_ELVEN, "wood_elven/wood_elven_crafting_table", "lotr:wood_elven_crafting_table", 1, rows("XX", "XX"), 'X', "#minecraft:planks");
        shaped(WOOD_ELVEN, "wood_elven/wood_elven_bed", "lotr:wood_elven_bed", 1, rows("XXX", "YYY"), 'X', "#minecraft:wool", 'Y', "#minecraft:planks");
        shaped(WOOD_ELVEN, "wood_elven/wood_elven_scout_hood", "lotr:wood_elven_scout_hood", 1, rows("XXX", "X X"), 'X', "minecraft:leather");
        shaped(WOOD_ELVEN, "wood_elven/wood_elven_scout_tunic", "lotr:wood_elven_scout_tunic", 1, rows("X X", "XXX", "XXX"), 'X', "minecraft:leather");
        shaped(WOOD_ELVEN, "wood_elven/wood_elven_scout_leggings", "lotr:wood_elven_scout_leggings", 1, rows("XXX", "X X", "X X"), 'X', "minecraft:leather");
        shaped(WOOD_ELVEN, "wood_elven/wood_elven_scout_boots", "lotr:wood_elven_scout_boots", 1, rows("X X", "X X"), 'X', "minecraft:leather");
        shaped(WOOD_ELVEN, "wood_elven/mirkwood_bow", "lotr:mirkwood_bow", 1, rows(" XY", "X Y", " XY"), 'X', "#lotr:sticks", 'Y', "minecraft:string");
        shaped(WOOD_ELVEN, "wood_elven/wood_elven_torch", "lotr:wood_elven_torch", 4, rows("X", "Y", "Z"), 'X', "lotr:mirk_oak_red_leaves", 'Y', "#minecraft:coals", 'Z', "#lotr:sticks");
        shaped(WOOD_ELVEN, "wood_elven/wood_elven_chandelier", "lotr:wood_elven_chandelier", 2, rows(" X ", "YZY"), 'X', "#lotr:sticks", 'Y', "lotr:wood_elven_torch", 'Z', "#minecraft:planks");
        shaped(WOOD_ELVEN, "wood_elven/mirkwood_banner", "lotr:mirkwood_banner", 1, rows("X", "Y", "Z"), 'X', "#minecraft:wool", 'Y', "#lotr:sticks", 'Z', "#minecraft:planks");
        shaped(WOOD_ELVEN, "wood_elven/wood_elven_brick", "lotr:wood_elven_brick", 4, rows("XX", "XX"), 'X', "minecraft:stone");
        shapeless(WOOD_ELVEN, "wood_elven/mossy_wood_elven_brick", "lotr:mossy_wood_elven_brick", 1, "lotr:wood_elven_brick", "#lotr:vines");
        shaped(WOOD_ELVEN, "wood_elven/wood_elven_brick_slab", "lotr:wood_elven_brick_slab", 6, rows("XXX"), 'X', "lotr:wood_elven_brick");
        shaped(WOOD_ELVEN, "wood_elven/mossy_wood_elven_brick_slab", "lotr:mossy_wood_elven_brick_slab", 6, rows("XXX"), 'X', "lotr:mossy_wood_elven_brick");
        shaped(WOOD_ELVEN, "wood_elven/cracked_wood_elven_brick_slab", "lotr:cracked_wood_elven_brick_slab", 6, rows("XXX"), 'X', "lotr:cracked_wood_elven_brick");
        shaped(WOOD_ELVEN, "wood_elven/wood_elven_brick_stairs", "lotr:wood_elven_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:wood_elven_brick");
        shaped(WOOD_ELVEN, "wood_elven/mossy_wood_elven_brick_stairs", "lotr:mossy_wood_elven_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:mossy_wood_elven_brick");
        shaped(WOOD_ELVEN, "wood_elven/cracked_wood_elven_brick_stairs", "lotr:cracked_wood_elven_brick_stairs", 4, rows("X  ", "XX ", "XXX"), 'X', "lotr:cracked_wood_elven_brick");
        shaped(WOOD_ELVEN, "wood_elven/wood_elven_brick_wall", "lotr:wood_elven_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:wood_elven_brick");
        shaped(WOOD_ELVEN, "wood_elven/mossy_wood_elven_brick_wall", "lotr:mossy_wood_elven_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:mossy_wood_elven_brick");
        shaped(WOOD_ELVEN, "wood_elven/cracked_wood_elven_brick_wall", "lotr:cracked_wood_elven_brick_wall", 6, rows("XXX", "XXX"), 'X', "lotr:cracked_wood_elven_brick");
        shaped(WOOD_ELVEN, "wood_elven/wood_elven_pillar", "lotr:wood_elven_pillar", 3, rows("X", "X", "X"), 'X', "minecraft:stone");
        shaped(WOOD_ELVEN, "wood_elven/wood_elven_pillar_slab", "lotr:wood_elven_pillar_slab", 6, rows("XXX"), 'X', "lotr:wood_elven_pillar");
        shaped(WOOD_ELVEN, "wood_elven/cracked_wood_elven_pillar_slab", "lotr:cracked_wood_elven_pillar_slab", 6, rows("XXX"), 'X', "lotr:cracked_wood_elven_pillar");
        shaped(WOOD_ELVEN, "wood_elven/carved_wood_elven_brick", "lotr:carved_wood_elven_brick", 1, rows("XX", "XX"), 'X', "lotr:wood_elven_brick");
        shaped(WOOD_ELVEN, "wood_elven/elven_forge", "lotr:elven_forge", 1, rows("XXX", "X X", "XXX"), 'X', "lotr:wood_elven_brick");
        shaped(WOOD_ELVEN, "wood_elven/wood_elf_wood_bars", "lotr:wood_elf_wood_bars", 8, rows("XXX", "XXX"), 'X', "#lotr:sticks");
        shaped(WOOD_ELVEN, "wood_elven/wood_elven_gold_brick", "lotr:wood_elven_gold_brick", 1, rows(" X ", "XYX", " X "), 'X', "minecraft:gold_nugget", 'Y', "lotr:wood_elven_brick");
        wildcardSmelting();
        crackedBrickSmelting();
    }

    // createSmeltingRecipes entries whose input was a metadata block with no metadata given, i.e. every subtype: LOTRMod.rock (six rocks) to scorched stone, and LOTRMod.oreGulduril (both its plain and Mordor forms) to the crystal.
    private void wildcardSmelting() {
        for (String rock : List.of("mordor_rock", "gondor_rock", "rohan_rock", "blue_rock", "red_rock", "chalk")) {
            smelting("scorched_stone_from_" + rock, "lotr:scorched_stone", "lotr:" + rock, 0.1f);
        }
        smelting("gulduril_crystal_from_smelting", "lotr:gulduril", "lotr:gulduril_ore", 1.0f);
        smelting("gulduril_crystal_from_smelting_mordor", "lotr:gulduril", "lotr:gulduril_mordor_ore", 1.0f);
    }

    // LOTRMillstoneRecipes.addCrackedBricks: every millstone cracking was also a furnace recipe at 0.1 xp.
    // Vanilla already smelts its own stone bricks, so vanilla results are left to vanilla.
    private void crackedBrickSmelting() {
        LOTRMillstoneRecipes.crackedBricks().forEach((brick, cracked) -> {
            if (BuiltInRegistries.ITEM.getKey(cracked.asItem()).getNamespace().equals("minecraft")) {
                return;
            }
            String name = BuiltInRegistries.ITEM.getKey(cracked.asItem()).getPath() + "_from_smelting";
            SimpleCookingRecipeBuilder.smelting(Ingredient.of(brick), RecipeCategory.BUILDING_BLOCKS,
                            CookingBookCategory.BLOCKS, cracked, 0.1f, 200)
                    .unlockedBy(getHasName(brick), has(brick))
                    .save(output, key(name));
        });
    }

    /**
     * Recipes the transcription above was missing or had wrong, checked line
     * by line against LOTRRecipes.
     */
    private void restoredRecipes() {
        // brick4:6, the carved black brick, at both Numenorean tables that had it.
        shaped(GONDORIAN, "gondorian/carved_black_umbar_brick", "lotr:carved_black_umbar_brick", 1, rows("XX", "XX"), 'X', "lotr:numenorean_brick");
        shaped(DOL_AMROTH, "dol_amroth/carved_black_umbar_brick", "lotr:carved_black_umbar_brick", 1, rows("XX", "XX"), 'X', "lotr:numenorean_brick");
        // dirtPath:0 from Blocks.dirt with no metadata: any of its three subtypes.
        shaped(null, "dirt_path", "minecraft:dirt_path", 2, rows("XX"), 'X', "minecraft:dirt|minecraft:coarse_dirt|minecraft:podzol");
        // createCommonNearHaradRecipes: the shish kebab on a skewer, corner to corner.
        for (LOTRCraftingTable table : List.of(NEAR_HARAD, UMBAR, GULF)) {
            shaped(table, table.getSerializedName() + "/shish_kebab", "lotr:shish_kebab", 2,
                    rows("  X", " X ", "Y  "), 'X', "lotr:kebab", 'Y', "#lotr:sticks");
        }
        // addDyeableWoolRobeRecipes: each colour of wool gives the robe already
        // dyed that colour -- EntitySheep.fleeceColorTable, white left undyed.
        for (LOTRCraftingTable table : List.of(NEAR_HARAD, UMBAR, GULF)) {
            woolRobes(table, "lotr:harad_turban", rows("XXX", "X X"));
            woolRobes(table, "lotr:harad_robe", rows("X X", "XXX", "XXX"));
            woolRobes(table, "lotr:harad_robe_leggings", rows("XXX", "X X", "X X"));
            woolRobes(table, "lotr:harad_robe_shoes", rows("X X", "X X"));
        }
        woolRobes(RHUN, "lotr:kaftan", rows("X X", "XXX", "XXX"));
        woolRobes(RHUN, "lotr:kaftan_leggings", rows("XXX", "X X", "X X"));
    }

    private static final String[] WOOL = {"white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray",
            "light_gray", "cyan", "purple", "blue", "brown", "green", "red", "black"};
    private static final float[][] FLEECE = {{1.0f, 1.0f, 1.0f}, {0.85f, 0.5f, 0.2f}, {0.7f, 0.3f, 0.85f}, {0.4f, 0.6f, 0.85f},
            {0.9f, 0.9f, 0.2f}, {0.5f, 0.8f, 0.1f}, {0.95f, 0.5f, 0.65f}, {0.3f, 0.3f, 0.3f}, {0.6f, 0.6f, 0.6f},
            {0.3f, 0.5f, 0.6f}, {0.5f, 0.25f, 0.7f}, {0.2f, 0.3f, 0.7f}, {0.4f, 0.3f, 0.2f}, {0.4f, 0.5f, 0.2f},
            {0.6f, 0.2f, 0.2f}, {0.1f, 0.1f, 0.1f}};

    private void woolRobes(LOTRCraftingTable table, String resultId, String[] rows) {
        Item result = item(resultId);
        String base = table.getSerializedName() + "/" + Identifier.parse(resultId).getPath();
        for (int i = 0; i < WOOL.length; i++) {
            String wool = "minecraft:" + WOOL[i] + "_wool";
            float[] c = FLEECE[i];
            net.minecraft.core.component.DataComponentPatch patch = net.minecraft.core.component.DataComponentPatch.EMPTY;
            if (c[0] != 1.0f || c[1] != 1.0f || c[2] != 1.0f) {
                int rgb = (int) (c[0] * 255.0f) << 16 | (int) (c[1] * 255.0f) << 8 | (int) (c[2] * 255.0f);
                patch = net.minecraft.core.component.DataComponentPatch.builder()
                        .set(net.minecraft.core.component.DataComponents.DYED_COLOR,
                                new net.minecraft.world.item.component.DyedItemColor(rgb))
                        .build();
            }
            LOTRFactionCraftingRecipe recipe = new LOTRFactionCraftingRecipe(
                    RecipeBuilder.createCraftingCommonInfo(true),
                    RecipeBuilder.createCraftingBookInfo(RecipeCategory.MISC, ""),
                    table, ShapedRecipePattern.of(Map.of('X', ingredient(wool)), List.of(rows)),
                    new ItemStackTemplate(BuiltInRegistries.ITEM.wrapAsHolder(result), 1, patch));
            saveFaction(base + "_" + WOOL[i], recipe, RecipeCategory.MISC, wool);
        }
    }

    private static String[] rows(String... rows) {
        return rows;
    }

    private static Item item(String id) {
        Item item = BuiltInRegistries.ITEM.getValue(Identifier.parse(id));
        if (item == Items.AIR) {
            throw new IllegalStateException("Transcribed recipe names an unregistered item: " + id);
        }
        return item;
    }

    private Ingredient ingredient(String id) {
        if (id.contains("|")) {
            // A 1.7.10 block given with no metadata matched every subtype; "a|b|c" lists them.
            return Ingredient.of(java.util.Arrays.stream(id.split("\\|")).map(LOTRTranscribedRecipes::item)
                    .toArray(Item[]::new));
        }
        return id.startsWith("#")
                ? tag(TagKey.create(Registries.ITEM, Identifier.parse(id.substring(1))))
                : Ingredient.of(item(id));
    }

    private Criterion<?> unlock(String id) {
        id = id.split("\\|")[0];
        return id.startsWith("#")
                ? has(TagKey.create(Registries.ITEM, Identifier.parse(id.substring(1))))
                : has(item(id));
    }

    private static String unlockName(String id) {
        id = id.split("\\|")[0];
        return "has_" + Identifier.parse(id.startsWith("#") ? id.substring(1) : id).getPath().replace('/', '_');
    }

    private static RecipeCategory category(Item result) {
        return result instanceof BlockItem ? RecipeCategory.BUILDING_BLOCKS : RecipeCategory.MISC;
    }

    /**
     * The namespace these recipes have always been written under: the mod id
     * in fabric.mod.json. Not LOTRMod.MOD_ID, which is now a display-style
     * string with spaces in it and is not a valid namespace.
     */
    private static final String RECIPE_NAMESPACE = "lord_of_the_rings_-_middle_earth";

    private static ResourceKey<Recipe<?>> key(String name) {
        return ResourceKey.create(Registries.RECIPE, Identifier.fromNamespaceAndPath(RECIPE_NAMESPACE, name));
    }

    // keyAndIngredients alternates a pattern character and an ingredient id, like the Object... tail of ShapedOreRecipe.
    private void shaped(LOTRCraftingTable table, String name, String resultId, int count, String[] rows, Object... keyAndIngredients) {
        Item result = item(resultId);
        RecipeCategory category = category(result);
        Map<Character, Ingredient> key = new LinkedHashMap<>();
        for (int i = 0; i < keyAndIngredients.length; i += 2) {
            key.put((Character) keyAndIngredients[i], ingredient((String) keyAndIngredients[i + 1]));
        }
        String first = (String) keyAndIngredients[1];

        if (table == null) {
            ShapedRecipeBuilder builder = shaped(category, result, count);
            for (String row : rows) {
                builder.pattern(row);
            }
            key.forEach(builder::define);
            builder.unlockedBy(unlockName(first), unlock(first)).save(output, key(name));
            return;
        }

        LOTRFactionCraftingRecipe recipe = new LOTRFactionCraftingRecipe(
                RecipeBuilder.createCraftingCommonInfo(true),
                RecipeBuilder.createCraftingBookInfo(category, ""),
                table, ShapedRecipePattern.of(key, List.of(rows)), new ItemStackTemplate(result, count));
        saveFaction(name, recipe, category, first);
    }

    private void shapeless(LOTRCraftingTable table, String name, String resultId, int count, String... ingredientIds) {
        Item result = item(resultId);
        RecipeCategory category = category(result);
        String first = ingredientIds[0];

        if (table == null) {
            ShapelessRecipeBuilder builder = shapeless(category, result, count);
            for (String id : ingredientIds) {
                builder.requires(ingredient(id));
            }
            builder.unlockedBy(unlockName(first), unlock(first)).save(output, key(name));
            return;
        }

        List<Ingredient> ingredients = new ArrayList<>();
        for (String id : ingredientIds) {
            ingredients.add(ingredient(id));
        }
        LOTRFactionShapelessRecipe recipe = new LOTRFactionShapelessRecipe(
                RecipeBuilder.createCraftingCommonInfo(true),
                RecipeBuilder.createCraftingBookInfo(category, ""),
                table, new ItemStackTemplate(result, count), ingredients);
        saveFaction(name, recipe, category, first);
    }

    private void saveFaction(String name, Recipe<?> recipe, RecipeCategory category, String unlockId) {
        ResourceKey<Recipe<?>> key = key(name);
        RecipeUnlockAdvancementBuilder advancement = new RecipeUnlockAdvancementBuilder();
        advancement.unlockedBy(unlockName(unlockId), unlock(unlockId));
        output.accept(key, recipe, advancement.build(output, key, category));
    }

    // GameRegistry.addSmelting: 200 ticks, as every 1.7.10 furnace recipe was.
    private void smelting(String name, String resultId, String inputId, float xp) {
        Item result = item(resultId);
        boolean block = result instanceof BlockItem;
        SimpleCookingRecipeBuilder.smelting(ingredient(inputId),
                        block ? RecipeCategory.BUILDING_BLOCKS : RecipeCategory.MISC,
                        block ? CookingBookCategory.BLOCKS : CookingBookCategory.MISC,
                        result, xp, 200)
                .unlockedBy(unlockName(inputId), unlock(inputId))
                .save(output, key(name));
    }
}
