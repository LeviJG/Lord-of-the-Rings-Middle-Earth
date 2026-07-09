package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import java.util.function.Function;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

/**
 * Central block registry.
 *  - registerCube:   solid stone-like cube_all (ores, bricks, rocks, storage blocks)
 *  - registerPlanks: wooden cube_all (wood sound, flammable, axe-mineable)
 * Mining tags are assigned in LOTRBlockTagProvider.
 */
public final class LOTRBlocks {

    // --- Ores ---
    public static final Block TIN_ORE = registerCube("tin_ore", 3.0f, 5.0f);
    public static final Block SILVER_ORE = registerCube("silver_ore", 3.0f, 5.0f);
    public static final Block MITHRIL_ORE = registerCube("mithril_ore", 4.0f, 10.0f);
    public static final Block SALT_ORE = registerCube("salt_ore", 3.0f, 5.0f);
    public static final Block SALTPETER_ORE = registerCube("saltpeter_ore", 3.0f, 5.0f);
    public static final Block SULFUR_ORE = registerCube("sulfur_ore", 3.0f, 5.0f);

    // --- Stone-like cube blocks ---
    public static final Block AMBER_BLOCK = registerCube("amber_block", 5.0f, 6.0f);
    public static final Block AMBER_ORE = registerCube("amber_ore", 3.0f, 5.0f);
    public static final Block AMETHYST_BLOCK = registerCube("amethyst_block", 5.0f, 6.0f);
    public static final Block AMETHYST_ORE = registerCube("amethyst_ore", 3.0f, 5.0f);
    public static final Block ANGMAR_BRICK = registerCube("angmar_brick", 1.5f, 6.0f);
    public static final Block ANGMAR_CRACKED_BRICK = registerCube("angmar_cracked_brick", 1.5f, 6.0f);
    public static final Block ANGMAR_SNOW_BRICK = registerCube("angmar_snow_brick", 1.5f, 6.0f);
    public static final Block ARNOR_BRICK = registerCube("arnor_brick", 1.5f, 6.0f);
    public static final Block ARNOR_CARVED_BRICK = registerCube("arnor_carved_brick", 1.5f, 6.0f);
    public static final Block ARNOR_CRACKED_BRICK = registerCube("arnor_cracked_brick", 1.5f, 6.0f);
    public static final Block ARNOR_MOSSY_BRICK = registerCube("arnor_mossy_brick", 1.5f, 6.0f);
    public static final Block BLACK_GONDOR_BRICK = registerCube("black_gondor_brick", 1.5f, 6.0f);
    public static final Block BLACK_GONDOR_CARVED_BRICK = registerCube("black_gondor_carved_brick", 1.5f, 6.0f);
    public static final Block BLACK_UMBAR_CARVED_BRICK = registerCube("black_umbar_carved_brick", 1.5f, 6.0f);
    public static final Block BLACK_URUK_STEEL_BLOCK = registerCube("black_uruk_steel_block", 5.0f, 6.0f);
    public static final Block BLUE_CARVED_BRICK = registerCube("blue_carved_brick", 1.5f, 6.0f);
    public static final Block BLUE_DWARF_STEEL_BLOCK = registerCube("blue_dwarf_steel_block", 5.0f, 6.0f);
    public static final Block BLUE_ROCK = registerCube("blue_rock", 1.5f, 6.0f);
    public static final Block BLUE_ROCK_BRICK = registerCube("blue_rock_brick", 1.5f, 6.0f);
    public static final Block BONE_BLOCK = registerCube("bone_block", 5.0f, 6.0f);
    public static final Block BRONZE_BLOCK = registerCube("bronze_block", 5.0f, 6.0f);
    public static final Block CHALK = registerCube("chalk", 1.5f, 6.0f);
    public static final Block CHALK_BRICK = registerCube("chalk_brick", 1.5f, 6.0f);
    public static final Block CLAY_TILE = registerCube("clay_tile", 1.5f, 6.0f);
    public static final Block CLAY_TILE_DYED_BLACK = registerCube("clay_tile_dyed_black", 1.5f, 6.0f);
    public static final Block CLAY_TILE_DYED_BLUE = registerCube("clay_tile_dyed_blue", 1.5f, 6.0f);
    public static final Block CLAY_TILE_DYED_BROWN = registerCube("clay_tile_dyed_brown", 1.5f, 6.0f);
    public static final Block CLAY_TILE_DYED_CYAN = registerCube("clay_tile_dyed_cyan", 1.5f, 6.0f);
    public static final Block CLAY_TILE_DYED_GRAY = registerCube("clay_tile_dyed_gray", 1.5f, 6.0f);
    public static final Block CLAY_TILE_DYED_GREEN = registerCube("clay_tile_dyed_green", 1.5f, 6.0f);
    public static final Block CLAY_TILE_DYED_LIGHT_BLUE = registerCube("clay_tile_dyed_light_blue", 1.5f, 6.0f);
    public static final Block CLAY_TILE_DYED_LIME = registerCube("clay_tile_dyed_lime", 1.5f, 6.0f);
    public static final Block CLAY_TILE_DYED_MAGENTA = registerCube("clay_tile_dyed_magenta", 1.5f, 6.0f);
    public static final Block CLAY_TILE_DYED_ORANGE = registerCube("clay_tile_dyed_orange", 1.5f, 6.0f);
    public static final Block CLAY_TILE_DYED_PINK = registerCube("clay_tile_dyed_pink", 1.5f, 6.0f);
    public static final Block CLAY_TILE_DYED_PURPLE = registerCube("clay_tile_dyed_purple", 1.5f, 6.0f);
    public static final Block CLAY_TILE_DYED_RED = registerCube("clay_tile_dyed_red", 1.5f, 6.0f);
    public static final Block CLAY_TILE_DYED_SILVER = registerCube("clay_tile_dyed_silver", 1.5f, 6.0f);
    public static final Block CLAY_TILE_DYED_WHITE = registerCube("clay_tile_dyed_white", 1.5f, 6.0f);
    public static final Block CLAY_TILE_DYED_YELLOW = registerCube("clay_tile_dyed_yellow", 1.5f, 6.0f);
    public static final Block CORAL_BLOCK = registerCube("coral_block", 5.0f, 6.0f);
    public static final Block DALE_BRICK = registerCube("dale_brick", 1.5f, 6.0f);
    public static final Block DALE_CARVED_BRICK = registerCube("dale_carved_brick", 1.5f, 6.0f);
    public static final Block DALE_CRACKED_BRICK = registerCube("dale_cracked_brick", 1.5f, 6.0f);
    public static final Block DALE_MOSSY_BRICK = registerCube("dale_mossy_brick", 1.5f, 6.0f);
    public static final Block DIAMOND_BLOCK = registerCube("diamond_block", 5.0f, 6.0f);
    public static final Block DIAMOND_ORE = registerCube("diamond_ore", 3.0f, 5.0f);
    public static final Block DOL_AMROTH_BRICK = registerCube("dol_amroth_brick", 1.5f, 6.0f);
    public static final Block DOL_GULDUR_BRICK = registerCube("dol_guldur_brick", 1.5f, 6.0f);
    public static final Block DOL_GULDUR_CARVED_BRICK = registerCube("dol_guldur_carved_brick", 1.5f, 6.0f);
    public static final Block DOL_GULDUR_CRACKED_BRICK = registerCube("dol_guldur_cracked_brick", 1.5f, 6.0f);
    public static final Block DOL_GULDUR_MOSSY_BRICK = registerCube("dol_guldur_mossy_brick", 1.5f, 6.0f);
    public static final Block DORWINION_BRICK = registerCube("dorwinion_brick", 1.5f, 6.0f);
    public static final Block DORWINION_CARVED_BRICK = registerCube("dorwinion_carved_brick", 1.5f, 6.0f);
    public static final Block DORWINION_CRACKED_BRICK = registerCube("dorwinion_cracked_brick", 1.5f, 6.0f);
    public static final Block DORWINION_FLOWERS_BRICK = registerCube("dorwinion_flowers_brick", 1.5f, 6.0f);
    public static final Block DORWINION_MOSSY_BRICK = registerCube("dorwinion_mossy_brick", 1.5f, 6.0f);
    public static final Block DWARF_STEEL_BLOCK = registerCube("dwarf_steel_block", 5.0f, 6.0f);
    public static final Block DWARVEN_BRICK = registerCube("dwarven_brick", 1.5f, 6.0f);
    public static final Block DWARVEN_CARVED_BRICK = registerCube("dwarven_carved_brick", 1.5f, 6.0f);
    public static final Block DWARVEN_CRACKED_BRICK = registerCube("dwarven_cracked_brick", 1.5f, 6.0f);
    public static final Block DWARVEN_GLOWING_BRICK = registerCube("dwarven_glowing_brick", 1.5f, 6.0f);
    public static final Block DWARVEN_OBSIDIAN_BRICK = registerCube("dwarven_obsidian_brick", 1.5f, 6.0f);
    public static final Block ELF_STEEL_BLOCK = registerCube("elf_steel_block", 5.0f, 6.0f);
    public static final Block EMERALD_BLOCK = registerCube("emerald_block", 5.0f, 6.0f);
    public static final Block EMERALD_ORE = registerCube("emerald_ore", 3.0f, 5.0f);
    public static final Block GALADHRIM_BRICK = registerCube("galadhrim_brick", 1.5f, 6.0f);
    public static final Block GALADHRIM_CARVED_BRICK = registerCube("galadhrim_carved_brick", 1.5f, 6.0f);
    public static final Block GALADHRIM_CRACKED_BRICK = registerCube("galadhrim_cracked_brick", 1.5f, 6.0f);
    public static final Block GALADHRIM_GOLD_BRICK = registerCube("galadhrim_gold_brick", 1.5f, 6.0f);
    public static final Block GALADHRIM_MOSSY_BRICK = registerCube("galadhrim_mossy_brick", 1.5f, 6.0f);
    public static final Block GALADHRIM_SILVER_BRICK = registerCube("galadhrim_silver_brick", 1.5f, 6.0f);
    public static final Block GALVORN_BLOCK = registerCube("galvorn_block", 5.0f, 6.0f);
    public static final Block GILDED_IRON_BLOCK = registerCube("gilded_iron_block", 5.0f, 6.0f);
    public static final Block GLOWSTONE_ORE = registerCube("glowstone_ore", 3.0f, 5.0f);
    public static final Block GONDOR_BRICK = registerCube("gondor_brick", 1.5f, 6.0f);
    public static final Block GONDOR_CARVED_BRICK = registerCube("gondor_carved_brick", 1.5f, 6.0f);
    public static final Block GONDOR_CRACKED_BRICK = registerCube("gondor_cracked_brick", 1.5f, 6.0f);
    public static final Block GONDOR_MOSSY_BRICK = registerCube("gondor_mossy_brick", 1.5f, 6.0f);
    public static final Block GONDOR_ROCK = registerCube("gondor_rock", 1.5f, 6.0f);
    public static final Block GONDOR_RUSTIC_BRICK = registerCube("gondor_rustic_brick", 1.5f, 6.0f);
    public static final Block GONDOR_RUSTIC_CRACKED_BRICK = registerCube("gondor_rustic_cracked_brick", 1.5f, 6.0f);
    public static final Block GONDOR_RUSTIC_MOSSY_BRICK = registerCube("gondor_rustic_mossy_brick", 1.5f, 6.0f);
    public static final Block GORAN_ROCK = registerCube("goran_rock", 1.5f, 6.0f);
    public static final Block GRAVEL = registerCube("gravel", 0.6f, 0.6f);
    public static final Block GULDURIL_BLOCK = registerCube("gulduril_block", 5.0f, 6.0f);
    public static final Block GULDURIL_MORDOR_ORE = registerCube("gulduril_mordor_ore", 3.0f, 5.0f);
    public static final Block GULDURIL_ORE = registerCube("gulduril_ore", 3.0f, 5.0f);
    public static final Block HIGH_ELVEN_BRICK = registerCube("high_elven_brick", 1.5f, 6.0f);
    public static final Block HIGH_ELVEN_CARVED_BRICK = registerCube("high_elven_carved_brick", 1.5f, 6.0f);
    public static final Block HIGH_ELVEN_CRACKED_BRICK = registerCube("high_elven_cracked_brick", 1.5f, 6.0f);
    public static final Block HIGH_ELVEN_GOLD_BRICK = registerCube("high_elven_gold_brick", 1.5f, 6.0f);
    public static final Block HIGH_ELVEN_MOSSY_BRICK = registerCube("high_elven_mossy_brick", 1.5f, 6.0f);
    public static final Block HIGH_ELVEN_SILVER_BRICK = registerCube("high_elven_silver_brick", 1.5f, 6.0f);
    public static final Block MITHRIL_BLOCK = registerCube("mithril_block", 5.0f, 6.0f);
    public static final Block MORDOR_BRICK = registerCube("mordor_brick", 1.5f, 6.0f);
    public static final Block MORDOR_CARVED_BRICK = registerCube("mordor_carved_brick", 1.5f, 6.0f);
    public static final Block MORDOR_CRACKED_BRICK = registerCube("mordor_cracked_brick", 1.5f, 6.0f);
    public static final Block MORDOR_DIRT = registerCube("mordor_dirt", 0.5f, 0.5f);
    public static final Block MORDOR_GRAVEL = registerCube("mordor_gravel", 0.6f, 0.6f);
    public static final Block MORDOR_MOSS_ROCK = registerCube("mordor_moss_rock", 1.5f, 6.0f);
    public static final Block MORDOR_ROCK = registerCube("mordor_rock", 1.5f, 6.0f);
    public static final Block MOREDAIN_BRICK = registerCube("moredain_brick", 1.5f, 6.0f);
    public static final Block MORGUL_IRON_MORDOR_ORE = registerCube("morgul_iron_mordor_ore", 3.0f, 5.0f);
    public static final Block MORGUL_IRON_ORE = registerCube("morgul_iron_ore", 3.0f, 5.0f);
    public static final Block MORGUL_STEEL_BLOCK = registerCube("morgul_steel_block", 5.0f, 6.0f);
    public static final Block MORWAITH_CRACKED_BRICK = registerCube("morwaith_cracked_brick", 1.5f, 6.0f);
    public static final Block NAURITE_BLOCK = registerCube("naurite_block", 5.0f, 6.0f);
    public static final Block NAURITE_ORE = registerCube("naurite_ore", 3.0f, 5.0f);
    public static final Block NEAR_HARAD_BRICK = registerCube("near_harad_brick", 1.5f, 6.0f);
    public static final Block NEAR_HARAD_CARVED_BRICK = registerCube("near_harad_carved_brick", 1.5f, 6.0f);
    public static final Block NEAR_HARAD_CRACKED_BRICK = registerCube("near_harad_cracked_brick", 1.5f, 6.0f);
    public static final Block NEAR_HARAD_LAPIS_BRICK = registerCube("near_harad_lapis_brick", 1.5f, 6.0f);
    public static final Block NEAR_HARAD_RED_BRICK = registerCube("near_harad_red_brick", 1.5f, 6.0f);
    public static final Block NEAR_HARAD_RED_CARVED_BRICK = registerCube("near_harad_red_carved_brick", 1.5f, 6.0f);
    public static final Block NEAR_HARAD_RED_CRACKED_BRICK = registerCube("near_harad_red_cracked_brick", 1.5f, 6.0f);
    public static final Block OBSIDIAN_GRAVEL = registerCube("obsidian_gravel", 0.6f, 0.6f);
    public static final Block OPAL_BLOCK = registerCube("opal_block", 5.0f, 6.0f);
    public static final Block OPAL_ORE = registerCube("opal_ore", 3.0f, 5.0f);
    public static final Block ORC_PLATING_IRON = registerCube("orc_plating_iron", 1.5f, 6.0f);
    public static final Block ORC_PLATING_RUST = registerCube("orc_plating_rust", 1.5f, 6.0f);
    public static final Block ORC_STEEL_BLOCK = registerCube("orc_steel_block", 5.0f, 6.0f);
    public static final Block PEARL_BLOCK = registerCube("pearl_block", 5.0f, 6.0f);
    public static final Block QUAGMIRE = registerCube("quagmire", 0.5f, 0.5f);
    public static final Block QUENDITE_BLOCK = registerCube("quendite_block", 5.0f, 6.0f);
    public static final Block QUENDITE_ORE = registerCube("quendite_ore", 3.0f, 5.0f);
    public static final Block RED_BRICK_CRACKED = registerCube("red_brick_cracked", 1.5f, 6.0f);
    public static final Block RED_BRICK_MOSSY = registerCube("red_brick_mossy", 1.5f, 6.0f);
    public static final Block RED_CARVED_BRICK = registerCube("red_carved_brick", 1.5f, 6.0f);
    public static final Block RED_CLAY = registerCube("red_clay", 0.5f, 0.5f);
    public static final Block RED_ROCK = registerCube("red_rock", 1.5f, 6.0f);
    public static final Block RED_ROCK_BRICK = registerCube("red_rock_brick", 1.5f, 6.0f);
    public static final Block RED_SANDSTONE = registerCube("red_sandstone", 1.5f, 6.0f);
    public static final Block RHUN_BRICK = registerCube("rhun_brick", 1.5f, 6.0f);
    public static final Block RHUN_CARVED_BRICK = registerCube("rhun_carved_brick", 1.5f, 6.0f);
    public static final Block RHUN_CRACKED_BRICK = registerCube("rhun_cracked_brick", 1.5f, 6.0f);
    public static final Block RHUN_FLOWERS_BRICK = registerCube("rhun_flowers_brick", 1.5f, 6.0f);
    public static final Block RHUN_GOLD_BRICK = registerCube("rhun_gold_brick", 1.5f, 6.0f);
    public static final Block RHUN_MOSSY_BRICK = registerCube("rhun_mossy_brick", 1.5f, 6.0f);
    public static final Block RHUN_RED_BRICK = registerCube("rhun_red_brick", 1.5f, 6.0f);
    public static final Block RHUN_RED_CARVED_BRICK = registerCube("rhun_red_carved_brick", 1.5f, 6.0f);
    public static final Block ROHAN_BRICK = registerCube("rohan_brick", 1.5f, 6.0f);
    public static final Block ROHAN_CARVED_BRICK = registerCube("rohan_carved_brick", 1.5f, 6.0f);
    public static final Block ROHAN_ROCK = registerCube("rohan_rock", 1.5f, 6.0f);
    public static final Block RUBY_BLOCK = registerCube("ruby_block", 5.0f, 6.0f);
    public static final Block RUBY_ORE = registerCube("ruby_ore", 3.0f, 5.0f);
    public static final Block SALT_BLOCK = registerCube("salt_block", 5.0f, 6.0f);
    public static final Block SALTPETER_BLOCK = registerCube("saltpeter_block", 5.0f, 6.0f);
    public static final Block SAPPHIRE_BLOCK = registerCube("sapphire_block", 5.0f, 6.0f);
    public static final Block SAPPHIRE_ORE = registerCube("sapphire_ore", 3.0f, 5.0f);
    public static final Block SCORCHED_STONE = registerCube("scorched_stone", 1.5f, 6.0f);
    public static final Block SILVER_BLOCK = registerCube("silver_block", 5.0f, 6.0f);
    public static final Block SULFUR_BLOCK = registerCube("sulfur_block", 5.0f, 6.0f);
    public static final Block TAUREDAIN_BRICK = registerCube("tauredain_brick", 1.5f, 6.0f);
    public static final Block TAUREDAIN_CRACKED_BRICK = registerCube("tauredain_cracked_brick", 1.5f, 6.0f);
    public static final Block TAUREDAIN_GOLD_BRICK = registerCube("tauredain_gold_brick", 1.5f, 6.0f);
    public static final Block TAUREDAIN_MOSSY_BRICK = registerCube("tauredain_mossy_brick", 1.5f, 6.0f);
    public static final Block TAUREDAIN_OBSIDIAN_BRICK = registerCube("tauredain_obsidian_brick", 1.5f, 6.0f);
    public static final Block TIN_BLOCK = registerCube("tin_block", 5.0f, 6.0f);
    public static final Block UMBAR_BRICK = registerCube("umbar_brick", 1.5f, 6.0f);
    public static final Block UMBAR_CARVED_BRICK = registerCube("umbar_carved_brick", 1.5f, 6.0f);
    public static final Block UMBAR_CRACKED_BRICK = registerCube("umbar_cracked_brick", 1.5f, 6.0f);
    public static final Block URUK_BRICK = registerCube("uruk_brick", 1.5f, 6.0f);
    public static final Block URUK_STEEL_BLOCK = registerCube("uruk_steel_block", 5.0f, 6.0f);
    public static final Block UTUMNO_FIRE_BRICK = registerCube("utumno_fire_brick", 1.5f, 6.0f);
    public static final Block UTUMNO_FIRE_TILE_BRICK = registerCube("utumno_fire_tile_brick", 1.5f, 6.0f);
    public static final Block UTUMNO_ICE_BRICK = registerCube("utumno_ice_brick", 1.5f, 6.0f);
    public static final Block UTUMNO_ICE_GLOWING_BRICK = registerCube("utumno_ice_glowing_brick", 1.5f, 6.0f);
    public static final Block UTUMNO_ICE_TILE_BRICK = registerCube("utumno_ice_tile_brick", 1.5f, 6.0f);
    public static final Block UTUMNO_OBSIDIAN_BRICK = registerCube("utumno_obsidian_brick", 1.5f, 6.0f);
    public static final Block UTUMNO_OBSIDIAN_FIRE_BRICK = registerCube("utumno_obsidian_fire_brick", 1.5f, 6.0f);
    public static final Block UTUMNO_OBSIDIAN_TILE_BRICK = registerCube("utumno_obsidian_tile_brick", 1.5f, 6.0f);
    public static final Block WHITE_SAND = registerCube("white_sand", 0.6f, 0.6f);
    public static final Block WHITE_SANDSTONE = registerCube("white_sandstone", 1.5f, 6.0f);
    public static final Block WOOD_ELVEN_BRICK = registerCube("wood_elven_brick", 1.5f, 6.0f);
    public static final Block WOOD_ELVEN_CARVED_BRICK = registerCube("wood_elven_carved_brick", 1.5f, 6.0f);
    public static final Block WOOD_ELVEN_CRACKED_BRICK = registerCube("wood_elven_cracked_brick", 1.5f, 6.0f);
    public static final Block WOOD_ELVEN_GOLD_BRICK = registerCube("wood_elven_gold_brick", 1.5f, 6.0f);
    public static final Block WOOD_ELVEN_MOSSY_BRICK = registerCube("wood_elven_mossy_brick", 1.5f, 6.0f);
    public static final Block WOOD_ELVEN_SILVER_BRICK = registerCube("wood_elven_silver_brick", 1.5f, 6.0f);

    // --- Wooden planks ---
    public static final Block ALMOND_PLANKS = registerPlanks("almond_planks");
    public static final Block APPLE_PLANKS = registerPlanks("apple_planks");
    public static final Block ASPEN_PLANKS = registerPlanks("aspen_planks");
    public static final Block BANANA_PLANKS = registerPlanks("banana_planks");
    public static final Block BAOBAB_PLANKS = registerPlanks("baobab_planks");
    public static final Block BEECH_PLANKS = registerPlanks("beech_planks");
    public static final Block CEDAR_PLANKS = registerPlanks("cedar_planks");
    public static final Block CHARRED_PLANKS = registerPlanks("charred_planks");
    public static final Block CHERRY_PLANKS = registerPlanks("cherry_planks");
    public static final Block CHESTNUT_PLANKS = registerPlanks("chestnut_planks");
    public static final Block CYPRESS_PLANKS = registerPlanks("cypress_planks");
    public static final Block DATE_PALM_PLANKS = registerPlanks("date_palm_planks");
    public static final Block DRAGON_PLANKS = registerPlanks("dragon_planks");
    public static final Block FIR_PLANKS = registerPlanks("fir_planks");
    public static final Block GREEN_OAK_PLANKS = registerPlanks("green_oak_planks");
    public static final Block HOLLY_PLANKS = registerPlanks("holly_planks");
    public static final Block KANUKA_PLANKS = registerPlanks("kanuka_planks");
    public static final Block LAIRELOSSE_PLANKS = registerPlanks("lairelosse_planks");
    public static final Block LARCH_PLANKS = registerPlanks("larch_planks");
    public static final Block LEBETHRON_PLANKS = registerPlanks("lebethron_planks");
    public static final Block LEMON_PLANKS = registerPlanks("lemon_planks");
    public static final Block LIME_PLANKS = registerPlanks("lime_planks");
    public static final Block MAHOGANY_PLANKS = registerPlanks("mahogany_planks");
    public static final Block MALLORN_PLANKS = registerPlanks("mallorn_planks");
    public static final Block MANGO_PLANKS = registerPlanks("mango_planks");
    public static final Block MANGROVE_PLANKS = registerPlanks("mangrove_planks");
    public static final Block MAPLE_PLANKS = registerPlanks("maple_planks");
    public static final Block MIRK_OAK_PLANKS = registerPlanks("mirk_oak_planks");
    public static final Block OLIVE_PLANKS = registerPlanks("olive_planks");
    public static final Block ORANGE_PLANKS = registerPlanks("orange_planks");
    public static final Block PALM_PLANKS = registerPlanks("palm_planks");
    public static final Block PEAR_PLANKS = registerPlanks("pear_planks");
    public static final Block PINE_PLANKS = registerPlanks("pine_planks");
    public static final Block PLUM_PLANKS = registerPlanks("plum_planks");
    public static final Block POMEGRANATE_PLANKS = registerPlanks("pomegranate_planks");
    public static final Block REDWOOD_PLANKS = registerPlanks("redwood_planks");
    public static final Block ROTTEN_PLANKS = registerPlanks("rotten_planks");
    public static final Block SHIRE_PINE_PLANKS = registerPlanks("shire_pine_planks");
    public static final Block WILLOW_PLANKS = registerPlanks("willow_planks");

    private LOTRBlocks() {
    }

    /** Solid stone-like cube: requires correct tool, stone sound. */
    private static Block registerCube(String name, float hardness, float resistance) {
        return register(name, Block::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.STONE)
                        .requiresCorrectToolForDrops()
                        .strength(hardness, resistance)
                        .sound(SoundType.STONE),
                true);
    }

    /** Wooden planks: wood sound, flammable, axe-mineable, no tool required. */
    private static Block registerPlanks(String name) {
        return register(name, Block::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.WOOD)
                        .instrument(NoteBlockInstrument.BASS)
                        .strength(2.0f, 3.0f)
                        .sound(SoundType.WOOD)
                        .ignitedByLava(),
                true);
    }

    public static Block register(String name, Function<BlockBehaviour.Properties, Block> factory,
                                 BlockBehaviour.Properties properties, boolean withItem) {
        ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, name));
        Block block = factory.apply(properties.setId(blockKey));
        Registry.register(BuiltInRegistries.BLOCK, blockKey, block);

        if (withItem) {
            ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM,
                    Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, name));
            BlockItem blockItem = new BlockItem(block,
                    new Item.Properties().setId(itemKey).useBlockDescriptionPrefix());
            Registry.register(BuiltInRegistries.ITEM, itemKey, blockItem);
        }
        return block;
    }

    public static void init() {
    }
}