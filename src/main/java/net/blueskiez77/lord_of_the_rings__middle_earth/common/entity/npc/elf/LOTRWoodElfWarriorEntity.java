package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.elf;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRRangedAttackGoal;
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
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityWoodElfWarrior: a warrior of the Woodland Realm in Wood-elven
 * armour -- nine in ten with its helmet -- with a Wood-elven sword, or now
 * and then a battlestaff or longspear, up close, and a Mirkwood bow from 24
 * blocks; one in five carries a spear as well, one in four rides an elk in
 * Wood-elven barding.
 *
 * <p>NOT ported yet: the Wood-elf shield (LOTRShields.ALIGNMENT_WOOD_ELF, D7),
 * and throwing the spear (spears keep vanilla's mechanics, user).
 */
public class LOTRWoodElfWarriorEntity extends LOTRWoodElfEntity {

    public LOTRWoodElfWarriorEntity(EntityType<? extends LOTRWoodElfWarriorEntity> type, Level level) {
        super(type, level);
        this.spawnRidingHorse = this.random.nextInt(4) == 0;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LOTRElfEntity.createAttributes()
                .add(Attributes.FOLLOW_RANGE, 24.0);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.removeGoal(rangedAttackAI());
        this.goalSelector.addGoal(2, meleeAttackAI());
    }

    @Override
    protected Goal createElfMeleeAttackAI() {
        return new LOTRAttackOnCollideGoal(this, 1.4, false);
    }

    @Override
    protected Goal createElfRangedAttackAI() {
        return new LOTRRangedAttackGoal(this, 1.25, 25, 35, 24.0f);
    }

    /** createMountToRide: an elk in Wood-elven barding. */
    @Override
    public Mob createMountToRide(ServerLevel level) {
        Mob elk = LOTREntities.ELK.create(level, EntitySpawnReason.JOCKEY);
        if (elk != null) {
            elk.setItemSlot(EquipmentSlot.BODY, new ItemStack(LOTRCombatItems.WOOD_ELVEN_ELK_ARMOR));
        }
        return elk;
    }

    @Override
    public float getAlignmentBonus() {
        return 2.0f;
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        if (isFriendly(player)) {
            if (this.hiredNPCInfo.getHiringPlayer() == player) {
                return "woodElf/elf/hired";
            }
            return isTrusted(player) ? "woodElf/warrior/friendly" : "woodElf/elf/neutral";
        }
        return "woodElf/warrior/hostile";
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        int i = this.random.nextInt(6);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(i == 0 ? LOTRCombatItems.WOOD_ELVEN_BATTLESTAFF
                : i == 1 ? LOTRCombatItems.WOOD_ELVEN_LONGSPEAR : LOTRCombatItems.WOOD_ELVEN_SWORD));
        this.npcItemsInv.setRangedWeapon(new ItemStack(LOTRCombatItems.MIRKWOOD_BOW));
        if (this.random.nextInt(5) == 0) {
            this.npcItemsInv.setSpearBackup(this.npcItemsInv.getMeleeWeapon().copy());
            this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.WOOD_ELVEN_SPEAR));
        }
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        setItemSlot(EquipmentSlot.FEET, new ItemStack(LOTRCombatItems.WOOD_ELVEN_BOOTS));
        setItemSlot(EquipmentSlot.LEGS, new ItemStack(LOTRCombatItems.WOOD_ELVEN_LEGGINGS));
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(LOTRCombatItems.WOOD_ELVEN_CHESTPLATE));
        setItemSlot(EquipmentSlot.HEAD, this.random.nextInt(10) == 0
                ? ItemStack.EMPTY : new ItemStack(LOTRCombatItems.WOOD_ELVEN_HELMET));
        return data;
    }
}
