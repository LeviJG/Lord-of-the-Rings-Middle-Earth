package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRSmokeShipModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRSmokeRingEntity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.DyeColor;

/**
 * LOTRRenderSmokeRing: a dyed puff of smoke that grows and fades, or, for the
 * mithril-touched pipe, a little ghostly ship.
 *
 * <p>The puff is the first cell of the mod's particles sheet, turned to face
 * the camera and tinted with the dye's colour (vanilla's own dye colours now,
 * where the original took the 1.7.10 fleece table). The ship was drawn with
 * texturing switched off, plain white at three quarters of the fade.
 */
public class LOTRSmokeRingRenderer extends EntityRenderer<LOTRSmokeRingEntity, LOTRSmokeRingRenderState> {

    private static final Identifier PARTICLES =
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "textures/misc/particles.png");

    /** drawSprite(0): one 16 px cell of the 128 px sheet. */
    private static final float CELL = 16.0f / 128.0f;

    private final LOTRSmokeShipModel ship;

    public LOTRSmokeRingRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.ship = new LOTRSmokeShipModel(LOTRSmokeShipModel.createLayer().bakeRoot());
    }

    @Override
    public LOTRSmokeRingRenderState createRenderState() {
        return new LOTRSmokeRingRenderState();
    }

    @Override
    public void extractRenderState(LOTRSmokeRingEntity ring, LOTRSmokeRingRenderState state, float partialTick) {
        super.extractRenderState(ring, state, partialTick);
        state.age = Math.min(1.0f, (ring.getSmokeAge() + partialTick) / LOTRSmokeRingEntity.MAX_AGE);
        state.colour = ring.getSmokeColour();
        state.yaw = ring.getYRot(partialTick);
        state.pitch = ring.getXRot(partialTick);
    }

    @Override
    public void submit(LOTRSmokeRingRenderState state, PoseStack poseStack, SubmitNodeCollector collector,
            CameraRenderState camera) {
        float opacity = 1.0f - state.age;
        poseStack.pushPose();
        if (state.colour == 16) {
            int colour = ARGB.colorFromFloat(opacity * 0.75f, 1.0f, 1.0f, 1.0f);
            poseStack.scale(0.3f, -0.3f, 0.3f);
            poseStack.mulPose(Axis.YP.rotationDegrees(state.yaw - 90.0f));
            poseStack.mulPose(Axis.ZN.rotationDegrees(state.pitch));
            poseStack.mulPose(Axis.YP.rotationDegrees(90.0f));
            collector.submitCustomGeometry(poseStack, RenderTypes.textBackground(), (pose, consumer) -> {
                PoseStack local = new PoseStack();
                local.last().set(pose);
                this.ship.render(local, consumer, state.lightCoords, OverlayTexture.NO_OVERLAY, colour);
            });
        } else {
            int dye = DyeColor.byId(state.colour).getTextureDiffuseColor();
            int colour = ARGB.color(Math.round(opacity * 255.0f), dye);
            float size = 0.1f + 0.9f * state.age;
            poseStack.scale(size, size, size);
            poseStack.mulPose(camera.orientation);
            collector.submitCustomGeometry(poseStack, RenderTypes.entityTranslucent(PARTICLES), (pose, consumer) -> {
                vertex(consumer, pose, -0.5f, -0.25f, 0.0f, CELL, colour, state.lightCoords);
                vertex(consumer, pose, 0.5f, -0.25f, CELL, CELL, colour, state.lightCoords);
                vertex(consumer, pose, 0.5f, 0.75f, CELL, 0.0f, colour, state.lightCoords);
                vertex(consumer, pose, -0.5f, 0.75f, 0.0f, 0.0f, colour, state.lightCoords);
            });
        }
        poseStack.popPose();
        super.submit(state, poseStack, collector, camera);
    }

    private static void vertex(com.mojang.blaze3d.vertex.VertexConsumer consumer, PoseStack.Pose pose,
            float x, float y, float u, float v, int colour, int light) {
        consumer.addVertex(pose, x, y, 0.0f).setColor(colour).setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0.0f, 1.0f, 0.0f);
    }
}
