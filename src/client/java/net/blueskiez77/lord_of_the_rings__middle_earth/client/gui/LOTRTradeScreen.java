package net.blueskiez77.lord_of_the_rings__middle_earth.client.gui;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade.LOTRTradeEntry;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory.LOTRTradeMenu;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

/**
 * LOTRGuiTrade: the trader's name, its two rows of trades with a price under
 * each (a locked trade shaded over, "..." under it, and one filling up shaded
 * part way), the offers row, and the sell button with the offers' worth.
 */
public class LOTRTradeScreen extends AbstractContainerScreen<LOTRTradeMenu> {

    public static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("lotr", "gui/npc/trade.png");
    private static final int TEXT_COLOUR = 0xFF404040;
    /** lockedTradeColor. */
    private static final int LOCKED_TRADE_COLOUR = -1610612736;

    private TradeButton buttonSell;

    public LOTRTradeScreen(LOTRTradeMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 176, 270);
    }

    @Override
    protected void init() {
        super.init();
        buttonSell = addRenderableWidget(new TradeButton(this.leftPos + 79, this.topPos + 164));
        buttonSell.active = false;
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        buttonSell.active = this.menu.totalSellPrice() > 0;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos, this.topPos, 0.0F, 0.0F,
                this.imageWidth, this.imageHeight, 512, 512);
        // A trade filling up towards its lock is shaded part way, behind its item.
        for (int i = 0; i < 18; ++i) {
            LOTRTradeMenu.TradeSlot slot = (LOTRTradeMenu.TradeSlot) this.menu.getSlot(i);
            LOTRTradeEntry trade = slot.getTrade();
            int lockedPixels;
            if (trade != null && trade.isAvailable() && (lockedPixels = trade.getLockedProgressForSlot()) > 0) {
                int x = this.leftPos + slot.x;
                int y = this.topPos + slot.y;
                graphics.fill(x, y, x + lockedPixels, y + 16, LOCKED_TRADE_COLOUR);
            }
        }
    }

    private void centred(GuiGraphicsExtractor graphics, String s, int x, int y) {
        graphics.text(this.font, s, x - this.font.width(s) / 2, y, TEXT_COLOUR, false);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        if (this.menu.theTraderNPC != null) {
            centred(graphics, this.menu.theTraderNPC.getNPCName(), 89, 11);
        }
        graphics.text(this.font, Component.translatable("container.lotr.trade.buy"), 8, 28, TEXT_COLOUR, false);
        graphics.text(this.font, Component.translatable("container.lotr.trade.sell"), 8, 79, TEXT_COLOUR, false);
        graphics.text(this.font, Component.translatable("container.lotr.trade.sellOffer"), 8, 129, TEXT_COLOUR, false);
        graphics.text(this.font, Component.translatable("container.inventory"), 8, 176, TEXT_COLOUR, false);
        for (int i = 0; i < 18; ++i) {
            renderTradeSlot(graphics, (LOTRTradeMenu.TradeSlot) this.menu.getSlot(i));
        }
        int totalSellPrice = this.menu.totalSellPrice();
        if (totalSellPrice > 0) {
            graphics.text(this.font, Component.translatable("container.lotr.trade.sellPrice", totalSellPrice),
                    100, 169, TEXT_COLOUR, false);
        }
    }

    /** renderCost: a price too wide for the slot is written at half size. */
    private void renderCost(GuiGraphicsExtractor graphics, String s, int x, int y) {
        boolean halfSize = this.font.width(s) > 15;
        if (halfSize) {
            graphics.pose().pushMatrix();
            graphics.pose().scale(0.5f, 0.5f);
            x *= 2;
            y *= 2;
            y += this.font.lineHeight / 2;
        }
        centred(graphics, s, x, y);
        if (halfSize) {
            graphics.pose().popMatrix();
        }
    }

    private void renderTradeSlot(GuiGraphicsExtractor graphics, LOTRTradeMenu.TradeSlot slot) {
        LOTRTradeEntry trade = slot.getTrade();
        if (trade == null) {
            return;
        }
        // A locked trade is shaded over in front of its item.
        if (!trade.isAvailable()) {
            graphics.fill(slot.x, slot.y, slot.x + 16, slot.y + 16, LOCKED_TRADE_COLOUR);
        }
        if (trade.isAvailable()) {
            int cost = slot.cost();
            if (cost > 0) {
                renderCost(graphics, Integer.toString(cost), slot.x + 8, slot.y + 22);
            }
        } else {
            centred(graphics, Component.translatable("container.lotr.trade.locked").getString(), slot.x + 8, slot.y + 22);
        }
    }

    /** LOTRGuiTradeButton: an 18x18 icon off the trade sheet, by hover state. */
    private final class TradeButton extends AbstractButton {
        TradeButton(int x, int y) {
            super(x, y, 18, 18, Component.literal("Trade"));
        }

        @Override
        public void onPress(InputWithModifiers input) {
            LOTRTradeScreen.this.minecraft.gameMode.handleInventoryButtonClick(
                    LOTRTradeScreen.this.menu.containerId, LOTRTradeMenu.SELL_BUTTON);
        }

        @Override
        protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
            int hoverState = !this.active ? 0 : isHovered() ? 2 : 1;
            graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, getX(), getY(),
                    176.0F, hoverState * 18, 18, 18, 512, 512);
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput output) {
            defaultButtonNarrationText(output);
        }
    }
}
