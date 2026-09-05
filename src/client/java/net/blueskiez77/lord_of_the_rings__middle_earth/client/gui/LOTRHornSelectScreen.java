package net.blueskiez77.lord_of_the_rings__middle_earth.client.gui;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCommandHornItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRHornModePayload;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

/**
 * LOTRGuiHornSelect: what a plain Horn of Command opens.
 *
 * <p>Two buttons, as the original had -- "Halt/Ready Units" and "Summon Units"
 * -- which turn the horn in hand into the order it will carry. Halt/Ready
 * chooses the halt form; blowing it flips to ready and back, so one horn covers
 * both.
 *
 * <p>The squadron field is the other half of LOTRSquadrons: a horn with a name
 * on it speaks only to hired units carrying that same name, and a blank one only
 * to units with no squadron. The original set it from LOTRGuiSquadronItem, a
 * separate screen; folding it in here saves a second dialog for one text field.
 */
public class LOTRHornSelectScreen extends Screen {

    private static final int BUTTON_WIDTH = 160;
    private static final int BUTTON_HEIGHT = 20;

    private final String initialSquadron;
    private EditBox squadronBox;

    public LOTRHornSelectScreen(ItemStack horn) {
        super(Component.translatable("lotr.gui.hornSelect.title"));
        this.initialSquadron = LOTRCommandHornItem.getSquadron(horn);
    }

    @Override
    protected void init() {
        int centreX = this.width / 2;
        int top = this.height / 2 - 50;

        this.squadronBox = new EditBox(this.font, centreX - BUTTON_WIDTH / 2, top,
                BUTTON_WIDTH, BUTTON_HEIGHT, Component.translatable("lotr.gui.hornSelect.squadron"));
        this.squadronBox.setMaxLength(LOTRCommandHornItem.SQUADRON_LENGTH_MAX);
        this.squadronBox.setValue(this.initialSquadron);
        addRenderableWidget(this.squadronBox);

        addRenderableWidget(Button.builder(
                Component.translatable("lotr.gui.hornSelect.haltReady"),
                button -> choose(LOTRCommandHornItem.Mode.HALT))
                .bounds(centreX - BUTTON_WIDTH / 2, top + 35, BUTTON_WIDTH, BUTTON_HEIGHT)
                .build());

        addRenderableWidget(Button.builder(
                Component.translatable("lotr.gui.hornSelect.summon"),
                button -> choose(LOTRCommandHornItem.Mode.SUMMON))
                .bounds(centreX - BUTTON_WIDTH / 2, top + 70, BUTTON_WIDTH, BUTTON_HEIGHT)
                .build());
    }

    private void choose(LOTRCommandHornItem.Mode mode) {
        ClientPlayNetworking.send(
                new LOTRHornModePayload(mode.ordinal(), this.squadronBox.getValue()));
        onClose();
    }

    // As LOTRBeaconScreen does it: extractRenderState draws the widgets, and
    // anything of the screen's own goes at the top of it so it sits underneath.
    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY,
            float partialTick) {
        graphics.text(this.font, this.title,
                this.width / 2 - this.font.width(this.title) / 2,
                this.height / 2 - 70, 0xFFFFFFFF, true);
        graphics.text(this.font,
                Component.translatable("lotr.gui.hornSelect.squadron"),
                this.squadronBox.getX(),
                this.squadronBox.getY() - 4 - this.font.lineHeight, 0xFFFFFFFF, false);
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
