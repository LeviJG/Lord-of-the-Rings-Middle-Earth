package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.rohan;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.quest.LOTRMiniQuest;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.quest.LOTRMiniQuestFactory;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityRohanShieldmaiden: a warrior woman, always on foot, bare-headed
 * half the time. Her mini-quests are offered more often -- one chance in
 * 4000 -- but only to those at +150 or more.
 */
public class LOTRRohanShieldmaidenEntity extends LOTRRohirrimWarriorEntity {

    public LOTRRohanShieldmaidenEntity(EntityType<? extends LOTRRohanShieldmaidenEntity> type, Level level) {
        super(type, level);
        this.questInfo.setOfferChance(4000);
        this.questInfo.setMinAlignment(150.0f);
        this.spawnRidingHorse = false;
    }

    @Override
    public void setupNPCGender() {
        this.familyInfo.setMale(false);
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        if (isFriendly(player)) {
            return this.hiredNPCInfo.getHiringPlayer() == player ? "rohan/warrior/hired" : "rohan/shieldmaiden/friendly";
        }
        return "rohan/warrior/hostile";
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        setItemSlot(EquipmentSlot.HEAD, this.random.nextBoolean()
                ? new ItemStack(LOTRCombatItems.ROHIRRIC_COIF) : ItemStack.EMPTY);
        return data;
    }

    @Override
    public @Nullable LOTRMiniQuest createMiniQuest() {
        return LOTRMiniQuestFactory.ROHAN_SHIELDMAIDEN.createQuest(this);
    }
}
