package net.blueskiez77.lord_of_the_rings__middle_earth.mixin.client;

import java.io.IOException;
import java.io.InputStream;

import net.blueskiez77.lord_of_the_rings__middle_earth.client.sound.LOTRMusic;

import net.minecraft.client.sounds.SoundBufferLibrary;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceProvider;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/** LOTRMusic: a music pack's track streams from its pack, not the resources. */
@Mixin(SoundBufferLibrary.class)
abstract class LOTRSoundBufferLibraryMixin {

    @Redirect(method = "lambda$getStream$0", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/server/packs/resources/ResourceProvider;open(Lnet/minecraft/resources/Identifier;)Ljava/io/InputStream;"))
    private InputStream lotr$openMusicTrack(ResourceProvider provider, Identifier location) throws IOException {
        InputStream track = LOTRMusic.openTrack(location);
        return track != null ? track : provider.open(location);
    }
}
