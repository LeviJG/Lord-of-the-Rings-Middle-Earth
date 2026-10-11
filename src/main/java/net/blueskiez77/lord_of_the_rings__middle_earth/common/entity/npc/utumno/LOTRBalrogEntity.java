package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.utumno;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRBalrogChargeGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRFollowHiringPlayerGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCAttributes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMaterialItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRUtumno;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityBalrog: a Balrog of Utumno -- 300 strong and healing, twice a man's height and more,
 * striking for 10 with a whip, sword, battleaxe or warhammer (or its bare fiery hands, which set
 * alight), and hurling what it strikes. Out of water it is wreathed in flame and smoke, now and then
 * setting fires about it (in Utumno burning through all that is no harder than obsidian). It charges
 * from afar (LOTRBalrogChargeGoal). Unharmed by fire or falling, hardly shoved; at its death the fires
 * about it die too. It leaves coal and the Flame of Udûn.
 */
public class LOTRBalrogEntity extends LOTRNPCEntity {

    private static final EntityDataAccessor<Boolean> DATA_CHARGING =
            SynchedEntityData.defineId(LOTRBalrogEntity.class, EntityDataSerializers.BOOLEAN);

    public int chargeLean;
    public int prevChargeLean;
    public int chargeFrustration;

    public LOTRBalrogEntity(EntityType<? extends LOTRBalrogEntity> type, Level level) {
        super(type, level);
        setPathfindingMalus(PathType.WATER, -1.0f);
        this.spawnsInDarkness = true;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createNPCAttributes()
                .add(Attributes.MAX_HEALTH, 300.0)
                .add(Attributes.FOLLOW_RANGE, 40.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(LOTRNPCAttributes.NPC_ATTACK_DAMAGE, 10.0)
                .add(LOTRNPCAttributes.BALROG_CHARGE_DAMAGE, 15.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new LOTRBalrogChargeGoal(this, 3.0, 20.0f, 200));
        this.goalSelector.addGoal(2, new LOTRAttackOnCollideGoal(this, 1.6, false));
        this.goalSelector.addGoal(3, new LOTRFollowHiringPlayerGoal(this));
        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 24.0f, 0.02f));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, LOTRNPCEntity.class, 16.0f, 0.02f));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Mob.class, 12.0f, 0.02f));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        addTargetTasks(true);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_CHARGING, false);
    }

    public boolean isBalrogCharging() {
        return this.entityData.get(DATA_CHARGING);
    }

    public void setBalrogCharging(boolean flag) {
        this.entityData.set(DATA_CHARGING, flag);
    }

    public float getInterpolatedChargeLean(float f) {
        return (this.prevChargeLean + (this.chargeLean - this.prevChargeLean) * f) / 10.0f;
    }

    /** isWreathedInFlame: alive and dry. */
    public boolean isWreathedInFlame() {
        return isAlive() && !isInWaterOrRain();
    }

    @Override
    public boolean doHurtTarget(ServerLevel level, Entity target) {
        if (super.doHurtTarget(level, target)) {
            LivingEntity curTarget = getTarget();
            if (curTarget != null && target == curTarget) {
                this.chargeFrustration = 0;
            }
            if (getMainHandItem().isEmpty()) {
                target.igniteForSeconds(5.0f);
            }
            float attackDamage = (float) getAttributeValue(LOTRNPCAttributes.NPC_ATTACK_DAMAGE);
            float knockbackModifier = 0.25f * attackDamage;
            float yaw = getYRot() * Mth.DEG_TO_RAD;
            target.push(-Mth.sin(yaw) * knockbackModifier * 0.5f, knockbackModifier * 0.1, Mth.cos(yaw) * knockbackModifier * 0.5f);
            return true;
        }
        return false;
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        if (source.is(DamageTypes.FALL)) {
            return false;
        }
        if (super.hurtServer(level, source, amount)) {
            LivingEntity curTarget = getTarget();
            if (curTarget != null && source.getEntity() == curTarget && source.getDirectEntity() == curTarget) {
                this.chargeFrustration = 0;
            }
            return true;
        }
        return false;
    }

    /** knockBack: a quarter as far as anything else. */
    @Override
    public void knockback(double strength, double x, double z, DamageSource source, float damage, boolean comesFromEffect) {
        super.knockback(strength, x, z, source, damage, comesFromEffect);
        setDeltaMovement(getDeltaMovement().scale(0.25));
    }

    @Override
    public void onAttackModeChange(AttackMode mode, boolean mounted) {
        setItemSlot(EquipmentSlot.MAINHAND, mode == AttackMode.IDLE ? this.npcItemsInv.getIdleItem() : this.npcItemsInv.getMeleeWeapon());
    }

    /** dropNPCEquipment: to a player, one time in three its weapon, whole. */
    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean killedByPlayer) {
        ItemStack heldItem = getMainHandItem();
        if (killedByPlayer && this.random.nextInt(3) == 0 && !heldItem.isEmpty()) {
            spawnAtLocation(level, heldItem.copy());
            setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        }
        super.dropCustomDeathLoot(level, source, killedByPlayer);
    }

    /** dropFewItems: 4 to 16 coal (more with looting), and the Flame of Udûn (more with looting). */
    @Override
    protected void dropNPCItems(ServerLevel level, boolean killedByPlayer, int looting) {
        int coals = LOTRWorldGenUtil.getRandomIntegerInRange(this.random, 4, 16 + looting * 8);
        for (int l = 0; l < coals; ++l) {
            spawnAtLocation(level, new ItemStack(Items.COAL));
        }
        int fires = 1;
        if (looting > 0) {
            float x = LOTRWorldGenUtil.randomFloatClamp(this.random, 0.0f, looting * 0.667f);
            while (x > 1.0f) {
                x -= 1.0f;
                ++fires;
            }
            if (this.random.nextFloat() < x) {
                ++fires;
            }
        }
        for (int l = 0; l < fires; ++l) {
            spawnAtLocation(level, new ItemStack(LOTRMaterialItems.FLAME_OF_UDUN));
        }
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        playSound(LOTRSounds.TROLL_STEP, 0.75f, getVoicePitch());
    }

    @Override
    protected int getBaseExperienceReward(ServerLevel level) {
        return 15 + this.random.nextInt(10);
    }

    @Override
    public LOTRFaction getFaction() {
        return LOTRFaction.UTUMNO;
    }

    @Override
    public LOTRAchievement getKillAchievement() {
        return LOTRAchievement.KILL_BALROG;
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return LOTRSounds.TROLL_SAY;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return LOTRSounds.TROLL_SAY;
    }

    @Override
    protected float getSoundVolume() {
        return 1.5f;
    }

    /** onDeath, on the client: a burst of flame and smoke. */
    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            for (int l = 0; l < 100; ++l) {
                double d = getX() + getBbWidth() * LOTRWorldGenUtil.randomFloatClamp(this.random, -1.0f, 1.0f);
                double d1 = getY() + getBbHeight() * LOTRWorldGenUtil.randomFloatClamp(this.random, 0.0f, 1.0f);
                double d2 = getZ() + getBbWidth() * LOTRWorldGenUtil.randomFloatClamp(this.random, -1.0f, 1.0f);
                level().addParticle(this.random.nextInt(3) == 0 ? ParticleTypes.EXPLOSION : ParticleTypes.FLAME, d, d1, d2,
                        this.random.nextGaussian() * 0.03, this.random.nextGaussian() * 0.03, this.random.nextGaussian() * 0.03);
            }
        }
        super.handleEntityEvent(id);
    }

    /** onDeath: every fire within eight goes out, and it gives a last great cry. */
    @Override
    public void die(DamageSource source) {
        if (level() instanceof ServerLevel level) {
            int exRange = 8;
            int i = Mth.floor(getX());
            int j = Mth.floor(getY());
            int k = Mth.floor(getZ());
            for (BlockPos pos : BlockPos.betweenClosed(i - exRange, j - exRange, k - exRange, i + exRange, j + exRange, k + exRange)) {
                if (level.getBlockState(pos).is(BlockTags.FIRE)) {
                    level.removeBlock(pos, false);
                }
            }
        }
        super.die(source);
        playSound(LOTRSounds.TROLL_SAY, getSoundVolume() * 2.0f, getVoicePitch() * 0.75f);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (getHealth() < getMaxHealth() && this.tickCount % 10 == 0) {
            heal(1.0f);
        }
        if (!level().isClientSide()) {
            this.chargeFrustration = getTarget() == null ? 0 : isBalrogCharging() ? 0 : this.chargeFrustration + 1;
        }
        this.prevChargeLean = this.chargeLean;
        if (isBalrogCharging()) {
            if (this.chargeLean < 10) {
                ++this.chargeLean;
            }
        } else if (this.chargeLean > 0) {
            --this.chargeLean;
        }
        if (!isWreathedInFlame()) {
            return;
        }
        if (level() instanceof ServerLevel level && this.random.nextInt(80) == 0) {
            boolean isUtumno = LOTRUtumno.isUtumno(level);
            float maxResistance = Blocks.OBSIDIAN.getExplosionResistance();
            for (int l = 0; l < 24; ++l) {
                BlockPos pos = new BlockPos(Mth.floor(getX()) + LOTRWorldGenUtil.getRandomIntegerInRange(this.random, -8, 8),
                        Mth.floor(getBoundingBox().minY) + LOTRWorldGenUtil.getRandomIntegerInRange(this.random, -4, 8),
                        Mth.floor(getZ()) + LOTRWorldGenUtil.getRandomIntegerInRange(this.random, -8, 8));
                BlockState block = level.getBlockState(pos);
                BlockState fire = BaseFireBlock.getState(level, pos);
                if (!block.canBeReplaced() && (!isUtumno || block.getBlock().getExplosionResistance() > maxResistance)
                        || !fire.canSurvive(level, pos)) {
                    continue;
                }
                level.setBlock(pos, fire, Block.UPDATE_ALL);
            }
        }
        if (level().isClientSide()) {
            if (isBalrogCharging()) {
                Vec3 motion = getDeltaMovement();
                for (int l = 0; l < 10; ++l) {
                    double d0 = getX() + (this.random.nextDouble() - 0.5) * getBbWidth() * 1.5;
                    double d1 = getY() + getBbHeight() * (0.5 + this.random.nextDouble());
                    double d2 = getZ() + (this.random.nextDouble() - 0.5) * getBbWidth() * 1.5;
                    level().addParticle(this.random.nextInt(3) == 0 ? ParticleTypes.FLAME : ParticleTypes.LARGE_SMOKE, d0, d1, d2,
                            motion.x * -2.0, motion.y * -0.5, motion.z * -2.0);
                }
            } else {
                for (int l = 0; l < 3; ++l) {
                    double d0 = getX() + (this.random.nextDouble() - 0.5) * getBbWidth() * 2.0;
                    double d1 = getY() + 0.5 + this.random.nextDouble() * getBbHeight() * 1.5;
                    double d2 = getZ() + (this.random.nextDouble() - 0.5) * getBbWidth() * 2.0;
                    level().addParticle(this.random.nextInt(3) == 0 ? ParticleTypes.FLAME : ParticleTypes.LARGE_SMOKE, d0, d1, d2, 0.0, 0.0, 0.0);
                }
            }
        }
    }

    /** onSpawnWithEgg: half the time a whip, else an Utumno sword, battleaxe or warhammer. */
    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        Item weapon;
        if (this.random.nextBoolean()) {
            weapon = LOTRCombatItems.BALROG_WHIP;
        } else {
            weapon = switch (this.random.nextInt(3)) {
                case 0 -> LOTRCombatItems.UTUMNO_SWORD;
                case 1 -> LOTRCombatItems.UTUMNO_BATTLEAXE;
                default -> LOTRCombatItems.UTUMNO_WARHAMMER;
            };
        }
        this.npcItemsInv.setMeleeWeapon(new ItemStack(weapon));
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon().copy());
        return data;
    }
}
