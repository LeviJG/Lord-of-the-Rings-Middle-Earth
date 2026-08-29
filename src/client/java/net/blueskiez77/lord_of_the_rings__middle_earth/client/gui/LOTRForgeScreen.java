package net.blueskiez77.lord_of_the_rings__middle_earth.client.gui;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory.LOTRForgeMenu;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

// The forge GUI: forge.png, 176x233, four lanes.
//
// Deliberately AbstractContainerScreen, not AbstractFurnaceScreen -- the latter
// brings a recipe book component typed to the vanilla furnace menu. The forge
// has no recipe book, matching the original.
public class LOTRForgeScreen extends AbstractContainerScreen<LOTRForgeMenu> {

    // The original ships this at assets/lotr/gui/forge.png -- note "gui/",
    // not "textures/gui/".
    private static final Identifier FORGE_LOCATION =
            Identifier.fromNamespaceAndPath("lotr", "gui/forge.png");

    public LOTRForgeScreen(LOTRForgeMenu menu, Inventory inventory, Component title) {
        // imageWidth/imageHeight are final and set only by this constructor:
        // 176 is vanilla's default, 233 is LOTRGuiAlloyForge's ySize. It also
        // derives inventoryLabelY = imageHeight - 94 = 139, which is exactly
        // the original's drawString y, so that needs no override.
        super(menu, inventory, title, 176, 233);
    }

    @Override
    protected void init() {
        super.init();
        // The original centred the forge name.
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
        this.titleLabelY = 6;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        int x = this.leftPos;
        int y = this.topPos;

        graphics.blit(RenderPipelines.GUI_TEXTURED, FORGE_LOCATION,
                x, y, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);

        // Flame, burning down. Sprite at u=176, v=0, 14 wide.
        if (this.menu.isLit()) {
            int k = this.menu.getLitProgress(12);
            graphics.blit(RenderPipelines.GUI_TEXTURED, FORGE_LOCATION,
                    x + 80, y + 112 + 12 - k, 176.0F, (float) (12 - k), 14, k + 2, 256, 256);
        }

        // Smelt arrow, filling upward. Sprite at u=176, v=14, 16 wide.
        int l = this.menu.getSmeltProgress(24);
        graphics.blit(RenderPipelines.GUI_TEXTURED, FORGE_LOCATION,
                x + 80, y + 58, 176.0F, 14.0F, 16, l + 1, 256, 256);
    }
}