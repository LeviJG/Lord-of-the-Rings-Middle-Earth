package net.blueskiez77.lord_of_the_rings__middle_earth.client.render;

import java.util.Map;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRCombatItems;

import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;

import org.jspecify.annotations.Nullable;

/**
 * LOTRItemMountArmor.getArmorTexture: the sheet a mount's barding is drawn
 * from, laid over the mount's own skin as the original's LayeredTexture did.
 *
 * <p>Horse barding is textured for 1.7.10's horse: the LOTR materials' own
 * horse_body sheets (already in that layout), and for vanilla's iron, gold and
 * diamond barding the 1.7.10 sheets the original's template items pointed at.
 * Leather barding came after 1.7.10 and has no sheet in that layout, so it is
 * not drawn.
 */
public final class LOTRMountArmorTextures {

    private static final Map<Item, Identifier> MOUNT_ARMOR = Map.of(
            LOTRCombatItems.WOOD_ELVEN_ELK_ARMOR, mount("elk_wood_elven"),
            LOTRCombatItems.DWARVEN_BOAR_ARMOR, mount("boar_dwarven"),
            LOTRCombatItems.BLUE_DWARVEN_BOAR_ARMOR, mount("boar_blue_dwarven"),
            LOTRCombatItems.HALF_TROLL_RHINO_ARMOR, mount("rhino_half_troll"),
            LOTRCombatItems.MORDOR_WARG_ARMOR, mount("warg_mordor"),
            LOTRCombatItems.ISENGARD_WARG_ARMOR, mount("warg_uruk"),
            LOTRCombatItems.ANGMAR_WARG_ARMOR, mount("warg_angmar"));

    private LOTRMountArmorTextures() {
    }

    private static Identifier mount(String name) {
        return Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "textures/entity/mount_armor/" + name + ".png");
    }

    private static Identifier legacy(String name) {
        return Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "textures/entity/legacy_horse/" + name + ".png");
    }

    public static @Nullable Identifier textureFor(ItemStack stack) {
        if (stack.isEmpty()) {
            return null;
        }
        Identifier own = MOUNT_ARMOR.get(stack.getItem());
        if (own != null) {
            return own;
        }
        Equippable equippable = stack.get(DataComponents.EQUIPPABLE);
        if (equippable == null || equippable.assetId().isEmpty()) {
            return null;
        }
        Identifier asset = equippable.assetId().get().identifier();
        if (asset.getNamespace().equals("minecraft")) {
            return switch (asset.getPath()) {
                case "iron" -> legacy("horse_armor_iron");
                case "gold" -> legacy("horse_armor_gold");
                case "diamond" -> legacy("horse_armor_diamond");
                default -> null;
            };
        }
        return Identifier.fromNamespaceAndPath(asset.getNamespace(),
                "textures/entity/equipment/horse_body/" + asset.getPath() + ".png");
    }
}
