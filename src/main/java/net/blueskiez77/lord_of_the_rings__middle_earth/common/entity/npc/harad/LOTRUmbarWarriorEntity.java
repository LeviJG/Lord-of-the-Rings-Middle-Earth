package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCAttributes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityUmbarWarrior: a warrior of Umbar in Umbaric armour (bare-headed
 * one time in ten) with an Umbaric scimitar, dagger, poleaxe, mace or pike,
 * and one in five an Umbaric spear as well. One in six rides out.
 *
 * <p>NOT ported yet: the Umbar shield (LOTRShields.ALIGNMENT_UMBAR, D7).
 */
public class LOTRUmbarWarriorEntity extends LOTRUmbarianEntity {

    private static final Item[] WEAPONS_IRON = {LOTRCombatItems.UMBARIC_SCIMITAR, LOTRCombatItems.UMBARIC_SCIMITAR,
            LOTRCombatItems.UMBARIC_SCIMITAR, LOTRCombatItems.UMBARIC_DAGGER, LOTRCombatItems.POISONED_UMBARIC_DAGGER,
            LOTRCombatItems.UMBARIC_POLEAXE, LOTRCombatItems.UMBARIC_POLEAXE, LOTRCombatItems.UMBARIC_MACE,
            LOTRCombatItems.UMBARIC_PIKE};

    public LOTRUmbarWarriorEntity(EntityType<? extends LOTRUmbarWarriorEntity> type, Level level) {
        super(type, level);
        this.spawnRidingHorse = this.random.nextInt(6) == 0;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LOTRNearHaradrimBaseEntity.createAttributes()
                .add(LOTRNPCAttributes.NPC_RANGED_ACCURACY, 0.75);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        addTargetTasks(true);
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
            return this.hiredNPCInfo.getHiringPlayer() == player
                    ? "nearHarad/umbar/warrior/hired" : "nearHarad/umbar/warrior/friendly";
        }
        return "nearHarad/umbar/warrior/hostile";
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(WEAPONS_IRON[this.random.nextInt(WEAPONS_IRON.length)]));
        if (this.random.nextInt(5) == 0) {
            this.npcItemsInv.setSpearBackup(this.npcItemsInv.getMeleeWeapon().copy());
            this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.UMBARIC_SPEAR));
        }
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        setItemSlot(EquipmentSlot.FEET, new ItemStack(LOTRCombatItems.UMBARIC_BOOTS));
        setItemSlot(EquipmentSlot.LEGS, new ItemStack(LOTRCombatItems.UMBARIC_LEGGINGS));
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(LOTRCombatItems.UMBARIC_CHESTPLATE));
        if (this.random.nextInt(10) == 0) {
            setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
        } else {
            setItemSlot(EquipmentSlot.HEAD, new ItemStack(LOTRCombatItems.UMBARIC_HELMET));
        }
        return data;
    }
}
