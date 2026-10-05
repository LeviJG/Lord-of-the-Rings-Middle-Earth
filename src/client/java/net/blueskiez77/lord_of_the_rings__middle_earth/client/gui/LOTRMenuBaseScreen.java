package net.blueskiez77.lord_of_the_rings__middle_earth.client.gui;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.LOTRKeyBindings;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;

/**
 * LOTRGuiMenuBase: a screen opened from the LOTR menu -- 200x256 in the
 * middle, with a Menu arrow back on its left; the menu key goes back too.
 */
public abstract class LOTRMenuBaseScreen extends Screen {

    protected int xSize = 200;
    protected int ySize = 256;
    /** buttonMenuReturn; the factions screen of another player has none. */
    protected LOTRLeftRightButton buttonMenuReturn;
    protected int guiLeft;
    protected int guiTop;

    protected LOTRMenuBaseScreen(Component title) {
        super(title);
    }

    @Override
    protected void init() {
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;
        int buttonH = 20;
        int buttonGap = 35;
        int minGap = 10;
        this.buttonMenuReturn = new LOTRLeftRightButton(true, 0, this.guiTop + (this.ySize + buttonH) / 4,
                Component.translatable("lotr.gui.menuButton"),
                () -> this.minecraft.setScreenAndShow(new LOTRMenuScreen()));
        this.buttonMenuReturn.setX(Math.min(buttonGap, this.guiLeft - minGap - this.buttonMenuReturn.getWidth()));
        addRenderableWidget(this.buttonMenuReturn);
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (LOTRKeyBindings.menu.matches(event)) {
            this.minecraft.setScreenAndShow(new LOTRMenuScreen());
            return true;
        }
        return closeOnInventoryKey(this, event) || super.keyPressed(event);
    }

    /** LOTRGuiScreenBase.keyTyped: the inventory key closes it, as Escape does. */
    static boolean closeOnInventoryKey(Screen screen, KeyEvent event) {
        if (net.minecraft.client.Minecraft.getInstance().options.keyInventory.matches(event)) {
            screen.onClose();
            return true;
        }
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.minecraft.player.isAlive()) {
            onClose();
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
