package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LOTRWorldGenLarch extends LOTRFeature {
    public LOTRWorldGenLarch(boolean flag) {
        super(flag);
    }

    @Override
    protected boolean generateFeature(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        int height = random.nextInt(9) + 8;
        int trunkBaseHeight = 2 + random.nextInt(2);
        int leafStart = height - trunkBaseHeight;
        int leafWidth = 2 + random.nextInt(2);
        boolean flag = true;
        if (j >= 1 && j + height + 1 <= 256) {
            for (int j1 = j; j1 <= j + 1 + height && flag; ++j1) {
                int range;
                range = j1 - j < trunkBaseHeight ? 0 : leafWidth;
                for (int i1 = i - range; i1 <= i + range && flag; ++i1) {
                    for (int k1 = k - range; k1 <= k + range && flag; ++k1) {
                        if (j1 >= 0 && j1 < 256) {
                            BlockState block = getBlock(world, i1, j1, k1);
                            if (block.isAir() || isLeaves(block)) {
                                continue;
                            }
                        }
                        flag = false;
                    }
                }
            }
            if (!flag) {
                return false;
            }
            BlockState soil = getBlock(world, i, j - 1, k);
            boolean isSoil = canSustainPlant(world, i, j - 1, k);
            if (isSoil && j < 256 - height - 1) {
                int j1;
                onPlantGrow(world, i, j - 1, k);
                int leafRange = random.nextInt(2);
                int maxLeafRange = 1;
                int minLeafRange = 0;
                for (int leafLayer = 0; leafLayer <= leafStart; ++leafLayer) {
                    j1 = j + height - leafLayer;
                    for (int i1 = i - leafRange; i1 <= i + leafRange; ++i1) {
                        int i2 = i1 - i;
                        for (int k1 = k - leafRange; k1 <= k + leafRange; ++k1) {
                            int k2 = k1 - k;
                            if (Math.abs(i2) == leafRange && Math.abs(k2) == leafRange && leafRange > 0 || !canBeReplacedByLeaves(getBlock(world, i1, j1, k1))) {
                                continue;
                            }
                            setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRLegacyBlocks.mod("leaves3"), 1);
                        }
                    }
                    if (leafRange >= maxLeafRange) {
                        leafRange = minLeafRange;
                        minLeafRange = 1;
                        maxLeafRange++;
                        if (maxLeafRange <= leafWidth) {
                            continue;
                        }
                        maxLeafRange = leafWidth;
                        continue;
                    }
                    ++leafRange;
                }
                int trunkTop = random.nextInt(3);
                for (j1 = 0; j1 < height - trunkTop; ++j1) {
                    BlockState block2 = getBlock(world, i, j + j1, k);
                    if (!block2.isAir() && !isLeaves(block2)) {
                        continue;
                    }
                    setBlockAndNotifyAdequately(world, i, j + j1, k, LOTRLegacyBlocks.mod("wood3"), 1);
                }
                return true;
            }
        }
        return false;
    }
}
