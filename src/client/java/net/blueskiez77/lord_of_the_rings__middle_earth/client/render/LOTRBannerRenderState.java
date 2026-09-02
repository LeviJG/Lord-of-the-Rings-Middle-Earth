package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBannerType;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;

/** Which banner, which way round, and whether it is stood up or hung on a wall. */
public class LOTRBannerRenderState extends BlockEntityRenderState {
    public LOTRBannerType type = LOTRBannerType.GONDOR;
    public float yaw;
    public boolean onWall;
}
