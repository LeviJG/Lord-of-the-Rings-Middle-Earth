package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItems;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * LOTREntityGiraffe: four blocks tall, bred on leaves, a weaker jumper, and
 * horse sounds. Drops leather ({@code lotr:entities/giraffe}) and, to a
 * player, now and then its rug ({@link LOTRRugDrops}).
 *
 * <p>NOT ported yet: the rideGiraffeShire achievement, for riding a saddled
 * one in the Shire (with the biomes, D10).
 */
public class LOTRGiraffeEntity extends LOTRHorseEntity {

    public LOTRGiraffeEntity(EntityType<? extends LOTRGiraffeEntity> type, Level level) {
        super(type, level);
    }

    /** isBreedingItem: any leaves block (BlockLeavesBase). */
    @Override
    public boolean isLotrBreedingItem(ItemStack stack) {
        return stack.is(ItemTags.LEAVES);
    }

    @Override
    public double clampChildHealth(double health) {
        return Mth.clamp(health, 12.0, 34.0);
    }

    @Override
    public double clampChildJump(double jump) {
        return Mth.clamp(jump, 0.2, 1.0);
    }

    @Override
    public double clampChildSpeed(double speed) {
        return Mth.clamp(speed, 0.08, 0.35);
    }

    @Override
    protected void onLOTRHorseSpawn() {
        getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(getAttributeValue(Attributes.JUMP_STRENGTH) * 0.8);
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean killedByPlayer) {
        super.dropCustomDeathLoot(level, source, killedByPlayer);
        if (killedByPlayer) {
            LOTRRugDrops.dropRug(this, level, source, LOTRItems.GIRAFFE_RUG);
        }
    }
}
