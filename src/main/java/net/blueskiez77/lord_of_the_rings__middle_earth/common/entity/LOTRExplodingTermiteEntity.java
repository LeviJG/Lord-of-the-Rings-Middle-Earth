package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItems;

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
        super(LOTREntities.EXPLODING_TERMITE, thrower, level, new ItemStack(LOTRItems.EXPLODING_TERMITE));
    }

    @Override
    protected Item getDefaultItem() {
        return LOTRItems.EXPLODING_TERMITE;
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
