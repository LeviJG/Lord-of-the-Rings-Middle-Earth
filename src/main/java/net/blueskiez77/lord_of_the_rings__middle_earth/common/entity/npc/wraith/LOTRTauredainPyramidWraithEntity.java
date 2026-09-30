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

/** LOTREntityTauredainPyramidWraith: a wraith of the Taurethrim pyramids, with a Taurethrim sword (one in three), dagger (poisoned or not), bludgeon or battleaxe, in Taurethrim armour but for the helmet. */
public class LOTRTauredainPyramidWraithEntity extends LOTRSkeletalWraithEntity {
    private static final Item[] WEAPONS = {LOTRCombatItems.TAURETHRIM_SWORD, LOTRCombatItems.TAURETHRIM_SWORD,
            LOTRCombatItems.TAURETHRIM_DAGGER, LOTRCombatItems.POISONED_TAURETHRIM_DAGGER,
            LOTRCombatItems.TAURETHRIM_BLUDGEON, LOTRCombatItems.TAURETHRIM_BATTLEAXE};

    public LOTRTauredainPyramidWraithEntity(EntityType<? extends LOTRTauredainPyramidWraithEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(WEAPONS[this.random.nextInt(WEAPONS.length)]));
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        wear(LOTRCombatItems.TAURETHRIM_BOOTS, LOTRCombatItems.TAURETHRIM_LEGGINGS,
                LOTRCombatItems.TAURETHRIM_CHESTPLATE);
        return data;
    }

    private void wear(Item boots, Item legs, Item body) {
        setItemSlot(EquipmentSlot.FEET, new ItemStack(boots));
        setItemSlot(EquipmentSlot.LEGS, new ItemStack(legs));
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(body));
    }
}
