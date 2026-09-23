package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.RailBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;

/**
 * LOTRItemMechanism: a cog and a flint, which turns a plain rail under it into
 * a mechanised one.
 *
 * <p>onItemUse only took metadata 0 to 5 -- the straight and ascending rails,
 * never the four curves -- because the mechanised rail, like a powered one,
 * has no curved shape to put them in.
 */
public class LOTRMechanismItem extends Item {
    public LOTRMechanismItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        if (!state.is(net.minecraft.world.level.block.Blocks.RAIL)) {
            return InteractionResult.PASS;
        }
        RailShape shape = state.getValue(RailBlock.SHAPE);
        if (shape != RailShape.NORTH_SOUTH && shape != RailShape.EAST_WEST && !shape.isSlope()) {
            return InteractionResult.PASS;
        }
        if (!level.isClientSide()) {
            // The original set the powered bit as it placed the rail.
            BlockState mechanised = LOTRBlocks.MECHANISED_RAIL.defaultBlockState()
                    .setValue(PoweredRailBlock.SHAPE, shape)
                    .setValue(PoweredRailBlock.POWERED, true);
            level.setBlock(pos, mechanised, Block.UPDATE_ALL);
            SoundType sound = mechanised.getSoundType();
            level.playSound(null, pos, sound.getPlaceSound(), SoundSource.BLOCKS,
                    (sound.getVolume() + 1.0f) / 2.0f, sound.getPitch() * 0.8f);
            Player player = context.getPlayer();
            context.getItemInHand().consume(1, player);
        }
        return InteractionResult.SUCCESS;
    }
}
