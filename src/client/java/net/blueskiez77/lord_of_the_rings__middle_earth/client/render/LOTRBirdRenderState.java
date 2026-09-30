package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import net.minecraft.client.renderer.item.ItemStackRenderState;

public class LOTRBirdRenderState extends LOTRSkinnedRenderState {
    public boolean still;
    public boolean flapping;
    /** handleRotationFloat: the wing-beat clock. */
    public float wingTime;
    public float birdScale = 1.0f;
    public final ItemStackRenderState stolenItem = new ItemStackRenderState();
}
