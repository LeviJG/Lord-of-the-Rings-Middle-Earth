package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.halftroll;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.shield.LOTRShields;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityHalfTrollWarrior: a half-troll a little quicker on its feet, in
 * half-troll armour (the helmet three times in four), with a half-troll
 * battleaxe, warhammer, mace, scimitar, dagger (poisoned or not) or pike. One
 * in twelve rides a rhino, half the time barded.
 */
public class LOTRHalfTrollWarriorEntity extends LOTRHalfTrollEntity {

    private static final Item[] WEAPONS = {LOTRCombatItems.HALF_TROLL_BATTLEAXE, LOTRCombatItems.HALF_TROLL_WARHAMMER,
            LOTRCombatItems.HALF_TROLL_MACE, LOTRCombatItems.HALF_TROLL_SCIMITAR, LOTRCombatItems.HALF_TROLL_DAGGER,
            LOTRCombatItems.POISONED_HALF_TROLL_DAGGER, LOTRCombatItems.HALF_TROLL_PIKE};

    public LOTRHalfTrollWarriorEntity(EntityType<? extends LOTRHalfTrollWarriorEntity> type, Level level) {
        super(type, level);
        this.npcShield = LOTRShields.ALIGNMENT_HALF_TROLL;
        this.spawnRidingHorse = this.random.nextInt(12) == 0;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LOTRHalfTrollEntity.createAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.24);
    }

    /** createMountToRide: a rhino, half the time in half-troll barding. */
    @Override
    public Mob createMountToRide(ServerLevel level) {
        Mob rhino = LOTREntities.RHINO.create(level, EntitySpawnReason.JOCKEY);
        if (rhino != null && this.random.nextBoolean()) {
            rhino.setItemSlot(EquipmentSlot.BODY, new ItemStack(LOTRCombatItems.HALF_TROLL_RHINO_ARMOR));
        }
        return rhino;
    }

    @Override
    public float getAlignmentBonus() {
        return 2.0f;
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(WEAPONS[this.random.nextInt(WEAPONS.length)]));
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        setItemSlot(EquipmentSlot.FEET, new ItemStack(LOTRCombatItems.HALF_TROLL_BOOTS));
        setItemSlot(EquipmentSlot.LEGS, new ItemStack(LOTRCombatItems.HALF_TROLL_LEGGINGS));
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(LOTRCombatItems.HALF_TROLL_CHESTPLATE));
        if (this.random.nextInt(4) != 0) {
            setItemSlot(EquipmentSlot.HEAD, new ItemStack(LOTRCombatItems.HALF_TROLL_HELMET));
        }
        return data;
    }
}
