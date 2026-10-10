package net.blueskiez77.lord_of_the_rings__middle_earth.client;

import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRSounds;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.elf.LOTRElfEntity;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;

import org.jspecify.annotations.Nullable;

/**
 * LOTRAmbience's jazz: once a second, if a jazz elf within sixteen blocks is playing a solo, every
 * other sound stops and its music plays where it stands, the now-playing line naming it; no other
 * music plays over it. It stops when the elf dies or is gone, or when the track ends.
 */
public final class LOTRJazzMusic {

    private static @Nullable SoundInstance playingJazzMusic;
    private static int jazzPlayerID;

    private LOTRJazzMusic() {
    }

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(LOTRJazzMusic::tick);
    }

    private static void tick(Minecraft mc) {
        if (mc.player == null || mc.level == null) {
            playingJazzMusic = null;
            return;
        }
        if (playingJazzMusic == null) {
            if (mc.player.tickCount % 20 == 0) {
                double range = 16.0;
                List<LOTRElfEntity> elves = mc.level.getEntitiesOfClass(LOTRElfEntity.class,
                        mc.player.getBoundingBox().inflate(range), elf -> elf.isAlive() && elf.isJazz() && elf.isSolo());
                if (!elves.isEmpty()) {
                    LOTRElfEntity elf = elves.getFirst();
                    mc.getSoundManager().stop();
                    jazzPlayerID = elf.getId();
                    SoundInstance music = new SimpleSoundInstance(LOTRSounds.MUSIC_JAZZ_ELF, SoundSource.RECORDS, 1.0f, 1.0f,
                            SoundInstance.createUnseededRandom(), elf.getX(), elf.getY(), elf.getZ());
                    mc.getSoundManager().play(music);
                    playingJazzMusic = music;
                    mc.gui.hud.setNowPlaying(Component.literal("The Galadhon Groovers - Funky Villagers"));
                }
            }
            return;
        }
        // onPlaySound: no music over the jazz.
        mc.getMusicManager().stopPlaying();
        Entity player = mc.level.getEntity(jazzPlayerID);
        if (player == null || !player.isAlive()) {
            mc.getSoundManager().stop(playingJazzMusic);
            playingJazzMusic = null;
        } else if (!mc.getSoundManager().isActive(playingJazzMusic)) {
            playingJazzMusic = null;
        }
    }
}
