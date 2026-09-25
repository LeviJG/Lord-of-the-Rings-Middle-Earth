package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;

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
 * LOTREntityArrowPoisoned: a plain arrow in every respect -- damage, drag,
 * pickup -- except that LOTREventHandler.onLivingHurt gave whatever it hurt
 * applyStandardPoison.
 */
public class LOTRPoisonedArrowEntity extends AbstractArrow {

    public LOTRPoisonedArrowEntity(EntityType<? extends LOTRPoisonedArrowEntity> type, Level level) {
        super(type, level);
    }

    public LOTRPoisonedArrowEntity(EntityType<? extends LOTRPoisonedArrowEntity> type, LivingEntity shooter,
            Level level, ItemStack arrow, @Nullable ItemStack bow) {
        super(type, shooter, level, arrow, bow);
    }

    public LOTRPoisonedArrowEntity(EntityType<? extends LOTRPoisonedArrowEntity> type, Level level,
            double x, double y, double z, ItemStack arrow, @Nullable ItemStack bow) {
        super(type, x, y, z, level, arrow, bow);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(LOTRCombatItems.POISONED_ARROW);
    }

    @Override
    protected void onHitEntity(EntityHitResult hit) {
        super.onHitEntity(hit);
        if (!level().isClientSide() && hit.getEntity() instanceof LivingEntity target) {
            poison(target);
        }
    }

    /** applyStandardPoison: {@code 1 + difficulty * 2} seconds, plus a roll of the same again. */
    private void poison(LivingEntity target) {
        Difficulty difficulty = level().getDifficulty();
        int duration = 1 + difficulty.getId() * 2;
        int ticks = (duration + target.getRandom().nextInt(duration)) * 20;
        target.addEffect(new MobEffectInstance(MobEffects.POISON, ticks),
                getOwner() instanceof LivingEntity shooter ? shooter : null);
    }
}
