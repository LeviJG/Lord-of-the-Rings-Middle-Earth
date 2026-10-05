package net.blueskiez77.lord_of_the_rings__middle_earth.client.gui;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRHiredPayloads;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;

/**
 * LOTRGuiHiredDismiss: the warning, and a note if a mount or rider of the
 * player's goes too; Dismiss, or Cancel back to the choice.
 */
public class LOTRHiredDismissScreen extends LOTRNPCInteractScreen {

    public LOTRHiredDismissScreen(LOTRNPCEntity entity) {
        super(entity);
    }

    @Override
    protected void init() {
        int y = this.height / 5 * 3 + 40;
        addRenderableWidget(Button.builder(Component.translatable("lotr.gui.dismiss.dismiss"),
                        b -> ClientPlayNetworking.send(new LOTRHiredPayloads.Dismiss(this.theEntity.getId(), 0)))
                .bounds(this.width / 2 - 65, y, 60, 20).build());
        addRenderableWidget(Button.builder(Component.translatable("lotr.gui.dismiss.cancel"),
                        b -> this.minecraft.setScreenAndShow(new LOTRHiredInteractScreen(this.theEntity)))
                .bounds(this.width / 2 + 5, y, 60, 20).build());
    }

    private boolean isOwn(Entity entity) {
        return entity instanceof LOTRNPCEntity npc && npc.hiredNPCInfo.getHiringPlayer() == this.minecraft.player;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        int y = this.height / 5 * 3;
        y = centred(graphics, Component.translatable("lotr.gui.dismiss.warning1"), y, 0xFFFFFFFF);
        y = centred(graphics, Component.translatable("lotr.gui.dismiss.warning2"), y, 0xFFFFFFFF);
        if (isOwn(this.theEntity.getVehicle())) {
            y = centred(graphics, Component.translatable("lotr.gui.dismiss.mount"), y, 0xFFAAAAAA);
        }
        if (isOwn(this.theEntity.getFirstPassenger())) {
            centred(graphics, Component.translatable("lotr.gui.dismiss.rider"), y, 0xFFAAAAAA);
        }
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
    }

    private int centred(GuiGraphicsExtractor graphics, Component s, int y, int colour) {
        graphics.text(this.font, s, (this.width - this.font.width(s)) / 2, y, colour, true);
        return y + this.font.lineHeight;
    }
}
