package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRInvasionSpawnerEntity;

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
 * LOTRRenderInvasionSpawner: the warband's icon, half as large again, spinning about the entity's
 * axis -- the item drawn flat from the entity's feet up and out to one side, as the original drew an
 * equipped item.
 */
public class LOTRInvasionSpawnerRenderer extends EntityRenderer<LOTRInvasionSpawnerEntity, LOTRInvasionSpawnerRenderer.State> {

    public static class State extends EntityRenderState {
        public final ItemStackRenderState icon = new ItemStackRenderState();
        public float spin;
    }

    private final ItemModelResolver items;

    public LOTRInvasionSpawnerRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.items = context.getItemModelResolver();
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(LOTRInvasionSpawnerEntity entity, State state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.spin = Mth.rotLerp(partialTick, entity.prevSpawnerSpin, entity.spawnerSpin);
        this.items.updateForNonLiving(state.icon, entity.getInvasionItem(), ItemDisplayContext.NONE, entity);
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(state.spin));
        float scale = 1.5f;
        poseStack.scale(scale, scale, scale);
        poseStack.translate(0.5f, 0.5f, 0.0f);
        state.icon.submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
        poseStack.popPose();
        super.submit(state, poseStack, collector, camera);
    }
}
