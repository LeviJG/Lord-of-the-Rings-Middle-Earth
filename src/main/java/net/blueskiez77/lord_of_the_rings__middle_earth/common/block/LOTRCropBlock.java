package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FarmlandBlock;
import net.minecraft.world.level.block.state.BlockState;

// Vanilla CropBlock.mayPlaceOn tests state.is(Blocks.FARMLAND) -- the specific
// vanilla block, not a tag. So on mud farmland every LOTR crop would pop off the
// instant it was planted. This accepts any FarmlandBlock instead.
public class LOTRCropBlock extends CropBlock {

    // No CODEC or codec() override: CropBlock declares codec() with an exact
    // type this cannot narrow, and the inherited one is only used for datapack
    // block definitions, which this mod does not use.
    public LOTRCropBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.getBlock() instanceof FarmlandBlock;
    }
}