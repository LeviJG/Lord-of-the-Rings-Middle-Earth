package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import java.util.ArrayList;
import java.util.List;
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
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

/**
 * Central block registry.
 *
 * Every registerX() helper appends the block it creates to the matching family
 * list below. Datagen (models, loot, tags) then iterates those lists instead of
 * naming all 443 blocks four separate times -- so adding a block here is the
 * only edit needed for it to get a model, a loot table and a mining tag.
 *
 * NOTE: several blocks share a name with vanilla (birch/spruce/jungle/acacia/
 * dark_oak/cherry variants). They are namespaced (lotr:...) so no conflict.
 * "silver_stained_glass" keeps the original mod's colour name; vanilla calls
 * that colour "light_gray".
 */
public final class LOTRBlocks {

    /** Harvest tier for stone-like cubes; drives NEEDS_STONE_TOOL / NEEDS_IRON_TOOL. */
    public enum Tier {
        STONE, IRON
    }

    // ------------------------------------------------------------------
    // Family lists.
    //
    // These MUST stay above the first block field. Java runs static
    // initialisers in textual order, and every registerX() call below writes
    // into one of these lists -- move them underneath and you get a
    // NullPointerException the moment the class loads.
    // ------------------------------------------------------------------
    public static final List<Block> ALL_CUBES = new ArrayList<>();
    public static final List<Block> CUBES_STONE_TIER = new ArrayList<>();
    public static final List<Block> CUBES_IRON_TIER = new ArrayList<>();
    public static final List<Block> ALL_PLANKS = new ArrayList<>();
    public static final List<Block> ALL_LEAVES = new ArrayList<>();
    public static final List<Block> ALL_SAPLINGS = new ArrayList<>();
    public static final List<Block> ALL_TRAPDOORS = new ArrayList<>();
    public static final List<Block> ALL_DOORS = new ArrayList<>();
    public static final List<Block> ALL_BARS = new ArrayList<>();
    public static final List<Block> ALL_CHANDELIERS = new ArrayList<>();
    public static final List<Block> ALL_GLASS = new ArrayList<>();

    // --- Ores ---
    public static final Block TIN_ORE = registerCube("tin_ore", 3.0f, 5.0f, Tier.STONE);
    public static final Block SILVER_ORE = registerCube("silver_ore", 3.0f, 5.0f, Tier.IRON);
    public static final Block MITHRIL_ORE = registerCube("mithril_ore", 4.0f, 10.0f, Tier.IRON);
    public static final Block SALT_ORE = registerCube("salt_ore", 3.0f, 5.0f, Tier.STONE);
    public static final Block SALTPETER_ORE = registerCube("saltpeter_ore", 3.0f, 5.0f, Tier.STONE);
    public static final Block SULFUR_ORE = registerCube("sulfur_ore", 3.0f, 5.0f, Tier.STONE);

    // --- Stone-like cube blocks ---
    public static final Block AMBER_BLOCK = registerCube("amber_block", 5.0f, 6.0f, Tier.IRON);
    public static final Block AMBER_ORE = registerCube("amber_ore", 3.0f, 5.0f, Tier.IRON);
    public static final Block AMETHYST_BLOCK = registerCube("amethyst_block", 5.0f, 6.0f, Tier.IRON);
    public static final Block AMETHYST_ORE = registerCube("amethyst_ore", 3.0f, 5.0f, Tier.IRON);
    public static final Block ANGMAR_BRICK = registerCube("angmar_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block ANGMAR_CRACKED_BRICK = registerCube("angmar_cracked_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block ANGMAR_SNOW_BRICK = registerCube("angmar_snow_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block ARNOR_BRICK = registerCube("arnor_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block ARNOR_CARVED_BRICK = registerCube("arnor_carved_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block ARNOR_CRACKED_BRICK = registerCube("arnor_cracked_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block ARNOR_MOSSY_BRICK = registerCube("arnor_mossy_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block BLACK_GONDOR_BRICK = registerCube("black_gondor_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block BLACK_GONDOR_CARVED_BRICK = registerCube("black_gondor_carved_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block BLACK_UMBAR_CARVED_BRICK = registerCube("black_umbar_carved_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block BLACK_URUK_STEEL_BLOCK = registerCube("black_uruk_steel_block", 5.0f, 6.0f, Tier.IRON);
    public static final Block BLUE_CARVED_BRICK = registerCube("blue_carved_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block BLUE_DWARF_STEEL_BLOCK = registerCube("blue_dwarf_steel_block", 5.0f, 6.0f, Tier.STONE);
    public static final Block BLUE_ROCK = registerCube("blue_rock", 1.5f, 6.0f, Tier.STONE);
    public static final Block BLUE_ROCK_BRICK = registerCube("blue_rock_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block BONE_BLOCK = registerCube("bone_block", 5.0f, 6.0f, Tier.STONE);
    public static final Block BRONZE_BLOCK = registerCube("bronze_block", 5.0f, 6.0f, Tier.STONE);
    public static final Block CHALK = registerCube("chalk", 1.5f, 6.0f, Tier.STONE);
    public static final Block CHALK_BRICK = registerCube("chalk_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block CLAY_TILE = registerCube("clay_tile", 1.5f, 6.0f, Tier.STONE);
    public static final Block CLAY_TILE_DYED_BLACK = registerCube("clay_tile_dyed_black", 1.5f, 6.0f, Tier.STONE);
    public static final Block CLAY_TILE_DYED_BLUE = registerCube("clay_tile_dyed_blue", 1.5f, 6.0f, Tier.STONE);
    public static final Block CLAY_TILE_DYED_BROWN = registerCube("clay_tile_dyed_brown", 1.5f, 6.0f, Tier.STONE);
    public static final Block CLAY_TILE_DYED_CYAN = registerCube("clay_tile_dyed_cyan", 1.5f, 6.0f, Tier.STONE);
    public static final Block CLAY_TILE_DYED_GRAY = registerCube("clay_tile_dyed_gray", 1.5f, 6.0f, Tier.STONE);
    public static final Block CLAY_TILE_DYED_GREEN = registerCube("clay_tile_dyed_green", 1.5f, 6.0f, Tier.STONE);
    public static final Block CLAY_TILE_DYED_LIGHT_BLUE = registerCube("clay_tile_dyed_light_blue", 1.5f, 6.0f, Tier.STONE);
    public static final Block CLAY_TILE_DYED_LIME = registerCube("clay_tile_dyed_lime", 1.5f, 6.0f, Tier.STONE);
    public static final Block CLAY_TILE_DYED_MAGENTA = registerCube("clay_tile_dyed_magenta", 1.5f, 6.0f, Tier.STONE);
    public static final Block CLAY_TILE_DYED_ORANGE = registerCube("clay_tile_dyed_orange", 1.5f, 6.0f, Tier.STONE);
    public static final Block CLAY_TILE_DYED_PINK = registerCube("clay_tile_dyed_pink", 1.5f, 6.0f, Tier.STONE);
    public static final Block CLAY_TILE_DYED_PURPLE = registerCube("clay_tile_dyed_purple", 1.5f, 6.0f, Tier.STONE);
    public static final Block CLAY_TILE_DYED_RED = registerCube("clay_tile_dyed_red", 1.5f, 6.0f, Tier.STONE);
    public static final Block CLAY_TILE_DYED_SILVER = registerCube("clay_tile_dyed_silver", 1.5f, 6.0f, Tier.STONE);
    public static final Block CLAY_TILE_DYED_WHITE = registerCube("clay_tile_dyed_white", 1.5f, 6.0f, Tier.STONE);
    public static final Block CLAY_TILE_DYED_YELLOW = registerCube("clay_tile_dyed_yellow", 1.5f, 6.0f, Tier.STONE);
    public static final Block CORAL_BLOCK = registerCube("coral_block", 5.0f, 6.0f, Tier.IRON);
    public static final Block DALE_BRICK = registerCube("dale_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block DALE_CARVED_BRICK = registerCube("dale_carved_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block DALE_CRACKED_BRICK = registerCube("dale_cracked_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block DALE_MOSSY_BRICK = registerCube("dale_mossy_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block DIAMOND_BLOCK = registerCube("diamond_block", 5.0f, 6.0f, Tier.IRON);
    public static final Block DIAMOND_ORE = registerCube("diamond_ore", 3.0f, 5.0f, Tier.IRON);
    public static final Block DOL_AMROTH_BRICK = registerCube("dol_amroth_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block DOL_GULDUR_BRICK = registerCube("dol_guldur_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block DOL_GULDUR_CARVED_BRICK = registerCube("dol_guldur_carved_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block DOL_GULDUR_CRACKED_BRICK = registerCube("dol_guldur_cracked_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block DOL_GULDUR_MOSSY_BRICK = registerCube("dol_guldur_mossy_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block DORWINION_BRICK = registerCube("dorwinion_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block DORWINION_CARVED_BRICK = registerCube("dorwinion_carved_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block DORWINION_CRACKED_BRICK = registerCube("dorwinion_cracked_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block DORWINION_FLOWERS_BRICK = registerCube("dorwinion_flowers_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block DORWINION_MOSSY_BRICK = registerCube("dorwinion_mossy_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block DWARF_STEEL_BLOCK = registerCube("dwarf_steel_block", 5.0f, 6.0f, Tier.STONE);
    public static final Block DWARVEN_BRICK = registerCube("dwarven_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block DWARVEN_CARVED_BRICK = registerCube("dwarven_carved_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block DWARVEN_CRACKED_BRICK = registerCube("dwarven_cracked_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block DWARVEN_GLOWING_BRICK = registerCube("dwarven_glowing_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block DWARVEN_OBSIDIAN_BRICK = registerCube("dwarven_obsidian_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block ELF_STEEL_BLOCK = registerCube("elf_steel_block", 5.0f, 6.0f, Tier.IRON);
    public static final Block EMERALD_BLOCK = registerCube("emerald_block", 5.0f, 6.0f, Tier.IRON);
    public static final Block EMERALD_ORE = registerCube("emerald_ore", 3.0f, 5.0f, Tier.IRON);
    public static final Block GALADHRIM_BRICK = registerCube("galadhrim_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block GALADHRIM_CARVED_BRICK = registerCube("galadhrim_carved_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block GALADHRIM_CRACKED_BRICK = registerCube("galadhrim_cracked_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block GALADHRIM_GOLD_BRICK = registerCube("galadhrim_gold_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block GALADHRIM_MOSSY_BRICK = registerCube("galadhrim_mossy_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block GALADHRIM_SILVER_BRICK = registerCube("galadhrim_silver_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block GALVORN_BLOCK = registerCube("galvorn_block", 5.0f, 6.0f, Tier.IRON);
    public static final Block GILDED_IRON_BLOCK = registerCube("gilded_iron_block", 5.0f, 6.0f, Tier.STONE);
    public static final Block GLOWSTONE_ORE = registerCube("glowstone_ore", 3.0f, 5.0f, Tier.STONE);
    public static final Block GONDOR_BRICK = registerCube("gondor_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block GONDOR_CARVED_BRICK = registerCube("gondor_carved_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block GONDOR_CRACKED_BRICK = registerCube("gondor_cracked_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block GONDOR_MOSSY_BRICK = registerCube("gondor_mossy_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block GONDOR_ROCK = registerCube("gondor_rock", 1.5f, 6.0f, Tier.STONE);
    public static final Block GONDOR_RUSTIC_BRICK = registerCube("gondor_rustic_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block GONDOR_RUSTIC_CRACKED_BRICK = registerCube("gondor_rustic_cracked_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block GONDOR_RUSTIC_MOSSY_BRICK = registerCube("gondor_rustic_mossy_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block GORAN_ROCK = registerCube("goran_rock", 1.5f, 6.0f, Tier.STONE);
    public static final Block GRAVEL = registerCube("gravel", 0.6f, 0.6f, Tier.STONE);
    public static final Block GULDURIL_BLOCK = registerCube("gulduril_block", 5.0f, 6.0f, Tier.IRON);
    public static final Block GULDURIL_MORDOR_ORE = registerCube("gulduril_mordor_ore", 3.0f, 5.0f, Tier.IRON);
    public static final Block GULDURIL_ORE = registerCube("gulduril_ore", 3.0f, 5.0f, Tier.IRON);
    public static final Block HIGH_ELVEN_BRICK = registerCube("high_elven_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block HIGH_ELVEN_CARVED_BRICK = registerCube("high_elven_carved_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block HIGH_ELVEN_CRACKED_BRICK = registerCube("high_elven_cracked_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block HIGH_ELVEN_GOLD_BRICK = registerCube("high_elven_gold_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block HIGH_ELVEN_MOSSY_BRICK = registerCube("high_elven_mossy_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block HIGH_ELVEN_SILVER_BRICK = registerCube("high_elven_silver_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block MITHRIL_BLOCK = registerCube("mithril_block", 5.0f, 6.0f, Tier.IRON);
    public static final Block MORDOR_BRICK = registerCube("mordor_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block MORDOR_CARVED_BRICK = registerCube("mordor_carved_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block MORDOR_CRACKED_BRICK = registerCube("mordor_cracked_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block MORDOR_DIRT = registerCube("mordor_dirt", 0.5f, 0.5f, Tier.STONE);
    public static final Block MORDOR_GRAVEL = registerCube("mordor_gravel", 0.6f, 0.6f, Tier.STONE);
    public static final Block MORDOR_MOSS_ROCK = registerCube("mordor_moss_rock", 1.5f, 6.0f, Tier.STONE);
    public static final Block MORDOR_ROCK = registerCube("mordor_rock", 1.5f, 6.0f, Tier.STONE);
    public static final Block MOREDAIN_BRICK = registerCube("moredain_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block MORGUL_IRON_MORDOR_ORE = registerCube("morgul_iron_mordor_ore", 3.0f, 5.0f, Tier.IRON);
    public static final Block MORGUL_IRON_ORE = registerCube("morgul_iron_ore", 3.0f, 5.0f, Tier.IRON);
    public static final Block MORGUL_STEEL_BLOCK = registerCube("morgul_steel_block", 5.0f, 6.0f, Tier.IRON);
    public static final Block MORWAITH_CRACKED_BRICK = registerCube("morwaith_cracked_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block NAURITE_BLOCK = registerCube("naurite_block", 5.0f, 6.0f, Tier.IRON);
    public static final Block NAURITE_ORE = registerCube("naurite_ore", 3.0f, 5.0f, Tier.IRON);
    public static final Block NEAR_HARAD_BRICK = registerCube("near_harad_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block NEAR_HARAD_CARVED_BRICK = registerCube("near_harad_carved_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block NEAR_HARAD_CRACKED_BRICK = registerCube("near_harad_cracked_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block NEAR_HARAD_LAPIS_BRICK = registerCube("near_harad_lapis_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block NEAR_HARAD_RED_BRICK = registerCube("near_harad_red_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block NEAR_HARAD_RED_CARVED_BRICK = registerCube("near_harad_red_carved_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block NEAR_HARAD_RED_CRACKED_BRICK = registerCube("near_harad_red_cracked_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block OBSIDIAN_GRAVEL = registerCube("obsidian_gravel", 0.6f, 0.6f, Tier.STONE);
    public static final Block OPAL_BLOCK = registerCube("opal_block", 5.0f, 6.0f, Tier.IRON);
    public static final Block OPAL_ORE = registerCube("opal_ore", 3.0f, 5.0f, Tier.IRON);
    public static final Block ORC_PLATING_IRON = registerCube("orc_plating_iron", 1.5f, 6.0f, Tier.STONE);
    public static final Block ORC_PLATING_RUST = registerCube("orc_plating_rust", 1.5f, 6.0f, Tier.STONE);
    public static final Block ORC_STEEL_BLOCK = registerCube("orc_steel_block", 5.0f, 6.0f, Tier.STONE);
    public static final Block PEARL_BLOCK = registerCube("pearl_block", 5.0f, 6.0f, Tier.IRON);
    public static final Block QUAGMIRE = registerCube("quagmire", 0.5f, 0.5f, Tier.STONE);
    public static final Block QUENDITE_BLOCK = registerCube("quendite_block", 5.0f, 6.0f, Tier.IRON);
    public static final Block QUENDITE_ORE = registerCube("quendite_ore", 3.0f, 5.0f, Tier.IRON);
    public static final Block RED_BRICK_CRACKED = registerCube("red_brick_cracked", 1.5f, 6.0f, Tier.STONE);
    public static final Block RED_BRICK_MOSSY = registerCube("red_brick_mossy", 1.5f, 6.0f, Tier.STONE);
    public static final Block RED_CARVED_BRICK = registerCube("red_carved_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block RED_CLAY = registerCube("red_clay", 0.5f, 0.5f, Tier.STONE);
    public static final Block RED_ROCK = registerCube("red_rock", 1.5f, 6.0f, Tier.STONE);
    public static final Block RED_ROCK_BRICK = registerCube("red_rock_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block RED_SANDSTONE = registerCube("red_sandstone", 1.5f, 6.0f, Tier.STONE);
    public static final Block RHUN_BRICK = registerCube("rhun_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block RHUN_CARVED_BRICK = registerCube("rhun_carved_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block RHUN_CRACKED_BRICK = registerCube("rhun_cracked_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block RHUN_FLOWERS_BRICK = registerCube("rhun_flowers_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block RHUN_GOLD_BRICK = registerCube("rhun_gold_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block RHUN_MOSSY_BRICK = registerCube("rhun_mossy_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block RHUN_RED_BRICK = registerCube("rhun_red_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block RHUN_RED_CARVED_BRICK = registerCube("rhun_red_carved_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block ROHAN_BRICK = registerCube("rohan_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block ROHAN_CARVED_BRICK = registerCube("rohan_carved_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block ROHAN_ROCK = registerCube("rohan_rock", 1.5f, 6.0f, Tier.STONE);
    public static final Block RUBY_BLOCK = registerCube("ruby_block", 5.0f, 6.0f, Tier.IRON);
    public static final Block RUBY_ORE = registerCube("ruby_ore", 3.0f, 5.0f, Tier.IRON);
    public static final Block SALT_BLOCK = registerCube("salt_block", 5.0f, 6.0f, Tier.STONE);
    public static final Block SALTPETER_BLOCK = registerCube("saltpeter_block", 5.0f, 6.0f, Tier.STONE);
    public static final Block SAPPHIRE_BLOCK = registerCube("sapphire_block", 5.0f, 6.0f, Tier.IRON);
    public static final Block SAPPHIRE_ORE = registerCube("sapphire_ore", 3.0f, 5.0f, Tier.IRON);
    public static final Block SCORCHED_STONE = registerCube("scorched_stone", 1.5f, 6.0f, Tier.STONE);
    public static final Block SILVER_BLOCK = registerCube("silver_block", 5.0f, 6.0f, Tier.STONE);
    public static final Block SULFUR_BLOCK = registerCube("sulfur_block", 5.0f, 6.0f, Tier.STONE);
    public static final Block TAUREDAIN_BRICK = registerCube("tauredain_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block TAUREDAIN_CRACKED_BRICK = registerCube("tauredain_cracked_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block TAUREDAIN_GOLD_BRICK = registerCube("tauredain_gold_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block TAUREDAIN_MOSSY_BRICK = registerCube("tauredain_mossy_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block TAUREDAIN_OBSIDIAN_BRICK = registerCube("tauredain_obsidian_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block TIN_BLOCK = registerCube("tin_block", 5.0f, 6.0f, Tier.STONE);
    public static final Block UMBAR_BRICK = registerCube("umbar_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block UMBAR_CARVED_BRICK = registerCube("umbar_carved_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block UMBAR_CRACKED_BRICK = registerCube("umbar_cracked_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block URUK_BRICK = registerCube("uruk_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block URUK_STEEL_BLOCK = registerCube("uruk_steel_block", 5.0f, 6.0f, Tier.STONE);
    public static final Block UTUMNO_FIRE_BRICK = registerCube("utumno_fire_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block UTUMNO_FIRE_TILE_BRICK = registerCube("utumno_fire_tile_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block UTUMNO_ICE_BRICK = registerCube("utumno_ice_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block UTUMNO_ICE_GLOWING_BRICK = registerCube("utumno_ice_glowing_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block UTUMNO_ICE_TILE_BRICK = registerCube("utumno_ice_tile_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block UTUMNO_OBSIDIAN_BRICK = registerCube("utumno_obsidian_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block UTUMNO_OBSIDIAN_FIRE_BRICK = registerCube("utumno_obsidian_fire_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block UTUMNO_OBSIDIAN_TILE_BRICK = registerCube("utumno_obsidian_tile_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block WHITE_SAND = registerCube("white_sand", 0.6f, 0.6f, Tier.STONE);
    public static final Block WHITE_SANDSTONE = registerCube("white_sandstone", 1.5f, 6.0f, Tier.STONE);
    public static final Block WOOD_ELVEN_BRICK = registerCube("wood_elven_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block WOOD_ELVEN_CARVED_BRICK = registerCube("wood_elven_carved_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block WOOD_ELVEN_CRACKED_BRICK = registerCube("wood_elven_cracked_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block WOOD_ELVEN_GOLD_BRICK = registerCube("wood_elven_gold_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block WOOD_ELVEN_MOSSY_BRICK = registerCube("wood_elven_mossy_brick", 1.5f, 6.0f, Tier.STONE);
    public static final Block WOOD_ELVEN_SILVER_BRICK = registerCube("wood_elven_silver_brick", 1.5f, 6.0f, Tier.STONE);

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

    // --- Leaves (untinted, cutout) ---
    public static final Block ACACIA_LEAVES = registerLeaves("acacia_leaves");
    public static final Block ALMOND_LEAVES = registerLeaves("almond_leaves");
    public static final Block APPLE_LEAVES = registerLeaves("apple_leaves");
    public static final Block ASPEN_LEAVES = registerLeaves("aspen_leaves");
    public static final Block BANANA_LEAVES = registerLeaves("banana_leaves");
    public static final Block BAOBAB_LEAVES = registerLeaves("baobab_leaves");
    public static final Block BEECH_LEAVES = registerLeaves("beech_leaves");
    public static final Block BIRCH_LEAVES = registerLeaves("birch_leaves");
    public static final Block CEDAR_LEAVES = registerLeaves("cedar_leaves");
    public static final Block CHERRY_LEAVES = registerLeaves("cherry_leaves");
    public static final Block CHESTNUT_LEAVES = registerLeaves("chestnut_leaves");
    public static final Block CYPRESS_LEAVES = registerLeaves("cypress_leaves");
    public static final Block DARK_OAK_LEAVES = registerLeaves("dark_oak_leaves");
    public static final Block DATE_PALM_LEAVES = registerLeaves("date_palm_leaves");
    public static final Block DRAGON_LEAVES = registerLeaves("dragon_leaves");
    public static final Block FIR_LEAVES = registerLeaves("fir_leaves");
    public static final Block GREEN_OAK_LEAVES = registerLeaves("green_oak_leaves");
    public static final Block HOLLY_LEAVES = registerLeaves("holly_leaves");
    public static final Block JUNGLE_LEAVES = registerLeaves("jungle_leaves");
    public static final Block KANUKA_LEAVES = registerLeaves("kanuka_leaves");
    public static final Block LAIRELOSSE_LEAVES = registerLeaves("lairelosse_leaves");
    public static final Block LARCH_LEAVES = registerLeaves("larch_leaves");
    public static final Block LEBETHRON_LEAVES = registerLeaves("lebethron_leaves");
    public static final Block LEMON_LEAVES = registerLeaves("lemon_leaves");
    public static final Block LIME_LEAVES = registerLeaves("lime_leaves");
    public static final Block MAHOGANY_LEAVES = registerLeaves("mahogany_leaves");
    public static final Block MALLORN_LEAVES = registerLeaves("mallorn_leaves");
    public static final Block MANGO_LEAVES = registerLeaves("mango_leaves");
    public static final Block MANGROVE_LEAVES = registerLeaves("mangrove_leaves");
    public static final Block MAPLE_LEAVES = registerLeaves("maple_leaves");
    public static final Block MIRK_OAK_LEAVES = registerLeaves("mirk_oak_leaves");
    public static final Block MIRK_OAK_RED_LEAVES = registerLeaves("mirk_oak_red_leaves");
    public static final Block OAK_LEAVES = registerLeaves("oak_leaves");
    public static final Block OLIVE_LEAVES = registerLeaves("olive_leaves");
    public static final Block ORANGE_LEAVES = registerLeaves("orange_leaves");
    public static final Block PALM_LEAVES = registerLeaves("palm_leaves");
    public static final Block PEAR_LEAVES = registerLeaves("pear_leaves");
    public static final Block PINE_LEAVES = registerLeaves("pine_leaves");
    public static final Block PLUM_LEAVES = registerLeaves("plum_leaves");
    public static final Block POMEGRANATE_LEAVES = registerLeaves("pomegranate_leaves");
    public static final Block REDWOOD_LEAVES = registerLeaves("redwood_leaves");
    public static final Block SHIRE_PINE_LEAVES = registerLeaves("shire_pine_leaves");
    public static final Block SPRUCE_LEAVES = registerLeaves("spruce_leaves");
    public static final Block WILLOW_LEAVES = registerLeaves("willow_leaves");

    // --- Saplings (cross model, decorative) ---
    public static final Block ALMOND_SAPLING = registerSapling("almond_sapling");
    public static final Block APPLE_SAPLING = registerSapling("apple_sapling");
    public static final Block ASPEN_SAPLING = registerSapling("aspen_sapling");
    public static final Block BANANA_SAPLING = registerSapling("banana_sapling");
    public static final Block BAOBAB_SAPLING = registerSapling("baobab_sapling");
    public static final Block BEECH_SAPLING = registerSapling("beech_sapling");
    public static final Block CEDAR_SAPLING = registerSapling("cedar_sapling");
    public static final Block CHERRY_SAPLING = registerSapling("cherry_sapling");
    public static final Block CHESTNUT_SAPLING = registerSapling("chestnut_sapling");
    public static final Block CYPRESS_SAPLING = registerSapling("cypress_sapling");
    public static final Block DATE_PALM_SAPLING = registerSapling("date_palm_sapling");
    public static final Block DRAGON_SAPLING = registerSapling("dragon_sapling");
    public static final Block FIR_SAPLING = registerSapling("fir_sapling");
    public static final Block GREEN_OAK_SAPLING = registerSapling("green_oak_sapling");
    public static final Block HOLLY_SAPLING = registerSapling("holly_sapling");
    public static final Block KANUKA_SAPLING = registerSapling("kanuka_sapling");
    public static final Block LAIRELOSSE_SAPLING = registerSapling("lairelosse_sapling");
    public static final Block LARCH_SAPLING = registerSapling("larch_sapling");
    public static final Block LEBETHRON_SAPLING = registerSapling("lebethron_sapling");
    public static final Block LEMON_SAPLING = registerSapling("lemon_sapling");
    public static final Block LIME_SAPLING = registerSapling("lime_sapling");
    public static final Block MAHOGANY_SAPLING = registerSapling("mahogany_sapling");
    public static final Block MALLORN_SAPLING = registerSapling("mallorn_sapling");
    public static final Block MANGO_SAPLING = registerSapling("mango_sapling");
    public static final Block MANGROVE_SAPLING = registerSapling("mangrove_sapling");
    public static final Block MAPLE_SAPLING = registerSapling("maple_sapling");
    public static final Block MIRK_OAK_RED_SAPLING = registerSapling("mirk_oak_red_sapling");
    public static final Block MIRK_OAK_SAPLING = registerSapling("mirk_oak_sapling");
    public static final Block OLIVE_SAPLING = registerSapling("olive_sapling");
    public static final Block ORANGE_SAPLING = registerSapling("orange_sapling");
    public static final Block PALM_SAPLING = registerSapling("palm_sapling");
    public static final Block PEAR_SAPLING = registerSapling("pear_sapling");
    public static final Block PINE_SAPLING = registerSapling("pine_sapling");
    public static final Block PLUM_SAPLING = registerSapling("plum_sapling");
    public static final Block POMEGRANATE_SAPLING = registerSapling("pomegranate_sapling");
    public static final Block REDWOOD_SAPLING = registerSapling("redwood_sapling");
    public static final Block SHIRE_PINE_SAPLING = registerSapling("shire_pine_sapling");
    public static final Block WILLOW_SAPLING = registerSapling("willow_sapling");

    // --- Trapdoors ---
    public static final Block ACACIA_TRAPDOOR = registerTrapdoor("acacia_trapdoor");
    public static final Block ALMOND_TRAPDOOR = registerTrapdoor("almond_trapdoor");
    public static final Block APPLE_TRAPDOOR = registerTrapdoor("apple_trapdoor");
    public static final Block ASPEN_TRAPDOOR = registerTrapdoor("aspen_trapdoor");
    public static final Block BANANA_TRAPDOOR = registerTrapdoor("banana_trapdoor");
    public static final Block BAOBAB_TRAPDOOR = registerTrapdoor("baobab_trapdoor");
    public static final Block BEECH_TRAPDOOR = registerTrapdoor("beech_trapdoor");
    public static final Block BIRCH_TRAPDOOR = registerTrapdoor("birch_trapdoor");
    public static final Block CEDAR_TRAPDOOR = registerTrapdoor("cedar_trapdoor");
    public static final Block CHARRED_TRAPDOOR = registerTrapdoor("charred_trapdoor");
    public static final Block CHERRY_TRAPDOOR = registerTrapdoor("cherry_trapdoor");
    public static final Block CHESTNUT_TRAPDOOR = registerTrapdoor("chestnut_trapdoor");
    public static final Block CYPRESS_TRAPDOOR = registerTrapdoor("cypress_trapdoor");
    public static final Block DARK_OAK_TRAPDOOR = registerTrapdoor("dark_oak_trapdoor");
    public static final Block DATE_PALM_TRAPDOOR = registerTrapdoor("date_palm_trapdoor");
    public static final Block DRAGON_TRAPDOOR = registerTrapdoor("dragon_trapdoor");
    public static final Block FIR_TRAPDOOR = registerTrapdoor("fir_trapdoor");
    public static final Block GREEN_OAK_TRAPDOOR = registerTrapdoor("green_oak_trapdoor");
    public static final Block HOLLY_TRAPDOOR = registerTrapdoor("holly_trapdoor");
    public static final Block JUNGLE_TRAPDOOR = registerTrapdoor("jungle_trapdoor");
    public static final Block KANUKA_TRAPDOOR = registerTrapdoor("kanuka_trapdoor");
    public static final Block LAIRELOSSE_TRAPDOOR = registerTrapdoor("lairelosse_trapdoor");
    public static final Block LARCH_TRAPDOOR = registerTrapdoor("larch_trapdoor");
    public static final Block LEBETHRON_TRAPDOOR = registerTrapdoor("lebethron_trapdoor");
    public static final Block LEMON_TRAPDOOR = registerTrapdoor("lemon_trapdoor");
    public static final Block LIME_TRAPDOOR = registerTrapdoor("lime_trapdoor");
    public static final Block MAHOGANY_TRAPDOOR = registerTrapdoor("mahogany_trapdoor");
    public static final Block MALLORN_TRAPDOOR = registerTrapdoor("mallorn_trapdoor");
    public static final Block MANGO_TRAPDOOR = registerTrapdoor("mango_trapdoor");
    public static final Block MANGROVE_TRAPDOOR = registerTrapdoor("mangrove_trapdoor");
    public static final Block MAPLE_TRAPDOOR = registerTrapdoor("maple_trapdoor");
    public static final Block MIRK_OAK_TRAPDOOR = registerTrapdoor("mirk_oak_trapdoor");
    public static final Block OLIVE_TRAPDOOR = registerTrapdoor("olive_trapdoor");
    public static final Block ORANGE_TRAPDOOR = registerTrapdoor("orange_trapdoor");
    public static final Block PALM_TRAPDOOR = registerTrapdoor("palm_trapdoor");
    public static final Block PEAR_TRAPDOOR = registerTrapdoor("pear_trapdoor");
    public static final Block PINE_TRAPDOOR = registerTrapdoor("pine_trapdoor");
    public static final Block PLUM_TRAPDOOR = registerTrapdoor("plum_trapdoor");
    public static final Block POMEGRANATE_TRAPDOOR = registerTrapdoor("pomegranate_trapdoor");
    public static final Block REDWOOD_TRAPDOOR = registerTrapdoor("redwood_trapdoor");
    public static final Block ROTTEN_TRAPDOOR = registerTrapdoor("rotten_trapdoor");
    public static final Block SHIRE_PINE_TRAPDOOR = registerTrapdoor("shire_pine_trapdoor");
    public static final Block SPRUCE_TRAPDOOR = registerTrapdoor("spruce_trapdoor");
    public static final Block WILLOW_TRAPDOOR = registerTrapdoor("willow_trapdoor");

    // --- Doors ---
    public static final Block ACACIA_DOOR = registerDoor("acacia_door");
    public static final Block ALMOND_DOOR = registerDoor("almond_door");
    public static final Block APPLE_DOOR = registerDoor("apple_door");
    public static final Block ASPEN_DOOR = registerDoor("aspen_door");
    public static final Block BANANA_DOOR = registerDoor("banana_door");
    public static final Block BAOBAB_DOOR = registerDoor("baobab_door");
    public static final Block BEECH_DOOR = registerDoor("beech_door");
    public static final Block BIRCH_DOOR = registerDoor("birch_door");
    public static final Block CEDAR_DOOR = registerDoor("cedar_door");
    public static final Block CHARRED_DOOR = registerDoor("charred_door");
    public static final Block CHERRY_DOOR = registerDoor("cherry_door");
    public static final Block CHESTNUT_DOOR = registerDoor("chestnut_door");
    public static final Block CYPRESS_DOOR = registerDoor("cypress_door");
    public static final Block DARK_OAK_DOOR = registerDoor("dark_oak_door");
    public static final Block DATE_PALM_DOOR = registerDoor("date_palm_door");
    public static final Block DRAGON_DOOR = registerDoor("dragon_door");
    public static final Block FIR_DOOR = registerDoor("fir_door");
    public static final Block GREEN_OAK_DOOR = registerDoor("green_oak_door");
    public static final Block HOLLY_DOOR = registerDoor("holly_door");
    public static final Block JUNGLE_DOOR = registerDoor("jungle_door");
    public static final Block KANUKA_DOOR = registerDoor("kanuka_door");
    public static final Block LAIRELOSSE_DOOR = registerDoor("lairelosse_door");
    public static final Block LARCH_DOOR = registerDoor("larch_door");
    public static final Block LEBETHRON_DOOR = registerDoor("lebethron_door");
    public static final Block LEMON_DOOR = registerDoor("lemon_door");
    public static final Block LIME_DOOR = registerDoor("lime_door");
    public static final Block MAHOGANY_DOOR = registerDoor("mahogany_door");
    public static final Block MALLORN_DOOR = registerDoor("mallorn_door");
    public static final Block MANGO_DOOR = registerDoor("mango_door");
    public static final Block MANGROVE_DOOR = registerDoor("mangrove_door");
    public static final Block MAPLE_DOOR = registerDoor("maple_door");
    public static final Block MIRK_OAK_DOOR = registerDoor("mirk_oak_door");
    public static final Block OLIVE_DOOR = registerDoor("olive_door");
    public static final Block ORANGE_DOOR = registerDoor("orange_door");
    public static final Block PALM_DOOR = registerDoor("palm_door");
    public static final Block PEAR_DOOR = registerDoor("pear_door");
    public static final Block PINE_DOOR = registerDoor("pine_door");
    public static final Block PLUM_DOOR = registerDoor("plum_door");
    public static final Block POMEGRANATE_DOOR = registerDoor("pomegranate_door");
    public static final Block REDWOOD_DOOR = registerDoor("redwood_door");
    public static final Block ROTTEN_DOOR = registerDoor("rotten_door");
    public static final Block SHIRE_PINE_DOOR = registerDoor("shire_pine_door");
    public static final Block SPRUCE_DOOR = registerDoor("spruce_door");
    public static final Block WILLOW_DOOR = registerDoor("willow_door");

    // --- Bars (pane model) ---
    public static final Block BLUE_DWARF_BARS = registerBars("blue_dwarf_bars");
    public static final Block BRONZE_BARS = registerBars("bronze_bars");
    public static final Block DWARF_BARS = registerBars("dwarf_bars");
    public static final Block GALADHRIM_BARS = registerBars("galadhrim_bars");
    public static final Block GALADHRIM_WOOD_BARS = registerBars("galadhrim_wood_bars");
    public static final Block GOLD_BARS = registerBars("gold_bars");
    public static final Block HIGH_ELF_BARS = registerBars("high_elf_bars");
    public static final Block HIGH_ELF_WOOD_BARS = registerBars("high_elf_wood_bars");
    public static final Block MITHRIL_BARS = registerBars("mithril_bars");
    public static final Block ORC_STEEL_BARS = registerBars("orc_steel_bars");
    public static final Block REED_BARS = registerBars("reed_bars");
    public static final Block SILVER_BARS = registerBars("silver_bars");
    public static final Block URUK_BARS = registerBars("uruk_bars");
    public static final Block WOOD_ELF_BARS = registerBars("wood_elf_bars");
    public static final Block WOOD_ELF_WOOD_BARS = registerBars("wood_elf_wood_bars");

    // --- Chandeliers (lantern-style hanging light) ---
    public static final Block BLUE_DWARVEN_CHANDELIER = registerChandelier("blue_dwarven_chandelier", LOTRChandelierBlock.ParticleStyle.FLAME);
    public static final Block BRONZE_CHANDELIER = registerChandelier("bronze_chandelier", LOTRChandelierBlock.ParticleStyle.FLAME);
    public static final Block DWARVEN_CHANDELIER = registerChandelier("dwarven_chandelier", LOTRChandelierBlock.ParticleStyle.FLAME);
    public static final Block GOLD_CHANDELIER = registerChandelier("gold_chandelier", LOTRChandelierBlock.ParticleStyle.FLAME);
    public static final Block HIGH_ELVEN_CHANDELIER = registerChandelier("high_elven_chandelier", LOTRChandelierBlock.ParticleStyle.HIGH_ELVEN);
    public static final Block IRON_CHANDELIER = registerChandelier("iron_chandelier", LOTRChandelierBlock.ParticleStyle.FLAME);
    public static final Block MALLORN_BLUE_CHANDELIER = registerChandelier("mallorn_blue_chandelier", LOTRChandelierBlock.ParticleStyle.MALLORN_BLUE);
    public static final Block MALLORN_GOLD_CHANDELIER = registerChandelier("mallorn_gold_chandelier", LOTRChandelierBlock.ParticleStyle.MALLORN_GOLD);
    public static final Block MALLORN_GREEN_CHANDELIER = registerChandelier("mallorn_green_chandelier", LOTRChandelierBlock.ParticleStyle.MALLORN_GREEN);
    public static final Block MALLORN_SILVER_CHANDELIER = registerChandelier("mallorn_silver_chandelier", LOTRChandelierBlock.ParticleStyle.MALLORN_SILVER);
    public static final Block MITHRIL_CHANDELIER = registerChandelier("mithril_chandelier", LOTRChandelierBlock.ParticleStyle.FLAME);
    public static final Block MORGUL_CHANDELIER = registerChandelier("morgul_chandelier", LOTRChandelierBlock.ParticleStyle.MORGUL);
    public static final Block ORC_CHANDELIER = registerChandelier("orc_chandelier", LOTRChandelierBlock.ParticleStyle.FLAME);
    public static final Block SILVER_CHANDELIER = registerChandelier("silver_chandelier", LOTRChandelierBlock.ParticleStyle.FLAME);
    public static final Block URUK_CHANDELIER = registerChandelier("uruk_chandelier", LOTRChandelierBlock.ParticleStyle.FLAME);
    public static final Block WOOD_ELVEN_CHANDELIER = registerChandelier("wood_elven_chandelier", LOTRChandelierBlock.ParticleStyle.WOOD_ELVEN);

    // --- Glass ---
    public static final Block BLACK_STAINED_GLASS = registerGlass("black_stained_glass");
    public static final Block BLUE_STAINED_GLASS = registerGlass("blue_stained_glass");
    public static final Block BROWN_STAINED_GLASS = registerGlass("brown_stained_glass");
    public static final Block CYAN_STAINED_GLASS = registerGlass("cyan_stained_glass");
    public static final Block GRAY_STAINED_GLASS = registerGlass("gray_stained_glass");
    public static final Block GREEN_STAINED_GLASS = registerGlass("green_stained_glass");
    public static final Block LIGHT_BLUE_STAINED_GLASS = registerGlass("light_blue_stained_glass");
    public static final Block LIME_STAINED_GLASS = registerGlass("lime_stained_glass");
    public static final Block MAGENTA_STAINED_GLASS = registerGlass("magenta_stained_glass");
    public static final Block ORANGE_STAINED_GLASS = registerGlass("orange_stained_glass");
    public static final Block PINK_STAINED_GLASS = registerGlass("pink_stained_glass");
    public static final Block PURPLE_STAINED_GLASS = registerGlass("purple_stained_glass");
    public static final Block RED_STAINED_GLASS = registerGlass("red_stained_glass");
    public static final Block SILVER_STAINED_GLASS = registerGlass("silver_stained_glass");
    public static final Block WHITE_STAINED_GLASS = registerGlass("white_stained_glass");
    public static final Block YELLOW_STAINED_GLASS = registerGlass("yellow_stained_glass");
    public static final Block GLASS = registerGlass("glass");

    private LOTRBlocks() {
    }

    // ------------------------------------------------------------------
    // Family helpers
    // ------------------------------------------------------------------

    private static Block registerCube(String name, float hardness, float resistance, Tier tier) {
        Block block = register(name, Block::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.STONE)
                        .requiresCorrectToolForDrops()
                        .strength(hardness, resistance)
                        .sound(SoundType.STONE),
                true);
        ALL_CUBES.add(block);
        (tier == Tier.IRON ? CUBES_IRON_TIER : CUBES_STONE_TIER).add(block);
        return block;
    }

    private static Block registerPlanks(String name) {
        return track(ALL_PLANKS, register(name, Block::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.WOOD)
                        .instrument(NoteBlockInstrument.BASS)
                        .strength(2.0f, 3.0f)
                        .sound(SoundType.WOOD)
                        .ignitedByLava(),
                true));
    }

    private static Block registerLeaves(String name) {
        return track(ALL_LEAVES, register(name, Block::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.PLANT)
                        .strength(0.2f)
                        .randomTicks()
                        .sound(SoundType.GRASS)
                        .noOcclusion()
                        .isViewBlocking((state, level, pos) -> false)
                        .ignitedByLava()
                        .pushReaction(PushReaction.DESTROY),
                true));
    }

    private static Block registerSapling(String name) {
        return track(ALL_SAPLINGS, register(name, Block::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.PLANT)
                        .noCollision()
                        .instabreak()
                        .sound(SoundType.GRASS)
                        .noOcclusion()
                        .pushReaction(PushReaction.DESTROY),
                true));
    }

    private static Block registerTrapdoor(String name) {
        return track(ALL_TRAPDOORS, register(name, props -> new TrapDoorBlock(BlockSetType.OAK, props),
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.WOOD)
                        .instrument(NoteBlockInstrument.BASS)
                        .strength(3.0f)
                        .sound(SoundType.WOOD)
                        .noOcclusion()
                        .isValidSpawn((state, level, pos, type) -> false)
                        .ignitedByLava(),
                true));
    }

    private static Block registerDoor(String name) {
        return track(ALL_DOORS, register(name, props -> new DoorBlock(BlockSetType.OAK, props),
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.WOOD)
                        .instrument(NoteBlockInstrument.BASS)
                        .strength(3.0f)
                        .sound(SoundType.WOOD)
                        .noOcclusion()
                        .pushReaction(PushReaction.DESTROY)
                        .ignitedByLava(),
                true));
    }

    /** Metal/wood bars: pane model, connects to neighbours. */
    private static Block registerBars(String name) {
        return track(ALL_BARS, register(name, IronBarsBlock::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.NONE)
                        .requiresCorrectToolForDrops()
                        .strength(5.0f, 6.0f)
                        .sound(SoundType.METAL)
                        .noOcclusion(),
                true));
    }

    /**
     * Chandelier: a crossed-quad light fixture that hangs from the ceiling.
     *
     * Ported from LOTRBlockChandelier (1.7.10): render type 1 (crossed
     * squares), bounds 1/16..15/16 on X/Z and 3/16..16/16 on Y, null collision
     * box, light level 0.9375 -> 15, hardness 0 / resistance 2, metal step
     * sound, and it pops off if whatever it is hanging from goes away.
     */
    private static Block registerChandelier(String name, LOTRChandelierBlock.ParticleStyle style) {
        return track(ALL_CHANDELIERS, register(name, props -> new LOTRChandelierBlock(style, props),
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.NONE)
                        .noCollision()
                        .strength(0.0f, 2.0f)
                        .sound(SoundType.METAL)
                        .lightLevel(state -> 15)
                        .noOcclusion()
                        .pushReaction(PushReaction.DESTROY),
                true));
    }

    /** Glass / stained glass: transparent full cube. */
    private static Block registerGlass(String name) {
        return track(ALL_GLASS, register(name, TransparentBlock::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.NONE)
                        .instrument(NoteBlockInstrument.HAT)
                        .strength(0.3f)
                        .sound(SoundType.GLASS)
                        .noOcclusion()
                        .isValidSpawn((state, level, pos, type) -> false)
                        .isRedstoneConductor((state, level, pos) -> false)
                        .isSuffocating((state, level, pos) -> false)
                        .isViewBlocking((state, level, pos) -> false),
                true));
    }

    private static Block track(List<Block> family, Block block) {
        family.add(block);
        return block;
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