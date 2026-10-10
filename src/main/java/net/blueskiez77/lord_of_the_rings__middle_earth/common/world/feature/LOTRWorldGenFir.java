package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LOTRWorldGenFir extends LOTRFeature {
    public LegacyBlock woodBlock = LOTRLegacyBlocks.mod("wood4");
    public int woodMeta = 3;
    public LegacyBlock leafBlock = LOTRLegacyBlocks.mod("leaves4");
    public int leafMeta = 3;
    public int minHeight = 6;
    public int maxHeight = 13;

    public LOTRWorldGenFir(boolean flag) {
        super(flag);
    }

    @Override
    protected boolean generateFeature(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        BlockState below;
        int height = LOTRWorldGenUtil.getRandomIntegerInRange(random, minHeight, maxHeight);
        boolean flag = true;
        if (j >= 1 && height + 2 <= 256) {
            for (int j1 = j; j1 <= j + height + 2; ++j1) {
                int range = 1;
                if (j1 == j) {
                    range = 0;
                }
                if (j1 >= j + height - 1) {
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
        } else {
            flag = false;
        }
        if (!((below = getBlock(world, i, j - 1, k)) != null && canSustainPlant(world, i, j - 1, k))) {
            flag = false;
        }
        if (!flag) {
            return false;
        }
        onPlantGrow(world, i, j - 1, k);
        int leafLevel = j + height + 2;
        int leafLayers = 3;
        for (int l = 0; l <= leafLayers * 2; ++l) {
            int leafRange = l / 2;
            for (int i1 = i - leafRange; i1 <= i + leafRange; ++i1) {
                for (int k1 = k - leafRange; k1 <= k + leafRange; ++k1) {
                    BlockState block = getBlock(world, i1, leafLevel, k1);
                    int i2 = Math.abs(i1 - i);
                    if (i2 + Math.abs(k1 - k) > leafRange || !isBlockReplaceable(block) && !isLeaves(block)) {
                        continue;
                    }
                    setBlockAndNotifyAdequately(world, i1, leafLevel, k1, leafBlock, leafMeta);
                }
            }
            --leafLevel;
        }
        for (int j1 = 0; j1 < height; ++j1) {
            setBlockAndNotifyAdequately(world, i, j + j1, k, woodBlock, woodMeta);
        }
        return true;
    }

    public LOTRWorldGenFir setMinMaxHeight(int min, int max) {
        minHeight = min;
        maxHeight = max;
        return this;
    }
}
