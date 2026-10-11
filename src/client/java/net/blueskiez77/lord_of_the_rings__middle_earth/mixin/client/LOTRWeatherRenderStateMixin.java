package net.blueskiez77.lord_of_the_rings__middle_earth.mixin.client;

import java.util.ArrayList;
import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.world.LOTRWeatherClient;

import net.minecraft.client.renderer.WeatherEffectRenderer;
import net.minecraft.client.renderer.state.level.WeatherRenderState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** The weather's ash and sandstorm columns, beside its rain and snow (LOTRWeatherClient). */
@Mixin(WeatherRenderState.class)
abstract class LOTRWeatherRenderStateMixin implements LOTRWeatherClient.StateExtension {

    @Unique
    private final List<WeatherEffectRenderer.ColumnInstance> lotr$ashColumns = new ArrayList<>();

    @Unique
    private final List<WeatherEffectRenderer.ColumnInstance> lotr$sandColumns = new ArrayList<>();

    @Override
    public List<WeatherEffectRenderer.ColumnInstance> lotr$ashColumns() {
        return this.lotr$ashColumns;
    }

    @Override
    public List<WeatherEffectRenderer.ColumnInstance> lotr$sandColumns() {
        return this.lotr$sandColumns;
    }

    @Inject(method = "reset", at = @At("TAIL"))
    private void lotr$reset(CallbackInfo ci) {
        this.lotr$ashColumns.clear();
        this.lotr$sandColumns.clear();
    }
}
