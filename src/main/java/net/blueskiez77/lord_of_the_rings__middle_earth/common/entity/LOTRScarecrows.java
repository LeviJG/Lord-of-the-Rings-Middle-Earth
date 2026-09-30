package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRScarecrows: a fence post with a pumpkin or a head on it -- somewhere two
 * to six blocks up, resting on a solid block -- keeps crop-thieving birds away
 * from anything within 16 blocks across and 8 up or down.
 */
public final class LOTRScarecrows {

    public static final int RANGE = 16;
    public static final int Y_RANGE = 8;

    private LOTRScarecrows() {
    }

    public static boolean anyScarecrowsNearby(BlockGetter level, BlockPos pos) {
        BlockPos.MutableBlockPos p = new BlockPos.MutableBlockPos();
        for (int i = pos.getX() - RANGE; i <= pos.getX() + RANGE; ++i) {
            for (int k = pos.getZ() - RANGE; k <= pos.getZ() + RANGE; ++k) {
                for (int j = pos.getY() - Y_RANGE; j <= pos.getY() + Y_RANGE; ++j) {
                    if (isScarecrowBase(level, p.set(i, j, k))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /** isScarecrowBase: a fence, with a pumpkin (BlockPumpkin, lit or not) or a skull on a solid block above. */
    public static boolean isScarecrowBase(BlockGetter level, BlockPos pos) {
        if (!(level.getBlockState(pos).getBlock() instanceof FenceBlock)) {
            return false;
        }
        for (int j = 2; j <= 6; ++j) {
            BlockState above = level.getBlockState(pos.above(j));
            boolean head = above.is(Blocks.CARVED_PUMPKIN) || above.is(Blocks.JACK_O_LANTERN) || above.is(Blocks.PUMPKIN)
                    || above.getBlock() instanceof AbstractSkullBlock;
            BlockPos under = pos.above(j - 1);
            if (head && level.getBlockState(under).isRedstoneConductor(level, under)) {
                return true;
            }
        }
        return false;
    }
}
