package net.blueskiez77.lord_of_the_rings__middle_earth.client.gui;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRMapCoords;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRRoads;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;

import com.mojang.blaze3d.platform.NativeImage;

import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.fabricmc.fabric.api.event.Event;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.client.gui.components.SpriteIconButton;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
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

    /**
     * The buttons are laid out once every other mod has had its turn with the title screen's (Mod
     * Menu adds its Mods button and moves the others round it, by their order and by their being
     * vanilla buttons), so the move down and the red-book buttons take in theirs too.
     */
    public static void init(Identifier lateInitPhase) {
        ScreenEvents.AFTER_INIT.addPhaseOrdering(Event.DEFAULT_PHASE, lateInitPhase);
        ScreenEvents.AFTER_INIT.register(lateInitPhase, (minecraft, screen, width, height) -> {
            if (screen instanceof LOTRMainMenuScreen menu) {
                menu.layOutButtons();
            }
        });
    }

    @Override
    protected void init() {
        super.init();
        // No splash text: the original's menu had none.
        this.splash = null;
        this.realmsButton = null;
        this.vanillaLayout.clear();
        for (AbstractWidget widget : Screens.getWidgets(this)) {
            if (!(LOTRModMenuCompat.LOADED && LOTRModMenuCompat.isModMenuWidget(widget))) {
                this.vanillaLayout.put(widget, new int[]{widget.getX(), widget.getY(), widget.getWidth()});
            }
        }
    }

    /** Where the title screen's own widgets stood before any other mod's AFTER_INIT moved them. */
    private final java.util.Map<AbstractWidget, int[]> vanillaLayout = new java.util.LinkedHashMap<>();

    /** initGui: the buttons moved down as far as 50, and the plain ones made red-book buttons, each in its place. */
    private void layOutButtons() {
        List<AbstractWidget> widgets = Screens.getWidgets(this);
        if (LOTRModMenuCompat.LOADED) {
            splitRealmsForModMenu(widgets);
        }
        List<AbstractWidget> buttons = new ArrayList<>();
        for (AbstractWidget widget : widgets) {
            if (!(widget instanceof PlainTextButton)) {
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
                LOTRRedBookButton redBook = new LOTRRedBookButton(plain.getX(), plain.getY(), plain.getWidth(),
                        plain.getHeight(), plain.getMessage(), () -> {
                }) {
                    @Override
                    public void onPress(InputWithModifiers input) {
                        plain.onPress(input);
                    }
                };
                redBook.active = plain.active;
                redBook.visible = plain.visible;
                widgets.set(widgets.indexOf(plain), redBook);
                if (plain.getMessage().getContents() instanceof TranslatableContents key
                        && key.getKey().equals("menu.online")) {
                    this.realmsButton = redBook;
                }
            }
        }
    }

    /**
     * With Mod Menu installed, its Mods button is always the right half of the Realms button, whichever
     * style Mod Menu is set to: its own changes to the screen are undone (its buttons taken away, the
     * others put back where the title screen placed them, the icon row centred again without its icon),
     * and the Realms button narrowed to make room.
     */
    private void splitRealmsForModMenu(List<AbstractWidget> widgets) {
        AbstractWidget realms = null;
        for (AbstractWidget widget : this.vanillaLayout.keySet()) {
            if (widget.getMessage().getContents() instanceof TranslatableContents key && key.getKey().equals("menu.online")) {
                realms = widget;
            }
        }
        for (int i = widgets.size() - 1; i >= 0; --i) {
            if (LOTRModMenuCompat.isModMenuWidget(widgets.get(i))) {
                // "Replace Realms" put its button in the Realms button's place.
                if (realms != null && !widgets.contains(realms)) {
                    widgets.set(i, realms);
                } else {
                    widgets.remove(i);
                }
            }
        }
        for (var entry : this.vanillaLayout.entrySet()) {
            entry.getKey().setPosition(entry.getValue()[0], entry.getValue()[1]);
            entry.getKey().setWidth(entry.getValue()[2]);
        }
        // The icon row (friends, language, accessibility), centred as the title screen centres it.
        List<AbstractWidget> icons = new ArrayList<>();
        for (AbstractWidget widget : widgets) {
            if (widget instanceof SpriteIconButton) {
                icons.add(widget);
            }
        }
        icons.sort(java.util.Comparator.comparingInt(AbstractWidget::getX));
        int iconsWidth = icons.size() * 20 + (icons.size() - 1) * 4;
        for (int k = 0; k < icons.size(); ++k) {
            icons.get(k).setX(this.width / 2 - iconsWidth / 2 + k * 24);
        }
        if (realms == null) {
            return;
        }
        realms.setWidth(98);
        Button mods = Button.builder(LOTRModMenuCompat.modsButtonText(),
                        button -> this.minecraft.gui.setScreen(LOTRModMenuCompat.modsScreen(this)))
                .bounds(realms.getX() + 100, realms.getY(), 98, 20).build();
        widgets.add(widgets.indexOf(realms) + 1, mods);
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

    private @org.jspecify.annotations.Nullable AbstractWidget realmsButton;

    /**
     * How far the Realms notification icons must move to sit at the right end of the Realms button.
     * They place themselves where the vanilla title screen's button is, by the size they were given
     * (which Mod Menu alters to follow its own moves); here the button has moved again, or narrowed.
     */
    public int[] realmsNotificationOffset(Screen notifications) {
        if (this.realmsButton == null) {
            return new int[]{0, 0};
        }
        int vanillaRight = notifications.width / 2 + 100;
        int vanillaY = notifications.height / 4 + 48 + 48;
        return new int[]{this.realmsButton.getRight() - vanillaRight, this.realmsButton.getY() - vanillaY};
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

        // renderRoads(false): a dot of black every so many points along each road. renderWaypoints(all,
        // labels off, toggles overridden): a locked waypoint's dot for each not hidden. Both are drawn as
        // one element of rectangles, smoothed to the screen's own pixels (see drawSmoothSprite).
        int guiScale = this.minecraft.getWindow().getGuiScale();
        LOTRMapQuadsRenderState quads = new LOTRMapQuadsRenderState(new ScreenRectangle(0, 0, this.width, this.height),
                graphics.scissorStack.peek());
        float[] dots = roadDots(Math.max(Math.round(400.0f / this.zoomStable), 1));
        for (int d = 0; d < dots.length; d += 2) {
            float x = screenX(dots[d], posX, zoom);
            float y = screenY(dots[d + 1], posY, zoom);
            if (x >= 0 && x < this.width && y >= 0 && y < this.height) {
                // The original's quad, x..x+1 by y..y+1, at the point itself.
                drawSmoothSprite(quads, x, y, guiScale, ROAD_DOT, 1);
            }
        }
        int[] waypointIcon = waypointIcon();
        for (LOTRWaypoint waypoint : LOTRWaypoint.values()) {
            if (waypoint.isHidden()) {
                continue;
            }
            float x = screenX(mapX(waypoint.xCoord), posX, zoom);
            float y = screenY(mapZ(waypoint.zCoord), posY, zoom);
            if (x >= -200 && x <= this.width + 200 && y >= -200 && y <= this.height + 200) {
                // drawTexturedModalRectFloat: the 4 by 4 icon centred on the waypoint itself.
                drawSmoothSprite(quads, x - 2.0f, y - 2.0f, guiScale, waypointIcon, WAYPOINT_ICON_SIZE);
            }
        }
        if (!quads.isEmpty()) {
            graphics.guiRenderState.addGuiElement(quads);
        }
    }

    private static final int[] ROAD_DOT = {0xFF000000};
    private static final int WAYPOINT_ICON_SIZE = 4;
    private int @org.jspecify.annotations.Nullable [] waypointIcon;

    /** The locked waypoint icon's colours, read from map_screen.png at (0, 200), row by row. */
    private int[] waypointIcon() {
        if (this.waypointIcon == null) {
            int[] icon = new int[WAYPOINT_ICON_SIZE * WAYPOINT_ICON_SIZE];
            try (InputStream in = this.minecraft.getResourceManager().open(MAP_ICONS);
                 NativeImage image = NativeImage.read(in)) {
                for (int y = 0; y < WAYPOINT_ICON_SIZE; ++y) {
                    for (int x = 0; x < WAYPOINT_ICON_SIZE; ++x) {
                        icon[y * WAYPOINT_ICON_SIZE + x] = image.getPixel(x, 200 + y);
                    }
                }
            } catch (IOException e) {
                LOTRMod.LOGGER.warn("LOTR: could not read the waypoint icon from {}", MAP_ICONS, e);
            }
            this.waypointIcon = icon;
        }
        return this.waypointIcon;
    }

    /**
     * A size by size sprite of texels, each a GUI pixel, its top left at a GUI position, drawn in the
     * screen's own pixels: wholly covered pixels take their texel's colour, and those on a texel's edge
     * a mix of the texels (and what lies beneath) by how much of the pixel each covers. Drawn so, it
     * glides as the map moves and zooms, rather than stepping a pixel at a time out of step with the
     * sprites beside it.
     */
    private static void drawSmoothSprite(LOTRMapQuadsRenderState quads, float guiX, float guiY, int guiScale,
                                         int[] texels, int size) {
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
                int alphaByte = Math.round(alpha * 255.0f);
                if (alphaByte > 0) {
                    quads.add((float) cols.start[c] / guiScale, (float) rows.start[r] / guiScale,
                            (float) (cols.start[c] + cols.width[c]) / guiScale,
                            (float) (rows.start[r] + rows.width[r]) / guiScale,
                            ARGB.color(alphaByte, Math.round(red / alpha), Math.round(green / alpha),
                                    Math.round(blue / alpha)));
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
