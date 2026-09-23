package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.BlockHitResult;

/**
 * LOTRItemMatch: a flint and steel you use once.
 *
 * <p>onItemUse handed a fresh flint and steel the click and, if it lit
 * anything, spent one match. The same here: vanilla's flint and steel does the
 * lighting -- fire, campfires, candles, portals -- on a throwaway stack, and a
 * successful strike uses up a match.
 */
public class LOTRMatchItem extends Item {

    public LOTRMatchItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockHitResult hit = new BlockHitResult(context.getClickLocation(), context.getClickedFace(),
                context.getClickedPos(), context.isInside());
        UseOnContext proxy = new UseOnContext(context.getLevel(), context.getPlayer(), context.getHand(),
                new ItemStack(Items.FLINT_AND_STEEL), hit);
        InteractionResult result = Items.FLINT_AND_STEEL.useOn(proxy);
        if (result.consumesAction()) {
            context.getItemInHand().consume(1, context.getPlayer());
        }
        return result;
    }
}
