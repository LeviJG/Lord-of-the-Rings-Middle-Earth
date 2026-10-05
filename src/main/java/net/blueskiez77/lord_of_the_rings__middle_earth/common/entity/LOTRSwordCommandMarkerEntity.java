package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

/**
 * LOTREntitySwordCommandMarker: the command sword falling onto the spot its
 * order fell, made only on the commanding player's client -- dropping 0.35 a
 * tick from six blocks up, gone after a second and a half. Never saved.
 */
public class LOTRSwordCommandMarkerEntity extends Entity {

    public int particleAge;
    public int particleMaxAge = 30;

    public LOTRSwordCommandMarkerEntity(EntityType<? extends LOTRSwordCommandMarkerEntity> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    @Override
    public void tick() {
        super.tick();
        setPos(getX(), getY() - 0.35, getZ());
        ++this.particleAge;
        if (this.particleAge >= this.particleMaxAge) {
            discard();
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        return false;
    }

    @Override
    public boolean shouldBeSaved() {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
    }
}
