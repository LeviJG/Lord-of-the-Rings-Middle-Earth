package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.mapgen.dwarvenmine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRDimension;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRWorldChunkManager;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBlueMountainsBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBlueMountainsFoothillsBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTREreborBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRGreyMountainsBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRIronHillsBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.mapgen.structure.LOTRMapGenStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.mapgen.structure.LOTRStructureComponent;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.mapgen.structure.LOTRStructureStart;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.village.LOTRVillagePositionCache;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.village.LocationInfo;

import net.minecraft.util.RandomSource;

/**
 * LOTRMapGenDwarvenMine: dwarven mines under the Iron Hills, the Blue Mountains (not their
 * foothills) and Erebor, one chunk in 150; ruined ones under the Grey Mountains, one in 500.
 */
public class LOTRMapGenDwarvenMine extends LOTRMapGenStructure {
    private static List<LOTRBiome> spawnBiomes;
    private static List<LOTRBiome> spawnBiomesRuined;
    public int spawnChance = 150;
    public int spawnChanceRuined = 500;

    private static final Map<String, Supplier<? extends LOTRStructureComponent>> COMPONENTS = Map.of(
            LOTRComponentDwarvenMineEntrance.ID, LOTRComponentDwarvenMineEntrance::new,
            LOTRComponentDwarvenMineCorridor.ID, LOTRComponentDwarvenMineCorridor::new,
            LOTRComponentDwarvenMineCrossing.ID, LOTRComponentDwarvenMineCrossing::new,
            LOTRComponentDwarvenMineStairs.ID, LOTRComponentDwarvenMineStairs::new);

    public LOTRMapGenDwarvenMine() {
        super("dwarven_mine");
    }

    @Override
    protected Map<String, Supplier<? extends LOTRStructureComponent>> componentTypes() {
        return COMPONENTS;
    }

    private static synchronized void setupSpawnBiomes() {
        if (spawnBiomes == null) {
            List<LOTRBiome> mines = new ArrayList<>();
            List<LOTRBiome> ruined = new ArrayList<>();
            for (LOTRBiome biome : LOTRDimension.MIDDLE_EARTH.biomeList) {
                if (biome == null) {
                    continue;
                }
                boolean mine = biome instanceof LOTRIronHillsBiome
                        || biome instanceof LOTRBlueMountainsBiome && !(biome instanceof LOTRBlueMountainsFoothillsBiome)
                        || biome instanceof LOTREreborBiome;
                if (mine) {
                    mines.add(biome);
                }
                if (biome instanceof LOTRGreyMountainsBiome) {
                    ruined.add(biome);
                }
            }
            spawnBiomesRuined = ruined;
            spawnBiomes = mines;
        }
    }

    @Override
    protected boolean canSpawnStructureAtCoords(long worldSeed, LOTRWorldChunkManager worldChunkMgr, RandomSource rand, int i, int k) {
        LOTRVillagePositionCache cache = worldChunkMgr.getStructureCache(this);
        LocationInfo cacheLocation = cache.getLocationAt(i, k);
        if (cacheLocation != null) {
            return cacheLocation.isPresent();
        }
        int i1 = i * 16 + 8;
        int k1 = k * 16 + 8;
        setupSpawnBiomes();
        if (worldChunkMgr.areBiomesViable(i1, k1, 0, spawnBiomes) ? rand.nextInt(this.spawnChance) == 0
                : worldChunkMgr.areBiomesViable(i1, k1, 0, spawnBiomesRuined) && rand.nextInt(this.spawnChanceRuined) == 0) {
            return cache.markResult(i, k, LocationInfo.RANDOM_GEN_HERE).isPresent();
        }
        return cache.markResult(i, k, LocationInfo.NONE_HERE).isPresent();
    }

    @Override
    protected LOTRStructureStart getStructureStart(LOTRWorldChunkManager chunkManager, RandomSource rand, int i, int k) {
        int i1 = i * 16 + 8;
        int k1 = k * 16 + 8;
        boolean ruined = spawnBiomesRuined.contains(chunkManager.getBiomeGenAt(i1, k1));
        // LOTRStructureDwarvenMineStart.
        LOTRStructureStart start = new LOTRStructureStart(i, k);
        LOTRComponentDwarvenMineEntrance startComponent = new LOTRComponentDwarvenMineEntrance(0, rand, (i << 4) + 8, (k << 4) + 8, ruined);
        start.components.add(startComponent);
        startComponent.buildComponent(startComponent, start.components, rand);
        start.updateBoundingBox();
        return start;
    }
}
