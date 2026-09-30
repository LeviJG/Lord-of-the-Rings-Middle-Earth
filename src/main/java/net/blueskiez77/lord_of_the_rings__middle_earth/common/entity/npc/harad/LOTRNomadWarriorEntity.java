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
 * LOTREntityNomadWarrior: a nomad in nomad armour, with a Haradric sword,
 * dagger (poisoned or not) or pike, and one in six a Haradric spear as well.
 * His head is bare one time in ten, else one time in three in a nomad turban,
 * else in a nomad cap. One in eight rides out on a camel.
 */
public class LOTRNomadWarriorEntity extends LOTRNomadEntity {

    private static final Item[] WEAPONS_BRONZE = {LOTRCombatItems.HARADRIC_SWORD, LOTRCombatItems.HARADRIC_SWORD,
            LOTRCombatItems.HARADRIC_SWORD, LOTRCombatItems.HARADRIC_DAGGER, LOTRCombatItems.POISONED_HARADRIC_DAGGER,
            LOTRCombatItems.HARADRIC_PIKE};

    public LOTRNomadWarriorEntity(EntityType<? extends LOTRNomadWarriorEntity> type, Level level) {
        super(type, level);
        this.spawnRidingHorse = this.random.nextInt(8) == 0;
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
                    ? "nearHarad/nomad/warrior/hired" : "nearHarad/nomad/warrior/friendly";
        }
        return "nearHarad/nomad/warrior/hostile";
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(WEAPONS_BRONZE[this.random.nextInt(WEAPONS_BRONZE.length)]));
        if (this.random.nextInt(6) == 0) {
            this.npcItemsInv.setSpearBackup(this.npcItemsInv.getMeleeWeapon().copy());
            this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.HARADRIC_SPEAR));
        }
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        setItemSlot(EquipmentSlot.FEET, new ItemStack(LOTRCombatItems.NOMAD_SHOES));
        setItemSlot(EquipmentSlot.LEGS, new ItemStack(LOTRCombatItems.NOMAD_LEGGINGS));
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(LOTRCombatItems.NOMAD_TUNIC));
        if (this.random.nextInt(10) == 0) {
            setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
        } else if (this.random.nextInt(3) == 0) {
            setItemSlot(EquipmentSlot.HEAD, turban(NOMAD_TURBAN_COLOURS[this.random.nextInt(NOMAD_TURBAN_COLOURS.length)]));
        } else {
            setItemSlot(EquipmentSlot.HEAD, new ItemStack(LOTRCombatItems.NOMAD_CAP));
        }
        return data;
    }
}
