package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.dunland;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRCombatBlocks;
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
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.warg.LOTRWargBombardierEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;

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
import net.minecraft.world.level.storage.ValueInput;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityDunlending: a hillman of Dunland, man or woman, who keeps out of
 * the water, opens doors, seeks out Dunland's enemies with a club or trident
 * (two times in nine each), a wooden or stone sword, a stone axe or hoe, or a
 * stone spear, one in four in a fur hat, eats and drinks as Dunland does, and
 * leaves bones and now and then something from a Dunlending house. One in
 * 10000 comes riding an Uruk warg bombardier with an orc bomb for a hat and
 * another in hand.
 *
 * <p>NOT ported yet: mini-quests (D14), the pull of Dunland and Adorland on
 * their wandering and their spawning above y 62 on the biome's own top block
 * (with the biomes), and the killDunlending achievement (D7).
 */
public class LOTRDunlendingEntity extends LOTRManEntity {

    public LOTRDunlendingEntity(EntityType<? extends LOTRDunlendingEntity> type, Level level) {
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
        this.goalSelector.addGoal(2, createDunlendingAttackAI());
        this.goalSelector.addGoal(3, new LOTRFollowHiringPlayerGoal(this));
        this.goalSelector.addGoal(4, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(6, new LOTREatGoal(this, LOTRFoods.DUNLENDING, 8000));
        this.goalSelector.addGoal(6, new LOTRDrinkGoal(this, LOTRFoods.DUNLENDING_DRINK, 8000));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0f, 0.02f));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, LOTRNPCEntity.class, 5.0f, 0.02f));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Mob.class, 8.0f, 0.02f));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        addTargetTasks(true);
    }

    protected Goal createDunlendingAttackAI() {
        return new LOTRAttackOnCollideGoal(this, 1.5, false);
    }

    @Override
    public LOTRFaction getFaction() {
        return LOTRFaction.DUNLAND;
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
        this.familyInfo.setName(LOTRNames.getDunlendingName(this.random, this.familyInfo.isMale()));
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
        if (isDrunkard()) {
            return "dunlending/drunkard/neutral";
        }
        if (isFriendly(player)) {
            return this.hiredNPCInfo.getHiringPlayer() == player
                    ? "dunlending/dunlending/hired" : "dunlending/dunlending/friendly";
        }
        return "dunlending/dunlending/hostile";
    }

    /** Wearing an orc bomb for a hat (the one-in-10000 bomb rider): the renderer draws it. */
    public boolean isWearingBomb() {
        return getItemBySlot(EquipmentSlot.HEAD).is(LOTRCombatBlocks.ORC_BOMB.asItem());
    }

    @Override
    protected int getBaseExperienceReward(ServerLevel level) {
        return 4 + this.random.nextInt(3);
    }

    @Override
    protected void dropNPCItems(ServerLevel level, boolean killedByPlayer, int looting) {
        super.dropNPCItems(level, killedByPlayer, looting);
        int bones = this.random.nextInt(2) + this.random.nextInt(looting + 1);
        for (int l = 0; l < bones; ++l) {
            spawnAtLocation(level, Items.BONE);
        }
        dropDunlendingItems(level, looting);
    }

    protected void dropDunlendingItems(ServerLevel level, int looting) {
        if (this.random.nextInt(5) == 0) {
            dropChestContents(level, LOTRChestContents.DUNLENDING_HOUSE, 1, 2 + looting);
        }
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        Item weapon = switch (this.random.nextInt(9)) {
            case 0, 1 -> LOTRCombatItems.DUNLENDING_CLUB;
            case 2, 3 -> LOTRCombatItems.DUNLENDING_TRIDENT;
            case 4 -> Items.WOODEN_SWORD;
            case 5 -> Items.STONE_SWORD;
            case 6 -> Items.STONE_AXE;
            case 7 -> Items.STONE_HOE;
            default -> LOTRCombatItems.STONE_SPEAR;
        };
        this.npcItemsInv.setMeleeWeapon(new ItemStack(weapon));
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        if (this.random.nextInt(4) == 0) {
            setItemSlot(EquipmentSlot.HEAD, new ItemStack(LOTRCombatItems.FUR_HAT));
        }
        if (this.random.nextInt(10000) == 0) {
            ServerLevel server = level.getLevel();
            LOTRWargBombardierEntity warg = LOTREntities.URUK_WARG_BOMBARDIER.create(server, EntitySpawnReason.JOCKEY);
            if (warg != null) {
                warg.snapTo(getX(), getY(), getZ(), getYRot(), 0.0f);
                warg.finalizeSpawn(level, difficulty, EntitySpawnReason.JOCKEY, null);
                warg.isNPCPersistent = this.isNPCPersistent;
                server.addFreshEntity(warg);
                startRiding(warg, true, false);
                setItemSlot(EquipmentSlot.HEAD, new ItemStack(LOTRCombatBlocks.ORC_BOMB));
                this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatBlocks.ORC_BOMB));
                this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
            }
        }
        return data;
    }

    /** The name was once kept as "DunlendingName". */
    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        input.getString("DunlendingName").ifPresent(this.familyInfo::setName);
    }
}
