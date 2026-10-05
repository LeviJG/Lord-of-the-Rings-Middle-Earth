package net.blueskiez77.lord_of_the_rings__middle_earth.client.gui;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory.LOTRCoinExchangeMenu;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

/**
 * LOTRGuiCoinExchange: the coins in the middle, an arrow button either side
 * to change them, and a frame round the side chosen until it is taken.
 */
public class LOTRCoinExchangeScreen extends AbstractContainerScreen<LOTRCoinExchangeMenu> {

    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("lotr", "gui/coin_exchange.png");
    private static final int TEXT_COLOUR = 0xFF404040;

    private ExchangeButton buttonLeft;
    private ExchangeButton buttonRight;

    public LOTRCoinExchangeScreen(LOTRCoinExchangeMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 176, 188);
    }

    @Override
    protected void init() {
        super.init();
        int i = this.leftPos + this.imageWidth / 2;
        int j = 28;
        int k = 16;
        buttonLeft = addRenderableWidget(new ExchangeButton(0, i - j - k, this.topPos + 45));
        buttonRight = addRenderableWidget(new ExchangeButton(1, i + j - k, this.topPos + 45));
        updateButtons();
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        updateButtons();
    }

    private void updateButtons() {
        buttonLeft.active = !this.menu.isExchanged() && !this.menu.exchangeInv.getItem(0).isEmpty();
        buttonRight.active = !this.menu.isExchanged() && !this.menu.exchangeInv.getItem(1).isEmpty();
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos, this.topPos, 0.0F, 0.0F,
                this.imageWidth, this.imageHeight, 256, 256);
        if (this.menu.isExchanged()) {
            for (int l = 1; l <= 2; ++l) {
                Slot slot = this.menu.getSlot(l);
                if (slot.hasItem()) {
                    graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos + slot.x - 5,
                            this.topPos + slot.y - 5, 176.0F, 51.0F, 26, 26, 256, 256);
                }
            }
        }
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        Component title = Component.translatable("container.lotr.coinExchange");
        graphics.text(this.font, title, 89 - this.font.width(title) / 2, 11, TEXT_COLOUR, false);
        graphics.text(this.font, Component.translatable("container.inventory"), 8, 94, TEXT_COLOUR, false);
    }

    /** LOTRGuiButtonCoinExchange: a 32x17 arrow off the sheet, by side and hover state. */
    private final class ExchangeButton extends AbstractButton {
        private final int id;

        ExchangeButton(int id, int x, int y) {
            super(x, y, 32, 17, Component.empty());
            this.id = id;
        }

        @Override
        public void onPress(InputWithModifiers input) {
            LOTRCoinExchangeScreen.this.minecraft.gameMode.handleInventoryButtonClick(
                    LOTRCoinExchangeScreen.this.menu.containerId, this.id);
        }

        @Override
        protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
            int k = !this.active ? 0 : isHovered() ? 2 : 1;
            graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, getX(), getY(),
                    176 + this.id * this.width, k * this.height, this.width, this.height, 256, 256);
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput output) {
            defaultButtonNarrationText(output);
        }
    }
}
