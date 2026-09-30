package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai;

import java.util.EnumSet;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.character.LOTRGollumEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.AABB;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityAIGollumFollowOwner: further than the start distance from his
 * owner, he goes to them (through water too), looking at them, until within
 * the stop distance; left 16 blocks behind with no path, he appears at their
 * feet if there is room and footing.
 */
public class LOTRGollumFollowOwnerGoal extends Goal {

    private final LOTRGollumEntity gollum;
    private final double moveSpeed;
    private final float minDist;
    private final float maxDist;
    private @Nullable Player owner;
    private int followTick;
    private float waterMalus;

    public LOTRGollumFollowOwnerGoal(LOTRGollumEntity gollum, double speed, float minDist, float maxDist) {
        this.gollum = gollum;
        this.moveSpeed = speed;
        this.minDist = minDist;
        this.maxDist = maxDist;
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        Player player = this.gollum.getGollumOwner();
        if (player == null || this.gollum.isGollumSitting() || this.gollum.distanceToSqr(player) < this.minDist * this.minDist) {
            return false;
        }
        this.owner = player;
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        return this.gollum.getGollumOwner() != null && this.owner != null && !this.gollum.getNavigation().isDone()
                && this.gollum.distanceToSqr(this.owner) > this.maxDist * this.maxDist && !this.gollum.isGollumSitting();
    }

    @Override
    public void start() {
        this.followTick = 0;
        this.waterMalus = this.gollum.getPathfindingMalus(PathType.WATER);
        this.gollum.setPathfindingMalus(PathType.WATER, 0.0f);
    }

    @Override
    public void stop() {
        this.owner = null;
        this.gollum.getNavigation().stop();
        this.gollum.setPathfindingMalus(PathType.WATER, this.waterMalus);
    }

    @Override
    public void tick() {
        Player player = this.owner;
        if (player == null) {
            return;
        }
        this.gollum.getLookControl().setLookAt(player, 10.0f, this.gollum.getMaxHeadXRot());
        if (!this.gollum.isGollumSitting() && --this.followTick <= 0) {
            this.followTick = 10;
            if (!this.gollum.getNavigation().moveTo(player, this.moveSpeed) && this.gollum.distanceToSqr(player) >= 256.0) {
                BlockPos below = BlockPos.containing(player.getX(), player.getBoundingBox().minY, player.getZ()).below();
                float f = this.gollum.getBbWidth() / 2.0f;
                AABB box = new AABB(player.getX() - f, player.getY(), player.getZ() - f,
                        player.getX() + f, player.getY() + this.gollum.getBbHeight(), player.getZ() + f);
                if (this.gollum.level().noBlockCollision(this.gollum, box)
                        && this.gollum.level().getBlockState(below).isFaceSturdy(this.gollum.level(), below, Direction.UP)) {
                    this.gollum.snapTo(player.getX(), player.getBoundingBox().minY, player.getZ(),
                            this.gollum.getYRot(), this.gollum.getXRot());
                    this.gollum.resetFallDistance();
                    this.gollum.getNavigation().stop();
                }
            }
        }
    }
}
