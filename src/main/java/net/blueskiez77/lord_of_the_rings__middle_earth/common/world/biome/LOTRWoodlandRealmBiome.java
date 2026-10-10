package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRRoadType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRBiomeSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTREventSpawner;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRInvasions;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnEntry;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.elf.LOTRWoodElfHouseStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.elf.LOTRWoodElfTowerStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.elf.LOTRWoodElvenForgeStructure;

/**
 * LOTRBiomeGenWoodlandRealm.
 */
public class LOTRWoodlandRealmBiome extends LOTRBiome {

    public LOTRWoodlandRealmBiome(int i, boolean major) {
        super(i, major);
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.ELK, 30, 4, 6));
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.DEER, 20, 4, 6));
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.BEAR, 2, 1, 4));
        spawnableCaveCreatureList.clear();
        spawnableLOTRAmbientList.add(new LOTRSpawnEntry(LOTREntities.BUTTERFLY, 10, 4, 4));
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.WOOD_ELVES, 10);
        arrspawnListContainer[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.WOOD_ELF_WARRIORS, 3);
        npcSpawnList.newFactionList(100).add(arrspawnListContainer);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer2 = new LOTRBiomeSpawnList.SpawnListContainer[3];
        arrspawnListContainer2[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GUNDABAD_ORCS, 10);
        arrspawnListContainer2[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GUNDABAD_WARGS, 2);
        arrspawnListContainer2[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GUNDABAD_URUKS, 1).setConquestThreshold(100.0f);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer2);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer3 = new LOTRBiomeSpawnList.SpawnListContainer[3];
        arrspawnListContainer3[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MIRKWOOD_SPIDERS, 10);
        arrspawnListContainer3[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.DOL_GULDUR_ORCS, 10);
        arrspawnListContainer3[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MIRK_TROLLS, 1).setConquestThreshold(100.0f);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer3);
        npcSpawnList.conquestGainRate = 0.2f;
        addBiomeVariant(LOTRBiomeVariant.FOREST);
        addBiomeVariant(LOTRBiomeVariant.FOREST_LIGHT);
        addBiomeVariant(LOTRBiomeVariant.FOREST_BEECH, 0.1f);
        addBiomeVariant(LOTRBiomeVariant.FOREST_BIRCH, 0.1f);
        addBiomeVariant(LOTRBiomeVariant.FOREST_LARCH, 0.1f);
        addBiomeVariant(LOTRBiomeVariant.FOREST_PINE, 0.1f);
        addBiomeVariant(LOTRBiomeVariant.FOREST_ASPEN, 0.1f);
        variantChance = 0.3f;
        decorator.treesPerChunk = 1;
        decorator.willowPerChunk = 2;
        decorator.flowersPerChunk = 3;
        decorator.doubleFlowersPerChunk = 1;
        decorator.grassPerChunk = 4;
        decorator.doubleGrassPerChunk = 1;
        decorator.enableFern = true;
        decorator.generateLava = false;
        decorator.generateCobwebs = false;
        decorator.addTree(LOTRTreeType.GREEN_OAK, 500);
        decorator.addTree(LOTRTreeType.GREEN_OAK_LARGE, 50);
        decorator.addTree(LOTRTreeType.GREEN_OAK_EXTREME, 80);
        decorator.addTree(LOTRTreeType.RED_OAK, 40);
        decorator.addTree(LOTRTreeType.RED_OAK_LARGE, 20);
        decorator.addTree(LOTRTreeType.OAK, 50);
        decorator.addTree(LOTRTreeType.OAK_LARGE, 100);
        decorator.addTree(LOTRTreeType.SPRUCE, 100);
        decorator.addTree(LOTRTreeType.CHESTNUT, 50);
        decorator.addTree(LOTRTreeType.CHESTNUT_LARGE, 50);
        decorator.addTree(LOTRTreeType.BEECH, 50);
        decorator.addTree(LOTRTreeType.BEECH_LARGE, 100);
        decorator.addTree(LOTRTreeType.LARCH, 100);
        decorator.addTree(LOTRTreeType.FIR, 200);
        decorator.addTree(LOTRTreeType.PINE, 200);
        decorator.addTree(LOTRTreeType.ASPEN, 50);
        decorator.addTree(LOTRTreeType.ASPEN_LARGE, 10);
        registerForestFlowers();
        decorator.clearRandomStructures();
        decorator.addRandomStructure(new LOTRWoodElfHouseStructure(false), 16);
        decorator.addRandomStructure(new LOTRWoodElfTowerStructure(false), 100);
        decorator.addRandomStructure(new LOTRWoodElvenForgeStructure(false), 80);
        registerTravellingTrader(LOTREntities.GALADHRIM_TRADER);
        registerTravellingTrader(LOTREntities.DORWINION_MERCHANT_ELF);
        setBanditChance(LOTREventSpawner.EventChance.NEVER);
        invasionSpawns.addInvasion(LOTRInvasions.DOL_GULDUR, LOTREventSpawner.EventChance.UNCOMMON);
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_WOODLAND_REALM;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.WOODLAND_REALM.getSubregion("woodlandRealm");
    }

    @Override
    public LOTRWaypoint.Region getBiomeWaypoints() {
        return LOTRWaypoint.Region.WOODLAND_REALM;
    }

    @Override
    public float getChanceToSpawnAnimals() {
        return 0.5f;
    }

    @Override
    public LOTRRoadType getRoadBlock() {
        return LOTRRoadType.WOOD_ELVEN;
    }

    @Override
    public int spawnCountMultiplier() {
        return 3;
    }
}
