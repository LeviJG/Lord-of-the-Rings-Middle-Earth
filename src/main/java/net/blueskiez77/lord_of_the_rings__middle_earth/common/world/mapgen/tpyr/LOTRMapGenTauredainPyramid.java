package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.mapgen.tpyr;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRDimension;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRWorldChunkManager;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRFarHaradJungleBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRTauredainClearingBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.mapgen.structure.LOTRMapGenStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.mapgen.structure.LOTRStructureComponent;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.mapgen.structure.LOTRStructureStart;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.village.LOTRVillagePositionCache;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.village.LocationInfo;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.LegacyRandomSource;

/**
 * LOTRMapGenTauredainPyramid: Tauredain pyramids in the Far Harad jungle (not its clearings), on a
 * grid of 24 chunks set apart by at least 12, one candidate in ten.
 */
public class LOTRMapGenTauredainPyramid extends LOTRMapGenStructure {
    private static List<LOTRBiome> spawnBiomes;
    public static final int MIN_DIST = 12;
    public static final int SEPARATION = 24;
    public int spawnChance = 10;

    private static final Map<String, Supplier<? extends LOTRStructureComponent>> COMPONENTS = Map.of(
            LOTRComponentTauredainPyramid.ID, LOTRComponentTauredainPyramid::new);

    public LOTRMapGenTauredainPyramid() {
        super("tauredain_pyramid");
    }

    @Override
    protected Map<String, Supplier<? extends LOTRStructureComponent>> componentTypes() {
        return COMPONENTS;
    }

    private static synchronized void setupSpawnBiomes() {
        if (spawnBiomes == null) {
            List<LOTRBiome> list = new ArrayList<>();
            for (LOTRBiome biome : LOTRDimension.MIDDLE_EARTH.biomeList) {
                if (biome instanceof LOTRFarHaradJungleBiome && !(biome instanceof LOTRTauredainClearingBiome)) {
                    list.add(biome);
                }
            }
            spawnBiomes = list;
        }
    }

    @Override
    protected boolean canSpawnStructureAtCoords(long worldSeed, LOTRWorldChunkManager worldChunkMgr, RandomSource rand, int i, int k) {
        LOTRVillagePositionCache cache = worldChunkMgr.getStructureCache(this);
        LocationInfo cacheLocation = cache.getLocationAt(i, k);
        if (cacheLocation != null) {
            return cacheLocation.isPresent();
        }
        setupSpawnBiomes();
        int i2 = Mth.floor((double) i / SEPARATION);
        int k2 = Mth.floor((double) k / SEPARATION);
        // World.setRandomSeed.
        RandomSource dRand = new LegacyRandomSource(i2 * 341873128712L + k2 * 132897987541L + worldSeed + 190169976);
        i2 *= SEPARATION;
        k2 *= SEPARATION;
        i2 += dRand.nextInt(SEPARATION - MIN_DIST + 1);
        if (i == i2 && k == k2 + dRand.nextInt(SEPARATION - MIN_DIST + 1)) {
            int i1 = i * 16 + 8;
            int k1 = k * 16 + 8;
            if (worldChunkMgr.areBiomesViable(i1, k1, 0, spawnBiomes) && rand.nextInt(this.spawnChance) == 0) {
                return cache.markResult(i, k, LocationInfo.RANDOM_GEN_HERE).isPresent();
            }
        }
        return cache.markResult(i, k, LocationInfo.NONE_HERE).isPresent();
    }

    @Override
    protected LOTRStructureStart getStructureStart(LOTRWorldChunkManager chunkManager, RandomSource rand, int i, int k) {
        // LOTRStructureTPyrStart.
        LOTRStructureStart start = new LOTRStructureStart(i, k);
        LOTRComponentTauredainPyramid startComponent = new LOTRComponentTauredainPyramid(0, rand, (i << 4) + 8, (k << 4) + 8);
        start.components.add(startComponent);
        startComponent.buildComponent(startComponent, start.components, rand);
        start.updateBoundingBox();
        return start;
    }
}
