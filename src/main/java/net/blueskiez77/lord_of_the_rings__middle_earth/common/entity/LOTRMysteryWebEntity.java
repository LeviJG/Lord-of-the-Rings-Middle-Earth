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

/** MysteryWeb: one of tabMisc's three throwables. */
public class LOTRMysteryWebEntity extends ThrowableItemProjectile {
    public LOTRMysteryWebEntity(EntityType<? extends LOTRMysteryWebEntity> type, Level level) {
        super(type, level);
    }

    public LOTRMysteryWebEntity(Level level, LivingEntity thrower) {
        super(LOTREntities.MYSTERY_WEB, thrower, level, new ItemStack(LOTRItems.MYSTERY_WEB));
    }

    @Override
    protected Item getDefaultItem() {
        return LOTRItems.MYSTERY_WEB;
    }

    /** getGravityVelocity: 0.01 -- it drifts. */
    @Override
    protected double getDefaultGravity() {
        return 0.01;
    }

    /**
     * onImpact spat out a Mirkwood spider or a piece of Mirkwood chest loot.
     * Neither exists in the port, so the web does the one thing left to it and
     * leaves a cobweb where it lands.
     */
    @Override
    protected void onHit(HitResult hit) {
        super.onHit(hit);
        if (level() instanceof ServerLevel server) {
            BlockPos pos = BlockPos.containing(position());
            if (server.getBlockState(pos).canBeReplaced()) {
                server.setBlock(pos, Blocks.COBWEB.defaultBlockState(), Block.UPDATE_ALL);
            }
            playSound(SoundEvents.ITEM_PICKUP, 0.2f,
                    ((this.random.nextFloat() - this.random.nextFloat()) * 0.7f + 1.0f) * 2.0f);
            discard();
        }
    }
}
