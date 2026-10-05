package net.blueskiez77.lord_of_the_rings__middle_earth.client.gui;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * LOTRGuiNPCInteract: the NPC's name over a row of choices. It closes if the
 * NPC dies or the player wanders more than ten blocks off.
 */
public abstract class LOTRNPCInteractScreen extends Screen {

    protected final LOTRNPCEntity theEntity;

    protected LOTRNPCInteractScreen(LOTRNPCEntity entity) {
        super(entity.getDisplayName());
        this.theEntity = entity;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        Component name = this.theEntity.getDisplayName();
        graphics.text(this.font, name, (this.width - this.font.width(name)) / 2, this.height / 5 * 3 - 20,
                0xFFFFFFFF, true);
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.theEntity.isAlive() || this.theEntity.distanceToSqr(this.minecraft.player) > 100.0) {
            onClose();
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
