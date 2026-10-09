package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.rohan;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.shield.LOTRShields;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCAttributes;
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
 * LOTREntityRohirrimWarrior (registered as "Rohirrim", the Rohirrim
 * Warrior): a rider of the Mark in full Rohirric armour, seeking out Rohan's
 * enemies. One in three rides a barded horse. He fights with a Rohirric
 * sword, or one time in three a battleaxe; mounted, one in four couches a
 * lance; and one in four carries a spear as well, falling back on his other
 * weapon.
 *
 * <p>NOT ported yet: throwing the spear (spears keep vanilla's mechanics,
 * user).
 */
public class LOTRRohirrimWarriorEntity extends LOTRRohanManEntity {

    public LOTRRohirrimWarriorEntity(EntityType<? extends LOTRRohirrimWarriorEntity> type, Level level) {
        super(type, level);
        this.npcShield = LOTRShields.ALIGNMENT_ROHAN;
        this.spawnRidingHorse = this.random.nextInt(3) == 0;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LOTRRohanManEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(LOTRNPCAttributes.NPC_RANGED_ACCURACY, 0.75)
                .add(LOTRNPCAttributes.HORSE_ATTACK_SPEED, 2.0);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        addTargetTasks(true);
    }

    @Override
    protected Goal createRohanAttackAI() {
        return new LOTRAttackOnCollideGoal(this, 1.45, false);
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
    public @Nullable String getSpeechBank(Player player) {
        if (isFriendly(player)) {
            return this.hiredNPCInfo.getHiringPlayer() == player ? "rohan/warrior/hired" : "rohan/warrior/friendly";
        }
        return "rohan/warrior/hostile";
    }

    @Override
    public void onAttackModeChange(AttackMode mode, boolean mounted) {
        if (mode == AttackMode.IDLE) {
            setItemSlot(EquipmentSlot.MAINHAND,
                    mounted ? this.npcItemsInv.getIdleItemMounted() : this.npcItemsInv.getIdleItem());
        } else {
            setItemSlot(EquipmentSlot.MAINHAND,
                    mounted ? this.npcItemsInv.getMeleeWeaponMounted() : this.npcItemsInv.getMeleeWeapon());
        }
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(this.random.nextInt(3) == 0
                ? LOTRCombatItems.ROHIRRIC_BATTLEAXE : LOTRCombatItems.ROHIRRIC_SWORD));
        this.npcItemsInv.setMeleeWeaponMounted(this.random.nextInt(4) == 0
                ? new ItemStack(LOTRCombatItems.ROHIRRIC_LANCE) : this.npcItemsInv.getMeleeWeapon().copy());
        if (this.random.nextInt(4) == 0) {
            this.npcItemsInv.setSpearBackup(this.npcItemsInv.getMeleeWeapon().copy());
            this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.ROHIRRIC_SPEAR));
        }
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        this.npcItemsInv.setIdleItemMounted(this.npcItemsInv.getMeleeWeaponMounted().copy());
        setItemSlot(EquipmentSlot.FEET, new ItemStack(LOTRCombatItems.ROHIRRIC_BOOTS));
        setItemSlot(EquipmentSlot.LEGS, new ItemStack(LOTRCombatItems.ROHIRRIC_LEGGINGS));
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(LOTRCombatItems.ROHIRRIC_HAUBERK));
        setItemSlot(EquipmentSlot.HEAD, new ItemStack(LOTRCombatItems.ROHIRRIC_COIF));
        return data;
    }
}
