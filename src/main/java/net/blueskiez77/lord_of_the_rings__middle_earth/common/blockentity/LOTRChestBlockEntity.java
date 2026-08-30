package net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * A vanilla chest under its own block entity type, so it can have its own
 * renderer, and under its own name.
 */
public class LOTRChestBlockEntity extends ChestBlockEntity {

    public LOTRChestBlockEntity(BlockPos pos, BlockState state) {
        super(LOTRBlockEntities.CHEST, pos, state);
    }

    /**
     * ChestBlockEntity's default is the flat "container.chest", so every one of
     * these would open a screen titled "Chest". Taking the name off the block
     * gives "Lebethron Casket", "Reed Basket", "Mallorn Box" and "Ancient
     * Haradric Chest" instead, with no container.* lang keys of their own.
     *
     * <p>This is only the DEFAULT. A chest renamed on an anvil still shows that
     * name: BlockItem copies the item's CUSTOM_NAME onto the block entity when
     * it is placed, and BaseContainerBlockEntity.getName prefers it over this.
     */
    @Override
    protected Component getDefaultName() {
        return getBlockState().getBlock().getName();
    }
}
