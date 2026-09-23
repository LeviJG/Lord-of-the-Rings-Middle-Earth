package net.blueskiez77.lord_of_the_rings__middle_earth.mixin;

import com.llamalad7.mixinextras.sugar.Local;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.enchant.LOTRModifiers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

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
}
