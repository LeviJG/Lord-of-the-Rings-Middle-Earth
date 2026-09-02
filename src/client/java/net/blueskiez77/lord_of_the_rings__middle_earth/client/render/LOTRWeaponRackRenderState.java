package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;

/** What the weapon rack renderer needs: which way it faces, and what is on it. */
public class LOTRWeaponRackRenderState extends BlockEntityRenderState {
    public float yaw;
    public boolean onWall;
    public final ItemStackRenderState weapon = new ItemStackRenderState();
}
