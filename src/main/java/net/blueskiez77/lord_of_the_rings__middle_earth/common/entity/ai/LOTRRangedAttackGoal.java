package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai;

import java.util.EnumSet;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.phys.Vec3;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityAIRangedAttack: closes to within range, backs off to 8-16 blocks
 * from a target nearer than five, stops once it has had the target in sight
 * for a second, and shoots when it can see it -- more often the closer the
 * target is, between {@code attackTimeMin} and {@code attackTimeMax} ticks.
 */
public class LOTRRangedAttackGoal extends Goal {

    private static final double MOVE_SPEED_FLEE = 1.5;

    private final Mob owner;
    private final RangedAttackMob ranged;
    private final double moveSpeed;
    private final int attackTimeMin;
    private final int attackTimeMax;
    private final float attackRange;
    private final float attackRangeSq;
    private @Nullable LivingEntity attackTarget;
    private int rangedAttackTime = -1;
    private int repathDelay;

    public <T extends Mob & RangedAttackMob> LOTRRangedAttackGoal(T owner, double speed, int min, int max, float range) {
        this.owner = owner;
        this.ranged = owner;
        this.moveSpeed = speed;
        this.attackTimeMin = min;
        this.attackTimeMax = max;
        this.attackRange = range;
        this.attackRangeSq = range * range;
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    /** findPositionAwayFrom: a spot {@code min} to {@code max} blocks from the target. */
    public static @Nullable Vec3 findPositionAwayFrom(LivingEntity entity, LivingEntity target, int min, int max) {
        RandomSource random = entity.getRandom();
        for (int l = 0; l < 24; ++l) {
            int i = Mth.floor(entity.getX()) - max + random.nextInt(max * 2 + 1);
            int j = Mth.floor(entity.getBoundingBox().minY) - 4 + random.nextInt(9);
            int k = Mth.floor(entity.getZ()) - max + random.nextInt(max * 2 + 1);
            double d = target.distanceToSqr(i, j, k);
            if (d <= min * min || d >= max * max) {
                continue;
            }
            return new Vec3(i, j, k);
        }
        return null;
    }

    private boolean mounted() {
        return this.owner instanceof LOTRNPCEntity npc && npc.ridingMount;
    }

    /**
     * Mounted, the mount carries it at the mount's own speed (user: a vanilla
     * horse's pace). The original drove it at the rider's horse attack speed,
     * 1.7 times that or more (LOTREntityAIHorseMoveToRiderTarget).
     */
    private double horseSpeed() {
        return 1.0;
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.owner.getTarget();
        if (target == null) {
            return false;
        }
        this.attackTarget = target;
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        if (!this.owner.isAlive()) {
            return false;
        }
        this.attackTarget = this.owner.getTarget();
        return this.attackTarget != null && this.attackTarget.isAlive();
    }

    @Override
    public void stop() {
        this.attackTarget = null;
        this.repathDelay = 0;
        this.rangedAttackTime = -1;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        LivingEntity target = this.attackTarget;
        double distanceSq = this.owner.distanceToSqr(target.getX(), target.getBoundingBox().minY, target.getZ());
        boolean canSee = this.owner.getSensing().hasLineOfSight(target);
        this.repathDelay = canSee ? this.repathDelay + 1 : 0;
        if (distanceSq <= this.attackRangeSq) {
            if (this.owner.distanceToSqr(target) < 25.0) {
                Vec3 away = findPositionAwayFrom(this.owner, target, 8, 16);
                if (away != null) {
                    this.owner.getNavigation().moveTo(away.x, away.y, away.z, mounted() ? horseSpeed() : MOVE_SPEED_FLEE);
                }
            } else if (this.repathDelay >= 20) {
                this.owner.getNavigation().stop();
                this.repathDelay = 0;
            }
        } else {
            this.owner.getNavigation().moveTo(target, mounted() ? horseSpeed() : this.moveSpeed);
        }
        this.owner.getLookControl().setLookAt(target, 30.0f, 30.0f);
        --this.rangedAttackTime;
        if (this.rangedAttackTime == 0) {
            if (distanceSq > this.attackRangeSq || !canSee) {
                return;
            }
            float distanceRatio = (float) Math.sqrt(distanceSq) / this.attackRange;
            this.ranged.performRangedAttack(target, Mth.clamp(distanceRatio, 0.1f, 1.0f));
            this.rangedAttackTime = Mth.floor(distanceRatio * (this.attackTimeMax - this.attackTimeMin) + this.attackTimeMin);
        } else if (this.rangedAttackTime < 0) {
            float distanceRatio = (float) Math.sqrt(distanceSq) / this.attackRange;
            this.rangedAttackTime = Mth.floor(distanceRatio * (this.attackTimeMax - this.attackTimeMin) + this.attackTimeMin);
        }
    }
}
