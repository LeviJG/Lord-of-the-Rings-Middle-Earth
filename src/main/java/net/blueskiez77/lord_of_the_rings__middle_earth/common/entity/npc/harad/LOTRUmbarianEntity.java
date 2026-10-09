package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRFoods;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNames;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.quest.LOTRMiniQuest;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.quest.LOTRMiniQuestFactory;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityUmbarian: a man or woman of Umbar, "Name of Umbar", with an
 * Umbaric dagger, an Umbar name, Southron food and drink, and now and then
 * something from a Southron house. Its horse is barded in Umbaric armour.
 */
public class LOTRUmbarianEntity extends LOTRNearHaradrimBaseEntity {

    public LOTRUmbarianEntity(EntityType<? extends LOTRUmbarianEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public LOTRFoods getHaradrimFoods() {
        return LOTRFoods.SOUTHRON;
    }

    @Override
    public LOTRFoods getHaradrimDrinks() {
        return LOTRFoods.SOUTHRON_DRINK;
    }

    @Override
    public void setupNPCName() {
        this.familyInfo.setName(LOTRNames.getUmbarName(this.random, this.familyInfo.isMale()));
    }

    /** createMountToRide: a horse in Umbaric barding. */
    @Override
    public Mob createMountToRide(ServerLevel level) {
        Mob horse = super.createMountToRide(level);
        if (horse != null) {
            horse.setItemSlot(EquipmentSlot.BODY, new ItemStack(LOTRCombatItems.UMBARIC_HORSE_ARMOR));
        }
        return horse;
    }

    /** A plain Umbarian is "Name of Umbar"; the others keep "Name, the Kind". */
    @Override
    protected Component getNPCFormattedName(String npcName, Component kind) {
        if (getType() == LOTREntities.UMBARIAN) {
            return Component.translatable("entity.lotr.umbarian.entityName", npcName);
        }
        return super.getNPCFormattedName(npcName, kind);
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        return isFriendly(player) ? "nearHarad/umbar/haradrim/friendly" : "nearHarad/umbar/haradrim/hostile";
    }

    @Override
    protected void dropHaradrimItems(ServerLevel level, boolean killedByPlayer, int looting) {
        if (this.random.nextInt(5) == 0) {
            dropChestContents(level, LOTRChestContents.NEAR_HARAD_HOUSE, 1, 2 + looting);
        }
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.UMBARIC_DAGGER));
        this.npcItemsInv.setIdleItem(ItemStack.EMPTY);
        return data;
    }

    @Override
    public @Nullable LOTRMiniQuest createMiniQuest() {
        return LOTRMiniQuestFactory.UMBAR.createQuest(this);
    }
}
