package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.elf;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRRangedAttackGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
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
 * LOTREntityRivendellWarrior: a warrior of Rivendell in its people's armour -- nine
 * in ten with the helmet -- with a sword, or now and then a battlestaff or
 * longspear, and a bow shooting quickly from 24 blocks; one in five carries a
 * spear as well, one in four rides a barded horse.
 *
 * <p>NOT ported yet: the Rivendell shield (LOTRShields.ALIGNMENT_RIVENDELL, D7), and
 * throwing the spear (spears keep vanilla's mechanics, user).
 */
public class LOTRRivendellWarriorEntity extends LOTRRivendellElfEntity {

    public LOTRRivendellWarriorEntity(EntityType<? extends LOTRRivendellWarriorEntity> type, Level level) {
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
        this.goalSelector.addGoal(2, meleeAttackAI());
    }

    @Override
    protected Goal createElfRangedAttackAI() {
        return new LOTRRangedAttackGoal(this, 1.25, 25, 40, 24.0f);
    }

    @Override
    public float getAlignmentBonus() {
        return 2.0f;
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        if (isFriendly(player)) {
            return this.hiredNPCInfo.getHiringPlayer() == player ? "rivendell/elf/hired" : "rivendell/warrior/friendly";
        }
        return "rivendell/warrior/hostile";
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        int i = this.random.nextInt(6);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(i == 0 ? LOTRCombatItems.RIVENDELL_BATTLESTAFF
                : i == 1 ? LOTRCombatItems.RIVENDELL_LONGSPEAR : LOTRCombatItems.RIVENDELL_SWORD));
        this.npcItemsInv.setRangedWeapon(new ItemStack(LOTRCombatItems.RIVENDELL_BOW));
        if (this.random.nextInt(5) == 0) {
            this.npcItemsInv.setSpearBackup(this.npcItemsInv.getMeleeWeapon().copy());
            this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.RIVENDELL_SPEAR));
        }
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        setItemSlot(EquipmentSlot.FEET, new ItemStack(LOTRCombatItems.RIVENDELL_BOOTS));
        setItemSlot(EquipmentSlot.LEGS, new ItemStack(LOTRCombatItems.RIVENDELL_LEGGINGS));
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(LOTRCombatItems.RIVENDELL_CHESTPLATE));
        if (this.random.nextInt(10) != 0) {
            setItemSlot(EquipmentSlot.HEAD, new ItemStack(LOTRCombatItems.RIVENDELL_HELMET));
        }
        return data;
    }
}
