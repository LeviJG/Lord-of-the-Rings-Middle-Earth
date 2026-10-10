package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.elf;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRFoods;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRDataComponents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRDrinkItem;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRFoodItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRVessel;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.quest.LOTRMiniQuest;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.quest.LOTRMiniQuestFactory;

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
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRLothlorienBiome;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;

/**
 * LOTREntityGaladhrimElf: an elf of Lothlórien, with an elven dagger and a
 * mallorn bow; its mounts wear Galadhrim barding. Slain by a player it may
 * leave a drink of miruvor, and one time in six something from an elven
 * house.
 */
public class LOTRGaladhrimElfEntity extends LOTRElfEntity {

    public LOTRGaladhrimElfEntity(EntityType<? extends LOTRGaladhrimElfEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public Mob createMountToRide(ServerLevel level) {
        Mob horse = super.createMountToRide(level);
        if (horse != null) {
            horse.setItemSlot(EquipmentSlot.BODY, new ItemStack(LOTRCombatItems.GALADHRIM_HORSE_ARMOR));
        }
        return horse;
    }

    @Override
    public LOTRFaction getFaction() {
        return LOTRFaction.LOTHLORIEN;
    }

    @Override
    public float getAlignmentBonus() {
        return 1.0f;
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        if (isFriendly(player)) {
            return this.hiredNPCInfo.getHiringPlayer() == player ? "galadhrim/elf/hired" : "galadhrim/elf/friendly";
        }
        return "galadhrim/elf/hostile";
    }

    /**
     * dropElfItems: to a player, now and then a light-to-strong miruvor in
     * one of the elves' vessels (1 in 20, less with looting); and one time in
     * six, whoever the killer, something from an elven house.
     */
    @Override
    protected void dropElfItems(ServerLevel level, boolean killedByPlayer, int looting) {
        super.dropElfItems(level, killedByPlayer, looting);
        if (killedByPlayer && this.random.nextInt(Math.max(20 - looting * 4, 1)) == 0) {
            ItemStack elfDrink = LOTRDrinkItem.stack(LOTRFoodItems.MIRUVOR, 1 + this.random.nextInt(3));
            LOTRVessel[] vessels = LOTRFoods.ELF_DRINK.getDrinkVessels();
            elfDrink.set(LOTRDataComponents.VESSEL, vessels[this.random.nextInt(vessels.length)]);
            spawnAtLocation(level, elfDrink, 0.0f);
        }
        if (this.random.nextInt(6) == 0) {
            dropChestContents(level, LOTRChestContents.ELF_HOUSE, 1, 1 + looting);
        }
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.GALADHRIM_DAGGER));
        this.npcItemsInv.setRangedWeapon(new ItemStack(LOTRCombatItems.MALLORN_BOW));
        this.npcItemsInv.setIdleItem(ItemStack.EMPTY);
        return data;
    }

    @Override
    public @Nullable LOTRMiniQuest createMiniQuest() {
        return LOTRMiniQuestFactory.GALADHRIM.createQuest(this);
    }

    @Override
    public LOTRAchievement getKillAchievement() {
        return LOTRAchievement.KILL_ELF;
    }

    /** getBlockPathWeight: drawn to its own lands. */
    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader level) {
        return homeBiomePull(level, pos, LOTRLothlorienBiome.class);
    }

    /** canElfSpawnHere: above y 62, on the biome's own top block. */
    @Override
    public boolean canElfSpawnHere(LevelAccessor level) {
        return isAboveSeaOnTopBlock(level, false);
    }
}
