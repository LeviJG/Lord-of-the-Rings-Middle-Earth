package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRTrollTotemModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRTrollTotemBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRTrollTotemBlockEntity;

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
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

/**
 * LOTRRenderTrollTotem. Draws one of the three totem parts from the shared
 * entity model, oriented by the block's FACING, with the head's jaw yawning
 * open when the totem is ready to summon.
 *
 * <p>The original's matrix was {@code translate(x+0.5, y+1.5, z+0.5)} then
 * {@code scale(1, -1, -1)} -- the standard flip that takes an entity model,
 * which is built y-down from a rotation point at 24, into block space. It is
 * reproduced exactly, so the model's own numbers need no adjustment.
 */
public class LOTRTrollTotemRenderer
        implements BlockEntityRenderer<LOTRTrollTotemBlockEntity, LOTRTrollTotemRenderState> {

    /** Was lotr:item/trollTotem.png, which is not a legal texture path now. */
    private static final Identifier TEXTURE =
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "textures/entity/troll_totem.png");

    private final LOTRTrollTotemModel model;

    public LOTRTrollTotemRenderer(BlockEntityRendererProvider.Context context) {
        // Baked here rather than through a registered model layer: the mesh is
        // a compile-time constant, so there is nothing for a resource reload
        // to change.
        this.model = new LOTRTrollTotemModel(LOTRTrollTotemModel.createLayer().bakeRoot());
    }

    @Override
    public LOTRTrollTotemRenderState createRenderState() {
        return new LOTRTrollTotemRenderState();
    }

    @Override
    public void extractRenderState(LOTRTrollTotemBlockEntity totem, LOTRTrollTotemRenderState state,
                                   float partialTick, Vec3 cameraPos,
                                   ModelFeatureRenderer.CrumblingOverlay crumbling) {
        BlockEntityRenderState.extractBase(totem, state, crumbling);

        BlockState blockState = totem.getBlockState();
        if (!(blockState.getBlock() instanceof LOTRTrollTotemBlock block)) {
            return;
        }

        state.part = block.part();
        state.yaw = yawOf(blockState.getValue(LOTRTrollTotemBlock.FACING));
        state.jawDegrees = totem.getJawRotation(partialTick);
    }

    /**
     * The original mapped its two rotation bits to 180/270/0/90 degrees, where
     * bit value 0 was the player facing south. Those four are the facings'
     * own yRot turned half a circle, because the totem is carved to look BACK
     * at whoever set it up.
     */
    private static float yawOf(Direction facing) {
        return facing.toYRot() + 180.0f;
    }

    @Override
    public void submit(LOTRTrollTotemRenderState state, PoseStack poseStack,
                       SubmitNodeCollector collector, CameraRenderState cameraState) {
        int light = state.lightCoords;
        var part = state.part;
        float yaw = state.yaw;
        float jaw = state.jawDegrees;

        collector.submitCustomGeometry(poseStack, RenderTypes.entitySolid(TEXTURE), (pose, consumer) -> {
            // ModelPart.render needs a PoseStack, and custom geometry hands
            // over a single Pose; seeding a local stack with it is the cheapest
            // way across, and keeps the caller's stack untouched.
            PoseStack local = new PoseStack();
            local.last().set(pose);

            local.translate(0.5f, 1.5f, 0.5f);
            local.scale(1.0f, -1.0f, -1.0f);
            local.mulPose(Axis.YP.rotationDegrees(yaw));

            switch (part) {
                case HEAD -> model.renderHead(local, consumer, light, OverlayTexture.NO_OVERLAY, jaw);
                case BODY -> model.renderBody(local, consumer, light, OverlayTexture.NO_OVERLAY);
                case BASE -> model.renderBase(local, consumer, light, OverlayTexture.NO_OVERLAY);
            }
        });
    }
}
