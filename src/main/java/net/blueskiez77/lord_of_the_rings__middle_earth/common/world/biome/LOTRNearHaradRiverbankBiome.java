package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRBiomeSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnEntry;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRHaradObeliskStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRHaradRuinedFortStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRMumakSkeletonStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRStoneRuinStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.village.LOTRVillageGenHaradNomad;

/**
 * LOTRBiomeGenNearHaradRiverbank.
 */
public class LOTRNearHaradRiverbankBiome extends LOTRNearHaradFertileBiome {

    public LOTRNearHaradRiverbankBiome(int i, boolean major) {
        super(i, major);
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.CAMEL, 20, 4, 4));
        npcSpawnList.clear();
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.NOMADS, 30).setSpawnChance(1000);
        arrspawnListContainer[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.NOMAD_WARRIORS, 10).setSpawnChance(1000);
        npcSpawnList.newFactionList(100).add(arrspawnListContainer);
        clearBiomeVariants();
        addBiomeVariant(LOTRBiomeVariant.FLOWERS);
        addBiomeVariant(LOTRBiomeVariant.FOREST_LIGHT);
        variantChance = 0.3f;
        decorator.treesPerChunk = 0;
        decorator.grassPerChunk = 10;
        decorator.doubleGrassPerChunk = 3;
        decorator.flowersPerChunk = 1;
        decorator.doubleFlowersPerChunk = 1;
        decorator.clearRandomStructures();
        decorator.addRandomStructure(new LOTRHaradObeliskStructure(false), 3000);
        decorator.addRandomStructure(new LOTRStoneRuinStructure.NEAR_HARAD(1, 3), 500);
        decorator.addRandomStructure(new LOTRMumakSkeletonStructure(false), 2000);
        decorator.addRandomStructure(new LOTRHaradRuinedFortStructure(false), 3000);
        decorator.clearVillages();
        decorator.addVillage(new LOTRVillageGenHaradNomad((String) null, 0.25f));
        clearTravellingTraders();
        registerTravellingTrader(LOTREntities.NOMAD_MERCHANT);
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_NEAR_HARAD;
    }

    @Override
    public LOTRWaypoint.Region getBiomeWaypoints() {
        return LOTRWaypoint.Region.HARAD_DESERT;
    }

    @Override
    public boolean getEnableRiver() {
        return false;
    }

    @Override
    public float getTreeIncreaseChance() {
        return 0.25f;
    }
}
