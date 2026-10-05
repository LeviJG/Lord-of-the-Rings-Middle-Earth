package net.blueskiez77.lord_of_the_rings__middle_earth.client.gui;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory.LOTRHiredFarmerInventoryMenu;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

/**
 * LOTRGuiHiredFarmerInventory: seeds, harvest and bone meal, the empty seed
 * and bone-meal slots marked; and a warning beside it while only one seed is
 * left, which the farmer keeps back.
 */
public class LOTRHiredFarmerInventoryScreen extends AbstractContainerScreen<LOTRHiredFarmerInventoryMenu> {

    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("lotr", "gui/npc/hired_farmer.png");
    private static final int TEXT_COLOUR = 0xFF404040;

    public LOTRHiredFarmerInventoryScreen(LOTRHiredFarmerInventoryMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 176, 161);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos, this.topPos, 0.0F, 0.0F,
                this.imageWidth, this.imageHeight, 256, 256);
        if (this.menu.getSlot(0).getItem().isEmpty()) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos + 80, this.topPos + 21, 176.0F, 0.0F,
                    16, 16, 256, 256);
        }
        if (this.menu.getSlot(3).getItem().isEmpty()) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos + 123, this.topPos + 34, 176.0F, 16.0F,
                    16, 16, 256, 256);
        }
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        if (this.menu.theNPC != null) {
            Component s = Component.literal(this.menu.theNPC.getNPCName());
            graphics.text(this.font, s, this.imageWidth / 2 - this.font.width(s) / 2, 6, TEXT_COLOUR, false);
        }
        graphics.text(this.font, Component.translatable("container.inventory"), 8, 67, TEXT_COLOUR, false);
        ItemStack seeds = this.menu.getSlot(0).getItem();
        if (!seeds.isEmpty() && seeds.getCount() == 1) {
            int y = 20;
            for (FormattedCharSequence line : this.font.split(
                    Component.translatable("lotr.gui.farmer.oneSeed").withStyle(ChatFormatting.RED), 120)) {
                graphics.text(this.font, line, this.imageWidth + 10, y, 0xFFFFFFFF, false);
                y += this.font.lineHeight;
            }
        }
    }
}
