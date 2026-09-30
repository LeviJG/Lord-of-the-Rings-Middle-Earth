package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.farharad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityTauredainWarrior: a Taurethrim warrior in Taurethrim armour, the
 * helmet four times in five, with a Taurethrim sword (three in eight),
 * dagger (poisoned or not), bludgeon, battleaxe or pike, and one in five a
 * Taurethrim spear as well. He seeks out his people's enemies.
 *
 * <p>NOT ported yet: the Taurethrim shield (LOTRShields.ALIGNMENT_TAUREDAIN,
 * D7).
 */
public class LOTRTauredainWarriorEntity extends LOTRTauredainEntity {

    private static final Item[] WEAPONS = {LOTRCombatItems.TAURETHRIM_SWORD, LOTRCombatItems.TAURETHRIM_SWORD,
            LOTRCombatItems.TAURETHRIM_SWORD, LOTRCombatItems.TAURETHRIM_DAGGER, LOTRCombatItems.POISONED_TAURETHRIM_DAGGER,
            LOTRCombatItems.TAURETHRIM_BLUDGEON, LOTRCombatItems.TAURETHRIM_BATTLEAXE, LOTRCombatItems.TAURETHRIM_PIKE};

    public LOTRTauredainWarriorEntity(EntityType<? extends LOTRTauredainWarriorEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        addTargetTasks(true);
    }

    @Override
    protected Goal createHaradrimAttackAI() {
        return new LOTRAttackOnCollideGoal(this, 1.5, true);
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
            return this.hiredNPCInfo.getHiringPlayer() == player ? "tauredain/warrior/hired" : "tauredain/warrior/friendly";
        }
        return "tauredain/warrior/hostile";
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(WEAPONS[this.random.nextInt(WEAPONS.length)]));
        if (this.random.nextInt(5) == 0) {
            this.npcItemsInv.setSpearBackup(this.npcItemsInv.getMeleeWeapon().copy());
            this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.TAURETHRIM_SPEAR));
        }
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        setItemSlot(EquipmentSlot.FEET, new ItemStack(LOTRCombatItems.TAURETHRIM_BOOTS));
        setItemSlot(EquipmentSlot.LEGS, new ItemStack(LOTRCombatItems.TAURETHRIM_LEGGINGS));
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(LOTRCombatItems.TAURETHRIM_CHESTPLATE));
        if (this.random.nextInt(5) != 0) {
            setItemSlot(EquipmentSlot.HEAD, new ItemStack(LOTRCombatItems.TAURETHRIM_HELMET));
        }
        return data;
    }
}
