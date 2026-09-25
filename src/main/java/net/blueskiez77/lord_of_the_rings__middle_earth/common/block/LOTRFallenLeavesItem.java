package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PlaceOnWaterBlockItem;
import net.minecraft.world.level.block.Block;

/**
 * LOTRItemFallenLeaves: set down on the ground or on still water
 * (LOTRItemWaterPlant.tryPlaceWaterPlant), and named after its leaf.
 */
public class LOTRFallenLeavesItem extends PlaceOnWaterBlockItem {

    public LOTRFallenLeavesItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public Component getName(ItemStack stack) {
        return ((LOTRFallenLeavesBlock) getBlock()).displayName();
    }
}
