package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.gondor;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityLamedonSoldier (the Lamedon Warrior): a warrior of Lamedon with a
 * Gondorian sword, warhammer or pike, one in six on a horse in Lamedon
 * barding; one in ten goes bare-headed.
 *
 * <p>NOT ported yet: the Lamedon shield and cape (LOTRShields.ALIGNMENT_LAMEDON,
 * LOTRCapes.LAMEDON).
 */
public class LOTRLamedonSoldierEntity extends LOTRGondorSoldierEntity {

    public LOTRLamedonSoldierEntity(EntityType<? extends LOTRLamedonSoldierEntity> type, Level level) {
        super(type, level);
        this.spawnRidingHorse = this.random.nextInt(6) == 0;
    }

    @Override
    protected Goal createGondorAttackAI() {
        return new LOTRAttackOnCollideGoal(this, 1.5, false);
    }

    @Override
    public Mob createMountToRide(ServerLevel level) {
        Mob horse = super.createMountToRide(level);
        if (horse != null) {
            horse.setItemSlot(EquipmentSlot.BODY, new ItemStack(LOTRCombatItems.LAMEDON_HORSE_ARMOR));
        }
        return horse;
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
        this.npcItemsInv.setMeleeWeapon(new ItemStack(switch (this.random.nextInt(3)) {
            case 0 -> LOTRCombatItems.GONDOR_SWORD;
            case 1 -> LOTRCombatItems.GONDOR_WARHAMMER;
            default -> LOTRCombatItems.GONDOR_PIKE;
        }));
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        setItemSlot(EquipmentSlot.FEET, new ItemStack(LOTRCombatItems.LAMEDON_BOOTS));
        setItemSlot(EquipmentSlot.LEGS, new ItemStack(LOTRCombatItems.LAMEDON_LEGGINGS));
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(LOTRCombatItems.LAMEDON_CHESTPLATE));
        setItemSlot(EquipmentSlot.HEAD, this.random.nextInt(10) == 0
                ? ItemStack.EMPTY : new ItemStack(LOTRCombatItems.LAMEDON_HELMET));
        return data;
    }
}
