package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRGandalfFireballEntity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.util.LightCoordsUtil;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

/**
 * LOTRRenderGandalfFireball: a one-block sprite turned to the camera, at full
 * brightness, cycling through cells 24 to 27 of the mod's particles sheet --
 * a frame every five ticks (animationTick).
 */
public class LOTRGandalfFireballRenderer
        extends EntityRenderer<LOTRGandalfFireballEntity, LOTRGandalfFireballRenderState> {

    private static final Identifier PARTICLES =
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "textures/misc/particles.png");

    /** One 16 px cell of the 128 px sheet. */
    private static final float CELL = 16.0f / 128.0f;

    public LOTRGandalfFireballRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public LOTRGandalfFireballRenderState createRenderState() {
        return new LOTRGandalfFireballRenderState();
    }

    @Override
    public void extractRenderState(LOTRGandalfFireballEntity fireball, LOTRGandalfFireballRenderState state,
            float partialTick) {
        super.extractRenderState(fireball, state, partialTick);
        state.frame = fireball.tickCount / 5 % 4;
    }

    @Override
    public void submit(LOTRGandalfFireballRenderState state, PoseStack poseStack, SubmitNodeCollector collector,
            CameraRenderState camera) {
        int index = 24 + state.frame;
        float u0 = index % 8 * CELL;
        float v0 = index / 8 * CELL;
        float u1 = u0 + CELL;
        float v1 = v0 + CELL;
        int light = LightCoordsUtil.FULL_BRIGHT;
        poseStack.pushPose();
        poseStack.mulPose(camera.orientation);
        collector.submitCustomGeometry(poseStack, RenderTypes.entityCutout(PARTICLES), (pose, consumer) -> {
            vertex(consumer, pose, -0.5f, -0.25f, u0, v1, light);
            vertex(consumer, pose, 0.5f, -0.25f, u1, v1, light);
            vertex(consumer, pose, 0.5f, 0.75f, u1, v0, light);
            vertex(consumer, pose, -0.5f, 0.75f, u0, v0, light);
        });
        poseStack.popPose();
        super.submit(state, poseStack, collector, camera);
    }

    private static void vertex(VertexConsumer consumer, PoseStack.Pose pose, float x, float y, float u, float v,
            int light) {
        consumer.addVertex(pose, x, y, 0.0f).setColor(0xFFFFFFFF).setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0.0f, 1.0f, 0.0f);
    }
}
