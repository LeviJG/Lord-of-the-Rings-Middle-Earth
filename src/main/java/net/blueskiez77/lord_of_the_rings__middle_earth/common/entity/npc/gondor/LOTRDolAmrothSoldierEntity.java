package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.gondor;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityDolAmrothSoldier (the Dol Amroth Man-at-arms): a levyman of
 * Dol Amroth in its gambeson and chaps, with a sword, one in six on a horse
 * in Dol Amroth barding; two in three wear a helmet of some sort.
 */
public class LOTRDolAmrothSoldierEntity extends LOTRGondorLevymanEntity {

    public LOTRDolAmrothSoldierEntity(EntityType<? extends LOTRDolAmrothSoldierEntity> type, Level level) {
        super(type, level);
        this.spawnRidingHorse = this.random.nextInt(6) == 0;
    }

    @Override
    public Mob createMountToRide(ServerLevel level) {
        Mob horse = super.createMountToRide(level);
        if (horse != null) {
            horse.setItemSlot(EquipmentSlot.BODY, new ItemStack(LOTRCombatItems.DOL_AMROTH_HORSE_ARMOR));
        }
        return horse;
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        if (isFriendly(player)) {
            return this.hiredNPCInfo.getHiringPlayer() == player ? "gondor/swanKnight/hired" : "gondor/swanKnight/friendly";
        }
        return "gondor/swanKnight/hostile";
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        Item[] weapons = {LOTRCombatItems.DOL_AMROTH_SWORD, LOTRCombatItems.DOL_AMROTH_SWORD, LOTRCombatItems.GONDOR_SWORD,
                Items.IRON_SWORD};
        this.npcItemsInv.setMeleeWeapon(new ItemStack(weapons[this.random.nextInt(weapons.length)]));
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.LEATHER_BOOTS));
        setItemSlot(EquipmentSlot.LEGS, new ItemStack(LOTRCombatItems.DOL_AMROTH_CHAPS));
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(LOTRCombatItems.DOL_AMROTH_GAMBESON));
        if (this.random.nextInt(3) == 0) {
            setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
        } else {
            setItemSlot(EquipmentSlot.HEAD, new ItemStack(switch (this.random.nextInt(3)) {
                case 0 -> LOTRCombatItems.DOL_AMROTH_HELMET;
                case 1 -> Items.IRON_HELMET;
                default -> Items.LEATHER_HELMET;
            }));
        }
        return data;
    }
}
