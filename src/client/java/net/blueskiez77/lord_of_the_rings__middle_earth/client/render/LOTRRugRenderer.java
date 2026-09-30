package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRBearModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRGiraffeModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRLionModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRWargModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRBearRugEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRWargskinRugEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRGiraffeRugEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRLionRugEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRRugEntity;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

/**
 * LOTRRenderRugBase with LOTRModelLionRug and LOTRModelBearRug: the animal's
 * own model, its body squashed flat and its legs splayed out to the sides,
 * drawn double-sided in the animal's skin. {@link #drawLion} and
 * {@link #drawBear} are the two rug models' render methods, transform for
 * transform.
 */
public abstract class LOTRRugRenderer<T extends LOTRRugEntity> extends EntityRenderer<T, LOTRRugRenderState> {

    protected LOTRRugRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public static LOTRRugRenderer<LOTRLionRugEntity> lion(EntityRendererProvider.Context context) {
        LOTRLionModel model = new LOTRLionModel(LOTRLionModel.createBodyLayer().bakeRoot());
        splayLegs(model.leg1, model.leg2, model.leg3, model.leg4);
        return new LOTRRugRenderer<>(context) {
            @Override
            protected void extractRug(LOTRLionRugEntity rug, LOTRRugRenderState state) {
                state.texture = rug.getRugType() == LOTRLionRugEntity.RugType.LIONESS
                        ? LOTRLionRenderer.TEXTURE_LIONESS : LOTRLionRenderer.TEXTURE_LION;
            }

            @Override
            protected void draw(PoseStack pose, VertexConsumer consumer, int light) {
                drawLion(model, pose, consumer, light);
            }
        };
    }

    public static LOTRRugRenderer<LOTRBearRugEntity> bear(EntityRendererProvider.Context context) {
        LOTRBearModel model = new LOTRBearModel(LOTRBearModel.createBodyLayer().bakeRoot());
        splayLegs(model.leg1, model.leg2, model.leg3, model.leg4);
        return new LOTRRugRenderer<>(context) {
            @Override
            protected void extractRug(LOTRBearRugEntity rug, LOTRRugRenderState state) {
                state.texture = LOTRBearRenderer.getBearSkin(rug.getRugType());
            }

            @Override
            protected void draw(PoseStack pose, VertexConsumer consumer, int light) {
                // LOTRRenderBearRug.preRenderCallback: scaleBearModel.
                pose.scale(LOTRBearRenderer.SCALE, LOTRBearRenderer.SCALE, LOTRBearRenderer.SCALE);
                drawBear(model, pose, consumer, light);
            }
        };
    }

    public static LOTRRugRenderer<LOTRWargskinRugEntity> warg(EntityRendererProvider.Context context) {
        LOTRWargModel model = new LOTRWargModel(LOTRWargModel.createBodyLayer(0.0f).bakeRoot());
        splayLegs(model.leg1, model.leg2, model.leg3, model.leg4);
        return new LOTRRugRenderer<>(context) {
            @Override
            protected void extractRug(LOTRWargskinRugEntity rug, LOTRRugRenderState state) {
                state.texture = LOTRWargRenderer.getWargSkin(rug.getRugType());
            }

            @Override
            protected void draw(PoseStack pose, VertexConsumer consumer, int light) {
                drawWarg(model, pose, consumer, light);
            }
        };
    }

    public static LOTRRugRenderer<LOTRGiraffeRugEntity> giraffe(EntityRendererProvider.Context context) {
        LOTRGiraffeModel model = new LOTRGiraffeModel(LOTRGiraffeModel.createBodyLayer(0.0f).bakeRoot());
        model.setRiddenHeadNeckRotation();
        splayLegs(model.leg1, model.leg2, model.leg3, model.leg4);
        Identifier texture = Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "textures/entity/giraffe/giraffe.png");
        return new LOTRRugRenderer<>(context) {
            @Override
            protected void extractRug(LOTRGiraffeRugEntity rug, LOTRRugRenderState state) {
                state.texture = texture;
            }

            @Override
            protected void draw(PoseStack pose, VertexConsumer consumer, int light) {
                drawGiraffe(model, pose, consumer, light);
            }
        };
    }

    /** The rug models' setRotationAngles: every leg turned out flat. */
    private static void splayLegs(ModelPart leg1, ModelPart leg2, ModelPart leg3, ModelPart leg4) {
        leg1.xRot = 0.5235988f;
        leg1.zRot = 1.5707964f;
        leg2.xRot = 0.5235988f;
        leg2.zRot = -1.5707964f;
        leg3.xRot = -0.34906584f;
        leg3.zRot = 1.5707964f;
        leg4.xRot = -0.34906584f;
        leg4.zRot = -1.5707964f;
    }

    protected abstract void extractRug(T rug, LOTRRugRenderState state);

    protected abstract void draw(PoseStack pose, VertexConsumer consumer, int light);

    @Override
    public LOTRRugRenderState createRenderState() {
        return new LOTRRugRenderState();
    }

    @Override
    public void extractRenderState(T rug, LOTRRugRenderState state, float partialTick) {
        super.extractRenderState(rug, state, partialTick);
        state.yaw = rug.getYRot();
        extractRug(rug, state);
    }

    @Override
    public void submit(LOTRRugRenderState state, PoseStack poseStack, SubmitNodeCollector collector,
                       CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.scale(-1.0f, -1.0f, 1.0f);
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0f - state.yaw));
        int light = state.lightCoords;
        // entityCutout draws both sides, as the original's glDisable(GL_CULL_FACE).
        collector.submitCustomGeometry(poseStack, RenderTypes.entityCutout(state.texture), (pose, consumer) -> {
            PoseStack local = new PoseStack();
            local.last().set(pose);
            draw(local, consumer, light);
        });
        poseStack.popPose();
        super.submit(state, poseStack, collector, camera);
    }

    /** LOTRModelLionRug.render. */
    private static void drawLion(LOTRLionModel lion, PoseStack pose, VertexConsumer consumer, int light) {
        int overlay = OverlayTexture.NO_OVERLAY;
        pose.translate(0.0f, -0.4f, 0.0f);
        pose.pushPose();
        pose.scale(1.5f, 0.4f, 1.0f);
        lion.body.render(pose, consumer, light, overlay);
        pose.popPose();
        lion.tail.render(pose, consumer, light, overlay);
        pose.pushPose();
        pose.translate(0.0f, -0.1f, 0.1f);
        lion.head.render(pose, consumer, light, overlay);
        lion.mane.render(pose, consumer, light, overlay);
        pose.popPose();
        pose.translate(0.0f, 0.15f, 0.0f);
        pose.pushPose();
        pose.translate(-0.4f, 0.0f, 0.0f);
        lion.leg1.render(pose, consumer, light, overlay);
        lion.leg3.render(pose, consumer, light, overlay);
        pose.popPose();
        pose.pushPose();
        pose.translate(0.4f, 0.0f, 0.0f);
        lion.leg2.render(pose, consumer, light, overlay);
        lion.leg4.render(pose, consumer, light, overlay);
        pose.popPose();
    }

    /** LOTRModelGiraffeRug.render. */
    private static void drawGiraffe(LOTRGiraffeModel giraffe, PoseStack pose, VertexConsumer consumer, int light) {
        int overlay = OverlayTexture.NO_OVERLAY;
        pose.translate(0.0f, 0.1f, 0.0f);
        pose.pushPose();
        pose.scale(1.5f, 0.4f, 1.0f);
        giraffe.body.render(pose, consumer, light, overlay);
        giraffe.tail.render(pose, consumer, light, overlay);
        pose.popPose();
        pose.pushPose();
        pose.translate(0.0f, 0.6f, -0.2f);
        giraffe.head.render(pose, consumer, light, overlay);
        giraffe.neck.render(pose, consumer, light, overlay);
        pose.popPose();
        pose.pushPose();
        pose.translate(-0.25f, 0.0f, 0.0f);
        giraffe.leg1.render(pose, consumer, light, overlay);
        giraffe.leg3.render(pose, consumer, light, overlay);
        pose.popPose();
        pose.pushPose();
        pose.translate(0.25f, 0.0f, 0.0f);
        giraffe.leg2.render(pose, consumer, light, overlay);
        giraffe.leg4.render(pose, consumer, light, overlay);
        pose.popPose();
    }

    /** LOTRModelWargskinRug.render. */
    private static void drawWarg(LOTRWargModel warg, PoseStack pose, VertexConsumer consumer, int light) {
        int overlay = OverlayTexture.NO_OVERLAY;
        pose.translate(0.0f, -0.3f, 0.0f);
        pose.pushPose();
        pose.scale(1.5f, 0.4f, 1.0f);
        warg.body.render(pose, consumer, light, overlay);
        pose.popPose();
        warg.tail.render(pose, consumer, light, overlay);
        pose.pushPose();
        pose.translate(0.0f, -0.5f, 0.1f);
        warg.head.render(pose, consumer, light, overlay);
        pose.popPose();
        pose.pushPose();
        pose.translate(-0.3f, 0.0f, 0.0f);
        warg.leg1.render(pose, consumer, light, overlay);
        warg.leg3.render(pose, consumer, light, overlay);
        pose.popPose();
        pose.pushPose();
        pose.translate(0.3f, 0.0f, 0.0f);
        warg.leg2.render(pose, consumer, light, overlay);
        warg.leg4.render(pose, consumer, light, overlay);
        pose.popPose();
    }

    /** LOTRModelBearRug.render. */
    private static void drawBear(LOTRBearModel bear, PoseStack pose, VertexConsumer consumer, int light) {
        int overlay = OverlayTexture.NO_OVERLAY;
        pose.translate(0.0f, -0.35f, 0.0f);
        pose.pushPose();
        pose.scale(1.5f, 0.4f, 1.0f);
        bear.body.render(pose, consumer, light, overlay);
        pose.popPose();
        pose.pushPose();
        pose.translate(0.0f, -0.4f, 0.1f);
        bear.head.render(pose, consumer, light, overlay);
        pose.popPose();
        pose.pushPose();
        pose.translate(-0.3f, 0.0f, 0.0f);
        bear.leg1.render(pose, consumer, light, overlay);
        bear.leg3.render(pose, consumer, light, overlay);
        pose.popPose();
        pose.pushPose();
        pose.translate(0.3f, 0.0f, 0.0f);
        bear.leg2.render(pose, consumer, light, overlay);
        bear.leg4.render(pose, consumer, light, overlay);
        pose.popPose();
    }
}
