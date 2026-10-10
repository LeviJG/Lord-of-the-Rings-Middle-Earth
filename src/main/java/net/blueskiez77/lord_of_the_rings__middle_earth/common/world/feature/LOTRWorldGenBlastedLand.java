package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LOTRWorldGenBlastedLand extends LOTRFeature {
    public boolean aflame;

    public LOTRWorldGenBlastedLand(boolean flag) {
        aflame = flag;
    }

    @Override
    protected boolean generateFeature(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        BlockState block = getBlock(world, i, j - 1, k);
        if (!LOTRLegacyBlocks.vanilla("grass").matches(block) && !LOTRLegacyBlocks.vanilla("dirt").matches(block) && !LOTRLegacyBlocks.vanilla("stone").matches(block)) {
            return false;
        }
        int radius = 5 + random.nextInt(8);
        for (int i1 = i - radius; i1 <= i + radius; ++i1) {
            for (int j1 = j - radius / 2; j1 <= j + radius / 2; ++j1) {
                for (int k1 = k - radius; k1 <= k + radius; ++k1) {
                    BlockState block2 = getBlock(world, i1, j1, k1);
                    if (!LOTRLegacyBlocks.vanilla("grass").matches(block2) && !LOTRLegacyBlocks.vanilla("dirt").matches(block2) && !LOTRLegacyBlocks.vanilla("stone").matches(block2)) {
                        continue;
                    }
                    int i2 = i1 - i;
                    int j2 = j1 - j;
                    int k2 = k1 - k;
                    double d = Math.sqrt(i2 * i2 + j2 * j2 + k2 * k2);
                    int chance = Mth.floor(d / 2.0);
                    if (chance < 1) {
                        chance = 1;
                    }
                    if (random.nextInt(chance) == 0) {
                        setBlock(world, i1, j1, k1, LOTRLegacyBlocks.mod("wasteBlock"), 0, 2);
                    }
                    if (!aflame || d >= radius / 2.0 || random.nextInt(10) != 0 || isOpaqueCube(getBlock(world, i1, j1 + 1, k1))) {
                        continue;
                    }
                    setBlock(world, i1, j1, k1, LOTRLegacyBlocks.mod("wasteBlock"), 0, 2);
                    setBlock(world, i1, j1 + 1, k1, LOTRLegacyBlocks.vanilla("fire"), 0, 2);
                }
            }
        }
        return true;
    }
}
