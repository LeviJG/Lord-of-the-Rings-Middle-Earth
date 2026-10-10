package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai;

import java.util.EnumSet;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRBannerBearer;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.PathType;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityAIFollowHiringPlayer: a hired unit more than eight blocks from its
 * player walks to within six of them, through water if need be, at a pace
 * that matches a player's whatever its own speed; if it cannot find a way and
 * is beyond its follow range (at least 24), it may teleport to them.
 *
 * <p>A banner bearer keeps with its fellows instead: the nearest of its
 * faction within sixteen blocks, but more than eight away, that is no NPC or
 * is hired by the same player.
 */
public class LOTRFollowHiringPlayerGoal extends Goal {

    private final LOTRNPCEntity npc;
    private final float minFollowDist = 8.0f;
    private final float maxNearDist = 6.0f;
    private final boolean isBannerBearer;
    private @Nullable Player theHiringPlayer;
    private @Nullable Mob bannerBearerTarget;
    private int followTick;
    private float oldWaterMalus;

    public LOTRFollowHiringPlayerGoal(LOTRNPCEntity npc) {
        this.npc = npc;
        this.isBannerBearer = npc instanceof LOTRBannerBearer;
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    /** A player's pace whatever its speed -- or its mount's, when it rides (LOTREntityAIHorseFollowHiringPlayer). */
    private double moveSpeed() {
        net.minecraft.world.entity.LivingEntity mover = this.npc.getControlledVehicle() instanceof net.minecraft.world.entity.LivingEntity vehicle
                ? vehicle : this.npc;
        return 1.0 / mover.getAttributeValue(Attributes.MOVEMENT_SPEED) * 0.37;
    }

    @Override
    public boolean canUse() {
        if (!this.npc.hiredNPCInfo.isActive) {
            return false;
        }
        Player player = this.npc.hiredNPCInfo.getHiringPlayer();
        if (player == null) {
            return false;
        }
        this.theHiringPlayer = player;
        if (!this.npc.hiredNPCInfo.shouldFollowPlayer()) {
            return false;
        }
        if (this.isBannerBearer) {
            Mob entityToFollow = null;
            double d = Double.MAX_VALUE;
            for (Mob entity : this.npc.level().getEntitiesOfClass(Mob.class, this.npc.getBoundingBox().inflate(16.0))) {
                if (entity == this.npc || LOTRNearestAttackableTargetGoal.factionOf(entity) != this.npc.getFaction()) {
                    continue;
                }
                if (entity instanceof LOTRNPCEntity other
                        && (!other.hiredNPCInfo.isActive || other.hiredNPCInfo.getHiringPlayer() != player)) {
                    continue;
                }
                double dist = this.npc.distanceToSqr(entity);
                if (dist < d && dist > this.minFollowDist * this.minFollowDist) {
                    d = dist;
                    entityToFollow = entity;
                }
            }
            if (entityToFollow != null) {
                this.bannerBearerTarget = entityToFollow;
                return true;
            }
        }
        return this.npc.distanceToSqr(player) >= this.minFollowDist * this.minFollowDist;
    }

    @Override
    public boolean canContinueToUse() {
        if (this.npc.hiredNPCInfo.isActive && this.npc.hiredNPCInfo.getHiringPlayer() != null
                && this.npc.hiredNPCInfo.shouldFollowPlayer() && !this.npc.getNavigation().isDone()) {
            LivingEntity target = this.bannerBearerTarget != null ? this.bannerBearerTarget : this.theHiringPlayer;
            return this.npc.distanceToSqr(target) > this.maxNearDist * this.maxNearDist;
        }
        return false;
    }

    @Override
    public void start() {
        this.followTick = 0;
        this.oldWaterMalus = this.npc.getPathfindingMalus(PathType.WATER);
        this.npc.setPathfindingMalus(PathType.WATER, 0.0f);
    }

    @Override
    public void stop() {
        this.theHiringPlayer = null;
        this.bannerBearerTarget = null;
        this.npc.getNavigation().stop();
        this.npc.setPathfindingMalus(PathType.WATER, this.oldWaterMalus);
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        LivingEntity target = this.bannerBearerTarget != null ? this.bannerBearerTarget : this.theHiringPlayer;
        this.npc.getLookControl().setLookAt(target, 10.0f, this.npc.getMaxHeadXRot());
        if (this.npc.hiredNPCInfo.shouldFollowPlayer() && --this.followTick <= 0) {
            this.followTick = 10;
            if (!this.npc.getNavigation().moveTo(target, moveSpeed()) && this.npc.hiredNPCInfo.teleportAutomatically) {
                double d = Math.max(this.npc.getAttributeValue(Attributes.FOLLOW_RANGE), 24.0);
                if (this.npc.distanceToSqr(this.theHiringPlayer) > d * d) {
                    this.npc.hiredNPCInfo.tryTeleportToHiringPlayer(false);
                }
            }
        }
    }
}
