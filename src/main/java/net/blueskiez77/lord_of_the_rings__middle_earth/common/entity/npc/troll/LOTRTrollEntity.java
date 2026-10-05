package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.troll;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRStoneTrollEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRFollowHiringPlayerGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRHiredRemainStillGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRTrollFleeSunGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRTrollTargetGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCAttributes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNames;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRSpeech;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRPlayerAlignments;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRFoodItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMaterialItems;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.RestrictSunGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityTroll (the Troll): a troll of Angmar -- 60 strong, armour 8, hitting
 * for 5 and flinging what it hits, hard to knock back, heavy of tread and
 * loud. It keeps out of the sun, and one caught under the open sky by day
 * burns for 300 ticks as it runs for shelter, then turns to stone
 * ({@link LOTRStoneTrollEntity}), in the outfit it wore. It seeks out Angmar's
 * enemies but lets be those at +100, and at 0 or better attacks only now and
 * then (LOTRTrollTargetGoal). One in ten has two heads: half again the
 * health, three more attack, four tenths quicker. Tickled with a feather by a
 * friend at +100, it sniffs, and after three sniffs (on average six feathers)
 * it sneezes out slime. It leaves troll bones and, now and then, slime and
 * whatever it has lately eaten.
 *
 * <p>NOT ported yet: the biomes where hostiles walk by day, which the sun
 * does not trouble (LOTRBiome.canSpawnHostilesInDay, D10), the killTroll,
 * killTrollFleeingSun and makeTrollSneeze achievements (D7), and the hired
 * unit's icon and health bar (with the hire screens).
 */
public class LOTRTrollEntity extends LOTRNPCEntity {

    private static final EntityDataAccessor<Byte> DATA_OUTFIT =
            SynchedEntityData.defineId(LOTRTrollEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Integer> DATA_BURN_TIME =
            SynchedEntityData.defineId(LOTRTrollEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Byte> DATA_SNEEZING =
            SynchedEntityData.defineId(LOTRTrollEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> DATA_TWO_HEADS =
            SynchedEntityData.defineId(LOTRTrollEntity.class, EntityDataSerializers.BOOLEAN);

    /** handleHealthUpdate 15: the poof of turning to stone; 16: a sniff. */
    private static final byte EVENT_STONE = 15;
    private static final byte EVENT_SNIFF = 16;

    public int sneeze;
    public int sniffTime;
    public boolean trollImmuneToSun;

    public LOTRTrollEntity(EntityType<? extends LOTRTrollEntity> type, Level level) {
        super(type, level);
        setPathfindingMalus(PathType.WATER, -1.0f);
        this.spawnsInDarkness = true;
        setTrollOutfit(this.random.nextInt(3));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createNPCAttributes()
                .add(Attributes.MAX_HEALTH, 60.0)
                .add(Attributes.FOLLOW_RANGE, 24.0)
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.ARMOR, 8.0)
                .add(LOTRNPCAttributes.NPC_ATTACK_DAMAGE, 5.0);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_OUTFIT, (byte) 0);
        builder.define(DATA_BURN_TIME, -1);
        builder.define(DATA_SNEEZING, (byte) 0);
        builder.define(DATA_TWO_HEADS, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new RestrictSunGoal(this));
        this.goalSelector.addGoal(2, new LOTRHiredRemainStillGoal(this));
        this.goalSelector.addGoal(3, new LOTRTrollFleeSunGoal(this, 2.5));
        this.goalSelector.addGoal(4, getTrollAttackAI());
        this.goalSelector.addGoal(5, new LOTRFollowHiringPlayerGoal(this));
        this.goalSelector.addGoal(6, new RandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 12.0f, 0.02f));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, LOTRNPCEntity.class, 8.0f, 0.02f));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Mob.class, 12.0f, 0.01f));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        addTargetTasks(true, LOTRTrollTargetGoal::new);
    }

    protected Goal getTrollAttackAI() {
        return new LOTRAttackOnCollideGoal(this, 1.4, false);
    }

    /** getTrollScale: the size of this kind of troll, hitbox and model alike. */
    public float getTrollScale() {
        return 1.0f;
    }

    public boolean hasTrollName() {
        return true;
    }

    // --- Synced state -------------------------------------------------------------

    public int getTrollOutfit() {
        return this.entityData.get(DATA_OUTFIT);
    }

    public void setTrollOutfit(int outfit) {
        this.entityData.set(DATA_OUTFIT, (byte) outfit);
    }

    public int getTrollBurnTime() {
        return this.entityData.get(DATA_BURN_TIME);
    }

    public void setTrollBurnTime(int time) {
        this.entityData.set(DATA_BURN_TIME, time);
    }

    public int getSneezingTime() {
        return this.entityData.get(DATA_SNEEZING);
    }

    public void setSneezingTime(int time) {
        this.entityData.set(DATA_SNEEZING, (byte) time);
    }

    public boolean hasTwoHeads() {
        return this.entityData.get(DATA_TWO_HEADS);
    }

    public void setHasTwoHeads(boolean flag) {
        this.entityData.set(DATA_TWO_HEADS, flag);
    }

    /**
     * new EntityArrow(world, this, target, speed, inaccuracy), whose place and
     * heading the mountain and snow trolls copied onto what they throw: a
     * block toward the target from just under the thrower's eyes, aimed at a
     * third of the target's height, lofted by a fifth of the distance.
     */
    protected void aimLikeArrow(Projectile projectile, LivingEntity target, float speed, float inaccuracy) {
        double y = getEyeY() - 0.1;
        double dx = target.getX() - getX();
        double dy = target.getBoundingBox().minY + target.getBbHeight() / 3.0f - y;
        double dz = target.getZ() - getZ();
        double horizontal = Math.sqrt(dx * dx + dz * dz);
        if (horizontal >= 1.0E-7) {
            projectile.snapTo(getX() + dx / horizontal, y, getZ() + dz / horizontal,
                    (float) (Mth.atan2(dz, dx) * Mth.RAD_TO_DEG) - 90.0f,
                    (float) -(Mth.atan2(dy, horizontal) * Mth.RAD_TO_DEG));
            projectile.shoot(dx, dy + horizontal * 0.2, dz, speed, inaccuracy);
        }
    }

    /** A mountain or snow troll winding up to throw (LOTRModelTroll's throwing pose). */
    public boolean isThrowing() {
        return false;
    }

    /** shouldRenderHeadHurt: while hurt, or sneezing. */
    public boolean shouldRenderHeadHurt() {
        return this.hurtTime > 0 || getSneezingTime() > 0;
    }

    // --- Combat -------------------------------------------------------------------

    /** attackEntityAsMob: and the one hit is thrown back a quarter of the damage's worth. */
    @Override
    public boolean doHurtTarget(ServerLevel level, Entity target) {
        if (super.doHurtTarget(level, target)) {
            float attackDamage = (float) getAttributeValue(LOTRNPCAttributes.NPC_ATTACK_DAMAGE);
            float knockback = 0.25f * attackDamage;
            float yaw = getYRot() * Mth.DEG_TO_RAD;
            target.push(-Mth.sin(yaw) * knockback * 0.5f, knockback * 0.1, Mth.cos(yaw) * knockback * 0.5f);
            return true;
        }
        return false;
    }

    /** knockBack: halved. */
    @Override
    public void knockback(double strength, double x, double z, DamageSource source, float damage,
                          boolean comesFromEffect) {
        super.knockback(strength, x, z, source, damage, comesFromEffect);
        setDeltaMovement(getDeltaMovement().scale(0.5));
    }

    // --- Tickling -----------------------------------------------------------------

    public boolean canTrollBeTickled(Player player) {
        return canNPCTalk() && isFriendly(player) && LOTRPlayerAlignments.getAlignment(player, getFaction()) >= 100.0f
                && getTarget() == null && getTrollBurnTime() == -1;
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level().isClientSide() && canTrollBeTickled(player) && stack.is(Items.FEATHER) && getSneezingTime() == 0) {
            if (this.random.nextBoolean()) {
                ++this.sneeze;
            }
            stack.consume(1, player);
            this.npcTalkTick = getNPCTalkInterval() / 2;
            if (this.sneeze >= 3) {
                setSneezingTime(16);
            } else {
                LOTRSpeech.sendSpeech(player, this, LOTRSpeech.getRandomSpeechForPlayer(this, "troll/tickle", player, null, null));
                playSound(LOTRSounds.TROLL_SNIFF, getSoundVolume(), getVoicePitch());
                level().broadcastEntityEvent(this, EVENT_SNIFF);
            }
        }
        return super.mobInteract(player, hand);
    }

    // --- Living -------------------------------------------------------------------

    @Override
    public void aiStep() {
        super.aiStep();
        if (getTrollBurnTime() >= 0 && isAlive()) {
            if (level().isClientSide()) {
                level().addParticle(ParticleTypes.LARGE_SMOKE, getX() + (this.random.nextDouble() - 0.5) * getBbWidth(),
                        getY() + this.random.nextDouble() * getBbHeight(),
                        getZ() + (this.random.nextDouble() - 0.5) * getBbWidth(), 0.0, 0.0, 0.0);
            } else {
                BlockPos pos = BlockPos.containing(getX(), getBoundingBox().minY, getZ());
                if (this.trollImmuneToSun || !level().isBrightOutside() || !level().canSeeSky(pos)) {
                    setTrollBurnTime(-1);
                } else {
                    setTrollBurnTime(getTrollBurnTime() - 1);
                    if (getTrollBurnTime() == 0) {
                        onTrollDeathBySun();
                        Player hirer = this.hiredNPCInfo.getHiringPlayer();
                        if (this.hiredNPCInfo.isActive && hirer != null) {
                            hirer.sendSystemMessage(Component.translatable("lotr.hiredNPC.trollStone", getName()));
                        }
                    }
                }
            }
        }
        if (this.sniffTime > 0) {
            --this.sniffTime;
        }
        if (!level().isClientSide() && getSneezingTime() > 0) {
            setSneezingTime(getSneezingTime() - 1);
            if (getSneezingTime() == 8) {
                playSound(LOTRSounds.TROLL_SNEEZE, getSoundVolume() * 1.5f, getVoicePitch());
            }
            if (getSneezingTime() == 4) {
                int slimes = 2 + this.random.nextInt(3);
                for (int i = 0; i < slimes; ++i) {
                    ItemEntity item = new ItemEntity(level(), getX(), getY() + getEyeHeight(), getZ(),
                            new ItemStack(Items.SLIME_BALL));
                    item.setPickUpDelay(40);
                    float yaw = this.yHeadRot * Mth.DEG_TO_RAD;
                    float pitch = getXRot() * Mth.DEG_TO_RAD;
                    double mx = -Mth.sin(yaw) * Mth.cos(pitch);
                    double mz = Mth.cos(yaw) * Mth.cos(pitch);
                    double my = -Mth.sin(pitch) + 0.1f;
                    float spread = 0.02f * this.random.nextFloat();
                    float angle = this.random.nextFloat() * Mth.PI * 2.0f;
                    mx += Math.cos(angle) * spread;
                    my += (this.random.nextFloat() - this.random.nextFloat()) * 0.1f;
                    mz += Math.sin(angle) * spread;
                    item.setDeltaMovement(mx, my, mz);
                    level().addFreshEntity(item);
                }
            }
            if (getSneezingTime() == 0) {
                this.sneeze = 0;
            }
        }
    }

    /** onTrollDeathBySun: a poof, and a stone troll stands where it was. */
    public void onTrollDeathBySun() {
        playSound(LOTRSounds.TROLL_TRANSFORM, getSoundVolume(), getVoicePitch());
        level().broadcastEntityEvent(this, EVENT_STONE);
        discard();
        LOTRStoneTrollEntity stone = new LOTRStoneTrollEntity(LOTREntities.STONE_TROLL, level());
        stone.snapTo(getX(), getY(), getZ(), getYRot(), 0.0f);
        stone.setTrollOutfit(getTrollOutfit());
        stone.setHasTwoHeads(hasTwoHeads());
        level().addFreshEntity(stone);
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == EVENT_STONE) {
            makePoofParticles();
        } else if (id == EVENT_SNIFF) {
            this.sniffTime = 16;
        } else {
            super.handleEntityEvent(id);
        }
    }

    // --- Its kind ------------------------------------------------------------------

    @Override
    public LOTRFaction getFaction() {
        return LOTRFaction.ANGMAR;
    }

    @Override
    public float getAlignmentBonus() {
        return 3.0f;
    }

    @Override
    public void setupNPCName() {
        this.familyInfo.setName(LOTRNames.getTrollName(this.random));
    }

    @Override
    public String getNPCName() {
        return hasTrollName() ? this.familyInfo.getName() : super.getNPCName();
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        if (getTrollBurnTime() >= 0) {
            return null;
        }
        if (LOTRPlayerAlignments.getAlignment(player, getFaction()) >= 100.0f && isFriendly(player)) {
            return this.hiredNPCInfo.getHiringPlayer() == player ? "troll/hired" : "troll/friendly";
        }
        return "troll/hostile";
    }

    @Override
    protected float getSoundVolume() {
        return 1.5f;
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
    protected SoundEvent getDeathSound() {
        return LOTRSounds.TROLL_SAY;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        playSound(LOTRSounds.TROLL_STEP, 0.75f, getVoicePitch());
    }

    @Override
    protected int getBaseExperienceReward(ServerLevel level) {
        return 4 + this.random.nextInt(5);
    }

    // --- Drops --------------------------------------------------------------------

    @Override
    protected void dropNPCItems(ServerLevel level, boolean killedByPlayer, int looting) {
        super.dropNPCItems(level, killedByPlayer, looting);
        int bones = 2 + this.random.nextInt(3) + this.random.nextInt(looting + 1);
        for (int l = 0; l < bones; ++l) {
            spawnAtLocation(level, LOTRMaterialItems.TROLL_BONE);
        }
        dropTrollItems(level, killedByPlayer, looting);
    }

    /** Now and then slime; and whatever it last ate. */
    protected void dropTrollItems(ServerLevel level, boolean killedByPlayer, int looting) {
        if (this.random.nextInt(3) == 0) {
            int j = 1 + this.random.nextInt(3) + this.random.nextInt(looting + 1);
            for (int k = 0; k < j; ++k) {
                spawnAtLocation(level, Items.SLIME_BALL);
            }
        }
        int animalDrops = 1 + this.random.nextInt(3) + this.random.nextInt(looting + 1);
        for (int l = 0; l < animalDrops; ++l) {
            ItemStack drop = switch (this.random.nextInt(10)) {
                case 0 -> new ItemStack(Items.LEATHER, 1 + this.random.nextInt(3));
                case 1 -> new ItemStack(Items.BEEF, 1 + this.random.nextInt(2));
                case 2 -> new ItemStack(Items.CHICKEN, 1 + this.random.nextInt(2));
                case 3 -> new ItemStack(Items.FEATHER, 1 + this.random.nextInt(3));
                case 4 -> new ItemStack(Items.PORKCHOP, 1 + this.random.nextInt(2));
                case 5 -> new ItemStack(Items.WOOL.white(), 1 + this.random.nextInt(3));
                case 6 -> new ItemStack(Items.ROTTEN_FLESH, 1 + this.random.nextInt(3));
                case 7 -> new ItemStack(Items.RABBIT, 1 + this.random.nextInt(2));
                case 8 -> new ItemStack(LOTRFoodItems.RAW_MUTTON, 1 + this.random.nextInt(2));
                default -> new ItemStack(LOTRFoodItems.RAW_VENISON, 1 + this.random.nextInt(2));
            };
            spawnAtLocation(level, drop, 0.0f);
        }
    }

    // --- Spawning and saving ----------------------------------------------------------

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        if (this.random.nextInt(10) == 0) {
            setHasTwoHeads(true);
            AttributeInstance health = getAttribute(Attributes.MAX_HEALTH);
            health.setBaseValue(health.getBaseValue() * 1.5);
            setHealth(getMaxHealth());
            AttributeInstance attack = getAttribute(LOTRNPCAttributes.NPC_ATTACK_DAMAGE);
            attack.setBaseValue(attack.getBaseValue() + 3.0);
            AttributeInstance speed = getAttribute(Attributes.MOVEMENT_SPEED);
            speed.setBaseValue(speed.getBaseValue() * 1.4);
        }
        return data;
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putByte("TrollOutfit", (byte) getTrollOutfit());
        output.putInt("TrollBurnTime", getTrollBurnTime());
        output.putInt("Sneeze", this.sneeze);
        output.putInt("SneezeTime", getSneezingTime());
        output.putBoolean("ImmuneToSun", this.trollImmuneToSun);
        output.putBoolean("TwoHeads", hasTwoHeads());
    }

    /** The name was once kept as "TrollName". */
    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        setTrollOutfit(input.getByteOr("TrollOutfit", (byte) 0));
        setTrollBurnTime(input.getIntOr("TrollBurnTime", 0));
        this.sneeze = input.getIntOr("Sneeze", 0);
        setSneezingTime(input.getIntOr("SneezeTime", 0));
        this.trollImmuneToSun = input.getBooleanOr("ImmuneToSun", false);
        setHasTwoHeads(input.getBooleanOr("TwoHeads", false));
        input.getString("TrollName").ifPresent(this.familyInfo::setName);
    }

    /** canReEquipHired: its player cannot dress it. */
    @Override
    public boolean canReEquipHired(int slot, ItemStack stack) {
        return false;
    }
}
