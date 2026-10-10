package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRBiomeSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTREventSpawner;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRInvasions;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnEntry;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRSmallStoneRuinStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRStoneRuinStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.village.LOTRVillageGenBree;

/**
 * LOTRBiomeGenBreeland.
 */
public class LOTRBreelandBiome extends LOTRBiome {

    public LOTRBreelandBiome(int i, boolean major) {
        super(i, major);
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.HORSE, 8, 2, 6));
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.BREE_MEN, 10).setSpawnChance(500);
        arrspawnListContainer[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.BREE_GUARDS, 4).setSpawnChance(500);
        npcSpawnList.newFactionList(100).add(arrspawnListContainer);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer2 = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer2[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.RUFFIANS, 10).setSpawnChance(500);
        npcSpawnList.newFactionList(5).add(arrspawnListContainer2);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer3 = new LOTRBiomeSpawnList.SpawnListContainer[3];
        arrspawnListContainer3[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GUNDABAD_ORCS, 10);
        arrspawnListContainer3[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GUNDABAD_WARGS, 2);
        arrspawnListContainer3[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GUNDABAD_URUKS, 2).setConquestThreshold(100.0f);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer3);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer4 = new LOTRBiomeSpawnList.SpawnListContainer[3];
        arrspawnListContainer4[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.ANGMAR_HILLMEN, 10);
        arrspawnListContainer4[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.ANGMAR_ORCS, 10).setConquestThreshold(50.0f);
        arrspawnListContainer4[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.ANGMAR_WARGS, 2).setConquestThreshold(50.0f);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer4);
        addBiomeVariant(LOTRBiomeVariant.FLOWERS);
        addBiomeVariant(LOTRBiomeVariant.FOREST_LIGHT);
        addBiomeVariant(LOTRBiomeVariant.STEPPE);
        addBiomeVariant(LOTRBiomeVariant.HILLS);
        addBiomeVariant(LOTRBiomeVariant.HILLS_FOREST);
        addBiomeVariant(LOTRBiomeVariant.FOREST_BEECH, 0.2f);
        addBiomeVariant(LOTRBiomeVariant.FOREST_BIRCH, 0.2f);
        addBiomeVariant(LOTRBiomeVariant.FOREST_ASPEN, 0.2f);
        addBiomeVariant(LOTRBiomeVariant.FOREST_MAPLE, 0.2f);
        addBiomeVariant(LOTRBiomeVariant.ORCHARD_APPLE_PEAR, 1.0f);
        decorator.setTreeCluster(8, 20);
        decorator.willowPerChunk = 1;
        decorator.flowersPerChunk = 2;
        decorator.doubleFlowersPerChunk = 1;
        decorator.grassPerChunk = 8;
        decorator.doubleGrassPerChunk = 1;
        decorator.generateAthelas = true;
        decorator.addTree(LOTRTreeType.OAK, 1000);
        decorator.addTree(LOTRTreeType.OAK_LARGE, 300);
        decorator.addTree(LOTRTreeType.BEECH, 300);
        decorator.addTree(LOTRTreeType.BEECH_LARGE, 75);
        decorator.addTree(LOTRTreeType.MAPLE, 200);
        decorator.addTree(LOTRTreeType.MAPLE_LARGE, 50);
        decorator.addTree(LOTRTreeType.CHESTNUT, 300);
        decorator.addTree(LOTRTreeType.CHESTNUT_LARGE, 75);
        decorator.addTree(LOTRTreeType.BIRCH, 50);
        decorator.addTree(LOTRTreeType.BIRCH_LARGE, 20);
        decorator.addTree(LOTRTreeType.ASPEN, 50);
        decorator.addTree(LOTRTreeType.ASPEN_LARGE, 10);
        decorator.addTree(LOTRTreeType.APPLE, 3);
        decorator.addTree(LOTRTreeType.PEAR, 3);
        registerPlainsFlowers();
        decorator.addRandomStructure(new LOTRSmallStoneRuinStructure(false), 500);
        decorator.addRandomStructure(new LOTRStoneRuinStructure.STONE(1, 3), 1000);
        decorator.addRandomStructure(new LOTRStoneRuinStructure.ARNOR(1, 3), 1000);
        LOTRVillageGenBree villageGen = new LOTRVillageGenBree((String) null, 1.0f);
        villageGen.addFixedLocationMapOffset(LOTRWaypoint.BREE, 2, 0, 3, "Bree");
        villageGen.addFixedLocationMapOffset(LOTRWaypoint.STADDLE, -2, 0, 1, "Staddle");
        villageGen.addFixedLocationMapOffset(LOTRWaypoint.COMBE, 2, 0, 3, "Combe");
        villageGen.addFixedLocationMapOffset(LOTRWaypoint.ARCHET, 0, -2, 2, "Archet");
        decorator.addVillage(villageGen);
        registerTravellingTrader(LOTREntities.GALADHRIM_TRADER);
        registerTravellingTrader(LOTREntities.BLUE_DWARF_MERCHANT);
        registerTravellingTrader(LOTREntities.IRON_HILLS_MERCHANT);
        registerTravellingTrader(LOTREntities.SCRAP_TRADER);
        registerTravellingTrader(LOTREntities.DALE_MERCHANT);
        registerTravellingTrader(LOTREntities.RIVENDELL_TRADER);
        setBanditChance(LOTREventSpawner.EventChance.BANDIT_RARE);
        invasionSpawns.addInvasion(LOTRInvasions.GUNDABAD, LOTREventSpawner.EventChance.RARE);
        invasionSpawns.addInvasion(LOTRInvasions.GUNDABAD_WARG, LOTREventSpawner.EventChance.RARE);
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_BREELAND;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.BREE.getSubregion("bree");
    }

    @Override
    public LOTRWaypoint.Region getBiomeWaypoints() {
        return LOTRWaypoint.Region.BREE_LAND;
    }

    @Override
    public float getChanceToSpawnAnimals() {
        return 0.25f;
    }

    @Override
    public float getTreeIncreaseChance() {
        return 0.05f;
    }

    @Override
    public boolean hasDomesticAnimals() {
        return true;
    }
}
