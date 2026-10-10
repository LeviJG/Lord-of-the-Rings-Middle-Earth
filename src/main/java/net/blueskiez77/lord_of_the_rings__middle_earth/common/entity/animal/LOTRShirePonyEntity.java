package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRPlayerAchievements;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * LOTREntityShirePony: a horse at four-fifths the size, weaker, slower and a
 * poorer jumper. The original let it be a donkey on the server (for a
 * donkey's lot: it carries a chest, LOTRChestedHorseEntity) and a horse on
 * the client (for a horse's coat, so the chest is not drawn), with horse
 * sounds forced back.
 *
 * <p>Riding a saddled one carrying a chest earns rideShirePony.
 */
public class LOTRShirePonyEntity extends LOTRChestedHorseEntity {

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

    @Override
    public void aiStep() {
        super.aiStep();
        if (level() instanceof ServerLevel && getFirstPassenger() instanceof Player player && isMountSaddled() && hasChest()) {
            LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.RIDE_SHIRE_PONY);
        }
    }
}
