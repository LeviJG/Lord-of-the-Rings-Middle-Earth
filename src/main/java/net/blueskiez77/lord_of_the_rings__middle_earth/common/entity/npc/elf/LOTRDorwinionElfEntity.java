package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.elf;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRFoods;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNames;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRDataComponents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.quest.LOTRMiniQuest;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.quest.LOTRMiniQuestFactory;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRDorwinionBiome;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;

/**
 * LOTREntityDorwinionElf: an elf of Dorwinion, of the Dorwinion faction with
 * its men. It fights only up close, with an elven dagger, drinks as
 * Dorwinion does, bears a Sindarin name, and slain by a player may leave a
 * Dorwinion wine, and one time in six something from a Dorwinion house.
 */
public class LOTRDorwinionElfEntity extends LOTRElfEntity {

    public LOTRDorwinionElfEntity(EntityType<? extends LOTRDorwinionElfEntity> type, Level level) {
        super(type, level);
    }

    /** createElfRangedAttackAI: the blade, from a distance too. */
    @Override
    protected Goal createElfRangedAttackAI() {
        return createElfMeleeAttackAI();
    }

    @Override
    public LOTRFoods getElfDrinks() {
        return LOTRFoods.DORWINION_DRINK;
    }

    @Override
    public LOTRFaction getFaction() {
        return LOTRFaction.DORWINION;
    }

    @Override
    public float getAlignmentBonus() {
        return 2.0f;
    }

    @Override
    public void setupNPCName() {
        this.familyInfo.setName(LOTRNames.getSindarinName(this.random, this.familyInfo.isMale()));
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        return isFriendly(player) ? "dorwinion/elf/friendly" : "dorwinion/elf/hostile";
    }

    /** Whatever the mode, the blade (onAttackModeChange); the idle item at rest. */
    @Override
    public void onAttackModeChange(AttackMode mode, boolean mounted) {
        Goal melee = meleeAttackAI();
        this.goalSelector.removeGoal(melee);
        this.goalSelector.removeGoal(rangedAttackAI());
        if (mode == AttackMode.IDLE) {
            setItemSlot(EquipmentSlot.MAINHAND, this.npcItemsInv.getIdleItem());
        } else {
            this.goalSelector.addGoal(2, melee);
            setItemSlot(EquipmentSlot.MAINHAND, this.npcItemsInv.getMeleeWeapon());
        }
    }

    /**
     * dropElfItems: to a player, now and then a light-to-strong Dorwinion wine
     * in one of Dorwinion's vessels; and one time in six, whoever the killer,
     * something from a Dorwinion house.
     */
    @Override
    protected void dropElfItems(ServerLevel level, boolean killedByPlayer, int looting) {
        super.dropElfItems(level, killedByPlayer, looting);
        if (killedByPlayer && this.random.nextInt(Math.max(20 - looting * 4, 1)) == 0) {
            ItemStack drink = LOTRFoods.DORWINION_DRINK.getRandomBrewableDrink(this.random);
            drink.set(LOTRDataComponents.DRINK_STRENGTH, 1 + this.random.nextInt(3));
            spawnAtLocation(level, drink, 0.0f);
        }
        if (this.random.nextInt(6) == 0) {
            dropChestContents(level, LOTRChestContents.DORWINION_HOUSE, 1, 1 + looting);
        }
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.DORWINION_ELVEN_DAGGER));
        this.npcItemsInv.setIdleItem(ItemStack.EMPTY);
        return data;
    }

    @Override
    public @Nullable LOTRMiniQuest createMiniQuest() {
        return LOTRMiniQuestFactory.DORWINION_ELF.createQuest(this);
    }

    @Override
    public LOTRAchievement getKillAchievement() {
        return LOTRAchievement.KILL_DORWINION_ELF;
    }

    /** getBlockPathWeight: drawn to its own lands. */
    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader level) {
        return homeBiomePull(level, pos, LOTRDorwinionBiome.class);
    }

    /** canElfSpawnHere: above y 62, on the biome's own top block. */
    @Override
    public boolean canElfSpawnHere(LevelAccessor level) {
        return isAboveSeaOnTopBlock(level, false);
    }
}
