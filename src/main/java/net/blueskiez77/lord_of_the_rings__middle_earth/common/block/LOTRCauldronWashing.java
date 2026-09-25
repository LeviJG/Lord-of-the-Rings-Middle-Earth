package net.blueskiez77.lord_of_the_rings__middle_earth.common.block;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRDataComponents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRSmokingPipeItem;

import net.minecraft.core.cauldron.CauldronInteractions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.LayeredCauldronBlock;

/**
 * The rest of LOTREventHandler's cauldron washing -- the dyed hats and robes
 * are vanilla's through #cauldron_can_remove_dye. Each wash takes a level of
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
        CauldronInteractions.WATER.put(LOTRItems.SMOKING_PIPE, (state, level, pos, player, hand, stack) -> {
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

        BuiltInRegistries.ITEM.keySet().stream()
                .filter(id -> id.getNamespace().equals("lotr") && id.getPath().startsWith("poisoned_")
                        && id.getPath().endsWith("_dagger"))
                .forEach(id -> washTo(BuiltInRegistries.ITEM.getValue(id),
                        BuiltInRegistries.ITEM.getValue(Identifier.fromNamespaceAndPath(
                                id.getNamespace(), id.getPath().substring("poisoned_".length())))));
        washTo(LOTRItems.MOON_CHISEL, LOTRItems.CHISEL);
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
