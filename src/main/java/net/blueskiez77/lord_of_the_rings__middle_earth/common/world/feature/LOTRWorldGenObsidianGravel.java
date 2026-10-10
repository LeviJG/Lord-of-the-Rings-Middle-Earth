package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LOTRWorldGenObsidianGravel extends LOTRFeature {
    public LegacyBlock genBlock = LOTRLegacyBlocks.mod("obsidianGravel");
    public int genMeta;

    public boolean canReplace(WorldGenLevel world, int i, int j, int k) {
        BlockState block = getBlock(world, i, j, k);
        LOTRBiome biome = getBiome(world, i, k);
        return block == biome.topBlock || block == biome.fillerBlock || LOTRLegacyBlocks.vanilla("stone").matches(block) || isBlockReplaceable(block);
    }

    @Override
    protected boolean generateFeature(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        LOTRBiome biome = getBiome(world, i, k);
        BlockState below = getBlock(world, i, j - 1, k);
        if (below != biome.topBlock && below != biome.fillerBlock && !LOTRLegacyBlocks.vanilla("stone").matches(below)) {
            return false;
        }
        int numBlocks = LOTRWorldGenUtil.getRandomIntegerInRange(random, 6, 16);
        float angle = random.nextFloat() * 3.1415927f;
        float sin = Mth.sin(angle);
        float cos = Mth.sin(angle);
        float div = 8.0f;
        double xMin = i - sin * numBlocks / div;
        double xMax = i + sin * numBlocks / div;
        double zMin = k - cos * numBlocks / div;
        double zMax = k + cos * numBlocks / div;
        double yMin = j + random.nextInt(3) - 2;
        double yMax = j + random.nextInt(3) - 2;
        for (int l = 0; l <= numBlocks; ++l) {
            float lerp = (float) l / numBlocks;
            double xLerp = xMin + (xMax - xMin) * lerp;
            double yLerp = yMin + (yMax - yMin) * lerp;
            double zLerp = zMin + (zMax - zMin) * lerp;
            double d9 = random.nextDouble() * numBlocks / 16.0;
            double d10 = (Mth.sin(l * 3.1415927f / numBlocks) + 1.0f) * d9 + 1.0;
            double d11 = (Mth.sin(l * 3.1415927f / numBlocks) + 1.0f) * d9 + 1.0;
            int i1 = Mth.floor(xLerp - d10 / 2.0);
            int j1 = Mth.floor(yLerp - d11 / 2.0);
            int k1 = Mth.floor(zLerp - d10 / 2.0);
            int l1 = Mth.floor(xLerp + d10 / 2.0);
            int i2 = Mth.floor(yLerp + d11 / 2.0);
            int j2 = Mth.floor(zLerp + d10 / 2.0);
            for (int k2 = i1; k2 <= l1; ++k2) {
                double d12 = (k2 + 0.5 - xLerp) / (d10 / 2.0);
                if (d12 * d12 >= 1.0) {
                    continue;
                }
                for (int l2 = j1; l2 <= i2; ++l2) {
                    double d13 = (l2 + 0.5 - yLerp) / (d11 / 2.0);
                    if (d12 * d12 + d13 * d13 >= 1.0) {
                        continue;
                    }
                    for (int i3 = k1; i3 <= j2; ++i3) {
                        double d14 = (i3 + 0.5 - zLerp) / (d10 / 2.0);
                        if (d12 * d12 + d13 * d13 + d14 * d14 >= 1.0 || !canReplace(world, k2, l2, i3)) {
                            continue;
                        }
                        setBlock(world, k2, l2, i3, genBlock, genMeta, 2);
                    }
                }
            }
        }
        return true;
    }
}
