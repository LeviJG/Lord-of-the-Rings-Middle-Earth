package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal;

import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRAttackRules;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/**
 * LOTREntityRhino: a hostile mount (4 attack), bred on wheat, half again as
 * tough (at least 40 health), faster, a poor jumper, with rhino barding.
 * Ridden flat out it charges: anything in its path takes fifteen times its
 * speed in damage, is flung, and -- if it had been after the rhino -- turns
 * on the rider instead; an Olog-hai's hammer sounds as it strikes. Drops are
 * {@code lotr:entities/rhino}: horns and rhino meat.
 */
public class LOTRRhinoEntity extends LOTRHorseEntity {

    public LOTRRhinoEntity(EntityType<? extends LOTRRhinoEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createHostileAttributes(4.0);
    }

    @Override
    public boolean isMountHostile() {
        return true;
    }

    @Override
    public boolean isLotrBreedingItem(ItemStack stack) {
        return stack.is(Items.WHEAT);
    }

    @Override
    public double clampChildHealth(double health) {
        return Mth.clamp(health, 20.0, 50.0);
    }

    @Override
    public double clampChildJump(double jump) {
        return Mth.clamp(jump, 0.2, 0.8);
    }

    @Override
    public double clampChildSpeed(double speed) {
        return Mth.clamp(speed, 0.12, 0.42);
    }

    @Override
    protected void onLOTRHorseSpawn() {
        getAttribute(Attributes.MAX_HEALTH).setBaseValue(Math.max(getAttributeValue(Attributes.MAX_HEALTH) * 1.5, 40.0));
        getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(getAttributeValue(Attributes.MOVEMENT_SPEED) * 1.2);
        getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(getAttributeValue(Attributes.JUMP_STRENGTH) * 0.5);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!(level() instanceof ServerLevel level)) {
            return;
        }
        if (getFirstPassenger() instanceof LivingEntity rider) {
            float momentum = momentum();
            setSprinting(momentum > 0.2f);
            if (momentum >= 0.32f) {
                charge(level, rider, momentum);
            }
        } else if (getTarget() != null) {
            setSprinting(momentum() > 0.2f);
        } else {
            setSprinting(false);
        }
    }

    private void charge(ServerLevel level, LivingEntity rider, float momentum) {
        float strength = momentum * 15.0f;
        Vec3 look = getLookAngle();
        double range = 0.5;
        AABB box = getBoundingBox().deflate(1.0).expandTowards(look.x * range, look.y * range, look.z * range).inflate(1.0);
        List<Entity> list = level.getEntities(this, box);
        boolean hitAny = false;
        for (Entity obj : list) {
            if (!(obj instanceof LivingEntity entity) || entity == rider
                    || !LOTRAttackRules.riderCanAttack(rider, entity)
                    || !entity.hurtServer(level, damageSources().mobAttack(this), strength)) {
                continue;
            }
            float knockback = strength * 0.05f;
            float yaw = getYRot() * Mth.DEG_TO_RAD;
            entity.push(-Mth.sin(yaw) * knockback, knockback, Mth.cos(yaw) * knockback);
            hitAny = true;
            if (entity instanceof Mob mob && mob.getTarget() == this) {
                mob.getNavigation().stop();
                mob.setTarget(rider);
            }
        }
        if (hitAny) {
            playSound(LOTRSounds.TROLL_OLOG_HAI_HAMMER, 1.0f, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return LOTRSounds.RHINO_SAY;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return LOTRSounds.RHINO_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return LOTRSounds.RHINO_DEATH;
    }

    @Override
    protected SoundEvent getAngrySound() {
        return LOTRSounds.RHINO_SAY;
    }
}
