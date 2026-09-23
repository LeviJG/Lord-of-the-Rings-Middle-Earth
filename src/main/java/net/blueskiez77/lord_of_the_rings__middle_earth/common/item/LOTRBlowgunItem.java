package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import java.util.List;
import java.util.function.Predicate;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

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

    /**
     * BowItem.releaseUsing step for step -- the same tenth-of-a-draw threshold
     * and power curve, over the blowgun's own five-tick draw -- but puffing the
     * mod's "lotr:item.dart" instead of a bowstring's twang.
     */
    @Override
    public boolean releaseUsing(ItemStack stack, Level level, LivingEntity shooter, int timeLeft) {
        if (!(shooter instanceof Player player)) {
            return false;
        }
        ItemStack projectile = player.getProjectile(stack);
        if (projectile.isEmpty()) {
            return false;
        }
        int held = getUseDuration(stack, shooter) - timeLeft;
        float power = getPowerForTime(held * 20 / DRAW_TICKS);
        if (power < 0.1) {
            return false;
        }
        List<ItemStack> drawn = draw(stack, projectile, player);
        if (level instanceof ServerLevel server && !drawn.isEmpty()) {
            shoot(server, player, player.getUsedItemHand(), stack, drawn, power * 3.0f, 1.0f, power == 1.0f, null);
        }
        level.playSound(null, player.getX(), player.getY(), player.getZ(), LOTRSounds.ITEM_DART, SoundSource.PLAYERS,
                1.0f, 1.0f / (level.getRandom().nextFloat() * 0.4f + 1.2f) + power * 0.5f);
        player.awardStat(Stats.ITEM_USED.get(this));
        return true;
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
