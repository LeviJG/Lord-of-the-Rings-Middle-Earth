package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

/** LOTRWorldGenFallenLeaves: the fallen leaves of a tree nearby, scattered on the ground and water under it. */
public class LOTRWorldGenFallenLeaves extends LOTRFeature {

    /** LOTRBlockFallenLeaves.fallenBlockMetaFromLeafBlockMeta: the fallen leaves of these leaves, if they have any. */
    public static @org.jspecify.annotations.Nullable BlockState fallenLeavesFor(BlockState leaves) {
        for (net.minecraft.world.level.block.Block block : net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks.ALL_FALLEN_LEAVES) {
            if (block instanceof net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRFallenLeavesBlock fallen
                    && fallen.leaves() == leaves.getBlock()) {
                return block.defaultBlockState();
            }
        }
        return null;
    }

    @Override
    protected boolean generateFeature(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        BlockState fallenLeaf = null;
        for (int l = 0; l < 40; ++l) {
            int i1 = i - random.nextInt(6) + random.nextInt(6);
            int j1 = j + random.nextInt(12);
            int k1 = k - random.nextInt(6) + random.nextInt(6);
            BlockState block = getBlock(world, i1, j1, k1);
            if (!isLeaves(block) || (fallenLeaf = fallenLeavesFor(block)) == null) {
                continue;
            }
            break;
        }
        if (fallenLeaf == null) {
            return false;
        }
        for (int l = 0; l < 64; ++l) {
            int i1 = i - random.nextInt(5) + random.nextInt(5);
            int j1 = j - random.nextInt(3) + random.nextInt(3);
            int k1 = k - random.nextInt(5) + random.nextInt(5);
            BlockState block = getBlock(world, i1, j1, k1);
            if (!canBlockStay(fallenLeaf, world, i1, j1, k1) || isLiquid(block) || !isBlockReplaceable(block)) {
                continue;
            }
            setBlock(world, i1, j1, k1, fallenLeaf, 2);
        }
        return true;
    }
}
