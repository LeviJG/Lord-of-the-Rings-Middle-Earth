package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FarmlandBlock;
import net.minecraft.world.level.block.state.BlockState;

// Farmland that reverts to mud rather than vanilla dirt.
//
// FarmlandBlock.turnToDirt is static and hardcodes Blocks.DIRT, so trampling
// mud farmland conjured plain dirt. Rather than reimplement the trample rules
// (which read private Level state), this lets vanilla do its job and then
// converts the result.
public class LOTRFarmlandBlock extends FarmlandBlock {

    // No CODEC or codec() override: FarmlandBlock declares codec() with an exact
    // type this cannot narrow, and the inherited one is only used for datapack
    // block definitions, which this mod does not use.
    public LOTRFarmlandBlock(Properties properties) {
        super(properties);
    }

    // Drying out. Vanilla randomTick reverts unwatered, uncropped farmland to
    // dirt; same interception as fallOn.
    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.randomTick(state, level, pos, random);
        if (level.getBlockState(pos).is(Blocks.DIRT)) {
            level.setBlockAndUpdate(pos, LOTRBuildingBlocks.MUD.defaultBlockState());
        }
    }

    // A block set on top: vanilla updateShape schedules this tick, which turns
    // the farmland to dirt. LOTRBlockMudFarmland.onNeighborBlockChange made it
    // mud instead.
    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.tick(state, level, pos, random);
        if (level.getBlockState(pos).is(Blocks.DIRT)) {
            level.setBlockAndUpdate(pos, LOTRBuildingBlocks.MUD.defaultBlockState());
        }
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, double fallDistance) {
        super.fallOn(level, state, pos, entity, fallDistance);
        // Vanilla turned it to dirt; make it mud instead.
        if (level.getBlockState(pos).is(Blocks.DIRT)) {
            level.setBlockAndUpdate(pos, LOTRBuildingBlocks.MUD.defaultBlockState());
        }
    }
}