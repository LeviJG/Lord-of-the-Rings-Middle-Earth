package net.blueskiez77.lord_of_the_rings__middle_earth.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTROverheadHolder;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTROverheadRendering;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.entity.Entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * What the original's renderers drew after the entity itself -- a hired
 * unit's icon and health bar, another player's alignment -- for every
 * renderer at once: read off the entity as its state is made, drawn as the
 * renderer finishes.
 */
@Mixin(EntityRenderer.class)
public abstract class LOTREntityRendererMixin {

    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void lotr$extractOverhead(Entity entity, EntityRenderState state, float partialTick, CallbackInfo ci) {
        ((LOTROverheadHolder) state).lotr$setEntityId(entity.getId());
        ((LOTROverheadHolder) state).lotr$setOverhead(
                LOTROverheadRendering.extract((EntityRenderer<?, ?>) (Object) this, entity, partialTick));
    }

    @Inject(method = "submit", at = @At("TAIL"))
    private void lotr$submitOverhead(EntityRenderState state, PoseStack poseStack, SubmitNodeCollector collector,
                                     CameraRenderState camera, CallbackInfo ci) {
        LOTROverheadRendering.Overhead overhead = ((LOTROverheadHolder) state).lotr$getOverhead();
        if (overhead != null) {
            LOTROverheadRendering.submit(overhead, poseStack, collector, camera);
        }
    }
}
