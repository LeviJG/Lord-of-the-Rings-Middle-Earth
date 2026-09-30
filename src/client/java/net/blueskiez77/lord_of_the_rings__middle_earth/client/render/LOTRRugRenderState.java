package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.resources.Identifier;

public class LOTRRugRenderState extends EntityRenderState {
    public float yaw;
    public Identifier texture = MissingTextureAtlasSprite.getLocation();
}
