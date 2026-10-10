package net.blueskiez77.lord_of_the_rings__middle_earth.common.world;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRDimension;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiomes;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;

import org.jspecify.annotations.Nullable;

/**
 * LOTRWorldChunkManager as a biome source: each four-block cell's biome from the genlayers at
 * that resolution, as LOTRChunkProvider shaped its terrain by. Recently asked-for 64-block
 * squares are kept, a few to a thread.
 */
public class LOTRBiomeSource extends BiomeSource {

    public static final MapCodec<LOTRBiomeSource> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            RegistryOps.retrieveGetter(Registries.BIOME)
    ).apply(i, LOTRBiomeSource::new));

    private static final int REGION = 16;
    private final HolderGetter<Biome> biomes;
    @SuppressWarnings("unchecked")
    private final Holder<Biome>[] byId = new Holder[256];
    private volatile @Nullable LOTRWorldChunkManager chunkManager;
    private final ThreadLocal<Map<Long, int[]>> cache = ThreadLocal.withInitial(() -> new LinkedHashMap<>(16, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<Long, int[]> eldest) {
            return size() > 64;
        }
    });

    public LOTRBiomeSource(HolderGetter<Biome> biomes) {
        this.biomes = biomes;
        for (LOTRBiome biome : LOTRDimension.MIDDLE_EARTH.biomeList) {
            if (biome != null) {
                this.byId[biome.biomeID] = biomes.getOrThrow(biome.key);
            }
        }
    }

    void init(LOTRWorldChunkManager manager) {
        this.chunkManager = manager;
        this.cache.remove();
    }

    @Override
    protected MapCodec<? extends BiomeSource> codec() {
        return CODEC;
    }

    @Override
    protected Stream<Holder<Biome>> collectPossibleBiomes() {
        return Stream.of(this.byId).filter(h -> h != null);
    }

    /** The biomes of an area of four-block cells, a row of {@code xSize} at a time. */
    public Holder<Biome>[] holders(int qx, int qz, int xSize, int zSize) {
        @SuppressWarnings("unchecked")
        Holder<Biome>[] out = new Holder[xSize * zSize];
        LOTRWorldChunkManager manager = this.chunkManager;
        if (manager == null) {
            java.util.Arrays.fill(out, this.byId[LOTRBiomes.OCEAN.biomeID]);
            return out;
        }
        LOTRBiome[] found = manager.getBiomesForGeneration(qx, qz, xSize, zSize);
        for (int l = 0; l < found.length; ++l) {
            out[l] = this.byId[found[l].biomeID];
        }
        return out;
    }

    @Override
    public Holder<Biome> getNoiseBiome(int qx, int qy, int qz, Climate.Sampler sampler) {
        LOTRWorldChunkManager manager = this.chunkManager;
        if (manager == null) {
            return this.byId[LOTRBiomes.OCEAN.biomeID];
        }
        int rx = Math.floorDiv(qx, REGION);
        int rz = Math.floorDiv(qz, REGION);
        long key = (long) rx << 32 | (rz & 0xFFFFFFFFL);
        int[] ids = this.cache.get().computeIfAbsent(key, k -> {
            LOTRBiome[] found = manager.getBiomesForGeneration(rx * REGION, rz * REGION, REGION, REGION);
            int[] a = new int[found.length];
            for (int l = 0; l < a.length; ++l) {
                a[l] = found[l].biomeID;
            }
            return a;
        });
        return this.byId[ids[(qx - rx * REGION) + (qz - rz * REGION) * REGION]];
    }
}
