package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

/**
 * LOTREntitySmokeRing: the ring a smoking pipe puffs out. It drifts off along
 * the smoker's look at a tenth of a block a tick, weightless, growing and
 * fading over fifteen seconds, and is gone at the first thing it touches or in
 * water. The colour is the pipe's: 0-15 the dye colours, 16 the magic smoke.
 */
public class LOTRSmokeRingEntity extends ThrowableProjectile {

    /** MAX_AGE, in ticks. */
    public static final int MAX_AGE = 300;

    /** func_70182_d: the speed it leaves the pipe at. */
    public static final float SPEED = 0.1f;

    private static final EntityDataAccessor<Integer> SMOKE_AGE =
            SynchedEntityData.defineId(LOTRSmokeRingEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Byte> SMOKE_COLOUR =
            SynchedEntityData.defineId(LOTRSmokeRingEntity.class, EntityDataSerializers.BYTE);

    public LOTRSmokeRingEntity(EntityType<? extends LOTRSmokeRingEntity> type, Level level) {
        super(type, level);
    }

    /** EntityThrowable(world, thrower): from the eyes, a touch below. */
    public LOTRSmokeRingEntity(Level level, LivingEntity smoker) {
        super(LOTREntities.SMOKE_RING, smoker.getX(), smoker.getEyeY() - 0.1, smoker.getZ(), level);
        setOwner(smoker);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(SMOKE_AGE, 0);
        builder.define(SMOKE_COLOUR, (byte) 0);
    }

    public int getSmokeAge() {
        return this.entityData.get(SMOKE_AGE);
    }

    public int getSmokeColour() {
        return this.entityData.get(SMOKE_COLOUR);
    }

    public LOTRSmokeRingEntity setSmokeColour(int colour) {
        this.entityData.set(SMOKE_COLOUR, (byte) colour);
        return this;
    }

    /** getGravityVelocity: none. */
    @Override
    protected double getDefaultGravity() {
        return 0.0;
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide()) {
            return;
        }
        if (isInWater()) {
            discard();
            return;
        }
        int age = getSmokeAge() + 1;
        this.entityData.set(SMOKE_AGE, age);
        if (age >= MAX_AGE) {
            discard();
        }
    }

    /** onImpact: anything but the smoker puts it out. */
    @Override
    protected void onHit(HitResult hit) {
        if (hit instanceof EntityHitResult entityHit && entityHit.getEntity() == getOwner()) {
            return;
        }
        super.onHit(hit);
        if (!level().isClientSide()) {
            discard();
        }
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("SmokeAge", getSmokeAge());
        output.putInt("SmokeColour", getSmokeColour());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.entityData.set(SMOKE_AGE, input.getIntOr("SmokeAge", 0));
        this.entityData.set(SMOKE_COLOUR, (byte) input.getIntOr("SmokeColour", 0));
    }
}
