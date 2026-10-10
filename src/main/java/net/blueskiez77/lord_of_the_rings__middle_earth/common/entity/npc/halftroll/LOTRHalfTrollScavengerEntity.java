package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.halftroll;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRPlayerAchievements;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade.LOTRTradeEntries;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade.LOTRTradeable;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityHalfTrollScavenger: a half-troll with a poisoned dagger who
 * trades in what he has scavenged with those at +50 or better. Unlike the
 * other traders he still goes looking for enemies, and he does not respawn.
 */
public class LOTRHalfTrollScavengerEntity extends LOTRHalfTrollEntity implements LOTRTradeable {

    public LOTRHalfTrollScavengerEntity(EntityType<? extends LOTRHalfTrollScavengerEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public LOTRTradeEntries getBuyPool() {
        return LOTRTradeEntries.HALF_TROLL_SCAVENGER_BUY;
    }

    @Override
    public LOTRTradeEntries getSellPool() {
        return LOTRTradeEntries.HALF_TROLL_SCAVENGER_SELL;
    }

    /** canTradeWith: +50 alignment and friendly. */
    @Override
    public boolean canTradeWith(Player player) {
        return LOTRPlayerAlignments.getAlignment(player, getFaction()) >= 50.0f && isFriendly(player);
    }

    @Override
    public boolean shouldTraderRespawn() {
        return false;
    }

    @Override
    public float getAlignmentBonus() {
        return 2.0f;
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        if (isFriendly(player)) {
            return canTradeWith(player) ? "halfTroll/scavenger/friendly" : "halfTroll/scavenger/neutral";
        }
        return "halfTroll/halfTroll/hostile";
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.POISONED_HALF_TROLL_DAGGER));
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        return data;
    }

    @Override
    public void onPlayerTrade(Player player, LOTRTradeEntries.TradeType type, ItemStack stack) {
        LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.TRADE_HALF_TROLL_SCAVENGER);
    }
}
