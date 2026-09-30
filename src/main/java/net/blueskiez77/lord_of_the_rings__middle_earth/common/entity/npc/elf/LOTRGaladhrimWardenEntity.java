package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.elf;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRRangedAttackGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityGaladhrimWarden: a watcher of Lothlórien's borders in a hithlain
 * cloak, with a mallorn bow in hand, shooting fast from 24 blocks. When it
 * takes a new target it slips out of sight -- all but invisible, and
 * silent-footed -- until it is hurt, strikes, or has been without a target
 * for a second.
 */
public class LOTRGaladhrimWardenEntity extends LOTRGaladhrimElfEntity {

    private static final EntityDataAccessor<Boolean> DATA_SNEAKING =
            SynchedEntityData.defineId(LOTRGaladhrimWardenEntity.class, EntityDataSerializers.BOOLEAN);

    private int sneakCooldown;
    private @Nullable LivingEntity prevElfTarget;

    public LOTRGaladhrimWardenEntity(EntityType<? extends LOTRGaladhrimWardenEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LOTRElfEntity.createAttributes()
                .add(Attributes.FOLLOW_RANGE, 24.0);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_SNEAKING, false);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, rangedAttackAI());
    }

    @Override
    protected Goal createElfRangedAttackAI() {
        return new LOTRRangedAttackGoal(this, 1.25, 25, 35, 24.0f);
    }

    public boolean isElfSneaking() {
        return this.entityData.get(DATA_SNEAKING);
    }

    public void setElfSneaking(boolean flag) {
        this.entityData.set(DATA_SNEAKING, flag);
        if (flag) {
            this.sneakCooldown = 20;
        }
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        boolean hurt = super.hurtServer(level, source, damage);
        if (hurt && isElfSneaking()) {
            setElfSneaking(false);
        }
        return hurt;
    }

    /** func_145780_a: no footsteps while unseen. */
    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        if (!isElfSneaking()) {
            super.playStepSound(pos, state);
        }
    }

    @Override
    public void setTarget(@Nullable LivingEntity target, boolean speak) {
        super.setTarget(target, speak);
        if (target != null && target != this.prevElfTarget) {
            this.prevElfTarget = target;
            if (!level().isClientSide() && !isElfSneaking()) {
                setElfSneaking(true);
            }
        }
    }

    /** swingItem: striking gives it away. */
    @Override
    public void swing(InteractionHand hand) {
        super.swing(hand);
        if (!level().isClientSide() && isElfSneaking()) {
            setElfSneaking(false);
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!level().isClientSide()) {
            if (isElfSneaking()) {
                if (getTarget() == null) {
                    if (this.sneakCooldown > 0) {
                        --this.sneakCooldown;
                    } else {
                        setElfSneaking(false);
                    }
                } else {
                    this.sneakCooldown = 20;
                }
            } else {
                this.sneakCooldown = 0;
            }
        }
    }

    @Override
    public float getAlignmentBonus() {
        return 2.0f;
    }

    @Override
    public @Nullable String getSpeechBank(Player player) {
        if (isFriendly(player)) {
            return this.hiredNPCInfo.getHiringPlayer() == player ? "galadhrim/elf/hired" : "galadhrim/warrior/friendly";
        }
        return "galadhrim/warrior/hostile";
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.GALADHRIM_DAGGER));
        this.npcItemsInv.setRangedWeapon(new ItemStack(LOTRCombatItems.MALLORN_BOW));
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getRangedWeapon().copy());
        setItemSlot(EquipmentSlot.FEET, new ItemStack(LOTRCombatItems.GALADHRIM_CLOAK_BOOTS));
        setItemSlot(EquipmentSlot.LEGS, new ItemStack(LOTRCombatItems.GALADHRIM_CLOAK_LEGGINGS));
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(LOTRCombatItems.GALADHRIM_CLOAK_TUNIC));
        if (this.random.nextInt(10) != 0) {
            setItemSlot(EquipmentSlot.HEAD, new ItemStack(LOTRCombatItems.GALADHRIM_CLOAK_HOOD));
        }
        return data;
    }
}
