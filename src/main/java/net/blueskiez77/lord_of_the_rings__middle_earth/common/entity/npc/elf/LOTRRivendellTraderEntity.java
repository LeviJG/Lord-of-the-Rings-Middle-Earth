package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.elf;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade.LOTRTradeEntries;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade.LOTRTravellingTrader;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityRivendellTrader (the Rivendell Wanderer): a travelling trader of
 * Rivendell who trades with those at +75 or better, with an escort of
 * Rivendell elves, well protected (armour 10) and seeking no one out.
 *
 * <p>NOT ported yet: its cape (LOTRCapes.RIVENDELL_TRADER, with NPC capes) and
 * the tradeRivendellTrader achievement.
 */
public class LOTRRivendellTraderEntity extends LOTRRivendellElfEntity implements LOTRTravellingTrader {

    public LOTRRivendellTraderEntity(EntityType<? extends LOTRRivendellTraderEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new LOTRAttackOnCollideGoal(this, 1.6, false));
        addTargetTasks(false);
    }

    @Override
    public LOTRTradeEntries getBuyPool() {
        return LOTRTradeEntries.RIVENDELL_TRADER_BUY;
    }

    @Override
    public LOTRTradeEntries getSellPool() {
        return LOTRTradeEntries.RIVENDELL_TRADER_SELL;
    }

    /** canTradeWith: +75 alignment and friendly. */
    @Override
    public boolean canTradeWith(Player player) {
        return LOTRPlayerAlignments.getAlignment(player, getFaction()) >= 75.0f && isFriendly(player);
    }

    @Override
    public @Nullable LOTRNPCEntity createTravellingEscort(ServerLevel level) {
        return LOTREntities.RIVENDELL_ELF.create(level, EntitySpawnReason.EVENT);
    }

    @Override
    public String getDepartureSpeech() {
        return "rivendell/trader/departure";
    }

    @Override
    public float getAlignmentBonus() {
        return 2.0f;
    }

    /** getExperiencePoints: 5 to 7. */
    @Override
    protected int getBaseExperienceReward(ServerLevel level) {
        return 5 + this.random.nextInt(3);
    }

    /** getTotalArmorValue: 10, whatever it wears. */
    @Override
    public int getArmorValue() {
        return 10;
    }

    /** A travelling trader brought back by a respawner comes back only once. */
    @Override
    public boolean shouldTraderRespawn() {
        return false;
    }

    @Override
    public boolean shouldRenderNPCHair() {
        return false;
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        if (isFriendly(player)) {
            return canTradeWith(player) ? "rivendell/trader/friendly" : "rivendell/trader/neutral";
        }
        return "rivendell/trader/hostile";
    }
}
