package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LOTRWorldGenLairelosse extends LOTRFeature {
    public int minHeight = 5;
    public int maxHeight = 8;
    public int extraTrunk;
    public LegacyBlock woodBlock = LOTRLegacyBlocks.mod("wood7");
    public int woodMeta = 2;
    public LegacyBlock leafBlock = LOTRLegacyBlocks.mod("leaves7");
    public int leafMeta = 2;

    public LOTRWorldGenLairelosse(boolean flag) {
        super(flag);
    }

    @Override
    protected boolean generateFeature(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        int height = LOTRWorldGenUtil.getRandomIntegerInRange(random, minHeight, maxHeight);
        int leafStart = j + 1 + extraTrunk + random.nextInt(3);
        int leafTop = j + height + 1;
        boolean flag = true;
        if (j >= 1 && j + height + 1 <= 256) {
            int k1;
            int i1;
            BlockState below;
            for (int j1 = j; j1 <= j + height + 1; ++j1) {
                int range = 1;
                if (j1 == j) {
                    range = 0;
                }
                if (j1 >= leafStart) {
                    range = 2;
                }
                for (int i12 = i - range; i12 <= i + extraTrunk + range && flag; ++i12) {
                    for (int k12 = k - range; k12 <= k + extraTrunk + range && flag; ++k12) {
                        if (j1 >= 0 && j1 < 256 && isReplaceable(world, i12, j1, k12)) {
                            continue;
                        }
                        flag = false;
                    }
                }
            }
            if (!flag) {
                return false;
            }
            boolean canGrow = true;
            for (i1 = i; i1 <= i + extraTrunk && canGrow; ++i1) {
                for (k1 = k; k1 <= k + extraTrunk && canGrow; ++k1) {
                    below = getBlock(world, i1, j - 1, k1);
                    if (canSustainPlant(world, i1, j - 1, k1)) {
                        continue;
                    }
                    canGrow = false;
                }
            }
            if (canGrow) {
                int k13;
                int j1;
                int i13;
                for (i1 = i; i1 <= i + extraTrunk; ++i1) {
                    for (k1 = k; k1 <= k + extraTrunk; ++k1) {
                        below = getBlock(world, i1, j - 1, k1);
                        onPlantGrow(world, i1, j - 1, k1);
                    }
                }
                int leafRange = 0;
                int maxRange = 2;
                for (j1 = leafTop; j1 >= leafStart; --j1) {
                    if (j1 >= leafTop - 1) {
                        leafRange = 0;
                    } else if (++leafRange > 2) {
                        leafRange = 1;
                    }
                    for (i13 = i - maxRange; i13 <= i + extraTrunk + maxRange; ++i13) {
                        for (k13 = k - maxRange; k13 <= k + extraTrunk + maxRange; ++k13) {
                            BlockState block;
                            int i2 = Math.abs(i13 - i);
                            int k2 = Math.abs(k13 - k);
                            if (i13 > i) {
                                i2 -= extraTrunk;
                            }
                            if (k13 > k) {
                                k2 -= extraTrunk;
                            }
                            if (i2 + k2 > leafRange || !isBlockReplaceable((block = getBlock(world, i13, j1, k13))) && !isLeaves(block)) {
                                continue;
                            }
                            setBlockAndNotifyAdequately(world, i13, j1, k13, leafBlock, leafMeta);
                        }
                    }
                }
                for (j1 = j; j1 < j + height; ++j1) {
                    for (i13 = i; i13 <= i + extraTrunk; ++i13) {
                        for (k13 = k; k13 <= k + extraTrunk; ++k13) {
                            setBlockAndNotifyAdequately(world, i13, j1, k13, woodBlock, woodMeta);
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }

    public LOTRWorldGenLairelosse setExtraTrunkWidth(int i) {
        extraTrunk = i;
        return this;
    }

    public LOTRWorldGenLairelosse setMinMaxHeight(int min, int max) {
        minHeight = min;
        maxHeight = max;
        return this;
    }
}
