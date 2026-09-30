package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai;

import java.util.EnumSet;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.warg.LOTRWargBombardierEntity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityAIWargBombardierAttack: run at the target, lighting the fuse
 * within four blocks -- it jumps to 20 and burns down -- and blow up at zero
 * with the bomb's strength (mobGriefing permitting the damage to blocks).
 * Out of range, a lit fuse climbs back above 20 and keeps burning down.
 */
public class LOTRWargBombardierAttackGoal extends Goal {

    private final LOTRWargBombardierEntity warg;
    private final double moveSpeed;
    private @Nullable LivingEntity entityTarget;
    private @Nullable Path path;
    private int randomMoveTick;

    public LOTRWargBombardierAttackGoal(LOTRWargBombardierEntity warg, double speed) {
        this.warg = warg;
        this.moveSpeed = speed;
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.warg.getTarget();
        if (target == null) {
            return false;
        }
        this.entityTarget = target;
        this.path = this.warg.getNavigation().createPath(target, 0);
        return this.path != null;
    }

    @Override
    public boolean canContinueToUse() {
        return this.warg.getTarget() != null && this.entityTarget.isAlive();
    }

    @Override
    public void start() {
        this.warg.getNavigation().moveTo(this.path, this.moveSpeed);
        this.randomMoveTick = 0;
    }

    @Override
    public void stop() {
        this.entityTarget = null;
        this.warg.getNavigation().stop();
        this.warg.setBombFuse(LOTRWargBombardierEntity.MAX_FUSE);
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        LivingEntity target = this.entityTarget;
        this.warg.getLookControl().setLookAt(target, 30.0f, 30.0f);
        if (this.warg.getSensing().hasLineOfSight(target) && --this.randomMoveTick <= 0) {
            this.randomMoveTick = 4 + this.warg.getRandom().nextInt(7);
            this.warg.getNavigation().moveTo(target, this.moveSpeed);
        }
        int fuse = this.warg.getBombFuse();
        if (this.warg.distanceToSqr(target.getX(), target.getBoundingBox().minY, target.getZ()) <= 16.0) {
            if (fuse > 20) {
                while (fuse > 20) {
                    fuse -= 10;
                }
                this.warg.setBombFuse(fuse);
            } else if (fuse > 0) {
                this.warg.setBombFuse(fuse - 1);
            } else {
                this.warg.level().explode(this.warg, this.warg.getX(), this.warg.getY(), this.warg.getZ(),
                        (this.warg.getBombStrengthLevel() + 1) * 4.0f, Level.ExplosionInteraction.MOB);
                this.warg.discard();
            }
        } else if (fuse <= 20) {
            while (fuse <= 20) {
                fuse += 10;
            }
            this.warg.setBombFuse(fuse);
        } else {
            this.warg.setBombFuse(fuse - 1);
        }
    }
}
