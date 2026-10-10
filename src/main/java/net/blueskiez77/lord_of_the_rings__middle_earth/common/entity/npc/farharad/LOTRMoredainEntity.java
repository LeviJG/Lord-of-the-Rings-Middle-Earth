package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.farharad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRDrinkGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTREatGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRFollowHiringPlayerGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRHiredRemainStillGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRFoods;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRManEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNames;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
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
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.PathType;

import org.jspecify.annotations.Nullable;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRFarHaradBiome;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;

/**
 * LOTREntityMoredain: a man or woman of the Moredain of Far Harad. They keep
 * out of the water, open doors, eat and drink as the Moredain do, carry a
 * Moredain dagger, seek out their people's enemies, ride zebras, and leave
 * bones and, one time in five, something from a Moredain hut.
 */
public class LOTRMoredainEntity extends LOTRManEntity {

    public LOTRMoredainEntity(EntityType<? extends LOTRMoredainEntity> type, Level level) {
        super(type, level);
        setPathfindingMalus(PathType.WATER, -1.0f);
        if (getNavigation() instanceof GroundPathNavigation navigation) {
            navigation.setCanOpenDoors(true);
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createNPCAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.MOVEMENT_SPEED, 0.2);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new LOTRHiredRemainStillGoal(this));
        this.goalSelector.addGoal(2, createHaradrimAttackAI());
        this.goalSelector.addGoal(3, new LOTRFollowHiringPlayerGoal(this));
        this.goalSelector.addGoal(4, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(6, new LOTREatGoal(this, LOTRFoods.MOREDAIN, 8000));
        this.goalSelector.addGoal(6, new LOTRDrinkGoal(this, LOTRFoods.MOREDAIN_DRINK, 8000));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 10.0f, 0.02f));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, LOTRNPCEntity.class, 5.0f, 0.02f));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Mob.class, 8.0f, 0.02f));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        addTargetTasks(true);
    }

    protected Goal createHaradrimAttackAI() {
        return new LOTRAttackOnCollideGoal(this, 1.5, true);
    }

    @Override
    public LOTRFaction getFaction() {
        return LOTRFaction.MORWAITH;
    }

    @Override
    public float getAlignmentBonus() {
        return 1.0f;
    }

    @Override
    public void setupNPCGender() {
        this.familyInfo.setMale(this.random.nextBoolean());
    }

    @Override
    public void setupNPCName() {
        this.familyInfo.setName(LOTRNames.getMoredainName(this.random, this.familyInfo.isMale()));
    }

    /** A plain Moredain is "Name of the Morwaith"; the others keep "Name, the Kind". */
    @Override
    protected Component getNPCFormattedName(String npcName, Component kind) {
        if (getType() == LOTREntities.MOREDAIN) {
            return Component.translatable("entity.lotr.moredain.entityName", npcName);
        }
        return super.getNPCFormattedName(npcName, kind);
    }

    /** createMountToRide: a zebra. */
    @Override
    public Mob createMountToRide(ServerLevel level) {
        return LOTREntities.ZEBRA.create(level, EntitySpawnReason.JOCKEY);
    }

    @Override
    public String getNPCName() {
        return this.familyInfo.getName();
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        if (isFriendly(player)) {
            return this.hiredNPCInfo.getHiringPlayer() == player ? "moredain/moredain/hired" : "moredain/moredain/friendly";
        }
        return "moredain/moredain/hostile";
    }

    @Override
    public void onAttackModeChange(AttackMode mode, boolean mounted) {
        setItemSlot(EquipmentSlot.MAINHAND,
                mode == AttackMode.IDLE ? this.npcItemsInv.getIdleItem() : this.npcItemsInv.getMeleeWeapon());
    }

    @Override
    protected void dropNPCItems(ServerLevel level, boolean killedByPlayer, int looting) {
        super.dropNPCItems(level, killedByPlayer, looting);
        int bones = this.random.nextInt(2) + this.random.nextInt(looting + 1);
        for (int l = 0; l < bones; ++l) {
            spawnAtLocation(level, Items.BONE);
        }
        dropHaradrimItems(level, killedByPlayer, looting);
    }

    protected void dropHaradrimItems(ServerLevel level, boolean killedByPlayer, int looting) {
        if (this.random.nextInt(5) == 0) {
            dropChestContents(level, LOTRChestContents.MOREDAIN_HUT, 1, 2 + looting);
        }
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.MORWAITH_DAGGER));
        this.npcItemsInv.setIdleItem(ItemStack.EMPTY);
        return data;
    }

    @Override
    public @Nullable LOTRMiniQuest createMiniQuest() {
        return LOTRMiniQuestFactory.MOREDAIN.createQuest(this);
    }

    @Override
    public LOTRAchievement getKillAchievement() {
        return LOTRAchievement.KILL_MOREDAIN;
    }

    /** getBlockPathWeight: drawn to its own lands. */
    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader level) {
        return homeBiomePull(level, pos, LOTRFarHaradBiome.class);
    }

    /** getCanSpawnHere: above y 62, on grass or sand. */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, EntitySpawnReason reason) {
        return super.checkSpawnRules(level, reason) && (this.liftSpawnRestrictions || isAboveSeaOnGrassOrSand(level));
    }
}
