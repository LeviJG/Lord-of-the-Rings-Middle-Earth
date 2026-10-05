package net.blueskiez77.lord_of_the_rings__middle_earth.client.gui;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRHiredPayloads;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

/**
 * LOTRGuiHiredInteract: Talk (if it has anything to say), Command, and
 * Dismiss, which asks first.
 */
public class LOTRHiredInteractScreen extends LOTRNPCInteractScreen {

    public LOTRHiredInteractScreen(LOTRNPCEntity entity) {
        super(entity);
    }

    @Override
    protected void init() {
        int y = this.height / 5 * 3;
        Button talk = addRenderableWidget(Button.builder(Component.translatable("lotr.gui.npc.talk"), b -> send(0))
                .bounds(this.width / 2 - 65, y, 60, 20).build());
        addRenderableWidget(Button.builder(Component.translatable("lotr.gui.npc.command"), b -> send(1))
                .bounds(this.width / 2 + 5, y, 60, 20).build());
        addRenderableWidget(Button.builder(Component.translatable("lotr.gui.npc.dismiss"),
                        b -> this.minecraft.setScreenAndShow(new LOTRHiredDismissScreen(this.theEntity)))
                .bounds(this.width / 2 - 65, y + 25, 130, 20).build());
        talk.active = this.theEntity.getSpeechBank(this.minecraft.player) != null;
    }

    private void send(int action) {
        ClientPlayNetworking.send(new LOTRHiredPayloads.Interact(this.theEntity.getId(), action));
    }
}
