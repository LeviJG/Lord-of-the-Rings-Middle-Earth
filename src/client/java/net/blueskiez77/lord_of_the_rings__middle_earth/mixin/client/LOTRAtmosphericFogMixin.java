package net.blueskiez77.lord_of_the_rings__middle_earth.mixin.client;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.world.LOTREnvironmentClient;

import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.environment.AtmosphericFogEnvironment;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** onRenderFog: Middle-earth's fog closing in, over today's (LOTREnvironmentClient.modifyFog). */
@Mixin(AtmosphericFogEnvironment.class)
abstract class LOTRAtmosphericFogMixin {

    @Inject(method = "setupFog", at = @At("TAIL"))
    private void lotr$middleEarthFog(FogData fog, Camera camera, ClientLevel level, float renderDistance, DeltaTracker deltaTracker,
                                     CallbackInfo ci) {
        if (camera.entity() != null) {
            LOTREnvironmentClient.modifyFog(fog, level, camera.entity(), renderDistance, deltaTracker.getGameTimeDeltaPartialTick(false));
        }
    }
}
