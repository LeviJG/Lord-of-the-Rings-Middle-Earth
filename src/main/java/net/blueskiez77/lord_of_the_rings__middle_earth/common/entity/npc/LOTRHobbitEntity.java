package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRDrinkGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTREatGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRHobbitChildFollowGoodPlayerGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRHobbitSmokeGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRNPCAvoidEvilPlayerGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRNPCFollowParentGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRNPCFollowSpouseGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRNPCMarryGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRNPCMateGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.bree.LOTRRuffianBruteEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.ent.LOTRHuornBaseEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.orc.LOTROrcEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.spider.LOTRSpiderEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.troll.LOTRTrollEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.warg.LOTRWargEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMaterialItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMiscItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.quest.LOTRMiniQuest;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.quest.LOTRMiniQuestFactory;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathType;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityHobbit: the folk of the Shire. They keep out of the water, open
 * doors, run from danger and from players the Shire hates, marry and raise
 * families, and eat, drink and smoke a good deal. They neither seek fights
 * nor fight back; the Shire's bounders, shirriffs, farmers and orcharders do.
 *
 * <p>NOT ported yet: the Shire's pull on their wandering
 * and their natural spawning (with the biomes), mini-quests, and the
 * achievements for killing, marrying and talking to a drunk hobbit.
 */
public class LOTRHobbitEntity extends LOTRManEntity {

    public LOTRHobbitEntity(EntityType<? extends LOTRHobbitEntity> type, Level level) {
        super(type, level);
        setPathfindingMalus(PathType.WATER, -1.0f);
        if (getNavigation() instanceof GroundPathNavigation navigation) {
            navigation.setCanOpenDoors(true);
        }
        this.familyInfo.marriageEntityClass = LOTRHobbitEntity.class;
        this.familyInfo.marriageRing = LOTRMiscItems.HOBBIT_MARRIAGE_RING;
        this.familyInfo.marriageAlignmentRequired = 100.0f;
        this.familyInfo.potentialMaxChildren = 4;
        this.familyInfo.timeToMature = 48000;
        this.familyInfo.breedingDelay = 24000;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createNPCAttributes()
                .add(Attributes.MAX_HEALTH, 16.0)
                .add(Attributes.MOVEMENT_SPEED, 0.2);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, LOTROrcEntity.class, 12.0f, 1.5, 1.8));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, LOTRWargEntity.class, 12.0f, 1.5, 1.8));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, LOTRTrollEntity.class, 12.0f, 1.5, 1.8));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, LOTRSpiderEntity.class, 12.0f, 1.5, 1.8));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, LOTRRuffianBruteEntity.class, 8.0f, 1.0, 1.5));
        // LOTREntityAIAvoidHuorn: only a huorn that has woken.
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, LOTRHuornBaseEntity.class,
                huorn -> ((LOTRHuornBaseEntity) huorn).isHuornActive(), 12.0f, 1.5, 1.8,
                EntitySelector.NO_CREATIVE_OR_SPECTATOR));
        if (panics()) {
            this.goalSelector.addGoal(2, new PanicGoal(this, 1.6));
        }
        this.goalSelector.addGoal(3, new LOTRNPCAvoidEvilPlayerGoal(this, 8.0f, 1.5, 1.8));
        this.goalSelector.addGoal(4, new LOTRHobbitChildFollowGoodPlayerGoal(this, 12.0f, 1.5));
        this.goalSelector.addGoal(5, new LOTRNPCMarryGoal(this, 1.3));
        this.goalSelector.addGoal(6, new LOTRNPCMateGoal(this, 1.3));
        this.goalSelector.addGoal(7, new LOTRNPCFollowParentGoal(this, 1.4));
        this.goalSelector.addGoal(8, new LOTRNPCFollowSpouseGoal(this, 1.1));
        this.goalSelector.addGoal(9, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(10, new WaterAvoidingRandomStrollGoal(this, 1.1));
        this.goalSelector.addGoal(11, new LOTREatGoal(this, getHobbitFoods(), 3000));
        this.goalSelector.addGoal(11, new LOTRDrinkGoal(this, getHobbitDrinks(), 3000));
        this.goalSelector.addGoal(11, new LOTRHobbitSmokeGoal(this, 4000));
        this.goalSelector.addGoal(12, new LookAtPlayerGoal(this, Player.class, 8.0f, 0.05f));
        this.goalSelector.addGoal(12, new LookAtPlayerGoal(this, LOTRNPCEntity.class, 5.0f, 0.05f));
        this.goalSelector.addGoal(13, new LookAtPlayerGoal(this, Mob.class, 8.0f, 0.02f));
        this.goalSelector.addGoal(14, new RandomLookAroundGoal(this));
    }

    /** False for the hobbits that removed EntityAIPanic to stand and fight. */
    protected boolean panics() {
        return true;
    }

    public LOTRFoods getHobbitFoods() {
        return LOTRFoods.HOBBIT;
    }

    public LOTRFoods getHobbitDrinks() {
        return LOTRFoods.HOBBIT_DRINK;
    }

    @Override
    public LOTRFaction getFaction() {
        return LOTRFaction.HOBBIT;
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
        this.familyInfo.setName(LOTRNames.getHobbitName(this.random, this.familyInfo.isMale()));
    }

    @Override
    public String getNPCName() {
        return this.familyInfo.getName();
    }

    @Override
    public void changeNPCNameForMarriage(LOTRNPCEntity spouse) {
        if (this.familyInfo.isMale()) {
            LOTRNames.changeHobbitSurnameForMarriage(this, spouse);
        } else if (spouse.familyInfo.isMale()) {
            LOTRNames.changeHobbitSurnameForMarriage(spouse, this);
        }
    }

    @Override
    public void createNPCChildName(LOTRNPCEntity maleParent, LOTRNPCEntity femaleParent) {
        this.familyInfo.setName(LOTRNames.getHobbitChildNameForParent(this.random, this.familyInfo.isMale(), maleParent));
    }

    /** onArtificalSpawn: one in ten spawned by hand comes as a child. */
    @Override
    public void onArtificalSpawn() {
        if (getClass() == this.familyInfo.marriageEntityClass && this.random.nextInt(10) == 0) {
            this.familyInfo.setChild();
        }
    }

    @Override
    public void onAttackModeChange(AttackMode mode, boolean mounted) {
        setItemSlot(EquipmentSlot.MAINHAND,
                mode == AttackMode.IDLE ? this.npcItemsInv.getIdleItem() : this.npcItemsInv.getMeleeWeapon());
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        if (isDrunkard()) {
            return "hobbit/drunkard/neutral";
        }
        if (isFriendlyAndAligned(player)) {
            return isBaby() ? "hobbit/child/friendly" : "hobbit/hobbit/friendly";
        }
        return isBaby() ? "hobbit/child/hostile" : "hobbit/hobbit/hostile";
    }

    @Override
    protected int getBaseExperienceReward(ServerLevel level) {
        return 1 + this.random.nextInt(3);
    }

    /** dropFewItems: hobbit bones, and now and then something from a study or a larder. */
    @Override
    protected void dropNPCItems(ServerLevel level, boolean killedByPlayer, int looting) {
        super.dropNPCItems(level, killedByPlayer, looting);
        int bones = this.random.nextInt(2) + this.random.nextInt(looting + 1);
        for (int l = 0; l < bones; ++l) {
            spawnAtLocation(level, LOTRMaterialItems.HOBBIT_BONE);
        }
        dropHobbitItems(level, looting);
    }

    protected void dropHobbitItems(ServerLevel level, int looting) {
        if (this.random.nextInt(8) == 0) {
            dropChestContents(level, LOTRChestContents.HOBBIT_HOLE_STUDY, 1, 1 + looting);
        }
        if (this.random.nextInt(4) == 0) {
            dropChestContents(level, LOTRChestContents.HOBBIT_HOLE_LARDER, 1, 2 + looting);
        }
    }

    @Override
    public @Nullable LOTRMiniQuest createMiniQuest() {
        return LOTRMiniQuestFactory.HOBBIT.createQuest(this);
    }
}
