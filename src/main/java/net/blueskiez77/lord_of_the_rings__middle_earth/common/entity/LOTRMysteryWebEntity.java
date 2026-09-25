package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMiscItems;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

/** MysteryWeb: one of tabMisc's three throwables. */
public class LOTRMysteryWebEntity extends ThrowableItemProjectile {
    public LOTRMysteryWebEntity(EntityType<? extends LOTRMysteryWebEntity> type, Level level) {
        super(type, level);
    }

    public LOTRMysteryWebEntity(Level level, LivingEntity thrower) {
        super(LOTREntities.MYSTERY_WEB, thrower, level, new ItemStack(LOTRMiscItems.MYSTERY_WEB));
    }

    @Override
    protected Item getDefaultItem() {
        return LOTRMiscItems.MYSTERY_WEB;
    }

    /** getGravityVelocity: 0.01 -- it drifts. */
    @Override
    protected double getDefaultGravity() {
        return 0.01;
    }

    /**
     * onImpact: nothing when it hits its thrower. Otherwise, one time in four,
     * a small Mirkwood spider comes out to attack the thrower; when there is no
     * spider -- always, until Mirkwood spiders are ported -- it pops out one
     * piece of Mirkwood chest loot, or one time in 500 a stack of 64 melon
     * slices, which waits ten ticks before it can be picked up.
     */
    @Override
    protected void onHit(HitResult hit) {
        if (getOwner() != null && hit instanceof EntityHitResult entityHit && entityHit.getEntity() == getOwner()) {
            return;
        }
        super.onHit(hit);
        if (level() instanceof ServerLevel server) {
            // rand.nextInt(4) == 0 would try the spider here (Track D).
            this.random.nextInt(4);
            ItemStack item = LOTRChestContents.pick(LOTRChestContents.MIRKWOOD_LOOT, this.random, false);
            if (this.random.nextInt(500) == 0) {
                item = new ItemStack(Items.MELON_SLICE, 64);
            }
            ItemEntity drop = new ItemEntity(server, getX(), getY(), getZ(), item);
            drop.setPickUpDelay(10);
            server.addFreshEntity(drop);
            playSound(SoundEvents.ITEM_PICKUP, 0.2f,
                    ((this.random.nextFloat() - this.random.nextFloat()) * 0.7f + 1.0f) * 2.0f);
            discard();
        }
    }
}
