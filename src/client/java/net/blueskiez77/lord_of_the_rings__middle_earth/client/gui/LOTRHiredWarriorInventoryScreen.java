package net.blueskiez77.lord_of_the_rings__middle_earth.client.gui;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory.LOTRHiredWarriorInventoryMenu;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

/** LOTRGuiHiredWarriorInventory: armour and weapon; a bombardier's bomb slot framed like the weapon's. */
public class LOTRHiredWarriorInventoryScreen extends AbstractContainerScreen<LOTRHiredWarriorInventoryMenu> {

    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("lotr", "gui/npc/hired_warrior.png");
    private static final int TEXT_COLOUR = 0xFF404040;

    public LOTRHiredWarriorInventoryScreen(LOTRHiredWarriorInventoryMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 176, 188);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos, this.topPos, 0.0F, 0.0F,
                this.imageWidth, this.imageHeight, 256, 256);
        if (this.menu.isBombardier()) {
            Slot slotMelee = this.menu.getSlot(4);
            Slot slotBomb = this.menu.getSlot(5);
            graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos + slotBomb.x - 1, this.topPos + slotBomb.y - 1,
                    slotMelee.x - 1, slotMelee.y - 1, 18, 18, 256, 256);
        }
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        Component s = Component.translatable("lotr.gui.warrior.openInv");
        graphics.text(this.font, s, this.imageWidth / 2 - this.font.width(s) / 2, 6, TEXT_COLOUR, false);
        graphics.text(this.font, Component.translatable("container.inventory"), 8, 95, TEXT_COLOUR, false);
    }
}
