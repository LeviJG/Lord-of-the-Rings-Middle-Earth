package net.blueskiez77.lord_of_the_rings__middle_earth.mixin.client;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.sound.LOTRMusic;

import net.minecraft.client.sounds.MusicManager;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** LOTRMusic.onPlaySound: vanilla's music kept quiet where the mod's plays. */
@Mixin(MusicManager.class)
abstract class LOTRMusicManagerMixin {

    @Shadow
    public abstract void stopPlaying();

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void lotr$silenceForLOTRMusic(CallbackInfo ci) {
        if (LOTRMusic.silencesVanillaMusic()) {
            stopPlaying();
            ci.cancel();
        }
    }
}
