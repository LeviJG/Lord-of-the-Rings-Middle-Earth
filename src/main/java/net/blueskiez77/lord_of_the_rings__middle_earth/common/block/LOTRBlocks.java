package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTREntityTags;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRAnimalJarItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.recipe.LOTRCraftingTable;

import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ColorRGBA;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.PlaceOnWaterBlockItem;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.network.chat.Component;
import net.minecraft.core.component.DataComponents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRTreasurePileItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItems;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WebBlock;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.CarpetBlock;
import net.minecraft.world.level.block.ColoredFallingBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.DirtPathBlock;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.UntintedParticleLeavesBlock;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class LOTRBlocks {
    // Mining tier, i.e. which needs_*_tool tag the block joins. NONE means pickaxe-mineable with no needs_* tag at all -- a wooden pickaxe suffices. That is what vanilla stone, stone bricks and bone blocks use, and it also matches the original: only sixteen blocks in 1.7.10 ever called setHarvestLevel, and everything else defaulted to level 0. Rock and brick families therefore take NONE, not STONE.
    public enum Tier {
        NONE, STONE, IRON
    }

    // These MUST stay above the first block field. Java runs static

    public static final List<Block> ALL_BLOCKS = new ArrayList<>();

    public static final List<Block> ALL_CUBES = new ArrayList<>();
    public static final List<Block> CUBES_STONE_TIER = new ArrayList<>();
    public static final List<Block> CUBES_IRON_TIER = new ArrayList<>();
    // Pickaxe-mineable, but no needs_*_tool tag: a wooden pickaxe works. */
    public static final List<Block> CUBES_NO_TIER = new ArrayList<>();
    /** Ores whose loot table is an item, not the block: see registerOre. */
    public static final List<Block> ORES_WITH_ITEM_DROPS = new ArrayList<>();
    public static final List<Block> ALL_PLANKS = new ArrayList<>();
    public static final List<Block> ALL_LEAVES = new ArrayList<>();
    public static final List<Block> ALL_SAPLINGS = new ArrayList<>();

    // Which sapling a leaf block drops. Every LOTR leaf has a sapling of the
    // same species, so the pairing is by name: <x>_leaves -> <x>_sapling.
    // Filled by pairLeavesWithSaplings() once both lists are populated.
    public static final Map<Block, Block> LEAVES_SAPLING = new LinkedHashMap<>();
    public static final List<Block> ALL_TRAPDOORS = new ArrayList<>();
    public static final List<Block> ALL_DOORS = new ArrayList<>();
    public static final List<Block> ALL_BARS = new ArrayList<>();
    public static final List<Block> ALL_GLASS_PANES = new ArrayList<>();

    // Which glass block a pane wears the texture of.
    public static final Map<Block, Block> GLASS_PANE_BASE = new LinkedHashMap<>();
    public static final List<Block> ALL_CHANDELIERS = new ArrayList<>();
    public static final List<Block> ALL_GLASS = new ArrayList<>();
    public static final List<Block> ALL_FLOWERS = new ArrayList<>();
    public static final List<Block> ALL_COLUMNS = new ArrayList<>();
    public static final List<Block> ALL_CARPETS = new ArrayList<>();
    public static final List<Block> ALL_PATHS = new ArrayList<>();
    public static final List<Block> ALL_FARMLAND = new ArrayList<>();
    public static final List<Block> ALL_RAILS = new ArrayList<>();
    public static final List<Block> ALL_FALLING = new ArrayList<>();

    public static final List<Block> SHOVEL_MINEABLE = new ArrayList<>();

    public static final List<Block> ALL_SOIL_COLUMNS = new ArrayList<>();

    public static final Map<Block, Block> SOIL_COLUMN_TEXTURE = new LinkedHashMap<>();

    public static final Map<Block, Block> TEXTURE_SOURCE = new LinkedHashMap<>();

    public static final List<Block> CUBES_COLUMN_TEXTURED = new ArrayList<>();
    /** Gulduril bricks. They carry a block entity purely to drive the glow renderer. */
    public static final List<Block> ALL_GULDURIL = new ArrayList<>();
    /** Dart traps. Face texture on the facing side, base brick on the other five. */
    public static final List<Block> ALL_DART_TRAPS = new ArrayList<>();
    /** Forges and ovens: <name>_front / _side / _top, plus _active when lit. */
    public static final List<Block> ALL_FORGES = new ArrayList<>();
    /** Dart trap -> the brick it borrows its five plain faces from. */
    public static final Map<Block, Block> DART_TRAP_BASE = new LinkedHashMap<>();

    public static final List<Block> ALL_BOTTOM_TOP = new ArrayList<>();
    public static final List<Block> ALL_TORCHES = new ArrayList<>();
    public static final List<Block> ALL_DOUBLE_TORCHES = new ArrayList<>();
    public static final List<Block> ALL_BIRD_CAGES = new ArrayList<>();
    // Cages and jars together: everything backed by an animal-jar block entity.
    public static final List<Block> ALL_ANIMAL_JARS = new ArrayList<>();
    public static final List<Block> ALL_BANNERS = new ArrayList<>();
    public static final List<Block> ALL_TREASURE_PILES = new ArrayList<>();
    public static final List<Block> ALL_ORC_BOMBS = new ArrayList<>();
    /** Each treasure pile to its two-pixel carpet item; see registerTreasurePile. */
    public static final Map<Block, Item> TREASURE_PILE_CARPETS = new LinkedHashMap<>();
    /** Each treasure pile to its full-block item, which is also its asItem(). */
    public static final Map<Block, Item> TREASURE_PILE_BLOCKS = new LinkedHashMap<>();
    public static final List<Block> ALL_WALL_BANNERS = new ArrayList<>();
    /** Each standing banner to the wall form its item falls back to. */
    public static final Map<Block, Block> BANNER_WALL_FORM = new LinkedHashMap<>();
    public static final List<Block> ALL_CLOVERS = new ArrayList<>();
    public static final List<Block> ALL_CRAFTING_TABLES = new ArrayList<>();
    public static final List<Block> ALL_VINES = new ArrayList<>();
    public static final List<Block> ALL_LADDERS = new ArrayList<>();
    public static final List<Block> ALL_GATES = new ArrayList<>();
    /** The three troll totem parts. Rendered by a block entity renderer, so no models are generated. */
    public static final List<Block> ALL_TROLL_TOTEMS = new ArrayList<>();
    /** The two kebab stands. Drawn by a block entity renderer, so no models. */
    public static final List<Block> ALL_KEBAB_STANDS = new ArrayList<>();
    /** The mod's own chests. Drawn by a block entity renderer, so no models. */
    public static final List<Block> ALL_CHESTS = new ArrayList<>();
    public static final List<Block> ALL_BEDS = new ArrayList<>();
    public static final List<Block> ALL_BUSHES = new ArrayList<>();
    public static final List<Block> ALL_CROPS = new ArrayList<>();

    public static final Map<Block, Integer> CROP_STAGES = new LinkedHashMap<>();
    public static final List<Block> ALL_FENCE_GATES = new ArrayList<>();

    public static final Map<Block, Block> FENCE_GATE_BASE = new LinkedHashMap<>();
    public static final List<Block> ALL_BUTTONS = new ArrayList<>();

    public static final Map<Block, Block> BUTTON_BASE = new LinkedHashMap<>();
    public static final List<Block> ALL_PRESSURE_PLATES = new ArrayList<>();

    public static final Map<Block, Block> PRESSURE_PLATE_BASE = new LinkedHashMap<>();

    public static final Map<Block, Block> TORCH_WALL = new LinkedHashMap<>();
    public static final List<Block> ALL_LOGS = new ArrayList<>();
    public static final List<Block> ALL_BEAMS = new ArrayList<>();
    public static final List<Block> ALL_PILLARS = new ArrayList<>();
    public static final List<Block> ALL_STAIRS = new ArrayList<>();
    public static final List<Block> ALL_SLABS = new ArrayList<>();

    public static final Map<Block, Block> SLAB_BASE = new LinkedHashMap<>();
    public static final List<Block> ALL_FENCES = new ArrayList<>();

    public static final Map<Block, Block> FENCE_BASE = new LinkedHashMap<>();
    public static final List<Block> ALL_WALLS = new ArrayList<>();

    public static final Map<Block, Block> WALL_BASE = new LinkedHashMap<>();

    public static final Map<Block, Block> STAIRS_BASE = new LinkedHashMap<>();

    private static final Map<Block, ResourceKey<Block>> BLOCK_KEYS = new LinkedHashMap<>();

    public static ResourceKey<Block> keyOf(Block block) {
        return BLOCK_KEYS.get(block);
    }

    public static final Block TIN_ORE = registerCube("tin_ore", 3.0f, 3.0f, Tier.STONE);
    public static final Block SILVER_ORE = registerCube("silver_ore", 3.0f, 3.0f, Tier.IRON);
    public static final Block MITHRIL_ORE = registerCube("mithril_ore", 4.0f, 6.0f, Tier.IRON);
    public static final Block SALT_ORE = registerCube("salt_ore", 3.0f, 3.0f, Tier.STONE);
    public static final Block SALTPETER_ORE = registerOre("saltpeter_ore", Tier.STONE, 0, 0, 2);
    public static final Block SULFUR_ORE = registerOre("sulfur_ore", Tier.STONE, 0, 0, 2);

    public static final Block AMBER_BLOCK = registerCube("amber_block", 5.0f, 6.0f, Tier.IRON, SoundType.METAL);
    public static final Block AMBER_ORE = registerOre("amber_ore", Tier.IRON, 0, 0, 2);
    public static final Block ANGMAR_BRICK = registerCube("angmar_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block CRACKED_ANGMAR_BRICK = registerCube("cracked_angmar_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block ANGMAR_SNOW_BRICK = registerCube("angmar_snow_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block ARNOR_BRICK = registerCube("arnor_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block CARVED_ARNOR_BRICK = registerCube("carved_arnor_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block CRACKED_ARNOR_BRICK = registerCube("cracked_arnor_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block MOSSY_ARNOR_BRICK = registerCube("mossy_arnor_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block NUMENOREAN_BRICK = registerCube("numenorean_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block CARVED_NUMENOREAN_BRICK = registerCube("carved_numenorean_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block CARVED_BLACK_UMBAR_BRICK = registerCube("carved_black_umbar_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block BLACK_URUK_STEEL_BLOCK = registerCube("black_uruk_steel_block", 5.0f, 6.0f, Tier.STONE, SoundType.METAL);
    public static final Block BLUE_CARVED_BRICK = registerCube("blue_carved_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block BLUE_DWARF_STEEL_BLOCK = registerCube("blue_dwarf_steel_block", 5.0f, 6.0f, Tier.STONE, SoundType.METAL);
    public static final Block BLUE_ROCK = registerCube("blue_rock", 1.5f, 6.0f, Tier.NONE);
    public static final Block BLUE_ROCK_BRICK = registerCube("blue_rock_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block BRONZE_BLOCK = registerCube("bronze_block", 5.0f, 6.0f, Tier.STONE, SoundType.METAL);
    public static final Block CHALK = registerCube("chalk", 1.5f, 6.0f, Tier.NONE);
    public static final Block CHALK_BRICK = registerCube("chalk_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block CLAY_TILE = registerCube("clay_tile", 1.5f, 6.0f, Tier.NONE);
    public static final Block CLAY_TILE_DYED_BLACK = registerCube("clay_tile_dyed_black", 1.5f, 6.0f, Tier.NONE);
    public static final Block CLAY_TILE_DYED_BLUE = registerCube("clay_tile_dyed_blue", 1.5f, 6.0f, Tier.NONE);
    public static final Block CLAY_TILE_DYED_BROWN = registerCube("clay_tile_dyed_brown", 1.5f, 6.0f, Tier.NONE);
    public static final Block CLAY_TILE_DYED_CYAN = registerCube("clay_tile_dyed_cyan", 1.5f, 6.0f, Tier.NONE);
    public static final Block CLAY_TILE_DYED_GRAY = registerCube("clay_tile_dyed_gray", 1.5f, 6.0f, Tier.NONE);
    public static final Block CLAY_TILE_DYED_GREEN = registerCube("clay_tile_dyed_green", 1.5f, 6.0f, Tier.NONE);
    public static final Block CLAY_TILE_DYED_LIGHT_BLUE = registerCube("clay_tile_dyed_light_blue", 1.5f, 6.0f, Tier.NONE);
    public static final Block CLAY_TILE_DYED_LIME = registerCube("clay_tile_dyed_lime", 1.5f, 6.0f, Tier.NONE);
    public static final Block CLAY_TILE_DYED_MAGENTA = registerCube("clay_tile_dyed_magenta", 1.5f, 6.0f, Tier.NONE);
    public static final Block CLAY_TILE_DYED_ORANGE = registerCube("clay_tile_dyed_orange", 1.5f, 6.0f, Tier.NONE);
    public static final Block CLAY_TILE_DYED_PINK = registerCube("clay_tile_dyed_pink", 1.5f, 6.0f, Tier.NONE);
    public static final Block CLAY_TILE_DYED_PURPLE = registerCube("clay_tile_dyed_purple", 1.5f, 6.0f, Tier.NONE);
    public static final Block CLAY_TILE_DYED_RED = registerCube("clay_tile_dyed_red", 1.5f, 6.0f, Tier.NONE);
    public static final Block CLAY_TILE_DYED_SILVER = registerCube("clay_tile_dyed_silver", 1.5f, 6.0f, Tier.NONE);
    public static final Block CLAY_TILE_DYED_WHITE = registerCube("clay_tile_dyed_white", 1.5f, 6.0f, Tier.NONE);
    public static final Block CLAY_TILE_DYED_YELLOW = registerCube("clay_tile_dyed_yellow", 1.5f, 6.0f, Tier.NONE);
    public static final Block DALE_BRICK = registerCube("dale_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block DALE_CARVED_BRICK = registerCube("dale_carved_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block DALE_CRACKED_BRICK = registerCube("dale_cracked_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block DALE_MOSSY_BRICK = registerCube("dale_mossy_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block DOL_AMROTH_BRICK = registerCube("dol_amroth_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block DOL_GULDUR_BRICK = registerCube("dol_guldur_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block CARVED_DOL_GULDUR_BRICK = registerCube("carved_dol_guldur_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block CRACKED_DOL_GULDUR_BRICK = registerCube("cracked_dol_guldur_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block MOSSY_DOL_GULDUR_BRICK = registerCube("mossy_dol_guldur_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block DORWINION_BRICK = registerCube("dorwinion_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block CARVED_DORWINION_BRICK = registerCube("carved_dorwinion_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block CRACKED_DORWINION_BRICK = registerCube("cracked_dorwinion_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block DORWINION_FLOWERS_BRICK = registerCube("dorwinion_flowers_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block MOSSY_DORWINION_BRICK = registerCube("mossy_dorwinion_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block DWARF_STEEL_BLOCK = registerCube("dwarf_steel_block", 5.0f, 6.0f, Tier.STONE, SoundType.METAL);
    public static final Block DWARVEN_BRICK = registerCube("dwarven_brick", 1.5f, 6.0f, Tier.NONE);

    public static final Block DWARVEN_SILVER_BRICK = registerCube("dwarven_silver_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block DWARVEN_GOLD_BRICK = registerCube("dwarven_gold_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block DWARVEN_MITHRIL_BRICK = registerCube("dwarven_mithril_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block CARVED_DWARVEN_BRICK = registerCube("carved_dwarven_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block CRACKED_DWARVEN_BRICK = registerCube("cracked_dwarven_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block GLOWING_DWARVEN_BRICK = registerCube("glowing_dwarven_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block OBSIDIAN_DWARVEN_BRICK = registerCube("obsidian_dwarven_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block ELF_STEEL_BLOCK = registerCube("elf_steel_block", 5.0f, 6.0f, Tier.IRON, SoundType.METAL);
    public static final Block GALADHRIM_BRICK = registerCube("galadhrim_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block CARVED_GALADHRIM_BRICK = registerCube("carved_galadhrim_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block CRACKED_GALADHRIM_BRICK = registerCube("cracked_galadhrim_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block GALADHRIM_GOLD_BRICK = registerCube("galadhrim_gold_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block MOSSY_GALADHRIM_BRICK = registerCube("mossy_galadhrim_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block GALADHRIM_SILVER_BRICK = registerCube("galadhrim_silver_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block GALVORN_BLOCK = registerCube("galvorn_block", 5.0f, 6.0f, Tier.IRON, SoundType.METAL);
    public static final Block GILDED_IRON_BLOCK = registerCube("gilded_iron_block", 5.0f, 6.0f, Tier.STONE, SoundType.METAL);
    public static final Block GLOWSTONE_ORE = registerOre("glowstone_ore", Tier.STONE, 11, 2, 4);
    public static final Block GONDOR_BRICK = registerCube("gondor_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block CARVED_GONDOR_BRICK = registerCube("carved_gondor_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block CRACKED_GONDOR_BRICK = registerCube("cracked_gondor_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block MOSSY_GONDOR_BRICK = registerCube("mossy_gondor_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block GONDOR_ROCK = registerCube("gondor_rock", 1.5f, 6.0f, Tier.NONE);
    public static final Block GONDOR_COBBLEBRICK = registerCube("gondor_cobblebrick", 1.5f, 6.0f, Tier.NONE);
    public static final Block CRACKED_GONDOR_COBBLEBRICK = registerCube("cracked_gondor_cobblebrick", 1.5f, 6.0f, Tier.NONE);
    public static final Block MOSSY_GONDOR_COBBLEBRICK = registerCube("mossy_gondor_cobblebrick", 1.5f, 6.0f, Tier.NONE);
    public static final Block GULDURIL_BLOCK = registerCube("gulduril_block", 5.0f, 6.0f, Tier.IRON, SoundType.METAL);

    public static final Block GULDURIL_MORDOR_ORE = registerOre("gulduril_mordor_ore", Tier.IRON, 11, 2, 5);
    public static final Block GULDURIL_ORE = registerOre("gulduril_ore", Tier.IRON, 11, 2, 5);
    public static final Block HIGH_ELVEN_BRICK = registerCube("high_elven_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block CARVED_HIGH_ELVEN_BRICK = registerCube("carved_high_elven_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block CRACKED_HIGH_ELVEN_BRICK = registerCube("cracked_high_elven_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block HIGH_ELVEN_GOLD_BRICK = registerCube("high_elven_gold_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block MOSSY_HIGH_ELVEN_BRICK = registerCube("mossy_high_elven_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block HIGH_ELVEN_SILVER_BRICK = registerCube("high_elven_silver_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block MITHRIL_BLOCK = registerCube("mithril_block", 5.0f, 6.0f, Tier.IRON, SoundType.METAL);
    public static final Block MORDOR_BRICK = registerCube("mordor_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block CARVED_MORDOR_BRICK = registerCube("carved_mordor_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block CRACKED_MORDOR_BRICK = registerCube("cracked_mordor_brick", 1.5f, 6.0f, Tier.NONE);
    // Gulduril bricks have no textures of their own: LOTRBlockGuldurilBrick was
    // a base brick with a glowing crystal overlay, so each borrows the brick it
    // was cut from via TEXTURE_SOURCE. Light 0.75 -> 11, hardness 3.0,
    // resistance 10.0 legacy -> 6.0 modern.
    public static final Block GULDURIL_MORDOR_BRICK = registerGulduril("gulduril_mordor_brick");
    public static final Block GULDURIL_CRACKED_MORDOR_BRICK = registerGulduril("gulduril_cracked_mordor_brick");
    public static final Block GULDURIL_ANGMAR_BRICK = registerGulduril("gulduril_angmar_brick");
    public static final Block GULDURIL_CRACKED_ANGMAR_BRICK = registerGulduril("gulduril_cracked_angmar_brick");
    public static final Block GULDURIL_DOL_GULDUR_BRICK = registerGulduril("gulduril_dol_guldur_brick");
    public static final Block GULDURIL_CRACKED_DOL_GULDUR_BRICK = registerGulduril("gulduril_cracked_dol_guldur_brick");
    public static final Block GULDURIL_GONDOR_BRICK = registerGulduril("gulduril_gondor_brick");
    public static final Block GULDURIL_MOSSY_GONDOR_BRICK = registerGulduril("gulduril_mossy_gondor_brick");
    public static final Block GULDURIL_CRACKED_GONDOR_BRICK = registerGulduril("gulduril_cracked_gondor_brick");
    public static final Block GULDURIL_NUMENOREAN_BRICK = registerGulduril("gulduril_numenorean_brick");
    public static final Block MORDOR_DIRT = registerSoil("mordor_dirt", 0.5f, 0.3f, SoundType.GRAVEL);
    public static final Block MORDOR_GRAVEL = registerFalling("mordor_gravel", 0.6f, 0xFF3A3A3A);
    public static final Block MORDOR_MOSS_ROCK = registerCube("mordor_moss_rock", 1.5f, 6.0f, Tier.NONE);
    public static final Block MORDOR_ROCK = registerCubeColumn("mordor_rock", 1.5f, 6.0f, Tier.NONE);
    public static final Block MORWAITH_BRICK = registerCube("morwaith_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block MORGUL_IRON_MORDOR_ORE = registerCube("morgul_iron_mordor_ore", 3.0f, 3.0f, Tier.STONE);
    public static final Block MORGUL_IRON_ORE = registerCube("morgul_iron_ore", 3.0f, 3.0f, Tier.STONE);
    public static final Block MORGUL_STEEL_BLOCK = registerCubeColumn("morgul_steel_block", 5.0f, 6.0f, Tier.IRON, SoundType.METAL);
    public static final Block CRACKED_MORWAITH_BRICK = registerCube("cracked_morwaith_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block NAURITE_BLOCK = registerCube("naurite_block", 5.0f, 6.0f, Tier.IRON, SoundType.METAL);
    public static final Block NAURITE_ORE = registerOre("naurite_ore", Tier.IRON, 7, 0, 2);
    public static final Block NEAR_HARAD_BRICK = registerCube("near_harad_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block NEAR_HARAD_CARVED_BRICK = registerCube("near_harad_carved_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block NEAR_HARAD_CRACKED_BRICK = registerCube("near_harad_cracked_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block NEAR_HARAD_LAPIS_BRICK = registerCube("near_harad_lapis_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block NEAR_HARAD_RED_BRICK = registerCube("near_harad_red_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block NEAR_HARAD_RED_CARVED_BRICK = registerCube("near_harad_red_carved_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block NEAR_HARAD_RED_CRACKED_BRICK = registerCube("near_harad_red_cracked_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block OBSIDIAN_GRAVEL = registerFalling("obsidian_gravel", 0.6f, 0xFF1B1B22);
    public static final Block OPAL_BLOCK = registerCube("opal_block", 5.0f, 6.0f, Tier.IRON, SoundType.METAL);
    public static final Block OPAL_ORE = registerOre("opal_ore", Tier.IRON, 0, 0, 2);
    public static final Block ORC_PLATING_IRON = registerCube("orc_plating_iron", 3.0f, 6.0f, Tier.NONE, SoundType.METAL);
    public static final Block ORC_PLATING_RUST = registerCube("orc_plating_rust", 3.0f, 6.0f, Tier.NONE, SoundType.METAL);
    public static final Block ORC_STEEL_BLOCK = registerCubeColumn("orc_steel_block", 5.0f, 6.0f, Tier.STONE, SoundType.METAL);
    public static final Block PEARL_BLOCK = registerCube("pearl_block", 5.0f, 6.0f, Tier.IRON, SoundType.METAL);

    public static final Block QUAGMIRE = track(ALL_CUBES, track(SHOVEL_MINEABLE, register("quagmire",
            LOTRQuagmireBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DIRT)
                    .strength(0.0f)
                    .sound(SoundType.GRAVEL)
                    .noCollision()
                    .noOcclusion(),
            true)));
    public static final Block QUENDITE_BLOCK = registerCube("quendite_block", 5.0f, 6.0f, Tier.IRON, SoundType.METAL);
    public static final Block QUENDITE_ORE = registerOre("quendite_ore", Tier.IRON, 11, 2, 5);
    public static final Block RED_BRICK_CRACKED = registerCube("red_brick_cracked", 1.5f, 6.0f, Tier.NONE);
    public static final Block RED_BRICK_MOSSY = registerCube("red_brick_mossy", 1.5f, 6.0f, Tier.NONE);
    public static final Block RED_CARVED_BRICK = registerCube("red_carved_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block RED_CLAY = registerSoil("red_clay", 0.5f, 0.3f, SoundType.GRAVEL);
    public static final Block RED_ROCK = registerCube("red_rock", 1.5f, 6.0f, Tier.NONE);
    public static final Block RED_ROCK_BRICK = registerCube("red_rock_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block RHUN_BRICK = registerCube("rhun_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block RHUN_CARVED_BRICK = registerCube("rhun_carved_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block RHUN_CRACKED_BRICK = registerCube("rhun_cracked_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block RHUN_FLOWERS_BRICK = registerCube("rhun_flowers_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block RHUN_GOLD_BRICK = registerCube("rhun_gold_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block RHUN_MOSSY_BRICK = registerCube("rhun_mossy_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block RHUN_RED_BRICK = registerCube("rhun_red_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block RHUN_RED_CARVED_BRICK = registerCube("rhun_red_carved_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block ROHAN_BRICK = registerCubeColumn("rohan_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block CARVED_ROHAN_BRICK = registerCube("carved_rohan_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block ROHAN_ROCK = registerCube("rohan_rock", 1.5f, 6.0f, Tier.NONE);
    public static final Block RUBY_BLOCK = registerCube("ruby_block", 5.0f, 6.0f, Tier.IRON, SoundType.METAL);
    public static final Block RUBY_ORE = registerOre("ruby_ore", Tier.IRON, 0, 0, 2);
    public static final Block SALT_BLOCK = registerCube("salt_block", 5.0f, 6.0f, Tier.STONE, SoundType.METAL);
    public static final Block SULFUR_BLOCK = registerCube("sulfur_block", 5.0f, 6.0f, Tier.STONE, SoundType.METAL);
    public static final Block SALTPETER_BLOCK = registerCube("saltpeter_block", 5.0f, 6.0f, Tier.STONE, SoundType.METAL);
    public static final Block SAPPHIRE_BLOCK = registerCube("sapphire_block", 5.0f, 6.0f, Tier.IRON, SoundType.METAL);
    public static final Block SAPPHIRE_ORE = registerOre("sapphire_ore", Tier.IRON, 0, 0, 2);
    public static final Block SCORCHED_STONE = registerCube("scorched_stone", 1.5f, 6.0f, Tier.NONE);
    // utumnoPillar 0/1/2. Column-textured like every other pillar.
    public static final Block FIRE_UTUMNO_PILLAR = registerPillar("fire_utumno_pillar");
    public static final Block ICE_UTUMNO_PILLAR = registerPillar("ice_utumno_pillar");
    public static final Block OBSIDIAN_UTUMNO_PILLAR = registerPillar("obsidian_utumno_pillar");
    public static final Block SILVER_BLOCK = registerCube("silver_block", 5.0f, 6.0f, Tier.IRON, SoundType.METAL);
    public static final Block TAUREDAIN_BRICK = registerCube("tauredain_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block TAUREDAIN_CRACKED_BRICK = registerCube("tauredain_cracked_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block TAUREDAIN_GOLD_BRICK = registerCube("tauredain_gold_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block TAUREDAIN_MOSSY_BRICK = registerCube("tauredain_mossy_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block TAUREDAIN_OBSIDIAN_BRICK = registerCube("tauredain_obsidian_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block TAUREDAIN_DART_TRAP = registerDartTrap("tauredain_dart_trap", TAUREDAIN_BRICK);
    public static final Block GOLD_TAUREDAIN_DART_TRAP = registerDartTrap("gold_tauredain_dart_trap", TAUREDAIN_GOLD_BRICK);
    public static final Block OBSIDIAN_TAUREDAIN_DART_TRAP = registerDartTrap("obsidian_tauredain_dart_trap", TAUREDAIN_OBSIDIAN_BRICK);

    // Forges. Four blocks, one block entity, thirteen slots -- see LOTRForgeBlockEntity.
    public static final Block DWARVEN_FORGE = registerForge("dwarven_forge");
    public static final Block ELVEN_FORGE = registerForge("elven_forge");
    public static final Block ORC_FORGE = registerForge("orc_forge");
    public static final Block ALLOY_FORGE = registerForge("alloy_forge");

    // Beacon of Gondor. Custom geometry, so it joins no family list: its
    // blockstate, models and item model are hand-written under
    // src/main/resources/assets/lotr/, not generated.
    public static final Block BEACON_OF_GONDOR = registerBeacon("beacon_of_gondor");

    // Hobbit oven. Nineteen slots, nine cook lanes -- see
    // LOTRHobbitOvenBlockEntity. Light 13 while lit, as in the original.
    public static final Block HOBBIT_OVEN = registerHobbitOven("hobbit_oven");

    public static final Block TIN_BLOCK = registerCube("tin_block", 5.0f, 6.0f, Tier.STONE, SoundType.METAL);
    public static final Block UMBAR_BRICK = registerCube("umbar_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block CARVED_UMBAR_BRICK = registerCube("carved_umbar_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block CRACKED_UMBAR_BRICK = registerCube("cracked_umbar_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block URUK_BRICK = registerCube("uruk_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block URUK_STEEL_BLOCK = registerCubeColumn("uruk_steel_block", 5.0f, 6.0f, Tier.STONE, SoundType.METAL);
    public static final Block FIRE_UTUMNO_BRICK = registerCube("fire_utumno_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block UTUMNO_FIRE_TILE_BRICK = registerCube("utumno_fire_tile_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block ICE_UTUMNO_BRICK = registerCube("ice_utumno_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block GLOWING_ICE_UTUMNO_BRICK = registerCube("glowing_ice_utumno_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block UTUMNO_ICE_TILE_BRICK = registerCube("utumno_ice_tile_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block OBSIDIAN_UTUMNO_BRICK = registerCube("obsidian_utumno_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block UTUMNO_OBSIDIAN_FIRE_BRICK = registerCube("utumno_obsidian_fire_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block UTUMNO_OBSIDIAN_TILE_BRICK = registerCube("utumno_obsidian_tile_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block UTUMNO_FIRE_TILE_BRICK_WALL = registerWall("utumno_fire_tile_brick_wall", UTUMNO_FIRE_TILE_BRICK);
    public static final Block UTUMNO_ICE_TILE_BRICK_WALL = registerWall("utumno_ice_tile_brick_wall", UTUMNO_ICE_TILE_BRICK);
    public static final Block UTUMNO_OBSIDIAN_TILE_BRICK_WALL = registerWall("utumno_obsidian_tile_brick_wall", UTUMNO_OBSIDIAN_TILE_BRICK);
    public static final Block UTUMNO_FIRE_TILE_BRICK_SLAB = registerSlab("utumno_fire_tile_brick_slab", UTUMNO_FIRE_TILE_BRICK);
    public static final Block UTUMNO_ICE_TILE_BRICK_SLAB = registerSlab("utumno_ice_tile_brick_slab", UTUMNO_ICE_TILE_BRICK);
    public static final Block UTUMNO_OBSIDIAN_TILE_BRICK_SLAB = registerSlab("utumno_obsidian_tile_brick_slab", UTUMNO_OBSIDIAN_TILE_BRICK);
    public static final Block WHITE_SAND = registerFalling("white_sand", 0.6f, 0xFFE6E0CF);
    public static final Block WHITE_SANDSTONE = registerBottomTop("white_sandstone", 0.8f, 0.8f);
    public static final Block WOOD_ELVEN_BRICK = registerCube("wood_elven_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block CARVED_WOOD_ELVEN_BRICK = registerCube("carved_wood_elven_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block CRACKED_WOOD_ELVEN_BRICK = registerCube("cracked_wood_elven_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block WOOD_ELVEN_GOLD_BRICK = registerCube("wood_elven_gold_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block MOSSY_WOOD_ELVEN_BRICK = registerCube("mossy_wood_elven_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block WOOD_ELVEN_SILVER_BRICK = registerCube("wood_elven_silver_brick", 1.5f, 6.0f, Tier.NONE);

    // --- Soft blocks (no tool required; original set no harvest level) ---

    public static final Block KEBAB_BLOCK = registerSoftBlock("kebab_block", 0.5f, SoundType.WOOD);
    public static final Block REMAINS = track(SHOVEL_MINEABLE, registerSoftBlock("remains", 3.0f, SoundType.GRAVEL));
    public static final Block THATCH_REED = registerSoftBlock("thatch_reed", 0.5f, SoundType.GRASS);
    public static final Block THATCH_THATCH = registerSoftBlock("thatch_thatch", 0.5f, SoundType.GRASS);

    public static final Block DIAMOND_BLOCK = registerCube("diamond_block", 5.0f, 6.0f, Tier.IRON, SoundType.METAL);
    public static final Block AMETHYST_ORE = registerOre("amethyst_ore", Tier.IRON, 0, 0, 2);
    public static final Block DIAMOND_ORE = registerOre("diamond_ore", Tier.IRON, 0, 0, 2);
    public static final Block EMERALD_ORE = registerOre("emerald_ore", Tier.IRON, 0, 0, 2);
    public static final Block MUD = track(SHOVEL_MINEABLE, registerSoftBlock("mud", 0.5f, SoundType.GRAVEL));
    // mud.1 and dirtPath.1 -- the jungle variants.
    public static final Block BARREN_JUNGLE_MUD = track(SHOVEL_MINEABLE, registerSoftBlock("barren_jungle_mud", 0.5f, SoundType.GRAVEL));
    static {
        // mud.1 shared mud.0's single icon in the original.
        TEXTURE_SOURCE.put(BARREN_JUNGLE_MUD, MUD);
    }
    public static final Block MUD_BRICK = registerCube("mud_brick", 1.5f, 6.0f, Tier.NONE);
    public static final Block TOPAZ_BLOCK = registerCube("topaz_block", 5.0f, 6.0f, Tier.IRON, SoundType.METAL);
    public static final Block AMETHYST_BLOCK = registerCube("amethyst_block", 5.0f, 6.0f, Tier.IRON, SoundType.METAL);
    public static final Block CORAL_BLOCK = registerCube("coral_block", 5.0f, 6.0f, Tier.IRON, SoundType.METAL);
    public static final Block EMERALD_BLOCK = registerCube("emerald_block", 5.0f, 6.0f, Tier.IRON, SoundType.METAL);
    public static final Block TOPAZ_ORE = registerOre("topaz_ore", Tier.IRON, 0, 0, 2);
    public static final Block BURNING_UTUMNO_BRICK = registerCube("burning_utumno_brick", 1.5f, 6.0f, Tier.NONE);

    public static final Block SMOOTH_MORDOR_ROCK = registerColumn("smooth_mordor_rock");
    public static final Block SMOOTH_GONDOR_ROCK = registerColumn("smooth_gondor_rock");
    public static final Block SMOOTH_ROHAN_ROCK = registerColumn("smooth_rohan_rock");
    public static final Block SMOOTH_BLUE_ROCK = registerColumn("smooth_blue_rock");
    public static final Block SMOOTH_RED_ROCK = registerColumn("smooth_red_rock");
    public static final Block SMOOTH_CHALK = registerColumn("smooth_chalk");
    public static final Block DRYSTONE = registerCube("drystone", 1.5f, 6.0f, Tier.NONE);

    public static final Block ORC_TORCH = registerDoubleTorch("orc_torch");

    // LOTRBlockOrcBomb, metadata 0/1/2 of one block in 1.7.10. The strength is
    // the blast radius and the fuse length; see LOTROrcBombBlock.
    // Registration order is getSubBlocks' order -- the three plain strengths,
    // then the three fire ones -- because the creative tab walks this list.
    // LOTRBlockRhunFire, the flame a broken jar leaves. No item: it is placed by
    // the jar and by nothing else.
    public static final Block KHAMULS_FIRE = register("khamuls_fire",
            LOTRKhamulsFireBlock::new, LOTRKhamulsFireBlock.fireProperties(), false);

    // LOTRBlockRhunFireJar, "Khamûl's Fire": hardness 0.5, stone footsteps, and
    // it falls. Registered before the bombs so it sits with them in the tab.
    public static final Block KHAMULS_FIRE_JAR = register("khamuls_fire_jar",
            LOTRKhamulsFireJarBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .strength(0.5f)
                    .sound(SoundType.STONE)
                    .noOcclusion()
                    .randomTicks()
                    .pushReaction(PushReaction.DESTROY),
            // tile.lotr.rhunFire.warning, "Extremely volatile" -- the line the
            // original put under the name to warn what handling one means.
            true, props -> props.component(DataComponents.LORE, new ItemLore(List.of(
                    Component.translatable("block.lotr.khamuls_fire_jar.warning")))));

    public static final Block ORC_BOMB = registerOrcBomb("orc_bomb", 0, false);
    public static final Block DOUBLE_STRENGTH_ORC_BOMB = registerOrcBomb("double_strength_orc_bomb", 1, false);
    public static final Block TRIPLE_STRENGTH_ORC_BOMB = registerOrcBomb("triple_strength_orc_bomb", 2, false);
    public static final Block ORC_FIRE_BOMB = registerOrcBomb("orc_fire_bomb", 0, true);
    public static final Block DOUBLE_STRENGTH_ORC_FIRE_BOMB = registerOrcBomb("double_strength_orc_fire_bomb", 1, true);
    public static final Block TRIPLE_STRENGTH_ORC_FIRE_BOMB = registerOrcBomb("triple_strength_orc_fire_bomb", 2, true);
    // lotr:tauredainDoubleTorch, the "Taurethrim Torch". Registered exactly as
    // the orc torch is -- both were a bare new LOTRBlockDoubleTorch() in
    // LOTRMod, differing only in their sprites.
    public static final Block TAUREDAIN_DOUBLE_TORCH = registerDoubleTorch("tauredain_double_torch");

    public static final Block BRONZE_BIRD_CAGE = registerBirdCage("bronze_bird_cage", SoundType.METAL);
    public static final Block IRON_BIRD_CAGE = registerBirdCage("iron_bird_cage", SoundType.METAL);
    public static final Block SILVER_BIRD_CAGE = registerBirdCage("silver_bird_cage", SoundType.METAL);
    public static final Block GOLD_BIRD_CAGE = registerBirdCage("gold_bird_cage", SoundType.METAL);
    public static final Block WOODEN_BIRD_CAGE = registerBirdCage("wooden_bird_cage", SoundType.WOOD);
    public static final Block BUTTERFLY_JAR = registerButterflyJar("butterfly_jar");

    // LOTRMod: new LOTRBlockWeaponRack(). Material.circuits, hardness 0.5,
    // resistance 1.0, wooden footsteps.

    // LOTRItemBanner.BannerType, one standing and one wall block each. Vanilla's
    // banner blocks do the placing and the rotating; see LOTRBannerBlock.
    public static final Block GONDOR_WALL_BANNER = registerWallBanner("gondor_wall_banner", LOTRBannerType.GONDOR);
    public static final Block GONDOR_BANNER = registerBanner("gondor_banner", LOTRBannerType.GONDOR, GONDOR_WALL_BANNER);
    public static final Block ROHAN_WALL_BANNER = registerWallBanner("rohan_wall_banner", LOTRBannerType.ROHAN);
    public static final Block ROHAN_BANNER = registerBanner("rohan_banner", LOTRBannerType.ROHAN, ROHAN_WALL_BANNER);
    public static final Block MORDOR_WALL_BANNER = registerWallBanner("mordor_wall_banner", LOTRBannerType.MORDOR);
    public static final Block MORDOR_BANNER = registerBanner("mordor_banner", LOTRBannerType.MORDOR, MORDOR_WALL_BANNER);
    public static final Block LOTHLORIEN_WALL_BANNER = registerWallBanner("lothlorien_wall_banner", LOTRBannerType.LOTHLORIEN);
    public static final Block LOTHLORIEN_BANNER = registerBanner("lothlorien_banner", LOTRBannerType.LOTHLORIEN, LOTHLORIEN_WALL_BANNER);
    public static final Block MIRKWOOD_WALL_BANNER = registerWallBanner("mirkwood_wall_banner", LOTRBannerType.MIRKWOOD);
    public static final Block MIRKWOOD_BANNER = registerBanner("mirkwood_banner", LOTRBannerType.MIRKWOOD, MIRKWOOD_WALL_BANNER);
    public static final Block DUNLAND_WALL_BANNER = registerWallBanner("dunland_wall_banner", LOTRBannerType.DUNLAND);
    public static final Block DUNLAND_BANNER = registerBanner("dunland_banner", LOTRBannerType.DUNLAND, DUNLAND_WALL_BANNER);
    public static final Block ISENGARD_WALL_BANNER = registerWallBanner("isengard_wall_banner", LOTRBannerType.ISENGARD);
    public static final Block ISENGARD_BANNER = registerBanner("isengard_banner", LOTRBannerType.ISENGARD, ISENGARD_WALL_BANNER);
    public static final Block DURIN_WALL_BANNER = registerWallBanner("durin_wall_banner", LOTRBannerType.DURIN);
    public static final Block DURIN_BANNER = registerBanner("durin_banner", LOTRBannerType.DURIN, DURIN_WALL_BANNER);
    public static final Block ANGMAR_WALL_BANNER = registerWallBanner("angmar_wall_banner", LOTRBannerType.ANGMAR);
    public static final Block ANGMAR_BANNER = registerBanner("angmar_banner", LOTRBannerType.ANGMAR, ANGMAR_WALL_BANNER);
    public static final Block NEAR_HARAD_WALL_BANNER = registerWallBanner("near_harad_wall_banner", LOTRBannerType.NEAR_HARAD);
    public static final Block NEAR_HARAD_BANNER = registerBanner("near_harad_banner", LOTRBannerType.NEAR_HARAD, NEAR_HARAD_WALL_BANNER);
    public static final Block HIGH_ELF_WALL_BANNER = registerWallBanner("high_elf_wall_banner", LOTRBannerType.HIGH_ELF);
    public static final Block HIGH_ELF_BANNER = registerBanner("high_elf_banner", LOTRBannerType.HIGH_ELF, HIGH_ELF_WALL_BANNER);
    public static final Block BLUE_MOUNTAINS_WALL_BANNER = registerWallBanner("blue_mountains_wall_banner", LOTRBannerType.BLUE_MOUNTAINS);
    public static final Block BLUE_MOUNTAINS_BANNER = registerBanner("blue_mountains_banner", LOTRBannerType.BLUE_MOUNTAINS, BLUE_MOUNTAINS_WALL_BANNER);
    public static final Block RANGER_WALL_BANNER = registerWallBanner("ranger_wall_banner", LOTRBannerType.RANGER);
    public static final Block RANGER_BANNER = registerBanner("ranger_banner", LOTRBannerType.RANGER, RANGER_WALL_BANNER);
    public static final Block DOL_GULDUR_WALL_BANNER = registerWallBanner("dol_guldur_wall_banner", LOTRBannerType.DOL_GULDUR);
    public static final Block DOL_GULDUR_BANNER = registerBanner("dol_guldur_banner", LOTRBannerType.DOL_GULDUR, DOL_GULDUR_WALL_BANNER);
    public static final Block GUNDABAD_WALL_BANNER = registerWallBanner("gundabad_wall_banner", LOTRBannerType.GUNDABAD);
    public static final Block GUNDABAD_BANNER = registerBanner("gundabad_banner", LOTRBannerType.GUNDABAD, GUNDABAD_WALL_BANNER);
    public static final Block HALF_TROLL_WALL_BANNER = registerWallBanner("half_troll_wall_banner", LOTRBannerType.HALF_TROLL);
    public static final Block HALF_TROLL_BANNER = registerBanner("half_troll_banner", LOTRBannerType.HALF_TROLL, HALF_TROLL_WALL_BANNER);
    public static final Block DOL_AMROTH_WALL_BANNER = registerWallBanner("dol_amroth_wall_banner", LOTRBannerType.DOL_AMROTH);
    public static final Block DOL_AMROTH_BANNER = registerBanner("dol_amroth_banner", LOTRBannerType.DOL_AMROTH, DOL_AMROTH_WALL_BANNER);
    public static final Block MOREDAIN_WALL_BANNER = registerWallBanner("moredain_wall_banner", LOTRBannerType.MOREDAIN);
    public static final Block MOREDAIN_BANNER = registerBanner("moredain_banner", LOTRBannerType.MOREDAIN, MOREDAIN_WALL_BANNER);
    public static final Block TAUREDAIN_WALL_BANNER = registerWallBanner("tauredain_wall_banner", LOTRBannerType.TAUREDAIN);
    public static final Block TAUREDAIN_BANNER = registerBanner("tauredain_banner", LOTRBannerType.TAUREDAIN, TAUREDAIN_WALL_BANNER);
    public static final Block DALE_WALL_BANNER = registerWallBanner("dale_wall_banner", LOTRBannerType.DALE);
    public static final Block DALE_BANNER = registerBanner("dale_banner", LOTRBannerType.DALE, DALE_WALL_BANNER);
    public static final Block DORWINION_WALL_BANNER = registerWallBanner("dorwinion_wall_banner", LOTRBannerType.DORWINION);
    public static final Block DORWINION_BANNER = registerBanner("dorwinion_banner", LOTRBannerType.DORWINION, DORWINION_WALL_BANNER);
    public static final Block HOBBIT_WALL_BANNER = registerWallBanner("hobbit_wall_banner", LOTRBannerType.HOBBIT);
    public static final Block HOBBIT_BANNER = registerBanner("hobbit_banner", LOTRBannerType.HOBBIT, HOBBIT_WALL_BANNER);
    public static final Block ANORIEN_WALL_BANNER = registerWallBanner("anorien_wall_banner", LOTRBannerType.ANORIEN);
    public static final Block ANORIEN_BANNER = registerBanner("anorien_banner", LOTRBannerType.ANORIEN, ANORIEN_WALL_BANNER);
    public static final Block ITHILIEN_WALL_BANNER = registerWallBanner("ithilien_wall_banner", LOTRBannerType.ITHILIEN);
    public static final Block ITHILIEN_BANNER = registerBanner("ithilien_banner", LOTRBannerType.ITHILIEN, ITHILIEN_WALL_BANNER);
    public static final Block LOSSARNACH_WALL_BANNER = registerWallBanner("lossarnach_wall_banner", LOTRBannerType.LOSSARNACH);
    public static final Block LOSSARNACH_BANNER = registerBanner("lossarnach_banner", LOTRBannerType.LOSSARNACH, LOSSARNACH_WALL_BANNER);
    public static final Block LEBENNIN_WALL_BANNER = registerWallBanner("lebennin_wall_banner", LOTRBannerType.LEBENNIN);
    public static final Block LEBENNIN_BANNER = registerBanner("lebennin_banner", LOTRBannerType.LEBENNIN, LEBENNIN_WALL_BANNER);
    public static final Block PELARGIR_WALL_BANNER = registerWallBanner("pelargir_wall_banner", LOTRBannerType.PELARGIR);
    public static final Block PELARGIR_BANNER = registerBanner("pelargir_banner", LOTRBannerType.PELARGIR, PELARGIR_WALL_BANNER);
    public static final Block BLACKROOT_VALE_WALL_BANNER = registerWallBanner("blackroot_vale_wall_banner", LOTRBannerType.BLACKROOT_VALE);
    public static final Block BLACKROOT_VALE_BANNER = registerBanner("blackroot_vale_banner", LOTRBannerType.BLACKROOT_VALE, BLACKROOT_VALE_WALL_BANNER);
    public static final Block PINNATH_GELIN_WALL_BANNER = registerWallBanner("pinnath_gelin_wall_banner", LOTRBannerType.PINNATH_GELIN);
    public static final Block PINNATH_GELIN_BANNER = registerBanner("pinnath_gelin_banner", LOTRBannerType.PINNATH_GELIN, PINNATH_GELIN_WALL_BANNER);
    public static final Block MINAS_MORGUL_WALL_BANNER = registerWallBanner("minas_morgul_wall_banner", LOTRBannerType.MINAS_MORGUL);
    public static final Block MINAS_MORGUL_BANNER = registerBanner("minas_morgul_banner", LOTRBannerType.MINAS_MORGUL, MINAS_MORGUL_WALL_BANNER);
    public static final Block BLACK_URUK_WALL_BANNER = registerWallBanner("black_uruk_wall_banner", LOTRBannerType.BLACK_URUK);
    public static final Block BLACK_URUK_BANNER = registerBanner("black_uruk_banner", LOTRBannerType.BLACK_URUK, BLACK_URUK_WALL_BANNER);
    public static final Block GONDOR_STEWARD_WALL_BANNER = registerWallBanner("gondor_steward_wall_banner", LOTRBannerType.GONDOR_STEWARD);
    public static final Block GONDOR_STEWARD_BANNER = registerBanner("gondor_steward_banner", LOTRBannerType.GONDOR_STEWARD, GONDOR_STEWARD_WALL_BANNER);
    public static final Block NAN_UNGOL_WALL_BANNER = registerWallBanner("nan_ungol_wall_banner", LOTRBannerType.NAN_UNGOL);
    public static final Block NAN_UNGOL_BANNER = registerBanner("nan_ungol_banner", LOTRBannerType.NAN_UNGOL, NAN_UNGOL_WALL_BANNER);
    public static final Block RHUDAUR_WALL_BANNER = registerWallBanner("rhudaur_wall_banner", LOTRBannerType.RHUDAUR);
    public static final Block RHUDAUR_BANNER = registerBanner("rhudaur_banner", LOTRBannerType.RHUDAUR, RHUDAUR_WALL_BANNER);
    public static final Block LAMEDON_WALL_BANNER = registerWallBanner("lamedon_wall_banner", LOTRBannerType.LAMEDON);
    public static final Block LAMEDON_BANNER = registerBanner("lamedon_banner", LOTRBannerType.LAMEDON, LAMEDON_WALL_BANNER);
    public static final Block RHUN_WALL_BANNER = registerWallBanner("rhun_wall_banner", LOTRBannerType.RHUN);
    public static final Block RHUN_BANNER = registerBanner("rhun_banner", LOTRBannerType.RHUN, RHUN_WALL_BANNER);
    public static final Block RIVENDELL_WALL_BANNER = registerWallBanner("rivendell_wall_banner", LOTRBannerType.RIVENDELL);
    public static final Block RIVENDELL_BANNER = registerBanner("rivendell_banner", LOTRBannerType.RIVENDELL, RIVENDELL_WALL_BANNER);
    public static final Block ESGAROTH_WALL_BANNER = registerWallBanner("esgaroth_wall_banner", LOTRBannerType.ESGAROTH);
    public static final Block ESGAROTH_BANNER = registerBanner("esgaroth_banner", LOTRBannerType.ESGAROTH, ESGAROTH_WALL_BANNER);
    public static final Block UMBAR_WALL_BANNER = registerWallBanner("umbar_wall_banner", LOTRBannerType.UMBAR);
    public static final Block UMBAR_BANNER = registerBanner("umbar_banner", LOTRBannerType.UMBAR, UMBAR_WALL_BANNER);
    public static final Block HARAD_NOMAD_WALL_BANNER = registerWallBanner("harad_nomad_wall_banner", LOTRBannerType.HARAD_NOMAD);
    public static final Block HARAD_NOMAD_BANNER = registerBanner("harad_nomad_banner", LOTRBannerType.HARAD_NOMAD, HARAD_NOMAD_WALL_BANNER);
    public static final Block HARAD_GULF_WALL_BANNER = registerWallBanner("harad_gulf_wall_banner", LOTRBannerType.HARAD_GULF);
    public static final Block HARAD_GULF_BANNER = registerBanner("harad_gulf_banner", LOTRBannerType.HARAD_GULF, HARAD_GULF_WALL_BANNER);
    public static final Block BREE_WALL_BANNER = registerWallBanner("bree_wall_banner", LOTRBannerType.BREE);
    public static final Block BREE_BANNER = registerBanner("bree_banner", LOTRBannerType.BREE, BREE_WALL_BANNER);

    public static final Block WEAPON_RACK = register("weapon_rack", LOTRWeaponRackBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .strength(0.5f, 1.0f)
                    .sound(SoundType.WOOD)
                    .noOcclusion()
                    .pushReaction(PushReaction.DESTROY),
            // Stack of one. LOTRItemBlockWeaponRack did NOT limit its stack --
            // racks stacked to 64 in 1.7.10 -- so this is a deliberate change.
            item -> item.stacksTo(1));

    public static final Block CLOVER = registerClover("clover", true);
    public static final Block FOUR_LEAF_CLOVER = registerClover("four_leaf_clover", false);

    public static final Block HIGH_ELVEN_WALL_TORCH = registerWallTorch("high_elven_wall_torch", LOTRGlowStyle.ELVEN_GLOW);
    public static final Block HIGH_ELVEN_TORCH = registerTorch("high_elven_torch", HIGH_ELVEN_WALL_TORCH, LOTRGlowStyle.ELVEN_GLOW);
    public static final Block WOOD_ELVEN_WALL_TORCH = registerWallTorch("wood_elven_wall_torch", LOTRGlowStyle.WOOD_ELVEN_TORCH);
    public static final Block WOOD_ELVEN_TORCH = registerTorch("wood_elven_torch", WOOD_ELVEN_WALL_TORCH, LOTRGlowStyle.WOOD_ELVEN_TORCH);
    public static final Block MORGUL_WALL_TORCH = registerWallTorch("morgul_wall_torch", LOTRGlowStyle.MORGUL);
    public static final Block MORGUL_TORCH = registerTorch("morgul_torch", MORGUL_WALL_TORCH, LOTRGlowStyle.MORGUL);
    public static final Block MALLORN_WALL_TORCH = registerWallTorch("mallorn_wall_torch", LOTRGlowStyle.MALLORN_SILVER);
    public static final Block MALLORN_TORCH = registerTorch("mallorn_torch", MALLORN_WALL_TORCH, LOTRGlowStyle.MALLORN_SILVER);
    public static final Block MALLORN_BLUE_WALL_TORCH = registerWallTorch("mallorn_blue_wall_torch", LOTRGlowStyle.MALLORN_BLUE);
    public static final Block BLUE_MALLORN_TORCH = registerTorch("blue_mallorn_torch", MALLORN_BLUE_WALL_TORCH, LOTRGlowStyle.MALLORN_BLUE);
    public static final Block MALLORN_GOLD_WALL_TORCH = registerWallTorch("mallorn_gold_wall_torch", LOTRGlowStyle.MALLORN_GOLD);
    public static final Block MALLORN_GOLD_TORCH = registerTorch("mallorn_gold_torch", MALLORN_GOLD_WALL_TORCH, LOTRGlowStyle.MALLORN_GOLD);
    public static final Block MALLORN_GREEN_WALL_TORCH = registerWallTorch("mallorn_green_wall_torch", LOTRGlowStyle.MALLORN_GREEN);
    public static final Block GREEN_MALLORN_TORCH = registerTorch("green_mallorn_torch", MALLORN_GREEN_WALL_TORCH, LOTRGlowStyle.MALLORN_GREEN);

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

    // LOTRBlockTallGrass, the one plant family the original biome-tinted:
    // colorMultiplier returned the biome grass colour, so the six textures are
    // greyscale and are coloured at render time. Everything else in ALL_FLOWERS
    // is painted in its texture -- arid grass and Mordor scrub included, which
    // is why they are not here.
    public static final List<Block> GRASS_TINTED = new ArrayList<>();

    // The three of those that also had a second, untinted overlay icon --
    // LOTRBlockTallGrass.grassOverlay = {false, true, true, true, false, false}
    // over {short, flower, wheat, thistle, nettle, fernsprout}.
    public static final List<Block> GRASS_TINTED_WITH_OVERLAY = new ArrayList<>();

    public static final Block TALL_GRASS_FERNSPROUT = registerGrass("tall_grass_fernsprout", LOTRPlantBlock.Ground.SOIL);
    public static final Block TALL_GRASS_FLOWER = registerGrass("tall_grass_flower", LOTRPlantBlock.Ground.SOIL);
    public static final Block TALL_GRASS_NETTLE = registerGrass("tall_grass_nettle", LOTRPlantBlock.Ground.SOIL, LOTRPlantBlock.Sting.NETTLE);
    public static final Block TALL_GRASS_SHORT = registerGrass("tall_grass_short", LOTRPlantBlock.Ground.SOIL);
    public static final Block TALL_GRASS_THISTLE = registerGrass("tall_grass_thistle", LOTRPlantBlock.Ground.SOIL, LOTRPlantBlock.Sting.THISTLE);
    public static final Block TALL_GRASS_WHEAT = registerGrass("tall_grass_wheat", LOTRPlantBlock.Ground.SOIL);

    // ALL_FLOWERS is a registration bucket, not a statement that everything in
    // it is a flower: the grasses, the stalks and the riverweed live there too,
    // and must stay out of #minecraft:small_flowers.
    public static final List<Block> NOT_SMALL_FLOWERS = new ArrayList<>();

    // Reeds, dried reeds and corn: plants that occupy a stack of blocks, have
    // their own hand-written models, and take a flat inventory icon of their own
    // rather than a picture of one segment.
    public static final List<Block> ALL_COLUMN_PLANTS = new ArrayList<>();

    static {
        // LOTRBlockClover.colorMultiplier returned getBiomeGrassColor too.
        GRASS_TINTED.addAll(ALL_CLOVERS);
        GRASS_TINTED.addAll(List.of(TALL_GRASS_SHORT, TALL_GRASS_FLOWER, TALL_GRASS_WHEAT,
                TALL_GRASS_THISTLE, TALL_GRASS_NETTLE, TALL_GRASS_FERNSPROUT));
        GRASS_TINTED_WITH_OVERLAY.addAll(List.of(TALL_GRASS_FLOWER, TALL_GRASS_WHEAT, TALL_GRASS_THISTLE));
    }

    public static final Block IVY = registerVine("ivy");
    public static final Block RED_IVY = registerVine("red_ivy");
    public static final Block MIRK_VINES = registerVine("mirk_vines");
    public static final Block WILLOW_VINES = registerVine("willow_vines");

    public static final Block HITHLAIN_LADDER = registerRope("hithlain_ladder", true, true);
    public static final Block MALLORN_LADDER = registerLadder("mallorn_ladder");

    // faces 0/1, sideIcon otherwise). Material.ground, no harvest level set.
    public static final Block TERMITE_MOUND = registerSoilColumn("termite_mound", 0.5f, 3.0f, SoundType.SAND, false);

    public static final Block INFESTED_TERMITE_MOUND = registerSoilColumn("infested_termite_mound", 0.5f, 3.0f, SoundType.SAND, false);
    static {
        SOIL_COLUMN_TEXTURE.put(INFESTED_TERMITE_MOUND, TERMITE_MOUND);
    }

    // colorMultiplier returned 0xFFFFFF, so it is deliberately NOT biome-tinted.
    public static final Block MUD_GRASS = registerSoilColumn("mud_grass", 0.6f, 0.6f, SoundType.GRASS, true);

    public static final Block QUENDITE_GRASS = registerSoilColumn("quendite_grass", 3.0f, 3.0f, SoundType.GRASS, false);

    public static final Block BONE_BLOCK = registerColumn("bone_block", 1.0f, 3.0f);
    static {
        TEXTURE_SOURCE.put(BONE_BLOCK, Blocks.BONE_BLOCK);
    }

    public static final Block HEARTH = registerBottomTop("hearth", 1.0f, 4.8f);

    public static final Block DAUB = registerSoftBlock("daub", 1.0f, SoundType.GRASS);

    public static final Block WASTE_BLOCK = track(SHOVEL_MINEABLE, registerSoftBlock("waste_block", 0.5f, SoundType.SAND));
    // LOTRBlockTreasurePile, NOT a plain cube: eight layer heights, the
    // shallowest of them carpet-thin. They were registered as full blocks here
    // by mistake.
    public static final Block TREASURE_COPPER = registerTreasurePile("treasure_copper");
    public static final Block TREASURE_GOLD = registerTreasurePile("treasure_gold");
    public static final Block TREASURE_SILVER = registerTreasurePile("treasure_silver");

    public static final Block DOL_AMROTH_GATE = registerWoodenGate("dol_amroth_gate");
    public static final Block DWARVEN_GATE = registerStoneGate("dwarven_gate");
    public static final Block ELVEN_GATE = registerWoodenGate("elven_gate");
    public static final Block GOLD_GATE = registerMetalGate("gold_gate");
    public static final Block GONDOR_GATE = registerWoodenGate("gondor_gate");
    public static final Block HIGH_ELVEN_GATE = registerWoodenGate("high_elven_gate");
    public static final Block BLUE_HOBBIT_GATE = registerWoodenGate("blue_hobbit_gate");
    public static final Block GREEN_HOBBIT_GATE = registerWoodenGate("green_hobbit_gate");
    public static final Block RED_HOBBIT_GATE = registerWoodenGate("red_hobbit_gate");
    public static final Block YELLOW_HOBBIT_GATE = registerWoodenGate("yellow_hobbit_gate");
    public static final Block MITHRIL_GATE = registerMetalGate("mithril_gate");
    public static final Block NEAR_HARAD_GATE = registerWoodenGate("near_harad_gate");
    public static final Block ORC_GATE = registerMetalGate("orc_gate");
    public static final Block RHUN_GATE = registerWoodenGate("rhun_gate");
    public static final Block ROHAN_GATE = registerWoodenGate("rohan_gate");
    public static final Block SILVER_GATE = registerMetalGate("silver_gate");
    public static final Block TAUREDAIN_GATE = registerWoodenGate("tauredain_gate");
    public static final Block URUK_GATE = registerMetalGate("uruk_gate");
    public static final Block WOOD_ELVEN_GATE = registerWoodenGate("wood_elven_gate");
    public static final Block WOODEN_GATE = registerWoodenGate("wooden_gate");

    // The two dwarven doors. Both are full cubes of plain stone that vanish
    // into a wall until opened, so unlike the gates above they get no
    // .noOcclusion() -- a closed one really is a solid block.
    // LOTRBlockChest. Three of them, differing only in texture and material:
    // the casket and the box are wood at hardness 2.5, the basket is cloth at
    // 0.5. None of them pair into double chests.
    public static final Block LEBETHRON_CASKET = registerChest("lebethron_casket", "lebethron",
            MapColor.WOOD, 2.5f, SoundType.WOOD);
    public static final Block REED_BASKET = registerChest("reed_basket", "basket",
            MapColor.SAND, 0.5f, SoundType.WOOL);
    public static final Block MALLORN_BOX = registerChest("mallorn_box", "mallorn",
            MapColor.WOOD, 2.5f, SoundType.WOOD);
    // The odd one out: stone rather than wood, hardness 3.0.
    public static final Block ANCIENT_HARADRIC_CHEST = registerChest("ancient_haradric_chest",
            "ancient_harad", MapColor.STONE, 3.0f, SoundType.STONE);
    // chestStone: LOTRBlockChest(Material.rock, ...), hardness 3.0, stone step
    // sound -- the same rock build as the Haradric chest, in plain grey.
    public static final Block STONE_CHEST = registerChest("stone_chest", "stone",
            MapColor.STONE, 3.0f, SoundType.STONE);

    // LOTRBlockKebabStand: Material.circuits, hardness 0, resistance 1, wood
    // step sound -- it breaks instantly and blocks nothing. The variant string
    // picks the renderer's texture: "" wooden, "sand" for Harad.
    // LOTRBlockMillstone: Material.rock, hardness 4.0, stone step sound. It has
    // no facing -- the original registers only a side and a top icon.
    public static final Block MILLSTONE = register("millstone", LOTRMillstoneBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .strength(4.0f)
                    .sound(SoundType.STONE),
            true);

    // LOTRBlockBed. Eight beds, vanilla behaviour throughout; the only thing
    // that varies is the upholstery and the planks on the underside, which the
    // original took from bedBottomBlock/bedBottomMetadata. The 1.7.10 ids are
    // in brackets where they differ from the display name.
    public static final Block GALADHRIM_BED = registerBed("galadhrim_bed");   // elvenBed
    public static final Block WOOD_ELVEN_BED = registerBed("wood_elven_bed");
    public static final Block HIGH_ELVEN_BED = registerBed("high_elven_bed");
    public static final Block DWARVEN_BED = registerBed("dwarven_bed");
    public static final Block ORC_BED = registerBed("orc_bed");
    public static final Block LION_FUR_BED = registerBed("lion_fur_bed");     // lionBed
    public static final Block FUR_BED = registerBed("fur_bed");               // wargFurBed
    public static final Block STRAW_BED = registerBed("straw_bed");

    // LOTRBlockOrcChain: Material.circuits, hardness 1.0, metal step sound. It
    // hangs from the ceiling, it is climbable, and chandeliers hang off it.
    public static final Block ORC_CHAIN = register("orc_chain", LOTROrcChainBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(1.0f)
                    .sound(SoundType.CHAIN)
                    // The chain keeps a hair-thin collision box of its own
                    // (getCollisionBoundingBoxFromPool), so this is noOcclusion
                    // rather than noCollision.
                    .noOcclusion(),
            true);

    public static final Block KEBAB_STAND = registerKebabStand("kebab_stand", "");
    public static final Block KEBAB_STAND_SAND = registerKebabStand("kebab_stand_sand", "sand");

    // LOTRBlockEntJar: Material.clay, hardness 1.0, glass step sound. It needs
    // a solid block underneath and drops when that goes.
    public static final Block ENT_JAR = register("ent_jar", LOTREntJarBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.CLAY)
                    .strength(1.0f)
                    .sound(SoundType.GLASS)
                    .noOcclusion()
                    .pushReaction(PushReaction.DESTROY),
            true);

    // The troll totem, one block per part. LOTRBlockTrollTotem: Material.rock
    // with default hardness -- it never called setHardness, so it keeps Block's
    // 0 hardness and breaks instantly, as in 1.7.10.
    public static final Block TROLL_TOTEM_HEAD = registerTrollTotem("troll_totem_head", LOTRTrollTotemBlock.Part.HEAD);
    public static final Block TROLL_TOTEM_BODY = registerTrollTotem("troll_totem_body", LOTRTrollTotemBlock.Part.BODY);
    public static final Block TROLL_TOTEM_BASE = registerTrollTotem("troll_totem_base", LOTRTrollTotemBlock.Part.BASE);

    // LOTRBlockCommandTable: Material.iron, hardness 2.5, metal step sound.
    // Its plank tabletop overhangs the block on every side, so it must not
    // occlude and its neighbours must keep drawing their faces.
    // LOTRBlockUnsmeltery extends LOTRBlockForgeBase: Material.rock, hardness
    // 4.0, stone sound -- the same numbers registerForge uses. Its cauldron is
    // drawn by a block entity renderer, so the block itself never occludes.
    public static final Block UNSMELTERY = track(CUBES_NO_TIER, register("unsmeltery", LOTRUnsmelteryBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .requiresCorrectToolForDrops()
                    .strength(4.0f, 6.0f)
                    .sound(SoundType.STONE)
                    .noOcclusion()
                    .lightLevel(state -> state.getValue(net.minecraft.world.level.block.AbstractFurnaceBlock.LIT) ? 13 : 0),
            true));

    public static final Block TABLE_OF_COMMAND = register("table_of_command", LOTRTableOfCommandBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .requiresCorrectToolForDrops()
                    .strength(2.5f)
                    .sound(SoundType.METAL)
                    .noOcclusion(),
            true);

    public static final Block DWARVEN_DOOR = registerDwarvenDoor("dwarven_door", LOTRDwarvenDoorBlock::new);
    public static final Block ITHILDIN_DWARVEN_DOOR =
            registerDwarvenDoor("ithildin_dwarven_door", LOTRIthildinDwarvenDoorBlock::new);

    public static final Block FLAX_CROP = registerCrop("flax_crop", 4);
    public static final Block LEEK_CROP = registerCrop("leek_crop", 4);
    public static final Block LETTUCE_CROP = registerCrop("lettuce_crop", 4);
    public static final Block PIPEWEED_CROP = registerCrop("pipeweed_crop", 4);
    public static final Block TURNIP_CROP = registerCrop("turnip_crop", 4);
    public static final Block YAM_CROP = registerCrop("yam_crop", 4);

    public static final Block BERRY_BUSH_BLACKBERRY = registerBush("berry_bush_blackberry");
    public static final Block BERRY_BUSH_BLUEBERRY = registerBush("berry_bush_blueberry");
    public static final Block BERRY_BUSH_CRANBERRY = registerBush("berry_bush_cranberry");
    public static final Block BERRY_BUSH_ELDERBERRY = registerBush("berry_bush_elderberry");
    public static final Block BERRY_BUSH_RASPBERRY = registerBush("berry_bush_raspberry");
    public static final Block BERRY_BUSH_WILDBERRY = registerBush("berry_bush_wildberry");

    public static final Block CORN_STALK = registerCorn("corn_stalk");
    public static final Block GRAPEVINE = registerStalk("grapevine", LOTRPlantBlock.Shape.POST, LOTRPlantBlock.Ground.STURDY_OR_SELF);
    public static final Block REEDS = registerReed("reeds", true);
    public static final Block DRIED_REEDS = registerReed("dried_reeds", false);
    public static final Block FANGORN_RIVERWEED = registerRiverweed("fangorn_riverweed");
    public static final Block WEB_UNGOLIANT = registerWeb("web_ungoliant");
    public static final Block ROPE = registerRope("rope", false, false);

    // LOTRMod: createMetal(false), createMetal(false), createWooden(false).
    // These are gates you open, not decorative bars -- registering them as
    // IronBarsBlock left three of the mod's gates unopenable.
    public static final Block GATE_BRONZE_BARS = registerMetalGate("gate_bronze_bars", false);
    public static final Block GATE_IRON_BARS = registerMetalGate("gate_iron_bars", false);
    public static final Block GATE_WOODEN_CROSS = registerWoodenGate("gate_wooden_cross", false);

    public static final Block MECHANISED_RAIL = registerRail("mechanised_rail");
    // LOTRBlockUtumnoReturnPortalBase: hardness -1 and resistance Float.MAX_VALUE, quantityDropped 0, not opaque.
    public static final Block UTUMNO_RETURN_PORTAL_BASE = register("utumno_return_portal_base",
            LOTRUtumnoReturnPortalBaseBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .strength(-1.0f, 3600000.0f)
                    .sound(SoundType.STONE)
                    .lightLevel(LOTRUtumnoReturnPortalBaseBlock::lightLevel)
                    .noOcclusion()
                    .noLootTable()
                    .pushReaction(PushReaction.BLOCK),
            true);
    // LOTRBlockUtumnoReturnLight: Material.circuits, hardness left at 0, setLightLevel(1.0f), no collision box, isReplaceable, drops nothing.
    public static final Block UTUMNO_RETURN_LIGHT = register("utumno_return_light",
            LOTRUtumnoReturnLightBlock::new,
            BlockBehaviour.Properties.of()
                    .noCollision()
                    .instabreak()
                    .replaceable()
                    .lightLevel(state -> 15)
                    .noOcclusion()
                    .noLootTable()
                    .pushReaction(PushReaction.DESTROY),
            true);
    public static final Block DIRT_PATH_MUD = registerPath("dirt_path_mud");
    public static final Block MUD_FARMLAND = registerFarmland("mud_farmland");

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

    public static final Block MORDOR_MOSS = registerCarpet("mordor_moss", 0.2f);
    public static final Block THATCH_FLOOR = registerCarpet("thatch_floor", 0.2f);

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

    public static final Block CHERRY_LEAVES = registerLeaves("cherry_leaves");
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

    public static final Block ASPHODEL = registerFlower("asphodel");
    public static final Block ATHELAS = registerFlower("athelas", LOTRPlantBlock.Shape.BROAD);
    public static final Block BLUEBELL = registerFlower("bluebell");
    public static final Block DWARF_HERB = registerFlower("dwarf_herb");
    public static final Block ELANOR = registerFlower("elanor");
    public static final Block FLAX_PLANT = registerFlower("flax_plant", LOTRPlantBlock.Shape.MEDIUM);
    public static final Block LAVENDER = registerFlower("lavender", LOTRPlantBlock.Shape.BROAD);
    public static final Block MARIGOLD = registerFlower("marigold");
    public static final Block MORGUL_FLOWER = registerFlower("morgul_flower", LOTRPlantBlock.Shape.WIDE, LOTRPlantBlock.Ground.SOIL_OR_MORDOR);
    public static final Block NIPHREDIL = registerFlower("niphredil");
    public static final Block SHIRE_HEATHER = registerFlower("shire_heather", LOTRPlantBlock.Shape.BROAD);
    public static final Block SIMBELMYNE = registerFlower("simbelmyne");
    public static final Block HARAD_FLOWER_DAISY = registerFlower("harad_flower_daisy");
    public static final Block PINK_HARAD_FLOWER = registerFlower("pink_harad_flower");
    public static final Block RED_HARAD_FLOWER = registerFlower("red_harad_flower");
    public static final Block YELLOW_HARAD_FLOWER = registerFlower("yellow_harad_flower");
    public static final Block RHUN_FLOWER_CHRYS_BLUE = registerFlower("rhun_flower_chrys_blue");
    public static final Block RHUN_FLOWER_CHRYS_ORANGE = registerFlower("rhun_flower_chrys_orange");
    public static final Block RHUN_FLOWER_CHRYS_PINK = registerFlower("rhun_flower_chrys_pink");
    public static final Block RHUN_FLOWER_CHRYS_WHITE = registerFlower("rhun_flower_chrys_white");
    public static final Block RHUN_FLOWER_CHRYS_YELLOW = registerFlower("rhun_flower_chrys_yellow");

    public static final Block ARID_GRASS = registerGrass("arid_grass", LOTRPlantBlock.Ground.SOIL_OR_SAND);
    public static final Block BLACKROOT = registerFlower("blackroot", LOTRPlantBlock.Shape.WIDE);
    public static final Block CORRUPT_MALLORN = registerFlower("corrupt_mallorn", LOTRPlantBlock.Shape.GRASS);
    public static final Block DEAD_MARSH_PLANT = registerFlower("dead_marsh_plant", LOTRPlantBlock.Shape.GRASS);
    public static final Block FANGORN_PLANT_BROWN = registerFlower("fangorn_plant_brown", LOTRPlantBlock.Shape.MEDIUM);
    public static final Block FANGORN_PLANT_GOLD = registerFlower("fangorn_plant_gold", LOTRPlantBlock.Shape.MEDIUM);
    public static final Block FANGORN_PLANT_GREEN = registerFlower("fangorn_plant_green", LOTRPlantBlock.Shape.MEDIUM);
    public static final Block FANGORN_PLANT_RED = registerFlower("fangorn_plant_red", LOTRPlantBlock.Shape.MEDIUM);
    public static final Block FANGORN_PLANT_SILVER = registerFlower("fangorn_plant_silver", LOTRPlantBlock.Shape.MEDIUM);
    public static final Block FANGORN_PLANT_YELLOW = registerFlower("fangorn_plant_yellow", LOTRPlantBlock.Shape.MEDIUM);
    public static final Block MORDOR_GRASS = registerGrass("mordor_grass", LOTRPlantBlock.Ground.MORDOR);
    public static final Block MORDOR_THORN = registerGrass("mordor_thorn", LOTRPlantBlock.Ground.MORDOR, LOTRPlantBlock.Sting.THORN);
    public static final Block MORGUL_SHROOM = registerFlower("morgul_shroom", LOTRPlantBlock.Shape.SHROOM, LOTRPlantBlock.Ground.MORDOR);
    public static final Block PIPEWEED_PLANT = registerFlower("pipeweed_plant", LOTRPlantBlock.Shape.GRASS);

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

    public static final Block ANGMAR_PILLAR = registerPillar("angmar_pillar");
    public static final Block CRACKED_ARNOR_PILLAR = registerPillar("cracked_arnor_pillar");
    public static final Block ARNOR_PILLAR = registerPillar("arnor_pillar");
    public static final Block NUMENOREAN_PILLAR = registerPillar("numenorean_pillar");
    public static final Block BLUE_ROCK_PILLAR = registerPillar("blue_rock_pillar");
    public static final Block BRICK_PILLAR = registerPillar("brick_pillar");
    public static final Block CHALK_PILLAR = registerPillar("chalk_pillar");
    public static final Block DALE_PILLAR = registerPillar("dale_pillar");
    public static final Block DOL_GULDUR_PILLAR = registerPillar("dol_guldur_pillar");
    public static final Block MOSSY_DORWINION_PILLAR = registerPillar("mossy_dorwinion_pillar");
    public static final Block DORWINION_PILLAR = registerPillar("dorwinion_pillar");
    public static final Block CRACKED_DWARVEN_PILLAR = registerPillar("cracked_dwarven_pillar");
    public static final Block DWARVEN_PILLAR = registerPillar("dwarven_pillar");
    public static final Block CRACKED_GALADHRIM_PILLAR = registerPillar("cracked_galadhrim_pillar");
    public static final Block GALADHRIM_PILLAR = registerPillar("galadhrim_pillar");
    public static final Block GONDOR_PILLAR = registerPillar("gondor_pillar");
    public static final Block CRACKED_HIGH_ELVEN_PILLAR = registerPillar("cracked_high_elven_pillar");
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
    public static final Block CRACKED_WOOD_ELVEN_PILLAR = registerPillar("cracked_wood_elven_pillar");
    public static final Block WOOD_ELVEN_PILLAR = registerPillar("wood_elven_pillar");

    public static final Block CHERRY_TRAPDOOR = registerTrapdoor("cherry_trapdoor");
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

    public static final Block CHERRY_DOOR = registerDoor("cherry_door");
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

    public static final Block BLUE_DWARVEN_CHANDELIER = registerChandelier("blue_dwarven_chandelier", LOTRGlowStyle.FLAME);
    public static final Block BRONZE_CHANDELIER = registerChandelier("bronze_chandelier", LOTRGlowStyle.FLAME);
    public static final Block DWARVEN_CHANDELIER = registerChandelier("dwarven_chandelier", LOTRGlowStyle.FLAME);
    public static final Block GOLD_CHANDELIER = registerChandelier("gold_chandelier", LOTRGlowStyle.FLAME);
    public static final Block HIGH_ELVEN_CHANDELIER = registerChandelier("high_elven_chandelier", LOTRGlowStyle.ELVEN_GLOW_STEADY);
    public static final Block IRON_CHANDELIER = registerChandelier("iron_chandelier", LOTRGlowStyle.FLAME);
    public static final Block BLUE_MALLORN_CHANDELIER = registerChandelier("blue_mallorn_chandelier", LOTRGlowStyle.MALLORN_BLUE);
    public static final Block MALLORN_GOLD_CHANDELIER = registerChandelier("mallorn_gold_chandelier", LOTRGlowStyle.MALLORN_GOLD);
    public static final Block GREEN_MALLORN_CHANDELIER = registerChandelier("green_mallorn_chandelier", LOTRGlowStyle.MALLORN_GREEN);
    public static final Block SILVER_MALLORN_CHANDELIER = registerChandelier("silver_mallorn_chandelier", LOTRGlowStyle.MALLORN_SILVER);
    public static final Block MITHRIL_CHANDELIER = registerChandelier("mithril_chandelier", LOTRGlowStyle.FLAME);
    public static final Block MORGUL_CHANDELIER = registerChandelier("morgul_chandelier", LOTRGlowStyle.MORGUL);
    public static final Block ORC_CHANDELIER = registerChandelier("orc_chandelier", LOTRGlowStyle.FLAME);
    public static final Block SILVER_CHANDELIER = registerChandelier("silver_chandelier", LOTRGlowStyle.FLAME);
    public static final Block URUK_CHANDELIER = registerChandelier("uruk_chandelier", LOTRGlowStyle.FLAME);
    public static final Block WOOD_ELVEN_CHANDELIER = registerChandelier("wood_elven_chandelier", LOTRGlowStyle.WOOD_ELVEN_CHANDELIER);

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

    public static final Block BLACK_STAINED_GLASS_PANE = registerGlassPane("black_stained_glass_pane", BLACK_STAINED_GLASS);
    public static final Block BLUE_STAINED_GLASS_PANE = registerGlassPane("blue_stained_glass_pane", BLUE_STAINED_GLASS);
    public static final Block BROWN_STAINED_GLASS_PANE = registerGlassPane("brown_stained_glass_pane", BROWN_STAINED_GLASS);
    public static final Block CYAN_STAINED_GLASS_PANE = registerGlassPane("cyan_stained_glass_pane", CYAN_STAINED_GLASS);
    public static final Block GRAY_STAINED_GLASS_PANE = registerGlassPane("gray_stained_glass_pane", GRAY_STAINED_GLASS);
    public static final Block GREEN_STAINED_GLASS_PANE = registerGlassPane("green_stained_glass_pane", GREEN_STAINED_GLASS);
    public static final Block LIGHT_BLUE_STAINED_GLASS_PANE = registerGlassPane("light_blue_stained_glass_pane", LIGHT_BLUE_STAINED_GLASS);
    public static final Block LIME_STAINED_GLASS_PANE = registerGlassPane("lime_stained_glass_pane", LIME_STAINED_GLASS);
    public static final Block MAGENTA_STAINED_GLASS_PANE = registerGlassPane("magenta_stained_glass_pane", MAGENTA_STAINED_GLASS);
    public static final Block ORANGE_STAINED_GLASS_PANE = registerGlassPane("orange_stained_glass_pane", ORANGE_STAINED_GLASS);
    public static final Block PINK_STAINED_GLASS_PANE = registerGlassPane("pink_stained_glass_pane", PINK_STAINED_GLASS);
    public static final Block PURPLE_STAINED_GLASS_PANE = registerGlassPane("purple_stained_glass_pane", PURPLE_STAINED_GLASS);
    public static final Block RED_STAINED_GLASS_PANE = registerGlassPane("red_stained_glass_pane", RED_STAINED_GLASS);
    public static final Block SILVER_STAINED_GLASS_PANE = registerGlassPane("silver_stained_glass_pane", SILVER_STAINED_GLASS);
    public static final Block WHITE_STAINED_GLASS_PANE = registerGlassPane("white_stained_glass_pane", WHITE_STAINED_GLASS);
    public static final Block YELLOW_STAINED_GLASS_PANE = registerGlassPane("yellow_stained_glass_pane", YELLOW_STAINED_GLASS);
    public static final Block GLASS_PANE = registerGlassPane("glass_pane", GLASS);

    public static final Block ALMOND_STAIRS = registerStairs("almond_stairs", ALMOND_PLANKS);
    public static final Block ANGMAR_BRICK_STAIRS = registerStairs("angmar_brick_stairs", ANGMAR_BRICK);
    public static final Block CRACKED_ANGMAR_BRICK_STAIRS = registerStairs("cracked_angmar_brick_stairs", CRACKED_ANGMAR_BRICK);
    public static final Block ANGMAR_SNOW_BRICK_STAIRS = registerStairs("angmar_snow_brick_stairs", ANGMAR_SNOW_BRICK);
    public static final Block APPLE_STAIRS = registerStairs("apple_stairs", APPLE_PLANKS);
    public static final Block ARNOR_BRICK_STAIRS = registerStairs("arnor_brick_stairs", ARNOR_BRICK);
    public static final Block CRACKED_ARNOR_BRICK_STAIRS = registerStairs("cracked_arnor_brick_stairs", CRACKED_ARNOR_BRICK);
    public static final Block MOSSY_ARNOR_BRICK_STAIRS = registerStairs("mossy_arnor_brick_stairs", MOSSY_ARNOR_BRICK);
    public static final Block ASPEN_STAIRS = registerStairs("aspen_stairs", ASPEN_PLANKS);
    public static final Block BANANA_STAIRS = registerStairs("banana_stairs", BANANA_PLANKS);
    public static final Block BAOBAB_STAIRS = registerStairs("baobab_stairs", BAOBAB_PLANKS);
    public static final Block BEECH_STAIRS = registerStairs("beech_stairs", BEECH_PLANKS);
    public static final Block NUMENOREAN_BRICK_STAIRS = registerStairs("numenorean_brick_stairs", NUMENOREAN_BRICK);
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
    public static final Block CRACKED_DOL_GULDUR_BRICK_STAIRS = registerStairs("cracked_dol_guldur_brick_stairs", CRACKED_DOL_GULDUR_BRICK);
    public static final Block MOSSY_DOL_GULDUR_BRICK_STAIRS = registerStairs("mossy_dol_guldur_brick_stairs", MOSSY_DOL_GULDUR_BRICK);
    public static final Block DORWINION_BRICK_STAIRS = registerStairs("dorwinion_brick_stairs", DORWINION_BRICK);
    public static final Block CRACKED_DORWINION_BRICK_STAIRS = registerStairs("cracked_dorwinion_brick_stairs", CRACKED_DORWINION_BRICK);
    public static final Block DORWINION_FLOWERS_BRICK_STAIRS = registerStairs("dorwinion_flowers_brick_stairs", DORWINION_FLOWERS_BRICK);
    public static final Block MOSSY_DORWINION_BRICK_STAIRS = registerStairs("mossy_dorwinion_brick_stairs", MOSSY_DORWINION_BRICK);
    public static final Block DRAGON_STAIRS = registerStairs("dragon_stairs", DRAGON_PLANKS);
    public static final Block DWARVEN_BRICK_STAIRS = registerStairs("dwarven_brick_stairs", DWARVEN_BRICK);
    public static final Block CRACKED_DWARVEN_BRICK_STAIRS = registerStairs("cracked_dwarven_brick_stairs", CRACKED_DWARVEN_BRICK);
    public static final Block OBSIDIAN_DWARVEN_BRICK_STAIRS = registerStairs("obsidian_dwarven_brick_stairs", OBSIDIAN_DWARVEN_BRICK);
    public static final Block FIR_STAIRS = registerStairs("fir_stairs", FIR_PLANKS);
    public static final Block GALADHRIM_BRICK_STAIRS = registerStairs("galadhrim_brick_stairs", GALADHRIM_BRICK);
    public static final Block CRACKED_GALADHRIM_BRICK_STAIRS = registerStairs("cracked_galadhrim_brick_stairs", CRACKED_GALADHRIM_BRICK);
    public static final Block MOSSY_GALADHRIM_BRICK_STAIRS = registerStairs("mossy_galadhrim_brick_stairs", MOSSY_GALADHRIM_BRICK);
    public static final Block GONDOR_BRICK_STAIRS = registerStairs("gondor_brick_stairs", GONDOR_BRICK);
    public static final Block CRACKED_GONDOR_BRICK_STAIRS = registerStairs("cracked_gondor_brick_stairs", CRACKED_GONDOR_BRICK);
    public static final Block MOSSY_GONDOR_BRICK_STAIRS = registerStairs("mossy_gondor_brick_stairs", MOSSY_GONDOR_BRICK);
    public static final Block GONDOR_ROCK_STAIRS = registerStairs("gondor_rock_stairs", GONDOR_ROCK);
    public static final Block GONDOR_COBBLEBRICK_STAIRS = registerStairs("gondor_cobblebrick_stairs", GONDOR_COBBLEBRICK);
    public static final Block CRACKED_GONDOR_COBBLEBRICK_STAIRS = registerStairs("cracked_gondor_cobblebrick_stairs", CRACKED_GONDOR_COBBLEBRICK);
    public static final Block MOSSY_GONDOR_COBBLEBRICK_STAIRS = registerStairs("mossy_gondor_cobblebrick_stairs", MOSSY_GONDOR_COBBLEBRICK);
    public static final Block GREEN_OAK_STAIRS = registerStairs("green_oak_stairs", GREEN_OAK_PLANKS);
    public static final Block HIGH_ELVEN_BRICK_STAIRS = registerStairs("high_elven_brick_stairs", HIGH_ELVEN_BRICK);
    public static final Block CRACKED_HIGH_ELVEN_BRICK_STAIRS = registerStairs("cracked_high_elven_brick_stairs", CRACKED_HIGH_ELVEN_BRICK);
    public static final Block MOSSY_HIGH_ELVEN_BRICK_STAIRS = registerStairs("mossy_high_elven_brick_stairs", MOSSY_HIGH_ELVEN_BRICK);
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
    public static final Block CRACKED_MORDOR_BRICK_STAIRS = registerStairs("cracked_mordor_brick_stairs", CRACKED_MORDOR_BRICK);
    public static final Block MORDOR_ROCK_STAIRS = registerStairs("mordor_rock_stairs", MORDOR_ROCK);
    public static final Block MORWAITH_BRICK_STAIRS = registerStairs("morwaith_brick_stairs", MORWAITH_BRICK);
    public static final Block CRACKED_MORWAITH_BRICK_STAIRS = registerStairs("cracked_morwaith_brick_stairs", CRACKED_MORWAITH_BRICK);
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
    public static final Block CRACKED_UMBAR_BRICK_STAIRS = registerStairs("cracked_umbar_brick_stairs", CRACKED_UMBAR_BRICK);
    public static final Block URUK_BRICK_STAIRS = registerStairs("uruk_brick_stairs", URUK_BRICK);
    public static final Block FIRE_UTUMNO_BRICK_STAIRS = registerStairs("fire_utumno_brick_stairs", FIRE_UTUMNO_BRICK);
    public static final Block UTUMNO_FIRE_TILE_BRICK_STAIRS = registerStairs("utumno_fire_tile_brick_stairs", UTUMNO_FIRE_TILE_BRICK);
    public static final Block ICE_UTUMNO_BRICK_STAIRS = registerStairs("ice_utumno_brick_stairs", ICE_UTUMNO_BRICK);
    public static final Block UTUMNO_ICE_TILE_BRICK_STAIRS = registerStairs("utumno_ice_tile_brick_stairs", UTUMNO_ICE_TILE_BRICK);
    public static final Block OBSIDIAN_UTUMNO_BRICK_STAIRS = registerStairs("obsidian_utumno_brick_stairs", OBSIDIAN_UTUMNO_BRICK);
    public static final Block UTUMNO_OBSIDIAN_TILE_BRICK_STAIRS = registerStairs("utumno_obsidian_tile_brick_stairs", UTUMNO_OBSIDIAN_TILE_BRICK);
    public static final Block WHITE_SANDSTONE_STAIRS = registerStairs("white_sandstone_stairs", WHITE_SANDSTONE);
    public static final Block WILLOW_STAIRS = registerStairs("willow_stairs", WILLOW_PLANKS);
    public static final Block WOOD_ELVEN_BRICK_STAIRS = registerStairs("wood_elven_brick_stairs", WOOD_ELVEN_BRICK);
    public static final Block CRACKED_WOOD_ELVEN_BRICK_STAIRS = registerStairs("cracked_wood_elven_brick_stairs", CRACKED_WOOD_ELVEN_BRICK);
    public static final Block MOSSY_WOOD_ELVEN_BRICK_STAIRS = registerStairs("mossy_wood_elven_brick_stairs", MOSSY_WOOD_ELVEN_BRICK);

    // Stairs cut from an existing block. Takes its properties from the base block, so a brick stair is as tough as its brick and a wooden stair burns like its planks -- which is what the 1.7.10 LOTRBlockStairs(block, meta) constructor did. The base is recorded in STAIRS_BASE so datagen can find the texture to use. NOTE: these fields must be declared AFTER every base block, since they dereference them during static init.
    private static Block registerStairs(String name, Block base) {
        Block stairs = register(name, props -> new StairBlock(base.defaultBlockState(), props),
                BlockBehaviour.Properties.ofFullCopy(base), true);
        ALL_STAIRS.add(stairs);
        STAIRS_BASE.put(stairs, base);
        return stairs;
    }

    public static final Block CHERRY_STAIRS = registerStairs("cherry_stairs", CHERRY_PLANKS);
    public static final Block DRYSTONE_STAIRS = registerStairs("drystone_stairs", DRYSTONE);
    public static final Block BONE_STAIRS = registerStairs("bone_stairs", BONE_BLOCK);
    public static final Block CLAY_TILE_DYED_SILVER_STAIRS = registerStairs("clay_tile_dyed_silver_stairs", CLAY_TILE_DYED_SILVER);
    public static final Block CRACKED_STONE_BRICK_STAIRS = registerStairs("cracked_stone_brick_stairs", Blocks.CRACKED_STONE_BRICKS);

    public static final Block ANGMAR_BRICK_SLAB = registerSlab("angmar_brick_slab", ANGMAR_BRICK);
    public static final Block CRACKED_ANGMAR_BRICK_SLAB = registerSlab("cracked_angmar_brick_slab", CRACKED_ANGMAR_BRICK);
    public static final Block ANGMAR_PILLAR_SLAB = registerSlab("angmar_pillar_slab", ANGMAR_PILLAR);
    public static final Block ANGMAR_SNOW_BRICK_SLAB = registerSlab("angmar_snow_brick_slab", ANGMAR_SNOW_BRICK);
    public static final Block ARNOR_BRICK_SLAB = registerSlab("arnor_brick_slab", ARNOR_BRICK);
    public static final Block CRACKED_ARNOR_BRICK_SLAB = registerSlab("cracked_arnor_brick_slab", CRACKED_ARNOR_BRICK);
    public static final Block CRACKED_ARNOR_PILLAR_SLAB = registerSlab("cracked_arnor_pillar_slab", CRACKED_ARNOR_PILLAR);
    public static final Block MOSSY_ARNOR_BRICK_SLAB = registerSlab("mossy_arnor_brick_slab", MOSSY_ARNOR_BRICK);
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
    public static final Block CRACKED_DOL_GULDUR_BRICK_SLAB = registerSlab("cracked_dol_guldur_brick_slab", CRACKED_DOL_GULDUR_BRICK);
    public static final Block MOSSY_DOL_GULDUR_BRICK_SLAB = registerSlab("mossy_dol_guldur_brick_slab", MOSSY_DOL_GULDUR_BRICK);
    public static final Block DOL_GULDUR_PILLAR_SLAB = registerSlab("dol_guldur_pillar_slab", DOL_GULDUR_PILLAR);
    public static final Block DORWINION_BRICK_SLAB = registerSlab("dorwinion_brick_slab", DORWINION_BRICK);
    public static final Block CRACKED_DORWINION_BRICK_SLAB = registerSlab("cracked_dorwinion_brick_slab", CRACKED_DORWINION_BRICK);
    public static final Block DORWINION_FLOWERS_BRICK_SLAB = registerSlab("dorwinion_flowers_brick_slab", DORWINION_FLOWERS_BRICK);
    public static final Block MOSSY_DORWINION_BRICK_SLAB = registerSlab("mossy_dorwinion_brick_slab", MOSSY_DORWINION_BRICK);
    public static final Block MOSSY_DORWINION_PILLAR_SLAB = registerSlab("mossy_dorwinion_pillar_slab", MOSSY_DORWINION_PILLAR);
    public static final Block DORWINION_PILLAR_SLAB = registerSlab("dorwinion_pillar_slab", DORWINION_PILLAR);
    public static final Block DWARVEN_BRICK_SLAB = registerSlab("dwarven_brick_slab", DWARVEN_BRICK);
    public static final Block CRACKED_DWARVEN_BRICK_SLAB = registerSlab("cracked_dwarven_brick_slab", CRACKED_DWARVEN_BRICK);
    public static final Block CRACKED_DWARVEN_PILLAR_SLAB = registerSlab("cracked_dwarven_pillar_slab", CRACKED_DWARVEN_PILLAR);
    public static final Block OBSIDIAN_DWARVEN_BRICK_SLAB = registerSlab("obsidian_dwarven_brick_slab", OBSIDIAN_DWARVEN_BRICK);
    public static final Block DWARVEN_PILLAR_SLAB = registerSlab("dwarven_pillar_slab", DWARVEN_PILLAR);
    public static final Block GALADHRIM_BRICK_SLAB = registerSlab("galadhrim_brick_slab", GALADHRIM_BRICK);
    public static final Block CRACKED_GALADHRIM_BRICK_SLAB = registerSlab("cracked_galadhrim_brick_slab", CRACKED_GALADHRIM_BRICK);
    public static final Block CRACKED_GALADHRIM_PILLAR_SLAB = registerSlab("cracked_galadhrim_pillar_slab", CRACKED_GALADHRIM_PILLAR);
    public static final Block MOSSY_GALADHRIM_BRICK_SLAB = registerSlab("mossy_galadhrim_brick_slab", MOSSY_GALADHRIM_BRICK);
    public static final Block GALADHRIM_PILLAR_SLAB = registerSlab("galadhrim_pillar_slab", GALADHRIM_PILLAR);
    public static final Block GONDOR_BRICK_SLAB = registerSlab("gondor_brick_slab", GONDOR_BRICK);
    public static final Block CRACKED_GONDOR_BRICK_SLAB = registerSlab("cracked_gondor_brick_slab", CRACKED_GONDOR_BRICK);
    public static final Block MOSSY_GONDOR_BRICK_SLAB = registerSlab("mossy_gondor_brick_slab", MOSSY_GONDOR_BRICK);
    public static final Block GONDOR_PILLAR_SLAB = registerSlab("gondor_pillar_slab", GONDOR_PILLAR);
    public static final Block GONDOR_ROCK_SLAB = registerSlab("gondor_rock_slab", GONDOR_ROCK);
    public static final Block HIGH_ELVEN_BRICK_SLAB = registerSlab("high_elven_brick_slab", HIGH_ELVEN_BRICK);
    public static final Block CRACKED_HIGH_ELVEN_BRICK_SLAB = registerSlab("cracked_high_elven_brick_slab", CRACKED_HIGH_ELVEN_BRICK);
    public static final Block CRACKED_HIGH_ELVEN_PILLAR_SLAB = registerSlab("cracked_high_elven_pillar_slab", CRACKED_HIGH_ELVEN_PILLAR);
    public static final Block MOSSY_HIGH_ELVEN_BRICK_SLAB = registerSlab("mossy_high_elven_brick_slab", MOSSY_HIGH_ELVEN_BRICK);
    public static final Block HIGH_ELVEN_PILLAR_SLAB = registerSlab("high_elven_pillar_slab", HIGH_ELVEN_PILLAR);
    public static final Block MORDOR_BRICK_SLAB = registerSlab("mordor_brick_slab", MORDOR_BRICK);
    public static final Block CRACKED_MORDOR_BRICK_SLAB = registerSlab("cracked_mordor_brick_slab", CRACKED_MORDOR_BRICK);
    public static final Block MORDOR_DIRT_SLAB = registerSlab("mordor_dirt_slab", MORDOR_DIRT);
    public static final Block MORDOR_GRAVEL_SLAB = registerSlab("mordor_gravel_slab", MORDOR_GRAVEL);
    public static final Block MORDOR_PILLAR_SLAB = registerSlab("mordor_pillar_slab", MORDOR_PILLAR);
    public static final Block MORDOR_ROCK_SLAB = registerSlab("mordor_rock_slab", MORDOR_ROCK);
    public static final Block CRACKED_MORWAITH_BRICK_SLAB = registerSlab("cracked_morwaith_brick_slab", CRACKED_MORWAITH_BRICK);
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
    public static final Block CRACKED_UMBAR_BRICK_SLAB = registerSlab("cracked_umbar_brick_slab", CRACKED_UMBAR_BRICK);
    public static final Block UMBAR_PILLAR_SLAB = registerSlab("umbar_pillar_slab", UMBAR_PILLAR);
    public static final Block URUK_BRICK_SLAB = registerSlab("uruk_brick_slab", URUK_BRICK);
    public static final Block URUK_PILLAR_SLAB = registerSlab("uruk_pillar_slab", URUK_PILLAR);
    public static final Block FIRE_UTUMNO_BRICK_SLAB = registerSlab("fire_utumno_brick_slab", FIRE_UTUMNO_BRICK);
    public static final Block ICE_UTUMNO_BRICK_SLAB = registerSlab("ice_utumno_brick_slab", ICE_UTUMNO_BRICK);
    public static final Block OBSIDIAN_UTUMNO_BRICK_SLAB = registerSlab("obsidian_utumno_brick_slab", OBSIDIAN_UTUMNO_BRICK);
    public static final Block WHITE_SAND_SLAB = registerSlab("white_sand_slab", WHITE_SAND);
    public static final Block WHITE_SANDSTONE_SLAB = registerSlab("white_sandstone_slab", WHITE_SANDSTONE);
    public static final Block WOOD_ELVEN_BRICK_SLAB = registerSlab("wood_elven_brick_slab", WOOD_ELVEN_BRICK);
    public static final Block CRACKED_WOOD_ELVEN_BRICK_SLAB = registerSlab("cracked_wood_elven_brick_slab", CRACKED_WOOD_ELVEN_BRICK);
    public static final Block CRACKED_WOOD_ELVEN_PILLAR_SLAB = registerSlab("cracked_wood_elven_pillar_slab", CRACKED_WOOD_ELVEN_PILLAR);
    public static final Block MOSSY_WOOD_ELVEN_BRICK_SLAB = registerSlab("mossy_wood_elven_brick_slab", MOSSY_WOOD_ELVEN_BRICK);
    public static final Block WOOD_ELVEN_PILLAR_SLAB = registerSlab("wood_elven_pillar_slab", WOOD_ELVEN_PILLAR);

    // Slab cut from an existing block. One modern SlabBlock replaces the 1.7.10 single/double pair, since SlabType covers bottom, top and double. Like stairs, these must be declared AFTER every base block.
    private static Block registerSlab(String name, Block base) {
        Block slab = register(name, SlabBlock::new,
                BlockBehaviour.Properties.ofFullCopy(base), true);
        ALL_SLABS.add(slab);
        SLAB_BASE.put(slab, base);
        return slab;
    }

    // Cut from vanilla gravel: lotr:gravel was removed as a duplicate. */
    public static final Block GRAVEL_SLAB = registerSlab("gravel_slab", Blocks.GRAVEL);

    public static final Block SMOOTH_MORDOR_ROCK_SLAB = registerSlab("smooth_mordor_rock_slab", SMOOTH_MORDOR_ROCK);

    public static final Block SMOOTH_GONDOR_ROCK_SLAB = registerSlab("smooth_gondor_rock_slab", SMOOTH_GONDOR_ROCK);

    public static final Block SMOOTH_ROHAN_ROCK_SLAB = registerSlab("smooth_rohan_rock_slab", SMOOTH_ROHAN_ROCK);

    public static final Block SMOOTH_BLUE_ROCK_SLAB = registerSlab("smooth_blue_rock_slab", SMOOTH_BLUE_ROCK);

    public static final Block SMOOTH_RED_ROCK_SLAB = registerSlab("smooth_red_rock_slab", SMOOTH_RED_ROCK);

    public static final Block SMOOTH_CHALK_SLAB = registerSlab("smooth_chalk_slab", SMOOTH_CHALK);

    public static final Block DRYSTONE_SLAB = registerSlab("drystone_slab", DRYSTONE);

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
    public static final Block LAIRELOSSE_FENCE = registerFence("lairelosse_fence", LAIRELOSSE_PLANKS);
    public static final Block ALMOND_FENCE = registerFence("almond_fence", ALMOND_PLANKS);
    public static final Block ROTTEN_FENCE = registerFence("rotten_fence", ROTTEN_PLANKS);
    public static final Block PLUM_FENCE = registerFence("plum_fence", PLUM_PLANKS);
    public static final Block REDWOOD_FENCE = registerFence("redwood_fence", REDWOOD_PLANKS);
    public static final Block POMEGRANATE_FENCE = registerFence("pomegranate_fence", POMEGRANATE_PLANKS);
    public static final Block PALM_FENCE = registerFence("palm_fence", PALM_PLANKS);
    public static final Block DRAGON_FENCE = registerFence("dragon_fence", DRAGON_PLANKS);
    public static final Block KANUKA_FENCE = registerFence("kanuka_fence", KANUKA_PLANKS);

    public static final Block MORDOR_ROCK_WALL = registerWall("mordor_rock_wall", MORDOR_ROCK);
    public static final Block MORDOR_BRICK_WALL = registerWall("mordor_brick_wall", MORDOR_BRICK);
    public static final Block GONDOR_ROCK_WALL = registerWall("gondor_rock_wall", GONDOR_ROCK);
    public static final Block GONDOR_BRICK_WALL = registerWall("gondor_brick_wall", GONDOR_BRICK);
    public static final Block MOSSY_GONDOR_BRICK_WALL = registerWall("mossy_gondor_brick_wall", MOSSY_GONDOR_BRICK);
    public static final Block CRACKED_GONDOR_BRICK_WALL = registerWall("cracked_gondor_brick_wall", CRACKED_GONDOR_BRICK);
    public static final Block ROHAN_BRICK_WALL = registerWall("rohan_brick_wall", ROHAN_BRICK);
    public static final Block DWARVEN_BRICK_WALL = registerWall("dwarven_brick_wall", DWARVEN_BRICK);
    public static final Block ROHAN_ROCK_WALL = registerWall("rohan_rock_wall", ROHAN_ROCK);
    public static final Block CRACKED_MORDOR_BRICK_WALL = registerWall("cracked_mordor_brick_wall", CRACKED_MORDOR_BRICK);
    public static final Block GALADHRIM_BRICK_WALL = registerWall("galadhrim_brick_wall", GALADHRIM_BRICK);
    public static final Block MOSSY_GALADHRIM_BRICK_WALL = registerWall("mossy_galadhrim_brick_wall", MOSSY_GALADHRIM_BRICK);
    public static final Block CRACKED_GALADHRIM_BRICK_WALL = registerWall("cracked_galadhrim_brick_wall", CRACKED_GALADHRIM_BRICK);
    public static final Block BLUE_ROCK_WALL = registerWall("blue_rock_wall", BLUE_ROCK);
    public static final Block BLUE_ROCK_BRICK_WALL = registerWall("blue_rock_brick_wall", BLUE_ROCK_BRICK);
    public static final Block NEAR_HARAD_BRICK_WALL = registerWall("near_harad_brick_wall", NEAR_HARAD_BRICK);
    public static final Block ANGMAR_BRICK_WALL = registerWall("angmar_brick_wall", ANGMAR_BRICK);
    public static final Block CRACKED_ANGMAR_BRICK_WALL = registerWall("cracked_angmar_brick_wall", CRACKED_ANGMAR_BRICK);
    public static final Block RED_ROCK_WALL = registerWall("red_rock_wall", RED_ROCK);
    public static final Block RED_ROCK_BRICK_WALL = registerWall("red_rock_brick_wall", RED_ROCK_BRICK);
    public static final Block ARNOR_BRICK_WALL = registerWall("arnor_brick_wall", ARNOR_BRICK);
    public static final Block MOSSY_ARNOR_BRICK_WALL = registerWall("mossy_arnor_brick_wall", MOSSY_ARNOR_BRICK);
    public static final Block CRACKED_ARNOR_BRICK_WALL = registerWall("cracked_arnor_brick_wall", CRACKED_ARNOR_BRICK);
    public static final Block URUK_BRICK_WALL = registerWall("uruk_brick_wall", URUK_BRICK);
    public static final Block DOL_GULDUR_BRICK_WALL = registerWall("dol_guldur_brick_wall", DOL_GULDUR_BRICK);
    public static final Block CRACKED_DOL_GULDUR_BRICK_WALL = registerWall("cracked_dol_guldur_brick_wall", CRACKED_DOL_GULDUR_BRICK);
    public static final Block HIGH_ELVEN_BRICK_WALL = registerWall("high_elven_brick_wall", HIGH_ELVEN_BRICK);
    public static final Block MOSSY_HIGH_ELVEN_BRICK_WALL = registerWall("mossy_high_elven_brick_wall", MOSSY_HIGH_ELVEN_BRICK);
    public static final Block CRACKED_HIGH_ELVEN_BRICK_WALL = registerWall("cracked_high_elven_brick_wall", CRACKED_HIGH_ELVEN_BRICK);
    public static final Block DOL_AMROTH_BRICK_WALL = registerWall("dol_amroth_brick_wall", DOL_AMROTH_BRICK);
    public static final Block NEAR_HARAD_CRACKED_BRICK_WALL = registerWall("near_harad_cracked_brick_wall", NEAR_HARAD_CRACKED_BRICK);
    public static final Block NEAR_HARAD_RED_BRICK_WALL = registerWall("near_harad_red_brick_wall", NEAR_HARAD_RED_BRICK);
    public static final Block NEAR_HARAD_RED_CRACKED_BRICK_WALL = registerWall("near_harad_red_cracked_brick_wall", NEAR_HARAD_RED_CRACKED_BRICK);
    public static final Block CHALK_WALL = registerWall("chalk_wall", CHALK);
    public static final Block CHALK_BRICK_WALL = registerWall("chalk_brick_wall", CHALK_BRICK);
    public static final Block MUD_BRICK_WALL = registerWall("mud_brick_wall", MUD_BRICK);
    public static final Block DALE_BRICK_WALL = registerWall("dale_brick_wall", DALE_BRICK);
    public static final Block DORWINION_BRICK_WALL = registerWall("dorwinion_brick_wall", DORWINION_BRICK);
    public static final Block MOSSY_DORWINION_BRICK_WALL = registerWall("mossy_dorwinion_brick_wall", MOSSY_DORWINION_BRICK);
    public static final Block CRACKED_DORWINION_BRICK_WALL = registerWall("cracked_dorwinion_brick_wall", CRACKED_DORWINION_BRICK);
    public static final Block DORWINION_FLOWERS_BRICK_WALL = registerWall("dorwinion_flowers_brick_wall", DORWINION_FLOWERS_BRICK);
    public static final Block WHITE_SANDSTONE_WALL = registerWall("white_sandstone_wall", WHITE_SANDSTONE);
    public static final Block RHUN_BRICK_WALL = registerWall("rhun_brick_wall", RHUN_BRICK);
    public static final Block TAUREDAIN_BRICK_WALL = registerWall("tauredain_brick_wall", TAUREDAIN_BRICK);
    public static final Block TAUREDAIN_MOSSY_BRICK_WALL = registerWall("tauredain_mossy_brick_wall", TAUREDAIN_MOSSY_BRICK);
    public static final Block TAUREDAIN_CRACKED_BRICK_WALL = registerWall("tauredain_cracked_brick_wall", TAUREDAIN_CRACKED_BRICK);
    public static final Block TAUREDAIN_GOLD_BRICK_WALL = registerWall("tauredain_gold_brick_wall", TAUREDAIN_GOLD_BRICK);
    public static final Block TAUREDAIN_OBSIDIAN_BRICK_WALL = registerWall("tauredain_obsidian_brick_wall", TAUREDAIN_OBSIDIAN_BRICK);
    public static final Block CRACKED_DWARVEN_BRICK_WALL = registerWall("cracked_dwarven_brick_wall", CRACKED_DWARVEN_BRICK);
    public static final Block OBSIDIAN_DWARVEN_BRICK_WALL = registerWall("obsidian_dwarven_brick_wall", OBSIDIAN_DWARVEN_BRICK);
    public static final Block RHUN_MOSSY_BRICK_WALL = registerWall("rhun_mossy_brick_wall", RHUN_MOSSY_BRICK);
    public static final Block RHUN_CRACKED_BRICK_WALL = registerWall("rhun_cracked_brick_wall", RHUN_CRACKED_BRICK);
    public static final Block RHUN_FLOWERS_BRICK_WALL = registerWall("rhun_flowers_brick_wall", RHUN_FLOWERS_BRICK);
    public static final Block RHUN_RED_BRICK_WALL = registerWall("rhun_red_brick_wall", RHUN_RED_BRICK);
    public static final Block DALE_MOSSY_BRICK_WALL = registerWall("dale_mossy_brick_wall", DALE_MOSSY_BRICK);
    public static final Block DALE_CRACKED_BRICK_WALL = registerWall("dale_cracked_brick_wall", DALE_CRACKED_BRICK);
    public static final Block FIRE_UTUMNO_BRICK_WALL = registerWall("fire_utumno_brick_wall", FIRE_UTUMNO_BRICK);
    public static final Block ICE_UTUMNO_BRICK_WALL = registerWall("ice_utumno_brick_wall", ICE_UTUMNO_BRICK);
    public static final Block OBSIDIAN_UTUMNO_BRICK_WALL = registerWall("obsidian_utumno_brick_wall", OBSIDIAN_UTUMNO_BRICK);
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
    public static final Block CLAY_TILE_DYED_SILVER_WALL = registerWall("clay_tile_dyed_silver_wall", CLAY_TILE_DYED_SILVER);
    public static final Block UMBAR_BRICK_WALL = registerWall("umbar_brick_wall", UMBAR_BRICK);
    public static final Block CRACKED_UMBAR_BRICK_WALL = registerWall("cracked_umbar_brick_wall", CRACKED_UMBAR_BRICK);
    public static final Block ANGMAR_SNOW_BRICK_WALL = registerWall("angmar_snow_brick_wall", ANGMAR_SNOW_BRICK);
    public static final Block MOSSY_DOL_GULDUR_BRICK_WALL = registerWall("mossy_dol_guldur_brick_wall", MOSSY_DOL_GULDUR_BRICK);
    public static final Block CRACKED_MORWAITH_BRICK_WALL = registerWall("cracked_morwaith_brick_wall", CRACKED_MORWAITH_BRICK);

    // Fence cut from an existing block. Must be declared AFTER every base. */
    private static Block registerFence(String name, Block base) {
        Block fence = register(name, FenceBlock::new,
                BlockBehaviour.Properties.ofFullCopy(base), true);
        ALL_FENCES.add(fence);
        FENCE_BASE.put(fence, base);
        return fence;
    }

    // Wall cut from an existing block. Must be declared AFTER every base. */
    private static Block registerWall(String name, Block base) {
        Block wall = register(name, WallBlock::new,
                BlockBehaviour.Properties.ofFullCopy(base), true);
        ALL_WALLS.add(wall);
        WALL_BASE.put(wall, base);
        return wall;
    }
    public static final Block DRYSTONE_WALL = registerWall("drystone_wall", DRYSTONE);
    public static final Block BONE_WALL = registerWall("bone_wall", BONE_BLOCK);
    public static final Block SCORCHED_STONE_WALL = registerWall("scorched_stone_wall", SCORCHED_STONE);
    public static final Block MORWAITH_BRICK_WALL = registerWall("morwaith_brick_wall", MORWAITH_BRICK);
    public static final Block NUMENOREAN_BRICK_WALL = registerWall("numenorean_brick_wall", NUMENOREAN_BRICK);
    public static final Block GONDOR_COBBLEBRICK_WALL = registerWall("gondor_cobblebrick_wall", GONDOR_COBBLEBRICK);
    public static final Block CLAY_TILE_WALL = registerWall("clay_tile_wall", CLAY_TILE);
    public static final Block RED_BRICK_MOSSY_WALL = registerWall("red_brick_mossy_wall", RED_BRICK_MOSSY);
    public static final Block RED_BRICK_CRACKED_WALL = registerWall("red_brick_cracked_wall", RED_BRICK_CRACKED);
    public static final Block WOOD_ELVEN_BRICK_WALL = registerWall("wood_elven_brick_wall", WOOD_ELVEN_BRICK);
    public static final Block MOSSY_WOOD_ELVEN_BRICK_WALL = registerWall("mossy_wood_elven_brick_wall", MOSSY_WOOD_ELVEN_BRICK);
    public static final Block CRACKED_WOOD_ELVEN_BRICK_WALL = registerWall("cracked_wood_elven_brick_wall", CRACKED_WOOD_ELVEN_BRICK);
    public static final Block MOSSY_GONDOR_COBBLEBRICK_WALL = registerWall("mossy_gondor_cobblebrick_wall", MOSSY_GONDOR_COBBLEBRICK);
    public static final Block CRACKED_GONDOR_COBBLEBRICK_WALL = registerWall("cracked_gondor_cobblebrick_wall", CRACKED_GONDOR_COBBLEBRICK);
    // Vanilla-adjacent walls it does NOT have: no plain stone wall, and no
    // cracked stone brick, mossy brick or cracked brick wall.
    public static final Block STONE_WALL = registerWall("stone_wall", Blocks.STONE);
    public static final Block CRACKED_STONE_BRICK_WALL = registerWall("cracked_stone_brick_wall", Blocks.CRACKED_STONE_BRICKS);

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
    public static final Block BONE_SLAB = registerSlab("bone_slab", BONE_BLOCK);
    public static final Block SCORCHED_STONE_SLAB = registerSlab("scorched_stone_slab", SCORCHED_STONE);
    public static final Block MORWAITH_BRICK_SLAB = registerSlab("morwaith_brick_slab", MORWAITH_BRICK);
    public static final Block NUMENOREAN_BRICK_SLAB = registerSlab("numenorean_brick_slab", NUMENOREAN_BRICK);
    public static final Block NUMENOREAN_PILLAR_SLAB = registerSlab("numenorean_pillar_slab", NUMENOREAN_PILLAR);
    public static final Block GONDOR_COBBLEBRICK_SLAB = registerSlab("gondor_cobblebrick_slab", GONDOR_COBBLEBRICK);
    public static final Block MOSSY_GONDOR_COBBLEBRICK_SLAB = registerSlab("mossy_gondor_cobblebrick_slab", MOSSY_GONDOR_COBBLEBRICK);
    public static final Block CRACKED_GONDOR_COBBLEBRICK_SLAB = registerSlab("cracked_gondor_cobblebrick_slab", CRACKED_GONDOR_COBBLEBRICK);
    public static final Block FIRE_UTUMNO_PILLAR_SLAB = registerSlab("fire_utumno_pillar_slab", FIRE_UTUMNO_PILLAR);
    public static final Block ICE_UTUMNO_PILLAR_SLAB = registerSlab("ice_utumno_pillar_slab", ICE_UTUMNO_PILLAR);
    public static final Block OBSIDIAN_UTUMNO_PILLAR_SLAB = registerSlab("obsidian_utumno_pillar_slab", OBSIDIAN_UTUMNO_PILLAR);
    public static final Block TAUR_GOLD_PILLAR_SLAB = registerSlab("taur_gold_pillar_slab", TAUR_GOLD_PILLAR);
    public static final Block TAUR_OBSIDIAN_PILLAR_SLAB = registerSlab("taur_obsidian_pillar_slab", TAUR_OBSIDIAN_PILLAR);
    public static final Block CLAY_TILE_DYED_SILVER_SLAB = registerSlab("clay_tile_dyed_silver_slab", CLAY_TILE_DYED_SILVER);
    public static final Block THATCH_REED_SLAB = registerSlab("thatch_reed_slab", THATCH_REED);
    public static final Block BARREN_JUNGLE_MUD_SLAB = registerSlab("barren_jungle_mud_slab", BARREN_JUNGLE_MUD);
    public static final Block RED_BRICK_MOSSY_SLAB = registerSlab("red_brick_mossy_slab", RED_BRICK_MOSSY);
    public static final Block RED_BRICK_CRACKED_SLAB = registerSlab("red_brick_cracked_slab", RED_BRICK_CRACKED);
    public static final Block DIRT_PATH_MUD_SLAB = registerSlab("dirt_path_mud_slab", DIRT_PATH_MUD);
    public static final Block CRACKED_STONE_BRICK_SLAB = registerSlab("cracked_stone_brick_slab", Blocks.CRACKED_STONE_BRICKS);
    public static final Block SAND_SLAB = registerSlab("sand_slab", Blocks.SAND);
    public static final Block RED_SAND_SLAB = registerSlab("red_sand_slab", Blocks.RED_SAND);
    public static final Block DIRT_SLAB = registerSlab("dirt_slab", Blocks.DIRT);
    public static final Block DIRT_PATH_SLAB = registerSlab("dirt_path_slab", Blocks.DIRT_PATH);

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

    private static Block registerColumn(String name) {
        return registerColumn(name, 1.5f, 6.0f);
    }

    private static Block registerColumn(String name, float hardness, float resistance) {
        return track(ALL_COLUMNS, register(name, Block::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.STONE)
                        .requiresCorrectToolForDrops()
                        .strength(hardness, resistance)
                        .sound(SoundType.STONE),
                true));
    }

    private static Block registerWallTorch(String name, LOTRGlowStyle glow) {
        return register(name, props -> new LOTRWallTorchBlock(glow, props),
                torchProperties(), false);
    }

    // The item is a StandingAndWallBlockItem, not a plain BlockItem: that is
    // what lets a torch be put on the SIDE of a block. With a plain BlockItem
    // the wall variants were registered but unreachable -- every LOTR torch
    // could only be stood on top of something.
    private static Block registerTorch(String name, Block wallVariant, LOTRGlowStyle glow) {
        Block torch = register(name, props -> new LOTRTorchBlock(glow, props),
                torchProperties(), true, UnaryOperator.identity(),
                (block, props) -> new StandingAndWallBlockItem(
                        block, wallVariant, Direction.DOWN, props));
        ALL_TORCHES.add(torch);
        TORCH_WALL.put(torch, wallVariant);
        return torch;
    }

    // LOTRMod: new LOTRBlockDoubleTorch(). Two blocks tall, no wall form.
    // LOTRBlockBanner / LOTRItemBanner: cloth on a post, hardness 1.0, wooden
    // footsteps and no collision box -- the original's banner entity had none
    // and you could walk through it. The DyeColor is vanilla's, unused by the
    // renderer; WHITE for all of them until something in the port actually
    // asks a banner for a colour.
    private static BlockBehaviour.Properties bannerProperties() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.WOOD)
                .noCollision()
                .strength(1.0f)
                .sound(SoundType.WOOD)
                .ignitedByLava();
    }

    private static Block registerBanner(String name, LOTRBannerType type, Block wallForm) {
        Block banner = track(ALL_BANNERS, register(name,
                props -> new LOTRBannerBlock(type, DyeColor.WHITE, props),
                bannerProperties(), true, props -> props.stacksTo(16),
                (block, props) -> new StandingAndWallBlockItem(block, wallForm, Direction.DOWN, props)));
        BANNER_WALL_FORM.put(banner, wallForm);
        return banner;
    }

    /** No item: the standing banner's StandingAndWallBlockItem places both. */
    private static Block registerWallBanner(String name, LOTRBannerType type) {
        return track(ALL_WALL_BANNERS, register(name,
                props -> new LOTRWallBannerBlock(type, DyeColor.WHITE, props),
                bannerProperties(),
                false));
    }

    private static Block registerDoubleTorch(String name) {
        return track(ALL_DOUBLE_TORCHES, register(name, LOTRDoubleTorchBlock::new,
                torchProperties(), true));
    }

    // LOTRBlockBirdCage / LOTRBlockBirdCageWood: Material.glass, hardness 0.5,
    // metal footsteps, a full cube with its own top, side and base sprites.
    // Stack size 1 and a LOTRAnimalJarItem: a cage carrying a bird cannot
    // stack with an empty one, and the item is what catches the bird.
    private static Block registerBirdCage(String name, SoundType sound) {
        return track(ALL_ANIMAL_JARS, track(ALL_BIRD_CAGES, register(name, LOTRAnimalJarBlock::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.METAL)
                        .strength(0.5f)
                        .sound(sound)
                        .noOcclusion()
                        .isViewBlocking((state, level, pos) -> false)
                        .isSuffocating((state, level, pos) -> false),
                true, props -> props.stacksTo(1),
                (block, props) -> new LOTRAnimalJarItem(
                        block, LOTREntityTags.BIRD_CAGE_CATCHABLE, props))));
    }

    // LOTRBlockButterflyJar: a glass pot on the same animal-jar machinery as
    // the bird cages, with its own squat shape and its own quarry.
    private static Block registerButterflyJar(String name) {
        return track(ALL_ANIMAL_JARS, register(name,
                // setBlockBounds(0.1875, 0, 0.1875, 0.8125, 0.75, 0.8125). Built here
                // rather than held in a static field: a constant declared below
                // this call would still be null when the block registers.
                props -> new LOTRAnimalJarBlock(Block.box(3.0, 0.0, 3.0, 13.0, 12.0, 13.0), props),
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.NONE)
                        .instabreak()
                        .sound(SoundType.GLASS)
                        .noOcclusion()
                        .isViewBlocking((state, level, pos) -> false)
                        .isSuffocating((state, level, pos) -> false),
                true, props -> props.stacksTo(1),
                (block, props) -> new LOTRAnimalJarItem(
                        block, LOTREntityTags.BUTTERFLY_JAR_CATCHABLE, props)));
    }


    // LOTRBlockClover: a LOTRBlockFlower with setBlockBounds(0.2, 0, 0.2, 0.8,
    // 0.4, 0.8), biome-grass-tinted like the tall grasses. Ordinary clover is
    // replaceable, the four-leaf one deliberately is not -- isReplaceable
    // returned meta != 1, so you cannot build over the lucky one by accident.
    private static Block registerClover(String name, boolean replaceable) {
        BlockBehaviour.Properties props = plantProperties()
                .offsetType(BlockBehaviour.OffsetType.XZ);
        if (replaceable) {
            props = props.replaceable();
        }
        return track(ALL_CLOVERS, track(ALL_FLOWERS, register(name,
                p -> new LOTRPlantBlock(LOTRPlantBlock.Shape.CLOVER,
                        LOTRPlantBlock.Ground.SOIL, p),
                props, true)));
    }

    private static BlockBehaviour.Properties torchProperties() {
        return BlockBehaviour.Properties.of()
                .noCollision()
                .instabreak()
                .lightLevel(state -> 14)
                .sound(SoundType.WOOD)
                .pushReaction(PushReaction.DESTROY);
    }

    private static Block registerCraftingTable(String name) {
        LOTRCraftingTable table = LOTRCraftingTable.valueOf(name.replace("_crafting_table", "").toUpperCase(Locale.ROOT));
        return track(ALL_CRAFTING_TABLES, register(name, props -> new LOTRCraftingTableBlock(table, props),
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.WOOD)
                        .instrument(NoteBlockInstrument.BASS)
                        .strength(2.5f)
                        .sound(SoundType.WOOD)
                        .ignitedByLava(),
                true));
    }

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

    private static Block registerLadder(String name) {
        return track(ALL_LADDERS, register(name, LadderBlock::new,
                ladderProperties().sound(SoundType.LADDER), true));
    }

    // A rope is a ladder that can also hang from another rope. `canRetract` is
    // what separated the two in 1.7.10 -- plain rope was new LOTRBlockRope(false)
    // -- and the elven rope adds LOTRBlockHithlainRope's glow and its bite.
    private static Block registerRope(String name, boolean canRetract, boolean elven) {
        BlockBehaviour.Properties props = ladderProperties().sound(SoundType.WOOL);
        if (elven) {
            // setLightLevel(0.375f) -> light 6.
            props = props.lightLevel(state -> 6);
        }
        return track(ALL_LADDERS, register(name,
                p -> new LOTRRopeBlock(canRetract, elven, p), props, true));
    }

    private static BlockBehaviour.Properties ladderProperties() {
        // No forceSolidOff(): it is deprecated in 26.2 and nothing in vanilla
        // sets it any more. A ladder is noOcclusion with a thin shape, so it
        // already reads as non-solid without the legacy override.
        return BlockBehaviour.Properties.of()
                .strength(0.4f)
                .noOcclusion()
                .pushReaction(PushReaction.DESTROY);
    }

    // Gates. LOTRMod built these with LOTRBlockGate.createWooden/createStone/
    // createMetal, which differed only in hardness, resistance and sound --
    // and the sound is what LOTRGateBlock reads back to pick its open/close
    // effect, so the three stay distinct rather than collapsing into one.
    //
    // The boolean is the ct flag from those same calls: true for the faction
    // gates, whose art ships as thirteen connected-texture pieces, false for
    // the three that are a single flat sprite.
    private static Block registerWoodenGate(String name) {
        return registerWoodenGate(name, true);
    }

    private static Block registerStoneGate(String name) {
        return registerStoneGate(name, true);
    }

    private static Block registerMetalGate(String name) {
        return registerMetalGate(name, true);
    }

    private static Block registerWoodenGate(String name, boolean connectedTextures) {
        return track(ALL_GATES, register(name, props -> new LOTRGateBlock(connectedTextures, props),
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.WOOD)
                        .strength(4.0f, 5.0f)
                        .sound(SoundType.WOOD)
                        .noOcclusion()
                        // A gate is a multiblock: a piston shoving one panel
                        // out of a wall would silently break the flood fill
                        // for the rest.
                        .pushReaction(PushReaction.BLOCK),
                true));
    }

    private static Block registerStoneGate(String name, boolean connectedTextures) {
        return track(ALL_GATES, register(name, props -> new LOTRGateBlock(connectedTextures, props),
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.STONE)
                        .requiresCorrectToolForDrops()
                        .strength(4.0f, 10.0f)
                        .sound(SoundType.STONE)
                        .noOcclusion()
                        // A gate is a multiblock: a piston shoving one panel
                        // out of a wall would silently break the flood fill
                        // for the rest.
                        .pushReaction(PushReaction.BLOCK),
                true));
    }

    private static Block registerMetalGate(String name, boolean connectedTextures) {
        return track(ALL_GATES, register(name, props -> new LOTRGateBlock(connectedTextures, props),
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.METAL)
                        .requiresCorrectToolForDrops()
                        .strength(4.0f, 10.0f)
                        .sound(SoundType.METAL)
                        .noOcclusion()
                        // A gate is a multiblock: a piston shoving one panel
                        // out of a wall would silently break the flood fill
                        // for the rest.
                        .pushReaction(PushReaction.BLOCK),
                true));
    }

    /**
     * LOTRBlockGateDwarven: Material.rock, hardness 4, resistance 10, stone
     * sound -- createStone's numbers -- plus setFullBlock(). It still joins
     * ALL_GATES so the connected-border plugin gives it the gate model and
     * datagen gives it a gate item icon; it is the shape and the stone skin
     * that differ, not the machinery.
     */
    private static Block registerDwarvenDoor(String name,
                                             Function<BlockBehaviour.Properties, Block> factory) {
        return track(ALL_GATES, register(name, factory,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.STONE)
                        .requiresCorrectToolForDrops()
                        .strength(4.0f, 10.0f)
                        .sound(SoundType.STONE)
                        // A gate is a multiblock: a piston shoving one panel
                        // out of a wall would silently break the flood fill
                        // for the rest.
                        .pushReaction(PushReaction.BLOCK),
                true));
    }

    /**
     * Beds are hardness 0.2 with a wood step sound, and LOTRItemBed set
     * maxStackSize 1 -- a bed is two blocks, so a stack of them would place
     * wrong.
     */
    private static Block registerBed(String name) {
        return track(ALL_BEDS, register(name, LOTRBedBlock::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.WOOL)
                        .strength(0.2f)
                        .sound(SoundType.WOOD)
                        .noOcclusion(),
                item -> item.stacksTo(1)));
    }

    private static Block registerChest(String name, String variant, MapColor color,
                                       float hardness, SoundType sound) {
        return track(ALL_CHESTS, register(name, props -> new LOTRChestBlock(variant, props),
                BlockBehaviour.Properties.of()
                        .mapColor(color)
                        .strength(hardness)
                        .sound(sound)
                        .noOcclusion(),
                true));
    }

    private static Block registerKebabStand(String name, String variant) {
        return track(ALL_KEBAB_STANDS, register(name, props -> new LOTRKebabStandBlock(variant, props),
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.WOOD)
                        .strength(0.0f, 1.0f)
                        .sound(SoundType.WOOD)
                        .noOcclusion()
                        .noCollision()
                        .pushReaction(PushReaction.DESTROY),
                true));
    }

    private static Block registerTrollTotem(String name, LOTRTrollTotemBlock.Part part) {
        return track(ALL_TROLL_TOTEMS, register(name, props -> new LOTRTrollTotemBlock(part, props),
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.STONE)
                        .sound(SoundType.STONE)
                        .noOcclusion()
                        // A totem is a three-block object; a piston pulling one
                        // part out would leave a broken idol behind.
                        .pushReaction(PushReaction.BLOCK),
                true));
    }

    private static Block registerFenceGate(String name, Block base) {
        Block gate = track(ALL_FENCE_GATES, register(name, props -> new FenceGateBlock(net.minecraft.world.level.block.state.properties.WoodType.OAK, props),
                BlockBehaviour.Properties.ofFullCopy(base), true));

        FENCE_GATE_BASE.put(gate, base);
        return gate;
    }

    private static Block registerButton(String name, Block base) {
        Block button = register(name,
                props -> new ButtonBlock(BlockSetType.STONE, 20, props),
                BlockBehaviour.Properties.ofFullCopy(base).noCollision().strength(0.5f),
                true);
        ALL_BUTTONS.add(button);
        BUTTON_BASE.put(button, base);
        return button;
    }

    private static Block registerPressurePlate(String name, Block base) {
        Block plate = register(name,
                props -> new PressurePlateBlock(BlockSetType.STONE, props),
                BlockBehaviour.Properties.ofFullCopy(base).noCollision().strength(0.5f),
                true);
        ALL_PRESSURE_PLATES.add(plate);
        PRESSURE_PLATE_BASE.put(plate, base);
        return plate;
    }

    // Crop with growth stages. CropBlock always has eight ages, but the 1.7.10 crops shipped only three or four stage textures -- CROP_STAGES records how many, and datagen maps several ages onto each texture. Registered WITH an item even though vanilla crops have none. Datagen's createCropBlock emits an item model, and with no item every crop resolves to minecraft:item/air -- six crops then collide on the same model id and datagen dies with "Duplicate model definition". Giving each crop a BlockItem keeps the ids distinct; it also makes them placeable for testing until the seed items exist.
    private static Block registerCrop(String name, int stages) {
        Block crop = register(name, LOTRCropBlock::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.PLANT)
                        .noCollision()
                        .randomTicks()
                        .instabreak()
                        .sound(SoundType.CROP)
                        .pushReaction(PushReaction.DESTROY),
                true);
        ALL_CROPS.add(crop);
        CROP_STAGES.put(crop, stages);
        return crop;
    }

    // LOTRBlockBerryBush: a full-cube block drawn like leaves, hardness 0.4,
    // grass footsteps, and solid enough to stand on -- the original never
    // removed its collision box. randomTicks is what ripens the berries.
    private static Block registerBush(String name) {
        return track(ALL_BUSHES, register(name, LOTRBerryBushBlock::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.PLANT)
                        .strength(0.4f)
                        .randomTicks()
                        .sound(SoundType.GRASS)
                        .noOcclusion()
                        .isViewBlocking((state, level, pos) -> false)
                        .isSuffocating((state, level, pos) -> false)
                        .ignitedByLava()
                        .pushReaction(PushReaction.DESTROY),
                true));
    }

    // LOTRMod: new BlockWeb(). It IS vanilla's cobweb, so it gets vanilla's
    // class AND vanilla's properties -- WebBlock is what slows anything walking
    // into it, requiresCorrectToolForDrops is what makes shears or a sword the
    // way to collect it, and strength 4.0 is vanilla's. The original's
    // setHardness(2.0f) and setLightOpacity(2) are not carried over: matching
    // vanilla is the behaviour that was asked for.
    private static Block registerWeb(String name) {
        return register(name, WebBlock::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.WOOL)
                        .forceSolidOn()
                        .noCollision()
                        .requiresCorrectToolForDrops()
                        .strength(4.0f)
                        .noOcclusion()
                        .pushReaction(PushReaction.DESTROY),
                true);
    }

    // Gravel- and sand-like block that falls when unsupported. The 1.7.10 originals extended BlockSand/BlockGravel, so they obeyed gravity; a plain Block does not. FallingBlock itself is abstract, so this uses ColoredFallingBlock the way vanilla gravel does -- the colour is the dust tint for the falling particles. Registered outside the tier lists like the other soft blocks, since the originals set no harvest level.
    private static Block registerFalling(String name, float hardness, int dustColor) {
        Block block = register(name, props -> new ColoredFallingBlock(new ColorRGBA(dustColor), props),
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.STONE)
                        .strength(hardness)
                        .sound(SoundType.GRAVEL),
                true);
        ALL_CUBES.add(block);
        ALL_FALLING.add(block);

        SHOVEL_MINEABLE.add(block);
        return block;
    }

    private static Block registerPath(String name) {
        return track(SHOVEL_MINEABLE, track(ALL_PATHS, register(name, DirtPathBlock::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.DIRT)
                        .strength(0.65f)
                        .sound(SoundType.GRASS)
                        .isViewBlocking((state, level, pos) -> true)
                        .isSuffocating((state, level, pos) -> true),
                true)));
    }

    private static Block registerFarmland(String name) {
        return track(SHOVEL_MINEABLE, track(ALL_FARMLAND, register(name, LOTRFarmlandBlock::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.DIRT)
                        .randomTicks()
                        .strength(0.6f)
                        .sound(SoundType.GRAVEL)
                        .isViewBlocking((state, level, pos) -> true)
                        .isSuffocating((state, level, pos) -> true),
                true)));
    }

    private static Block registerRail(String name) {
        return track(ALL_RAILS, register(name, PoweredRailBlock::new,
                BlockBehaviour.Properties.of()
                        .noCollision()
                        .strength(0.7f)
                        .sound(SoundType.METAL)
                        .pushReaction(PushReaction.DESTROY),
                true));
    }

    // noOcclusion matters more than it looks: without it the block UNDER the
    // carpet culls its own top face, and since the carpet is only one pixel
    // thick you end up looking straight through the floor. LOTRBlockMordorMoss
    // and LOTRBlockThatchFloor both returned false from isOpaqueCube for the
    // same reason.
    private static Block registerCarpet(String name, float hardness) {
        return track(ALL_CARPETS, register(name, CarpetBlock::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.PLANT)
                        .strength(hardness)
                        .sound(SoundType.GRASS)
                        .noOcclusion()
                        .isViewBlocking((state, level, pos) -> false)
                        .isSuffocating((state, level, pos) -> false),
                true));
    }

    // Soil, clay and mire: dug with a shovel, not a pickaxe. The 1.7.10 originals set harvest level "shovel" 0, which requires no tool at all -- so unlike registerCube this deliberately omits requiresCorrectToolForDrops(), or they would stop dropping to hand.

    private static Block registerSoilColumn(String name, float hardness, float resistance,
                                            SoundType sound, boolean randomTicks) {
        BlockBehaviour.Properties props = BlockBehaviour.Properties.of()
                .mapColor(MapColor.GRASS)
                .strength(hardness, resistance)
                .sound(sound);
        if (randomTicks) {
            props = props.randomTicks();
        }
        Block block = register(name, Block::new, props, true);
        ALL_SOIL_COLUMNS.add(block);
        SHOVEL_MINEABLE.add(block);
        return block;
    }

    private static Block registerBottomTop(String name, float hardness, float resistance) {
        Block block = register(name, Block::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.STONE)
                        .requiresCorrectToolForDrops()
                        .strength(hardness, resistance)
                        .sound(SoundType.STONE),
                true);
        ALL_BOTTOM_TOP.add(block);
        CUBES_NO_TIER.add(block);
        return block;
    }

    private static Block registerSoil(String name, float hardness, float resistance, SoundType sound) {
        Block block = register(name, Block::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.DIRT)
                        .strength(hardness, resistance)
                        .sound(sound),
                true);
        ALL_CUBES.add(block);
        SHOVEL_MINEABLE.add(block);
        return block;
    }

    private static Block registerCubeColumn(String name, float hardness, float resistance, Tier tier) {
        return registerCubeColumn(name, hardness, resistance, tier, SoundType.STONE);
    }

    private static Block registerCubeColumn(String name, float hardness, float resistance, Tier tier, SoundType sound) {
        Block block = registerCube(name, hardness, resistance, tier, sound);
        CUBES_COLUMN_TEXTURED.add(block);
        return block;
    }

    // A brick with a gulduril crystal in it. The glow is baked into an animated
    // texture (16-frame strip + .mcmeta) rather than drawn by a block entity
    // renderer, so a wall of these costs nothing per frame. Light 11 = the
    // original's setLightLevel(0.75f).
    // LOTRBlockDartTrap: Material.rock, hardness 4.0, stone sound. getIcon put
    // <name>_face on the facing side and the base brick's texture elsewhere;
    // brick4 metas 0/3/4 are the tauredain, gold and obsidian bricks.
    // LOTRBlockForgeBase: Material.rock, hardness 4.0, stone sound.
    private static Block registerForge(String name) {
        return track(ALL_FORGES, track(CUBES_NO_TIER, register(name, LOTRForgeBlock::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.STONE)
                        .requiresCorrectToolForDrops()
                        .strength(4.0f, 6.0f)
                        .sound(SoundType.STONE)
                        .lightLevel(state -> state.getValue(net.minecraft.world.level.block.AbstractFurnaceBlock.LIT) ? 13 : 0),
                true)));
    }

    private static Block registerDartTrap(String name, Block base) {
        Block block = register(name, LOTRDartTrapBlock::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.STONE)
                        .requiresCorrectToolForDrops()
                        .strength(4.0f, 6.0f)
                        .sound(SoundType.STONE),
                true);
        ALL_DART_TRAPS.add(block);
        CUBES_NO_TIER.add(block);
        DART_TRAP_BASE.put(block, base);
        return block;
    }

    private static Block registerHobbitOven(String name) {
        return register(name, LOTRHobbitOvenBlock::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.STONE)
                        .strength(3.5f)
                        .sound(SoundType.STONE)
                        .lightLevel(state -> state.getValue(LOTRHobbitOvenBlock.LIT) ? 13 : 0),
                true);
    }

    private static Block registerBeacon(String name) {
        return register(name, LOTRBeaconBlock::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.WOOD)
                        .strength(0.0f, 5.0f)
                        .sound(SoundType.WOOD)
                        // isFullyLit() drove getLightValue in the original. A
                        // blockstate light level cannot ask the block entity,
                        // so FULLY_LIT -- set a hundred ticks after ignition --
                        // is the flag that carries it.
                        .lightLevel(state -> state.getValue(LOTRBeaconBlock.FULLY_LIT) ? 15 : 0)
                        .noOcclusion(),
                true);
    }

    private static Block registerGulduril(String name) {
        Block block = register(name, Block::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.STONE)
                        .requiresCorrectToolForDrops()
                        .strength(3.0f, 6.0f)
                        .lightLevel(state -> 11)
                        .sound(SoundType.STONE),
                true);
        ALL_CUBES.add(block);
        CUBES_NO_TIER.add(block);
        ALL_GULDURIL.add(block);
        return block;
    }

    /**
     * An ore that drops something other than itself: LOTRBlockOre and
     * LOTRBlockOreGem, hardness 3 / resistance 5 (3 in modern units). The drop
     * table is in LOTRBlockLootProvider; the XP is DropExperienceBlock's, which
     * like dropBlockAsItemWithChance gives none when the block itself comes
     * back under Silk Touch. setLightLevel(f) was (int) (15 * f): 0.5 is 7,
     * 0.75 is 11.
     */
    private static Block registerOre(String name, Tier tier, int light, int xpMin, int xpMax) {
        Block block = register(name, props -> new DropExperienceBlock(UniformInt.of(xpMin, xpMax), props),
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.STONE)
                        .requiresCorrectToolForDrops()
                        .strength(3.0f, 3.0f)
                        .sound(SoundType.STONE)
                        .lightLevel(state -> light),
                true);
        ALL_CUBES.add(block);
        ORES_WITH_ITEM_DROPS.add(block);
        switch (tier) {
            case IRON -> CUBES_IRON_TIER.add(block);
            case STONE -> CUBES_STONE_TIER.add(block);
            case NONE -> CUBES_NO_TIER.add(block);
        }
        return block;
    }

    private static Block registerCube(String name, float hardness, float resistance, Tier tier) {
        return registerCube(name, hardness, resistance, tier, SoundType.STONE);
    }

    // LOTRBlockOreStorageBase set soundTypeMetal, so every metal and gem
    // storage block rings rather than clacks -- same as vanilla's iron block.
    private static Block registerCube(String name, float hardness, float resistance, Tier tier, SoundType sound) {
        Block block = register(name, Block::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.STONE)
                        .requiresCorrectToolForDrops()
                        .strength(hardness, resistance)
                        .sound(sound),
                true);
        ALL_CUBES.add(block);
        switch (tier) {
            case IRON -> CUBES_IRON_TIER.add(block);
            case STONE -> CUBES_STONE_TIER.add(block);
            case NONE -> CUBES_NO_TIER.add(block);
        }
        return block;
    }

    // LOTRBlockTreasurePile: Material.circuits and hardness 0, so it breaks
    // instantly and never blocks a piston. The original gave it its own
    // "lotr:treasure" step sound, which the port has no sound event for yet;
    // metal is the nearest vanilla has.
    // LOTRBlockOrcBomb: Material.iron, hardness 3, resistance 0 -- it is no
    // more blast-proof than air, because a blast is meant to set it off rather
    // than be soaked up by it.
    private static Block registerOrcBomb(String name, int strengthLevel, boolean fire) {
        // LOTRItemOrcBomb.addInformation put up to two lines under the name --
        // "Double Strength" or "Triple Strength", then "Fire" -- since all six
        // shared the one name "Orc Bomb" and the lines are the only thing
        // telling them apart in a hand. They are a default LORE component here
        // rather than a tooltip override, because Item.appendHoverText is
        // deprecated and this never varies per stack.
        List<Component> lines = new ArrayList<>();
        if (strengthLevel == 1) {
            lines.add(Component.translatable("block.lotr.orc_bomb.double_strength"));
        } else if (strengthLevel == 2) {
            lines.add(Component.translatable("block.lotr.orc_bomb.triple_strength"));
        }
        if (fire) {
            lines.add(Component.translatable("block.lotr.orc_bomb.fire"));
        }
        UnaryOperator<Item.Properties> lore = lines.isEmpty()
                ? UnaryOperator.identity()
                : props -> props.component(DataComponents.LORE, new ItemLore(List.copyOf(lines)));

        return track(ALL_ORC_BOMBS, register(name,
                props -> new LOTROrcBombBlock(strengthLevel, fire, props),
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.METAL)
                        .strength(3.0f, 0.0f)
                        .sound(SoundType.METAL)
                        .noOcclusion(),
                true, lore));
    }

    /**
     * One block per metal, and TWO items for it: getSubBlocks offered metadata 0
     * and metadata 7, a two-pixel scatter and the full block, both under the one
     * name.
     *
     * <p>Registration ORDER matters and is not incidental. A BlockItem enters
     * itself into Item.BY_BLOCK on construction, so the last one built for a
     * block is the one Block.asItem() answers with. The carpet is therefore
     * registered first and the whole block second, leaving asItem() -- and with
     * it pick-block, and anything that hands a Block where an Item is wanted --
     * pointing at the full block. Building them the other way round is what made
     * the creative tab add the carpet twice and crash.
     */
    private static Block registerTreasurePile(String name) {
        Block pile = track(ALL_TREASURE_PILES, register(name, LOTRTreasurePileBlock::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.METAL)
                        .instabreak()
                        .sound(SoundType.METAL)
                        .noOcclusion()
                        .pushReaction(PushReaction.DESTROY),
                false));
        // register(withItem = false) skips ALL_BLOCKS as well as the item, and
        // the catch-all EVERYTHING tab walks that list.
        ALL_BLOCKS.add(pile);

        TREASURE_PILE_CARPETS.put(pile,
                LOTRItems.registerTreasurePileItem(pile, name + "_carpet", 1));
        TREASURE_PILE_BLOCKS.put(pile,
                LOTRItems.registerTreasurePileItem(pile, name, LOTRTreasurePileBlock.MAX_LAYERS));
        return pile;
    }

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

    // LeavesBlock, not a plain Block: it brings decay when the supporting log
    // is gone, the DISTANCE/PERSISTENT properties, particle and sound handling,
    // and shears/hoe harvesting. A plain Block gave none of that.
    // LeavesBlock is abstract in 26.2. ParticleLeavesBlock is the untinted
    // concrete subclass; vanilla uses TintedParticleLeavesBlock for oak and the
    // rest, but LOTRBlockLeavesBase.colorMultiplier returned 0xFFFFFF, so LOTR
    // leaves are deliberately NOT biome-tinted -- mallorn stays gold.
    // 0.01F is vanilla's particle chance.
    // NOTE FOR LEVI: if the constructor arity differs, Ctrl-click
    // ParticleLeavesBlock and paste it; only this one line changes.
    private static Block registerLeaves(String name) {
        return track(ALL_LEAVES, register(name,
                props -> new UntintedParticleLeavesBlock(0.01F, ParticleTypes.FALLING_SPORE_BLOSSOM, props),
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.PLANT)
                        .strength(0.2f)
                        .randomTicks()
                        .sound(SoundType.GRASS)
                        .noOcclusion()
                        // The remaining five come from vanilla's
                        // Blocks.leavesProperties(), which our copy was missing.
                        // Without them LOTR leaves smothered players, blocked
                        // redstone, would not burn in lava, survived pistons and
                        // spawned any mob that fits -- none of which matches
                        // either vanilla leaves or BlockLeaves in 1.7.10.
                        .isValidSpawn((state, level, pos, type) ->
                                type == EntityTypes.OCELOT || type == EntityTypes.PARROT)
                        .isViewBlocking((state, level, pos) -> false)
                        .isSuffocating((state, level, pos) -> false)
                        .isRedstoneConductor((state, level, pos) -> false)
                        .ignitedByLava()
                        .pushReaction(PushReaction.DESTROY),
                true));
    }

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

    private static Block registerPillar(String name) {
        return track(ALL_PILLARS, register(name, LOTRPillarBlock::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.STONE)
                        .requiresCorrectToolForDrops()
                        .strength(1.5f, 6.0f)
                        .sound(SoundType.STONE),
                true));
    }

    // A real SaplingBlock, not a plain Block: LOTRBlockSaplingBase extended
    // BlockSapling in 1.7.10, and only the vanilla class brings the STAGE
    // property, the "must stand on dirt" survival check and BonemealableBlock.
    // randomTicks() is what lets SaplingBlock.randomTick run at all. The tree
    // features are not ported yet, so each gets a placeholder grower -- see
    // LOTRSaplingBlock.
    private static Block registerSapling(String name) {
        return track(ALL_SAPLINGS, register(name,
                props -> new LOTRSaplingBlock(LOTRSaplingBlock.placeholderGrower(name), props),
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.PLANT)
                        .noCollision()
                        .randomTicks()
                        .instabreak()
                        .sound(SoundType.GRASS)
                        .pushReaction(PushReaction.DESTROY),
                true));
    }

    private static Block registerFlower(String name) {
        return registerFlower(name, LOTRPlantBlock.Shape.FLOWER);
    }

    private static Block registerFlower(String name, LOTRPlantBlock.Shape shape) {
        return registerFlower(name, shape, LOTRPlantBlock.Ground.SOIL);
    }

    // A LOTRBlockFlower: the shape is its setBlockBounds/setFlowerBounds call
    // and the ground its canBlockStay override, both from 1.7.10. XZ offset
    // because LOTRRenderBlocks.renderFlowerBlock jittered a flower's position
    // by up to 0.15 in x and z from a hash of its coordinates -- that is
    // exactly what OffsetType.XZ does.
    private static Block registerFlower(String name, LOTRPlantBlock.Shape shape,
            LOTRPlantBlock.Ground ground) {
        return track(ALL_FLOWERS, register(name, props -> new LOTRPlantBlock(shape, ground, props),
                plantProperties().offsetType(BlockBehaviour.OffsetType.XZ),
                true));
    }

    // A LOTRBlockGrass. Two things separate it from a flower: renderGrass
    // jittered y as well as x and z (OffsetType.XYZ), and isReplaceable
    // returned true, so you can build straight through it.
    private static Block registerGrass(String name, LOTRPlantBlock.Ground ground) {
        return registerGrass(name, ground, LOTRPlantBlock.Sting.NONE);
    }

    private static Block registerGrass(String name, LOTRPlantBlock.Ground ground,
            LOTRPlantBlock.Sting sting) {
        return track(ALL_FLOWERS, register(name,
                props -> new LOTRPlantBlock(LOTRPlantBlock.Shape.GRASS, ground, sting, props),
                plantProperties()
                        .replaceable()
                        .offsetType(BlockBehaviour.OffsetType.XYZ),
                true));
    }

    // Reeds: a column rooted on the bed under shallow water. `grows` is false
    // for the dried reeds, matching LOTRBlockReedDry.canReedGrow. A plain
    // BlockItem, deliberately -- PlaceOnWaterBlockItem places on TOP of the
    // water, which is where the 1.7.10 reeds sat but not where these do.
    private static Block registerReed(String name, boolean grows) {
        return track(ALL_FLOWERS, track(ALL_COLUMN_PLANTS, register(name,
                props -> new LOTRReedBlock(grows, props),
                plantProperties().randomTicks(), true)));
    }

    private static Block registerCorn(String name) {
        return track(ALL_FLOWERS, track(ALL_COLUMN_PLANTS, register(name, LOTRCornBlock::new,
                plantProperties().randomTicks(), true)));
    }

    // Corn, reeds and the grapevine post: full-height columns that grow upward,
    // drawn by their own renderers in 1.7.10 with no positional jitter.
    private static Block registerStalk(String name, LOTRPlantBlock.Shape shape,
            LOTRPlantBlock.Ground ground) {
        return track(ALL_FLOWERS, register(name,
                props -> new LOTRPlantBlock(shape, ground, props),
                plantProperties(), true));
    }

    // Fangorn riverweed, the mod's one lily pad.
    // Riverweed keeps its collision box: a lily pad is something you stand on,
    // and vanilla's does not set noCollision. plantProperties() does, so the
    // riverweed builds its own.
    private static Block registerRiverweed(String name) {
        return track(ALL_FLOWERS, register(name, LOTRRiverweedBlock::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.PLANT)
                        .instabreak()
                        .sound(SoundType.LILY_PAD)
                        .noOcclusion()
                        .pushReaction(PushReaction.DESTROY),
                PlaceOnWaterBlockItem::new));
    }

    private static BlockBehaviour.Properties plantProperties() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .noCollision()
                .instabreak()
                .sound(SoundType.GRASS)
                .noOcclusion()
                .pushReaction(PushReaction.DESTROY);
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

    // LOTRBlockGlassPane / LOTRBlockStainedGlassPane. Vanilla's own panes are
    // IronBarsBlock too -- the class is shared, only the properties differ:
    // glass breaks instantly, rings like glass, and needs no tool.
    private static Block registerGlassPane(String name, Block glass) {
        Block pane = track(ALL_GLASS_PANES, register(name, IronBarsBlock::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.NONE)
                        .strength(0.3f)
                        .sound(SoundType.GLASS)
                        .noOcclusion()
                        .pushReaction(PushReaction.DESTROY),
                true));
        GLASS_PANE_BASE.put(pane, glass);
        return pane;
    }

    private static Block registerBars(String name) {
        return track(ALL_BARS, register(name, IronBarsBlock::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.NONE)
                        .requiresCorrectToolForDrops()
                        .strength(5.0f, 6.0f)
                        .sound(SoundType.METAL)
                        .noOcclusion()
                        // A gate is a multiblock: a piston shoving one panel
                        // out of a wall would silently break the flood fill
                        // for the rest.
                        .pushReaction(PushReaction.BLOCK),
                true));
    }

    private static Block registerChandelier(String name, LOTRGlowStyle style) {
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
                                 BlockBehaviour.Properties properties,
                                 UnaryOperator<Item.Properties> itemProperties) {
        return register(name, factory, properties, true, itemProperties);
    }

    public static Block register(String name, Function<BlockBehaviour.Properties, Block> factory,
                                 BlockBehaviour.Properties properties, boolean withItem) {
        return register(name, factory, properties, withItem, UnaryOperator.identity());
    }

    // Register with a BlockItem other than a plain one. The water plants need
    // PlaceOnWaterBlockItem: a fluid is not a clickable surface, so a plain
    // BlockItem right-clicked at water does nothing at all -- which is why the
    // reeds and the riverweed could not be placed. It raycasts into the fluid
    // and puts the block on its surface; vanilla's lily pad uses it, and
    // LOTRItemWaterPlant was the 1.7.10 equivalent.
    private static Block register(String name, Function<BlockBehaviour.Properties, Block> factory,
                                  BlockBehaviour.Properties properties,
                                  BiFunction<Block, Item.Properties, BlockItem> itemFactory) {
        return register(name, factory, properties, true, UnaryOperator.identity(), itemFactory);
    }

    private static Block register(String name, Function<BlockBehaviour.Properties, Block> factory,
                                  BlockBehaviour.Properties properties, boolean withItem,
                                  UnaryOperator<Item.Properties> itemProperties) {
        return register(name, factory, properties, withItem, itemProperties, BlockItem::new);
    }

    private static Block register(String name, Function<BlockBehaviour.Properties, Block> factory,
                                  BlockBehaviour.Properties properties, boolean withItem,
                                  UnaryOperator<Item.Properties> itemProperties,
                                  BiFunction<Block, Item.Properties, BlockItem> itemFactory) {
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
            Item.Properties props = itemProperties.apply(
                    new Item.Properties().setId(itemKey).useBlockDescriptionPrefix());
            BlockItem blockItem = itemFactory.apply(block, props);
            Registry.register(BuiltInRegistries.ITEM, itemKey, blockItem);
        }
        return block;
    }

    // Runs after every field above is initialised, so it also covers datagen,
    // which touches LOTRBlocks without going through init().
    static {
        pairLeavesWithSaplings();
        NOT_SMALL_FLOWERS.addAll(GRASS_TINTED);
        NOT_SMALL_FLOWERS.addAll(List.of(ARID_GRASS, MORDOR_GRASS, MORDOR_THORN,
                CORN_STALK, REEDS, DRIED_REEDS, GRAPEVINE, FANGORN_RIVERWEED,
                DEAD_MARSH_PLANT, CORRUPT_MALLORN, MORGUL_SHROOM));
    }

    public static void init() {
    }

    // <x>_leaves -> <x>_sapling. Every leaf block in the mod has a matching
    // sapling, so a missing pair means one of the two lists has drifted and the
    // leaf would silently drop nothing -- hence the hard failure.
    private static void pairLeavesWithSaplings() {
        Map<String, Block> saplingsByStem = new LinkedHashMap<>();
        ALL_SAPLINGS.forEach(sapling -> saplingsByStem.put(
                keyOf(sapling).identifier().getPath().replaceAll("_sapling$", ""), sapling));

        for (Block leaves : ALL_LEAVES) {
            String stem = keyOf(leaves).identifier().getPath().replaceAll("_leaves$", "");
            Block sapling = saplingsByStem.get(stem);
            if (sapling == null) {
                throw new IllegalStateException("No sapling registered for leaves " + stem);
            }
            LEAVES_SAPLING.put(leaves, sapling);
        }
    }
}