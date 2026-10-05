package net.blueskiez77.lord_of_the_rings__middle_earth.client.gui;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRHiredNetworking;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCommandHornItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRHiredPayloads;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

/**
 * LOTRGuiSquadronItem: at a table of command, the company the command sword
 * or horn in hand speaks to. It is sent as the screen closes.
 */
public class LOTRSquadronItemScreen extends Screen {

    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("lotr", "gui/squadron_item.png");
    private static final int TEXT_COLOUR = 0xFF404040;
    private static final int X_SIZE = 200;
    private static final int Y_SIZE = 120;

    private int guiLeft;
    private int guiTop;
    private ItemStack theItem = ItemStack.EMPTY;
    private EditBox squadronNameField;

    public LOTRSquadronItemScreen() {
        super(Component.translatable("lotr.gui.squadronItem.squadron"));
    }

    @Override
    protected void init() {
        this.guiLeft = (this.width - X_SIZE) / 2;
        this.guiTop = (this.height - Y_SIZE) / 2;
        addRenderableWidget(Button.builder(Component.translatable("lotr.gui.squadronItem.done"), b -> onClose())
                .bounds(this.guiLeft + X_SIZE / 2 - 40, this.guiTop + 85, 80, 20).build());
        this.squadronNameField = new EditBox(this.font, this.guiLeft + X_SIZE / 2 - 80, this.guiTop + 50, 160, 20,
                Component.translatable("lotr.gui.squadronItem.squadron"));
        this.squadronNameField.setMaxLength(LOTRCommandHornItem.SQUADRON_LENGTH_MAX);
        this.squadronNameField.setHint(Component.translatable("lotr.gui.squadronItem.none"));
        ItemStack held = this.minecraft.player.getMainHandItem();
        if (LOTRHiredNetworking.isSquadronItem(held)) {
            this.theItem = held;
            this.squadronNameField.setValue(LOTRCommandHornItem.getSquadron(held));
        }
        addRenderableWidget(this.squadronNameField);
    }

    @Override
    public void tick() {
        super.tick();
        ItemStack held = this.minecraft.player.getMainHandItem();
        if (!LOTRHiredNetworking.isSquadronItem(held)) {
            onClose();
        } else {
            this.theItem = held;
        }
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.guiLeft, this.guiTop, 0.0F, 0.0F, X_SIZE, Y_SIZE, 256, 256);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        Component name = this.theItem.getHoverName();
        graphics.text(this.font, name, this.guiLeft + X_SIZE / 2 - this.font.width(name) / 2, this.guiTop + 11,
                TEXT_COLOUR, false);
        graphics.text(this.font, Component.translatable("lotr.gui.squadronItem.squadron"), this.squadronNameField.getX(),
                this.squadronNameField.getY() - this.font.lineHeight - 3, TEXT_COLOUR, false);
    }

    @Override
    public void removed() {
        super.removed();
        ClientPlayNetworking.send(new LOTRHiredPayloads.ItemSquadron(this.squadronNameField.getValue()));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
