package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.elf;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRSmith;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade.LOTRTradeEntries;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityHighElfSmith: an elven smith of Lindon, hooded in its
 * smith's cloak, who trades with those at +100 or better and seeks no one
 * out.
 *
 * <p>NOT ported yet: its cape (highElfSmith_cape, with NPC capes) and the
 * tradeHighElfSmith achievement.
 */
public class LOTRHighElfSmithEntity extends LOTRHighElfEntity implements LOTRSmith {

    public LOTRHighElfSmithEntity(EntityType<? extends LOTRHighElfSmithEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        addTargetTasks(false);
    }

    @Override
    public LOTRTradeEntries getBuyPool() {
        return LOTRTradeEntries.HIGH_ELF_SMITH_BUY;
    }

    @Override
    public LOTRTradeEntries getSellPool() {
        return LOTRTradeEntries.HIGH_ELF_SMITH_SELL;
    }

    /** canTradeWith: +100 alignment and friendly. */
    @Override
    public boolean canTradeWith(Player player) {
        return LOTRPlayerAlignments.getAlignment(player, getFaction()) >= 100.0f && isFriendly(player);
    }

    @Override
    public float getAlignmentBonus() {
        return 2.0f;
    }

    @Override
    public boolean shouldRenderNPCHair() {
        return false;
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        if (isFriendly(player)) {
            return canTradeWith(player) ? "highElf/smith/friendly" : "highElf/smith/neutral";
        }
        return "highElf/smith/hostile";
    }
}
