package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.core.Direction;

import org.jspecify.annotations.Nullable;

/** Mirrors the fields of vanilla's ChestRenderState that a single chest needs. */
public class LOTRChestRenderState extends BlockEntityRenderState {

    public @Nullable SpriteId sprite;
    public Direction facing = Direction.NORTH;

    /** Raw openness, 0 shut to 1 open. The easing is applied at submit. */
    public float open;
}
