package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRChunkTerrain;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRBiomeGenForodwaithMountains.
 */
public class LOTRForodwaithMountainsBiome extends LOTRForodwaithBiome {

    public LOTRForodwaithMountainsBiome(int i, boolean major) {
        super(i, major);
    }

    @Override
    public void generateMountainTerrain(RandomSource random, LOTRChunkTerrain terrain, int i, int k, int xzIndex, int ySize,
                                        int height, int rockDepth, LOTRBiomeVariant variant,
                                        BlockState topBlock, BlockState fillerBlock) {
        BlockState[] blocks = terrain.blocks;
        int snowHeight = 100 - rockDepth;
        int stoneHeight = snowHeight - 30;
        for (int j = ySize - 1; j >= stoneHeight; --j) {
            int index = LOTRChunkTerrain.index(xzIndex, j);
            BlockState block = blocks[index];
            if (j >= snowHeight && block.is(topBlock.getBlock())) {
                blocks[index] = LOTRLegacyBlocks.vanilla("snow").state(0);
                continue;
            }
            if (!block.is(topBlock.getBlock()) && !block.is(fillerBlock.getBlock())) {
                continue;
            }
            blocks[index] = LOTRLegacyBlocks.vanilla("stone").state(0);
        }
    }
}
