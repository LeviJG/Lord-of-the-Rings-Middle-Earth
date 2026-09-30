package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai;

import java.util.EnumSet;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

/**
 * LOTREntityAIHiredRemainStill: a halted unit on dry ground, with nothing
 * alive to fight, stands where it is, looking straight ahead and a little up.
 */
public class LOTRHiredRemainStillGoal extends Goal {

    private final LOTRNPCEntity npc;

    public LOTRHiredRemainStillGoal(LOTRNPCEntity npc) {
        this.npc = npc;
        setFlags(EnumSet.of(Flag.LOOK, Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        if (!this.npc.hiredNPCInfo.isActive || this.npc.isInWater() || !this.npc.onGround()) {
            return false;
        }
        return this.npc.hiredNPCInfo.isHalted() && (this.npc.getTarget() == null || !this.npc.getTarget().isAlive());
    }

    @Override
    public void start() {
        this.npc.getNavigation().stop();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        this.npc.getNavigation().stop();
        Vec3 pos = new Vec3(this.npc.getX(), this.npc.getY() + this.npc.getEyeHeight(), this.npc.getZ());
        Vec3 look = this.npc.getLookAngle().normalize();
        // The original's y term added pos.y twice over; it only tips the gaze upward.
        Vec3 lookUp = pos.add(look.x * 3.0, pos.y + 0.25, look.z * 3.0);
        this.npc.getLookControl().setLookAt(lookUp.x, lookUp.y, lookUp.z, 20.0f, 20.0f);
    }
}
