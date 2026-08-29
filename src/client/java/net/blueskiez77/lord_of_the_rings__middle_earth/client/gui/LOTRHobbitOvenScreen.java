package net.blueskiez77.lord_of_the_rings__middle_earth.client.gui;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory.LOTRHobbitOvenMenu;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

// LOTRGuiHobbitOven: oven.png, 176x215, flame at (80, 94), arrow at (80, 40).
public class LOTRHobbitOvenScreen extends AbstractContainerScreen<LOTRHobbitOvenMenu> {

    private static final Identifier OVEN_LOCATION =
            Identifier.fromNamespaceAndPath("lotr", "gui/oven.png");

    public LOTRHobbitOvenScreen(LOTRHobbitOvenMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 176, 215);
        // The original drew "container.inventory" at y 121; imageHeight - 94
        // is 121, so the inherited value is already right.
    }

    @Override
    protected void init() {
        super.init();
        // drawGuiContainerForegroundLayer centred the oven name.
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
        this.titleLabelY = 6;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        int x = this.leftPos;
        int y = this.topPos;

        graphics.blit(RenderPipelines.GUI_TEXTURED, OVEN_LOCATION,
                x, y, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);

        if (this.menu.isCooking()) {
            int k = this.menu.getLitProgress(12);
            graphics.blit(RenderPipelines.GUI_TEXTURED, OVEN_LOCATION,
                    x + 80, y + 94 + 12 - k, 176.0F, (float) (12 - k), 14, k + 2, 256, 256);
        }

        int l = this.menu.getCookProgress(24);
        graphics.blit(RenderPipelines.GUI_TEXTURED, OVEN_LOCATION,
                x + 80, y + 40, 176.0F, 14.0F, 16, l + 1, 256, 256);
    }
}