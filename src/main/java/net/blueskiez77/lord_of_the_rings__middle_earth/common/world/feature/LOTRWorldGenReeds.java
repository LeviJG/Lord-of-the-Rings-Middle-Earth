package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlockTags;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRReedBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;

/**
 * LOTRWorldGenReeds: sixteen tries about the spot at reeds (or dried reeds), each where the
 * original's reed could stand -- in the air just above still water, not frozen over -- and from one
 * to three segments tall above the surface.
 *
 * <p>The original reed sat on the water's surface; the port's stands on the bed and grows up
 * through the water (LOTRReedBlock), so the column is planted on the bed below that spot, through
 * the water and on up -- and only where the reed may be planted: water no deeper than
 * {@link LOTRReedBlock#MAX_WATER_DEPTH} (the original allowed five), over ground it takes.
 */
public class LOTRWorldGenReeds extends LOTRFeature {
    public LegacyBlock reedBlock;

    public LOTRWorldGenReeds(LegacyBlock block) {
        reedBlock = block;
    }

    @Override
    protected boolean generateFeature(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        if (!(reedBlock.state(0).getBlock() instanceof LOTRReedBlock reed)) {
            return false;
        }
        block0:
        for (int l = 0; l < 16; ++l) {
            int i1 = i + random.nextInt(8) - random.nextInt(8);
            int j1 = j + random.nextInt(4) - random.nextInt(4);
            int k1 = k + random.nextInt(8) - random.nextInt(8);
            BlockPos surface = new BlockPos(i1, j1, k1);
            if (!world.getBlockState(surface).isAir()) {
                continue;
            }
            int depth = 0;
            BlockPos bed = surface.below();
            while (bed.getY() > world.getMinY() && world.getFluidState(bed).isSourceOfType(Fluids.WATER)
                    && world.getBlockState(bed).is(Blocks.WATER)) {
                if (++depth > LOTRReedBlock.MAX_WATER_DEPTH) {
                    continue block0;
                }
                bed = bed.below();
            }
            if (depth == 0 || !world.getBlockState(bed).is(LOTRBlockTags.REEDS_PLANTABLE_ON)) {
                continue;
            }
            if (LOTRWorldGenUtil.isBlockFreezable(world, surface.below())) {
                continue;
            }
            int reedHeight = 1 + random.nextInt(3);
            int aboveWater = 0;
            while (aboveWater < reedHeight && world.getBlockState(surface.above(aboveWater)).isAir()) {
                ++aboveWater;
            }
            reed.placeColumn(world, bed.above(), depth + aboveWater, Block.UPDATE_CLIENTS);
        }
        return true;
    }
}
