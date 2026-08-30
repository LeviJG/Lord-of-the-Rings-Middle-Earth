package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;

import org.jspecify.annotations.Nullable;

/**
 * What the render thread needs to draw one block's share of an ithildin
 * engraving. 26.2 splits block entity rendering into an extract pass that reads
 * the world and a submit pass that only reads this, so everything the quad
 * depends on -- brightness, orientation, sprite -- is resolved here first.
 */
public class LOTRIthildinDoorRenderState extends BlockEntityRenderState {

    /** 0 when the engraving is invisible, up to LOTRDwarvenGlowLogic's fullGlow. */
    public float glow;

    /** The face the engraving sits on: the opposite of the door's FACING. */
    public Direction glowFace = Direction.NORTH;

    /** The in-plane direction the design's local X runs along. */
    public Direction across = Direction.EAST;

    public @Nullable TextureAtlasSprite sprite;
}
