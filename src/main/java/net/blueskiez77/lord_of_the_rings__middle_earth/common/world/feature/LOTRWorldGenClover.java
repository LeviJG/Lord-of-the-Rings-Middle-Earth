package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LOTRWorldGenClover extends LOTRFeature {
    @Override
    protected boolean generateFeature(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        BlockState block;
        //noinspection StatementWithEmptyBody
        while ((((block = getBlock(world, i, j, k))).isAir() || isLeaves(block)) && --j > 0) {
        }
        for (int l = 0; l < 128; ++l) {
            int j1;
            int k1;
            int i1 = i + random.nextInt(8) - random.nextInt(8);
            if (!isAirBlock(world, i1, j1 = j + random.nextInt(4) - random.nextInt(4), k1 = k + random.nextInt(8) - random.nextInt(8)) || !canBlockStay(LOTRLegacyBlocks.mod("clover"), world, i1, j1, k1)) {
                continue;
            }
            if (random.nextInt(500) == 0) {
                setBlock(world, i1, j1, k1, LOTRLegacyBlocks.mod("clover"), 1, 2);
                continue;
            }
            setBlock(world, i1, j1, k1, LOTRLegacyBlocks.mod("clover"), 0, 2);
        }
        return true;
    }
}
