package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRBlockMorgulShroom: a Mordor plant that spreads beside water.
 *
 * <p>updateTick, transcribed: one random tick in ten it looks for water in the
 * layer below, two blocks out each way; if there is any, it picks a random spot
 * within one block and grows a new shroom there when that spot is air the
 * shroom could stand in.
 */
public class LOTRMorgulShroomBlock extends LOTRPlantBlock {
    public LOTRMorgulShroomBlock(Properties properties) {
        super(Shape.SHROOM, Ground.MORDOR, properties);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (random.nextInt(10) != 0) {
            return;
        }
        boolean nearbyWater = false;
        for (int dx = -2; dx <= 2 && !nearbyWater; ++dx) {
            for (int dz = -2; dz <= 2 && !nearbyWater; ++dz) {
                if (level.getFluidState(pos.offset(dx, -1, dz)).is(FluidTags.WATER)) {
                    nearbyWater = true;
                }
            }
        }
        if (!nearbyWater) {
            return;
        }
        BlockPos target = pos.offset(random.nextInt(3) - 1, random.nextInt(3) - 1, random.nextInt(3) - 1);
        BlockState shroom = defaultBlockState();
        if (level.isEmptyBlock(target) && shroom.canSurvive(level, target)) {
            level.setBlock(target, shroom, Block.UPDATE_ALL);
        }
    }
}
