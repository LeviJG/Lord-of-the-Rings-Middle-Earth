package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRPlayerAchievements;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRUnitTradeEntries;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRUnitTradeable;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRInvasions;

/**
 * LOTREntityHobbitShirriff (registered as "HobbitShirriffChief", the Hobbit
 * Shirriff): a bounder who leads the watch. Unlike his bounders he seeks no
 * one out -- he only answers attacks -- and wears a dark green hat with a
 * green feather.
 *
 * He hires out bounders to those at +50 or better.
 */
public class LOTRHobbitShirriffEntity extends LOTRHobbitBounderEntity implements LOTRUnitTradeable {

    public LOTRHobbitShirriffEntity(EntityType<? extends LOTRHobbitShirriffEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        addTargetTasks(false);
    }

    @Override
    public LOTRUnitTradeEntries getUnits() {
        return LOTRUnitTradeEntries.HOBBIT_SHIRRIFF;
    }

    @Override
    public LOTRInvasions getWarhorn() {
        return LOTRInvasions.HOBBIT;
    }

    /** canTradeWith: +50 alignment and friendly. */
    @Override
    public boolean canTradeWith(Player player) {
        return net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments
                .getAlignment(player, getFaction()) >= 50.0f && isFriendlyAndAligned(player);
    }

    @Override
    public float getAlignmentBonus() {
        return 5.0f;
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        if (isFriendlyAndAligned(player)) {
            return canTradeWith(player) ? "hobbit/shirriff/friendly" : "hobbit/shirriff/neutral";
        }
        return "hobbit/bounder/hostile";
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        setItemSlot(EquipmentSlot.HEAD, hat(2301981, 3381529));
        return data;
    }

    @Override
    public void onUnitTrade(Player player) {
        LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.TRADE_HOBBIT_SHIRRIFF);
    }
}
