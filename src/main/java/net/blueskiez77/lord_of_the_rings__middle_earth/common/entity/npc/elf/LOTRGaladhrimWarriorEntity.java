package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.elf;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
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
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityGaladhrimWarrior: a warrior of Lothlórien in Galadhrim armour --
 * nine in ten with its helmet -- with an elven sword, or now and then a
 * battlestaff or longspear, and a Galadhrim bow shooting from 24 blocks;
 * one in five carries a spear as well, one in four rides a barded horse.
 *
 * <p>NOT ported yet: the Galadhrim shield (LOTRShields.ALIGNMENT_GALADHRIM,
 * D7); warriors called out against a player felling mallorn in Lothlórien
 * ("DefendingTree", with the biomes) and the takeMallornWood achievement for
 * killing one (D7); and throwing the spear (spears keep vanilla's mechanics,
 * user).
 */
public class LOTRGaladhrimWarriorEntity extends LOTRGaladhrimElfEntity {

    /** Called out to defend Lothlórien's trees. */
    public boolean isDefendingTree;

    public LOTRGaladhrimWarriorEntity(EntityType<? extends LOTRGaladhrimWarriorEntity> type, Level level) {
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
    protected Goal createElfMeleeAttackAI() {
        return new LOTRAttackOnCollideGoal(this, 1.4, false);
    }

    @Override
    protected Goal createElfRangedAttackAI() {
        return new LOTRRangedAttackGoal(this, 1.25, 30, 40, 24.0f);
    }

    @Override
    public float getAlignmentBonus() {
        return 2.0f;
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        if (isFriendly(player)) {
            return this.hiredNPCInfo.getHiringPlayer() == player ? "galadhrim/elf/hired" : "galadhrim/warrior/friendly";
        }
        return "galadhrim/warrior/hostile";
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("DefendingTree", this.isDefendingTree);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.isDefendingTree = input.getBooleanOr("DefendingTree", false);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        int i = this.random.nextInt(6);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(i == 0 ? LOTRCombatItems.GALADHRIM_BATTLESTAFF
                : i == 1 ? LOTRCombatItems.GALADHRIM_LONGSPEAR : LOTRCombatItems.GALADHRIM_SWORD));
        this.npcItemsInv.setRangedWeapon(new ItemStack(LOTRCombatItems.GALADHRIM_BOW));
        if (this.random.nextInt(5) == 0) {
            this.npcItemsInv.setSpearBackup(this.npcItemsInv.getMeleeWeapon().copy());
            this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.GALADHRIM_SPEAR));
        }
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        setItemSlot(EquipmentSlot.FEET, new ItemStack(LOTRCombatItems.GALADHRIM_BOOTS));
        setItemSlot(EquipmentSlot.LEGS, new ItemStack(LOTRCombatItems.GALADHRIM_LEGGINGS));
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(LOTRCombatItems.GALADHRIM_CHESTPLATE));
        if (this.random.nextInt(10) != 0) {
            setItemSlot(EquipmentSlot.HEAD, new ItemStack(LOTRCombatItems.GALADHRIM_HELMET));
        }
        return data;
    }
}
