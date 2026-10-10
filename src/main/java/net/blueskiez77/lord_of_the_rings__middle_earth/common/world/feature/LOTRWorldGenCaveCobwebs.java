package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LOTRWorldGenCaveCobwebs extends LOTRFeature {
    @Override
    protected boolean generateFeature(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        for (int l = 0; l < 64; ++l) {
            int j1;
            int k1;
            int i1 = i - random.nextInt(6) + random.nextInt(6);
            if (!isAirBlock(world, i1, j1 = j - random.nextInt(4) + random.nextInt(4), k1 = k - random.nextInt(6) + random.nextInt(6))) {
                continue;
            }
            boolean flag = isStoneBlock(world, i1 - 1, j1, k1);
            if (isStoneBlock(world, i1 + 1, j1, k1)) {
                flag = true;
            }
            if (isStoneBlock(world, i1, j1 - 1, k1)) {
                flag = true;
            }
            if (isStoneBlock(world, i1, j1 + 1, k1)) {
                flag = true;
            }
            if (isStoneBlock(world, i1, j1, k1 - 1)) {
                flag = true;
            }
            if (isStoneBlock(world, i1, j1, k1 + 1)) {
                flag = true;
            }
            if (!flag) {
                continue;
            }
            setBlock(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("web"), 0, 2);
        }
        return true;
    }

    public boolean isStoneBlock(WorldGenLevel world, int i, int j, int k) {
        BlockState block = getBlock(world, i, j, k);
        return LOTRLegacyBlocks.vanilla("stone").matches(block) || LOTRLegacyBlocks.mod("rock").matches(block);
    }
}
