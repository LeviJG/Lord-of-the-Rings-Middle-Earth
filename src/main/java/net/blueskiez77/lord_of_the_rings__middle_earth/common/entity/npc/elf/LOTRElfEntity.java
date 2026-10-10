package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.elf;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRParticles;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRDrinkGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTREatGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRFollowHiringPlayerGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRHiredRemainStillGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRRangedAttackGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRFoods;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCAttributes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNames;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRFoodItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMaterialItems;

import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import org.jspecify.annotations.Nullable;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.EntitySpawnReason;

/**
 * LOTREntityElf: the Eldar. Hardy (30 health), untouched by poison, and
 * seeking out their people's enemies -- with a blade up close and a bow from
 * sixteen blocks out. They keep out of the water, open doors, eat and drink
 * as elves do, bear Sindarin or Quenya names, and leave elf bones and arrows
 * when slain, and now and then lembas to a player. A male elf cries out as it
 * attacks and now and then speaks idly.
 *
 * <p>An elf whose "BoopBoopBaDoop" flag is set is a jazz elf: every so often
 * it plays a solo for three to eighteen seconds, head bobbing, spinning on
 * the spot in shifting colours with its saxophone, notes drifting off it.
 *
 * <p>An elf bows to a friendly player within eight blocks who belongs to the
 * bowing-elves group, holding the bow for two seconds, unless it has a
 * target. The original read that group from the mod authors' web service of
 * player details; here it is the vanilla player tag {@value #BOWING_ELVES_TAG}
 * (user decision), given with {@code /tag <player> add lotr.bowing_elves}.
 */
public abstract class LOTRElfEntity extends LOTRNPCEntity {

    private static final EntityDataAccessor<Byte> DATA_JAZZ =
            SynchedEntityData.defineId(LOTRElfEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Integer> DATA_BOWING_TICK =
            SynchedEntityData.defineId(LOTRElfEntity.class, EntityDataSerializers.INT);

    /** ExclusiveGroup.BOWING_ELVES, as a player tag. */
    public static final String BOWING_ELVES_TAG = "lotr.bowing_elves";

    private @Nullable Goal rangedAttackAI;
    private @Nullable Goal meleeAttackAI;
    private int soloTick;
    private float soloSpinSpeed;
    public float soloSpin;
    public float prevSoloSpin;
    private float bowAmount;
    private float prevBowAmount;

    protected LOTRElfEntity(EntityType<? extends LOTRElfEntity> type, Level level) {
        super(type, level);
        setPathfindingMalus(PathType.WATER, -1.0f);
        if (getNavigation() instanceof GroundPathNavigation navigation) {
            navigation.setCanOpenDoors(true);
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createNPCAttributes()
                .add(Attributes.MAX_HEALTH, 30.0)
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(LOTRNPCAttributes.NPC_RANGED_ACCURACY, 0.5);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_JAZZ, (byte) 0);
        builder.define(DATA_BOWING_TICK, 0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new LOTRHiredRemainStillGoal(this));
        this.goalSelector.addGoal(3, new LOTRFollowHiringPlayerGoal(this));
        this.goalSelector.addGoal(4, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(6, new LOTREatGoal(this, LOTRFoods.ELF, 12000));
        this.goalSelector.addGoal(6, new LOTRDrinkGoal(this, getElfDrinks(), 8000));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0f, 0.02f));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, LOTRNPCEntity.class, 5.0f, 0.02f));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Mob.class, 8.0f, 0.02f));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        addTargetTasks(true);
    }

    protected Goal createElfMeleeAttackAI() {
        return new LOTRAttackOnCollideGoal(this, 1.5, false);
    }

    protected Goal createElfRangedAttackAI() {
        return new LOTRRangedAttackGoal(this, 1.25, 40, 60, 16.0f);
    }

    /** The melee attack goal, made once. */
    protected Goal meleeAttackAI() {
        if (this.meleeAttackAI == null) {
            this.meleeAttackAI = createElfMeleeAttackAI();
        }
        return this.meleeAttackAI;
    }

    /** The ranged attack goal, made once. */
    protected Goal rangedAttackAI() {
        if (this.rangedAttackAI == null) {
            this.rangedAttackAI = createElfRangedAttackAI();
        }
        return this.rangedAttackAI;
    }

    public LOTRFoods getElfDrinks() {
        return LOTRFoods.ELF_DRINK;
    }

    /** addPotionEffect: poison passes elves by. */
    @Override
    public boolean canBeAffected(MobEffectInstance effect) {
        return !effect.is(MobEffects.POISON) && super.canBeAffected(effect);
    }

    @Override
    public void setupNPCGender() {
        this.familyInfo.setMale(this.random.nextBoolean());
    }

    @Override
    public void setupNPCName() {
        this.familyInfo.setName(LOTRNames.getSindarinOrQuenyaName(this.random, this.familyInfo.isMale()));
    }

    @Override
    public String getNPCName() {
        return this.familyInfo.getName();
    }

    /** A jazz elf goes by "Jazz-elf". */
    @Override
    public Component getEntityClassName() {
        return isJazz() ? Component.literal("Jazz-elf") : super.getEntityClassName();
    }

    @Override
    public @Nullable SoundEvent getAttackSound() {
        return this.familyInfo.isMale() ? LOTRSounds.ELF_MALE_ATTACK : super.getAttackSound();
    }

    /** getLivingSound: a male elf out of a fight now and then speaks. */
    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        if (getTarget() == null && this.random.nextInt(10) == 0 && this.familyInfo.isMale()) {
            return LOTRSounds.ELF_MALE_SAY;
        }
        return super.getAmbientSound();
    }

    @Override
    public void onAttackModeChange(AttackMode mode, boolean mounted) {
        Goal melee = meleeAttackAI();
        Goal ranged = rangedAttackAI();
        if (mode == AttackMode.IDLE) {
            this.goalSelector.removeGoal(melee);
            this.goalSelector.removeGoal(ranged);
            setItemSlot(EquipmentSlot.MAINHAND, this.npcItemsInv.getIdleItem());
        } else if (mode == AttackMode.MELEE) {
            this.goalSelector.removeGoal(melee);
            this.goalSelector.removeGoal(ranged);
            this.goalSelector.addGoal(2, melee);
            setItemSlot(EquipmentSlot.MAINHAND, this.npcItemsInv.getMeleeWeapon());
        } else if (mode == AttackMode.RANGED) {
            this.goalSelector.removeGoal(melee);
            this.goalSelector.removeGoal(ranged);
            this.goalSelector.addGoal(2, ranged);
            setItemSlot(EquipmentSlot.MAINHAND, this.npcItemsInv.getRangedWeapon());
        }
    }

    // --- Jazz ------------------------------------------------------------------------

    private boolean getJazzFlag(int i) {
        return (this.entityData.get(DATA_JAZZ) & 1 << i) != 0;
    }

    private void setJazzFlag(int i, boolean flag) {
        byte b = this.entityData.get(DATA_JAZZ);
        int pow2 = 1 << i;
        this.entityData.set(DATA_JAZZ, flag ? (byte) (b | pow2) : (byte) (b & ~pow2));
    }

    public boolean isJazz() {
        return getJazzFlag(0);
    }

    public void setJazz(boolean flag) {
        setJazzFlag(0, flag);
    }

    public boolean isSolo() {
        return getJazzFlag(1);
    }

    private void setSolo(boolean flag) {
        setJazzFlag(1, flag);
    }

    public float getSoloSpin(float partialTick) {
        return this.prevSoloSpin + (this.soloSpin - this.prevSoloSpin) * partialTick;
    }

    // --- Bowing -----------------------------------------------------------------------

    /** getBowingAmount: 0 upright to 1 fully bowed, eased over five ticks. */
    public float getBowingAmount(float partialTick) {
        return this.prevBowAmount + (this.bowAmount - this.prevBowAmount) * partialTick;
    }

    private int getBowingTick() {
        return this.entityData.get(DATA_BOWING_TICK);
    }

    private void setBowingTick(int tick) {
        this.entityData.set(DATA_BOWING_TICK, tick);
    }

    /**
     * onLivingUpdate's bowing: counted up to 40 ticks while a friendly player
     * of the group is near and nothing is being fought, then held at -1 until
     * they go; the elf stops and faces the player while it counts.
     */
    private void updateBowing() {
        if (level().isClientSide()) {
            this.prevBowAmount = this.bowAmount;
            int tick = getBowingTick();
            if (tick <= 0 && this.bowAmount > 0.0f) {
                this.bowAmount = Math.max(this.bowAmount - 0.2f, 0.0f);
            } else if (tick > 0 && this.bowAmount < 1.0f) {
                this.bowAmount = Math.min(this.bowAmount + 0.2f, 1.0f);
            }
            return;
        }
        double range = 8.0;
        double rangeSq = range * range;
        java.util.List<Player> players = level().getEntitiesOfClass(Player.class, getBoundingBox().inflate(range),
                player -> player.isAlive() && isFriendly(player) && distanceToSqr(player) <= rangeSq
                        && player.entityTags().contains(BOWING_ELVES_TAG));
        if (players.isEmpty() || getTarget() != null) {
            setBowingTick(0);
            return;
        }
        int tick = getBowingTick();
        if (tick >= 0) {
            ++tick;
        }
        if (tick > 40) {
            tick = -1;
        }
        setBowingTick(tick);
        if (tick >= 0) {
            getNavigation().stop();
            Player bowingPlayer = players.get(0);
            float bowLook = (float) Math.toDegrees(Math.atan2(bowingPlayer.getZ() - getZ(), bowingPlayer.getX() - getX()));
            setYRot(bowLook - 90.0f);
            setYHeadRot(bowLook - 90.0f);
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        updateBowing();
        if (!isJazz()) {
            return;
        }
        if (!level().isClientSide()) {
            if (this.soloTick > 0) {
                --this.soloTick;
                setXRot(-10.0f + (Mth.sin(this.soloTick * 0.3f) + 1.0f) / 2.0f * -30.0f);
            } else if (this.random.nextInt(200) == 0) {
                this.soloTick = 60 + this.random.nextInt(300);
            }
            setSolo(this.soloTick > 0);
        } else if (isSolo()) {
            if (this.random.nextInt(3) == 0) {
                level().addParticle(LOTRParticles.MUSIC, getX(), getBoundingBox().minY + getEyeHeight(), getZ(),
                        Mth.nextDouble(this.random, -0.1, 0.1), Mth.nextDouble(this.random, -0.1, 0.1),
                        Mth.nextDouble(this.random, -0.1, 0.1));
            }
            if (this.soloSpinSpeed == 0.0f || this.random.nextInt(30) == 0) {
                this.soloSpinSpeed = Mth.randomBetween(this.random, -25.0f, 25.0f);
            }
            this.prevSoloSpin = this.soloSpin;
            this.soloSpin += this.soloSpinSpeed;
        } else {
            this.soloSpin = 0.0f;
            this.prevSoloSpin = 0.0f;
            this.soloSpinSpeed = 0.0f;
        }
    }

    // --- Drops -----------------------------------------------------------------------

    /** dropFewItems: elf bones, arrows, and the people's own. */
    @Override
    protected void dropNPCItems(ServerLevel level, boolean killedByPlayer, int looting) {
        super.dropNPCItems(level, killedByPlayer, looting);
        int bones = this.random.nextInt(2) + this.random.nextInt(looting + 1);
        for (int l = 0; l < bones; ++l) {
            spawnAtLocation(level, LOTRMaterialItems.ELF_BONE);
        }
        dropNPCAmmo(level, Items.ARROW, looting);
        dropElfItems(level, killedByPlayer, looting);
    }

    /** dropElfItems: to a player, lembas one time in 40, less with looting. */
    protected void dropElfItems(ServerLevel level, boolean killedByPlayer, int looting) {
        if (killedByPlayer && this.random.nextInt(Math.max(40 - looting * 8, 1)) == 0) {
            spawnAtLocation(level, LOTRFoodItems.LEMBAS);
        }
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("BoopBoopBaDoop", isJazz());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        setJazz(input.getBooleanOr("BoopBoopBaDoop", false));
    }

    /** canElfSpawnHere: where its kind of elf may spawn naturally. */
    public abstract boolean canElfSpawnHere(LevelAccessor level);

    /** getCanSpawnHere: where its kind of elf may (canElfSpawnHere). */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, EntitySpawnReason reason) {
        return super.checkSpawnRules(level, reason) && (this.liftSpawnRestrictions || canElfSpawnHere(level));
    }
}
