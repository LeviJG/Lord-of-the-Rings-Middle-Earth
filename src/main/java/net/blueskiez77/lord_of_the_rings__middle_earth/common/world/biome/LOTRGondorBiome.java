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
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRGondorObeliskStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRGondorRuinStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRGondorRuinsStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRGondorStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRGondorTurretStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRGondorWatchfortStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRRuinedGondorTowerStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRSmallStoneRuinStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.village.LOTRVillageGenGondor;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

/**
 * LOTRBiomeGenGondor.
 */
public class LOTRGondorBiome extends LOTRBiome {

    public LOTRGondorBiome(int i, boolean major) {
        super(i, major);
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.HORSE, 5, 2, 6));
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.BEAR, 3, 1, 4));
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GONDOR_SOLDIERS, 10).setSpawnChance(100);
        npcSpawnList.newFactionList(100, 0.0f).add(arrspawnListContainer);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer2 = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer2[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GONDOR_SOLDIERS, 10);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer2);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer3 = new LOTRBiomeSpawnList.SpawnListContainer[6];
        arrspawnListContainer3[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MORDOR_ORCS, 20);
        arrspawnListContainer3[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MORDOR_BOMBARDIERS, 1);
        arrspawnListContainer3[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MORDOR_WARGS, 4).setConquestThreshold(50.0f);
        arrspawnListContainer3[3] = LOTRBiomeSpawnList.entry(LOTRSpawnList.BLACK_URUKS, 1).setConquestThreshold(50.0f);
        arrspawnListContainer3[4] = LOTRBiomeSpawnList.entry(LOTRSpawnList.BLACK_URUKS, 2).setConquestThreshold(100.0f);
        arrspawnListContainer3[5] = LOTRBiomeSpawnList.entry(LOTRSpawnList.OLOG_HAI, 1).setConquestThreshold(200.0f);
        npcSpawnList.newFactionList(5).add(arrspawnListContainer3);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer4 = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer4[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.URUK_HAI, 10);
        arrspawnListContainer4[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.URUK_WARGS, 2).setConquestThreshold(50.0f);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer4);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer5 = new LOTRBiomeSpawnList.SpawnListContainer[8];
        arrspawnListContainer5[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.HARNEDHRIM, 2).setConquestThreshold(100.0f);
        arrspawnListContainer5[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.HARNEDOR_WARRIORS, 10);
        arrspawnListContainer5[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.COAST_SOUTHRONS, 2).setConquestThreshold(100.0f);
        arrspawnListContainer5[3] = LOTRBiomeSpawnList.entry(LOTRSpawnList.SOUTHRON_WARRIORS, 10);
        arrspawnListContainer5[4] = LOTRBiomeSpawnList.entry(LOTRSpawnList.UMBARIANS, 1).setConquestThreshold(100.0f);
        arrspawnListContainer5[5] = LOTRBiomeSpawnList.entry(LOTRSpawnList.UMBAR_SOLDIERS, 5);
        arrspawnListContainer5[6] = LOTRBiomeSpawnList.entry(LOTRSpawnList.CORSAIRS, 5);
        arrspawnListContainer5[7] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GONDOR_RENEGADES, 10);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer5);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer6 = new LOTRBiomeSpawnList.SpawnListContainer[3];
        arrspawnListContainer6[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.EASTERLINGS, 2).setConquestThreshold(100.0f);
        arrspawnListContainer6[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.EASTERLING_WARRIORS, 10);
        arrspawnListContainer6[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.EASTERLING_GOLD_WARRIORS, 2).setConquestThreshold(50.0f);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer6);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer7 = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer7[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.HALF_TROLLS, 10);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer7);
        addBiomeVariantSet(LOTRBiomeVariant.SET_NORMAL_OAK);
        addBiomeVariant(LOTRBiomeVariant.FOREST_BEECH, 0.5f);
        addBiomeVariant(LOTRBiomeVariant.FOREST_BIRCH, 0.5f);
        addBiomeVariant(LOTRBiomeVariant.ORCHARD_APPLE_PEAR, 0.2f);
        addBiomeVariant(LOTRBiomeVariant.ORCHARD_PLUM, 0.2f);
        decorator.addSoil(new LOTRVanillaWorldGens.Minable(LOTRLegacyBlocks.mod("rock"), 1, 60, LOTRLegacyBlocks.vanilla("stone")), 2.0f, 0, 64);
        decorator.setTreeCluster(10, 30);
        decorator.willowPerChunk = 1;
        decorator.flowersPerChunk = 1;
        decorator.grassPerChunk = 10;
        decorator.doubleGrassPerChunk = 1;
        decorator.generateAthelas = true;
        decorator.addTree(LOTRTreeType.OAK, 1000);
        decorator.addTree(LOTRTreeType.OAK_LARGE, 300);
        decorator.addTree(LOTRTreeType.BIRCH, 50);
        decorator.addTree(LOTRTreeType.BIRCH_LARGE, 20);
        decorator.addTree(LOTRTreeType.BEECH, 50);
        decorator.addTree(LOTRTreeType.BEECH_LARGE, 20);
        decorator.addTree(LOTRTreeType.APPLE, 5);
        decorator.addTree(LOTRTreeType.PEAR, 5);
        decorator.addTree(LOTRTreeType.PLUM, 5);
        decorator.addTree(LOTRTreeType.OLIVE, 1);
        decorator.addTree(LOTRTreeType.ALMOND, 1);
        registerPlainsFlowers();
        addFiefdomStructures();
        decorator.addRandomStructure(new LOTRGondorRuinsStructure(), 600);
        decorator.addRandomStructure(new LOTRRuinedGondorTowerStructure(false), 1000);
        decorator.addRandomStructure(new LOTRGondorObeliskStructure(false), 1000);
        decorator.addRandomStructure(new LOTRGondorRuinStructure(false), 1000);
        decorator.addRandomStructure(new LOTRSmallStoneRuinStructure(false), 500);
        registerTravellingTrader(LOTREntities.GALADHRIM_TRADER);
        registerTravellingTrader(LOTREntities.IRON_HILLS_MERCHANT);
        registerTravellingTrader(LOTREntities.SCRAP_TRADER);
        registerTravellingTrader(LOTREntities.DORWINION_MERCHANT_ELF);
        registerTravellingTrader(LOTREntities.DALE_MERCHANT);
        registerTravellingTrader(LOTREntities.DORWINION_MERCHANT_MAN);
        setBanditChance(LOTREventSpawner.EventChance.BANDIT_RARE);
        invasionSpawns.addInvasion(LOTRInvasions.MORDOR, LOTREventSpawner.EventChance.UNCOMMON);
        invasionSpawns.addInvasion(LOTRInvasions.MORDOR_BLACK_URUK, LOTREventSpawner.EventChance.RARE);
        invasionSpawns.addInvasion(LOTRInvasions.MORDOR_NAN_UNGOL, LOTREventSpawner.EventChance.RARE);
        invasionSpawns.addInvasion(LOTRInvasions.MORDOR_WARG, LOTREventSpawner.EventChance.RARE);
        invasionSpawns.addInvasion(LOTRInvasions.NEAR_HARAD_HARNEDOR, LOTREventSpawner.EventChance.RARE);
        invasionSpawns.addInvasion(LOTRInvasions.NEAR_HARAD_COAST, LOTREventSpawner.EventChance.RARE);
        invasionSpawns.addInvasion(LOTRInvasions.NEAR_HARAD_UMBAR, LOTREventSpawner.EventChance.RARE);
        invasionSpawns.addInvasion(LOTRInvasions.NEAR_HARAD_CORSAIR, LOTREventSpawner.EventChance.RARE);
        invasionSpawns.addInvasion(LOTRInvasions.RHUN, LOTREventSpawner.EventChance.RARE);
    }

    public void addFiefdomStructures() {
        decorator.addRandomStructure(new LOTRGondorWatchfortStructure(false), 800);
        decorator.addRandomStructure(new LOTRGondorTurretStructure(false), 1000);
        decorator.addVillage(new LOTRVillageGenGondor((String) null, LOTRGondorStructure.GondorFiefdom.GONDOR, 0.75f));
    }

    @Override
    public void decorate(WorldGenLevel world, RandomSource random, int i, int k) {
        super.decorate(world, random, i, k);
        if (random.nextInt(24) == 0) {
            int i1 = i + random.nextInt(16) + 8;
            int j1 = random.nextInt(128);
            int k1 = k + random.nextInt(16) + 8;
            new LOTRVanillaWorldGens.Flowers(LOTRLegacyBlocks.mod("pipeweedPlant")).generate(world, random, i1, j1, k1);
        }
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_GONDOR;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.GONDOR.getSubregion("gondor");
    }

    @Override
    public LOTRWaypoint.Region getBiomeWaypoints() {
        return LOTRWaypoint.Region.GONDOR;
    }

    @Override
    public float getChanceToSpawnAnimals() {
        return 0.25f;
    }

    @Override
    public LOTRRoadType getRoadBlock() {
        return LOTRRoadType.GONDOR_MIX;
    }

    @Override
    public float getTreeIncreaseChance() {
        return 0.02f;
    }

    @Override
    public int spawnCountMultiplier() {
        return 3;
    }
}
