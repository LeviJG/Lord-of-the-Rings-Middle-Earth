package net.blueskiez77.lord_of_the_rings__middle_earth.mixin.client;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.world.LOTRCloudRenderer;

import net.minecraft.client.CloudStatus;
import net.minecraft.client.renderer.CloudRenderer;
import net.minecraft.world.phys.Vec3;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** LOTRWorldProvider.getCloudRenderer ("Cloud range" above 0): Middle-earth's own clouds (LOTRCloudRenderer). */
@Mixin(CloudRenderer.class)
abstract class LOTRCloudRendererMixin {

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void lotr$middleEarthClouds(int color, CloudStatus cloudStatus, float bottomY, int range, Vec3 cameraPosition, long gameTime,
                                        float partialTicks, CallbackInfo ci) {
        if (LOTRCloudRenderer.isEnabled()) {
            LOTRCloudRenderer.render(color, bottomY, cameraPosition, partialTicks);
            ci.cancel();
        }
    }
}
