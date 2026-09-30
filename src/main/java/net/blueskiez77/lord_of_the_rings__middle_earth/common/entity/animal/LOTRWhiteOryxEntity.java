package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityWhiteOryx: a gemsbok nine-tenths the size, with random skins and
 * the deer's drops ({@code lotr:entities/white_oryx}).
 */
public class LOTRWhiteOryxEntity extends LOTRGemsbokEntity {

    /** ORYX_SCALE: the hitbox (LOTREntities) and the renderer. */
    public static final float ORYX_SCALE = 0.9f;

    public LOTRWhiteOryxEntity(EntityType<? extends LOTRWhiteOryxEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LOTRGemsbokEntity.createAttributes().add(Attributes.MAX_HEALTH, 16.0);
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
        return LOTREntities.WHITE_ORYX.create(level, EntitySpawnReason.BREEDING);
    }

    @Override
    public float getGemsbokSoundPitch() {
        return 0.9f;
    }
}
