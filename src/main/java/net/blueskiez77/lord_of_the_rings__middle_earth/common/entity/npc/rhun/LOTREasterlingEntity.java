package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.rhun;

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
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMiscItems;

import net.minecraft.core.component.DataComponents;
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
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.PathType;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityEasterling: a man or woman of Rhûn. They keep out of the water,
 * open doors, eat and drink as Rhûn does, fight back with a Rhûnic, iron or
 * bronze dagger but seek no one out (their soldiers do), and leave bones and,
 * one time in six, something from an Easterling house. Drunkards have their
 * own speech.
 *
 * <p>NOT ported yet: the pull of the Rhûn lands on their wandering and their
 * spawning above y 62 on the biome's top block (with the biomes), the
 * killEasterling achievement (D7), and mini-quests (D14).
 */
public class LOTREasterlingEntity extends LOTRManEntity {

    private static final Item[] WEAPONS = {LOTRCombatItems.RHUNIC_DAGGER, LOTRCombatItems.IRON_DAGGER,
            LOTRCombatItems.BRONZE_DAGGER};

    public LOTREasterlingEntity(EntityType<? extends LOTREasterlingEntity> type, Level level) {
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
        this.goalSelector.addGoal(2, createEasterlingAttackAI());
        this.goalSelector.addGoal(3, new LOTRFollowHiringPlayerGoal(this));
        this.goalSelector.addGoal(4, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(6, new LOTREatGoal(this, LOTRFoods.RHUN, 8000));
        this.goalSelector.addGoal(6, new LOTRDrinkGoal(this, LOTRFoods.RHUN_DRINK, 8000));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0f, 0.02f));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, LOTRNPCEntity.class, 5.0f, 0.02f));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Mob.class, 8.0f, 0.02f));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        addTargetTasks(false);
    }

    protected Goal createEasterlingAttackAI() {
        return new LOTRAttackOnCollideGoal(this, 1.4, false);
    }

    /** LOTRItemHaradRobes.setRobesColor on a kaftan piece. */
    public static ItemStack kaftan(Item item, int colour) {
        ItemStack kaftan = new ItemStack(item);
        kaftan.set(DataComponents.DYED_COLOR, new DyedItemColor(colour));
        return kaftan;
    }

    /** A market trader's kaftan and kaftan leggings, both of one colour. */
    protected void wearKaftan(int colour) {
        setItemSlot(EquipmentSlot.CHEST, kaftan(LOTRMiscItems.KAFTAN, colour));
        setItemSlot(EquipmentSlot.LEGS, kaftan(LOTRMiscItems.KAFTAN_LEGGINGS, colour));
    }

    @Override
    public LOTRFaction getFaction() {
        return LOTRFaction.RHUDEL;
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
        this.familyInfo.setName(LOTRNames.getRhunicName(this.random, this.familyInfo.isMale()));
    }

    @Override
    public String getNPCName() {
        return this.familyInfo.getName();
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        if (isDrunkard()) {
            return "rhun/drunkard/neutral";
        }
        return isFriendly(player) ? "rhun/man/friendly" : "rhun/man/hostile";
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
        dropRhunItems(level, killedByPlayer, looting);
    }

    protected void dropRhunItems(ServerLevel level, boolean killedByPlayer, int looting) {
        if (this.random.nextInt(6) == 0) {
            dropChestContents(level, LOTRChestContents.EASTERLING_HOUSE, 1, 2 + looting);
        }
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(WEAPONS[this.random.nextInt(WEAPONS.length)]));
        this.npcItemsInv.setIdleItem(ItemStack.EMPTY);
        return data;
    }
}
