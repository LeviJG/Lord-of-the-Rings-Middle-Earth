package net.blueskiez77.lord_of_the_rings__middle_earth.client;

import java.util.ArrayList;
import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRAlignmentBar;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFactionRank;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

/**
 * The factions menu: a paged list of playable factions showing the player's
 * standing (alignment bar + rank) with each, and a pledge button for the
 * selected faction. Modern replacement for LOTRGuiFactions.
 *
 * Each faction row is a transparent Button widget rather than a custom
 * mouseClicked override, because the 26.1 mouseClicked signature changed;
 * letting widgets own their clicks avoids the signature entirely and is the
 * idiomatic 26.1 approach.
 *
 * Rendering is texture-free (primitive fill/text), same as the HUD bar.
 * 26.1: override extractRenderState(GuiGraphicsExtractor, mouseX, mouseY, delta).
 */
public class LOTRScreenFactions extends Screen {

    private static final int ROW_HEIGHT = 24;
    private static final int LIST_TOP = 40;
    private static final int ROW_HALF_WIDTH = 130;

    private final List<LOTRFaction> factions = new ArrayList<>();
    private int selectedIndex;
    private int scrollOffset;
    private int visibleRows;
    private Button pledgeButton;
    private Button prevButton;
    private Button nextButton;

    public LOTRScreenFactions() {
        super(Component.translatable("lotr.gui.factions.title"));
    }

    @Override
    protected void init() {
        factions.clear();
        factions.addAll(LOTRFaction.getPlayableAlignmentFactions());
        if (selectedIndex >= factions.size()) {
            selectedIndex = 0;
        }

        int listBottom = height - 40;
        visibleRows = Math.max(1, (listBottom - LIST_TOP) / ROW_HEIGHT);

        rebuildRowButtons();

        prevButton = Button.builder(Component.literal("\u25B2"), btn -> scroll(-1))
                .bounds(width / 2 + ROW_HALF_WIDTH + 4, LIST_TOP, 20, 20).build();
        nextButton = Button.builder(Component.literal("\u25BC"), btn -> scroll(1))
                .bounds(width / 2 + ROW_HALF_WIDTH + 4, LIST_TOP + 24, 20, 20).build();
        addRenderableWidget(prevButton);
        addRenderableWidget(nextButton);

        pledgeButton = Button.builder(Component.translatable("lotr.gui.factions.pledge"), btn -> onPledge())
                .bounds(width / 2 - 100, height - 30, 200, 20).build();
        addRenderableWidget(pledgeButton);

        addRenderableWidget(Button.builder(Component.translatable("gui.done"), btn -> onClose())
                .bounds(width - 90, 10, 80, 20).build());

        updatePledgeButton();
    }

    private void rebuildRowButtons() {
        for (int i = 0; i < visibleRows; i++) {
            int index = i + scrollOffset;
            if (index >= factions.size()) {
                break;
            }
            int rowY = LIST_TOP + i * ROW_HEIGHT;
            int rowIndex = index;
            // Transparent clickable row; visuals are drawn in extractRenderState.
            Button row = Button.builder(Component.empty(), btn -> selectRow(rowIndex))
                    .bounds(width / 2 - ROW_HALF_WIDTH, rowY, ROW_HALF_WIDTH * 2, ROW_HEIGHT - 2)
                    .build();
            row.setAlpha(0.0f);
            addRenderableWidget(row);
        }
    }

    private void selectRow(int index) {
        if (index >= 0 && index < factions.size()) {
            selectedIndex = index;
            LOTRAlignmentHud.setViewedFaction(factions.get(index));
            updatePledgeButton();
        }
    }

    private void scroll(int dir) {
        int maxScroll = Math.max(0, factions.size() - visibleRows);
        int next = Math.max(0, Math.min(maxScroll, scrollOffset + dir));
        if (next != scrollOffset) {
            scrollOffset = next;
            rebuild();
        }
    }

    private void rebuild() {
        this.clearWidgets();
        init();
    }

    private void onPledge() {
        Player player = minecraft != null ? minecraft.player : null;
        if (player == null || factions.isEmpty()) {
            return;
        }
        LOTRFaction faction = factions.get(selectedIndex);
        // Client-side reflection of state; a real pledge needs a server packet (deferred).
        if (LOTRPlayerAlignments.canPledgeTo(player, faction)) {
            LOTRPlayerAlignments.setPledgeFaction(player, faction);
            updatePledgeButton();
        }
    }

    private void updatePledgeButton() {
        Player player = minecraft != null ? minecraft.player : null;
        if (player == null || factions.isEmpty()) {
            pledgeButton.active = false;
            return;
        }
        LOTRFaction faction = factions.get(selectedIndex);
        pledgeButton.active = LOTRPlayerAlignments.canPledgeTo(player, faction)
                && !LOTRPlayerAlignments.isPledgedTo(player, faction);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        super.extractRenderState(graphics, mouseX, mouseY, delta);

        int titleWidth = font.width(title);
        graphics.text(font, title, width / 2 - titleWidth / 2, 16, 0xFFFFFFFF, true);

        Player player = minecraft != null ? minecraft.player : null;
        if (player == null) {
            return;
        }

        int centerX = width / 2;
        int rowLeft = centerX - ROW_HALF_WIDTH;
        int rowRight = centerX + ROW_HALF_WIDTH;

        for (int i = 0; i < visibleRows; i++) {
            int index = i + scrollOffset;
            if (index >= factions.size()) {
                break;
            }
            LOTRFaction faction = factions.get(index);
            int rowY = LIST_TOP + i * ROW_HEIGHT;

            if (index == selectedIndex) {
                graphics.fill(rowLeft, rowY, rowRight, rowY + ROW_HEIGHT - 2, 0x40FFFFFF);
            }

            float alignment = LOTRPlayerAlignments.getAlignment(player, faction);
            LOTRFactionRank rank = LOTRPlayerAlignments.getRank(player, faction);
            LOTRAlignmentBar bar = LOTRAlignmentBar.compute(faction, alignment);

            int factionColor = 0xFF000000 | faction.getFactionColor();
            Component name = Component.translatable(faction.untranslatedFactionName());
            graphics.text(font, name, rowLeft + 4, rowY + 2, factionColor, true);

            int barX = rowLeft + 4;
            int barY = rowY + 13;
            int barW = 150;
            int barH = 4;
            graphics.fill(barX - 1, barY - 1, barX + barW + 1, barY + barH + 1, 0x80000000);
            int fillW = Math.round(bar.progress * barW);
            graphics.fill(barX, barY, barX + fillW, barY + barH, factionColor);

            Component standing = Component.literal(formatAlignment(alignment) + "  ").append(rank.getDisplayName());
            int sw = font.width(standing);
            graphics.text(font, standing, rowRight - 4 - sw, rowY + 6, 0xFFDDDDDD, true);

            if (LOTRPlayerAlignments.isPledgedTo(player, faction)) {
                graphics.text(font, Component.translatable("lotr.gui.factions.pledged"),
                        barX + barW + 8, rowY + 6, 0xFFFFD700, true);
            }
        }
    }

    private static String formatAlignment(float alignment) {
        if (alignment == Math.floor(alignment)) {
            return String.valueOf((int) alignment);
        }
        return String.format("%.1f", alignment);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}