package net.blueskiez77.lord_of_the_rings__middle_earth.mixin;

import com.llamalad7.mixinextras.sugar.Local;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.enchant.LOTRModifiers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.enchant.LOTRModifierSpecials;

/**
 * getLaunchSpeedFactor: a launcher's damage modifier is a launch speed
 * modifier. The arrow leaves faster or slower, and hits the harder or softer
 * for it -- "x1.1 launch speed", as the tooltip says.
 */
@Mixin(ProjectileWeaponItem.class)
abstract class LOTRProjectileWeaponItemMixin {
    @ModifyVariable(method = "shoot", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private float lotr$launchSpeed(float power, @Local(argsOnly = true) ItemStack weapon) {
        return power * LOTRModifiers.rangedDamageFactor(weapon);
    }

    /** setProjectileEnchantment: the launcher's weapon specials ride on what it looses. */
    @org.spongepowered.asm.mixin.injection.Inject(method = "createProjectile", at = @At("RETURN"))
    private void lotr$carrySpecials(net.minecraft.world.level.Level level, net.minecraft.world.entity.LivingEntity shooter,
            ItemStack weapon, ItemStack ammo, boolean crit,
            org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable<net.minecraft.world.entity.projectile.Projectile> cir) {
        LOTRModifierSpecials.onLaunch(weapon, cir.getReturnValue());
    }
}
