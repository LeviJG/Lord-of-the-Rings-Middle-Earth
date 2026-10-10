package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.rohan;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRPlayerAchievements;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRInvasions;

/**
 * LOTREntityRohirrimMarshal: a warrior who leads the éored, in marshal's
 * armour with a Rohirric sword. Unlike his riders he seeks no one out -- he
 * only answers attacks.
 *
 * He hires out warriors, bowmen (either mounted) and banner bearers to those
 * at +150 or better.
 */
public class LOTRRohirrimMarshalEntity extends LOTRRohirrimWarriorEntity implements LOTRUnitTradeable {

    public LOTRRohirrimMarshalEntity(EntityType<? extends LOTRRohirrimMarshalEntity> type, Level level) {
        super(type, level);
        this.npcCape = LOTRCapes.ROHAN;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LOTRRohirrimWarriorEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 25.0);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        addTargetTasks(false);
    }

    @Override
    public LOTRUnitTradeEntries getUnits() {
        return LOTRUnitTradeEntries.ROHIRRIM_MARSHAL;
    }

    @Override
    public LOTRInvasions getWarhorn() {
        return LOTRInvasions.ROHAN;
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
            return canTradeWith(player) ? "rohan/marshal/friendly" : "rohan/marshal/neutral";
        }
        return "rohan/warrior/hostile";
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.ROHIRRIC_SWORD));
        this.npcItemsInv.setMeleeWeaponMounted(this.npcItemsInv.getMeleeWeapon().copy());
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        this.npcItemsInv.setIdleItemMounted(this.npcItemsInv.getMeleeWeaponMounted().copy());
        setItemSlot(EquipmentSlot.FEET, new ItemStack(LOTRCombatItems.ROHIRRIC_MARSHAL_BOOTS));
        setItemSlot(EquipmentSlot.LEGS, new ItemStack(LOTRCombatItems.ROHIRRIC_MARSHAL_LEGGINGS));
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(LOTRCombatItems.ROHIRRIC_MARSHAL_CHESTPLATE));
        setItemSlot(EquipmentSlot.HEAD, new ItemStack(LOTRCombatItems.ROHIRRIC_MARSHAL_HELMET));
        return data;
    }

    @Override
    public void onUnitTrade(Player player) {
        LOTRPlayerAchievements.addAchievement(player, LOTRAchievement.TRADE_ROHIRRIM_MARSHAL);
    }
}
