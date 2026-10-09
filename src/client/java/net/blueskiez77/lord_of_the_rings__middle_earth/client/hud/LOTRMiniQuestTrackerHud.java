package net.blueskiez77.lord_of_the_rings__middle_earth.client.hud;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.quest.LOTRClientMiniQuests;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.config.LOTRConfig;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.quest.LOTRMiniQuest;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;

import org.jspecify.annotations.Nullable;

/**
 * LOTRGuiMiniquestTracker: the quest the player follows, in a corner (the top left, or with "Flip
 * quest tracker" the top right) -- its icon, a bar of its quest colour filling as it goes with its
 * progress on it, and under it the task, or that it failed or is done. A quest just finished stays
 * shown for ten seconds if nothing else is followed. Not while a screen is open or the debug screen
 * is up.
 */
public final class LOTRMiniQuestTrackerHud {

    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("lotr", "gui/quest/tracker.png");
    private static final int BAR_X = 16;
    private static final int BAR_Y = 10;
    private static final int BAR_WIDTH = 90;
    private static final int BAR_HEIGHT = 15;
    private static final int BAR_EDGE = 2;
    private static final int ICON_WIDTH = 20;
    private static final int ICON_HEIGHT = 20;
    private static final int GAP = 4;

    private static @Nullable LOTRMiniQuest trackedQuest;
    private static boolean holdingComplete;
    private static int completeTime;

    private LOTRMiniQuestTrackerHud() {
    }

    public static void init() {
        HudElementRegistry.addLast(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "miniquest_tracker"),
                LOTRMiniQuestTrackerHud::render);
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!client.isPaused()) {
                update(client);
            }
        });
    }

    private static void update(Minecraft mc) {
        if (mc.player == null) {
            trackedQuest = null;
            return;
        }
        if (trackedQuest != null && trackedQuest.isCompleted() && !holdingComplete) {
            completeTime = 200;
            holdingComplete = true;
        }
        LOTRMiniQuest current = LOTRClientMiniQuests.getTrackingMiniQuest();
        if (completeTime > 0 && current == null) {
            --completeTime;
        } else {
            trackedQuest = current;
            holdingComplete = false;
        }
    }

    private static void render(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        LOTRMiniQuest quest = trackedQuest;
        if (!LOTRConfig.enableQuestTracker || mc.player == null || quest == null || mc.gui.screen() != null
                || mc.getDebugOverlay().showDebugScreen()) {
            return;
        }
        Font fr = mc.font;
        boolean flip = LOTRConfig.trackingQuestRight;
        Component objective = quest.isFailed() ? quest.getQuestFailureShorthand()
                : quest.isCompleted() ? Component.translatable("lotr.gui.redBook.mq.diary.complete") : quest.getQuestObjective();
        Component progress = quest.getQuestProgressShorthand();
        int x = flip ? graphics.guiWidth() - BAR_X - ICON_WIDTH : BAR_X;
        int y = BAR_Y;
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0.0F, 0.0F, ICON_WIDTH, ICON_HEIGHT, 256, 256);
        int iconX = x + (ICON_WIDTH - 16) / 2;
        int iconY = y + (ICON_HEIGHT - 16) / 2;
        x = flip ? x - (BAR_WIDTH + GAP) : x + ICON_WIDTH + GAP;
        int meterWidth = Math.round((BAR_WIDTH - BAR_EDGE * 2) * quest.getCompletionFactor());
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x + BAR_EDGE, y, ICON_WIDTH + BAR_EDGE, BAR_HEIGHT,
                meterWidth, BAR_HEIGHT, 256, 256, 0xFF000000 | quest.getQuestColor());
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, ICON_WIDTH, 0.0F, BAR_WIDTH, BAR_HEIGHT, 256, 256);
        LOTRAlignmentBarRenderer.drawAlignmentText(graphics, fr, x + BAR_WIDTH / 2 - fr.width(progress) / 2,
                y + BAR_HEIGHT - BAR_HEIGHT / 2 - fr.lineHeight / 2, progress, 1.0f);
        int textY = y + BAR_HEIGHT + GAP;
        for (FormattedCharSequence line : fr.split(objective, BAR_WIDTH)) {
            graphics.text(fr, line, x, textY, 0xFFFFFFFF, false);
            textY += fr.lineHeight;
        }
        graphics.item(quest.getQuestIcon(), iconX, iconY);
    }
}
