package net.blueskiez77.lord_of_the_rings__middle_earth.client.gui;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade.LOTRTradeNetworking;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRTradePayloads;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

/**
 * LOTRGuiUnitTradeInteract, and LOTRGuiMercenaryInteract which differed only
 * in its packet: Talk or Hire.
 */
public class LOTRUnitTradeInteractScreen extends LOTRNPCInteractScreen {

    public LOTRUnitTradeInteractScreen(LOTRNPCEntity entity) {
        super(entity);
    }

    @Override
    protected void init() {
        int y = this.height / 5 * 3;
        addRenderableWidget(Button.builder(Component.translatable("lotr.gui.npc.talk"),
                        b -> send(LOTRTradeNetworking.ACTION_TALK))
                .bounds(this.width / 2 - 65, y, 60, 20).build());
        addRenderableWidget(Button.builder(Component.translatable("lotr.gui.npc.hire"),
                        b -> send(LOTRTradeNetworking.ACTION_HIRE))
                .bounds(this.width / 2 + 5, y, 60, 20).build());
    }

    private void send(int action) {
        ClientPlayNetworking.send(new LOTRTradePayloads.UnitInteract(this.theEntity.getId(), action));
    }
}
