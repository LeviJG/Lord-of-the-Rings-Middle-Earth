package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.CarpetBlock;
import net.minecraft.world.level.block.CraftingTableBlock;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.WallBlock;
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
    /**
     * Every block this class registers, in registration order.
     *
     * The creative tab iterates this rather than a hand-written list of
     * families, so a new family cannot silently go missing from the menu.
     */
    public static final List<Block> ALL_BLOCKS = new ArrayList<>();

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
    public static final List<Block> ALL_FLOWERS = new ArrayList<>();
    public static final List<Block> ALL_COLUMNS = new ArrayList<>();
    public static final List<Block> ALL_CARPETS = new ArrayList<>();
    public static final List<Block> ALL_TORCHES = new ArrayList<>();
    public static final List<Block> ALL_CRAFTING_TABLES = new ArrayList<>();
    public static final List<Block> ALL_VINES = new ArrayList<>();
    public static final List<Block> ALL_LADDERS = new ArrayList<>();
    public static final List<Block> ALL_GATES = new ArrayList<>();
    public static final List<Block> ALL_BUSHES = new ArrayList<>();
    public static final List<Block> ALL_FENCE_GATES = new ArrayList<>();
    /** fence gate -> the block it copies its texture and properties from. */
    public static final Map<Block, Block> FENCE_GATE_BASE = new LinkedHashMap<>();
    public static final List<Block> ALL_BUTTONS = new ArrayList<>();
    public static final List<Block> ALL_PRESSURE_PLATES = new ArrayList<>();
    /** standing torch -> its wall variant. */
    public static final Map<Block, Block> TORCH_WALL = new LinkedHashMap<>();
    public static final List<Block> ALL_LOGS = new ArrayList<>();
    public static final List<Block> ALL_BEAMS = new ArrayList<>();
    public static final List<Block> ALL_PILLARS = new ArrayList<>();
    public static final List<Block> ALL_STAIRS = new ArrayList<>();
    public static final List<Block> ALL_SLABS = new ArrayList<>();
    /** slab block -> the block it copies its texture and properties from. */
    public static final Map<Block, Block> SLAB_BASE = new LinkedHashMap<>();
    public static final List<Block> ALL_FENCES = new ArrayList<>();
    /** fence block -> the block it copies its texture and properties from. */
    public static final Map<Block, Block> FENCE_BASE = new LinkedHashMap<>();
    public static final List<Block> ALL_WALLS = new ArrayList<>();
    /** wall block -> the block it copies its texture and properties from. */
    public static final Map<Block, Block> WALL_BASE = new LinkedHashMap<>();
    /** stairs block -> the block it copies its texture and properties from. */
    public static final Map<Block, Block> STAIRS_BASE = new LinkedHashMap<>();

    /**
     * Registry key for every block registered here.
     *
     * 26.2 moved data generation over to ids rather than raw Block instances
     * (BlockIds/ItemIds vanilla-side, and valueLookupBuilder was removed from
     * the Fabric tag provider as a result). register() already builds the key
     * it needs, so recording it here means datagen can ask for it later without
     * a registry round-trip.
     */
    private static final Map<Block, ResourceKey<Block>> BLOCK_KEYS = new LinkedHashMap<>();

    /** The registry key a block was registered under. */
    public static ResourceKey<Block> keyOf(Block block) {
        return BLOCK_KEYS.get(block);
    }

    // --- Ores ---
    public static final Block TIN_ORE = registerCube("tin_ore", 3.0f, 5.0f, Tier.STONE);
    public static final Block SILVER_ORE = registerCube("silver_ore", 3.0f, 5.0f, Tier.IRON);
    public static final Block MITHRIL_ORE = registerCube("mithril_ore", 4.0f, 10.0f, Tier.IRON);
    public static final Block SALT_ORE = registerCube("salt_ore", 3.0f, 5.0f, Tier.STONE);
    public static final Block SALTPETER_ORE = registerCube("saltpeter_ore", 3.0f, 5.0f, Tier.STONE);
    public static final Block SULFUR_ORE = registerCube("sulfur_ore", 3.0f, 5.0f, Tier.STONE);

    // --- Stone-like cube blocks ---
    public static final Block AMBER_BLOCK = registerCube("amber_block", 5.0f, 10.0f, Tier.IRON);
    public static final Block AMBER_ORE = registerCube("amber_ore", 3.0f, 5.0f, Tier.IRON);
    public static final Block ANGMAR_BRICK = registerCube("angmar_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block ANGMAR_CRACKED_BRICK = registerCube("angmar_cracked_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block ANGMAR_SNOW_BRICK = registerCube("angmar_snow_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block ARNOR_BRICK = registerCube("arnor_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block ARNOR_CARVED_BRICK = registerCube("arnor_carved_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block ARNOR_CRACKED_BRICK = registerCube("arnor_cracked_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block ARNOR_MOSSY_BRICK = registerCube("arnor_mossy_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block BLACK_GONDOR_BRICK = registerCube("black_gondor_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block BLACK_GONDOR_CARVED_BRICK = registerCube("black_gondor_carved_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block BLACK_UMBAR_CARVED_BRICK = registerCube("black_umbar_carved_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block BLACK_URUK_STEEL_BLOCK = registerCube("black_uruk_steel_block", 5.0f, 10.0f, Tier.STONE);
    public static final Block BLUE_CARVED_BRICK = registerCube("blue_carved_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block BLUE_DWARF_STEEL_BLOCK = registerCube("blue_dwarf_steel_block", 5.0f, 10.0f, Tier.STONE);
    public static final Block BLUE_ROCK = registerCube("blue_rock", 1.5f, 10.0f, Tier.STONE);
    public static final Block BLUE_ROCK_BRICK = registerCube("blue_rock_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block BRONZE_BLOCK = registerCube("bronze_block", 5.0f, 10.0f, Tier.STONE);
    public static final Block CHALK = registerCube("chalk", 1.5f, 10.0f, Tier.STONE);
    public static final Block CHALK_BRICK = registerCube("chalk_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block CLAY_TILE = registerCube("clay_tile", 1.5f, 10.0f, Tier.STONE);
    public static final Block CLAY_TILE_DYED_BLACK = registerCube("clay_tile_dyed_black", 1.5f, 10.0f, Tier.STONE);
    public static final Block CLAY_TILE_DYED_BLUE = registerCube("clay_tile_dyed_blue", 1.5f, 10.0f, Tier.STONE);
    public static final Block CLAY_TILE_DYED_BROWN = registerCube("clay_tile_dyed_brown", 1.5f, 10.0f, Tier.STONE);
    public static final Block CLAY_TILE_DYED_CYAN = registerCube("clay_tile_dyed_cyan", 1.5f, 10.0f, Tier.STONE);
    public static final Block CLAY_TILE_DYED_GRAY = registerCube("clay_tile_dyed_gray", 1.5f, 10.0f, Tier.STONE);
    public static final Block CLAY_TILE_DYED_GREEN = registerCube("clay_tile_dyed_green", 1.5f, 10.0f, Tier.STONE);
    public static final Block CLAY_TILE_DYED_LIGHT_BLUE = registerCube("clay_tile_dyed_light_blue", 1.5f, 10.0f, Tier.STONE);
    public static final Block CLAY_TILE_DYED_LIME = registerCube("clay_tile_dyed_lime", 1.5f, 10.0f, Tier.STONE);
    public static final Block CLAY_TILE_DYED_MAGENTA = registerCube("clay_tile_dyed_magenta", 1.5f, 10.0f, Tier.STONE);
    public static final Block CLAY_TILE_DYED_ORANGE = registerCube("clay_tile_dyed_orange", 1.5f, 10.0f, Tier.STONE);
    public static final Block CLAY_TILE_DYED_PINK = registerCube("clay_tile_dyed_pink", 1.5f, 10.0f, Tier.STONE);
    public static final Block CLAY_TILE_DYED_PURPLE = registerCube("clay_tile_dyed_purple", 1.5f, 10.0f, Tier.STONE);
    public static final Block CLAY_TILE_DYED_RED = registerCube("clay_tile_dyed_red", 1.5f, 10.0f, Tier.STONE);
    public static final Block CLAY_TILE_DYED_SILVER = registerCube("clay_tile_dyed_silver", 1.5f, 10.0f, Tier.STONE);
    public static final Block CLAY_TILE_DYED_WHITE = registerCube("clay_tile_dyed_white", 1.5f, 10.0f, Tier.STONE);
    public static final Block CLAY_TILE_DYED_YELLOW = registerCube("clay_tile_dyed_yellow", 1.5f, 10.0f, Tier.STONE);
    public static final Block CORAL_BLOCK = registerSoftBlock("coral_block", 5.0f, SoundType.STONE);
    public static final Block DALE_BRICK = registerCube("dale_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block DALE_CARVED_BRICK = registerCube("dale_carved_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block DALE_CRACKED_BRICK = registerCube("dale_cracked_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block DALE_MOSSY_BRICK = registerCube("dale_mossy_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block DOL_AMROTH_BRICK = registerCube("dol_amroth_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block DOL_GULDUR_BRICK = registerCube("dol_guldur_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block DOL_GULDUR_CARVED_BRICK = registerCube("dol_guldur_carved_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block DOL_GULDUR_CRACKED_BRICK = registerCube("dol_guldur_cracked_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block DOL_GULDUR_MOSSY_BRICK = registerCube("dol_guldur_mossy_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block DORWINION_BRICK = registerCube("dorwinion_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block DORWINION_CARVED_BRICK = registerCube("dorwinion_carved_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block DORWINION_CRACKED_BRICK = registerCube("dorwinion_cracked_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block DORWINION_FLOWERS_BRICK = registerCube("dorwinion_flowers_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block DORWINION_MOSSY_BRICK = registerCube("dorwinion_mossy_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block DWARF_STEEL_BLOCK = registerCube("dwarf_steel_block", 5.0f, 10.0f, Tier.STONE);
    public static final Block DWARVEN_BRICK = registerCube("dwarven_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block DWARVEN_CARVED_BRICK = registerCube("dwarven_carved_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block DWARVEN_CRACKED_BRICK = registerCube("dwarven_cracked_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block DWARVEN_GLOWING_BRICK = registerCube("dwarven_glowing_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block DWARVEN_OBSIDIAN_BRICK = registerCube("dwarven_obsidian_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block ELF_STEEL_BLOCK = registerCube("elf_steel_block", 5.0f, 10.0f, Tier.IRON);
    public static final Block GALADHRIM_BRICK = registerCube("galadhrim_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block GALADHRIM_CARVED_BRICK = registerCube("galadhrim_carved_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block GALADHRIM_CRACKED_BRICK = registerCube("galadhrim_cracked_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block GALADHRIM_GOLD_BRICK = registerCube("galadhrim_gold_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block GALADHRIM_MOSSY_BRICK = registerCube("galadhrim_mossy_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block GALADHRIM_SILVER_BRICK = registerCube("galadhrim_silver_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block GALVORN_BLOCK = registerCube("galvorn_block", 5.0f, 10.0f, Tier.IRON);
    public static final Block GILDED_IRON_BLOCK = registerCube("gilded_iron_block", 5.0f, 10.0f, Tier.STONE);
    public static final Block GLOWSTONE_ORE = registerCube("glowstone_ore", 3.0f, 5.0f, Tier.STONE);
    public static final Block GONDOR_BRICK = registerCube("gondor_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block GONDOR_CARVED_BRICK = registerCube("gondor_carved_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block GONDOR_CRACKED_BRICK = registerCube("gondor_cracked_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block GONDOR_MOSSY_BRICK = registerCube("gondor_mossy_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block GONDOR_ROCK = registerCube("gondor_rock", 1.5f, 10.0f, Tier.STONE);
    public static final Block GONDOR_RUSTIC_BRICK = registerCube("gondor_rustic_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block GONDOR_RUSTIC_CRACKED_BRICK = registerCube("gondor_rustic_cracked_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block GONDOR_RUSTIC_MOSSY_BRICK = registerCube("gondor_rustic_mossy_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block GULDURIL_BLOCK = registerCube("gulduril_block", 5.0f, 10.0f, Tier.IRON);
    public static final Block GULDURIL_MORDOR_ORE = registerCube("gulduril_mordor_ore", 3.0f, 5.0f, Tier.IRON);
    public static final Block GULDURIL_ORE = registerCube("gulduril_ore", 3.0f, 5.0f, Tier.IRON);
    public static final Block HIGH_ELVEN_BRICK = registerCube("high_elven_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block HIGH_ELVEN_CARVED_BRICK = registerCube("high_elven_carved_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block HIGH_ELVEN_CRACKED_BRICK = registerCube("high_elven_cracked_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block HIGH_ELVEN_GOLD_BRICK = registerCube("high_elven_gold_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block HIGH_ELVEN_MOSSY_BRICK = registerCube("high_elven_mossy_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block HIGH_ELVEN_SILVER_BRICK = registerCube("high_elven_silver_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block MITHRIL_BLOCK = registerCube("mithril_block", 5.0f, 10.0f, Tier.IRON);
    public static final Block MORDOR_BRICK = registerCube("mordor_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block MORDOR_CARVED_BRICK = registerCube("mordor_carved_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block MORDOR_CRACKED_BRICK = registerCube("mordor_cracked_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block MORDOR_DIRT = registerCube("mordor_dirt", 0.5f, 0.5f, Tier.STONE);
    public static final Block MORDOR_GRAVEL = registerCube("mordor_gravel", 0.6f, 0.6f, Tier.STONE);
    public static final Block MORDOR_MOSS_ROCK = registerCube("mordor_moss_rock", 1.5f, 10.0f, Tier.STONE);
    public static final Block MORDOR_ROCK = registerCube("mordor_rock", 1.5f, 10.0f, Tier.STONE);
    public static final Block MOREDAIN_BRICK = registerCube("moredain_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block MORGUL_IRON_MORDOR_ORE = registerCube("morgul_iron_mordor_ore", 3.0f, 5.0f, Tier.STONE);
    public static final Block MORGUL_IRON_ORE = registerCube("morgul_iron_ore", 3.0f, 5.0f, Tier.STONE);
    public static final Block MORGUL_STEEL_BLOCK = registerCube("morgul_steel_block", 5.0f, 10.0f, Tier.IRON);
    public static final Block MORWAITH_CRACKED_BRICK = registerCube("morwaith_cracked_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block NAURITE_BLOCK = registerCube("naurite_block", 5.0f, 10.0f, Tier.IRON);
    public static final Block NAURITE_ORE = registerCube("naurite_ore", 3.0f, 5.0f, Tier.IRON);
    public static final Block NEAR_HARAD_BRICK = registerCube("near_harad_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block NEAR_HARAD_CARVED_BRICK = registerCube("near_harad_carved_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block NEAR_HARAD_CRACKED_BRICK = registerCube("near_harad_cracked_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block NEAR_HARAD_LAPIS_BRICK = registerCube("near_harad_lapis_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block NEAR_HARAD_RED_BRICK = registerCube("near_harad_red_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block NEAR_HARAD_RED_CARVED_BRICK = registerCube("near_harad_red_carved_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block NEAR_HARAD_RED_CRACKED_BRICK = registerCube("near_harad_red_cracked_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block OBSIDIAN_GRAVEL = registerCube("obsidian_gravel", 0.6f, 0.6f, Tier.STONE);
    public static final Block OPAL_BLOCK = registerCube("opal_block", 5.0f, 10.0f, Tier.IRON);
    public static final Block OPAL_ORE = registerCube("opal_ore", 3.0f, 5.0f, Tier.IRON);
    public static final Block ORC_PLATING_IRON = registerCube("orc_plating_iron", 1.5f, 10.0f, Tier.STONE);
    public static final Block ORC_PLATING_RUST = registerCube("orc_plating_rust", 1.5f, 10.0f, Tier.STONE);
    public static final Block ORC_STEEL_BLOCK = registerCube("orc_steel_block", 5.0f, 10.0f, Tier.STONE);
    public static final Block PEARL_BLOCK = registerCube("pearl_block", 5.0f, 10.0f, Tier.IRON);
    public static final Block QUAGMIRE = registerCube("quagmire", 0.5f, 0.5f, Tier.STONE);
    public static final Block QUENDITE_BLOCK = registerCube("quendite_block", 5.0f, 10.0f, Tier.IRON);
    public static final Block QUENDITE_ORE = registerCube("quendite_ore", 3.0f, 5.0f, Tier.IRON);
    public static final Block RED_BRICK_CRACKED = registerCube("red_brick_cracked", 1.5f, 10.0f, Tier.STONE);
    public static final Block RED_BRICK_MOSSY = registerCube("red_brick_mossy", 1.5f, 10.0f, Tier.STONE);
    public static final Block RED_CARVED_BRICK = registerCube("red_carved_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block RED_CLAY = registerCube("red_clay", 0.5f, 0.5f, Tier.STONE);
    public static final Block RED_ROCK = registerCube("red_rock", 1.5f, 10.0f, Tier.STONE);
    public static final Block RED_ROCK_BRICK = registerCube("red_rock_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block RHUN_BRICK = registerCube("rhun_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block RHUN_CARVED_BRICK = registerCube("rhun_carved_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block RHUN_CRACKED_BRICK = registerCube("rhun_cracked_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block RHUN_FLOWERS_BRICK = registerCube("rhun_flowers_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block RHUN_GOLD_BRICK = registerCube("rhun_gold_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block RHUN_MOSSY_BRICK = registerCube("rhun_mossy_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block RHUN_RED_BRICK = registerCube("rhun_red_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block RHUN_RED_CARVED_BRICK = registerCube("rhun_red_carved_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block ROHAN_BRICK = registerCube("rohan_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block ROHAN_CARVED_BRICK = registerCube("rohan_carved_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block ROHAN_ROCK = registerCube("rohan_rock", 1.5f, 10.0f, Tier.STONE);
    public static final Block RUBY_BLOCK = registerCube("ruby_block", 5.0f, 10.0f, Tier.IRON);
    public static final Block RUBY_ORE = registerCube("ruby_ore", 3.0f, 5.0f, Tier.IRON);
    public static final Block SALT_BLOCK = registerCube("salt_block", 5.0f, 10.0f, Tier.STONE);
    public static final Block SALTPETER_BLOCK = registerCube("saltpeter_block", 5.0f, 10.0f, Tier.STONE);
    public static final Block SAPPHIRE_BLOCK = registerCube("sapphire_block", 5.0f, 10.0f, Tier.IRON);
    public static final Block SAPPHIRE_ORE = registerCube("sapphire_ore", 3.0f, 5.0f, Tier.IRON);
    public static final Block SCORCHED_STONE = registerCube("scorched_stone", 1.5f, 10.0f, Tier.STONE);
    public static final Block SILVER_BLOCK = registerCube("silver_block", 5.0f, 10.0f, Tier.IRON);
    public static final Block TAUREDAIN_BRICK = registerCube("tauredain_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block TAUREDAIN_CRACKED_BRICK = registerCube("tauredain_cracked_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block TAUREDAIN_GOLD_BRICK = registerCube("tauredain_gold_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block TAUREDAIN_MOSSY_BRICK = registerCube("tauredain_mossy_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block TAUREDAIN_OBSIDIAN_BRICK = registerCube("tauredain_obsidian_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block TIN_BLOCK = registerCube("tin_block", 5.0f, 10.0f, Tier.STONE);
    public static final Block UMBAR_BRICK = registerCube("umbar_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block UMBAR_CARVED_BRICK = registerCube("umbar_carved_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block UMBAR_CRACKED_BRICK = registerCube("umbar_cracked_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block URUK_BRICK = registerCube("uruk_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block URUK_STEEL_BLOCK = registerCube("uruk_steel_block", 5.0f, 10.0f, Tier.STONE);
    public static final Block UTUMNO_FIRE_BRICK = registerCube("utumno_fire_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block UTUMNO_FIRE_TILE_BRICK = registerCube("utumno_fire_tile_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block UTUMNO_ICE_BRICK = registerCube("utumno_ice_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block UTUMNO_ICE_GLOWING_BRICK = registerCube("utumno_ice_glowing_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block UTUMNO_ICE_TILE_BRICK = registerCube("utumno_ice_tile_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block UTUMNO_OBSIDIAN_BRICK = registerCube("utumno_obsidian_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block UTUMNO_OBSIDIAN_FIRE_BRICK = registerCube("utumno_obsidian_fire_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block UTUMNO_OBSIDIAN_TILE_BRICK = registerCube("utumno_obsidian_tile_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block WHITE_SAND = registerCube("white_sand", 0.6f, 0.6f, Tier.STONE);
    public static final Block WHITE_SANDSTONE = registerCube("white_sandstone", 1.5f, 10.0f, Tier.STONE);
    public static final Block WOOD_ELVEN_BRICK = registerCube("wood_elven_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block WOOD_ELVEN_CARVED_BRICK = registerCube("wood_elven_carved_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block WOOD_ELVEN_CRACKED_BRICK = registerCube("wood_elven_cracked_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block WOOD_ELVEN_GOLD_BRICK = registerCube("wood_elven_gold_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block WOOD_ELVEN_MOSSY_BRICK = registerCube("wood_elven_mossy_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block WOOD_ELVEN_SILVER_BRICK = registerCube("wood_elven_silver_brick", 1.5f, 10.0f, Tier.STONE);


    // --- Soft blocks (no tool required; original set no harvest level) ---
    //
    // Deferred, needs a bespoke model rather than cube_all: coral reef (six
    // colours) and thatch flooring both used custom render IDs in 1.7.10, and
    // thatch flooring plus withered moss are 1-pixel carpets, not full cubes.
    public static final Block KEBAB_BLOCK = registerSoftBlock("kebab_block", 0.5f, SoundType.WOOD);
    public static final Block REMAINS = registerSoftBlock("remains", 3.0f, SoundType.GRAVEL);
    public static final Block THATCH_REED = registerSoftBlock("thatch_reed", 0.5f, SoundType.GRASS);
    public static final Block THATCH_THATCH = registerSoftBlock("thatch_thatch", 0.5f, SoundType.GRASS);

    // --- Additional stone-like cubes ---
    public static final Block RED_SANDSTONE = registerCube("red_sandstone", 1.5f, 10.0f, Tier.STONE);
    public static final Block DIAMOND_BLOCK = registerCube("diamond_block", 5.0f, 10.0f, Tier.IRON);
    public static final Block AMETHYST_ORE = registerCube("amethyst_ore", 3.0f, 5.0f, Tier.IRON);
    public static final Block DIAMOND_ORE = registerCube("diamond_ore", 3.0f, 5.0f, Tier.IRON);
    public static final Block MUD = registerSoftBlock("mud", 0.5f, SoundType.GRAVEL);
    public static final Block MUD_BRICK = registerCube("mud_brick", 1.5f, 10.0f, Tier.STONE);
    public static final Block TOPAZ_BLOCK = registerCube("topaz_block", 5.0f, 10.0f, Tier.IRON);
    public static final Block TOPAZ_ORE = registerCube("topaz_ore", 3.0f, 5.0f, Tier.IRON);
    public static final Block UTUMNO_BURNING_BRICK = registerCube("utumno_burning_brick", 1.5f, 10.0f, Tier.STONE);

    // --- Smooth stone (distinct top texture; not axis-rotatable) ---
    public static final Block SMOOTH_MORDOR_ROCK = registerColumn("smooth_mordor_rock");
    public static final Block SMOOTH_GONDOR_ROCK = registerColumn("smooth_gondor_rock");
    public static final Block SMOOTH_ROHAN_ROCK = registerColumn("smooth_rohan_rock");
    public static final Block SMOOTH_BLUE_ROCK = registerColumn("smooth_blue_rock");
    public static final Block SMOOTH_RED_ROCK = registerColumn("smooth_red_rock");
    public static final Block SMOOTH_CHALK = registerColumn("smooth_chalk");
    public static final Block GONDOR_COBBLEBRICK = registerCube("gondor_cobblebrick", 1.5f, 10.0f, Tier.STONE);

    // --- Torches (a standing block plus a wall variant, as in vanilla) ---
    public static final Block HIGH_ELVEN_WALL_TORCH = registerWallTorch("high_elven_wall_torch");
    public static final Block HIGH_ELVEN_TORCH = registerTorch("high_elven_torch", HIGH_ELVEN_WALL_TORCH);
    public static final Block WOOD_ELVEN_WALL_TORCH = registerWallTorch("wood_elven_wall_torch");
    public static final Block WOOD_ELVEN_TORCH = registerTorch("wood_elven_torch", WOOD_ELVEN_WALL_TORCH);
    public static final Block MORGUL_WALL_TORCH = registerWallTorch("morgul_wall_torch");
    public static final Block MORGUL_TORCH = registerTorch("morgul_torch", MORGUL_WALL_TORCH);
    public static final Block MALLORN_WALL_TORCH = registerWallTorch("mallorn_wall_torch");
    public static final Block MALLORN_TORCH = registerTorch("mallorn_torch", MALLORN_WALL_TORCH);
    public static final Block MALLORN_BLUE_WALL_TORCH = registerWallTorch("mallorn_blue_wall_torch");
    public static final Block MALLORN_BLUE_TORCH = registerTorch("mallorn_blue_torch", MALLORN_BLUE_WALL_TORCH);
    public static final Block MALLORN_GOLD_WALL_TORCH = registerWallTorch("mallorn_gold_wall_torch");
    public static final Block MALLORN_GOLD_TORCH = registerTorch("mallorn_gold_torch", MALLORN_GOLD_WALL_TORCH);
    public static final Block MALLORN_GREEN_WALL_TORCH = registerWallTorch("mallorn_green_wall_torch");
    public static final Block MALLORN_GREEN_TORCH = registerTorch("mallorn_green_torch", MALLORN_GREEN_WALL_TORCH);

    // --- Faction crafting tables ---
    public static final Block ANGMAR_CRAFTING_TABLE = registerCraftingTable("angmar_crafting_table");
    public static final Block BLUE_DWARVEN_CRAFTING_TABLE = registerCraftingTable("blue_dwarven_crafting_table");
    public static final Block BREE_CRAFTING_TABLE = registerCraftingTable("bree_crafting_table");
    public static final Block DALE_CRAFTING_TABLE = registerCraftingTable("dale_crafting_table");
    public static final Block DOL_AMROTH_CRAFTING_TABLE = registerCraftingTable("dol_amroth_crafting_table");
    public static final Block DOL_GULDUR_CRAFTING_TABLE = registerCraftingTable("dol_guldur_crafting_table");
    public static final Block DORWINION_CRAFTING_TABLE = registerCraftingTable("dorwinion_crafting_table");
    public static final Block DUNLENDING_CRAFTING_TABLE = registerCraftingTable("dunlending_crafting_table");
    public static final Block DWARVEN_CRAFTING_TABLE = registerCraftingTable("dwarven_crafting_table");
    public static final Block ELVEN_CRAFTING_TABLE = registerCraftingTable("elven_crafting_table");
    public static final Block GONDORIAN_CRAFTING_TABLE = registerCraftingTable("gondorian_crafting_table");
    public static final Block GULF_CRAFTING_TABLE = registerCraftingTable("gulf_crafting_table");
    public static final Block GUNDABAD_CRAFTING_TABLE = registerCraftingTable("gundabad_crafting_table");
    public static final Block HALF_TROLL_CRAFTING_TABLE = registerCraftingTable("half_troll_crafting_table");
    public static final Block HIGH_ELVEN_CRAFTING_TABLE = registerCraftingTable("high_elven_crafting_table");
    public static final Block HOBBIT_CRAFTING_TABLE = registerCraftingTable("hobbit_crafting_table");
    public static final Block MOREDAIN_CRAFTING_TABLE = registerCraftingTable("moredain_crafting_table");
    public static final Block MORGUL_CRAFTING_TABLE = registerCraftingTable("morgul_crafting_table");
    public static final Block NEAR_HARAD_CRAFTING_TABLE = registerCraftingTable("near_harad_crafting_table");
    public static final Block RANGER_CRAFTING_TABLE = registerCraftingTable("ranger_crafting_table");
    public static final Block RHUN_CRAFTING_TABLE = registerCraftingTable("rhun_crafting_table");
    public static final Block RIVENDELL_CRAFTING_TABLE = registerCraftingTable("rivendell_crafting_table");
    public static final Block ROHIRRIC_CRAFTING_TABLE = registerCraftingTable("rohirric_crafting_table");
    public static final Block TAUREDAIN_CRAFTING_TABLE = registerCraftingTable("tauredain_crafting_table");
    public static final Block UMBAR_CRAFTING_TABLE = registerCraftingTable("umbar_crafting_table");
    public static final Block URUK_CRAFTING_TABLE = registerCraftingTable("uruk_crafting_table");
    public static final Block WOOD_ELVEN_CRAFTING_TABLE = registerCraftingTable("wood_elven_crafting_table");

    // --- Tall grass and small plants (cross model) ---
    public static final Block TALL_GRASS_FERNSPROUT = registerFlower("tall_grass_fernsprout");
    public static final Block TALL_GRASS_FLOWER = registerFlower("tall_grass_flower");
    public static final Block TALL_GRASS_NETTLE = registerFlower("tall_grass_nettle");
    public static final Block TALL_GRASS_SHORT = registerFlower("tall_grass_short");
    public static final Block TALL_GRASS_THISTLE = registerFlower("tall_grass_thistle");
    public static final Block TALL_GRASS_WHEAT = registerFlower("tall_grass_wheat");

    // --- Climbable vines ---
    public static final Block IVY = registerVine("ivy");
    public static final Block IVY_RED = registerVine("ivy_red");
    public static final Block MIRK_VINES = registerVine("mirk_vines");
    public static final Block WILLOW_VINES = registerVine("willow_vines");

    // --- Ladders ---
    public static final Block HITHLAIN_LADDER = registerLadder("hithlain_ladder");
    public static final Block MALLORN_LADDER = registerLadder("mallorn_ladder");

    // --- Misc solid blocks ---
    public static final Block TERMITE_MOUND = registerSoftBlock("termite_mound", 0.5f, SoundType.SAND);
    public static final Block MARZIPAN_CHOCOLATE = registerSoftBlock("marzipan_chocolate", 0.5f, SoundType.WOOL);
    public static final Block TREASURE_COPPER = registerSoftBlock("treasure_copper", 3.0f, SoundType.METAL);
    public static final Block TREASURE_GOLD = registerSoftBlock("treasure_gold", 3.0f, SoundType.METAL);
    public static final Block TREASURE_SILVER = registerSoftBlock("treasure_silver", 3.0f, SoundType.METAL);
    public static final Block COPPER_BLOCK = registerSoftBlock("copper_block", 5.0f, SoundType.METAL);

    // --- Faction gates ---
    public static final Block DOL_AMROTH_GATE = registerGate("dol_amroth_gate");
    public static final Block DWARVEN_GATE = registerGate("dwarven_gate");
    public static final Block ELVEN_GATE = registerGate("elven_gate");
    public static final Block GOLD_GATE = registerGate("gold_gate");
    public static final Block GONDOR_GATE = registerGate("gondor_gate");
    public static final Block HIGH_ELVEN_GATE = registerGate("high_elven_gate");
    public static final Block HOBBIT_BLUE_GATE = registerGate("hobbit_blue_gate");
    public static final Block HOBBIT_GREEN_GATE = registerGate("hobbit_green_gate");
    public static final Block HOBBIT_RED_GATE = registerGate("hobbit_red_gate");
    public static final Block HOBBIT_YELLOW_GATE = registerGate("hobbit_yellow_gate");
    public static final Block MITHRIL_GATE = registerGate("mithril_gate");
    public static final Block NEAR_HARAD_GATE = registerGate("near_harad_gate");
    public static final Block ORC_GATE = registerGate("orc_gate");
    public static final Block RHUN_GATE = registerGate("rhun_gate");
    public static final Block ROHAN_GATE = registerGate("rohan_gate");
    public static final Block SILVER_GATE = registerGate("silver_gate");
    public static final Block TAUREDAIN_GATE = registerGate("tauredain_gate");
    public static final Block URUK_GATE = registerGate("uruk_gate");
    public static final Block WOOD_ELVEN_GATE = registerGate("wood_elven_gate");
    public static final Block WOODEN_GATE = registerGate("wooden_gate");

    // --- Berry bushes ---
    public static final Block BERRY_BUSH_BLACKBERRY = registerBush("berry_bush_blackberry");
    public static final Block BERRY_BUSH_BLUEBERRY = registerBush("berry_bush_blueberry");
    public static final Block BERRY_BUSH_CRANBERRY = registerBush("berry_bush_cranberry");
    public static final Block BERRY_BUSH_ELDERBERRY = registerBush("berry_bush_elderberry");
    public static final Block BERRY_BUSH_RASPBERRY = registerBush("berry_bush_raspberry");
    public static final Block BERRY_BUSH_WILDBERRY = registerBush("berry_bush_wildberry");

    // --- Tall crops and water plants (cross model) ---
    public static final Block CORN_STALK = registerFlower("corn_stalk");
    public static final Block GRAPEVINE = registerFlower("grapevine");
    public static final Block REEDS = registerFlower("reeds");
    public static final Block DRIED_REEDS = registerFlower("dried_reeds");
    public static final Block FANGORN_RIVERWEED = registerFlower("fangorn_riverweed");
    public static final Block WEB_UNGOLIANT = registerWeb("web_ungoliant");
    public static final Block ROPE = registerLadder("rope");

    // --- Coral reef ---
    public static final Block CORAL_REEF = registerSoftBlock("coral_reef", 1.0f, SoundType.STONE);
    public static final Block CORAL_REEF_BLUE = registerSoftBlock("coral_reef_blue", 1.0f, SoundType.STONE);
    public static final Block CORAL_REEF_GREEN = registerSoftBlock("coral_reef_green", 1.0f, SoundType.STONE);
    public static final Block CORAL_REEF_PURPLE = registerSoftBlock("coral_reef_purple", 1.0f, SoundType.STONE);
    public static final Block CORAL_REEF_RED = registerSoftBlock("coral_reef_red", 1.0f, SoundType.STONE);
    public static final Block CORAL_REEF_YELLOW = registerSoftBlock("coral_reef_yellow", 1.0f, SoundType.STONE);

    // --- Portcullises (bar-style gates) ---
    public static final Block GATE_BRONZE_BARS = registerBars("gate_bronze_bars");
    public static final Block GATE_IRON_BARS = registerBars("gate_iron_bars");
    public static final Block GATE_WOODEN_CROSS = registerBars("gate_wooden_cross");

    // --- Misc functional blocks (behaviour deferred; see notes) ---
    public static final Block MECHANISED_RAIL_OFF = registerSoftBlock("mechanised_rail_off", 0.7f, SoundType.METAL);
    public static final Block MECHANISED_RAIL_ON = registerSoftBlock("mechanised_rail_on", 0.7f, SoundType.METAL);
    public static final Block UTUMNO_RETURN_PORTAL_BASE = registerSoftBlock("utumno_return_portal_base", 50.0f, SoundType.STONE);
    public static final Block UTUMNO_RETURN_LIGHT = registerSoftBlock("utumno_return_light", 0.3f, SoundType.GLASS);
    public static final Block DIRT_PATH_DIRT = registerSoftBlock("dirt_path_dirt", 0.65f, SoundType.GRAVEL);
    public static final Block DIRT_PATH_MUD = registerSoftBlock("dirt_path_mud", 0.65f, SoundType.GRAVEL);
    public static final Block MUD_FARMLAND_DRY = registerSoftBlock("mud_farmland_dry", 0.6f, SoundType.GRAVEL);
    public static final Block MUD_FARMLAND_WET = registerSoftBlock("mud_farmland_wet", 0.6f, SoundType.GRAVEL);

    // --- Fence gates (one per plank species) ---

    // --- Buttons and pressure plates (rock, from the base game's own rock types) ---
    public static final Block MORDOR_ROCK_BUTTON = registerButton("mordor_rock_button", MORDOR_ROCK);
    public static final Block MORDOR_ROCK_PRESSURE_PLATE = registerPressurePlate("mordor_rock_pressure_plate", MORDOR_ROCK);
    public static final Block GONDOR_ROCK_BUTTON = registerButton("gondor_rock_button", GONDOR_ROCK);
    public static final Block GONDOR_ROCK_PRESSURE_PLATE = registerPressurePlate("gondor_rock_pressure_plate", GONDOR_ROCK);
    public static final Block ROHAN_ROCK_BUTTON = registerButton("rohan_rock_button", ROHAN_ROCK);
    public static final Block ROHAN_ROCK_PRESSURE_PLATE = registerPressurePlate("rohan_rock_pressure_plate", ROHAN_ROCK);
    public static final Block BLUE_ROCK_BUTTON = registerButton("blue_rock_button", BLUE_ROCK);
    public static final Block BLUE_ROCK_PRESSURE_PLATE = registerPressurePlate("blue_rock_pressure_plate", BLUE_ROCK);
    public static final Block RED_ROCK_BUTTON = registerButton("red_rock_button", RED_ROCK);
    public static final Block RED_ROCK_PRESSURE_PLATE = registerPressurePlate("red_rock_pressure_plate", RED_ROCK);
    public static final Block CHALK_BUTTON = registerButton("chalk_button", CHALK);
    public static final Block CHALK_PRESSURE_PLATE = registerPressurePlate("chalk_pressure_plate", CHALK);


    // --- Carpets (one pixel tall) ---
    public static final Block MORDOR_MOSS = registerCarpet("mordor_moss", 0.2f);
    public static final Block THATCH_FLOOR = registerCarpet("thatch_floor", 0.2f);

    // --- Wooden planks ---
    public static final Block CHERRY_PLANKS = registerPlanks("cherry_planks");
    public static final Block ALMOND_PLANKS = registerPlanks("almond_planks");
    public static final Block APPLE_PLANKS = registerPlanks("apple_planks");
    public static final Block ASPEN_PLANKS = registerPlanks("aspen_planks");
    public static final Block BANANA_PLANKS = registerPlanks("banana_planks");
    public static final Block BAOBAB_PLANKS = registerPlanks("baobab_planks");
    public static final Block BEECH_PLANKS = registerPlanks("beech_planks");
    public static final Block CEDAR_PLANKS = registerPlanks("cedar_planks");
    public static final Block CHARRED_PLANKS = registerPlanks("charred_planks");
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
    public static final Block CHERRY_LEAVES = registerLeaves("cherry_leaves");
    public static final Block DARK_OAK_LEAVES = registerLeaves("dark_oak_leaves");
    public static final Block MANGROVE_LEAVES = registerLeaves("mangrove_leaves");
    public static final Block ALMOND_LEAVES = registerLeaves("almond_leaves");
    public static final Block APPLE_LEAVES = registerLeaves("apple_leaves");
    public static final Block ASPEN_LEAVES = registerLeaves("aspen_leaves");
    public static final Block BANANA_LEAVES = registerLeaves("banana_leaves");
    public static final Block BAOBAB_LEAVES = registerLeaves("baobab_leaves");
    public static final Block BEECH_LEAVES = registerLeaves("beech_leaves");
    public static final Block CEDAR_LEAVES = registerLeaves("cedar_leaves");
    public static final Block CHESTNUT_LEAVES = registerLeaves("chestnut_leaves");
    public static final Block CYPRESS_LEAVES = registerLeaves("cypress_leaves");
    public static final Block DATE_PALM_LEAVES = registerLeaves("date_palm_leaves");
    public static final Block DRAGON_LEAVES = registerLeaves("dragon_leaves");
    public static final Block FIR_LEAVES = registerLeaves("fir_leaves");
    public static final Block GREEN_OAK_LEAVES = registerLeaves("green_oak_leaves");
    public static final Block HOLLY_LEAVES = registerLeaves("holly_leaves");
    public static final Block KANUKA_LEAVES = registerLeaves("kanuka_leaves");
    public static final Block LAIRELOSSE_LEAVES = registerLeaves("lairelosse_leaves");
    public static final Block LARCH_LEAVES = registerLeaves("larch_leaves");
    public static final Block LEBETHRON_LEAVES = registerLeaves("lebethron_leaves");
    public static final Block LEMON_LEAVES = registerLeaves("lemon_leaves");
    public static final Block LIME_LEAVES = registerLeaves("lime_leaves");
    public static final Block MAHOGANY_LEAVES = registerLeaves("mahogany_leaves");
    public static final Block MALLORN_LEAVES = registerLeaves("mallorn_leaves");
    public static final Block MANGO_LEAVES = registerLeaves("mango_leaves");
    public static final Block MAPLE_LEAVES = registerLeaves("maple_leaves");
    public static final Block MIRK_OAK_LEAVES = registerLeaves("mirk_oak_leaves");
    public static final Block MIRK_OAK_RED_LEAVES = registerLeaves("mirk_oak_red_leaves");
    public static final Block OLIVE_LEAVES = registerLeaves("olive_leaves");
    public static final Block ORANGE_LEAVES = registerLeaves("orange_leaves");
    public static final Block PALM_LEAVES = registerLeaves("palm_leaves");
    public static final Block PEAR_LEAVES = registerLeaves("pear_leaves");
    public static final Block PINE_LEAVES = registerLeaves("pine_leaves");
    public static final Block PLUM_LEAVES = registerLeaves("plum_leaves");
    public static final Block POMEGRANATE_LEAVES = registerLeaves("pomegranate_leaves");
    public static final Block REDWOOD_LEAVES = registerLeaves("redwood_leaves");
    public static final Block SHIRE_PINE_LEAVES = registerLeaves("shire_pine_leaves");
    public static final Block WILLOW_LEAVES = registerLeaves("willow_leaves");

    // --- Saplings (cross model, decorative) ---
    public static final Block CHERRY_SAPLING = registerSapling("cherry_sapling");
    public static final Block ALMOND_SAPLING = registerSapling("almond_sapling");
    public static final Block APPLE_SAPLING = registerSapling("apple_sapling");
    public static final Block ASPEN_SAPLING = registerSapling("aspen_sapling");
    public static final Block BANANA_SAPLING = registerSapling("banana_sapling");
    public static final Block BAOBAB_SAPLING = registerSapling("baobab_sapling");
    public static final Block BEECH_SAPLING = registerSapling("beech_sapling");
    public static final Block CEDAR_SAPLING = registerSapling("cedar_sapling");
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


    // --- Flowers and small plants (cross model, decorative) ---
    public static final Block ASPHODEL = registerFlower("asphodel");
    public static final Block ATHELAS = registerFlower("athelas");
    public static final Block BLUEBELL = registerFlower("bluebell");
    public static final Block DWARF_HERB = registerFlower("dwarf_herb");
    public static final Block ELANOR = registerFlower("elanor");
    public static final Block FLAX_PLANT = registerFlower("flax_plant");
    public static final Block LAVENDER = registerFlower("lavender");
    public static final Block MARIGOLD = registerFlower("marigold");
    public static final Block MORGUL_FLOWER = registerFlower("morgul_flower");
    public static final Block NIPHREDIL = registerFlower("niphredil");
    public static final Block SHIRE_HEATHER = registerFlower("shire_heather");
    public static final Block SIMBELMYNE = registerFlower("simbelmyne");
    public static final Block HARAD_FLOWER_DAISY = registerFlower("harad_flower_daisy");
    public static final Block HARAD_FLOWER_PINK = registerFlower("harad_flower_pink");
    public static final Block HARAD_FLOWER_RED = registerFlower("harad_flower_red");
    public static final Block HARAD_FLOWER_YELLOW = registerFlower("harad_flower_yellow");
    public static final Block RHUN_FLOWER_CHRYS_BLUE = registerFlower("rhun_flower_chrys_blue");
    public static final Block RHUN_FLOWER_CHRYS_ORANGE = registerFlower("rhun_flower_chrys_orange");
    public static final Block RHUN_FLOWER_CHRYS_PINK = registerFlower("rhun_flower_chrys_pink");
    public static final Block RHUN_FLOWER_CHRYS_WHITE = registerFlower("rhun_flower_chrys_white");
    public static final Block RHUN_FLOWER_CHRYS_YELLOW = registerFlower("rhun_flower_chrys_yellow");


    // --- Plants (cross model; all extend LOTRBlockFlower or BlockBush) ---
    public static final Block ARID_GRASS = registerFlower("arid_grass");
    public static final Block BLACKROOT = registerFlower("blackroot");
    public static final Block CORRUPT_MALLORN = registerFlower("corrupt_mallorn");
    public static final Block DEAD_MARSH_PLANT = registerFlower("dead_marsh_plant");
    public static final Block FANGORN_PLANT_BROWN = registerFlower("fangorn_plant_brown");
    public static final Block FANGORN_PLANT_GOLD = registerFlower("fangorn_plant_gold");
    public static final Block FANGORN_PLANT_GREEN = registerFlower("fangorn_plant_green");
    public static final Block FANGORN_PLANT_RED = registerFlower("fangorn_plant_red");
    public static final Block FANGORN_PLANT_SILVER = registerFlower("fangorn_plant_silver");
    public static final Block FANGORN_PLANT_YELLOW = registerFlower("fangorn_plant_yellow");
    public static final Block MORDOR_GRASS = registerFlower("mordor_grass");
    public static final Block MORDOR_THORN = registerFlower("mordor_thorn");
    public static final Block MORGUL_SHROOM = registerFlower("morgul_shroom");
    public static final Block PIPEWEED_PLANT = registerFlower("pipeweed_plant");


    // --- Logs (axis-rotatable, side + _top textures) ---
    public static final Block CHERRY_LOG = registerLog("cherry_log");
    public static final Block ALMOND_LOG = registerLog("almond_log");
    public static final Block APPLE_LOG = registerLog("apple_log");
    public static final Block ASPEN_LOG = registerLog("aspen_log");
    public static final Block BANANA_LOG = registerLog("banana_log");
    public static final Block BAOBAB_LOG = registerLog("baobab_log");
    public static final Block BEECH_LOG = registerLog("beech_log");
    public static final Block CEDAR_LOG = registerLog("cedar_log");
    public static final Block CHARRED_LOG = registerLog("charred_log");
    public static final Block CHESTNUT_LOG = registerLog("chestnut_log");
    public static final Block CYPRESS_LOG = registerLog("cypress_log");
    public static final Block DATE_PALM_LOG = registerLog("date_palm_log");
    public static final Block DRAGON_LOG = registerLog("dragon_log");
    public static final Block FIR_LOG = registerLog("fir_log");
    public static final Block GREEN_OAK_LOG = registerLog("green_oak_log");
    public static final Block HOLLY_LOG = registerLog("holly_log");
    public static final Block KANUKA_LOG = registerLog("kanuka_log");
    public static final Block LAIRELOSSE_LOG = registerLog("lairelosse_log");
    public static final Block LARCH_LOG = registerLog("larch_log");
    public static final Block LEBETHRON_LOG = registerLog("lebethron_log");
    public static final Block LEMON_LOG = registerLog("lemon_log");
    public static final Block LIME_LOG = registerLog("lime_log");
    public static final Block MAHOGANY_LOG = registerLog("mahogany_log");
    public static final Block MALLORN_LOG = registerLog("mallorn_log");
    public static final Block MANGO_LOG = registerLog("mango_log");
    public static final Block MANGROVE_LOG = registerLog("mangrove_log");
    public static final Block MAPLE_LOG = registerLog("maple_log");
    public static final Block MIRK_OAK_LOG = registerLog("mirk_oak_log");
    public static final Block OLIVE_LOG = registerLog("olive_log");
    public static final Block ORANGE_LOG = registerLog("orange_log");
    public static final Block PALM_LOG = registerLog("palm_log");
    public static final Block PEAR_LOG = registerLog("pear_log");
    public static final Block PINE_LOG = registerLog("pine_log");
    public static final Block PLUM_LOG = registerLog("plum_log");
    public static final Block POMEGRANATE_LOG = registerLog("pomegranate_log");
    public static final Block REDWOOD_LOG = registerLog("redwood_log");
    public static final Block ROTTEN_LOG = registerLog("rotten_log");
    public static final Block SHIRE_PINE_LOG = registerLog("shire_pine_log");
    public static final Block WILLOW_LOG = registerLog("willow_log");

    // --- Wooden beams (axis-rotatable) ---
    public static final Block ACACIA_BEAM = registerBeam("acacia_beam");
    public static final Block ALMOND_BEAM = registerBeam("almond_beam");
    public static final Block APPLE_BEAM = registerBeam("apple_beam");
    public static final Block ASPEN_BEAM = registerBeam("aspen_beam");
    public static final Block BANANA_BEAM = registerBeam("banana_beam");
    public static final Block BAOBAB_BEAM = registerBeam("baobab_beam");
    public static final Block BEECH_BEAM = registerBeam("beech_beam");
    public static final Block BIRCH_BEAM = registerBeam("birch_beam");
    public static final Block CEDAR_BEAM = registerBeam("cedar_beam");
    public static final Block CHARRED_BEAM = registerBeam("charred_beam");
    public static final Block CHERRY_BEAM = registerBeam("cherry_beam");
    public static final Block CHESTNUT_BEAM = registerBeam("chestnut_beam");
    public static final Block CYPRESS_BEAM = registerBeam("cypress_beam");
    public static final Block DARK_OAK_BEAM = registerBeam("dark_oak_beam");
    public static final Block DATE_PALM_BEAM = registerBeam("date_palm_beam");
    public static final Block DRAGON_BEAM = registerBeam("dragon_beam");
    public static final Block FIR_BEAM = registerBeam("fir_beam");
    public static final Block GREEN_OAK_BEAM = registerBeam("green_oak_beam");
    public static final Block HOLLY_BEAM = registerBeam("holly_beam");
    public static final Block JUNGLE_BEAM = registerBeam("jungle_beam");
    public static final Block KANUKA_BEAM = registerBeam("kanuka_beam");
    public static final Block LAIRELOSSE_BEAM = registerBeam("lairelosse_beam");
    public static final Block LARCH_BEAM = registerBeam("larch_beam");
    public static final Block LEBETHRON_BEAM = registerBeam("lebethron_beam");
    public static final Block LEMON_BEAM = registerBeam("lemon_beam");
    public static final Block LIME_BEAM = registerBeam("lime_beam");
    public static final Block MAHOGANY_BEAM = registerBeam("mahogany_beam");
    public static final Block MALLORN_BEAM = registerBeam("mallorn_beam");
    public static final Block MANGO_BEAM = registerBeam("mango_beam");
    public static final Block MANGROVE_BEAM = registerBeam("mangrove_beam");
    public static final Block MAPLE_BEAM = registerBeam("maple_beam");
    public static final Block MIRK_OAK_BEAM = registerBeam("mirk_oak_beam");
    public static final Block OAK_BEAM = registerBeam("oak_beam");
    public static final Block OLIVE_BEAM = registerBeam("olive_beam");
    public static final Block ORANGE_BEAM = registerBeam("orange_beam");
    public static final Block PALM_BEAM = registerBeam("palm_beam");
    public static final Block PEAR_BEAM = registerBeam("pear_beam");
    public static final Block PINE_BEAM = registerBeam("pine_beam");
    public static final Block PLUM_BEAM = registerBeam("plum_beam");
    public static final Block POMEGRANATE_BEAM = registerBeam("pomegranate_beam");
    public static final Block REDWOOD_BEAM = registerBeam("redwood_beam");
    public static final Block ROHAN_BEAM = registerBeam("rohan_beam");
    public static final Block ROHAN_GOLD_BEAM = registerBeam("rohan_gold_beam");
    public static final Block ROTTEN_BEAM = registerBeam("rotten_beam");
    public static final Block SHIRE_PINE_BEAM = registerBeam("shire_pine_beam");
    public static final Block SPRUCE_BEAM = registerBeam("spruce_beam");
    public static final Block WILLOW_BEAM = registerBeam("willow_beam");

    // --- Stone pillars (axis-rotatable) ---
    public static final Block ANGMAR_PILLAR = registerPillar("angmar_pillar");
    public static final Block ARNOR_CRACKED_PILLAR = registerPillar("arnor_cracked_pillar");
    public static final Block ARNOR_PILLAR = registerPillar("arnor_pillar");
    public static final Block BLACK_GONDOR_PILLAR = registerPillar("black_gondor_pillar");
    public static final Block BLUE_ROCK_PILLAR = registerPillar("blue_rock_pillar");
    public static final Block BRICK_PILLAR = registerPillar("brick_pillar");
    public static final Block CHALK_PILLAR = registerPillar("chalk_pillar");
    public static final Block DALE_PILLAR = registerPillar("dale_pillar");
    public static final Block DOL_GULDUR_PILLAR = registerPillar("dol_guldur_pillar");
    public static final Block DORWINION_MOSSY_PILLAR = registerPillar("dorwinion_mossy_pillar");
    public static final Block DORWINION_PILLAR = registerPillar("dorwinion_pillar");
    public static final Block DWARVEN_CRACKED_PILLAR = registerPillar("dwarven_cracked_pillar");
    public static final Block DWARVEN_PILLAR = registerPillar("dwarven_pillar");
    public static final Block GALADHRIM_CRACKED_PILLAR = registerPillar("galadhrim_cracked_pillar");
    public static final Block GALADHRIM_PILLAR = registerPillar("galadhrim_pillar");
    public static final Block GONDOR_PILLAR = registerPillar("gondor_pillar");
    public static final Block HIGH_ELVEN_CRACKED_PILLAR = registerPillar("high_elven_cracked_pillar");
    public static final Block HIGH_ELVEN_PILLAR = registerPillar("high_elven_pillar");
    public static final Block MORDOR_PILLAR = registerPillar("mordor_pillar");
    public static final Block NEAR_HARAD_PILLAR = registerPillar("near_harad_pillar");
    public static final Block NEAR_HARAD_RED_PILLAR = registerPillar("near_harad_red_pillar");
    public static final Block RED_ROCK_PILLAR = registerPillar("red_rock_pillar");
    public static final Block RHUN_PILLAR = registerPillar("rhun_pillar");
    public static final Block RHUN_RED_PILLAR = registerPillar("rhun_red_pillar");
    public static final Block ROHAN_PILLAR = registerPillar("rohan_pillar");
    public static final Block STONE_PILLAR = registerPillar("stone_pillar");
    public static final Block TAUR_GOLD_PILLAR = registerPillar("taur_gold_pillar");
    public static final Block TAUR_OBSIDIAN_PILLAR = registerPillar("taur_obsidian_pillar");
    public static final Block TAUREDAIN_PILLAR = registerPillar("tauredain_pillar");
    public static final Block UMBAR_PILLAR = registerPillar("umbar_pillar");
    public static final Block URUK_PILLAR = registerPillar("uruk_pillar");
    public static final Block WOOD_ELVEN_CRACKED_PILLAR = registerPillar("wood_elven_cracked_pillar");
    public static final Block WOOD_ELVEN_PILLAR = registerPillar("wood_elven_pillar");

    // --- Trapdoors ---
    public static final Block CHERRY_TRAPDOOR = registerTrapdoor("cherry_trapdoor");
    public static final Block DARK_OAK_TRAPDOOR = registerTrapdoor("dark_oak_trapdoor");
    public static final Block ALMOND_TRAPDOOR = registerTrapdoor("almond_trapdoor");
    public static final Block APPLE_TRAPDOOR = registerTrapdoor("apple_trapdoor");
    public static final Block ASPEN_TRAPDOOR = registerTrapdoor("aspen_trapdoor");
    public static final Block BANANA_TRAPDOOR = registerTrapdoor("banana_trapdoor");
    public static final Block BAOBAB_TRAPDOOR = registerTrapdoor("baobab_trapdoor");
    public static final Block BEECH_TRAPDOOR = registerTrapdoor("beech_trapdoor");
    public static final Block CEDAR_TRAPDOOR = registerTrapdoor("cedar_trapdoor");
    public static final Block CHARRED_TRAPDOOR = registerTrapdoor("charred_trapdoor");
    public static final Block CHESTNUT_TRAPDOOR = registerTrapdoor("chestnut_trapdoor");
    public static final Block CYPRESS_TRAPDOOR = registerTrapdoor("cypress_trapdoor");
    public static final Block DATE_PALM_TRAPDOOR = registerTrapdoor("date_palm_trapdoor");
    public static final Block DRAGON_TRAPDOOR = registerTrapdoor("dragon_trapdoor");
    public static final Block FIR_TRAPDOOR = registerTrapdoor("fir_trapdoor");
    public static final Block GREEN_OAK_TRAPDOOR = registerTrapdoor("green_oak_trapdoor");
    public static final Block HOLLY_TRAPDOOR = registerTrapdoor("holly_trapdoor");
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
    public static final Block WILLOW_TRAPDOOR = registerTrapdoor("willow_trapdoor");

    // --- Doors ---
    public static final Block CHERRY_DOOR = registerDoor("cherry_door");
    public static final Block DARK_OAK_DOOR = registerDoor("dark_oak_door");
    public static final Block ALMOND_DOOR = registerDoor("almond_door");
    public static final Block APPLE_DOOR = registerDoor("apple_door");
    public static final Block ASPEN_DOOR = registerDoor("aspen_door");
    public static final Block BANANA_DOOR = registerDoor("banana_door");
    public static final Block BAOBAB_DOOR = registerDoor("baobab_door");
    public static final Block BEECH_DOOR = registerDoor("beech_door");
    public static final Block CEDAR_DOOR = registerDoor("cedar_door");
    public static final Block CHARRED_DOOR = registerDoor("charred_door");
    public static final Block CHESTNUT_DOOR = registerDoor("chestnut_door");
    public static final Block CYPRESS_DOOR = registerDoor("cypress_door");
    public static final Block DATE_PALM_DOOR = registerDoor("date_palm_door");
    public static final Block DRAGON_DOOR = registerDoor("dragon_door");
    public static final Block FIR_DOOR = registerDoor("fir_door");
    public static final Block GREEN_OAK_DOOR = registerDoor("green_oak_door");
    public static final Block HOLLY_DOOR = registerDoor("holly_door");
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

    // --- Stairs (properties and texture copied from the base block) ---
    public static final Block ALMOND_STAIRS = registerStairs("almond_stairs", ALMOND_PLANKS);
    public static final Block ANGMAR_BRICK_STAIRS = registerStairs("angmar_brick_stairs", ANGMAR_BRICK);
    public static final Block ANGMAR_CRACKED_BRICK_STAIRS = registerStairs("angmar_cracked_brick_stairs", ANGMAR_CRACKED_BRICK);
    public static final Block ANGMAR_SNOW_BRICK_STAIRS = registerStairs("angmar_snow_brick_stairs", ANGMAR_SNOW_BRICK);
    public static final Block APPLE_STAIRS = registerStairs("apple_stairs", APPLE_PLANKS);
    public static final Block ARNOR_BRICK_STAIRS = registerStairs("arnor_brick_stairs", ARNOR_BRICK);
    public static final Block ARNOR_CRACKED_BRICK_STAIRS = registerStairs("arnor_cracked_brick_stairs", ARNOR_CRACKED_BRICK);
    public static final Block ARNOR_MOSSY_BRICK_STAIRS = registerStairs("arnor_mossy_brick_stairs", ARNOR_MOSSY_BRICK);
    public static final Block ASPEN_STAIRS = registerStairs("aspen_stairs", ASPEN_PLANKS);
    public static final Block BANANA_STAIRS = registerStairs("banana_stairs", BANANA_PLANKS);
    public static final Block BAOBAB_STAIRS = registerStairs("baobab_stairs", BAOBAB_PLANKS);
    public static final Block BEECH_STAIRS = registerStairs("beech_stairs", BEECH_PLANKS);
    public static final Block BLACK_GONDOR_BRICK_STAIRS = registerStairs("black_gondor_brick_stairs", BLACK_GONDOR_BRICK);
    public static final Block BLUE_ROCK_BRICK_STAIRS = registerStairs("blue_rock_brick_stairs", BLUE_ROCK_BRICK);
    public static final Block BLUE_ROCK_STAIRS = registerStairs("blue_rock_stairs", BLUE_ROCK);
    public static final Block CEDAR_STAIRS = registerStairs("cedar_stairs", CEDAR_PLANKS);
    public static final Block CHALK_BRICK_STAIRS = registerStairs("chalk_brick_stairs", CHALK_BRICK);
    public static final Block CHALK_STAIRS = registerStairs("chalk_stairs", CHALK);
    public static final Block CHARRED_STAIRS = registerStairs("charred_stairs", CHARRED_PLANKS);
    public static final Block CHESTNUT_STAIRS = registerStairs("chestnut_stairs", CHESTNUT_PLANKS);
    public static final Block CLAY_TILE_DYED_BLACK_STAIRS = registerStairs("clay_tile_dyed_black_stairs", CLAY_TILE_DYED_BLACK);
    public static final Block CLAY_TILE_DYED_BLUE_STAIRS = registerStairs("clay_tile_dyed_blue_stairs", CLAY_TILE_DYED_BLUE);
    public static final Block CLAY_TILE_DYED_BROWN_STAIRS = registerStairs("clay_tile_dyed_brown_stairs", CLAY_TILE_DYED_BROWN);
    public static final Block CLAY_TILE_DYED_CYAN_STAIRS = registerStairs("clay_tile_dyed_cyan_stairs", CLAY_TILE_DYED_CYAN);
    public static final Block CLAY_TILE_DYED_GRAY_STAIRS = registerStairs("clay_tile_dyed_gray_stairs", CLAY_TILE_DYED_GRAY);
    public static final Block CLAY_TILE_DYED_GREEN_STAIRS = registerStairs("clay_tile_dyed_green_stairs", CLAY_TILE_DYED_GREEN);
    public static final Block CLAY_TILE_DYED_LIGHT_BLUE_STAIRS = registerStairs("clay_tile_dyed_light_blue_stairs", CLAY_TILE_DYED_LIGHT_BLUE);
    public static final Block CLAY_TILE_DYED_LIME_STAIRS = registerStairs("clay_tile_dyed_lime_stairs", CLAY_TILE_DYED_LIME);
    public static final Block CLAY_TILE_DYED_MAGENTA_STAIRS = registerStairs("clay_tile_dyed_magenta_stairs", CLAY_TILE_DYED_MAGENTA);
    public static final Block CLAY_TILE_DYED_ORANGE_STAIRS = registerStairs("clay_tile_dyed_orange_stairs", CLAY_TILE_DYED_ORANGE);
    public static final Block CLAY_TILE_DYED_PINK_STAIRS = registerStairs("clay_tile_dyed_pink_stairs", CLAY_TILE_DYED_PINK);
    public static final Block CLAY_TILE_DYED_PURPLE_STAIRS = registerStairs("clay_tile_dyed_purple_stairs", CLAY_TILE_DYED_PURPLE);
    public static final Block CLAY_TILE_DYED_RED_STAIRS = registerStairs("clay_tile_dyed_red_stairs", CLAY_TILE_DYED_RED);
    public static final Block CLAY_TILE_DYED_SILVER_STAIRS = registerStairs("clay_tile_dyed_silver_stairs", CLAY_TILE_DYED_SILVER);
    public static final Block CLAY_TILE_DYED_WHITE_STAIRS = registerStairs("clay_tile_dyed_white_stairs", CLAY_TILE_DYED_WHITE);
    public static final Block CLAY_TILE_DYED_YELLOW_STAIRS = registerStairs("clay_tile_dyed_yellow_stairs", CLAY_TILE_DYED_YELLOW);
    public static final Block CLAY_TILE_STAIRS = registerStairs("clay_tile_stairs", CLAY_TILE);
    public static final Block CYPRESS_STAIRS = registerStairs("cypress_stairs", CYPRESS_PLANKS);
    public static final Block DALE_BRICK_STAIRS = registerStairs("dale_brick_stairs", DALE_BRICK);
    public static final Block DALE_CRACKED_BRICK_STAIRS = registerStairs("dale_cracked_brick_stairs", DALE_CRACKED_BRICK);
    public static final Block DALE_MOSSY_BRICK_STAIRS = registerStairs("dale_mossy_brick_stairs", DALE_MOSSY_BRICK);
    public static final Block DATE_PALM_STAIRS = registerStairs("date_palm_stairs", DATE_PALM_PLANKS);
    public static final Block DOL_AMROTH_BRICK_STAIRS = registerStairs("dol_amroth_brick_stairs", DOL_AMROTH_BRICK);
    public static final Block DOL_GULDUR_BRICK_STAIRS = registerStairs("dol_guldur_brick_stairs", DOL_GULDUR_BRICK);
    public static final Block DOL_GULDUR_CRACKED_BRICK_STAIRS = registerStairs("dol_guldur_cracked_brick_stairs", DOL_GULDUR_CRACKED_BRICK);
    public static final Block DOL_GULDUR_MOSSY_BRICK_STAIRS = registerStairs("dol_guldur_mossy_brick_stairs", DOL_GULDUR_MOSSY_BRICK);
    public static final Block DORWINION_BRICK_STAIRS = registerStairs("dorwinion_brick_stairs", DORWINION_BRICK);
    public static final Block DORWINION_CRACKED_BRICK_STAIRS = registerStairs("dorwinion_cracked_brick_stairs", DORWINION_CRACKED_BRICK);
    public static final Block DORWINION_FLOWERS_BRICK_STAIRS = registerStairs("dorwinion_flowers_brick_stairs", DORWINION_FLOWERS_BRICK);
    public static final Block DORWINION_MOSSY_BRICK_STAIRS = registerStairs("dorwinion_mossy_brick_stairs", DORWINION_MOSSY_BRICK);
    public static final Block DRAGON_STAIRS = registerStairs("dragon_stairs", DRAGON_PLANKS);
    public static final Block DWARVEN_BRICK_STAIRS = registerStairs("dwarven_brick_stairs", DWARVEN_BRICK);
    public static final Block DWARVEN_CRACKED_BRICK_STAIRS = registerStairs("dwarven_cracked_brick_stairs", DWARVEN_CRACKED_BRICK);
    public static final Block DWARVEN_OBSIDIAN_BRICK_STAIRS = registerStairs("dwarven_obsidian_brick_stairs", DWARVEN_OBSIDIAN_BRICK);
    public static final Block FIR_STAIRS = registerStairs("fir_stairs", FIR_PLANKS);
    public static final Block GALADHRIM_BRICK_STAIRS = registerStairs("galadhrim_brick_stairs", GALADHRIM_BRICK);
    public static final Block GALADHRIM_CRACKED_BRICK_STAIRS = registerStairs("galadhrim_cracked_brick_stairs", GALADHRIM_CRACKED_BRICK);
    public static final Block GALADHRIM_MOSSY_BRICK_STAIRS = registerStairs("galadhrim_mossy_brick_stairs", GALADHRIM_MOSSY_BRICK);
    public static final Block GONDOR_BRICK_STAIRS = registerStairs("gondor_brick_stairs", GONDOR_BRICK);
    public static final Block GONDOR_CRACKED_BRICK_STAIRS = registerStairs("gondor_cracked_brick_stairs", GONDOR_CRACKED_BRICK);
    public static final Block GONDOR_MOSSY_BRICK_STAIRS = registerStairs("gondor_mossy_brick_stairs", GONDOR_MOSSY_BRICK);
    public static final Block GONDOR_ROCK_STAIRS = registerStairs("gondor_rock_stairs", GONDOR_ROCK);
    public static final Block GONDOR_RUSTIC_BRICK_STAIRS = registerStairs("gondor_rustic_brick_stairs", GONDOR_RUSTIC_BRICK);
    public static final Block GONDOR_RUSTIC_CRACKED_BRICK_STAIRS = registerStairs("gondor_rustic_cracked_brick_stairs", GONDOR_RUSTIC_CRACKED_BRICK);
    public static final Block GONDOR_RUSTIC_MOSSY_BRICK_STAIRS = registerStairs("gondor_rustic_mossy_brick_stairs", GONDOR_RUSTIC_MOSSY_BRICK);
    public static final Block GREEN_OAK_STAIRS = registerStairs("green_oak_stairs", GREEN_OAK_PLANKS);
    public static final Block HIGH_ELVEN_BRICK_STAIRS = registerStairs("high_elven_brick_stairs", HIGH_ELVEN_BRICK);
    public static final Block HIGH_ELVEN_CRACKED_BRICK_STAIRS = registerStairs("high_elven_cracked_brick_stairs", HIGH_ELVEN_CRACKED_BRICK);
    public static final Block HIGH_ELVEN_MOSSY_BRICK_STAIRS = registerStairs("high_elven_mossy_brick_stairs", HIGH_ELVEN_MOSSY_BRICK);
    public static final Block HOLLY_STAIRS = registerStairs("holly_stairs", HOLLY_PLANKS);
    public static final Block KANUKA_STAIRS = registerStairs("kanuka_stairs", KANUKA_PLANKS);
    public static final Block LAIRELOSSE_STAIRS = registerStairs("lairelosse_stairs", LAIRELOSSE_PLANKS);
    public static final Block LARCH_STAIRS = registerStairs("larch_stairs", LARCH_PLANKS);
    public static final Block LEBETHRON_STAIRS = registerStairs("lebethron_stairs", LEBETHRON_PLANKS);
    public static final Block LEMON_STAIRS = registerStairs("lemon_stairs", LEMON_PLANKS);
    public static final Block LIME_STAIRS = registerStairs("lime_stairs", LIME_PLANKS);
    public static final Block MAHOGANY_STAIRS = registerStairs("mahogany_stairs", MAHOGANY_PLANKS);
    public static final Block MALLORN_STAIRS = registerStairs("mallorn_stairs", MALLORN_PLANKS);
    public static final Block MANGO_STAIRS = registerStairs("mango_stairs", MANGO_PLANKS);
    public static final Block MANGROVE_STAIRS = registerStairs("mangrove_stairs", MANGROVE_PLANKS);
    public static final Block MAPLE_STAIRS = registerStairs("maple_stairs", MAPLE_PLANKS);
    public static final Block MIRK_OAK_STAIRS = registerStairs("mirk_oak_stairs", MIRK_OAK_PLANKS);
    public static final Block MORDOR_BRICK_STAIRS = registerStairs("mordor_brick_stairs", MORDOR_BRICK);
    public static final Block MORDOR_CRACKED_BRICK_STAIRS = registerStairs("mordor_cracked_brick_stairs", MORDOR_CRACKED_BRICK);
    public static final Block MORDOR_ROCK_STAIRS = registerStairs("mordor_rock_stairs", MORDOR_ROCK);
    public static final Block MOREDAIN_BRICK_STAIRS = registerStairs("moredain_brick_stairs", MOREDAIN_BRICK);
    public static final Block MORWAITH_CRACKED_BRICK_STAIRS = registerStairs("morwaith_cracked_brick_stairs", MORWAITH_CRACKED_BRICK);
    public static final Block MUD_BRICK_STAIRS = registerStairs("mud_brick_stairs", MUD_BRICK);
    public static final Block NEAR_HARAD_BRICK_STAIRS = registerStairs("near_harad_brick_stairs", NEAR_HARAD_BRICK);
    public static final Block NEAR_HARAD_CRACKED_BRICK_STAIRS = registerStairs("near_harad_cracked_brick_stairs", NEAR_HARAD_CRACKED_BRICK);
    public static final Block NEAR_HARAD_RED_BRICK_STAIRS = registerStairs("near_harad_red_brick_stairs", NEAR_HARAD_RED_BRICK);
    public static final Block NEAR_HARAD_RED_CRACKED_BRICK_STAIRS = registerStairs("near_harad_red_cracked_brick_stairs", NEAR_HARAD_RED_CRACKED_BRICK);
    public static final Block OLIVE_STAIRS = registerStairs("olive_stairs", OLIVE_PLANKS);
    public static final Block ORANGE_STAIRS = registerStairs("orange_stairs", ORANGE_PLANKS);
    public static final Block PALM_STAIRS = registerStairs("palm_stairs", PALM_PLANKS);
    public static final Block PEAR_STAIRS = registerStairs("pear_stairs", PEAR_PLANKS);
    public static final Block PINE_STAIRS = registerStairs("pine_stairs", PINE_PLANKS);
    public static final Block PLUM_STAIRS = registerStairs("plum_stairs", PLUM_PLANKS);
    public static final Block POMEGRANATE_STAIRS = registerStairs("pomegranate_stairs", POMEGRANATE_PLANKS);
    public static final Block RED_BRICK_CRACKED_STAIRS = registerStairs("red_brick_cracked_stairs", RED_BRICK_CRACKED);
    public static final Block RED_BRICK_MOSSY_STAIRS = registerStairs("red_brick_mossy_stairs", RED_BRICK_MOSSY);
    public static final Block RED_ROCK_BRICK_STAIRS = registerStairs("red_rock_brick_stairs", RED_ROCK_BRICK);
    public static final Block RED_ROCK_STAIRS = registerStairs("red_rock_stairs", RED_ROCK);
    public static final Block REDWOOD_STAIRS = registerStairs("redwood_stairs", REDWOOD_PLANKS);
    public static final Block RHUN_BRICK_STAIRS = registerStairs("rhun_brick_stairs", RHUN_BRICK);
    public static final Block RHUN_CRACKED_BRICK_STAIRS = registerStairs("rhun_cracked_brick_stairs", RHUN_CRACKED_BRICK);
    public static final Block RHUN_FLOWERS_BRICK_STAIRS = registerStairs("rhun_flowers_brick_stairs", RHUN_FLOWERS_BRICK);
    public static final Block RHUN_MOSSY_BRICK_STAIRS = registerStairs("rhun_mossy_brick_stairs", RHUN_MOSSY_BRICK);
    public static final Block RHUN_RED_BRICK_STAIRS = registerStairs("rhun_red_brick_stairs", RHUN_RED_BRICK);
    public static final Block ROHAN_BRICK_STAIRS = registerStairs("rohan_brick_stairs", ROHAN_BRICK);
    public static final Block ROHAN_ROCK_STAIRS = registerStairs("rohan_rock_stairs", ROHAN_ROCK);
    public static final Block ROTTEN_STAIRS = registerStairs("rotten_stairs", ROTTEN_PLANKS);
    public static final Block SCORCHED_STONE_STAIRS = registerStairs("scorched_stone_stairs", SCORCHED_STONE);
    public static final Block SHIRE_PINE_STAIRS = registerStairs("shire_pine_stairs", SHIRE_PINE_PLANKS);
    public static final Block TAUREDAIN_BRICK_STAIRS = registerStairs("tauredain_brick_stairs", TAUREDAIN_BRICK);
    public static final Block TAUREDAIN_CRACKED_BRICK_STAIRS = registerStairs("tauredain_cracked_brick_stairs", TAUREDAIN_CRACKED_BRICK);
    public static final Block TAUREDAIN_GOLD_BRICK_STAIRS = registerStairs("tauredain_gold_brick_stairs", TAUREDAIN_GOLD_BRICK);
    public static final Block TAUREDAIN_MOSSY_BRICK_STAIRS = registerStairs("tauredain_mossy_brick_stairs", TAUREDAIN_MOSSY_BRICK);
    public static final Block TAUREDAIN_OBSIDIAN_BRICK_STAIRS = registerStairs("tauredain_obsidian_brick_stairs", TAUREDAIN_OBSIDIAN_BRICK);
    public static final Block THATCH_REED_STAIRS = registerStairs("thatch_reed_stairs", THATCH_REED);
    public static final Block THATCH_THATCH_STAIRS = registerStairs("thatch_thatch_stairs", THATCH_THATCH);
    public static final Block UMBAR_BRICK_STAIRS = registerStairs("umbar_brick_stairs", UMBAR_BRICK);
    public static final Block UMBAR_CRACKED_BRICK_STAIRS = registerStairs("umbar_cracked_brick_stairs", UMBAR_CRACKED_BRICK);
    public static final Block URUK_BRICK_STAIRS = registerStairs("uruk_brick_stairs", URUK_BRICK);
    public static final Block UTUMNO_FIRE_BRICK_STAIRS = registerStairs("utumno_fire_brick_stairs", UTUMNO_FIRE_BRICK);
    public static final Block UTUMNO_FIRE_TILE_BRICK_STAIRS = registerStairs("utumno_fire_tile_brick_stairs", UTUMNO_FIRE_TILE_BRICK);
    public static final Block UTUMNO_ICE_BRICK_STAIRS = registerStairs("utumno_ice_brick_stairs", UTUMNO_ICE_BRICK);
    public static final Block UTUMNO_ICE_TILE_BRICK_STAIRS = registerStairs("utumno_ice_tile_brick_stairs", UTUMNO_ICE_TILE_BRICK);
    public static final Block UTUMNO_OBSIDIAN_BRICK_STAIRS = registerStairs("utumno_obsidian_brick_stairs", UTUMNO_OBSIDIAN_BRICK);
    public static final Block UTUMNO_OBSIDIAN_TILE_BRICK_STAIRS = registerStairs("utumno_obsidian_tile_brick_stairs", UTUMNO_OBSIDIAN_TILE_BRICK);
    public static final Block WHITE_SANDSTONE_STAIRS = registerStairs("white_sandstone_stairs", WHITE_SANDSTONE);
    public static final Block WILLOW_STAIRS = registerStairs("willow_stairs", WILLOW_PLANKS);
    public static final Block WOOD_ELVEN_BRICK_STAIRS = registerStairs("wood_elven_brick_stairs", WOOD_ELVEN_BRICK);
    public static final Block WOOD_ELVEN_CRACKED_BRICK_STAIRS = registerStairs("wood_elven_cracked_brick_stairs", WOOD_ELVEN_CRACKED_BRICK);
    public static final Block WOOD_ELVEN_MOSSY_BRICK_STAIRS = registerStairs("wood_elven_mossy_brick_stairs", WOOD_ELVEN_MOSSY_BRICK);

    /**
     * Stairs cut from an existing block.
     *
     * Takes its properties from the base block, so a brick stair is as tough as
     * its brick and a wooden stair burns like its planks -- which is what the
     * 1.7.10 LOTRBlockStairs(block, meta) constructor did. The base is recorded
     * in STAIRS_BASE so datagen can find the texture to use.
     *
     * NOTE: these fields must be declared AFTER every base block, since they
     * dereference them during static init.
     */
    private static Block registerStairs(String name, Block base) {
        Block stairs = register(name, props -> new StairBlock(base.defaultBlockState(), props),
                BlockBehaviour.Properties.ofFullCopy(base), true);
        ALL_STAIRS.add(stairs);
        STAIRS_BASE.put(stairs, base);
        return stairs;
    }

    public static final Block CHERRY_STAIRS = registerStairs("cherry_stairs", CHERRY_PLANKS);
    public static final Block RED_SANDSTONE_STAIRS = registerStairs("red_sandstone_stairs", RED_SANDSTONE);
    public static final Block GONDOR_COBBLEBRICK_STAIRS = registerStairs("gondor_cobblebrick_stairs", GONDOR_COBBLEBRICK);

    // --- Slabs (properties and texture copied from the base block) ---
    public static final Block ANGMAR_BRICK_SLAB = registerSlab("angmar_brick_slab", ANGMAR_BRICK);
    public static final Block ANGMAR_CRACKED_BRICK_SLAB = registerSlab("angmar_cracked_brick_slab", ANGMAR_CRACKED_BRICK);
    public static final Block ANGMAR_PILLAR_SLAB = registerSlab("angmar_pillar_slab", ANGMAR_PILLAR);
    public static final Block ANGMAR_SNOW_BRICK_SLAB = registerSlab("angmar_snow_brick_slab", ANGMAR_SNOW_BRICK);
    public static final Block ARNOR_BRICK_SLAB = registerSlab("arnor_brick_slab", ARNOR_BRICK);
    public static final Block ARNOR_CRACKED_BRICK_SLAB = registerSlab("arnor_cracked_brick_slab", ARNOR_CRACKED_BRICK);
    public static final Block ARNOR_CRACKED_PILLAR_SLAB = registerSlab("arnor_cracked_pillar_slab", ARNOR_CRACKED_PILLAR);
    public static final Block ARNOR_MOSSY_BRICK_SLAB = registerSlab("arnor_mossy_brick_slab", ARNOR_MOSSY_BRICK);
    public static final Block ARNOR_PILLAR_SLAB = registerSlab("arnor_pillar_slab", ARNOR_PILLAR);
    public static final Block BLUE_ROCK_BRICK_SLAB = registerSlab("blue_rock_brick_slab", BLUE_ROCK_BRICK);
    public static final Block BLUE_ROCK_PILLAR_SLAB = registerSlab("blue_rock_pillar_slab", BLUE_ROCK_PILLAR);
    public static final Block BLUE_ROCK_SLAB = registerSlab("blue_rock_slab", BLUE_ROCK);
    public static final Block BRICK_PILLAR_SLAB = registerSlab("brick_pillar_slab", BRICK_PILLAR);
    public static final Block CHALK_BRICK_SLAB = registerSlab("chalk_brick_slab", CHALK_BRICK);
    public static final Block CHALK_PILLAR_SLAB = registerSlab("chalk_pillar_slab", CHALK_PILLAR);
    public static final Block CHALK_SLAB = registerSlab("chalk_slab", CHALK);
    public static final Block CLAY_TILE_DYED_BLACK_SLAB = registerSlab("clay_tile_dyed_black_slab", CLAY_TILE_DYED_BLACK);
    public static final Block CLAY_TILE_DYED_BLUE_SLAB = registerSlab("clay_tile_dyed_blue_slab", CLAY_TILE_DYED_BLUE);
    public static final Block CLAY_TILE_DYED_BROWN_SLAB = registerSlab("clay_tile_dyed_brown_slab", CLAY_TILE_DYED_BROWN);
    public static final Block CLAY_TILE_DYED_CYAN_SLAB = registerSlab("clay_tile_dyed_cyan_slab", CLAY_TILE_DYED_CYAN);
    public static final Block CLAY_TILE_DYED_GRAY_SLAB = registerSlab("clay_tile_dyed_gray_slab", CLAY_TILE_DYED_GRAY);
    public static final Block CLAY_TILE_DYED_GREEN_SLAB = registerSlab("clay_tile_dyed_green_slab", CLAY_TILE_DYED_GREEN);
    public static final Block CLAY_TILE_DYED_LIGHT_BLUE_SLAB = registerSlab("clay_tile_dyed_light_blue_slab", CLAY_TILE_DYED_LIGHT_BLUE);
    public static final Block CLAY_TILE_DYED_LIME_SLAB = registerSlab("clay_tile_dyed_lime_slab", CLAY_TILE_DYED_LIME);
    public static final Block CLAY_TILE_DYED_MAGENTA_SLAB = registerSlab("clay_tile_dyed_magenta_slab", CLAY_TILE_DYED_MAGENTA);
    public static final Block CLAY_TILE_DYED_ORANGE_SLAB = registerSlab("clay_tile_dyed_orange_slab", CLAY_TILE_DYED_ORANGE);
    public static final Block CLAY_TILE_DYED_PINK_SLAB = registerSlab("clay_tile_dyed_pink_slab", CLAY_TILE_DYED_PINK);
    public static final Block CLAY_TILE_DYED_PURPLE_SLAB = registerSlab("clay_tile_dyed_purple_slab", CLAY_TILE_DYED_PURPLE);
    public static final Block CLAY_TILE_DYED_RED_SLAB = registerSlab("clay_tile_dyed_red_slab", CLAY_TILE_DYED_RED);
    public static final Block CLAY_TILE_DYED_WHITE_SLAB = registerSlab("clay_tile_dyed_white_slab", CLAY_TILE_DYED_WHITE);
    public static final Block CLAY_TILE_DYED_YELLOW_SLAB = registerSlab("clay_tile_dyed_yellow_slab", CLAY_TILE_DYED_YELLOW);
    public static final Block CLAY_TILE_SLAB = registerSlab("clay_tile_slab", CLAY_TILE);
    public static final Block DALE_BRICK_SLAB = registerSlab("dale_brick_slab", DALE_BRICK);
    public static final Block DALE_CRACKED_BRICK_SLAB = registerSlab("dale_cracked_brick_slab", DALE_CRACKED_BRICK);
    public static final Block DALE_MOSSY_BRICK_SLAB = registerSlab("dale_mossy_brick_slab", DALE_MOSSY_BRICK);
    public static final Block DALE_PILLAR_SLAB = registerSlab("dale_pillar_slab", DALE_PILLAR);
    public static final Block DOL_AMROTH_BRICK_SLAB = registerSlab("dol_amroth_brick_slab", DOL_AMROTH_BRICK);
    public static final Block DOL_GULDUR_BRICK_SLAB = registerSlab("dol_guldur_brick_slab", DOL_GULDUR_BRICK);
    public static final Block DOL_GULDUR_CRACKED_BRICK_SLAB = registerSlab("dol_guldur_cracked_brick_slab", DOL_GULDUR_CRACKED_BRICK);
    public static final Block DOL_GULDUR_MOSSY_BRICK_SLAB = registerSlab("dol_guldur_mossy_brick_slab", DOL_GULDUR_MOSSY_BRICK);
    public static final Block DOL_GULDUR_PILLAR_SLAB = registerSlab("dol_guldur_pillar_slab", DOL_GULDUR_PILLAR);
    public static final Block DORWINION_BRICK_SLAB = registerSlab("dorwinion_brick_slab", DORWINION_BRICK);
    public static final Block DORWINION_CRACKED_BRICK_SLAB = registerSlab("dorwinion_cracked_brick_slab", DORWINION_CRACKED_BRICK);
    public static final Block DORWINION_FLOWERS_BRICK_SLAB = registerSlab("dorwinion_flowers_brick_slab", DORWINION_FLOWERS_BRICK);
    public static final Block DORWINION_MOSSY_BRICK_SLAB = registerSlab("dorwinion_mossy_brick_slab", DORWINION_MOSSY_BRICK);
    public static final Block DORWINION_MOSSY_PILLAR_SLAB = registerSlab("dorwinion_mossy_pillar_slab", DORWINION_MOSSY_PILLAR);
    public static final Block DORWINION_PILLAR_SLAB = registerSlab("dorwinion_pillar_slab", DORWINION_PILLAR);
    public static final Block DWARVEN_BRICK_SLAB = registerSlab("dwarven_brick_slab", DWARVEN_BRICK);
    public static final Block DWARVEN_CRACKED_BRICK_SLAB = registerSlab("dwarven_cracked_brick_slab", DWARVEN_CRACKED_BRICK);
    public static final Block DWARVEN_CRACKED_PILLAR_SLAB = registerSlab("dwarven_cracked_pillar_slab", DWARVEN_CRACKED_PILLAR);
    public static final Block DWARVEN_OBSIDIAN_BRICK_SLAB = registerSlab("dwarven_obsidian_brick_slab", DWARVEN_OBSIDIAN_BRICK);
    public static final Block DWARVEN_PILLAR_SLAB = registerSlab("dwarven_pillar_slab", DWARVEN_PILLAR);
    public static final Block GALADHRIM_BRICK_SLAB = registerSlab("galadhrim_brick_slab", GALADHRIM_BRICK);
    public static final Block GALADHRIM_CRACKED_BRICK_SLAB = registerSlab("galadhrim_cracked_brick_slab", GALADHRIM_CRACKED_BRICK);
    public static final Block GALADHRIM_CRACKED_PILLAR_SLAB = registerSlab("galadhrim_cracked_pillar_slab", GALADHRIM_CRACKED_PILLAR);
    public static final Block GALADHRIM_MOSSY_BRICK_SLAB = registerSlab("galadhrim_mossy_brick_slab", GALADHRIM_MOSSY_BRICK);
    public static final Block GALADHRIM_PILLAR_SLAB = registerSlab("galadhrim_pillar_slab", GALADHRIM_PILLAR);
    public static final Block GONDOR_BRICK_SLAB = registerSlab("gondor_brick_slab", GONDOR_BRICK);
    public static final Block GONDOR_CRACKED_BRICK_SLAB = registerSlab("gondor_cracked_brick_slab", GONDOR_CRACKED_BRICK);
    public static final Block GONDOR_MOSSY_BRICK_SLAB = registerSlab("gondor_mossy_brick_slab", GONDOR_MOSSY_BRICK);
    public static final Block GONDOR_PILLAR_SLAB = registerSlab("gondor_pillar_slab", GONDOR_PILLAR);
    public static final Block GONDOR_ROCK_SLAB = registerSlab("gondor_rock_slab", GONDOR_ROCK);
    public static final Block HIGH_ELVEN_BRICK_SLAB = registerSlab("high_elven_brick_slab", HIGH_ELVEN_BRICK);
    public static final Block HIGH_ELVEN_CRACKED_BRICK_SLAB = registerSlab("high_elven_cracked_brick_slab", HIGH_ELVEN_CRACKED_BRICK);
    public static final Block HIGH_ELVEN_CRACKED_PILLAR_SLAB = registerSlab("high_elven_cracked_pillar_slab", HIGH_ELVEN_CRACKED_PILLAR);
    public static final Block HIGH_ELVEN_MOSSY_BRICK_SLAB = registerSlab("high_elven_mossy_brick_slab", HIGH_ELVEN_MOSSY_BRICK);
    public static final Block HIGH_ELVEN_PILLAR_SLAB = registerSlab("high_elven_pillar_slab", HIGH_ELVEN_PILLAR);
    public static final Block MORDOR_BRICK_SLAB = registerSlab("mordor_brick_slab", MORDOR_BRICK);
    public static final Block MORDOR_CRACKED_BRICK_SLAB = registerSlab("mordor_cracked_brick_slab", MORDOR_CRACKED_BRICK);
    public static final Block MORDOR_DIRT_SLAB = registerSlab("mordor_dirt_slab", MORDOR_DIRT);
    public static final Block MORDOR_GRAVEL_SLAB = registerSlab("mordor_gravel_slab", MORDOR_GRAVEL);
    public static final Block MORDOR_PILLAR_SLAB = registerSlab("mordor_pillar_slab", MORDOR_PILLAR);
    public static final Block MORDOR_ROCK_SLAB = registerSlab("mordor_rock_slab", MORDOR_ROCK);
    public static final Block MORWAITH_CRACKED_BRICK_SLAB = registerSlab("morwaith_cracked_brick_slab", MORWAITH_CRACKED_BRICK);
    public static final Block MUD_BRICK_SLAB = registerSlab("mud_brick_slab", MUD_BRICK);
    public static final Block NEAR_HARAD_BRICK_SLAB = registerSlab("near_harad_brick_slab", NEAR_HARAD_BRICK);
    public static final Block NEAR_HARAD_CRACKED_BRICK_SLAB = registerSlab("near_harad_cracked_brick_slab", NEAR_HARAD_CRACKED_BRICK);
    public static final Block NEAR_HARAD_PILLAR_SLAB = registerSlab("near_harad_pillar_slab", NEAR_HARAD_PILLAR);
    public static final Block NEAR_HARAD_RED_BRICK_SLAB = registerSlab("near_harad_red_brick_slab", NEAR_HARAD_RED_BRICK);
    public static final Block NEAR_HARAD_RED_CRACKED_BRICK_SLAB = registerSlab("near_harad_red_cracked_brick_slab", NEAR_HARAD_RED_CRACKED_BRICK);
    public static final Block NEAR_HARAD_RED_PILLAR_SLAB = registerSlab("near_harad_red_pillar_slab", NEAR_HARAD_RED_PILLAR);
    public static final Block OBSIDIAN_GRAVEL_SLAB = registerSlab("obsidian_gravel_slab", OBSIDIAN_GRAVEL);
    public static final Block RED_ROCK_BRICK_SLAB = registerSlab("red_rock_brick_slab", RED_ROCK_BRICK);
    public static final Block RED_ROCK_PILLAR_SLAB = registerSlab("red_rock_pillar_slab", RED_ROCK_PILLAR);
    public static final Block RED_ROCK_SLAB = registerSlab("red_rock_slab", RED_ROCK);
    public static final Block RHUN_BRICK_SLAB = registerSlab("rhun_brick_slab", RHUN_BRICK);
    public static final Block RHUN_CRACKED_BRICK_SLAB = registerSlab("rhun_cracked_brick_slab", RHUN_CRACKED_BRICK);
    public static final Block RHUN_FLOWERS_BRICK_SLAB = registerSlab("rhun_flowers_brick_slab", RHUN_FLOWERS_BRICK);
    public static final Block RHUN_MOSSY_BRICK_SLAB = registerSlab("rhun_mossy_brick_slab", RHUN_MOSSY_BRICK);
    public static final Block RHUN_PILLAR_SLAB = registerSlab("rhun_pillar_slab", RHUN_PILLAR);
    public static final Block RHUN_RED_BRICK_SLAB = registerSlab("rhun_red_brick_slab", RHUN_RED_BRICK);
    public static final Block RHUN_RED_PILLAR_SLAB = registerSlab("rhun_red_pillar_slab", RHUN_RED_PILLAR);
    public static final Block ROHAN_BRICK_SLAB = registerSlab("rohan_brick_slab", ROHAN_BRICK);
    public static final Block ROHAN_PILLAR_SLAB = registerSlab("rohan_pillar_slab", ROHAN_PILLAR);
    public static final Block ROHAN_ROCK_SLAB = registerSlab("rohan_rock_slab", ROHAN_ROCK);
    public static final Block STONE_PILLAR_SLAB = registerSlab("stone_pillar_slab", STONE_PILLAR);
    public static final Block TAUREDAIN_BRICK_SLAB = registerSlab("tauredain_brick_slab", TAUREDAIN_BRICK);
    public static final Block TAUREDAIN_CRACKED_BRICK_SLAB = registerSlab("tauredain_cracked_brick_slab", TAUREDAIN_CRACKED_BRICK);
    public static final Block TAUREDAIN_GOLD_BRICK_SLAB = registerSlab("tauredain_gold_brick_slab", TAUREDAIN_GOLD_BRICK);
    public static final Block TAUREDAIN_MOSSY_BRICK_SLAB = registerSlab("tauredain_mossy_brick_slab", TAUREDAIN_MOSSY_BRICK);
    public static final Block TAUREDAIN_OBSIDIAN_BRICK_SLAB = registerSlab("tauredain_obsidian_brick_slab", TAUREDAIN_OBSIDIAN_BRICK);
    public static final Block TAUREDAIN_PILLAR_SLAB = registerSlab("tauredain_pillar_slab", TAUREDAIN_PILLAR);
    public static final Block THATCH_THATCH_SLAB = registerSlab("thatch_thatch_slab", THATCH_THATCH);
    public static final Block UMBAR_BRICK_SLAB = registerSlab("umbar_brick_slab", UMBAR_BRICK);
    public static final Block UMBAR_CRACKED_BRICK_SLAB = registerSlab("umbar_cracked_brick_slab", UMBAR_CRACKED_BRICK);
    public static final Block UMBAR_PILLAR_SLAB = registerSlab("umbar_pillar_slab", UMBAR_PILLAR);
    public static final Block URUK_BRICK_SLAB = registerSlab("uruk_brick_slab", URUK_BRICK);
    public static final Block URUK_PILLAR_SLAB = registerSlab("uruk_pillar_slab", URUK_PILLAR);
    public static final Block UTUMNO_FIRE_BRICK_SLAB = registerSlab("utumno_fire_brick_slab", UTUMNO_FIRE_BRICK);
    public static final Block UTUMNO_ICE_BRICK_SLAB = registerSlab("utumno_ice_brick_slab", UTUMNO_ICE_BRICK);
    public static final Block UTUMNO_OBSIDIAN_BRICK_SLAB = registerSlab("utumno_obsidian_brick_slab", UTUMNO_OBSIDIAN_BRICK);
    public static final Block WHITE_SAND_SLAB = registerSlab("white_sand_slab", WHITE_SAND);
    public static final Block WHITE_SANDSTONE_SLAB = registerSlab("white_sandstone_slab", WHITE_SANDSTONE);
    public static final Block WOOD_ELVEN_BRICK_SLAB = registerSlab("wood_elven_brick_slab", WOOD_ELVEN_BRICK);
    public static final Block WOOD_ELVEN_CRACKED_BRICK_SLAB = registerSlab("wood_elven_cracked_brick_slab", WOOD_ELVEN_CRACKED_BRICK);
    public static final Block WOOD_ELVEN_CRACKED_PILLAR_SLAB = registerSlab("wood_elven_cracked_pillar_slab", WOOD_ELVEN_CRACKED_PILLAR);
    public static final Block WOOD_ELVEN_MOSSY_BRICK_SLAB = registerSlab("wood_elven_mossy_brick_slab", WOOD_ELVEN_MOSSY_BRICK);
    public static final Block WOOD_ELVEN_PILLAR_SLAB = registerSlab("wood_elven_pillar_slab", WOOD_ELVEN_PILLAR);

    /**
     * Slab cut from an existing block. One modern SlabBlock replaces the
     * 1.7.10 single/double pair, since SlabType covers bottom, top and double.
     *
     * Like stairs, these must be declared AFTER every base block.
     */
    private static Block registerSlab(String name, Block base) {
        Block slab = register(name, SlabBlock::new,
                BlockBehaviour.Properties.ofFullCopy(base), true);
        ALL_SLABS.add(slab);
        SLAB_BASE.put(slab, base);
        return slab;
    }

    public static final Block RED_SANDSTONE_SLAB = registerSlab("red_sandstone_slab", RED_SANDSTONE);


    public static final Block SMOOTH_MORDOR_ROCK_SLAB = registerSlab("smooth_mordor_rock_slab", SMOOTH_MORDOR_ROCK);

    public static final Block SMOOTH_GONDOR_ROCK_SLAB = registerSlab("smooth_gondor_rock_slab", SMOOTH_GONDOR_ROCK);

    public static final Block SMOOTH_ROHAN_ROCK_SLAB = registerSlab("smooth_rohan_rock_slab", SMOOTH_ROHAN_ROCK);

    public static final Block SMOOTH_BLUE_ROCK_SLAB = registerSlab("smooth_blue_rock_slab", SMOOTH_BLUE_ROCK);

    public static final Block SMOOTH_RED_ROCK_SLAB = registerSlab("smooth_red_rock_slab", SMOOTH_RED_ROCK);

    public static final Block SMOOTH_CHALK_SLAB = registerSlab("smooth_chalk_slab", SMOOTH_CHALK);

    public static final Block GONDOR_COBBLEBRICK_SLAB = registerSlab("gondor_cobblebrick_slab", GONDOR_COBBLEBRICK);

    // --- Fences (properties and texture copied from the base block) ---
    public static final Block SHIRE_PINE_FENCE = registerFence("shire_pine_fence", SHIRE_PINE_PLANKS);
    public static final Block MALLORN_FENCE = registerFence("mallorn_fence", MALLORN_PLANKS);
    public static final Block MIRK_OAK_FENCE = registerFence("mirk_oak_fence", MIRK_OAK_PLANKS);
    public static final Block CHARRED_FENCE = registerFence("charred_fence", CHARRED_PLANKS);
    public static final Block APPLE_FENCE = registerFence("apple_fence", APPLE_PLANKS);
    public static final Block PEAR_FENCE = registerFence("pear_fence", PEAR_PLANKS);
    public static final Block CHERRY_FENCE = registerFence("cherry_fence", CHERRY_PLANKS);
    public static final Block MANGO_FENCE = registerFence("mango_fence", MANGO_PLANKS);
    public static final Block LEBETHRON_FENCE = registerFence("lebethron_fence", LEBETHRON_PLANKS);
    public static final Block BEECH_FENCE = registerFence("beech_fence", BEECH_PLANKS);
    public static final Block HOLLY_FENCE = registerFence("holly_fence", HOLLY_PLANKS);
    public static final Block BANANA_FENCE = registerFence("banana_fence", BANANA_PLANKS);
    public static final Block MAPLE_FENCE = registerFence("maple_fence", MAPLE_PLANKS);
    public static final Block LARCH_FENCE = registerFence("larch_fence", LARCH_PLANKS);
    public static final Block DATE_PALM_FENCE = registerFence("date_palm_fence", DATE_PALM_PLANKS);
    public static final Block MANGROVE_FENCE = registerFence("mangrove_fence", MANGROVE_PLANKS);
    public static final Block CHESTNUT_FENCE = registerFence("chestnut_fence", CHESTNUT_PLANKS);
    public static final Block BAOBAB_FENCE = registerFence("baobab_fence", BAOBAB_PLANKS);
    public static final Block CEDAR_FENCE = registerFence("cedar_fence", CEDAR_PLANKS);
    public static final Block FIR_FENCE = registerFence("fir_fence", FIR_PLANKS);
    public static final Block PINE_FENCE = registerFence("pine_fence", PINE_PLANKS);
    public static final Block LEMON_FENCE = registerFence("lemon_fence", LEMON_PLANKS);
    public static final Block ORANGE_FENCE = registerFence("orange_fence", ORANGE_PLANKS);
    public static final Block LIME_FENCE = registerFence("lime_fence", LIME_PLANKS);
    public static final Block MAHOGANY_FENCE = registerFence("mahogany_fence", MAHOGANY_PLANKS);
    public static final Block WILLOW_FENCE = registerFence("willow_fence", WILLOW_PLANKS);
    public static final Block CYPRESS_FENCE = registerFence("cypress_fence", CYPRESS_PLANKS);
    public static final Block OLIVE_FENCE = registerFence("olive_fence", OLIVE_PLANKS);
    public static final Block ASPEN_FENCE = registerFence("aspen_fence", ASPEN_PLANKS);
    public static final Block GREEN_OAK_FENCE = registerFence("green_oak_fence", GREEN_OAK_PLANKS);
    public static final Block ALMOND_FENCE = registerFence("almond_fence", ALMOND_PLANKS);
    public static final Block ROTTEN_FENCE = registerFence("rotten_fence", ROTTEN_PLANKS);
    public static final Block PLUM_FENCE = registerFence("plum_fence", PLUM_PLANKS);
    public static final Block REDWOOD_FENCE = registerFence("redwood_fence", REDWOOD_PLANKS);
    public static final Block POMEGRANATE_FENCE = registerFence("pomegranate_fence", POMEGRANATE_PLANKS);
    public static final Block PALM_FENCE = registerFence("palm_fence", PALM_PLANKS);
    public static final Block KANUKA_FENCE = registerFence("kanuka_fence", KANUKA_PLANKS);

    // --- Walls (properties and texture copied from the base block) ---
    public static final Block MORDOR_ROCK_WALL = registerWall("mordor_rock_wall", MORDOR_ROCK);
    public static final Block MORDOR_BRICK_WALL = registerWall("mordor_brick_wall", MORDOR_BRICK);
    public static final Block GONDOR_ROCK_WALL = registerWall("gondor_rock_wall", GONDOR_ROCK);
    public static final Block GONDOR_BRICK_WALL = registerWall("gondor_brick_wall", GONDOR_BRICK);
    public static final Block GONDOR_MOSSY_BRICK_WALL = registerWall("gondor_mossy_brick_wall", GONDOR_MOSSY_BRICK);
    public static final Block GONDOR_CRACKED_BRICK_WALL = registerWall("gondor_cracked_brick_wall", GONDOR_CRACKED_BRICK);
    public static final Block ROHAN_BRICK_WALL = registerWall("rohan_brick_wall", ROHAN_BRICK);
    public static final Block DWARVEN_BRICK_WALL = registerWall("dwarven_brick_wall", DWARVEN_BRICK);
    public static final Block ROHAN_ROCK_WALL = registerWall("rohan_rock_wall", ROHAN_ROCK);
    public static final Block MORDOR_CRACKED_BRICK_WALL = registerWall("mordor_cracked_brick_wall", MORDOR_CRACKED_BRICK);
    public static final Block GALADHRIM_BRICK_WALL = registerWall("galadhrim_brick_wall", GALADHRIM_BRICK);
    public static final Block GALADHRIM_MOSSY_BRICK_WALL = registerWall("galadhrim_mossy_brick_wall", GALADHRIM_MOSSY_BRICK);
    public static final Block GALADHRIM_CRACKED_BRICK_WALL = registerWall("galadhrim_cracked_brick_wall", GALADHRIM_CRACKED_BRICK);
    public static final Block BLUE_ROCK_WALL = registerWall("blue_rock_wall", BLUE_ROCK);
    public static final Block BLUE_ROCK_BRICK_WALL = registerWall("blue_rock_brick_wall", BLUE_ROCK_BRICK);
    public static final Block NEAR_HARAD_BRICK_WALL = registerWall("near_harad_brick_wall", NEAR_HARAD_BRICK);
    public static final Block ANGMAR_BRICK_WALL = registerWall("angmar_brick_wall", ANGMAR_BRICK);
    public static final Block ANGMAR_CRACKED_BRICK_WALL = registerWall("angmar_cracked_brick_wall", ANGMAR_CRACKED_BRICK);
    public static final Block RED_ROCK_WALL = registerWall("red_rock_wall", RED_ROCK);
    public static final Block RED_ROCK_BRICK_WALL = registerWall("red_rock_brick_wall", RED_ROCK_BRICK);
    public static final Block ARNOR_BRICK_WALL = registerWall("arnor_brick_wall", ARNOR_BRICK);
    public static final Block ARNOR_MOSSY_BRICK_WALL = registerWall("arnor_mossy_brick_wall", ARNOR_MOSSY_BRICK);
    public static final Block ARNOR_CRACKED_BRICK_WALL = registerWall("arnor_cracked_brick_wall", ARNOR_CRACKED_BRICK);
    public static final Block URUK_BRICK_WALL = registerWall("uruk_brick_wall", URUK_BRICK);
    public static final Block DOL_GULDUR_BRICK_WALL = registerWall("dol_guldur_brick_wall", DOL_GULDUR_BRICK);
    public static final Block DOL_GULDUR_CRACKED_BRICK_WALL = registerWall("dol_guldur_cracked_brick_wall", DOL_GULDUR_CRACKED_BRICK);
    public static final Block HIGH_ELVEN_BRICK_WALL = registerWall("high_elven_brick_wall", HIGH_ELVEN_BRICK);
    public static final Block HIGH_ELVEN_MOSSY_BRICK_WALL = registerWall("high_elven_mossy_brick_wall", HIGH_ELVEN_MOSSY_BRICK);
    public static final Block HIGH_ELVEN_CRACKED_BRICK_WALL = registerWall("high_elven_cracked_brick_wall", HIGH_ELVEN_CRACKED_BRICK);
    public static final Block DOL_AMROTH_BRICK_WALL = registerWall("dol_amroth_brick_wall", DOL_AMROTH_BRICK);
    public static final Block NEAR_HARAD_CRACKED_BRICK_WALL = registerWall("near_harad_cracked_brick_wall", NEAR_HARAD_CRACKED_BRICK);
    public static final Block NEAR_HARAD_RED_BRICK_WALL = registerWall("near_harad_red_brick_wall", NEAR_HARAD_RED_BRICK);
    public static final Block NEAR_HARAD_RED_CRACKED_BRICK_WALL = registerWall("near_harad_red_cracked_brick_wall", NEAR_HARAD_RED_CRACKED_BRICK);
    public static final Block CHALK_WALL = registerWall("chalk_wall", CHALK);
    public static final Block CHALK_BRICK_WALL = registerWall("chalk_brick_wall", CHALK_BRICK);
    public static final Block MUD_BRICK_WALL = registerWall("mud_brick_wall", MUD_BRICK);
    public static final Block DALE_BRICK_WALL = registerWall("dale_brick_wall", DALE_BRICK);
    public static final Block DORWINION_BRICK_WALL = registerWall("dorwinion_brick_wall", DORWINION_BRICK);
    public static final Block DORWINION_MOSSY_BRICK_WALL = registerWall("dorwinion_mossy_brick_wall", DORWINION_MOSSY_BRICK);
    public static final Block DORWINION_CRACKED_BRICK_WALL = registerWall("dorwinion_cracked_brick_wall", DORWINION_CRACKED_BRICK);
    public static final Block DORWINION_FLOWERS_BRICK_WALL = registerWall("dorwinion_flowers_brick_wall", DORWINION_FLOWERS_BRICK);
    public static final Block WHITE_SANDSTONE_WALL = registerWall("white_sandstone_wall", WHITE_SANDSTONE);
    public static final Block RHUN_BRICK_WALL = registerWall("rhun_brick_wall", RHUN_BRICK);
    public static final Block TAUREDAIN_BRICK_WALL = registerWall("tauredain_brick_wall", TAUREDAIN_BRICK);
    public static final Block TAUREDAIN_MOSSY_BRICK_WALL = registerWall("tauredain_mossy_brick_wall", TAUREDAIN_MOSSY_BRICK);
    public static final Block TAUREDAIN_CRACKED_BRICK_WALL = registerWall("tauredain_cracked_brick_wall", TAUREDAIN_CRACKED_BRICK);
    public static final Block TAUREDAIN_GOLD_BRICK_WALL = registerWall("tauredain_gold_brick_wall", TAUREDAIN_GOLD_BRICK);
    public static final Block TAUREDAIN_OBSIDIAN_BRICK_WALL = registerWall("tauredain_obsidian_brick_wall", TAUREDAIN_OBSIDIAN_BRICK);
    public static final Block DWARVEN_CRACKED_BRICK_WALL = registerWall("dwarven_cracked_brick_wall", DWARVEN_CRACKED_BRICK);
    public static final Block DWARVEN_OBSIDIAN_BRICK_WALL = registerWall("dwarven_obsidian_brick_wall", DWARVEN_OBSIDIAN_BRICK);
    public static final Block RHUN_MOSSY_BRICK_WALL = registerWall("rhun_mossy_brick_wall", RHUN_MOSSY_BRICK);
    public static final Block RHUN_CRACKED_BRICK_WALL = registerWall("rhun_cracked_brick_wall", RHUN_CRACKED_BRICK);
    public static final Block RHUN_FLOWERS_BRICK_WALL = registerWall("rhun_flowers_brick_wall", RHUN_FLOWERS_BRICK);
    public static final Block RHUN_RED_BRICK_WALL = registerWall("rhun_red_brick_wall", RHUN_RED_BRICK);
    public static final Block DALE_MOSSY_BRICK_WALL = registerWall("dale_mossy_brick_wall", DALE_MOSSY_BRICK);
    public static final Block DALE_CRACKED_BRICK_WALL = registerWall("dale_cracked_brick_wall", DALE_CRACKED_BRICK);
    public static final Block RED_SANDSTONE_WALL = registerWall("red_sandstone_wall", RED_SANDSTONE);
    public static final Block UTUMNO_FIRE_BRICK_WALL = registerWall("utumno_fire_brick_wall", UTUMNO_FIRE_BRICK);
    public static final Block UTUMNO_ICE_BRICK_WALL = registerWall("utumno_ice_brick_wall", UTUMNO_ICE_BRICK);
    public static final Block UTUMNO_OBSIDIAN_BRICK_WALL = registerWall("utumno_obsidian_brick_wall", UTUMNO_OBSIDIAN_BRICK);
    public static final Block CLAY_TILE_DYED_WHITE_WALL = registerWall("clay_tile_dyed_white_wall", CLAY_TILE_DYED_WHITE);
    public static final Block CLAY_TILE_DYED_ORANGE_WALL = registerWall("clay_tile_dyed_orange_wall", CLAY_TILE_DYED_ORANGE);
    public static final Block CLAY_TILE_DYED_MAGENTA_WALL = registerWall("clay_tile_dyed_magenta_wall", CLAY_TILE_DYED_MAGENTA);
    public static final Block CLAY_TILE_DYED_LIGHT_BLUE_WALL = registerWall("clay_tile_dyed_light_blue_wall", CLAY_TILE_DYED_LIGHT_BLUE);
    public static final Block CLAY_TILE_DYED_YELLOW_WALL = registerWall("clay_tile_dyed_yellow_wall", CLAY_TILE_DYED_YELLOW);
    public static final Block CLAY_TILE_DYED_LIME_WALL = registerWall("clay_tile_dyed_lime_wall", CLAY_TILE_DYED_LIME);
    public static final Block CLAY_TILE_DYED_PINK_WALL = registerWall("clay_tile_dyed_pink_wall", CLAY_TILE_DYED_PINK);
    public static final Block CLAY_TILE_DYED_GRAY_WALL = registerWall("clay_tile_dyed_gray_wall", CLAY_TILE_DYED_GRAY);
    public static final Block CLAY_TILE_DYED_CYAN_WALL = registerWall("clay_tile_dyed_cyan_wall", CLAY_TILE_DYED_CYAN);
    public static final Block CLAY_TILE_DYED_PURPLE_WALL = registerWall("clay_tile_dyed_purple_wall", CLAY_TILE_DYED_PURPLE);
    public static final Block CLAY_TILE_DYED_BLUE_WALL = registerWall("clay_tile_dyed_blue_wall", CLAY_TILE_DYED_BLUE);
    public static final Block CLAY_TILE_DYED_BROWN_WALL = registerWall("clay_tile_dyed_brown_wall", CLAY_TILE_DYED_BROWN);
    public static final Block CLAY_TILE_DYED_GREEN_WALL = registerWall("clay_tile_dyed_green_wall", CLAY_TILE_DYED_GREEN);
    public static final Block CLAY_TILE_DYED_RED_WALL = registerWall("clay_tile_dyed_red_wall", CLAY_TILE_DYED_RED);
    public static final Block CLAY_TILE_DYED_BLACK_WALL = registerWall("clay_tile_dyed_black_wall", CLAY_TILE_DYED_BLACK);
    public static final Block UMBAR_BRICK_WALL = registerWall("umbar_brick_wall", UMBAR_BRICK);
    public static final Block UMBAR_CRACKED_BRICK_WALL = registerWall("umbar_cracked_brick_wall", UMBAR_CRACKED_BRICK);
    public static final Block ANGMAR_SNOW_BRICK_WALL = registerWall("angmar_snow_brick_wall", ANGMAR_SNOW_BRICK);
    public static final Block DOL_GULDUR_MOSSY_BRICK_WALL = registerWall("dol_guldur_mossy_brick_wall", DOL_GULDUR_MOSSY_BRICK);
    public static final Block MORWAITH_CRACKED_BRICK_WALL = registerWall("morwaith_cracked_brick_wall", MORWAITH_CRACKED_BRICK);

    /** Fence cut from an existing block. Must be declared AFTER every base. */
    private static Block registerFence(String name, Block base) {
        Block fence = register(name, FenceBlock::new,
                BlockBehaviour.Properties.ofFullCopy(base), true);
        ALL_FENCES.add(fence);
        FENCE_BASE.put(fence, base);
        return fence;
    }

    /** Wall cut from an existing block. Must be declared AFTER every base. */
    private static Block registerWall(String name, Block base) {
        Block wall = register(name, WallBlock::new,
                BlockBehaviour.Properties.ofFullCopy(base), true);
        ALL_WALLS.add(wall);
        WALL_BASE.put(wall, base);
        return wall;
    }
    public static final Block GONDOR_COBBLEBRICK_WALL = registerWall("gondor_cobblebrick_wall", GONDOR_COBBLEBRICK);

    // --- Wooden slabs ---
    public static final Block CHERRY_SLAB = registerSlab("cherry_slab", CHERRY_PLANKS);
    public static final Block ALMOND_SLAB = registerSlab("almond_slab", ALMOND_PLANKS);
    public static final Block APPLE_SLAB = registerSlab("apple_slab", APPLE_PLANKS);
    public static final Block ASPEN_SLAB = registerSlab("aspen_slab", ASPEN_PLANKS);
    public static final Block BANANA_SLAB = registerSlab("banana_slab", BANANA_PLANKS);
    public static final Block BAOBAB_SLAB = registerSlab("baobab_slab", BAOBAB_PLANKS);
    public static final Block BEECH_SLAB = registerSlab("beech_slab", BEECH_PLANKS);
    public static final Block CEDAR_SLAB = registerSlab("cedar_slab", CEDAR_PLANKS);
    public static final Block CHARRED_SLAB = registerSlab("charred_slab", CHARRED_PLANKS);
    public static final Block CHESTNUT_SLAB = registerSlab("chestnut_slab", CHESTNUT_PLANKS);
    public static final Block CYPRESS_SLAB = registerSlab("cypress_slab", CYPRESS_PLANKS);
    public static final Block DATE_PALM_SLAB = registerSlab("date_palm_slab", DATE_PALM_PLANKS);
    public static final Block DRAGON_SLAB = registerSlab("dragon_slab", DRAGON_PLANKS);
    public static final Block FIR_SLAB = registerSlab("fir_slab", FIR_PLANKS);
    public static final Block GREEN_OAK_SLAB = registerSlab("green_oak_slab", GREEN_OAK_PLANKS);
    public static final Block HOLLY_SLAB = registerSlab("holly_slab", HOLLY_PLANKS);
    public static final Block KANUKA_SLAB = registerSlab("kanuka_slab", KANUKA_PLANKS);
    public static final Block LAIRELOSSE_SLAB = registerSlab("lairelosse_slab", LAIRELOSSE_PLANKS);
    public static final Block LARCH_SLAB = registerSlab("larch_slab", LARCH_PLANKS);
    public static final Block LEBETHRON_SLAB = registerSlab("lebethron_slab", LEBETHRON_PLANKS);
    public static final Block LEMON_SLAB = registerSlab("lemon_slab", LEMON_PLANKS);
    public static final Block LIME_SLAB = registerSlab("lime_slab", LIME_PLANKS);
    public static final Block MAHOGANY_SLAB = registerSlab("mahogany_slab", MAHOGANY_PLANKS);
    public static final Block MALLORN_SLAB = registerSlab("mallorn_slab", MALLORN_PLANKS);
    public static final Block MANGO_SLAB = registerSlab("mango_slab", MANGO_PLANKS);
    public static final Block MANGROVE_SLAB = registerSlab("mangrove_slab", MANGROVE_PLANKS);
    public static final Block MAPLE_SLAB = registerSlab("maple_slab", MAPLE_PLANKS);
    public static final Block MIRK_OAK_SLAB = registerSlab("mirk_oak_slab", MIRK_OAK_PLANKS);
    public static final Block OLIVE_SLAB = registerSlab("olive_slab", OLIVE_PLANKS);
    public static final Block ORANGE_SLAB = registerSlab("orange_slab", ORANGE_PLANKS);
    public static final Block PALM_SLAB = registerSlab("palm_slab", PALM_PLANKS);
    public static final Block PEAR_SLAB = registerSlab("pear_slab", PEAR_PLANKS);
    public static final Block PINE_SLAB = registerSlab("pine_slab", PINE_PLANKS);
    public static final Block PLUM_SLAB = registerSlab("plum_slab", PLUM_PLANKS);
    public static final Block POMEGRANATE_SLAB = registerSlab("pomegranate_slab", POMEGRANATE_PLANKS);
    public static final Block REDWOOD_SLAB = registerSlab("redwood_slab", REDWOOD_PLANKS);
    public static final Block ROTTEN_SLAB = registerSlab("rotten_slab", ROTTEN_PLANKS);
    public static final Block SHIRE_PINE_SLAB = registerSlab("shire_pine_slab", SHIRE_PINE_PLANKS);
    public static final Block WILLOW_SLAB = registerSlab("willow_slab", WILLOW_PLANKS);
    // --- Fence gates (declared last: they reference the plank blocks) ---
    public static final Block ALMOND_FENCE_GATE = registerFenceGate("almond_fence_gate", ALMOND_PLANKS);
    public static final Block APPLE_FENCE_GATE = registerFenceGate("apple_fence_gate", APPLE_PLANKS);
    public static final Block ASPEN_FENCE_GATE = registerFenceGate("aspen_fence_gate", ASPEN_PLANKS);
    public static final Block BANANA_FENCE_GATE = registerFenceGate("banana_fence_gate", BANANA_PLANKS);
    public static final Block BAOBAB_FENCE_GATE = registerFenceGate("baobab_fence_gate", BAOBAB_PLANKS);
    public static final Block BEECH_FENCE_GATE = registerFenceGate("beech_fence_gate", BEECH_PLANKS);
    public static final Block CEDAR_FENCE_GATE = registerFenceGate("cedar_fence_gate", CEDAR_PLANKS);
    public static final Block CHARRED_FENCE_GATE = registerFenceGate("charred_fence_gate", CHARRED_PLANKS);
    public static final Block CHERRY_FENCE_GATE = registerFenceGate("cherry_fence_gate", CHERRY_PLANKS);
    public static final Block CHESTNUT_FENCE_GATE = registerFenceGate("chestnut_fence_gate", CHESTNUT_PLANKS);
    public static final Block CYPRESS_FENCE_GATE = registerFenceGate("cypress_fence_gate", CYPRESS_PLANKS);
    public static final Block DATE_PALM_FENCE_GATE = registerFenceGate("date_palm_fence_gate", DATE_PALM_PLANKS);
    public static final Block DRAGON_FENCE_GATE = registerFenceGate("dragon_fence_gate", DRAGON_PLANKS);
    public static final Block FIR_FENCE_GATE = registerFenceGate("fir_fence_gate", FIR_PLANKS);
    public static final Block GREEN_OAK_FENCE_GATE = registerFenceGate("green_oak_fence_gate", GREEN_OAK_PLANKS);
    public static final Block HOLLY_FENCE_GATE = registerFenceGate("holly_fence_gate", HOLLY_PLANKS);
    public static final Block KANUKA_FENCE_GATE = registerFenceGate("kanuka_fence_gate", KANUKA_PLANKS);
    public static final Block LAIRELOSSE_FENCE_GATE = registerFenceGate("lairelosse_fence_gate", LAIRELOSSE_PLANKS);
    public static final Block LARCH_FENCE_GATE = registerFenceGate("larch_fence_gate", LARCH_PLANKS);
    public static final Block LEBETHRON_FENCE_GATE = registerFenceGate("lebethron_fence_gate", LEBETHRON_PLANKS);
    public static final Block LEMON_FENCE_GATE = registerFenceGate("lemon_fence_gate", LEMON_PLANKS);
    public static final Block LIME_FENCE_GATE = registerFenceGate("lime_fence_gate", LIME_PLANKS);
    public static final Block MAHOGANY_FENCE_GATE = registerFenceGate("mahogany_fence_gate", MAHOGANY_PLANKS);
    public static final Block MALLORN_FENCE_GATE = registerFenceGate("mallorn_fence_gate", MALLORN_PLANKS);
    public static final Block MANGO_FENCE_GATE = registerFenceGate("mango_fence_gate", MANGO_PLANKS);
    public static final Block MANGROVE_FENCE_GATE = registerFenceGate("mangrove_fence_gate", MANGROVE_PLANKS);
    public static final Block MAPLE_FENCE_GATE = registerFenceGate("maple_fence_gate", MAPLE_PLANKS);
    public static final Block MIRK_OAK_FENCE_GATE = registerFenceGate("mirk_oak_fence_gate", MIRK_OAK_PLANKS);
    public static final Block OLIVE_FENCE_GATE = registerFenceGate("olive_fence_gate", OLIVE_PLANKS);
    public static final Block ORANGE_FENCE_GATE = registerFenceGate("orange_fence_gate", ORANGE_PLANKS);
    public static final Block PALM_FENCE_GATE = registerFenceGate("palm_fence_gate", PALM_PLANKS);
    public static final Block PEAR_FENCE_GATE = registerFenceGate("pear_fence_gate", PEAR_PLANKS);
    public static final Block PINE_FENCE_GATE = registerFenceGate("pine_fence_gate", PINE_PLANKS);
    public static final Block PLUM_FENCE_GATE = registerFenceGate("plum_fence_gate", PLUM_PLANKS);
    public static final Block POMEGRANATE_FENCE_GATE = registerFenceGate("pomegranate_fence_gate", POMEGRANATE_PLANKS);
    public static final Block REDWOOD_FENCE_GATE = registerFenceGate("redwood_fence_gate", REDWOOD_PLANKS);
    public static final Block ROTTEN_FENCE_GATE = registerFenceGate("rotten_fence_gate", ROTTEN_PLANKS);
    public static final Block SHIRE_PINE_FENCE_GATE = registerFenceGate("shire_pine_fence_gate", SHIRE_PINE_PLANKS);
    public static final Block WILLOW_FENCE_GATE = registerFenceGate("willow_fence_gate", WILLOW_PLANKS);

    private LOTRBlocks() {
    }

    // ------------------------------------------------------------------
    // Family helpers
    // ------------------------------------------------------------------

    /**
     * Full cube with a distinct top texture (smooth stone and friends).
     * Uses <name>_side and <name>_top, like the column blocks, but has no axis
     * property -- the original was a plain block, not a rotatable pillar.
     */
    private static Block registerColumn(String name) {
        return track(ALL_COLUMNS, register(name, Block::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.STONE)
                        .requiresCorrectToolForDrops()
                        .strength(1.5f, 6.0f)
                        .sound(SoundType.STONE),
                true));
    }

    /**
     * Carpet: one pixel tall, needs a block beneath it. Withered moss and
     * thatch flooring both set 1/16 bounds in 1.7.10 rather than being full
     * cubes, which is what CarpetBlock reproduces.
     */
    /**
     * Wall-mounted torch. Registered without an item: a torch drops and places
     * as the standing block, and vanilla does the same for its wall variants.
     * Declared BEFORE its standing counterpart, which references it.
     */
    private static Block registerWallTorch(String name) {
        return register(name, props -> new WallTorchBlock(ParticleTypes.FLAME, props),
                BlockBehaviour.Properties.of()
                        .noCollision()
                        .instabreak()
                        .lightLevel(state -> 14)
                        .sound(SoundType.WOOD)
                        .pushReaction(PushReaction.DESTROY),
                false);
    }

    /**
     * Standing torch. Light level 14 matches the original's setLightLevel(0.875f).
     */
    private static Block registerTorch(String name, Block wallVariant) {
        Block torch = register(name, props -> new TorchBlock(ParticleTypes.FLAME, props),
                BlockBehaviour.Properties.of()
                        .noCollision()
                        .instabreak()
                        .lightLevel(state -> 14)
                        .sound(SoundType.WOOD)
                        .pushReaction(PushReaction.DESTROY),
                true);
        ALL_TORCHES.add(torch);
        TORCH_WALL.put(torch, wallVariant);
        return torch;
    }

    /**
     * Faction crafting table.
     *
     * Uses vanilla CraftingTableBlock, so these open the normal 3x3 grid. The
     * 1.7.10 originals each had their own faction recipe list and a bespoke
     * menu; that needs a block entity and a custom screen, so it is deferred --
     * these are functional tables with the right art in the meantime.
     */
    private static Block registerCraftingTable(String name) {
        return track(ALL_CRAFTING_TABLES, register(name, CraftingTableBlock::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.WOOD)
                        .instrument(NoteBlockInstrument.BASS)
                        .strength(2.5f)
                        .sound(SoundType.WOOD)
                        .ignitedByLava(),
                true));
    }

    /** Climbable vine that spreads across block faces, like vanilla vines. */
    private static Block registerVine(String name) {
        return track(ALL_VINES, register(name, VineBlock::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.PLANT)
                        .noCollision()
                        .randomTicks()
                        .strength(0.2f)
                        .sound(SoundType.VINE)
                        .ignitedByLava()
                        .pushReaction(PushReaction.DESTROY),
                true));
    }

    /** Ladder / climbable rope. */
    private static Block registerLadder(String name) {
        return track(ALL_LADDERS, register(name, LadderBlock::new,
                BlockBehaviour.Properties.of()
                        .forceSolidOff()
                        .strength(0.4f)
                        .sound(SoundType.LADDER)
                        .noOcclusion()
                        .pushReaction(PushReaction.DESTROY),
                true));
    }

    /**
     * Faction gate.
     *
     * Registered as a plain solid block for now. In 1.7.10 these were
     * openable (a whole gate slid away when powered or clicked) and used a
     * thirteen-piece connected texture like the mithril block. Both the
     * open/close behaviour and the connected border are deferred; this gets
     * the block and its art in place.
     */
    private static Block registerGate(String name) {
        return track(ALL_GATES, register(name, Block::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.METAL)
                        .requiresCorrectToolForDrops()
                        .strength(5.0f, 6.0f)
                        .sound(SoundType.METAL),
                true));
    }

    /**
     * Berry bush. The 1.7.10 block packed a bare and a fruited state into one
     * Block via metadata; only the fruited form is registered here, as a plain
     * bush. Growth and harvesting need a random-tick age property, deferred.
     */
    /** Fence gate. Uses the same plank texture as the matching fence. */
    private static Block registerFenceGate(String name, Block base) {
        return track(ALL_FENCE_GATES, register(name, props -> new FenceGateBlock(net.minecraft.world.level.block.state.properties.WoodType.OAK, props),
                BlockBehaviour.Properties.ofFullCopy(base), true));
    }

    /** Stone button. Reuses the base block's texture (no separate art). */
    private static Block registerButton(String name, Block base) {
        return track(ALL_BUTTONS, register(name,
                props -> new ButtonBlock(BlockSetType.STONE, 20, props),
                BlockBehaviour.Properties.ofFullCopy(base).noCollision().strength(0.5f),
                true));
    }

    /** Stone pressure plate. Reuses the base block's texture. */
    private static Block registerPressurePlate(String name, Block base) {
        return track(ALL_PRESSURE_PLATES, register(name,
                props -> new PressurePlateBlock(BlockSetType.STONE, props),
                BlockBehaviour.Properties.ofFullCopy(base).noCollision().strength(0.5f),
                true));
    }

    private static Block registerBush(String name) {
        return track(ALL_BUSHES, register(name, Block::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.PLANT)
                        .noCollision()
                        .strength(0.2f)
                        .sound(SoundType.SWEET_BERRY_BUSH)
                        .noOcclusion()
                        .pushReaction(PushReaction.DESTROY),
                true));
    }

    /** Web: slows entities to a crawl, like a cobweb. */
    private static Block registerWeb(String name) {
        return register(name, Block::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.WOOL)
                        .noCollision()
                        .strength(4.0f)
                        .noOcclusion()
                        .pushReaction(PushReaction.DESTROY),
                true);
    }

    private static Block registerCarpet(String name, float hardness) {
        return track(ALL_CARPETS, register(name, CarpetBlock::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.PLANT)
                        .strength(hardness)
                        .sound(SoundType.GRASS),
                true));
    }

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

    /**
     * Full cube that needs no particular tool.
     *
     * Mud, thatch, coral reef, remains and the like set a hardness in 1.7.10
     * but never called setHarvestLevel, so they broke with anything and always
     * dropped. Reproduced here by omitting requiresCorrectToolForDrops and
     * keeping them out of the tier lists, so they pick up no mineable tag.
     */
    private static Block registerSoftBlock(String name, float hardness, SoundType sound) {
        return track(ALL_CUBES, register(name, Block::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.STONE)
                        .strength(hardness)
                        .sound(sound),
                true));
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

    /**
     * Log / beam: axis-rotatable column. Textures are <name> for the bark side
     * and <name>_top for the cut end, which is what TexturedModel.COLUMN looks
     * for. The original's 1.7.10 wood blocks packed four species into one Block
     * via metadata; each becomes its own block here.
     */
    private static Block registerLog(String name) {
        return track(ALL_LOGS, register(name, RotatedPillarBlock::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.WOOD)
                        .instrument(NoteBlockInstrument.BASS)
                        .strength(2.0f)
                        .sound(SoundType.WOOD)
                        .ignitedByLava(),
                true));
    }

    /** Decorative wooden beam. Same shape and properties as a log. */
    private static Block registerBeam(String name) {
        return track(ALL_BEAMS, register(name, RotatedPillarBlock::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.WOOD)
                        .instrument(NoteBlockInstrument.BASS)
                        .strength(2.0f)
                        .sound(SoundType.WOOD)
                        .ignitedByLava(),
                true));
    }

    /**
     * Stone pillar. The original shipped five textures per pillar (face, side,
     * plus sideTop/sideMiddle/sideBottom for a connected column); only face and
     * side are used here, as the modern column model takes just an end and a
     * side. The connected variants would need the same treatment as the
     * mithril block's connected border.
     */
    private static Block registerPillar(String name) {
        return track(ALL_PILLARS, register(name, RotatedPillarBlock::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.STONE)
                        .requiresCorrectToolForDrops()
                        .strength(1.5f, 6.0f)
                        .sound(SoundType.STONE),
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

    /**
     * Flower / small plant: cross model, no collision, instant break.
     *
     * Matches 1.7.10 LOTRBlockFlower, which was a vanilla BlockBush with
     * hardness 0 and the grass step sound. The original's per-flower
     * setFlowerBounds() calls only shrank the selection box a little and are
     * not reproduced -- vanilla's own flowers all share one box.
     *
     * The metadata-variant blocks of the original (LOTRBlockHaradFlower with
     * four variants, LOTRBlockRhunFlower with five) become one block each here,
     * which is the modern idiom.
     */
    private static Block registerFlower(String name) {
        return track(ALL_FLOWERS, register(name, Block::new,
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
        BLOCK_KEYS.put(block, blockKey);
        if (withItem) {
            ALL_BLOCKS.add(block);
        }

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