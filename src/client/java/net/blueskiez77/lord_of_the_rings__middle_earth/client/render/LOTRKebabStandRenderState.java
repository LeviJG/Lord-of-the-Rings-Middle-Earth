package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.resources.Identifier;

/** What the kebab stand needs to draw: its texture, its facing, and its load. */
public class LOTRKebabStandRenderState extends BlockEntityRenderState {

    /** Wooden or Haradric stand sheet. */
    public Identifier standTexture = Identifier.withDefaultNamespace("missingno");

    /** Degrees about Y. */
    public float yaw;

    /** 0-8 pieces on the spit. */
    public int meatCount;

    /** Raw or cooked meat sheet. */
    public boolean cooked;

    /** Degrees the spit has turned. */
    public float spin;
}
