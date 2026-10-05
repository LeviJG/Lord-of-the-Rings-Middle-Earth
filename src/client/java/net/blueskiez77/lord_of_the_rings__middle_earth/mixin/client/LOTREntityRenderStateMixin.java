package net.blueskiez77.lord_of_the_rings__middle_earth.mixin.client;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTROverheadHolder;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTROverheadRendering;

import net.minecraft.client.renderer.entity.state.EntityRenderState;

import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EntityRenderState.class)
public class LOTREntityRenderStateMixin implements LOTROverheadHolder {

    @Unique
    private LOTROverheadRendering.@Nullable Overhead lotr$overhead;

    @Unique
    private int lotr$entityId = -1;

    @Override
    public int lotr$getEntityId() {
        return this.lotr$entityId;
    }

    @Override
    public void lotr$setEntityId(int id) {
        this.lotr$entityId = id;
    }

    @Override
    public LOTROverheadRendering.@Nullable Overhead lotr$getOverhead() {
        return this.lotr$overhead;
    }

    @Override
    public void lotr$setOverhead(LOTROverheadRendering.@Nullable Overhead overhead) {
        this.lotr$overhead = overhead;
    }
}
