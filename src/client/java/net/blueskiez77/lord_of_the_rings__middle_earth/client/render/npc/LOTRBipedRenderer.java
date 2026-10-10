package net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc;

import com.mojang.blaze3d.vertex.PoseStack;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.model.LOTRBipedModel;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRBannerBearer;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMiscItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRSlingItem;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;

import org.jspecify.annotations.Nullable;

/**
 * LOTRRenderBiped: an NPC drawn at 15/16 of its size, as a player is, with its
 * held items and armour, and anything it is saying over its head
 * (LOTRNPCRendering.renderSpeech).
 *
 * <p>Only a block or a mob head is drawn on its head, as renderEquippedItems
 * did; anything else there (a marriage ring) is left to the renderer.
 *
 * <p>Its cape, if it wears one, hangs from its shoulders (renderNPCCape). The
 * hired unit's icon and health bar are drawn over its head with the other
 * overhead marks (LOTROverheadRendering).
 *
 * <p>Its shield, if it bears one, is the shield layer's; its mini-quest marks
 * are drawn with the other overhead marks.
 *
 * <p>NOT ported yet: the special armour models (LOTRArmorModels) on NPCs.
 */
public abstract class LOTRBipedRenderer<T extends LOTRNPCEntity, S extends LOTRNPCRenderState, M extends LOTRBipedModel<S>>
        extends HumanoidMobRenderer<T, S, M> {

    public static final float PLAYER_SCALE = 0.9375f;

    protected LOTRBipedRenderer(EntityRendererProvider.Context context, M model, M babyModel, float shadow) {
        super(context, model, babyModel, shadow);
        addLayer(new LOTRNPCCapeLayer<>(this));
        addLayer(new net.blueskiez77.lord_of_the_rings__middle_earth.client.render.LOTRShieldLayer<>(this));
    }

    /** getCapeToRender: the NPC's own cape; a renderer may give its kind one. */
    protected @Nullable Identifier getCapeToRender(T npc) {
        return npc.npcCape;
    }

    @Override
    public void extractRenderState(T npc, S state, float partialTick) {
        super.extractRenderState(npc, state, partialTick);
        state.drunkard = npc.isDrunkard();
        state.holdingItem = !npc.getMainHandItem().isEmpty();
        state.heldItemRight = heldItemValue(npc, npc.getMainHandItem());
        state.aimedBow = state.heldItemRight == 3 && isRanged(npc.getMainHandItem());
        state.renderChest = npc.shouldRenderNPCChest();
        state.renderHair = npc.shouldRenderNPCHair();
        // getHeldItemLeft: a banner bearer's banner, raised; else a trader shows a
        // silver coin in its left hand. With a shield (not ported yet) it would
        // show it only unhired and out of a fight.
        ItemStack ownLeft = npc.getHeldItemLeft();
        if (!ownLeft.isEmpty()) {
            state.leftHandItemStack = ownLeft;
            this.itemModelResolver.updateForLiving(state.leftHandItemState, ownLeft,
                    ItemDisplayContext.THIRD_PERSON_LEFT_HAND, npc);
            state.heldItemLeft = heldItemValue(npc, ownLeft);
        } else if (npc instanceof LOTRBannerBearer bearer) {
            ItemStack banner = bearer.getBannerItem();
            state.leftHandItemStack = banner;
            this.itemModelResolver.updateForLiving(state.leftHandItemState, banner,
                    ItemDisplayContext.THIRD_PERSON_LEFT_HAND, npc);
            state.heldItemLeft = 3;
        } else if (npc.isTrader()) {
            ItemStack coin = new ItemStack(LOTRMiscItems.SILVER_COIN);
            state.leftHandItemStack = coin;
            this.itemModelResolver.updateForLiving(state.leftHandItemState, coin,
                    ItemDisplayContext.THIRD_PERSON_LEFT_HAND, npc);
            state.heldItemLeft = heldItemValue(npc, coin);
        } else {
            state.heldItemLeft = 0;
        }
        if (!(npc.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof BlockItem)) {
            state.headItem.clear();
        }
        state.cape = getCapeToRender(npc);
        state.speech = LOTRNPCSpeechRendering.extract(npc, getFont());
        state.questMarks = LOTRNPCQuestRendering.extract(npc, partialTick);
    }

    /** A bow, crossbow or sling: raised to aim in a fight (LOTRItemSpear aside). */
    private static boolean isRanged(ItemStack stack) {
        ItemUseAnimation use = stack.getUseAnimation();
        return use == ItemUseAnimation.BOW || use == ItemUseAnimation.CROSSBOW || stack.getItem() instanceof LOTRSlingItem;
    }

    /**
     * LOTRArmorModels.setupHeldItem: 1 for anything held, 3 for a ranged
     * weapon raised in a fight or for food being eaten (and for a banner,
     * which only a banner bearer's left hand carries).
     */
    private static int heldItemValue(LOTRNPCEntity npc, ItemStack stack) {
        if (stack.isEmpty()) {
            return 0;
        }
        if (isRanged(stack) && npc.isInCombatStance() || npc.isEating()) {
            return 3;
        }
        return 1;
    }

    /** preRenderCallback. */
    @Override
    protected void scale(S state, PoseStack poseStack) {
        poseStack.scale(PLAYER_SCALE, PLAYER_SCALE, PLAYER_SCALE);
    }

    @Override
    public void submit(S state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        super.submit(state, poseStack, collector, camera);
        if (state.questMarks != null) {
            LOTRNPCQuestRendering.submit(state.questMarks, state.boundingBoxHeight, poseStack, collector, camera);
        }
        if (state.speech != null && state.distanceToCameraSq <= LOTRNPCSpeechRendering.NAME_TAG_RANGE * LOTRNPCSpeechRendering.NAME_TAG_RANGE) {
            LOTRNPCSpeechRendering.submit(getFont(), state.boundingBoxHeight, state.speech, poseStack, collector, camera);
        }
    }
}
