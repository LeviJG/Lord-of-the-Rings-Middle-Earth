package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRTreasurePileBlock;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import org.jspecify.annotations.Nullable;

/**
 * One of the two forms a treasure pile is carried in.
 *
 * <p>LOTRBlockTreasurePile.getSubBlocks offered exactly two: metadata 0, a
 * two-pixel scatter across the floor, and metadata 7, the full block. Both wore
 * the same name -- a "Copper Hoard" is a copper hoard whether it is ankle-deep
 * or waist-deep -- and they are two items here for the same reason they were
 * two metadata values there.
 *
 * <p>{@link #layers} is how deep this form places. Placing onto an existing pile
 * still adds to it: the block's own getStateForPlacement caps the total at
 * eight, so a full block dropped on a half-full pile simply fills it.
 */
public class LOTRTreasurePileItem extends BlockItem {

    private final int layers;

    public LOTRTreasurePileItem(Block block, int layers, Properties properties) {
        super(block, properties);
        this.layers = layers;
    }

    public int getLayers() {
        return this.layers;
    }

    @Override
    protected @Nullable BlockState getPlacementState(BlockPlaceContext context) {
        BlockState state = super.getPlacementState(context);
        if (state == null || !state.hasProperty(LOTRTreasurePileBlock.LAYERS)) {
            return state;
        }
        // super already folded this placement into any pile that was there, at
        // one layer; top it up to what this form is actually worth.
        BlockState existing = context.getLevel().getBlockState(context.getClickedPos());
        int already = existing.is(state.getBlock())
                ? existing.getValue(LOTRTreasurePileBlock.LAYERS)
                : 0;
        int total = Math.min(LOTRTreasurePileBlock.MAX_LAYERS, already + this.layers);
        return state.setValue(LOTRTreasurePileBlock.LAYERS, total);
    }
}
