package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LOTRWorldGenCharredTrees extends LOTRFeature {
    public LOTRWorldGenCharredTrees() {
        super(false);
    }

    @Override
    protected boolean generateFeature(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        BlockState below = getBlock(world, i, j - 1, k);
        if (!net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRMordorBiome.isSurfaceMordorBlock(world, i, j - 1, k) && !LOTRLegacyBlocks.vanilla("stone").matches(below) && !LOTRLegacyBlocks.vanilla("sand").matches(below) && !LOTRLegacyBlocks.vanilla("gravel").matches(below) && !canSustainPlant(world, i, j - 1, k)) {
            return false;
        }
        onPlantGrow(world, i, j - 1, k);
        int height = 2 + random.nextInt(5);
        for (int j1 = j; j1 < j + height; ++j1) {
            setBlock(world, i, j1, k, LOTRLegacyBlocks.mod("wood"), 3, 2);
        }
        if (height >= 4) {
            for (int branch = 0; branch < 4; ++branch) {
                int branchLength = 2 + random.nextInt(4);
                int branchHorizontalPos = 0;
                int branchVerticalPos = j + height - random.nextInt(2);
                for (int l = 0; l < branchLength; ++l) {
                    if (random.nextInt(4) == 0) {
                        ++branchHorizontalPos;
                    }
                    if (random.nextInt(3) != 0) {
                        ++branchVerticalPos;
                    }
                    switch (branch) {
                        case 0: {
                            setBlock(world, i - branchHorizontalPos, branchVerticalPos, k, LOTRLegacyBlocks.mod("wood"), 15, 2);
                            continue;
                        }
                        case 1: {
                            setBlock(world, i, branchVerticalPos, k + branchHorizontalPos, LOTRLegacyBlocks.mod("wood"), 15, 2);
                            continue;
                        }
                        case 2: {
                            setBlock(world, i + branchHorizontalPos, branchVerticalPos, k, LOTRLegacyBlocks.mod("wood"), 15, 2);
                            continue;
                        }
                        case 3: {
                            setBlock(world, i, branchVerticalPos, k - branchHorizontalPos, LOTRLegacyBlocks.mod("wood"), 15, 2);
                        }
                    }
                }
            }
        }
        return true;
    }
}
