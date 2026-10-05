package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.ranger;

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
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRToolItems;

import net.minecraft.core.BlockPos;
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
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.PathType;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityDunedain: the Dúnedain of the North, "Name of the Dúnedain" --
 * men and women with Gondorian names, of the Rangers of the North. They keep
 * out of water, open doors, go looking for their people's enemies, fight with
 * a dagger or an axe, eat and drink as the Rangers do, and can be hired. Their
 * mounts come in iron horse armour. They wander anywhere as readily
 * (getBlockPathWeight 20 everywhere). Slain, they leave bones and one time in
 * six something from a Ranger's house.
 *
 * <p>NOT ported yet: mini-quests (D14), their natural spawn check (above y 62
 * on the biome's top block, grass or sand -- with the biomes, D10), and the
 * killDunedain achievement (D7).
 */
public class LOTRDunedainEntity extends LOTRManEntity {

    public LOTRDunedainEntity(EntityType<? extends LOTRDunedainEntity> type, Level level) {
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
        this.goalSelector.addGoal(2, createDunedainAttackAI());
        this.goalSelector.addGoal(3, new LOTRFollowHiringPlayerGoal(this));
        this.goalSelector.addGoal(4, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(6, new LOTREatGoal(this, getDunedainFoods(), 8000));
        this.goalSelector.addGoal(6, new LOTRDrinkGoal(this, getDunedainDrinks(), 8000));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0f, 0.02f));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, LOTRNPCEntity.class, 5.0f, 0.02f));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Mob.class, 8.0f, 0.02f));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        addTargetTasks(true);
    }

    protected Goal createDunedainAttackAI() {
        return new LOTRAttackOnCollideGoal(this, 1.4, false);
    }

    public LOTRFoods getDunedainFoods() {
        return LOTRFoods.RANGER;
    }

    public LOTRFoods getDunedainDrinks() {
        return LOTRFoods.RANGER_DRINK;
    }

    /** createMountToRide: a horse in iron horse armour. */
    @Override
    public Mob createMountToRide(ServerLevel level) {
        Mob horse = super.createMountToRide(level);
        if (horse != null) {
            horse.setItemSlot(EquipmentSlot.BODY, new ItemStack(Items.IRON_HORSE_ARMOR));
        }
        return horse;
    }

    @Override
    public LOTRFaction getFaction() {
        return LOTRFaction.RANGER_NORTH;
    }

    @Override
    public float getAlignmentBonus() {
        return 1.0f;
    }

    /** getBlockPathWeight: 20 everywhere. */
    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader level) {
        return 20.0f;
    }

    @Override
    public void setupNPCGender() {
        this.familyInfo.setMale(this.random.nextBoolean());
    }

    @Override
    public void setupNPCName() {
        this.familyInfo.setName(LOTRNames.getGondorName(this.random, this.familyInfo.isMale()));
    }

    @Override
    public String getNPCName() {
        return this.familyInfo.getName();
    }

    /** A plain Dúnadan is "Name of the Dúnedain"; the others keep "Name, the Kind". */
    @Override
    protected Component getNPCFormattedName(String npcName, Component kind) {
        if (getType() == LOTREntities.DUNEDAIN) {
            return Component.translatable("entity.lotr.dunedain.entityName", npcName);
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
        if (isDrunkard()) {
            return "rangerNorth/drunkard/neutral";
        }
        return isFriendly(player) ? "rangerNorth/man/friendly" : "rangerNorth/man/hostile";
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        Item[] weapons = {LOTRCombatItems.BARROW_BLADE, LOTRCombatItems.IRON_DAGGER, LOTRCombatItems.BRONZE_DAGGER,
                Items.IRON_AXE, LOTRToolItems.BRONZE_AXE, Items.STONE_AXE};
        this.npcItemsInv.setMeleeWeapon(new ItemStack(weapons[this.random.nextInt(weapons.length)]));
        this.npcItemsInv.setIdleItem(ItemStack.EMPTY);
        return data;
    }

    /** dropFewItems: bones, and one time in six something from a Ranger's house. */
    @Override
    protected void dropNPCItems(ServerLevel level, boolean killedByPlayer, int looting) {
        super.dropNPCItems(level, killedByPlayer, looting);
        int bones = this.random.nextInt(2) + this.random.nextInt(looting + 1);
        for (int l = 0; l < bones; ++l) {
            spawnAtLocation(level, Items.BONE);
        }
        dropDunedainItems(level, looting);
    }

    protected void dropDunedainItems(ServerLevel level, int looting) {
        if (this.random.nextInt(6) == 0) {
            dropChestContents(level, LOTRChestContents.RANGER_HOUSE, 1, 2 + looting);
        }
    }
}
