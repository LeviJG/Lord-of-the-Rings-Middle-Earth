package net.blueskiez77.lord_of_the_rings__middle_earth.client.sound;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRDimension;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRWorldGen;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRMusicRegion;

import net.fabricmc.fabric.api.resource.v1.ResourceLoader;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

import org.jspecify.annotations.Nullable;

/**
 * LOTRMusic: the music packs -- .zip files in ".minecraft/lotrmusic", each with a "music.json"
 * naming its tracks (in "assets/lotrmusic") and the regions, subregions and times they play in --
 * loaded as the resources load, with a readme of the regions and categories written beside them.
 * While there are tracks, vanilla's music is kept silent in Middle-earth and Utumno, and on the
 * menu when there are menu tracks (LOTRMusicTicker plays them).
 *
 * <p>The original made each track a registered sound read through a resource domain of its own;
 * here each track is a sound of its own (LOTRMusicTrack) whose stream is opened from its pack
 * (openTrack, from SoundBufferLibrary), so a pack's filenames need not be valid ids.
 */
public final class LOTRMusic {

    public static final String JSON_FILENAME = "music.json";
    public static final String MUSIC_RESOURCE_PATH = "lotrmusic";
    private static final List<LOTRMusicTrack> allTracks = new ArrayList<>();
    private static final Map<LOTRMusicRegion.Sub, LOTRRegionTrackPool> regionTracks = new HashMap<>();
    /** Each track's stream: its pack and entry, by the path of its sound ("sounds/track_N.ogg"). */
    private static final Map<String, TrackSource> trackSources = new HashMap<>();
    private static final List<ZipFile> openPacks = new ArrayList<>();
    private static boolean initSubregions;
    private static File musicDir;

    private record TrackSource(ZipFile zip, ZipEntry entry) {
    }

    private LOTRMusic() {
    }

    public static void init() {
        ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloadListener(
                Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "music"),
                (ResourceManagerReloadListener) LOTRMusic::loadMusicPacks);
        LOTRMusicTicker.init();
    }

    private static void addTrackToRegions(LOTRMusicTrack track) {
        allTracks.add(track);
        for (LOTRMusicRegion region : track.getAllRegions()) {
            if (region.hasNoSubregions()) {
                getTracksForRegion(region, null).addTrack(track);
                continue;
            }
            for (String sub : track.getRegionInfo(region).getSubregions()) {
                LOTRRegionTrackPool pool = getTracksForRegion(region, sub);
                if (pool != null) {
                    pool.addTrack(track);
                }
            }
        }
    }

    public static @Nullable LOTRRegionTrackPool getTracksForRegion(LOTRMusicRegion region, @Nullable String sub) {
        if (region.hasSubregion(sub) || region.hasNoSubregions() && sub == null) {
            return regionTracks.computeIfAbsent(region.getSubregion(sub), key -> new LOTRRegionTrackPool(region));
        }
        LOTRMod.LOGGER.warn("LOTRMusic: No subregion {} for region {}!", sub, region.regionName);
        return null;
    }

    public static boolean hasTracks() {
        return !allTracks.isEmpty();
    }

    public static boolean isLOTRDimension() {
        Minecraft mc = Minecraft.getInstance();
        return mc.player != null && mc.level != null
                && (mc.level.dimension() == LOTRWorldGen.MIDDLE_EARTH || mc.level.dimension() == LOTRWorldGen.UTUMNO);
    }

    /** isMenuMusic: vanilla's situational music is the menu's -- no player in a world. */
    public static boolean isMenuMusic() {
        return Minecraft.getInstance().player == null;
    }

    /** onPlaySound: whether vanilla's music keeps quiet for the mod's. */
    public static boolean silencesVanillaMusic() {
        if (allTracks.isEmpty()) {
            return false;
        }
        if (isLOTRDimension()) {
            return true;
        }
        LOTRRegionTrackPool menu = getTracksForRegion(LOTRMusicRegion.MENU, null);
        return isMenuMusic() && menu != null && !menu.isEmpty();
    }

    /** A track's stream, from its pack, for the sound engine; null for any other sound. */
    public static @Nullable InputStream openTrack(Identifier location) throws IOException {
        if (!MUSIC_RESOURCE_PATH.equals(location.getNamespace())) {
            return null;
        }
        TrackSource source;
        synchronized (trackSources) {
            source = trackSources.get(location.getPath());
        }
        if (source == null) {
            throw new IOException("No LOTR music track " + location);
        }
        return source.zip().getInputStream(source.entry());
    }

    private static void loadMusicPacks(ResourceManager resourceManager) {
        Minecraft mc = Minecraft.getInstance();
        musicDir = new File(mc.gameDirectory, MUSIC_RESOURCE_PATH);
        if (!musicDir.exists()) {
            musicDir.mkdirs();
        }
        LOTRMusicTicker.stopCurrentTrack();
        allTracks.clear();
        regionTracks.clear();
        synchronized (trackSources) {
            trackSources.clear();
            for (ZipFile zip : openPacks) {
                try {
                    zip.close();
                } catch (IOException ignored) {
                }
            }
            openPacks.clear();
        }
        if (!initSubregions) {
            for (LOTRDimension dim : LOTRDimension.values()) {
                for (LOTRBiome biome : dim.biomeList) {
                    if (biome != null) {
                        biome.getBiomeMusic();
                    }
                }
            }
            initSubregions = true;
        }
        File[] files = musicDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (!file.isFile() || !file.getName().endsWith(".zip")) {
                    continue;
                }
                try {
                    loadMusicPack(new ZipFile(file));
                } catch (Exception e) {
                    LOTRMod.LOGGER.warn("LOTRMusic: Failed to load music pack {}!", file.getName(), e);
                }
            }
        }
        try {
            generateReadme(resourceManager);
        } catch (IOException e) {
            LOTRMod.LOGGER.warn("LOTRMusic: Failed to write the music readme", e);
        }
    }

    private static void loadMusicPack(ZipFile zip) throws IOException {
        ZipEntry entry = zip.getEntry(JSON_FILENAME);
        if (entry == null) {
            zip.close();
            return;
        }
        List<LOTRMusicTrack> packTracks = new ArrayList<>();
        JsonObject root;
        try (InputStreamReader reader = new InputStreamReader(zip.getInputStream(entry), StandardCharsets.UTF_8)) {
            root = JsonParser.parseReader(reader).getAsJsonObject();
        }
        for (JsonElement e : root.get("tracks").getAsJsonArray()) {
            JsonObject trackData = e.getAsJsonObject();
            String filename = trackData.get("file").getAsString();
            ZipEntry trackEntry = zip.getEntry("assets/lotrmusic/" + filename);
            if (trackEntry == null) {
                LOTRMod.LOGGER.warn("LOTRMusic: Track {} in pack {} does not exist!", filename, zip.getName());
                continue;
            }
            Identifier id = Identifier.fromNamespaceAndPath(MUSIC_RESOURCE_PATH, "track_" + trackSources.size());
            LOTRMusicTrack track = new LOTRMusicTrack(id, filename);
            if (trackData.has("title")) {
                track.setTitle(trackData.get("title").getAsString());
            }
            for (JsonElement r : trackData.get("regions").getAsJsonArray()) {
                JsonObject regionData = r.getAsJsonObject();
                String regionName = regionData.get("name").getAsString();
                LOTRMusicRegion region = null;
                boolean allRegions = false;
                if (LOTRMusicRegion.ALL_REGION_CODE.equalsIgnoreCase(regionName)) {
                    allRegions = true;
                } else {
                    region = LOTRMusicRegion.forName(regionName);
                    if (region == null) {
                        LOTRMod.LOGGER.warn("LOTRMusic: No region named {}!", regionName);
                        continue;
                    }
                }
                Collection<String> subregionNames = new ArrayList<>();
                if (region != null && regionData.has("sub")) {
                    for (JsonElement s : regionData.get("sub").getAsJsonArray()) {
                        String sub = s.getAsString();
                        if (region.hasSubregion(sub)) {
                            subregionNames.add(sub);
                        } else {
                            LOTRMod.LOGGER.warn("LOTRMusic: No subregion {} for region {}!", sub, region.regionName);
                        }
                    }
                }
                Collection<LOTRMusicCategory> regionCategories = new ArrayList<>();
                if (region != null && regionData.has("categories")) {
                    for (JsonElement cat : regionData.get("categories").getAsJsonArray()) {
                        LOTRMusicCategory category = LOTRMusicCategory.forName(cat.getAsString());
                        if (category != null) {
                            regionCategories.add(category);
                        } else {
                            LOTRMod.LOGGER.warn("LOTRMusic: No category named {}!", cat.getAsString());
                        }
                    }
                }
                double weight = regionData.has("weight") ? regionData.get("weight").getAsDouble() : -1.0;
                Collection<LOTRMusicRegion> regionsAdd = allRegions ? Arrays.asList(LOTRMusicRegion.values()) : List.of(region);
                for (LOTRMusicRegion reg : regionsAdd) {
                    LOTRMusicTrack.RegionInfo regInfo = track.createRegionInfo(reg);
                    if (weight >= 0.0) {
                        regInfo.setWeight(weight);
                    }
                    if (subregionNames.isEmpty()) {
                        regInfo.addAllSubregions();
                    } else {
                        subregionNames.forEach(regInfo::addSubregion);
                    }
                    if (regionCategories.isEmpty()) {
                        regInfo.addAllCategories();
                    } else {
                        regionCategories.forEach(regInfo::addCategory);
                    }
                }
            }
            if (trackData.has("authors")) {
                for (JsonElement a : trackData.get("authors").getAsJsonArray()) {
                    track.addAuthor(a.getAsString());
                }
            }
            synchronized (trackSources) {
                trackSources.put("sounds/" + id.getPath() + ".ogg", new TrackSource(zip, trackEntry));
            }
            addTrackToRegions(track);
            packTracks.add(track);
        }
        synchronized (trackSources) {
            openPacks.add(zip);
        }
        LOTRMod.LOGGER.info("LOTRMusic: Successfully loaded music pack {} with {} tracks", zip.getName(), packTracks.size());
    }

    /** generateReadme: the template, with the regions (and their subregions) and categories filled in. */
    private static void generateReadme(ResourceManager resourceManager) throws IOException {
        File readme = new File(musicDir, "readme.txt");
        Identifier template = Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "music/readme.txt");
        try (PrintStream writer = new PrintStream(Files.newOutputStream(readme.toPath()), true, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(new InputStreamReader(resourceManager.open(template), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty() && line.charAt(0) == '﻿') {
                    line = line.substring(1);
                }
                if ("#REGIONS#".equals(line)) {
                    writer.println(LOTRMusicRegion.ALL_REGION_CODE);
                    for (LOTRMusicRegion region : LOTRMusicRegion.values()) {
                        StringBuilder regionString = new StringBuilder(region.regionName);
                        List<String> subregions = region.getAllSubregions();
                        if (!subregions.isEmpty()) {
                            regionString.append(": {").append(String.join(", ", subregions)).append("}");
                        }
                        writer.println(regionString);
                    }
                    continue;
                }
                if ("#CATEGORIES#".equals(line)) {
                    for (LOTRMusicCategory category : LOTRMusicCategory.values()) {
                        writer.println(category.categoryName);
                    }
                    continue;
                }
                writer.println(line);
            }
        }
    }
}
