package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.gondor;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade.LOTRTradeable;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityGondorMarketTrader: a Gondorian keeping a market stall, who trades
 * with anyone Gondor does not dislike.
 *
 * <p>NOT ported yet: the tradeGondorMarketTrader achievement.
 */
public abstract class LOTRGondorMarketTraderEntity extends LOTRGondorManEntity implements LOTRTradeable {

    protected LOTRGondorMarketTraderEntity(EntityType<? extends LOTRGondorMarketTraderEntity> type, Level level) {
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
        return isFriendly(player) ? "gondor/marketTrader/friendly" : "gondor/marketTrader/hostile";
    }
}
