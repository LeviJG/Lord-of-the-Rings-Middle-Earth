package net.blueskiez77.lord_of_the_rings__middle_earth.mixin.client;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.LOTRDrunkCamera;

import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** The drunken view drift, once a frame alongside the mouse's turn. */
@Mixin(MouseHandler.class)
abstract class LOTRMouseHandlerMixin {

    @Inject(method = "handleAccumulatedMovement", at = @At("HEAD"))
    private void lotr$drunkCamera(CallbackInfo ci) {
        LOTRDrunkCamera.frame(Minecraft.getInstance());
    }
}
