package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import com.mojang.serialization.MapCodec;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRBlockEntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRMillstoneBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

import org.jspecify.annotations.Nullable;

/**
 * LOTRBlockMillstone. A grindstone you drive with redstone rather than fuel.
 *
 * <p>The original tracked "running" in bit 8 of the block metadata --
 * {@code isMillstoneActive} / {@code toggleMillstoneActive} -- and used it for
 * two things: to pick the animated running texture over the still one, and to
 * decide whether to throw smoke. {@link #ACTIVE} is that bit.
 *
 * <p>There is deliberately no FACING property. The original's
 * {@code registerBlockIcons} registers only a side and a top, so the block looks
 * the same from every horizontal direction and has nothing to orient.
 */
public class LOTRMillstoneBlock extends BaseEntityBlock {

    public static final MapCodec<LOTRMillstoneBlock> CODEC = simpleCodec(LOTRMillstoneBlock::new);

    /** Metadata bit 8 in the original: is the millstone turning? */
    public static final BooleanProperty ACTIVE = BlockStateProperties.LIT;

    public LOTRMillstoneBlock(Properties properties) {
        super(properties);
        registerDefaultState(getStateDefinition().any().setValue(ACTIVE, false));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<net.minecraft.world.level.block.Block, BlockState> builder) {
        builder.add(ACTIVE);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LOTRMillstoneBlockEntity(pos, state);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(
            Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide() || type != LOTRBlockEntities.MILLSTONE) {
            return null;
        }
        return (BlockEntityTicker<T>) (BlockEntityTicker<LOTRMillstoneBlockEntity>)
                (innerLevel, pos, blockState, entity) ->
                        LOTRMillstoneBlockEntity.serverTick((ServerLevel) innerLevel, pos, blockState, entity);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                               Player player, BlockHitResult hit) {
        if (!level.isClientSide()
                && level.getBlockEntity(pos) instanceof LOTRMillstoneBlockEntity millstone) {
            player.openMenu(millstone);
        }
        return InteractionResult.SUCCESS;
    }

    /** hasComparatorInputOverride / getComparatorInputOverride. */
    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction side) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos));
    }

    /**
     * randomDisplayTick: six puffs of smoke a tick from just above the stone,
     * scattered within the middle 0.4 of the block, while it is running.
     */
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (!state.getValue(ACTIVE)) {
            return;
        }

        for (int i = 0; i < 6; ++i) {
            level.addParticle(ParticleTypes.SMOKE,
                    pos.getX() + 0.5 + (random.nextFloat() * 0.4f - 0.2f),
                    pos.getY() + 0.9 + random.nextFloat() * 0.2,
                    pos.getZ() + 0.5 + (random.nextFloat() * 0.4f - 0.2f),
                    0.0, 0.0, 0.0);
        }
    }
}
