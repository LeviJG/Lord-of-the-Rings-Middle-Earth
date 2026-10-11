package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import com.mojang.blaze3d.vertex.PoseStack;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRUtumnoPortalBlockEntity;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;

/**
 * LOTRRenderUtumnoPortal: the darkness welling up the shaft -- sixty sheets of black, half a block
 * apart, from the block above the portal up, each fainter than the last. Seen from 256 blocks off.
 */
public class LOTRUtumnoPortalRenderer implements BlockEntityRenderer<LOTRUtumnoPortalBlockEntity, BlockEntityRenderState> {

    public LOTRUtumnoPortalRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public BlockEntityRenderState createRenderState() {
        return new BlockEntityRenderState();
    }

    @Override
    public boolean shouldRenderOffScreen() {
        return true;
    }

    @Override
    public int getViewDistance() {
        return 256;
    }

    @Override
    public void submit(BlockEntityRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        int passes = 60;
        collector.submitCustomGeometry(poseStack, RenderTypes.debugQuads(), (pose, buffer) -> {
            float width = 0.5f;
            for (int i = 0; i < passes; ++i) {
                float y = 1.0f + i * 0.5f;
                float alpha = (float) (passes - i) / passes;
                buffer.addVertex(pose, 0.5f + width, y, 0.5f + width).setColor(0.0f, 0.0f, 0.0f, alpha);
                buffer.addVertex(pose, 0.5f + width, y, 0.5f - width).setColor(0.0f, 0.0f, 0.0f, alpha);
                buffer.addVertex(pose, 0.5f - width, y, 0.5f - width).setColor(0.0f, 0.0f, 0.0f, alpha);
                buffer.addVertex(pose, 0.5f - width, y, 0.5f + width).setColor(0.0f, 0.0f, 0.0f, alpha);
            }
        });
    }
}
