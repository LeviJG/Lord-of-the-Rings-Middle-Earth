package net.blueskiez77.lord_of_the_rings__middle_earth.client.gui;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.LOTRClientFactionState;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.hud.LOTRAlignmentBarRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRDimension;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRAlignmentValues;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFactionData;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFactionRank;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFactionRelations;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRRankOptions;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRViewingFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRBrokenPledgePayload;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRMenuPayloads;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRPledgeSetPayload;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import com.mojang.blaze3d.platform.NativeImage;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

import org.jspecify.annotations.Nullable;

/**
 * LOTRGuiFactions: one faction at a time, chosen on the coloured bar of the
 * region's factions (or the mouse wheel), the region switched with the
 * button beneath. Above, the alignment bar; on the page, by turns, the
 * player's standing with it (rank, kills, trades, hires, the pledge button),
 * its ranks, its allies and its enemies -- the last three scrolling. Pledging
 * and unpledging each ask first, explaining what stands in the way.
 * /alignmentsee shows another player's alignments in the same screen.
 *
 * <p>NOT ported yet: the faction's map on the front page and the button to
 * its control zone on the full map (LOTRGuiMap, D13); the mini-quest events
 * of opening it and cycling factions (D14); regions of another dimension
 * (every player is in Middle-earth until Utumno, D10).
 */
public class LOTRFactionsScreen extends LOTRMenuBaseScreen {

    public static final Identifier FACTIONS_TEXTURE = Identifier.fromNamespaceAndPath("lotr", "gui/factions.png");
    public static final Identifier FACTIONS_TEXTURE_FULL = Identifier.fromNamespaceAndPath("lotr", "gui/factions_full.png");
    private static final int TEXT_COLOUR = 0xFF7A5D43;

    private static LOTRDimension currentDimension;
    private static LOTRDimension prevDimension;
    private static LOTRDimension.DimensionRegion currentRegion;
    private static LOTRDimension.DimensionRegion prevRegion;
    private static List<LOTRFaction> currentFactionList;
    private static Page currentPage = Page.FRONT;
    private static @Nullable Integer avgPageColour;

    private int currentFactionIndex;
    private int prevFactionIndex;
    private LOTRFaction currentFaction;
    private final int pageY = 46;
    private final int pageWidth = 256;
    private final int pageHeight = 128;
    private final int pageBorderLeft = 16;
    private final int pageBorderTop = 12;
    private final int pageMapX = 159;
    private final int pageMapY = 22;
    private final int pageMapSize = 80;
    private LOTRRedBookButton buttonRegions;
    private PageButton buttonPagePrev;
    private PageButton buttonPageNext;
    private PledgeButton buttonPledge;
    private PledgeButton buttonPledgeConfirm;
    private PledgeButton buttonPledgeRevoke;
    private float currentScroll;
    private boolean isScrolling;
    private boolean wasMouseDown;
    private final int scrollBarWidth = 240;
    private final int scrollBarHeight = 14;
    private final int scrollBarX;
    private final int scrollBarY = 180;
    private final int scrollBarBorder = 1;
    private final int scrollWidgetWidth = 17;
    private final int scrollWidgetHeight = 12;
    private final LOTRScrollPane scrollPaneAlliesEnemies = new LOTRScrollPane(7, 7).setColors(5521198, 8019267);
    private final int scrollAlliesEnemiesX = 138;
    private int numDisplayedAlliesEnemies;
    private List<Object> currentAlliesEnemies = new ArrayList<>();
    private boolean isOtherPlayer;
    private String otherPlayerName;
    private Map<LOTRFaction, Float> playerAlignmentMap;
    private boolean isPledging;
    private boolean isUnpledging;

    public LOTRFactionsScreen() {
        super(Component.translatable("lotr.gui.factions"));
        this.xSize = this.pageWidth;
        this.scrollBarX = this.xSize / 2 - this.scrollBarWidth / 2;
    }

    /** setOtherPlayer: /alignmentsee's view of someone else's alignments. */
    public LOTRFactionsScreen setOtherPlayer(String name, Map<LOTRFaction, Float> alignments) {
        this.isOtherPlayer = true;
        this.otherPlayerName = name;
        this.playerAlignmentMap = alignments;
        return this;
    }

    private Player player() {
        return this.minecraft.player;
    }

    @Override
    protected void init() {
        super.init();
        if (this.isOtherPlayer) {
            removeWidget(this.buttonMenuReturn);
        }
        this.buttonRegions = addRenderableWidget(new LOTRRedBookButton(this.guiLeft + this.xSize / 2 - 60, this.guiTop + 200,
                120, 20, Component.empty(), this::cycleRegion));
        this.buttonPagePrev = addRenderableWidget(new PageButton(this.guiLeft + 8, this.guiTop + this.pageY + 104, false));
        this.buttonPageNext = addRenderableWidget(new PageButton(this.guiLeft + 232, this.guiTop + this.pageY + 104, true));
        this.buttonPledge = addRenderableWidget(new PledgeButton(this.guiLeft + 14,
                this.guiTop + this.pageY + this.pageHeight - 42, () -> {
                    if (LOTRPlayerAlignments.isPledgedTo(player(), this.currentFaction)) {
                        this.isUnpledging = true;
                    } else {
                        this.isPledging = true;
                    }
                }));
        this.buttonPledgeConfirm = addRenderableWidget(new PledgeButton(this.guiLeft + this.pageWidth / 2 - 16,
                this.guiTop + this.pageY + this.pageHeight - 44, () -> {
                    ClientPlayNetworking.send(new LOTRPledgeSetPayload(this.currentFaction));
                    this.isPledging = false;
                }));
        this.buttonPledgeRevoke = addRenderableWidget(new PledgeButton(this.guiLeft + this.pageWidth / 2 - 16,
                this.guiTop + this.pageY + this.pageHeight - 44, () -> {
                    ClientPlayNetworking.send(new LOTRPledgeSetPayload(null));
                    this.isUnpledging = false;
                    onClose();
                }));
        this.buttonPledgeRevoke.isBroken = true;
        prevDimension = currentDimension = LOTRDimension.MIDDLE_EARTH;
        this.currentFaction = LOTRViewingFaction.getViewingFaction(player());
        prevRegion = currentRegion = this.currentFaction.factionRegion;
        currentFactionList = currentRegion.factionList;
        this.prevFactionIndex = this.currentFactionIndex = currentFactionList.indexOf(this.currentFaction);
        setCurrentScrollFromFaction();
        updateButtons();
    }

    private void cycleRegion() {
        List<LOTRDimension.DimensionRegion> regionList = currentDimension.dimensionRegions;
        if (!regionList.isEmpty()) {
            int i = Math.floorMod(regionList.indexOf(currentRegion) + 1, regionList.size());
            currentRegion = regionList.get(i);
            updateCurrentDimensionAndFaction();
            setCurrentScrollFromFaction();
            this.scrollPaneAlliesEnemies.resetScroll();
            this.isPledging = false;
            this.isUnpledging = false;
        }
    }

    private void turnPage(boolean next) {
        Page newPage = next ? currentPage.next() : currentPage.prev();
        if (newPage != null) {
            currentPage = newPage;
            this.scrollPaneAlliesEnemies.resetScroll();
            this.isPledging = false;
            this.isUnpledging = false;
        }
    }

    private float alignment() {
        if (this.isOtherPlayer && this.playerAlignmentMap != null) {
            return this.playerAlignmentMap.getOrDefault(this.currentFaction, 0.0f);
        }
        return LOTRPlayerAlignments.getAlignment(player(), this.currentFaction);
    }

    /** drawScreen's button half. */
    private void updateButtons() {
        Player player = player();
        if (!this.isPledging && !this.isUnpledging) {
            this.buttonPagePrev.active = currentPage.prev() != null;
            this.buttonPageNext.active = currentPage.next() != null;
            if (!this.isOtherPlayer && currentPage == Page.FRONT) {
                if (LOTRPlayerAlignments.isPledgedTo(player, this.currentFaction)) {
                    this.buttonPledge.isBroken = this.buttonPledge.isHovered();
                    this.buttonPledge.active = true;
                    this.buttonPledge.visible = true;
                    this.buttonPledge.setDisplayLines(Component.translatable("lotr.gui.factions.unpledge"));
                } else {
                    this.buttonPledge.isBroken = false;
                    this.buttonPledge.visible = LOTRPlayerAlignments.get(player).pledgeFaction() == null
                            && this.currentFaction.isPlayableAlignmentFaction()
                            && LOTRPlayerAlignments.getAlignment(player, this.currentFaction) >= 0.0f;
                    this.buttonPledge.active = this.buttonPledge.visible
                            && LOTRPlayerAlignments.hasPledgeAlignment(player, this.currentFaction);
                    this.buttonPledge.setDisplayLines(Component.translatable("lotr.gui.factions.pledge"),
                            Component.translatable("lotr.gui.factions.pledgeReq",
                                    LOTRAlignmentValues.formatAlignForDisplay(this.currentFaction.getPledgeAlignment())));
                }
            } else {
                this.buttonPledge.active = false;
                this.buttonPledge.visible = false;
            }
            this.buttonPledgeConfirm.active = false;
            this.buttonPledgeConfirm.visible = false;
            this.buttonPledgeRevoke.active = false;
            this.buttonPledgeRevoke.visible = false;
        } else {
            this.buttonPagePrev.active = false;
            this.buttonPageNext.active = false;
            this.buttonPledge.active = false;
            this.buttonPledge.visible = false;
            if (this.isPledging) {
                this.buttonPledgeConfirm.visible = true;
                this.buttonPledgeConfirm.active = LOTRClientFactionState.canMakeNewPledge()
                        && LOTRPlayerAlignments.canPledgeTo(player, this.currentFaction);
                this.buttonPledgeConfirm.setDisplayLines(Component.translatable("lotr.gui.factions.pledge"));
                this.buttonPledgeRevoke.active = false;
                this.buttonPledgeRevoke.visible = false;
            } else {
                this.buttonPledgeConfirm.active = false;
                this.buttonPledgeConfirm.visible = false;
                this.buttonPledgeRevoke.active = true;
                this.buttonPledgeRevoke.visible = true;
                this.buttonPledgeRevoke.setDisplayLines(Component.translatable("lotr.gui.factions.unpledge"));
            }
        }
        if (currentRegion != null && currentDimension.dimensionRegions.size() > 1) {
            this.buttonRegions.setMessage(currentRegion.getRegionName());
            this.buttonRegions.active = true;
            this.buttonRegions.visible = true;
        } else {
            this.buttonRegions.setMessage(Component.empty());
            this.buttonRegions.active = false;
            this.buttonRegions.visible = false;
        }
    }

    private boolean useFullPageTexture() {
        return this.isPledging || this.isUnpledging || currentPage == Page.RANKS;
    }

    private boolean hasScrollBar() {
        return currentFactionList.size() > 1;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(RenderPipelines.GUI_TEXTURED, useFullPageTexture() ? FACTIONS_TEXTURE_FULL : FACTIONS_TEXTURE,
                this.guiLeft, this.guiTop + this.pageY, 0.0F, 0.0F, this.pageWidth, this.pageHeight, 256, 256);
        if (hasScrollBar()) {
            int x0 = this.guiLeft + this.scrollBarX;
            int y0 = this.guiTop + this.scrollBarY;
            graphics.blit(RenderPipelines.GUI_TEXTURED, FACTIONS_TEXTURE, x0, y0, 0.0F, 128.0F,
                    this.scrollBarWidth, this.scrollBarHeight, 256, 256);
            int factions = currentFactionList.size();
            int inner = this.scrollBarWidth - this.scrollBarBorder * 2;
            for (int index = 0; index < factions; ++index) {
                float[] rgb = currentFactionList.get(index).getFactionRGB();
                float shade = 0.6f;
                int colour = 0xFF000000 | (int) (rgb[0] * shade * 255) << 16 | (int) (rgb[1] * shade * 255) << 8
                        | (int) (rgb[2] * shade * 255);
                int xMin = x0 + this.scrollBarBorder + Math.round((float) index / factions * inner);
                int xMax = x0 + this.scrollBarBorder + Math.round((float) (index + 1) / factions * inner);
                graphics.blit(RenderPipelines.GUI_TEXTURED, FACTIONS_TEXTURE, xMin, y0 + this.scrollBarBorder,
                        this.scrollBarBorder, 128 + this.scrollBarBorder, xMax - xMin,
                        this.scrollBarHeight - this.scrollBarBorder * 2, this.scrollBarWidth - this.scrollBarBorder * 2,
                        this.scrollBarHeight - this.scrollBarBorder * 2, 256, 256, colour);
            }
            int scroll = (int) (this.currentScroll * (inner - this.scrollWidgetWidth));
            graphics.blit(RenderPipelines.GUI_TEXTURED, FACTIONS_TEXTURE, x0 + this.scrollBarBorder + scroll,
                    y0 + this.scrollBarBorder, 0.0F, 142.0F, this.scrollWidgetWidth, this.scrollWidgetHeight, 256, 256);
        }
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        updateButtons();
        setupScrollBar(mouseX, mouseY);
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        Player player = player();
        boolean fem = LOTRRankOptions.useFeminineRanks(player);
        boolean mouseOverAlignLock = false;
        boolean mouseOverWarCrimes = false;
        Component title = this.isOtherPlayer ? Component.translatable("lotr.gui.factions.titleOther", this.otherPlayerName)
                : Component.translatable("lotr.gui.factions.title", currentDimension.getDimensionName());
        graphics.text(this.font, title, this.guiLeft + this.xSize / 2 - this.font.width(title) / 2, this.guiTop - 30,
                0xFFFFFFFF, false);
        if (this.currentFaction == null) {
            return;
        }
        float alignment = alignment();
        int x = this.guiLeft + this.xSize / 2;
        int y = this.guiTop;
        LOTRAlignmentBarRenderer.renderAlignmentBar(graphics, alignment, this.isOtherPlayer, this.currentFaction, x, y,
                true, false, true, true);
        Component s = this.currentFaction.factionSubtitle();
        graphics.text(this.font, s, x - this.font.width(s) / 2, y + this.font.lineHeight + 22, 0xFFFFFFFF, false);
        if (!useFullPageTexture()) {
            int wcX = this.guiLeft + this.pageMapX + 3;
            int wcY = this.guiTop + this.pageY + this.pageMapY + this.pageMapSize + 5;
            int wcWidth = 8;
            graphics.blit(RenderPipelines.GUI_TEXTURED, FACTIONS_TEXTURE, wcX, wcY,
                    this.currentFaction.approvesWarCrimes ? 33.0F : 41.0F, 142.0F, wcWidth, wcWidth, 256, 256);
            mouseOverWarCrimes = mouseX >= wcX && mouseX < wcX + wcWidth && mouseY >= wcY && mouseY < wcY + wcWidth;
        }
        x = this.guiLeft + this.pageBorderLeft;
        y = this.guiTop + this.pageY + this.pageBorderTop;
        if (!this.isPledging && !this.isUnpledging) {
            if (currentPage == Page.FRONT) {
                if (this.isOtherPlayer) {
                    graphics.text(this.font, Component.translatable("lotr.gui.factions.pageOther", this.otherPlayerName),
                            x, y, TEXT_COLOUR, false);
                    y += this.font.lineHeight * 2;
                }
                Component alignmentInfo = Component.translatable("lotr.gui.factions.alignment");
                graphics.text(this.font, alignmentInfo, x, y, TEXT_COLOUR, false);
                Component alignmentString = Component.literal(LOTRAlignmentValues.formatAlignForDisplay(alignment));
                x += this.font.width(alignmentInfo) + 5;
                LOTRAlignmentBarRenderer.drawAlignmentText(graphics, this.font, x, y, alignmentString, 1.0f);
                if (LOTRPlayerAlignments.isPledgeEnemyAlignmentLimited(player, this.currentFaction)) {
                    int lockX = x + this.font.width(alignmentString) + 5;
                    int lockWidth = 16;
                    graphics.blit(RenderPipelines.GUI_TEXTURED, FACTIONS_TEXTURE, lockX, y, 0.0F, 200.0F, lockWidth, lockWidth,
                            256, 256);
                    mouseOverAlignLock = mouseX >= lockX && mouseX < lockX + lockWidth && mouseY >= y && mouseY < y + lockWidth;
                }
                x = this.guiLeft + this.pageBorderLeft;
                LOTRFactionRank curRank = this.currentFaction.getRank(alignment);
                y += this.font.lineHeight;
                graphics.text(this.font, Component.translatable("lotr.gui.factions.alignment.state",
                        curRank.getFullNameWithGender(fem)), x, y, TEXT_COLOUR, false);
                y += this.font.lineHeight * 2;
                if (!this.isOtherPlayer) {
                    LOTRFactionData factionData = LOTRFactionData.get(player, this.currentFaction);
                    if (alignment >= 0.0f) {
                        graphics.text(this.font, Component.translatable("lotr.gui.factions.data.enemiesKilled",
                                factionData.enemiesKilled()), x, y, TEXT_COLOUR, false);
                        y += this.font.lineHeight;
                        graphics.text(this.font, Component.translatable("lotr.gui.factions.data.trades",
                                factionData.tradeCount()), x, y, TEXT_COLOUR, false);
                        y += this.font.lineHeight;
                        graphics.text(this.font, Component.translatable("lotr.gui.factions.data.hires",
                                factionData.hireCount()), x, y, TEXT_COLOUR, false);
                        y += this.font.lineHeight;
                        graphics.text(this.font, Component.translatable("lotr.gui.factions.data.miniquests",
                                factionData.miniQuestsCompleted()), x, y, TEXT_COLOUR, false);
                        y += this.font.lineHeight;
                        float conq;
                        if (LOTRPlayerAlignments.isPledgedTo(player, this.currentFaction)
                                && (conq = factionData.conquestEarned()) != 0.0f) {
                            graphics.text(this.font, Component.translatable("lotr.gui.factions.data.conquest",
                                    Math.round(conq)), x, y, TEXT_COLOUR, false);
                            y += this.font.lineHeight;
                        }
                    }
                    if (alignment <= 0.0f) {
                        graphics.text(this.font, Component.translatable("lotr.gui.factions.data.npcsKilled",
                                factionData.npcsKilled()), x, y, TEXT_COLOUR, false);
                    }
                    if (this.buttonPledge.visible && LOTRPlayerAlignments.isPledgedTo(player, this.currentFaction)) {
                        Component pledged = Component.translatable("lotr.gui.factions.pledged");
                        int px = this.buttonPledge.getX() + this.buttonPledge.getWidth() + 8;
                        int py = this.buttonPledge.getY() + this.buttonPledge.getHeight() / 2 - this.font.lineHeight / 2;
                        graphics.text(this.font, pledged, px, py, 0xFFFF0000, false);
                    }
                }
            } else if (currentPage == Page.RANKS) {
                LOTRFactionRank curRank = this.currentFaction.getRank(alignment);
                int[] minMax = this.scrollPaneAlliesEnemies.getMinMaxIndices(this.currentAlliesEnemies, this.numDisplayedAlliesEnemies);
                for (int index = minMax[0]; index <= minMax[1]; ++index) {
                    Object listObj = this.currentAlliesEnemies.get(index);
                    if (listObj instanceof Component header) {
                        graphics.text(this.font, header, x, y, TEXT_COLOUR, false);
                    } else if (listObj instanceof LOTRFactionRank rank) {
                        Component rankName = rank.getShortNameWithGender(fem);
                        String rankAlign = rank == LOTRFactionRank.RANK_ENEMY ? "-"
                                : LOTRAlignmentValues.formatAlignForDisplay(rank.alignment);
                        LOTRFactionRank above = this.currentFaction.getRankAbove(curRank);
                        boolean hiddenRankName = !LOTRPlayerAlignments.isPledgedTo(player, this.currentFaction)
                                && rank.alignment > this.currentFaction.getPledgeAlignment()
                                && above != null && rank.alignment > above.alignment;
                        if (hiddenRankName) {
                            rankName = Component.translatable("lotr.gui.factions.rank?");
                        }
                        Component line = Component.translatable("lotr.gui.factions.listRank", rankName, rankAlign);
                        if (rank == curRank) {
                            LOTRAlignmentBarRenderer.drawAlignmentText(graphics, this.font, x, y, line, 1.0f);
                        } else {
                            graphics.text(this.font, line, x, y, TEXT_COLOUR, false);
                        }
                    }
                    y += this.font.lineHeight;
                }
            } else {
                int avgBgColor = averagePageColour();
                int[] minMax = this.scrollPaneAlliesEnemies.getMinMaxIndices(this.currentAlliesEnemies, this.numDisplayedAlliesEnemies);
                for (int index = minMax[0]; index <= minMax[1]; ++index) {
                    Object listObj = this.currentAlliesEnemies.get(index);
                    if (listObj instanceof LOTRFactionRelations.Relation rel) {
                        graphics.text(this.font, Component.translatable("lotr.gui.factions.relationHeader", rel.getDisplayName()),
                                x, y, TEXT_COLOUR, false);
                    } else if (listObj instanceof LOTRFaction fac) {
                        graphics.text(this.font, Component.translatable("lotr.gui.factions.list", fac.factionName()), x, y,
                                0xFF000000 | findContrastingColor(fac.getFactionColor(), avgBgColor), false);
                    }
                    y += this.font.lineHeight;
                }
            }
            if (this.scrollPaneAlliesEnemies.hasScrollBar) {
                this.scrollPaneAlliesEnemies.drawScrollBar(graphics);
            }
        } else {
            int stringWidth = this.pageWidth - this.pageBorderLeft * 2;
            List<FormattedCharSequence> displayLines = new ArrayList<>();
            if (this.isPledging) {
                List<LOTRFaction> facsPreventingPledge = new ArrayList<>(
                        LOTRPlayerAlignments.getFactionsPreventingPledgeTo(player, this.currentFaction));
                if (facsPreventingPledge.isEmpty()) {
                    if (LOTRClientFactionState.canMakeNewPledge()) {
                        if (LOTRPlayerAlignments.canPledgeTo(player, this.currentFaction)) {
                            displayLines.addAll(this.font.split(Component.translatable("lotr.gui.factions.pledgeDesc1",
                                    this.currentFaction.factionName()), stringWidth));
                            displayLines.add(FormattedCharSequence.EMPTY);
                            displayLines.addAll(this.font.split(Component.translatable("lotr.gui.factions.pledgeDesc2"), stringWidth));
                        }
                    } else {
                        LOTRBrokenPledgePayload broken = LOTRClientFactionState.get();
                        Component brokenPledgeName = broken.brokenFaction() == null
                                ? Component.translatable("lotr.gui.factions.pledgeUnknown") : broken.brokenFaction().factionName();
                        displayLines.addAll(this.font.split(Component.translatable("lotr.gui.factions.pledgeBreakCooldown",
                                this.currentFaction.factionName(), brokenPledgeName), stringWidth));
                        displayLines.add(FormattedCharSequence.EMPTY);
                        graphics.blit(RenderPipelines.GUI_TEXTURED, FACTIONS_TEXTURE, this.guiLeft + this.pageWidth / 2 - 97,
                                this.guiTop + this.pageY + 56, 0.0F, 240.0F, 194, 16, 256, 256);
                        float cdFrac = broken.cooldownStart() == 0 ? 0.0f : (float) broken.cooldown() / broken.cooldownStart();
                        graphics.blit(RenderPipelines.GUI_TEXTURED, FACTIONS_TEXTURE, this.guiLeft + this.pageWidth / 2 - 75,
                                this.guiTop + this.pageY + 60, 22.0F, 232.0F, Mth.ceil(cdFrac * 150.0f), 8, 256, 256);
                    }
                } else {
                    facsPreventingPledge.sort((o1, o2) -> -Float.compare(LOTRPlayerAlignments.getAlignment(player, o1),
                            LOTRPlayerAlignments.getAlignment(player, o2)));
                    Component facNames;
                    int n = facsPreventingPledge.size();
                    if (n == 1) {
                        facNames = Component.translatable("lotr.gui.factions.enemies1", facsPreventingPledge.get(0).factionName());
                    } else if (n == 2) {
                        facNames = Component.translatable("lotr.gui.factions.enemies2", facsPreventingPledge.get(0).factionName(),
                                facsPreventingPledge.get(1).factionName());
                    } else if (n == 3) {
                        facNames = Component.translatable("lotr.gui.factions.enemies3", facsPreventingPledge.get(0).factionName(),
                                facsPreventingPledge.get(1).factionName(), facsPreventingPledge.get(2).factionName());
                    } else {
                        facNames = Component.translatable("lotr.gui.factions.enemies3+", facsPreventingPledge.get(0).factionName(),
                                facsPreventingPledge.get(1).factionName(), facsPreventingPledge.get(2).factionName(), n - 3);
                    }
                    displayLines.addAll(this.font.split(Component.translatable("lotr.gui.factions.pledgeEnemies",
                            this.currentFaction.factionName(), facNames), stringWidth));
                    displayLines.add(FormattedCharSequence.EMPTY);
                }
            } else {
                displayLines.addAll(this.font.split(Component.translatable("lotr.gui.factions.unpledgeDesc1",
                        this.currentFaction.factionName()), stringWidth));
                displayLines.add(FormattedCharSequence.EMPTY);
                displayLines.addAll(this.font.split(Component.translatable("lotr.gui.factions.unpledgeDesc2"), stringWidth));
            }
            for (FormattedCharSequence line : displayLines) {
                graphics.text(this.font, line, x, y, TEXT_COLOUR, false);
                y += this.font.lineHeight;
            }
        }
        if (mouseOverAlignLock) {
            LOTRFaction pledge = LOTRPlayerAlignments.get(player).pledgeFaction();
            String alignLimit = LOTRAlignmentValues.formatAlignForDisplay(
                    LOTRPlayerAlignments.getPledgeEnemyAlignmentLimit(player, this.currentFaction));
            graphics.setTooltipForNextFrame(this.font, this.font.split(Component.translatable("lotr.gui.factions.pledgeLocked",
                    alignLimit, pledge == null ? Component.empty() : pledge.factionName()), 200), mouseX, mouseY);
        }
        if (mouseOverWarCrimes) {
            graphics.setTooltipForNextFrame(this.font, this.font.split(Component.translatable(this.currentFaction.approvesWarCrimes
                    ? "lotr.gui.factions.warCrimesYes" : "lotr.gui.factions.warCrimesNo"), 200), mouseX, mouseY);
        }
        for (PledgeButton button : List.of(this.buttonPledge, this.buttonPledgeConfirm, this.buttonPledgeRevoke)) {
            if (button.visible && button.isHovered() && button.displayLines != null) {
                List<FormattedCharSequence> lines = new ArrayList<>();
                for (Component line : button.displayLines) {
                    lines.add(line.getVisualOrderText());
                }
                graphics.setTooltipForNextFrame(this.font, lines, mouseX, mouseY);
            }
        }
    }

    /** LOTRTextures.computeAverageFactionPageColor, over the page's text area. */
    private int averagePageColour() {
        if (avgPageColour == null) {
            int avg = 0;
            try (var in = this.minecraft.getResourceManager().open(FACTIONS_TEXTURE);
                 NativeImage image = NativeImage.read(in)) {
                long r = 0, g = 0, b = 0, a = 0;
                int count = 0;
                for (int u = 20; u < 120; ++u) {
                    for (int v = 20; v < 80; ++v) {
                        int argb = image.getPixel(u, v);
                        a += argb >>> 24 & 0xFF;
                        r += argb >> 16 & 0xFF;
                        g += argb >> 8 & 0xFF;
                        b += argb & 0xFF;
                        ++count;
                    }
                }
                avg = (int) (a / count) << 24 | (int) (r / count) << 16 | (int) (g / count) << 8 | (int) (b / count);
            } catch (Exception e) {
                avg = 0;
            }
            avgPageColour = avg;
        }
        return avgPageColour;
    }

    /** LOTRTextures.findContrastingColor: the text's colour, lightened or darkened off the page. */
    private static int findContrastingColor(int text, int bg) {
        float[] hsbText = java.awt.Color.RGBtoHSB(text >> 16 & 0xFF, text >> 8 & 0xFF, text & 0xFF, null);
        float[] hsbBg = java.awt.Color.RGBtoHSB(bg >> 16 & 0xFF, bg >> 8 & 0xFF, bg & 0xFF, null);
        float bText = hsbText[2];
        float bBg = hsbBg[2];
        float limit = 0.4f;
        if (Math.abs(bText - bBg) < limit) {
            bText = bBg > 0.66f ? bBg - limit : bBg + limit;
        }
        return java.awt.Color.HSBtoRGB(hsbText[0], hsbText[1], bText) & 0xFFFFFF;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        int k = (int) Math.signum(scrollY);
        if (k != 0) {
            if (this.scrollPaneAlliesEnemies.hasScrollBar && this.scrollPaneAlliesEnemies.mouseOver) {
                int l = this.currentAlliesEnemies.size() - this.numDisplayedAlliesEnemies;
                this.scrollPaneAlliesEnemies.mouseWheelScroll(k, l);
            } else {
                if (k < 0) {
                    this.currentFactionIndex = Math.min(this.currentFactionIndex + 1, Math.max(0, currentFactionList.size() - 1));
                }
                if (k > 0) {
                    this.currentFactionIndex = Math.max(this.currentFactionIndex - 1, 0);
                }
                setCurrentScrollFromFaction();
                this.scrollPaneAlliesEnemies.resetScroll();
                this.isPledging = false;
                this.isUnpledging = false;
            }
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (event.isEscape() || this.minecraft.options.keyInventory.matches(event)) {
            if (this.isPledging) {
                this.isPledging = false;
                return true;
            }
            if (this.isUnpledging) {
                this.isUnpledging = false;
                return true;
            }
            if (this.isOtherPlayer) {
                onClose();
                return true;
            }
        }
        return super.keyPressed(event);
    }

    private void setCurrentScrollFromFaction() {
        this.currentScroll = (float) this.currentFactionIndex / (currentFactionList.size() - 1);
    }

    /** setupScrollBar: the faction bar dragged, and the scrolling list built for the page. */
    private void setupScrollBar(int i, int j) {
        boolean isMouseDown = this.minecraft.mouseHandler.isLeftPressed();
        int i1 = this.guiLeft + this.scrollBarX;
        int j1 = this.guiTop + this.scrollBarY;
        int i2 = i1 + this.scrollBarWidth;
        int j2 = j1 + this.scrollBarHeight;
        if (!this.wasMouseDown && isMouseDown && i >= i1 && j >= j1 && i < i2 && j < j2) {
            this.isScrolling = true;
        }
        if (!isMouseDown) {
            this.isScrolling = false;
        }
        this.wasMouseDown = isMouseDown;
        if (this.isScrolling) {
            this.currentScroll = Mth.clamp((i - i1 - this.scrollWidgetWidth / 2.0f) / ((float) (i2 - i1) - this.scrollWidgetWidth),
                    0.0f, 1.0f);
            this.currentFactionIndex = Math.round(this.currentScroll * (currentFactionList.size() - 1));
            this.scrollPaneAlliesEnemies.resetScroll();
        }
        if (currentPage == Page.ALLIES || currentPage == Page.ENEMIES || currentPage == Page.RANKS) {
            this.currentAlliesEnemies = new ArrayList<>();
            if (currentPage == Page.ALLIES) {
                addRelation(LOTRFactionRelations.Relation.ALLY);
                addRelation(LOTRFactionRelations.Relation.FRIEND);
            } else if (currentPage == Page.ENEMIES) {
                addRelation(LOTRFactionRelations.Relation.MORTAL_ENEMY);
                addRelation(LOTRFactionRelations.Relation.ENEMY);
            } else {
                this.currentAlliesEnemies.add(Component.translatable("lotr.gui.factions.rankHeader"));
                if (LOTRPlayerAlignments.getAlignment(player(), this.currentFaction) <= 0.0f) {
                    this.currentAlliesEnemies.add(LOTRFactionRank.RANK_ENEMY);
                }
                LOTRFactionRank rank = LOTRFactionRank.RANK_NEUTRAL;
                while (true) {
                    this.currentAlliesEnemies.add(rank);
                    LOTRFactionRank nextRank = this.currentFaction.getRankAbove(rank);
                    if (nextRank == null || nextRank.isDummyRank() || this.currentAlliesEnemies.contains(nextRank)) {
                        break;
                    }
                    rank = nextRank;
                }
            }
            this.scrollPaneAlliesEnemies.hasScrollBar = false;
            this.numDisplayedAlliesEnemies = this.currentAlliesEnemies.size();
            if (this.numDisplayedAlliesEnemies > 10) {
                this.numDisplayedAlliesEnemies = 10;
                this.scrollPaneAlliesEnemies.hasScrollBar = true;
            }
            this.scrollPaneAlliesEnemies.paneX0 = this.guiLeft;
            this.scrollPaneAlliesEnemies.scrollBarX0 = this.guiLeft + this.scrollAlliesEnemiesX;
            if (currentPage == Page.RANKS) {
                this.scrollPaneAlliesEnemies.scrollBarX0 += 50;
            }
            this.scrollPaneAlliesEnemies.paneY0 = this.guiTop + this.pageY + this.pageBorderTop;
            this.scrollPaneAlliesEnemies.paneY1 = this.scrollPaneAlliesEnemies.paneY0 + this.font.lineHeight * this.numDisplayedAlliesEnemies;
        } else {
            this.scrollPaneAlliesEnemies.hasScrollBar = false;
        }
        this.scrollPaneAlliesEnemies.mouseDragScroll(i, j, isMouseDown);
    }

    private void addRelation(LOTRFactionRelations.Relation rel) {
        List<LOTRFaction> others = this.currentFaction.getOthersOfRelation(rel);
        if (!others.isEmpty()) {
            if (!this.currentAlliesEnemies.isEmpty()) {
                this.currentAlliesEnemies.add(Component.empty());
            }
            this.currentAlliesEnemies.add(rel);
            this.currentAlliesEnemies.addAll(others);
        }
    }

    /** updateCurrentDimensionAndFaction: a newly chosen faction is told to the server. */
    private void updateCurrentDimensionAndFaction() {
        Player player = player();
        Map<LOTRDimension.DimensionRegion, LOTRFaction> lastViewedRegions = new EnumMap<>(LOTRDimension.DimensionRegion.class);
        if (this.currentFactionIndex != this.prevFactionIndex) {
            this.currentFaction = currentFactionList.get(this.currentFactionIndex);
        }
        this.prevFactionIndex = this.currentFactionIndex;
        currentDimension = LOTRDimension.MIDDLE_EARTH;
        if (currentDimension != prevDimension) {
            currentRegion = currentDimension.dimensionRegions.get(0);
        }
        if (currentRegion != prevRegion) {
            LOTRViewingFaction.setRegionLastViewedFaction(player, prevRegion, this.currentFaction);
            lastViewedRegions.put(prevRegion, this.currentFaction);
            currentFactionList = currentRegion.factionList;
            this.currentFaction = LOTRViewingFaction.getRegionLastViewedFaction(player, currentRegion);
            this.prevFactionIndex = this.currentFactionIndex = currentFactionList.indexOf(this.currentFaction);
        }
        prevDimension = currentDimension;
        prevRegion = currentRegion;
        if (this.currentFaction != LOTRViewingFaction.getViewingFaction(player)) {
            LOTRViewingFaction.setViewingFaction(player, this.currentFaction);
            ClientPlayNetworking.send(new LOTRMenuPayloads.ClientInfo(this.currentFaction, new HashMap<>(lastViewedRegions)));
            this.isPledging = false;
            this.isUnpledging = false;
        }
    }

    @Override
    public void tick() {
        super.tick();
        updateCurrentDimensionAndFaction();
        Player player = player();
        if (this.isPledging && !LOTRPlayerAlignments.hasPledgeAlignment(player, this.currentFaction)) {
            this.isPledging = false;
        }
        if (this.isUnpledging && !LOTRPlayerAlignments.isPledgedTo(player, this.currentFaction)) {
            this.isUnpledging = false;
        }
    }

    public enum Page {
        FRONT, RANKS, ALLIES, ENEMIES;

        public @Nullable Page next() {
            return ordinal() == values().length - 1 ? null : values()[ordinal() + 1];
        }

        public @Nullable Page prev() {
            return ordinal() == 0 ? null : values()[ordinal() - 1];
        }
    }

    /** LOTRGuiButtonFactionsPage: a page corner, with an arrow beside it while it can turn. */
    private final class PageButton extends AbstractButton {
        private final boolean leftOrRight;

        PageButton(int x, int y, boolean leftOrRight) {
            super(x, y, 16, 16, Component.empty());
            this.leftOrRight = leftOrRight;
        }

        @Override
        public void onPress(InputWithModifiers input) {
            turnPage(this.leftOrRight);
        }

        @Override
        protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
            int k = !this.active ? 0 : isHovered() ? 2 : 1;
            graphics.blit(RenderPipelines.GUI_TEXTURED, FACTIONS_TEXTURE, getX(), getY(), k * 16,
                    this.leftOrRight ? 176.0F : 160.0F, this.width, this.height, 256, 256);
            if (this.active) {
                var fr = LOTRFactionsScreen.this.font;
                int stringY = getY() + this.height / 2 - fr.lineHeight / 2;
                if (this.leftOrRight) {
                    graphics.text(fr, "->", getX() - fr.width("->"), stringY, 0xFF000000, false);
                } else {
                    graphics.text(fr, "<-", getX() + this.width, stringY, 0xFF000000, false);
                }
            }
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput output) {
            defaultButtonNarrationText(output);
        }
    }

    /** LOTRGuiButtonPledge: the pledge ring, broken when unpledging. */
    private final class PledgeButton extends AbstractButton {
        boolean isBroken;
        @Nullable List<Component> displayLines;
        private final Runnable onPress;

        PledgeButton(int x, int y, Runnable onPress) {
            super(x, y, 32, 32, Component.empty());
            this.onPress = onPress;
        }

        void setDisplayLines(Component... lines) {
            this.displayLines = List.of(lines);
        }

        @Override
        public void onPress(InputWithModifiers input) {
            this.onPress.run();
        }

        private int hoverState() {
            boolean hovered = isHovered();
            if (this.isBroken) {
                return hovered ? 4 : 3;
            }
            if (!this.active) {
                return 0;
            }
            return hovered ? 2 : 1;
        }

        @Override
        protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, LOTRAlignmentBarRenderer.ALIGNMENT, getX(), getY(),
                    hoverState() * this.width, 180.0F, this.width, this.height, 256, 256);
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput output) {
            defaultButtonNarrationText(output);
        }
    }
}
