package net.blueskiez77.lord_of_the_rings__middle_earth.client.render.npc;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import net.minecraft.util.FormattedCharSequence;

import org.jspecify.annotations.Nullable;

/**
 * LOTRNPCRendering.renderSpeech: the yellow name, and what the NPC is saying
 * in lines of 150, over its head, fading in its last second -- for any NPC
 * renderer, biped or not.
 */
public final class LOTRNPCSpeechRendering {

    /** RendererLivingEntity.NAME_TAG_RANGE. */
    public static final double NAME_TAG_RANGE = 64.0;
    private static final int SPEECH_WIDTH = 150;
    private static final float SPEECH_SCALE = 0.015f;

    private LOTRNPCSpeechRendering() {
    }

    /** What the NPC is saying now, split for drawing; null if nothing (or dead). */
    public static LOTRNPCRenderState.@Nullable SpeechLines extract(LOTRNPCEntity npc, Font font) {
        if (!npc.isAlive()) {
            LOTRSpeechClient.removeSpeech(npc);
            return null;
        }
        LOTRSpeechClient.TimedSpeech speech = LOTRSpeechClient.getSpeechFor(npc);
        if (speech == null) {
            return null;
        }
        List<FormattedCharSequence> lines = font.split(Component.literal(speech.getSpeech()), SPEECH_WIDTH);
        return new LOTRNPCRenderState.SpeechLines(
                npc.getName().copy().withStyle(ChatFormatting.YELLOW).getVisualOrderText(), lines, speech.getAge());
    }

    public static void submit(Font font, float boundingBoxHeight, LOTRNPCRenderState.SpeechLines speech,
                              PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        int fontHeight = font.lineHeight;
        float alpha = 0.8f;
        if (speech.age() < 0.1f) {
            alpha *= speech.age() / 0.1f;
        }
        int textColour = (int) (alpha * 255.0f) << 24 | 0xFFFFFF;
        int background = ARGB.colorFromFloat(0.25f * alpha, 0.0f, 0.0f, 0.0f);
        poseStack.pushPose();
        poseStack.translate(0.0f, boundingBoxHeight + 0.3f, 0.0f);
        poseStack.mulPose(camera.orientation);
        poseStack.scale(SPEECH_SCALE, -SPEECH_SCALE, SPEECH_SCALE);
        float y = -fontHeight * (3 + speech.lines().size());
        collector.submitText(poseStack, -font.width(speech.name()) / 2.0f, y, speech.name(), false,
                Font.DisplayMode.SEE_THROUGH, 0xF000F0, textColour, background, 0);
        y += fontHeight;
        for (FormattedCharSequence line : speech.lines()) {
            y += fontHeight;
            collector.submitText(poseStack, -font.width(line) / 2.0f, y, line, false,
                    Font.DisplayMode.SEE_THROUGH, 0xF000F0, textColour, background, 0);
        }
        poseStack.popPose();
    }
}
