package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.ent;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRFollowHiringPlayerGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRHiredRemainStillGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRHuornTargetGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCAttributes;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.pathfinder.PathType;

/**
 * LOTREntityHuornBase: a tree that is more than a tree -- 60 strong, hitting
 * for 4, quick when roused, seeing 24 blocks. At rest it is a tree, standing
 * square on its block and not to be shoved about; it wakes when struck, or
 * while it has somewhere to go or someone to kill. It never drowns, sounds
 * like breaking wood, and looks for victims only now and then
 * (LOTRHuornTargetGoal). It does not wander.
 */
public abstract class LOTRHuornBaseEntity extends LOTRTreeEntity {

    private static final EntityDataAccessor<Boolean> DATA_ACTIVE =
            SynchedEntityData.defineId(LOTRHuornBaseEntity.class, EntityDataSerializers.BOOLEAN);

    protected LOTRHuornBaseEntity(EntityType<? extends LOTRHuornBaseEntity> type, Level level) {
        super(type, level);
        setPathfindingMalus(PathType.WATER, -1.0f);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createNPCAttributes()
                .add(Attributes.MAX_HEALTH, 60.0)
                .add(Attributes.FOLLOW_RANGE, 24.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(LOTRNPCAttributes.NPC_ATTACK_DAMAGE, 4.0);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ACTIVE, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new LOTRHiredRemainStillGoal(this));
        this.goalSelector.addGoal(2, new LOTRAttackOnCollideGoal(this, 1.5, false));
        this.goalSelector.addGoal(3, new LOTRFollowHiringPlayerGoal(this));
        addTargetTasks(true, LOTRHuornTargetGoal::forPlayers, LOTRHuornTargetGoal::forFactions);
    }

    public boolean isHuornActive() {
        return this.entityData.get(DATA_ACTIVE);
    }

    public void setHuornActive(boolean flag) {
        this.entityData.set(DATA_ACTIVE, flag);
    }

    /** applyEntityCollision and collideWithEntity: a sleeping huorn is not moved by what bumps it. */
    @Override
    public void push(double x, double y, double z) {
        if (isHuornActive()) {
            super.push(x, y, z);
        }
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        boolean flag = super.hurtServer(level, source, damage);
        if (flag && !isHuornActive()) {
            setHuornActive(true);
        }
        return flag;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!level().isClientSide()) {
            boolean active = !getNavigation().isDone() || getTarget() != null && getTarget().isAlive();
            setHuornActive(active);
        }
    }

    /** decreaseAirSupply: it does not drown. */
    @Override
    protected int decreaseAirSupply(int air) {
        return air;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundType.WOOD.getBreakSound();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundType.WOOD.getBreakSound();
    }

    @Override
    public float getVoicePitch() {
        return super.getVoicePitch() * 0.75f;
    }
}
