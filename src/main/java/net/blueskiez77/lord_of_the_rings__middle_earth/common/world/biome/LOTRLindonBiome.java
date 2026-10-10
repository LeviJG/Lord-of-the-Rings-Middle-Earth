package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRVanillaWorldGens;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRRoadType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRBiomeSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTREventSpawner;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRInvasions;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnEntry;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.elf.LOTRHighElfHouseStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.elf.LOTRHighElvenForgeStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.elf.LOTRHighElvenHallStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.elf.LOTRHighElvenTowerStructure;

/**
 * LOTRBiomeGenLindon.
 */
public class LOTRLindonBiome extends LOTRBiome {

    public LOTRLindonBiome(int i, boolean major) {
        super(i, major);
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.HORSE, 5, 2, 6));
        spawnableLOTRAmbientList.add(new LOTRSpawnEntry(LOTREntities.SEAGULL, 6, 4, 4));
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.LINDON_ELVES, 10);
        arrspawnListContainer[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.LINDON_WARRIORS, 2);
        npcSpawnList.newFactionList(100).add(arrspawnListContainer);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer2 = new LOTRBiomeSpawnList.SpawnListContainer[3];
        arrspawnListContainer2[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GUNDABAD_ORCS, 10);
        arrspawnListContainer2[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GUNDABAD_WARGS, 2);
        arrspawnListContainer2[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GUNDABAD_URUKS, 1).setConquestThreshold(50.0f);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer2);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer3 = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer3[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.ANGMAR_ORCS, 10);
        arrspawnListContainer3[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.ANGMAR_WARGS, 2);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer3);
        npcSpawnList.conquestGainRate = 0.5f;
        addBiomeVariant(LOTRBiomeVariant.FLOWERS);
        addBiomeVariant(LOTRBiomeVariant.FOREST);
        addBiomeVariant(LOTRBiomeVariant.FOREST_LIGHT);
        addBiomeVariant(LOTRBiomeVariant.STEPPE);
        addBiomeVariant(LOTRBiomeVariant.HILLS);
        addBiomeVariant(LOTRBiomeVariant.HILLS_FOREST);
        addBiomeVariant(LOTRBiomeVariant.DENSEFOREST_OAK);
        addBiomeVariant(LOTRBiomeVariant.DENSEFOREST_BIRCH);
        addBiomeVariant(LOTRBiomeVariant.FOREST_BEECH, 0.2f);
        addBiomeVariant(LOTRBiomeVariant.FOREST_BIRCH, 0.5f);
        addBiomeVariant(LOTRBiomeVariant.FOREST_ASPEN, 0.5f);
        addBiomeVariant(LOTRBiomeVariant.ORCHARD_APPLE_PEAR, 1.0f);
        decorator.addOre(new LOTRVanillaWorldGens.Minable(LOTRLegacyBlocks.mod("oreQuendite"), 6), 6.0f, 0, 48);
        decorator.setTreeCluster(10, 20);
        decorator.treesPerChunk = 0;
        decorator.willowPerChunk = 1;
        decorator.flowersPerChunk = 3;
        decorator.grassPerChunk = 8;
        decorator.doubleGrassPerChunk = 1;
        decorator.whiteSand = true;
        decorator.addTree(LOTRTreeType.OAK, 100);
        decorator.addTree(LOTRTreeType.OAK_LARGE, 25);
        decorator.addTree(LOTRTreeType.BIRCH, 500);
        decorator.addTree(LOTRTreeType.BIRCH_TALL, 500);
        decorator.addTree(LOTRTreeType.BIRCH_LARGE, 200);
        decorator.addTree(LOTRTreeType.BIRCH_PARTY, 50);
        decorator.addTree(LOTRTreeType.BEECH, 100);
        decorator.addTree(LOTRTreeType.BEECH_LARGE, 25);
        decorator.addTree(LOTRTreeType.CHESTNUT, 40);
        decorator.addTree(LOTRTreeType.CHESTNUT_LARGE, 10);
        decorator.addTree(LOTRTreeType.ASPEN, 300);
        decorator.addTree(LOTRTreeType.ASPEN_LARGE, 100);
        decorator.addTree(LOTRTreeType.APPLE, 2);
        decorator.addTree(LOTRTreeType.PEAR, 2);
        registerPlainsFlowers();
        decorator.addRandomStructure(new LOTRHighElfHouseStructure(false), 300);
        decorator.addRandomStructure(new LOTRHighElvenHallStructure(false), 600);
        decorator.addRandomStructure(new LOTRHighElvenForgeStructure(false), 1000);
        decorator.addRandomStructure(new LOTRHighElvenTowerStructure(false), 900);
        registerTravellingTrader(LOTREntities.GALADHRIM_TRADER);
        registerTravellingTrader(LOTREntities.BLUE_DWARF_MERCHANT);
        registerTravellingTrader(LOTREntities.RIVENDELL_TRADER);
        setBanditChance(LOTREventSpawner.EventChance.NEVER);
        invasionSpawns.addInvasion(LOTRInvasions.GUNDABAD, LOTREventSpawner.EventChance.RARE);
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_LINDON;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.LINDON.getSubregion("lindon");
    }

    @Override
    public LOTRWaypoint.Region getBiomeWaypoints() {
        return LOTRWaypoint.Region.LINDON;
    }

    @Override
    public float getChanceToSpawnAnimals() {
        return 0.25f;
    }

    @Override
    public LOTRRoadType getRoadBlock() {
        return LOTRRoadType.HIGH_ELVEN;
    }

    @Override
    public boolean hasSeasonalGrass() {
        return false;
    }

    @Override
    public int spawnCountMultiplier() {
        return 5;
    }
}
