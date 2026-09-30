package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.cow.AbstractCow;
import net.minecraft.world.level.Level;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityKineAraw: a bigger, tougher aurochs (scale 1.15), charging faster,
 * that drops the kine of Araw horn ({@code lotr:entities/kine_of_araw}).
 */
public class LOTRKineArawEntity extends LOTRAurochsEntity {

    /** KINE_SCALE: the hitbox (LOTREntities) and the renderer. */
    public static final float KINE_SCALE = 1.15f;

    public LOTRKineArawEntity(EntityType<? extends LOTRKineArawEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LOTRAurochsEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 40.0)
                .add(Attributes.ATTACK_DAMAGE, 5.0);
    }

    @Override
    protected double attackSpeed() {
        return 1.9;
    }

    @Override
    public @Nullable AbstractCow getBreedOffspring(ServerLevel level, AgeableMob partner) {
        return LOTREntities.KINE_OF_ARAW.create(level, EntitySpawnReason.BREEDING);
    }
}
