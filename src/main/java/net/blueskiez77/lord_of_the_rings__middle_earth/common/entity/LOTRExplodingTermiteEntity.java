package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMiscItems;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

/** ExplodingTermite: one of tabMisc's three throwables. */
public class LOTRExplodingTermiteEntity extends ThrowableItemProjectile {
    public LOTRExplodingTermiteEntity(EntityType<? extends LOTRExplodingTermiteEntity> type, Level level) {
        super(type, level);
    }

    public LOTRExplodingTermiteEntity(Level level, LivingEntity thrower) {
        super(LOTREntities.EXPLODING_TERMITE, thrower, level, new ItemStack(LOTRMiscItems.EXPLODING_TERMITE));
    }

    /**
     * 1.7.10 drew a thrown termite from the moment it was thrown. 26.2's
     * ThrowableProjectile hides one for its first two ticks while it is within
     * 3.5 blocks, so with the game's tick frozen a termite just thrown stayed
     * invisible up close. The rest of the check -- the size-based range -- is
     * vanilla's.
     */
    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        double size = this.getBoundingBox().getSize() * 4.0;
        if (Double.isNaN(size)) {
            size = 4.0;
        }
        size *= 64.0;
        return distance < size * size;
    }

    @Override
    protected Item getDefaultItem() {
        return LOTRMiscItems.EXPLODING_TERMITE;
    }

    /** onImpact: an explosion of strength two, which is what makes it "exploding". */
    @Override
    protected void onHit(HitResult hit) {
        super.onHit(hit);
        if (!level().isClientSide()) {
            level().explode(this, getX(), getY(), getZ(), 2.0f, Level.ExplosionInteraction.TNT);
            discard();
        }
    }
}
