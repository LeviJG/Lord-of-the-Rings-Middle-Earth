package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import org.jspecify.annotations.Nullable;

/** LOTREntityCrebain: a crow, 1.8 times the size, with 10.0 health and a deeper call. */
public class LOTRCrebainEntity extends LOTRBirdEntity {

    /** CREBAIN_SCALE: the hitbox (LOTREntities) and the renderer. */
    public static final float SCALE = 1.8f;

    public LOTRCrebainEntity(EntityType<? extends LOTRCrebainEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LOTRBirdEntity.createAttributes().add(Attributes.MAX_HEALTH, 10.0);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason reason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, groupData);
        setBirdType(BirdType.CROW);
        return data;
    }

    @Override
    public String getBirdTextureDir() {
        return "crebain";
    }

    @Override
    public float getVoicePitch() {
        return super.getVoicePitch() * 0.85f;
    }
}
