package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * The orc torch: a torch two blocks tall.
 *
 * <p>LOTRBlockDoubleTorch is its own block with a metadata bit for which half
 * you are looking at, and a canBlockStay that ties the two halves together --
 * the lower half needs something to stand on AND its own upper half, the upper
 * half needs the lower. DoublePlantBlock is the modern class with exactly that
 * shape of rule, which is why this extends it rather than TorchBlock.
 *
 * <p>Unlike the other torches it does not hang on walls; the original has no
 * wall form and no facing metadata at all.
 */
public class LOTRDoubleTorchBlock extends DoublePlantBlock {
    public static final MapCodec<LOTRDoubleTorchBlock> CODEC = simpleCodec(LOTRDoubleTorchBlock::new);

    // setBlockBoundsBasedOnState: a 2x2 post, full height for the shaft and
    // half height for the head.
    private static final VoxelShape SHAFT = Block.box(7.0, 0.0, 7.0, 9.0, 16.0, 9.0);
    private static final VoxelShape HEAD = Block.box(7.0, 0.0, 7.0, 9.0, 8.0, 9.0);

    public LOTRDoubleTorchBlock(Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<LOTRDoubleTorchBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(HALF) == DoubleBlockHalf.UPPER ? HEAD : SHAFT;
    }

    /**
     * LOTRBlockDoubleTorch.canPlaceTorchOn: the lower half wants a face it could
     * stand a torch on, not the dirt a plant wants. DoublePlantBlock's own
     * mayPlaceOn asks for soil, so it has to be replaced.
     */
    @Override
    protected boolean mayPlaceOn(BlockState below, BlockGetter level, BlockPos belowPos) {
        return below.isFaceSturdy(level, belowPos, Direction.UP);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            return level.getBlockState(pos.below()).is(this);
        }
        return super.canSurvive(state, level, pos);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        // Only the top half burns: the flame sits at the head of the torch.
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            LOTRGlowStyle.FLAME.spawn(level, random,
                    pos.getX() + 0.5, pos.getY() + 0.7, pos.getZ() + 0.5);
        }
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks,
            BlockPos pos, Direction direction, BlockPos neighbourPos, BlockState neighbourState,
            RandomSource random) {
        return super.updateShape(state, level, ticks, pos, direction, neighbourPos, neighbourState, random);
    }
}
