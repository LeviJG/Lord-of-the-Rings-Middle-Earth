package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.bree;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade.LOTRTradeable;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityBreeMarketTrader: a Bree-man keeping a market stall, who trades
 * with anyone Bree-land does not dislike.
 *
 * <p>NOT ported yet: the tradeBreeMarketTrader achievement.
 */
public abstract class LOTRBreeMarketTraderEntity extends LOTRBreeManEntity implements LOTRTradeable {

    protected LOTRBreeMarketTraderEntity(EntityType<? extends LOTRBreeMarketTraderEntity> type, Level level) {
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
        return isFriendlyAndAligned(player) ? "bree/marketTrader/man/friendly" : "bree/marketTrader/man/hostile";
    }
}
