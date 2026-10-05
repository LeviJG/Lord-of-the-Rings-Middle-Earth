package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.ranger;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRRangedAttackGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCAttributes;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityRanger: a ranger -- always a man, 25 strong and quick, seeing 24
 * blocks, in his people's hood, tunic, leggings and boots, with a dagger and a
 * bow, shooting from 20 blocks. On taking a new target he goes into hiding:
 * silent-footed and all but invisible, until a second after he has no target,
 * until he is struck or strikes, or while he rides. He leaves arrows.
 *
 * <p>NOT ported yet: the ranger's cape (with the NPC capes).
 */
public abstract class LOTRRangerEntity extends LOTRDunedainEntity {

    private static final EntityDataAccessor<Boolean> DATA_SNEAKING =
            SynchedEntityData.defineId(LOTRRangerEntity.class, EntityDataSerializers.BOOLEAN);

    private @Nullable Goal meleeAttackAI;
    private @Nullable Goal rangedAttackAI;
    public int sneakCooldown;
    private @Nullable LivingEntity prevRangerTarget;

    protected LOTRRangerEntity(EntityType<? extends LOTRRangerEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LOTRDunedainEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 25.0)
                .add(Attributes.FOLLOW_RANGE, 24.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(LOTRNPCAttributes.NPC_RANGED_ACCURACY, 0.5);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_SNEAKING, false);
    }

    @Override
    protected Goal createDunedainAttackAI() {
        return meleeAttackAI();
    }

    private Goal meleeAttackAI() {
        if (this.meleeAttackAI == null) {
            this.meleeAttackAI = new LOTRAttackOnCollideGoal(this, 1.5, true);
        }
        return this.meleeAttackAI;
    }

    private Goal rangedAttackAI() {
        if (this.rangedAttackAI == null) {
            this.rangedAttackAI = new LOTRRangedAttackGoal(this, 1.25, 20, 40, 20.0f);
        }
        return this.rangedAttackAI;
    }

    public boolean isRangerSneaking() {
        return this.entityData.get(DATA_SNEAKING);
    }

    public void setRangerSneaking(boolean flag) {
        this.entityData.set(DATA_SNEAKING, flag);
        if (flag) {
            this.sneakCooldown = 20;
        }
    }

    @Override
    public void onAttackModeChange(AttackMode mode, boolean mounted) {
        this.goalSelector.removeGoal(meleeAttackAI());
        this.goalSelector.removeGoal(rangedAttackAI());
        if (mode == AttackMode.IDLE) {
            setItemSlot(EquipmentSlot.MAINHAND, this.npcItemsInv.getIdleItem());
        } else if (mode == AttackMode.MELEE) {
            this.goalSelector.addGoal(2, meleeAttackAI());
            setItemSlot(EquipmentSlot.MAINHAND, this.npcItemsInv.getMeleeWeapon());
        } else {
            this.goalSelector.addGoal(2, rangedAttackAI());
            setItemSlot(EquipmentSlot.MAINHAND, this.npcItemsInv.getRangedWeapon());
        }
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        boolean flag = super.hurtServer(level, source, damage);
        if (flag && isRangerSneaking()) {
            setRangerSneaking(false);
        }
        return flag;
    }

    /** func_145780_a: no footsteps while hidden. */
    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        if (!isRangerSneaking()) {
            super.playStepSound(pos, state);
        }
    }

    @Override
    public void swing(InteractionHand hand) {
        super.swing(hand);
        if (!level().isClientSide() && isRangerSneaking()) {
            setRangerSneaking(false);
        }
    }

    /** setAttackTarget: a new target sends him into hiding, if he is on foot. */
    @Override
    public void setTarget(@Nullable LivingEntity target, boolean speak) {
        super.setTarget(target, speak);
        if (target != null && target != this.prevRangerTarget) {
            this.prevRangerTarget = target;
            if (!level().isClientSide() && !isRangerSneaking() && getVehicle() == null) {
                setRangerSneaking(true);
            }
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (level().isClientSide()) {
            return;
        }
        if (getVehicle() == null) {
            if (isRangerSneaking()) {
                if (getTarget() == null) {
                    if (this.sneakCooldown > 0) {
                        --this.sneakCooldown;
                    } else {
                        setRangerSneaking(false);
                    }
                } else {
                    this.sneakCooldown = 20;
                }
            } else {
                this.sneakCooldown = 0;
            }
        } else if (isRangerSneaking()) {
            setRangerSneaking(false);
        }
    }

    @Override
    public void setupNPCGender() {
        this.familyInfo.setMale(true);
    }

    @Override
    protected void dropNPCItems(ServerLevel level, boolean killedByPlayer, int looting) {
        super.dropNPCItems(level, killedByPlayer, looting);
        dropNPCAmmo(level, Items.ARROW, looting);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRCombatItems.IRON_DAGGER));
        this.npcItemsInv.setRangedWeapon(new ItemStack(Items.BOW));
        this.npcItemsInv.setIdleItem(ItemStack.EMPTY);
        setItemSlot(EquipmentSlot.FEET, new ItemStack(LOTRCombatItems.RANGER_BOOTS));
        setItemSlot(EquipmentSlot.LEGS, new ItemStack(LOTRCombatItems.RANGER_LEGGINGS));
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(LOTRCombatItems.RANGER_TUNIC));
        setItemSlot(EquipmentSlot.HEAD, new ItemStack(LOTRCombatItems.RANGER_HOOD));
        return data;
    }
}
