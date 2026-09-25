package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRFoodBlocks;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.level.block.Block;

public class LOTRPlateEntityRenderState extends EntityRenderState {
    public float yaw;
    public Block plate = LOTRFoodBlocks.FINE_PLATE;
}
