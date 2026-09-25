package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import java.util.function.Consumer;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

/**
 * LOTRItemLeatherHat: a brimmed hat, dyed any colour (DYED_COLOR, the undyed
 * leather brown otherwise) and with an optional feather in its band
 * (HAT_FEATHER). The tooltip says which.
 */
public class LOTRLeatherHatItem extends Item implements LOTRTooltipItem {

    /** HAT_LEATHER, the colour of an undyed hat. */
    public static final int HAT_LEATHER = 0x684A36;
    /** FEATHER_WHITE, a plain feather. */
    public static final int FEATHER_WHITE = 0xFFFFFF;

    public LOTRLeatherHatItem(Properties properties) {
        super(properties);
    }

    public static boolean hasFeather(ItemStack stack) {
        return stack.has(LOTRDataComponents.HAT_FEATHER);
    }

    /** isFeatherDyed: a feather that is not plain white. */
    public static boolean isFeatherDyed(ItemStack stack) {
        Integer feather = stack.get(LOTRDataComponents.HAT_FEATHER);
        return feather != null && feather != FEATHER_WHITE;
    }

    @Override
    public void addTooltip(ItemStack stack, Item.TooltipContext context, Consumer<Component> builder, TooltipFlag flag) {
        if (stack.has(DataComponents.DYED_COLOR)) {
            builder.accept(Component.translatable("item.lotr.hat.dyed"));
        }
        if (hasFeather(stack)) {
            builder.accept(Component.translatable("item.lotr.hat.feathered"));
        }
    }
}
