package net.blueskiez77.lord_of_the_rings__middle_earth.client.gui;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory.LOTRCraftingMenu;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

// Mirrors vanilla CraftingScreen, minus the recipe book. CraftingScreen extends AbstractRecipeBookScreen<CraftingMenu> and builds a CraftingRecipeBookComponent(menu), both hard-typed to CraftingMenu, which LOTRCraftingMenu is not -- it extends AbstractCraftingMenu directly because CraftingMenu's constructor hard-calls super(MenuType.CRAFTING, ...). Adding the book back needs a faction RecipeBookComponent.
public class LOTRCraftingScreen extends AbstractContainerScreen<LOTRCraftingMenu> {

    private static final Identifier CRAFTING_TABLE_LOCATION =
            Identifier.withDefaultNamespace("textures/gui/container/crafting_table.png");

    public LOTRCraftingScreen(LOTRCraftingMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = 29;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        int xo = this.leftPos;
        int yo = (this.height - this.imageHeight) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, CRAFTING_TABLE_LOCATION,
                xo, yo, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
    }
}