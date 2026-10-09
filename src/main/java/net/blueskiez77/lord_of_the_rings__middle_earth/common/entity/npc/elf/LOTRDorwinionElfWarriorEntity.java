package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.elf;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.shield.LOTRShields;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityDorwinionElfWarrior (the Warrior of Bladorthin): in Dorwinion's
 * elven armour -- nine in ten helmeted -- with a Dorwinion elven sword, one
 * in five with a spear of Bladorthin besides, or else the spear alone.
 *
 * <p>NOT ported yet: the Dorwinion elf shield
 * (LOTRShields.ALIGNMENT_DORWINION_ELF, D7), and throwing the spear (spears
 * keep vanilla's mechanics, user).
 */
public class LOTRDorwinionElfWarriorEntity extends LOTRDorwinionElfEntity {

    public LOTRDorwinionElfWarriorEntity(EntityType<? extends LOTRDorwinionElfWarriorEntity> type, Level level) {
        super(type, level);
        this.npcShield = LOTRShields.ALIGNMENT_DORWINION_ELF;
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
    public float getAlignmentBonus() {
        return 3.0f;
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        if (isFriendly(player)) {
            return this.hiredNPCInfo.getHiringPlayer() == player ? "dorwinion/elfWarrior/hired" : "dorwinion/elfWarrior/friendly";
        }
        return "dorwinion/elfWarrior/hostile";
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        if (this.random.nextInt(2) == 0) {
            this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.DORWINION_ELVEN_SWORD));
            if (this.random.nextInt(5) == 0) {
                this.npcItemsInv.setSpearBackup(this.npcItemsInv.getMeleeWeapon().copy());
                this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.BLADORTHIN_SPEAR));
            }
        } else {
            this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.BLADORTHIN_SPEAR));
            this.npcItemsInv.setSpearBackup(ItemStack.EMPTY);
        }
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        setItemSlot(EquipmentSlot.FEET, new ItemStack(LOTRCombatItems.DORWINION_ELVEN_BOOTS));
        setItemSlot(EquipmentSlot.LEGS, new ItemStack(LOTRCombatItems.DORWINION_ELVEN_LEGGINGS));
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(LOTRCombatItems.DORWINION_ELVEN_CHESTPLATE));
        if (this.random.nextInt(10) != 0) {
            setItemSlot(EquipmentSlot.HEAD, new ItemStack(LOTRCombatItems.DORWINION_ELVEN_HELMET));
        }
        return data;
    }
}
