package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRTrollTotemBlock.Part;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;

/** Which part of the totem to draw, which way it faces, and how far the jaw has opened. */
public class LOTRTrollTotemRenderState extends BlockEntityRenderState {

    public Part part = Part.BASE;

    /** Degrees about Y, already converted from the block's FACING. */
    public float yaw;

    /** Only meaningful for the head. */
    public float jawDegrees;
}
