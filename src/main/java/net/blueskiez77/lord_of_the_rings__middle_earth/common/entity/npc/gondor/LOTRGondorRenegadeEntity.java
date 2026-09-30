package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.gondor;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityGondorRenegade: a Gondorian soldier gone over to Umbar -- of the
 * Near Harad faction, on foot, bare-headed, armed as Umbar arms its men, in
 * Gondorian armour or one time in five Pelargir's.
 *
 * <p>NOT ported yet: his mini-quests (offered at +50, 1 in 4000, D14).
 */
public class LOTRGondorRenegadeEntity extends LOTRGondorSoldierEntity {

    public LOTRGondorRenegadeEntity(EntityType<? extends LOTRGondorRenegadeEntity> type, Level level) {
        super(type, level);
        this.spawnRidingHorse = false;
    }

    @Override
    public LOTRFaction getFaction() {
        return LOTRFaction.NEAR_HARAD;
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        if (isFriendlyAndAligned(player)) {
            return this.hiredNPCInfo.getHiringPlayer() == player ? "nearHarad/renegade/hired" : "nearHarad/renegade/friendly";
        }
        return "nearHarad/renegade/hostile";
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        Item[] weapons = {LOTRCombatItems.UMBARIC_SCIMITAR, LOTRCombatItems.UMBARIC_SCIMITAR, LOTRCombatItems.UMBARIC_SCIMITAR,
                LOTRCombatItems.UMBARIC_POLEAXE, LOTRCombatItems.UMBARIC_POLEAXE, LOTRCombatItems.UMBARIC_MACE,
                LOTRCombatItems.UMBARIC_PIKE};
        this.npcItemsInv.setMeleeWeapon(new ItemStack(weapons[this.random.nextInt(weapons.length)]));
        this.npcItemsInv.setMeleeWeaponMounted(this.npcItemsInv.getMeleeWeapon().copy());
        if (this.random.nextInt(5) == 0) {
            this.npcItemsInv.setSpearBackup(this.npcItemsInv.getMeleeWeapon().copy());
            this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.UMBARIC_SPEAR));
        }
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        this.npcItemsInv.setIdleItemMounted(this.npcItemsInv.getMeleeWeaponMounted().copy());
        if (this.random.nextInt(5) == 0) {
            setItemSlot(EquipmentSlot.FEET, new ItemStack(LOTRCombatItems.PELARGIR_BOOTS));
            setItemSlot(EquipmentSlot.LEGS, new ItemStack(LOTRCombatItems.PELARGIR_LEGGINGS));
            setItemSlot(EquipmentSlot.CHEST, new ItemStack(LOTRCombatItems.PELARGIR_CHESTPLATE));
        } else {
            setItemSlot(EquipmentSlot.FEET, new ItemStack(LOTRCombatItems.GONDOR_BOOTS));
            setItemSlot(EquipmentSlot.LEGS, new ItemStack(LOTRCombatItems.GONDOR_LEGGINGS));
            setItemSlot(EquipmentSlot.CHEST, new ItemStack(LOTRCombatItems.GONDOR_CHESTPLATE));
        }
        setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
        return data;
    }
}
