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
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnEntry;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.elf.LOTRRivendellForgeStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.elf.LOTRRivendellHallStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.elf.LOTRRivendellHouseStructure;

/**
 * LOTRBiomeGenRivendell.
 */
public class LOTRRivendellBiome extends LOTRBiome {

    public LOTRRivendellBiome(int i, boolean major) {
        super(i, major);
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.HORSE, 5, 2, 6));
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.RIVENDELL_ELVES, 10);
        arrspawnListContainer[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.RIVENDELL_WARRIORS, 2);
        npcSpawnList.newFactionList(100).add(arrspawnListContainer);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer2 = new LOTRBiomeSpawnList.SpawnListContainer[3];
        arrspawnListContainer2[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GUNDABAD_ORCS, 10);
        arrspawnListContainer2[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GUNDABAD_WARGS, 2);
        arrspawnListContainer2[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GUNDABAD_URUKS, 2).setConquestThreshold(100.0f);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer2);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer3 = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer3[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.ANGMAR_ORCS, 10);
        arrspawnListContainer3[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.ANGMAR_WARGS, 2);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer3);
        npcSpawnList.conquestGainRate = 0.2f;
        variantChance = 0.3f;
        addBiomeVariant(LOTRBiomeVariant.FLOWERS);
        addBiomeVariant(LOTRBiomeVariant.FOREST_LIGHT);
        decorator.addOre(new LOTRVanillaWorldGens.Minable(LOTRLegacyBlocks.mod("oreQuendite"), 6), 6.0f, 0, 48);
        decorator.treesPerChunk = 0;
        decorator.willowPerChunk = 1;
        decorator.flowersPerChunk = 5;
        decorator.grassPerChunk = 6;
        decorator.doubleGrassPerChunk = 1;
        decorator.addTree(LOTRTreeType.OAK, 500);
        decorator.addTree(LOTRTreeType.OAK_LARGE, 200);
        decorator.addTree(LOTRTreeType.BEECH, 500);
        decorator.addTree(LOTRTreeType.BEECH_LARGE, 200);
        decorator.addTree(LOTRTreeType.BIRCH, 200);
        decorator.addTree(LOTRTreeType.BIRCH_TALL, 100);
        decorator.addTree(LOTRTreeType.BIRCH_LARGE, 50);
        decorator.addTree(LOTRTreeType.CHESTNUT, 50);
        decorator.addTree(LOTRTreeType.CHESTNUT_LARGE, 10);
        decorator.addTree(LOTRTreeType.ASPEN, 50);
        decorator.addTree(LOTRTreeType.ASPEN_LARGE, 20);
        decorator.addTree(LOTRTreeType.APPLE, 2);
        decorator.addTree(LOTRTreeType.PEAR, 2);
        registerPlainsFlowers();
        decorator.addRandomStructure(new LOTRRivendellHouseStructure(false), 100);
        decorator.addRandomStructure(new LOTRRivendellHallStructure(false), 200);
        decorator.addRandomStructure(new LOTRRivendellForgeStructure(false), 200);
        registerTravellingTrader(LOTREntities.GALADHRIM_TRADER);
        setBanditChance(LOTREventSpawner.EventChance.NEVER);
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_RIVENDELL;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.RIVENDELL.getSubregion("rivendell");
    }

    @Override
    public LOTRWaypoint.Region getBiomeWaypoints() {
        return LOTRWaypoint.Region.RIVENDELL_VALE;
    }

    @Override
    public float getChanceToSpawnAnimals() {
        return 0.25f;
    }

    @Override
    public boolean getEnableRiver() {
        return false;
    }

    @Override
    public LOTRRoadType getRoadBlock() {
        return LOTRRoadType.HIGH_ELVEN;
    }

    @Override
    public float getTreeIncreaseChance() {
        return 0.25f;
    }

    @Override
    public boolean hasSeasonalGrass() {
        return false;
    }

    @Override
    public int spawnCountMultiplier() {
        return 3;
    }
}
