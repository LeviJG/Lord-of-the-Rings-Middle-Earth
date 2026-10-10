package net.blueskiez77.lord_of_the_rings__middle_earth.common.inventory;

import java.util.HashMap;
import java.util.Map;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRPlayerAchievements;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade.LOTRTradeEntries;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade.LOTRTradeEntry;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade.LOTRTradeSellResult;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade.LOTRTradeable;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCoins;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import org.jspecify.annotations.Nullable;

/**
 * LOTRContainerTrade: a row of what the trader sells, a row of what it buys
 * (both for show; a bought item is taken from its slot), and a row to put
 * things up for sale. The sell button is menu button 0, in place of
 * LOTRPacketSell. Closing the screen hands back anything left on offer.
 */
public class LOTRTradeMenu extends AbstractContainerMenu {

    public static final int SELL_BUTTON = 0;

    public final SimpleContainer tradeInvBuy = new SimpleContainer(9);
    public final SimpleContainer tradeInvSell = new SimpleContainer(9);
    public final SimpleContainer tradeInvSellOffer = new SimpleContainer(9);
    public final @Nullable LOTRNPCEntity theTraderNPC;

    /** The client's: the trader as the opening data names it. */
    public LOTRTradeMenu(int containerId, Inventory inventory, Integer entityId) {
        this(containerId, inventory,
                inventory.player.level().getEntity(entityId) instanceof LOTRNPCEntity npc && npc instanceof LOTRTradeable
                        ? npc : null);
    }

    public LOTRTradeMenu(int containerId, Inventory inventory, @Nullable LOTRNPCEntity trader) {
        super(LOTRMenus.TRADE, containerId);
        this.theTraderNPC = trader;
        if (trader != null && !trader.level().isClientSide()) {
            updateAllTradeSlots();
        }
        for (int i = 0; i < 9; ++i) {
            addSlot(new TradeSlot(tradeInvBuy, i, 8 + i * 18, 40, LOTRTradeEntries.TradeType.BUY));
        }
        for (int i = 0; i < 9; ++i) {
            addSlot(new TradeSlot(tradeInvSell, i, 8 + i * 18, 92, LOTRTradeEntries.TradeType.SELL));
        }
        for (int i = 0; i < 9; ++i) {
            addSlot(new Slot(tradeInvSellOffer, i, 8 + i * 18, 141));
        }
        addStandardInventorySlots(inventory, 8, 188);
    }

    public static boolean isTradingWith(Player player, LOTRNPCEntity npc) {
        return player.containerMenu instanceof LOTRTradeMenu menu && menu.theTraderNPC == npc;
    }

    @Override
    public boolean stillValid(Player player) {
        LOTRNPCEntity npc = this.theTraderNPC;
        return npc != null && player.distanceTo(npc) <= 12.0 && npc.isAlive() && npc.getTarget() == null
                && ((LOTRTradeable) npc).canTradeWith(player);
    }

    /** onContainerClosed: the offers go back to the player. */
    @Override
    public void removed(Player player) {
        super.removed(player);
        if (!player.level().isClientSide()) {
            clearContainer(player, tradeInvSellOffer);
        }
    }

    /** What the offers would fetch; the screen shows it and wakes the sell button. */
    public int totalSellPrice() {
        if (this.theTraderNPC == null) {
            return 0;
        }
        int total = 0;
        for (int i = 0; i < tradeInvSellOffer.getContainerSize(); ++i) {
            ItemStack stack = tradeInvSellOffer.getItem(i);
            LOTRTradeSellResult result;
            if (!stack.isEmpty() && (result = LOTRTradeEntries.getItemSellResult(stack, this.theTraderNPC)) != null) {
                total += result.totalSellValue;
            }
        }
        return total;
    }

    /** LOTRPacketSell's handler: everything on offer that the trader takes, for coins. */
    @Override
    public boolean clickMenuButton(Player player, int id) {
        LOTRNPCEntity trader = this.theTraderNPC;
        if (id != SELL_BUTTON || trader == null || trader.traderNPCInfo == null || player.level().isClientSide()) {
            return false;
        }
        Map<LOTRTradeEntry, Integer> tradesUsed = new HashMap<>();
        int totalCoins = 0;
        for (int i = 0; i < tradeInvSellOffer.getContainerSize(); ++i) {
            ItemStack stack = tradeInvSellOffer.getItem(i);
            LOTRTradeSellResult sellResult;
            if (stack.isEmpty() || (sellResult = LOTRTradeEntries.getItemSellResult(stack, trader)) == null) {
                continue;
            }
            LOTRTradeEntry trade = trader.traderNPCInfo.getSellTrades()[sellResult.tradeIndex];
            totalCoins += sellResult.totalSellValue;
            if (trade != null) {
                tradesUsed.merge(trade, sellResult.totalSellValue, Integer::sum);
            }
            stack.shrink(sellResult.itemsSold);
            if (stack.isEmpty()) {
                tradeInvSellOffer.setItem(i, ItemStack.EMPTY);
            }
        }
        if (totalCoins > 0) {
            for (Map.Entry<LOTRTradeEntry, Integer> e : tradesUsed.entrySet()) {
                trader.traderNPCInfo.onTrade(player, e.getKey(), LOTRTradeEntries.TradeType.SELL, e.getValue());
            }
            LOTRCoins.giveCoins(totalCoins, player);
            if (totalCoins >= 1000) {
                LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.EARN_MANY_COINS);
            }
            trader.playTradeSound();
        }
        return true;
    }

    /** transferStackInSlot. */
    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = slots.get(index);
        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }
        // 1.7.10 re-checked canTakeStack on every repeat of a shift-click; the
        // modern loop does not, so a buy slot checks here for each one.
        if (index < 9 && !slot.mayPickup(player)) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = slot.getItem();
        ItemStack original = stack.copy();
        LOTRTradeSellResult sellResult = this.theTraderNPC == null ? null
                : LOTRTradeEntries.getItemSellResult(stack, this.theTraderNPC);
        boolean sellable = sellResult != null && sellResult.tradesMade > 0;
        if (index < 9) {
            if (!moveItemStackTo(stack, 27, 63, true)) {
                return ItemStack.EMPTY;
            }
        } else if (index < 18) {
            return ItemStack.EMPTY;
        } else if (index < 27) {
            if (!moveItemStackTo(stack, 27, 63, true)) {
                return ItemStack.EMPTY;
            }
        } else if (sellable) {
            if (!moveItemStackTo(stack, 18, 27, false)) {
                return ItemStack.EMPTY;
            }
        } else if (index < 54) {
            if (!moveItemStackTo(stack, 54, 63, false)) {
                return ItemStack.EMPTY;
            }
        } else if (index < 63 && !moveItemStackTo(stack, 27, 54, false)) {
            return ItemStack.EMPTY;
        }
        if (stack.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        if (stack.getCount() == original.getCount()) {
            return ItemStack.EMPTY;
        }
        slot.onTake(player, stack);
        return original;
    }

    /** updateAllTradeSlots: the trader's current trades into the two show rows. */
    public void updateAllTradeSlots() {
        if (this.theTraderNPC == null || this.theTraderNPC.traderNPCInfo == null) {
            return;
        }
        fill(tradeInvBuy, this.theTraderNPC.traderNPCInfo.getBuyTrades());
        fill(tradeInvSell, this.theTraderNPC.traderNPCInfo.getSellTrades());
    }

    private static void fill(Container inv, LOTRTradeEntry[] trades) {
        for (int i = 0; i < inv.getContainerSize(); ++i) {
            LOTRTradeEntry trade = i < trades.length ? trades[i] : null;
            inv.setItem(i, trade != null ? trade.createTradeItem() : ItemStack.EMPTY);
        }
    }

    /** LOTRSlotTrade: a show slot. A bought item costs its price and is restocked at once. */
    public class TradeSlot extends Slot {
        public final LOTRTradeEntries.TradeType tradeType;

        TradeSlot(Container container, int index, int x, int y, LOTRTradeEntries.TradeType type) {
            super(container, index, x, y);
            this.tradeType = type;
        }

        /** LOTRSlotProtected. */
        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }

        @Override
        public boolean mayPickup(Player player) {
            if (this.tradeType == LOTRTradeEntries.TradeType.SELL) {
                return false;
            }
            LOTRTradeEntry trade = getTrade();
            if (trade != null && !trade.isAvailable()) {
                return false;
            }
            if (LOTRCoins.getInventoryValue(player) < cost()) {
                return false;
            }
            return super.mayPickup(player);
        }

        public int cost() {
            LOTRTradeEntry trade = getTrade();
            return trade == null ? 0 : trade.getCost();
        }

        public @Nullable LOTRTradeEntry getTrade() {
            LOTRNPCEntity npc = LOTRTradeMenu.this.theTraderNPC;
            if (npc == null || npc.traderNPCInfo == null) {
                return null;
            }
            LOTRTradeEntry[] trades = this.tradeType == LOTRTradeEntries.TradeType.BUY
                    ? npc.traderNPCInfo.getBuyTrades() : npc.traderNPCInfo.getSellTrades();
            int i = getContainerSlot();
            return i >= 0 && i < trades.length ? trades[i] : null;
        }

        /** onPickupFromSlot. */
        @Override
        public void onTake(Player player, ItemStack stack) {
            LOTRNPCEntity npc = LOTRTradeMenu.this.theTraderNPC;
            boolean server = !player.level().isClientSide();
            if (this.tradeType == LOTRTradeEntries.TradeType.BUY && server) {
                LOTRCoins.takeCoins(cost(), player);
            }
            super.onTake(player, stack);
            if (this.tradeType == LOTRTradeEntries.TradeType.BUY && server && npc != null && npc.traderNPCInfo != null) {
                LOTRTradeEntry trade = getTrade();
                if (trade != null) {
                    set(trade.createTradeItem());
                    // sendContainerToPlayer
                    LOTRTradeMenu.this.sendAllDataToRemote();
                    npc.traderNPCInfo.onTrade(player, trade, LOTRTradeEntries.TradeType.BUY, cost());
                    npc.playTradeSound();
                }
            }
        }
    }
}
