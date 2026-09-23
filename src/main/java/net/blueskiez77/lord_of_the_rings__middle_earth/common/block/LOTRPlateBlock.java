package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import com.mojang.serialization.MapCodec;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRPlateBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

/**
 * LOTRBlockPlate: a plate to pile food on.
 *
 * <p>onBlockActivated, transcribed. An empty plate takes one of any plain food
 * (nothing that leaves a bowl or a bottle behind). A plate with food takes
 * more of exactly the same food up to a stack; with anything else in hand --
 * or nothing -- you eat one off it, if you are hungry.
 *
 * <p>The food spills when the plate is broken; the plate itself drops as its
 * item through the loot table.
 */
public class LOTRPlateBlock extends BaseEntityBlock {
    public static final MapCodec<LOTRPlateBlock> CODEC = simpleCodec(LOTRPlateBlock::new);
    /** setBlockBounds(0.125, 0, 0.125, 0.875, 0.125, 0.875). */
    private static final VoxelShape SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 2.0, 14.0);

    public LOTRPlateBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LOTRPlateBlockEntity(pos, state);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player,
            InteractionHand hand, BlockHitResult hitResult) {
        if (!(level.getBlockEntity(pos) instanceof LOTRPlateBlockEntity plate)) {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        }
        ItemStack food = plate.getFoodItem();
        if (food.isEmpty() && LOTRPlateBlockEntity.isValidFoodItem(stack)) {
            if (!level.isClientSide()) {
                plate.setFoodItem(stack.copyWithCount(1));
            }
            if (!player.hasInfiniteMaterials()) {
                stack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }
        if (!food.isEmpty()) {
            if (!stack.isEmpty() && ItemStack.isSameItemSameComponents(stack, food)) {
                if (food.getCount() < food.getMaxStackSize()) {
                    if (!level.isClientSide()) {
                        plate.setFoodItem(food.copyWithCount(food.getCount() + 1));
                    }
                    if (!player.hasInfiniteMaterials()) {
                        stack.shrink(1);
                    }
                    return InteractionResult.SUCCESS;
                }
            } else if (player.canEat(false)) {
                if (!level.isClientSide()) {
                    // onEaten always took one, creative or not.
                    ItemStack eaten = food.copy();
                    int before = eaten.getCount();
                    eaten.finishUsingItem(level, player);
                    if (eaten.getCount() == before) {
                        eaten.shrink(1);
                    }
                    plate.setFoodItem(eaten);
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    /** canBlockStay: a solid top face underneath. */
    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos below = pos.below();
        return level.getBlockState(below).isFaceSturdy(level, below, Direction.UP);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos,
            Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (direction == Direction.DOWN && !state.canSurvive(level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, level, ticks, pos, direction, neighborPos, neighborState, random);
    }

    /** getPickBlock: one of the food on it, or the plate. */
    @Override
    protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
        if (level.getBlockEntity(pos) instanceof LOTRPlateBlockEntity plate && !plate.getFoodItem().isEmpty()) {
            return plate.getFoodItem().copyWithCount(1);
        }
        return super.getCloneItemStack(level, pos, state, includeData);
    }
}
