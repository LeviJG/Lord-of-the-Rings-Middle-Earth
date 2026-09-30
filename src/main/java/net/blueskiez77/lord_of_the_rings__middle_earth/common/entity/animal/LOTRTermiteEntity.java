package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMiscItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRSpawnEggItem;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityTermite: a tiny walking bomb. It runs at its target and, once
 * within three blocks, hisses like a creeper and goes off a second later
 * (strength 2, breaking blocks only if mobGriefing allows). Its bite does
 * nothing. Killed by a player, it leaves an exploding termite to throw, and
 * vanishes at once instead of lying dead. It never despawns. Arthropod
 * ({@code minecraft:arthropod}); silverfish sounds.
 */
public class LOTRTermiteEntity extends Monster {

    public static final float EXPLOSION_SIZE = 2.0f;

    private int fuseTime;

    public LOTRTermiteEntity(EntityType<? extends LOTRTermiteEntity> type, Level level) {
        super(type, level);
        this.xpReward = 2;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 8.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new LOTRAttackOnCollideGoal(this, 1.0, true));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 0, true, false, null));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LOTRNPCEntity.class, 0, true, false, null));
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        LOTRSpawnEggItem.playHatchSound(this, reason);
        return super.finalizeSpawn(level, difficulty, reason, groupData);
    }

    /** attackEntityAsMob: the bite does nothing; the explosion is the attack. */
    @Override
    public boolean doHurtTarget(ServerLevel level, Entity target) {
        return true;
    }

    @Override
    public boolean removeWhenFarAway(double distSqr) {
        return false;
    }

    public void explode() {
        if (level() instanceof ServerLevel level) {
            level.explode(this, getX(), getY(), getZ(), EXPLOSION_SIZE, Level.ExplosionInteraction.MOB);
            discard();
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide()) {
            LivingEntity target = getTarget();
            if (target != null && distanceTo(target) < 3.0f) {
                if (this.fuseTime == 0) {
                    playSound(SoundEvents.CREEPER_PRIMED, 1.0f, 0.5f);
                }
                ++this.fuseTime;
                if (this.fuseTime >= 20) {
                    explode();
                }
            } else {
                --this.fuseTime;
            }
            this.fuseTime = Math.min(Math.max(this.fuseTime, 0), 20);
        }
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        if (level() instanceof ServerLevel level && source.getEntity() instanceof Player) {
            spawnAtLocation(level, new ItemStack(LOTRMiscItems.EXPLODING_TERMITE));
            discard();
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SILVERFISH_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.SILVERFISH_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SILVERFISH_DEATH;
    }
}
