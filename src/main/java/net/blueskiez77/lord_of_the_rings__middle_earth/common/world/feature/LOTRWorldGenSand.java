package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LOTRWorldGenSand extends LOTRFeature {
    public LegacyBlock sandBlock;
    public int radius;
    public int heightRadius;

    public LOTRWorldGenSand(LegacyBlock b, int r, int hr) {
        sandBlock = b;
        radius = r;
        heightRadius = hr;
    }

    @Override
    protected boolean generateFeature(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        boolean mid = false;
        boolean adj = false;
        int waterCheck = 1;
        for (int i1 = -waterCheck; i1 <= waterCheck; ++i1) {
            for (int k1 = -waterCheck; k1 <= waterCheck; ++k1) {
                int i2 = i + i1;
                int k2 = k + k1;
                if (!isWater(getBlock(world, i2, j, k2))) {
                    continue;
                }
                if (i1 == 0 && k1 == 0) {
                    mid = true;
                    continue;
                }
                adj = true;
            }
        }
        if (!mid || !adj) {
            return false;
        }
        LOTRBiome biome = getBiome(world, i, k);
        int r = random.nextInt(radius - 2) + 2;
        int hr = heightRadius;
        for (int i1 = i - r; i1 <= i + r; ++i1) {
            for (int k1 = k - r; k1 <= k + r; ++k1) {
                int i2 = i1 - i;
                int k2 = k1 - k;
                if (i2 * i2 + k2 * k2 > r * r) {
                    continue;
                }
                for (int j1 = j - hr; j1 <= j + hr; ++j1) {
                    BlockState block = getBlock(world, i1, j1, k1);
                    if (block != biome.topBlock && block != biome.fillerBlock || random.nextInt(3) == 0) {
                        continue;
                    }
                    setBlock(world, i1, j1, k1, sandBlock, 0, 2);
                }
            }
        }
        return true;
    }
}
