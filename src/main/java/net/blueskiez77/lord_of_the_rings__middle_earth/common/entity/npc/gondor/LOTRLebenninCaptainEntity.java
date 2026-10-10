package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.gondor;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRPlayerAchievements;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRCapes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRUnitTradeEntries;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.hire.LOTRUnitTradeable;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.shield.LOTRShields;

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
 * LOTREntityLebenninCaptain (the Lebennin Levymaster): a soldier of Gondor
 * who leads Lebennin's levies, bare-headed with a Gondorian sword. He seeks no
 * one out -- he only answers attacks.
 *
 * He hires out Lebennin levymen, soldiers, archers and banner bearers to
 * those at +150 or better.
 */
public class LOTRLebenninCaptainEntity extends LOTRGondorSoldierEntity implements LOTRUnitTradeable {

    public LOTRLebenninCaptainEntity(EntityType<? extends LOTRLebenninCaptainEntity> type, Level level) {
        super(type, level);
        this.npcShield = LOTRShields.ALIGNMENT_LEBENNIN;
        this.npcCape = LOTRCapes.GONDOR;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LOTRGondorSoldierEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 25.0);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        addTargetTasks(false);
    }

    @Override
    protected Goal createGondorAttackAI() {
        return new LOTRAttackOnCollideGoal(this, 1.6, false);
    }

    @Override
    public LOTRUnitTradeEntries getUnits() {
        return LOTRUnitTradeEntries.LEBENNIN_CAPTAIN;
    }

    @Override
    public LOTRInvasions getWarhorn() {
        return LOTRInvasions.GONDOR_LEBENNIN;
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
            return canTradeWith(player) ? "gondor/lebenninCaptain/friendly" : "gondor/lebenninCaptain/neutral";
        }
        return "gondor/soldier/hostile";
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.GONDOR_SWORD));
        this.npcItemsInv.setMeleeWeaponMounted(this.npcItemsInv.getMeleeWeapon().copy());
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        this.npcItemsInv.setIdleItemMounted(this.npcItemsInv.getMeleeWeaponMounted().copy());
        setItemSlot(EquipmentSlot.FEET, new ItemStack(LOTRCombatItems.GONDOR_BOOTS));
        setItemSlot(EquipmentSlot.LEGS, new ItemStack(LOTRCombatItems.GONDOR_LEGGINGS));
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(LOTRCombatItems.GONDOR_CHESTPLATE));
        setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
        return data;
    }

    @Override
    public void onUnitTrade(Player player) {
        LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.TRADE_LEBENNIN_CAPTAIN);
    }
}
