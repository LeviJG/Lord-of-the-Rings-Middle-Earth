package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

/**
 * ItemRenderer.renderItemIn2D: a whole texture drawn as a 1.7.10 held item was -- the unit square
 * from (0, 0) to (1, 1), front and back a sixteenth apart, and a sliver for every row and column of
 * pixels between them, so it has depth.
 */
public final class LOTRItemIn2D {

    private LOTRItemIn2D() {
    }

    /** The texture, {@code width} by {@code height} pixels, in the given colour (ARGB) and light. */
    public static void submit(PoseStack poseStack, SubmitNodeCollector collector, Identifier texture, int width, int height,
                              int colour, int light) {
        float thickness = 0.0625f;
        // renderItemIn2D(tess, maxU, minV, minU, maxV, ...): the u flipped, as the original called it.
        float u1 = 1.0f;
        float v1 = 0.0f;
        float u2 = 0.0f;
        float v2 = 1.0f;
        collector.submitCustomGeometry(poseStack, RenderTypes.entityCutout(texture), (pose, vc) -> {
            quad(vc, pose, colour, light, 0, 0, 1,
                    0, 0, 0, u1, v2, 1, 0, 0, u2, v2, 1, 1, 0, u2, v1, 0, 1, 0, u1, v1);
            quad(vc, pose, colour, light, 0, 0, -1,
                    0, 1, -thickness, u1, v1, 1, 1, -thickness, u2, v1, 1, 0, -thickness, u2, v2, 0, 0, -thickness, u1, v2);
            float halfU = 0.5f * (u1 - u2) / width;
            float halfV = 0.5f * (v2 - v1) / height;
            for (int k = 0; k < width; ++k) {
                float x = (float) k / width;
                float u = u1 + (u2 - u1) * x - halfU;
                quad(vc, pose, colour, light, -1, 0, 0,
                        x, 0, -thickness, u, v2, x, 0, 0, u, v2, x, 1, 0, u, v1, x, 1, -thickness, u, v1);
                float x2 = x + 1.0f / width;
                quad(vc, pose, colour, light, 1, 0, 0,
                        x2, 1, -thickness, u, v1, x2, 1, 0, u, v1, x2, 0, 0, u, v2, x2, 0, -thickness, u, v2);
            }
            for (int k = 0; k < height; ++k) {
                float y = (float) k / height;
                float v = v2 + (v1 - v2) * y - halfV;
                float y2 = y + 1.0f / height;
                quad(vc, pose, colour, light, 0, 1, 0,
                        0, y2, 0, u1, v, 1, y2, 0, u2, v, 1, y2, -thickness, u2, v, 0, y2, -thickness, u1, v);
                quad(vc, pose, colour, light, 0, -1, 0,
                        1, y, 0, u2, v, 0, y, 0, u1, v, 0, y, -thickness, u1, v, 1, y, -thickness, u2, v);
            }
        });
    }

    private static void quad(VertexConsumer vc, PoseStack.Pose pose, int colour, int light, float nx, float ny, float nz,
                             float... v) {
        for (int i = 0; i < 4; ++i) {
            int o = i * 5;
            vc.addVertex(pose, v[o], v[o + 1], v[o + 2]).setColor(colour).setUv(v[o + 3], v[o + 4])
                    .setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, nx, ny, nz);
        }
    }
}
