package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRTrophyType;

import net.minecraft.client.renderer.entity.state.EntityRenderState;

/** Which trophy, and whether it is stood on the floor or hung on a wall. */
public class LOTRBossTrophyRenderState extends EntityRenderState {
    public LOTRTrophyType type = LOTRTrophyType.MOUNTAIN_TROLL_CHIEFTAIN;
    public boolean hanging;
    public float rotation;
}
