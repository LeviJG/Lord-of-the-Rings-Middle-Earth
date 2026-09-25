package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRBarrelBoatEntity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;

/**
 * LOTRRenderEntityBarrel: the barrel item, half again its size, set half a
 * block up and turned to the barrel's heading, rocking when struck as the boat
 * did. No shadow (getShadowSize 0).
 */
public class LOTRBarrelBoatRenderer extends EntityRenderer<LOTRBarrelBoatEntity, LOTRBarrelBoatRenderer.State> {

    public static class State extends EntityRenderState {
        public float yaw;
        public float hurtTime;
        public float damage;
        public int hurtDir;
        public final ItemStackRenderState barrel = new ItemStackRenderState();
    }

    private final ItemModelResolver items;

    public LOTRBarrelBoatRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.items = context.getItemModelResolver();
        this.shadowRadius = 0.0f;
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(LOTRBarrelBoatEntity barrel, State state, float partialTick) {
        super.extractRenderState(barrel, state, partialTick);
        state.yaw = barrel.getYRot(partialTick);
        state.hurtTime = barrel.getHurtTime() - partialTick;
        state.damage = Math.max(0.0f, barrel.getDamage() - partialTick);
        state.hurtDir = barrel.getHurtDir();
        this.items.updateForNonLiving(state.barrel, barrel.getBarrelItem(), ItemDisplayContext.NONE, barrel);
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.translate(0.0f, 0.5f, 0.0f);
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0f - state.yaw));
        if (state.hurtTime > 0.0f) {
            poseStack.mulPose(Axis.XP.rotationDegrees(
                    Mth.sin(state.hurtTime) * state.hurtTime * state.damage / 10.0f * state.hurtDir));
        }
        poseStack.scale(1.5f, 1.5f, 1.5f);
        state.barrel.submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
        poseStack.popPose();
        super.submit(state, poseStack, collector, camera);
    }
}
