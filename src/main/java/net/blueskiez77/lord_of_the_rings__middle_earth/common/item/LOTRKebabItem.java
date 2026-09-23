package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * LOTRItemKebab: food, with one eating in a hundred telling you so. The line is
 * the original's literal chat text, not a translation key.
 */
public class LOTRKebabItem extends Item {
    public LOTRKebabItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!level.isClientSide() && entity instanceof Player player && level.getRandom().nextInt(100) == 0) {
            player.sendSystemMessage(Component.literal("That was a good kebab. You feel a lot better."));
        }
        return super.finishUsingItem(stack, level, entity);
    }
}
