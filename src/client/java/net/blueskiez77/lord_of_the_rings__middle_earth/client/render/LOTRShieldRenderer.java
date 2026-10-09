package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.shield.LOTRShields;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ARGB;

/**
 * LOTRRenderShield: a shield as a flat slab a pixel of its sheet thick -- its face the sheet's left
 * half, its back the right -- worn on the back, or on the left arm turned out with a weapon in hand.
 * The shield's art sits in the middle of a 32 x 32 square drawn a block and a half across.
 */
public final class LOTRShieldRenderer {

    private static final int SHIELD_WIDTH = 32;
    private static final int SHIELD_HEIGHT = 32;
    private static final float MODELSCALE = 0.0625f;

    private LOTRShieldRenderer() {
    }

    /**
     * renderShield, placed by the body (on the back) or the left arm; {@code alpha} below 255 draws it
     * faint, as for an invisible player one can still see.
     */
    public static void submit(LOTRShields shield, PoseStack poseStack, SubmitNodeCollector collector, int light,
                              ModelPart body, ModelPart leftArm, boolean renderOnBack, boolean wearingChestplate,
                              int alpha) {
        poseStack.pushPose();
        if (renderOnBack) {
            body.translateAndRotate(poseStack);
        } else {
            leftArm.translateAndRotate(poseStack);
        }
        poseStack.scale(-1.5f, -1.5f, 1.5f);
        if (renderOnBack) {
            poseStack.translate(0.5f, -0.8f, 0.0f);
            poseStack.translate(0.0f, 0.0f, wearingChestplate ? 0.24f : 0.16f);
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0f));
        } else {
            // The original raised it further when blocking with a sword, which 26.2's swords do not do.
            poseStack.mulPose(Axis.YP.rotationDegrees(60.0f));
            poseStack.translate(-0.5f, -0.75f, 0.0f);
            poseStack.translate(0.0f, 0.0f, wearingChestplate ? -0.24f : -0.16f);
            poseStack.mulPose(Axis.ZP.rotationDegrees(-15.0f));
        }
        submitSlab(shield, poseStack, collector, light, alpha);
        poseStack.popPose();
    }

    /** The slab itself, from (0, 0) to (1, 1): its face, then its back, mirrored across. */
    public static void submitSlab(LOTRShields shield, PoseStack poseStack, SubmitNodeCollector collector, int light,
                                  int alpha) {
        RenderType renderType = alpha < 255 ? RenderTypes.entityTranslucent(shield.getTexture())
                : RenderTypes.entityCutout(shield.getTexture());
        int color = ARGB.color(alpha, 255, 255, 255);
        poseStack.pushPose();
        collector.submitCustomGeometry(poseStack, renderType, (pose, consumer) -> doRenderShield(pose, consumer, 0.0f, light, color));
        poseStack.translate(1.0f, 0.0f, 0.0f);
        poseStack.scale(-1.0f, 1.0f, 1.0f);
        collector.submitCustomGeometry(poseStack, renderType, (pose, consumer) -> doRenderShield(pose, consumer, 0.5f, light, color));
        poseStack.popPose();
    }

    /** doRenderShield: one half of the slab, its face from {@code f} across half the sheet. */
    private static void doRenderShield(PoseStack.Pose pose, VertexConsumer consumer, float f, int light, int color) {
        float minU = f;
        float maxU = 0.5f + f;
        float minV = 0.0f;
        float maxV = 1.0f;
        float depth1 = MODELSCALE * 0.5f * f;
        float depth2 = MODELSCALE * 0.5f * (0.5f + f);
        vertex(pose, consumer, 0.0f, 0.0f, depth1, maxU, maxV, 0.0f, 0.0f, 1.0f, light, color);
        vertex(pose, consumer, 1.0f, 0.0f, depth1, minU, maxV, 0.0f, 0.0f, 1.0f, light, color);
        vertex(pose, consumer, 1.0f, 1.0f, depth1, minU, minV, 0.0f, 0.0f, 1.0f, light, color);
        vertex(pose, consumer, 0.0f, 1.0f, depth1, maxU, minV, 0.0f, 0.0f, 1.0f, light, color);
        vertex(pose, consumer, 0.0f, 1.0f, depth2, maxU, minV, 0.0f, 0.0f, -1.0f, light, color);
        vertex(pose, consumer, 1.0f, 1.0f, depth2, minU, minV, 0.0f, 0.0f, -1.0f, light, color);
        vertex(pose, consumer, 1.0f, 0.0f, depth2, minU, maxV, 0.0f, 0.0f, -1.0f, light, color);
        vertex(pose, consumer, 0.0f, 0.0f, depth2, maxU, maxV, 0.0f, 0.0f, -1.0f, light, color);
        float f5 = 0.5f * (maxU - minU) / SHIELD_WIDTH;
        float f6 = 0.5f * (maxV - minV) / SHIELD_HEIGHT;
        for (int k = 0; k < SHIELD_WIDTH; ++k) {
            float f7 = (float) k / SHIELD_WIDTH;
            float f8 = maxU + (minU - maxU) * f7 - f5;
            vertex(pose, consumer, f7, 0.0f, depth2, f8, maxV, -1.0f, 0.0f, 0.0f, light, color);
            vertex(pose, consumer, f7, 0.0f, depth1, f8, maxV, -1.0f, 0.0f, 0.0f, light, color);
            vertex(pose, consumer, f7, 1.0f, depth1, f8, minV, -1.0f, 0.0f, 0.0f, light, color);
            vertex(pose, consumer, f7, 1.0f, depth2, f8, minV, -1.0f, 0.0f, 0.0f, light, color);
        }
        for (int k = 0; k < SHIELD_WIDTH; ++k) {
            float f7 = (float) k / SHIELD_WIDTH;
            float f8 = maxU + (minU - maxU) * f7 - f5;
            float f9 = f7 + 1.0f / SHIELD_WIDTH;
            vertex(pose, consumer, f9, 1.0f, depth2, f8, minV, 1.0f, 0.0f, 0.0f, light, color);
            vertex(pose, consumer, f9, 1.0f, depth1, f8, minV, 1.0f, 0.0f, 0.0f, light, color);
            vertex(pose, consumer, f9, 0.0f, depth1, f8, maxV, 1.0f, 0.0f, 0.0f, light, color);
            vertex(pose, consumer, f9, 0.0f, depth2, f8, maxV, 1.0f, 0.0f, 0.0f, light, color);
        }
        for (int k = 0; k < SHIELD_HEIGHT; ++k) {
            float f7 = (float) k / SHIELD_HEIGHT;
            float f8 = maxV + (minV - maxV) * f7 - f6;
            float f9 = f7 + 1.0f / SHIELD_HEIGHT;
            vertex(pose, consumer, 0.0f, f9, depth1, maxU, f8, 0.0f, 1.0f, 0.0f, light, color);
            vertex(pose, consumer, 1.0f, f9, depth1, minU, f8, 0.0f, 1.0f, 0.0f, light, color);
            vertex(pose, consumer, 1.0f, f9, depth2, minU, f8, 0.0f, 1.0f, 0.0f, light, color);
            vertex(pose, consumer, 0.0f, f9, depth2, maxU, f8, 0.0f, 1.0f, 0.0f, light, color);
        }
        for (int k = 0; k < SHIELD_HEIGHT; ++k) {
            float f7 = (float) k / SHIELD_HEIGHT;
            float f8 = maxV + (minV - maxV) * f7 - f6;
            vertex(pose, consumer, 1.0f, f7, depth1, minU, f8, 0.0f, -1.0f, 0.0f, light, color);
            vertex(pose, consumer, 0.0f, f7, depth1, maxU, f8, 0.0f, -1.0f, 0.0f, light, color);
            vertex(pose, consumer, 0.0f, f7, depth2, maxU, f8, 0.0f, -1.0f, 0.0f, light, color);
            vertex(pose, consumer, 1.0f, f7, depth2, minU, f8, 0.0f, -1.0f, 0.0f, light, color);
        }
    }

    private static void vertex(PoseStack.Pose pose, VertexConsumer consumer, float x, float y, float z, float u, float v,
                               float nx, float ny, float nz, int light, int color) {
        consumer.addVertex(pose, x, y, z).setColor(color).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light).setNormal(pose, nx, ny, nz);
    }
}
