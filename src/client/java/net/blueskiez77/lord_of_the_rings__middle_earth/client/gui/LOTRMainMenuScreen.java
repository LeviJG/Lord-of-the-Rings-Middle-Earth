package net.blueskiez77.lord_of_the_rings__middle_earth.client.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.map.LOTRMapRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;


import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.fabricmc.fabric.api.event.Event;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.client.gui.components.SpriteIconButton;
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
 * <p>The map is drawn by {@link LOTRMapRenderer}, as the original's
 * LOTRGuiRendererMap drew it: no labels, no fading, every waypoint locked.
 */
public class LOTRMainMenuScreen extends TitleScreen {

    private static final Identifier MENU_OVERLAY = Identifier.fromNamespaceAndPath("lotr", "gui/menu_overlay.png");
    /** LOTRBiome.ocean's colour. */

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
    private final LOTRMapRenderer mapRenderer = new LOTRMapRenderer();
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
        LOTRMapRenderer.drawVignettes(graphics, 2, 0, 0, this.width, this.height, this.width, this.height);
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

    /** LOTRGuiRendererMap.renderMap over the whole screen, the view eased between ticks. */
    private void renderMap(GuiGraphicsExtractor graphics, float partialTick) {
        double posX = this.prevMapX + (this.mapX - this.prevMapX) * partialTick;
        double posY = this.prevMapY + (this.mapY - this.prevMapY) * partialTick;
        this.mapRenderer.setView(posX, posY, this.zoomExp, this.zoomStable);
        this.mapRenderer.renderMap(graphics, this.font, null, false, 0, 0, this.width, this.height);
    }
}
