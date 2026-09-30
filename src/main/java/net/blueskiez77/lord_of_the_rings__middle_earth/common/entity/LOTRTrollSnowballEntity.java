package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.Snowball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

/**
 * LOTREntityTrollSnowball: a snow troll's snowball, a snowball that hits
 * anything for 3 -- not only blazes -- and bursts as any snowball does.
 */
public class LOTRTrollSnowballEntity extends Snowball {

    public LOTRTrollSnowballEntity(EntityType<? extends LOTRTrollSnowballEntity> type, Level level) {
        super(type, level);
    }

    public LOTRTrollSnowballEntity(Level level, LivingEntity thrower) {
        super(LOTREntities.TROLL_SNOWBALL, level);
        setOwner(thrower);
        setItem(new ItemStack(Items.SNOWBALL));
        setPos(thrower.getX(), thrower.getEyeY() - 0.1, thrower.getZ());
    }

    @Override
    protected void onHitEntity(EntityHitResult hit) {
        if (level() instanceof ServerLevel level) {
            hit.getEntity().hurtServer(level, damageSources().thrown(this, getOwner()), 3.0f);
        }
    }
}
