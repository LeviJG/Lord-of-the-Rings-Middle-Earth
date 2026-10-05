package net.blueskiez77.lord_of_the_rings__middle_earth.client.hud;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRAlignmentBar;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRAlignmentValues;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFactionRank;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRRankOptions;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

/**
 * LOTRTickHandlerClient's alignment drawing: the bar (the faction's colour,
 * the ring at the player's progress through the rank, flashing as it
 * changes, the pledge ring if pledged; arrows either side in its control
 * zone), the rank limits beneath, the faction's name and the rank or value;
 * and the gold bordered text all of it is written in.
 */
public final class LOTRAlignmentBarRenderer {

    public static final Identifier ALIGNMENT = Identifier.fromNamespaceAndPath("lotr", "gui/alignment.png");
    private static final int ALIGNMENT_TEXT_COLOUR = 16772620;

    private LOTRAlignmentBarRenderer() {
    }

    private static int rgba(float r, float g, float b, float a) {
        return (Mth.clamp((int) (a * 255.0f), 0, 255) << 24) | ((int) (r * 255.0f) << 16) | ((int) (g * 255.0f) << 8)
                | (int) (b * 255.0f);
    }

    public static void renderAlignmentBar(GuiGraphicsExtractor graphics, float alignment, boolean isOtherPlayer,
            LOTRFaction faction, float x, float y, boolean renderFacName, boolean renderValue, boolean renderLimits,
            boolean renderLimitValues) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        boolean fem = LOTRRankOptions.useFeminineRanks(player);
        LOTRFactionRank rank = faction.getRank(alignment);
        boolean pledged = LOTRPlayerAlignments.isPledgedTo(player, faction);
        LOTRAlignmentTicker ticker = LOTRAlignmentTicker.forFaction(faction);
        LOTRAlignmentBar bar = LOTRAlignmentBar.compute(faction, alignment);
        float alignMin = bar.alignMin;
        float alignMax = bar.alignMax;
        float ringProgress = (alignment - alignMin) / (alignMax - alignMin);
        int barWidth = 232;
        int barHeight = 14;
        int activeBarWidth = 220;
        float[] factionColors = faction.getFactionRGB();
        int bx = Math.round(x - barWidth / 2.0f);
        int by = Math.round(y);
        graphics.blit(RenderPipelines.GUI_TEXTURED, ALIGNMENT, bx, by, 0.0F, 14.0F, barWidth, barHeight, 256, 256,
                rgba(factionColors[0], factionColors[1], factionColors[2], 1.0f));
        graphics.blit(RenderPipelines.GUI_TEXTURED, ALIGNMENT, bx, by, 0.0F, 0.0F, barWidth, barHeight, 256, 256);
        float ringProgressAdj = (ringProgress - 0.5f) * 2.0f;
        int ringSize = 16;
        int ringX = Math.round(x - ringSize / 2.0f + ringProgressAdj * activeBarWidth / 2.0f);
        int ringY = Math.round(y + barHeight / 2.0f - ringSize / 2.0f);
        int flashTick = ticker.flashTick;
        graphics.blit(RenderPipelines.GUI_TEXTURED, ALIGNMENT, ringX, ringY, 16 * Math.round(flashTick / 3.0f),
                pledged ? 212.0F : 36.0F, ringSize, ringSize, 256, 256);
        if (faction.isPlayableAlignmentFaction()) {
            float alpha;
            boolean definedZone;
            if (faction.inControlZone(player)) {
                alpha = 1.0f;
                definedZone = faction.inDefinedControlZone(player);
            } else {
                alpha = faction.getControlZoneAlignmentMultiplier(player);
                definedZone = true;
            }
            if (alpha > 0.0f) {
                int arrowSize = 14;
                int y0 = definedZone ? 60 : 88;
                int y1 = definedZone ? 74 : 102;
                int tint = rgba(factionColors[0], factionColors[1], factionColors[2], alpha);
                int white = rgba(1.0f, 1.0f, 1.0f, alpha);
                graphics.blit(RenderPipelines.GUI_TEXTURED, ALIGNMENT, bx - arrowSize, by, 0.0F, y1, arrowSize, arrowSize, 256, 256, tint);
                graphics.blit(RenderPipelines.GUI_TEXTURED, ALIGNMENT, bx + barWidth, by, arrowSize, y1, arrowSize, arrowSize, 256, 256, tint);
                graphics.blit(RenderPipelines.GUI_TEXTURED, ALIGNMENT, bx - arrowSize, by, 0.0F, y0, arrowSize, arrowSize, 256, 256, white);
                graphics.blit(RenderPipelines.GUI_TEXTURED, ALIGNMENT, bx + barWidth, by, arrowSize, y0, arrowSize, arrowSize, 256, 256, white);
            }
        }
        Font fr = mc.font;
        int textX = Math.round(x);
        int textY = Math.round(y + barHeight + 4.0f);
        if (renderLimits) {
            Component sMin = bar.rankMin.getShortNameWithGender(fem);
            Component sMax = bar.rankMax.getShortNameWithGender(fem);
            if (renderLimitValues) {
                sMin = Component.translatable("lotr.gui.factions.alignment.limits", sMin, LOTRAlignmentValues.formatAlignForDisplay(alignMin));
                sMax = Component.translatable("lotr.gui.factions.alignment.limits", sMax, LOTRAlignmentValues.formatAlignForDisplay(alignMax));
            }
            int limitsX = barWidth / 2 - 6;
            int xMin = Math.round(x - limitsX);
            int xMax = Math.round(x + limitsX);
            graphics.pose().pushMatrix();
            graphics.pose().scale(0.5f, 0.5f);
            drawAlignmentText(graphics, fr, xMin * 2 - fr.width(sMin) / 2, textY * 2, sMin, 1.0f);
            drawAlignmentText(graphics, fr, xMax * 2 - fr.width(sMax) / 2, textY * 2, sMax, 1.0f);
            graphics.pose().popMatrix();
        }
        if (renderFacName) {
            Component name = faction.factionName();
            drawAlignmentText(graphics, fr, textX - fr.width(name) / 2, textY, name, 1.0f);
        }
        if (renderValue) {
            Component alignS;
            float alignAlpha;
            int numericalTick = ticker.numericalTick;
            if (numericalTick > 0) {
                alignS = Component.literal(LOTRAlignmentValues.formatAlignForDisplay(alignment));
                alignAlpha = triangleWave(numericalTick, 0.7f, 1.0f, 30.0f);
                int fadeTick = 15;
                if (numericalTick < fadeTick) {
                    alignAlpha *= 0;
                }
            } else {
                alignS = rank.getShortNameWithGender(fem);
                alignAlpha = 1.0f;
            }
            drawAlignmentText(graphics, fr, textX - fr.width(alignS) / 2, textY + fr.lineHeight + 3, alignS, alignAlpha);
        }
    }

    /** LOTRFunctions.triangleWave. */
    private static float triangleWave(float t, float min, float max, float period) {
        return min + (max - min) * (Math.abs(t % period / period - 0.5f) * 2.0f);
    }

    /** renderAlignmentDrain: the drain icon, "-n" bordered on it. */
    public static void renderAlignmentDrain(GuiGraphicsExtractor graphics, Font fr, int x, int y, int numFactions, float alpha) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, ALIGNMENT, x, y, 0.0F, 128.0F, 16, 16, 256, 256, rgba(1.0f, 1.0f, 1.0f, alpha));
        Component s = Component.literal("-" + numFactions);
        drawBorderedText(graphics, fr, x + 8 - fr.width(s) / 2, y + 8 - fr.lineHeight / 2, s, 0xFFFFFF, alpha);
    }

    public static void drawAlignmentText(GuiGraphicsExtractor graphics, Font f, int x, int y, Component s, float alphaF) {
        drawBorderedText(graphics, f, x, y, s, ALIGNMENT_TEXT_COLOUR, alphaF);
    }

    public static void drawBorderedText(GuiGraphicsExtractor graphics, Font f, int x, int y, Component s, int color, float alphaF) {
        int alpha = Mth.clamp((int) (alphaF * 255.0f), 4, 255) << 24;
        int[][] offsets = {{-1, -1}, {0, -1}, {1, -1}, {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}};
        for (int[] o : offsets) {
            graphics.text(f, s, x + o[0], y + o[1], alpha, false);
        }
        graphics.text(f, s, x, y, color | alpha, false);
    }
}
