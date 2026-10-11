package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRBalrogModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.config.LOTRConfig;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.utumno.LOTRBalrogEntity;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

/**
 * LOTRRenderBalrog: the Balrog at twice its model's size, leaning into a charge, its weapon held out
 * in its right hand. While it burns, a sheet of fire slides over it, brighter as it charges, and its
 * glowing skin shows through full bright.
 */
public class LOTRBalrogRenderer extends MobRenderer<LOTRBalrogEntity, LOTRBalrogRenderer.State, LOTRBalrogModel> {

    private static final LOTRRandomSkins SKINS = LOTRRandomSkins.loadSkinsList("lotr:mob/balrog/balrog", "balrog/balrog");
    private static final LOTRRandomSkins SKINS_BRIGHT = LOTRRandomSkins.loadSkinsList("lotr:mob/balrog/balrog_bright", "balrog/balrog_bright");
    private static final Identifier FIRE = Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "textures/entity/balrog/fire.png");
    private static final int FULL_BRIGHT = 0xF000F0;

    public static class State extends LivingEntityRenderState {
        public Identifier skin = FIRE;
        public Identifier skinBright = FIRE;
        public boolean wreathed;
        public boolean charging;
        public float lean;
        public boolean showWings;
        public int heldItemRight;
        public float attackTime;
        public final ItemStackRenderState heldItem = new ItemStackRenderState();
    }

    private final ItemModelResolver items;

    public LOTRBalrogRenderer(EntityRendererProvider.Context context) {
        super(context, new LOTRBalrogModel(LOTRBalrogModel.createLayer(0.0f).bakeRoot(), false), 0.5f);
        this.items = context.getItemModelResolver();
        LOTRBalrogModel fireModel = new LOTRBalrogModel(LOTRBalrogModel.createLayer(0.0f).bakeRoot(), true);
        LOTRBalrogModel brightModel = new LOTRBalrogModel(LOTRBalrogModel.createLayer(0.05f).bakeRoot(), false);
        LOTRBalrogModel main = getModel();
        // shouldRenderPass 1 and 2: the fire (added on, its texture sliding), then the bright skin.
        addLayer(new RenderLayer<>(this) {
            @Override
            public void submit(PoseStack poseStack, SubmitNodeCollector collector, int light, State state, float yRot, float xRot) {
                if (!state.wreathed || state.isInvisible) {
                    return;
                }
                float f1 = state.ageInTicks;
                float alpha = state.charging ? 0.6f + Mth.sin(f1 * 0.1f) * 0.15f : 0.3f + Mth.sin(f1 * 0.05f) * 0.15f;
                int tint = ARGB.colorFromFloat(1.0f, alpha, alpha, alpha);
                collector.order(1).submitModel(fireModel, state, poseStack, RenderTypes.energySwirl(FIRE, f1 * 0.01f, f1 * 0.01f),
                        FULL_BRIGHT, OverlayTexture.NO_OVERLAY, tint, null, state.outlineColor, null);
                collector.order(2).submitModel(brightModel, state, poseStack, RenderTypes.entityTranslucent(state.skinBright),
                        FULL_BRIGHT, OverlayTexture.NO_OVERLAY, -1, null, state.outlineColor, null);
            }
        });
        // renderEquippedItems: the weapon, in the right hand.
        addLayer(new RenderLayer<>(this) {
            @Override
            public void submit(PoseStack poseStack, SubmitNodeCollector collector, int light, State state, float yRot, float xRot) {
                if (state.heldItem.isEmpty()) {
                    return;
                }
                poseStack.pushPose();
                main.translateToRightArm(poseStack);
                poseStack.translate(-0.25f, 1.5f, -0.125f);
                float scale = 1.25f;
                poseStack.scale(scale, -scale, scale);
                poseStack.mulPose(Axis.XP.rotationDegrees(-100.0f));
                poseStack.mulPose(Axis.YP.rotationDegrees(45.0f));
                state.heldItem.submit(poseStack, collector, light, OverlayTexture.NO_OVERLAY, state.outlineColor);
                poseStack.popPose();
            }
        });
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(LOTRBalrogEntity balrog, State state, float partialTick) {
        super.extractRenderState(balrog, state, partialTick);
        state.skin = SKINS.getRandomSkin(balrog.getUUID());
        state.skinBright = SKINS_BRIGHT.getRandomSkin(balrog.getUUID());
        state.wreathed = balrog.isWreathedInFlame();
        state.charging = balrog.isBalrogCharging();
        state.lean = balrog.getInterpolatedChargeLean(partialTick);
        state.showWings = LOTRConfig.balrogWings && state.wreathed;
        state.attackTime = balrog.getAttackAnim(partialTick);
        ItemStack held = balrog.getMainHandItem();
        state.heldItemRight = held.isEmpty() ? 0 : 2;
        this.items.updateForLiving(state.heldItem, held, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, balrog);
    }

    /** preRenderCallback: twice the size, and the lean of a charge. */
    @Override
    protected void scale(State state, PoseStack poseStack) {
        float scale = 2.0f;
        poseStack.scale(scale, scale, scale);
        if (state.charging) {
            poseStack.mulPose(Axis.XP.rotationDegrees(state.lean * 35.0f));
        }
    }

    @Override
    public Identifier getTextureLocation(State state) {
        return state.skin;
    }
}
