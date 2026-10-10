package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRRoadType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRBiomeSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnEntry;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.elf.LOTRRuinedWoodElfTowerStructure;

/**
 * LOTRBiomeGenMirkwood.
 */
public abstract class LOTRMirkwoodBiome extends LOTRBiome {

    protected LOTRMirkwoodBiome(int i, boolean major) {
        super(i, major);
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.ELK, 30, 4, 6));
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.DEER, 30, 4, 6));
        spawnableLOTRAmbientList.add(new LOTRSpawnEntry(LOTREntities.BUTTERFLY, 10, 4, 4));
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer = new LOTRBiomeSpawnList.SpawnListContainer[3];
        arrspawnListContainer[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MIRKWOOD_SPIDERS, 10);
        arrspawnListContainer[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.DOL_GULDUR_ORCS, 10).setConquestOnly();
        arrspawnListContainer[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MIRK_TROLLS, 2).setConquestThreshold(100.0f);
        npcSpawnList.newFactionList(100).add(arrspawnListContainer);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer2 = new LOTRBiomeSpawnList.SpawnListContainer[3];
        arrspawnListContainer2[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GUNDABAD_ORCS, 10);
        arrspawnListContainer2[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GUNDABAD_WARGS, 2);
        arrspawnListContainer2[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GUNDABAD_URUKS, 1).setConquestThreshold(100.0f);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer2);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer3 = new LOTRBiomeSpawnList.SpawnListContainer[4];
        arrspawnListContainer3[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.WOOD_ELF_WARRIORS, 10);
        arrspawnListContainer3[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.WOOD_ELVES, 5).setConquestThreshold(50.0f);
        arrspawnListContainer3[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.WOOD_ELVES, 5).setConquestThreshold(200.0f);
        arrspawnListContainer3[3] = LOTRBiomeSpawnList.entry(LOTRSpawnList.WOOD_ELVES, 5).setConquestThreshold(400.0f);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer3);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer4 = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer4[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GALADHRIM_WARRIORS, 10);
        arrspawnListContainer4[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GALADHRIM_WARDENS, 5);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer4);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer5 = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer5[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.HUORNS, 20);
        arrspawnListContainer5[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.ENTS, 10).setConquestThreshold(50.0f);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer5);
        registerForestFlowers();
        decorator.addRandomStructure(new LOTRRuinedWoodElfTowerStructure(false), 500);
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_MIRKWOOD;
    }

    @Override
    public LOTRWaypoint.Region getBiomeWaypoints() {
        return LOTRWaypoint.Region.MIRKWOOD;
    }

    @Override
    public LOTRRoadType.BridgeType getBridgeBlock() {
        return LOTRRoadType.BridgeType.MIRKWOOD;
    }

    @Override
    public LOTRRoadType getRoadBlock() {
        return LOTRRoadType.WOOD_ELVEN_RUINED.setRepair(0.6f);
    }
}
