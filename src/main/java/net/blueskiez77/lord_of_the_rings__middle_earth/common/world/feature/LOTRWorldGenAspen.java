package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LOTRWorldGenAspen extends LOTRFeature {
    public LegacyBlock woodBlock = LOTRLegacyBlocks.mod("wood7");
    public int woodMeta;
    public LegacyBlock leafBlock = LOTRLegacyBlocks.mod("leaves7");
    public int leafMeta;
    public int minHeight = 8;
    public int maxHeight = 15;
    public int extraTrunk;

    public LOTRWorldGenAspen(boolean flag) {
        super(flag);
    }

    @Override
    protected boolean generateFeature(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        int j1;
        int i1;
        int k1;
        int i12;
        BlockState below;
        int height = LOTRWorldGenUtil.getRandomIntegerInRange(random, minHeight, maxHeight);
        int leafMin = 3 + random.nextInt(3);
        leafMin = j + leafMin - 1;
        int leafTop = j + height + 1;
        boolean flag = true;
        if (j >= 1 && height + 1 <= 256) {
            for (int j12 = j; j12 <= j + height + 1; ++j12) {
                int range = 1;
                if (j12 == j) {
                    range = 0;
                }
                if (j12 >= leafMin) {
                    range = 2;
                }
                for (i1 = i - range; i1 <= i + extraTrunk + range && flag; ++i1) {
                    for (int k12 = k - range; k12 <= k + extraTrunk + range && flag; ++k12) {
                        if (j12 >= 0 && j12 < 256 && isReplaceable(world, i1, j12, k12)) {
                            continue;
                        }
                        flag = false;
                    }
                }
            }
        } else {
            flag = false;
        }
        if (!flag) {
            return false;
        }
        boolean canGrow = true;
        for (i12 = i; i12 <= i + extraTrunk && canGrow; ++i12) {
            for (k1 = k; k1 <= k + extraTrunk && canGrow; ++k1) {
                below = getBlock(world, i12, j - 1, k1);
                if (canSustainPlant(world, i12, j - 1, k1)) {
                    continue;
                }
                canGrow = false;
            }
        }
        if (!canGrow) {
            return false;
        }
        for (i12 = i; i12 <= i + extraTrunk; ++i12) {
            for (k1 = k; k1 <= k + extraTrunk; ++k1) {
                below = getBlock(world, i12, j - 1, k1);
                onPlantGrow(world, i12, j - 1, k1);
            }
        }
        for (j1 = leafMin; j1 <= leafTop; ++j1) {
            int leafWidth = 2;
            if (j1 >= leafTop - 1) {
                leafWidth = 0;
            } else if (j1 >= leafTop - 3 || j1 <= leafMin + 1 || random.nextInt(4) == 0) {
                leafWidth = 1;
            }
            int branches = 4 + random.nextInt(5);
            for (int b = 0; b < branches; ++b) {
                BlockState block;
                int i13 = i;
                int k13 = k;
                if (extraTrunk > 0) {
                    i13 += random.nextInt(extraTrunk + 1);
                    k13 += random.nextInt(extraTrunk + 1);
                }
                int i2 = i13;
                int k2 = k13;
                int length = 4 + random.nextInt(8);
                for (int l = 0; l < (length *= extraTrunk + 1) && Math.abs(i2 - i13) <= leafWidth && Math.abs(k2 - k13) <= leafWidth && (isBlockReplaceable((block = getBlock(world, i2, j1, k2))) || isLeaves(block)); ++l) {
                    setBlockAndNotifyAdequately(world, i2, j1, k2, leafBlock, leafMeta);
                    int dir = random.nextInt(4);
                    switch (dir) {
                        case 0:
                            --i2;
                            continue;
                        case 1:
                            ++i2;
                            continue;
                        case 2:
                            --k2;
                            continue;
                        default:
                            break;
                    }
                    ++k2;
                }
            }
        }
        for (j1 = j; j1 < j + height; ++j1) {
            for (i1 = i; i1 <= i + extraTrunk; ++i1) {
                for (int k14 = k; k14 <= k + extraTrunk; ++k14) {
                    setBlockAndNotifyAdequately(world, i1, j1, k14, woodBlock, woodMeta);
                }
            }
        }
        return true;
    }

    public LOTRWorldGenAspen setBlocks(LegacyBlock b1, int m1, LegacyBlock b2, int m2) {
        woodBlock = b1;
        woodMeta = m1;
        leafBlock = b2;
        leafMeta = m2;
        return this;
    }

    public LOTRWorldGenAspen setExtraTrunkWidth(int w) {
        extraTrunk = w;
        return this;
    }

    public LOTRWorldGenAspen setMinMaxHeight(int min, int max) {
        minHeight = min;
        maxHeight = max;
        return this;
    }
}
