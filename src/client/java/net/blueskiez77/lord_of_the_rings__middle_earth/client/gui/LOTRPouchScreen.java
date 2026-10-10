package net.blueskiez77.lord_of_the_rings__middle_earth.client.gui;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory.LOTRPouchMenu;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRPouchNetworking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

import org.lwjgl.glfw.GLFW;

/**
 * LOTRGuiPouch: the pouch's rows, its name in a box above them -- typing in it renames the pouch at
 * once, and a blank name takes the custom name off -- and the player's inventory.
 */
public class LOTRPouchScreen extends AbstractContainerScreen<LOTRPouchMenu> {

    public static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("lotr", "gui/pouch.png");
    private static final int TEXT_COLOUR = 0xFF404040;

    private final int pouchRows;
    private EditBox nameField;

    public LOTRPouchScreen(LOTRPouchMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 176, 180);
        this.pouchRows = menu.capacity / 9;
    }

    @Override
    protected void init() {
        super.init();
        this.nameField = addRenderableWidget(new EditBox(this.font, this.leftPos + this.imageWidth / 2 - 80, this.topPos + 7,
                160, 20, Component.empty()));
        this.nameField.setMaxLength(32);
        this.nameField.setValue(this.menu.getDisplayName().getString());
        this.nameField.setResponder(this::renamePouch);
    }

    private void renamePouch(String name) {
        this.menu.renamePouch(name);
        ClientPlayNetworking.send(new LOTRPouchNetworking.RenamePayload(name));
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos, this.topPos, 0.0F, 0.0F,
                this.imageWidth, this.imageHeight, 256, 256);
        for (int l = 0; l < this.pouchRows; ++l) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos + 7, this.topPos + 29 + l * 18, 0.0F, 180.0F,
                    162, 18, 256, 256);
        }
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        graphics.text(this.font, Component.translatable("container.inventory"), 8, this.imageHeight - 96 + 2, TEXT_COLOUR, false);
    }

    /** keyTyped: the name box takes its keys first. */
    @Override
    public boolean keyPressed(KeyEvent event) {
        if (event.key() == GLFW.GLFW_KEY_ESCAPE) {
            return super.keyPressed(event);
        }
        return this.nameField.keyPressed(event) || this.nameField.canConsumeInput() || super.keyPressed(event);
    }
}
