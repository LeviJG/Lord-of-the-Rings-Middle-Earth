package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.bree;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRPlayerAchievements;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade.LOTRTradeEntries;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade.LOTRTradeable;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityBreeHobbitTrader: a Bree-hobbit keeping a market stall, with the
 * same wares as the Bree-man who keeps one.
 */
public abstract class LOTRBreeHobbitTraderEntity extends LOTRBreeHobbitEntity implements LOTRTradeable {

    protected LOTRBreeHobbitTraderEntity(EntityType<? extends LOTRBreeHobbitTraderEntity> type, Level level) {
        super(type, level);
    }

    /** canTradeWith: not disliked, and friendly. */
    @Override
    public boolean canTradeWith(Player player) {
        return LOTRPlayerAlignments.getAlignment(player, getFaction()) >= 0.0f && isFriendlyAndAligned(player);
    }

    @Override
    public float getAlignmentBonus() {
        return 2.0f;
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        return isFriendlyAndAligned(player) ? "bree/marketTrader/hobbit/friendly" : "bree/marketTrader/hobbit/hostile";
    }

    @Override
    public void onPlayerTrade(Player player, LOTRTradeEntries.TradeType type, ItemStack stack) {
        LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.TRADE_BREE_MARKET_TRADER);
    }
}
