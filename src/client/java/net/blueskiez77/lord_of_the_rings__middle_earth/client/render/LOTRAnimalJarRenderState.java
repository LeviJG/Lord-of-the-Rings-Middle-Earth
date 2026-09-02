package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

import org.jspecify.annotations.Nullable;

/** What the cage renderer needs: the occupant, already extracted, or nothing. */
public class LOTRAnimalJarRenderState extends BlockEntityRenderState {
    public @Nullable EntityRenderState occupant;
    /** How far up the cage the occupant sits. */
    public float height;
    /** Turns slowly on the spot, like the original's caged bird did. */
    public float spin;
}
