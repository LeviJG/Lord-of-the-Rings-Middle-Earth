package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;

import net.minecraft.world.entity.ai.goal.Goal;

import org.jspecify.annotations.Nullable;

/** LOTREntityAINPCFollowParent: a child keeps within 3-16 blocks of its same-sex parent. */
public class LOTRNPCFollowParentGoal extends Goal {

    private final LOTRNPCEntity npc;
    private final double moveSpeed;
    private @Nullable LOTRNPCEntity parent;
    private int followTick;

    public LOTRNPCFollowParentGoal(LOTRNPCEntity npc, double speed) {
        this.npc = npc;
        this.moveSpeed = speed;
    }

    @Override
    public boolean canUse() {
        if (this.npc.familyInfo.getAge() >= 0) {
            return false;
        }
        LOTRNPCEntity parent = this.npc.familyInfo.getParentToFollow();
        if (parent == null || this.npc.distanceToSqr(parent) < 9.0 || this.npc.distanceToSqr(parent) >= 256.0) {
            return false;
        }
        this.parent = parent;
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        return this.parent != null && this.parent.isAlive() && this.npc.distanceToSqr(this.parent) >= 9.0
                && this.npc.distanceToSqr(this.parent) <= 256.0;
    }

    @Override
    public void start() {
        this.followTick = 0;
    }

    @Override
    public void stop() {
        this.parent = null;
    }

    @Override
    public void tick() {
        if (--this.followTick <= 0) {
            this.followTick = 10;
            this.npc.getNavigation().moveTo(this.parent, this.moveSpeed);
        }
    }
}
