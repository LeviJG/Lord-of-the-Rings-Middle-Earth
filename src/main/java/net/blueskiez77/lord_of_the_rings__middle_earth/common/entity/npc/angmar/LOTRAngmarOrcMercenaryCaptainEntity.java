package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.angmar;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRPlayerAchievements;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRUnitTradeEntries;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRUnitTradeable;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.orc.LOTROrcEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRInvasions;

/**
 * LOTREntityAngmarOrcMercenaryCaptain (the Angmar Orc Chieftain): a man-sized
 * orc, no weak one, bare-headed in Angmar armour with a skull staff. He seeks
 * no one out -- he only answers attacks. He hires out Angmar's orcs, archers,
 * bombardiers, wargs (ridden, half the time barded), warg bombardiers and
 * banner bearers to those at +150 or better.
 */
public class LOTRAngmarOrcMercenaryCaptainEntity extends LOTRAngmarOrcEntity implements LOTRUnitTradeable {

    public LOTRAngmarOrcMercenaryCaptainEntity(EntityType<? extends LOTRAngmarOrcMercenaryCaptainEntity> type, Level level) {
        super(type, level);
        this.isWeakOrc = false;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LOTROrcEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 25.0);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        addTargetTasks(false);
    }

    @Override
    public LOTRUnitTradeEntries getUnits() {
        return LOTRUnitTradeEntries.ANGMAR_ORC_MERCENARY_CAPTAIN;
    }

    @Override
    public LOTRInvasions getWarhorn() {
        return LOTRInvasions.ANGMAR;
    }

    /** canTradeWith: +150 alignment and friendly. */
    @Override
    public boolean canTradeWith(Player player) {
        return LOTRPlayerAlignments.getAlignment(player, getFaction()) >= 150.0f && isFriendly(player);
    }

    @Override
    public float getAlignmentBonus() {
        return 5.0f;
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        if (isFriendly(player)) {
            return canTradeWith(player) ? "angmar/chieftain/friendly" : "angmar/chieftain/neutral";
        }
        return "angmar/orc/hostile";
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.ORC_SKULL_STAFF));
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        setItemSlot(EquipmentSlot.FEET, new ItemStack(LOTRCombatItems.ANGMAR_BOOTS));
        setItemSlot(EquipmentSlot.LEGS, new ItemStack(LOTRCombatItems.ANGMAR_LEGGINGS));
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(LOTRCombatItems.ANGMAR_CHESTPLATE));
        setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
        return data;
    }

    @Override
    public void onUnitTrade(Player player) {
        LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.TRADE_ANGMAR_CAPTAIN);
    }
}
