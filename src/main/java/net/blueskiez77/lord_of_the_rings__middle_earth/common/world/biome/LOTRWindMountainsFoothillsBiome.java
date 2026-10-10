package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRChunkTerrain;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRBiomeGenWindMountainsFoothills.
 */
public class LOTRWindMountainsFoothillsBiome extends LOTRWindMountainsBiome {

    public LOTRWindMountainsFoothillsBiome(int i, boolean major) {
        super(i, major);
        biomeTerrain.resetXZScale();
        biomeTerrain.resetHeightStretchFactor();
        decorator.biomeGemFactor = 0.75f;
    }

    @Override
    public void generateMountainTerrain(RandomSource random, LOTRChunkTerrain terrain, int i, int k, int xzIndex, int ySize,
                                        int height, int rockDepth, LOTRBiomeVariant variant,
                                        BlockState topBlock, BlockState fillerBlock) {
    }
}
