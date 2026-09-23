package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import java.util.function.Consumer;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRBarrelBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

/**
 * LOTRItemBarrel: a barrel that remembers what it held.
 *
 * <p>The original kept the block entity's NBT under "LOTRBarrelData"; that is
 * the {@code lotr:barrel_data} component here. Placing the barrel loads it back
 * in, and the tooltip shows the same subtitle as the barrel's screen.
 *
 * <p>NOT ported: onItemRightClick, which set the barrel on water as a rideable
 * LOTREntityBarrel.
 */
public class LOTRBarrelItem extends BlockItem {
    public LOTRBarrelItem(Block block, Properties properties) {
        super(block, properties);
    }

    /** placeBlockAt -> loadBarrelDataToTE. */
    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos pos, Level level, @Nullable Player player, ItemStack stack,
            BlockState state) {
        boolean updated = super.updateCustomBlockEntityTag(pos, level, player, stack, state);
        CustomData data = stack.get(LOTRDataComponents.BARREL_DATA);
        if (data != null && !level.isClientSide() && level.getBlockEntity(pos) instanceof LOTRBarrelBlockEntity barrel) {
            barrel.loadFromTag(data.copyTag(), level.registryAccess());
            barrel.setChanged();
            return true;
        }
        return updated;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display,
            Consumer<Component> builder, TooltipFlag flag) {
        CustomData data = stack.get(LOTRDataComponents.BARREL_DATA);
        if (data == null || context.registries() == null) {
            return;
        }
        LOTRBarrelBlockEntity barrel = new LOTRBarrelBlockEntity(BlockPos.ZERO, getBlock().defaultBlockState());
        barrel.loadFromTag(data.copyTag(), context.registries());
        builder.accept(barrel.subtitle());
    }
}
