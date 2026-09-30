package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.gondor;

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
 * LOTREntityBlackrootSoldier (the Blackroot Vale Soldier): a soldier of the
 * Blackroot Vale with a Gondorian sword, one in ten mounted; one in ten goes
 * bare-headed.
 *
 * <p>NOT ported yet: the Blackroot Vale shield (LOTRShields.ALIGNMENT_BLACKROOT_VALE, D7).
 */
public class LOTRBlackrootSoldierEntity extends LOTRGondorSoldierEntity {

    public LOTRBlackrootSoldierEntity(EntityType<? extends LOTRBlackrootSoldierEntity> type, Level level) {
        super(type, level);
        this.spawnRidingHorse = this.random.nextInt(10) == 0;
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
        this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.GONDOR_SWORD));
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        setItemSlot(EquipmentSlot.FEET, new ItemStack(LOTRCombatItems.BLACKROOT_VALE_BOOTS));
        setItemSlot(EquipmentSlot.LEGS, new ItemStack(LOTRCombatItems.BLACKROOT_VALE_LEGGINGS));
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(LOTRCombatItems.BLACKROOT_VALE_CHESTPLATE));
        setItemSlot(EquipmentSlot.HEAD, this.random.nextInt(10) == 0
                ? ItemStack.EMPTY : new ItemStack(LOTRCombatItems.BLACKROOT_VALE_HELMET));
        return data;
    }
}
