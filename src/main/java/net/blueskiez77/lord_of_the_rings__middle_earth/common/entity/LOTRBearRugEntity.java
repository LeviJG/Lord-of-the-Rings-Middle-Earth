package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal.LOTRBearEntity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

/** LOTREntityBearRug: a bear's skin, in its colour (LOTREntityBear.BearType). */
public class LOTRBearRugEntity extends LOTRRugEntity {

    /** dataWatcher 18. */
    private static final EntityDataAccessor<Byte> DATA_TYPE =
            SynchedEntityData.defineId(LOTRBearRugEntity.class, EntityDataSerializers.BYTE);

    public LOTRBearRugEntity(EntityType<? extends LOTRBearRugEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_TYPE, (byte) 0);
    }

    public LOTRBearEntity.BearType getRugType() {
        return LOTRBearEntity.BearType.forID(this.entityData.get(DATA_TYPE));
    }

    public void setRugType(LOTRBearEntity.BearType type) {
        this.entityData.set(DATA_TYPE, (byte) type.bearID);
    }

    @Override
    public ItemStack getRugItem() {
        return new ItemStack(getRugType().rug());
    }

    @Override
    public SoundEvent getRugNoise() {
        return LOTRSounds.BEAR_SAY;
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        output.putByte("RugType", (byte) getRugType().bearID);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        setRugType(LOTRBearEntity.BearType.forID(input.getByteOr("RugType", (byte) 0)));
    }
}
