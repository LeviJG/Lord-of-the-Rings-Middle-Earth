package net.blueskiez77.lord_of_the_rings__middle_earth.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.world.LOTRWeatherClient;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.WeatherEffectRenderer;
import net.minecraft.client.renderer.state.level.WeatherRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * LOTRWeatherRenderer: Middle-earth's ash and sandstorms where the land takes no rain, and its own rain
 * and snow textures with "New weather" (LOTRWeatherClient).
 */
@Mixin(WeatherEffectRenderer.class)
abstract class LOTRWeatherEffectRendererMixin {

    @WrapOperation(method = "extractRenderState", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/multiplayer/ClientLevel;getPrecipitationAt(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/biome/Biome$Precipitation;"))
    private Biome.Precipitation lotr$ashAndSand(ClientLevel level, BlockPos pos, Operation<Biome.Precipitation> original,
                                                @Local(argsOnly = true) float partialTicks, @Local(argsOnly = true) WeatherRenderState state) {
        LOTRWeatherClient.StateExtension ext = LOTRWeatherClient.extension(state);
        if (ext != null && LOTRWeatherClient.addColumn(level, pos.getX(), pos.getZ(), pos.getY(), state.radius, partialTicks,
                ext.lotr$ashColumns(), ext.lotr$sandColumns())) {
            return Biome.Precipitation.NONE;
        }
        if (LOTRWeatherClient.isChristmasSnow(level)) {
            return Biome.Precipitation.SNOW;
        }
        return original.call(level, pos);
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/texture/TextureManager;getTexture(Lnet/minecraft/resources/Identifier;)Lnet/minecraft/client/renderer/texture/AbstractTexture;",
            ordinal = 0))
    private Identifier lotr$rainTexture(Identifier texture) {
        return LOTRWeatherClient.texture(texture, false);
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/texture/TextureManager;getTexture(Lnet/minecraft/resources/Identifier;)Lnet/minecraft/client/renderer/texture/AbstractTexture;",
            ordinal = 1))
    private Identifier lotr$snowTexture(Identifier texture) {
        return LOTRWeatherClient.texture(texture, true);
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void lotr$renderAshAndSand(Vec3 cameraPos, WeatherRenderState state, CallbackInfo ci) {
        LOTRWeatherClient.StateExtension ext = LOTRWeatherClient.extension(state);
        if (ext != null) {
            LOTRWeatherClient.render(cameraPos, state.radius, state.intensity, ext.lotr$ashColumns(), ext.lotr$sandColumns());
        }
    }
}
