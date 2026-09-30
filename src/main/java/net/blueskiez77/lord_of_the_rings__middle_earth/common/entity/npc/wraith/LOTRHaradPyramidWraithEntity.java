package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.wraith;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/** LOTREntityHaradPyramidWraith: a wraith of the Harad pyramids, 30 strong, with a poisoned Umbaric dagger in coast armour or a poisoned Haradric one in Gulfen armour, but no helmet. The scorpions let it be. */
public class LOTRHaradPyramidWraithEntity extends LOTRSkeletalWraithEntity {

    public LOTRHaradPyramidWraithEntity(EntityType<? extends LOTRHaradPyramidWraithEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LOTRSkeletalWraithEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 30.0);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        if (this.random.nextBoolean()) {
            this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.POISONED_UMBARIC_DAGGER));
            this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
            wear(LOTRCombatItems.COAST_SOUTHRON_BOOTS, LOTRCombatItems.COAST_SOUTHRON_LEGGINGS,
                    LOTRCombatItems.COAST_SOUTHRON_CHESTPLATE);
        } else {
            this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.POISONED_HARADRIC_DAGGER));
            this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
            wear(LOTRCombatItems.GULFEN_BOOTS, LOTRCombatItems.GULFEN_LEGGINGS, LOTRCombatItems.GULFEN_CHESTPLATE);
        }
        return data;
    }

    private void wear(Item boots, Item legs, Item body) {
        setItemSlot(EquipmentSlot.FEET, new ItemStack(boots));
        setItemSlot(EquipmentSlot.LEGS, new ItemStack(legs));
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(body));
    }
}
