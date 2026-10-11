package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRPortalEntity;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

/**
 * LOTRRenderPortal and LOTRModelPortal: the Ring portal -- a ring of sixty blocks of gold 38 pixels
 * out, tipped ten degrees and turning, with the ring's writing burning round it inside and out, full
 * bright. While it grows it is drawn to its share of its full size.
 */
public class LOTRPortalRenderer extends EntityRenderer<LOTRPortalEntity, LOTRPortalRenderer.State> {

    private static final Identifier RING_TEXTURE = Identifier.fromNamespaceAndPath("lotr", "misc/portal.png");
    private static final Identifier WRITING_TEXTURE = Identifier.fromNamespaceAndPath("lotr", "misc/portal_writing.png");
    private static final int PARTS = 60;
    private static final int FULL_BRIGHT = 0xF000F0;

    public static class State extends EntityRenderState {
        float scale;
        float rotation;
    }

    private final ModelPart ring;

    public LOTRPortalRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.ring = createRing().bakeRoot();
    }

    /** LOTRModelPortal(false): sixty 4x7x3 blocks, each turned a sixtieth further round. */
    private static LayerDefinition createRing() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        for (int i = 0; i < PARTS; ++i) {
            root.addOrReplaceChild("part" + i, CubeListBuilder.create().texOffs(0, 0).addBox(-2.0f, -3.5f, -38.0f, 4, 7, 3),
                    PartPose.rotation(0.0f, i / (float) PARTS * (float) Math.PI * 2.0f, 0.0f));
        }
        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(LOTRPortalEntity portal, State state, float partialTick) {
        super.extractRenderState(portal, state, partialTick);
        float portalScale = portal.getScale();
        state.scale = portalScale < LOTRPortalEntity.MAX_SCALE ? (portalScale + partialTick) / LOTRPortalEntity.MAX_SCALE : 1.0f;
        state.rotation = portal.getPortalRotation(partialTick);
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        int light = state.lightCoords;
        poseStack.pushPose();
        poseStack.translate(0.0f, 0.75f, 0.0f);
        poseStack.scale(1.0f, -1.0f, 1.0f);
        poseStack.scale(state.scale, state.scale, state.scale);
        poseStack.mulPose(Axis.YP.rotationDegrees(state.rotation));
        poseStack.mulPose(Axis.XP.rotationDegrees(10.0f));
        // glDisable(GL_CULL_FACE): both faces of everything.
        collector.submitCustomGeometry(poseStack, RenderTypes.entityCutout(RING_TEXTURE), (pose, consumer) -> {
            PoseStack local = new PoseStack();
            local.last().set(pose);
            this.ring.render(local, consumer, light, OverlayTexture.NO_OVERLAY);
        });
        writing(poseStack, collector, 1.05f);
        writing(poseStack, collector, 0.85f);
        poseStack.popPose();
        super.submit(state, poseStack, collector, camera);
    }

    /** LOTRModelPortal(true): sixty strips of the writing on a ring {@code scale} times the blocks', mirrored. */
    private static void writing(PoseStack poseStack, SubmitNodeCollector collector, float scale) {
        poseStack.pushPose();
        poseStack.scale(-1.0f, 1.0f, 1.0f);
        collector.submitCustomGeometry(poseStack, RenderTypes.entityCutout(WRITING_TEXTURE), (pose, vc) -> {
            float f5 = 0.0625f * scale;
            double depth = 38.0;
            double halfX = 2.0;
            double halfY = 2.5;
            double[][] parts = {{halfX, -halfY, -depth}, {-halfX, -halfY, -depth}, {-halfX, halfY, -depth}, {halfX, halfY, -depth}};
            float[] us = {1.0f, 0.0f, 0.0f, 1.0f};
            float[] vs = {0.0f, 0.0f, 1.0f, 1.0f};
            for (int i = 0; i < PARTS; ++i) {
                double rotate = -(i / (double) PARTS * Math.PI * 2.0);
                double cos = Math.cos(rotate);
                double sin = Math.sin(rotate);
                float uMin = i / (float) PARTS;
                float uMax = (i + 1) / (float) PARTS;
                for (int j = 0; j < 4; ++j) {
                    // Vec3.rotateAroundY.
                    double x = parts[j][0] * cos + parts[j][2] * sin;
                    double z = parts[j][2] * cos - parts[j][0] * sin;
                    vertex(vc, pose, (float) x * f5, (float) parts[j][1] * f5, (float) z * f5, us[j] == 1.0f ? uMax : uMin, vs[j]);
                }
            }
        });
        poseStack.popPose();
    }

    private static void vertex(VertexConsumer vc, PoseStack.Pose pose, float x, float y, float z, float u, float v) {
        vc.addVertex(pose, x, y, z).setColor(-1).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(FULL_BRIGHT)
                .setNormal(pose, 0.0f, 1.0f, 0.0f);
    }
}
