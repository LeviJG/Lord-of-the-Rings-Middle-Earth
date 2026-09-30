package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

/**
 * LOTREntityElk: a hostile mount (3 attack), bred on wheat, random skins,
 * the deer's drops ({@code lotr:entities/elk}), up to half again the usual
 * health, and elk barding.
 */
public class LOTRElkEntity extends LOTRHorseEntity {

    public LOTRElkEntity(EntityType<? extends LOTRElkEntity> type, Level level) {
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
        return 1.25;
    }

    @Override
    public boolean isLotrBreedingItem(ItemStack stack) {
        return stack.is(Items.WHEAT);
    }

    @Override
    public double clampChildHealth(double health) {
        return Mth.clamp(health, 16.0, 50.0);
    }

    @Override
    public double clampChildSpeed(double speed) {
        return Mth.clamp(speed, 0.08, 0.34);
    }

    @Override
    protected void onLOTRHorseSpawn() {
        double maxHealth = getAttributeValue(Attributes.MAX_HEALTH);
        getAttribute(Attributes.MAX_HEALTH).setBaseValue(maxHealth * (1.0f + this.random.nextFloat() * 0.5f));
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return LOTRSounds.ELK_SAY;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return LOTRSounds.ELK_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return LOTRSounds.ELK_DEATH;
    }
}
