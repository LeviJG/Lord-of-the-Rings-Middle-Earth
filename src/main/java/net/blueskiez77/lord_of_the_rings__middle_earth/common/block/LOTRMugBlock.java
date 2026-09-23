package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import com.mojang.serialization.MapCodec;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRBlockEntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRMugBlockEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRDrinkItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRVessel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

/**
 * LOTRBlockMug and its subclasses: a vessel set down on a surface, empty or
 * with a drink in it.
 *
 * <p>Right-click a block with any vessel or drink (tryPlaceMug) to set it down
 * facing the way you look. On the block, onBlockActivated:
 *
 * <ul>
 *   <li>an empty vessel takes the drink out, moved into that vessel;</li>
 *   <li>a full drink pours into an empty one, and you keep its empty vessel;</li>
 *   <li>anything else drinks it where it stands, if you could drink it.</li>
 * </ul>
 *
 * <p>An empty vessel left out in the rain may fill with water (1 in 6000 a tick).
 * Everything draws in {@code LOTRMugRenderer}; the block has no model.
 *
 * <p>NOT ported: poisoning a drink here -- the bottle of poison is not ported.
 */
public class LOTRMugBlock extends BaseEntityBlock {
    public static final MapCodec<LOTRMugBlock> CODEC =
            simpleCodec(props -> new LOTRMugBlock(LOTRVessel.MUG, 3.0f, 8.0f, props));
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    /** MUG_SCALE. */
    public static final float MUG_SCALE = 0.75f;

    private final LOTRVessel vessel;
    private final VoxelShape shape;

    /** width and height in pixels, before the 0.75 the vessel is drawn at. */
    public LOTRMugBlock(LOTRVessel vessel, float width, float height, Properties properties) {
        super(properties);
        this.vessel = vessel;
        double half = width * MUG_SCALE;
        this.shape = Block.box(8.0 - half, 0.0, 8.0 - half, 8.0 + half, height * MUG_SCALE, 8.0 + half);
        registerDefaultState(getStateDefinition().any().setValue(FACING, Direction.SOUTH));
    }

    public LOTRVessel vessel() {
        return vessel;
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
        return shape;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LOTRMugBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState state,
            BlockEntityType<T> type) {
        return level.isClientSide() ? null
                : createTickerHelper(type, LOTRBlockEntities.MUG, LOTRMugBlockEntity::serverTick);
    }

    /** canBlockStay: canPlaceTorchOnTop below. */
    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return Block.canSupportCenter(level, pos.below(), Direction.UP);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos,
            Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (direction == Direction.DOWN && !state.canSurvive(level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, level, ticks, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player,
            InteractionHand hand, BlockHitResult hitResult) {
        if (!(level.getBlockEntity(pos) instanceof LOTRMugBlockEntity mug)) {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        }
        ItemStack mugItem = mug.getMugItem();
        if (!mug.isEmpty() && LOTRVessel.isEmptyDrink(stack)) {
            if (!level.isClientSide()) {
                ItemStack taken = LOTRVessel.of(stack).fill(mugItem);
                if (player.hasInfiniteMaterials()) {
                    player.setItemInHand(hand, taken);
                } else {
                    stack.shrink(1);
                    if (stack.isEmpty()) {
                        player.setItemInHand(hand, taken);
                    } else if (!player.getInventory().add(taken)) {
                        player.drop(taken, false);
                    }
                }
                mug.setEmpty();
                level.playSound(null, player.getX(), player.getY(), player.getZ(), LOTRSounds.ITEM_MUG_FILL,
                        SoundSource.PLAYERS, 0.5f, 0.8f + level.getRandom().nextFloat() * 0.4f);
            }
            return InteractionResult.SUCCESS;
        }
        if (mug.isEmpty() && LOTRVessel.isFullDrink(stack)) {
            if (!level.isClientSide()) {
                ItemStack emptyVessel = LOTRVessel.of(stack).emptyStack();
                ItemStack fill = stack.copyWithCount(1);
                player.setItemInHand(hand, emptyVessel);
                mug.setMugItem(fill);
                level.playSound(null, pos, LOTRSounds.ITEM_MUG_FILL, SoundSource.BLOCKS, 0.5f,
                        0.8f + level.getRandom().nextFloat() * 0.4f);
            }
            return InteractionResult.SUCCESS;
        }
        if (!mug.isEmpty()) {
            ItemStack equivalent = LOTRVessel.equivalentDrink(mugItem);
            if (equivalent.getItem() instanceof LOTRDrinkItem drink && drink.canPlayerDrink(player)) {
                if (!level.isClientSide()) {
                    mugItem.finishUsingItem(level, player);
                    mug.setEmpty();
                    level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.GENERIC_DRINK,
                            SoundSource.PLAYERS, 0.5f, level.getRandom().nextFloat() * 0.1f + 0.9f);
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    /** onBlockHarvested: nothing drops for a creative player. */
    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (player.hasInfiniteMaterials() && level.getBlockEntity(pos) instanceof LOTRMugBlockEntity mug) {
            mug.markCreativeBroken();
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    /** getPickBlock: the vessel, with its drink. */
    @Override
    protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
        if (level.getBlockEntity(pos) instanceof LOTRMugBlockEntity mug) {
            return mug.getMugItem();
        }
        return super.getCloneItemStack(level, pos, state, includeData);
    }

    /**
     * LOTRItemMug.tryPlaceMug: set the held vessel or drink down in the block
     * beside the clicked face, if that is air (not water) above something a
     * torch could stand on. Takes one from the stack, creative or not, as the
     * original did.
     */
    public static InteractionResult tryPlaceMug(UseOnContext context, ItemStack stack) {
        LOTRVessel vessel = LOTRVessel.of(stack);
        if (vessel == null || !vessel.canPlace()) {
            return InteractionResult.PASS;
        }
        Level level = context.getLevel();
        Direction side = context.getClickedFace();
        BlockPos pos = context.getClickedPos().relative(side);
        BlockState existing = level.getBlockState(pos);
        if (!existing.canBeReplaced() || !existing.getFluidState().isEmpty()) {
            return InteractionResult.PASS;
        }
        Player player = context.getPlayer();
        if (player != null && !player.mayUseItemAt(pos, side, stack)) {
            return InteractionResult.PASS;
        }
        BlockState state = vessel.block().defaultBlockState().setValue(FACING, context.getHorizontalDirection());
        if (!state.canSurvive(level, pos)) {
            return InteractionResult.PASS;
        }
        if (!level.isClientSide()) {
            level.setBlock(pos, state, Block.UPDATE_ALL);
            if (level.getBlockEntity(pos) instanceof LOTRMugBlockEntity mug) {
                mug.setMugItem(stack.copyWithCount(1));
                mug.setVessel(vessel);
            }
            SoundType sound = state.getSoundType();
            level.playSound(null, pos, sound.getPlaceSound(), SoundSource.BLOCKS, (sound.getVolume() + 1.0f) / 2.0f,
                    sound.getPitch() * 0.8f);
        }
        stack.shrink(1);
        return InteractionResult.SUCCESS;
    }
}
