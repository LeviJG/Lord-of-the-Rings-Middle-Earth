package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai;

import java.util.EnumSet;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.character.LOTRGollumEntity;

import net.minecraft.world.entity.ai.goal.Goal;

/** LOTREntityAIGollumRemainStill: told to stay, on dry ground, he stays. */
public class LOTRGollumRemainStillGoal extends Goal {

    private final LOTRGollumEntity gollum;

    public LOTRGollumRemainStillGoal(LOTRGollumEntity gollum) {
        this.gollum = gollum;
        setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        if (this.gollum.getGollumOwner() == null || this.gollum.isInWater() || !this.gollum.onGround()) {
            return false;
        }
        return this.gollum.isGollumSitting();
    }

    @Override
    public void start() {
        this.gollum.getNavigation().stop();
    }
}
