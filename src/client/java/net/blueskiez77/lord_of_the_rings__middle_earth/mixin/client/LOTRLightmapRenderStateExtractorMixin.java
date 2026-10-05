package net.blueskiez77.lord_of_the_rings__middle_earth.mixin.client;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.LOTRScrapTraderMisbehaviour;

import net.minecraft.client.renderer.LightmapRenderStateExtractor;
import net.minecraft.client.renderer.state.LightmapRenderState;

import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * The oddment collector's misbehaving: every light in the world put out, as
 * the original filled the light brightness table with nothing.
 */
@Mixin(LightmapRenderStateExtractor.class)
abstract class LOTRLightmapRenderStateExtractorMixin {

    private static final Vector3f BLACK = new Vector3f();

    @Inject(method = "extract", at = @At("TAIL"))
    private void lotr$scrapTraderDarkness(LightmapRenderState state, float partialTicks, CallbackInfo ci) {
        if (LOTRScrapTraderMisbehaviour.isMisbehaving()) {
            state.needsUpdate = true;
            state.blockFactor = 0.0f;
            state.skyFactor = 0.0f;
            state.blockLightTint = BLACK;
            state.skyLightColor = BLACK;
            state.ambientColor = BLACK;
            state.brightness = 0.0f;
            state.nightVisionEffectIntensity = 0.0f;
        }
    }
}
