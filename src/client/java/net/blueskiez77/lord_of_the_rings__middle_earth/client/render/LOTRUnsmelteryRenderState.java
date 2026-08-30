package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;

/** How far the cauldron has tipped, which way it faces, and whether it is lit. */
public class LOTRUnsmelteryRenderState extends BlockEntityRenderState {

    /** -1..1, from LOTRUnsmelteryBlockEntity.getRockingAmount. */
    public float rocking;

    /** Degrees about Y, from the block's FACING. */
    public float yaw;

    /** Picks the active texture over the idle one. */
    public boolean lit;
}
