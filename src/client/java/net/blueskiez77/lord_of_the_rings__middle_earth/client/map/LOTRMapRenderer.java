package net.blueskiez77.lord_of_the_rings__middle_earth.client.map;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import com.mojang.blaze3d.platform.NativeImage;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.map.LOTRMapQuadsRenderState;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRPlayerAchievements;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.config.LOTRConfig;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRAbstractWaypoint;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRCustomWaypoint;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRMapCoords;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRRoads;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

import org.jspecify.annotations.Nullable;

/**
 * LOTRGuiRendererMap, with the drawing the original kept in LOTRGuiMap and LOTRTextures: the map of
 * Middle-earth in a window of the screen, centred on a place on the map image at a zoom -- the ocean,
 * the map (Meneltarma hidden in the sea until the player has stood on it) and its overlay, the roads
 * and their names, the waypoints and theirs, and the map's labels -- and the vignettes laid over it.
 * The main menu, the map screen, the screen of a journey and the loading screen on entering
 * Middle-earth draw with it.
 *
 * <p>Dots and icons are drawn smoothed to the screen's own pixels (see {@link #drawSmoothSprite}), so
 * they glide as the map moves rather than stepping a pixel at a time.
 *
 * <p>The sepia map: this version of the original made no sepia image (its generation was commented
 * out), so the sepia map is the map itself, on a sepia sea.
 */
public final class LOTRMapRenderer {

    public static final Identifier MAP = Identifier.fromNamespaceAndPath("lotr", "map/map.png");
    public static final Identifier MAP_OVERLAY = Identifier.fromNamespaceAndPath("lotr", "map/map_overlay.png");
    public static final Identifier MAP_ICONS = Identifier.fromNamespaceAndPath("lotr", "map/map_screen.png");
    public static final Identifier OSRS = Identifier.fromNamespaceAndPath("lotr", "map/osrs.png");
    public static final Identifier VIGNETTE = Identifier.withDefaultNamespace("textures/misc/vignette.png");
    public static final int MAP_IMAGE_WIDTH = 3200;
    public static final int MAP_IMAGE_HEIGHT = 4000;
    /** LOTRBiome.ocean's colour. */
    public static final int OCEAN_COLOUR = 153997;

    /** The centre of the view, on the map image. */
    public double posX;
    public double posY;
    public float zoomScale = 1.0f;
    public float zoomExp;
    public float zoomScaleStable = 1.0f;
    /** The window the map is drawn in. */
    public int mapXMin;
    public int mapYMin;
    public int mapXMax;
    public int mapYMax;
    public boolean enableZoomOutWPFading = true;

    public void setView(double x, double y, float zoomExp, float zoomStable) {
        this.posX = x;
        this.posY = y;
        this.zoomExp = zoomExp;
        this.zoomScale = (float) Math.pow(2.0, zoomExp);
        this.zoomScaleStable = zoomStable;
    }

    public void setWindow(int x0, int y0, int x1, int y1) {
        this.mapXMin = x0;
        this.mapYMin = y0;
        this.mapXMax = x1;
        this.mapYMax = y1;
    }

    public int mapWidth() {
        return this.mapXMax - this.mapXMin;
    }

    public int mapHeight() {
        return this.mapYMax - this.mapYMin;
    }

    public static boolean isOSRS() {
        return LOTRConfig.osrsMap;
    }

    /**
     * LOTRGuiRendererMap.renderMap: in a window of the screen, the ocean, the map and its overlay, the
     * roads and every fixed waypoint, without labels or fading.
     */
    public void renderMap(GuiGraphicsExtractor graphics, Font font, @Nullable Player player, boolean sepia, int x0, int y0, int x1, int y1) {
        setWindow(x0, y0, x1, y1);
        graphics.fill(x0, y0, x1, y1, getMapOceanColor(sepia));
        this.enableZoomOutWPFading = false;
        drawMapAndOverlay(graphics, player, sepia, 1.0f, true);
        drawRoads(graphics, font, false);
        drawWaypoints(graphics, font, List.of(LOTRWaypoint.values()), player, waypoint -> true, false);
    }

    // ---------------------------------------------------------------- coordinates

    /** transformCoords: a world position to the screen. */
    public float[] transformCoords(double x, double z) {
        return transformMapCoords(x / LOTRMapCoords.SCALE + LOTRMapCoords.ORIGIN_X, z / LOTRMapCoords.SCALE + LOTRMapCoords.ORIGIN_Z);
    }

    /** transformMapCoords: a place on the map image to the screen. */
    public float[] transformMapCoords(double x, double z) {
        float sx = (float) ((x - this.posX) * this.zoomScale) + (this.mapXMin + mapWidth() / 2.0f);
        float sy = (float) ((z - this.posY) * this.zoomScale) + (this.mapYMin + mapHeight() / 2.0f);
        return new float[]{sx, sy};
    }

    // ---------------------------------------------------------------- colours

    /** LOTRTextures.getMapOceanColor. */
    public static int getMapOceanColor(boolean sepia) {
        if (isOSRS()) {
            return -10324058;
        }
        return 0xFF000000 | (sepia ? getSepia(OCEAN_COLOUR) : OCEAN_COLOUR);
    }

    /** LOTRTextures.getSepia. */
    public static int getSepia(int rgb) {
        float r = ARGB.red(rgb) / 255.0f;
        float g = ARGB.green(rgb) / 255.0f;
        float b = ARGB.blue(rgb) / 255.0f;
        float newR = Mth.clamp(r * 0.79f + g * 0.39f + b * 0.26f, 0.0f, 1.0f);
        float newG = Mth.clamp(r * 0.52f + g * 0.35f + b * 0.19f, 0.0f, 1.0f);
        float newB = Mth.clamp(r * 0.35f + g * 0.26f + b * 0.15f, 0.0f, 1.0f);
        return ARGB.color(ARGB.alpha(rgb), Math.round(newR * 255.0f), Math.round(newG * 255.0f), Math.round(newB * 255.0f));
    }

    // ---------------------------------------------------------------- the map

    /**
     * renderMapAndOverlay: the ocean over the window, the part of the map image in view (clipped to the
     * image's edges), Meneltarma's waters, and over all the map's overlay, faintly.
     */
    public void drawMapAndOverlay(GuiGraphicsExtractor graphics, @Nullable Player player, boolean sepia, float alpha, boolean overlay) {
        int x0 = this.mapXMin;
        int x1 = this.mapXMax;
        int y0 = this.mapYMin;
        int y1 = this.mapYMax;
        double mapScaleX = mapWidth() / this.zoomScale;
        double mapScaleY = mapHeight() / this.zoomScale;
        double minU = (this.posX - mapScaleX / 2.0) / MAP_IMAGE_WIDTH;
        double maxU = (this.posX + mapScaleX / 2.0) / MAP_IMAGE_WIDTH;
        double minV = (this.posY - mapScaleY / 2.0) / MAP_IMAGE_HEIGHT;
        double maxV = (this.posY + mapScaleY / 2.0) / MAP_IMAGE_HEIGHT;
        if (minU < 0.0) {
            x0 = this.mapXMin + (int) Math.round(-minU * MAP_IMAGE_WIDTH * this.zoomScale);
            minU = 0.0;
        }
        if (maxU > 1.0) {
            x1 = this.mapXMax - (int) Math.round((maxU - 1.0) * MAP_IMAGE_WIDTH * this.zoomScale);
            maxU = 1.0;
        }
        if (minV < 0.0) {
            y0 = this.mapYMin + (int) Math.round(-minV * MAP_IMAGE_HEIGHT * this.zoomScale);
            minV = 0.0;
        }
        if (maxV > 1.0) {
            y1 = this.mapYMax - (int) Math.round((maxV - 1.0) * MAP_IMAGE_HEIGHT * this.zoomScale);
            maxV = 1.0;
        }
        if (x1 > x0 && y1 > y0 && maxU > minU && maxV > minV) {
            if (alpha >= 1.0f) {
                graphics.blit(MAP, x0, y0, x1, y1, (float) minU, (float) maxU, (float) minV, (float) maxV);
            } else {
                graphics.blit(RenderPipelines.GUI_TEXTURED, MAP, x0, y0, (float) (minU * MAP_IMAGE_WIDTH), (float) (minV * MAP_IMAGE_HEIGHT),
                        x1 - x0, y1 - y0, (int) Math.round((maxU - minU) * MAP_IMAGE_WIDTH), (int) Math.round((maxV - minV) * MAP_IMAGE_HEIGHT),
                        MAP_IMAGE_WIDTH, MAP_IMAGE_HEIGHT, ARGB.white(alpha));
            }
            hideMeneltarma(graphics, player, sepia, x0, x1, y0, y1, minU, maxU, minV, maxV);
        }
        if (overlay && !isOSRS()) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, MAP_OVERLAY, this.mapXMin, this.mapYMin, 0.0f, 0.0f, mapWidth(), mapHeight(),
                    256, 256, 256, 256, ARGB.white(0.2f));
        }
    }

    /** LOTRTextures.drawMap's Meneltarma: the sea over the isle, forty map pixels square, until enterMeneltarma. */
    private static void hideMeneltarma(GuiGraphicsExtractor graphics, @Nullable Player player, boolean sepia, int x0, int x1, int y0, int y1,
                                       double minU, double maxU, double minV, double maxV) {
        if (player != null && LOTRPlayerAchievements.hasAchievement(player, LOTRAchievement.ENTER_MENELTARMA)) {
            return;
        }
        double mtX = LOTRWaypoint.MENELTARMA_SUMMIT.getX();
        double mtY = LOTRWaypoint.MENELTARMA_SUMMIT.getY();
        int mtW = 20;
        double mtMinU = Math.max((mtX - mtW) / MAP_IMAGE_WIDTH, minU);
        double mtMaxU = Math.min((mtX + mtW) / MAP_IMAGE_WIDTH, maxU);
        double mtMinV = Math.max((mtY - mtW) / MAP_IMAGE_HEIGHT, minV);
        double mtMaxV = Math.min((mtY + mtW) / MAP_IMAGE_HEIGHT, maxV);
        if (mtMinU >= mtMaxU || mtMinV >= mtMaxV) {
            return;
        }
        double ratioX = (x1 - x0) / (maxU - minU);
        double ratioY = (y1 - y0) / (maxV - minV);
        graphics.fill((int) Math.floor(x0 + (mtMinU - minU) * ratioX), (int) Math.floor(y0 + (mtMinV - minV) * ratioY),
                (int) Math.ceil(x0 + (mtMaxU - minU) * ratioX), (int) Math.ceil(y0 + (mtMaxV - minV) * ratioY), getMapOceanColor(sepia));
    }

    // ---------------------------------------------------------------- roads

    /**
     * renderRoads: a dot every so many points along each road (fewer the further out), fading as
     * one zooms out; and with labels, each road's name along it, upright, every so often but not by
     * its ends.
     */
    public void drawRoads(GuiGraphicsExtractor graphics, Font font, boolean labels) {
        float roadZoomlerp = Math.min((this.zoomExp + 3.3f) / 2.2f, 1.0f);
        if (!this.enableZoomOutWPFading) {
            roadZoomlerp = 1.0f;
        }
        if (roadZoomlerp <= 0.0f) {
            return;
        }
        int guiScale = Minecraft.getInstance().getWindow().getGuiScale();
        LOTRMapQuadsRenderState quads = newQuads(graphics);
        int interval = Math.max(Math.round(400.0f / this.zoomScaleStable), 1);
        int roadColour = isOSRS() ? 6575407 : 0;
        float roadAlpha = isOSRS() ? 1.0f : roadZoomlerp;
        int[] dot = {ARGB.color(Math.round(roadAlpha * 255.0f), roadColour)};
        List<Runnable> names = new ArrayList<>();
        for (Iterator<LOTRRoads> roads = LOTRRoads.getAllRoadsForDisplay(); roads.hasNext(); ) {
            LOTRRoads road = roads.next();
            int points = road.pointCount();
            for (int i = 0; i < points; i += interval) {
                LOTRRoads.RoadPoint point = road.pointAt(i);
                float[] pos = transformCoords(point.x(), point.z());
                float x = pos[0];
                float y = pos[1];
                if (x >= this.mapXMin && x < this.mapXMax && y >= this.mapYMin && y < this.mapYMax) {
                    if (isOSRS()) {
                        float roadWidth = 3.0f * this.zoomScale;
                        quads.add(x - (roadWidth - 1.0f), y - (roadWidth - 1.0f), x + roadWidth, y + roadWidth, dot[0]);
                    } else {
                        drawSmoothSprite(quads, x, y, guiScale, dot, 1, 1.0f);
                    }
                }
                int clip = 200;
                if (!labels || x < this.mapXMin - clip || x > this.mapXMax + clip || y < this.mapYMin - clip || y > this.mapYMax + clip) {
                    continue;
                }
                float zoomlerp = Math.min((this.zoomExp + 1.0f) / 4.0f, 1.0f);
                if (zoomlerp <= 0.0f) {
                    continue;
                }
                Component name = road.getDisplayName();
                int nameWidth = font.width(name);
                int nameInterval = (int) ((nameWidth * 2 + 100) * 200.0f / this.zoomScaleStable);
                if (nameInterval <= 0 || i % nameInterval >= interval) {
                    continue;
                }
                double dMax = (nameWidth / 2.0 + 25.0) * zoomlerp;
                boolean endNear = false;
                for (LOTRRoads.RoadPoint end : road.endpoints) {
                    float[] endPos = transformCoords(end.x(), end.z());
                    float dx = x - endPos[0];
                    float dy = y - endPos[1];
                    if (dx * dx + dy * dy < dMax * dMax) {
                        endNear = true;
                    }
                }
                if (endNear) {
                    continue;
                }
                LOTRRoads.RoadPoint next = road.pointAt(Math.min(i + 1, points - 1));
                LOTRRoads.RoadPoint prev = road.pointAt(Math.max(i - 1, 0));
                float angle = (float) Math.toDegrees(Math.atan((next.z() - prev.z()) / (next.x() - prev.x())));
                if (Math.abs(angle) > 90.0f) {
                    angle += 180.0f;
                }
                float finalAngle = angle;
                float scale = zoomlerp;
                int alphaI = Mth.clamp((int) (zoomlerp * 0.8f * 255.0f), 4, 255);
                names.add(() -> drawRotatedText(graphics, font, name, x, y, scale, finalAngle, -nameWidth / 2, -15, alphaI, true));
            }
        }
        if (!quads.isEmpty()) {
            graphics.guiRenderState.addGuiElement(quads);
        }
        if (!names.isEmpty()) {
            graphics.enableScissor(this.mapXMin, this.mapYMin, this.mapXMax, this.mapYMax);
            names.forEach(Runnable::run);
            graphics.disableScissor();
        }
    }

    // ---------------------------------------------------------------- waypoints

    private static final int WAYPOINT_ICON_SIZE = 4;
    private static int @Nullable [] @Nullable [] waypointIcons;

    /** The map sheet's 4 by 4 waypoint icons: (locked, unlocked) by (fixed, custom, shared), from (0, 200). */
    private static int[] waypointIcon(boolean unlocked, boolean custom, boolean shared) {
        if (waypointIcons == null) {
            int[][] icons = new int[6][WAYPOINT_ICON_SIZE * WAYPOINT_ICON_SIZE];
            try (InputStream in = Minecraft.getInstance().getResourceManager().open(MAP_ICONS);
                 NativeImage image = NativeImage.read(in)) {
                for (int kind = 0; kind < 3; ++kind) {
                    for (int lock = 0; lock < 2; ++lock) {
                        int[] icon = icons[kind * 2 + lock];
                        for (int y = 0; y < WAYPOINT_ICON_SIZE; ++y) {
                            for (int x = 0; x < WAYPOINT_ICON_SIZE; ++x) {
                                icon[y * WAYPOINT_ICON_SIZE + x] = image.getPixel(lock * 4 + x, 200 + kind * 4 + y);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                LOTRMod.LOGGER.warn("LOTR: could not read the waypoint icons from {}", MAP_ICONS, e);
            }
            waypointIcons = icons;
        }
        int kind = shared ? 2 : custom ? 1 : 0;
        return waypointIcons[kind * 2 + (unlocked ? 1 : 0)];
    }

    /** wpZoomlerp: the waypoints fading out as one zooms far out. */
    public float waypointFade() {
        float wpZoomlerp = Math.min((this.zoomExp + 3.3f) / 2.2f, 1.0f);
        return this.enableZoomOutWPFading ? wpZoomlerp : 1.0f;
    }

    /**
     * renderWaypoints, the first pass: each one shown, unless hidden and not yet unlocked, its icon by
     * its kind and whether the player has it, and with labels its name above it.
     */
    public void drawWaypoints(GuiGraphicsExtractor graphics, Font font, Iterable<? extends LOTRAbstractWaypoint> waypoints,
                              @Nullable Player player, Predicate<LOTRAbstractWaypoint> visible, boolean labels) {
        float wpZoomlerp = waypointFade();
        if (wpZoomlerp <= 0.0f) {
            return;
        }
        int guiScale = Minecraft.getInstance().getWindow().getGuiScale();
        graphics.enableScissor(this.mapXMin, this.mapYMin, this.mapXMax, this.mapYMax);
        LOTRMapQuadsRenderState quads = newQuads(graphics);
        List<Runnable> overQuads = new ArrayList<>();
        for (LOTRAbstractWaypoint waypoint : waypoints) {
            boolean unlocked = player != null && waypoint.hasPlayerUnlocked(player);
            boolean custom = waypoint instanceof LOTRCustomWaypoint;
            boolean shared = waypoint instanceof LOTRCustomWaypoint cwp && cwp.isShared();
            if (!visible.test(waypoint) || waypoint.isHidden() && !unlocked) {
                continue;
            }
            float[] pos = transformCoords(waypoint.getXCoord(), waypoint.getZCoord());
            float x = pos[0];
            float y = pos[1];
            int clip = 200;
            if (x < this.mapXMin - clip || x > this.mapXMax + clip || y < this.mapYMin - clip || y > this.mapYMax + clip) {
                continue;
            }
            if (isOSRS()) {
                overQuads.add(() -> {
                    graphics.pose().pushMatrix();
                    graphics.pose().scale(0.33f, 0.33f);
                    graphics.blit(RenderPipelines.GUI_TEXTURED, OSRS, Math.round(x / 0.33f - 8.0f), Math.round(y / 0.33f - 8.0f),
                            0.0f, 0.0f, 15, 15, 256, 256);
                    graphics.pose().popMatrix();
                });
            } else {
                drawSmoothSprite(quads, x - 2.0f, y - 2.0f, guiScale, waypointIcon(unlocked, custom, shared), WAYPOINT_ICON_SIZE, wpZoomlerp);
            }
            if (labels) {
                float zoomlerp = Math.min((this.zoomExp + 1.0f) / 4.0f, 1.0f);
                if (zoomlerp > 0.0f) {
                    Component name = waypoint.getDisplayName();
                    int alphaI = Mth.clamp((int) (zoomlerp * 0.8f * 255.0f), 4, 255);
                    overQuads.add(() -> drawRotatedText(graphics, font, name, x, y, zoomlerp, 0.0f, -font.width(name) / 2, -15, alphaI, true));
                }
            }
        }
        if (!quads.isEmpty()) {
            graphics.guiRenderState.addGuiElement(quads);
        }
        overQuads.forEach(Runnable::run);
        graphics.disableScissor();
    }

    /** renderWaypoints, the second pass: the one under the mouse, within five pixels, nearest. */
    public @Nullable LOTRAbstractWaypoint waypointUnderMouse(Iterable<? extends LOTRAbstractWaypoint> waypoints, @Nullable Player player,
                                                            Predicate<LOTRAbstractWaypoint> visible, int mouseX, int mouseY,
                                                            @Nullable LOTRAbstractWaypoint except) {
        if (waypointFade() <= 0.0f) {
            return null;
        }
        LOTRAbstractWaypoint mouseOverWP = null;
        double distanceMouseOverWP = Double.MAX_VALUE;
        for (LOTRAbstractWaypoint waypoint : waypoints) {
            boolean unlocked = player != null && waypoint.hasPlayerUnlocked(player);
            if (waypoint == except || !visible.test(waypoint) || waypoint.isHidden() && !unlocked) {
                continue;
            }
            float[] pos = transformCoords(waypoint.getXCoord(), waypoint.getZCoord());
            if (pos[0] >= this.mapXMin - 2 && pos[0] <= this.mapXMax + 2 && pos[1] >= this.mapYMin - 2 && pos[1] <= this.mapYMax + 2) {
                double dx = pos[0] - mouseX;
                double dy = pos[1] - mouseY;
                double distToWP = Math.sqrt(dx * dx + dy * dy);
                if (distToWP <= 5.0 && distToWP <= distanceMouseOverWP) {
                    mouseOverWP = waypoint;
                    distanceMouseOverWP = distToWP;
                }
            }
        }
        return mouseOverWP;
    }

    // ---------------------------------------------------------------- labels

    /** renderLabels: each land's name, at its zooms only, brightest midway, turned as it lies. */
    public void drawLabels(GuiGraphicsExtractor graphics, Font font) {
        graphics.enableScissor(this.mapXMin, this.mapYMin, this.mapXMax, this.mapYMax);
        for (LOTRMapLabels label : LOTRMapLabels.values()) {
            float zoomlerp = (this.zoomExp - label.minZoom) / (label.maxZoom - label.minZoom);
            if (zoomlerp <= 0.0f || zoomlerp >= 1.0f) {
                continue;
            }
            float alpha = (0.5f - Math.abs(zoomlerp - 0.5f)) / 0.5f * 0.7f;
            if (isOSRS()) {
                if (alpha < 0.3f) {
                    continue;
                }
                alpha = 1.0f;
            }
            float[] pos = transformMapCoords(label.posX, label.posY);
            Component s = label.getDisplayName();
            int alphaI = Mth.clamp((int) (alpha * 255.0f), 4, 255);
            float scale = this.zoomScale * label.scale;
            int strX = -font.width(s) / 2;
            int strY = -font.lineHeight / 2;
            if (isOSRS()) {
                int colour = label.scale > 2.5f ? 16755200 : 16777215;
                graphics.pose().pushMatrix();
                graphics.pose().translate(pos[0], pos[1]);
                graphics.pose().scale(scale, scale);
                graphics.text(font, s, strX + 1, strY + 1, alphaI << 24, false);
                graphics.text(font, s, strX, strY, colour | alphaI << 24, false);
                graphics.pose().popMatrix();
            } else {
                drawRotatedText(graphics, font, s, pos[0], pos[1], scale, label.angle, strX, strY, alphaI, false);
            }
        }
        graphics.disableScissor();
    }

    /** Text at a point, scaled and turned about it, white at an alpha, with a shadow cast down-right if asked. */
    private static void drawRotatedText(GuiGraphicsExtractor graphics, Font font, Component s, float x, float y, float scale, float angle,
                                        int strX, int strY, int alphaI, boolean shadow) {
        graphics.pose().pushMatrix();
        graphics.pose().translate(x, y);
        graphics.pose().scale(scale, scale);
        graphics.pose().rotate((float) Math.toRadians(angle));
        if (shadow) {
            graphics.text(font, s, strX + 1, strY + 1, alphaI << 24, false);
        }
        graphics.text(font, s, strX, strY, 0xFFFFFF | alphaI << 24, false);
        graphics.pose().popMatrix();
    }

    // ---------------------------------------------------------------- vignettes

    /** renderVignettes: the vignette laid over the window, that many times, as much of it as the window takes of the screen. */
    public static void drawVignettes(GuiGraphicsExtractor graphics, int count, int x0, int y0, int x1, int y1, int screenWidth, int screenHeight) {
        for (int l = 0; l < count; ++l) {
            graphics.blit(RenderPipelines.VIGNETTE, VIGNETTE, x0, y0, (float) x0 / screenWidth * 256.0f, (float) y0 / screenHeight * 256.0f,
                    x1 - x0, y1 - y0, Math.round((float) (x1 - x0) / screenWidth * 256.0f), Math.round((float) (y1 - y0) / screenHeight * 256.0f),
                    256, 256);
        }
    }

    // ---------------------------------------------------------------- smoothed sprites

    private LOTRMapQuadsRenderState newQuads(GuiGraphicsExtractor graphics) {
        return new LOTRMapQuadsRenderState(new ScreenRectangle(this.mapXMin, this.mapYMin, mapWidth(), mapHeight()),
                graphics.scissorStack.peek());
    }

    /**
     * A size by size sprite of texels, each a GUI pixel, its top left at a GUI position, drawn in the
     * screen's own pixels: wholly covered pixels take their texel's colour, and those on a texel's edge
     * a mix of the texels (and what lies beneath) by how much of the pixel each covers; all at an
     * alpha.
     */
    public static void drawSmoothSprite(LOTRMapQuadsRenderState quads, float guiX, float guiY, int guiScale, int[] texels, int size, float fade) {
        Bands cols = new Bands(guiX * guiScale, size, guiScale);
        Bands rows = new Bands(guiY * guiScale, size, guiScale);
        for (int c = 0; c < cols.count; ++c) {
            if (cols.width[c] <= 0) {
                continue;
            }
            for (int r = 0; r < rows.count; ++r) {
                if (rows.width[r] <= 0) {
                    continue;
                }
                float alpha = 0.0f;
                float red = 0.0f;
                float green = 0.0f;
                float blue = 0.0f;
                for (int a = 0; a < 2; ++a) {
                    int tx = cols.texel[c][a];
                    for (int b = 0; b < 2; ++b) {
                        int ty = rows.texel[r][b];
                        if (tx < 0 || ty < 0) {
                            continue;
                        }
                        int texel = texels[ty * size + tx];
                        float weight = cols.weight[c][a] * rows.weight[r][b] * ARGB.alpha(texel) / 255.0f;
                        alpha += weight;
                        red += ARGB.red(texel) * weight;
                        green += ARGB.green(texel) * weight;
                        blue += ARGB.blue(texel) * weight;
                    }
                }
                int alphaByte = Math.round(alpha * fade * 255.0f);
                if (alphaByte > 0) {
                    quads.add((float) cols.start[c] / guiScale, (float) rows.start[r] / guiScale,
                            (float) (cols.start[c] + cols.width[c]) / guiScale,
                            (float) (rows.start[r] + rows.width[r]) / guiScale,
                            ARGB.color(alphaByte, Math.round(red / alpha), Math.round(green / alpha), Math.round(blue / alpha)));
                }
            }
        }
    }

    /**
     * One axis of a smoothed sprite, in screen pixels: for each texel boundary, the pixel line it falls
     * in, shared by the texel before (by how far in the boundary lies) and the texel after; between
     * them, each texel's wholly covered lines. A texel of -1 is none, beyond the sprite.
     */
    private static final class Bands {
        final int count;
        final int[] start;
        final int[] width;
        final int[][] texel;
        final float[][] weight;

        Bands(float screenPos, int size, int scale) {
            int origin = Mth.floor(screenPos);
            float frac = screenPos - origin;
            this.count = size * 2 + 1;
            this.start = new int[this.count];
            this.width = new int[this.count];
            this.texel = new int[this.count][2];
            this.weight = new float[this.count][2];
            for (int k = 0; k <= size; ++k) {
                int edge = k * 2;
                this.start[edge] = origin + k * scale;
                this.width[edge] = 1;
                this.texel[edge][0] = k > 0 ? k - 1 : -1;
                this.weight[edge][0] = frac;
                this.texel[edge][1] = k < size ? k : -1;
                this.weight[edge][1] = 1.0f - frac;
                if (k < size) {
                    this.start[edge + 1] = origin + k * scale + 1;
                    this.width[edge + 1] = scale - 1;
                    this.texel[edge + 1][0] = k;
                    this.weight[edge + 1][0] = 1.0f;
                    this.texel[edge + 1][1] = -1;
                }
            }
        }
    }
}
