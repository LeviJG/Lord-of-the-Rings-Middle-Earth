package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import com.mojang.serialization.MapCodec;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRWeaponRackBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * A weapon rack: a stand that displays one weapon.
 *
 * <p>LOTRBlockWeaponRack packs two things into its metadata -- a horizontal
 * facing in the low two bits, and bit 4 for "hung on a wall rather than stood
 * on the floor". Those are {@link #FACING} and {@link #ON_WALL} here. A floor
 * rack takes its facing from the player who placed it; a wall rack takes it
 * from the face that was clicked.
 *
 * <p>Right-clicking an empty rack with a weapon puts it up; right-clicking a
 * full one takes it back, into your hand if it is free and onto the floor if it
 * is not. What counts as a weapon is an item tag -- see
 * LOTRWeaponRackBlockEntity.canAccept.
 *
 * <p>The block draws nothing of its own: getRenderType returned -1, so the
 * whole rack, weapon and all, is the block entity renderer's work.
 */
public class LOTRWeaponRackBlock extends BaseEntityBlock {
    public static final MapCodec<LOTRWeaponRackBlock> CODEC = simpleCodec(LOTRWeaponRackBlock::new);

    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty ON_WALL = BooleanProperty.create("on_wall");

    // Selection boxes. A port choice: the original's setBlockBoundsBasedOnState
    // (0.2 in from the unfaced sides, 0.9 high) fitted the model badly, so these
    // are measured off LOTRModelWeaponRack through the renderer's matrix, plus
    // a sixteenth of slack. A wall rack sits against the wall OPPOSITE its
    // facing, since FACING is the clicked face.
    //
    //   floor: base plate x 1..15, z 5..11; holder tops out at y 11.
    //   wall:  base.xRot of -90 lays the same parts flat, giving y 4..8 and
    //          z 0..5 measured out from the wall face, x 1..15 as before.
    private static final VoxelShape FLOOR_NS = Block.box(1.0, 0.0, 5.0, 15.0, 12.0, 11.0);
    private static final VoxelShape FLOOR_EW = Block.box(5.0, 0.0, 1.0, 11.0, 12.0, 15.0);
    private static final VoxelShape WALL_NORTH = Block.box(1.0, 3.0, 11.0, 15.0, 9.0, 16.0);
    private static final VoxelShape WALL_SOUTH = Block.box(1.0, 3.0, 0.0, 15.0, 9.0, 5.0);
    private static final VoxelShape WALL_WEST = Block.box(11.0, 3.0, 1.0, 16.0, 9.0, 15.0);
    private static final VoxelShape WALL_EAST = Block.box(0.0, 3.0, 1.0, 5.0, 9.0, 15.0);

    public LOTRWeaponRackBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(ON_WALL, false));
    }

    @Override
    protected MapCodec<LOTRWeaponRackBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, ON_WALL);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LOTRWeaponRackBlockEntity(pos, state);
    }

    /** getRenderType() == -1: nothing in the chunk mesh, all of it in the renderer. */
    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        if (!state.getValue(ON_WALL)) {
            return facing.getAxis() == Direction.Axis.Z ? FLOOR_NS : FLOOR_EW;
        }
        return switch (facing) {
            case NORTH -> WALL_NORTH;
            case SOUTH -> WALL_SOUTH;
            case WEST -> WALL_WEST;
            default -> WALL_EAST;
        };
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        // getCollisionBoundingBoxFromPool returned null.
        return net.minecraft.world.phys.shapes.Shapes.empty();
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction face = context.getClickedFace();
        if (face.getAxis() == Direction.Axis.Y) {
            // onBlockPlaced gave a floor rack for the top AND the underside of
            // a block; a floor rack faces whoever put it down.
            return defaultBlockState()
                    .setValue(ON_WALL, false)
                    .setValue(FACING, context.getHorizontalDirection().getOpposite());
        }
        return defaultBlockState().setValue(ON_WALL, true).setValue(FACING, face);
    }

    /** canBlockStay: a floor rack wants the block below, a wall rack the wall behind. */
    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        if (!state.getValue(ON_WALL)) {
            return canSupportCenter(level, pos.below(), Direction.UP);
        }
        Direction behind = state.getValue(FACING).getOpposite();
        return level.getBlockState(pos.relative(behind))
                .isFaceSturdy(level, pos.relative(behind), state.getValue(FACING));
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks,
            BlockPos pos, Direction direction, BlockPos neighbourPos, BlockState neighbourState,
            RandomSource random) {
        return canSurvive(state, level, pos) ? state : Blocks.AIR.defaultBlockState();
    }

    @Override
    protected InteractionResult useItemOn(ItemStack held, BlockState state, Level level, BlockPos pos,
            Player player, InteractionHand hand, BlockHitResult hit) {
        return interact(state, level, pos, player, hand, held);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
            Player player, BlockHitResult hit) {
        return interact(state, level, pos, player, InteractionHand.MAIN_HAND, ItemStack.EMPTY);
    }

    private InteractionResult interact(BlockState state, Level level, BlockPos pos, Player player,
            InteractionHand hand, ItemStack held) {
        if (!(level.getBlockEntity(pos) instanceof LOTRWeaponRackBlockEntity rack)) {
            return InteractionResult.PASS;
        }

        if (!rack.getWeapon().isEmpty()) {
            if (!level.isClientSide()) {
                ItemStack taken = rack.getWeapon();
                rack.setWeapon(ItemStack.EMPTY);
                if (held.isEmpty()) {
                    player.setItemInHand(hand, taken);
                    level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS,
                            0.2F, ((level.getRandom().nextFloat() - level.getRandom().nextFloat())
                                    * 0.7F + 1.0F) * 2.0F);
                } else {
                    popResource(level, pos, taken);
                }
            }
            return InteractionResult.SUCCESS;
        }

        if (!LOTRWeaponRackBlockEntity.canAccept(held)) {
            return InteractionResult.PASS;
        }
        if (!level.isClientSide()) {
            rack.setWeapon(held.copyWithCount(1));
        }
        if (!player.getAbilities().instabuild) {
            held.shrink(1);
        }
        return InteractionResult.SUCCESS;
    }

}
