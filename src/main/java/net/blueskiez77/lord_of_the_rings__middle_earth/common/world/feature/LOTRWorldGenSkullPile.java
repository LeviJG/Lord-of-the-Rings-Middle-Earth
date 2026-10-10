package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LOTRWorldGenSkullPile extends LOTRFeature {
    public LOTRWorldGenSkullPile() {
        super(false);
    }

    @Override
    protected boolean generateFeature(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        for (int l = 0; l < 4; ++l) {
            int j1;
            int k1;
            int i1 = i - 4 + random.nextInt(9);
            if (!isOpaqueCube(getBlock(world, i1, (j1 = getHeightValue(world, i1, k1 = k - 4 + random.nextInt(9))) - 1, k1)) || !isBlockReplaceable(getBlock(world, i1, j1, k1))) {
                continue;
            }
            // A skeleton's skull on the floor, turned one of the sixteen ways (func_145903_a).
            setBlock(world, i1, j1, k1, net.minecraft.world.level.block.Blocks.SKELETON_SKULL.defaultBlockState()
                    .setValue(net.minecraft.world.level.block.SkullBlock.ROTATION, random.nextInt(16)), 2);
        }
        return true;
    }
}
