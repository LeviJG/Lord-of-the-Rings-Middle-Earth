package net.blueskiez77.lord_of_the_rings__middle_earth.client.gui;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory.LOTRMillstoneMenu;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

// LOTRGuiMillstone: a 176x182 frame, taller than a furnace's because the two
// slots are stacked rather than side by side. The one moving part is a 14px
// column at 85,47 that fills DOWNWARD as the grind progresses -- meal falling
// from the upper stone to the lower.
public class LOTRMillstoneScreen extends AbstractContainerScreen<LOTRMillstoneMenu> {

    // Shipped at assets/lotr/gui/, not assets/lotr/textures/gui/, matching the
    // other screens in this port.
    private static final Identifier MILLSTONE_LOCATION =
            Identifier.fromNamespaceAndPath("lotr", "gui/millstone.png");

    public LOTRMillstoneScreen(LOTRMillstoneMenu menu, Inventory inventory, Component title) {
        // ySize = 182 in the original, and its "Inventory" label sits at y 88.
        super(menu, inventory, title, 176, 182);
        this.inventoryLabelY = 88;
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

        graphics.blit(RenderPipelines.GUI_TEXTURED, MILLSTONE_LOCATION,
                x, y, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);

        // The chute, growing downward from its top edge. Sprite at u=176, v=0.
        if (this.menu.isMilling()) {
            int k = this.menu.getMillProgress(14);
            graphics.blit(RenderPipelines.GUI_TEXTURED, MILLSTONE_LOCATION,
                    x + 85, y + 47, 176.0F, 0.0F, 14, k, 256, 256);
        }
    }
}
