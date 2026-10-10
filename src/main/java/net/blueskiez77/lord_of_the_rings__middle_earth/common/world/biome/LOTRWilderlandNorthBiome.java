package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRBiomeSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTREventSpawner;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRInvasions;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.orc.LOTRGundabadCampStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRBurntHouseStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRRottenHouseStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRRuinedHouseStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRSmallStoneRuinStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRStoneRuinStructure;

/**
 * LOTRBiomeGenWilderlandNorth.
 */
public class LOTRWilderlandNorthBiome extends LOTRWilderlandBiome {

    public LOTRWilderlandNorthBiome(int i, boolean major) {
        super(i, major);
        npcSpawnList.clear();
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer = new LOTRBiomeSpawnList.SpawnListContainer[3];
        arrspawnListContainer[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GUNDABAD_ORCS, 10);
        arrspawnListContainer[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GUNDABAD_WARGS, 2);
        arrspawnListContainer[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GUNDABAD_URUKS, 2).setConquestThreshold(50.0f);
        npcSpawnList.newFactionList(100).add(arrspawnListContainer);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer2 = new LOTRBiomeSpawnList.SpawnListContainer[3];
        arrspawnListContainer2[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.DOL_GULDUR_ORCS, 10);
        arrspawnListContainer2[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MIRKWOOD_SPIDERS, 2);
        arrspawnListContainer2[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MIRK_TROLLS, 1).setConquestThreshold(200.0f);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer2);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer3 = new LOTRBiomeSpawnList.SpawnListContainer[3];
        arrspawnListContainer3[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.DALE_SOLDIERS, 10);
        arrspawnListContainer3[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.DALE_MEN, 2).setConquestThreshold(50.0f);
        arrspawnListContainer3[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.DALE_MEN, 2).setConquestThreshold(100.0f);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer3);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer4 = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer4[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.DWARVES, 10);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer4);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer5 = new LOTRBiomeSpawnList.SpawnListContainer[4];
        arrspawnListContainer5[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.EASTERLING_WARRIORS, 10);
        arrspawnListContainer5[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.EASTERLING_GOLD_WARRIORS, 1);
        arrspawnListContainer5[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.EASTERLING_GOLD_WARRIORS, 2).setConquestThreshold(50.0f);
        arrspawnListContainer5[3] = LOTRBiomeSpawnList.entry(LOTRSpawnList.EASTERLINGS, 5).setConquestThreshold(200.0f);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer5);
        clearBiomeVariants();
        addBiomeVariantSet(LOTRBiomeVariant.SET_NORMAL_OAK_SPRUCE);
        addBiomeVariant(LOTRBiomeVariant.FOREST_PINE);
        decorator.setTreeCluster(6, 8);
        decorator.flowersPerChunk = 2;
        decorator.doubleFlowersPerChunk = 0;
        decorator.grassPerChunk = 10;
        decorator.doubleGrassPerChunk = 5;
        decorator.clearTrees();
        decorator.addTree(LOTRTreeType.OAK, 500);
        decorator.addTree(LOTRTreeType.OAK_LARGE, 100);
        decorator.addTree(LOTRTreeType.OAK_DEAD, 500);
        decorator.addTree(LOTRTreeType.SPRUCE, 300);
        decorator.addTree(LOTRTreeType.SPRUCE_DEAD, 100);
        decorator.addTree(LOTRTreeType.FIR, 200);
        decorator.addTree(LOTRTreeType.PINE, 200);
        registerPlainsFlowers();
        decorator.clearRandomStructures();
        decorator.addRandomStructure(new LOTRGundabadCampStructure(false), 2000);
        decorator.addRandomStructure(new LOTRRuinedHouseStructure(false), 2000);
        decorator.addRandomStructure(new LOTRBurntHouseStructure(false), 3000);
        decorator.addRandomStructure(new LOTRRottenHouseStructure(false), 3000);
        decorator.addRandomStructure(new LOTRStoneRuinStructure.STONE(1, 4), 800);
        decorator.addRandomStructure(new LOTRSmallStoneRuinStructure(false), 600);
        clearTravellingTraders();
        registerTravellingTrader(LOTREntities.IRON_HILLS_MERCHANT);
        registerTravellingTrader(LOTREntities.SCRAP_TRADER);
        registerTravellingTrader(LOTREntities.DALE_MERCHANT);
        setBanditChance(LOTREventSpawner.EventChance.BANDIT_UNCOMMON);
        invasionSpawns.clearInvasions();
        invasionSpawns.addInvasion(LOTRInvasions.GUNDABAD, LOTREventSpawner.EventChance.COMMON);
        invasionSpawns.addInvasion(LOTRInvasions.GUNDABAD_WARG, LOTREventSpawner.EventChance.UNCOMMON);
        invasionSpawns.addInvasion(LOTRInvasions.DOL_GULDUR, LOTREventSpawner.EventChance.RARE);
        invasionSpawns.addInvasion(LOTRInvasions.DALE, LOTREventSpawner.EventChance.RARE);
    }

    @Override
    public int spawnCountMultiplier() {
        return 1;
    }
}
