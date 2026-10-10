package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.angmar;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.orc.LOTROrcEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRToolItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.quest.LOTRMiniQuest;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.quest.LOTRMiniQuestFactory;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityAngmarOrc: an orc of Angmar in Angmar armour (bare-headed one
 * time in five), with a sword (three times in ten), battleaxe, dagger
 * (poisoned or not), warhammer, pickaxe, axe or poleaxe; one in six carries a
 * spear as well, falling back on the other. Now and then it leaves something
 * from an Angmar tent.
 */
public class LOTRAngmarOrcEntity extends LOTROrcEntity {

    public LOTRAngmarOrcEntity(EntityType<? extends LOTRAngmarOrcEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected Goal createOrcAttackAI() {
        return new LOTRAttackOnCollideGoal(this, 1.4, false);
    }

    @Override
    public LOTRFaction getFaction() {
        return LOTRFaction.ANGMAR;
    }

    @Override
    public float getAlignmentBonus() {
        return 1.0f;
    }

    @Override
    public String getOrcSkirmishSpeech() {
        return "angmar/orc/skirmish";
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        if (isFriendly(player)) {
            if (this.hiredNPCInfo.getHiringPlayer() == player) {
                return "angmar/orc/hired";
            }
            return LOTRPlayerAlignments.getAlignment(player, getFaction()) >= 100.0f
                    ? "angmar/orc/friendly" : "angmar/orc/neutral";
        }
        return "angmar/orc/hostile";
    }

    @Override
    protected void dropOrcItems(ServerLevel level, boolean killedByPlayer, int looting) {
        if (this.random.nextInt(6) == 0) {
            dropChestContents(level, LOTRChestContents.ANGMAR_TENT, 1, 2 + looting);
        }
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(switch (this.random.nextInt(10)) {
            case 0, 1, 2 -> LOTRCombatItems.ANGMAR_SWORD;
            case 3 -> LOTRCombatItems.ANGMAR_BATTLEAXE;
            case 4 -> LOTRCombatItems.ANGMAR_DAGGER;
            case 5 -> LOTRCombatItems.POISONED_ANGMAR_DAGGER;
            case 6 -> LOTRCombatItems.ANGMAR_WARHAMMER;
            case 7 -> LOTRToolItems.ANGMAR_PICKAXE;
            case 8 -> LOTRToolItems.ANGMAR_AXE;
            default -> LOTRCombatItems.ANGMAR_POLEAXE;
        }));
        if (this.random.nextInt(6) == 0) {
            this.npcItemsInv.setSpearBackup(this.npcItemsInv.getMeleeWeapon().copy());
            this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.ANGMAR_SPEAR));
        }
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        setItemSlot(EquipmentSlot.FEET, new ItemStack(LOTRCombatItems.ANGMAR_BOOTS));
        setItemSlot(EquipmentSlot.LEGS, new ItemStack(LOTRCombatItems.ANGMAR_LEGGINGS));
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(LOTRCombatItems.ANGMAR_CHESTPLATE));
        if (this.random.nextInt(5) != 0) {
            setItemSlot(EquipmentSlot.HEAD, new ItemStack(LOTRCombatItems.ANGMAR_HELMET));
        }
        return data;
    }

    @Override
    public @Nullable LOTRMiniQuest createMiniQuest() {
        return LOTRMiniQuestFactory.ANGMAR.createQuest(this);
    }

    @Override
    public LOTRAchievement getKillAchievement() {
        return LOTRAchievement.KILL_ANGMAR_ORC;
    }
}
