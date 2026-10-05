package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import java.util.function.Consumer;

import net.minecraft.network.chat.Component;
import net.minecraft.util.Unit;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

/**
 * LOTRItemHaradTurban: a Haradric turban, dyed like the robes, which may be
 * set with a gold ornament on its brow (a gold nugget at a Near Harad table);
 * its tooltip says so.
 */
public class LOTRHaradTurbanItem extends Item implements LOTRTooltipItem {

    public LOTRHaradTurbanItem(Properties properties) {
        super(properties);
    }

    public static boolean hasOrnament(ItemStack stack) {
        return stack.has(LOTRDataComponents.TURBAN_ORNAMENT);
    }

    public static ItemStack setHasOrnament(ItemStack stack, boolean ornament) {
        if (ornament) {
            stack.set(LOTRDataComponents.TURBAN_ORNAMENT, Unit.INSTANCE);
        } else {
            stack.remove(LOTRDataComponents.TURBAN_ORNAMENT);
        }
        return stack;
    }

    @Override
    public void addTooltip(ItemStack stack, Item.TooltipContext context, Consumer<Component> builder, TooltipFlag flag) {
        if (hasOrnament(stack)) {
            builder.accept(Component.translatable("item.lotr.haradRobes.ornament"));
        }
    }
}
