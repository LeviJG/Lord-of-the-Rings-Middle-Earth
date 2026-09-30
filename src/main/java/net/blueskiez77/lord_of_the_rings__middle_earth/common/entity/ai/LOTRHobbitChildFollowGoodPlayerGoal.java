package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityAIHobbitChildFollowGoodPlayer: a hobbit child tags along, 3-16
 * blocks behind, after the nearest player with +200 alignment or more.
 */
public class LOTRHobbitChildFollowGoodPlayerGoal extends Goal {

    private final LOTRNPCEntity hobbit;
    private final float range;
    private final double speed;
    private @Nullable Player playerToFollow;
    private int followDelay;

    public LOTRHobbitChildFollowGoodPlayerGoal(LOTRNPCEntity hobbit, float range, double speed) {
        this.hobbit = hobbit;
        this.range = range;
        this.speed = speed;
    }

    @Override
    public boolean canUse() {
        if (this.hobbit.familyInfo.getAge() >= 0) {
            return false;
        }
        Player best = null;
        double distanceSq = Double.MAX_VALUE;
        for (Player candidate : this.hobbit.level().getEntitiesOfClass(Player.class,
                this.hobbit.getBoundingBox().inflate(this.range, 3.0, this.range))) {
            if (LOTRPlayerAlignments.getAlignment(candidate, this.hobbit.getFaction()) < 200.0f) {
                continue;
            }
            double d = this.hobbit.distanceToSqr(candidate);
            if (d <= distanceSq) {
                distanceSq = d;
                best = candidate;
            }
        }
        if (best == null || distanceSq < 9.0) {
            return false;
        }
        this.playerToFollow = best;
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        if (!this.playerToFollow.isAlive() || this.hobbit.familyInfo.getAge() >= 0) {
            return false;
        }
        double distanceSq = this.hobbit.distanceToSqr(this.playerToFollow);
        return distanceSq >= 9.0 && distanceSq <= 256.0;
    }

    @Override
    public void start() {
        this.followDelay = 0;
    }

    @Override
    public void stop() {
        this.playerToFollow = null;
    }

    @Override
    public void tick() {
        if (--this.followDelay <= 0) {
            this.followDelay = 10;
            this.hobbit.getNavigation().moveTo(this.playerToFollow, this.speed);
        }
    }
}
