package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.gondor;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRCapes;
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
 * LOTREntityPinnathGelinSoldier: a soldier of Pinnath Gelin with a Gondorian
 * sword or pike, one in eight mounted; one in ten goes bare-headed.
 *
 * <p>NOT ported yet: the Pinnath Gelin shield and cape (LOTRShields.ALIGNMENT_PINNATH_GELIN,
 * LOTRCapes.PINNATH_GELIN).
 */
public class LOTRPinnathGelinSoldierEntity extends LOTRGondorSoldierEntity {

    public LOTRPinnathGelinSoldierEntity(EntityType<? extends LOTRPinnathGelinSoldierEntity> type, Level level) {
        super(type, level);
        this.npcCape = LOTRCapes.PINNATH_GELIN;
        this.spawnRidingHorse = this.random.nextInt(8) == 0;
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
        this.npcItemsInv.setMeleeWeapon(new ItemStack(this.random.nextInt(2) == 0
                ? LOTRCombatItems.GONDOR_SWORD : LOTRCombatItems.GONDOR_PIKE));
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        setItemSlot(EquipmentSlot.FEET, new ItemStack(LOTRCombatItems.PINNATH_GELIN_BOOTS));
        setItemSlot(EquipmentSlot.LEGS, new ItemStack(LOTRCombatItems.PINNATH_GELIN_LEGGINGS));
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(LOTRCombatItems.PINNATH_GELIN_CHESTPLATE));
        setItemSlot(EquipmentSlot.HEAD, this.random.nextInt(10) == 0
                ? ItemStack.EMPTY : new ItemStack(LOTRCombatItems.PINNATH_GELIN_HELMET));
        return data;
    }
}
