package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/**
 * morgulBlade: a plain LOTRItemSword of MORGUL steel, but LOTREventHandler's
 * onLivingHurt gave anything struck by a MORGUL-material weapon eight seconds
 * of wither -- and the blade is the only such weapon.
 */
public class LOTRMorgulBladeItem extends LOTRModifiableItem {

    /** new PotionEffect(Potion.wither.id, 160). */
    private static final int WITHER_TICKS = 160;

    public LOTRMorgulBladeItem(Properties properties) {
        super(properties);
    }

    @Override
    public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        super.hurtEnemy(stack, target, attacker);
        if (!target.level().isClientSide()) {
            target.addEffect(new MobEffectInstance(MobEffects.WITHER, WITHER_TICKS), attacker);
        }
    }
}
