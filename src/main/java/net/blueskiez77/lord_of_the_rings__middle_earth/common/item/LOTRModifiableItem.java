package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import net.minecraft.world.item.Item;

/**
 * A weapon or piece of armour that carries LOTR modifiers.
 *
 * <p>Nothing to do here any more: LOTRModifiers rolls every item a player gets,
 * the mod's and vanilla's alike, as LOTREnchantmentHelper.onEntityUpdate did,
 * so this is a plain Item. It stays because every weapon and armour piece is
 * registered through it.
 */
public class LOTRModifiableItem extends Item {

    public LOTRModifiableItem(Properties properties) {
        super(properties);
    }

}
