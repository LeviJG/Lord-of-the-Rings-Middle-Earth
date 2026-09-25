package net.blueskiez77.lord_of_the_rings__middle_earth.common.item;

import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRThrowingAxeEntity;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;

import net.minecraft.resources.Identifier;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * The full-set effects LOTREventHandler gave three kinds of armour, each
 * checked by the four pieces being worn together.
 */
public final class LOTRArmourSets {

    private static final List<EquipmentSlot> ARMOUR_SLOTS =
            List.of(EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET);

    /**
     * LOTREntityWoodElfScout.scoutArmorSpeedBoost: +0.3, operation 2 -- a
     * multiplier on the total -- and not saved, as a transient modifier is not.
     */
    private static final Identifier SCOUT_SPEED_ID =
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "wood_elven_scout_speed");
    private static final AttributeModifier SCOUT_SPEED = new AttributeModifier(
            SCOUT_SPEED_ID, 0.3, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

    private LOTRArmourSets() {
    }

    public static void init() {
        ServerLivingEntityEvents.ALLOW_DAMAGE.register(LOTRArmourSets::galvornStopsMissiles);
        ServerLivingEntityEvents.AFTER_DAMAGE.register((entity, source, base, taken, blocked) -> {
            if (!blocked) {
                morgulRotsWeapon(entity, source);
            }
        });
    }

    private static boolean wearingAll(LivingEntity entity, Item head, Item chest, Item legs, Item feet) {
        return entity.getItemBySlot(EquipmentSlot.HEAD).is(head)
                && entity.getItemBySlot(EquipmentSlot.CHEST).is(chest)
                && entity.getItemBySlot(EquipmentSlot.LEGS).is(legs)
                && entity.getItemBySlot(EquipmentSlot.FEET).is(feet);
    }

    /**
     * onLivingAttacked: a full set of galvorn turns arrows, crossbow bolts and
     * darts. A player's armour takes the wear instead -- damageArmor, a quarter
     * of the blow per piece and at least one -- and the attack is cancelled.
     */
    private static boolean galvornStopsMissiles(LivingEntity entity, DamageSource source, float amount) {
        if (!(source.getDirectEntity() instanceof AbstractArrow missile)
                || missile instanceof ThrownTrident || missile instanceof LOTRThrowingAxeEntity) {
            return true;
        }
        if (!wearingAll(entity, LOTRCombatItems.GALVORN_HELMET, LOTRCombatItems.GALVORN_CHESTPLATE,
                LOTRCombatItems.GALVORN_LEGGINGS, LOTRCombatItems.GALVORN_BOOTS)) {
            return true;
        }
        if (entity instanceof Player) {
            int wear = (int) Math.max(1.0f, amount / 4.0f);
            for (EquipmentSlot slot : ARMOUR_SLOTS) {
                entity.getItemBySlot(slot).hurtAndBreak(wear, entity, slot);
            }
        }
        return false;
    }

    /**
     * onLivingHurt: strike someone in a full Morgul set with a melee weapon and
     * the weapon rots -- its remaining durability falls to nine tenths.
     */
    private static void morgulRotsWeapon(LivingEntity entity, DamageSource source) {
        if (!(source.getEntity() instanceof LivingEntity attacker) || source.getDirectEntity() != attacker) {
            return;
        }
        if (!wearingAll(entity, LOTRCombatItems.MORGUL_HELMET, LOTRCombatItems.MORGUL_CHESTPLATE,
                LOTRCombatItems.MORGUL_LEGGINGS, LOTRCombatItems.MORGUL_BOOTS)) {
            return;
        }
        ItemStack weapon = attacker.getMainHandItem();
        if (weapon.isEmpty() || !weapon.isDamageableItem()) {
            return;
        }
        int damage = weapon.getDamageValue();
        int maxDamage = weapon.getMaxDamage();
        float durability = 1.0f - (float) damage / maxDamage;
        int newDamage = Math.min(Math.round((1.0f - durability * 0.9f) * maxDamage), maxDamage);
        weapon.hurtAndBreak(newDamage - damage, attacker, EquipmentSlot.MAINHAND);
    }

    /**
     * updateWoodElvenScoutArmorEffect: every ten ticks, the scout speed boost
     * comes off and goes back on only if all four scout pieces are worn.
     */
    public static void tickScoutSpeed(LivingEntity entity) {
        if (entity.level().isClientSide() || !entity.isAlive() || entity.tickCount % 10 != 0) {
            return;
        }
        AttributeInstance speed = entity.getAttribute(Attributes.MOVEMENT_SPEED);
        if (speed == null) {
            return;
        }
        speed.removeModifier(SCOUT_SPEED_ID);
        if (wearingAll(entity, LOTRCombatItems.WOOD_ELVEN_SCOUT_HOOD, LOTRCombatItems.WOOD_ELVEN_SCOUT_TUNIC,
                LOTRCombatItems.WOOD_ELVEN_SCOUT_LEGGINGS, LOTRCombatItems.WOOD_ELVEN_SCOUT_BOOTS)) {
            speed.addTransientModifier(SCOUT_SPEED);
        }
    }
}
