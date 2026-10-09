package net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.quest.LOTRClientMiniQuests;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMiscItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.quest.LOTRMiniQuest;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import org.jspecify.annotations.Nullable;

/**
 * LOTRNPCRendering.renderQuestBook and renderQuestOffer: over an NPC whose quest the player is on, the
 * red book -- turning above its head within twelve blocks, beyond that a flat icon seen through walls,
 * growing with the distance and fading a little -- and over one with a quest to offer them (within
 * sixteen blocks), the quest-offer mark in the quest's colour. Neither while the NPC is speaking.
 */
public final class LOTRNPCQuestRendering {

    private static final int FULL_BRIGHT = 0xF000F0;
    private static final Identifier RED_BOOK = Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "textures/item/red_book.png");
    private static final Identifier QUEST_OFFER = Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "textures/misc/quest_offer.png");

    /** What an NPC shows of its quests, for one frame. */
    public record QuestMarks(@Nullable ItemStackRenderState book, float distance, float age, int offerColor) {
    }

    private LOTRNPCQuestRendering() {
    }

    public static @Nullable QuestMarks extract(LOTRNPCEntity npc, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.getCameraEntity() == null || LOTRSpeechClient.getSpeechFor(npc) != null) {
            return null;
        }
        float distance = mc.getCameraEntity().distanceTo(npc);
        if (LOTRClientMiniQuests.anyActiveQuestsFor(npc)) {
            ItemStackRenderState book = new ItemStackRenderState();
            mc.getItemModelResolver().updateForNonLiving(book, new ItemStack(LOTRMiscItems.RED_BOOK),
                    ItemDisplayContext.NONE, npc);
            return new QuestMarks(book, distance, npc.tickCount + partialTick, -1);
        }
        if (npc.isAlive() && npc.questInfo.clientIsOffering && distance <= 16.0f) {
            return new QuestMarks(null, distance, 0.0f, npc.questInfo.clientOfferColor);
        }
        return null;
    }

    public static void submit(QuestMarks marks, float height, PoseStack poseStack, SubmitNodeCollector collector,
                              CameraRenderState camera) {
        if (marks.book() != null) {
            submitBook(marks, height, poseStack, collector, camera);
        } else {
            poseStack.pushPose();
            poseStack.translate(0.0f, height + 1.0f, 0.0f);
            poseStack.mulPose(camera.orientation);
            float scale = 0.75f / 16.0f;
            poseStack.scale(scale, -scale, scale);
            icon(poseStack, collector, RenderTypes.text(QUEST_OFFER), 0xFF000000 | marks.offerColor());
            poseStack.popPose();
        }
    }

    private static void submitBook(QuestMarks marks, float height, PoseStack poseStack, SubmitNodeCollector collector,
                                   CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.translate(0.0f, height + 1.3f, 0.0f);
        if (marks.distance() <= LOTRMiniQuest.RENDER_HEAD_DISTANCE) {
            poseStack.mulPose(Axis.YP.rotationDegrees(marks.age() % 360.0f * 6.0f));
            marks.book().submit(poseStack, collector, FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 0);
        } else {
            float scale = (float) Math.pow(marks.distance() / LOTRMiniQuest.RENDER_HEAD_DISTANCE, 1.1);
            float alpha = (float) Math.pow(scale, -0.4);
            poseStack.mulPose(camera.orientation);
            scale /= 16.0f;
            poseStack.scale(scale, -scale, scale);
            int alphaBits = Mth.clamp((int) (alpha * 255.0f), 0, 255) << 24;
            icon(poseStack, collector, RenderTypes.textSeeThrough(RED_BOOK), alphaBits | 0xFFFFFF);
        }
        poseStack.popPose();
    }

    /** renderIcon(-8, -8, icon, 16, 16). */
    private static void icon(PoseStack poseStack, SubmitNodeCollector collector, RenderType type, int colour) {
        collector.submitCustomGeometry(poseStack, type, (pose, vc) -> {
            vc.addVertex(pose, -8.0f, 8.0f, 0.0f).setColor(colour).setUv(0.0f, 1.0f).setLight(FULL_BRIGHT);
            vc.addVertex(pose, 8.0f, 8.0f, 0.0f).setColor(colour).setUv(1.0f, 1.0f).setLight(FULL_BRIGHT);
            vc.addVertex(pose, 8.0f, -8.0f, 0.0f).setColor(colour).setUv(1.0f, 0.0f).setLight(FULL_BRIGHT);
            vc.addVertex(pose, -8.0f, -8.0f, 0.0f).setColor(colour).setUv(0.0f, 0.0f).setLight(FULL_BRIGHT);
        });
    }
}
