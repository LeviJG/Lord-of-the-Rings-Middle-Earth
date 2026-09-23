package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.HoeItem;

/**
 * LOTRItemHoe: vanilla's hoe -- it tills -- carrying LOTR modifiers.
 *
 * <p>A 1.7.10 ItemHoe had no attack damage of its own, so the modifier is set
 * to cancel the material's bonus and a hoe hits like a fist, as it did. The
 * swing speed is vanilla's, which rises with the tier: a stone-tier hoe -2.0,
 * an iron-tier one -1.0, anything better 0.
 */
public class LOTRHoeItem extends HoeItem implements LOTRModifiable {

    public LOTRHoeItem(ToolMaterial material, Properties properties) {
        super(material, -material.attackDamageBonus(), speedFor(material), properties);
    }

    private static float speedFor(ToolMaterial material) {
        var tier = material.incorrectBlocksForDrops();
        if (tier.equals(BlockTags.INCORRECT_FOR_WOODEN_TOOL)) {
            return -3.0f;
        }
        if (tier.equals(BlockTags.INCORRECT_FOR_STONE_TOOL)) {
            return -2.0f;
        }
        if (tier.equals(BlockTags.INCORRECT_FOR_IRON_TOOL) || tier.equals(BlockTags.INCORRECT_FOR_COPPER_TOOL)) {
            return -1.0f;
        }
        return 0.0f;
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity holder, EquipmentSlot slot) {
        super.inventoryTick(stack, level, holder, slot);
        rollModifiersOnce(stack, level, holder);
    }
}
