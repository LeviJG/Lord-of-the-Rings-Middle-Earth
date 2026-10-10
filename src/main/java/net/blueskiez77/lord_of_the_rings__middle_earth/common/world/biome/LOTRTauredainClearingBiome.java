package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRRoadType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRBiomeSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnEntry;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.village.LOTRVillageGenTauredain;

/**
 * LOTRBiomeGenTauredainClearing.
 */
public class LOTRTauredainClearingBiome extends LOTRFarHaradJungleBiome {

    public LOTRTauredainClearingBiome(int i, boolean major) {
        super(i, major);
        obsidianGravelRarity = 500;
        spawnableMonsterList.clear();
        spawnableLOTRAmbientList.clear();
        spawnableLOTRAmbientList.add(new LOTRSpawnEntry(LOTREntities.BIRD, 10, 4, 4));
        spawnableLOTRAmbientList.add(new LOTRSpawnEntry(LOTREntities.BUTTERFLY, 15, 4, 4));
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer = new LOTRBiomeSpawnList.SpawnListContainer[3];
        arrspawnListContainer[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.TAURETHRIM, 10);
        arrspawnListContainer[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.TAURETHRIM_WARRIORS, 4);
        arrspawnListContainer[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.TAURETHRIM_WARRIORS, 10).setConquestOnly();
        npcSpawnList.newFactionList(100).add(arrspawnListContainer);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer2 = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer2[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MORDOR_ORCS, 10);
        arrspawnListContainer2[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.BLACK_URUKS, 2).setConquestThreshold(50.0f);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer2);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer3 = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer3[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.SOUTHRON_WARRIORS, 10);
        arrspawnListContainer3[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GULF_WARRIORS, 10);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer3);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer4 = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer4[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MORWAITH_WARRIORS, 10);
        arrspawnListContainer4[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MORWAITH, 5);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer4);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer5 = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer5[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.HALF_TROLLS, 10);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer5);
        npcSpawnList.conquestGainRate = 0.5f;
        clearBiomeVariants();
        variantChance = 0.1f;
        addBiomeVariant(LOTRBiomeVariant.FLOWERS);
        decorator.setTreeCluster(16, 40);
        decorator.treesPerChunk = 0;
        decorator.vinesPerChunk = 0;
        decorator.grassPerChunk = 10;
        decorator.doubleGrassPerChunk = 6;
        decorator.addVillage(new LOTRVillageGenTauredain((String) null, 0.6f));
        biomeColors.setSky(11590117);
        biomeColors.setFog(12705243);
        invasionSpawns.clearInvasions();
    }

    @Override
    public LOTRRoadType getRoadBlock() {
        return LOTRRoadType.TAUREDAIN;
    }

    @Override
    public boolean hasJungleLakes() {
        return false;
    }

    @Override
    public int spawnCountMultiplier() {
        return 3;
    }
}
