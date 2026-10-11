package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import com.mojang.serialization.MapCodec;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRUtumnoReturnPortalBlockEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRWorldGen;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
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

import org.jspecify.annotations.Nullable;

/**
 * LOTRBlockUtumnoReturnPortal: the altar made the way out, once fifteen of Utumno's servants have
 * died by it. It may stand only in Utumno, and its column of light goes with it.
 */
public class LOTRUtumnoReturnPortalBlock extends BaseEntityBlock {
    public static final MapCodec<LOTRUtumnoReturnPortalBlock> CODEC = simpleCodec(LOTRUtumnoReturnPortalBlock::new);

    public LOTRUtumnoReturnPortalBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LOTRUtumnoReturnPortalBlockEntity(pos, state);
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
        if (level.dimension() != LOTRWorldGen.UTUMNO) {
            level.removeBlock(pos, false);
        }
    }

    /** breakBlock: the column of light above goes too. */
    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
        super.affectNeighborsAfterRemoval(state, level, pos, movedByPiston);
        for (int j1 = pos.getY(); j1 <= level.getMaxY(); ++j1) {
            BlockPos p = new BlockPos(pos.getX(), j1, pos.getZ());
            if (level.getBlockState(p).is(LOTRUtilityBlocks.UTUMNO_RETURN_LIGHT)) {
                level.removeBlock(p, false);
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return (BlockEntityTicker<T>) (BlockEntityTicker<LOTRUtumnoReturnPortalBlockEntity>) LOTRUtumnoReturnPortalBlockEntity::tick;
    }
}
