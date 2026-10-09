package net.blueskiez77.lord_of_the_rings__middle_earth.client.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.quest.LOTRClientMiniQuests;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRDimension;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.quest.LOTRMiniQuestWelcome;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import org.jspecify.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

/**
 * LOTRGuiMenu: two rows of icons -- achievements, map, factions, "?",
 * fellowships, titles, shields, options -- each with a key of its own. The
 * menu key reopens whichever of them was last opened from here.
 *
 * <p>NOT ported yet, so their icons are greyed out: the achievements (D7)
 * and map (D13) screens; and
 * openMenu's welcome mini-quest forcing the map or factions (D14).
 */
public class LOTRMenuScreen extends Screen {

    public static final Identifier MENU_ICONS = Identifier.fromNamespaceAndPath("lotr", "gui/menu_icons.png");

    /** lastMenuScreen. */
    private static @Nullable Supplier<Screen> lastMenuScreen;

    public LOTRMenuScreen() {
        super(Component.translatable("lotr.gui.menu", LOTRDimension.MIDDLE_EARTH.getDimensionName()));
    }

    /**
     * openMenu: the factions screen while the Grey Wanderer's welcome asks for it (the map, which it
     * asks for first, with the map, D13); else the last menu screen.
     */
    public static Screen openMenu() {
        if (LOTRMiniQuestWelcome.forceMenuMapFactions(LOTRClientMiniQuests.getActiveMiniQuests())[1]) {
            return new LOTRFactionsScreen();
        }
        return lastMenuScreen != null ? lastMenuScreen.get() : new LOTRMenuScreen();
    }

    public static void resetLastMenuScreen() {
        lastMenuScreen = null;
    }

    @Override
    protected void init() {
        resetLastMenuScreen();
        int midX = this.width / 2;
        int midY = this.height / 2;
        int buttonGap = 10;
        int buttonSize = 32;
        List<MenuButton> buttons = new ArrayList<>();
        buttons.add(new MenuButton(2, null, "lotr.gui.achievements", GLFW.GLFW_KEY_A, false));
        buttons.add(new MenuButton(3, null, "lotr.gui.map", GLFW.GLFW_KEY_M, false));
        buttons.add(new MenuButton(4, LOTRFactionsScreen::new, "lotr.gui.factions", GLFW.GLFW_KEY_F, true));
        buttons.add(new MenuButton(0, null, null, -1, true));
        buttons.add(new MenuButton(6, LOTRFellowshipsScreen::new, "lotr.gui.fellowships", GLFW.GLFW_KEY_P, true));
        buttons.add(new MenuButton(7, LOTRTitlesScreen::new, "lotr.gui.titles", GLFW.GLFW_KEY_T, true));
        buttons.add(new MenuButton(5, LOTRShieldsScreen::new, "lotr.gui.shields", GLFW.GLFW_KEY_S, true));
        buttons.add(new MenuButton(1, LOTROptionsScreen::new, "lotr.gui.options", GLFW.GLFW_KEY_O, true));
        int numButtons = buttons.size();
        int numTopRowButtons = (numButtons - 1) / 2 + 1;
        int numBtmRowButtons = numButtons - numTopRowButtons;
        int topRowLeft = midX - (numTopRowButtons * buttonSize + (numTopRowButtons - 1) * buttonGap) / 2;
        int btmRowLeft = midX - (numBtmRowButtons * buttonSize + (numBtmRowButtons - 1) * buttonGap) / 2;
        for (int l = 0; l < numButtons; ++l) {
            MenuButton button = buttons.get(l);
            if (l < numTopRowButtons) {
                button.setPosition(topRowLeft + l * (buttonSize + buttonGap), midY - buttonGap / 2 - buttonSize);
            } else {
                button.setPosition(btmRowLeft + (l - numTopRowButtons) * (buttonSize + buttonGap), midY + buttonGap / 2);
            }
            addRenderableWidget(button);
        }
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        graphics.text(this.font, this.title, this.width / 2 - this.font.width(this.title) / 2, this.height / 2 - 80,
                0xFFFFFFFF, true);
        for (var child : children()) {
            if (child instanceof MenuButton button && button.isHovered() && button.label != null) {
                graphics.setTooltipForNextFrame(this.font, button.label, mouseX, mouseY);
            }
        }
    }

    /** keyTyped: a button's own key opens it. */
    @Override
    public boolean keyPressed(KeyEvent event) {
        for (var child : children()) {
            if (child instanceof MenuButton button && button.visible && button.active && button.menuKeyCode >= 0
                    && event.key() == button.menuKeyCode) {
                button.onPress(event);
                return true;
            }
        }
        return LOTRMenuBaseScreen.closeOnInventoryKey(this, event) || super.keyPressed(event);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    /** LOTRGuiButtonMenu: a 32x32 icon off the sheet, its row the button's id. */
    private final class MenuButton extends AbstractButton {
        private final int id;
        private final @Nullable Supplier<Screen> menuScreen;
        private final @Nullable Component label;
        private final int menuKeyCode;

        MenuButton(int id, @Nullable Supplier<Screen> menuScreen, @Nullable String labelKey, int key, boolean enabled) {
            super(0, 0, 32, 32, labelKey == null ? Component.literal("?") : Component.translatable(labelKey));
            this.id = id;
            this.menuScreen = menuScreen;
            this.label = labelKey == null ? Component.literal("?") : Component.translatable(labelKey);
            this.menuKeyCode = key;
            this.active = enabled;
        }

        @Override
        public void onPress(InputWithModifiers input) {
            if (this.menuScreen != null) {
                LOTRMenuScreen.this.minecraft.setScreenAndShow(this.menuScreen.get());
                lastMenuScreen = this.menuScreen;
            }
        }

        @Override
        protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
            int u = (this.active ? 0 : this.width * 2) + (isHovered() ? this.width : 0);
            graphics.blit(RenderPipelines.GUI_TEXTURED, MENU_ICONS, getX(), getY(), u, this.id * this.height,
                    this.width, this.height, 256, 256);
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput output) {
            defaultButtonNarrationText(output);
        }
    }
}
