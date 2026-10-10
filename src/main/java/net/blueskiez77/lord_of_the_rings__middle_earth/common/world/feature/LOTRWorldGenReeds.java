package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LOTRWorldGenReeds extends LOTRFeature {
    public LegacyBlock reedBlock;

    public LOTRWorldGenReeds(LegacyBlock block) {
        reedBlock = block;
    }

    @Override
    protected boolean generateFeature(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        block0:
        for (int l = 0; l < 16; ++l) {
            int i1 = i + random.nextInt(8) - random.nextInt(8);
            int j1 = j + random.nextInt(4) - random.nextInt(4);
            int k1 = k + random.nextInt(8) - random.nextInt(8);
            int maxDepth = 5;
            for (int j2 = j1 - 1; j2 > 0 && isWater(getBlock(world, i1, j2, k1)); --j2) {
                if (j2 < j1 - maxDepth) {
                    continue block0;
                }
            }
            if (LOTRWorldGenUtil.isBlockFreezable(world, new net.minecraft.core.BlockPos(i1, j1 - 1, k1))) {
                continue;
            }
            int reedHeight = 1 + random.nextInt(3);
            for (int j2 = j1; j2 < j1 + reedHeight; ++j2) {
                if (!isAirBlock(world, i1, j2, k1) || !canBlockStay(reedBlock, world, i1, j2, k1)) {
                    continue;
                }
                setBlock(world, i1, j2, k1, reedBlock, 0, 2);
            }
        }
        return true;
    }
}
