package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import java.util.function.Predicate;

import net.minecraft.world.item.ItemStack;

/**
 * LOTRItemBlowgun: a tube, and a very quick one.
 *
 * <p>getMaxDrawTime was FIVE ticks -- a quarter of a bow's -- so a blowgun is
 * the fastest thing to bring to bear in the mod by a wide margin, in exchange
 * for a dart that hits for about three. It is built on {@link LOTRBowItem},
 * which already scales vanilla's draw curve by the tick count, so the five
 * ticks need nothing further.
 *
 * <p>It eats DARTS and nothing else, the way LOTRItemBlowgun's own inventory
 * search did: getInvDartSlot looked for an LOTRItemDart and would not take an
 * arrow.
 */
public class LOTRBlowgunItem extends LOTRBowItem {

    /** getMaxDrawTime. */
    public static final int DRAW_TICKS = 5;

    /** getInvDartSlot: a dart, and never an arrow. */
    private static final Predicate<ItemStack> DARTS_ONLY =
            stack -> stack.getItem() instanceof LOTRDartItem;

    public LOTRBlowgunItem(Properties properties) {
        super(DRAW_TICKS, 1.0f, properties);
    }

    @Override
    public Predicate<ItemStack> getSupportedHeldProjectiles() {
        return DARTS_ONLY;
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return DARTS_ONLY;
    }
}
