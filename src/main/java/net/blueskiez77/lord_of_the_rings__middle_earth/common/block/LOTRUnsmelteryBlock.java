package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import com.mojang.serialization.MapCodec;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRBlockEntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRUnsmelteryBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import org.jspecify.annotations.Nullable;

/**
 * LOTRBlockUnsmeltery -- a cauldron on a stand that pulls metal back out of
 * finished gear.
 *
 * <p>Like {@link LOTRForgeBlock} this leans on AbstractFurnaceBlock for FACING,
 * LIT, the comparator output, rotate/mirror and the open-container click, but
 * not for the ticker or the drops -- both of those are typed to
 * AbstractFurnaceBlockEntity, which this is not.
 *
 * <p>The block draws nothing of its own: `getRenderType` in the original
 * pointed at a custom renderer and `renderAsNormalBlock` was false, so the
 * whole cauldron comes from LOTRUnsmelteryRenderer.
 */
public class LOTRUnsmelteryBlock extends AbstractFurnaceBlock {

    public static final MapCodec<LOTRUnsmelteryBlock> CODEC = simpleCodec(LOTRUnsmelteryBlock::new);

    public LOTRUnsmelteryBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends AbstractFurnaceBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LOTRUnsmelteryBlockEntity(pos, state);
    }

    @Override
    protected void openContainer(Level level, BlockPos pos, Player player) {
        if (level.getBlockEntity(pos) instanceof LOTRUnsmelteryBlockEntity unsmeltery) {
            player.openMenu(unsmeltery);
        }
    }

    /** The cauldron is drawn entirely by the block entity renderer. */
    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    /**
     * Both sides tick: the server smelts, and the client sways the cauldron.
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(
            Level level, BlockState state, BlockEntityType<T> type) {
        if (type != LOTRBlockEntities.UNSMELTERY) {
            return null;
        }
        if (level.isClientSide()) {
            return (BlockEntityTicker<T>) (BlockEntityTicker<LOTRUnsmelteryBlockEntity>)
                    LOTRUnsmelteryBlockEntity::clientTick;
        }
        return (BlockEntityTicker<T>) (BlockEntityTicker<LOTRUnsmelteryBlockEntity>)
                (innerLevel, pos, blockState, entity) ->
                        LOTRUnsmelteryBlockEntity.serverTick((ServerLevel) innerLevel, pos, blockState, entity);
    }

    /**
     * Three slots of contents that nothing upstream will drop for us --
     * AbstractFurnaceBlock only runs Containers.updateNeighboursAfterDestroy,
     * leaving the dropping to BaseContainerBlockEntity, which this is not.
     */
    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level,
                                               BlockPos pos, boolean movedByPiston) {
        if (level.getBlockEntity(pos) instanceof LOTRUnsmelteryBlockEntity unsmeltery) {
            Containers.dropContents(level, pos, unsmeltery.getItems());
        }
        super.affectNeighborsAfterRemoval(state, level, pos, movedByPiston);
    }

    /**
     * randomDisplayTick. The original calls super first -- so the unsmeltery
     * gets LOTRBlockForgeBase's flame front like every other forge -- and only
     * then adds three LARGE smoke puffs of its own from the cauldron's mouth.
     * useLargeSmoke() returns false so the base's six-puff cloud is skipped;
     * these three replace it.
     */
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (!state.getValue(AbstractFurnaceBlock.LIT)) {
            return;
        }
        LOTRForgeBlock.flameFront(state, level, pos, random);

        for (int i = 0; i < 3; ++i) {
            level.addParticle(ParticleTypes.LARGE_SMOKE,
                    pos.getX() + 0.25 + random.nextFloat() * 0.5,
                    pos.getY() + 0.5 + random.nextFloat() * 0.5,
                    pos.getZ() + 0.25 + random.nextFloat() * 0.5,
                    0.0, 0.0, 0.0);
        }
    }
}
