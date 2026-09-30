package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade;

import java.util.Set;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRDrinkItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRVessel;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import org.jspecify.annotations.Nullable;

/**
 * LOTRTradeEntry: an item and its price in silver coins. Each trade also
 * remembers how much has lately gone through it: once that reaches the
 * trader's lock value it is unavailable until it decays, and a refresh locks
 * every trade for a while.
 */
public class LOTRTradeEntry {

    /** Saved as the original's keys, with the item under "Item". */
    public static final Codec<LOTRTradeEntry> CODEC = RecordCodecBuilder.create(i -> i.group(
            ItemStack.CODEC.fieldOf("Item").forGetter(t -> t.tradeItem),
            Codec.INT.fieldOf("Cost").forGetter(t -> t.tradeCost),
            Codec.INT.optionalFieldOf("RecentTradeValue", 0).forGetter(t -> t.recentTradeValue),
            Codec.INT.optionalFieldOf("LockedTicks", 0).forGetter(t -> t.lockedTicks)
    ).apply(i, (item, cost, recent, locked) -> {
        LOTRTradeEntry trade = new LOTRTradeEntry(item, cost);
        trade.recentTradeValue = recent;
        trade.lockedTicks = locked;
        return trade;
    }));

    private final ItemStack tradeItem;
    private int tradeCost;
    private int recentTradeValue;
    private int lockedTicks;
    private @Nullable LOTRTraderNPCInfo theTrader;
    /** A pool entry's drink of random strength (the original's damage 9999). */
    boolean randomStrength;
    /** Other items the trade accepts: the original's wildcard damage (coal and charcoal). */
    Set<Item> alsoMatches = Set.of();

    public LOTRTradeEntry(ItemStack stack, int cost) {
        this.tradeItem = stack;
        this.tradeCost = cost;
    }

    public ItemStack createTradeItem() {
        return this.tradeItem.copy();
    }

    public void doTransaction(int value) {
        this.recentTradeValue += value;
    }

    public int getCost() {
        return this.tradeCost;
    }

    public void setCost(int cost) {
        this.tradeCost = cost;
    }

    public float getLockedProgress() {
        if (this.theTrader != null && this.theTrader.shouldLockTrades()) {
            return (float) this.recentTradeValue / this.theTrader.getLockTradeAtValue();
        }
        return 0.0f;
    }

    public int getLockedProgressForSlot() {
        return Math.round(getLockedProgress() * 16);
    }

    public boolean isAvailable() {
        if (this.theTrader != null && this.theTrader.shouldLockTrades()) {
            return this.recentTradeValue < this.theTrader.getLockTradeAtValue() && this.lockedTicks <= 0;
        }
        return true;
    }

    /**
     * matches: a drink by its drink alone, whatever it is served in; anything
     * else by its item and wear (OreDictionary.itemMatches, not strict).
     *
     * <p>NOT ported yet: refusing pickpocketed items (IPickpocketable).
     */
    public boolean matches(ItemStack stack) {
        ItemStack trade = createTradeItem();
        if (trade.getItem() instanceof LOTRDrinkItem) {
            return LOTRVessel.equivalentDrink(trade).getItem() == LOTRVessel.equivalentDrink(stack).getItem();
        }
        if (this.alsoMatches.contains(stack.getItem())) {
            return true;
        }
        return trade.getItem() == stack.getItem() && trade.getDamageValue() == stack.getDamageValue();
    }

    public void setLockedForTicks(int ticks) {
        this.lockedTicks = ticks;
    }

    public void setOwningTrader(LOTRTraderNPCInfo trader) {
        if (this.theTrader != null) {
            throw new IllegalArgumentException("Cannot assign already-owned trade entry to a different trader!");
        }
        this.theTrader = trader;
    }

    /** updateAvailability: decay and unlock; true if what a trade slot shows has changed. */
    public boolean updateAvailability(int tick) {
        boolean prevAvailable = isAvailable();
        int prevLockProgress = getLockedProgressForSlot();
        if (tick % this.theTrader.getValueDecayTicks() == 0 && this.recentTradeValue > 0) {
            --this.recentTradeValue;
        }
        if (this.lockedTicks > 0) {
            --this.lockedTicks;
        }
        return isAvailable() != prevAvailable || getLockedProgressForSlot() != prevLockProgress;
    }
}
