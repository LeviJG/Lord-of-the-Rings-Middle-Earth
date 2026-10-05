package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

import org.jspecify.annotations.Nullable;

/** What the jar renderer needs: the occupant, already extracted, or nothing. */
public class LOTRAnimalJarRenderState extends BlockEntityRenderState {
    public @Nullable EntityRenderState occupant;
    /** Where the occupant stands in the jar, bobbing included, as a height within the block. */
    public float height;
}
