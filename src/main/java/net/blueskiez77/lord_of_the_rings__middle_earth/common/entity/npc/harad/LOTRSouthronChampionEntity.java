package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRCapes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCAttributes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntitySouthronChampion: a warrior who always rides out barded, with an
 * Umbaric spear and a scimitar, poleaxe or mace to fall back on, in coast
 * Southron armour and a champion's helmet -- 25 health, and quicker to strike
 * from the saddle.
 *
 * <p>NOT ported yet: his cape (LOTRCapes.SOUTHRON_CHAMPION), with NPC capes.
 */
public class LOTRSouthronChampionEntity extends LOTRNearHaradrimWarriorEntity {

    private static final Item[] WEAPONS_CHAMPION = {LOTRCombatItems.UMBARIC_SCIMITAR, LOTRCombatItems.UMBARIC_POLEAXE,
            LOTRCombatItems.UMBARIC_MACE};

    public LOTRSouthronChampionEntity(EntityType<? extends LOTRSouthronChampionEntity> type, Level level) {
        super(type, level);
        this.npcCape = LOTRCapes.SOUTHRON_CHAMPION;
        this.spawnRidingHorse = true;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LOTRNearHaradrimBaseEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 25.0)
                .add(Attributes.FOLLOW_RANGE, 24.0)
                .add(LOTRNPCAttributes.NPC_RANGED_ACCURACY, 0.5)
                .add(LOTRNPCAttributes.HORSE_ATTACK_SPEED, 1.9);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(WEAPONS_CHAMPION[this.random.nextInt(WEAPONS_CHAMPION.length)]));
        this.npcItemsInv.setSpearBackup(this.npcItemsInv.getMeleeWeapon().copy());
        this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.UMBARIC_SPEAR));
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        setItemSlot(EquipmentSlot.FEET, new ItemStack(LOTRCombatItems.COAST_SOUTHRON_BOOTS));
        setItemSlot(EquipmentSlot.LEGS, new ItemStack(LOTRCombatItems.COAST_SOUTHRON_LEGGINGS));
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(LOTRCombatItems.COAST_SOUTHRON_CHESTPLATE));
        setItemSlot(EquipmentSlot.HEAD, new ItemStack(LOTRCombatItems.SOUTHRON_CHAMPION_HELMET));
        return data;
    }
}
