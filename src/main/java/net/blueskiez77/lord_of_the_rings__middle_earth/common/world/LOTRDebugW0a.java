package net.blueskiez77.lord_of_the_rings__middle_earth.common.world;

import java.util.concurrent.atomic.AtomicInteger;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;

/**
 * TEMPORARY (W0a diagnosis): counts the stone, the mod's rocks and the ores in Middle-earth chunks at
 * each stage -- after fillFromNoise, before and after populate, and at unload -- and logs ("[LOTR W0a
 * stage]") the chunks found with little or none, a limited number of times per stage. Remove with
 * the hollow underground's fix.
 */
public final class LOTRDebugW0a {
    private static final AtomicInteger LOGGED = new AtomicInteger();
    /** What fillFromNoise left in each chunk: the chunk object, its sections array, and the rock-like count. */
    private static final java.util.Map<Long, String> FILLED = new java.util.concurrent.ConcurrentHashMap<>();

    public static void recordFill(ChunkAccess chunk) {
        FILLED.put(chunk.getPos().pack(), "chunk@" + System.identityHashCode(chunk) + " sections@"
                + System.identityHashCode(chunk.getSections()) + " section4@" + System.identityHashCode(chunk.getSections()[4])
                + " count " + count(chunk));
    }

    private LOTRDebugW0a() {
    }

    public static void init() {
        ServerChunkEvents.CHUNK_LOAD.register((level, chunk, generated) -> {
            if (level.getChunkSource().getGenerator() instanceof LOTRChunkGenerator) {
                check(generated ? "fullNew" : "fullLoaded", chunk, -1);
            }
        });
        ServerChunkEvents.CHUNK_UNLOAD.register((level, chunk) -> {
            if (level.getChunkSource().getGenerator() instanceof LOTRChunkGenerator) {
                check("unload", chunk, -1);
            }
        });
    }

    public static boolean isRockLike(BlockState state) {
        if (state.isAir()) {
            return false;
        }
        String path = BuiltInRegistries.BLOCK.getKey(state.getBlock()).getPath();
        return path.equals("stone") || path.endsWith("_rock") || path.endsWith("_ore") || path.startsWith("mordor_");
    }

    public static int count(ChunkAccess chunk) {
        int[] n = new int[1];
        for (LevelChunkSection section : chunk.getSections()) {
            if (section.hasOnlyAir()) {
                continue;
            }
            section.getStates().count((s, c) -> {
                if (isRockLike(s)) {
                    n[0] += c;
                }
            });
        }
        return n[0];
    }

    /** Logs the chunk if it holds under 500 rock-like blocks (expected: the terrain's count, or -1 if unknown). */
    public static void check(String stage, ChunkAccess chunk, int expected) {
        int n = count(chunk);
        boolean lost = expected >= 0 ? expected > 2000 && n < expected / 4 : n < 500;
        if (lost && LOGGED.incrementAndGet() <= 150) {
            LOTRMod.LOGGER.warn("[LOTR W0a stage] {} chunk {} ({}): {} rock-like blocks (terrain had {}), status {}, class {}, thread {}; now chunk@{} sections@{} section4@{}; at fill: {}",
                    stage, chunk.getPos(), chunk.getPos().getMiddleBlockPosition(64), n, expected,
                    chunk.getPersistedStatus(), chunk.getClass().getSimpleName(), Thread.currentThread().getName(),
                    System.identityHashCode(chunk), System.identityHashCode(chunk.getSections()),
                    System.identityHashCode(chunk.getSections()[4]), FILLED.get(chunk.getPos().pack()));
        }
    }

    /** The terrain array's own count. */
    public static int count(LOTRChunkTerrain terrain) {
        int n = 0;
        for (BlockState s : terrain.blocks) {
            if (isRockLike(s)) {
                ++n;
            }
        }
        return n;
    }
}
