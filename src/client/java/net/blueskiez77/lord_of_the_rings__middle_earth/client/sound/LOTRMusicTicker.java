package net.blueskiez77.lord_of_the_rings__middle_earth.client.sound;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.config.LOTRConfig;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiomes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRMusicRegion;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

import org.jspecify.annotations.Nullable;

/**
 * LOTRMusicTicker: a track of the region the player is in (by day, by night or underground) after a
 * wait (the config's intervals), one at a time; on the menu, the menu's. With "Display music track"
 * on, a new track's title and authors fade in and out in the corner (LOTRTickHandlerClient).
 */
public final class LOTRMusicTicker {

    private static final RandomSource MUSIC_RAND = RandomSource.create();
    private static @Nullable LOTRMusicTrack currentTrack;
    private static boolean wasPlayingMenu = true;
    private static int timing = 100;
    private static @Nullable LOTRMusicTrack lastTrack;
    private static int musicTrackTick;

    private LOTRMusicTicker() {
    }

    static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            update(MUSIC_RAND);
            if (LOTRConfig.displayMusicTrack) {
                if (currentTrack != lastTrack) {
                    lastTrack = currentTrack;
                    musicTrackTick = 200;
                }
                if (lastTrack != null && musicTrackTick > 0) {
                    --musicTrackTick;
                }
            }
        });
        HudElementRegistry.addLast(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "music_track"), LOTRMusicTicker::renderNowPlaying);
    }

    static void stopCurrentTrack() {
        if (currentTrack != null) {
            Minecraft.getInstance().getSoundManager().stop(currentTrack);
            currentTrack = null;
        }
    }

    private static @Nullable LOTRMusicCategory getCurrentCategory(Minecraft mc) {
        ClientLevel level = mc.level;
        LocalPlayer player = mc.player;
        if (level == null || player == null) {
            return null;
        }
        BlockPos pos = BlockPos.containing(player.getX(), player.getBoundingBox().minY, player.getZ());
        if (LOTRMusicCategory.isCave(level, pos)) {
            return LOTRMusicCategory.CAVE;
        }
        return LOTRMusicCategory.isDay(level) ? LOTRMusicCategory.DAY : LOTRMusicCategory.NIGHT;
    }

    private static LOTRMusicRegion.@Nullable Sub getCurrentRegion(Minecraft mc) {
        if (LOTRMusic.isMenuMusic()) {
            return LOTRMusicRegion.MENU.getWithoutSub();
        }
        if (LOTRMusic.isLOTRDimension()) {
            int i = Mth.floor(mc.player.getX());
            int k = Mth.floor(mc.player.getZ());
            if (mc.level.hasChunk(i >> 4, k >> 4)) {
                LOTRBiome biome = LOTRBiomes.of(mc.level.getBiome(new BlockPos(i, Mth.floor(mc.player.getY()), k)));
                if (biome != null) {
                    return biome.getBiomeMusic();
                }
            }
        }
        return null;
    }

    private static @Nullable LOTRMusicTrack getNewTrack(Minecraft mc, RandomSource rand) {
        LOTRMusicRegion.Sub regionSub = getCurrentRegion(mc);
        if (regionSub == null) {
            return null;
        }
        LOTRMusicCategory category = getCurrentCategory(mc);
        LOTRMusicRegion region = regionSub.region();
        Predicate<LOTRMusicTrack> filter = category != null ? LOTRRegionTrackPool.forRegionAndCategory(region, category) : track -> true;
        LOTRRegionTrackPool trackPool = LOTRMusic.getTracksForRegion(region, regionSub.subregion());
        return trackPool == null ? null : trackPool.getRandomTrack(rand, filter);
    }

    private static void resetTiming(RandomSource rand) {
        timing = LOTRMusic.isMenuMusic()
                ? Mth.randomBetweenInclusive(rand, LOTRConfig.musicIntervalMenuMin * 20, LOTRConfig.musicIntervalMenuMax * 20)
                : Mth.randomBetweenInclusive(rand, LOTRConfig.musicIntervalMin * 20, LOTRConfig.musicIntervalMax * 20);
    }

    private static void update(RandomSource rand) {
        Minecraft mc = Minecraft.getInstance();
        boolean noMusic = mc.options.getSoundSourceVolume(SoundSource.MUSIC) <= 0.0f;
        boolean menu = LOTRMusic.isMenuMusic();
        if (wasPlayingMenu != menu) {
            stopCurrentTrack();
            wasPlayingMenu = menu;
            timing = 100;
        }
        if (currentTrack != null) {
            if (noMusic) {
                mc.getSoundManager().stop(currentTrack);
            }
            if (!mc.getSoundManager().isActive(currentTrack)) {
                currentTrack = null;
                resetTiming(rand);
            }
        }
        if (!noMusic) {
            boolean update = menu || LOTRMusic.isLOTRDimension() && !mc.isPaused();
            if (update && currentTrack == null && --timing <= 0) {
                currentTrack = getNewTrack(mc, rand);
                if (currentTrack != null) {
                    wasPlayingMenu = menu;
                    mc.getSoundManager().play(currentTrack);
                    timing = Integer.MAX_VALUE;
                } else {
                    timing = 400;
                }
            }
        }
    }

    /** The track just begun: "Now playing:", its title and its authors, in the bottom right, fading. */
    private static void renderNowPlaying(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        LOTRMusicTrack track = lastTrack;
        if (!LOTRConfig.displayMusicTrack || mc.gui.screen() != null || track == null || musicTrackTick <= 0) {
            return;
        }
        List<String> lines = new ArrayList<>();
        lines.add(Component.translatable("lotr.music.nowPlaying").getString());
        lines.add(track.getTitle());
        if (!track.getAuthors().isEmpty()) {
            lines.add("(" + String.join(", ", track.getAuthors()) + ")");
        }
        int border = 20;
        int y = graphics.guiHeight() - border - lines.size() * mc.font.lineHeight;
        float alpha = 1.0f;
        if (musicTrackTick >= 140) {
            alpha = (200 - musicTrackTick) / 60.0f;
        } else if (musicTrackTick <= 60) {
            alpha = musicTrackTick / 60.0f;
        }
        int colour = ARGB.color(Mth.clamp(Math.round(alpha * 255.0f), 4, 255), 0xFFFFFF);
        for (String line : lines) {
            graphics.text(mc.font, line, graphics.guiWidth() - border - mc.font.width(line), y, colour, false);
            y += mc.font.lineHeight;
        }
    }
}
