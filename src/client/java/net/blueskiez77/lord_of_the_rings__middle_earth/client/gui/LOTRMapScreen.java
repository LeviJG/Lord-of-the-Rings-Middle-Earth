package net.blueskiez77.lord_of_the_rings__middle_earth.client.gui;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import com.mojang.blaze3d.platform.InputConstants;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.LOTRKeyBindings;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.hud.LOTRAlignmentBarRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.fellowship.LOTRClientFellowships;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.map.LOTRClientCustomWaypoints;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.map.LOTRClientFastTravel;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.map.LOTRClientMap;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.map.LOTRMapQuadsRenderState;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.map.LOTRMapRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.quest.LOTRClientMiniQuests;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRPlayerAchievements;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.command.LOTRMiscCommands;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.config.LOTRConfig;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRAlignmentValues;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRControlZone;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFactionRank;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRRankOptions;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRViewingFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fellowship.LOTRFellowshipPayloads.View;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMiscItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRDimension;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.quest.LOTRMiniQuest;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.quest.LOTRMiniQuestEvent;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRWorldGen;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiomes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.genlayer.LOTRGenLayerWorld;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRAbstractWaypoint;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRConquestGrid;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRConquestZone;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRCustomWaypoint;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRCustomWaypoints;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRFastTravel;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRFixedStructures;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRMapCoords;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRMapPlayerData;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;

import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.PlayerFaceExtractor;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.PlayerSkin;

import org.jspecify.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

/**
 * LOTRGuiMap: the map of Middle-earth, fullscreen or in a window, dragged or moved with the movement
 * and arrow keys, zoomed by the wheel or its widgets between seven steps; with the roads, the other
 * players the server shows (and the player), the mini-quests' givers, the waypoints and the labels,
 * and below it the waypoint selected (its fast travel, by the Fast Travel key, or what it waits on) or
 * the land under the pointer (with an operator's teleport there, by the Map Teleport key). Custom
 * waypoints are made, renamed, deleted and opened to fellowships in overlays on the map, and shared
 * ones hidden; the toggles for waypoints, custom ones and hidden shared ones are kept by the server.
 * From the factions screen it shows a faction's areas of influence instead.
 *
 * A locked enemy waypoint that conquest could open says so. From the Table of Command it is the
 * conquest map (setConquestGrid): a smaller sepia map in a frame, a faction's conquest strength in
 * each zone, asked of the server faction by faction, the factions chosen by region and a scroll bar.
 */
public class LOTRMapScreen extends LOTRMenuBaseScreen {

    private static final int BORDER_COLOR = -6156032;
    private static boolean fullscreen = true;
    private static int zoomPower;
    private static final int ZOOM_TICKS_MAX = 6;
    private static final ItemStack QUEST_BOOK_ICON = new ItemStack(LOTRMiscItems.RED_BOOK);

    private final LOTRMapRenderer map = new LOTRMapRenderer();
    private final List<Widget> mapWidgets = new ArrayList<>();
    private Widget widgetAddCWP;
    private Widget widgetDelCWP;
    private Widget widgetRenameCWP;
    private Widget widgetToggleWPs;
    private Widget widgetToggleCWPs;
    private Widget widgetToggleHiddenSWPs;
    private Widget widgetZoomIn;
    private Widget widgetZoomOut;
    private Widget widgetFullScreen;
    private Widget widgetSepia;
    private Widget widgetLabels;
    private Widget widgetShareCWP;
    private Widget widgetHideSWP;
    private Widget widgetUnhideSWP;
    private int mapXMin;
    private int mapXMax;
    private int mapYMin;
    private int mapYMax;
    private int mapWidth;
    private int mapHeight;
    private float posX;
    private float posY;
    private int isMouseButtonDown;
    private int prevMouseX;
    private int prevMouseY;
    private boolean isMouseWithinMap;
    private int mouseXCoord;
    private int mouseZCoord;
    private float posXMove;
    private float posYMove;
    private float prevPosX;
    private float prevPosY;
    private int prevZoomPower = zoomPower;
    private float zoomScale;
    private float zoomScaleStable;
    private float zoomExp;
    private int zoomTicks;
    private @Nullable LOTRAbstractWaypoint selectedWaypoint;
    private boolean hasOverlay;
    private Component @Nullable [] overlayLines;
    private Button buttonOverlayFunction;
    private EditBox nameWPTextField;
    private boolean creatingWaypoint;
    private boolean creatingWaypointNew;
    private boolean deletingWaypoint;
    private boolean renamingWaypoint;
    private boolean sharingWaypoint;
    private boolean sharingWaypointNew;
    private LOTRFellowshipsScreen fellowshipDrawGUI;
    private @Nullable View mouseOverRemoveSharedFellowship;
    private final LOTRScrollPane scrollPaneWPShares = new LOTRScrollPane(9, 8);
    private @Nullable List<UUID> displayedWPShareList;
    private int displayedWPShares;
    private int tickCounter;
    private boolean hasControlZones;
    private @Nullable LOTRFaction controlZoneFaction;
    private boolean mouseControlZone;
    private boolean mouseControlZoneReduced;
    /** The tooltips the original drew raised above the map, drawn here once the map is done. */
    private final List<Runnable> raisedTooltips = new ArrayList<>();
    private static final Identifier CONQUEST_TEXTURE = Identifier.fromNamespaceAndPath("lotr", "map/conquest.png");
    /** LOTRClientProxy.alignmentTexture. */
    private static final Identifier ALIGNMENT_TEXTURE = Identifier.fromNamespaceAndPath("lotr", "gui/alignment.png");
    private static LOTRDimension.@Nullable DimensionRegion currentRegion;
    private static LOTRDimension.@Nullable DimensionRegion prevRegion;
    private static List<LOTRFaction> currentFactionList = List.of();
    private static final Map<LOTRDimension.DimensionRegion, LOTRFaction> lastViewedRegions = new EnumMap<>(LOTRDimension.DimensionRegion.class);
    private boolean isConquestGrid;
    private boolean loadingConquestGrid;
    private final Map<LOTRFaction, List<LOTRConquestZone>> facConquestGrids = new EnumMap<>(LOTRFaction.class);
    private final Set<LOTRFaction> requestedFacGrids = EnumSet.noneOf(LOTRFaction.class);
    private final Set<LOTRFaction> receivedFacGrids = EnumSet.noneOf(LOTRFaction.class);
    private int ticksUntilRequestFac = 40;
    private float highestViewedConqStr;
    private int currentFactionIndex;
    private int prevFactionIndex;
    private @Nullable LOTRFaction conquestViewingFaction;
    private float currentFacScroll;
    private boolean isFacScrolling;
    private boolean wasMouseDown;
    private boolean mouseInFacScroll;
    private static final int FAC_SCROLL_WIDTH = 240;
    private static final int FAC_SCROLL_HEIGHT = 14;
    private static final int FAC_SCROLL_BORDER = 1;
    private static final int FAC_SCROLL_WIDGET_WIDTH = 17;
    private static final int FAC_SCROLL_WIDGET_HEIGHT = 12;
    private int facScrollX;
    private int facScrollY;
    private @Nullable LOTRRedBookButton buttonConquestRegions;

    public LOTRMapScreen() {
        super(Component.translatable("lotr.gui.map.title"));
        if (!LOTRGenLayerWorld.loadedBiomeImage()) {
            new LOTRGenLayerWorld();
        }
    }

    /** setConquestGrid: the conquest map, from the Table of Command. */
    public LOTRMapScreen setConquestGrid() {
        this.isConquestGrid = true;
        return this;
    }

    /** setControlZone: the faction's areas of influence, from the factions screen. */
    public LOTRMapScreen setControlZone(LOTRFaction f) {
        this.hasControlZones = true;
        this.controlZoneFaction = f;
        return this;
    }

    // ---------------------------------------------------------------- the toggles, kept by the server

    private static boolean showWP() {
        return LOTRClientMap.showWP;
    }

    private static boolean showCWP() {
        return LOTRClientMap.showCWP;
    }

    private static boolean showHiddenSWP() {
        return LOTRClientMap.showHiddenSWP;
    }

    private static void sendClientInfoPacket() {
        ClientPlayNetworking.send(new LOTRMapPlayerData.TogglesPayload(LOTRClientMap.showWP, LOTRClientMap.showCWP, LOTRClientMap.showHiddenSWP));
    }

    // ---------------------------------------------------------------- set-up

    @Override
    protected void init() {
        this.xSize = 256;
        this.ySize = 256;
        super.init();
        LOTRClientMap.loadToggles(this.minecraft.player);
        if (fullscreen) {
            int midX = this.width / 2;
            int d = 100;
            this.buttonMenuReturn.setX(midX - d - this.buttonMenuReturn.getWidth());
            this.buttonMenuReturn.setY(4);
        }
        if (this.isConquestGrid || this.hasControlZones) {
            removeWidget(this.buttonMenuReturn);
        }
        setupMapWidgets();
        setupMapDimensions();
        if (this.isConquestGrid) {
            this.loadingConquestGrid = true;
            LOTRFaction faction = LOTRPlayerAlignments.get(this.minecraft.player).pledgeFaction();
            if (faction == null) {
                faction = LOTRViewingFaction.getViewingFaction(this.minecraft.player);
            }
            this.conquestViewingFaction = faction;
            prevRegion = currentRegion = faction.factionRegion;
            currentFactionList = currentRegion.factionList;
            this.prevFactionIndex = this.currentFactionIndex = currentFactionList.indexOf(faction);
            lastViewedRegions.put(currentRegion, faction);
            this.facScrollX = this.mapXMin;
            this.facScrollY = this.mapYMax + 8;
            setCurrentScrollFromFaction();
            this.buttonConquestRegions = addRenderableWidget(new LOTRRedBookButton(this.mapXMax - 120, this.mapYMax + 5, 120, 20,
                    Component.empty(), this::nextConquestRegion));
        }
        if (this.hasControlZones && this.controlZoneFaction != null) {
            int[] zoneBorders = this.controlZoneFaction.calculateFullControlZoneWorldBorders();
            int xMin = zoneBorders[0];
            int xMax = zoneBorders[1];
            int zMin = zoneBorders[2];
            int zMax = zoneBorders[3];
            float x = (xMin + xMax) / 2.0f;
            float z = (zMin + zMax) / 2.0f;
            this.posX = x / LOTRMapCoords.SCALE + 810.0f;
            this.posY = z / LOTRMapCoords.SCALE + 730.0f;
            double mapZoneWidth = (double) (xMax - xMin) / LOTRMapCoords.SCALE;
            double mapZoneHeight = (double) (zMax - zMin) / LOTRMapCoords.SCALE;
            int zoomPowerWidth = Mth.floor(Math.log(this.mapWidth / mapZoneWidth) / Math.log(2.0));
            int zoomPowerHeight = Mth.floor(Math.log(this.mapHeight / mapZoneHeight) / Math.log(2.0));
            this.prevZoomPower = zoomPower = Math.min(zoomPowerWidth, zoomPowerHeight);
        } else if (this.minecraft.player != null) {
            this.posX = (float) (this.minecraft.player.getX() / LOTRMapCoords.SCALE) + 810.0f;
            this.posY = (float) (this.minecraft.player.getZ() / LOTRMapCoords.SCALE) + 730.0f;
        }
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        setupZoomVariables(1.0f);
        this.buttonOverlayFunction = Button.builder(Component.empty(), b -> overlayFunction()).bounds(0, 0, 160, 20).build();
        this.buttonOverlayFunction.visible = false;
        this.buttonOverlayFunction.active = false;
        addWidget(this.buttonOverlayFunction);
        this.nameWPTextField = new EditBox(this.font, this.mapXMin + this.mapWidth / 2 - 80, this.mapYMin + 50, 160, 20, Component.empty());
        this.nameWPTextField.setVisible(false);
        addWidget(this.nameWPTextField);
        this.fellowshipDrawGUI = new LOTRFellowshipsScreen();
        this.fellowshipDrawGUI.init(this.width, this.height);
        if (this.minecraft.gui.screen() == this) {
            LOTRClientMiniQuests.sendEvent(LOTRMiniQuestEvent.VIEW_MAP);
        }
    }

    private void setupMapDimensions() {
        if (this.isConquestGrid) {
            int windowWidth = 400;
            int windowHeight = 240;
            this.mapXMin = this.width / 2 - windowWidth / 2;
            this.mapXMax = this.width / 2 + windowWidth / 2;
            this.mapYMin = this.height / 2 - 16 - windowHeight / 2;
            this.mapYMax = this.mapYMin + windowHeight;
        } else if (fullscreen) {
            this.mapXMin = 30;
            this.mapXMax = this.width - 30;
            this.mapYMin = 30;
            this.mapYMax = this.height - 30;
        } else {
            int windowWidth = 312;
            this.mapXMin = this.width / 2 - windowWidth / 2;
            this.mapXMax = this.width / 2 + windowWidth / 2;
            this.mapYMin = this.guiTop;
            this.mapYMax = this.guiTop + 200;
        }
        this.mapWidth = this.mapXMax - this.mapXMin;
        this.mapHeight = this.mapYMax - this.mapYMin;
    }

    private void setupMapWidgets() {
        this.mapWidgets.clear();
        this.widgetAddCWP = addMapWidget(-16, 6, "addCWP", 0, 0);
        this.widgetDelCWP = addMapWidget(-16, 20, "delCWP", 10, 0);
        this.widgetRenameCWP = addMapWidget(-16, 34, "renameCWP", 0, 20);
        this.widgetToggleWPs = addMapWidget(-58, 6, "toggleWPs", 80, 0);
        this.widgetToggleCWPs = addMapWidget(-44, 6, "toggleCWPs", 90, 0);
        this.widgetToggleHiddenSWPs = addMapWidget(-30, 6, "toggleHiddenSWPs", 100, 0);
        this.widgetZoomIn = addMapWidget(6, 6, "zoomIn", 30, 0);
        this.widgetZoomOut = addMapWidget(6, 20, "zoomOut", 40, 0);
        this.widgetFullScreen = addMapWidget(20, 6, "fullScreen", 50, 0);
        this.widgetSepia = addMapWidget(34, 6, "sepia", 60, 0);
        this.widgetLabels = addMapWidget(-72, 6, "labels", 70, 0);
        this.widgetShareCWP = addMapWidget(-16, 48, "shareCWP", 10, 10);
        this.widgetHideSWP = addMapWidget(-16, 20, "hideSWP", 20, 0);
        this.widgetUnhideSWP = addMapWidget(-16, 20, "unhideSWP", 20, 10);
        if (this.isConquestGrid) {
            this.mapWidgets.clear();
            this.mapWidgets.addAll(List.of(this.widgetToggleWPs, this.widgetToggleCWPs, this.widgetToggleHiddenSWPs,
                    this.widgetZoomIn, this.widgetZoomOut, this.widgetLabels));
        }
    }

    private Widget addMapWidget(int x, int y, String name, int u, int v) {
        Widget widget = new Widget(x, y, 10, name, u, v);
        this.mapWidgets.add(widget);
        return widget;
    }

    private void setupZoomVariables(float f) {
        this.zoomExp = zoomPower;
        if (this.zoomTicks > 0) {
            float progress = (ZOOM_TICKS_MAX - (this.zoomTicks - f)) / ZOOM_TICKS_MAX;
            this.zoomExp = this.prevZoomPower + (zoomPower - this.prevZoomPower) * progress;
        }
        this.zoomScale = (float) Math.pow(2.0, this.zoomExp);
        this.zoomScaleStable = (float) Math.pow(2.0, this.zoomTicks == 0 ? zoomPower : Math.min(zoomPower, this.prevZoomPower));
    }

    private void setupScrollBars(int i, int j) {
        int maxDisplayedWPShares = fullscreen ? 8 : 5;
        if (this.selectedWaypoint instanceof LOTRCustomWaypoint cwp && this.hasOverlay && this.sharingWaypoint) {
            this.displayedWPShareList = cwp.sharedFellowshipIDs;
            this.displayedWPShares = this.displayedWPShareList.size();
            this.scrollPaneWPShares.hasScrollBar = false;
            if (this.displayedWPShares > maxDisplayedWPShares) {
                this.displayedWPShares = maxDisplayedWPShares;
                this.scrollPaneWPShares.hasScrollBar = true;
            }
        } else {
            this.displayedWPShareList = null;
            this.displayedWPShares = 0;
            this.scrollPaneWPShares.hasScrollBar = false;
            this.scrollPaneWPShares.mouseDragScroll(i, j, isLeftButtonDown());
        }
    }

    // ---------------------------------------------------------------- state

    private boolean isMiddleEarth() {
        return this.minecraft.player != null && this.minecraft.player.level().dimension() == LOTRWorldGen.MIDDLE_EARTH;
    }

    private boolean hasMapLabels() {
        if (this.isConquestGrid) {
            return LOTRConfig.mapLabelsConquest;
        }
        return LOTRConfig.mapLabels;
    }

    private boolean canCreateWaypointAtPosition() {
        int minY = LOTRClientCustomWaypoints.getCustomWaypointMinY();
        return minY < 0 || this.minecraft.player.getBoundingBox().minY >= minY;
    }

    /** canTeleport: in Middle-earth, standing in a loaded chunk, and an operator as the server says (requestIsOp). */
    private boolean canTeleport() {
        if (!isMiddleEarth() || this.loadingConquestGrid) {
            return false;
        }
        LocalPlayer player = this.minecraft.player;
        if (!this.minecraft.level.getChunkSource().hasChunk(Mth.floor(player.getX()) >> 4, Mth.floor(player.getZ()) >> 4)) {
            return false;
        }
        return LOTRClientMap.requestIsOp();
    }

    private boolean isWaypointVisible(LOTRAbstractWaypoint wp) {
        if (wp instanceof LOTRCustomWaypoint cwp) {
            if (cwp.isShared() && cwp.isSharedHidden() && !showHiddenSWP()) {
                return false;
            }
            return showCWP();
        }
        return showWP();
    }

    /** getAllAvailableWaypoints: the fixed ones, the player's own, and those shared with them. */
    private static List<LOTRAbstractWaypoint> getAllAvailableWaypoints() {
        List<LOTRAbstractWaypoint> list = new ArrayList<>(List.of(LOTRWaypoint.values()));
        list.addAll(LOTRClientCustomWaypoints.getCustomWaypoints());
        list.addAll(LOTRClientCustomWaypoints.getSharedCustomWaypoints());
        return list;
    }

    private static boolean isValidWaypointName(String name) {
        return !name.isBlank();
    }

    private static @Nullable View getFellowshipByName(String name) {
        return LOTRClientFellowships.getByName(name.strip());
    }

    private static boolean isExistingFellowshipName(String name) {
        return getFellowshipByName(name) != null;
    }

    private static boolean isExistingUnsharedFellowshipName(String name, LOTRCustomWaypoint waypoint) {
        View fs = getFellowshipByName(name);
        return fs != null && !waypoint.hasSharedFellowship(fs.fellowshipID());
    }

    private static boolean isFellowshipAlreadyShared(String name, LOTRCustomWaypoint waypoint) {
        return isExistingFellowshipName(name) && !isExistingUnsharedFellowshipName(name, waypoint);
    }

    /**
     * Mouse.isButtonDown(0). Today's MouseHandler.isLeftPressed only follows the button while no
     * screen is open, so the button is read from the window itself.
     */
    private boolean isLeftButtonDown() {
        return GLFW.glfwGetMouseButton(this.minecraft.getWindow().handle(), GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;
    }

    private void zoom(int boost) {
        this.prevZoomPower = zoomPower;
        zoomPower += boost;
        this.zoomTicks = ZOOM_TICKS_MAX;
        this.selectedWaypoint = null;
    }

    // ---------------------------------------------------------------- drawing

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        setupMapDimensions();
        if (this.isConquestGrid) {
            // The frame's parchment, in the colour of the faction viewed.
            super.extractBackground(graphics, mouseX, mouseY, partialTick);
            float[] cqColors = this.conquestViewingFaction != null ? this.conquestViewingFaction.getFactionRGB() : new float[]{0.65f, 0.5f, 0.35f};
            int w = 8;
            int up = 22;
            int down = 54;
            graphics.blit(RenderPipelines.GUI_TEXTURED, LOTRMapRenderer.MAP_OVERLAY, this.mapXMin - w, this.mapYMin - up, 0.0f, 0.0f,
                    this.mapWidth + w * 2, this.mapHeight + up + down, 256, 256, 256, 256, ARGB.colorFromFloat(1.0f, cqColors[0], cqColors[1], cqColors[2]));
        } else if (fullscreen) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, LOTRMapRenderer.MAP_OVERLAY, 0, 0, 0.0f, 0.0f, this.width, this.height, 256, 256, 256, 256,
                    ARGB.colorFromFloat(1.0f, 0.65f, 0.5f, 0.35f));
        } else {
            super.extractBackground(graphics, mouseX, mouseY, partialTick);
        }
        renderGraduatedRects(graphics, this.mapXMin - 1, this.mapYMin - 1, this.mapXMax + 1, this.mapYMax + 1, BORDER_COLOR, -16777216,
                this.isConquestGrid ? 2 : 4);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int i, int j, float partialTick) {
        float f = this.minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(false);
        LocalPlayer player = this.minecraft.player;
        setupMapDimensions();
        setupZoomVariables(f);
        if (this.isConquestGrid) {
            this.loadingConquestGrid = !this.receivedFacGrids.contains(this.conquestViewingFaction);
            this.highestViewedConqStr = 0.0f;
            setupConquestScrollBar(i, j);
            this.buttonConquestRegions.setMessage(currentRegion.getRegionName());
        }
        this.posX = this.prevPosX;
        this.posY = this.prevPosY;
        this.isMouseWithinMap = i >= this.mapXMin && i < this.mapXMax && j >= this.mapYMin && j < this.mapYMax;
        if (!this.hasOverlay && !this.isFacScrolling && this.zoomTicks == 0 && isLeftButtonDown()) {
            if ((this.isMouseButtonDown == 0 || this.isMouseButtonDown == 1) && this.isMouseWithinMap) {
                if (this.isMouseButtonDown == 0) {
                    this.isMouseButtonDown = 1;
                } else {
                    float x2 = (i - this.prevMouseX) / this.zoomScale;
                    float y2 = (j - this.prevMouseY) / this.zoomScale;
                    this.posX -= x2;
                    this.posY -= y2;
                    if (x2 != 0.0f || y2 != 0.0f) {
                        this.selectedWaypoint = null;
                    }
                }
                this.prevMouseX = i;
                this.prevMouseY = j;
            }
        } else {
            this.isMouseButtonDown = 0;
        }
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.posX += this.posXMove * f;
        this.posY += this.posYMove * f;
        boolean isSepia = this.isConquestGrid || LOTRConfig.enableSepiaMap;
        setupScrollBars(i, j);
        graphics.fill(this.mapXMin, this.mapYMin, this.mapXMax, this.mapYMax, LOTRMapRenderer.getMapOceanColor(isSepia));
        if (!this.isConquestGrid) {
            Component title = Component.translatable("lotr.gui.map.title");
            graphics.centeredText(this.font, title, this.width / 2, fullscreen ? 10 : this.guiTop - 30, 0xFFFFFFFF);
        }
        this.map.setView(this.posX, this.posY, this.zoomExp, this.zoomScaleStable);
        this.map.setWindow(this.mapXMin, this.mapYMin, this.mapXMax, this.mapYMax);
        this.map.enableZoomOutWPFading = true;
        this.raisedTooltips.clear();
        if (this.hasControlZones && LOTRFaction.controlZonesEnabled(this.minecraft.level)) {
            this.map.drawMapAndOverlay(graphics, player, isSepia, 1.0f, false);
            renderControlZone(graphics, i, j);
            this.map.drawMapAndOverlay(graphics, player, isSepia, 0.5f, true);
            Component tooltip = null;
            if (this.mouseControlZone) {
                tooltip = Component.translatable("lotr.gui.map.controlZoneFull");
            } else if (this.mouseControlZoneReduced) {
                tooltip = Component.translatable("lotr.gui.map.controlZoneReduced");
            }
            if (tooltip != null) {
                Component zoneTooltip = tooltip;
                this.raisedTooltips.add(() -> {
                    int strWidth = this.font.width(zoneTooltip);
                    int border = 3;
                    int rectWidth = strWidth + border * 2;
                    int rectHeight = this.font.lineHeight + border * 2;
                    int mapBorder2 = 2;
                    int rectX = Math.min(Math.max(i + 12, this.mapXMin + mapBorder2), this.mapXMax - mapBorder2 - rectWidth);
                    int rectY = Math.min(Math.max(j - 12, this.mapYMin + mapBorder2), this.mapYMax - mapBorder2 - rectHeight);
                    drawFancyRect(graphics, rectX, rectY, rectX + rectWidth, rectY + rectHeight);
                    graphics.text(this.font, zoneTooltip, rectX + border, rectY + border, 0xFFFFFFFF, false);
                });
            }
        } else {
            this.map.drawMapAndOverlay(graphics, player, isSepia, 1.0f, true);
            if (this.isConquestGrid && this.conquestViewingFaction != null) {
                requestConquestGridIfNeed(this.conquestViewingFaction);
                if (!this.loadingConquestGrid) {
                    renderConquestZones(graphics, i, j);
                }
            }
        }
        // LOTRTextures.drawMapCompassBottomLeft.
        graphics.blit(RenderPipelines.GUI_TEXTURED, LOTRMapRenderer.MAP_ICONS, this.mapXMin + 12, this.mapYMax - 12 - 32, 224.0f, 200.0f, 32, 32, 256, 256);
        if ((showWP() || showCWP()) && LOTRFixedStructures.hasMapFeatures()) {
            this.map.drawRoads(graphics, this.font, hasMapLabels());
        }
        renderPlayers(graphics, i, j);
        if (!this.loadingConquestGrid) {
            renderMiniQuests(graphics, i, j);
        }
        List<LOTRAbstractWaypoint> waypoints = getAllAvailableWaypoints();
        boolean showWaypoints = (showWP() || showCWP() || showHiddenSWP()) && LOTRFixedStructures.hasMapFeatures();
        if (showWaypoints) {
            this.map.drawWaypoints(graphics, this.font, waypoints, player, this::isWaypointVisible, hasMapLabels());
        }
        if (hasMapLabels()) {
            this.map.drawLabels(graphics, this.font);
        }
        this.raisedTooltips.forEach(Runnable::run);
        if (showWaypoints) {
            LOTRAbstractWaypoint mouseOverWP = this.map.waypointUnderMouse(waypoints, player, this::isWaypointVisible, i, j, this.selectedWaypoint);
            if (mouseOverWP != null && !this.hasOverlay && !this.loadingConquestGrid) {
                renderWaypointTooltip(graphics, mouseOverWP, false);
            }
        }
        if (!this.loadingConquestGrid && this.selectedWaypoint != null && isWaypointVisible(this.selectedWaypoint)) {
            if (!this.hasOverlay) {
                renderWaypointTooltip(graphics, this.selectedWaypoint, true);
            }
        } else {
            this.selectedWaypoint = null;
        }
        if (this.isConquestGrid) {
            if (this.loadingConquestGrid) {
                renderConquestLoading(graphics);
            }
            graphics.blit(RenderPipelines.GUI_TEXTURED, CONQUEST_TEXTURE, this.mapXMin - 8, this.mapYMin - 22, 0.0f, 0.0f,
                    this.mapWidth + 16, this.mapHeight + 22 + 54, 512, 512);
        }
        if (!this.hasOverlay) {
            if (isMiddleEarth() && this.selectedWaypoint != null) {
                renderSelectedWaypointInfo(graphics);
            } else if (this.isMouseWithinMap) {
                renderMouseInfo(graphics, i, j);
            }
        }
        if (this.isConquestGrid && this.conquestViewingFaction != null) {
            renderConquestHeader(graphics);
        }
        if (!this.hasOverlay && this.hasControlZones && this.controlZoneFaction != null) {
            Component s = Component.translatable("lotr.gui.map.controlZones", this.controlZoneFaction.factionName());
            int x = this.mapXMin + this.mapWidth / 2;
            LOTRAlignmentBarRenderer.drawAlignmentText(graphics, this.font, x - this.font.width(s) / 2, this.mapYMin + 20, s, 1.0f);
            if (!LOTRFaction.controlZonesEnabled(this.minecraft.level)) {
                s = Component.translatable("lotr.gui.map.controlZonesDisabled");
                LOTRAlignmentBarRenderer.drawAlignmentText(graphics, this.font, x - this.font.width(s) / 2, this.mapYMin + this.mapHeight / 2, s, 1.0f);
            }
        }
        super.extractRenderState(graphics, i, j, partialTick);
        renderMapWidgets(graphics, i, j);
        if (this.hasOverlay) {
            renderOverlay(graphics, i, j, partialTick);
        }
    }

    private void renderSelectedWaypointInfo(GuiGraphicsExtractor graphics) {
        LocalPlayer player = this.minecraft.player;
        LOTRAbstractWaypoint wp = this.selectedWaypoint;
        boolean hasUnlocked = wp.hasPlayerUnlocked(player);
        int ftSince = LOTRClientFastTravel.getTimeSinceFT();
        int wpTimeThreshold = LOTRClientFastTravel.getWaypointFTTime(wp, player);
        int timeRemaining = wpTimeThreshold - ftSince;
        boolean canFastTravel = hasUnlocked && timeRemaining <= 0;
        Component notUnlocked = Component.literal("If you can read this, something has gone hideously wrong");
        if (wp instanceof LOTRCustomWaypoint cwp) {
            if (cwp.isShared()) {
                notUnlocked = Component.translatable("lotr.gui.map.locked.shared");
            }
        } else if (wp instanceof LOTRWaypoint fixed) {
            notUnlocked = Component.translatable(!fixed.isCompatibleAlignment(player) ? "lotr.gui.map.locked.enemy" : "lotr.gui.map.locked.region");
        }
        LOTRFaction pledge = LOTRPlayerAlignments.get(player).pledgeFaction();
        Component conquestUnlock = pledge == null ? Component.empty() : Component.translatable("lotr.gui.map.locked.conquerable", pledge.factionName());
        boolean conquerable = wp instanceof LOTRWaypoint fixedWp && fixedWp.isConquestUnlockable(player);
        Component ftPrompt = Component.translatable("lotr.gui.map.fastTravel.prompt", LOTRKeyBindings.fastTravel.getTranslatedKeyMessage());
        Component ftMoreTime = Component.translatable("lotr.gui.map.fastTravel.moreTime", LOTRMiscCommands.hmsTime(timeRemaining));
        Component ftWaitTime = Component.translatable("lotr.gui.map.fastTravel.waitTime", LOTRMiscCommands.hmsTime(wpTimeThreshold));
        if (fullscreen || this.isConquestGrid) {
            if (!hasUnlocked) {
                if (conquerable) {
                    renderFullscreenSubtitles(graphics, notUnlocked, conquestUnlock);
                } else {
                    renderFullscreenSubtitles(graphics, notUnlocked);
                }
            } else if (canFastTravel) {
                renderFullscreenSubtitles(graphics, ftPrompt, ftWaitTime);
            } else {
                renderFullscreenSubtitles(graphics, ftMoreTime, ftWaitTime);
            }
        } else {
            List<Component> lines = new ArrayList<>();
            if (hasUnlocked) {
                lines.add(canFastTravel ? ftPrompt : ftMoreTime);
                lines.add(ftWaitTime);
            } else {
                lines.add(notUnlocked);
                if (conquerable) {
                    lines.add(conquestUnlock);
                }
            }
            int stringHeight = this.font.lineHeight;
            int border = 3;
            int rectHeight = border + (stringHeight + border) * lines.size();
            int x3 = this.mapXMin;
            int y3 = this.mapYMax + 10;
            int strX = this.mapXMin + this.mapWidth / 2;
            int strY = y3 + border;
            drawFancyRect(graphics, x3, y3, x3 + this.mapWidth, y3 + rectHeight);
            for (Component s : lines) {
                graphics.centeredText(this.font, s, strX, strY, 0xFFFFFFFF);
                strY += stringHeight + border;
            }
        }
    }

    private void renderMouseInfo(GuiGraphicsExtractor graphics, int i, int j) {
        float biomePosX = this.posX + (i - this.mapXMin - (float) this.mapWidth / 2) / this.zoomScale;
        float biomePosZ = this.posY + (j - this.mapYMin - (float) this.mapHeight / 2) / this.zoomScale;
        LOTRBiome biome = LOTRGenLayerWorld.getBiomeOrOcean(Mth.floor(biomePosX), Mth.floor(biomePosZ));
        if (biome.isHiddenBiome() && !LOTRPlayerAchievements.hasAchievement(this.minecraft.player, biome.getBiomeAchievement())) {
            biome = LOTRBiomes.OCEAN;
        }
        this.mouseXCoord = Math.round((biomePosX - 810.0f) * LOTRMapCoords.SCALE);
        this.mouseZCoord = Math.round((biomePosZ - 730.0f) * LOTRMapCoords.SCALE);
        Component biomeName = biome.getBiomeDisplayName();
        Component coords = Component.translatable("lotr.gui.map.coords", this.mouseXCoord, this.mouseZCoord);
        Component teleport = Component.translatable("lotr.gui.map.tp", LOTRKeyBindings.mapTeleport.getTranslatedKeyMessage());
        int stringHeight = this.font.lineHeight;
        if (fullscreen || this.isConquestGrid) {
            renderFullscreenSubtitles(graphics, biomeName, coords);
            if (canTeleport()) {
                graphics.pose().pushMatrix();
                if (this.isConquestGrid) {
                    graphics.pose().translate((float) (this.mapXMax - this.mapXMin) / 2 - 8 - (float) this.font.width(teleport) / 2, 0.0f);
                } else {
                    graphics.pose().translate((float) this.width / 2 - 30 - (float) this.font.width(teleport) / 2, 0.0f);
                }
                renderFullscreenSubtitles(graphics, teleport);
                graphics.pose().popMatrix();
            }
        } else {
            int border = 3;
            int rectHeight = border * 3 + stringHeight * 2;
            if (canTeleport()) {
                rectHeight += (stringHeight + border) * 2;
            }
            int x4 = this.mapXMin;
            int y4 = this.mapYMax + 10;
            drawFancyRect(graphics, x4, y4, x4 + this.mapWidth, y4 + rectHeight);
            int strX = this.mapXMin + this.mapWidth / 2;
            int strY = y4 + border;
            graphics.centeredText(this.font, biomeName, strX, strY, 0xFFFFFFFF);
            graphics.centeredText(this.font, coords, strX, strY += stringHeight + border, 0xFFFFFFFF);
            if (canTeleport()) {
                graphics.centeredText(this.font, teleport, strX, strY + (stringHeight + border) * 2, 0xFFFFFFFF);
            }
        }
    }

    private void renderFullscreenSubtitles(GuiGraphicsExtractor graphics, Component... lines) {
        int strX = this.mapXMin + this.mapWidth / 2;
        int strY = this.mapYMax + 7;
        if (this.isConquestGrid) {
            strY = this.mapYMax + 26;
        }
        int border = this.font.lineHeight + 3;
        if (lines.length == 1) {
            strY += border / 2;
        }
        for (Component s : lines) {
            graphics.centeredText(this.font, s, strX, strY, 0xFFFFFFFF);
            strY += border;
        }
    }

    private static void renderGraduatedRects(GuiGraphicsExtractor graphics, int x1, int y1, int x2, int y2, int c1, int c2, int w) {
        for (int l = w - 1; l >= 0; --l) {
            float f = (float) l / (w - 1);
            int r = Math.round(ARGB.red(c1) + (ARGB.red(c2) - ARGB.red(c1)) * f);
            int g = Math.round(ARGB.green(c1) + (ARGB.green(c2) - ARGB.green(c1)) * f);
            int b = Math.round(ARGB.blue(c1) + (ARGB.blue(c2) - ARGB.blue(c1)) * f);
            graphics.fill(x1 - l, y1 - l, x2 + l, y2 + l, ARGB.color(255, r, g, b) - 16777216);
        }
    }

    /** drawFancyRect: a dark box with a one-pixel red-brown border. */
    private static void drawFancyRect(GuiGraphicsExtractor graphics, int x1, int y1, int x2, int y2) {
        graphics.fill(x1, y1, x2, y2, -1073741824);
        graphics.fill(x1 - 1, y1 - 1, x2 + 1, y1, BORDER_COLOR);
        graphics.fill(x1 - 1, y2, x2 + 1, y2 + 1, BORDER_COLOR);
        graphics.fill(x1 - 1, y1, x1, y2, BORDER_COLOR);
        graphics.fill(x2, y1, x2 + 1, y2, BORDER_COLOR);
    }

    /** A name in a box below a point on the map, kept within the map. */
    private void raiseNameTooltip(GuiGraphicsExtractor graphics, Component name, int x, int y) {
        this.raisedTooltips.add(() -> {
            int border = 3;
            int rectWidth = this.font.width(name) + border * 2;
            int rectHeight = this.font.lineHeight + border * 2;
            int mapBorder2 = 2;
            int rectX = Math.min(Math.max(x - rectWidth / 2, this.mapXMin + mapBorder2), this.mapXMax - mapBorder2 - rectWidth);
            int rectY = Math.min(Math.max(y, this.mapYMin + mapBorder2), this.mapYMax - mapBorder2 - rectHeight);
            drawFancyRect(graphics, rectX, rectY, rectX + rectWidth, rectY + rectHeight);
            graphics.text(this.font, name, rectX + border, rectY + border, 0xFFFFFFFF, false);
        });
    }

    /** renderControlZone: each zone's reduced range, then its full range ringed in dark red, as filled circles. */
    private void renderControlZone(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        this.mouseControlZone = false;
        this.mouseControlZoneReduced = false;
        LOTRFaction faction = this.controlZoneFaction;
        List<LOTRControlZone> controlZones = faction.controlZones;
        if (faction.factionDimension != LOTRDimension.MIDDLE_EARTH || controlZones.isEmpty()) {
            return;
        }
        graphics.enableScissor(this.mapXMin, this.mapYMin, this.mapXMax, this.mapYMax);
        LOTRMapQuadsRenderState quads = new LOTRMapQuadsRenderState(new ScreenRectangle(this.mapXMin, this.mapYMin, this.mapWidth, this.mapHeight),
                graphics.scissorStack.peek());
        for (int pass = 0; pass <= 2; ++pass) {
            int color = 16711680;
            if (pass == 1) {
                color = 5570560;
            }
            if (pass == 0) {
                color = 16733525;
            }
            color |= 0xFF000000;
            for (LOTRControlZone zone : controlZones) {
                float radius = zone.radius;
                if (pass == 2) {
                    radius -= 1.0f;
                }
                if (pass == 0) {
                    radius = zone.radius + faction.getControlZoneReducedRange();
                }
                float radiusWorld = LOTRMapCoords.mapToWorldR(radius);
                float[] centre = this.map.transformCoords(zone.xCoord, zone.zCoord);
                int sides = 100;
                float[] prev = null;
                for (int l = sides; l >= 0; --l) {
                    float angle = (float) l / sides * 2.0f * (float) Math.PI;
                    float[] point = this.map.transformCoords(zone.xCoord + Mth.cos(angle) * radiusWorld, zone.zCoord + Mth.sin(angle) * radiusWorld);
                    if (prev != null) {
                        quads.addQuad(centre[0], centre[1], prev[0], prev[1], point[0], point[1], point[0], point[1], color);
                    }
                    prev = point;
                }
                if (this.mouseControlZone && this.mouseControlZoneReduced) {
                    continue;
                }
                float dx = mouseX - centre[0];
                float dy = mouseY - centre[1];
                float rScaled = radius * this.zoomScale;
                if (dx * dx + dy * dy > rScaled * rScaled) {
                    continue;
                }
                if (pass >= 1) {
                    this.mouseControlZone = true;
                } else {
                    this.mouseControlZoneReduced = true;
                }
            }
        }
        graphics.guiRenderState.addGuiElement(quads);
        graphics.disableScissor();
    }

    /** renderPlayers: each player's face where they stand, kept within the map, and the name of the one under the mouse. */
    private void renderPlayers(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        Component mouseOverPlayerName = null;
        int mouseOverPlayerX = 0;
        int mouseOverPlayerY = 0;
        double distanceMouseOverPlayer = Double.MAX_VALUE;
        int iconWidthHalf = 4;
        Map<UUID, LOTRClientMap.PlayerLocationInfo> playersToRender = new HashMap<>(LOTRClientMap.getPlayerLocations());
        LocalPlayer player = this.minecraft.player;
        if (isMiddleEarth()) {
            playersToRender.put(player.getUUID(), new LOTRClientMap.PlayerLocationInfo(player.getGameProfile(), player.getX(), player.getZ()));
        }
        for (LOTRClientMap.PlayerLocationInfo info : playersToRender.values()) {
            float[] pos = this.map.transformCoords(info.posX(), info.posZ());
            int playerX = Math.round(pos[0]);
            int playerY = Math.round(pos[1]);
            double distToPlayer = renderPlayerIcon(graphics, info, playerX, playerY, mouseX, mouseY);
            if (distToPlayer > iconWidthHalf + 3 || distToPlayer > distanceMouseOverPlayer) {
                continue;
            }
            mouseOverPlayerName = Component.literal(info.profile().name());
            mouseOverPlayerX = playerX;
            mouseOverPlayerY = playerY;
            distanceMouseOverPlayer = distToPlayer;
        }
        if (mouseOverPlayerName != null && !this.hasOverlay) {
            raiseNameTooltip(graphics, mouseOverPlayerName, mouseOverPlayerX, mouseOverPlayerY + iconWidthHalf + 3);
        }
    }

    private double renderPlayerIcon(GuiGraphicsExtractor graphics, LOTRClientMap.PlayerLocationInfo info, int playerX, int playerY,
                                    int mouseX, int mouseY) {
        int iconWidthHalf = 4;
        int iconBorder = iconWidthHalf + 1;
        playerX = Math.min(Math.max(this.mapXMin + iconBorder, playerX), this.mapXMax - iconBorder - 1);
        playerY = Math.min(Math.max(this.mapYMin + iconBorder, playerY), this.mapYMax - iconBorder - 1);
        LocalPlayer player = this.minecraft.player;
        PlayerSkin skin = info.profile().id().equals(player.getUUID()) ? player.getSkin() : LOTRClientMap.getSkin(info.profile());
        PlayerFaceExtractor.extractRenderState(graphics, skin, playerX - iconWidthHalf, playerY - iconWidthHalf, iconWidthHalf * 2);
        double dx = playerX - mouseX;
        double dy = playerY - mouseY;
        return Math.sqrt(dx * dx + dy * dy);
    }

    /** renderMiniQuests: a small red book where each active quest's giver was last, and the giver's name under the mouse. */
    private void renderMiniQuests(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        if (this.hasOverlay) {
            return;
        }
        LOTRMiniQuest mouseOverQuest = null;
        int mouseOverQuestX = 0;
        int mouseOverQuestY = 0;
        double distanceMouseOverQuest = Double.MAX_VALUE;
        for (LOTRMiniQuest quest : LOTRClientMiniQuests.getActiveMiniQuests()) {
            if (quest.lastLocation == null) {
                continue;
            }
            float[] pos = this.map.transformCoords(quest.lastLocation.getX(), quest.lastLocation.getZ());
            int questX = Math.round(pos[0]);
            int questY = Math.round(pos[1]);
            float scale = 0.5f;
            float invScale = 1.0f / scale;
            int iconWidthHalf = (int) (8 * scale);
            int iconBorder = iconWidthHalf + 1;
            questX = Math.min(Math.max(this.mapXMin + iconBorder, questX), this.mapXMax - iconBorder - 1);
            questY = Math.min(Math.max(this.mapYMin + iconBorder, questY), this.mapYMax - iconBorder - 1);
            int iconX = Math.round(questX * invScale);
            int iconY = Math.round(questY * invScale);
            graphics.pose().pushMatrix();
            graphics.pose().scale(scale, scale);
            graphics.item(QUEST_BOOK_ICON, iconX - iconWidthHalf, iconY - iconWidthHalf);
            graphics.pose().popMatrix();
            double dx = questX - mouseX;
            double dy = questY - mouseY;
            double distToQuest = Math.sqrt(dx * dx + dy * dy);
            if (distToQuest > iconWidthHalf + 3 || distToQuest > distanceMouseOverQuest) {
                continue;
            }
            mouseOverQuest = quest;
            mouseOverQuestX = questX;
            mouseOverQuestY = questY;
            distanceMouseOverQuest = distToQuest;
        }
        if (mouseOverQuest != null) {
            raiseNameTooltip(graphics, Component.literal(mouseOverQuest.entityName), mouseOverQuestX, mouseOverQuestY + 7);
        }
    }

    /** renderWaypointTooltip: the name; selected, its coordinates and its lore too, smaller. */
    private void renderWaypointTooltip(GuiGraphicsExtractor graphics, LOTRAbstractWaypoint waypoint, boolean selected) {
        Component name = waypoint.getDisplayName();
        int wpX = waypoint.getXCoord();
        int wpZ = waypoint.getZCoord();
        int wpY = waypoint.getYCoordSaved();
        Component coords = wpY >= 0 ? Component.translatable("lotr.gui.map.coordsY", wpX, wpY, wpZ)
                : Component.translatable("lotr.gui.map.coords", wpX, wpZ);
        Component loreText = waypoint.getLoreText(this.minecraft.player);
        float guiScale = this.minecraft.getWindow().getGuiScale();
        float loreScale = guiScale <= 2.0f ? guiScale : guiScale - 1.0f;
        float loreScaleRel = loreScale / guiScale;
        float loreScaleRelInv = 1.0f / loreScaleRel;
        int loreFontHeight = Mth.ceil(this.font.lineHeight * loreScaleRel);
        float[] pos = this.map.transformCoords(wpX, wpZ);
        int rectX = Math.round(pos[0]);
        int rectY = Math.round(pos[1]) + 5;
        int border = 3;
        int stringHeight = this.font.lineHeight;
        int innerRectWidth = this.font.width(name);
        if (selected) {
            innerRectWidth = Math.max(innerRectWidth, this.font.width(coords));
            if (loreText != null) {
                innerRectWidth += 50;
                innerRectWidth = Math.round(innerRectWidth * (loreScaleRel / 0.66667f));
            }
        }
        int rectWidth = innerRectWidth + border * 2;
        rectX -= rectWidth / 2;
        int rectHeight = border * 2 + stringHeight;
        List<FormattedCharSequence> loreLines = List.of();
        if (selected) {
            rectHeight += border + stringHeight;
            if (loreText != null) {
                loreLines = this.font.split(loreText, (int) (innerRectWidth * loreScaleRelInv));
                rectHeight += border + loreLines.size() * loreFontHeight + border;
            }
        }
        int mapBorder2 = 2;
        rectX = Math.min(Math.max(rectX, this.mapXMin + mapBorder2), this.mapXMax - mapBorder2 - rectWidth);
        rectY = Math.min(Math.max(rectY, this.mapYMin + mapBorder2), this.mapYMax - mapBorder2 - rectHeight);
        int stringX = rectX + rectWidth / 2;
        int stringY = rectY + border;
        drawFancyRect(graphics, rectX, rectY, rectX + rectWidth, rectY + rectHeight);
        graphics.centeredText(this.font, name, stringX, stringY, 0xFFFFFFFF);
        stringY += this.font.lineHeight + border;
        if (selected) {
            graphics.centeredText(this.font, coords, stringX, stringY, 0xFFFFFFFF);
            stringY += this.font.lineHeight + border * 2;
            graphics.pose().pushMatrix();
            graphics.pose().scale(loreScaleRel, loreScaleRel);
            for (FormattedCharSequence line : loreLines) {
                graphics.centeredText(this.font, line, (int) (stringX * loreScaleRelInv), (int) (stringY * loreScaleRelInv), 0xFFFFFFFF);
                stringY += loreFontHeight;
            }
            graphics.pose().popMatrix();
        }
    }

    private void renderMapWidgets(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        boolean me = isMiddleEarth();
        LOTRCustomWaypoint ownCWP = this.selectedWaypoint instanceof LOTRCustomWaypoint cwp && !cwp.isShared() ? cwp : null;
        LOTRCustomWaypoint sharedCWP = this.selectedWaypoint instanceof LOTRCustomWaypoint cwp && cwp.isShared() ? cwp : null;
        this.widgetAddCWP.visible = !this.hasOverlay && me;
        this.widgetAddCWP.texVIndex = canCreateWaypointAtPosition() ? 0 : 1;
        this.widgetDelCWP.visible = !this.hasOverlay && me && ownCWP != null;
        this.widgetRenameCWP.visible = !this.hasOverlay && me && ownCWP != null;
        this.widgetToggleWPs.visible = !this.hasOverlay;
        this.widgetToggleWPs.texVIndex = showWP() ? 0 : 1;
        this.widgetToggleCWPs.visible = !this.hasOverlay;
        this.widgetToggleCWPs.texVIndex = showCWP() ? 0 : 1;
        this.widgetToggleHiddenSWPs.visible = !this.hasOverlay;
        this.widgetToggleHiddenSWPs.texVIndex = showHiddenSWP() ? 1 : 0;
        this.widgetZoomIn.visible = !this.hasOverlay;
        this.widgetZoomIn.texVIndex = zoomPower < 4 ? 0 : 1;
        this.widgetZoomOut.visible = !this.hasOverlay;
        this.widgetZoomOut.texVIndex = zoomPower > -3 ? 0 : 1;
        this.widgetFullScreen.visible = !this.hasOverlay;
        this.widgetSepia.visible = !this.hasOverlay;
        this.widgetLabels.visible = !this.hasOverlay;
        this.widgetShareCWP.visible = !this.hasOverlay && me && ownCWP != null;
        this.widgetHideSWP.visible = !this.hasOverlay && me && sharedCWP != null && !sharedCWP.isSharedHidden();
        this.widgetUnhideSWP.visible = !this.hasOverlay && me && sharedCWP != null && sharedCWP.isSharedHidden();
        Widget mouseOverWidget = null;
        for (Widget widget : this.mapWidgets) {
            if (!widget.visible) {
                continue;
            }
            graphics.blit(RenderPipelines.GUI_TEXTURED, LOTRMapRenderer.MAP_ICONS, this.mapXMin + widget.getMapXPos(this.mapWidth),
                    this.mapYMin + widget.getMapYPos(this.mapHeight), widget.texUBase, widget.getTexV(), widget.width, widget.width, 256, 256);
            if (widget.isMouseOver(mouseX - this.mapXMin, mouseY - this.mapYMin, this.mapWidth, this.mapHeight)) {
                mouseOverWidget = widget;
            }
        }
        if (mouseOverWidget != null) {
            graphics.setTooltipForNextFrame(this.font, this.font.split(Component.translatable("lotr.gui.map.widget." + mouseOverWidget.name), 200),
                    mouseX, mouseY);
        }
    }

    private void renderOverlay(GuiGraphicsExtractor graphics, int i, int j, float partialTick) {
        graphics.nextStratum();
        int overlayBorder = fullscreen ? 40 : 10;
        int rectX0 = this.mapXMin + overlayBorder;
        int rectY0 = this.mapYMin + overlayBorder;
        int rectX1 = this.mapXMax - overlayBorder;
        int rectY1 = this.mapYMax - overlayBorder;
        drawFancyRect(graphics, rectX0, rectY0, rectX1, rectY1);
        if (this.overlayLines == null) {
            return;
        }
        int stringX = this.mapXMin + this.mapWidth / 2;
        int stringY = rectY0 + 3 + this.font.lineHeight;
        int stringWidth = (int) ((this.mapWidth - overlayBorder * 2) * 0.75f);
        for (Component s : this.overlayLines) {
            for (FormattedCharSequence line : this.font.split(s, stringWidth)) {
                graphics.centeredText(this.font, line, stringX, stringY, 0xFFFFFFFF);
                stringY += this.font.lineHeight;
            }
        }
        stringY += this.font.lineHeight;
        if (this.sharingWaypoint && this.displayedWPShareList != null) {
            this.fellowshipDrawGUI.clearMouseOverFellowship();
            this.mouseOverRemoveSharedFellowship = null;
            int iconWidth = 8;
            int iconGap = 8;
            int fullWidth = this.fellowshipDrawGUI.getXSize() + iconGap + iconWidth;
            int fsX = this.mapXMin + this.mapWidth / 2 - fullWidth / 2;
            int fsY = stringY;
            this.scrollPaneWPShares.paneX0 = fsX;
            this.scrollPaneWPShares.scrollBarX0 = fsX + fullWidth + 2 + 2;
            this.scrollPaneWPShares.paneY0 = fsY;
            this.scrollPaneWPShares.paneY1 = fsY + (this.font.lineHeight + 5) * this.displayedWPShares;
            this.scrollPaneWPShares.mouseDragScroll(i, j, isLeftButtonDown());
            int[] sharesMinMax = this.scrollPaneWPShares.getMinMaxIndices(this.displayedWPShareList, this.displayedWPShares);
            for (int index = sharesMinMax[0]; index <= sharesMinMax[1]; ++index) {
                View fs = LOTRClientFellowships.getByID(this.displayedWPShareList.get(index));
                if (fs == null) {
                    continue;
                }
                this.fellowshipDrawGUI.drawFellowshipEntry(graphics, fs, fsX, fsY, i, j, false, fullWidth);
                int iconRemoveX = fsX + this.fellowshipDrawGUI.getXSize() + iconGap;
                int iconRemoveY = fsY;
                boolean mouseOverRemove = false;
                if (fs == this.fellowshipDrawGUI.getMouseOverFellowship()) {
                    mouseOverRemove = i >= iconRemoveX && i <= iconRemoveX + iconWidth && j >= iconRemoveY && j <= iconRemoveY + iconWidth;
                    if (mouseOverRemove) {
                        this.mouseOverRemoveSharedFellowship = fs;
                    }
                }
                graphics.blit(RenderPipelines.GUI_TEXTURED, LOTRFellowshipsScreen.ICONS, iconRemoveX, iconRemoveY, 8.0f,
                        16 + (mouseOverRemove ? 0 : iconWidth), iconWidth, iconWidth, 256, 256);
                fsY = stringY += this.font.lineHeight + 5;
            }
            if (this.scrollPaneWPShares.hasScrollBar) {
                this.scrollPaneWPShares.drawScrollBar(graphics);
            }
        }
        boolean typing = this.creatingWaypointNew || this.renamingWaypoint || this.sharingWaypointNew;
        this.nameWPTextField.setVisible(typing);
        if (typing) {
            this.nameWPTextField.setX((rectX0 + rectX1) / 2 - this.nameWPTextField.getWidth() / 2);
            this.nameWPTextField.setY(stringY);
            this.nameWPTextField.extractRenderState(graphics, i, j, partialTick);
            if (this.sharingWaypointNew && this.selectedWaypoint instanceof LOTRCustomWaypoint cwp
                    && isFellowshipAlreadyShared(this.nameWPTextField.getValue(), cwp)) {
                Component alreadyShared = Component.translatable("lotr.gui.map.customWaypoint.shareNew.already");
                int sx = this.nameWPTextField.getX() + this.nameWPTextField.getWidth() + 6;
                int sy = this.nameWPTextField.getY() + this.nameWPTextField.getHeight() / 2 - this.font.lineHeight / 2;
                graphics.text(this.font, alreadyShared, sx, sy, 0xFFFF0000, false);
            }
            stringY += this.nameWPTextField.getHeight() + this.font.lineHeight;
        }
        stringY += this.font.lineHeight;
        if (this.buttonOverlayFunction.visible) {
            this.buttonOverlayFunction.active = true;
            if (this.creatingWaypointNew || this.renamingWaypoint) {
                this.buttonOverlayFunction.active = isValidWaypointName(this.nameWPTextField.getValue());
            }
            if (this.sharingWaypointNew) {
                this.buttonOverlayFunction.active = this.selectedWaypoint instanceof LOTRCustomWaypoint cwp
                        && isExistingUnsharedFellowshipName(this.nameWPTextField.getValue(), cwp);
            }
            this.buttonOverlayFunction.setX(stringX - this.buttonOverlayFunction.getWidth() / 2);
            this.buttonOverlayFunction.setY(stringY);
            if (this.sharingWaypoint) {
                this.buttonOverlayFunction.setY(rectY1 - 3 - this.font.lineHeight - this.buttonOverlayFunction.getHeight());
            }
            this.buttonOverlayFunction.extractRenderState(graphics, i, j, partialTick);
        }
    }

    // ---------------------------------------------------------------- overlays

    private void overlayFunction() {
        if (this.creatingWaypoint) {
            openOverlayCreateNew();
        } else if (this.creatingWaypointNew && isValidWaypointName(this.nameWPTextField.getValue())) {
            ClientPlayNetworking.send(new LOTRCustomWaypoints.CreatePayload(this.nameWPTextField.getValue()));
            closeOverlay();
        } else if (this.deletingWaypoint && this.selectedWaypoint instanceof LOTRCustomWaypoint cwp) {
            ClientPlayNetworking.send(new LOTRCustomWaypoints.DeletePayload(cwp.getID()));
            closeOverlay();
            this.selectedWaypoint = null;
        } else if (this.renamingWaypoint && this.selectedWaypoint instanceof LOTRCustomWaypoint cwp
                && isValidWaypointName(this.nameWPTextField.getValue())) {
            ClientPlayNetworking.send(new LOTRCustomWaypoints.RenamePayload(cwp.getID(), this.nameWPTextField.getValue()));
            closeOverlay();
        } else if (this.sharingWaypoint) {
            openOverlayShareNew();
        } else if (this.sharingWaypointNew && this.selectedWaypoint instanceof LOTRCustomWaypoint cwp
                && isExistingUnsharedFellowshipName(this.nameWPTextField.getValue(), cwp)) {
            ClientPlayNetworking.send(new LOTRCustomWaypoints.SharePayload(cwp.getID(), this.nameWPTextField.getValue(), true));
            openOverlayShare();
        }
    }

    private void closeOverlay() {
        this.hasOverlay = false;
        this.overlayLines = null;
        this.creatingWaypoint = false;
        this.creatingWaypointNew = false;
        this.deletingWaypoint = false;
        this.renamingWaypoint = false;
        this.sharingWaypoint = false;
        this.sharingWaypointNew = false;
        this.buttonOverlayFunction.visible = false;
        this.buttonOverlayFunction.active = false;
        this.nameWPTextField.setValue("");
        this.nameWPTextField.setVisible(false);
        setFocused(null);
    }

    private void openOverlay(Component[] lines, @Nullable String buttonKey) {
        this.hasOverlay = true;
        this.overlayLines = lines;
        if (buttonKey != null) {
            this.buttonOverlayFunction.visible = true;
            this.buttonOverlayFunction.setMessage(Component.translatable(buttonKey));
        }
    }

    private void focusTextField() {
        this.nameWPTextField.setVisible(true);
        setFocused(this.nameWPTextField);
    }

    private void openOverlayCreate() {
        closeOverlay();
        this.creatingWaypoint = true;
        int numWaypoints = LOTRClientCustomWaypoints.getCustomWaypoints().size();
        int maxWaypoints = 5 + LOTRPlayerAchievements.countEarned(this.minecraft.player) / 5;
        int remaining = maxWaypoints - numWaypoints;
        if (remaining <= 0) {
            openOverlay(new Component[]{Component.translatable("lotr.gui.map.customWaypoint.allUsed.1", maxWaypoints), Component.empty(),
                    Component.translatable("lotr.gui.map.customWaypoint.allUsed.2")}, null);
        } else {
            openOverlay(new Component[]{Component.translatable("lotr.gui.map.customWaypoint.create.1", numWaypoints, maxWaypoints), Component.empty(),
                    Component.translatable("lotr.gui.map.customWaypoint.create.2")}, "lotr.gui.map.customWaypoint.create.button");
        }
    }

    private void openOverlayCreateNew() {
        closeOverlay();
        this.creatingWaypointNew = true;
        openOverlay(new Component[]{Component.translatable("lotr.gui.map.customWaypoint.createNew.1")}, "lotr.gui.map.customWaypoint.createNew.button");
        focusTextField();
    }

    private void openOverlayDelete() {
        closeOverlay();
        this.deletingWaypoint = true;
        openOverlay(new Component[]{Component.translatable("lotr.gui.map.customWaypoint.delete.1", this.selectedWaypoint.getDisplayName())},
                "lotr.gui.map.customWaypoint.delete.button");
    }

    private void openOverlayRename() {
        closeOverlay();
        this.renamingWaypoint = true;
        openOverlay(new Component[]{Component.translatable("lotr.gui.map.customWaypoint.rename.1", this.selectedWaypoint.getDisplayName())},
                "lotr.gui.map.customWaypoint.rename.button");
        focusTextField();
    }

    private void openOverlayShare() {
        closeOverlay();
        this.sharingWaypoint = true;
        openOverlay(new Component[]{Component.translatable("lotr.gui.map.customWaypoint.share.1", this.selectedWaypoint.getDisplayName())},
                "lotr.gui.map.customWaypoint.share.button");
    }

    private void openOverlayShareNew() {
        closeOverlay();
        this.sharingWaypointNew = true;
        openOverlay(new Component[]{Component.translatable("lotr.gui.map.customWaypoint.shareNew.1", this.selectedWaypoint.getDisplayName())},
                "lotr.gui.map.customWaypoint.shareNew.button");
        focusTextField();
    }

    /** setCWPProtectionMessage: the server refused a waypoint where banner protection forbids building. */
    private void setCWPProtectionMessage(Component message) {
        closeOverlay();
        openOverlay(new Component[]{Component.translatable("lotr.gui.map.customWaypoint.protected.1", message), Component.empty(),
                Component.translatable("lotr.gui.map.customWaypoint.protected.2")}, null);
    }

    // ---------------------------------------------------------------- input

    @Override
    public void tick() {
        super.tick();
        ++this.tickCounter;
        if (this.zoomTicks > 0) {
            --this.zoomTicks;
        }
        handleMapKeyboardMovement();
        Component protection = LOTRClientCustomWaypoints.takeProtectionMessage();
        if (protection != null) {
            setCWPProtectionMessage(protection);
        }
        if (this.isConquestGrid) {
            updateCurrentDimensionAndFaction();
            if (this.ticksUntilRequestFac > 0) {
                --this.ticksUntilRequestFac;
            }
        }
    }

    // ---------------------------------------------------------------- the conquest map

    /** receiveConquestGrid: a faction's grid, from the server. */
    public void receiveConquestGrid(LOTRFaction conqFac, List<LOTRConquestZone> allZones) {
        if (this.isConquestGrid) {
            this.receivedFacGrids.add(conqFac);
            this.facConquestGrids.put(conqFac, allZones);
        }
    }

    private void requestConquestGridIfNeed(LOTRFaction conqFac) {
        if (!this.requestedFacGrids.contains(conqFac) && this.ticksUntilRequestFac <= 0) {
            this.requestedFacGrids.add(conqFac);
            ClientPlayNetworking.send(new LOTRConquestGrid.GridRequestPayload(conqFac));
        }
    }

    private boolean hasConquestScrollBar() {
        return this.isConquestGrid && currentFactionList.size() > 1;
    }

    private void setCurrentScrollFromFaction() {
        this.currentFacScroll = (float) this.currentFactionIndex / (currentFactionList.size() - 1);
    }

    private void nextConquestRegion() {
        List<LOTRDimension.DimensionRegion> regionList = LOTRDimension.MIDDLE_EARTH.dimensionRegions;
        if (!regionList.isEmpty()) {
            currentRegion = regionList.get(Math.floorMod(regionList.indexOf(currentRegion) + 1, regionList.size()));
            updateCurrentDimensionAndFaction();
            setCurrentScrollFromFaction();
        }
    }

    /** setupConquestScrollBar: dragging the faction scroll bar picks the faction under it. */
    private void setupConquestScrollBar(int i, int j) {
        boolean isMouseDown = isLeftButtonDown();
        int i1 = this.facScrollX;
        int j1 = this.facScrollY;
        int i2 = i1 + FAC_SCROLL_WIDTH;
        int j2 = j1 + FAC_SCROLL_HEIGHT;
        this.mouseInFacScroll = i >= i1 && j >= j1 && i < i2 && j < j2;
        if (!this.wasMouseDown && isMouseDown && this.mouseInFacScroll) {
            this.isFacScrolling = true;
        }
        if (!isMouseDown) {
            this.isFacScrolling = false;
        }
        this.wasMouseDown = isMouseDown;
        if (this.isFacScrolling) {
            this.currentFacScroll = Mth.clamp((i - i1 - FAC_SCROLL_WIDGET_WIDTH / 2.0f) / ((float) (i2 - i1) - FAC_SCROLL_WIDGET_WIDTH), 0.0f, 1.0f);
            this.currentFactionIndex = Math.round(this.currentFacScroll * (currentFactionList.size() - 1));
        }
    }

    private void updateCurrentDimensionAndFaction() {
        if (this.currentFactionIndex != this.prevFactionIndex) {
            this.conquestViewingFaction = currentFactionList.get(this.currentFactionIndex);
            this.ticksUntilRequestFac = 40;
        }
        this.prevFactionIndex = this.currentFactionIndex;
        if (currentRegion != prevRegion) {
            lastViewedRegions.put(prevRegion, this.conquestViewingFaction);
            currentFactionList = currentRegion.factionList;
            this.conquestViewingFaction = lastViewedRegions.containsKey(currentRegion) ? lastViewedRegions.get(currentRegion)
                    : currentRegion.factionList.get(0);
            this.prevFactionIndex = this.currentFactionIndex = currentFactionList.indexOf(this.conquestViewingFaction);
            this.ticksUntilRequestFac = 40;
        }
        prevRegion = currentRegion;
    }

    /**
     * The faction's zones in view, each red by its strength against the strongest in view (striped
     * where it only aids allies, grey where it does nothing); the zone under the mouse ringed, with
     * its strength.
     */
    private void renderConquestZones(GuiGraphicsExtractor graphics, int i, int j) {
        LOTRFaction faction = this.conquestViewingFaction;
        List<LOTRConquestZone> allZones = this.facConquestGrids.getOrDefault(faction, List.of());
        List<LOTRConquestZone> zonesInView = new ArrayList<>();
        this.highestViewedConqStr = 0.0f;
        float mouseOverStr = 0.0f;
        LOTRConquestZone mouseOverZone = null;
        LOTRConquestGrid.ConquestEffective mouseOverEffect = null;
        graphics.enableScissor(this.mapXMin, this.mapYMin, this.mapXMax, this.mapYMax);
        LOTRMapQuadsRenderState quads = new LOTRMapQuadsRenderState(new ScreenRectangle(this.mapXMin, this.mapYMin, this.mapWidth, this.mapHeight),
                graphics.scissorStack.peek());
        for (int pass = 0; pass <= 1; ++pass) {
            if (pass == 1 && this.highestViewedConqStr <= 0.0f) {
                continue;
            }
            for (LOTRConquestZone zone : pass == 0 ? allZones : zonesInView) {
                float strength = zone.getConquestStrength(faction, this.minecraft.level);
                int[] min = LOTRConquestGrid.getMinCoordsOnMap(zone);
                int[] max = LOTRConquestGrid.getMaxCoordsOnMap(zone);
                float[] minF = this.map.transformMapCoords(min[0], min[1]);
                float[] maxF = this.map.transformMapCoords(max[0], max[1]);
                float minX = minF[0];
                float maxX = maxF[0];
                float minY = minF[1];
                float maxY = maxF[1];
                if (maxX < this.mapXMin || minX > this.mapXMax || maxY < this.mapYMin || minY > this.mapYMax) {
                    continue;
                }
                if (pass == 0) {
                    this.highestViewedConqStr = Math.max(this.highestViewedConqStr, strength);
                    zonesInView.add(zone);
                    continue;
                }
                if (strength <= 0.0f) {
                    continue;
                }
                float strAlpha = Math.max(Mth.clamp(strength / this.highestViewedConqStr, 0.0f, 1.0f), 0.1f);
                LOTRConquestGrid.ConquestEffective effect = LOTRConquestGrid.getConquestEffectIn(this.minecraft.level, zone, faction);
                int zoneColor = 0xBB0000 | Math.round(strAlpha * 255.0f) << 24;
                int zoneColor2 = 0x1E1E1E | Math.round(strAlpha * 255.0f) << 24;
                if (effect == LOTRConquestGrid.ConquestEffective.EFFECTIVE) {
                    quads.add(minX, minY, maxX, maxY, zoneColor);
                } else if (effect == LOTRConquestGrid.ConquestEffective.ALLY_BOOST) {
                    int strips = 8;
                    float stripYSize = (maxY - minY) / strips;
                    for (int l = 0; l < strips; ++l) {
                        quads.add(minX, minY + stripYSize * l, maxX, minY + stripYSize * (l + 1), l % 2 == 0 ? zoneColor : zoneColor2);
                    }
                } else {
                    quads.add(minX, minY, maxX, maxY, zoneColor2);
                }
                if (i >= minX && i < maxX && j >= minY && j < maxY) {
                    mouseOverStr = strength;
                    mouseOverZone = zone;
                    mouseOverEffect = effect;
                }
            }
        }
        if (mouseOverZone != null && this.isMouseWithinMap) {
            int[] min = LOTRConquestGrid.getMinCoordsOnMap(mouseOverZone);
            int[] max = LOTRConquestGrid.getMaxCoordsOnMap(mouseOverZone);
            float[] minF = this.map.transformMapCoords(min[0], min[1]);
            float[] maxF = this.map.transformMapCoords(max[0], max[1]);
            float minX = minF[0];
            float maxX = maxF[0];
            float minY = minF[1];
            float maxY = maxF[1];
            quads.add(minX - 1.0f, minY - 1.0f, maxX + 1.0f, minY, -16777216);
            quads.add(minX - 1.0f, maxY, maxX + 1.0f, maxY + 1.0f, -16777216);
            quads.add(minX - 1.0f, minY, minX, maxY, -16777216);
            quads.add(maxX, minY, maxX + 1.0f, maxY, -16777216);
            quads.add(minX - 2.0f, minY - 2.0f, maxX + 2.0f, minY - 1.0f, -4521984);
            quads.add(minX - 2.0f, maxY + 1.0f, maxX + 2.0f, maxY + 2.0f, -4521984);
            quads.add(minX - 2.0f, minY - 1.0f, minX - 1.0f, maxY + 1.0f, -4521984);
            quads.add(maxX + 1.0f, minY - 1.0f, maxX + 2.0f, maxY + 1.0f, -4521984);
            Component tooltip = Component.literal(LOTRAlignmentValues.formatConqForDisplay(mouseOverStr, false));
            Component subtip = null;
            if (mouseOverEffect == LOTRConquestGrid.ConquestEffective.ALLY_BOOST) {
                subtip = Component.translatable("lotr.gui.map.conquest.allyBoost", faction.factionName());
            } else if (mouseOverEffect == LOTRConquestGrid.ConquestEffective.NO_EFFECT) {
                subtip = Component.translatable("lotr.gui.map.conquest.noEffect");
            }
            Component finalSubtip = subtip;
            this.raisedTooltips.add(() -> renderConquestTooltip(graphics, i, j, tooltip, finalSubtip));
        }
        graphics.guiRenderState.addGuiElement(quads);
        graphics.disableScissor();
    }

    /** The zone's strength by the conquest icon, and below it, smaller, what the strength does there. */
    private void renderConquestTooltip(GuiGraphicsExtractor graphics, int i, int j, Component tooltip, @Nullable Component subtip) {
        int strWidth = this.font.width(tooltip);
        int subWidth = subtip == null ? 0 : this.font.width(subtip);
        int strHeight = this.font.lineHeight;
        float guiScale = this.minecraft.getWindow().getGuiScale();
        float subScale = guiScale <= 2.0f ? guiScale : guiScale - 1.0f;
        float subScaleRel = subScale / guiScale;
        int border = 3;
        int iconSize = 16;
        int rectWidth = border * 2 + Math.max(strWidth + iconSize + border, (int) (subWidth * subScaleRel));
        int rectHeight = Math.max(strHeight, iconSize) + border * 2;
        if (subtip != null) {
            rectHeight += border + (int) (strHeight * subScaleRel);
        }
        int mapBorder2 = 2;
        int rectX = Math.min(Math.max(i + 12, this.mapXMin + mapBorder2), this.mapXMax - mapBorder2 - rectWidth);
        int rectY = Math.min(Math.max(j - 12, this.mapYMin + mapBorder2), this.mapYMax - mapBorder2 - rectHeight);
        drawFancyRect(graphics, rectX, rectY, rectX + rectWidth, rectY + rectHeight);
        graphics.blit(RenderPipelines.GUI_TEXTURED, ALIGNMENT_TEXTURE, rectX + border, rectY + border, 0.0f, 228.0f, iconSize, iconSize, 256, 256);
        graphics.text(this.font, tooltip, rectX + iconSize + border * 2, rectY + border + (iconSize - strHeight) / 2, 0xFFFFFFFF, false);
        if (subtip != null) {
            graphics.pose().pushMatrix();
            graphics.pose().scale(subScaleRel, subScaleRel);
            int subX = rectX + border;
            int subY = rectY + border * 2 + iconSize;
            graphics.text(this.font, subtip, Math.round(subX / subScaleRel), Math.round(subY / subScaleRel), 0xFFFFFFFF, false);
            graphics.pose().popMatrix();
        }
    }

    /** While the grid is coming (or cannot be seen): the map veiled, and why. */
    private void renderConquestLoading(GuiGraphicsExtractor graphics) {
        graphics.fill(this.mapXMin, this.mapYMin, this.mapXMax, this.mapYMax, -1429949539);
        graphics.blit(RenderPipelines.GUI_TEXTURED, LOTRMapRenderer.MAP_OVERLAY, this.mapXMin, this.mapYMin, 0.0f, 0.0f, this.mapWidth, this.mapHeight,
                256, 256, 256, 256, ARGB.colorFromFloat(0.2f, 1.0f, 1.0f, 1.0f));
        LocalPlayer player = this.minecraft.player;
        LOTRConquestGrid.ConquestViewableQuery query = LOTRConquestGrid.canPlayerViewConquest(player, this.conquestViewingFaction);
        Component loadText;
        if (query.result() == LOTRConquestGrid.ConquestViewable.CAN_VIEW) {
            loadText = Component.translatable("lotr.gui.map.conquest.wait").append(".".repeat(1 + this.tickCounter / 10 % 3));
        } else if (query.result() == LOTRConquestGrid.ConquestViewable.UNPLEDGED) {
            loadText = Component.translatable("lotr.gui.map.conquest.noPledge");
        } else {
            LOTRFaction pledgeFac = LOTRPlayerAlignments.get(player).pledgeFaction();
            LOTRFactionRank needRank = query.needRank();
            String needAlign = LOTRAlignmentValues.formatAlignForDisplay(needRank.alignment);
            loadText = Component.translatable("lotr.gui.map.conquest.needRank", pledgeFac.factionName(),
                    needRank.getFullNameWithGender(LOTRRankOptions.useFeminineRanks(player)), needAlign);
        }
        // The lang's line breaks are real ones here, which Font.split keeps.
        List<FormattedCharSequence> loadLines = this.font.split(loadText, 250);
        int stringX = this.mapXMin + this.mapWidth / 2;
        int stringY = (this.mapYMin + this.mapYMax) / 2 - loadLines.size() * this.font.lineHeight / 2;
        for (FormattedCharSequence line : loadLines) {
            graphics.centeredText(this.font, line, stringX, stringY, 0xFF000000 | 3748142);
            stringY += this.font.lineHeight;
        }
    }

    /** The title, the strength key in the corner and the faction scroll bar. */
    private void renderConquestHeader(GuiGraphicsExtractor graphics) {
        Component s = Component.translatable("lotr.gui.map.conquest.title", this.conquestViewingFaction.factionName());
        int x = this.mapXMin + this.mapWidth / 2;
        LOTRAlignmentBarRenderer.drawAlignmentText(graphics, this.font, x - this.font.width(s) / 2, this.mapYMin - 14, s, 1.0f);
        if (!this.loadingConquestGrid) {
            int keyBorder = 8;
            int keyWidth = 24;
            int keyHeight = 12;
            int iconSize = 16;
            int iconGap = keyBorder / 2;
            float guiScale = this.minecraft.getWindow().getGuiScale();
            float labelScale = guiScale <= 2.0f ? guiScale : guiScale - 1.0f;
            float labelScaleRel = labelScale / guiScale;
            graphics.blit(RenderPipelines.GUI_TEXTURED, ALIGNMENT_TEXTURE, this.mapXMax - keyBorder - iconSize, this.mapYMax - keyBorder - iconSize,
                    0.0f, 228.0f, iconSize, iconSize, 256, 256);
            for (int pass = 0; pass <= 1; ++pass) {
                for (int l = 0; l <= 10; ++l) {
                    float frac = (10 - l) / 10.0f;
                    int x1 = this.mapXMax - keyBorder - iconSize - iconGap - l * keyWidth;
                    int x0 = x1 - keyWidth;
                    int y1 = this.mapYMax - keyBorder - (iconSize - keyHeight) / 2;
                    int y0 = y1 - keyHeight;
                    if (pass == 0) {
                        graphics.fill(x0, y0, x1, y1, 0xBB0000 | Math.round(frac * 255.0f) << 24);
                        continue;
                    }
                    if (l % 2 != 0) {
                        continue;
                    }
                    Component keyLabel = Component.literal(LOTRAlignmentValues.formatConqForDisplay(frac * this.highestViewedConqStr, false));
                    int strX = (int) ((x0 + keyWidth / 2) / labelScaleRel);
                    int strY = (int) ((y0 + keyHeight / 2) / labelScaleRel) - this.font.lineHeight / 2;
                    graphics.pose().pushMatrix();
                    graphics.pose().scale(labelScaleRel, labelScaleRel);
                    graphics.centeredText(this.font, keyLabel, strX, strY, 0xFFFFFFFF);
                    graphics.pose().popMatrix();
                }
            }
        }
        if (hasConquestScrollBar()) {
            int x0 = this.facScrollX;
            int y0 = this.facScrollY;
            graphics.blit(RenderPipelines.GUI_TEXTURED, LOTRFactionsScreen.FACTIONS_TEXTURE, x0, y0, 0.0f, 128.0f, FAC_SCROLL_WIDTH, FAC_SCROLL_HEIGHT, 256, 256);
            int factions = currentFactionList.size();
            int inner = FAC_SCROLL_WIDTH - FAC_SCROLL_BORDER * 2;
            for (int index = 0; index < factions; ++index) {
                float[] rgb = currentFactionList.get(index).getFactionRGB();
                float shade = 0.6f;
                int colour = ARGB.colorFromFloat(1.0f, rgb[0] * shade, rgb[1] * shade, rgb[2] * shade);
                int xMin = x0 + FAC_SCROLL_BORDER + Math.round((float) index / factions * inner);
                int xMax = x0 + FAC_SCROLL_BORDER + Math.round((float) (index + 1) / factions * inner);
                graphics.blit(RenderPipelines.GUI_TEXTURED, LOTRFactionsScreen.FACTIONS_TEXTURE, xMin, y0 + FAC_SCROLL_BORDER,
                        FAC_SCROLL_BORDER, 128 + FAC_SCROLL_BORDER, xMax - xMin, FAC_SCROLL_HEIGHT - FAC_SCROLL_BORDER * 2,
                        inner, FAC_SCROLL_HEIGHT - FAC_SCROLL_BORDER * 2, 256, 256, colour);
            }
            int scroll = (int) (this.currentFacScroll * (inner - FAC_SCROLL_WIDGET_WIDTH));
            graphics.blit(RenderPipelines.GUI_TEXTURED, LOTRFactionsScreen.FACTIONS_TEXTURE, x0 + FAC_SCROLL_BORDER + scroll, y0 + FAC_SCROLL_BORDER,
                    0.0f, 142.0f, FAC_SCROLL_WIDGET_WIDTH, FAC_SCROLL_WIDGET_HEIGHT, 256, 256);
        }
    }

    private void handleMapKeyboardMovement() {
        this.prevPosX += this.posXMove;
        this.prevPosY += this.posYMove;
        this.posXMove = 0.0f;
        this.posYMove = 0.0f;
        if (this.hasOverlay) {
            return;
        }
        float move = 12.0f / (float) Math.pow(this.zoomScale, 0.8f);
        if (isKeyDownAndNotMouse(this.minecraft.options.keyLeft) || isKeyDown(GLFW.GLFW_KEY_LEFT)) {
            this.posXMove -= move;
        }
        if (isKeyDownAndNotMouse(this.minecraft.options.keyRight) || isKeyDown(GLFW.GLFW_KEY_RIGHT)) {
            this.posXMove += move;
        }
        if (isKeyDownAndNotMouse(this.minecraft.options.keyUp) || isKeyDown(GLFW.GLFW_KEY_UP)) {
            this.posYMove -= move;
        }
        if (isKeyDownAndNotMouse(this.minecraft.options.keyDown) || isKeyDown(GLFW.GLFW_KEY_DOWN)) {
            this.posYMove += move;
        }
        if (this.posXMove != 0.0f || this.posYMove != 0.0f) {
            this.selectedWaypoint = null;
        }
    }

    private boolean isKeyDown(int key) {
        return InputConstants.isKeyDown(this.minecraft.getWindow(), key);
    }

    private boolean isKeyDownAndNotMouse(KeyMapping keyMapping) {
        InputConstants.Key key = KeyMappingHelper.getBoundKeyOf(keyMapping);
        return key.getType() == InputConstants.Type.KEYSYM && key.getValue() >= 0 && isKeyDown(key.getValue());
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        boolean back = event.isEscape() || this.minecraft.options.keyInventory.matches(event);
        if (this.hasOverlay) {
            boolean typing = this.creatingWaypointNew || this.renamingWaypoint || this.sharingWaypointNew;
            if (typing && !event.isEscape() && (this.nameWPTextField.keyPressed(event) || this.nameWPTextField.canConsumeInput())) {
                return true;
            }
            if (back) {
                if (this.creatingWaypointNew) {
                    openOverlayCreate();
                } else if (this.sharingWaypointNew) {
                    openOverlayShare();
                } else {
                    closeOverlay();
                }
            }
            return true;
        }
        LocalPlayer player = this.minecraft.player;
        if (!this.loadingConquestGrid && LOTRKeyBindings.fastTravel.matches(event) && isMiddleEarth() && this.selectedWaypoint != null
                && this.selectedWaypoint.hasPlayerUnlocked(player)
                && LOTRClientFastTravel.getTimeSinceFT() >= LOTRClientFastTravel.getWaypointFTTime(this.selectedWaypoint, player)) {
            boolean custom = this.selectedWaypoint instanceof LOTRCustomWaypoint;
            Optional<UUID> sharer = this.selectedWaypoint instanceof LOTRCustomWaypoint cwp ? Optional.ofNullable(cwp.sharingPlayer) : Optional.empty();
            ClientPlayNetworking.send(new LOTRFastTravel.FastTravelPayload(custom, this.selectedWaypoint.getID(), sharer));
            onClose();
            return true;
        }
        if (!this.loadingConquestGrid && this.selectedWaypoint == null && LOTRKeyBindings.mapTeleport.matches(event) && this.isMouseWithinMap
                && canTeleport()) {
            ClientPlayNetworking.send(new LOTRMapPlayerData.TeleportPayload(this.mouseXCoord, this.mouseZCoord));
            onClose();
            return true;
        }
        if (this.hasControlZones && back) {
            this.minecraft.setScreenAndShow(new LOTRFactionsScreen());
            return true;
        }
        return super.keyPressed(event);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        int k = (int) Math.signum(scrollY);
        if (this.isConquestGrid && hasConquestScrollBar() && this.mouseInFacScroll && k != 0) {
            if (k < 0) {
                this.currentFactionIndex = Math.min(this.currentFactionIndex + 1, Math.max(0, currentFactionList.size() - 1));
            } else {
                this.currentFactionIndex = Math.max(this.currentFactionIndex - 1, 0);
            }
            setCurrentScrollFromFaction();
            return true;
        }
        if (!this.hasOverlay && this.zoomTicks == 0) {
            if (k < 0 && zoomPower > -3) {
                zoom(-1);
                return true;
            }
            if (k > 0 && zoomPower < 4) {
                zoom(1);
                return true;
            }
        }
        if (this.hasOverlay && k != 0 && this.scrollPaneWPShares.hasScrollBar && this.scrollPaneWPShares.mouseOver
                && this.displayedWPShareList != null) {
            this.scrollPaneWPShares.mouseWheelScroll(k, this.displayedWPShareList.size() - this.displayedWPShares);
        }
        return true;
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        int i = (int) event.x();
        int j = (int) event.y();
        int k = event.button();
        Widget mouseWidget = null;
        for (Widget widget : this.mapWidgets) {
            if (widget.isMouseOver(i - this.mapXMin, j - this.mapYMin, this.mapWidth, this.mapHeight)) {
                mouseWidget = widget;
                break;
            }
        }
        if (this.hasOverlay && k == 0 && this.sharingWaypoint && this.mouseOverRemoveSharedFellowship != null
                && this.selectedWaypoint instanceof LOTRCustomWaypoint cwp) {
            ClientPlayNetworking.send(new LOTRCustomWaypoints.SharePayload(cwp.getID(), this.mouseOverRemoveSharedFellowship.name(), false));
            return true;
        }
        if (!this.hasOverlay && k == 0 && isMiddleEarth() && this.selectedWaypoint instanceof LOTRCustomWaypoint cwp) {
            if (cwp.isShared()) {
                if (mouseWidget == this.widgetHideSWP) {
                    ClientPlayNetworking.send(new LOTRCustomWaypoints.SharedHidePayload(cwp.sharingPlayer, cwp.getID(), true));
                    this.selectedWaypoint = null;
                    return true;
                }
                if (mouseWidget == this.widgetUnhideSWP) {
                    ClientPlayNetworking.send(new LOTRCustomWaypoints.SharedHidePayload(cwp.sharingPlayer, cwp.getID(), false));
                    return true;
                }
            } else {
                if (mouseWidget == this.widgetDelCWP) {
                    openOverlayDelete();
                    return true;
                }
                if (mouseWidget == this.widgetRenameCWP) {
                    openOverlayRename();
                    return true;
                }
                if (mouseWidget == this.widgetShareCWP) {
                    openOverlayShare();
                    return true;
                }
            }
        }
        if (!this.hasOverlay && k == 0 && isMiddleEarth() && mouseWidget == this.widgetAddCWP && canCreateWaypointAtPosition()) {
            openOverlayCreate();
            return true;
        }
        if (!this.hasOverlay && k == 0) {
            if (mouseWidget == this.widgetToggleWPs) {
                LOTRClientMap.showWP = !LOTRClientMap.showWP;
                sendClientInfoPacket();
                return true;
            }
            if (mouseWidget == this.widgetToggleCWPs) {
                LOTRClientMap.showCWP = !LOTRClientMap.showCWP;
                sendClientInfoPacket();
                return true;
            }
            if (mouseWidget == this.widgetToggleHiddenSWPs) {
                LOTRClientMap.showHiddenSWP = !LOTRClientMap.showHiddenSWP;
                sendClientInfoPacket();
                return true;
            }
            if (this.zoomTicks == 0) {
                if (mouseWidget == this.widgetZoomIn && zoomPower < 4) {
                    zoom(1);
                    return true;
                }
                if (mouseWidget == this.widgetZoomOut && zoomPower > -3) {
                    zoom(-1);
                    return true;
                }
            }
            if (mouseWidget == this.widgetFullScreen) {
                fullscreen = !fullscreen;
                rebuildWidgets();
                return true;
            }
            if (mouseWidget == this.widgetSepia) {
                LOTRConfig.toggleSepia();
                return true;
            }
            if (mouseWidget == this.widgetLabels) {
                if (this.isConquestGrid) {
                    LOTRConfig.toggleMapLabelsConquest();
                } else {
                    LOTRConfig.toggleMapLabels();
                }
                return true;
            }
        }
        if (!this.hasOverlay && !this.loadingConquestGrid && k == 0 && this.isMouseWithinMap) {
            this.selectedWaypoint = null;
            double distanceSelectedWP = Double.MAX_VALUE;
            LocalPlayer player = this.minecraft.player;
            for (LOTRAbstractWaypoint waypoint : getAllAvailableWaypoints()) {
                boolean unlocked = waypoint.hasPlayerUnlocked(player);
                if (!isWaypointVisible(waypoint) || waypoint.isHidden() && !unlocked) {
                    continue;
                }
                float[] pos = this.map.transformCoords(waypoint.getXCoord(), waypoint.getZCoord());
                float dx = pos[0] - i;
                float dy = pos[1] - j;
                double distToWP = Math.sqrt(dx * dx + dy * dy);
                if (distToWP > 5.0 || distToWP > distanceSelectedWP) {
                    continue;
                }
                this.selectedWaypoint = waypoint;
                distanceSelectedWP = distToWP;
            }
        }
        return super.mouseClicked(event, doubleClick);
    }

    /** LOTRGuiMapWidget: a 10 by 10 button drawn on the map, placed from its left or (if negative) right, and top or bottom. */
    private static final class Widget {
        final int xPos;
        final int yPos;
        final int width;
        final String name;
        final int texUBase;
        final int texVBase;
        int texVIndex;
        boolean visible = true;

        Widget(int x, int y, int w, String s, int u, int v) {
            this.xPos = x;
            this.yPos = y;
            this.width = w;
            this.name = s;
            this.texUBase = u;
            this.texVBase = v;
        }

        int getMapXPos(int mapWidth) {
            return this.xPos < 0 ? mapWidth + this.xPos : this.xPos;
        }

        int getMapYPos(int mapHeight) {
            return this.yPos < 0 ? mapHeight + this.yPos : this.yPos;
        }

        int getTexV() {
            return this.texVBase + this.texVIndex * this.width;
        }

        boolean isMouseOver(int mouseX, int mouseY, int mapWidth, int mapHeight) {
            return this.visible && mouseX >= getMapXPos(mapWidth) && mouseX < getMapXPos(mapWidth) + this.width
                    && mouseY >= getMapYPos(mapHeight) && mouseY < getMapYPos(mapHeight) + this.width;
        }
    }
}
