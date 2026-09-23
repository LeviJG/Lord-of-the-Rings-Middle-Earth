package net.blueskiez77.lord_of_the_rings__middle_earth.common.effect;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRDamageTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

/**
 * LOTRPotionPoisonKilling: what a bottle of poison does to whoever drinks it.
 *
 * <p>Poison's colour, but unlike vanilla poison it does not stop at half a
 * heart: one point of armour-bypassing magic damage every {@code 5 >> level}
 * ticks, until it has run its course or you have.
 */
public class LOTRDrinkPoisonMobEffect extends MobEffect {
    public LOTRDrinkPoisonMobEffect() {
        // Potion.poison.getLiquidColor()
        super(MobEffectCategory.HARMFUL, 8889187);
    }

    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity mob, int amplification) {
        mob.hurtServer(level, LOTRDamageTypes.poisonDrink(level), 1.0F);
        return true;
    }

    /** isReady: {@code freq = 5 >> level}, every tick once that reaches zero. */
    @Override
    public boolean shouldApplyEffectTickThisTick(int tickCount, int amplification) {
        int freq = 5 >> amplification;
        return freq == 0 || tickCount % freq == 0;
    }
}
