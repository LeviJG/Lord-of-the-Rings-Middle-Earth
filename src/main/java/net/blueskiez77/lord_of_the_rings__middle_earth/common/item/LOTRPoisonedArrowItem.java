package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRPoisonedArrowEntity;

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
 * LOTRItemArrowPoisoned: an arrow whose hit poisons.
 *
 * <p>An ArrowItem so bows can fire it, but deliberately NOT in
 * {@code #minecraft:arrows}: LOTRItemBow's getInvArrowSlot took a plain arrow or
 * this one, while a vanilla bow knew only plain arrows. LOTRBowItem names it
 * directly, so it goes in LOTR bows and no others.
 */
public class LOTRPoisonedArrowItem extends ArrowItem {

    public LOTRPoisonedArrowItem(Properties properties) {
        super(properties);
    }

    @Override
    public AbstractArrow createArrow(Level level, ItemStack ammo, LivingEntity shooter,
            @Nullable ItemStack weapon) {
        return new LOTRPoisonedArrowEntity(LOTREntities.POISONED_ARROW, shooter, level,
                ammo.copyWithCount(1), weapon);
    }

    /** LOTRDispenseArrowPoisoned. */
    @Override
    public Projectile asProjectile(Level level, Position pos, ItemStack stack, Direction direction) {
        LOTRPoisonedArrowEntity arrow = new LOTRPoisonedArrowEntity(LOTREntities.POISONED_ARROW, level,
                pos.x(), pos.y(), pos.z(), stack.copyWithCount(1), null);
        arrow.pickup = AbstractArrow.Pickup.ALLOWED;
        return arrow;
    }
}
