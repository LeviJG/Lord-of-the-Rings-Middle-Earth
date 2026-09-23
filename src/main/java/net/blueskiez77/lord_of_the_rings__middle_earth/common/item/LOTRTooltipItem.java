package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import java.util.function.Consumer;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

/**
 * An item with its own tooltip lines -- the original's addInformation.
 * Item.appendHoverText is deprecated in 26.2, so the client's
 * ItemTooltipCallback asks these items for their lines and puts them under
 * the item name, where appendHoverText would have.
 */
public interface LOTRTooltipItem {
    void addTooltip(ItemStack stack, Item.TooltipContext context, Consumer<Component> builder, TooltipFlag flag);
}
