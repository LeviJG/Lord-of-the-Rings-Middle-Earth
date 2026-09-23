package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRPlateEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;

/**
 * LOTRRenderPlate: a thrown plate is the plate block, turned by its spin. The
 * original drew it through renderStandardInvBlock, which centres a block on the
 * origin, so it hangs half a block around the entity's position.
 */
public class LOTRPlateEntityRenderer extends EntityRenderer<LOTRPlateEntity, LOTRPlateEntityRenderState> {
    public LOTRPlateEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public LOTRPlateEntityRenderState createRenderState() {
        return new LOTRPlateEntityRenderState();
    }

    @Override
    public void extractRenderState(LOTRPlateEntity plate, LOTRPlateEntityRenderState state, float partialTick) {
        super.extractRenderState(plate, state, partialTick);
        state.yaw = plate.getYRot(partialTick);
        state.plate = plate.getPlateBlock();
    }

    @Override
    public void submit(LOTRPlateEntityRenderState state, PoseStack poseStack, SubmitNodeCollector collector,
            CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(-state.yaw));
        poseStack.translate(-0.5f, -0.5f, -0.5f);
        LOTRPlateGeometry.submit(poseStack, collector, state.plate, state.lightCoords);
        poseStack.popPose();
        super.submit(state, poseStack, collector, camera);
    }
}
