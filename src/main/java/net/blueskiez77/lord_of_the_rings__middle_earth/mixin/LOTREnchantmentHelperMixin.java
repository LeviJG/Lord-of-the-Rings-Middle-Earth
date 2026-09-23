package net.blueskiez77.lord_of_the_rings__middle_earth.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.enchant.LOTRModifiers;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * LOTRReplacedMethods.Enchants: the places the original's coremod made vanilla
 * enchantment arithmetic ask LOTREnchantmentHelper as well.
 */
@Mixin(EnchantmentHelper.class)
abstract class LOTREnchantmentHelperMixin {

    /** getLootingModifier and getFortuneModifier: + calcLootingLevel. */
    @ModifyReturnValue(method = "getItemEnchantmentLevel", at = @At("RETURN"))
    private static int lotr$looting(int level, @Local(argsOnly = true) Holder<Enchantment> enchantment,
            @Local(argsOnly = true) ItemInstance piece) {
        if (enchantment.is(Enchantments.LOOTING) || enchantment.is(Enchantments.FORTUNE)) {
            return level + LOTRModifiers.lootLevel(piece);
        }
        return level;
    }

    /** attemptDamageItem: a durable item shrugs off some of its wear. */
    @ModifyReturnValue(method = "processDurabilityChange", at = @At("RETURN"))
    private static int lotr$durability(int amount, @Local(argsOnly = true) ServerLevel level,
            @Local(argsOnly = true) ItemStack stack, @Local(argsOnly = true) int original) {
        return Math.max(0, amount - LOTRModifiers.negatedDamage(stack, level.getRandom(), original));
    }

    /** getSpecialArmorProtection: + the fire, fall and projectile protection worn. */
    @ModifyReturnValue(method = "getDamageProtection", at = @At("RETURN"))
    private static float lotr$specialProtection(float protection, @Local(argsOnly = true) LivingEntity victim,
            @Local(argsOnly = true) DamageSource source) {
        return protection + LOTRModifiers.specialProtection(victim, source);
    }

    /**
     * getEnchantmentModifierLiving: a bane's extra damage, in melee. And the
     * launch speed modifiers work on the arrow as it flies (see
     * LOTRProjectileWeaponItemMixin), so nothing ranged is added here.
     */
    @ModifyReturnValue(method = "modifyDamage", at = @At("RETURN"))
    private static float lotr$bane(float damage, @Local(argsOnly = true) ItemStack weapon,
            @Local(argsOnly = true) Entity victim, @Local(argsOnly = true) DamageSource source) {
        if (source.is(DamageTypeTags.IS_PROJECTILE)) {
            return damage;
        }
        return damage + LOTRModifiers.baneDamage(weapon, victim);
    }

    /** applyBowModifiers: the launcher's knockback, added to the punch its arrow carries. */
    @ModifyReturnValue(method = "modifyKnockback", at = @At("RETURN"))
    private static float lotr$rangedKnockback(float knockback, @Local(argsOnly = true) ItemStack weapon,
            @Local(argsOnly = true) DamageSource source) {
        if (!source.is(DamageTypeTags.IS_PROJECTILE)) {
            return knockback;
        }
        return knockback + LOTRModifiers.rangedKnockback(weapon);
    }

    /**
     * getSilkTouchModifier: a Silken tool does everything silk touch does --
     * ice stays ice, a bee nest keeps its bees, an infested block and a
     * decorated pot come away whole.
     */
    @ModifyReturnValue(method = "hasTag", at = @At("RETURN"))
    private static boolean lotr$silkTouch(boolean has, @Local(argsOnly = true) ItemStack stack,
            @Local(argsOnly = true) TagKey<Enchantment> tag) {
        if (has || !LOTRModifiers.isSilkTouch(stack)) {
            return has;
        }
        return tag.equals(EnchantmentTags.PREVENTS_ICE_MELTING)
                || tag.equals(EnchantmentTags.PREVENTS_BEE_SPAWNS_WHEN_MINING)
                || tag.equals(EnchantmentTags.PREVENTS_INFESTED_SPAWNS)
                || tag.equals(EnchantmentTags.PREVENTS_DECORATED_POT_SHATTERING);
    }
}
