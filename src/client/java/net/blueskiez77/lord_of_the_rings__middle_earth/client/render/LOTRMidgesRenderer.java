package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRMidgeModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal.LOTRMidgesEntity;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.Identifier;

/**
 * LOTRRenderMidges: no shadow, and each midge drawn at a fifth of the model's
 * size at its own place in the swarm (renderModel).
 */
public class LOTRMidgesRenderer extends MobRenderer<LOTRMidgesEntity, LOTRMidgesRenderState, LOTRMidgeModel> {

    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "textures/entity/midge.png");

    public LOTRMidgesRenderer(EntityRendererProvider.Context context) {
        super(context, new LOTRMidgeModel(LOTRMidgeModel.createBodyLayer().bakeRoot(), true), 0.0f);
        LOTRMidgeModel midge = new LOTRMidgeModel(LOTRMidgeModel.createBodyLayer().bakeRoot(), false);
        addLayer(new RenderLayer<>(this) {
            @Override
            public void submit(PoseStack poseStack, SubmitNodeCollector collector, int light,
                               LOTRMidgesRenderState state, float yRot, float xRot) {
                float[] m = state.midges;
                for (int i = 0; i + 3 < m.length; i += 4) {
                    poseStack.pushPose();
                    poseStack.translate(m[i], m[i + 1], m[i + 2]);
                    poseStack.mulPose(Axis.YP.rotationDegrees(m[i + 3]));
                    poseStack.scale(0.2f, 0.2f, 0.2f);
                    coloredCutoutModelCopyLayerRender(midge, TEXTURE, poseStack, collector, light, state, -1, 1);
                    poseStack.popPose();
                }
            }
        });
    }

    @Override
    public LOTRMidgesRenderState createRenderState() {
        return new LOTRMidgesRenderState();
    }

    @Override
    public void extractRenderState(LOTRMidgesEntity entity, LOTRMidgesRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        float[] m = new float[entity.midges.length * 4];
        for (int i = 0; i < entity.midges.length; ++i) {
            LOTRMidgesEntity.Midge midge = entity.midges[i];
            m[i * 4] = midge.posX;
            m[i * 4 + 1] = midge.prevPosY + (midge.posY - midge.prevPosY) * partialTick;
            m[i * 4 + 2] = midge.posZ;
            m[i * 4 + 3] = midge.rotation;
        }
        state.midges = m;
    }

    @Override
    public Identifier getTextureLocation(LOTRMidgesRenderState state) {
        return TEXTURE;
    }
}
