package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.rohan;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade.LOTRTradeable;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityRohanMarketTrader: a Rohirrim keeping a market stall, who trades
 * with anyone Rohan does not dislike.
 *
 * <p>NOT ported yet: the tradeRohanMarketTrader achievement.
 */
public abstract class LOTRRohanMarketTraderEntity extends LOTRRohanManEntity implements LOTRTradeable {

    protected LOTRRohanMarketTraderEntity(EntityType<? extends LOTRRohanMarketTraderEntity> type, Level level) {
        super(type, level);
    }

    /** canTradeWith: not disliked, and friendly. */
    @Override
    public boolean canTradeWith(Player player) {
        return LOTRPlayerAlignments.getAlignment(player, getFaction()) >= 0.0f && isFriendly(player);
    }

    @Override
    public float getAlignmentBonus() {
        return 2.0f;
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        return isFriendly(player) ? "rohan/marketTrader/friendly" : "rohan/marketTrader/hostile";
    }
}
