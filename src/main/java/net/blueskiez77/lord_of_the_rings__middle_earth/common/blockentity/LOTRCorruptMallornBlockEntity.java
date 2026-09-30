package net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRTileEntityCorruptMallorn: marks a corrupt mallorn for the Ents that
 * come looking for one to heal.
 *
 * <p>NOT ported yet: its updateEntity, which in Fangorn, one tick in forty,
 * spawned an Ent within 20 blocks if none was within 24 -- with the biomes
 * (D10).
 */
public class LOTRCorruptMallornBlockEntity extends BlockEntity {

    public LOTRCorruptMallornBlockEntity(BlockPos pos, BlockState state) {
        super(LOTRBlockEntities.CORRUPT_MALLORN, pos, state);
    }
}
