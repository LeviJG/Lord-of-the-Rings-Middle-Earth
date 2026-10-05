package net.blueskiez77.lord_of_the_rings__middle_earth.client.hud;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

import org.jspecify.annotations.Nullable;

/**
 * LOTRModelCompass: the on-screen compass -- a dial tilted back 40 degrees,
 * turned with the player, ringed by the Ring Portal's ring of 60 blocks and
 * two bands of its writing (LOTRModelPortal) -- drawn into the HUD as a
 * picture in picture. Units are GUI pixels, as the original's were.
 */
public class LOTRCompassRenderer extends PictureInPictureRenderer<LOTRCompassRenderer.State> {

    private static final Identifier COMPASS = Identifier.fromNamespaceAndPath("lotr", "misc/compass.png");
    /** LOTRRenderPortal.ringTexture / writingTexture. */
    private static final Identifier RING = Identifier.fromNamespaceAndPath("lotr", "misc/portal.png");
    private static final Identifier WRITING = Identifier.fromNamespaceAndPath("lotr", "misc/portal_writing.png");
    private static final int LIGHT = 0xF000F0;
    private static final int PARTS = 60;

    public record State(float rotation, int x0, int y0, int x1, int y1, float scale, @Nullable ScreenRectangle scissorArea,
                        @Nullable ScreenRectangle bounds) implements PictureInPictureRenderState {
        public State(float rotation, int x0, int y0, int x1, int y1) {
            this(rotation, x0, y0, x1, y1, 1.0f, null, PictureInPictureRenderState.getBounds(x0, y0, x1, y1, null));
        }
    }

    @Override
    public Class<State> getRenderStateClass() {
        return State.class;
    }

    /** Centred in the picture, not at its foot. */
    @Override
    protected float getTranslateY(int height, int guiScale) {
        return height / 2.0f;
    }

    @Override
    protected String getTextureLabel() {
        return "lotr_compass";
    }

    @Override
    protected void renderToTexture(State state, PoseStack poseStack, SubmitNodeCollector collector) {
        poseStack.pushPose();
        poseStack.scale(1.0f, 1.0f, -1.0f);
        poseStack.mulPose(Axis.XP.rotationDegrees(40.0f));
        poseStack.mulPose(Axis.YP.rotationDegrees(state.rotation()));
        // compass.render(scale * 2): the 32x32 dial, flat, both faces.
        collector.submitCustomGeometry(poseStack, RenderTypes.entityCutout(COMPASS), (pose, vc) -> {
            float r = 32.0f;
            quad(vc, pose, r, 0, r, 1, 0, -r, 0, r, 0, 0, -r, 0, -r, 0, 1, r, 0, -r, 1, 1);
        });
        // The ring: 60 blocks 4x7x3, 38 out, each turned a sixtieth further.
        for (int i = 0; i < PARTS; ++i) {
            poseStack.pushPose();
            poseStack.mulPose(Axis.YP.rotation(i / (float) PARTS * (float) Math.PI * 2.0f));
            collector.submitCustomGeometry(poseStack, RenderTypes.entityCutout(RING),
                    (pose, vc) -> box(vc, pose, -2.0f, -3.5f, -38.0f, 4, 7, 3, 64, 32));
            poseStack.popPose();
        }
        writing(poseStack, collector, 1.05f);
        writing(poseStack, collector, 0.85f);
        poseStack.popPose();
    }

    /** LOTRModelPortal's script: 60 strips of the writing round the ring, mirrored. */
    private static void writing(PoseStack poseStack, SubmitNodeCollector collector, float f5) {
        poseStack.pushPose();
        poseStack.scale(-1.0f, 1.0f, 1.0f);
        collector.submitCustomGeometry(poseStack, RenderTypes.entityCutout(WRITING), (pose, vc) -> {
            double depth = 38.0;
            double halfX = 2.0;
            double halfY = 2.5;
            double[][] parts = {{halfX, -halfY, -depth}, {-halfX, -halfY, -depth}, {-halfX, halfY, -depth}, {halfX, halfY, -depth}};
            for (int i = 0; i < PARTS; ++i) {
                double rotate = -(i / (double) PARTS * Math.PI * 2.0);
                float[][] p = new float[4][];
                for (int j = 0; j < 4; ++j) {
                    // Vec3.rotateAroundY.
                    double cos = Math.cos(rotate);
                    double sin = Math.sin(rotate);
                    double x = parts[j][0] * cos + parts[j][2] * sin;
                    double z = parts[j][2] * cos - parts[j][0] * sin;
                    p[j] = new float[]{(float) x * f5, (float) parts[j][1] * f5, (float) z * f5};
                }
                float uMin = i / (float) PARTS;
                float uMax = (i + 1) / (float) PARTS;
                quad(vc, pose, p[0][0], p[0][1], p[0][2], uMax, 0.0f, p[1][0], p[1][1], p[1][2], uMin, 0.0f,
                        p[2][0], p[2][1], p[2][2], uMin, 1.0f, p[3][0], p[3][1], p[3][2], uMax, 1.0f);
            }
        });
        poseStack.popPose();
    }

    /** ModelBox: a box with 1.7.10's texture layout, its offset at (0, 0). */
    private static void box(VertexConsumer vc, PoseStack.Pose pose, float x, float y, float z, int w, int h, int d,
                            float texW, float texH) {
        float x2 = x + w;
        float y2 = y + h;
        float z2 = z + d;
        float[][] v = {{x, y, z}, {x2, y, z}, {x2, y2, z}, {x, y2, z}, {x, y, z2}, {x2, y, z2}, {x2, y2, z2}, {x, y2, z2}};
        face(vc, pose, v[5], v[1], v[2], v[6], d + w, d, d + w + d, d + h, texW, texH);
        face(vc, pose, v[0], v[4], v[7], v[3], 0, d, d, d + h, texW, texH);
        face(vc, pose, v[5], v[4], v[0], v[1], d, 0, d + w, d, texW, texH);
        face(vc, pose, v[2], v[3], v[7], v[6], d + w, d, d + w + w, 0, texW, texH);
        face(vc, pose, v[1], v[0], v[3], v[2], d, d, d + w, d + h, texW, texH);
        face(vc, pose, v[4], v[5], v[6], v[7], d + w + d, d, d + w + d + w, d + h, texW, texH);
    }

    /** TexturedQuad: corners take (u2, v1), (u1, v1), (u1, v2), (u2, v2). */
    private static void face(VertexConsumer vc, PoseStack.Pose pose, float[] a, float[] b, float[] c, float[] e,
                             float u1, float v1, float u2, float v2, float texW, float texH) {
        quad(vc, pose, a[0], a[1], a[2], u2 / texW, v1 / texH, b[0], b[1], b[2], u1 / texW, v1 / texH,
                c[0], c[1], c[2], u1 / texW, v2 / texH, e[0], e[1], e[2], u2 / texW, v2 / texH);
    }

    private static void quad(VertexConsumer vc, PoseStack.Pose pose,
                             float x0, float y0, float z0, float u0, float v0, float x1, float y1, float z1, float u1, float v1,
                             float x2, float y2, float z2, float u2, float v2, float x3, float y3, float z3, float u3, float v3) {
        vertex(vc, pose, x0, y0, z0, u0, v0);
        vertex(vc, pose, x1, y1, z1, u1, v1);
        vertex(vc, pose, x2, y2, z2, u2, v2);
        vertex(vc, pose, x3, y3, z3, u3, v3);
    }

    private static void vertex(VertexConsumer vc, PoseStack.Pose pose, float x, float y, float z, float u, float v) {
        vc.addVertex(pose, x, y, z).setColor(-1).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LIGHT)
                .setNormal(pose, 0.0f, 1.0f, 0.0f);
    }
}
