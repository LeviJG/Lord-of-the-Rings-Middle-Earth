package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.wraith;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/** LOTREntityGondorRuinsWraith: the wraith of a soldier of Gondor, with a Gondor sword, in Gondor armour but for the helmet. */
public class LOTRGondorRuinsWraithEntity extends LOTRSkeletalWraithEntity {

    public LOTRGondorRuinsWraithEntity(EntityType<? extends LOTRGondorRuinsWraithEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.GONDOR_SWORD));
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        wear(LOTRCombatItems.GONDOR_BOOTS, LOTRCombatItems.GONDOR_LEGGINGS, LOTRCombatItems.GONDOR_CHESTPLATE);
        return data;
    }

    private void wear(Item boots, Item legs, Item body) {
        setItemSlot(EquipmentSlot.FEET, new ItemStack(boots));
        setItemSlot(EquipmentSlot.LEGS, new ItemStack(legs));
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(body));
    }
}
