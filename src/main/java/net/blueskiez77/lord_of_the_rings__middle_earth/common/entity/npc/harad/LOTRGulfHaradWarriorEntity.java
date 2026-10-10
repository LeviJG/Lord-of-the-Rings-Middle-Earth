package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.shield.LOTRShields;
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
 * LOTREntityGulfHaradWarrior (the Gulfing Warrior): a warrior of the Gulf in
 * Gulfen armour. Two times in three he carries a Gulfen khopesh, else a
 * Haradric sword, dagger (poisoned or not) or pike; one in five has a
 * Haradric spear as well. His head is bare one time in ten, else in a Gulfen
 * helmet. One in ten rides out.
 */
public class LOTRGulfHaradWarriorEntity extends LOTRGulfHaradrimEntity {

    private static final Item[] WEAPONS_HARAD = {LOTRCombatItems.HARADRIC_SWORD, LOTRCombatItems.HARADRIC_SWORD,
            LOTRCombatItems.HARADRIC_DAGGER, LOTRCombatItems.POISONED_HARADRIC_DAGGER, LOTRCombatItems.HARADRIC_PIKE};

    public LOTRGulfHaradWarriorEntity(EntityType<? extends LOTRGulfHaradWarriorEntity> type, Level level) {
        super(type, level);
        this.npcShield = LOTRShields.ALIGNMENT_GULF;
        this.spawnRidingHorse = this.random.nextInt(10) == 0;
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
                    ? "nearHarad/gulf/warrior/hired" : "nearHarad/gulf/warrior/friendly";
        }
        return "nearHarad/gulf/warrior/hostile";
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        if (this.random.nextInt(3) == 0) {
            this.npcItemsInv.setMeleeWeapon(new ItemStack(WEAPONS_HARAD[this.random.nextInt(WEAPONS_HARAD.length)]));
        } else {
            this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.GULFEN_KHOPESH));
        }
        if (this.random.nextInt(5) == 0) {
            this.npcItemsInv.setSpearBackup(this.npcItemsInv.getMeleeWeapon().copy());
            this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.HARADRIC_SPEAR));
        }
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        setItemSlot(EquipmentSlot.FEET, new ItemStack(LOTRCombatItems.GULFEN_BOOTS));
        setItemSlot(EquipmentSlot.LEGS, new ItemStack(LOTRCombatItems.GULFEN_LEGGINGS));
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(LOTRCombatItems.GULFEN_CHESTPLATE));
        if (this.random.nextInt(10) == 0) {
            setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
        } else {
            setItemSlot(EquipmentSlot.HEAD, new ItemStack(LOTRCombatItems.GULFEN_HELMET));
        }
        return data;
    }
}
