package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/**
 * LOTRItemDagger with DaggerEffect.POISON: a dagger whose cut festers.
 *
 * <p>applyStandardPoison scaled the dose with the world's difficulty --
 * {@code duration = 1 + difficultyId * 2}, then poison for
 * {@code (duration + rand(duration)) * 20} ticks. So on Easy that is 3 to 5
 * seconds, on Normal 5 to 9, and on Hard 7 to 13. Peaceful gives 1 to 1, which
 * is moot since poison cannot apply there anyway.
 */
public class LOTRPoisonedDaggerItem extends LOTRModifiableItem {

    public LOTRPoisonedDaggerItem(Properties properties) {
        super(properties);
    }

    /**
     * hitEntity: the original damaged the dagger by one and then poisoned what
     * it hit. Durability is vanilla's job now, so only the poison is left.
     *
     * <p>ONE hook, and it has to be this one. Player.itemAttackInteraction
     * calls ItemStack.hurtEnemy and then, if that returned true,
     * ItemStack.postHurtEnemy -- and it returns true exactly when the stack
     * carries a WEAPON component, which a dagger does. So overriding both fired
     * twice on every landed hit, each rolling its own duration, and since
     * MobEffectInstance keeps whichever is longer the dose came out as the
     * greater of two draws instead of the single draw applyStandardPoison made.
     * On Hard that pulled 7-13 seconds up towards 10-13.
     */
    @Override
    public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        super.hurtEnemy(stack, target, attacker);
        poison(target, attacker);
    }

    /**
     * applyStandardPoison, with the attacker named as the source so the poison's
     * kills are credited to whoever held the dagger rather than to nobody.
     */
    private static void poison(LivingEntity target, LivingEntity attacker) {
        if (target.level().isClientSide()) {
            return;
        }
        Difficulty difficulty = target.level().getDifficulty();
        int duration = 1 + difficulty.getId() * 2;
        int ticks = (duration + target.getRandom().nextInt(duration)) * 20;
        target.addEffect(new MobEffectInstance(MobEffects.POISON, ticks), attacker);
    }
}
