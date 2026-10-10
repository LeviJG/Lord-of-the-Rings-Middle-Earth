package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal;

import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRAttackRules;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCMount;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRSpawnEggItem;

import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.equine.Horse;
import net.minecraft.world.entity.animal.equine.Markings;
import net.minecraft.world.entity.animal.equine.Variant;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.Util;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityHorse: the Middle-earth horse, and the base of every LOTR mount
 * (pony, elk, camel, zebra, giraffe, wild boar, rhino). It is vanilla's horse
 * -- coats, markings, saddle, barding, taming, the inventory -- with:
 *
 * <ul>
 * <li>its own breeding: only with its own kind, only on its breeding item
 *     ({@link #isLotrBreedingItem}; apples for the horse) once tamed, and never
 *     on vanilla's golden foods; the foal's health, jump and speed lie between
 *     its parents' give or take a little ({@link #getChildAttribute}),
 *     clamped per mount ({@link #clampChildHealth} and friends);</li>
 * <li>per-mount adjustments to the random spawn stats
 *     ({@link #onLOTRHorseSpawn});</li>
 * <li>hostile mounts ({@link #isMountHostile}: elk, boar, rhino), which
 *     charge what hurt them instead of panicking once grown, call the herd
 *     when a young one is hurt, and are "enraged" (no interaction) while they
 *     have a target -- following a mob rider's target;</li>
 * <li>swimming up when a player rides it into water;</li>
 * <li>the sprint charge: ridden at a gallop, it strikes whatever it runs into.
 *     This is a later addition in the copy of the original the port follows
 *     (performSprintingActions), kept at the user's word. Its sounds
 *     ("mob.horse.sprint" and so on) never existed, so it is silent.</li>
 * </ul>
 *
 * <p>The mount's speed is measured from its position change, not its
 * velocity: a player's mount moves on the client now, so its velocity on
 * the server is not the gallop the original read.
 *
 * <p>An NPC's mount ({@link LOTRNPCMount}) refuses players, is steered by its
 * rider and walks where its rider would. (isHorseSaddled's trick of reading
 * unsaddled while an NPC's horse moved, so its rider's input was not taken,
 * is 26.2's controlling-passenger rule: only a player rider steers by input.)
 *
 * <p>NOT ported yet (D10): onLOTRHorseSpawn's Rohan and Dor-en-Ernil breed
 * boosts.
 */
public class LOTRHorseEntity extends Horse implements LOTRNPCMount {

    /** dataWatcher 25, 26 and 29. */
    private static final EntityDataAccessor<Boolean> DATA_BELONGS_NPC =
            SynchedEntityData.defineId(LOTRHorseEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_MOUNTABLE =
            SynchedEntityData.defineId(LOTRHorseEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_ENRAGED =
            SynchedEntityData.defineId(LOTRHorseEntity.class, EntityDataSerializers.BOOLEAN);

    private static final float STRENGTH_MULTIPLIER = 9.0f;
    private static final double ATTACK_RANGE = 1.0;
    private static final float KNOCKBACK_MULTIPLIER = 0.03f;
    private static final float SPRINT_THRESHOLD = 0.15f;
    private static final float ATTACK_THRESHOLD = 0.25f;

    private boolean isSprintCharging;

    public LOTRHorseEntity(EntityType<? extends LOTRHorseEntity> type, Level level) {
        super(type, level);
    }

    /** applyEntityAttributes: 20 health, before the spawn randomises it. */
    public static AttributeSupplier.Builder createAttributes() {
        return Horse.createBaseHorseAttributes().add(Attributes.MAX_HEALTH, 20.0);
    }

    /** A hostile mount also registered attackDamage. */
    public static AttributeSupplier.Builder createHostileAttributes(double attackDamage) {
        return createAttributes().add(Attributes.ATTACK_DAMAGE, attackDamage);
    }

    // --- Per-mount hooks ------------------------------------------------

    public boolean isMountHostile() {
        return false;
    }

    /** createMountAttackAI's speed, for hostile mounts. */
    protected double mountAttackSpeed() {
        return 1.0;
    }

    /** isBreedingItem: apples ("apple" in the ore dictionary). */
    public boolean isLotrBreedingItem(ItemStack stack) {
        return stack.is(Items.APPLE);
    }

    public double clampChildHealth(double health) {
        return Mth.clamp(health, 12.0, 48.0);
    }

    public double clampChildJump(double jump) {
        return Mth.clamp(jump, 0.3, 1.0);
    }

    public double clampChildSpeed(double speed) {
        return Mth.clamp(speed, 0.08, 0.45);
    }

    /** onLOTRHorseSpawn: the plain horse's breed boosts are biome-bound (D10). */
    protected void onLOTRHorseSpawn() {
    }

    // --- Goals ------------------------------------------------------------

    @Override
    protected void addBehaviourGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        // The panic task, which a grown hostile mount swapped for its attack.
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.2) {
            @Override
            public boolean canUse() {
                return (!isMountHostile() || isBaby()) && super.canUse();
            }

            @Override
            protected boolean shouldPanic() {
                return !isMobControlled() && super.shouldPanic();
            }
        });
        if (isMountHostile()) {
            this.goalSelector.addGoal(1, new LOTRAttackOnCollideGoal(this, mountAttackSpeed(), true) {
                @Override
                public boolean canUse() {
                    return !isBaby() && super.canUse();
                }
            });
            this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        }
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.25, i -> i.is(ItemTags.HORSE_TEMPT_ITEMS), false));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_BELONGS_NPC, false);
        builder.define(DATA_MOUNTABLE, true);
        builder.define(DATA_ENRAGED, false);
    }

    // --- State ------------------------------------------------------------

    /** saddleMountForWorldGen: a structure's horse, grown, saddled and tame. */
    public void saddleMountForWorldGen() {
        setAge(0);
        setItemSlot(net.minecraft.world.entity.EquipmentSlot.SADDLE, new ItemStack(net.minecraft.world.item.Items.SADDLE));
        setTamed(true);
    }

    @Override
    public boolean isMountSaddled() {
        return isSaddled();
    }

    @Override
    public boolean getBelongsToNPC() {
        return this.entityData.get(DATA_BELONGS_NPC);
    }

    /** setBelongsToNPC: an NPC's mount is tamed, saddled and grown. */
    @Override
    public void setBelongsToNPC(boolean flag) {
        this.entityData.set(DATA_BELONGS_NPC, flag);
        if (flag) {
            setTamed(true);
            if (!isSaddled()) {
                setItemSlot(net.minecraft.world.entity.EquipmentSlot.SADDLE, new ItemStack(Items.SADDLE));
            }
            if (getAge() < 0) {
                setAge(0);
            }
        }
    }

    public boolean getMountable() {
        return this.entityData.get(DATA_MOUNTABLE);
    }

    public void setMountable(boolean flag) {
        this.entityData.set(DATA_MOUNTABLE, flag);
    }

    public boolean isMountEnraged() {
        return this.entityData.get(DATA_ENRAGED);
    }

    public void setMountEnraged(boolean flag) {
        this.entityData.set(DATA_ENRAGED, flag);
    }

    // --- Spawning and breeding -------------------------------------------

    /** onSpawnWithEgg: vanilla's random stats, then the mount's own adjustments. */
    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        LOTRSpawnEggItem.playHatchSound(this, reason);
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        onLOTRHorseSpawn();
        setHealth(getMaxHealth());
        return data;
    }

    /** Only with its own kind (vanilla's would take any horse or donkey). */
    @Override
    public boolean canMate(Animal partner) {
        return partner != this && partner.getType() == getType() && partner instanceof LOTRHorseEntity other
                && canParent() && other.canParent();
    }

    /** createChild. */
    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
        @SuppressWarnings("unchecked")
        EntityType<? extends LOTRHorseEntity> type = (EntityType<? extends LOTRHorseEntity>) getType();
        LOTRHorseEntity child = type.create(level, EntitySpawnReason.BREEDING);
        if (child == null || !(partner instanceof LOTRHorseEntity other)) {
            return child;
        }
        // Vanilla's coat inheritance, as super.createChild gave it.
        int skin = this.random.nextInt(9);
        Variant variant = skin < 4 ? getVariant() : skin < 8 ? other.getVariant()
                : Util.getRandom(Variant.values(), this.random);
        int marking = this.random.nextInt(5);
        Markings markings = marking < 2 ? getMarkings() : marking < 4 ? other.getMarkings()
                : Util.getRandom(Markings.values(), this.random);
        child.setVariantAndMarkings(variant, markings);

        double maxHealth = child.clampChildHealth(getChildAttribute(other, Attributes.MAX_HEALTH, 3.0));
        child.getAttribute(Attributes.MAX_HEALTH).setBaseValue(maxHealth);
        child.setHealth(child.getMaxHealth());
        double jump = child.clampChildJump(getChildAttribute(other, Attributes.JUMP_STRENGTH, 0.1));
        child.getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(jump);
        double speed = child.clampChildSpeed(getChildAttribute(other, Attributes.MOVEMENT_SPEED, 0.03));
        child.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(speed);
        if (isTamed() && other.isTamed()) {
            child.setTamed(true);
        }
        return child;
    }

    /** getChildAttribute: anywhere between the parents' values, widened by the variance. */
    private double getChildAttribute(LOTRHorseEntity other, Holder<Attribute> stat, double variance) {
        double a = getAttribute(stat).getBaseValue();
        double b = other.getAttribute(stat).getBaseValue();
        double lo = Math.min(a, b) - variance;
        double hi = Math.max(a, b) + variance;
        return lo + this.random.nextDouble() * (hi - lo);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return isLotrBreedingItem(stack) || super.isFood(stack);
    }

    /**
     * interact: the breeding item puts a tame, grown mount in love; any other
     * food is vanilla's feeding, but never starts love on its own.
     */
    @Override
    protected boolean handleEating(Player player, ItemStack stack) {
        if (isLotrBreedingItem(stack) && getAge() == 0 && !isInLove() && isTamed()) {
            if (!level().isClientSide()) {
                setInLove(player);
            }
            return true;
        }
        boolean prevInLove = isInLove();
        boolean eaten = super.handleEating(player, stack);
        if (isInLove() && !prevInLove) {
            resetLove();
        }
        return eaten;
    }

    @Override
    public EntityDimensions getDefaultDimensions(Pose pose) {
        EntityDimensions dims = getType().getDimensions();
        return isBaby() ? dims.scale(0.5f) : dims;
    }

    // --- Interaction --------------------------------------------------------

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (!getMountable() || isMountEnraged()) {
            return InteractionResult.PASS;
        }
        if (getBelongsToNPC()) {
            if (!isVehicle()) {
                if (!level().isClientSide()) {
                    player.sendSystemMessage(Component.translatable("chat.lotr.mountOwnedByNPC"));
                }
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        }
        return super.mobInteract(player, hand);
    }

    /** getBlockPathWeight: an NPC's mount walks where its rider would. */
    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader level) {
        if (getBelongsToNPC() && getFirstPassenger() instanceof LOTRNPCEntity rider) {
            return rider.getWalkTargetValue(pos, level);
        }
        return super.getWalkTargetValue(pos, level);
    }

    @Override
    public boolean canBeLeashed() {
        return !getBelongsToNPC() && super.canBeLeashed();
    }

    @Override
    public boolean removeWhenFarAway(double distSqr) {
        return getBelongsToNPC() && !isVehicle();
    }

    /** onDeath: an NPC's mount keeps its saddle and barding. */
    @Override
    protected void dropEquipment(ServerLevel level) {
        if (getBelongsToNPC()) {
            this.inventory.clearContent();
            return;
        }
        super.dropEquipment(level);
    }

    // --- Combat -------------------------------------------------------------

    @Override
    public boolean doHurtTarget(ServerLevel level, Entity target) {
        return target.hurtServer(level, damageSources().mobAttack(this),
                (float) getAttributeValue(Attributes.ATTACK_DAMAGE));
    }

    /** attackEntityFrom: a hurt young hostile mount sets its herd on the attacker. */
    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        boolean hurt = super.hurtServer(level, source, damage);
        if (hurt && isBaby() && isMountHostile() && source.getEntity() instanceof LivingEntity attacker) {
            for (Entity entity : level.getEntities(this, getBoundingBox().inflate(12.0))) {
                if (entity.getClass() == getClass() && entity instanceof LOTRHorseEntity mount
                        && !mount.isBaby() && !mount.isTamed()) {
                    mount.setTarget(attacker);
                }
            }
        }
        return hurt;
    }

    /** This tick's horizontal travel: the gallop, however the mount is being moved. */
    protected float momentum() {
        return (float) Math.sqrt((getX() - this.xo) * (getX() - this.xo) + (getZ() - this.zo) * (getZ() - this.zo));
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!(level() instanceof ServerLevel level)) {
            return;
        }
        Entity rider = getFirstPassenger();
        if (rider instanceof Player && isInWater() && getDeltaMovement().y < 0.0
                && level.noCollision(this, getBoundingBox().expandTowards(0.0, -1.0, 0.0))
                && this.random.nextFloat() < 0.55f) {
            setDeltaMovement(getDeltaMovement().add(0.0, 0.05, 0.0));
            this.needsSync = true;
        }
        if (isMountHostile()) {
            LivingEntity target = getTarget();
            if (target != null && (!target.isAlive() || target instanceof Player player && player.isCreative())) {
                setTarget(null);
            }
            if (rider instanceof Mob mobRider) {
                setTarget(mobRider.getTarget());
            } else if (rider instanceof Player) {
                setTarget(null);
            }
            setMountEnraged(getTarget() != null);
        }
        if (rider instanceof LivingEntity) {
            float momentum = momentum();
            boolean shouldSprint = momentum > SPRINT_THRESHOLD;
            if (shouldSprint != this.isSprintCharging) {
                setSprinting(shouldSprint);
                this.isSprintCharging = shouldSprint;
            }
            if (this.isSprintCharging && momentum >= ATTACK_THRESHOLD) {
                performSprintingActions(level, rider, momentum);
            }
        } else if (this.isSprintCharging) {
            setSprinting(false);
            this.isSprintCharging = false;
        }
    }

    /** performSprintingActions: strike whatever the galloping mount runs into. */
    private void performSprintingActions(ServerLevel level, Entity rider, float momentum) {
        float strength = momentum * STRENGTH_MULTIPLIER;
        Vec3 look = getLookAngle();
        AABB box = getBoundingBox().deflate(1.0)
                .expandTowards(look.x * ATTACK_RANGE, look.y * ATTACK_RANGE, look.z * ATTACK_RANGE)
                .inflate(1.0);
        List<Entity> entities = level.getEntities(this, box);
        for (Entity entity : entities) {
            if (entity instanceof LivingEntity target && entity != rider
                    && LOTRAttackRules.riderCanAttack(rider, target)) {
                boolean hit = target.hurtServer(level, damageSources().mobAttack(this), strength);
                // addVelocity every tick of the charge, as the original -- but a player's
                // client heard of it only on a tick the blow landed (1.7.10 sent a player
                // new motion only when struck), where 26.2 sends every push. So a player
                // is pushed only by a blow that lands, instead of being lifted every tick.
                if (hit || !(target instanceof Player)) {
                    float knockback = strength * KNOCKBACK_MULTIPLIER;
                    float yaw = getYRot() * Mth.DEG_TO_RAD;
                    target.push(-Mth.sin(yaw) * knockback, knockback, Mth.cos(yaw) * knockback);
                }
            }
        }
    }

    // --- Saving -------------------------------------------------------------

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("BelongsNPC", getBelongsToNPC());
        output.putBoolean("Mountable", getMountable());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        setBelongsToNPC(input.getBooleanOr("BelongsNPC", false));
        setMountable(input.getBooleanOr("Mountable", true));
    }
}
