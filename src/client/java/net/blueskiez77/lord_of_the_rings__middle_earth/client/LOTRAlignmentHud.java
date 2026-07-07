package net.blueskiez77.lord_of_the_rings__middle_earth.client;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRAlignmentBar;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFactionRank;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

/**
 * On-screen alignment bar. Upgrades the earlier text-only readout into a real
 * progress bar toward the next rank, using the faithful rank-window math in
 * LOTRAlignmentBar.
 *
 * Rendering is deliberately texture-FREE: the original used a bitmap atlas
 * with raw GL11 blits, none of which port cleanly to 26.1. Instead we draw
 * with primitive fill() rectangles. When the texture atlas is ported later,
 * swap the fill() calls for blits; the math is reused.
 *
 * 26.1 API notes (verified against working project code):
 *  - Identifier lives in net.minecraft.resources.
 *  - HUD graphics param is GuiGraphicsExtractor; text via .text(...), boxes
 *    via .fill(x1, y1, x2, y2, argb). Colors are ARGB (0xFF......).
 *
 * Viewed faction is hardcoded to GONDOR until a selection system exists.
 */
public final class LOTRAlignmentHud {

    private static final Identifier ID =
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "alignment_hud");

    private static final int BAR_WIDTH = 120;
    private static final int BAR_HEIGHT = 6;
    private static final int MARKER_WIDTH = 3;
    private static final int MARGIN_TOP = 6;

    private static LOTRFaction viewedFaction = LOTRFaction.GONDOR;

    private LOTRAlignmentHud() {
    }

    public static void register() {
        HudElementRegistry.attachElementBefore(VanillaHudElements.CHAT, ID, LOTRAlignmentHud::render);
    }

    public static void setViewedFaction(LOTRFaction faction) {
        if (faction != null) {
            viewedFaction = faction;
        }
    }

    private static void render(GuiGraphicsExtractor graphics, DeltaTracker delta) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null || mc.options.hideGui) {
            return;
        }
        LOTRFaction faction = viewedFaction;
        if (faction == null) {
            return;
        }

        float alignment = LOTRPlayerAlignments.getAlignment(player, faction);
        LOTRFactionRank rank = LOTRPlayerAlignments.getRank(player, faction);
        LOTRAlignmentBar bar = LOTRAlignmentBar.compute(faction, alignment);

        int screenW = graphics.guiWidth();
        int centerX = screenW / 2;
        int barLeft = centerX - BAR_WIDTH / 2;
        int barTop = MARGIN_TOP;

        int factionColor = 0xFF000000 | faction.getFactionColor();
        int trackBg = 0x80000000;
        int white = 0xFFFFFFFF;

        graphics.fill(barLeft - 1, barTop - 1, barLeft + BAR_WIDTH + 1, barTop + BAR_HEIGHT + 1, trackBg);
        int fillWidth = Math.round(bar.progress * BAR_WIDTH);
        graphics.fill(barLeft, barTop, barLeft + fillWidth, barTop + BAR_HEIGHT, factionColor);

        int markerX = barLeft + fillWidth - MARKER_WIDTH / 2;
        graphics.fill(markerX, barTop - 1, markerX + MARKER_WIDTH, barTop + BAR_HEIGHT + 1, white);

        Component name = Component.translatable(faction.untranslatedFactionName());
        int nameWidth = mc.font.width(name);
        graphics.text(mc.font, name, centerX - nameWidth / 2, barTop - 11, factionColor, true);

        Component below = Component.literal(formatAlignment(alignment) + "  ").append(rank.getDisplayName());
        int belowWidth = mc.font.width(below);
        graphics.text(mc.font, below, centerX - belowWidth / 2, barTop + BAR_HEIGHT + 2, white, true);

        Component minLabel = bar.rankMin.getShortNameWithGender(false);
        Component maxLabel = bar.rankMax.getShortNameWithGender(false);
        graphics.text(mc.font, minLabel, barLeft, barTop + BAR_HEIGHT + 12, 0xFFBBBBBB, true);
        graphics.text(mc.font, maxLabel, barLeft + BAR_WIDTH - mc.font.width(maxLabel), barTop + BAR_HEIGHT + 12, 0xFFBBBBBB, true);
    }

    private static String formatAlignment(float alignment) {
        if (alignment == Math.floor(alignment)) {
            return String.valueOf((int) alignment);
        }
        return String.format("%.1f", alignment);
    }
}