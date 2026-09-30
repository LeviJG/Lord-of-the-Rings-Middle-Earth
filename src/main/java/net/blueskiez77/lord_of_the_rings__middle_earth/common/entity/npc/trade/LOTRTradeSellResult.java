package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade;

import net.minecraft.world.item.ItemStack;

/** LOTRTradeSellResult: how much of a stack a sell trade takes, and for how much. */
public class LOTRTradeSellResult {

    public final int tradeIndex;
    public final int tradeValue;
    public final int tradeStackSize;
    public final int tradesMade;
    public final int itemsSold;
    public final int totalSellValue;

    public LOTRTradeSellResult(int index, LOTRTradeEntry trade, ItemStack sellItem) {
        ItemStack tradeItem = trade.createTradeItem();
        this.tradeIndex = index;
        this.tradeValue = trade.getCost();
        this.tradeStackSize = tradeItem.getCount();
        this.tradesMade = !trade.isAvailable() ? 0 : sellItem.getCount() / this.tradeStackSize;
        this.itemsSold = this.tradesMade * tradeItem.getCount();
        this.totalSellValue = this.tradesMade * this.tradeValue;
    }
}
