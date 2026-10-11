package net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRPortalBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRTileEntityPortalBase (with its LOTRTileEntityElvenPortal and LOTRTileEntityMorgulPortal): each
 * block of a built portal, there to be drawn and to see that its portal still stands -- it goes the
 * moment no portal around it is whole.
 */
public class LOTRPortalBlockEntity extends BlockEntity {

    public LOTRPortalBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public static LOTRPortalBlockEntity elven(BlockPos pos, BlockState state) {
        return new LOTRPortalBlockEntity(LOTRBlockEntities.ELVEN_PORTAL, pos, state);
    }

    public static LOTRPortalBlockEntity morgul(BlockPos pos, BlockState state) {
        return new LOTRPortalBlockEntity(LOTRBlockEntities.MORGUL_PORTAL, pos, state);
    }

    /** updateEntity: gone unless one of the nine centres about it is still a whole portal. */
    public static void serverTick(Level level, BlockPos pos, BlockState state, LOTRPortalBlockEntity portal) {
        if (!(state.getBlock() instanceof LOTRPortalBlock portalBlock)) {
            return;
        }
        for (int i1 = -1; i1 <= 1; ++i1) {
            for (int k1 = -1; k1 <= 1; ++k1) {
                if (portalBlock.isValidPortalLocation(level, pos.getX() + i1, pos.getY(), pos.getZ() + k1, true)) {
                    return;
                }
            }
        }
        level.removeBlock(pos, false);
    }
}
