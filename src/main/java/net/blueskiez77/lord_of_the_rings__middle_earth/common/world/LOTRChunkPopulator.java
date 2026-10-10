package net.blueskiez77.lord_of_the_rings__middle_earth.common.world;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import com.mojang.serialization.Codec;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;

/**
 * Middle-earth's decoration, run as 1.7.10 ran populate: a chunk is decorated once it and its
 * neighbours to the east, south and south-east are all loaded, the decoration spreading over the
 * four of them -- and, for the structures, as far as they reach. It runs in the level itself, on
 * the server's thread, so a structure may read and build into any chunk, as the original's did;
 * the chunk generator's own feature step cannot reach past one chunk, and reading further crashes.
 *
 * <p>Whether a chunk has been decorated is kept with it ("TerrainPopulated" in 1.7.10's chunks).
 */
public final class LOTRChunkPopulator {

    public static final AttachmentType<Boolean> POPULATED = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "populated"),
            builder -> builder.persistent(Codec.BOOL));

    /** The chunks to look at, per level: those loaded since, and the three before each. */
    private static final Map<ServerLevel, Set<Long>> PENDING = new WeakHashMap<>();

    private LOTRChunkPopulator() {
    }

    public static void init() {
        ServerChunkEvents.CHUNK_LOAD.register(LOTRChunkPopulator::onChunkLoad);
        ServerTickEvents.END_LEVEL_TICK.register(LOTRChunkPopulator::populatePending);
    }

    private static void onChunkLoad(ServerLevel level, LevelChunk chunk, boolean generated) {
        if (!(level.getChunkSource().getGenerator() instanceof LOTRChunkGenerator)) {
            return;
        }
        int x = chunk.getPos().x();
        int z = chunk.getPos().z();
        Set<Long> pending = PENDING.computeIfAbsent(level, l -> new LinkedHashSet<>());
        // Chunk.populateChunk: this chunk, and those to the west, north and north-west, whose
        // squares this one may have completed.
        pending.add(ChunkPos.pack(x, z));
        pending.add(ChunkPos.pack(x - 1, z));
        pending.add(ChunkPos.pack(x, z - 1));
        pending.add(ChunkPos.pack(x - 1, z - 1));
    }

    private static void populatePending(ServerLevel level) {
        Set<Long> pending = PENDING.get(level);
        if (pending == null || pending.isEmpty()
                || !(level.getChunkSource().getGenerator() instanceof LOTRChunkGenerator generator)) {
            return;
        }
        // Decorating may load more chunks, which add to the set; those wait for the next tick.
        Long[] chunks = pending.toArray(new Long[0]);
        pending.clear();
        for (long packed : chunks) {
            int x = ChunkPos.getX(packed);
            int z = ChunkPos.getZ(packed);
            LevelChunk chunk = level.getChunkSource().getChunkNow(x, z);
            if (chunk == null || chunk.getAttachedOrElse(POPULATED, false)) {
                continue;
            }
            if (level.getChunkSource().getChunkNow(x + 1, z) == null || level.getChunkSource().getChunkNow(x, z + 1) == null
                    || level.getChunkSource().getChunkNow(x + 1, z + 1) == null) {
                continue;
            }
            chunk.setAttached(POPULATED, true);
            generator.populate(level, x, z);
        }
    }
}
