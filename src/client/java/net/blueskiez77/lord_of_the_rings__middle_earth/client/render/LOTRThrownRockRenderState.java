package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;

/** What LOTRRenderThrownRock reads off a thrown rock. */
public class LOTRThrownRockRenderState extends EntityRenderState {
    public float yaw;
    public float pitch;
    public final ItemStackRenderState block = new ItemStackRenderState();
}
