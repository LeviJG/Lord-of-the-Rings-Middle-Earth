package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai;

import java.util.EnumSet;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRBandit;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;

import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityAIBanditFlee: a bandit with loot and no fight on its hands runs
 * from the nearest player within its follow range, picking somewhere away
 * from them each time it stops.
 */
public class LOTRBanditFleeGoal extends Goal {

    private final LOTRBandit theBandit;
    private final LOTRNPCEntity theBanditAsNPC;
    private final double speed;
    private final double range;
    private @Nullable Player targetPlayer;

    public LOTRBanditFleeGoal(LOTRBandit bandit, double speed) {
        this.theBandit = bandit;
        this.theBanditAsNPC = bandit.getBanditAsNPC();
        this.speed = speed;
        this.range = this.theBanditAsNPC.getAttributeValue(Attributes.FOLLOW_RANGE);
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    private @Nullable Player findNearestPlayer() {
        double distance = this.range;
        Player nearest = null;
        for (Player player : this.theBanditAsNPC.level().getEntitiesOfClass(Player.class,
                this.theBanditAsNPC.getBoundingBox().inflate(this.range))) {
            double d = this.theBanditAsNPC.distanceTo(player);
            if (!player.isCreative() && d < distance) {
                distance = d;
                nearest = player;
            }
        }
        return nearest;
    }

    @Override
    public boolean canUse() {
        if (this.theBanditAsNPC.getTarget() != null || this.theBandit.getBanditInventory().isEmpty()) {
            return false;
        }
        this.targetPlayer = findNearestPlayer();
        return this.targetPlayer != null;
    }

    @Override
    public boolean canContinueToUse() {
        if (this.targetPlayer == null || !this.targetPlayer.isAlive() || this.targetPlayer.isCreative()) {
            return false;
        }
        return this.theBanditAsNPC.getTarget() == null
                && this.theBanditAsNPC.distanceToSqr(this.targetPlayer) < this.range * this.range;
    }

    @Override
    public void stop() {
        this.targetPlayer = null;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (this.targetPlayer != null && this.theBanditAsNPC.getNavigation().isDone()) {
            Vec3 away = DefaultRandomPos.getPosAway(this.theBanditAsNPC, (int) this.range, 10, this.targetPlayer.position());
            if (away != null) {
                this.theBanditAsNPC.getNavigation().moveTo(away.x, away.y, away.z, this.speed);
            }
            this.targetPlayer = findNearestPlayer();
        }
    }
}
