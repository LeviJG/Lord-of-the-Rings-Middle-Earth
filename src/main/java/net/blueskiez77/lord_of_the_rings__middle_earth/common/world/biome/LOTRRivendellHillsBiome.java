package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRBiomeSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTREventSpawner;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRInvasions;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

/**
 * LOTRBiomeGenRivendellHills.
 */
public class LOTRRivendellHillsBiome extends LOTRRivendellBiome {

    public LOTRRivendellHillsBiome(int i, boolean major) {
        super(i, major);
        fillerBlock = LOTRLegacyBlocks.mod("rock").state(5);
        npcSpawnList.clear();
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.RIVENDELL_WARRIORS, 10).setSpawnChance(500);
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
        clearBiomeVariants();
        variantChance = 0.4f;
        addBiomeVariant(LOTRBiomeVariant.FLOWERS);
        addBiomeVariant(LOTRBiomeVariant.FOREST_LIGHT);
        decorator.treesPerChunk = 3;
        decorator.flowersPerChunk = 2;
        decorator.grassPerChunk = 10;
        decorator.doubleGrassPerChunk = 2;
        decorator.clearTrees();
        decorator.addTree(LOTRTreeType.PINE, 1000);
        decorator.addTree(LOTRTreeType.PINE_SHRUB, 200);
        decorator.addTree(LOTRTreeType.FIR, 100);
        decorator.addTree(LOTRTreeType.SPRUCE, 100);
        decorator.addTree(LOTRTreeType.ASPEN, 100);
        decorator.addTree(LOTRTreeType.ASPEN_LARGE, 50);
        decorator.addTree(LOTRTreeType.OAK, 100);
        decorator.addTree(LOTRTreeType.OAK_LARGE, 50);
        biomeColors.resetGrass();
        decorator.clearRandomStructures();
        invasionSpawns.clearInvasions();
        invasionSpawns.addInvasion(LOTRInvasions.HIGH_ELF_RIVENDELL, LOTREventSpawner.EventChance.UNCOMMON);
        invasionSpawns.addInvasion(LOTRInvasions.GUNDABAD, LOTREventSpawner.EventChance.RARE);
        invasionSpawns.addInvasion(LOTRInvasions.GUNDABAD_WARG, LOTREventSpawner.EventChance.RARE);
        invasionSpawns.addInvasion(LOTRInvasions.ANGMAR, LOTREventSpawner.EventChance.RARE);
        invasionSpawns.addInvasion(LOTRInvasions.ANGMAR_HILLMEN, LOTREventSpawner.EventChance.RARE);
        invasionSpawns.addInvasion(LOTRInvasions.ANGMAR_WARG, LOTREventSpawner.EventChance.RARE);
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRBiomes.LONE_LANDS.getBiomeAchievement();
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRBiomes.LONE_LANDS.getBiomeMusic();
    }

    @Override
    public LOTRWaypoint.Region getBiomeWaypoints() {
        return LOTRBiomes.LONE_LANDS.getBiomeWaypoints();
    }

    @Override
    public float getChanceToSpawnAnimals() {
        return 1.0f;
    }

    @Override
    public boolean hasSeasonalGrass() {
        return true;
    }

    @Override
    public int spawnCountMultiplier() {
        return 1;
    }
}
