package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.farharad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade.LOTRTradeable;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityMoredainVillageTrader: a trader of a Moredain village, trading
 * with anyone the Moredain do not dislike, who seeks no one out.
 *
 * <p>NOT ported yet: the tradeMoredainVillager achievement (D7).
 */
public abstract class LOTRMoredainVillageTraderEntity extends LOTRMoredainEntity implements LOTRTradeable {

    protected LOTRMoredainVillageTraderEntity(EntityType<? extends LOTRMoredainVillageTraderEntity> type, Level level) {
        super(type, level);
    }

    /** canTradeWith: not disliked, and friendly. */
    @Override
    public boolean canTradeWith(Player player) {
        return LOTRPlayerAlignments.getAlignment(player, getFaction()) >= 0.0f && isFriendly(player);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        addTargetTasks(false);
    }

    @Override
    public float getAlignmentBonus() {
        return 2.0f;
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        return isFriendly(player) ? "moredain/trader/friendly" : "moredain/moredain/hostile";
    }
}
