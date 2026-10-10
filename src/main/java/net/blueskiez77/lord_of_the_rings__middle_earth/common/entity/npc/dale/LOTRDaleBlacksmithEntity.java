package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.dale;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRPlayerAchievements;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRSmith;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.trade.LOTRTradeEntries;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityDaleBlacksmith: a smith of Dale with his hammer, 25 strong,
 * trades with those at +50 or better and leaves iron
 * ingots when slain.
 */
public class LOTRDaleBlacksmithEntity extends LOTRDaleManEntity implements LOTRSmith {

    public LOTRDaleBlacksmithEntity(EntityType<? extends LOTRDaleBlacksmithEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LOTRDaleManEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 25.0);
    }

    @Override
    protected Goal createDaleAttackAI() {
        return new LOTRAttackOnCollideGoal(this, 1.4, false);
    }

    @Override
    public LOTRTradeEntries getBuyPool() {
        return LOTRTradeEntries.DALE_BLACKSMITH_BUY;
    }

    @Override
    public LOTRTradeEntries getSellPool() {
        return LOTRTradeEntries.DALE_BLACKSMITH_SELL;
    }

    /** canTradeWith: +50 alignment and friendly. */
    @Override
    public boolean canTradeWith(Player player) {
        return LOTRPlayerAlignments.getAlignment(player, getFaction()) >= 50.0f && isFriendly(player);
    }

    @Override
    public boolean shouldTraderRespawn() {
        return true;
    }

    @Override
    public void setupNPCGender() {
        this.familyInfo.setMale(true);
    }

    @Override
    public float getAlignmentBonus() {
        return 2.0f;
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        if (isFriendly(player)) {
            return canTradeWith(player) ? "dale/blacksmith/friendly" : "dale/blacksmith/neutral";
        }
        return "dale/blacksmith/hostile";
    }

    @Override
    protected void dropNPCItems(ServerLevel level, boolean killedByPlayer, int looting) {
        super.dropNPCItems(level, killedByPlayer, looting);
        int ingots = 1 + this.random.nextInt(3) + this.random.nextInt(looting + 1);
        for (int l = 0; l < ingots; ++l) {
            spawnAtLocation(level, Items.IRON_INGOT);
        }
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.BLACKSMITH_HAMMER));
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        return data;
    }

    @Override
    public void onPlayerTrade(Player player, LOTRTradeEntries.TradeType type, ItemStack stack) {
        LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.TRADE_DALE_BLACKSMITH);
    }
}
