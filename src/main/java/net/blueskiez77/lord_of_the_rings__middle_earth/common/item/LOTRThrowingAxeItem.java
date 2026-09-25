package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRThrowingAxeEntity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.Level;

/**
 * LOTRItemThrowingAxe: an axe made to leave your hand.
 *
 * <p>One per stack, and every throw costs it a point of durability -- picked up
 * again it is a little more worn, and the throw that would finish it breaks it
 * where it lies. Its damage is the material's, worked the same way a battleaxe's
 * is: {@code (material damage + 4) * 0.5}, multiplied at impact by how fast the
 * axe was still travelling, which is what makes a long throw hit softer than a
 * close one.
 *
 * <p>It carries LOTR modifiers like the rest of the tab -- the damage and
 * knockback families, exactly the two LOTREnchantmentType.THROWING_AXE is named
 * on -- plus the durability ones. Only knockback and durability change the
 * throw; see the note on LOTRThrowingAxeEntity for why, which is the
 * original's behaviour rather than a gap here.
 *
 * <p>NOT ported: LOTRDispenseThrowingAxe, which let a dispenser throw one. The
 * behaviour was registered from this constructor in 1.7.10, and 26.2 wants it
 * on DispenserBlock's registry at mod init; it wants doing alongside the other
 * dispenser behaviours the port has yet to bring over, not on its own here.
 */
public class LOTRThrowingAxeItem extends Item implements net.minecraft.world.item.ProjectileItem {

    /**
     * The speed the axe leaves the hand at.
     *
     * <p>The original threw at three -- charge 2.0, which
     * LOTREntityProjectileBase multiplied by 1.5 -- the same as an arrow at a
     * full draw, and far too flat and quick to read as a tumbling axe. Two is
     * the port's own figure, a shade under a trident's 2.5.
     */
    public static final float THROW_VELOCITY = 2.0f;

    /** What the original threw at, which the damage and the arc are pinned to. */
    public static final float ORIGINAL_THROW_VELOCITY = 3.0f;

    /** setThrowableHeading's f1: the same scatter the original threw with. */
    public static final float THROW_INACCURACY = 1.0f;

    private final ToolMaterial material;

    public LOTRThrowingAxeItem(ToolMaterial material, Properties properties) {
        super(properties);
        this.material = material;
    }

    public ToolMaterial getMaterial() {
        return this.material;
    }

    /**
     * getRangedDamageMultiplier: {@code (material damage + 4) * 0.5}.
     *
     * <p>The +4 is LOTRItemSword's, so a throwing axe is worked out from the
     * same base every other weapon in the mod is, then halved. The entity
     * multiplies this by its speed at the moment of impact, which is what
     * AbstractArrow does with base damage anyway -- so this is handed straight
     * to setBaseDamage and vanilla's own arithmetic does the rest.
     *
     * <p>The trailing term keeps the axe hitting for what it always did. Impact
     * damage is speed times this figure, so slowing the throw from three to two
     * would have taken a third off it as a side effect; scaling by the ratio
     * puts that third back and leaves the throw's SPEED as the only thing that
     * changed. A long throw still lands softer than a close one, since the
     * speed at impact is still what counts.
     *
     * <p>Nothing here stands in for the critical bonus any more: the axe gets
     * the real thing, rolled per hit, without the particles that used to come
     * with it. See LOTRThrowingAxeEntity.onHitEntity.
     */
    public float getThrownBaseDamage() {
        return (this.material.attackDamageBonus() + 4.0f) * 0.5f
                * (ORIGINAL_THROW_VELOCITY / THROW_VELOCITY);
    }

    /**
     * onItemRightClick: throw it.
     *
     * <p>The axe carries a COPY of the stack, so its wear and its modifiers
     * travel with it and come back on pickup. In creative the thrown axe is
     * marked creative-only, which is vanilla's way of saying "this one is not
     * worth picking up", and the held stack is not spent -- both of which the
     * original did with canBePickedUp and the creative check.
     */
    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // The original played "random.bow", which is a bowstring and wrong for
        // something you throw with your arm. A trident's throw is the nearest
        // vanilla has to a heavy blade leaving a hand.
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0f,
                1.0f / (level.getRandom().nextFloat() * 0.4f + 1.2f) + 0.25f);

        if (level instanceof ServerLevel server) {
            LOTRThrowingAxeEntity axe = new LOTRThrowingAxeEntity(
                    LOTREntities.THROWING_AXE, player, server, stack.copyWithCount(1));
            axe.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f,
                    THROW_VELOCITY, THROW_INACCURACY);
            if (player.hasInfiniteMaterials()) {
                axe.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
            }
            net.blueskiez77.lord_of_the_rings__middle_earth.common.enchant.LOTRModifierSpecials.onLaunch(stack, axe);
            server.addFreshEntity(axe);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.hasInfiniteMaterials()) {
            stack.shrink(1);
        }
        return InteractionResult.SUCCESS;
    }

    /**
     * LOTRDispenseThrowingAxe: out of the front at 1.1 with 6.0 spread (vanilla's
     * dispenser defaults), one axe per shot.
     */
    @Override
    public net.minecraft.world.entity.projectile.Projectile asProjectile(Level level,
            net.minecraft.core.Position pos, ItemStack stack, net.minecraft.core.Direction direction) {
        return new LOTRThrowingAxeEntity(LOTREntities.THROWING_AXE, level,
                pos.x(), pos.y(), pos.z(), stack.copyWithCount(1));
    }
}
