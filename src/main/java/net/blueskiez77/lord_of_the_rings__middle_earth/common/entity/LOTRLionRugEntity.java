package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItems;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

/** LOTREntityLionRug: a lion's or a lioness's skin (LOTRItemLionRug.LionRugType). */
public class LOTRLionRugEntity extends LOTRRugEntity {

    /** dataWatcher 18. */
    private static final EntityDataAccessor<Byte> DATA_TYPE =
            SynchedEntityData.defineId(LOTRLionRugEntity.class, EntityDataSerializers.BYTE);

    public LOTRLionRugEntity(EntityType<? extends LOTRLionRugEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_TYPE, (byte) 0);
    }

    public RugType getRugType() {
        return RugType.forID(this.entityData.get(DATA_TYPE));
    }

    public void setRugType(RugType type) {
        this.entityData.set(DATA_TYPE, (byte) type.lionID);
    }

    @Override
    public ItemStack getRugItem() {
        return new ItemStack(getRugType().item());
    }

    @Override
    public SoundEvent getRugNoise() {
        return LOTRSounds.LION_SAY;
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        output.putByte("RugType", (byte) getRugType().lionID);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        setRugType(RugType.forID(input.getByteOr("RugType", (byte) 0)));
    }

    /** LOTRItemLionRug.LionRugType. */
    public enum RugType {
        LION(0), LIONESS(1);

        public final int lionID;

        RugType(int id) {
            this.lionID = id;
        }

        public static RugType forID(int id) {
            for (RugType t : values()) {
                if (t.lionID == id) {
                    return t;
                }
            }
            return LION;
        }

        public Item item() {
            return this == LIONESS ? LOTRItems.LIONESS_RUG : LOTRItems.LION_RUG;
        }
    }
}
