package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRVanillaWorldGens;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenBoulder;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenerator;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRRoadType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRBiomeSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTREventSpawner;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRInvasions;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnEntry;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRSmallStoneRuinStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.rohan.LOTRRohanBarrowStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.rohan.LOTRRohanWatchtowerStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.village.LOTRVillageGenRohan;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

/**
 * LOTRBiomeGenRohan.
 */
public class LOTRRohanBiome extends LOTRBiome {

    public LOTRWorldGenerator boulderGen = new LOTRWorldGenBoulder(LOTRLegacyBlocks.mod("rock"), 2, 1, 4);

    public LOTRRohanBiome(int i, boolean major) {
        super(i, major);
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.HORSE, 30, 2, 6));
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.BEAR, 3, 1, 4));
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.ROHIRRIM_WARRIORS, 10).setSpawnChance(100);
        npcSpawnList.newFactionList(100, 0.0f).add(arrspawnListContainer);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer2 = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer2[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.ROHIRRIM_WARRIORS, 10);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer2);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer3 = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer3[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.URUK_HAI, 10);
        arrspawnListContainer3[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.URUK_WARGS, 4);
        npcSpawnList.newFactionList(5).add(arrspawnListContainer3);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer4 = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer4[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.DUNLENDINGS, 10);
        arrspawnListContainer4[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.DUNLENDING_WARRIORS, 10);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer4);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer5 = new LOTRBiomeSpawnList.SpawnListContainer[3];
        arrspawnListContainer5[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.DOL_GULDUR_ORCS, 10);
        arrspawnListContainer5[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MIRKWOOD_SPIDERS, 2).setConquestThreshold(50.0f);
        arrspawnListContainer5[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MIRK_TROLLS, 1).setConquestThreshold(200.0f);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer5);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer6 = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer6[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.ENTS, 5);
        arrspawnListContainer6[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.HUORNS, 20);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer6);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer7 = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer7[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GONDOR_SOLDIERS, 10);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer7);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer8 = new LOTRBiomeSpawnList.SpawnListContainer[4];
        arrspawnListContainer8[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MORDOR_ORCS, 10);
        arrspawnListContainer8[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MORDOR_WARGS, 2);
        arrspawnListContainer8[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.BLACK_URUKS, 2).setConquestThreshold(50.0f);
        arrspawnListContainer8[3] = LOTRBiomeSpawnList.entry(LOTRSpawnList.OLOG_HAI, 1).setConquestThreshold(200.0f);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer8);
        variantChance = 0.3f;
        addBiomeVariantSet(LOTRBiomeVariant.SET_NORMAL_OAK);
        addBiomeVariant(LOTRBiomeVariant.BOULDERS_ROHAN);
        addBiomeVariant(LOTRBiomeVariant.FOREST_BEECH, 0.5f);
        addBiomeVariant(LOTRBiomeVariant.FOREST_BIRCH, 0.5f);
        addBiomeVariant(LOTRBiomeVariant.ORCHARD_APPLE_PEAR, 0.5f);
        decorator.addSoil(new LOTRVanillaWorldGens.Minable(LOTRLegacyBlocks.mod("rock"), 2, 60, LOTRLegacyBlocks.vanilla("stone")), 2.0f, 0, 64);
        decorator.setTreeCluster(12, 30);
        decorator.willowPerChunk = 1;
        decorator.flowersPerChunk = 4;
        decorator.grassPerChunk = 15;
        decorator.doubleGrassPerChunk = 5;
        decorator.addTree(LOTRTreeType.OAK, 400);
        decorator.addTree(LOTRTreeType.OAK_LARGE, 800);
        decorator.addTree(LOTRTreeType.BIRCH, 20);
        decorator.addTree(LOTRTreeType.BIRCH_LARGE, 10);
        decorator.addTree(LOTRTreeType.BEECH, 20);
        decorator.addTree(LOTRTreeType.BEECH_LARGE, 10);
        decorator.addTree(LOTRTreeType.PINE, 20);
        decorator.addTree(LOTRTreeType.APPLE, 2);
        decorator.addTree(LOTRTreeType.PEAR, 2);
        registerPlainsFlowers();
        addFlower(LOTRLegacyBlocks.mod("simbelmyne"), 0, 2);
        decorator.addRandomStructure(new LOTRRohanBarrowStructure(false), 600);
        decorator.addRandomStructure(new LOTRRohanWatchtowerStructure(false), 600);
        decorator.addRandomStructure(new LOTRSmallStoneRuinStructure(false), 500);
        decorator.addVillage(new LOTRVillageGenRohan((String) null, 0.6f));
        registerTravellingTrader(LOTREntities.GALADHRIM_TRADER);
        registerTravellingTrader(LOTREntities.IRON_HILLS_MERCHANT);
        registerTravellingTrader(LOTREntities.SCRAP_TRADER);
        registerTravellingTrader(LOTREntities.DALE_MERCHANT);
        registerTravellingTrader(LOTREntities.DORWINION_MERCHANT_MAN);
        setBanditChance(LOTREventSpawner.EventChance.BANDIT_RARE);
        invasionSpawns.addInvasion(LOTRInvasions.DUNLAND, LOTREventSpawner.EventChance.UNCOMMON);
        invasionSpawns.addInvasion(LOTRInvasions.URUK_HAI, LOTREventSpawner.EventChance.UNCOMMON);
    }

    @Override
    public void decorate(WorldGenLevel world, RandomSource random, int i, int k) {
        super.decorate(world, random, i, k);
        if (random.nextInt(32) == 0) {
            for (int l = 0; l < 3; ++l) {
                int i1 = i + random.nextInt(16) + 8;
                int k1 = k + random.nextInt(16) + 8;
                boulderGen.generate(world, random, i1, LOTRWorldGenUtil.getHeightValue(world, i1, k1), k1);
            }
        }
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_ROHAN;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.ROHAN.getSubregion("rohan");
    }

    @Override
    public LOTRWaypoint.Region getBiomeWaypoints() {
        return LOTRWaypoint.Region.ROHAN;
    }

    @Override
    public float getChanceToSpawnAnimals() {
        return 0.5f;
    }

    @Override
    public LOTRRoadType getRoadBlock() {
        return LOTRRoadType.ROHAN_MIX.setRepair(0.9f);
    }

    @Override
    public float getTreeIncreaseChance() {
        return 0.05f;
    }

    @Override
    public int spawnCountMultiplier() {
        return 3;
    }
}
