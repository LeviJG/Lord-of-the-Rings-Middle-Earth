package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.util.RandomSource;

// A pillar segment. Ported from LOTRBlockPillarBase: the horizontal faces lose
// their top or bottom edge when another pillar is stacked against them, so a
// column reads as one shaft rather than a stack of separate blocks. The top and
// bottom faces never change.
//
// isPillarAt in the original matched ANY pillar, not just the same kind, so a
// dwarven pillar stacks cleanly onto a mordor one. UP and DOWN follow that.
public class LOTRPillarBlock extends Block {

    public static final MapCodec<LOTRPillarBlock> CODEC = simpleCodec(LOTRPillarBlock::new);

    public static final BooleanProperty UP = BooleanProperty.create("up");
    public static final BooleanProperty DOWN = BooleanProperty.create("down");

    public LOTRPillarBlock(Properties properties) {
        super(properties);
        registerDefaultState(getStateDefinition().any()
                .setValue(UP, Boolean.FALSE)
                .setValue(DOWN, Boolean.FALSE));
    }

    @Override
    protected MapCodec<? extends LOTRPillarBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(UP, DOWN);
    }

    private static boolean pillarAt(BlockGetter level, BlockPos pos) {
        return level.getBlockState(pos).getBlock() instanceof LOTRPillarBlock;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockGetter level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        return defaultBlockState()
                .setValue(UP, pillarAt(level, pos.above()))
                .setValue(DOWN, pillarAt(level, pos.below()));
    }

    // Signature matched to LOTRChandelierBlock, which is the one already proven
    // to compile in this project.
    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess tickAccess,
                                     BlockPos pos, Direction direction, BlockPos neighbourPos,
                                     BlockState neighbourState, RandomSource random) {
        if (direction == Direction.UP) {
            return state.setValue(UP, neighbourState.getBlock() instanceof LOTRPillarBlock);
        }
        if (direction == Direction.DOWN) {
            return state.setValue(DOWN, neighbourState.getBlock() instanceof LOTRPillarBlock);
        }
        return state;
    }
}