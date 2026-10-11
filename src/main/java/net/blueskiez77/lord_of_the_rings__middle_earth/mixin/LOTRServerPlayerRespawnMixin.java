package net.blueskiez77.lord_of_the_rings__middle_earth.mixin;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRMiddleEarthRespawn;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.portal.TeleportTransition;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** The respawn of a player who died in Middle-earth (LOTRMiddleEarthRespawn). */
@Mixin(ServerPlayer.class)
public abstract class LOTRServerPlayerRespawnMixin {

    @Inject(method = "findRespawnPositionAndUseSpawnBlock", at = @At("RETURN"), cancellable = true)
    private void lotr$middleEarthRespawn(boolean useCharge, TeleportTransition.PostTeleportTransition post,
            CallbackInfoReturnable<TeleportTransition> cir) {
        cir.setReturnValue(LOTRMiddleEarthRespawn.adjust((ServerPlayer) (Object) this, cir.getReturnValue()));
    }
}
