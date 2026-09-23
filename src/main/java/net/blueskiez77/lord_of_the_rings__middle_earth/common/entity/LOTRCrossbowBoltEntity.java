package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCrossbowBoltItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItems;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.phys.EntityHitResult;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityCrossbowBolt: the short, heavy shaft a crossbow throws.
 *
 * <p>Nothing to it but the weight. getBaseImpactDamage was
 * {@code speed * 2.0 * boltDamageFactor} with the factor sitting at 2, so a
 * bolt hits for four times its speed where an arrow manages two -- twice an
 * arrow, which is the whole point of a weapon that takes a second and a quarter
 * to reload. AbstractArrow multiplies base damage by speed itself, so the
 * figure goes in as base damage and vanilla's arithmetic does the rest.
 *
 * <p>Everything else -- the 0.99 drag, the 0.05 of gravity, sticking in blocks,
 * the seven-tick shake, being picked back up, the 1200-tick despawn -- is what
 * LOTREntityProjectileBase hand-wrote and what AbstractArrow already does.
 *
 * <p>A POISONED bolt -- crossbowBoltPoisoned -- poisons what it hits, which was
 * onCollideWithTarget's job. Whether a bolt is poisoned is synced to clients so
 * the renderer can pick the bottom half of the original's bolt sheet.
 */
public class LOTRCrossbowBoltEntity extends AbstractArrow {

    /** BOLT_RELATIVE_TO_ARROW * boltDamageFactor: 2 x 2, twice an arrow's 2.0. */
    public static final double BASE_DAMAGE = 4.0;

    private static final EntityDataAccessor<Boolean> POISONED =
            SynchedEntityData.defineId(LOTRCrossbowBoltEntity.class, EntityDataSerializers.BOOLEAN);

    public LOTRCrossbowBoltEntity(EntityType<? extends LOTRCrossbowBoltEntity> type, Level level) {
        super(type, level);
        setBaseDamage(BASE_DAMAGE);
    }

    public LOTRCrossbowBoltEntity(EntityType<? extends LOTRCrossbowBoltEntity> type,
            LivingEntity shooter, Level level, ItemStack bolt, @Nullable ItemStack crossbow) {
        super(type, shooter, level, bolt, crossbow);
        setBaseDamage(BASE_DAMAGE);
        updatePoisoned();
    }

    /** The form a dispenser uses: a bolt at a position, with nobody behind it. */
    public LOTRCrossbowBoltEntity(EntityType<? extends LOTRCrossbowBoltEntity> type, Level level,
            double x, double y, double z, ItemStack bolt, @Nullable ItemStack crossbow) {
        super(type, x, y, z, level, bolt, crossbow);
        setBaseDamage(BASE_DAMAGE);
        updatePoisoned();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(POISONED, false);
    }

    private void updatePoisoned() {
        this.entityData.set(POISONED, getPickupItemStackOrigin().getItem() instanceof LOTRCrossbowBoltItem bolt
                && bolt.isPoisoned());
    }

    public boolean isPoisoned() {
        return this.entityData.get(POISONED);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        updatePoisoned();
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(LOTRItems.CROSSBOW_BOLT);
    }

    @Override
    protected void onHitEntity(EntityHitResult hit) {
        super.onHitEntity(hit);
        if (!level().isClientSide() && isPoisoned() && hit.getEntity() instanceof LivingEntity target) {
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
