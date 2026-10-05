package net.blueskiez77.lord_of_the_rings__middle_earth.client.gui;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory.LOTRGollumMenu;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

/** LOTRGuiGollum: his name over his pack, and the player's inventory. */
public class LOTRGollumScreen extends AbstractContainerScreen<LOTRGollumMenu> {

    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("lotr", "gui/npc/gollum.png");
    private static final int TEXT_COLOUR = 0xFF404040;

    public LOTRGollumScreen(LOTRGollumMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 176, 168);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos, this.topPos, 0.0F, 0.0F,
                this.imageWidth, this.imageHeight, 256, 256);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        graphics.text(this.font, this.title, this.imageWidth / 2 - this.font.width(this.title) / 2, 6, TEXT_COLOUR, false);
        graphics.text(this.font, Component.translatable("container.inventory"), 8, 74, TEXT_COLOUR, false);
    }
}
