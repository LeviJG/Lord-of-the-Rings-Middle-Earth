package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai;

import java.util.EnumSet;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRBoss;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;

import net.minecraft.world.entity.ai.goal.Goal;

/**
 * LOTREntityAIBossJumpAttack: a boss with a target, one tick in twenty, may
 * leap -- the likelier the more it is hurt (getBaseChanceModifier), the more
 * enemies crowd it (four times as likely for each, when there is more than
 * one) and the closer its target is within eight blocks.
 */
public class LOTRBossJumpAttackGoal extends Goal {

    private final LOTRNPCEntity boss;
    private final double jumpSpeed;
    private final float jumpChance;

    public LOTRBossJumpAttackGoal(LOTRNPCEntity boss, double jumpSpeed, float jumpChance) {
        this.boss = boss;
        this.jumpSpeed = jumpSpeed;
        this.jumpChance = jumpChance;
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.boss.getTarget() == null || this.boss.bossInfo == null || this.boss.bossInfo.jumpAttack) {
            return false;
        }
        if (this.boss.getRandom().nextInt(20) == 0) {
            float f = ((LOTRBoss) this.boss).getBaseChanceModifier() * this.jumpChance;
            int enemies = this.boss.bossInfo.getNearbyEnemies().size();
            if (enemies > 1.0f) {
                f *= enemies * 4.0f;
            }
            float distance = (8.0f - this.boss.distanceTo(this.boss.getTarget())) / 2.0f;
            if (distance > 1.0f) {
                f *= distance;
            }
            return this.boss.getRandom().nextFloat() < f;
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return false;
    }

    @Override
    public void start() {
        this.boss.bossInfo.doJumpAttack(this.jumpSpeed);
    }
}
