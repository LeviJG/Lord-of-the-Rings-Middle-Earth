package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRFoodBlocks;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.boat.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

/**
 * LOTREntityBarrel: a barrel set on the water to ride, like a boat -- the
 * original was 1.7.10's EntityBoat with a barrel drawn in its place. Its boat
 * mechanics follow 26.2's boat, as a value inherited from 1.7.10 vanilla
 * would; what is the mod's own is kept:
 *
 * <ul>
 *   <li>one rider;</li>
 *   <li>slower: speedMultiplier ran 0.04 to 0.25 where the boat's ran 0.07
 *       to 0.40, so a barrel tops out at five-eighths of a boat. 26.2's
 *       paddling is private, so while someone is riding the horizontal speed
 *       is scaled by {@link #RIDDEN_DRAG} a tick, which in water (friction
 *       0.9) caps it at that five-eighths;</li>
 *   <li>broken, it drops the barrel item it was placed from, contents and all
 *       (getBarrelDrop, barrelItemData).</li>
 * </ul>
 *
 * <p>Picking it gives a plain barrel: 26.2 makes a boat's getPickResult final,
 * where the original gave the barrel with its contents.
 */
public class LOTRBarrelBoatEntity extends Boat {

    /** k with k / (1 - 0.9k) = 0.625 / 0.1, the ratio of the two top speeds. */
    private static final double RIDDEN_DRAG = 0.943;

    private static final EntityDataAccessor<ItemStack> BARREL_ITEM =
            SynchedEntityData.defineId(LOTRBarrelBoatEntity.class, EntityDataSerializers.ITEM_STACK);

    public LOTRBarrelBoatEntity(EntityType<? extends LOTRBarrelBoatEntity> type, Level level) {
        super(type, level, () -> LOTRFoodBlocks.BARREL.asItem());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(BARREL_ITEM, new ItemStack(LOTRFoodBlocks.BARREL));
    }

    /** The barrel it was placed from, what it drops and what is drawn. */
    public ItemStack getBarrelItem() {
        return this.entityData.get(BARREL_ITEM);
    }

    public void setBarrelItem(ItemStack stack) {
        this.entityData.set(BARREL_ITEM, stack.copyWithCount(1));
    }

    @Override
    protected int getMaxPassengers() {
        return 1;
    }

    @Override
    public void tick() {
        super.tick();
        if (isLocalInstanceAuthoritative() && getControllingPassenger() != null) {
            Vec3 motion = getDeltaMovement();
            setDeltaMovement(motion.x * RIDDEN_DRAG, motion.y, motion.z * RIDDEN_DRAG);
        }
    }

    /** getBarrelDrop: the barrel item, with whatever it held. */
    @Override
    protected void destroy(ServerLevel level, DamageSource source) {
        if (level.getGameRules().get(net.minecraft.world.level.gamerules.GameRules.ENTITY_DROPS)) {
            spawnAtLocation(level, getBarrelItem());
        }
    }

    /** Only barrels bump barrels: the original pushed nothing else. */
    @Override
    public boolean canCollideWith(Entity other) {
        return other instanceof LOTRBarrelBoatEntity && super.canCollideWith(other);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.store("BarrelItem", ItemStack.CODEC, getBarrelItem());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        input.read("BarrelItem", ItemStack.CODEC).ifPresent(this::setBarrelItem);
    }
}
