package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRBiomeSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTREventSpawner;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnList;

/**
 * LOTRBiomeGenOldForest.
 */
public class LOTROldForestBiome extends LOTRBiome {

    public LOTROldForestBiome(int i, boolean major) {
        super(i, major);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.DARK_HUORNS, 10);
        npcSpawnList.newFactionList(100).add(arrspawnListContainer);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer2 = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer2[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.HOBBITS, 10);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer2);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer3 = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer3[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GUNDABAD_ORCS, 10);
        arrspawnListContainer3[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GUNDABAD_WARGS, 2);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer3);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer4 = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer4[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.ANGMAR_ORCS, 10);
        arrspawnListContainer4[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.ANGMAR_WARGS, 2);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer4);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer5 = new LOTRBiomeSpawnList.SpawnListContainer[3];
        arrspawnListContainer5[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.ISENGARD_SNAGA, 5);
        arrspawnListContainer5[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.URUK_HAI, 10);
        arrspawnListContainer5[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.URUK_WARGS, 2).setConquestThreshold(50.0f);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer5);
        addBiomeVariantSet(LOTRBiomeVariant.SET_FOREST);
        decorator.treesPerChunk = 16;
        decorator.willowPerChunk = 2;
        decorator.flowersPerChunk = 1;
        decorator.grassPerChunk = 12;
        decorator.doubleGrassPerChunk = 5;
        decorator.enableFern = true;
        decorator.mushroomsPerChunk = 2;
        decorator.addTree(LOTRTreeType.OAK, 500);
        decorator.addTree(LOTRTreeType.OAK_TALL, 1000);
        decorator.addTree(LOTRTreeType.OAK_TALLER, 200);
        decorator.addTree(LOTRTreeType.OAK_LARGE, 500);
        decorator.addTree(LOTRTreeType.DARK_OAK, 1000);
        decorator.addTree(LOTRTreeType.FIR, 500);
        decorator.addTree(LOTRTreeType.PINE, 500);
        registerForestFlowers();
        biomeColors.setGrass(4686398);
        biomeColors.setFoliage(3172394);
        biomeColors.setFog(1651225);
        biomeColors.setFoggy(true);
        setBanditChance(LOTREventSpawner.EventChance.NEVER);
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_OLD_FOREST;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.OLD_FOREST.getSubregion("oldForest");
    }

    @Override
    public LOTRWaypoint.Region getBiomeWaypoints() {
        return LOTRWaypoint.Region.OLD_FOREST;
    }

    @Override
    public float getChanceToSpawnAnimals() {
        return 0.0f;
    }
}
