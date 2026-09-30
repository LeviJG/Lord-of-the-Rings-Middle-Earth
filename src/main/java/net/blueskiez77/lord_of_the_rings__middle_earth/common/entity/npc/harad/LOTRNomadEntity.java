package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal.LOTRCamelEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRFoods;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNames;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;

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
 * LOTREntityNomad: a nomad of the Harad desert, with a Haradric dagger, a
 * nomad name, nomad food and drink, and now and then something from a nomad
 * tent. One in four wears a turban of one of five sandy colours. Nomads ride
 * camels, chested and carpeted.
 *
 * <p>NOT ported yet: ImmuneToHeat (D10) and mini-quests (D14).
 */
public class LOTRNomadEntity extends LOTRNearHaradrimBaseEntity {

    public static final int[] NOMAD_TURBAN_COLOURS = {15392448, 13550476, 10063441, 8354400, 8343622};

    public LOTRNomadEntity(EntityType<? extends LOTRNomadEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        addTargetTasks(false);
    }

    /** createMountToRide: a camel with a chest and a carpet. */
    @Override
    public Mob createMountToRide(ServerLevel level) {
        LOTRCamelEntity camel = LOTREntities.CAMEL.create(level, EntitySpawnReason.JOCKEY);
        if (camel != null) {
            camel.setNomadChestAndCarpet();
        }
        return camel;
    }

    @Override
    public LOTRFoods getHaradrimFoods() {
        return LOTRFoods.NOMAD;
    }

    @Override
    public LOTRFoods getHaradrimDrinks() {
        return LOTRFoods.NOMAD_DRINK;
    }

    @Override
    public void setupNPCName() {
        this.familyInfo.setName(LOTRNames.getNomadName(this.random, this.familyInfo.isMale()));
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        return isFriendly(player) ? "nearHarad/nomad/nomad/friendly" : "nearHarad/nomad/nomad/hostile";
    }

    @Override
    protected void dropHaradrimItems(ServerLevel level, boolean killedByPlayer, int looting) {
        if (this.random.nextInt(5) == 0) {
            dropChestContents(level, LOTRChestContents.NOMAD_TENT, 1, 2 + looting);
        }
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.HARADRIC_DAGGER));
        this.npcItemsInv.setIdleItem(ItemStack.EMPTY);
        if (this.random.nextInt(4) == 0) {
            setItemSlot(EquipmentSlot.HEAD, turban(NOMAD_TURBAN_COLOURS[this.random.nextInt(NOMAD_TURBAN_COLOURS.length)]));
        } else {
            setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
        }
        return data;
    }
}
