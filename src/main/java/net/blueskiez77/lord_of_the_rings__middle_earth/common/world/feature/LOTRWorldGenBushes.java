package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

/** LOTRWorldGenBushes: a low bush of the leaves of a tree nearby, one block or three across. */
public class LOTRWorldGenBushes extends LOTRFeature {

    @Override
    protected boolean generateFeature(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        BlockState leafBlock = null;
        for (int l = 0; l < 40; ++l) {
            int i1 = i - random.nextInt(6) + random.nextInt(6);
            int j1 = j + random.nextInt(12);
            int k1 = k - random.nextInt(6) + random.nextInt(6);
            BlockState block = getBlock(world, i1, j1, k1);
            if (block.getBlock() instanceof net.minecraft.world.level.block.LeavesBlock) {
                leafBlock = block.setValue(net.minecraft.world.level.block.LeavesBlock.PERSISTENT, true);
                break;
            }
        }
        if (leafBlock == null) {
            return false;
        }
        if (canSustainPlant(world, i, j - 1, k)) {
            int size = 0;
            if (random.nextInt(3) == 0) {
                ++size;
            }
            for (int i1 = -size; i1 <= size; ++i1) {
                for (int k1 = -size; k1 <= size; ++k1) {
                    int i2 = i + i1;
                    int k2 = k + k1;
                    if (size != 0 && Math.abs(i1) == size && Math.abs(k1) == size && random.nextInt(3) != 0) {
                        continue;
                    }
                    BlockState block = getBlock(world, i2, j, k2);
                    if (isLiquid(block) || !isBlockReplaceable(block)) {
                        continue;
                    }
                    setBlock(world, i2, j, k2, leafBlock, 2);
                }
            }
        }
        return true;
    }
}
