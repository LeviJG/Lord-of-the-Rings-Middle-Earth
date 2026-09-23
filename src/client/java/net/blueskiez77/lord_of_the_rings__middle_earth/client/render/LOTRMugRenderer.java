package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRVesselModels;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRMugBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRMugBlockEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRDrinkItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRVessel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

/**
 * LOTRRenderMug: a set-down vessel, and the surface of whatever is in it.
 *
 * <p>The matrix is the original's: to the block's centre, flipped y-down for the
 * model, scaled to 0.75, turned by the facing (metadata 0..3 as 90, 180, 270,
 * 0 degrees), and the skull cup and horns a further quarter turn back.
 *
 * <p>The liquid is the drink's own liquid texture. Mugs, goblets and the skull
 * cup show a flat surface (renderMeniscus); the wine glass and the bottle are
 * see-through, so they show a little block of liquid instead (renderLiquid);
 * the horns carry their surface down the horn's bend.
 */
public class LOTRMugRenderer implements BlockEntityRenderer<LOTRMugBlockEntity, LOTRMugRenderState> {
    private static final float SCALE = 1.0f / 16.0f;

    private final LOTRVesselModels models = new LOTRVesselModels();

    public LOTRMugRenderer(BlockEntityRendererProvider.Context context) {
    }

    /** The drink's liquid texture -- the original's {@code <drink>_liquid} icon. */
    public static Identifier liquidTexture(ItemStack drink) {
        Identifier id = BuiltInRegistries.ITEM.getKey(drink.getItem());
        return Identifier.fromNamespaceAndPath(id.getNamespace(), "textures/entity/drink/" + id.getPath() + ".png");
    }

    private static Identifier vesselTexture(LOTRVessel vessel) {
        return Identifier.fromNamespaceAndPath("lotr", "textures/entity/vessel/" + vessel.getSerializedName() + ".png");
    }

    @Override
    public LOTRMugRenderState createRenderState() {
        return new LOTRMugRenderState();
    }

    @Override
    public void extractRenderState(LOTRMugBlockEntity mug, LOTRMugRenderState state, float partialTick,
            Vec3 cameraPos, ModelFeatureRenderer.CrumblingOverlay crumbling) {
        BlockEntityRenderState.extractBase(mug, state, crumbling);
        state.vessel = mug.getVessel();
        state.meta = mug.getBlockState().hasProperty(LOTRMugBlock.FACING)
                ? mug.getBlockState().getValue(LOTRMugBlock.FACING).get2DDataValue() : 0;
        ItemStack drink = mug.getMugItemForRender();
        state.liquid = !mug.isEmpty() && drink.getItem() instanceof LOTRDrinkItem ? liquidTexture(drink) : null;
    }

    @Override
    public void submit(LOTRMugRenderState state, PoseStack poseStack, SubmitNodeCollector collector,
            CameraRenderState camera) {
        LOTRVessel vessel = state.vessel;
        int light = state.lightCoords;
        poseStack.pushPose();
        poseStack.translate(0.5f, 0.0f, 0.5f);
        poseStack.scale(-1.0f, -1.0f, 1.0f);
        poseStack.scale(LOTRMugBlock.MUG_SCALE, LOTRMugBlock.MUG_SCALE, LOTRMugBlock.MUG_SCALE);
        poseStack.mulPose(Axis.YP.rotationDegrees(((state.meta + 1) % 4) * 90.0f));
        if (vessel == LOTRVessel.SKULL || vessel == LOTRVessel.HORN || vessel == LOTRVessel.HORN_GOLD) {
            poseStack.mulPose(Axis.YP.rotationDegrees(-90.0f));
        }
        if (state.liquid != null) {
            collector.submitCustomGeometry(poseStack, RenderTypes.entityTranslucent(state.liquid),
                    (pose, consumer) -> drawLiquid(pose, consumer, vessel, light));
        }
        ModelPart model = models.modelFor(vessel);
        collector.submitCustomGeometry(poseStack, RenderTypes.entityTranslucent(vesselTexture(vessel)),
                (pose, consumer) -> {
                    PoseStack local = new PoseStack();
                    local.last().set(pose);
                    model.render(local, consumer, light, OverlayTexture.NO_OVERLAY);
                });
        poseStack.popPose();
    }

    private void drawLiquid(PoseStack.Pose pose, VertexConsumer consumer, LOTRVessel vessel, int light) {
        switch (vessel) {
            case MUG, MUG_CLAY -> meniscus(pose, consumer, 6, 10, 2.0f, 7.0f, light);
            case GOBLET_GOLD, GOBLET_SILVER, GOBLET_COPPER, GOBLET_WOOD ->
                    meniscus(pose, consumer, 6, 9, 1.5f, 8.0f, light);
            case SKULL -> meniscus(pose, consumer, 5, 11, 3.0f, 9.0f, light);
            case GLASS -> liquidBox(pose, consumer, 6, 9, 6.0f, 9.0f, light);
            case BOTTLE -> liquidBox(pose, consumer, 6, 10, 1.0f, 5.0f, light);
            case HORN, HORN_GOLD -> {
                PoseStack local = new PoseStack();
                local.last().set(pose);
                models.applyHornLiquidTransform(local);
                meniscus(local.last(), consumer, 6, 9, -1.5f, 5.0f, light);
            }
            default -> {
            }
        }
    }

    /** renderMeniscus: a flat square of the liquid's pixels uvMin..uvMax, at a height. */
    private static void meniscus(PoseStack.Pose pose, VertexConsumer consumer, int uvMin, int uvMax, float width,
            float height, int light) {
        float w = width * SCALE;
        float h = -height * SCALE;
        float t0 = uvMin / 16.0f;
        float t1 = uvMax / 16.0f;
        LOTRPlateGeometry.vertex(pose, consumer, -w, h, w, t0, t1, 0, -1, 0, light);
        LOTRPlateGeometry.vertex(pose, consumer, w, h, w, t1, t1, 0, -1, 0, light);
        LOTRPlateGeometry.vertex(pose, consumer, w, h, -w, t1, t0, 0, -1, 0, light);
        LOTRPlateGeometry.vertex(pose, consumer, -w, h, -w, t0, t0, 0, -1, 0, light);
    }

    /**
     * renderLiquid: a box of liquid, (uvMax - uvMin) pixels across and centred,
     * standing from yMin to yMax pixels up.
     */
    private static void liquidBox(PoseStack.Pose pose, VertexConsumer consumer, int uvMin, int uvMax, float yMin,
            float yMax, int light) {
        float s = (uvMax - uvMin) / 2.0f * SCALE;
        float top = -yMax * SCALE;
        float bottom = -yMin * SCALE;
        float u0 = uvMin / 16.0f;
        float u1 = uvMax / 16.0f;
        float v0 = yMin / 16.0f;
        float v1 = yMax / 16.0f;
        // Top and bottom.
        LOTRPlateGeometry.vertex(pose, consumer, -s, top, -s, u0, u0, 0, -1, 0, light);
        LOTRPlateGeometry.vertex(pose, consumer, -s, top, s, u0, u1, 0, -1, 0, light);
        LOTRPlateGeometry.vertex(pose, consumer, s, top, s, u1, u1, 0, -1, 0, light);
        LOTRPlateGeometry.vertex(pose, consumer, s, top, -s, u1, u0, 0, -1, 0, light);
        LOTRPlateGeometry.vertex(pose, consumer, -s, bottom, s, u0, u1, 0, 1, 0, light);
        LOTRPlateGeometry.vertex(pose, consumer, -s, bottom, -s, u0, u0, 0, 1, 0, light);
        LOTRPlateGeometry.vertex(pose, consumer, s, bottom, -s, u1, u0, 0, 1, 0, light);
        LOTRPlateGeometry.vertex(pose, consumer, s, bottom, s, u1, u1, 0, 1, 0, light);
        // The four sides.
        LOTRPlateGeometry.vertex(pose, consumer, s, top, -s, u1, v1, 0, 0, -1, light);
        LOTRPlateGeometry.vertex(pose, consumer, s, bottom, -s, u1, v0, 0, 0, -1, light);
        LOTRPlateGeometry.vertex(pose, consumer, -s, bottom, -s, u0, v0, 0, 0, -1, light);
        LOTRPlateGeometry.vertex(pose, consumer, -s, top, -s, u0, v1, 0, 0, -1, light);
        LOTRPlateGeometry.vertex(pose, consumer, -s, top, s, u0, v1, 0, 0, 1, light);
        LOTRPlateGeometry.vertex(pose, consumer, -s, bottom, s, u0, v0, 0, 0, 1, light);
        LOTRPlateGeometry.vertex(pose, consumer, s, bottom, s, u1, v0, 0, 0, 1, light);
        LOTRPlateGeometry.vertex(pose, consumer, s, top, s, u1, v1, 0, 0, 1, light);
        LOTRPlateGeometry.vertex(pose, consumer, -s, top, -s, u0, v1, -1, 0, 0, light);
        LOTRPlateGeometry.vertex(pose, consumer, -s, bottom, -s, u0, v0, -1, 0, 0, light);
        LOTRPlateGeometry.vertex(pose, consumer, -s, bottom, s, u1, v0, -1, 0, 0, light);
        LOTRPlateGeometry.vertex(pose, consumer, -s, top, s, u1, v1, -1, 0, 0, light);
        LOTRPlateGeometry.vertex(pose, consumer, s, top, s, u1, v1, 1, 0, 0, light);
        LOTRPlateGeometry.vertex(pose, consumer, s, bottom, s, u1, v0, 1, 0, 0, light);
        LOTRPlateGeometry.vertex(pose, consumer, s, bottom, -s, u0, v0, 1, 0, 0, light);
        LOTRPlateGeometry.vertex(pose, consumer, s, top, -s, u0, v1, 1, 0, 0, light);
    }
}
