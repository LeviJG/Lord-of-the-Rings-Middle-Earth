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

/** LOTRGuiButtonLeftRight: a long arrow off the widget sheet, its label set in from the point. */
public class LOTRLeftRightButton extends AbstractButton {

    private static final Identifier WIDGETS = Identifier.fromNamespaceAndPath("lotr", "gui/widgets.png");

    private final boolean leftOrRight;
    private final Runnable onPress;

    public LOTRLeftRightButton(boolean leftOrRight, int x, int y, Component label, Runnable onPress) {
        super(x, y, 120, 20, label);
        this.leftOrRight = leftOrRight;
        this.onPress = onPress;
    }

    @Override
    public void onPress(InputWithModifiers input) {
        this.onPress.run();
    }

    @Override
    protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        int k = !this.active ? 0 : isHovered() ? 2 : 1;
        graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS, getX(), getY(), this.leftOrRight ? 0.0F : 136.0F,
                k * 20, this.width, this.height, 256, 256);
        Font font = Minecraft.getInstance().font;
        int colour = !this.active ? -6250336 : isHovered() ? 0xFFFFFFA0 : 0xFFE0E0E0;
        int cx = this.leftOrRight ? getX() + 67 : getX() + this.width - 67;
        graphics.text(font, getMessage(), cx - font.width(getMessage()) / 2, getY() + (this.height - 8) / 2, colour, true);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {
        defaultButtonNarrationText(output);
    }
}
