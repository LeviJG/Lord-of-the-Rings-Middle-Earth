package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * A weapon or piece of armour that carries LOTR modifiers.
 *
 * <p>The original rolled an item's modifiers where the item came from -- chest
 * loot, an NPC trade, a smith's anvil. None of those three exist in the port,
 * so the roll happens the first time the item is seen in a player's inventory
 * instead, which comes to the same thing for anything picked up, crafted or
 * pulled out of the creative menu.
 *
 * <p>It happens once: LOTRModifiers marks the stack, and a marked stack is
 * never rolled again, so a Keen blade stays keen.
 *
 * <p>This is the port's own trigger, not the original's, and it is the piece to
 * revisit when loot tables, trades or the LOTR anvil land -- at that point the
 * roll belongs at those three sites and this override should go.
 */
public class LOTRModifiableItem extends Item implements LOTRModifiable {

    public LOTRModifiableItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity holder, EquipmentSlot slot) {
        super.inventoryTick(stack, level, holder, slot);
        rollModifiersOnce(stack, level, holder);
    }
}
