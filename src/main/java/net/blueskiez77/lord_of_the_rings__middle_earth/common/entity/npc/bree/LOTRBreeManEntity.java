package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.bree;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRBreeEatGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRDrinkGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRFollowHiringPlayerGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRHiredRemainStillGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRHobbitSmokeGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRFoods;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRManEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNames;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRToolItems;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
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

/**
 * LOTREntityBreeMan: the Big Folk of Bree-land. They keep out of the water,
 * open doors, fight back with a dagger or an axe when attacked, eat, drink
 * and smoke, and can be hired where their people allow it. One man in two
 * thousand is called Peter Jackson, and eats nothing but carrots.
 *
 * <p>The goals are laid out as the original's: its guards, captain and the
 * ruffians change only the attack, hiring and avoidance steps.
 *
 * <p>NOT ported yet: pickpocketing
 * (IPickpocketable, and its BREE_PICKPOCKET pool), mini-quests (D14), the
 * pull of Bree-land on their wandering and their natural spawning (with the
 * biomes), and the killBreelander achievement (D7).
 */
public class LOTRBreeManEntity extends LOTRManEntity {

    public static final String CARROT_EATER_NAME = "Peter Jackson";

    public LOTRBreeManEntity(EntityType<? extends LOTRBreeManEntity> type, Level level) {
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
        int p = addBreeAttackAI(2);
        addBreeHiringAI(p + 1);
        this.goalSelector.addGoal(p + 2, new OpenDoorGoal(this, true));
        addBreeAvoidAI(p + 3);
        this.goalSelector.addGoal(p + 4, new RandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(p + 5, new LOTRBreeEatGoal(this, LOTRFoods.BREE, 8000));
        this.goalSelector.addGoal(p + 5, new LOTRDrinkGoal(this, LOTRFoods.BREE_DRINK, 8000));
        this.goalSelector.addGoal(p + 5, new LOTRHobbitSmokeGoal(this, 12000));
        this.goalSelector.addGoal(p + 6, new LookAtPlayerGoal(this, Player.class, 8.0f, 0.02f));
        this.goalSelector.addGoal(p + 6, new LookAtPlayerGoal(this, LOTRNPCEntity.class, 5.0f, 0.02f));
        this.goalSelector.addGoal(p + 7, new LookAtPlayerGoal(this, Mob.class, 8.0f, 0.02f));
        this.goalSelector.addGoal(p + 8, new RandomLookAroundGoal(this));
        addTargetTasks(false);
    }

    /** The attack step; returns the last priority it used. */
    protected int addBreeAttackAI(int prio) {
        this.goalSelector.addGoal(prio, new LOTRAttackOnCollideGoal(this, 1.3, false));
        return prio;
    }

    /** The avoidance step: a Bree-man keeps eight blocks from ruffian brutes. */
    protected void addBreeAvoidAI(int prio) {
        this.goalSelector.addGoal(prio, new AvoidEntityGoal<>(this, LOTRRuffianBruteEntity.class, 8.0f, 1.0, 1.5));
    }

    protected void addBreeHiringAI(int prio) {
        this.goalSelector.addGoal(prio, new LOTRFollowHiringPlayerGoal(this));
    }

    @Override
    public LOTRFaction getFaction() {
        return LOTRFaction.BREE;
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
        this.familyInfo.setName(LOTRNames.getBreeName(this.random, this.familyInfo.isMale()));
    }

    @Override
    public String getNPCName() {
        return this.familyInfo.getName();
    }

    @Override
    public void onAttackModeChange(AttackMode mode, boolean mounted) {
        setItemSlot(EquipmentSlot.MAINHAND,
                mode == AttackMode.IDLE ? this.npcItemsInv.getIdleItem() : this.npcItemsInv.getMeleeWeapon());
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        return isFriendlyAndAligned(player) ? "bree/man/friendly" : "bree/man/hostile";
    }

    /** onSpawnWithEgg: a dagger or an axe, and nothing in hand at rest. */
    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        Item[] weapons = {LOTRCombatItems.IRON_DAGGER, LOTRCombatItems.BRONZE_DAGGER, Items.IRON_AXE,
                LOTRToolItems.BRONZE_AXE, Items.STONE_AXE};
        this.npcItemsInv.setMeleeWeapon(new ItemStack(weapons[this.random.nextInt(weapons.length)]));
        this.npcItemsInv.setIdleItem(ItemStack.EMPTY);
        if (this.familyInfo.isMale() && this.random.nextInt(2000) == 0) {
            this.familyInfo.setName(CARROT_EATER_NAME);
            this.npcItemsInv.setIdleItem(new ItemStack(Items.CARROT));
        }
        return data;
    }

    /** dropFewItems: bones, and one time in six something from a Bree-land house. */
    @Override
    protected void dropNPCItems(ServerLevel level, boolean killedByPlayer, int looting) {
        super.dropNPCItems(level, killedByPlayer, looting);
        int bones = this.random.nextInt(2) + this.random.nextInt(looting + 1);
        for (int l = 0; l < bones; ++l) {
            spawnAtLocation(level, Items.BONE);
        }
        dropBreeItems(level, looting);
    }

    protected void dropBreeItems(ServerLevel level, int looting) {
        if (this.random.nextInt(6) == 0) {
            dropChestContents(level, LOTRChestContents.BREE_HOUSE, 1, 2 + looting);
        }
    }
}
