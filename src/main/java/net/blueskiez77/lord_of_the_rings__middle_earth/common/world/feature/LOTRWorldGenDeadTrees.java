package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LOTRWorldGenDeadTrees extends LOTRFeature {
    public LegacyBlock woodBlock;
    public int woodMeta;

    public LOTRWorldGenDeadTrees(LegacyBlock block, int i) {
        super(false);
        woodBlock = block;
        woodMeta = i;
    }

    @Override
    protected boolean generateFeature(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        BlockState below = getBlock(world, i, j - 1, k);
        if (!canSustainPlant(world, i, j - 1, k) && !LOTRLegacyBlocks.vanilla("stone").matches(below) && !LOTRLegacyBlocks.vanilla("sand").matches(below) && !LOTRLegacyBlocks.vanilla("gravel").matches(below)) {
            return false;
        }
        onPlantGrow(world, i, j - 1, k);
        int height = 3 + random.nextInt(4);
        for (int j1 = j; j1 < j + height; ++j1) {
            setBlockAndNotifyAdequately(world, i, j1, k, woodBlock, woodMeta);
        }
        for (int branch = 0; branch < 4; ++branch) {
            int branchLength = 3 + random.nextInt(5);
            int branchHorizontalPos = 0;
            int branchVerticalPos = j + height - 1 - random.nextInt(2);
            for (int l = 0; l < branchLength; ++l) {
                if (random.nextInt(4) == 0) {
                    ++branchHorizontalPos;
                }
                if (random.nextInt(3) != 0) {
                    ++branchVerticalPos;
                }
                switch (branch) {
                    case 0: {
                        setBlockAndNotifyAdequately(world, i - branchHorizontalPos, branchVerticalPos, k, woodBlock, woodMeta | 0xC);
                        continue;
                    }
                    case 1: {
                        setBlockAndNotifyAdequately(world, i, branchVerticalPos, k + branchHorizontalPos, woodBlock, woodMeta | 0xC);
                        continue;
                    }
                    case 2: {
                        setBlockAndNotifyAdequately(world, i + branchHorizontalPos, branchVerticalPos, k, woodBlock, woodMeta | 0xC);
                        continue;
                    }
                    case 3: {
                        setBlockAndNotifyAdequately(world, i, branchVerticalPos, k - branchHorizontalPos, woodBlock, woodMeta | 0xC);
                    }
                }
            }
        }
        return true;
    }
}
