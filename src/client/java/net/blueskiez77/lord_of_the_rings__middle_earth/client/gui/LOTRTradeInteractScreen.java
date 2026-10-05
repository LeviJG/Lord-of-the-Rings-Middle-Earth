package net.blueskiez77.lord_of_the_rings__middle_earth.client.gui;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRSmith;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade.LOTRTradeNetworking;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.network.LOTRTradePayloads;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

/**
 * LOTRGuiTradeInteract: Talk or Trade, and Exchange Coins beneath; a smith
 * has a Smith button too, and the other two move over for it. Each sends
 * LOTRPacketTraderInteract with its action.
 * LOTRGuiTradeUnitTradeInteract, for a trader who also hires out units,
 * adds Hire beneath.
 */
public class LOTRTradeInteractScreen extends LOTRNPCInteractScreen {

    private final boolean unitTrade;

    public LOTRTradeInteractScreen(LOTRNPCEntity entity, boolean unitTrade) {
        super(entity);
        this.unitTrade = unitTrade;
    }

    @Override
    protected void init() {
        int y = this.height / 5 * 3;
        int shift = this.theEntity instanceof LOTRSmith ? 35 : 0;
        addRenderableWidget(button("lotr.gui.npc.talk", LOTRTradeNetworking.ACTION_TALK, this.width / 2 - 65 - shift, y, 60));
        addRenderableWidget(button("lotr.gui.npc.trade", LOTRTradeNetworking.ACTION_TRADE, this.width / 2 + 5 - shift, y, 60));
        addRenderableWidget(button("lotr.gui.npc.exchange", LOTRTradeNetworking.ACTION_EXCHANGE, this.width / 2 - 65, y + 25, 130));
        if (this.theEntity instanceof LOTRSmith) {
            addRenderableWidget(button("lotr.gui.npc.smith", LOTRTradeNetworking.ACTION_SMITH, this.width / 2 + 40, y, 60));
        }
        if (this.unitTrade) {
            addRenderableWidget(Button.builder(Component.translatable("lotr.gui.npc.hire"),
                            b -> ClientPlayNetworking.send(new LOTRTradePayloads.UnitInteract(this.theEntity.getId(),
                                    LOTRTradeNetworking.ACTION_HIRE)))
                    .bounds(this.width / 2 - 65, y + 50, 130, 20)
                    .build());
        }
    }

    private Button button(String key, int action, int x, int y, int width) {
        return Button.builder(Component.translatable(key),
                        b -> ClientPlayNetworking.send(new LOTRTradePayloads.Interact(this.theEntity.getId(), action)))
                .bounds(x, y, width, 20)
                .build();
    }
}
