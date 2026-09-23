package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.block.LOTRMugBlock;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRItemGlassBottle and LOTRItemPotion: the mod replaced vanilla's glass
 * bottle and potion items so that they could be set down as a bottle block too,
 * empty or with water in it.
 *
 * <p>Items cannot be replaced now, so this does the same from a use-on-block
 * hook. The block's own interaction still comes first, as it did in 1.7.10 --
 * a bottle clicked on a cauldron still fills from the cauldron -- and only when
 * that does nothing is the bottle set down.
 */
public final class LOTRVanillaVessels {
    private LOTRVanillaVessels() {
    }

    public static void init() {
        UseBlockCallback.EVENT.register((player, level, hand, hitResult) -> {
            ItemStack stack = player.getItemInHand(hand);
            if (player.isSpectator() || !(stack.is(Items.GLASS_BOTTLE) || LOTRVessel.isWaterBottle(stack))) {
                return InteractionResult.PASS;
            }
            BlockState state = level.getBlockState(hitResult.getBlockPos());
            boolean holding = !player.getMainHandItem().isEmpty() || !player.getOffhandItem().isEmpty();
            if (!(player.isSecondaryUseActive() && holding)) {
                InteractionResult used = state.useItemOn(stack, level, player, hand, hitResult);
                if (used.consumesAction()) {
                    return used;
                }
                if (used instanceof InteractionResult.TryEmptyHandInteraction && hand == InteractionHand.MAIN_HAND) {
                    InteractionResult empty = state.useWithoutItem(level, player, hitResult);
                    if (empty.consumesAction()) {
                        return empty;
                    }
                }
            }
            InteractionResult placed = LOTRMugBlock.tryPlaceMug(new UseOnContext(player, hand, hitResult), stack);
            return placed.consumesAction() ? placed : InteractionResult.PASS;
        });
    }
}
