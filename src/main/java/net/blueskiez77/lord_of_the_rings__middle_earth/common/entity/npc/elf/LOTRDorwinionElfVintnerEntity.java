package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.elf;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRPlayerAchievements;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade.LOTRTradeEntries;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade.LOTRTradeable;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;

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
 * LOTREntityDorwinionElfVintner (the Dorwinion Vintner-elf): sells wine of
 * every strength to those at +50 or better, hooded in its cloak, with one of
 * its wines in hand, and seeks no one out.
 */
public class LOTRDorwinionElfVintnerEntity extends LOTRDorwinionElfEntity implements LOTRTradeable {

    public LOTRDorwinionElfVintnerEntity(EntityType<? extends LOTRDorwinionElfVintnerEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        addTargetTasks(false);
    }

    @Override
    public LOTRTradeEntries getBuyPool() {
        return LOTRTradeEntries.DORWINION_VINTNER_BUY;
    }

    @Override
    public LOTRTradeEntries getSellPool() {
        return LOTRTradeEntries.DORWINION_VINTNER_SELL;
    }

    /** canTradeWith: +50 alignment and friendly. */
    @Override
    public boolean canTradeWith(Player player) {
        return LOTRPlayerAlignments.getAlignment(player, getFaction()) >= 50.0f && isFriendly(player);
    }

    /** onSpawnWithEgg: one of its own wines in hand, as the first of a fresh set of its trades. */
    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setIdleItem(getBuyPool().getRandomTrades(this.random)[0].createTradeItem());
        return data;
    }

    @Override
    public boolean shouldRenderNPCHair() {
        return false;
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        if (isFriendly(player)) {
            return canTradeWith(player) ? "dorwinion/elfVintner/friendly" : "dorwinion/elfVintner/neutral";
        }
        return "dorwinion/elf/hostile";
    }

    @Override
    public void onPlayerTrade(Player player, LOTRTradeEntries.TradeType type, ItemStack stack) {
        if (type == LOTRTradeEntries.TradeType.BUY) {
            LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.BUY_WINE_VINTNER);
        }
    }
}
