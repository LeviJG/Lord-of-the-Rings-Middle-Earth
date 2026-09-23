package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/** LOTRItemBerry.setPoisonous: wildberries feed you, then poison you for three to six seconds. */
public class LOTRPoisonousBerryItem extends Item {

    public LOTRPoisonousBerryItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        ItemStack result = super.finishUsingItem(stack, level, entity);
        if (!level.isClientSide()) {
            entity.addEffect(new MobEffectInstance(MobEffects.POISON, (3 + level.getRandom().nextInt(4)) * 20));
        }
        return result;
    }
}
