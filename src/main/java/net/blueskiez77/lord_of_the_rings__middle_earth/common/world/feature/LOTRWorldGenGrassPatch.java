package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LOTRWorldGenGrassPatch extends LOTRFeature {
    @Override
    protected boolean generateFeature(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        if (getBlock(world, i, j - 1, k) != LOTRLegacyBlocks.vanilla("stone")) {
            return false;
        }
        int radius = 3 + random.nextInt(3);
        int heightValue = getHeightValue(world, i, k);
        for (int i1 = i - radius; i1 <= i + radius; ++i1) {
            for (int k1 = k - radius; k1 <= k + radius; ++k1) {
                BlockState block;
                int i2 = i1 - i;
                int k2 = k1 - k;
                if (i2 * i2 + k2 * k2 >= radius * radius || getHeightValue(world, i1, k1) != heightValue) {
                    continue;
                }
                for (int j1 = heightValue - 1; j1 > heightValue - 5 && ((block = getBlock(world, i1, j1, k1)) == LOTRLegacyBlocks.vanilla("dirt") || LOTRLegacyBlocks.vanilla("stone").matches(block)); --j1) {
                    if (j1 == heightValue - 1) {
                        setBlock(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("grass"), 0, 2);
                        continue;
                    }
                    setBlock(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("dirt"), 0, 2);
                }
            }
        }
        return true;
    }
}
