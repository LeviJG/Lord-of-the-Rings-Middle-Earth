package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.gondor;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.shield.LOTRShields;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityPelargirMarine: a marine of Pelargir, always on foot, in full
 * Pelargir armour with a trident or an eket.
 */
public class LOTRPelargirMarineEntity extends LOTRGondorSoldierEntity {

    public LOTRPelargirMarineEntity(EntityType<? extends LOTRPelargirMarineEntity> type, Level level) {
        super(type, level);
        this.npcShield = LOTRShields.ALIGNMENT_PELARGIR;
        this.spawnRidingHorse = false;
    }

    /** No mounted weapons: the same weapon, riding or not. */
    @Override
    public void onAttackModeChange(AttackMode mode, boolean mounted) {
        setItemSlot(EquipmentSlot.MAINHAND,
                mode == AttackMode.IDLE ? this.npcItemsInv.getIdleItem() : this.npcItemsInv.getMeleeWeapon());
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(this.random.nextBoolean()
                ? LOTRCombatItems.PELARGIR_TRIDENT : LOTRCombatItems.PELARGIR_EKET));
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        setItemSlot(EquipmentSlot.FEET, new ItemStack(LOTRCombatItems.PELARGIR_BOOTS));
        setItemSlot(EquipmentSlot.LEGS, new ItemStack(LOTRCombatItems.PELARGIR_LEGGINGS));
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(LOTRCombatItems.PELARGIR_CHESTPLATE));
        setItemSlot(EquipmentSlot.HEAD, new ItemStack(LOTRCombatItems.PELARGIR_HELMET));
        return data;
    }
}
