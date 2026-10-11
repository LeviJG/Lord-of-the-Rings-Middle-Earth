package net.blueskiez77.lord_of_the_rings__middle_earth.mixin.client;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.world.LOTRUtumnoClient;

import net.minecraft.client.Camera;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/** EntityRenderer.camRoll: the view rolled about its own line of sight (Utumno's return portal). */
@Mixin(Camera.class)
abstract class LOTRCameraMixin {
    @ModifyArg(method = "setRotation(FF)V", at = @At(value = "INVOKE",
            target = "Lorg/joml/Quaternionf;rotationYXZ(FFF)Lorg/joml/Quaternionf;"), index = 2)
    private float lotr$roll(float roll) {
        return roll + LOTRUtumnoClient.getCameraRoll() * (float) (Math.PI / 180.0);
    }
}
