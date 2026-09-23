package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import com.mojang.serialization.MapCodec;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRBlockEntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRCarvedSignBlockEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

/**
 * LOTRBlockSignCarved: words carved into the face of a stone or wooden block.
 *
 * <p>A wall sign with nothing of its own to draw -- getIcon returned the empty
 * block icon, so all you see is the text, on the face of the block behind it.
 * {@link #FACING} is the side of that block it was carved on (the original's
 * metadata). The outline runs the full height of the face, as
 * setBlockBoundsBasedOnState stretched it to, two pixels deep; there is no
 * collision, and nothing drops. Pick-block gives the chisel that carves it.
 *
 * <p>The same class serves the ithildin sign, which glows at night when a
 * player is near; {@link LOTRCarvedSignBlockEntity#isIthildin} tells them apart.
 */
public class LOTRCarvedSignBlock extends BaseEntityBlock {
    public static final MapCodec<LOTRCarvedSignBlock> CODEC = simpleCodec(LOTRCarvedSignBlock::new);
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;

    private static final VoxelShape NORTH = Block.box(0.0, 0.0, 14.0, 16.0, 16.0, 16.0);
    private static final VoxelShape SOUTH = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 2.0);
    private static final VoxelShape WEST = Block.box(14.0, 0.0, 0.0, 16.0, 16.0, 16.0);
    private static final VoxelShape EAST = Block.box(0.0, 0.0, 0.0, 2.0, 16.0, 16.0);

    public LOTRCarvedSignBlock(Properties properties) {
        super(properties);
        registerDefaultState(getStateDefinition().any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case SOUTH -> SOUTH;
            case WEST -> WEST;
            case EAST -> EAST;
            default -> NORTH;
        };
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LOTRCarvedSignBlockEntity(pos, state);
    }

    /** LOTRTileEntitySignCarvedIthildin.updateEntity ramps the glow, client-side. */
    @Override
    @SuppressWarnings("unchecked")
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState state,
            BlockEntityType<T> type) {
        if (!level.isClientSide() || type != LOTRBlockEntities.CARVED_SIGN || this != LOTRBlocks.CARVED_ITHILDIN_SIGN) {
            return null;
        }
        return (BlockEntityTicker<T>) (BlockEntityTicker<LOTRCarvedSignBlockEntity>) LOTRCarvedSignBlockEntity::clientTick;
    }

    /**
     * BlockSign's wall check: the block it is carved into must still be solid.
     * isSolid is Material.isSolid's successor and what vanilla's WallSignBlock
     * still uses; it is deprecated with no replacement, and isFaceSturdy would
     * change which blocks can hold a sign.
     */
    @SuppressWarnings("deprecation")
    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.relative(state.getValue(FACING).getOpposite())).isSolid();
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos,
            Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (direction == state.getValue(FACING).getOpposite() && !state.canSurvive(level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, level, ticks, pos, direction, neighborPos, neighborState, random);
    }

    /** getItem: the chisel, the moon-chisel for ithildin. */
    @Override
    protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
        return new ItemStack(this == LOTRBlocks.CARVED_ITHILDIN_SIGN ? LOTRItems.MOON_CHISEL : LOTRItems.CHISEL);
    }
}
