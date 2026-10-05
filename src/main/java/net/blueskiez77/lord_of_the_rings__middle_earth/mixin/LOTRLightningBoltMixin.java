package net.blueskiez77.lord_of_the_rings__middle_earth.mixin;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.config.LOTRConfig;

import net.minecraft.world.entity.LightningBolt;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** LOTRReplacedMethods.Lightning.doSetBlock: with "Disable lightning grief", a bolt sets no fire. */
@Mixin(LightningBolt.class)
abstract class LOTRLightningBoltMixin {

    @Inject(method = "spawnFire", at = @At("HEAD"), cancellable = true)
    private void lotr$noFire(int additionalSources, CallbackInfo ci) {
        if (LOTRConfig.disableLightningGrief) {
            ci.cancel();
        }
    }
}
