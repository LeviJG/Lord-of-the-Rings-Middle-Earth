package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRPlayerAchievements;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRCapes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRUnitTradeEntries;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRUnitTradeable;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRInvasions;

/**
 * LOTREntityNearHaradrimWarlord (the Southron Warlord): a warrior who leads,
 * in his own skin, with an Umbaric poleaxe and a Southron champion's helmet.
 * He seeks no one out -- he only answers attacks. He hires out warriors,
 * archers, mounted champions (pledged only) and banner bearers to those at
 * +150 or better.
 */
public class LOTRNearHaradrimWarlordEntity extends LOTRNearHaradrimWarriorEntity implements LOTRUnitTradeable {

    public LOTRNearHaradrimWarlordEntity(EntityType<? extends LOTRNearHaradrimWarlordEntity> type, Level level) {
        super(type, level);
        this.npcCape = LOTRCapes.NEAR_HARAD;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LOTRNearHaradrimWarriorEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 25.0);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        addTargetTasks(false);
    }

    @Override
    protected Goal createHaradrimAttackAI() {
        return new LOTRAttackOnCollideGoal(this, 1.6, false);
    }

    @Override
    public LOTRUnitTradeEntries getUnits() {
        return LOTRUnitTradeEntries.NEAR_HARADRIM_WARLORD;
    }

    @Override
    public LOTRInvasions getWarhorn() {
        return LOTRInvasions.NEAR_HARAD_COAST;
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
            return canTradeWith(player) ? "nearHarad/coast/warlord/friendly" : "nearHarad/coast/warlord/neutral";
        }
        return "nearHarad/coast/warrior/hostile";
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.UMBARIC_POLEAXE));
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        setItemSlot(EquipmentSlot.HEAD, new ItemStack(LOTRCombatItems.SOUTHRON_CHAMPION_HELMET));
        return data;
    }

    @Override
    public void onUnitTrade(Player player) {
        LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.TRADE_NEAR_HARAD_WARLORD);
    }
}
