package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import com.mojang.serialization.MapCodec;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * LOTRBlockHangingFruit, with LOTRBlockBanana and LOTRBlockDate as the two
 * sets of numbers: a bunch of fruit hanging off the side of a log.
 *
 * <p>The original's metadata was the side the log is on (0 north, 1 south,
 * 2 west, 3 east, via ForgeDirection.getOrientation(meta + 2)); {@link #FACING}
 * holds that direction. It stays only while that neighbour is wood, checked on
 * neighbour updates and on random ticks as updateTick did, and drops its fruit
 * when it goes.
 */
public class LOTRHangingFruitBlock extends HorizontalDirectionalBlock {
    public static final MapCodec<LOTRHangingFruitBlock> CODEC =
            simpleCodec(props -> new LOTRHangingFruitBlock(3.0, 15.0, () -> Items.AIR, props));

    private final VoxelShape north;
    private final VoxelShape south;
    private final VoxelShape west;
    private final VoxelShape east;
    private final Supplier<Item> fruit;

    /** minY and maxY in pixels: the banana's 0.1875..0.9375, the date's 0.3125..0.6875. */
    public LOTRHangingFruitBlock(double minY, double maxY, Supplier<Item> fruit, Properties properties) {
        super(properties);
        this.north = Block.box(6.0, minY, 0.0, 10.0, maxY, 4.0);
        this.south = Block.box(6.0, minY, 12.0, 10.0, maxY, 16.0);
        this.west = Block.box(0.0, minY, 6.0, 4.0, maxY, 10.0);
        this.east = Block.box(12.0, minY, 6.0, 16.0, maxY, 10.0);
        this.fruit = fruit;
        registerDefaultState(getStateDefinition().any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case SOUTH -> south;
            case WEST -> west;
            case EAST -> east;
            default -> north;
        };
    }

    /** canBlockStay: Block.isWood on the side it hangs from. */
    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.relative(state.getValue(FACING))).is(BlockTags.LOGS);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos,
            Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (direction == state.getValue(FACING) && !state.canSurvive(level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, level, ticks, pos, direction, neighborPos, neighborState, random);
    }

    /** updateTick made the same check. */
    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!state.canSurvive(level, pos)) {
            level.destroyBlock(pos, true);
        }
    }

    /** getItem: the fruit, not the block. */
    @Override
    protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
        return new ItemStack(fruit.get());
    }
}
