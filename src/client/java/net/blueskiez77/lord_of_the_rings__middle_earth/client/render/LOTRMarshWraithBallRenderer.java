package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRMarshWraithBallEntity;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;

/**
 * LOTRRenderWraithBall: a block-wide sprite, three-quarters opaque, facing
 * the viewer, flickering through the sixteen frames of its 4x4 sheet.
 *
 * <p>The frame's row was computed with a float division, {@code (float)
 * index / 4 * 16}, so rows slide down a quarter-cell with each frame rather
 * than stepping; kept as it was.
 */
public class LOTRMarshWraithBallRenderer extends EntityRenderer<LOTRMarshWraithBallEntity, LOTRMarshWraithBallRenderState> {

    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE,
            "textures/entity/wraith/marsh_wraith_ball.png");

    public LOTRMarshWraithBallRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public LOTRMarshWraithBallRenderState createRenderState() {
        return new LOTRMarshWraithBallRenderState();
    }

    @Override
    public void extractRenderState(LOTRMarshWraithBallEntity ball, LOTRMarshWraithBallRenderState state, float partialTick) {
        super.extractRenderState(ball, state, partialTick);
        state.frame = ball.animationTick;
    }

    @Override
    public void submit(LOTRMarshWraithBallRenderState state, PoseStack poseStack, SubmitNodeCollector collector,
                       CameraRenderState camera) {
        int index = state.frame;
        float u0 = (index % 4 * 16) / 64.0f;
        float u1 = (index % 4 * 16 + 16) / 64.0f;
        float v0 = ((float) index / 4 * 16) / 64.0f;
        float v1 = ((float) index / 4 * 16 + 16) / 64.0f;
        int colour = ARGB.white(0.75f);
        int light = state.lightCoords;
        poseStack.pushPose();
        poseStack.mulPose(camera.orientation);
        collector.submitCustomGeometry(poseStack, RenderTypes.entityTranslucent(TEXTURE), (pose, consumer) -> {
            vertex(consumer, pose, -0.5f, -0.25f, u0, v1, colour, light);
            vertex(consumer, pose, 0.5f, -0.25f, u1, v1, colour, light);
            vertex(consumer, pose, 0.5f, 0.75f, u1, v0, colour, light);
            vertex(consumer, pose, -0.5f, 0.75f, u0, v0, colour, light);
        });
        poseStack.popPose();
        super.submit(state, poseStack, collector, camera);
    }

    private static void vertex(VertexConsumer consumer, PoseStack.Pose pose, float x, float y, float u, float v,
                               int colour, int light) {
        consumer.addVertex(pose, x, y, 0.0f).setColor(colour).setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0.0f, 1.0f, 0.0f);
    }
}
