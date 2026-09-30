package net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRGollumModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.character.LOTRGollumEntity;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

/**
 * LOTRRenderGollum: Gollum at 0.85 size, a caught fish in his mouth, and his
 * name always over his head within 64 blocks, half a block higher than
 * usual, unless he is speaking.
 *
 * <p>NOT ported yet: his health bar for his owner (with the hired units').
 */
public class LOTRGollumRenderer extends MobRenderer<LOTRGollumEntity, LOTRGollumRenderer.State, LOTRGollumModel> {

    private static final Identifier SKIN =
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "textures/entity/char/gollum.png");

    public static class State extends LivingEntityRenderState {
        public boolean sitting;
        public boolean fleeing;
        public boolean holdingFish;
        public final ItemStackRenderState fish = new ItemStackRenderState();
        public LOTRNPCRenderState.@org.jspecify.annotations.Nullable SpeechLines speech;
    }

    private final ItemModelResolver items;

    public LOTRGollumRenderer(EntityRendererProvider.Context context) {
        super(context, new LOTRGollumModel(LOTRGollumModel.createLayer().bakeRoot()), 0.5f);
        this.items = context.getItemModelResolver();
        LOTRGollumModel model = getModel();
        // renderEquippedItems: a fish, held in his mouth.
        addLayer(new RenderLayer<>(this) {
            @Override
            public void submit(PoseStack poseStack, SubmitNodeCollector collector, int light, State state,
                               float yRot, float xRot) {
                if (!state.holdingFish) {
                    return;
                }
                poseStack.pushPose();
                model.head.translateAndRotate(poseStack);
                poseStack.translate(0.21875f, 0.03125f, -0.375f);
                float f = 0.375f;
                poseStack.scale(f, f, f);
                poseStack.mulPose(Axis.ZP.rotationDegrees(60.0f));
                poseStack.mulPose(Axis.XP.rotationDegrees(-50.0f));
                state.fish.submit(poseStack, collector, light, OverlayTexture.NO_OVERLAY, state.outlineColor);
                poseStack.popPose();
            }
        });
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    protected boolean shouldShowName(LOTRGollumEntity gollum, double distanceSq) {
        return true;
    }

    @Override
    public void extractRenderState(LOTRGollumEntity gollum, State state, float partialTick) {
        super.extractRenderState(gollum, state, partialTick);
        state.sitting = gollum.isGollumSitting();
        state.fleeing = gollum.isGollumFleeing();
        ItemStack held = gollum.getMainHandItem();
        state.holdingFish = held.is(ItemTags.FISHES);
        if (state.holdingFish) {
            this.items.updateForLiving(state.fish, held, ItemDisplayContext.NONE, gollum);
        }
        state.speech = LOTRNPCSpeechRendering.extract(gollum, getFont());
        if (state.speech != null) {
            state.nameTag = null;
        } else if (state.nameTagAttachment != null) {
            state.nameTagAttachment = state.nameTagAttachment.add(0.0, 0.5, 0.0);
        }
    }

    @Override
    public Identifier getTextureLocation(State state) {
        return SKIN;
    }

    @Override
    protected void scale(State state, PoseStack poseStack) {
        float scale = 0.85f;
        poseStack.scale(scale, scale, scale);
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        super.submit(state, poseStack, collector, camera);
        if (state.speech != null && state.distanceToCameraSq <= LOTRNPCSpeechRendering.NAME_TAG_RANGE * LOTRNPCSpeechRendering.NAME_TAG_RANGE) {
            LOTRNPCSpeechRendering.submit(getFont(), state.boundingBoxHeight, state.speech, poseStack, collector, camera);
        }
    }
}
