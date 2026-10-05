package net.blueskiez77.lord_of_the_rings__middle_earth.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

/** LOTRGuiButtonRedBook: the red book's button strip, its label in brown. */
public class LOTRRedBookButton extends AbstractButton {

    private static final Identifier RED_BOOK = Identifier.fromNamespaceAndPath("lotr", "gui/quest/red_book.png");

    private final Runnable onPress;

    public LOTRRedBookButton(int x, int y, int w, int h, Component label, Runnable onPress) {
        super(x, y, w, h, label);
        this.onPress = onPress;
    }

    @Override
    public void onPress(InputWithModifiers input) {
        this.onPress.run();
    }

    @Override
    protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        int k = !this.active ? 0 : isHovered() ? 2 : 1;
        graphics.blit(RenderPipelines.GUI_TEXTURED, RED_BOOK, getX(), getY(), 170.0F, 256 + k * 20,
                this.width, this.height, 512, 512);
        graphics.blit(RenderPipelines.GUI_TEXTURED, RED_BOOK, getX(), getY(), 170.0F, 316.0F,
                this.width / 2, this.height, 512, 512);
        graphics.blit(RenderPipelines.GUI_TEXTURED, RED_BOOK, getX() + this.width / 2, getY(),
                370 - (float) this.width / 2, 316.0F, this.width / 2, this.height, 512, 512);
        Font font = Minecraft.getInstance().font;
        int colour = this.active ? 0xFF7A5D43 : 0xFF543F2E;
        graphics.text(font, getMessage(), getX() + this.width / 2 - font.width(getMessage()) / 2,
                getY() + (this.height - 8) / 2, colour, false);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {
        defaultButtonNarrationText(output);
    }
}
