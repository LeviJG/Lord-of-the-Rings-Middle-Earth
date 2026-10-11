package net.blueskiez77.lord_of_the_rings__middle_earth.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.world.LOTRAuroraRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.world.LOTREnvironmentClient;
import net.blueskiez77.lord_of_the_rings__middle_earth.client.world.LOTRSkyRenderer;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.config.LOTRConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SkyRenderer;
import net.minecraft.world.level.MoonPhase;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** LOTRWorldProvider.getSkyRenderer ("LOTR sky"): Middle-earth's sun, moon, stars and Eärendil (LOTRSkyRenderer). */
@Mixin(SkyRenderer.class)
abstract class LOTRSkyRendererMixin {

    /** The sunrise's colour this frame, for the sun's sunrise glow drawn after it. */
    @Unique
    private int lotr$sunriseColor;

    @Unique
    private static boolean lotr$middleEarthSky() {
        return LOTRConfig.enableLOTRSky && LOTREnvironmentClient.isMiddleEarth(Minecraft.getInstance().level);
    }

    @ModifyVariable(method = "renderSunriseAndSunset", at = @At("HEAD"), argsOnly = true)
    private int lotr$sunriseColor(int color) {
        if (!lotr$middleEarthSky()) {
            this.lotr$sunriseColor = 0;
            return color;
        }
        this.lotr$sunriseColor = color;
        return LOTRSkyRenderer.sunriseColor(color);
    }

    @Inject(method = "renderSunMoonAndStars", at = @At("HEAD"), cancellable = true)
    private void lotr$middleEarthCelestials(PoseStack poseStack, float sunAngle, float moonAngle, float starAngle, MoonPhase moonPhase,
                                            float rainBrightness, float starBrightness, CallbackInfo ci) {
        if (lotr$middleEarthSky()) {
            LOTRSkyRenderer.renderCelestials(poseStack, sunAngle, rainBrightness, starBrightness, this.lotr$sunriseColor);
            lotr$aurora(sunAngle);
            ci.cancel();
        }
    }

    @Inject(method = "renderSunMoonAndStars", at = @At("TAIL"))
    private void lotr$auroraAfterVanillaSky(PoseStack poseStack, float sunAngle, float moonAngle, float starAngle, MoonPhase moonPhase,
                                            float rainBrightness, float starBrightness, CallbackInfo ci) {
        lotr$aurora(sunAngle);
    }

    /** LOTRTickHandlerClient.onRenderWorldLast: the northern lights, behind everything ("Aurora"). */
    @Unique
    private static void lotr$aurora(float sunAngle) {
        Minecraft mc = Minecraft.getInstance();
        if (LOTRAuroraRenderer.isEnabled() && mc.level != null) {
            float partialTick = mc.getDeltaTracker().getGameTimeDeltaPartialTick(false);
            float celestialAngle = sunAngle / ((float) Math.PI * 2.0f);
            LOTRAuroraRenderer.render(LOTREnvironmentClient.sunBrightness(mc.level, celestialAngle, partialTick),
                    mc.level.getRainLevel(partialTick), partialTick);
        }
    }
}
