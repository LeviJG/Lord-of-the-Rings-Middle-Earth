package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;

public class LOTRPlateRenderState extends BlockEntityRenderState {
    public final ItemStackRenderState food = new ItemStackRenderState();
    public int count;
    public float[] rotations = new float[0];
}
