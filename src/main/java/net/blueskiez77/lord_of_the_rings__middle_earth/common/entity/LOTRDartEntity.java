package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRDartItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItems;

import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityDart: a blowgun dart, which barely hurts and is not meant to.
 *
 * <p>getBaseImpactDamage was {@code speed * dartDamageFactor} with the factor
 * pinned at a minimum of 1, so a dart hits for about its speed -- three at point
 * blank against an arrow's six. The point of it is the POISON: a poisoned dart
 * delivers applyStandardPoison, exactly as a poisoned dagger's cut does.
 *
 * <p>getKnockbackFactor was 0.5, so a dart shoves half as hard as an arrow.
 */
public class LOTRDartEntity extends AbstractArrow {

    /** dartDamageFactor, clamped to a minimum of one by the blowgun. */
    public static final double BASE_DAMAGE = 1.0;

    public LOTRDartEntity(EntityType<? extends LOTRDartEntity> type, Level level) {
        super(type, level);
        setBaseDamage(BASE_DAMAGE);
    }

    public LOTRDartEntity(EntityType<? extends LOTRDartEntity> type, LivingEntity shooter,
            Level level, ItemStack dart, @Nullable ItemStack blowgun) {
        super(type, shooter, level, dart, blowgun);
        setBaseDamage(BASE_DAMAGE);
    }

    public LOTRDartEntity(EntityType<? extends LOTRDartEntity> type, Level level,
            double x, double y, double z, ItemStack dart, @Nullable ItemStack blowgun) {
        super(type, x, y, z, level, dart, blowgun);
        setBaseDamage(BASE_DAMAGE);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(LOTRItems.TAURETHRIM_DART);
    }

    /**
     * onCollideWithTarget: a poisoned dart poisons, on the dagger's own dose --
     * {@code 1 + difficulty * 2} seconds plus a roll of the same again.
     */
    @Override
    protected void onHitEntity(EntityHitResult hit) {
        super.onHitEntity(hit);
        if (level().isClientSide()
                || !(getPickupItemStackOrigin().getItem() instanceof LOTRDartItem dart)
                || !dart.isPoisoned()
                || !(hit.getEntity() instanceof LivingEntity target)) {
            return;
        }
        Difficulty difficulty = level().getDifficulty();
        int duration = 1 + difficulty.getId() * 2;
        int ticks = (duration + target.getRandom().nextInt(duration)) * 20;
        target.addEffect(new MobEffectInstance(MobEffects.POISON, ticks),
                getOwner() instanceof LivingEntity shooter ? shooter : null);
    }
}
