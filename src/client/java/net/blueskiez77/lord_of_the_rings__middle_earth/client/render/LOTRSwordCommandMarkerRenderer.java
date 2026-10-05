package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRSwordCommandMarkerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import org.jspecify.annotations.Nullable;

/**
 * LOTRRenderSwordCommandMarker: the command sword, point down, turned to face
 * the player, as a sword is held.
 */
public class LOTRSwordCommandMarkerRenderer
        extends EntityRenderer<LOTRSwordCommandMarkerEntity, LOTRSwordCommandMarkerRenderer.State> {

    public static class State extends EntityRenderState {
        final ItemStackRenderState sword = new ItemStackRenderState();
        float viewerYaw;
    }

    private final ItemModelResolver items;
    private @Nullable ItemStack sword;

    public LOTRSwordCommandMarkerRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.items = context.getItemModelResolver();
        this.shadowRadius = 0.0f;
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(LOTRSwordCommandMarkerEntity marker, State state, float partialTick) {
        super.extractRenderState(marker, state, partialTick);
        if (this.sword == null) {
            this.sword = new ItemStack(LOTRCombatItems.COMMAND_SWORD);
        }
        var viewer = Minecraft.getInstance().player;
        state.viewerYaw = viewer == null ? 0.0f : viewer.getViewYRot(partialTick);
        this.items.updateForNonLiving(state.sword, this.sword, ItemDisplayContext.NONE, marker);
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        poseStack.pushPose();
        // The original's placement: half its height up (yOffset), turned to the
        // viewer, tipped 135 degrees, then as an item held in the hand.
        poseStack.translate(0.0f, 0.25f, 0.0f);
        poseStack.mulPose(Axis.YP.rotationDegrees(-state.viewerYaw));
        poseStack.mulPose(Axis.ZP.rotationDegrees(135.0f));
        float scale = 1.2f;
        poseStack.translate(-0.75f * scale, 0.0f, 0.03125f * scale);
        poseStack.scale(scale, scale, scale);
        poseStack.translate(0.9375f, 0.0625f, 0.0f);
        poseStack.mulPose(Axis.ZP.rotationDegrees(-335.0f));
        poseStack.mulPose(Axis.YP.rotationDegrees(-50.0f));
        poseStack.translate(0.5f, 0.5f, 0.0f);
        state.sword.submit(poseStack, collector, 0xF000F0, OverlayTexture.NO_OVERLAY, state.outlineColor);
        poseStack.popPose();
        super.submit(state, poseStack, collector, camera);
    }
}
