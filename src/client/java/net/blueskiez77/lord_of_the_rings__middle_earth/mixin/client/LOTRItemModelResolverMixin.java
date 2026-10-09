package net.blueskiez77.lord_of_the_rings__middle_earth.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRDataComponents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.shield.LOTRPlayerShields;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.shield.LOTRShields;

import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * A vanilla shield in the hands of a player who has chosen a LOTR shield is drawn with that shield's
 * face: the stack drawn carries the design, which its item model then shows (LOTRShieldDesignRenderer).
 */
@Mixin(ItemModelResolver.class)
abstract class LOTRItemModelResolverMixin {

    @ModifyVariable(method = "appendItemLayers", at = @At("HEAD"), argsOnly = true)
    private ItemStack lotr$shieldDesign(ItemStack item, @Local(argsOnly = true) @Nullable ItemOwner owner) {
        if (item.is(Items.SHIELD) && owner != null && owner.asLivingEntity() instanceof Player player) {
            LOTRShields shield = LOTRPlayerShields.getShield(player);
            if (shield != null) {
                ItemStack designed = item.copy();
                designed.set(LOTRDataComponents.SHIELD_DESIGN, shield);
                return designed;
            }
        }
        return item;
    }
}
