package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRKebabStandModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRKebabStandBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRKebabStandBlockEntity;

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
 * LOTRRenderKebabStand. The stand, and the turning lump of meat on its spit.
 *
 * <p>Two quirks carried over from the original, both easy to get wrong:
 *
 * <ul>
 *   <li>The flip here is {@code scale(-1, -1, 1)}, not the {@code (1, -1, -1)}
 *       the other block entity models in this port use, and it is applied
 *       AFTER the yaw rather than before. Both are reproduced in that order.</li>
 *   <li>The facing-to-angle mapping is its own: north 0, west 90, south 180,
 *       east 270. That is not {@code toYRot()} nor its half turn, so it is
 *       spelled out rather than derived.</li>
 * </ul>
 */
public class LOTRKebabStandRenderer
        implements BlockEntityRenderer<LOTRKebabStandBlockEntity, LOTRKebabStandRenderState> {

    private static final Identifier STAND_WOOD = texture("kebab_stand");
    private static final Identifier STAND_SAND = texture("kebab_stand_sand");
    private static final Identifier MEAT_RAW = texture("kebab_raw");
    private static final Identifier MEAT_COOKED = texture("kebab_cooked");

    private static Identifier texture(String name) {
        return Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "textures/entity/" + name + ".png");
    }

    private final LOTRKebabStandModel model;

    public LOTRKebabStandRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new LOTRKebabStandModel(
                LOTRKebabStandModel.createStandLayer().bakeRoot(),
                LOTRKebabStandModel.createMeatLayer().bakeRoot());
    }

    @Override
    public LOTRKebabStandRenderState createRenderState() {
        return new LOTRKebabStandRenderState();
    }

    @Override
    public void extractRenderState(LOTRKebabStandBlockEntity stand, LOTRKebabStandRenderState state,
                                   float partialTick, Vec3 cameraPos,
                                   ModelFeatureRenderer.CrumblingOverlay crumbling) {
        BlockEntityRenderState.extractBase(stand, state, crumbling);

        BlockState blockState = stand.getBlockState();
        if (!(blockState.getBlock() instanceof LOTRKebabStandBlock block)) {
            return;
        }
        state.standTexture = "sand".equals(block.variant()) ? STAND_SAND : STAND_WOOD;
        state.yaw = yawOf(blockState.getValue(LOTRKebabStandBlock.FACING));
        state.meatCount = stand.getMeatCount();
        state.cooked = stand.isCooked();
        state.spin = stand.getSpin(partialTick);
    }

    /** The original's metadata switch: 2/north 0, 4/west 90, 3/south 180, 5/east 270. */
    private static float yawOf(Direction facing) {
        return switch (facing) {
            case NORTH -> 0.0f;
            case WEST -> 90.0f;
            case SOUTH -> 180.0f;
            case EAST -> 270.0f;
            default -> 0.0f;
        };
    }

    @Override
    public void submit(LOTRKebabStandRenderState state, PoseStack poseStack,
                       SubmitNodeCollector collector, CameraRenderState cameraState) {
        int light = state.lightCoords;
        float yaw = state.yaw;
        int meatCount = state.meatCount;
        float spin = state.spin;

        collector.submitCustomGeometry(poseStack, RenderTypes.entityCutout(state.standTexture),
                (pose, consumer) -> {
                    PoseStack local = place(pose, yaw);
                    model.renderStand(local, consumer, light, OverlayTexture.NO_OVERLAY);
                });

        if (meatCount > 0) {
            Identifier meatTexture = state.cooked ? MEAT_COOKED : MEAT_RAW;
            collector.submitCustomGeometry(poseStack, RenderTypes.entityCutout(meatTexture),
                    (pose, consumer) -> {
                        PoseStack local = place(pose, yaw);
                        model.renderMeat(local, consumer, light, OverlayTexture.NO_OVERLAY,
                                meatCount, spin);
                    });
        }
    }

    /**
     * translate, THEN yaw, THEN the flip -- the original's order, which differs
     * from the totem's and the unsmeltery's. ModelPart.render needs a PoseStack
     * and custom geometry hands over one Pose, so a local stack is seeded here.
     */
    private static PoseStack place(PoseStack.Pose pose, float yaw) {
        PoseStack local = new PoseStack();
        local.last().set(pose);
        local.translate(0.5f, 1.5f, 0.5f);
        local.mulPose(Axis.YP.rotationDegrees(yaw));
        local.scale(-1.0f, -1.0f, 1.0f);
        return local;
    }
}
