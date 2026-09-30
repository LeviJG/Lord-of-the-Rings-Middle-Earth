package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityAIAvoidWithChance: EntityAIAvoidEntity that only starts on a
 * roll of {@code chance} per check, optionally with a sound as it bolts.
 */
public class LOTRAvoidWithChanceGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {

    private final float chance;
    private final @Nullable SoundEvent sound;

    public LOTRAvoidWithChanceGoal(PathfinderMob mob, Class<T> avoidClass, float maxDist,
                                   double walkSpeed, double sprintSpeed, float chance) {
        this(mob, avoidClass, maxDist, walkSpeed, sprintSpeed, chance, null);
    }

    public LOTRAvoidWithChanceGoal(PathfinderMob mob, Class<T> avoidClass, float maxDist,
                                   double walkSpeed, double sprintSpeed, float chance,
                                   @Nullable SoundEvent sound) {
        super(mob, avoidClass, maxDist, walkSpeed, sprintSpeed);
        this.chance = chance;
        this.sound = sound;
    }

    @Override
    public boolean canUse() {
        return this.mob.getRandom().nextFloat() < this.chance && super.canUse();
    }

    @Override
    public void start() {
        super.start();
        if (this.sound != null) {
            this.mob.playSound(this.sound, 0.5f,
                    (this.mob.getRandom().nextFloat() - this.mob.getRandom().nextFloat()) * 0.2f + 1.0f);
        }
    }
}
