package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.enchant.LOTRModifiers;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRThrowingAxeItem;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

/**
 * LOTREntityThrowingAxe, on vanilla's arrow rather than on the mod's own
 * projectile base.
 *
 * <p>LOTREntityProjectileBase was a hand-written EntityArrow that carried an
 * ItemStack: it flew with 0.99 drag and 0.05 of gravity, stuck in the first
 * block it met, shook for seven ticks, and could be walked into and picked up
 * again. AbstractArrow is all of that, and its impact damage is worked out with
 * the same expression -- {@code ceil(speed * baseDamage)}, plus
 * {@code rand(damage / 2 + 2)} when the shot is critical -- so handing it
 * {@link LOTRThrowingAxeItem#getThrownBaseDamage()} reproduces the original's
 * numbers exactly rather than approximating them.
 *
 * <p>What is left is the three things vanilla arrows do not do: the axe TUMBLES
 * in flight, it WEARS as it is thrown and picked up, and a stuck one waits five
 * minutes rather than one for its owner to come back for it.
 *
 * <p>NOT ported: the mod's damage modifiers do not reach the thrown damage.
 * getRangedDamageMultiplier read the material and vanilla's bane enchantments
 * and nothing else, so a Keen axe hit for the same as a plain one; only the
 * knockback family changed the throw. That is reproduced rather than corrected
 * -- see LOTRModifier.Kind.THROWING_AXE.
 */
public class LOTRThrowingAxeEntity extends AbstractArrow {

    /**
     * Ticks per revolution. axeRotation ran 0 to 9 in the original -- a turn
     * every ten ticks, which at the speed an axe now travels reads as barely
     * turning at all. Five puts it at four revolutions a second.
     */
    public static final int SPIN_PERIOD = 5;

    /** maxTicksInGround: five minutes for a thrown axe somebody may want back. */
    private static final int DESPAWN_TICKS_OWNED = 6000;

    /** And one minute for one nobody can pick up. */
    private static final int DESPAWN_TICKS_LOOSE = 1200;

    /**
     * Where in its tumble the axe is, 0 through SPIN_PERIOD - 1.
     *
     * <p>Not synched, and deliberately not: the original kept axeRotation as a
     * plain field and counted it up in onUpdate, which runs on both sides. Both
     * copies start at zero when the entity appears and step once a tick, so
     * they stay together without a byte on the wire.
     */
    private int spin;

    private int groundTicks;

    // NO gravity override. The axe uses AbstractArrow's own 0.05, at the user's
    // asking -- it briefly flew on a lighter pull worked out from the speed
    // ratio, and then on the midpoint of the two, and vanilla's is what stuck.

    public LOTRThrowingAxeEntity(EntityType<? extends LOTRThrowingAxeEntity> type, Level level) {
        super(type, level);
    }

    public LOTRThrowingAxeEntity(EntityType<? extends LOTRThrowingAxeEntity> type,
            LivingEntity thrower, Level level, ItemStack axe) {
        // No "fired from" weapon: an axe is not launched by anything, it IS the
        // thing thrown, so vanilla's launcher-enchantment path has nothing to
        // read and the knockback that matters is handled in doKnockback below.
        super(type, thrower, level, axe, null);
        setBaseDamage(baseDamageOf(axe));
    }

    /** LOTRItemThrowingAxe.getRangedDamageMultiplier, or nothing if it is not one. */
    private static double baseDamageOf(ItemStack stack) {
        return stack.getItem() instanceof LOTRThrowingAxeItem axe
                ? axe.getThrownBaseDamage()
                : 0.0;
    }

    /** Only ever read when a stack is missing; the plainest of the three will do. */
    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(LOTRItems.IRON_THROWING_AXE);
    }

    /** The axe itself is the weapon, as ThrownTrident's is. */
    @Override
    public ItemStack getWeaponItem() {
        return getPickupItemStackOrigin();
    }

    /** Whether it has landed. The renderer needs to know; isInGround is protected. */
    public boolean isStuck() {
        return isInGround();
    }

    public int getSpin() {
        return this.spin;
    }

    @Override
    public void tick() {
        super.tick();
        if (!isInGround()) {
            this.spin = (this.spin + 1) % SPIN_PERIOD;
        }
    }

    /**
     * The critical bonus, without the critical particles.
     *
     * <p>AbstractArrow reads isCritArrow for two unrelated things: it adds
     * {@code rand(damage / 2 + 2)} here, and it trails CRIT particles behind
     * the projectile every tick of its flight. The original's throw wanted the
     * first and the particles are an arrow's tell, wrong on an axe.
     *
     * <p>They can be had apart. The flag goes up for the length of the hit and
     * straight back down, and both happen inside ONE server tick -- so the
     * damage roll sees a critical axe, and SynchedEntityData, which packs the
     * CURRENT value of a dirty entry when the tick ends rather than a history
     * of it, sends the client false. The client never holds the flag, so it
     * never draws a particle.
     */
    @Override
    protected void onHitEntity(EntityHitResult hit) {
        setCritArrow(true);
        try {
            super.onHitEntity(hit);
        } finally {
            setCritArrow(false);
        }
    }

    /**
     * maxTicksInGround. Vanilla counts to 1200 either way; the original gave a
     * player's own axe five times that, which matters for a weapon you are
     * expected to walk over and collect.
     */
    @Override
    protected void tickDespawn() {
        this.groundTicks++;
        int limit = this.pickup == Pickup.ALLOWED ? DESPAWN_TICKS_OWNED : DESPAWN_TICKS_LOOSE;
        if (this.groundTicks >= limit) {
            discard();
        }
    }

    /**
     * The mod's own knockback modifiers, on top of whatever vanilla worked out.
     *
     * <p>LOTREntityProjectileBase's own line: the horizontal motion, normalised,
     * scaled by the knockback level and 0.6, with a tenth of a block of lift.
     * LOTRWeaponStats.getRangedKnockback sent a throwing axe down the melee
     * path, so it is the axe's own knockback1/knockback2 that counts here.
     */
    @Override
    protected void doKnockback(LivingEntity target, DamageSource source) {
        super.doKnockback(target, source);

        int knockback = LOTRModifiers.knockback(getPickupItemStackOrigin());
        if (knockback <= 0) {
            return;
        }
        Vec3 motion = getDeltaMovement();
        double horizontal = Math.sqrt(motion.x * motion.x + motion.z * motion.z);
        if (horizontal > 0.0) {
            target.push(motion.x * knockback * 0.6 / horizontal, 0.1,
                    motion.z * knockback * 0.6 / horizontal);
        }
    }

    /**
     * createPickupDrop: picking the axe up costs it a point, and the throw that
     * would finish it breaks it instead.
     *
     * <p>Returning true for a broken axe is what makes AbstractArrow.playerTouch
     * take the entity away with nothing to show for it, which is the original's
     * {@code else setDead()} branch.
     */
    @Override
    protected boolean tryPickup(Player player) {
        return switch (this.pickup) {
            case DISALLOWED -> false;
            case ALLOWED -> {
                ItemStack worn = wornPickup();
                yield worn.isEmpty() || player.getInventory().add(worn);
            }
            case CREATIVE_ONLY -> player.hasInfiniteMaterials();
        };
    }

    /** The stack the player gets back, one point of durability poorer. */
    private ItemStack wornPickup() {
        ItemStack stack = getPickupItem();
        if (!stack.isDamageableItem()) {
            return stack;
        }
        int damage = stack.getDamageValue() + 1;
        if (damage >= stack.getMaxDamage()) {
            return ItemStack.EMPTY;
        }
        stack.setDamageValue(damage);
        return stack;
    }
}
