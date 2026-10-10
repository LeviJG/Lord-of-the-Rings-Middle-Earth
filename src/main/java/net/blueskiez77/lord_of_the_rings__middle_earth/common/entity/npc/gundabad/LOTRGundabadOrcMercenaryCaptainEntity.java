package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.gundabad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRPlayerAchievements;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRUnitTradeEntries;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRUnitTradeable;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;

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
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRInvasions;

/**
 * LOTREntityGundabadOrcMercenaryCaptain (the Gundabad Orc Chieftain): a
 * Gundabad Uruk, bare-headed with a skull staff. He seeks no one out -- he
 * only answers attacks. He hires out Gundabad's orcs and archers (either on
 * wargs), wargs, banner bearers, and Uruks and Uruk archers to those pledged
 * to Gundabad, at +100 or better.
 */
public class LOTRGundabadOrcMercenaryCaptainEntity extends LOTRGundabadUrukEntity implements LOTRUnitTradeable {

    public LOTRGundabadOrcMercenaryCaptainEntity(EntityType<? extends LOTRGundabadOrcMercenaryCaptainEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        addTargetTasks(false);
    }

    @Override
    public LOTRUnitTradeEntries getUnits() {
        return LOTRUnitTradeEntries.GUNDABAD_ORC_MERCENARY_CAPTAIN;
    }

    @Override
    public LOTRInvasions getWarhorn() {
        return LOTRInvasions.GUNDABAD;
    }

    /** canTradeWith: +100 alignment and friendly. */
    @Override
    public boolean canTradeWith(Player player) {
        return LOTRPlayerAlignments.getAlignment(player, getFaction()) >= 100.0f && isFriendly(player);
    }

    @Override
    public float getAlignmentBonus() {
        return 5.0f;
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        if (isFriendly(player)) {
            return canTradeWith(player) ? "gundabad/chieftain/friendly" : "gundabad/chieftain/neutral";
        }
        return "gundabad/orc/hostile";
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.ORC_SKULL_STAFF));
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
        return data;
    }

    @Override
    public void onUnitTrade(Player player) {
        LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.TRADE_GUNDABAD_CAPTAIN);
    }
}
