package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

/**
 * LOTREntityShirePony: a horse at four-fifths the size, weaker, slower and a
 * poorer jumper. The original let it be a donkey on the server (for a
 * donkey's lot) and a horse on the client (for a horse's coat), with horse
 * sounds forced back; it cannot carry a chest.
 *
 * <p>NOT ported yet: the rideShirePony achievement, for riding a saddled
 * pony carrying a chest -- which the pony cannot carry (D7).
 */
public class LOTRShirePonyEntity extends LOTRHorseEntity {

    /** PONY_SCALE: the hitbox (LOTREntities) and the renderer. */
    public static final float PONY_SCALE = 0.8f;

    public LOTRShirePonyEntity(EntityType<? extends LOTRShirePonyEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public double clampChildHealth(double health) {
        return Mth.clamp(health, 10.0, 28.0);
    }

    @Override
    public double clampChildJump(double jump) {
        return Mth.clamp(jump, 0.2, 1.0);
    }

    @Override
    public double clampChildSpeed(double speed) {
        return Mth.clamp(speed, 0.08, 0.3);
    }

    @Override
    protected void onLOTRHorseSpawn() {
        getAttribute(Attributes.MAX_HEALTH).setBaseValue(getAttributeValue(Attributes.MAX_HEALTH) * 0.75);
        getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(getAttributeValue(Attributes.JUMP_STRENGTH) * 0.5);
        getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(getAttribute(Attributes.MOVEMENT_SPEED).getBaseValue() * 0.8);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.HORSE_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.HORSE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.HORSE_DEATH;
    }

    @Override
    protected SoundEvent getAngrySound() {
        return SoundEvents.HORSE_ANGRY;
    }
}
