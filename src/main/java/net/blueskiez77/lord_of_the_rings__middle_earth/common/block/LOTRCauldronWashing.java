package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRDataComponents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRMiscItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRToolItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRSmokingPipeItem;

import net.minecraft.core.cauldron.CauldronInteractions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.core.component.DataComponents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRLeatherHatItem;

/**
 * The rest of LOTREventHandler's cauldron washing -- the dyed feather, party
 * hat and robes are vanilla's through #cauldron_can_remove_dye; the leather hat
 * is here, because its feather is washed too. Each wash takes a level of
 * water, as func_150024_a(meta - 1) did.
 *
 * <p>A dyed pipe's smoke goes back to plain (the magic smoke is not a dye and
 * stays). A poisoned weapon washes back to the one it was made from --
 * LOTRRecipePoisonWeapon.poisonedToInput, which held every poisoned dagger and,
 * because the moon chisel was made through the same recipe class, the moon
 * chisel back into a plain one. The stack keeps its wear and anything else it
 * carries, as func_150996_a kept them.
 */
public final class LOTRCauldronWashing {

    private LOTRCauldronWashing() {
    }

    public static void init() {
        CauldronInteractions.WATER.put(LOTRMiscItems.SMOKING_PIPE, (state, level, pos, player, hand, stack) -> {
            int colour = LOTRSmokingPipeItem.getSmokeColor(stack);
            if (colour == 0 || colour == LOTRSmokingPipeItem.MAGIC_COLOR) {
                return InteractionResult.TRY_WITH_EMPTY_HAND;
            }
            if (!level.isClientSide()) {
                stack.set(LOTRDataComponents.SMOKE_COLOR, 0);
                player.awardStat(Stats.USE_CAULDRON);
                LayeredCauldronBlock.lowerFillLevel(state, level, pos);
            }
            return InteractionResult.SUCCESS;
        });

        // A leather hat loses its dye and its feather's (removeHatAndFeatherDye),
        // the feather staying in the band, white. Washed if either is dyed.
        CauldronInteractions.WATER.put(LOTRMiscItems.LEATHER_HAT, (state, level, pos, player, hand, stack) -> {
            if (!stack.has(DataComponents.DYED_COLOR) && !LOTRLeatherHatItem.isFeatherDyed(stack)) {
                return InteractionResult.TRY_WITH_EMPTY_HAND;
            }
            if (!level.isClientSide()) {
                stack.remove(DataComponents.DYED_COLOR);
                if (LOTRLeatherHatItem.hasFeather(stack)) {
                    stack.set(LOTRDataComponents.HAT_FEATHER, LOTRLeatherHatItem.FEATHER_WHITE);
                }
                player.awardStat(Stats.USE_CAULDRON);
                LayeredCauldronBlock.lowerFillLevel(state, level, pos);
            }
            return InteractionResult.SUCCESS;
        });
        BuiltInRegistries.ITEM.keySet().stream()
                .filter(id -> id.getNamespace().equals("lotr") && id.getPath().startsWith("poisoned_")
                        && id.getPath().endsWith("_dagger"))
                .forEach(id -> washTo(BuiltInRegistries.ITEM.getValue(id),
                        BuiltInRegistries.ITEM.getValue(Identifier.fromNamespaceAndPath(
                                id.getNamespace(), id.getPath().substring("poisoned_".length())))));
        washTo(LOTRToolItems.MOON_CHISEL, LOTRToolItems.CHISEL);
    }

    private static void washTo(Item poisoned, Item plain) {
        CauldronInteractions.WATER.put(poisoned, (state, level, pos, player, hand, stack) -> {
            if (!level.isClientSide()) {
                player.setItemInHand(hand, stack.transmuteCopy(plain));
                player.awardStat(Stats.USE_CAULDRON);
                LayeredCauldronBlock.lowerFillLevel(state, level, pos);
            }
            return InteractionResult.SUCCESS;
        });
    }
}
