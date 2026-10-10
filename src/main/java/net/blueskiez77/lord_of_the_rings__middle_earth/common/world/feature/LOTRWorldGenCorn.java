package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LOTRWorldGenCorn extends LOTRFeature {
    @Override
    protected boolean generateFeature(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        for (int l = 0; l < 20; ++l) {
            int j1;
            int k1;
            int i1 = i + random.nextInt(4) - random.nextInt(4);
            BlockState replace = getBlock(world, i1, j1 = j, k1 = k + random.nextInt(4) - random.nextInt(4));
            if (!isBlockReplaceable(replace) || isLiquid(replace)) {
                continue;
            }
            boolean adjWater = false;
            block1:
            for (int i2 = -1; i2 <= 1; ++i2) {
                for (int k2 = -1; k2 <= 1; ++k2) {
                    if (Math.abs(i2) + Math.abs(k2) != 1 || !isWater(getBlock(world, i1 + i2, j - 1, k1 + k2))) {
                        continue;
                    }
                    adjWater = true;
                    break block1;
                }
            }
            if (!adjWater) {
                continue;
            }
            int cornHeight = 2 + random.nextInt(2);
            for (int j2 = 0; j2 < cornHeight; ++j2) {
                if (!canBlockStay(LOTRLegacyBlocks.mod("cornStalk"), world, i1, j1 + j2, k1)) {
                    continue;
                }
                setBlock(world, i1, j1 + j2, k1, LOTRLegacyBlocks.mod("cornStalk"), 0, 2);
            }
        }
        return true;
    }
}
