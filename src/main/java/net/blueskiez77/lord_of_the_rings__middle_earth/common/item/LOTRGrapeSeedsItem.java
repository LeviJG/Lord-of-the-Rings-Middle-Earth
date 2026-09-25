package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRGrapevineBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

/**
 * LOTRItemGrapeSeeds. Grapes are not planted in the ground: the seeds go onto a
 * bare grapevine post, and only one with farmland at most three blocks below it
 * through other posts (LOTRBlockGrapevine.canPlantGrapesAt). Anywhere else they
 * do nothing.
 *
 * <p>Still a BlockItem, so the vine's asItem() and pick-block give the seeds,
 * as the original's getItem did; only useOn is replaced.
 */
public class LOTRGrapeSeedsItem extends BlockItem {

    public LOTRGrapeSeedsItem(Block vine, Properties properties) {
        super(vine, properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        if (context.getPlayer() != null && !context.getPlayer().mayUseItemAt(pos, context.getClickedFace(), context.getItemInHand())) {
            return InteractionResult.FAIL;
        }
        if (!level.getBlockState(pos).is(LOTRBlocks.GRAPEVINE) || !LOTRGrapevineBlock.canPlantGrapesAt(level, pos)) {
            return InteractionResult.PASS;
        }
        if (!level.isClientSide()) {
            level.setBlock(pos, getBlock().defaultBlockState(), Block.UPDATE_ALL);
            context.getItemInHand().consume(1, context.getPlayer());
        }
        return InteractionResult.SUCCESS;
    }
}
