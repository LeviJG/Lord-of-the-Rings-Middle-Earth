package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.mapgen.structure;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Supplier;

import com.mojang.serialization.Codec;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRWorldChunkManager;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

/**
 * 1.7.10's MapGenStructure: a kind of structure found on chunks about each one generated (MapGenBase's
 * range of eight, each chunk with its own seeded random) and built a piece at a time as each chunk
 * it reaches is populated. The structures found are kept with the level (MapGenStructureData), so
 * what a piece settled on when first built -- its height -- holds for the rest of it.
 */
public abstract class LOTRMapGenStructure {
    public static final int RANGE = 8;

    private final Map<ServerLevel, Map<Long, LOTRStructureStart>> structureMaps = new WeakHashMap<>();
    private final SavedDataType<Data> dataType;

    protected LOTRMapGenStructure(String name) {
        this.dataType = new SavedDataType<>(Identifier.fromNamespaceAndPath(LOTRMod.NAMESPACE, "mapgen_" + name),
                Data::new, Data.CODEC, null);
    }

    /** The pieces it can be made of, by their saved names. */
    protected abstract Map<String, Supplier<? extends LOTRStructureComponent>> componentTypes();

    protected abstract boolean canSpawnStructureAtCoords(long worldSeed, LOTRWorldChunkManager chunkManager, RandomSource rand, int i, int k);

    protected abstract LOTRStructureStart getStructureStart(LOTRWorldChunkManager chunkManager, RandomSource rand, int i, int k);

    private Map<Long, LOTRStructureStart> structureMap(ServerLevel level) {
        return this.structureMaps.computeIfAbsent(level, l -> {
            Map<Long, LOTRStructureStart> map = new HashMap<>();
            Data data = level.getDataStorage().computeIfAbsent(this.dataType);
            for (Map.Entry<String, CompoundTag> e : data.starts.entrySet()) {
                map.put(Long.parseLong(e.getKey()), LOTRStructureStart.load(e.getValue(), componentTypes()));
            }
            return map;
        });
    }

    private void saveStart(ServerLevel level, long key, LOTRStructureStart start) {
        Data data = level.getDataStorage().computeIfAbsent(this.dataType);
        data.starts.put(Long.toString(key), start.save());
        data.setDirty();
    }

    /** MapGenBase.generate: every chunk within range of this one looked at for a structure starting there. */
    private void findStarts(ServerLevel level, LOTRWorldChunkManager chunkManager, int chunkX, int chunkZ) {
        Map<Long, LOTRStructureStart> map = structureMap(level);
        long seed = level.getSeed();
        RandomSource rand = new LegacyRandomSource(seed);
        long j = rand.nextLong();
        long k = rand.nextLong();
        for (int l1 = chunkX - RANGE; l1 <= chunkX + RANGE; ++l1) {
            for (int i2 = chunkZ - RANGE; i2 <= chunkZ + RANGE; ++i2) {
                long key = ChunkPos.pack(l1, i2);
                if (map.containsKey(key)) {
                    continue;
                }
                rand.setSeed(l1 * j ^ i2 * k ^ seed);
                rand.nextInt();
                if (canSpawnStructureAtCoords(seed, chunkManager, rand, l1, i2)) {
                    LOTRStructureStart start = getStructureStart(chunkManager, rand, l1, i2);
                    map.put(key, start);
                    saveStart(level, key, start);
                }
            }
        }
    }

    /** generateStructuresInChunk: each structure reaching the area this chunk populates builds its part of it. */
    public boolean generateStructuresInChunk(ServerLevel level, LOTRWorldChunkManager chunkManager, RandomSource rand, int chunkX, int chunkZ) {
        findStarts(level, chunkManager, chunkX, chunkZ);
        int k = (chunkX << 4) + 8;
        int l = (chunkZ << 4) + 8;
        boolean flag = false;
        for (Map.Entry<Long, LOTRStructureStart> e : structureMap(level).entrySet()) {
            LOTRStructureStart start = e.getValue();
            if (start.isSizeableStructure() && start.boundingBox.intersectsWith(k, l, k + 15, l + 15)) {
                start.generateStructure(level, rand, new LOTRStructureBoundingBox(k, l, k + 15, l + 15));
                flag = true;
                saveStart(level, e.getKey(), start);
            }
        }
        return flag;
    }

    /** MapGenStructureData: the structures found, by their starting chunk. */
    public static final class Data extends SavedData {
        static final Codec<Data> CODEC = Codec.unboundedMap(Codec.STRING, CompoundTag.CODEC)
                .xmap(Data::new, d -> d.starts);

        final Map<String, CompoundTag> starts;

        Data() {
            this.starts = new HashMap<>();
        }

        Data(Map<String, CompoundTag> starts) {
            this.starts = new HashMap<>(starts);
        }
    }
}
