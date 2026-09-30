package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.character.LOTRGollumEntity;

import net.minecraft.world.entity.ai.goal.PanicGoal;

/** LOTREntityAIGollumPanic: hurt or burning, he runs, arms over his head. */
public class LOTRGollumPanicGoal extends PanicGoal {

    private final LOTRGollumEntity gollum;

    public LOTRGollumPanicGoal(LOTRGollumEntity gollum, double speed) {
        super(gollum, speed);
        this.gollum = gollum;
    }

    @Override
    public void start() {
        super.start();
        this.gollum.setGollumFleeing(true);
    }

    @Override
    public void stop() {
        super.stop();
        this.gollum.setGollumFleeing(false);
    }
}
