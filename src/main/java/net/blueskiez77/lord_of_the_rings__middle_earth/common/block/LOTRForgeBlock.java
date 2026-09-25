package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import com.mojang.serialization.MapCodec;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRBlockEntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRForgeBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import org.jspecify.annotations.Nullable;

// Forges and ovens. Extends AbstractFurnaceBlock for FACING, LIT, the
// comparator output, rotate/mirror and useWithoutItem, but not for the ticker:
// createFurnaceTicker is typed to AbstractFurnaceBlockEntity. The contents drop
// on their own -- BlockEntity.preRemoveSideEffects empties any Container.
public class LOTRForgeBlock extends AbstractFurnaceBlock {

    public static final MapCodec<LOTRForgeBlock> CODEC = simpleCodec(LOTRForgeBlock::new);

    public LOTRForgeBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends AbstractFurnaceBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LOTRForgeBlockEntity(pos, state);
    }

    @Override
    protected void openContainer(Level level, BlockPos pos, Player player) {
        if (level.getBlockEntity(pos) instanceof LOTRForgeBlockEntity forge) {
            player.openMenu(forge);
        }
    }

    // LOTRBlockForgeBase.randomDisplayTick was vanilla's furnace effect
    // verbatim -- same 0.52 offset, same 6/16 vertical jitter, same two
    // particles -- so this is FurnaceBlock.animateTick with nothing changed.
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (!state.getValue(LIT)) {
            return;
        }

        double x = pos.getX() + 0.5;
        double y = pos.getY();
        double z = pos.getZ() + 0.5;

        if (random.nextDouble() < 0.1) {
            level.playLocalSound(x, y, z, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
        }

        flameFront(state, level, pos, random);

        // useLargeSmoke(), true for all four forges: six puffs from the top
        // corners, two in three of them large.
        for (int l = 0; l < 6; ++l) {
            double cx = (random.nextBoolean() ? 0.0 : 1.0) - 0.1 + random.nextFloat() * 0.2;
            double cz = (random.nextBoolean() ? 0.0 : 1.0) - 0.1 + random.nextFloat() * 0.2;
            level.addParticle(random.nextInt(3) > 0 ? ParticleTypes.LARGE_SMOKE : ParticleTypes.SMOKE,
                    pos.getX() + cx, pos.getY() + 0.5, pos.getZ() + cz, 0.0, 0.0, 0.0);
        }
    }

    /**
     * LOTRBlockForgeBase.randomDisplayTick: smoke and flame at the mouth of the
     * fire, half a block out along FACING, at a random height in the lower six
     * sixteenths and jittered sideways.
     *
     * <p>The LAVA particles are an ADDITION -- the original had none. They are
     * the sparks that pop off hot metal, and they arc and fall on their own, so
     * a lit forge throws a few rather than just glowing.
     */
    public static void flameFront(BlockState state, Level level, BlockPos pos, RandomSource random) {
        double x = pos.getX() + 0.5;
        double y = pos.getY();
        double z = pos.getZ() + 0.5;

        Direction facing = state.getValue(FACING);
        Direction.Axis axis = facing.getAxis();
        double edge = 0.52;
        double jitter = random.nextDouble() * 0.6 - 0.3;
        double dx = axis == Direction.Axis.X ? facing.getStepX() * edge : jitter;
        double dy = random.nextDouble() * 6.0 / 16.0;
        double dz = axis == Direction.Axis.Z ? facing.getStepZ() * edge : jitter;

        level.addParticle(ParticleTypes.SMOKE, x + dx, y + dy, z + dz, 0.0, 0.0, 0.0);
        level.addParticle(ParticleTypes.FLAME, x + dx, y + dy, z + dz, 0.0, 0.0, 0.0);

        // Sparks, thrown clear of the fire's mouth. Sparingly -- LAVA
        // particles are large and long-lived, so one in four ticks reads as a
        // working forge rather than an eruption.
        if (random.nextInt(4) == 0) {
            level.addParticle(ParticleTypes.LAVA, x + dx, y + dy, z + dz, 0.0, 0.0, 0.0);
        }
    }

    // Mirrors createFurnaceTicker's shape: server side only, and the ServerLevel
    // is captured so the tick method can take it directly.
    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState state,
                                                                            BlockEntityType<T> type) {
        if (level instanceof ServerLevel serverLevel) {
            return createTickerHelper(type, LOTRBlockEntities.FORGE,
                    (innerLevel, pos, blockState, entity) ->
                            LOTRForgeBlockEntity.serverTick(serverLevel, pos, blockState, entity));
        }
        return null;
    }
}