package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.dorwinion;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
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

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathType;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityDorwinionMan (the Dorwinman): the men of Dorwinion, "Name of
 * Dorwinion". They keep out of the water, open doors, eat and drink as
 * Dorwinion does, and run from a fight rather than join it.
 *
 * <p>NOT ported yet: mini-quests (DORWINION, D14), the pull of Dorwinion on
 * their wandering and their natural spawning (with the biomes), and the
 * killDorwinion achievement (D7).
 */
public class LOTRDorwinionManEntity extends LOTRManEntity {

    public LOTRDorwinionManEntity(EntityType<? extends LOTRDorwinionManEntity> type, Level level) {
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
        this.goalSelector.addGoal(2, createDorwinionAttackAI());
        this.goalSelector.addGoal(3, new LOTRFollowHiringPlayerGoal(this));
        this.goalSelector.addGoal(4, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(6, new LOTREatGoal(this, LOTRFoods.DORWINION, 8000));
        this.goalSelector.addGoal(6, new LOTRDrinkGoal(this, LOTRFoods.DORWINION_DRINK, 8000));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0f, 0.02f));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, LOTRNPCEntity.class, 5.0f, 0.02f));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Mob.class, 8.0f, 0.02f));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        addTargetTasks(false);
    }

    protected Goal createDorwinionAttackAI() {
        return new PanicGoal(this, 1.7);
    }

    @Override
    public LOTRFaction getFaction() {
        return LOTRFaction.DORWINION;
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
        this.familyInfo.setName(LOTRNames.getDorwinionName(this.random, this.familyInfo.isMale()));
    }

    @Override
    public String getNPCName() {
        return this.familyInfo.getName();
    }

    /** A plain Dorwinion man is "Name of Dorwinion"; the others keep "Name, the Kind". */
    @Override
    protected Component getNPCFormattedName(String npcName, Component kind) {
        if (getType() == LOTREntities.DORWINION_MAN) {
            return Component.translatable("entity.lotr.dorwinion_man.entityName", npcName);
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
        return isFriendly(player) ? "dorwinion/man/friendly" : "dorwinion/man/hostile";
    }

    /** dropFewItems: bones, and one time in five something from a Dorwinion house. */
    @Override
    protected void dropNPCItems(ServerLevel level, boolean killedByPlayer, int looting) {
        super.dropNPCItems(level, killedByPlayer, looting);
        int bones = this.random.nextInt(2) + this.random.nextInt(looting + 1);
        for (int l = 0; l < bones; ++l) {
            spawnAtLocation(level, Items.BONE);
        }
        dropDorwinionItems(level, looting);
    }

    protected void dropDorwinionItems(ServerLevel level, int looting) {
        if (this.random.nextInt(5) == 0) {
            dropChestContents(level, LOTRChestContents.DORWINION_HOUSE, 1, 2 + looting);
        }
    }
}
