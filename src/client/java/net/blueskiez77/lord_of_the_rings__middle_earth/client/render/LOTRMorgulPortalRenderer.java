package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import com.mojang.blaze3d.vertex.PoseStack;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.world.LOTRPortalsClient;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRPortalBlockEntity;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import org.jspecify.annotations.Nullable;

/**
 * LOTRRenderMorgulPortal: a sheet of the gulduril glow bobbing over each block of the portal, full
 * bright. Every corner takes the same point of the texture, sliding along its top row with time, so
 * the sheet is one colour that shifts as it goes.
 */
public class LOTRMorgulPortalRenderer implements BlockEntityRenderer<LOTRPortalBlockEntity, LOTRMorgulPortalRenderer.State> {

    public static class State extends BlockEntityRenderState {
        float time;
    }

    public LOTRMorgulPortalRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(LOTRPortalBlockEntity portal, State state, float partialTick, Vec3 cameraPos,
                                   ModelFeatureRenderer.@Nullable CrumblingOverlay crumbling) {
        BlockEntityRenderState.extractBase(portal, state, crumbling);
        state.time = LOTRPortalsClient.clientTick + partialTick;
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        float f1 = state.time;
        float u = f1 * 0.01f;
        float c = 0.9f;
        poseStack.pushPose();
        poseStack.translate(0.5f, 0.5f + 0.25f * Mth.sin(f1 / 40.0f), 0.5f);
        collector.submitCustomGeometry(poseStack, LOTRPortalRenderTypes.MORGUL_PORTAL, (pose, buffer) -> {
            buffer.addVertex(pose, 0.5f, 0.0f, 0.5f).setUv(u, 0.0f).setColor(c, c, c, 1.0f);
            buffer.addVertex(pose, 0.5f, 0.0f, -0.5f).setUv(u, 0.0f).setColor(c, c, c, 1.0f);
            buffer.addVertex(pose, -0.5f, 0.0f, -0.5f).setUv(u, 0.0f).setColor(c, c, c, 1.0f);
            buffer.addVertex(pose, -0.5f, 0.0f, 0.5f).setUv(u, 0.0f).setColor(c, c, c, 1.0f);
        });
        poseStack.popPose();
    }
}
