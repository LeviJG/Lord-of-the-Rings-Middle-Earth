package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRHangingFruitBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

/**
 * LOTRItemHangingFruit: a banana or a date, which is food, and which hangs
 * itself back on the side of a log when used on one.
 *
 * <p>onItemUse: on the top or bottom of a log it does nothing, so the click
 * falls through to eating; on a side it hangs the fruit in the air beyond,
 * facing back at the log, and always takes the click.
 */
public class LOTRHangingFruitItem extends Item {
    private final Block fruitBlock;

    public LOTRHangingFruitItem(Block fruitBlock, Properties properties) {
        super(properties);
        this.fruitBlock = fruitBlock;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        if (!level.getBlockState(pos).is(BlockTags.LOGS)) {
            return InteractionResult.PASS;
        }
        Direction side = context.getClickedFace();
        if (side.getAxis() == Direction.Axis.Y) {
            return InteractionResult.PASS;
        }
        BlockPos target = pos.relative(side);
        if (level.isEmptyBlock(target)) {
            if (!level.isClientSide()) {
                level.setBlock(target, fruitBlock.defaultBlockState()
                        .setValue(LOTRHangingFruitBlock.FACING, side.getOpposite()), Block.UPDATE_ALL);
            }
            Player player = context.getPlayer();
            if (player == null || !player.hasInfiniteMaterials()) {
                context.getItemInHand().shrink(1);
            }
        }
        return InteractionResult.SUCCESS;
    }
}
