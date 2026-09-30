package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.halftroll;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRDrinkGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTREatGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRFollowHiringPlayerGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRHiredRemainStillGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRNearestAttackableTargetGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRFoods;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCAttributes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNames;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMaterialItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRToolMaterials;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
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
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.animal.rabbit.Rabbit;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityHalfTroll: a half-troll of Far Harad, 35 strong and hitting for
 * 6, seeing 24 blocks, who seeks out its people's enemies -- and rabbits
 * (vanilla's, standing in for LOTREntityRabbit) -- by day or dark. Half have
 * a mohawk, half have horns and half of those full horns. It eats and drinks
 * as half-trolls do, takes an orc's name, and leaves rotten flesh and troll
 * bones. Hired, it will wear only half-troll armour.
 *
 * <p>NOT ported yet: the killHalfTroll achievement (D7) and mini-quests (D14).
 */
public class LOTRHalfTrollEntity extends LOTRNPCEntity {

    private static final EntityDataAccessor<Byte> DATA_MODEL_FLAGS =
            SynchedEntityData.defineId(LOTRHalfTrollEntity.class, EntityDataSerializers.BYTE);

    public LOTRHalfTrollEntity(EntityType<? extends LOTRHalfTrollEntity> type, Level level) {
        super(type, level);
        setPathfindingMalus(PathType.WATER, -1.0f);
        this.spawnsInDarkness = true;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createNPCAttributes()
                .add(Attributes.MAX_HEALTH, 35.0)
                .add(Attributes.FOLLOW_RANGE, 24.0)
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(LOTRNPCAttributes.NPC_ATTACK_DAMAGE, 6.0)
                .add(LOTRNPCAttributes.HORSE_ATTACK_SPEED, 1.5);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_MODEL_FLAGS, (byte) 0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new LOTRHiredRemainStillGoal(this));
        this.goalSelector.addGoal(2, createHalfTrollAttackAI());
        this.goalSelector.addGoal(3, new LOTRFollowHiringPlayerGoal(this));
        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(5, new LOTREatGoal(this, LOTRFoods.HALF_TROLL, 6000));
        this.goalSelector.addGoal(5, new LOTRDrinkGoal(this, LOTRFoods.HALF_TROLL_DRINK, 6000));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0f, 0.02f));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, LOTRNPCEntity.class, 5.0f, 0.02f));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Mob.class, 8.0f, 0.02f));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        int target = addTargetTasks(true);
        this.targetSelector.addGoal(target + 1, new LOTRNearestAttackableTargetGoal(this, Rabbit.class, 1000, false, null));
    }

    protected Goal createHalfTrollAttackAI() {
        return new LOTRAttackOnCollideGoal(this, 1.4, false);
    }

    /** canReEquipHired: a hired half-troll's armour slots take only half-troll armour. */
    @Override
    public boolean canReEquipHired(EquipmentSlot slot, ItemStack stack) {
        if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
            Equippable equippable = stack.get(DataComponents.EQUIPPABLE);
            return equippable != null && equippable.assetId().filter(LOTRToolMaterials.HALF_TROLL_ASSET::equals).isPresent();
        }
        return super.canReEquipHired(slot, stack);
    }

    // --- The mohawk and horns -------------------------------------------------

    private boolean getModelFlag(int part) {
        return (this.entityData.get(DATA_MODEL_FLAGS) & 1 << part) != 0;
    }

    private void setModelFlag(int part, boolean flag) {
        int i = this.entityData.get(DATA_MODEL_FLAGS);
        int pow2 = 1 << part;
        this.entityData.set(DATA_MODEL_FLAGS, (byte) (flag ? i | pow2 : i & ~pow2));
    }

    public boolean hasMohawk() {
        return getModelFlag(1);
    }

    public void setHasMohawk(boolean flag) {
        setModelFlag(1, flag);
    }

    public boolean hasHorns() {
        return getModelFlag(2);
    }

    public void setHasHorns(boolean flag) {
        setModelFlag(2, flag);
    }

    public boolean hasFullHorns() {
        return getModelFlag(3);
    }

    public void setHasFullHorns(boolean flag) {
        setModelFlag(3, flag);
    }

    // --- The rest --------------------------------------------------------------

    @Override
    public LOTRFaction getFaction() {
        return LOTRFaction.HALF_TROLL;
    }

    @Override
    public float getAlignmentBonus() {
        return 1.0f;
    }

    @Override
    public void setupNPCName() {
        this.familyInfo.setName(LOTRNames.getOrcName(this.random));
    }

    @Override
    public String getNPCName() {
        return this.familyInfo.getName();
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        if (isFriendly(player)) {
            return this.hiredNPCInfo.getHiringPlayer() == player ? "halfTroll/halfTroll/hired" : "halfTroll/halfTroll/friendly";
        }
        return "halfTroll/halfTroll/hostile";
    }

    @Override
    public void onAttackModeChange(AttackMode mode, boolean mounted) {
        setItemSlot(EquipmentSlot.MAINHAND,
                mode == AttackMode.IDLE ? this.npcItemsInv.getIdleItem() : this.npcItemsInv.getMeleeWeapon());
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return LOTRSounds.HALF_TROLL_SAY;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return LOTRSounds.HALF_TROLL_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return LOTRSounds.HALF_TROLL_DEATH;
    }

    @Override
    protected int getBaseExperienceReward(ServerLevel level) {
        return 4 + this.random.nextInt(3);
    }

    @Override
    protected void dropNPCItems(ServerLevel level, boolean killedByPlayer, int looting) {
        super.dropNPCItems(level, killedByPlayer, looting);
        int flesh = this.random.nextInt(3) + this.random.nextInt(looting + 1);
        for (int l = 0; l < flesh; ++l) {
            spawnAtLocation(level, Items.ROTTEN_FLESH);
        }
        int bones = this.random.nextInt(2) + this.random.nextInt(looting + 1);
        for (int l = 0; l < bones; ++l) {
            spawnAtLocation(level, LOTRMaterialItems.TROLL_BONE);
        }
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        setHasMohawk(this.random.nextBoolean());
        if (this.random.nextBoolean()) {
            setHasHorns(true);
            setHasFullHorns(this.random.nextBoolean());
        }
        return data;
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("Mohawk", hasMohawk());
        output.putBoolean("Horns", hasHorns());
        output.putBoolean("HornsFull", hasFullHorns());
    }

    /** The name was once kept as "HalfTrollName". */
    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        setHasMohawk(input.getBooleanOr("Mohawk", false));
        setHasHorns(input.getBooleanOr("Horns", false));
        setHasFullHorns(input.getBooleanOr("HornsFull", false));
        input.getString("HalfTrollName").ifPresent(this.familyInfo::setName);
    }
}
