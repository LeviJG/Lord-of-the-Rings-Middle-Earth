package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.warg;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRWargBombardierAttackGoal;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityWargBombardier: a warg with an orc bomb strapped on its back,
 * sent running at the enemy to blow itself up. Nobody rides it and it
 * carries no saddle. It hisses as it picks a target; its fuse smokes once
 * lit.
 */
public abstract class LOTRWargBombardierEntity extends LOTRWargEntity {

    /** dataWatcher 21 and 22. */
    private static final EntityDataAccessor<Byte> DATA_FUSE =
            SynchedEntityData.defineId(LOTRWargBombardierEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Byte> DATA_STRENGTH =
            SynchedEntityData.defineId(LOTRWargBombardierEntity.class, EntityDataSerializers.BYTE);

    public static final int MAX_FUSE = 35;

    protected LOTRWargBombardierEntity(EntityType<? extends LOTRWargBombardierEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_FUSE, (byte) MAX_FUSE);
        builder.define(DATA_STRENGTH, (byte) 1);
    }

    public int getBombFuse() {
        return this.entityData.get(DATA_FUSE);
    }

    public void setBombFuse(int fuse) {
        this.entityData.set(DATA_FUSE, (byte) fuse);
    }

    public int getBombStrengthLevel() {
        return this.entityData.get(DATA_STRENGTH);
    }

    public void setBombStrengthLevel(int level) {
        this.entityData.set(DATA_STRENGTH, (byte) level);
    }

    @Override
    public boolean canWargBeRidden() {
        return false;
    }

    @Override
    public boolean isMountSaddled() {
        return false;
    }

    @Override
    protected Goal getWargAttackAI() {
        return new LOTRWargBombardierAttackGoal(this, 1.7);
    }

    @Override
    public void setTarget(@Nullable LivingEntity target, boolean speak) {
        super.setTarget(target, speak);
        if (target != null) {
            playSound(SoundEvents.TNT_PRIMED, 1.0f, 1.0f);
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (getBombFuse() < MAX_FUSE && level().isClientSide()) {
            level().addParticle(ParticleTypes.SMOKE, getX(), getY() + 2.2, getZ(), 0.0, 0.0, 0.0);
        }
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putByte("BombFuse", (byte) getBombFuse());
        output.putByte("BombStrengthLevel", (byte) getBombStrengthLevel());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        setBombFuse(input.getByteOr("BombFuse", (byte) MAX_FUSE));
        setBombStrengthLevel(input.getByteOr("BombStrengthLevel", (byte) 1));
    }
}
