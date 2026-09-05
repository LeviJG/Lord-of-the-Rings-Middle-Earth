package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.enchant.LOTRModifiers;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * The modifier roll, for items that already extend something else.
 *
 * <p>{@link LOTRModifiableItem} is the usual way in, but a bow has to extend
 * BowItem and a poisoned dagger its own class, so the one useful line lives
 * here for them to call from inventoryTick.
 */
public interface LOTRModifiable {

    /** See LOTRModifiableItem for why the roll happens here rather than at a loot site. */
    default void rollModifiersOnce(ItemStack stack, ServerLevel level, Entity holder) {
        if (holder instanceof Player && !LOTRModifiers.hasBeenRolled(stack)) {
            LOTRModifiers.applyRandom(stack, level.getRandom(), false);
        }
    }
}
