package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRBlockEntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRKebabStandBlockEntity;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jspecify.annotations.Nullable;

/**
 * LOTRBlockKebabStand -- a spit-roast for meat.
 *
 * <p>It is a rack, not a machine: set it on TOP of something that holds fuel --
 * a furnace, a forge, a chest of coal -- skewer up to eight pieces of raw meat
 * onto it, and it draws fuel from the container below and roasts them into
 * Kebabs. Right-click with meat to add, right-click empty-handed to take a
 * piece back, cooked ones first.
 *
 * <p>Two variants exist for the two halves of the map: a plain wooden stand and
 * a pale {@code sand} one for Harad. They differ only in the texture the
 * renderer binds, which is why the variant rides on the block.
 */
public class LOTRKebabStandBlock extends Block implements EntityBlock {

    public static final MapCodec<LOTRKebabStandBlock> CODEC =
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Codec.STRING.optionalFieldOf("variant", "")
                            .forGetter(LOTRKebabStandBlock::variant),
                    propertiesCodec()
            ).apply(instance, LOTRKebabStandBlock::new));

    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;

    /** setBlockBounds was the full cube, but getCollisionBoundingBox was null. */
    private static final VoxelShape SHAPE = Shapes.block();

    /** "" for the wooden stand, "sand" for the Haradric one. */
    private final String variant;

    public LOTRKebabStandBlock(String variant, Properties properties) {
        super(properties);
        this.variant = variant;
        registerDefaultState(getStateDefinition().any().setValue(FACING, Direction.NORTH));
    }

    public String variant() {
        return variant;
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LOTRKebabStandBlockEntity(pos, state);
    }

    /** The whole stand is drawn by LOTRKebabStandRenderer. */
    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(
            Level level, BlockState state, BlockEntityType<T> type) {
        if (type != LOTRBlockEntities.KEBAB_STAND) {
            return null;
        }
        if (level.isClientSide()) {
            return (BlockEntityTicker<T>) (BlockEntityTicker<LOTRKebabStandBlockEntity>)
                    LOTRKebabStandBlockEntity::clientTick;
        }
        return (BlockEntityTicker<T>) (BlockEntityTicker<LOTRKebabStandBlockEntity>)
                (innerLevel, pos, blockState, entity) ->
                        LOTRKebabStandBlockEntity.serverTick((ServerLevel) innerLevel, pos, blockState, entity);
    }

    // ------------------------------------------------------------------ shape

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    /** getCollisionBoundingBoxFromPool returned null: you walk through it. */
    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos,
                                           CollisionContext context) {
        return Shapes.empty();
    }

    // ------------------------------------------------------------- attachment

    /** canBlockStay: it needs something under it -- the fire it cooks over. */
    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos below = pos.below();
        return level.getBlockState(below).isFaceSturdy(level, below, Direction.UP);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess tickAccess,
                                     BlockPos pos, Direction direction, BlockPos neighborPos,
                                     BlockState neighborState, RandomSource random) {
        if (direction == Direction.DOWN && !canSurvive(state, level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, level, tickAccess, pos, direction, neighborPos, neighborState, random);
    }

    // ------------------------------------------------------------- placement

    /**
     * onBlockPlacedBy mapped the player's yaw to the OPPOSITE facing, so the
     * stand's open side turns to face whoever set it down.
     */
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    // ------------------------------------------------------------ interaction

    /**
     * onBlockActivated. Holding raw meat and there is room? Add a skewer.
     * Otherwise, if anything is on the spit, take one back -- cooked first.
     */
    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                          Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!(level.getBlockEntity(pos) instanceof LOTRKebabStandBlockEntity stand)) {
            return InteractionResult.PASS;
        }

        if (!stand.isCooked() && stand.isMeat(stack) && stand.hasEmptySlot()) {
            if (!level.isClientSide() && stand.addMeat(stack) && !player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }
        return takeMeat(level, pos, player, stand);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                               Player player, BlockHitResult hitResult) {
        if (!(level.getBlockEntity(pos) instanceof LOTRKebabStandBlockEntity stand)) {
            return InteractionResult.PASS;
        }
        return takeMeat(level, pos, player, stand);
    }

    private static InteractionResult takeMeat(Level level, BlockPos pos, Player player,
                                              LOTRKebabStandBlockEntity stand) {
        if (stand.getMeatCount() <= 0) {
            return InteractionResult.PASS;
        }
        if (!level.isClientSide()) {
            ItemStack meat = stand.removeFirstMeat();
            if (!meat.isEmpty() && !player.getInventory().add(meat)) {
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), meat);
            }
            level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS,
                    0.5f, 0.5f + level.getRandom().nextFloat() * 0.5f);
        }
        return InteractionResult.SUCCESS;
    }

    /**
     * The original stored the spit's contents in the dropped item's NBT, so a
     * stand kept its meat when you picked it up. That needs a data component
     * here; for now the meat simply drops alongside the stand, which loses
     * nothing but the tidiness. See docs/TODO-kebab-stand.md.
     */
    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level,
                                               BlockPos pos, boolean movedByPiston) {
        if (level.getBlockEntity(pos) instanceof LOTRKebabStandBlockEntity stand) {
            Containers.dropContents(level, pos, stand.getMeats());
        }
        super.affectNeighborsAfterRemoval(state, level, pos, movedByPiston);
    }
}
