package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import org.jspecify.annotations.Nullable;

/**
 * LOTRItemBow: a bow that may draw faster and shoot harder than vanilla's.
 *
 * <p>Two figures set an elven bow apart from a plain one. {@code bowPullTime} is
 * how long a full draw takes -- 20 ticks as standard, 16 for the Galadhrim and
 * High Elven bows, 14 for a Mirkwood one. {@code arrowDamageFactor} multiplies
 * the arrow's launch speed, which is what gives those bows their flatter, longer
 * shot: 1.0 as standard and 1.25 for the elven bows.
 *
 * <p>It carries LOTR modifiers like everything else in the tab, though only the
 * durability ones can apply: the ranged family -- rangedStrong, rangedKnockback
 * -- is not ported, and the melee ones have nothing to act on here.
 *
 * <p>The draw is reached through releaseUsing rather than by overriding
 * getPowerForTime, which is static in 26.2 and so cannot be replaced. Vanilla
 * works out the ticks held as {@code useDuration - timeLeft} and feeds that to a
 * curve where 20 ticks is a full pull; scaling those ticks by
 * {@code 20 / drawTicks} before handing them back makes a 16-tick pull reach
 * full power at 16, with vanilla's curve otherwise untouched.
 */
public class LOTRBowItem extends BowItem implements LOTRModifiable {

    /** Vanilla's full draw, and the original's default bowPullTime. */
    private static final int STANDARD_DRAW_TICKS = 20;

    private final int drawTicks;
    private final float velocityFactor;

    public LOTRBowItem(int drawTicks, float velocityFactor, Properties properties) {
        super(properties);
        this.drawTicks = drawTicks;
        this.velocityFactor = velocityFactor;
    }

    public int getDrawTicks() {
        return this.drawTicks;
    }

    public float getVelocityFactor() {
        return this.velocityFactor;
    }

    @Override
    public void inventoryTick(ItemStack stack, net.minecraft.server.level.ServerLevel level,
            net.minecraft.world.entity.Entity holder, net.minecraft.world.entity.EquipmentSlot slot) {
        super.inventoryTick(stack, level, holder, slot);
        rollModifiersOnce(stack, level, holder);
    }

    @Override
    public boolean releaseUsing(ItemStack stack, Level level, LivingEntity shooter, int timeLeft) {
        if (this.drawTicks != STANDARD_DRAW_TICKS) {
            int held = getUseDuration(stack, shooter) - timeLeft;
            int scaled = held * STANDARD_DRAW_TICKS / this.drawTicks;
            timeLeft = getUseDuration(stack, shooter) - scaled;
        }
        return super.releaseUsing(stack, level, shooter, timeLeft);
    }

    /**
     * applyBowModifiers scaled the arrow's motion by arrowDamageFactor. The
     * modern equivalent is the launch velocity handed to the projectile here --
     * a faster arrow both flies further and hits harder, which is what the
     * original's name for the field meant.
     */
    @Override
    protected void shootProjectile(LivingEntity shooter, Projectile projectile, int index,
            float velocity, float inaccuracy, float angle, @Nullable LivingEntity target) {
        super.shootProjectile(shooter, projectile, index,
                velocity * this.velocityFactor, inaccuracy, angle, target);
    }
}
