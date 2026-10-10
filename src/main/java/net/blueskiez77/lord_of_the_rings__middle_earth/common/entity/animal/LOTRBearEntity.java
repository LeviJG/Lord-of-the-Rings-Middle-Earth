package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal;

import java.util.Locale;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRSpawnEggItem;

import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import org.jspecify.annotations.Nullable;
import net.minecraft.world.level.LevelAccessor;

/**
 * LOTREntityBear: light, dark or black, a cub taking either parent's colour.
 * Its temper is the lion's (see LOTRLionBaseEntity): an adult that is hurt, or
 * whose cub is hurt within 12 blocks, stays angry for ten seconds after it
 * loses its target, attacking and turning on players it can see.
 *
 * <p>A group spawns an adult first and then cubs half the time
 * (BearGroupSpawnData, which is vanilla's AgeableMobGroupData at 0.5). One
 * bear in ten thousand is named Wojtek.
 *
 * <p>Drops are {@code lotr:entities/bear}: one to three fur plus looting. The
 * rug of its colour drops in {@link #dropCustomDeathLoot}.
 */
public class LOTRBearEntity extends Animal implements LOTRAnimalSpawnConditions {

    /** dataWatcher 18. */
    private static final EntityDataAccessor<Byte> DATA_TYPE =
            SynchedEntityData.defineId(LOTRBearEntity.class, EntityDataSerializers.BYTE);
    /** dataWatcher 20. */
    private static final EntityDataAccessor<Boolean> DATA_HOSTILE =
            SynchedEntityData.defineId(LOTRBearEntity.class, EntityDataSerializers.BOOLEAN);

    private int hostileTick;

    public LOTRBearEntity(EntityType<? extends LOTRBearEntity> type, Level level) {
        super(type, level);
        setBearType(BearType.forID(this.random.nextInt(BearType.values().length)));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 40.0)
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.ATTACK_DAMAGE, 4.0);
    }

    private boolean angryAdult() {
        return !isBaby() && this.hostileTick > 0;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new LOTRAttackOnCollideGoal(this, 1.7, false) {
            @Override
            public boolean canUse() {
                return angryAdult() && super.canUse();
            }
        });
        this.goalSelector.addGoal(2, new PanicGoal(this, 1.5) {
            @Override
            public boolean canUse() {
                return isBaby() && super.canUse();
            }
        });
        this.goalSelector.addGoal(3, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.4, this::isFood, false));
        this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.4));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0f));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 0, true, false, null) {
            @Override
            public boolean canUse() {
                return angryAdult() && super.canUse();
            }
        });
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_TYPE, (byte) 0);
        builder.define(DATA_HOSTILE, false);
    }

    /** onSpawnWithEgg. */
    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        LOTRSpawnEggItem.playHatchSound(this, reason);
        if (groupData == null) {
            groupData = new AgeableMobGroupData(0.5f);
        }
        if (this.random.nextInt(10000) == 0) {
            setCustomName(Component.literal("Wojtek"));
        }
        return super.finalizeSpawn(level, difficulty, reason, groupData);
    }

    public BearType getBearType() {
        return BearType.forID(this.entityData.get(DATA_TYPE));
    }

    public void setBearType(BearType type) {
        this.entityData.set(DATA_TYPE, (byte) type.bearID);
    }

    @Override
    public boolean doHurtTarget(ServerLevel level, Entity target) {
        return target.hurtServer(level, damageSources().mobAttack(this),
                (float) getAttributeValue(Attributes.ATTACK_DAMAGE));
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        boolean hurt = super.hurtServer(level, source, damage);
        if (hurt && source.getEntity() instanceof LivingEntity attacker) {
            if (isBaby()) {
                for (LOTRBearEntity bear : level.getEntitiesOfClass(LOTRBearEntity.class,
                        getBoundingBox().inflate(12.0), bear -> bear != this && !bear.isBaby())) {
                    bear.becomeAngryAt(attacker);
                }
            } else {
                becomeAngryAt(attacker);
            }
        }
        return hurt;
    }

    public void becomeAngryAt(LivingEntity entity) {
        setTarget(entity);
        this.hostileTick = 200;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (level() instanceof ServerLevel) {
            LivingEntity target = getTarget();
            if (target != null && (!target.isAlive() || target instanceof Player player && player.isCreative())) {
                setTarget(null);
            }
            if (this.hostileTick > 0 && getTarget() == null) {
                --this.hostileTick;
            }
            setHostile(this.hostileTick > 0);
            if (isHostile()) {
                resetLove();
            }
        }
    }

    public boolean isHostile() {
        return this.entityData.get(DATA_HOSTILE);
    }

    public void setHostile(boolean hostile) {
        this.entityData.set(DATA_HOSTILE, hostile);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (isHostile()) {
            return InteractionResult.PASS;
        }
        return super.mobInteract(player, hand);
    }

    /** isBreedingItem: Items.fish, which in 1.7.10 was every raw fish. */
    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(Items.COD) || stack.is(Items.SALMON) || stack.is(Items.TROPICAL_FISH)
                || stack.is(Items.PUFFERFISH);
    }

    /** createChild: the colour of one parent or the other. */
    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
        LOTRBearEntity child = LOTREntities.BEAR.create(level, EntitySpawnReason.BREEDING);
        if (child != null && partner instanceof LOTRBearEntity mate) {
            child.setBearType(this.random.nextBoolean() ? getBearType() : mate.getBearType());
        }
        return child;
    }

    /** dropFewItems' rug: only a player's kill, 1 in (30 - 5 per looting level). */
    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean killedByPlayer) {
        super.dropCustomDeathLoot(level, source, killedByPlayer);
        if (killedByPlayer) {
            LOTRRugDrops.dropRug(this, level, source, getBearType().rug());
        }
    }

    /** getExperiencePoints: 2-4. */
    @Override
    protected int getBaseExperienceReward(ServerLevel level) {
        return 2 + this.random.nextInt(3);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putByte("BearType", (byte) getBearType().bearID);
        output.putInt("Angry", this.hostileTick);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        // Only if saved: otherwise it keeps the colour it was given at random.
        setBearType(BearType.forID(input.getByteOr("BearType", (byte) getBearType().bearID)));
        this.hostileTick = input.getIntOr("Angry", 0);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return LOTRSounds.BEAR_SAY;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return LOTRSounds.BEAR_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return LOTRSounds.BEAR_DEATH;
    }

    @Override
    public int getAmbientSoundInterval() {
        return 200;
    }

    public enum BearType {
        LIGHT(0), DARK(1), BLACK(2);

        public final int bearID;

        BearType(int id) {
            this.bearID = id;
        }

        public static BearType forID(int id) {
            for (BearType t : values()) {
                if (t.bearID == id) {
                    return t;
                }
            }
            return LIGHT;
        }

        public String textureName() {
            return name().toLowerCase(Locale.ROOT);
        }

        /** The bearRug item of this colour. */
        public net.minecraft.world.item.Item rug() {
            return switch (this) {
                case LIGHT -> net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItems.LIGHT_BEAR_RUG;
                case DARK -> net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItems.DARK_BEAR_RUG;
                case BLACK -> net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItems.BLACK_BEAR_RUG;
            };
        }
    }

    /** canWorldGenSpawnAt: only where the variant here has trees. */
    @Override
    public boolean canWorldGenSpawnAt(int i, int j, int k, net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome biome,
                                      net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant variant) {
        return biome.decorator.getVariantTreesPerChunk(variant) >= 1;
    }

    /** getCanSpawnHere: in Middle-earth, only where the variant here has trees. */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, EntitySpawnReason reason) {
        net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRWorldChunkManager chunkManager = level instanceof net.minecraft.world.level.WorldGenLevel w
                ? net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRWorldChunkManager.of(w) : null;
        net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome biome =
                net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiomes.of(level.getBiome(blockPosition()));
        if (chunkManager != null && biome != null) {
            return super.checkSpawnRules(level, reason)
                    && canWorldGenSpawnAt(getBlockX(), getBlockY(), getBlockZ(), biome, chunkManager.getBiomeVariantAt(getBlockX(), getBlockZ()));
        }
        return super.checkSpawnRules(level, reason);
    }
}
