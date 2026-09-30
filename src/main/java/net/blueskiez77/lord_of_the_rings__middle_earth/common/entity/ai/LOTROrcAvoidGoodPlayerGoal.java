package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai;

import java.util.EnumSet;
import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.orc.LOTROrcEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityAIOrcAvoidGoodPlayer: a weak orc, unhired and not of Mordor, runs
 * from a player at -500 or worse -- unless it or an ally close by is already
 * fighting a player. Once the player strikes it, it stops running.
 */
public class LOTROrcAvoidGoodPlayerGoal extends Goal {

    private final LOTROrcEntity orc;
    private final float distance;
    private final double speed;
    private @Nullable Player closestEnemyPlayer;
    private @Nullable Path path;

    public LOTROrcAvoidGoodPlayerGoal(LOTROrcEntity orc, float distance, double speed) {
        this.orc = orc;
        this.distance = distance;
        this.speed = speed;
        setFlags(EnumSet.of(Flag.MOVE));
    }

    private boolean anyNearbyOrcsAttacked() {
        List<Mob> allies = this.orc.level().getEntitiesOfClass(Mob.class,
                this.orc.getBoundingBox().inflate(this.distance, this.distance / 2.0, this.distance),
                e -> e != this.orc && LOTRNearestAttackableTargetGoal.factionOf(e).isGoodRelation(this.orc.getFaction()));
        for (Mob ally : allies) {
            if (ally instanceof LOTROrcEntity allyOrc ? allyOrc.currentRevengeTarget instanceof Player
                    : ally.getTarget() instanceof Player) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canUse() {
        if (!this.orc.isWeakOrc || this.orc.hiredNPCInfo.isActive || this.orc.getFaction() == LOTRFaction.MORDOR) {
            return false;
        }
        if (this.orc.currentRevengeTarget != null || anyNearbyOrcsAttacked()) {
            return false;
        }
        List<Player> players = this.orc.level().getEntitiesOfClass(Player.class,
                this.orc.getBoundingBox().inflate(this.distance, this.distance / 2.0, this.distance),
                p -> !p.isCreative() && !p.isSpectator() && this.orc.currentRevengeTarget != p
                        && LOTRPlayerAlignments.getAlignment(p, this.orc.getFaction()) <= -500.0f);
        if (players.isEmpty()) {
            return false;
        }
        this.closestEnemyPlayer = players.get(0);
        Vec3 flee = DefaultRandomPos.getPosAway(this.orc, 16, 7, this.closestEnemyPlayer.position());
        if (flee == null || this.closestEnemyPlayer.distanceToSqr(flee) < this.closestEnemyPlayer.distanceToSqr(this.orc)) {
            return false;
        }
        this.path = this.orc.getNavigation().createPath(flee.x, flee.y, flee.z, 0);
        return this.path != null && this.path.canReach();
    }

    @Override
    public boolean canContinueToUse() {
        return !this.orc.getNavigation().isDone() && this.orc.getLastHurtByMob() != this.closestEnemyPlayer
                && !anyNearbyOrcsAttacked();
    }

    @Override
    public void start() {
        this.orc.getNavigation().moveTo(this.path, this.speed);
    }

    @Override
    public void stop() {
        this.closestEnemyPlayer = null;
    }
}
