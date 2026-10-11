package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import com.mojang.blaze3d.vertex.PoseStack;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRPortalBlockEntity;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;

/** LOTRRenderElvenPortal: the portal's surface, three quarters of a block up, seen from above. */
public class LOTRElvenPortalRenderer implements BlockEntityRenderer<LOTRPortalBlockEntity, BlockEntityRenderState> {

    public LOTRElvenPortalRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public BlockEntityRenderState createRenderState() {
        return new BlockEntityRenderState();
    }

    @Override
    public void submit(BlockEntityRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        collector.submitCustomGeometry(poseStack, LOTRPortalRenderTypes.ELVEN_PORTAL, (pose, buffer) -> {
            float y = 0.75f;
            buffer.addVertex(pose, 0.0f, y, 0.0f);
            buffer.addVertex(pose, 0.0f, y, 1.0f);
            buffer.addVertex(pose, 1.0f, y, 1.0f);
            buffer.addVertex(pose, 1.0f, y, 0.0f);
        });
    }
}
