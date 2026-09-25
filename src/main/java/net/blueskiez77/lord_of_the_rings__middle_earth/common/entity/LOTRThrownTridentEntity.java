package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * A thrown LOTR trident.
 *
 * <p>It exists for one reason: the RENDERER is chosen by entity type.
 * ThrownTridentRenderer draws a fixed TridentModel with a fixed vanilla
 * texture, so a LOTR trident thrown as an EntityType.TRIDENT came back looking
 * like a vanilla one no matter what stack it carried. Its own type gets its own
 * renderer, and the sprite in the air is the sprite in the hand.
 *
 * <p>Everything else is ThrownTrident's: sticking, the return flight under
 * Loyalty, the damage, the pickup. The only awkwardness is getting IN -- the
 * throwing constructor hardcodes EntityType.TRIDENT and there is no protected
 * form that takes a type, so the three things it does are done here instead.
 * Two of them touch private synched data; see lotr.accesswidener.
 */
public class LOTRThrownTridentEntity extends ThrownTrident {

    public LOTRThrownTridentEntity(EntityType<? extends LOTRThrownTridentEntity> type, Level level) {
        super(type, level);
    }

    public LOTRThrownTridentEntity(EntityType<? extends LOTRThrownTridentEntity> type, Level level,
            LivingEntity thrower, ItemStack trident) {
        this(type, level);
        // AbstractArrow's own constructor puts a thrown projectile a tenth of a
        // block below eye height, which is what the throw looks right from.
        setPos(thrower.getX(), thrower.getEyeY() - 0.1, thrower.getZ());
        setOwner(thrower);
        setPickupItemStack(trident.copy());
        this.entityData.set(ThrownTrident.ID_LOYALTY, getLoyaltyFromItem(trident));
        this.entityData.set(ThrownTrident.ID_FOIL, trident.hasFoil());
    }

    /**
     * Only read when the carried stack is missing -- but it MUST be ours rather
     * than ThrownTrident's vanilla one, or a trident that lost its stack would
     * hand back a vanilla trident on pickup.
     */
    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(LOTRCombatItems.DUNLENDING_TRIDENT);
    }
}
