package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRBannerType;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

/**
 * LOTRBannerBearer: an NPC who carries its faction's banner raised in its
 * left hand. Up to five of them within sixteen blocks strengthen their
 * fellows (LOTREntityNPC.updateNearbyBanners); hired, one keeps among the
 * player's other units rather than at the player's side.
 */
public interface LOTRBannerBearer {

    LOTRBannerType getBannerType();

    /** getHeldItemLeft: the banner, as its item. */
    default ItemStack getBannerItem() {
        return new ItemStack(BuiltInRegistries.ITEM.getValue(
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, getBannerType().getSerializedName() + "_banner")));
    }
}
