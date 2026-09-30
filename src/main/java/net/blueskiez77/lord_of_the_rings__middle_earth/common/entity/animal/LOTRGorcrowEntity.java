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

/** LOTREntityGorcrow: a crow, 1.4 times the size, with 8.0 health and a deeper call. */
public class LOTRGorcrowEntity extends LOTRBirdEntity {

    /** GORCROW_SCALE: the hitbox (LOTREntities) and the renderer. */
    public static final float SCALE = 1.4f;

    public LOTRGorcrowEntity(EntityType<? extends LOTRGorcrowEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LOTRBirdEntity.createAttributes().add(Attributes.MAX_HEALTH, 8.0);
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
        return "gorcrow";
    }

    @Override
    public float getVoicePitch() {
        return super.getVoicePitch() * 0.75f;
    }
}
