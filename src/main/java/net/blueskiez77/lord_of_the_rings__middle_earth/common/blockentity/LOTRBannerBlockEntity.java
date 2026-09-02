package net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBannerBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBannerType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRWallBannerBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * A faction banner's block entity, which stores nothing at all.
 *
 * <p>It exists only because vanilla's AbstractBannerBlock is a BaseEntityBlock
 * and a block entity is what gets a block a renderer. Which banner this is
 * comes from the block itself, so there is no state to save -- unlike a vanilla
 * banner, whose patterns and name have to live somewhere.
 *
 * <p>When banner territory protection is ported, the owning player and
 * fellowship data that LOTREntityBanner carried belong here.
 */
public class LOTRBannerBlockEntity extends BlockEntity {

    public LOTRBannerBlockEntity(BlockPos pos, BlockState state) {
        super(LOTRBlockEntities.BANNER, pos, state);
    }

    /** Which banner to draw, read back off the block. */
    public LOTRBannerType getBannerType() {
        BlockState state = getBlockState();
        if (state.getBlock() instanceof LOTRBannerBlock standing) {
            return standing.getBannerType();
        }
        if (state.getBlock() instanceof LOTRWallBannerBlock wall) {
            return wall.getBannerType();
        }
        return LOTRBannerType.GONDOR;
    }
}
