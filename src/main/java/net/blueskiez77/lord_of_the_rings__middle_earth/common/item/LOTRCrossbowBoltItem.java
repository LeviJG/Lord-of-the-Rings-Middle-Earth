package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRCrossbowBoltEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;

import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import org.jspecify.annotations.Nullable;

/**
 * LOTRItemCrossbowBolt: ammunition, and nothing else.
 *
 * <p>It is an {@link ArrowItem} so that vanilla's own projectile-weapon
 * machinery can fire it: ProjectileWeaponItem.createProjectile looks for an
 * ArrowItem in the ammunition slot and asks it to build the projectile, so
 * overriding createArrow is all it takes for a crossbow to shoot bolts.
 *
 * <p>Deliberately NOT in the {@code #minecraft:arrows} tag. That tag is what
 * vanilla bows and crossbows test their ammunition against, and the original
 * kept the two apart: LOTRItemBow's getInvArrowSlot only ever looked for
 * arrows, and LOTRItemCrossbow's getInvBoltSlot only ever for bolts. Leaving
 * the bolt out of the tag keeps a bolt out of every bow in the game, and
 * {@link LOTRCrossbowItem} names the bolt directly rather than going through
 * the tag.
 *
 * <p>The poisoned bolt is the same class with the flag set, as
 * LOTRItemCrossbowBolt.setPoisoned had it; the bolt entity delivers the dose.
 */
public class LOTRCrossbowBoltItem extends ArrowItem {

    private final boolean poisoned;

    public LOTRCrossbowBoltItem(Properties properties) {
        this(false, properties);
    }

    public LOTRCrossbowBoltItem(boolean poisoned, Properties properties) {
        super(properties);
        this.poisoned = poisoned;
    }

    public boolean isPoisoned() {
        return this.poisoned;
    }

    @Override
    public AbstractArrow createArrow(Level level, ItemStack ammo, LivingEntity shooter,
            @Nullable ItemStack weapon) {
        return new LOTRCrossbowBoltEntity(LOTREntities.CROSSBOW_BOLT, shooter, level,
                ammo.copyWithCount(1), weapon);
    }

    /** LOTRDispenseCrossbowBolt: a dispenser throws one as it would an arrow. */
    @Override
    public Projectile asProjectile(Level level, Position pos, ItemStack stack, Direction direction) {
        LOTRCrossbowBoltEntity bolt = new LOTRCrossbowBoltEntity(LOTREntities.CROSSBOW_BOLT, level,
                pos.x(), pos.y(), pos.z(), stack.copyWithCount(1), null);
        bolt.pickup = AbstractArrow.Pickup.ALLOWED;
        return bolt;
    }
}
