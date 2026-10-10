package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.dale;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRDrinkGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTREatGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRFollowHiringPlayerGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRHiredRemainStillGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRFoods;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRManEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCAttributes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNames;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRToolItems;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.PathType;

import org.jspecify.annotations.Nullable;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRDaleBiome;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;

/**
 * LOTREntityDaleMan: the people of Dale, "Name of Dale". They keep out of the
 * water, open doors, fight back with a dagger or an axe, eat and drink as Dale
 * does, and can be hired. Slain, they leave bones and one time in six
 * something from a Dale house.
 */
public class LOTRDaleManEntity extends LOTRManEntity {

    public LOTRDaleManEntity(EntityType<? extends LOTRDaleManEntity> type, Level level) {
        super(type, level);
        setPathfindingMalus(PathType.WATER, -1.0f);
        if (getNavigation() instanceof GroundPathNavigation navigation) {
            navigation.setCanOpenDoors(true);
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createNPCAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(LOTRNPCAttributes.NPC_RANGED_ACCURACY, 0.5);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new LOTRHiredRemainStillGoal(this));
        this.goalSelector.addGoal(2, createDaleAttackAI());
        this.goalSelector.addGoal(3, new LOTRFollowHiringPlayerGoal(this));
        this.goalSelector.addGoal(4, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(6, new LOTREatGoal(this, LOTRFoods.DALE, 8000));
        this.goalSelector.addGoal(6, new LOTRDrinkGoal(this, LOTRFoods.DALE_DRINK, 8000));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0f, 0.02f));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, LOTRNPCEntity.class, 5.0f, 0.02f));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Mob.class, 8.0f, 0.02f));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        addTargetTasks(false);
    }

    protected Goal createDaleAttackAI() {
        return new LOTRAttackOnCollideGoal(this, 1.3, false);
    }

    @Override
    public LOTRFaction getFaction() {
        return LOTRFaction.DALE;
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
        this.familyInfo.setName(LOTRNames.getDalishName(this.random, this.familyInfo.isMale()));
    }

    @Override
    public String getNPCName() {
        return this.familyInfo.getName();
    }

    /** A plain man of Dale is "Name of Dale"; the others keep "Name, the Kind". */
    @Override
    protected Component getNPCFormattedName(String npcName, Component kind) {
        if (getType() == LOTREntities.DALE_MAN) {
            return Component.translatable("entity.lotr.dale_man.entityName", npcName);
        }
        return super.getNPCFormattedName(npcName, kind);
    }

    @Override
    public void onAttackModeChange(AttackMode mode, boolean mounted) {
        setItemSlot(EquipmentSlot.MAINHAND,
                mode == AttackMode.IDLE ? this.npcItemsInv.getIdleItem() : this.npcItemsInv.getMeleeWeapon());
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        return isFriendly(player) ? "dale/man/friendly" : "dale/man/hostile";
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        Item[] weapons = {LOTRCombatItems.DALE_DAGGER, LOTRCombatItems.IRON_DAGGER, LOTRCombatItems.BRONZE_DAGGER,
                Items.IRON_AXE, LOTRToolItems.BRONZE_AXE, Items.STONE_AXE};
        this.npcItemsInv.setMeleeWeapon(new ItemStack(weapons[this.random.nextInt(weapons.length)]));
        this.npcItemsInv.setIdleItem(ItemStack.EMPTY);
        return data;
    }

    /** dropFewItems: bones, and one time in six something from a Dale house. */
    @Override
    protected void dropNPCItems(ServerLevel level, boolean killedByPlayer, int looting) {
        super.dropNPCItems(level, killedByPlayer, looting);
        int bones = this.random.nextInt(2) + this.random.nextInt(looting + 1);
        for (int l = 0; l < bones; ++l) {
            spawnAtLocation(level, Items.BONE);
        }
        if (this.random.nextInt(6) == 0) {
            dropChestContents(level, LOTRChestContents.DALE_HOUSE, 1, 2 + looting);
        }
    }

    @Override
    public @Nullable LOTRMiniQuest createMiniQuest() {
        return LOTRMiniQuestFactory.DALE.createQuest(this);
    }

    @Override
    public LOTRAchievement getKillAchievement() {
        return LOTRAchievement.KILL_DALISH;
    }

    /** getBlockPathWeight: drawn to its own lands. */
    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader level) {
        return homeBiomePull(level, pos, LOTRDaleBiome.class);
    }

    /** getCanSpawnHere: above y 62, on the biome's own top block. */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, EntitySpawnReason reason) {
        return super.checkSpawnRules(level, reason) && (this.liftSpawnRestrictions || isAboveSeaOnTopBlock(level, false));
    }
}
