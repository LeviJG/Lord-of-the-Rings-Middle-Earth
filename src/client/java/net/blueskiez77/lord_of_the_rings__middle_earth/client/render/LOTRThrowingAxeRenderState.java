package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;

/** What LOTRRenderThrowingAxe read off the entity: its heading, its tumble and the axe itself. */
public class LOTRThrowingAxeRenderState extends EntityRenderState {
    public float yaw;
    public float pitch;
    public float shake;
    public boolean stuck;

    /** axeRotation plus the partial tick, in degrees: 0 to 360 over ten ticks. */
    public float spin;

    public final ItemStackRenderState axe = new ItemStackRenderState();
}
