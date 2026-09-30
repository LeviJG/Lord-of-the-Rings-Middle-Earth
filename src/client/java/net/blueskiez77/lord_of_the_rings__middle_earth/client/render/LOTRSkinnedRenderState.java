package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.resources.Identifier;

/** A creature's render state with the skin picked for it (LOTRRandomSkins). */
public class LOTRSkinnedRenderState extends LivingEntityRenderState {
    public Identifier skin = MissingTextureAtlasSprite.getLocation();
}
