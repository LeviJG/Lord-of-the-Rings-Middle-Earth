package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai;

import java.util.EnumSet;
import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRHobbitEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityAINPCAvoidEvilPlayer: children run from players with negative
 * alignment, and hobbits of any age from those at -100 or below. The flight
 * is quicker once the player is within seven blocks.
 *
 * <p>The 1.7.10 fork this port follows had this filter inverted -- it kept the
 * players who should NOT be feared, so hobbits fled their friends. This is the
 * condition as the original mod wrote it.
 */
public class LOTRNPCAvoidEvilPlayerGoal extends Goal {

    private final LOTRNPCEntity npc;
    private final float distance;
    private final double farSpeed;
    private final double nearSpeed;
    private @Nullable Player avoided;
    private @Nullable Path path;

    public LOTRNPCAvoidEvilPlayerGoal(LOTRNPCEntity npc, float distance, double farSpeed, double nearSpeed) {
        this.npc = npc;
        this.distance = distance;
        this.farSpeed = farSpeed;
        this.nearSpeed = nearSpeed;
        setFlags(EnumSet.of(Flag.MOVE));
    }

    private boolean isFeared(Player player) {
        if (player.isCreative() || player.isSpectator()) {
            return false;
        }
        float alignment = LOTRPlayerAlignments.getAlignment(player, this.npc.getFaction());
        return this.npc.familyInfo.getAge() < 0 && alignment < 0.0f
                || this.npc instanceof LOTRHobbitEntity && alignment <= -100.0f;
    }

    @Override
    public boolean canUse() {
        List<Player> players = this.npc.level().getEntitiesOfClass(Player.class,
                this.npc.getBoundingBox().inflate(this.distance, this.distance / 2.0, this.distance), this::isFeared);
        if (players.isEmpty()) {
            return false;
        }
        this.avoided = players.get(0);
        Vec3 flee = DefaultRandomPos.getPosAway(this.npc, 16, 7, this.avoided.position());
        if (flee == null || this.avoided.distanceToSqr(flee) < this.avoided.distanceToSqr(this.npc)) {
            return false;
        }
        this.path = this.npc.getNavigation().createPath(flee.x, flee.y, flee.z, 0);
        return this.path != null && this.path.canReach();
    }

    @Override
    public boolean canContinueToUse() {
        return !this.npc.getNavigation().isDone();
    }

    @Override
    public void start() {
        this.npc.getNavigation().moveTo(this.path, this.farSpeed);
    }

    @Override
    public void stop() {
        this.avoided = null;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        this.npc.getNavigation().setSpeedModifier(
                this.npc.distanceToSqr(this.avoided) < 49.0 ? this.nearSpeed : this.farSpeed);
    }
}
