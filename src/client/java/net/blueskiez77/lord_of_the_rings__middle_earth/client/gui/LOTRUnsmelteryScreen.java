package net.blueskiez77.lord_of_the_rings__middle_earth.client.gui;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory.LOTRUnsmelteryMenu;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

// LOTRGuiUnsmeltery: the standard 176x166 furnace frame, flame at 56,36 and
// the smelt arrow at 79,34. AbstractContainerScreen rather than
// AbstractFurnaceScreen for the same reason as the forge -- no recipe book.
public class LOTRUnsmelteryScreen extends AbstractContainerScreen<LOTRUnsmelteryMenu> {

    // Shipped at assets/lotr/gui/, not assets/lotr/textures/gui/, matching the
    // other screens in this port. The original file is named unsmelter.png; it
    // is unsmeltery.png here so it matches the block.
    private static final Identifier UNSMELTERY_LOCATION =
            Identifier.fromNamespaceAndPath("lotr", "gui/unsmeltery.png");

    public LOTRUnsmelteryScreen(LOTRUnsmelteryMenu menu, Inventory inventory, Component title) {
        // LOTRGuiUnsmeltery set ySize = 176, but its inventory label sits at
        // y 72 and its player slots at y 84 -- a 166-tall frame. 166 gives
        // inventoryLabelY = 166 - 94 = 72, which is exactly the original.
        super(menu, inventory, title, 176, 166);
    }

    @Override
    protected void init() {
        super.init();
        // drawGuiContainerForegroundLayer centred the name.
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
        this.titleLabelY = 6;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        int x = this.leftPos;
        int y = this.topPos;

        graphics.blit(RenderPipelines.GUI_TEXTURED, UNSMELTERY_LOCATION,
                x, y, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);

        // Flame, burning down. Sprite at u=176, v=0, 14 wide.
        if (this.menu.isLit()) {
            int k = this.menu.getLitProgress(13);
            graphics.blit(RenderPipelines.GUI_TEXTURED, UNSMELTERY_LOCATION,
                    x + 56, y + 36 + 12 - k, 176.0F, (float) (12 - k), 14, k + 1, 256, 256);
        }

        // Smelt arrow, filling rightward. Sprite at u=176, v=14, 16 tall.
        int l = this.menu.getSmeltProgress(24);
        graphics.blit(RenderPipelines.GUI_TEXTURED, UNSMELTERY_LOCATION,
                x + 79, y + 34, 176.0F, 14.0F, l + 1, 16, 256, 256);
    }
}
