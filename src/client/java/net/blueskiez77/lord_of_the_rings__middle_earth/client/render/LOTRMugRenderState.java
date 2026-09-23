package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRVessel;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

public class LOTRMugRenderState extends BlockEntityRenderState {
    public LOTRVessel vessel = LOTRVessel.MUG;
    /** The facing's horizontal index, which is what the original's metadata held. */
    public int meta;
    public @Nullable Identifier liquid;
}
