package net.blueskiez77.lord_of_the_rings__middle_earth.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTREffects;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * LOTRTickHandlerClient.onPreRenderGameOverlay (HEALTH): a player with drink
 * poison (LOTRPoisonedDrinks.killingPoison) has the hearts drawn as poisoned
 * hearts. The original gave poison client-side while they were drawn; here the
 * heart type's poison check counts drink poison too.
 */
@Mixin(targets = "net.minecraft.client.gui.Hud$HeartType")
abstract class LOTRHeartTypeMixin {

    @WrapOperation(method = "forPlayer", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/player/Player;hasEffect(Lnet/minecraft/core/Holder;)Z"))
    private static boolean lotr$drinkPoison(Player player, Holder<MobEffect> effect, Operation<Boolean> original) {
        return original.call(player, effect) || effect == MobEffects.POISON && player.hasEffect(LOTREffects.DRINK_POISON);
    }
}
