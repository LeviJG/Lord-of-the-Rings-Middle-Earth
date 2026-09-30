package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRTraderRespawnEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMiscItems;

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
import net.minecraft.world.item.ItemStack;

/**
 * LOTRRenderTraderRespawn: the silver coin, a block across when grown,
 * standing on its edge and spinning, bobbing as the trader's return nears.
 * The original drew the coin's icon with renderItemIn2D from the entity's
 * feet up; GROUND centres the coin's model and halves it, so it is raised by
 * half its size and doubled.
 */
public class LOTRTraderRespawnRenderer extends EntityRenderer<LOTRTraderRespawnEntity, LOTRTraderRespawnRenderer.State> {

    public static class State extends EntityRenderState {
        public final ItemStackRenderState coin = new ItemStackRenderState();
        public float spin;
        public float scale;
        public float bob;
    }

    private final ItemModelResolver items;

    public LOTRTraderRespawnRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.items = context.getItemModelResolver();
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(LOTRTraderRespawnEntity entity, State state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.spin = Mth.rotLerp(partialTick, entity.prevSpawnerSpin, entity.spawnerSpin);
        state.scale = entity.getScaleFloat(partialTick);
        state.bob = entity.getBobbingOffset(partialTick);
        this.items.updateForNonLiving(state.coin, new ItemStack(LOTRMiscItems.SILVER_COIN), ItemDisplayContext.GROUND, entity);
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(state.spin));
        poseStack.translate(0.0f, state.bob + 0.5f * state.scale, 0.0f);
        poseStack.scale(2.0f * state.scale, 2.0f * state.scale, 2.0f * state.scale);
        state.coin.submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
        poseStack.popPose();
        super.submit(state, poseStack, collector, camera);
    }
}
