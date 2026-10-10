package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LOTRWorldGenStreams extends LOTRFeature {
    public LegacyBlock liquidBlock;

    public LOTRWorldGenStreams(LegacyBlock block) {
        liquidBlock = block;
    }

    @Override
    protected boolean generateFeature(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        if (!isRock(world, i, j + 1, k) || !isRock(world, i, j - 1, k) || !isAirBlock(world, i, j, k) && !isRock(world, i, j, k)) {
            return false;
        }
        int sides = 0;
        if (isRock(world, i - 1, j, k)) {
            ++sides;
        }
        if (isRock(world, i + 1, j, k)) {
            ++sides;
        }
        if (isRock(world, i, j, k - 1)) {
            ++sides;
        }
        if (isRock(world, i, j, k + 1)) {
            ++sides;
        }
        int openAir = 0;
        if (isAirBlock(world, i - 1, j, k)) {
            ++openAir;
        }
        if (isAirBlock(world, i + 1, j, k)) {
            ++openAir;
        }
        if (isAirBlock(world, i, j, k - 1)) {
            ++openAir;
        }
        if (isAirBlock(world, i, j, k + 1)) {
            ++openAir;
        }
        if (sides == 3 && openAir == 1) {
            setBlock(world, i, j, k, liquidBlock, 0, 2);
            // updateTick with scheduledUpdatesAreImmediate: the spring flows at once, as vanilla's springs are ticked.
            net.minecraft.core.BlockPos pos = new net.minecraft.core.BlockPos(i, j, k);
            world.scheduleTick(pos, world.getFluidState(pos).getType(), 0);
        }
        return true;
    }

    public boolean isRock(WorldGenLevel world, int i, int j, int k) {
        BlockState block = getBlock(world, i, j, k);
        return LOTRLegacyBlocks.vanilla("stone").matches(block) || LOTRLegacyBlocks.vanilla("sandstone").matches(block) || LOTRLegacyBlocks.mod("rock").matches(block);
    }
}
