package net.blueskiez77.lord_of_the_rings__middle_earth.client.gui;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory.LOTRNPCMountInventoryMenu;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

/**
 * LOTRGuiNPCMountInventory: the horse's screen for a warg -- its saddle and
 * barding slots, and the mount itself following the mouse.
 */
public class LOTRNPCMountInventoryScreen extends AbstractContainerScreen<LOTRNPCMountInventoryMenu> {

    /** The vanilla horse screen the original drew on. */
    private static final Identifier TEXTURE = Identifier.withDefaultNamespace("textures/gui/container/horse.png");
    private static final Identifier SLOT = Identifier.withDefaultNamespace("container/slot");

    public LOTRNPCMountInventoryScreen(LOTRNPCMountInventoryMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 176, 166);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos, this.topPos, 0.0F, 0.0F,
                this.imageWidth, this.imageHeight, 256, 256);
        for (int i = 0; i < 2; ++i) {
            Slot slot = this.menu.getSlot(i);
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SLOT, this.leftPos + slot.x - 1, this.topPos + slot.y - 1, 18, 18);
        }
        if (this.menu.theMount != null) {
            InventoryScreen.extractEntityInInventoryFollowsMouse(graphics, this.leftPos + 26, this.topPos + 18,
                    this.leftPos + 78, this.topPos + 70, 17, 0.25f, mouseX, mouseY, this.menu.theMount);
        }
    }
}
