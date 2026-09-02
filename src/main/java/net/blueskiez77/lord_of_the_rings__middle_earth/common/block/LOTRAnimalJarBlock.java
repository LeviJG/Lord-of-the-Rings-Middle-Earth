package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import java.util.List;

import com.mojang.serialization.MapCodec;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRAnimalJarBlockEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRAnimalJarItem;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * A bird cage: LOTRBlockBirdCage, and the LOTRBlockAnimalJar it inherits from.
 *
 * <p>The cage holds a creature as data rather than as a live entity, so what it
 * is holding has to survive being broken and carried around. That is
 * {@link #getDrops}, which stamps the block entity onto the dropped stack, and
 * the BlockItem's own placement, which copies it back. Right-clicking a full
 * cage lets the occupant out, which is the counterpart to catching one with the
 * cage in hand -- see LOTRAnimalJarItem.
 *
 * <p>canBlockStay: the cage wants a solid top face beneath it and drops if that
 * goes, which is why this is not a plain Block.
 */
public class LOTRAnimalJarBlock extends BaseEntityBlock {
    public static final MapCodec<LOTRAnimalJarBlock> CODEC =
            simpleCodec(props -> new LOTRAnimalJarBlock(Shapes.block(), props));

    /**
     * The cage is a full cube; the butterfly jar is a squat glass pot --
     * LOTRBlockButterflyJar's setBlockBounds(0.1875, 0, 0.1875, 0.8125, 0.75,
     * 0.8125).
     */
    private final VoxelShape shape;

    public LOTRAnimalJarBlock(Properties properties) {
        this(Shapes.block(), properties);
    }

    public LOTRAnimalJarBlock(VoxelShape shape, Properties properties) {
        super(properties);
        this.shape = shape;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos,
            net.minecraft.world.phys.shapes.CollisionContext context) {
        return shape;
    }

    @Override
    protected MapCodec<LOTRAnimalJarBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LOTRAnimalJarBlockEntity(pos, state);
    }

    @Override
    protected net.minecraft.world.level.block.RenderShape getRenderShape(BlockState state) {
        // The cage itself is an ordinary model; only its occupant would need a
        // block entity renderer.
        return net.minecraft.world.level.block.RenderShape.MODEL;
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return canSupportCenter(level, pos.below(), Direction.UP);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level,
            net.minecraft.world.level.ScheduledTickAccess ticks, BlockPos pos, Direction direction,
            BlockPos neighbourPos, BlockState neighbourState, net.minecraft.util.RandomSource random) {
        return canSurvive(state, level, pos) ? state
                : net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
    }

    /** Right-click a full cage and whatever is in it goes free. */
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
            Player player, BlockHitResult hit) {
        if (!(level.getBlockEntity(pos) instanceof LOTRAnimalJarBlockEntity jar) || jar.isEmpty()) {
            return InteractionResult.PASS;
        }
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        if (!jar.release(level, pos)) {
            return InteractionResult.PASS;
        }
        level.playSound(null, pos, SoundEvents.CHICKEN_EGG, SoundSource.BLOCKS,
                0.5F, 0.5F + level.getRandom().nextFloat() * 0.5F);
        return InteractionResult.SUCCESS;
    }

    /**
     * LOTRBlockAnimalJar.getJarDrop: the cage comes back carrying whatever was
     * inside it, so breaking and replacing one does not set the bird free.
     *
     * <p>The occupant travels in the stack's block_entity_data, which is how
     * vanilla moves a spawner's or a shulker box's contents. The BlockItem
     * copies it straight back onto the block entity when the cage is placed
     * again, so nothing else has to be written for the round trip.
     */
    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        List<ItemStack> drops = super.getDrops(state, params);
        if (params.getOptionalParameter(LootContextParams.BLOCK_ENTITY)
                instanceof LOTRAnimalJarBlockEntity jar && !jar.isEmpty()) {
            drops.forEach(stack -> {
                if (stack.getItem() instanceof BlockItem) {
                    LOTRAnimalJarItem.setJarEntity(stack, jar.getEntityData());
                }
            });
        }
        return drops;
    }
}
