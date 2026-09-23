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

/** Conker: one of tabMisc's three throwables. */
public class LOTRConkerEntity extends ThrowableItemProjectile {
    public LOTRConkerEntity(EntityType<? extends LOTRConkerEntity> type, Level level) {
        super(type, level);
    }

    public LOTRConkerEntity(Level level, LivingEntity thrower) {
        super(LOTREntities.CONKER, thrower, level, new ItemStack(LOTRItems.CONKER));
    }

    @Override
    protected Item getDefaultItem() {
        return LOTRItems.CONKER;
    }

    /** getGravityVelocity: 0.04, a little lighter than a snowball. */
    @Override
    protected double getDefaultGravity() {
        return 0.04;
    }

    /** onImpact: a point of damage to whatever it hits, then it lies where it fell. */
    @Override
    protected void onHitEntity(EntityHitResult hit) {
        super.onHitEntity(hit);
        hit.getEntity().hurt(damageSources().thrown(this, getOwner()), 1.0f);
    }

    @Override
    protected void onHit(HitResult hit) {
        super.onHit(hit);
        if (level() instanceof ServerLevel server) {
            spawnAtLocation(server, new ItemStack(LOTRItems.CONKER));
            discard();
        }
    }
}
