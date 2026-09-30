package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;

public class LOTRDeerRenderState extends LivingEntityRenderState {
    public boolean male;
    public Identifier skin = MissingTextureAtlasSprite.getLocation();
}
