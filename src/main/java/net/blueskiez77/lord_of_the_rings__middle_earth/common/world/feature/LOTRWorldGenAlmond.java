package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LOTRWorldGenAlmond extends LOTRFeature {
    public int minHeight = 4;
    public int maxHeight = 5;
    public LegacyBlock woodBlock = LOTRLegacyBlocks.mod("wood7");
    public int woodMeta = 3;
    public LegacyBlock leafBlock = LOTRLegacyBlocks.mod("leaves7");
    public int leafMeta = 3;

    public LOTRWorldGenAlmond(boolean flag) {
        super(flag);
    }

    @Override
    protected boolean generateFeature(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        int height = LOTRWorldGenUtil.getRandomIntegerInRange(random, minHeight, maxHeight);
        int leafStart = j + height - 3;
        int leafTop = j + height;
        boolean flag = true;
        if (j >= 1 && j + height + 1 <= 256) {
            for (int j1 = j; j1 <= j + height + 1; ++j1) {
                int range = 1;
                if (j1 == j) {
                    range = 0;
                }
                if (j1 >= leafStart) {
                    range = 2;
                }
                for (int i1 = i - range; i1 <= i + range && flag; ++i1) {
                    for (int k1 = k - range; k1 <= k + range && flag; ++k1) {
                        if (j1 >= 0 && j1 < 256 && isReplaceable(world, i1, j1, k1)) {
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
            BlockState below = getBlock(world, i, j - 1, k);
            if (!canSustainPlant(world, i, j - 1, k)) {
                canGrow = false;
            }
            if (canGrow) {
                int j1;
                below = getBlock(world, i, j - 1, k);
                onPlantGrow(world, i, j - 1, k);
                for (j1 = leafStart; j1 <= leafTop; ++j1) {
                    int leafRange;
                    int maxRange = 2;
                    int j2 = leafTop - j1;
                    leafRange = j2 == 0 ? 1 : j2 == 1 ? 2 : j2 == 2 ? 3 : 1;
                    for (int i1 = i - maxRange; i1 <= i + maxRange; ++i1) {
                        for (int k1 = k - maxRange; k1 <= k + maxRange; ++k1) {
                            BlockState block;
                            int i2 = Math.abs(i1 - i);
                            if (i2 + Math.abs(k1 - k) > leafRange || !isBlockReplaceable((block = getBlock(world, i1, j1, k1))) && !isLeaves(block)) {
                                continue;
                            }
                            setBlockAndNotifyAdequately(world, i1, j1, k1, leafBlock, leafMeta);
                        }
                    }
                }
                for (j1 = j; j1 < j + height; ++j1) {
                    BlockState block = getBlock(world, i, j1, k);
                    if (!isBlockReplaceable(block) && !isLeaves(block)) {
                        continue;
                    }
                    setBlockAndNotifyAdequately(world, i, j1, k, woodBlock, woodMeta);
                }
                return true;
            }
        }
        return false;
    }
}
