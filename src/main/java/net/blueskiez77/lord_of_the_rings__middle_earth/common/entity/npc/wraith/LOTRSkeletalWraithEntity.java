package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.wraith;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRLegacyWorld;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.ai.LOTRAttackOnCollideGoal;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.fac.LOTRFaction;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FleeSunGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.RestrictSunGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntitySkeletalWraith: the restless dead of an old grave, a skeleton in
 * the arms and armour it fell in -- 24 strong, fireproof, undead, hostile to
 * all and seeking them out. It keeps from the sun, and caught in the light by
 * day burns as a skeleton does, its helmet taking the burning for it while it
 * lasts. It smokes, sounds like a skeleton, drops only bones, and no rares.
 */
public abstract class LOTRSkeletalWraithEntity extends LOTRNPCEntity {

    protected LOTRSkeletalWraithEntity(EntityType<? extends LOTRSkeletalWraithEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createNPCAttributes()
                .add(Attributes.MAX_HEALTH, 24.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new RestrictSunGoal(this));
        this.goalSelector.addGoal(2, new FleeSunGoal(this, 1.0));
        this.goalSelector.addGoal(3, new LOTRAttackOnCollideGoal(this, 1.2, false));
        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0f, 0.02f));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        addTargetTasks(true);
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public LOTRFaction getFaction() {
        return LOTRFaction.HOSTILE;
    }

    @Override
    public boolean canDropRares() {
        return false;
    }

    @Override
    public void onAttackModeChange(AttackMode mode, boolean mounted) {
        setItemSlot(EquipmentSlot.MAINHAND,
                mode == AttackMode.IDLE ? this.npcItemsInv.getIdleItem() : this.npcItemsInv.getMeleeWeapon());
    }

    /** onLivingUpdate: the sun's burning, then smoke. */
    @Override
    public void aiStep() {
        if (level() instanceof ServerLevel level && level.isBrightOutside()) {
            float f = LOTRLegacyWorld.brightness(this);
            BlockPos pos = BlockPos.containing(getX(), getY(), getZ());
            if (f > 0.5f && this.random.nextFloat() * 30.0f < (f - 0.4f) * 2.0f && level.canSeeSky(pos)) {
                ItemStack helmet = getItemBySlot(EquipmentSlot.HEAD);
                if (!helmet.isEmpty()) {
                    if (helmet.isDamageableItem()) {
                        helmet.setDamageValue(helmet.getDamageValue() + this.random.nextInt(2));
                        if (helmet.getDamageValue() >= helmet.getMaxDamage()) {
                            onEquippedItemBroken(helmet.getItem(), EquipmentSlot.HEAD);
                            setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
                        }
                    }
                } else {
                    igniteForSeconds(8.0f);
                }
            }
        }
        super.aiStep();
        if (this.random.nextBoolean()) {
            level().addParticle(ParticleTypes.SMOKE, getX() + (this.random.nextDouble() - 0.5) * getBbWidth(),
                    getY() + this.random.nextDouble() * getBbHeight(),
                    getZ() + (this.random.nextDouble() - 0.5) * getBbWidth(), 0.0, 0.0, 0.0);
        }
    }

    @Override
    protected void dropNPCItems(ServerLevel level, boolean killedByPlayer, int looting) {
        super.dropNPCItems(level, killedByPlayer, looting);
        int bones = this.random.nextInt(3) + this.random.nextInt(looting + 1);
        for (int l = 0; l < bones; ++l) {
            spawnAtLocation(level, Items.BONE);
        }
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return SoundEvents.SKELETON_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.SKELETON_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SKELETON_DEATH;
    }
}
