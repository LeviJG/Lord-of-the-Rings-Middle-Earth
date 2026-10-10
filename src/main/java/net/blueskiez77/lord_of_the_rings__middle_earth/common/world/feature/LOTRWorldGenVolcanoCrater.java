package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LOTRWorldGenVolcanoCrater extends LOTRFeature {
    public int minWidth = 5;
    public int maxWidth = 15;
    public int heightCheck = 8;

    @Override
    protected boolean generateFeature(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        getBiome(world, i, k);
        if (!net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2.isSurfaceStatic(world, i, j - 1, k) && getBlock(world, i, j - 1, k) != LOTRLegacyBlocks.vanilla("stone")) {
            return false;
        }
        int craterWidth = LOTRWorldGenUtil.getRandomIntegerInRange(random, minWidth, maxWidth);
        int highestHeight = j;
        int lowestHeight = j;
        for (int i1 = i - craterWidth; i1 <= i + craterWidth; ++i1) {
            for (int k1 = k - craterWidth; k1 <= k + craterWidth; ++k1) {
                int heightValue = getHeightValue(world, i1, k1);
                int j1 = heightValue - 1;
                if (!net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2.isSurfaceStatic(world, i1, j1, k1) && getBlock(world, i1, j1, k1) != LOTRLegacyBlocks.vanilla("stone")) {
                    return false;
                }
                if (heightValue > highestHeight) {
                    highestHeight = heightValue;
                }
                if (heightValue >= lowestHeight) {
                    continue;
                }
                lowestHeight = heightValue;
            }
        }
        if (highestHeight - lowestHeight > heightCheck) {
            return false;
        }
        int spheres = 1;
        for (int l = 0; l < spheres; ++l) {
            int posY = getTopSolidOrLiquidBlock(world, i, k);
            int sphereWidth = LOTRWorldGenUtil.getRandomIntegerInRange(random, minWidth, maxWidth);
            for (int i1 = i - sphereWidth; i1 <= i + sphereWidth; ++i1) {
                for (int k1 = k - sphereWidth; k1 <= k + sphereWidth; ++k1) {
                    int j1;
                    int i2 = i1 - i;
                    int k2 = k1 - k;
                    int xzDistSq = i2 * i2 + k2 * k2;
                    if (xzDistSq >= sphereWidth * sphereWidth && (xzDistSq >= (sphereWidth + 1) * (sphereWidth + 1) || random.nextInt(3) != 0)) {
                        continue;
                    }
                    for (int j2 = getTopSolidOrLiquidBlock(world, i1, k1); j2 > posY; --j2) {
                        setBlockAndNotifyAdequately(world, i1, j2, k1, LOTRLegacyBlocks.vanilla("air"), 0);
                    }
                    int depthHere = (int) ((sphereWidth - Math.sqrt(xzDistSq)) * 0.7) + random.nextInt(2);
                    for (j1 = posY - depthHere - 1; j1 >= posY - (depthHere + heightCheck + 2 + random.nextInt(2)) && !isOpaqueCube(getBlock(world, i1, j1, k1)); --j1) {
                        setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("stone"), 0);
                    }
                    for (j1 = posY; j1 >= posY - depthHere; --j1) {
                        int jDepth = posY - j1;
                        if (jDepth > 6) {
                            setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("lava"), 0);
                            if (!isOpaqueCube(getBlock(world, i1, j1 - 1, k1))) {
                                setBlockAndNotifyAdequately(world, i1, j1 - 1, k1, LOTRLegacyBlocks.vanilla("obsidian"), 0);
                            }
                        } else {
                            setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("air"), 0);
                        }
                        if (jDepth > 4) {
                            setBlockAndNotifyAdequately(world, i1, j1 - 1, k1, LOTRLegacyBlocks.vanilla("obsidian"), 0);
                            continue;
                        }
                        if (jDepth <= 2) {
                            continue;
                        }
                        if (random.nextInt(4) == 0) {
                            setBlockAndNotifyAdequately(world, i1, j1 - 1, k1, LOTRLegacyBlocks.vanilla("gravel"), 0);
                            setBlockAndNotifyAdequately(world, i1, j1 - 2, k1, LOTRLegacyBlocks.vanilla("stone"), 0);
                            continue;
                        }
                        setBlockAndNotifyAdequately(world, i1, j1 - 1, k1, LOTRLegacyBlocks.mod("obsidianGravel"), 0);
                        setBlockAndNotifyAdequately(world, i1, j1 - 2, k1, LOTRLegacyBlocks.vanilla("obsidian"), 0);
                    }
                }
            }
        }
        return true;
    }
}
