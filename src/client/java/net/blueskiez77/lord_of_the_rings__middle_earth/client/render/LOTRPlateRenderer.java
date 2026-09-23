package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import java.util.Random;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTRPlateBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

/**
 * LOTRRenderPlateFood: the food piled on a plate, one flat sprite per item.
 *
 * <p>Each lies flat, 0.5625 of a block across, a thirty-second above the one
 * below it and starting an eighth up, turned by an angle seeded from the
 * plate's position and its place in the pile -- so a pile always looks the
 * same, and no two plates alike.
 */
public class LOTRPlateRenderer implements BlockEntityRenderer<LOTRPlateBlockEntity, LOTRPlateRenderState> {
    private final ItemModelResolver items;

    public LOTRPlateRenderer(BlockEntityRendererProvider.Context context) {
        this.items = context.itemModelResolver();
    }

    @Override
    public LOTRPlateRenderState createRenderState() {
        return new LOTRPlateRenderState();
    }

    @Override
    public void extractRenderState(LOTRPlateBlockEntity plate, LOTRPlateRenderState state, float partialTick,
            Vec3 cameraPos, ModelFeatureRenderer.CrumblingOverlay crumbling) {
        BlockEntityRenderState.extractBase(plate, state, crumbling);
        ItemStack food = plate.getFoodItem();
        state.food.clear();
        state.count = food.getCount();
        if (food.isEmpty()) {
            return;
        }
        items.updateForTopItem(state.food, food, ItemDisplayContext.NONE, plate.getLevel(), null, 0);
        BlockPos pos = plate.getBlockPos();
        state.rotations = new float[state.count];
        Random random = new Random();
        for (int l = 0; l < state.count; ++l) {
            random.setSeed(pos.getX() * 3129871L ^ pos.getZ() * 116129781L ^ pos.getY() + l * 5930563L);
            state.rotations[l] = random.nextFloat() * 360.0f;
        }
    }

    @Override
    public void submit(LOTRPlateRenderState state, PoseStack poseStack, SubmitNodeCollector collector,
            CameraRenderState camera) {
        if (state.food.isEmpty() || state.count <= 0) {
            return;
        }
        float lowerOffset = 0.125f;
        for (int l = 0; l < state.count && l < state.rotations.length; ++l) {
            poseStack.pushPose();
            float offset = lowerOffset;
            poseStack.translate(0.5f, offset, 0.5f);
            lowerOffset = offset + 0.03125f;
            poseStack.mulPose(Axis.YP.rotationDegrees(state.rotations[l]));
            poseStack.mulPose(Axis.XP.rotationDegrees(90.0f));
            poseStack.translate(-0.25f, -0.25f, 0.0f);
            poseStack.scale(0.5625f, 0.5625f, 0.5625f);
            // renderItemIn2D drew the sprite across 0..1 in x and y, from z = 0
            // back to -1/16. An item model is drawn centred on the origin
            // instead -- its display transform opens with a -0.5 translate --
            // so it is put back by half a block in x and y, and z is moved so
            // its sixteenth of depth sits behind z = 0 as the sprite did.
            poseStack.translate(0.5f, 0.5f, -0.03125f);
            state.food.submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }
    }
}
