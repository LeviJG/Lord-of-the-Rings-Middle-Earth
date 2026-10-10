package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.bree;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.quest.LOTRMiniQuest;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.quest.LOTRMiniQuestFactory;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityRuffianBrute: a big ruffian (30 health) who goes about with his
 * weapon out. Bree-folk and hobbits give him a wide berth.
 */
public class LOTRRuffianBruteEntity extends LOTRBreeRuffianEntity {

    public LOTRRuffianBruteEntity(EntityType<? extends LOTRRuffianBruteEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LOTRBreeManEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 30.0);
    }

    @Override
    protected int addBreeAttackAI(int prio) {
        this.goalSelector.addGoal(prio, new LOTRAttackOnCollideGoal(this, 1.5, false));
        return prio;
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        return data;
    }

    @Override
    public @Nullable LOTRMiniQuest createMiniQuest() {
        return LOTRMiniQuestFactory.RUFFIAN_BRUTE.createQuest(this);
    }

    @Override
    public LOTRAchievement getKillAchievement() {
        return LOTRAchievement.KILL_RUFFIAN_BRUTE;
    }
}
