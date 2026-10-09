package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.rhun;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.shield.LOTRShields;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityEasterlingGoldWarrior (the Golden Warrior): an Easterling warrior
 * of 25 health in golden Rhûnic armour, whose horse wears Rhûnic barding.
 *
 * <p>NOT ported yet: the Rhûn shield (LOTRShields.ALIGNMENT_RHUN, D7).
 */
public class LOTREasterlingGoldWarriorEntity extends LOTREasterlingWarriorEntity {

    public LOTREasterlingGoldWarriorEntity(EntityType<? extends LOTREasterlingGoldWarriorEntity> type, Level level) {
        super(type, level);
        this.npcShield = LOTRShields.ALIGNMENT_RHUN;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LOTREasterlingWarriorEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 25.0);
    }

    /** createMountToRide: a horse in Rhûnic barding. */
    @Override
    public Mob createMountToRide(ServerLevel level) {
        Mob horse = super.createMountToRide(level);
        if (horse != null) {
            horse.setItemSlot(EquipmentSlot.BODY, new ItemStack(LOTRCombatItems.RHUNIC_HORSE_ARMOR));
        }
        return horse;
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        setItemSlot(EquipmentSlot.FEET, new ItemStack(LOTRCombatItems.GOLDEN_RHUNIC_BOOTS));
        setItemSlot(EquipmentSlot.LEGS, new ItemStack(LOTRCombatItems.GOLDEN_RHUNIC_LEGGINGS));
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(LOTRCombatItems.GOLDEN_RHUNIC_CHESTPLATE));
        setItemSlot(EquipmentSlot.HEAD, new ItemStack(LOTRCombatItems.GOLDEN_RHUNIC_HELMET));
        return data;
    }
}
