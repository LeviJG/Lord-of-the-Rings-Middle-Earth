package net.blueskiez77.lord_of_the_rings__middle_earth.client.gui;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory.LOTRChestWithPouchMenu;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

/** LOTRGuiChestWithPouch: the chest's rows, the pouch's under them, and the player's inventory. */
public class LOTRChestWithPouchScreen extends AbstractContainerScreen<LOTRChestWithPouchMenu> {

    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("lotr", "gui/pouch_with_chest.png");
    private static final int TEXT_COLOUR = 0xFF404040;

    private final int chestRows;
    private final int pouchRows;

    public LOTRChestWithPouchScreen(LOTRChestWithPouchMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 176, 180 + menu.numChestRows * 18);
        this.chestRows = menu.numChestRows;
        this.pouchRows = menu.numPouchRows;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        int x = this.leftPos;
        int y = this.topPos;
        int chestBottom = y + 17 + this.chestRows * 18;
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0.0F, 0.0F, this.imageWidth, 17 + this.chestRows * 18, 256, 256);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, chestBottom, 0.0F, 125.0F, this.imageWidth, 13, 256, 256);
        for (int l = 0; l < 3; ++l) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, chestBottom + 13 + l * 18, 0.0F, 138.0F, this.imageWidth, 18,
                    256, 256);
        }
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, chestBottom + 67, 0.0F, 156.0F, this.imageWidth, 96, 256, 256);
        for (int l = 0; l < this.pouchRows; ++l) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, LOTRPouchScreen.TEXTURE, x + 7, chestBottom + 13 + l * 18, 0.0F, 180.0F,
                    162, 18, 256, 256);
        }
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        graphics.text(this.font, this.title, 8, 6, TEXT_COLOUR, false);
        graphics.text(this.font, this.minecraft.player.getInventory().getItem(this.menu.pouchSlot).getHoverName(), 8,
                this.imageHeight - 160, TEXT_COLOUR, false);
        graphics.text(this.font, Component.translatable("container.inventory"), 8, this.imageHeight - 93, TEXT_COLOUR, false);
    }
}
