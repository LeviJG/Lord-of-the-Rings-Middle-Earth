package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

/**
 * For the scorpion's sting and the crocodile's snap: handleRotationFloat, the
 * attack timer counted down through the partial tick, over 20.
 */
public class LOTRStrikeRenderState extends LivingEntityRenderState {
    public float strike;
    public float scorpionScale = 1.0f;
    public boolean desert;
}
