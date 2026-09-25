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
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRFoodItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRBarrelItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRPlateItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRVessel;
import net.minecraft.sounds.SoundEvents;
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
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRKebabStandItem;

import static net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBuildingBlocks.*;
import static net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRCombatBlocks.*;
import static net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRDecorationBlocks.*;
import static net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRFoodBlocks.*;
import static net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRMiscBlocks.*;
import static net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRUtilityBlocks.*;

public final class LOTRBlocks {
    // Mining tier, i.e. which needs_*_tool tag the block joins. NONE means pickaxe-mineable with no needs_* tag at all -- a wooden pickaxe suffices. That is what vanilla stone, stone bricks and bone blocks use, and it also matches the original: only sixteen blocks in 1.7.10 ever called setHarvestLevel, and everything else defaulted to level 0. Rock and brick families therefore take NONE, not STONE.
    public enum Tier {
        NONE, STONE, IRON
    }

    // These MUST stay above the first block field. Java runs static

    public static final List<Block> ALL_BLOCKS = new ArrayList<>();

    /** LOTRBlockFallenLeaves, one per leaf type: vanilla's six, then the mod's. */
    public static final List<Block> ALL_FALLEN_LEAVES = new ArrayList<>();

    /** The six LOTRBlockStalactite blocks, stalactite then stalagmite, per material. */
    public static final List<Block> ALL_STALACTITES = new ArrayList<>();

    public static final List<Block> ALL_CUBES = new ArrayList<>();
    public static final List<Block> CUBES_STONE_TIER = new ArrayList<>();
    public static final List<Block> CUBES_IRON_TIER = new ArrayList<>();
    // Pickaxe-mineable, but no needs_*_tool tag: a wooden pickaxe works. */
    public static final List<Block> CUBES_NO_TIER = new ArrayList<>();
    public static final List<Block> ALL_PLANKS = new ArrayList<>();
    public static final List<Block> ALL_LEAVES = new ArrayList<>();
    public static final List<Block> ALL_SAPLINGS = new ArrayList<>();

    // Which sapling a leaf block drops. Every LOTR leaf has a sapling of the
    // same species, so the pairing is by name: <x>_leaves -> <x>_sapling.
    // Filled by pairLeavesWithSaplings() once both lists are populated.
    public static final Map<Block, Block> LEAVES_SAPLING = new LinkedHashMap<>();
    public static final List<Block> ALL_TRAPDOORS = new ArrayList<>();
    public static final List<Block> ALL_DOORS = new ArrayList<>();
    // Float.MAX_VALUE, as LOTRBlockUtumnoBrick/Pillar/SlabBase set it.
    static final float UTUMNO_RESISTANCE = Float.MAX_VALUE;
    public static final List<Block> ALL_BARS = new ArrayList<>();
    public static final List<Block> ALL_GLASS_PANES = new ArrayList<>();

    // Which glass block a pane wears the texture of.
    public static final Map<Block, Block> GLASS_PANE_BASE = new LinkedHashMap<>();
    public static final List<Block> ALL_CHANDELIERS = new ArrayList<>();
    public static final List<Block> ALL_GLASS = new ArrayList<>();
    public static final List<Block> ALL_FLOWERS = new ArrayList<>();
    public static final List<Block> ALL_DOUBLE_FLOWERS = new ArrayList<>();
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

    static final Map<Block, ResourceKey<Block>> BLOCK_KEYS = new LinkedHashMap<>();

    public static ResourceKey<Block> keyOf(Block block) {
        return BLOCK_KEYS.get(block);
    }

    // --- tabFood ------------------------------------------------------------
    // The barrel, the cakes and pies that are eaten where they are set down,
    // the fruit that hangs off logs, the plates, and the vessel blocks a
    // drink is set down as.

    public static final List<Block> ALL_PLATES = new ArrayList<>();
    public static final List<Block> ALL_MUG_BLOCKS = new ArrayList<>();

    /**
     * LOTRBlockTreasurePile.soundTypeTreasure: its own break, step and place
     * sounds. The original also shook the pitch by +/-15% on every play, which
     * a SoundType has no way to say; it plays at 1.0.
     */
    public static final SoundType TREASURE_SOUND = new SoundType(1.0f, 1.0f, LOTRSounds.BLOCK_TREASURE_BREAK,
            LOTRSounds.BLOCK_TREASURE_STEP, LOTRSounds.BLOCK_TREASURE_PLACE,
            LOTRSounds.BLOCK_TREASURE_STEP, LOTRSounds.BLOCK_TREASURE_STEP);

    /**
     * LOTRBlockPlate.soundTypePlate: stone's steps and placing, with the
     * plate's own smash as its break sound. The wooden plate sounds of wood.
     */
    public static final SoundType PLATE_SOUND = new SoundType(1.0f, 1.0f, LOTRSounds.BLOCK_PLATE_BREAK,
            SoundEvents.STONE_STEP, SoundEvents.STONE_PLACE, SoundEvents.STONE_HIT, SoundEvents.STONE_FALL);

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

    // ALL_FLOWERS is a registration bucket, not a statement that everything in
    // it is a flower: the grasses, the stalks and the riverweed live there too,
    // and must stay out of #minecraft:small_flowers.
    public static final List<Block> NOT_SMALL_FLOWERS = new ArrayList<>();

    // Reeds, dried reeds and corn: plants that occupy a stack of blocks, have
    // their own hand-written models, and take a flat inventory icon of their own
    // rather than a picture of one segment.
    public static final List<Block> ALL_COLUMN_PLANTS = new ArrayList<>();

    // Stairs cut from an existing block. Takes its properties from the base block, so a brick stair is as tough as its brick and a wooden stair burns like its planks -- which is what the 1.7.10 LOTRBlockStairs(block, meta) constructor did. The base is recorded in STAIRS_BASE so datagen can find the texture to use. NOTE: these fields must be declared AFTER every base block, since they dereference them during static init.
    static Block registerStairs(String name, Block base) {
        Block stairs = register(name, props -> new StairBlock(base.defaultBlockState(), props),
                BlockBehaviour.Properties.ofFullCopy(base), true);
        ALL_STAIRS.add(stairs);
        STAIRS_BASE.put(stairs, base);
        return stairs;
    }

    // Slab cut from an existing block. One modern SlabBlock replaces the 1.7.10 single/double pair, since SlabType covers bottom, top and double. Like stairs, these must be declared AFTER every base block.
    static Block registerSlab(String name, Block base) {
        Block slab = register(name, SlabBlock::new,
                BlockBehaviour.Properties.ofFullCopy(base), true);
        ALL_SLABS.add(slab);
        SLAB_BASE.put(slab, base);
        return slab;
    }

    // LOTRBlockSlab through LOTRBlockSlab14. LOTRMod.java gave every one of
    // them setHardness(2.0).setResistance(10.0).setStepSound(soundTypeStone),
    // whatever block the slab was cut from: hardness 2, blast resistance 6.
    static Block registerStoneSlab(String name, Block base) {
        Block slab = register(name, SlabBlock::new,
                BlockBehaviour.Properties.ofFullCopy(base).strength(2.0f, 6.0f).sound(SoundType.STONE), true);
        ALL_SLABS.add(slab);
        SLAB_BASE.put(slab, base);
        return slab;
    }

    // Fence cut from an existing block. Must be declared AFTER every base. */
    static Block registerFence(String name, Block base) {
        Block fence = register(name, FenceBlock::new,
                BlockBehaviour.Properties.ofFullCopy(base), true);
        ALL_FENCES.add(fence);
        FENCE_BASE.put(fence, base);
        return fence;
    }

    // Wall cut from an existing block. Must be declared AFTER every base. */
    static Block registerWall(String name, Block base) {
        Block wall = register(name, WallBlock::new,
                BlockBehaviour.Properties.ofFullCopy(base), true);
        ALL_WALLS.add(wall);
        WALL_BASE.put(wall, base);
        return wall;
    }

    private LOTRBlocks() {
    }

    static Block registerColumn(String name) {
        return registerColumn(name, 1.5f, 6.0f);
    }

    static Block registerColumn(String name, float hardness, float resistance) {
        return track(ALL_COLUMNS, register(name, Block::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.STONE)
                        .requiresCorrectToolForDrops()
                        .strength(hardness, resistance)
                        .sound(SoundType.STONE),
                true));
    }

    static Block registerWallTorch(String name, LOTRGlowStyle glow) {
        return register(name, props -> new LOTRWallTorchBlock(glow, props),
                torchProperties(torchLight(glow)), false);
    }

    /**
     * LOTRBlockWoodElvenTorch set 0.9375 (light 14); the high-elven, Morgul and
     * mallorn torches all set 0.875 (light 13).
     */
    static int torchLight(LOTRGlowStyle glow) {
        return glow == LOTRGlowStyle.WOOD_ELVEN_TORCH ? 14 : 13;
    }

    // The item is a StandingAndWallBlockItem, not a plain BlockItem: that is
    // what lets a torch be put on the SIDE of a block. With a plain BlockItem
    // the wall variants were registered but unreachable -- every LOTR torch
    // could only be stood on top of something.
    static Block registerTorch(String name, Block wallVariant, LOTRGlowStyle glow) {
        Block torch = register(name, props -> new LOTRTorchBlock(glow, props),
                torchProperties(torchLight(glow)), true, UnaryOperator.identity(),
                (block, props) -> new StandingAndWallBlockItem(
                        block, wallVariant, Direction.DOWN, props));
        ALL_TORCHES.add(torch);
        TORCH_WALL.put(torch, wallVariant);
        return torch;
    }

    // LOTRMod: new LOTRBlockDoubleTorch(). Two blocks tall, no wall form.
    // LOTRItemBanner / LOTREntityBanner: cloth on a post, hardness 1.0, wooden
    // footsteps and no collision box -- the original's banner entity had none
    // and you could walk through it. The DyeColor is vanilla's, unused by the
    // renderer; WHITE for all of them until something in the port actually
    // asks a banner for a colour.
    static BlockBehaviour.Properties bannerProperties() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.WOOD)
                .noCollision()
                .strength(1.0f)
                .sound(SoundType.WOOD)
                .ignitedByLava();
    }

    static Block registerBanner(String name, LOTRBannerType type, Block wallForm) {
        Block banner = track(ALL_BANNERS, register(name,
                props -> new LOTRBannerBlock(type, DyeColor.WHITE, props),
                bannerProperties(), true, props -> props.stacksTo(16),
                (block, props) -> new StandingAndWallBlockItem(block, wallForm, Direction.DOWN, props)));
        BANNER_WALL_FORM.put(banner, wallForm);
        return banner;
    }

    /** No item: the standing banner's StandingAndWallBlockItem places both. */
    static Block registerWallBanner(String name, LOTRBannerType type) {
        return track(ALL_WALL_BANNERS, register(name,
                props -> new LOTRWallBannerBlock(type, DyeColor.WHITE, props),
                bannerProperties(),
                false));
    }

    static Block registerDoubleTorch(String name) {
        // LOTRBlockDoubleTorch.getLightValue: 14 from the flame at the top, and
        // none from the shaft below it.
        return track(ALL_DOUBLE_TORCHES, register(name, LOTRDoubleTorchBlock::new,
                torchProperties(14).lightLevel(state ->
                        state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER ? 14 : 0),
                true));
    }

    // LOTRBlockBirdCage / LOTRBlockBirdCageWood: Material.glass, hardness 0.5,
    // metal footsteps, a full cube with its own top, side and base sprites.
    // Stack size 1 and a LOTRAnimalJarItem: a cage carrying a bird cannot
    // stack with an empty one, and the item is what catches the bird.
    static Block registerBirdCage(String name, SoundType sound) {
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
    static Block registerButterflyJar(String name) {
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
    static Block registerClover(String name, boolean replaceable) {
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

    static BlockBehaviour.Properties torchProperties(int light) {
        return BlockBehaviour.Properties.of()
                .noCollision()
                .instabreak()
                .lightLevel(state -> light)
                .sound(SoundType.WOOD)
                .pushReaction(PushReaction.DESTROY);
    }

    // Every faction table is built like today's vanilla crafting table (the
    // user's call), whatever Material the original gave it.
    static Block registerCraftingTable(String name) {
        LOTRCraftingTable table = LOTRCraftingTable.valueOf(name.replace("_crafting_table", "").toUpperCase(Locale.ROOT));
        return track(ALL_CRAFTING_TABLES, register(name, props -> new LOTRCraftingTableBlock(table, props),
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.WOOD)
                        .instrument(NoteBlockInstrument.BASS)
                        .strength(2.5f)
                        .sound(SoundType.WOOD)
                        // LOTRBlockMorgulTable alone glowed, setLightLevel(0.5f).
                        .lightLevel(state -> table == LOTRCraftingTable.MORGUL ? 7 : 0)
                        .ignitedByLava(),
                true));
    }

    static Block registerVine(String name) {
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

    static Block registerLadder(String name) {
        return track(ALL_LADDERS, register(name, LadderBlock::new,
                ladderProperties().sound(SoundType.LADDER), true));
    }

    // A rope is a ladder that can also hang from another rope. `canRetract` is
    // what separated the two in 1.7.10 -- plain rope was new LOTRBlockRope(false)
    // -- and the elven rope adds LOTRBlockHithlainRope's glow and its bite.
    static Block registerRope(String name, boolean canRetract, boolean elven) {
        BlockBehaviour.Properties props = ladderProperties().sound(SoundType.WOOL);
        if (elven) {
            // setLightLevel(0.375f) -> (int) (15 * 0.375) = 5.
            props = props.lightLevel(state -> 5);
        }
        return track(ALL_LADDERS, register(name,
                p -> new LOTRRopeBlock(canRetract, elven, p), props, true));
    }

    static BlockBehaviour.Properties ladderProperties() {
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
    static Block registerWoodenGate(String name) {
        return registerWoodenGate(name, true);
    }

    static Block registerStoneGate(String name) {
        return registerStoneGate(name, true);
    }

    static Block registerMetalGate(String name) {
        return registerMetalGate(name, true);
    }

    // LOTRBlockGate.createWooden/createStone/createMetal: hardness 4, and
    // setResistance 5 (wood) or 10 (stone, metal), which 1.7.10 turned into
    // blast resistance 3 and 6. Gates refuse pistons: a gate is a multiblock,
    // and shoving one panel out of it would break the flood fill for the rest.
    static Block registerWoodenGate(String name, boolean connectedTextures) {
        return track(ALL_GATES, register(name, props -> new LOTRGateBlock(connectedTextures, props),
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.WOOD)
                        .strength(4.0f, 3.0f)
                        .sound(SoundType.WOOD)
                        .noOcclusion()
                        .pushReaction(PushReaction.BLOCK),
                true));
    }

    static Block registerStoneGate(String name, boolean connectedTextures) {
        return track(ALL_GATES, register(name, props -> new LOTRGateBlock(connectedTextures, props),
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.STONE)
                        .requiresCorrectToolForDrops()
                        .strength(4.0f, 6.0f)
                        .sound(SoundType.STONE)
                        .noOcclusion()
                        .pushReaction(PushReaction.BLOCK),
                true));
    }

    static Block registerMetalGate(String name, boolean connectedTextures) {
        return track(ALL_GATES, register(name, props -> new LOTRGateBlock(connectedTextures, props),
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.METAL)
                        .requiresCorrectToolForDrops()
                        .strength(4.0f, 6.0f)
                        .sound(SoundType.METAL)
                        .noOcclusion()
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
    static Block registerDwarvenDoor(String name,
                                             Function<BlockBehaviour.Properties, Block> factory) {
        return track(ALL_GATES, register(name, factory,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.STONE)
                        .requiresCorrectToolForDrops()
                        .strength(4.0f, 6.0f)
                        .sound(SoundType.STONE)
                        .pushReaction(PushReaction.BLOCK),
                true));
    }

    /**
     * Beds are hardness 0.2 with a wood step sound, and LOTRItemBed set
     * maxStackSize 1 -- a bed is two blocks, so a stack of them would place
     * wrong.
     */
    static Block registerBed(String name) {
        return track(ALL_BEDS, register(name, LOTRBedBlock::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.WOOL)
                        .strength(0.2f)
                        .sound(SoundType.WOOD)
                        .noOcclusion(),
                item -> item.stacksTo(1)));
    }

    static Block registerChest(String name, String variant, MapColor color,
                                       float hardness, SoundType sound) {
        BlockBehaviour.Properties props = BlockBehaviour.Properties.of()
                .mapColor(color)
                .strength(hardness)
                .sound(sound)
                .noOcclusion();
        // The Material.rock chests need a pickaxe to drop, as stone does.
        if (color == MapColor.STONE) {
            props = props.requiresCorrectToolForDrops();
        }
        return track(ALL_CHESTS, register(name, p -> new LOTRChestBlock(variant, p), props, true));
    }

    static Block registerKebabStand(String name, String variant) {
        return track(ALL_KEBAB_STANDS, register(name, props -> new LOTRKebabStandBlock(variant, props),
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.WOOD)
                        .strength(0.0f, 0.6f) // 1.7.10 blast resistance = setResistance x 3 / 5 (or the hardness, if higher).
                        .sound(SoundType.WOOD)
                        .noOcclusion()
                        .noCollision()
                        .pushReaction(PushReaction.DESTROY),
                // No loot table: the block entity drops the stand with its meat inside.
                true, UnaryOperator.identity(), LOTRKebabStandItem::new));
    }

    static Block registerTrollTotem(String name, LOTRTrollTotemBlock.Part part) {
        return track(ALL_TROLL_TOTEMS, register(name, props -> new LOTRTrollTotemBlock(part, props),
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.STONE)
                        // LOTRMod: trollTotem.setHardness(5.0f).setResistance(20.0f)
                        // -- blast resistance 12 -- on Material.rock.
                        .requiresCorrectToolForDrops()
                        .strength(5.0f, 12.0f)
                        .sound(SoundType.STONE)
                        .noOcclusion()
                        // A totem is a three-block object; a piston pulling one
                        // part out would leave a broken idol behind.
                        .pushReaction(PushReaction.BLOCK),
                true));
    }

    static Block registerFenceGate(String name, Block base) {
        Block gate = track(ALL_FENCE_GATES, register(name, props -> new FenceGateBlock(net.minecraft.world.level.block.state.properties.WoodType.OAK, props),
                BlockBehaviour.Properties.ofFullCopy(base), true));

        FENCE_GATE_BASE.put(gate, base);
        return gate;
    }

    static Block registerButton(String name, Block base) {
        Block button = register(name,
                props -> new ButtonBlock(BlockSetType.STONE, 20, props),
                BlockBehaviour.Properties.ofFullCopy(base).noCollision().strength(0.5f),
                true);
        ALL_BUTTONS.add(button);
        BUTTON_BASE.put(button, base);
        return button;
    }

    static Block registerPressurePlate(String name, Block base) {
        Block plate = register(name,
                props -> new PressurePlateBlock(BlockSetType.STONE, props),
                BlockBehaviour.Properties.ofFullCopy(base).noCollision().strength(0.5f),
                true);
        ALL_PRESSURE_PLATES.add(plate);
        PRESSURE_PLATE_BASE.put(plate, base);
        return plate;
    }

    // Crop with growth stages. CropBlock always has eight ages, but the 1.7.10 crops shipped only three or four stage textures -- CROP_STAGES records how many, and datagen maps several ages onto each texture. Registered WITH an item even though vanilla crops have none. Datagen's createCropBlock emits an item model, and with no item every crop resolves to minecraft:item/air -- six crops then collide on the same model id and datagen dies with "Duplicate model definition". Giving each crop a BlockItem keeps the ids distinct; it also makes them placeable for testing until the seed items exist.
    static Block registerCrop(String name, int stages) {
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
    static Block registerBush(String name) {
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
    static Block registerWeb(String name) {
        return register(name, WebBlock::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.WOOL)
                        .forceSolidOn()
                        .noCollision()
                        .requiresCorrectToolForDrops()
                        .strength(2.0f) // LOTRMod: webUngoliant setHardness(2.0f), not vanilla cobweb's 4.
                        .noOcclusion()
                        .pushReaction(PushReaction.DESTROY),
                true);
    }

    // Gravel- and sand-like block that falls when unsupported. The 1.7.10 originals extended BlockSand/BlockGravel, so they obeyed gravity; a plain Block does not. FallingBlock itself is abstract, so this uses ColoredFallingBlock the way vanilla gravel does -- the colour is the dust tint for the falling particles. Registered outside the tier lists like the other soft blocks, since the originals set no harvest level.
    static Block registerFalling(String name, float hardness, int dustColor) {
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

    static Block registerPath(String name) {
        return track(SHOVEL_MINEABLE, track(ALL_PATHS, register(name, DirtPathBlock::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.DIRT)
                        .strength(0.5f) // LOTRBlockDirtPath: hardness 0.5, soundTypeGravel.
                        .sound(SoundType.GRAVEL)
                        .isViewBlocking((state, level, pos) -> true)
                        .isSuffocating((state, level, pos) -> true),
                true)));
    }

    static Block registerFarmland(String name) {
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

    static Block registerRail(String name, boolean defaultPower, boolean withItem) {
        return track(ALL_RAILS, register(name, p -> new LOTRMechanisedRailBlock(defaultPower, p),
                BlockBehaviour.Properties.of()
                        .noCollision()
                        .strength(0.7f)
                        .sound(SoundType.METAL)
                        .pushReaction(PushReaction.DESTROY),
                withItem));
    }

    // noOcclusion matters more than it looks: without it the block UNDER the
    // carpet culls its own top face, and since the carpet is only one pixel
    // thick you end up looking straight through the floor. LOTRBlockMordorMoss
    // and LOTRBlockThatchFloor both returned false from isOpaqueCube for the
    // same reason.
    static Block registerCarpet(String name, float hardness) {
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

    static Block registerSoilColumn(String name, float hardness, float resistance,
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

    static Block registerBottomTop(String name, float hardness, float resistance) {
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

    static Block registerSoil(String name, float hardness, float resistance, SoundType sound) {
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

    static Block registerCubeColumn(String name, float hardness, float resistance, Tier tier) {
        return registerCubeColumn(name, hardness, resistance, tier, SoundType.STONE);
    }

    static Block registerCubeColumn(String name, float hardness, float resistance, Tier tier, SoundType sound) {
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
    static Block registerForge(String name) {
        return track(ALL_FORGES, track(CUBES_NO_TIER, register(name, LOTRForgeBlock::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.STONE)
                        .requiresCorrectToolForDrops()
                        .strength(4.0f) // LOTRBlockForgeBase: setHardness(4) only.
                        .sound(SoundType.STONE)
                        .lightLevel(state -> state.getValue(net.minecraft.world.level.block.AbstractFurnaceBlock.LIT) ? 13 : 0),
                true)));
    }

    static Block registerDartTrap(String name, Block base) {
        Block block = register(name, LOTRDartTrapBlock::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.STONE)
                        .requiresCorrectToolForDrops()
                        // LOTRBlockDartTrap: setHardness(4) alone, so blast resistance 4 too.
                        .strength(4.0f)
                        .sound(SoundType.STONE),
                true);
        ALL_DART_TRAPS.add(block);
        CUBES_NO_TIER.add(block);
        DART_TRAP_BASE.put(block, base);
        return block;
    }

    static Block registerHobbitOven(String name) {
        return register(name, LOTRHobbitOvenBlock::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.STONE)
                        .requiresCorrectToolForDrops()
                        .strength(3.5f)
                        .sound(SoundType.STONE)
                        .lightLevel(state -> state.getValue(LOTRHobbitOvenBlock.LIT) ? 13 : 0),
                true);
    }

    static Block registerBeacon(String name) {
        return register(name, LOTRBeaconBlock::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.WOOD)
                        .strength(0.0f, 3.0f) // 1.7.10 blast resistance = setResistance x 3 / 5 (or the hardness, if higher).
                        .sound(SoundType.WOOD)
                        // isFullyLit() drove getLightValue in the original. A
                        // blockstate light level cannot ask the block entity,
                        // so FULLY_LIT -- set a hundred ticks after ignition --
                        // is the flag that carries it.
                        .lightLevel(state -> state.getValue(LOTRBeaconBlock.FULLY_LIT) ? 15 : 0)
                        .noOcclusion(),
                true);
    }

    static Block registerGulduril(String name) {
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
    static Block registerOre(String name, Tier tier, int light, int xpMin, int xpMax) {
        Block block = register(name, props -> new DropExperienceBlock(UniformInt.of(xpMin, xpMax), props),
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.STONE)
                        .requiresCorrectToolForDrops()
                        .strength(3.0f, 3.0f)
                        .sound(SoundType.STONE)
                        .lightLevel(state -> light),
                true);
        ALL_CUBES.add(block);
        switch (tier) {
            case IRON -> CUBES_IRON_TIER.add(block);
            case STONE -> CUBES_STONE_TIER.add(block);
            case NONE -> CUBES_NO_TIER.add(block);
        }
        return block;
    }

    static Block registerCube(String name, float hardness, float resistance, Tier tier) {
        return registerCube(name, hardness, resistance, tier, SoundType.STONE);
    }

    // LOTRBlockOreStorageBase set soundTypeMetal, so every metal and gem
    // storage block rings rather than clacks -- same as vanilla's iron block.
    static Block registerCube(String name, float hardness, float resistance, Tier tier, SoundType sound) {
        return registerCube(name, hardness, resistance, tier, sound, 0);
    }

    // The same, glowing: setLightLevel(f) was (int) (15 * f), or a
    // getLightValue override for the one metadata that shone.
    static Block registerCube(String name, float hardness, float resistance, Tier tier, SoundType sound,
            int light) {
        return registerCube(name, Block::new, hardness, resistance, tier, sound, light);
    }

    static Block registerCube(String name, Function<BlockBehaviour.Properties, Block> factory, float hardness,
            float resistance, Tier tier, SoundType sound, int light) {
        Block block = register(name, factory,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.STONE)
                        .requiresCorrectToolForDrops()
                        .strength(hardness, resistance)
                        .sound(sound)
                        .lightLevel(state -> light),
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
    // instantly and never blocks a piston.
    // LOTRBlockOrcBomb: Material.iron, hardness 3, resistance 0 -- it is no
    // more blast-proof than air, because a blast is meant to set it off rather
    // than be soaked up by it.
    static Block registerOrcBomb(String name, int strengthLevel, boolean fire) {
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
    static Block registerTreasurePile(String name) {
        Block pile = track(ALL_TREASURE_PILES, register(name, LOTRTreasurePileBlock::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.METAL)
                        .instabreak()
                        .sound(TREASURE_SOUND)
                        .noOcclusion()
                        .pushReaction(PushReaction.DESTROY),
                false));
        // register(withItem = false) skips ALL_BLOCKS as well as the item, and
        // the catch-all EVERYTHING tab walks that list.
        ALL_BLOCKS.add(pile);

        TREASURE_PILE_CARPETS.put(pile, registerTreasurePileItem(pile, name + "_carpet", 1));
        TREASURE_PILE_BLOCKS.put(pile, registerTreasurePileItem(pile, name, LOTRTreasurePileBlock.MAX_LAYERS));
        return pile;
    }

    /**
     * One of the two forms a treasure pile is carried in -- the two-pixel carpet
     * at one layer, or the full block at eight.
     *
     * <p>Registered here and not in LOTRItems. Calling into LOTRItems from this
     * class's static initialiser loaded LOTRItems halfway through LOTRBlocks,
     * before the crops further down existed, so every seed food was built
     * around a null block -- which is what crashed planting lettuce.
     */
    static Item registerTreasurePileItem(Block pile, String name, int layers) {
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM,
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, name));
        Item item = new LOTRTreasurePileItem(pile, layers, new Item.Properties().setId(key));
        Registry.register(BuiltInRegistries.ITEM, key, item);
        return item;
    }

    // LOTRBlockPlaceableFood: Material.cake, hardness 0.5, cloth steps.
    static Block registerPlaceableFood(String name, float halfWidth, float height, int heal,
            float saturation, boolean oneToAStack) {
        UnaryOperator<Item.Properties> itemProperties = oneToAStack
                ? props -> props.stacksTo(1)
                : UnaryOperator.identity();
        return register(name, props -> new LOTRPlaceableFoodBlock(halfWidth, height, heal, saturation, props),
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.WOOL)
                        .strength(0.5f)
                        .sound(SoundType.WOOL)
                        .noOcclusion()
                        .pushReaction(PushReaction.DESTROY),
                true, itemProperties);
    }

    static Block registerHangingFruit(String name, double minY, double maxY,
            java.util.function.Supplier<Item> fruit) {
        return register(name, props -> new LOTRHangingFruitBlock(minY, maxY, fruit, props),
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.PLANT)
                        // setHardness(0).setResistance(1): 1.7.10 stored 3x
                        // and exploded against a fifth, so 0.6 of blast resistance.
                        .strength(0.0f, 0.6f)
                        .sound(SoundType.WOOD)
                        .randomTicks()
                        .noOcclusion()
                        .pushReaction(PushReaction.DESTROY),
                false);
    }

    // LOTRBlockPlate: Material.circuits, hardness 0.
    static Block registerPlate(String name, SoundType sound) {
        return track(ALL_PLATES, register(name, LOTRPlateBlock::new,
                BlockBehaviour.Properties.of()
                        .instabreak()
                        .sound(sound)
                        .noOcclusion()
                        .pushReaction(PushReaction.DESTROY),
                true, UnaryOperator.identity(), LOTRPlateItem::new));
    }

    // LOTRBlockMug: Material.circuits, hardness 0; the vessel items place it.
    static Block registerMug(String name, LOTRVessel vessel, float width, float height, SoundType sound) {
        return track(ALL_MUG_BLOCKS, register(name, props -> new LOTRMugBlock(vessel, width, height, props),
                BlockBehaviour.Properties.of()
                        .instabreak()
                        .sound(sound)
                        .noOcclusion()
                        .pushReaction(PushReaction.DESTROY),
                false));
    }

    static Block registerCarvedSign(String name) {
        return register(name, LOTRCarvedSignBlock::new,
                BlockBehaviour.Properties.of()
                        .strength(0.5f)
                        .sound(SoundType.STONE)
                        .noCollision()
                        .noOcclusion()
                        .pushReaction(PushReaction.DESTROY),
                false);
    }

    static Block registerSoftBlock(String name, float hardness, SoundType sound) {
        return track(ALL_CUBES, register(name, Block::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.STONE)
                        .strength(hardness)
                        .sound(sound),
                true));
    }

    static Block registerPlanks(String name) {
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
    static Block registerLeaves(String name) {
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

    static Block registerLog(String name) {
        return track(ALL_LOGS, register(name, RotatedPillarBlock::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.WOOD)
                        .instrument(NoteBlockInstrument.BASS)
                        .strength(2.0f)
                        .sound(SoundType.WOOD)
                        .ignitedByLava(),
                true));
    }

    static Block registerBeam(String name) {
        return track(ALL_BEAMS, register(name, RotatedPillarBlock::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.WOOD)
                        .instrument(NoteBlockInstrument.BASS)
                        .strength(2.0f)
                        .sound(SoundType.WOOD)
                        .ignitedByLava(),
                true));
    }

    static Block registerPillar(String name) {
        return registerPillar(name, 6.0f);
    }

    static Block registerPillar(String name, float resistance) {
        return track(ALL_PILLARS, register(name, LOTRPillarBlock::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.STONE)
                        .requiresCorrectToolForDrops()
                        .strength(1.5f, resistance)
                        .sound(SoundType.STONE),
                true));
    }

    // A real SaplingBlock, not a plain Block: LOTRBlockSaplingBase extended
    // BlockSapling in 1.7.10, and only the vanilla class brings the STAGE
    // property, the "must stand on dirt" survival check and BonemealableBlock.
    // randomTicks() is what lets SaplingBlock.randomTick run at all. The tree
    // features are not ported yet, so each gets a placeholder grower -- see
    // LOTRSaplingBlock.
    static Block registerSapling(String name) {
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

    static Block registerDoubleFlower(String name) {
        return track(ALL_DOUBLE_FLOWERS, register(name, net.minecraft.world.level.block.TallFlowerBlock::new,
                plantProperties().offsetType(BlockBehaviour.OffsetType.XZ), true));
    }

    static Block registerFlower(String name) {
        return registerFlower(name, LOTRPlantBlock.Shape.FLOWER);
    }

    static Block registerFlower(String name, LOTRPlantBlock.Shape shape) {
        return registerFlower(name, shape, LOTRPlantBlock.Ground.SOIL);
    }

    // A LOTRBlockFlower: the shape is its setBlockBounds/setFlowerBounds call
    // and the ground its canBlockStay override, both from 1.7.10. XZ offset
    // because LOTRRenderBlocks.renderFlowerBlock jittered a flower's position
    // by up to 0.15 in x and z from a hash of its coordinates -- that is
    // exactly what OffsetType.XZ does.
    static Block registerFlower(String name, LOTRPlantBlock.Shape shape,
            LOTRPlantBlock.Ground ground) {
        return track(ALL_FLOWERS, register(name, props -> new LOTRPlantBlock(shape, ground, props),
                plantProperties().offsetType(BlockBehaviour.OffsetType.XZ),
                true));
    }

    // A LOTRBlockGrass. Two things separate it from a flower: renderGrass
    // jittered y as well as x and z (OffsetType.XYZ), and isReplaceable
    // returned true, so you can build straight through it.
    static Block registerGrass(String name, LOTRPlantBlock.Ground ground) {
        return registerGrass(name, ground, LOTRPlantBlock.Sting.NONE);
    }

    static Block registerGrass(String name, LOTRPlantBlock.Ground ground,
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
    static Block registerReed(String name, boolean grows) {
        return track(ALL_FLOWERS, track(ALL_COLUMN_PLANTS, register(name,
                props -> new LOTRReedBlock(grows, props),
                plantProperties().randomTicks(), true)));
    }

    static Block registerCorn(String name) {
        return track(ALL_FLOWERS, track(ALL_COLUMN_PLANTS, register(name, LOTRCornBlock::new,
                plantProperties().randomTicks(), true)));
    }

    // Corn, reeds and the grapevine post: full-height columns that grow upward,
    // drawn by their own renderers in 1.7.10 with no positional jitter.
    static Block registerGrapevine(String name) {
        return register(name, LOTRGrapevineBlock::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.PLANT)
                        .noCollision()
                        .instabreak()
                        .randomTicks()
                        .sound(SoundType.GRASS)
                        .noOcclusion()
                        .pushReaction(PushReaction.DESTROY),
                false);
    }

    // Fangorn riverweed, the mod's one lily pad.
    // Riverweed keeps its collision box: a lily pad is something you stand on,
    // and vanilla's does not set noCollision. plantProperties() does, so the
    // riverweed builds its own.
    static Block registerRiverweed(String name) {
        return track(ALL_FLOWERS, register(name, LOTRRiverweedBlock::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.PLANT)
                        .instabreak()
                        .sound(SoundType.LILY_PAD)
                        .noOcclusion()
                        .pushReaction(PushReaction.DESTROY),
                PlaceOnWaterBlockItem::new));
    }

    static BlockBehaviour.Properties plantProperties() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .noCollision()
                .instabreak()
                .sound(SoundType.GRASS)
                .noOcclusion()
                .pushReaction(PushReaction.DESTROY);
    }

    static Block registerTrapdoor(String name) {
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

    static Block registerDoor(String name) {
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
    static Block registerGlassPane(String name, Block glass) {
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

    static Block registerBars(String name) {
        return track(ALL_BARS, register(name, IronBarsBlock::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.NONE)
                        .requiresCorrectToolForDrops()
                        .strength(5.0f, 6.0f)
                        .sound(SoundType.METAL)
                        .noOcclusion(),
                true));
    }

    // LOTRBlockWoodBars: Material.wood, setHardness(2.0), setResistance(5.0)
    // (blast resistance 3), soundTypeWood. Wood needs no tool to drop.
    static Block registerWoodBars(String name) {
        return track(ALL_BARS, register(name, IronBarsBlock::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.WOOD)
                        .strength(2.0f, 3.0f)
                        .sound(SoundType.WOOD)
                        .ignitedByLava()
                        .noOcclusion(),
                true));
    }

    // LOTRBlockReedBars: Material.grass, setHardness(0.5), soundTypeGrass.
    static Block registerReedBars(String name) {
        return track(ALL_BARS, register(name, IronBarsBlock::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.PLANT)
                        .strength(0.5f)
                        .sound(SoundType.GRASS)
                        .ignitedByLava()
                        .noOcclusion(),
                true));
    }

    static Block registerChandelier(String name, LOTRGlowStyle style) {
        return track(ALL_CHANDELIERS, register(name, props -> new LOTRChandelierBlock(style, props),
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.NONE)
                        .noCollision()
                        .strength(0.0f, 1.2f) // 1.7.10 blast resistance = setResistance x 3 / 5 (or the hardness, if higher).
                        .sound(SoundType.METAL)
                        // setLightLevel(0.9375f): 14.
                        .lightLevel(state -> 14)
                        .noOcclusion()
                        .pushReaction(PushReaction.DESTROY),
                true));
    }

    static Block registerGlass(String name) {
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

    static Block track(List<Block> family, Block block) {
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
    /**
     * LOTRBlockFallenLeaves: Material.vine at hardness 0.2 with grass steps --
     * replaceable, lit by lava, broken by pistons -- and no collision box.
     */
    static Block registerFallenLeaves(Block leaves) {
        String leafName = BuiltInRegistries.BLOCK.getKey(leaves).getPath();
        Block block = register("fallen_" + leafName, props -> new LOTRFallenLeavesBlock(leaves, props),
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.PLANT)
                        .strength(0.2f)
                        .sound(SoundType.GRASS)
                        .noCollision()
                        .noOcclusion()
                        .replaceable()
                        .ignitedByLava()
                        .pushReaction(PushReaction.DESTROY),
                LOTRFallenLeavesItem::new);
        ALL_FALLEN_LEAVES.add(block);
        return block;
    }

    /**
     * new LOTRBlockStalactite(model, 0): the model block's hardness,
     * resistance, sound and tool needs. Not its slipperiness, which the
     * original never copied, so the ice pair is not slippery.
     */
    static Block registerStalactite(String name, Block model, boolean hanging) {
        Block block = register(name, props -> new LOTRStalactiteBlock(props, hanging),
                BlockBehaviour.Properties.ofFullCopy(model).friction(0.6f).noOcclusion(), true);
        ALL_STALACTITES.add(block);
        return block;
    }

    static Block register(String name, Function<BlockBehaviour.Properties, Block> factory,
                                  BlockBehaviour.Properties properties,
                                  BiFunction<Block, Item.Properties, BlockItem> itemFactory) {
        return register(name, factory, properties, true, UnaryOperator.identity(), itemFactory);
    }

    static Block register(String name, Function<BlockBehaviour.Properties, Block> factory,
                                  BlockBehaviour.Properties properties, boolean withItem,
                                  UnaryOperator<Item.Properties> itemProperties) {
        return register(name, factory, properties, withItem, itemProperties, BlockItem::new);
    }

    static Block register(String name, Function<BlockBehaviour.Properties, Block> factory,
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

    // Loads the tab classes (their blocks register as they load, in the
    // order they first appeared in this file), then the passes that need
    // every block to exist. Called from mod init.
    public static void init() {
        LOTRBuildingBlocks.init();
        LOTRUtilityBlocks.init();
        LOTRFoodBlocks.init();
        LOTRDecorationBlocks.init();
        LOTRCombatBlocks.init();
        LOTRMiscBlocks.init();
        pairLeavesWithSaplings();
        NOT_SMALL_FLOWERS.addAll(GRASS_TINTED);
        NOT_SMALL_FLOWERS.addAll(List.of(ARID_GRASS, MORDOR_GRASS, MORDOR_THORN,
                CORN_STALK, REEDS, DRIED_REEDS, GRAPEVINE, FANGORN_RIVERWEED,
                DEAD_MARSH_PLANT, CORRUPT_MALLORN, MORGUL_SHROOM));
    }

    // <x>_leaves -> <x>_sapling. Every leaf block in the mod has a matching
    // sapling, so a missing pair means one of the two lists has drifted and the
    // leaf would silently drop nothing -- hence the hard failure.
    static void pairLeavesWithSaplings() {
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
