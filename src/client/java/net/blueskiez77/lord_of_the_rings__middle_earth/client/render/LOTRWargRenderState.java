package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;

import org.jspecify.annotations.Nullable;

public class LOTRWargRenderState extends LivingEntityRenderState {
    public Identifier skin;
    public float tailRotation;
    public boolean saddled;
    public @Nullable Identifier armor;
    /** A bombardier's bomb and its fuse, or none. */
    public final BlockModelRenderState bomb = new BlockModelRenderState();
    public boolean hasBomb;
    public float bombFuse;
}
