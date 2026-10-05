package net.blueskiez77.lord_of_the_rings__middle_earth.client.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRMapCoords;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRRoads;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;

/**
 * LOTRGuiMainMenu (config "Custom main menu"): the title screen over a map of
 * Middle-earth that drifts from waypoint to waypoint along the Fellowship's
 * road and round the north again, slowly zooming in and out, with its roads
 * and waypoints, under two vignettes. The first menu of a session opens from
 * black, zooming in. The buttons sit lower, as red-book buttons, and the
 * mod's title stands under the logo.
 *
 * <p>The map is drawn here as the original's map renderer drew it for the
 * menu (LOTRGuiRendererMap, without labels, the sepia map or the player's
 * waypoints); the full map screen is D13.
 */
public class LOTRMainMenuScreen extends TitleScreen {

    private static final Identifier MAP = Identifier.fromNamespaceAndPath("lotr", "map/map.png");
    private static final Identifier MAP_OVERLAY = Identifier.fromNamespaceAndPath("lotr", "map/map_overlay.png");
    private static final Identifier MAP_ICONS = Identifier.fromNamespaceAndPath("lotr", "map/map_screen.png");
    private static final Identifier MENU_OVERLAY = Identifier.fromNamespaceAndPath("lotr", "gui/menu_overlay.png");
    private static final Identifier VIGNETTE = Identifier.withDefaultNamespace("textures/misc/vignette.png");
    private static final int MAP_IMAGE_WIDTH = 3200;
    private static final int MAP_IMAGE_HEIGHT = 4000;
    /** LOTRBiome.ocean's colour. */
    private static final int OCEAN_COLOUR = 0xFF000000 | 153997;

    private static final List<LOTRWaypoint> WAYPOINT_ROUTE = new ArrayList<>();
    private static final Random RAND = new Random();
    private static final boolean RANDOM_WP_START = false;
    private static boolean isFirstMenu = true;
    private static int tickCounter;
    private static int currentWPIndex;
    private static float mapSpeed;
    private static float mapVelX;
    private static float mapVelY;

    private final boolean fadeIn = isFirstMenu;
    private long firstRenderTime;
    private double prevMapX;
    private double mapX;
    private double prevMapY;
    private double mapY;
    private float zoomExp;
    private float zoomStable;

    public LOTRMainMenuScreen() {
        super(isFirstMenu);
        isFirstMenu = false;
        if (WAYPOINT_ROUTE.isEmpty()) {
            setupWaypoints();
            currentWPIndex = RANDOM_WP_START ? RAND.nextInt(WAYPOINT_ROUTE.size()) : 0;
        }
        LOTRWaypoint wp = WAYPOINT_ROUTE.get(currentWPIndex);
        this.prevMapX = this.mapX = wp.getX();
        this.prevMapY = this.mapY = wp.getY();
    }

    private static void setupWaypoints() {
        WAYPOINT_ROUTE.clear();
        WAYPOINT_ROUTE.addAll(List.of(LOTRWaypoint.HOBBITON, LOTRWaypoint.BRANDYWINE_BRIDGE, LOTRWaypoint.BUCKLEBURY,
                LOTRWaypoint.WITHYWINDLE_VALLEY, LOTRWaypoint.BREE, LOTRWaypoint.WEATHERTOP, LOTRWaypoint.RIVENDELL,
                LOTRWaypoint.WEST_GATE, LOTRWaypoint.DIMRILL_DALE, LOTRWaypoint.CERIN_AMROTH,
                LOTRWaypoint.CARAS_GALADHON, LOTRWaypoint.NORTH_UNDEEP, LOTRWaypoint.SOUTH_UNDEEP,
                LOTRWaypoint.ARGONATH, LOTRWaypoint.RAUROS, LOTRWaypoint.EDORAS, LOTRWaypoint.HELMS_DEEP,
                LOTRWaypoint.ISENGARD, LOTRWaypoint.DUNHARROW, LOTRWaypoint.ERECH, LOTRWaypoint.MINAS_TIRITH,
                LOTRWaypoint.MINAS_MORGUL, LOTRWaypoint.MOUNT_DOOM, LOTRWaypoint.MORANNON,
                LOTRWaypoint.EAST_RHOVANION_ROAD, LOTRWaypoint.OLD_RHOVANION, LOTRWaypoint.RUNNING_FORD,
                LOTRWaypoint.DALE_CITY, LOTRWaypoint.THRANDUIL_HALLS, LOTRWaypoint.ENCHANTED_RIVER,
                LOTRWaypoint.FOREST_GATE, LOTRWaypoint.BEORN, LOTRWaypoint.EAGLES_EYRIE, LOTRWaypoint.GOBLIN_TOWN,
                LOTRWaypoint.MOUNT_GRAM, LOTRWaypoint.FORNOST, LOTRWaypoint.ANNUMINAS, LOTRWaypoint.MITHLOND_NORTH,
                LOTRWaypoint.TOWER_HILLS));
    }

    /** initGui: the buttons moved down as far as 50, and the plain ones made red-book buttons. */
    @Override
    protected void init() {
        super.init();
        // No splash text: the original's menu had none.
        this.splash = null;
        List<AbstractWidget> buttons = new ArrayList<>();
        for (var child : children()) {
            if (child instanceof AbstractWidget widget && !(widget instanceof PlainTextButton)) {
                buttons.add(widget);
            }
        }
        int lowerButtonMaxY = 0;
        for (AbstractWidget button : buttons) {
            lowerButtonMaxY = Math.max(lowerButtonMaxY, button.getY() + button.getHeight());
        }
        int moveDown = Math.max(Math.min(50, this.height - 25 - lowerButtonMaxY), 0);
        for (AbstractWidget button : buttons) {
            button.setY(button.getY() + moveDown);
            if (button instanceof Button.Plain plain) {
                removeWidget(plain);
                LOTRRedBookButton redBook = new LOTRRedBookButton(plain.getX(), plain.getY(), plain.getWidth(),
                        plain.getHeight(), plain.getMessage(), () -> {
                }) {
                    @Override
                    public void onPress(InputWithModifiers input) {
                        plain.onPress(input);
                    }
                };
                redBook.active = plain.active;
                addRenderableWidget(redBook);
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        ++tickCounter;
        this.prevMapX = this.mapX;
        this.prevMapY = this.mapY;
        LOTRWaypoint wp = WAYPOINT_ROUTE.get(currentWPIndex);
        double dx = wp.getX() - this.mapX;
        double dy = wp.getY() - this.mapY;
        double dist = Math.sqrt(dx * dx + dy * dy);
        if (dist <= 12.0) {
            if (++currentWPIndex >= WAYPOINT_ROUTE.size()) {
                currentWPIndex = 0;
            }
        } else {
            mapSpeed = Math.min(mapSpeed + 0.01f, 0.8f);
            double vXNew = dx / dist * mapSpeed;
            double vYNew = dy / dist * mapSpeed;
            float a = 0.02f;
            mapVelX = (float) (mapVelX + (vXNew - mapVelX) * a);
            mapVelY = (float) (mapVelY + (vYNew - mapVelY) * a);
        }
        this.mapX += mapVelX;
        this.mapY += mapVelY;
    }

    private float fade() {
        if (this.firstRenderTime == 0L && this.fadeIn) {
            this.firstRenderTime = Util.getMillis();
        }
        return this.fadeIn ? (Util.getMillis() - this.firstRenderTime) / 1000.0f : 1.0f;
    }

    /**
     * In place of the panorama: the map, its vignettes, and the first menu's black fading away.
     * The float a screen is handed is the ticks since the last frame, not the way through this
     * tick that drawScreen's was, so the map is placed by the game's own partial tick.
     */
    @Override
    protected void extractPanorama(GuiGraphicsExtractor graphics, float frameTicks) {
        float partialTick = this.minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(false);
        float fade = fade();
        this.zoomExp = -0.1f + Mth.cos((tickCounter + partialTick) * 0.003f) * 0.8f;
        if (this.fadeIn) {
            this.zoomExp += Mth.clamp(1.0f - fade * 0.5f, 0.0f, 1.0f) * -1.5f;
        }
        this.zoomStable = (float) Math.pow(2.0, -0.1);
        renderMap(graphics, partialTick);
        for (int l = 0; l < 2; ++l) {
            graphics.blit(RenderPipelines.VIGNETTE, VIGNETTE, 0, 0, 0.0f, 0.0f, this.width, this.height,
                    this.width, this.height, -1);
        }
        float overlayAlpha = this.fadeIn ? Mth.clamp(1.0f - fade, 0.0f, 1.0f) : 0.0f;
        if (overlayAlpha > 0.0f) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, MENU_OVERLAY, 0, 0, 0.0f, 0.0f, this.width, this.height,
                    16, 128, 16, 128, ARGB.white(overlayAlpha));
        }
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        float fadeAlpha = this.fadeIn ? Mth.clamp(fade() - 1.0f, 0.0f, 1.0f) : 1.0f;
        if (fadeAlpha > 0.02f) {
            Component subtitle = Component.translatable("lotr.menu.title");
            graphics.text(this.font, subtitle, this.width / 2 - this.font.width(subtitle) / 2, 80, ARGB.white(fadeAlpha));
        }
    }

    /** LOTRGuiRendererMap.renderMap over the whole screen: the ocean, the map and its overlay, the roads, the waypoints. */
    private void renderMap(GuiGraphicsExtractor graphics, float partialTick) {
        graphics.fill(0, 0, this.width, this.height, OCEAN_COLOUR);
        float zoom = (float) Math.pow(2.0, this.zoomExp);
        double posX = this.prevMapX + (this.mapX - this.prevMapX) * partialTick;
        double posY = this.prevMapY + (this.mapY - this.prevMapY) * partialTick;

        // renderMapAndOverlay: the part of the map image in view, clipped to the image's edges.
        double mapScaleX = this.width / zoom;
        double mapScaleY = this.height / zoom;
        double minU = (posX - mapScaleX / 2.0) / MAP_IMAGE_WIDTH;
        double maxU = (posX + mapScaleX / 2.0) / MAP_IMAGE_WIDTH;
        double minV = (posY - mapScaleY / 2.0) / MAP_IMAGE_HEIGHT;
        double maxV = (posY + mapScaleY / 2.0) / MAP_IMAGE_HEIGHT;
        int x0 = 0;
        int x1 = this.width;
        int y0 = 0;
        int y1 = this.height;
        if (minU < 0.0) {
            x0 = (int) Math.round(-minU * MAP_IMAGE_WIDTH * zoom);
            minU = 0.0;
        }
        if (maxU > 1.0) {
            x1 = this.width - (int) Math.round((maxU - 1.0) * MAP_IMAGE_WIDTH * zoom);
            maxU = 1.0;
        }
        if (minV < 0.0) {
            y0 = (int) Math.round(-minV * MAP_IMAGE_HEIGHT * zoom);
            minV = 0.0;
        }
        if (maxV > 1.0) {
            y1 = this.height - (int) Math.round((maxV - 1.0) * MAP_IMAGE_HEIGHT * zoom);
            maxV = 1.0;
        }
        if (x1 > x0 && y1 > y0) {
            graphics.blit(MAP, x0, y0, x1, y1, (float) minU, (float) maxU, (float) minV, (float) maxV);
        }
        graphics.blit(RenderPipelines.GUI_TEXTURED, MAP_OVERLAY, 0, 0, 0.0f, 0.0f, this.width, this.height,
                256, 256, 256, 256, ARGB.white(0.2f));

        // renderRoads(false): a dot of black every so many points along each road.
        float[] dots = roadDots(Math.max(Math.round(400.0f / this.zoomStable), 1));
        for (int d = 0; d < dots.length; d += 2) {
            float x = screenX(dots[d], posX, zoom);
            float y = screenY(dots[d + 1], posY, zoom);
            if (x >= 0 && x < this.width && y >= 0 && y < this.height) {
                // At the point itself, not the nearest pixel: the original's quad ran x..x+1, y..y+1.
                graphics.pose().pushMatrix();
                graphics.pose().translate(x, y);
                graphics.fill(0, 0, 1, 1, 0xFF000000);
                graphics.pose().popMatrix();
            }
        }

        // renderWaypoints(all, labels off, toggles overridden): a locked waypoint's dot for each not hidden.
        for (LOTRWaypoint waypoint : LOTRWaypoint.values()) {
            if (waypoint.isHidden()) {
                continue;
            }
            float x = screenX(mapX(waypoint.xCoord), posX, zoom);
            float y = screenY(mapZ(waypoint.zCoord), posY, zoom);
            if (x >= -200 && x <= this.width + 200 && y >= -200 && y <= this.height + 200) {
                // drawTexturedModalRectFloat: at the waypoint itself, not the nearest pixel.
                graphics.pose().pushMatrix();
                graphics.pose().translate(x - 2.0f, y - 2.0f);
                graphics.blit(RenderPipelines.GUI_TEXTURED, MAP_ICONS, 0, 0, 0.0f, 200.0f, 4, 4, 256, 256);
                graphics.pose().popMatrix();
            }
        }
    }

    private static float @org.jspecify.annotations.Nullable [] roadDots;
    private static int roadDotsInterval;

    /** The road points drawn, every interval-th of each road, on the map image, worked out once (they do not move, only the view does). */
    private static float[] roadDots(int interval) {
        if (roadDots == null || roadDotsInterval != interval) {
            List<Float> dots = new ArrayList<>();
            for (Iterator<LOTRRoads> roads = LOTRRoads.getAllRoadsForDisplay(); roads.hasNext(); ) {
                LOTRRoads road = roads.next();
                for (int i = 0; i < road.pointCount(); i += interval) {
                    LOTRRoads.RoadPoint point = road.pointAt(i);
                    dots.add((float) mapX(point.x()));
                    dots.add((float) mapZ(point.z()));
                }
            }
            float[] array = new float[dots.size()];
            for (int i = 0; i < array.length; ++i) {
                array[i] = dots.get(i);
            }
            roadDots = array;
            roadDotsInterval = interval;
        }
        return roadDots;
    }

    /** transformCoords, first half: a world position to the map image. */
    private static double mapX(double worldX) {
        return worldX / LOTRMapCoords.SCALE + LOTRMapCoords.ORIGIN_X;
    }

    private static double mapZ(double worldZ) {
        return worldZ / LOTRMapCoords.SCALE + LOTRMapCoords.ORIGIN_Z;
    }

    /** transformCoords, second half: a place on the map image to the screen, by the view's centre and zoom. */
    private float screenX(double mapX, double posX, float zoom) {
        return (float) ((mapX - posX) * zoom + this.width / 2.0);
    }

    private float screenY(double mapZ, double posY, float zoom) {
        return (float) ((mapZ - posY) * zoom + this.height / 2.0);
    }
}
