package net.blueskiez77.lord_of_the_rings__middle_earth.client.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRGuiMessageTypes;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;

/**
 * LOTRGuiMessage: a one-time explanation on a parchment, which no key will
 * close; its Dismiss button wakes after a moment. The alignment-drain one
 * shows the drain icon it describes.
 */
public class LOTRMessageScreen extends Screen {

    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("lotr", "gui/message.png");
    private static final Identifier ALIGNMENT = Identifier.fromNamespaceAndPath("lotr", "gui/alignment.png");
    private static final int X_SIZE = 240;
    private static final int Y_SIZE = 160;
    private static final int BORDER = 10;

    private final LOTRGuiMessageTypes type;
    private int guiLeft;
    private int guiTop;
    private LOTRRedBookButton buttonDismiss;
    /** buttonTimer: counted down a frame at a time, as the original drew it. */
    private int buttonTimer = 60;

    public LOTRMessageScreen(LOTRGuiMessageTypes type) {
        super(type.getMessage());
        this.type = type;
    }

    @Override
    protected void init() {
        this.guiLeft = (this.width - X_SIZE) / 2;
        this.guiTop = (this.height - Y_SIZE) / 2;
        this.buttonDismiss = addRenderableWidget(new LOTRRedBookButton(this.guiLeft + X_SIZE / 2 - 40, this.guiTop + Y_SIZE + 20,
                80, 20, Component.translatable("lotr.gui.message.dismiss"), this::onClose));
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.guiLeft, this.guiTop, 0.0F, 0.0F, X_SIZE, Y_SIZE, 256, 256);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        if (this.buttonTimer > 0) {
            --this.buttonTimer;
        }
        this.buttonDismiss.active = this.buttonTimer == 0;
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        int pageWidth = X_SIZE - BORDER * 2;
        List<FormattedCharSequence> msgLines = new ArrayList<>();
        for (String line : this.type.getMessage().getString().split(Pattern.quote("\\n"))) {
            msgLines.addAll(this.font.split(Component.literal(line), pageWidth));
        }
        int x = this.guiLeft + BORDER;
        int y = this.guiTop + BORDER;
        for (FormattedCharSequence line : msgLines) {
            graphics.text(this.font, line, x, y, 0xFF7A5D43, false);
            y += this.font.lineHeight;
        }
        Component s = Component.translatable("lotr.gui.message.notDisplayedAgain");
        graphics.text(this.font, s, this.guiLeft + X_SIZE / 2 - this.font.width(s) / 2,
                this.guiTop + Y_SIZE - BORDER / 2 - this.font.lineHeight, 0xFF938169, false);
        if (this.type == LOTRGuiMessageTypes.ALIGN_DRAIN) {
            int numIcons = 3;
            int iconGap = 40;
            for (int l = 0; l < numIcons; ++l) {
                int iconX = this.guiLeft + X_SIZE / 2 - (numIcons - 1) * iconGap / 2;
                int iconY = this.guiTop + BORDER + 14;
                renderAlignmentDrain(graphics, iconX + (l * iconGap - 8), iconY, l + 1);
            }
        }
    }

    /** LOTRTickHandlerClient.renderAlignmentDrain: the drain icon, "-n" bordered on it. */
    private void renderAlignmentDrain(GuiGraphicsExtractor graphics, int x, int y, int numFactions) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, ALIGNMENT, x, y, 0.0F, 128.0F, 16, 16, 256, 256);
        String s = "-" + numFactions;
        int tx = x + 8 - this.font.width(s) / 2;
        int ty = y + 8 - this.font.lineHeight / 2;
        for (int[] d : new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}}) {
            graphics.text(this.font, s, tx + d[0], ty + d[1], 0xFF000000, false);
        }
        graphics.text(this.font, s, tx, ty, 0xFFFFFFFF, false);
    }

    /** keyTyped did nothing: only Dismiss closes it. */
    @Override
    public boolean keyPressed(KeyEvent event) {
        return true;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
