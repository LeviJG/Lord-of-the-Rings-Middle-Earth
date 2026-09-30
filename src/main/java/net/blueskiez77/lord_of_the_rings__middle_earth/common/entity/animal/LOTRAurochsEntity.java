package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRSpawnEggItem;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
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
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.cow.AbstractCow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityAurochs: a wild cow that fights back. It is still a cow -- it
 * breeds on wheat and can be milked -- but an adult charges whatever hurt it,
 * and a calf that is hurt sets every adult of its kind within 12 blocks on the
 * attacker. A calf panics instead of fighting: the original swapped the panic
 * task for the attack task, at the same priority, as the calf grew up, which
 * {@link #registerGoals} expresses as two goals gated on age.
 *
 * <p>While it has a target it is enraged: the head lowers (the model) and it
 * cannot be interacted with. Drops are {@code lotr:entities/aurochs}: two to
 * four leather and beef each, plus looting, and a horn.
 *
 * <p>The cow's own tasks are modern vanilla's (AbstractCow), as they were
 * 1.7.10 vanilla's in the original.
 */
public class LOTRAurochsEntity extends AbstractCow {

    /** dataWatcher 20. */
    private static final EntityDataAccessor<Boolean> DATA_ENRAGED =
            SynchedEntityData.defineId(LOTRAurochsEntity.class, EntityDataSerializers.BOOLEAN);

    public LOTRAurochsEntity(EntityType<? extends LOTRAurochsEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 30.0)
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.ATTACK_DAMAGE, 4.0);
    }

    /** createAurochsAttackAI's speed. */
    protected double attackSpeed() {
        return 1.7;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 2.0) {
            @Override
            public boolean canUse() {
                return isBaby() && super.canUse();
            }
        });
        this.goalSelector.addGoal(1, new LOTRAttackOnCollideGoal(this, attackSpeed(), true) {
            @Override
            public boolean canUse() {
                return !isBaby() && super.canUse();
            }
        });
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.25, stack -> stack.is(ItemTags.COW_FOOD), false));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0f));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ENRAGED, false);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        LOTRSpawnEggItem.playHatchSound(this, reason);
        return super.finalizeSpawn(level, difficulty, reason, groupData);
    }

    public boolean isAurochsEnraged() {
        return this.entityData.get(DATA_ENRAGED);
    }

    public void setAurochsEnraged(boolean enraged) {
        this.entityData.set(DATA_ENRAGED, enraged);
    }

    /** attackEntityAsMob: the attack damage, and a shove of 0.375 along its facing. */
    @Override
    public boolean doHurtTarget(ServerLevel level, Entity target) {
        float damage = (float) getAttributeValue(Attributes.ATTACK_DAMAGE);
        boolean hit = target.hurtServer(level, damageSources().mobAttack(this), damage);
        if (hit) {
            float kb = 0.75f;
            float yaw = getYRot() * Mth.DEG_TO_RAD;
            target.push(-Mth.sin(yaw) * kb * 0.5f, 0.0, Mth.cos(yaw) * kb * 0.5f);
        }
        return hit;
    }

    /** attackEntityFrom: a hurt calf calls the adults of its kind on the attacker. */
    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        boolean hurt = super.hurtServer(level, source, damage);
        if (hurt && isBaby() && source.getEntity() instanceof LivingEntity attacker) {
            for (Entity entity : level.getEntities(this, getBoundingBox().inflate(12.0))) {
                if (entity.getClass() == getClass() && !((LOTRAurochsEntity) entity).isBaby()) {
                    ((LOTRAurochsEntity) entity).setTarget(attacker);
                }
            }
        }
        return hurt;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (level() instanceof ServerLevel) {
            LivingEntity target = getTarget();
            if (target != null && (!target.isAlive() || target instanceof Player player && player.isCreative())) {
                setTarget(null);
            }
            Entity rider = getFirstPassenger();
            if (rider instanceof Mob mobRider) {
                setTarget(mobRider.getTarget());
            } else if (rider instanceof Player) {
                setTarget(null);
            }
            setAurochsEnraged(getTarget() != null);
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (isAurochsEnraged()) {
            return InteractionResult.PASS;
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public @Nullable AbstractCow getBreedOffspring(ServerLevel level, AgeableMob partner) {
        return LOTREntities.AUROCHS.create(level, EntitySpawnReason.BREEDING);
    }

    /** AbstractCow sizes a calf from the vanilla cow; this sizes it from its own type. */
    @Override
    public EntityDimensions getDefaultDimensions(Pose pose) {
        return getType().getDimensions();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return LOTRSounds.AUROCHS_SAY;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return LOTRSounds.AUROCHS_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return LOTRSounds.AUROCHS_HURT;
    }

    @Override
    public float getVoicePitch() {
        return super.getVoicePitch() * 0.75f;
    }

    @Override
    protected float getSoundVolume() {
        return 1.0f;
    }

    @Override
    public int getAmbientSoundInterval() {
        return 200;
    }
}
