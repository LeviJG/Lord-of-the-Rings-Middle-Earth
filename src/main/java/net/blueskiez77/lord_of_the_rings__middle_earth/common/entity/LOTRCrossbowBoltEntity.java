package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItems;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import org.jspecify.annotations.Nullable;

/**
 * LOTREntityCrossbowBolt: the short, heavy shaft a crossbow throws.
 *
 * <p>Nothing to it but the weight. getBaseImpactDamage was
 * {@code speed * 2.0 * boltDamageFactor} with the factor sitting at 2, so a
 * bolt hits for four times its speed where an arrow manages two -- twice an
 * arrow, which is the whole point of a weapon that takes a second and a quarter
 * to reload. AbstractArrow multiplies base damage by speed itself, so the
 * figure goes in as base damage and vanilla's arithmetic does the rest.
 *
 * <p>Everything else -- the 0.99 drag, the 0.05 of gravity, sticking in blocks,
 * the seven-tick shake, being picked back up, the 1200-tick despawn -- is what
 * LOTREntityProjectileBase hand-wrote and what AbstractArrow already does.
 *
 * <p>NOT ported: onCollideWithTarget's poison, which belongs to the poisoned
 * bolt. crossbowBoltPoisoned is a separate item in the original and is not in
 * the port yet; the top half of crossbow_bolt.png is the plain bolt and the
 * bottom half is waiting for it.
 */
public class LOTRCrossbowBoltEntity extends AbstractArrow {

    /** BOLT_RELATIVE_TO_ARROW * boltDamageFactor: 2 x 2, twice an arrow's 2.0. */
    public static final double BASE_DAMAGE = 4.0;

    public LOTRCrossbowBoltEntity(EntityType<? extends LOTRCrossbowBoltEntity> type, Level level) {
        super(type, level);
        setBaseDamage(BASE_DAMAGE);
    }

    public LOTRCrossbowBoltEntity(EntityType<? extends LOTRCrossbowBoltEntity> type,
            LivingEntity shooter, Level level, ItemStack bolt, @Nullable ItemStack crossbow) {
        super(type, shooter, level, bolt, crossbow);
        setBaseDamage(BASE_DAMAGE);
    }

    /** The form a dispenser uses: a bolt at a position, with nobody behind it. */
    public LOTRCrossbowBoltEntity(EntityType<? extends LOTRCrossbowBoltEntity> type, Level level,
            double x, double y, double z, ItemStack bolt, @Nullable ItemStack crossbow) {
        super(type, x, y, z, level, bolt, crossbow);
        setBaseDamage(BASE_DAMAGE);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(LOTRItems.CROSSBOW_BOLT);
    }
}
