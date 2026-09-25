package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import java.util.function.Consumer;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.enchant.LOTRModifier;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import org.jspecify.annotations.Nullable;

/**
 * LOTRItemModifierTemplate: a scroll naming one modifier -- "Keen Smith's
 * Scroll", "Sturdy Smith's Scroll". The original had one for every beneficial
 * modifier that could be rolled (hasTemplateItem: weight above zero and
 * beneficial), and the anvil read the modifier off the scroll.
 *
 * <p>NOT ported: applying it. LOTR's anvil is not in the port, so a scroll
 * names its modifier and does nothing else yet.
 */
public class LOTRSmithsScrollItem extends Item implements LOTRTooltipItem {
    public LOTRSmithsScrollItem(Properties properties) {
        super(properties);
    }

    public static @Nullable LOTRModifier getModifier(ItemStack stack) {
        return stack.get(LOTRDataComponents.SCROLL_MODIFIER);
    }

    public static ItemStack of(LOTRModifier modifier) {
        ItemStack stack = new ItemStack(LOTRItems.SMITHS_SCROLL);
        stack.set(LOTRDataComponents.SCROLL_MODIFIER, modifier);
        return stack;
    }

    /** getItemStackDisplayName: "%s Smith's Scroll", filled in with the modifier. */
    @Override
    public Component getName(ItemStack stack) {
        LOTRModifier modifier = getModifier(stack);
        return modifier == null
                ? super.getName(stack)
                : Component.translatable("item.lotr.smiths_scroll.named",
                        Component.translatable(modifier.translationKey()));
    }

    @Override
    public void addTooltip(ItemStack stack, Item.TooltipContext context, Consumer<Component> builder, TooltipFlag flag) {
        LOTRModifier modifier = getModifier(stack);
        if (modifier != null) {
            // getNamedFormattedDescription: "Keen: +0.5 melee damage".
            builder.accept(Component.translatable("lotr.enchant.descFormat",
                            Component.translatable(modifier.translationKey()), modifier.description(false, true))
                    .withStyle(modifier.isBeneficial() ? ChatFormatting.GRAY : ChatFormatting.DARK_GRAY));
        }
    }
}
