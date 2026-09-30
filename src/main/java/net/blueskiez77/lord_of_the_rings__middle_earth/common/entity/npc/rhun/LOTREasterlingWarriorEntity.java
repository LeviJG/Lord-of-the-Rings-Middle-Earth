package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.rhun;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCAttributes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityEasterlingWarrior: a soldier of Rhûn in full Rhûnic armour, with
 * a Rhûnic sword or battleaxe (three in ten each), bardiche, dagger (poisoned
 * or not) or pike, and one in five a Rhûnic spear as well. One in six rides
 * out.
 */
public class LOTREasterlingWarriorEntity extends LOTREasterlingLevymanEntity {

    private static final Item[] WEAPONS = {LOTRCombatItems.RHUNIC_SWORD, LOTRCombatItems.RHUNIC_SWORD,
            LOTRCombatItems.RHUNIC_SWORD, LOTRCombatItems.RHUNIC_BATTLEAXE, LOTRCombatItems.RHUNIC_BATTLEAXE,
            LOTRCombatItems.RHUNIC_BATTLEAXE, LOTRCombatItems.RHUNIC_BARDICHE, LOTRCombatItems.RHUNIC_DAGGER,
            LOTRCombatItems.POISONED_RHUNIC_DAGGER, LOTRCombatItems.RHUNIC_PIKE};

    public LOTREasterlingWarriorEntity(EntityType<? extends LOTREasterlingWarriorEntity> type, Level level) {
        super(type, level);
        this.spawnRidingHorse = this.random.nextInt(6) == 0;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LOTREasterlingEntity.createAttributes()
                .add(LOTRNPCAttributes.NPC_RANGED_ACCURACY, 0.75);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(WEAPONS[this.random.nextInt(WEAPONS.length)]));
        if (this.random.nextInt(5) == 0) {
            this.npcItemsInv.setSpearBackup(this.npcItemsInv.getMeleeWeapon().copy());
            this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.RHUNIC_SPEAR));
        }
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        setItemSlot(EquipmentSlot.FEET, new ItemStack(LOTRCombatItems.RHUNIC_BOOTS));
        setItemSlot(EquipmentSlot.LEGS, new ItemStack(LOTRCombatItems.RHUNIC_LEGGINGS));
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(LOTRCombatItems.RHUNIC_CHESTPLATE));
        setItemSlot(EquipmentSlot.HEAD, new ItemStack(LOTRCombatItems.RHUNIC_HELMET));
        return data;
    }
}
