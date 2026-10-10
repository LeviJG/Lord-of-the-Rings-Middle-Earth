package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.bree;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRPlayerAchievements;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRHobbitBounderEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRUnitTradeEntries;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRUnitTradeable;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRInvasions;

/**
 * LOTREntityBreeCaptain (the Bree-land Sherriff): a guard who leads the
 * watch. Unlike his guards he seeks no one out -- he only answers attacks.
 * He carries a book and quill at rest and wears a brown hat with a green
 * feather.
 *
 * He hires out guards and banner bearers to those at +100 or better.
 */
public class LOTRBreeCaptainEntity extends LOTRBreeGuardEntity implements LOTRUnitTradeable {

    public LOTRBreeCaptainEntity(EntityType<? extends LOTRBreeCaptainEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LOTRBreeManEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 25.0);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        addTargetTasks(false);
    }

    @Override
    protected int addBreeAttackAI(int prio) {
        this.goalSelector.addGoal(prio, new LOTRAttackOnCollideGoal(this, 1.5, false));
        return prio;
    }

    @Override
    public LOTRUnitTradeEntries getUnits() {
        return LOTRUnitTradeEntries.BREE_CAPTAIN;
    }

    @Override
    public LOTRInvasions getWarhorn() {
        return LOTRInvasions.BREE;
    }

    /** canTradeWith: +100 alignment and friendly. */
    @Override
    public boolean canTradeWith(Player player) {
        return LOTRPlayerAlignments.getAlignment(player, getFaction()) >= 100.0f && isFriendlyAndAligned(player);
    }

    @Override
    public float getAlignmentBonus() {
        return 5.0f;
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        if (isFriendlyAndAligned(player)) {
            return canTradeWith(player) ? "bree/captain/friendly" : "bree/captain/neutral";
        }
        return "bree/captain/hostile";
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setIdleItem(new ItemStack(Items.WRITABLE_BOOK));
        setItemSlot(EquipmentSlot.HEAD, LOTRHobbitBounderEntity.hat(6834742, 40960));
        return data;
    }

    @Override
    public void onUnitTrade(Player player) {
        LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.TRADE_BREE_CAPTAIN);
    }
}
