package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCAttributes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.utumno.LOTRBalrogEntity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/**
 * LOTREntityAIBalrogCharge: when its quarry is twenty blocks off or more, or it has been ten seconds
 * without landing a blow, the Balrog charges -- at three times its pace, for up to ten seconds,
 * striking all it runs into for its charge damage, hurling them away and setting them alight -- until
 * it hits the one it charges.
 */
public class LOTRBalrogChargeGoal extends LOTRAttackOnCollideGoal {

    private final LOTRBalrogEntity theBalrog;
    private final float chargeDist;
    private final int frustrationTime;
    private boolean hitChargeTarget;
    private int chargingTick;

    public LOTRBalrogChargeGoal(LOTRBalrogEntity balrog, double speed, float dist, int frustration) {
        super(balrog, speed, false);
        this.theBalrog = balrog;
        this.chargeDist = dist;
        this.frustrationTime = frustration;
    }

    @Override
    public boolean canUse() {
        if (this.theBalrog.isBalrogCharging()) {
            return false;
        }
        if (super.canUse()) {
            if (this.theBalrog.chargeFrustration >= this.frustrationTime) {
                return true;
            }
            return this.theBalrog.distanceToSqr(this.attackTarget) >= this.chargeDist * this.chargeDist;
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        if (!this.theBalrog.isAlive()) {
            return false;
        }
        this.attackTarget = this.theBalrog.getTarget();
        if (this.attackTarget == null || !this.attackTarget.isAlive()) {
            return false;
        }
        return this.chargingTick > 0 && !this.hitChargeTarget;
    }

    @Override
    public void start() {
        super.start();
        this.theBalrog.setBalrogCharging(true);
        this.hitChargeTarget = false;
        this.chargingTick = 200;
    }

    @Override
    public void stop() {
        super.stop();
        this.theBalrog.setBalrogCharging(false);
        this.hitChargeTarget = false;
        this.chargingTick = 0;
    }

    @Override
    public void tick() {
        updateLookAndPathing();
        if (this.chargingTick > 0) {
            --this.chargingTick;
        }
        if (!(this.theBalrog.level() instanceof ServerLevel level)) {
            return;
        }
        Vec3 motion = this.theBalrog.getDeltaMovement();
        double motionExtent = 2.0;
        float angleRad = (float) Math.atan2(motion.z, motion.x);
        AABB targetBB = this.theBalrog.getBoundingBox().inflate(0.5, 0.0, 0.5)
                .move(Mth.cos(angleRad) * motionExtent, 0.0, Mth.sin(angleRad) * motionExtent);
        for (Entity obj : level.getEntities(this.theBalrog, targetBB)) {
            if (!(obj instanceof LivingEntity hitEntity) || hitEntity == this.theBalrog.getFirstPassenger()) {
                continue;
            }
            float attackStr = (float) this.theBalrog.getAttributeValue(LOTRNPCAttributes.BALROG_CHARGE_DAMAGE);
            if (!hitEntity.hurtServer(level, level.damageSources().mobAttack(this.theBalrog), attackStr)) {
                continue;
            }
            float knock = 2.5f;
            float knockY = 0.5f;
            float yaw = this.theBalrog.getYRot() * Mth.DEG_TO_RAD;
            hitEntity.push(-Mth.sin(yaw) * knock, knockY, Mth.cos(yaw) * knock);
            hitEntity.igniteForSeconds(6.0f);
            if (hitEntity == this.attackTarget) {
                this.hitChargeTarget = true;
            }
        }
    }
}
