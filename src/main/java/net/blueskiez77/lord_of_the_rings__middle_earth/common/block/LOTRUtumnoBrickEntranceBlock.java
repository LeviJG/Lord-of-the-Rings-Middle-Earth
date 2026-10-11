package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRBlockUtumnoBrickEntrance: the unbreakable ice brick of Utumno's entrance shaft, looking and
 * dropping (should it ever be broken) as ice Utumno brick.
 */
public class LOTRUtumnoBrickEntranceBlock extends Block {
    public static final MapCodec<LOTRUtumnoBrickEntranceBlock> CODEC = simpleCodec(LOTRUtumnoBrickEntranceBlock::new);

    public LOTRUtumnoBrickEntranceBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
        return new ItemStack(LOTRBuildingBlocks.ICE_UTUMNO_BRICK);
    }
}
