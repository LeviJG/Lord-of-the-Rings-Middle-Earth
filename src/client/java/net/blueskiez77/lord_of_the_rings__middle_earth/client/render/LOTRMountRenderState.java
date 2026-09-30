package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import net.minecraft.client.renderer.entity.state.EquineRenderState;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.resources.Identifier;

import org.jspecify.annotations.Nullable;

/** A LOTR mount: vanilla's equine state plus the layers the original composited into one texture. */
public class LOTRMountRenderState extends EquineRenderState {
    public Identifier skin = MissingTextureAtlasSprite.getLocation();
    /** The horse's markings layer, if any. */
    public @Nullable Identifier markings;
    /** The barding's texture, if it wears barding the original drew. */
    public @Nullable Identifier armor;
    /** The camel's carpet colour (ARGB), or 0 for none. */
    public int carpetColor;
    public boolean chested;
    /** The giraffe lowers its neck under a player. */
    public boolean riddenByPlayer;
    /** LOTRMod.isChristmas: the elk's red nose. */
    public boolean christmas;
}
