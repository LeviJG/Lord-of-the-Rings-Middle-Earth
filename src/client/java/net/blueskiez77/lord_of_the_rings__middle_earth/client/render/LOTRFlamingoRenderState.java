package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

public class LOTRFlamingoRenderState extends LivingEntityRenderState {
    /** handleRotationFloat: the chicken's wing flap, in place of the age in ticks. */
    public float flap;
    public int fishingCur;
    /** The fishing tick between its previous and current value. */
    public float fishing;
}
