package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import com.mojang.serialization.MapCodec;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRUtumnoPortalBlockEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRWorldGen;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;

import org.jspecify.annotations.Nullable;

/**
 * LOTRBlockUtumnoPortal: the darkness at the bottom of the entrance shaft. Whoever falls into it is
 * slowed as in a web and taken down into Utumno; it may lie only in Middle-earth.
 */
public class LOTRUtumnoPortalBlock extends BaseEntityBlock {
    public static final MapCodec<LOTRUtumnoPortalBlock> CODEC = simpleCodec(LOTRUtumnoPortalBlock::new);

    public LOTRUtumnoPortalBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LOTRUtumnoPortalBlockEntity(pos, state);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
        return ItemStack.EMPTY;
    }

    @Override
    protected boolean canBeReplaced(BlockState state, Fluid fluid) {
        return false;
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if (level.dimension() != LOTRWorldGen.MIDDLE_EARTH) {
            level.removeBlock(pos, false);
        }
    }

    /** onEntityCollidedWithBlock: setInWeb, then down into Utumno. */
    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier,
                                boolean isPrecise) {
        entity.makeStuckInBlock(state, new Vec3(0.25, 0.05f, 0.25));
        if (level.getBlockEntity(pos) instanceof LOTRUtumnoPortalBlockEntity portal) {
            portal.transferEntity(entity);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return (BlockEntityTicker<T>) (BlockEntityTicker<LOTRUtumnoPortalBlockEntity>) LOTRUtumnoPortalBlockEntity::tick;
    }
}
