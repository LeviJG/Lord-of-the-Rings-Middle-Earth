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

/** LOTREntityRohanBarrowWraith: the wraith of a Marshal of the Mark, with a Rohirric sword, in marshal armour but for the helmet. */
public class LOTRRohanBarrowWraithEntity extends LOTRSkeletalWraithEntity {

    public LOTRRohanBarrowWraithEntity(EntityType<? extends LOTRRohanBarrowWraithEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.ROHIRRIC_SWORD));
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        wear(LOTRCombatItems.ROHIRRIC_MARSHAL_BOOTS, LOTRCombatItems.ROHIRRIC_MARSHAL_LEGGINGS,
                LOTRCombatItems.ROHIRRIC_MARSHAL_CHESTPLATE);
        return data;
    }

    private void wear(Item boots, Item legs, Item body) {
        setItemSlot(EquipmentSlot.FEET, new ItemStack(boots));
        setItemSlot(EquipmentSlot.LEGS, new ItemStack(legs));
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(body));
    }
}
