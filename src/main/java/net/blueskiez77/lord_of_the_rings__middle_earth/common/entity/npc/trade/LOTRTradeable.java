package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRBartender;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * LOTRTradeable: an NPC that buys and sells from its two pools. Bartenders
 * are {@link LOTRBartender}s too.
 */
public interface LOTRTradeable {

    boolean canTradeWith(Player player);

    LOTRTradeEntries getBuyPool();

    LOTRTradeEntries getSellPool();

    /** onPlayerTrade: the trader's own reaction (its achievements). */
    default void onPlayerTrade(Player player, LOTRTradeEntries.TradeType type, ItemStack stack) {
    }
}
