package net.blueskiez77.lord_of_the_rings__middle_earth.common;

import java.util.HashMap;
import java.util.Map;

import com.mojang.serialization.Codec;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRCreatureType;

import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

/**
 * LOTRSpawnDamping: how much each kind of spawning's cap shrinks per player beyond the first, so a
 * crowded server is not overrun -- the NPCs' ("lotr_npc") and each creature type's, set by
 * {@code /spawnDamping} and kept with the world (the original's spawn_damping.dat).
 */
public final class LOTRSpawnDamping extends SavedData {
    public static final String TYPE_NPC = "lotr_npc";

    private static final Codec<LOTRSpawnDamping> CODEC = Codec.unboundedMap(Codec.STRING, Codec.FLOAT)
            .xmap(LOTRSpawnDamping::new, d -> d.spawnDamping);
    private static final SavedDataType<LOTRSpawnDamping> TYPE = new SavedDataType<>(
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "spawn_damping"), LOTRSpawnDamping::new, CODEC, null);

    private final Map<String, Float> spawnDamping;

    private LOTRSpawnDamping() {
        this.spawnDamping = new HashMap<>();
    }

    private LOTRSpawnDamping(Map<String, Float> map) {
        this.spawnDamping = new HashMap<>(map);
    }

    private static LOTRSpawnDamping get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(TYPE);
    }

    public static int getBaseSpawnCapForInfo(String type, Level level) {
        if (type.equals(TYPE_NPC)) {
            return LOTRDimension.getCurrentDimensionWithFallback(level).spawnCap;
        }
        LOTRCreatureType creatureType = LOTRCreatureType.forName(type);
        return creatureType == null ? 0 : creatureType.maxNumberOfCreature;
    }

    public static int getCreatureSpawnCap(LOTRCreatureType type, Level level) {
        return getSpawnCap(type.typeName, type.maxNumberOfCreature, level);
    }

    public static int getNPCSpawnCap(Level level) {
        return getSpawnCap(TYPE_NPC, LOTRDimension.getCurrentDimensionWithFallback(level).spawnCap, level);
    }

    public static int getSpawnCap(MinecraftServer server, String type, int baseCap, int players) {
        float damp = getSpawnDamping(server, type);
        float dampFraction = (players - 1) * damp;
        dampFraction = Mth.clamp(dampFraction, 0.0f, 1.0f);
        float stationaryPointValue = 0.5f + damp / 2.0f;
        if (dampFraction > stationaryPointValue) {
            dampFraction = stationaryPointValue;
        }
        int capPerPlayer = Math.round(baseCap * (1.0f - dampFraction));
        return Math.max(capPerPlayer, 1);
    }

    public static int getSpawnCap(String type, int baseCap, Level level) {
        return getSpawnCap(level.getServer(), type, baseCap, level.players().size());
    }

    public static float getSpawnDamping(MinecraftServer server, String type) {
        return get(server).spawnDamping.getOrDefault(type, 0.0f);
    }

    public static void resetAll(MinecraftServer server) {
        LOTRSpawnDamping data = get(server);
        data.spawnDamping.clear();
        data.setDirty();
    }

    public static void setSpawnDamping(MinecraftServer server, String type, float damping) {
        LOTRSpawnDamping data = get(server);
        data.spawnDamping.put(type, damping);
        data.setDirty();
    }
}
