package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import java.util.EnumMap;
import java.util.Map;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRBannerModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBannerType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRBannerBlockEntity;

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
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.WallBannerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

/**
 * LOTRRenderBanner, moved from an entity renderer onto the block entity.
 *
 * <p>The original's matrix was {@code translate(x, y + 1.5, z)}, a
 * {@code scale(-1, -1, 1)}, a yaw of {@code 180 - rotationYaw} and then a
 * hair's-breadth {@code translate(0, 0.01, 0)} to keep the cloth off the post.
 * All of it is kept; only the source of the yaw changes, since a banner is now
 * a block and its facing comes from a block state rather than an entity's
 * rotation.
 */
public class LOTRBannerRenderer
        implements BlockEntityRenderer<LOTRBannerBlockEntity, LOTRBannerRenderState> {

    /** Was lotr:item/banner/stand.png -- the base and post, shared by all of them. */
    private static final Identifier STAND = Identifier.fromNamespaceAndPath(
            LOTRMod.NAMESPACE, "textures/entity/banner/stand.png");

    private static final Map<LOTRBannerType, Identifier> CLOTH =
            new EnumMap<>(LOTRBannerType.class);

    static {
        for (LOTRBannerType type : LOTRBannerType.values()) {
            CLOTH.put(type, Identifier.fromNamespaceAndPath(
                    LOTRMod.NAMESPACE, "textures/entity/banner/" + type.textureName() + ".png"));
        }
    }

    private final LOTRBannerModel model;

    public LOTRBannerRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new LOTRBannerModel(LOTRBannerModel.createLayer().bakeRoot());
    }

    @Override
    public LOTRBannerRenderState createRenderState() {
        return new LOTRBannerRenderState();
    }

    @Override
    public void extractRenderState(LOTRBannerBlockEntity banner, LOTRBannerRenderState state,
            float partialTick, Vec3 cameraPos, ModelFeatureRenderer.CrumblingOverlay crumbling) {
        BlockEntityRenderState.extractBase(banner, state, crumbling);

        state.type = banner.getBannerType();
        BlockState blockState = banner.getBlockState();
        if (blockState.hasProperty(WallBannerBlock.FACING)) {
            // A wall banner hangs facing away from the wall.
            state.onWall = true;
            state.yaw = blockState.getValue(WallBannerBlock.FACING).toYRot();
        } else {
            // A standing one carries vanilla's sixteenth-turn rotation.
            state.onWall = false;
            state.yaw = blockState.hasProperty(BannerBlock.ROTATION)
                    ? blockState.getValue(BannerBlock.ROTATION) * 360.0f / 16.0f
                    : 0.0f;
        }
    }

    @Override
    public void submit(LOTRBannerRenderState state, PoseStack poseStack,
            SubmitNodeCollector collector, CameraRenderState camera) {
        int light = state.lightCoords;
        boolean onWall = state.onWall;

        // entityCutout, not entitySolid: doRender began with a
        // glDisable(GL_CULL_FACE). The cloth is a zero-thickness quad and would
        // vanish from one side entirely under culling.
        submitPart(poseStack, collector, STAND, light,
                (stack, consumer) -> model.renderPost(stack, consumer, light,
                        OverlayTexture.NO_OVERLAY, onWall),
                state, onWall);
        submitPart(poseStack, collector, CLOTH.get(state.type), light,
                (stack, consumer) -> model.renderCloth(stack, consumer, light,
                        OverlayTexture.NO_OVERLAY),
                state, onWall);
    }

    private interface PartRenderer {
        void render(PoseStack poseStack, com.mojang.blaze3d.vertex.VertexConsumer consumer);
    }

    private static void submitPart(PoseStack poseStack, SubmitNodeCollector collector,
            Identifier texture, int light, PartRenderer part,
            LOTRBannerRenderState state, boolean onWall) {
        collector.submitCustomGeometry(poseStack, RenderTypes.entityCutout(texture),
                (pose, consumer) -> {
                    // ModelPart.render wants a PoseStack and custom geometry
                    // hands over a single Pose; a local stack seeded with it is
                    // the cheapest way across, as the weapon rack does.
                    PoseStack local = new PoseStack();
                    local.last().set(pose);
                    applyBannerTransform(local, state.yaw, onWall);
                    part.render(local, consumer);
                });
    }

    private static void applyBannerTransform(PoseStack poseStack, float yaw, boolean onWall) {
        poseStack.translate(0.5f, 1.5f, 0.5f);
        poseStack.scale(-1.0f, -1.0f, 1.0f);
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0f - yaw));
        if (onWall) {
            // Hung flat against the wall behind it rather than stood in the
            // middle of its block, and dropped so the cloth sits where a
            // vanilla wall banner's does.
            poseStack.translate(0.0f, -0.25f, -0.4375f);
        }
        // The original's 0.01 nudge, keeping the cloth from z-fighting the post.
        poseStack.translate(0.0f, 0.01f, 0.0f);
    }
}
