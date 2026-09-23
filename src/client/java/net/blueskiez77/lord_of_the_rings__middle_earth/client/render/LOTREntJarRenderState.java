package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import net.minecraft.data.AtlasIds;
import net.minecraft.resources.Identifier;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import org.jspecify.annotations.Nullable;

/** The liquid surface inside one Ent Jar: how high, what colour, what texture. */
public class LOTREntJarRenderState extends BlockEntityRenderState {

    /** Height of the surface in block-local space, or negative when empty. */
    public float surfaceY = -1.0f;

    /** ARGB tint. For water this is the biome's water colour. */
    public int tint = 0xFFFFFFFF;

    public @Nullable TextureAtlasSprite sprite;

    /** The atlas the sprite lives on: blocks for water, items for a draught. */
    public Identifier atlas = AtlasIds.BLOCKS;

    /** Which sixteenths of the sprite are stretched over the surface. */
    public float uvFrom;
    public float uvTo = 6.0f;
}
