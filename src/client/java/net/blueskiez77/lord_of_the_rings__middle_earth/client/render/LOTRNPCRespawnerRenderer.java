package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRNPCRespawnerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMiscItems;

import net.minecraft.client.Minecraft;
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
 * LOTRRenderNPCRespawner: for a creative player only, the respawner's icon two
 * blocks across, standing in the middle of its box and spinning. As with the
 * trader's coin, GROUND centres the model and halves it, so it is doubled
 * again.
 */
public class LOTRNPCRespawnerRenderer extends EntityRenderer<LOTRNPCRespawnerEntity, LOTRNPCRespawnerRenderer.State> {

    public static class State extends EntityRenderState {
        public final ItemStackRenderState icon = new ItemStackRenderState();
        public float spin;
        public boolean visible;
    }

    private final ItemModelResolver items;

    public LOTRNPCRespawnerRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.items = context.getItemModelResolver();
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(LOTRNPCRespawnerEntity entity, State state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.visible = Minecraft.getInstance().player != null && Minecraft.getInstance().player.isCreative();
        state.spin = Mth.rotLerp(partialTick, entity.prevSpawnerSpin, entity.spawnerSpin);
        this.items.updateForNonLiving(state.icon, new ItemStack(LOTRMiscItems.NPC_RESPAWNER), ItemDisplayContext.GROUND, entity);
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        if (!state.visible) {
            return;
        }
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(state.spin));
        poseStack.translate(0.0f, 0.5f, 0.0f);
        poseStack.scale(4.0f, 4.0f, 4.0f);
        state.icon.submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
        poseStack.popPose();
    }
}
