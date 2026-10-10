package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.pig.PigSoundVariant;
import net.minecraft.world.entity.animal.pig.PigSoundVariants;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;

/**
 * LOTREntityWildBoar: a hostile mount (3 attack), bred on carrots, with a
 * pig's voice and step, at most 25 health, and boar barding. Drops are
 * {@code lotr:entities/boar}: one to three porkchops plus looting.
 */
public class LOTRWildBoarEntity extends LOTRHorseEntity {

    public LOTRWildBoarEntity(EntityType<? extends LOTRWildBoarEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createHostileAttributes(3.0);
    }

    @Override
    public boolean isMountHostile() {
        return true;
    }

    @Override
    protected double mountAttackSpeed() {
        return 1.2;
    }

    @Override
    public boolean isLotrBreedingItem(ItemStack stack) {
        return stack.is(Items.CARROT);
    }

    @Override
    public double clampChildHealth(double health) {
        return Mth.clamp(health, 10.0, 30.0);
    }

    @Override
    public double clampChildSpeed(double speed) {
        return Mth.clamp(speed, 0.08, 0.29);
    }

    @Override
    protected void onLOTRHorseSpawn() {
        getAttribute(Attributes.MAX_HEALTH).setBaseValue(Math.min(getAttributeValue(Attributes.MAX_HEALTH), 25.0));
        getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(clampChildSpeed(getAttributeValue(Attributes.MOVEMENT_SPEED)));
    }

    /** readEntityFromNBT: an over-fast boar from an old save is slowed to the clamp. */
    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        double speed = getAttributeValue(Attributes.MOVEMENT_SPEED);
        double clamped = clampChildSpeed(speed);
        if (clamped < speed) {
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(clamped);
        }
    }

    /** The 1.7.10 pig's voice: 26.2's classic pig. */
    private static PigSoundVariant.PigSoundSet pig() {
        return SoundEvents.PIG_SOUNDS.get(PigSoundVariants.SoundSet.CLASSIC).adultSounds();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        playSound(SoundEvents.PIG_STEP.value(), 0.15f, 1.0f);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return pig().ambientSound().value();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return pig().ambientSound().value();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return pig().deathSound().value();
    }

    @Override
    protected SoundEvent getAngrySound() {
        return pig().ambientSound().value();
    }
}
