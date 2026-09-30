package net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc;

import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;

import org.jspecify.annotations.Nullable;

/** What LOTRModelHuorn and LOTRRenderHuorn read off a huorn. */
public class LOTRHuornRenderState extends LivingEntityRenderState {
    public @Nullable Identifier face;
    public boolean active;
    public final BlockModelRenderState wood = new BlockModelRenderState();
    public final BlockModelRenderState leaves = new BlockModelRenderState();
}
