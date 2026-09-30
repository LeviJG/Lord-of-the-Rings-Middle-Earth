package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.block.Block;

/**
 * LOTRValuableItems: what a magpie will steal (and what a bandit takes first) -- coins, rings and gems, and
 * the repair material of any tool material that mines at iron level or
 * better (harvest level 2 up), vanilla's and the mod's alike, as the original
 * gathered them from every ToolMaterial there was.
 */
public final class LOTRValuableItems {

    private static final Set<TagKey<Block>> IRON_OR_BETTER = Set.of(
            BlockTags.INCORRECT_FOR_IRON_TOOL, BlockTags.INCORRECT_FOR_DIAMOND_TOOL, BlockTags.INCORRECT_FOR_NETHERITE_TOOL);

    private static List<TagKey<Item>> toolMaterials;

    private LOTRValuableItems() {
    }

    /** LOTRItemCoin. */
    public static boolean isCoin(ItemStack stack) {
        return stack.is(LOTRMiscItems.SILVER_COIN) || stack.is(LOTRMiscItems.SILVER_COIN_STACK)
                || stack.is(LOTRMiscItems.SILVER_COIN_PILE);
    }

    /** LOTRItemRing. */
    public static boolean isRing(ItemStack stack) {
        return stack.is(LOTRMiscItems.GOLD_RING) || stack.is(LOTRMiscItems.SILVER_RING)
                || stack.is(LOTRMiscItems.MITHRIL_RING);
    }

    /** LOTRItemGem. */
    public static boolean isGem(ItemStack stack) {
        return stack.is(LOTRMaterialItems.TOPAZ) || stack.is(LOTRMaterialItems.AMETHYST)
                || stack.is(LOTRMaterialItems.SAPPHIRE) || stack.is(LOTRMaterialItems.RUBY)
                || stack.is(LOTRMaterialItems.AMBER) || stack.is(LOTRMaterialItems.DIAMOND)
                || stack.is(LOTRMaterialItems.PEARL) || stack.is(LOTRMaterialItems.CORAL)
                || stack.is(LOTRMaterialItems.OPAL) || stack.is(LOTRMaterialItems.EMERALD);
    }

    private static boolean isTreasure(ItemStack stack) {
        return isCoin(stack) || isRing(stack) || isGem(stack);
    }

    /** One of getToolMaterials' repair items. */
    public static boolean isToolMaterial(ItemStack stack) {
        for (TagKey<Item> repair : getToolMaterials()) {
            if (stack.is(repair)) {
                return true;
            }
        }
        return false;
    }

    public static boolean canMagpieSteal(ItemStack stack) {
        return isTreasure(stack) || isToolMaterial(stack);
    }

    /** registerToolMaterials: every ToolMaterial constant, vanilla's and LOTRToolMaterials'. */
    private static List<TagKey<Item>> getToolMaterials() {
        if (toolMaterials == null) {
            List<TagKey<Item>> list = new ArrayList<>();
            for (Class<?> holder : List.of(ToolMaterial.class, LOTRToolMaterials.class)) {
                for (Field field : holder.getDeclaredFields()) {
                    if (field.getType() == ToolMaterial.class && Modifier.isStatic(field.getModifiers())) {
                        try {
                            field.setAccessible(true);
                            ToolMaterial material = (ToolMaterial) field.get(null);
                            if (material != null && IRON_OR_BETTER.contains(material.incorrectBlocksForDrops())) {
                                list.add(material.repairItems());
                            }
                        } catch (ReflectiveOperationException | RuntimeException e) {
                            // As the original: a material that cannot be read is skipped.
                        }
                    }
                }
            }
            toolMaterials = list;
        }
        return toolMaterials;
    }
}
