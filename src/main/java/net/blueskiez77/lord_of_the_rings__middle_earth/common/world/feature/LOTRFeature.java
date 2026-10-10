package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiomes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;

import org.jspecify.annotations.Nullable;

/**
 * 1.7.10's WorldGenerator and WorldGenAbstractTree, for the mod's trees and features: placing
 * blocks (notifying neighbours when grown from a sapling, {@link #notify}), and the World and
 * Block questions the original asked -- what is there, whether it may be replaced, whether it is
 * leaves or wood, whether a plant could stand on it.
 *
 * <p>The leaves a feature places are given their distance from the nearest log once it is done
 * (as vanilla's TreeFeature does), today's leaves decaying by that distance where 1.7.10's
 * checked for wood when they ticked.
 */
public abstract class LOTRFeature implements LOTRWorldGenerator {

    /** 1.7.10's Direction.offsetX and offsetZ: south, west, north, east. */
    protected static final int[] DIR_OFFSET_X = {0, -1, 0, 1};
    protected static final int[] DIR_OFFSET_Z = {1, 0, -1, 0};

    protected final boolean notify;
    private @Nullable List<BlockPos> placedLeaves;

    protected LOTRFeature(boolean notify) {
        this.notify = notify;
    }

    protected LOTRFeature() {
        this(false);
    }

    @Override
    // Synchronized: a biome's generators are shared, and 26.2 decorates chunks on several threads
    // at once, where 1.7.10 decorated one at a time; many generators keep state as they work.
    public final synchronized boolean generate(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        List<BlockPos> outer = this.placedLeaves;
        this.placedLeaves = new ArrayList<>();
        try {
            boolean generated = generateFeature(world, random, i, j, k);
            fixLeafDistances(world, this.placedLeaves);
            return generated;
        } finally {
            this.placedLeaves = outer;
        }
    }

    /** The original's generate. */
    protected abstract boolean generateFeature(WorldGenLevel world, RandomSource random, int i, int j, int k);

    // --- Reading the world ----------------------------------------------------

    public static BlockState getBlock(WorldGenLevel world, int i, int j, int k) {
        return world.getBlockState(new BlockPos(i, j, k));
    }

    public static boolean isAirBlock(WorldGenLevel world, int i, int j, int k) {
        return world.isEmptyBlock(new BlockPos(i, j, k));
    }

    public static int getHeightValue(WorldGenLevel world, int i, int k) {
        return LOTRWorldGenUtil.getHeightValue(world, i, k);
    }

    /** getPrecipitationHeight: one above the highest block that stops movement or holds a fluid. */
    public static int getPrecipitationHeight(WorldGenLevel world, int i, int k) {
        return world.getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING, i, k);
    }

    public static int getTopSolidOrLiquidBlock(WorldGenLevel world, int i, int k) {
        return LOTRWorldGenUtil.getTopSolidOrLiquidBlock(world, i, k);
    }

    public static @Nullable LOTRBiome getBiome(WorldGenLevel world, int i, int k) {
        return LOTRBiomes.of(world.getBiome(new BlockPos(i, 64, k)));
    }

    // --- Asking about blocks --------------------------------------------------

    /** Block.isReplaceable: air, plants, snow layers, fluids and the like. */
    public static boolean isBlockReplaceable(BlockState state) {
        return state.canBeReplaced();
    }

    public static boolean isLeaves(BlockState state) {
        return state.is(BlockTags.LEAVES);
    }

    public static boolean isWood(BlockState state) {
        return state.is(BlockTags.LOGS);
    }

    public static boolean isOpaqueCube(BlockState state) {
        return state.isSolidRender();
    }

    public static boolean isLiquid(BlockState state) {
        return !state.getFluidState().isEmpty();
    }

    public static boolean isWater(BlockState state) {
        return state.getFluidState().is(net.minecraft.tags.FluidTags.WATER);
    }

    /** Material.air / leaves / wood, or grass, dirt, a log, a sapling or vines: what a tree may grow through (func_150523_a). */
    public static boolean isTreeReplaceable(BlockState state) {
        return state.isAir() || isLeaves(state) || isWood(state) || state.is(Blocks.GRASS_BLOCK) || state.is(BlockTags.DIRT)
                || state.getBlock() instanceof net.minecraft.world.level.block.SaplingBlock || state.is(Blocks.VINE);
    }

    /** WorldGenAbstractTree.isReplaceable. */
    public boolean isReplaceable(WorldGenLevel world, int i, int j, int k) {
        return isTreeReplaceable(getBlock(world, i, j, k));
    }

    /** Block.canSustainPlant(.., UP, sapling): ground a sapling could stand on. */
    public static boolean canSustainPlant(WorldGenLevel world, int i, int j, int k) {
        return Blocks.OAK_SAPLING.defaultBlockState().canSurvive(world, new BlockPos(i, j + 1, k));
    }

    /** onPlantGrow: grass, podzol or mycelium under a growing trunk turns to dirt. */
    public static void onPlantGrow(WorldGenLevel world, int i, int j, int k) {
        BlockPos pos = new BlockPos(i, j, k);
        BlockState state = world.getBlockState(pos);
        if (state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.MYCELIUM) || state.is(Blocks.PODZOL)) {
            world.setBlock(pos, Blocks.DIRT.defaultBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    public static boolean isOpaqueAt(WorldGenLevel world, int i, int j, int k) {
        return isOpaqueCube(getBlock(world, i, j, k));
    }

    /** Material.rock: stone and the like, broken by a pickaxe. */
    public static boolean isRock(BlockState state) {
        return state.is(BlockTags.MINEABLE_WITH_PICKAXE) && state.isSolidRender();
    }

    /** Block.canBlockStay: whether the block (at metadata 0) could stand there. */
    public static boolean canBlockStay(LegacyBlock block, WorldGenLevel world, int i, int j, int k) {
        return block.state(0).canSurvive(world, new BlockPos(i, j, k));
    }

    public static boolean canBlockStay(BlockState state, WorldGenLevel world, int i, int j, int k) {
        return state.canSurvive(world, new BlockPos(i, j, k));
    }

    /** Block.canBeReplacedByLeaves: anything but an opaque block. */
    public static boolean canBeReplacedByLeaves(BlockState state) {
        return !state.isSolidRender();
    }

    // --- Placing blocks -------------------------------------------------------

    public void setBlockAndNotifyAdequately(WorldGenLevel world, int i, int j, int k, LegacyBlock block, int meta) {
        setBlockAndNotifyAdequately(world, i, j, k, block.state(meta));
    }

    public void setBlockAndNotifyAdequately(WorldGenLevel world, int i, int j, int k, BlockState state) {
        setBlock(world, i, j, k, state, this.notify ? Block.UPDATE_ALL : Block.UPDATE_CLIENTS);
    }

    /** func_150515_a. */
    public void setBlock(WorldGenLevel world, int i, int j, int k, LegacyBlock block) {
        setBlockAndNotifyAdequately(world, i, j, k, block.state(0));
    }

    public void setBlock(WorldGenLevel world, int i, int j, int k, LegacyBlock block, int meta, int flags) {
        setBlock(world, i, j, k, block.state(meta), flags);
    }

    public void setBlock(WorldGenLevel world, int i, int j, int k, BlockState state, int flags) {
        BlockPos pos = new BlockPos(i, j, k);
        world.setBlock(pos, state, flags);
        if (this.placedLeaves != null && state.hasProperty(LeavesBlock.DISTANCE)) {
            this.placedLeaves.add(pos);
        }
    }

    /**
     * BlockDoublePlant.func_149889_c behind canPlaceBlockAt: a two-block plant, if it may stand there
     * with room above (checked when {@code check}).
     */
    public boolean placeDoublePlant(WorldGenLevel world, BlockState lower, int i, int j, int k, boolean check) {
        BlockPos pos = new BlockPos(i, j, k);
        if (check && (!lower.canSurvive(world, pos) || !world.isEmptyBlock(pos.above()))) {
            return false;
        }
        if (lower.getBlock() instanceof net.minecraft.world.level.block.DoublePlantBlock) {
            net.minecraft.world.level.block.DoublePlantBlock.placeAt(world, lower, pos, Block.UPDATE_CLIENTS);
        } else {
            world.setBlock(pos, lower, Block.UPDATE_CLIENTS);
        }
        return true;
    }

    /** Gives each placed leaf its distance from the nearest log (or 7, to decay, when none is within six). */
    protected static void fixLeafDistances(WorldGenLevel world, List<BlockPos> leaves) {
        if (leaves.isEmpty()) {
            return;
        }
        Map<BlockPos, Integer> distance = new HashMap<>();
        Deque<BlockPos> queue = new ArrayDeque<>();
        for (BlockPos pos : leaves) {
            if (!world.getBlockState(pos).hasProperty(LeavesBlock.DISTANCE)) {
                continue;
            }
            distance.put(pos, LeavesBlock.DECAY_DISTANCE);
            for (Direction dir : Direction.values()) {
                OptionalInt d = LeavesBlock.getOptionalDistanceAt(world.getBlockState(pos.relative(dir)));
                if (d.isPresent() && d.getAsInt() == 0) {
                    distance.put(pos, 1);
                    queue.add(pos);
                    break;
                }
            }
        }
        while (!queue.isEmpty()) {
            BlockPos pos = queue.poll();
            int next = distance.get(pos) + 1;
            for (Direction dir : Direction.values()) {
                BlockPos n = pos.relative(dir);
                Integer d = distance.get(n);
                if (d != null && d > next) {
                    distance.put(n, next);
                    queue.add(n);
                }
            }
        }
        for (Map.Entry<BlockPos, Integer> e : distance.entrySet()) {
            BlockState state = world.getBlockState(e.getKey());
            if (state.hasProperty(LeavesBlock.DISTANCE)) {
                int d = Math.min(e.getValue(), LeavesBlock.DECAY_DISTANCE);
                if (state.getValue(LeavesBlock.DISTANCE) != d) {
                    world.setBlock(e.getKey(), state.setValue(LeavesBlock.DISTANCE, d), Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE);
                }
            }
        }
    }


    /**
     * Another generator, run as a feature: afterwards the leaves it left within {@code radius}
     * across and {@code height} up of where it stood, not yet sure of their distance, are given it.
     */
    public static final class Wrapped extends LOTRFeature {
        private final LOTRWorldGenerator inner;
        private final int radius;
        private final int height;

        public Wrapped(LOTRWorldGenerator inner, int radius, int height) {
            this.inner = inner;
            this.radius = radius;
            this.height = height;
        }

        @Override
        protected boolean generateFeature(WorldGenLevel world, RandomSource random, int i, int j, int k) {
            boolean generated = this.inner.generate(world, random, i, j, k);
            if (generated) {
                List<BlockPos> leaves = new ArrayList<>();
                for (BlockPos pos : BlockPos.betweenClosed(i - this.radius, j - 8, k - this.radius, i + this.radius, j + this.height, k + this.radius)) {
                    BlockState state = world.getBlockState(pos);
                    if (state.hasProperty(LeavesBlock.DISTANCE) && !state.getValue(LeavesBlock.PERSISTENT)
                            && state.getValue(LeavesBlock.DISTANCE) == LeavesBlock.DECAY_DISTANCE) {
                        leaves.add(pos.immutable());
                    }
                }
                fixLeafDistances(world, leaves);
            }
            return generated;
        }
    }
}
