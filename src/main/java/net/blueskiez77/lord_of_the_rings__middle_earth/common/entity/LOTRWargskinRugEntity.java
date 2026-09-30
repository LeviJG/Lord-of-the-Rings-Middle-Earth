package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.warg.LOTRWargType;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

/** LOTREntityWargskinRug: a warg's skin, in its coat (LOTREntityWarg.WargType). */
public class LOTRWargskinRugEntity extends LOTRRugEntity {

    /** dataWatcher 18. */
    private static final EntityDataAccessor<Byte> DATA_TYPE =
            SynchedEntityData.defineId(LOTRWargskinRugEntity.class, EntityDataSerializers.BYTE);

    public LOTRWargskinRugEntity(EntityType<? extends LOTRWargskinRugEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_TYPE, (byte) 0);
    }

    public LOTRWargType getRugType() {
        return LOTRWargType.forID(this.entityData.get(DATA_TYPE));
    }

    public void setRugType(LOTRWargType type) {
        this.entityData.set(DATA_TYPE, (byte) type.wargID);
    }

    @Override
    public ItemStack getRugItem() {
        return new ItemStack(getRugType().rug());
    }

    @Override
    public SoundEvent getRugNoise() {
        return LOTRSounds.WARG_SAY;
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        output.putByte("RugType", (byte) getRugType().wargID);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        setRugType(LOTRWargType.forID(input.getByteOr("RugType", (byte) 0)));
    }
}
