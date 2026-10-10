package net.blueskiez77.lord_of_the_rings__middle_earth.common.world;

import java.util.Arrays;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import org.jspecify.annotations.Nullable;

/**
 * LOTRChunkProvider's Block[] and metadata arrays as one: a chunk's 16 x 16 x 256 blocks while
 * its terrain is built, at index {@code (x * 16 + z) * 256 + y} as the original's were, before
 * they go into the chunk.
 */
public final class LOTRChunkTerrain {

    public static final int HEIGHT = 256;
    public static final BlockState AIR = Blocks.AIR.defaultBlockState();
    public static final BlockState STONE = Blocks.STONE.defaultBlockState();
    public static final BlockState WATER = Blocks.WATER.defaultBlockState();

    public final BlockState[] blocks = new BlockState[16 * 16 * HEIGHT];
    /** The chunk's biome variants, a column each at {@code x + z * 16}, once its surface is laid. */
    public LOTRBiomeVariant @Nullable [] variants;

    public LOTRChunkTerrain() {
        Arrays.fill(this.blocks, AIR);
    }

    public static int index(int xzIndex, int y) {
        return xzIndex * HEIGHT + y;
    }

    /** Block.isOpaqueCube, for the terrain's solid blocks. */
    public static boolean isOpaque(BlockState state) {
        return state.canOcclude() && !state.isAir() && state.getFluidState().isEmpty();
    }
}
