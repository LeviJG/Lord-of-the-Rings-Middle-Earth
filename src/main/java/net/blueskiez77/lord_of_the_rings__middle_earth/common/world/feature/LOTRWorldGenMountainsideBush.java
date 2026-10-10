package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LOTRWorldGenMountainsideBush extends LOTRFeature {
    public LegacyBlock leafBlock;
    public int leafMeta;

    public LOTRWorldGenMountainsideBush(LegacyBlock block, int meta) {
        leafBlock = block;
        leafMeta = meta;
    }

    @Override
    protected boolean generateFeature(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        for (int l = 0; l < 64; ++l) {
            int j1;
            int k1;
            int i1 = i + LOTRWorldGenUtil.getRandomIntegerInRange(random, -2, 2);
            if (!isAirBlock(world, i1, j1 = j + LOTRWorldGenUtil.getRandomIntegerInRange(random, -2, 2), k1 = k + LOTRWorldGenUtil.getRandomIntegerInRange(random, -2, 2)) || !isStone(world, i1 - 1, j1, k1) && !isStone(world, i1 + 1, j1, k1) && !isStone(world, i1, j1, k1 - 1) && !isStone(world, i1, j1, k1 + 1)) {
                continue;
            }
            setBlock(world, i1, j1, k1, leafBlock, leafMeta | 4, 2);
        }
        return true;
    }

    public boolean isStone(WorldGenLevel world, int i, int j, int k) {
        return isRock(getBlock(world, i, j, k));
    }
}
