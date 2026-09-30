package net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

/**
 * The speech banks and name banks are plain text files the original read
 * straight out of its own jar (not through resource packs), on the server as
 * much as the client. This does the same through the mod container: every
 * {@code .txt} (or other extension) under a folder of the mod's resources, keyed by its path below
 * that folder without the extension, one entry per line (a UTF-8 byte-order
 * mark stripped, as BOMInputStream did).
 */
public final class LOTRModTextFiles {

    private LOTRModTextFiles() {
    }

    static Map<String, List<String>> readAll(String folder) {
        return readAll(folder, ".txt");
    }

    public static Map<String, List<String>> readAll(String folder, String extension) {
        Map<String, List<String>> files = new LinkedHashMap<>();
        ModContainer mod = FabricLoader.getInstance().getModContainer("lord_of_the_rings_-_middle_earth").orElse(null);
        if (mod == null) {
            LOTRMod.LOGGER.error("LOTR: could not find the mod container to read {}", folder);
            return files;
        }
        for (Path root : mod.getRootPaths()) {
            Path dir = root.resolve(folder);
            if (!Files.isDirectory(dir)) {
                continue;
            }
            try (Stream<Path> walk = Files.walk(dir)) {
                for (Path file : (Iterable<Path>) walk::iterator) {
                    String name = dir.relativize(file).toString().replace(file.getFileSystem().getSeparator(), "/");
                    if (!Files.isRegularFile(file) || !name.endsWith(extension)) {
                        continue;
                    }
                    try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
                        List<String> lines = new ArrayList<>();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            if (lines.isEmpty() && !line.isEmpty() && line.charAt(0) == '﻿') {
                                line = line.substring(1);
                            }
                            lines.add(line);
                        }
                        files.put(name.substring(0, name.length() - extension.length()), lines);
                    } catch (IOException e) {
                        LOTRMod.LOGGER.error("LOTR: failed to read {}", file, e);
                    }
                }
            } catch (IOException e) {
                LOTRMod.LOGGER.error("LOTR: failed to list {}", dir, e);
            }
        }
        return files;
    }
}
