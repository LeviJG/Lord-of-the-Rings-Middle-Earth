package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.farharad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityMoredainWarrior: a Moredain warrior in Moredain armour, the
 * helmet one time in three. Four times in five he carries a Moredain weapon,
 * else half the time an Umbaric one and half a Haradric bronze one; one in
 * three has a Moredain spear as well. One in ten rides out on a zebra.
 *
 * <p>NOT ported yet: the Moredain shield (LOTRShields.ALIGNMENT_MOREDAIN, D7).
 */
public class LOTRMoredainWarriorEntity extends LOTRMoredainEntity {

    private static final Item[] WEAPONS_MOREDAIN = {LOTRCombatItems.MORWAITH_BATTLEAXE, LOTRCombatItems.MORWAITH_BATTLEAXE,
            LOTRCombatItems.MORWAITH_DAGGER, LOTRCombatItems.POISONED_MORWAITH_DAGGER, LOTRCombatItems.MORWAITH_CLUB,
            LOTRCombatItems.MORWAITH_CLUB, LOTRCombatItems.MORWAITH_SPEAR, LOTRCombatItems.MORWAITH_SPEAR,
            LOTRCombatItems.MORWAITH_SWORD, LOTRCombatItems.MORWAITH_SWORD};
    private static final Item[] WEAPONS_IRON = {LOTRCombatItems.UMBARIC_SCIMITAR, LOTRCombatItems.UMBARIC_DAGGER,
            LOTRCombatItems.UMBARIC_POLEAXE, LOTRCombatItems.UMBARIC_MACE, LOTRCombatItems.UMBARIC_SPEAR};
    private static final Item[] WEAPONS_BRONZE = {LOTRCombatItems.HARADRIC_SWORD, LOTRCombatItems.HARADRIC_DAGGER,
            LOTRCombatItems.HARADRIC_SPEAR};

    public LOTRMoredainWarriorEntity(EntityType<? extends LOTRMoredainWarriorEntity> type, Level level) {
        super(type, level);
        this.spawnRidingHorse = this.random.nextInt(10) == 0;
    }

    @Override
    protected Goal createHaradrimAttackAI() {
        return new LOTRAttackOnCollideGoal(this, 1.7, true);
    }

    @Override
    public void setupNPCGender() {
        this.familyInfo.setMale(true);
    }

    @Override
    public float getAlignmentBonus() {
        return 2.0f;
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        Item[] weapons = this.random.nextInt(5) == 0
                ? (this.random.nextBoolean() ? WEAPONS_IRON : WEAPONS_BRONZE) : WEAPONS_MOREDAIN;
        this.npcItemsInv.setMeleeWeapon(new ItemStack(weapons[this.random.nextInt(weapons.length)]));
        if (this.random.nextInt(3) == 0) {
            this.npcItemsInv.setSpearBackup(this.npcItemsInv.getMeleeWeapon().copy());
            this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.MORWAITH_SPEAR));
        }
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        setItemSlot(EquipmentSlot.FEET, new ItemStack(LOTRCombatItems.MORWAITH_BOOTS));
        setItemSlot(EquipmentSlot.LEGS, new ItemStack(LOTRCombatItems.MORWAITH_LEGGINGS));
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(LOTRCombatItems.MORWAITH_CHESTPLATE));
        if (this.random.nextInt(3) == 0) {
            setItemSlot(EquipmentSlot.HEAD, new ItemStack(LOTRCombatItems.MORWAITH_HELMET));
        }
        return data;
    }
}
