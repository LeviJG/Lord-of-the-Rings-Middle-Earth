package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import com.mojang.serialization.MapCodec;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory.LOTRAnvilMenu;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * The LOTR anvil: vanilla's anvil in every way -- it falls, it lands with a
 * clang, it faces the way it was set down -- except that it opens
 * LOTRContainerAnvil, which in the original took over the vanilla anvil's
 * screen. It never wears: the LOTR anvil did no damage to the block.
 */
public class LOTRAnvilBlock extends AnvilBlock {
    public static final MapCodec<LOTRAnvilBlock> CODEC = simpleCodec(LOTRAnvilBlock::new);

    public LOTRAnvilBlock(Properties properties) {
        super(properties);
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public MapCodec<AnvilBlock> codec() {
        return (MapCodec) CODEC;
    }

    @Override
    protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return new SimpleMenuProvider((containerId, inventory, player) -> new LOTRAnvilMenu(
                containerId, inventory, ContainerLevelAccess.create(level, pos)),
                Component.translatable("container.lotr.anvil"));
    }
}
