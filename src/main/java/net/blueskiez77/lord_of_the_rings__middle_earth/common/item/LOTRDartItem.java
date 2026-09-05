package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRDartEntity;
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
 * LOTRItemDart: what a blowgun spits.
 *
 * <p>An {@link ArrowItem} for the same reason the crossbow bolt is one -- it is
 * what vanilla's projectile-weapon machinery knows how to fire -- and, like the
 * bolt, deliberately OUT of {@code #minecraft:arrows}, so no bow in the game
 * will take one. {@link LOTRBlowgunItem} names them through a tag of their own.
 *
 * <p>The poisoned form is the same item class with the flag set, as
 * LOTRItemDart.setPoisoned had it; the dose is applied by the dart entity.
 */
public class LOTRDartItem extends ArrowItem {

    private final boolean poisoned;

    public LOTRDartItem(boolean poisoned, Properties properties) {
        super(properties);
        this.poisoned = poisoned;
    }

    public boolean isPoisoned() {
        return this.poisoned;
    }

    @Override
    public AbstractArrow createArrow(Level level, ItemStack ammo, LivingEntity shooter,
            @Nullable ItemStack weapon) {
        return new LOTRDartEntity(LOTREntities.DART, shooter, level,
                ammo.copyWithCount(1), weapon);
    }

    @Override
    public Projectile asProjectile(Level level, Position pos, ItemStack stack, Direction direction) {
        LOTRDartEntity dart = new LOTRDartEntity(LOTREntities.DART, level,
                pos.x(), pos.y(), pos.z(), stack.copyWithCount(1), null);
        dart.pickup = AbstractArrow.Pickup.ALLOWED;
        return dart;
    }
}
