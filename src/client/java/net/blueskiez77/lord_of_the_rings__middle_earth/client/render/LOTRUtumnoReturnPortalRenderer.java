package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRUtumnoReturnPortalBlockEntity;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import org.jspecify.annotations.Nullable;

/**
 * LOTRRenderUtumnoReturnPortal: the shaft of light -- twelve nested columns of pale light, breathing
 * brighter and dimmer, up to the top of Utumno -- and at its foot twelve circles of light, each
 * turning at its own speed and swelling and shrinking. Drawn wherever it is.
 */
public class LOTRUtumnoReturnPortalRenderer
        implements BlockEntityRenderer<LOTRUtumnoReturnPortalBlockEntity, LOTRUtumnoReturnPortalRenderer.State> {

    public static class State extends BlockEntityRenderState {
        float renderTime;
    }

    public LOTRUtumnoReturnPortalRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(LOTRUtumnoReturnPortalBlockEntity portal, State state, float partialTick, Vec3 cameraPos,
                                   ModelFeatureRenderer.@Nullable CrumblingOverlay crumbling) {
        BlockEntityRenderState.extractBase(portal, state, crumbling);
        state.renderTime = portal.ticksExisted + partialTick;
    }

    @Override
    public boolean shouldRenderOffScreen() {
        return true;
    }

    @Override
    public int getViewDistance() {
        return 512;
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        float renderTime = state.renderTime;
        poseStack.pushPose();
        poseStack.translate(0.5f, 0.0f, 0.5f);
        float beamAlpha = 0.2f + 0.15f * Mth.sin(renderTime * 0.1f);
        collector.submitCustomGeometry(poseStack, RenderTypes.debugQuads(), (pose, buffer) -> {
            int passes = 12;
            float bottom = 0.0f;
            float top = bottom + LOTRUtumnoReturnPortalBlockEntity.PORTAL_TOP;
            for (int i = 0; i < passes; ++i) {
                float width = (float) (i + 1) / passes * 0.99f / 2.0f;
                beam(buffer, pose, width, bottom, top, width, -width, bottom, top, width, beamAlpha);
                beam(buffer, pose, width, bottom, top, -width, -width, bottom, top, -width, beamAlpha);
                beam(buffer, pose, width, bottom, top, width, width, bottom, top, -width, beamAlpha);
                beam(buffer, pose, -width, bottom, top, width, -width, bottom, top, -width, beamAlpha);
            }
        });
        int circles = 12;
        for (int i = 0; i < circles; ++i) {
            poseStack.pushPose();
            poseStack.mulPose(Axis.YP.rotationDegrees(renderTime * i * 0.2f));
            float alpha = 0.15f + 0.1f * Mth.sin(renderTime * 0.1f + i);
            float width = 1.5f + 1.5f * Mth.sin(renderTime * 0.05f + i);
            float height = 0.01f + i * 0.01f;
            collector.submitCustomGeometry(poseStack, LOTRPortalRenderTypes.UTUMNO_LIGHT_CIRCLE, (pose, buffer) -> {
                buffer.addVertex(pose, width, height, width).setUv(1.0f, 1.0f).setColor(1.0f, 1.0f, 1.0f, alpha);
                buffer.addVertex(pose, width, height, -width).setUv(1.0f, 0.0f).setColor(1.0f, 1.0f, 1.0f, alpha);
                buffer.addVertex(pose, -width, height, -width).setUv(0.0f, 0.0f).setColor(1.0f, 1.0f, 1.0f, alpha);
                buffer.addVertex(pose, -width, height, width).setUv(0.0f, 1.0f).setColor(1.0f, 1.0f, 1.0f, alpha);
            });
            poseStack.popPose();
        }
        poseStack.popPose();
    }

    /** One upright face of a column, from (x0, z0) to (x1, z1). */
    private static void beam(VertexConsumer buffer, PoseStack.Pose pose, float x0, float bottom, float top, float z0,
                             float x1, float bottom1, float top1, float z1, float alpha) {
        buffer.addVertex(pose, x0, bottom, z0).setColor(1.0f, 1.0f, 1.0f, alpha);
        buffer.addVertex(pose, x0, top, z0).setColor(1.0f, 1.0f, 1.0f, alpha);
        buffer.addVertex(pose, x1, top1, z1).setColor(1.0f, 1.0f, 1.0f, alpha);
        buffer.addVertex(pose, x1, bottom1, z1).setColor(1.0f, 1.0f, 1.0f, alpha);
    }
}
