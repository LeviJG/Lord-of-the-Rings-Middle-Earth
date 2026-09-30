package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade;

import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

/**
 * LOTRTraderNPCInfo: a trader's current buy and sell trades. Trades lock once
 * enough value has gone through them and unlock as it decays; after 5000
 * coins' worth of trade the trader draws fresh trades, all locked for five
 * minutes. Now and then it calls out to players nearby.
 *
 * <p>NOT ported yet, with the trade screen (D16): sending the trades to a
 * player with the screen open (LOTRPacketTraderInfo), and the faction trade
 * counter (LOTRFactionData.addTrade).
 */
public class LOTRTraderNPCInfo {

    private final LOTRNPCEntity theEntity;
    private LOTRTradeEntry[] buyTrades = new LOTRTradeEntry[0];
    private LOTRTradeEntry[] sellTrades = new LOTRTradeEntry[0];
    private int timeUntilAdvertisement;
    private int timeSinceTrade;
    private boolean shouldLockTrades = true;
    private int lockTradeAtValue = 200;
    private int lockValueDecayTicks = 60;
    private boolean shouldRefresh = true;
    private int valueSinceRefresh;
    private int refreshAtValue = 5000;
    private int lockTicksAfterRefresh = 6000;

    public LOTRTraderNPCInfo(LOTRNPCEntity npc) {
        this.theEntity = npc;
        if (npc instanceof LOTRTradeable && !npc.level().isClientSide()) {
            refreshTrades();
        }
    }

    public LOTRTradeEntry[] getBuyTrades() {
        return this.buyTrades;
    }

    public LOTRTradeEntry[] getSellTrades() {
        return this.sellTrades;
    }

    private void setBuyTrades(LOTRTradeEntry[] trades) {
        for (LOTRTradeEntry trade : trades) {
            trade.setOwningTrader(this);
        }
        this.buyTrades = trades;
    }

    private void setSellTrades(LOTRTradeEntry[] trades) {
        for (LOTRTradeEntry trade : trades) {
            trade.setOwningTrader(this);
        }
        this.sellTrades = trades;
    }

    public int getLockTradeAtValue() {
        return this.lockTradeAtValue;
    }

    public int getValueDecayTicks() {
        return this.lockValueDecayTicks;
    }

    public boolean shouldLockTrades() {
        return this.shouldLockTrades;
    }

    /** onTrade: the trader's reaction, then the value put through the trade. */
    public void onTrade(Player player, LOTRTradeEntry trade, LOTRTradeEntries.TradeType type, int value) {
        ((LOTRTradeable) this.theEntity).onPlayerTrade(player, type, trade.createTradeItem());
        trade.doTransaction(value);
        this.timeSinceTrade = 0;
        this.valueSinceRefresh += value;
    }

    /** onUpdate, server side. */
    public void tick() {
        if (this.timeUntilAdvertisement > 0) {
            --this.timeUntilAdvertisement;
        }
        ++this.timeSinceTrade;
        int ticksExisted = this.theEntity.tickCount;
        for (LOTRTradeEntry trade : this.buyTrades) {
            if (trade != null) {
                trade.updateAvailability(ticksExisted);
            }
        }
        for (LOTRTradeEntry trade : this.sellTrades) {
            if (trade != null) {
                trade.updateAvailability(ticksExisted);
            }
        }
        if (this.shouldRefresh && this.valueSinceRefresh >= this.refreshAtValue) {
            refreshTrades();
            setAllTradesDelayed();
        }
        if (this.theEntity.isAlive() && this.theEntity.getTarget() == null && this.timeUntilAdvertisement == 0
                && this.timeSinceTrade > 600) {
            double range = 10.0;
            List<Player> players = this.theEntity.level().getEntitiesOfClass(Player.class,
                    this.theEntity.getBoundingBox().inflate(range));
            for (Player player : players) {
                // The original also skipped a player with any screen open.
                if (!player.isAlive() || player.isCreative()
                        || player.containerMenu != player.inventoryMenu) {
                    continue;
                }
                String speechBank = this.theEntity.getSpeechBank(player);
                if (speechBank != null && this.theEntity.getRandom().nextInt(3) == 0) {
                    this.theEntity.sendSpeechBank(player, speechBank);
                }
            }
            this.timeUntilAdvertisement = 20 * Mth.nextInt(this.theEntity.getRandom(), 5, 20);
        }
    }

    public void refreshTrades() {
        LOTRTradeable trader = (LOTRTradeable) this.theEntity;
        setBuyTrades(trader.getBuyPool().getRandomTrades(this.theEntity.getRandom()));
        setSellTrades(trader.getSellPool().getRandomTrades(this.theEntity.getRandom()));
        this.valueSinceRefresh = 0;
    }

    private void setAllTradesDelayed() {
        for (LOTRTradeEntry trade : this.buyTrades) {
            if (trade != null) {
                trade.setLockedForTicks(this.lockTicksAfterRefresh);
            }
        }
        for (LOTRTradeEntry trade : this.sellTrades) {
            if (trade != null) {
                trade.setLockedForTicks(this.lockTicksAfterRefresh);
            }
        }
    }

    /** onSpawnWithEgg: one trader in ten thousand charges a hundred times over. */
    public void inflateBuyPrices() {
        for (LOTRTradeEntry trade : this.buyTrades) {
            trade.setCost(trade.getCost() * 100);
        }
    }

    public void save(ValueOutput output) {
        ValueOutput.TypedOutputList<LOTRTradeEntry> buy = output.child("LOTRBuyTrades").list("Trades", LOTRTradeEntry.CODEC);
        for (LOTRTradeEntry trade : this.buyTrades) {
            if (trade != null) {
                buy.add(trade);
            }
        }
        ValueOutput.TypedOutputList<LOTRTradeEntry> sell = output.child("LOTRSellTrades").list("Trades", LOTRTradeEntry.CODEC);
        for (LOTRTradeEntry trade : this.sellTrades) {
            if (trade != null) {
                sell.add(trade);
            }
        }
        output.putInt("TimeSinceTrade", this.timeSinceTrade);
        output.putBoolean("ShouldLockTrades", this.shouldLockTrades);
        output.putInt("LockTradeAtValue", this.lockTradeAtValue);
        output.putInt("LockValueDecayTicks", this.lockValueDecayTicks);
        output.putBoolean("ShouldRefresh", this.shouldRefresh);
        output.putInt("RefreshAtValue", this.refreshAtValue);
        output.putInt("LockTicksAfterRefresh", this.lockTicksAfterRefresh);
        output.putInt("ValueSinceRefresh", this.valueSinceRefresh);
    }

    public void load(ValueInput input) {
        input.child("LOTRBuyTrades").ifPresent(data -> data.list("Trades", LOTRTradeEntry.CODEC).ifPresent(list -> {
            this.buyTrades = new LOTRTradeEntry[0];
            setBuyTrades(list.stream().toArray(LOTRTradeEntry[]::new));
        }));
        input.child("LOTRSellTrades").ifPresent(data -> data.list("Trades", LOTRTradeEntry.CODEC).ifPresent(list -> {
            this.sellTrades = new LOTRTradeEntry[0];
            setSellTrades(list.stream().toArray(LOTRTradeEntry[]::new));
        }));
        this.timeSinceTrade = input.getIntOr("TimeSinceTrade", 0);
        this.shouldLockTrades = input.getBooleanOr("ShouldLockTrades", this.shouldLockTrades);
        this.lockTradeAtValue = input.getIntOr("LockTradeAtValue", this.lockTradeAtValue);
        this.lockValueDecayTicks = input.getIntOr("LockValueDecayTicks", this.lockValueDecayTicks);
        this.shouldRefresh = input.getBooleanOr("ShouldRefresh", this.shouldRefresh);
        this.refreshAtValue = input.getIntOr("RefreshAtValue", this.refreshAtValue);
        this.lockTicksAfterRefresh = input.getIntOr("LockTicksAfterRefresh", this.lockTicksAfterRefresh);
        this.valueSinceRefresh = input.getIntOr("ValueSinceRefresh", 0);
    }
}
