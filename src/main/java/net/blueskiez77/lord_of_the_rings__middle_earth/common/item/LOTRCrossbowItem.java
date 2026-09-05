package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import java.util.function.Predicate;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;

import org.jspecify.annotations.Nullable;

/**
 * LOTRItemCrossbow, on vanilla's CrossbowItem.
 *
 * <p>The two are the same machine. Both hold the draw, load a bolt into the
 * weapon itself, keep it there until the next right click, and fire it without
 * a second draw -- the original kept the loaded round in a "LOTRCrossbowAmmo"
 * NBT tag and 26.2 keeps it in the {@code charged_projectiles} component, which
 * is the same idea with a tooltip and a model property attached. So this is
 * CrossbowItem with two changes.
 *
 * <p>FIRST: it eats bolts and nothing else. getInvBoltSlot walked the inventory
 * for an LOTRItemCrossbowBolt, and neither arrows nor firework rockets would do;
 * the two predicates below say the same to vanilla's ammunition search.
 *
 * <p>SECOND: the material matters. boltDamageFactor was
 * {@code 1 + max(0, (material damage - 2) * 0.1)} -- 1.0 for iron, which is
 * exactly the two the expression subtracts, 1.1 for uruk steel and 1.3 for
 * mithril -- and it scaled the speed the bolt left at. A faster bolt hits
 * harder, because impact damage is speed times the bolt's own weight, so this
 * one number is the whole difference between the three.
 *
 * <p>DIVERGENCE, deliberately and at the user's asking: the DRAW is vanilla's,
 * not the original's. LOTRItemCrossbow.getMaxDrawTime returned a flat 50 ticks;
 * CrossbowItem.getChargeDuration is 25, less whatever Quick Charge takes off.
 * Nothing is overridden to reach that -- it is simply what CrossbowItem does.
 *
 * <p>Also from vanilla rather than the original: the loading and firing SOUNDS.
 * 26.2 reads a crossbow's charging sounds from an enchantment effect component
 * and hardcodes CROSSBOW_SHOOT inside the shooting path, so neither can be
 * swapped per item without reimplementing onUseTick and shoot wholesale. The
 * mod's own item.crossbow and item.crossbowLoad oggs are still in
 * {@code old mod/}, unported, if that trade stops being worth it.
 */
public class LOTRCrossbowItem extends CrossbowItem implements LOTRModifiable {

    /** getInvBoltSlot: a bolt, and never an arrow or a rocket. */
    private static final Predicate<ItemStack> BOLTS_ONLY =
            stack -> stack.getItem() instanceof LOTRCrossbowBoltItem;

    /** setMaxDamage(material.getMaxUses() * 1.25f). */
    private static final float DURABILITY_FACTOR = 1.25f;

    private final float boltVelocityFactor;

    public LOTRCrossbowItem(ToolMaterial material, Properties properties) {
        super(properties);
        this.boltVelocityFactor = velocityFactorOf(material);
    }

    /** boltDamageFactor: 1 + max(0, (damage - 2) * 0.1). */
    public static float velocityFactorOf(ToolMaterial material) {
        return 1.0f + Math.max(0.0f, (material.attackDamageBonus() - 2.0f) * 0.1f);
    }

    public float getBoltVelocityFactor() {
        return this.boltVelocityFactor;
    }

    /**
     * The properties every LOTR crossbow shares.
     *
     * <p>Enchantability is the original's own formula rather than the
     * material's raw figure: LOTRItemCrossbow overrode getItemEnchantability to
     * {@code 1 + enchantability / 5}, so an iron crossbow sits at 3 and a
     * mithril one at 2 -- a crossbow is a machine, and a poor thing to enchant.
     */
    public static Item.Properties properties(ToolMaterial material) {
        return new Item.Properties()
                .stacksTo(1)
                .durability((int) (material.durability() * DURABILITY_FACTOR))
                .repairable(material.repairItems())
                .enchantable(1 + material.enchantmentValue() / 5);
    }

    /**
     * It carries the durability modifiers, the same as a bow does: the
     * original's LOTREnchantmentType.BREAKABLE was simply "it wears out", and
     * LOTRItemCrossbow extended ItemBow. The melee families have nothing to act
     * on here and the ranged ones are not ported.
     */
    @Override
    public void inventoryTick(ItemStack stack, net.minecraft.server.level.ServerLevel level,
            net.minecraft.world.entity.Entity holder,
            net.minecraft.world.entity.EquipmentSlot slot) {
        super.inventoryTick(stack, level, holder, slot);
        rollModifiersOnce(stack, level, holder);
    }

    @Override
    public Predicate<ItemStack> getSupportedHeldProjectiles() {
        return BOLTS_ONLY;
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return BOLTS_ONLY;
    }

    /**
     * getCrossbowLaunchSpeedFactor, applied where vanilla hands the bolt its
     * launch velocity. The base is CrossbowItem's own 3.15 rather than the
     * original's 3.0, since the draw is vanilla's too and the two belong
     * together.
     */
    @Override
    protected void shootProjectile(LivingEntity shooter, Projectile projectile, int index,
            float velocity, float inaccuracy, float angle, @Nullable LivingEntity target) {
        super.shootProjectile(shooter, projectile, index,
                velocity * this.boltVelocityFactor, inaccuracy, angle, target);
    }
}
