package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

/** Item tags the mod defines for itself. */
public final class LOTRItemTags {
    /**
     * What a weapon rack will hold. LOTRTileEntityWeaponRack.canAcceptItem
     * asked LOTRWeaponStats.isMeleeWeapon or isRangedWeapon, plus hoes and
     * fishing rods. LOTRWeaponStats is not ported, so the answer lives in a tag
     * -- which also means the mod's own weapons only have to be added to it
     * once, rather than taught to a stats table.
     */
    public static final TagKey<Item> WEAPON_RACK_HOLDABLE = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "weapon_rack_holdable"));

    private LOTRItemTags() {
    }
}
