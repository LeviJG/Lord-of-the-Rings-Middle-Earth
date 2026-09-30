package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.character.LOTRGollumEntity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;

/** LOTREntityAIGollumAvoidEntity: running from them, arms over his head. */
public class LOTRGollumAvoidEntityGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {

    private final LOTRGollumEntity gollum;

    public LOTRGollumAvoidEntityGoal(LOTRGollumEntity gollum, Class<T> avoidClass, float distance, double walkSpeed,
                                     double sprintSpeed) {
        super(gollum, avoidClass, distance, walkSpeed, sprintSpeed);
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
