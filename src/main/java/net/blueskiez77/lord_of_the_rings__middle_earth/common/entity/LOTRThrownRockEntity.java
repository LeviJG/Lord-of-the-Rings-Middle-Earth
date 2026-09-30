package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRParticles;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.troll.LOTRMountainTrollEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.troll.LOTRTrollEntity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

/**
 * LOTREntityThrownRock: a boulder four blocks across hurled by a mountain
 * troll, falling at a tenth of a block per tick per tick and turning over
 * every three seconds. Whatever it strikes -- a creature, for the thrower's
 * rock damage, or the ground -- it smashes on: a burst of stone, one to three
 * cobblestones, a crash. A troll totem's rock ({@link #getSpawnsTroll}) leaves
 * a troll where it lands, one time in three a mountain troll that drops no
 * totem.
 */
public class LOTRThrownRockEntity extends ThrowableProjectile {

    private static final EntityDataAccessor<Boolean> DATA_SPAWNS_TROLL =
            SynchedEntityData.defineId(LOTRThrownRockEntity.class, EntityDataSerializers.BOOLEAN);
    /** handleHealthUpdate 15: the smash. */
    private static final byte EVENT_SMASH = 15;

    public int rockRotation;
    private float damage;

    public LOTRThrownRockEntity(EntityType<? extends LOTRThrownRockEntity> type, Level level) {
        super(type, level);
    }

    public LOTRThrownRockEntity(Level level, LivingEntity thrower) {
        this(LOTREntities.THROWN_ROCK, level);
        setOwner(thrower);
        setPos(thrower.getX(), thrower.getEyeY() - 0.1, thrower.getZ());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_SPAWNS_TROLL, false);
    }

    public boolean getSpawnsTroll() {
        return this.entityData.get(DATA_SPAWNS_TROLL);
    }

    public void setSpawnsTroll(boolean flag) {
        this.entityData.set(DATA_SPAWNS_TROLL, flag);
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    @Override
    protected double getDefaultGravity() {
        return 0.1;
    }

    @Override
    public void tick() {
        super.tick();
        ++this.rockRotation;
        if (this.rockRotation > 60) {
            this.rockRotation = 0;
        }
        setXRot(this.rockRotation / 60.0f * 360.0f);
        while (getXRot() - this.xRotO < -180.0f) {
            this.xRotO -= 360.0f;
        }
        while (getXRot() - this.xRotO >= 180.0f) {
            this.xRotO += 360.0f;
        }
    }

    @Override
    protected void onHit(HitResult hit) {
        if (!(level() instanceof ServerLevel level)) {
            return;
        }
        boolean smash = hit.getType() == HitResult.Type.BLOCK;
        if (hit instanceof EntityHitResult entityHit) {
            smash = entityHit.getEntity().hurtServer(level, damageSources().thrown(this, getOwner()), this.damage);
        }
        if (smash) {
            if (getSpawnsTroll()) {
                LOTRTrollEntity troll = this.random.nextInt(3) == 0
                        ? LOTREntities.MOUNTAIN_TROLL.create(level, EntitySpawnReason.TRIGGERED)
                        : LOTREntities.TROLL.create(level, EntitySpawnReason.TRIGGERED);
                if (troll != null) {
                    if (troll instanceof LOTRMountainTrollEntity mountainTroll) {
                        mountainTroll.setCanDropTrollTotem(false);
                    }
                    troll.snapTo(getX(), getY(), getZ(), this.random.nextFloat() * 360.0f, 0.0f);
                    troll.finalizeSpawn(level, level.getCurrentDifficultyAt(troll.blockPosition()),
                            EntitySpawnReason.TRIGGERED, null);
                    level.addFreshEntity(troll);
                }
            }
            level.broadcastEntityEvent(this, EVENT_SMASH);
            int drops = 1 + this.random.nextInt(3);
            for (int l = 0; l < drops; ++l) {
                spawnAtLocation(level, Blocks.COBBLESTONE);
            }
            playSound(LOTRSounds.TROLL_ROCK_SMASH, 2.0f,
                    (1.0f + (this.random.nextFloat() - this.random.nextFloat()) * 0.2f) * 0.7f);
            discard();
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == EVENT_SMASH) {
            for (int l = 0; l < 32; ++l) {
                level().addParticle(LOTRParticles.LARGE_STONE, getX() + this.random.nextGaussian() * getBbWidth(),
                        getY() + this.random.nextDouble() * getBbHeight(),
                        getZ() + this.random.nextGaussian() * getBbWidth(), 0.0, 0.0, 0.0);
            }
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putFloat("RockDamage", this.damage);
        output.putBoolean("Troll", getSpawnsTroll());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.damage = input.getFloatOr("RockDamage", 0.0f);
        setSpawnsTroll(input.getBooleanOr("Troll", false));
    }
}
