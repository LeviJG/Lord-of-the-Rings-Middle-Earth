package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal;

import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRFoodItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMaterialItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRSpawnEggItem;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityCrocodile. Breathes underwater and floats up through it, swims
 * fast at whatever it is chasing, and snaps its jaws (the snap time, for the
 * model's head). It hunts players only in the dark (brightness under 0.5),
 * and now and then, with nothing to chase, goes for a harmless animal within
 * 12 blocks.
 *
 * <p>Drops: rotten flesh (the loot table {@code lotr:entities/crocodile}),
 * and in {@link #dropCustomDeathLoot} zero to two more plus looting, each a
 * bone, a raw fish, leather, raw zebra or a gemsbok hide.
 *
 * <p>NOT ported yet: getCanSpawnHere's water
 * search and the Far Harad swamp's light exemption (D10/D12).
 */
public class LOTRCrocodileEntity extends Monster {

    /** dataWatcher 20. */
    private static final EntityDataAccessor<Integer> DATA_SNAP =
            SynchedEntityData.defineId(LOTRCrocodileEntity.class, EntityDataSerializers.INT);

    public LOTRCrocodileEntity(EntityType<? extends LOTRCrocodileEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.ATTACK_DAMAGE, 4.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new LOTRAttackOnCollideGoal(this, 1.5, false));
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0f));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Mob.class, 12.0f));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        // targetAI: only while it is dark where the crocodile is. getBrightness is
        // the light-dependent value; vanilla's spider reads it the same way.
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 0, true, false, null) {
            @Override
            @SuppressWarnings("deprecation")
            public boolean canUse() {
                return getLightLevelDependentMagicValue() < 0.5f && super.canUse();
            }
        });
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, LOTRNPCEntity.class, 400, true, false, null));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_SNAP, 0);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        LOTRSpawnEggItem.playHatchSound(this, reason);
        return super.finalizeSpawn(level, difficulty, reason, groupData);
    }

    public int getSnapTime() {
        return this.entityData.get(DATA_SNAP);
    }

    public void setSnapTime(int time) {
        this.entityData.set(DATA_SNAP, time);
    }

    @Override
    public boolean doHurtTarget(ServerLevel level, Entity target) {
        boolean hit = super.doHurtTarget(level, target);
        if (hit) {
            setSnapTime(20);
            playSound(LOTRSounds.CROCODILE_SNAP, getSoundVolume(), getVoicePitch());
        }
        return hit;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    /** moveEntityWithHeading: an extra push through the water while it has a target. */
    @Override
    public void travel(Vec3 input) {
        if (!level().isClientSide() && isInWater() && getTarget() != null) {
            moveRelative(0.1f, input);
        }
        super.travel(input);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (level().isClientSide()) {
            return;
        }
        if (isInWater()) {
            setDeltaMovement(getDeltaMovement().add(0.0, 0.02, 0.0));
        }
        LivingEntity target = getTarget();
        if (target != null && (!target.isAlive() || target instanceof Player player && player.isCreative())) {
            setTarget(null);
        }
        int snap = getSnapTime();
        if (snap > 0) {
            setSnapTime(snap - 1);
        }
        if (getTarget() == null && this.random.nextInt(1000) == 0) {
            List<Animal> animals = level().getEntitiesOfClass(Animal.class, getBoundingBox().inflate(12.0, 6.0, 12.0));
            if (!animals.isEmpty()) {
                Animal animal = animals.get(this.random.nextInt(animals.size()));
                if (!animal.getAttributes().hasAttribute(Attributes.ATTACK_DAMAGE)) {
                    setTarget(animal);
                }
            }
        }
    }

    /** dropFewItems' extra drops, beyond the rotten flesh. */
    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean killedByPlayer) {
        super.dropCustomDeathLoot(level, source, killedByPlayer);
        int looting = 0;
        if (source.getEntity() instanceof LivingEntity killer) {
            looting = EnchantmentHelper.getEnchantmentLevel(
                    level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.LOOTING), killer);
        }
        Item[] drops = {Items.BONE, Items.COD, Items.LEATHER, LOTRFoodItems.RAW_ZEBRA, LOTRMaterialItems.GEMSBOK_HIDE};
        int count = this.random.nextInt(3) + this.random.nextInt(looting + 1);
        for (int i = 0; i < count; ++i) {
            spawnAtLocation(level, new ItemStack(drops[this.random.nextInt(drops.length)]));
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return LOTRSounds.CROCODILE_SAY;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return LOTRSounds.CROCODILE_DEATH;
    }
}
