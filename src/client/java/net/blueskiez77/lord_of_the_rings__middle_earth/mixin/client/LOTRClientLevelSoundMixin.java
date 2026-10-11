package net.blueskiez77.lord_of_the_rings__middle_earth.mixin.client;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.world.LOTRAmbienceClient;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.sounds.SoundEvent;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/** LOTRAmbience.onPlaySound ("New weather"): Middle-earth's own rain and thunder (LOTRAmbienceClient). */
@Mixin(ClientLevel.class)
abstract class LOTRClientLevelSoundMixin {

    @ModifyVariable(method = "playSound(DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FFZJ)V", at = @At("HEAD"),
            argsOnly = true)
    private SoundEvent lotr$middleEarthWeatherSound(SoundEvent sound) {
        return LOTRAmbienceClient.replaceSound(sound);
    }
}
