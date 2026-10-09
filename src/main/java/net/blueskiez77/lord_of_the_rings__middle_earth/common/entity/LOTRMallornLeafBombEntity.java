package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity;

import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRLeafParticleOptions;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRParticles;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRDecorationBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;

import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityMallornLeafBomb: the Mallorn Ent's whirl of leaves, two blocks
 * across, flying straight and weightless at a block a tick. It has no model:
 * it is drawn only as a spinning ring of mallorn-leaf fragments. Striking the
 * ground, anything it may hurt, or ten seconds out, it bursts, hurting
 * everything within two blocks that its thrower may attack for its damage
 * over the distance (at most the whole of it). The thrower is never hurt, and
 * jumps as the burst is reckoned.
 */
public class LOTRMallornLeafBombEntity extends ThrowableProjectile {

    public static final int MAX_LEAVES_AGE = 200;

    public int leavesAge;
    public float leavesDamage;

    public LOTRMallornLeafBombEntity(EntityType<? extends LOTRMallornLeafBombEntity> type, Level level) {
        super(type, level);
    }

    /** Thrown from just below the thrower's eyes, a block toward the target, at its lower third. */
    public LOTRMallornLeafBombEntity(Level level, LivingEntity thrower, LivingEntity target) {
        this(LOTREntities.MALLORN_LEAF_BOMB, level);
        setOwner(thrower);
        double y = thrower.getEyeY() - 0.1;
        setPos(thrower.getX(), y, thrower.getZ());
        double dx = target.getX() - thrower.getX();
        double dy = target.getBoundingBox().minY + target.getBbHeight() / 3.0f - y;
        double dz = target.getZ() - thrower.getZ();
        double dxz = Math.sqrt(dx * dx + dz * dz);
        if (dxz >= 1.0E-7) {
            float yaw = (float) (Mth.atan2(dz, dx) * Mth.RAD_TO_DEG) - 90.0f;
            float pitch = (float) -(Mth.atan2(dy, dxz) * Mth.RAD_TO_DEG);
            snapTo(thrower.getX() + dx / dxz, y, thrower.getZ() + dz / dxz, yaw, pitch);
            shoot(dx, dy, dz, 1.0f, 1.0f);
        }
    }

    @Override
    protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {
    }

    @Override
    protected double getDefaultGravity() {
        return 0.0;
    }

    private @Nullable LivingEntity getThrower() {
        return getOwner() instanceof LivingEntity living ? living : null;
    }

    public void explode() {
        if (!(level() instanceof ServerLevel level)) {
            return;
        }
        double range = 2.0;
        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(range));
        for (LivingEntity entity : entities) {
            if (!isEntityVulnerable(entity)) {
                continue;
            }
            float damage = this.leavesDamage / Math.max(1.0f, distanceTo(entity));
            if (damage <= 0.0f) {
                continue;
            }
            LivingEntity thrower = getThrower();
            entity.hurtServer(level, thrower != null ? damageSources().mobAttack(thrower) : damageSources().generic(),
                    damage);
        }
        discard();
    }

    public boolean isEntityVulnerable(Entity target) {
        LivingEntity thrower = getThrower();
        if (target == thrower) {
            return false;
        }
        if (target instanceof LivingEntity livingTarget) {
            if (thrower instanceof LOTRNPCEntity npc) {
                npc.getJumpControl().jump();
                return LOTRAttackRules.canNPCAttackEntity(npc, livingTarget, false);
            }
            return true;
        }
        return false;
    }

    @Override
    protected void onHit(HitResult hit) {
        if (level().isClientSide()) {
            return;
        }
        if (hit.getType() == HitResult.Type.BLOCK) {
            explode();
        } else if (hit instanceof EntityHitResult entityHit && isEntityVulnerable(entityHit.getEntity())) {
            explode();
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide()) {
            // A ring of twenty gold leaves, each with a fragment of mallorn leaves, spun about the line of flight.
            Vec3 axis = getDeltaMovement().reverse();
            BlockParticleOption leaves = new BlockParticleOption(LOTRParticles.MALLORN_ENT_HEAL,
                    LOTRDecorationBlocks.MALLORN_LEAVES.defaultBlockState());
            int count = 20;
            for (int l = 0; l < count; ++l) {
                float angle = (float) l / count * Mth.TWO_PI;
                Vec3 rotate = new Vec3(1.0, 1.0, 1.0).xRot((float) Math.toRadians(40.0)).yRot(angle);
                float dot = (float) rotate.dot(axis);
                Vec3 parallel = axis.scale(dot);
                Vec3 perp = rotate.subtract(parallel);
                Vec3 cross = rotate.cross(axis);
                Vec3 result = parallel.add(cross.scale(Mth.sin(-angle))).add(perp.scale(Mth.cos(-angle)));
                level().addParticle(LOTRLeafParticleOptions.of(LOTRParticles.LEAF_GOLD, 30, 0), getX(), getY(), getZ(),
                        result.x / 10.0, result.y / 10.0, result.z / 10.0);
                level().addParticle(leaves, getX(), getY(), getZ(), result.x / 20.0, result.y / 20.0, result.z / 20.0);
            }
        } else {
            ++this.leavesAge;
            if (this.leavesAge >= MAX_LEAVES_AGE) {
                explode();
            }
        }
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("LeavesAge", this.leavesAge);
        output.putFloat("LeavesDamage", this.leavesDamage);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.leavesAge = input.getIntOr("LeavesAge", 0);
        this.leavesDamage = input.getFloatOr("LeavesDamage", 0.0f);
    }
}
