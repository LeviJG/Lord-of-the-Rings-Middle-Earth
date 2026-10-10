package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;

import net.minecraft.world.entity.ai.goal.Goal;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityAINPCFollowSpouse: a married NPC 6-16 blocks from its spouse
 * heads back to it every ten seconds, or at once beyond 12.
 */
public class LOTRNPCFollowSpouseGoal extends Goal {

    private final LOTRNPCEntity npc;
    private final double moveSpeed;
    private @Nullable LOTRNPCEntity spouse;
    private int followTick;

    public LOTRNPCFollowSpouseGoal(LOTRNPCEntity npc, double speed) {
        this.npc = npc;
        this.moveSpeed = speed;
    }

    @Override
    public boolean canUse() {
        LOTRNPCEntity spouse = this.npc.familyInfo.getSpouse();
        if (spouse == null || !spouse.isAlive() || this.npc.distanceToSqr(spouse) < 36.0
                || this.npc.distanceToSqr(spouse) >= 256.0) {
            return false;
        }
        this.spouse = spouse;
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        if (!this.spouse.isAlive()) {
            return false;
        }
        double d = this.npc.distanceToSqr(this.spouse);
        return d >= 36.0 && d <= 256.0;
    }

    @Override
    public void start() {
        this.followTick = 200;
    }

    @Override
    public void stop() {
        this.spouse = null;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        --this.followTick;
        if (this.npc.distanceToSqr(this.spouse) > 144.0 || this.followTick <= 0) {
            this.followTick = 200;
            this.npc.getNavigation().moveTo(this.spouse, this.moveSpeed);
        }
    }
}
