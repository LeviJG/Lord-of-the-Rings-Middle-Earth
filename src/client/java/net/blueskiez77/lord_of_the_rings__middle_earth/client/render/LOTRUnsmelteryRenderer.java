package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRUnsmelteryModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRUnsmelteryBlockEntity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

/**
 * LOTRRenderUnsmeltery. The cauldron, swaying while it works, in one of two
 * textures depending on whether its fire is lit.
 *
 * <p>Same placement as the troll totem -- translate(0.5, 1.5, 0.5) then
 * scale(1, -1, -1) -- but the facing maps straight through
 * {@link net.minecraft.core.Direction#toYRot()} here, with no half turn: the
 * original's metadata cases were north 180, south 0, west 90, east 270, which
 * is exactly what toYRot returns.
 */
public class LOTRUnsmelteryRenderer
        implements BlockEntityRenderer<LOTRUnsmelteryBlockEntity, LOTRUnsmelteryRenderState> {

    private static final Identifier IDLE = Identifier.fromNamespaceAndPath(
            LOTRMod.NAMESPACE, "textures/entity/unsmeltery_idle.png");
    private static final Identifier ACTIVE = Identifier.fromNamespaceAndPath(
            LOTRMod.NAMESPACE, "textures/entity/unsmeltery_active.png");

    private final LOTRUnsmelteryModel model;

    public LOTRUnsmelteryRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new LOTRUnsmelteryModel(LOTRUnsmelteryModel.createLayer().bakeRoot());
    }

    @Override
    public LOTRUnsmelteryRenderState createRenderState() {
        return new LOTRUnsmelteryRenderState();
    }

    @Override
    public void extractRenderState(LOTRUnsmelteryBlockEntity unsmeltery, LOTRUnsmelteryRenderState state,
                                   float partialTick, Vec3 cameraPos,
                                   ModelFeatureRenderer.CrumblingOverlay crumbling) {
        BlockEntityRenderState.extractBase(unsmeltery, state, crumbling);

        BlockState blockState = unsmeltery.getBlockState();
        if (!(blockState.getBlock() instanceof AbstractFurnaceBlock)) {
            return;
        }
        state.yaw = blockState.getValue(AbstractFurnaceBlock.FACING).toYRot();
        state.lit = blockState.getValue(AbstractFurnaceBlock.LIT);
        state.rocking = unsmeltery.getRockingAmount(partialTick);
    }

    @Override
    public void submit(LOTRUnsmelteryRenderState state, PoseStack poseStack,
                       SubmitNodeCollector collector, CameraRenderState cameraState) {
        int light = state.lightCoords;
        float yaw = state.yaw;
        float rocking = state.rocking;

        collector.submitCustomGeometry(poseStack,
                RenderTypes.entitySolid(state.lit ? ACTIVE : IDLE), (pose, consumer) -> {
                    // ModelPart.render wants a PoseStack; custom geometry hands
                    // over one Pose, so seed a local stack from it.
                    PoseStack local = new PoseStack();
                    local.last().set(pose);

                    local.translate(0.5f, 1.5f, 0.5f);
                    local.scale(1.0f, -1.0f, -1.0f);
                    local.mulPose(Axis.YP.rotationDegrees(yaw));

                    model.render(local, consumer, light, OverlayTexture.NO_OVERLAY, rocking);
                });
    }
}
