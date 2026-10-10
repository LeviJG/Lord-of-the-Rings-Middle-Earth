package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai;

import java.util.EnumSet;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCRideableEntity;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

/**
 * LOTREntityAIUntamedPanic: an untamed mount with a player on its back
 * bolts about. Now and then it either gives in -- the more its temper has
 * worn down, the likelier -- and is tamed, or throws the player, its temper
 * five points shorter, with an angry cry and a puff of smoke.
 */
public class LOTRUntamedPanicGoal extends Goal {

    private final LOTRNPCRideableEntity mount;
    private final double speed;
    private double targetX;
    private double targetY;
    private double targetZ;

    public LOTRUntamedPanicGoal(LOTRNPCRideableEntity mount, double speed) {
        this.mount = mount;
        this.speed = speed;
        setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!this.mount.isNPCTamed() && this.mount.getFirstPassenger() instanceof Player) {
            Vec3 target = DefaultRandomPos.getPos(this.mount, 5, 4);
            if (target == null) {
                return false;
            }
            this.targetX = target.x;
            this.targetY = target.y;
            this.targetZ = target.z;
            return true;
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return !this.mount.getNavigation().isDone() && this.mount.getFirstPassenger() instanceof Player
                && !this.mount.isNPCTamed();
    }

    @Override
    public void start() {
        this.mount.getNavigation().moveTo(this.targetX, this.targetY, this.targetZ, this.speed);
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (this.mount.getRandom().nextInt(50) != 0) {
            return;
        }
        if (this.mount.getFirstPassenger() instanceof Player player) {
            int temper = this.mount.getNPCTemper();
            int max = this.mount.getMaxNPCTemper();
            if (max > 0 && this.mount.getRandom().nextInt(max) < temper) {
                this.mount.tameNPC(player);
                this.mount.spawnHearts();
                return;
            }
            this.mount.increaseNPCTemper(5);
        }
        this.mount.ejectPassengers();
        this.mount.angerNPC();
        this.mount.spawnSmokes();
    }
}
