package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.dunland;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.shield.LOTRShields;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityDunlendingWarrior: a Dunlending man-at-arms in Dunlending armour
 * (bare-headed one time in ten), with an iron or bronze sword, dagger or
 * battleaxe or an iron pike; one in five carries an iron or bronze spear as
 * well, falling back on the other.
 */
public class LOTRDunlendingWarriorEntity extends LOTRDunlendingEntity {

    public LOTRDunlendingWarriorEntity(EntityType<? extends LOTRDunlendingWarriorEntity> type, Level level) {
        super(type, level);
        this.npcShield = LOTRShields.ALIGNMENT_DUNLAND;
    }

    @Override
    protected Goal createDunlendingAttackAI() {
        return new LOTRAttackOnCollideGoal(this, 1.6, false);
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
        Item weapon = switch (this.random.nextInt(7)) {
            case 0 -> Items.IRON_SWORD;
            case 1 -> LOTRCombatItems.BRONZE_SWORD;
            case 2 -> LOTRCombatItems.IRON_DAGGER;
            case 3 -> LOTRCombatItems.BRONZE_DAGGER;
            case 4 -> LOTRCombatItems.IRON_BATTLEAXE;
            case 5 -> LOTRCombatItems.BRONZE_BATTLEAXE;
            default -> LOTRCombatItems.IRON_PIKE;
        };
        this.npcItemsInv.setMeleeWeapon(new ItemStack(weapon));
        if (this.random.nextInt(5) == 0) {
            this.npcItemsInv.setSpearBackup(this.npcItemsInv.getMeleeWeapon().copy());
            this.npcItemsInv.setMeleeWeapon(new ItemStack(this.random.nextBoolean()
                    ? LOTRCombatItems.IRON_SPEAR : LOTRCombatItems.BRONZE_SPEAR));
        }
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        setItemSlot(EquipmentSlot.FEET, new ItemStack(LOTRCombatItems.DUNLENDING_BOOTS));
        setItemSlot(EquipmentSlot.LEGS, new ItemStack(LOTRCombatItems.DUNLENDING_LEGGINGS));
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(LOTRCombatItems.DUNLENDING_CHESTPLATE));
        if (this.random.nextInt(10) != 0) {
            setItemSlot(EquipmentSlot.HEAD, new ItemStack(LOTRCombatItems.DUNLENDING_HELMET));
        }
        return data;
    }
}
