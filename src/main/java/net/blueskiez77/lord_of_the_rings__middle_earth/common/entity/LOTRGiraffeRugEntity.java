package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItems;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import org.jspecify.annotations.Nullable;

/** LOTREntityGiraffeRug: a giraffe's hide. Its noise was "", so it never growls. */
public class LOTRGiraffeRugEntity extends LOTRRugEntity {

    public LOTRGiraffeRugEntity(EntityType<? extends LOTRGiraffeRugEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    public ItemStack getRugItem() {
        return new ItemStack(LOTRItems.GIRAFFE_RUG);
    }

    @Override
    public @Nullable SoundEvent getRugNoise() {
        return null;
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
    }
}
