package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.orc;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRNurnWheatFarmStructure extends LOTRNurnFarmBaseStructure {
    public LOTRNurnWheatFarmStructure(boolean flag) {
        super(flag);
    }

    @Override
    public void generateCrops(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        for (int i1 = i - 4; i1 <= i + 4; ++i1) {
            for (int k1 = k - 4; k1 <= k + 4; ++k1) {
                if (Math.abs(i1 - i) == 4 && Math.abs(k1 - k) == 4) {
                    setBlockAndNotifyAdequately(world, i1, j + 1, k1, LOTRLegacyBlocks.mod("brick"), 0);
                    setBlockAndNotifyAdequately(world, i1, j + 2, k1, LOTRLegacyBlocks.mod("brick"), 0);
                    setBlockAndNotifyAdequately(world, i1, j + 3, k1, LOTRLegacyBlocks.mod("fence"), 3);
                    setBlockAndNotifyAdequately(world, i1, j + 4, k1, LOTRLegacyBlocks.vanilla("wool"), 12);
                    placeSkull(world, random, i1, j + 5, k1);
                    continue;
                }
                if (Math.abs(i1 - i) <= 1 && Math.abs(k1 - k) <= 1) {
                    setBlockAndNotifyAdequately(world, i1, j + 1, k1, LOTRLegacyBlocks.mod("brick"), 0);
                    if (Math.abs(i1 - i) == 0 || Math.abs(k1 - k) == 0) {
                        continue;
                    }
                    placeOrcTorch(world, i1, j + 2, k1);
                    continue;
                }
                if (i1 == i || k1 == k) {
                    if (Math.abs(i1 - i) > 3 || Math.abs(k1 - k) > 3) {
                        continue;
                    }
                    setBlockAndNotifyAdequately(world, i1, j, k1, LOTRLegacyBlocks.vanilla("water"), 0);
                    continue;
                }
                setBlockAndNotifyAdequately(world, i1, j, k1, LOTRLegacyBlocks.vanilla("farmland"), 7);
                setBlockAndNotifyAdequately(world, i1, j + 1, k1, LOTRLegacyBlocks.vanilla("wheat"), 7);
            }
        }
        setBlockAndNotifyAdequately(world, i, j + 1, k, LOTRLegacyBlocks.mod("morgulTable"), 0);
    }
}
