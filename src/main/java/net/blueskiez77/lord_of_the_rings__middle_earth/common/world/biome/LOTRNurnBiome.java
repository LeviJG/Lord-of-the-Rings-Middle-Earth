package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenBoulder;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenerator;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRBiomeSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.orc.LOTRNurnWheatFarmStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.orc.LOTROrcSlaverTowerStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRSmallStoneRuinStructure;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

/**
 * LOTRBiomeGenNurn.
 */
public class LOTRNurnBiome extends LOTRMordorBiome {

    public LOTRWorldGenerator nurnBoulderGen = new LOTRWorldGenBoulder(LOTRLegacyBlocks.mod("rock"), 0, 1, 3);

    public LOTRNurnBiome(int i, boolean major) {
        super(i, major);
        topBlock = LOTRLegacyBlocks.vanilla("grass").state(0);
        fillerBlock = LOTRLegacyBlocks.vanilla("dirt").state(0);
        enableRain = true;
        npcSpawnList.clear();
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer = new LOTRBiomeSpawnList.SpawnListContainer[6];
        arrspawnListContainer[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MORDOR_ORCS, 30);
        arrspawnListContainer[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MORDOR_BOMBARDIERS, 5);
        arrspawnListContainer[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MORDOR_WARGS, 5).setConquestThreshold(50.0f);
        arrspawnListContainer[3] = LOTRBiomeSpawnList.entry(LOTRSpawnList.BLACK_URUKS, 2).setConquestThreshold(50.0f);
        arrspawnListContainer[4] = LOTRBiomeSpawnList.entry(LOTRSpawnList.BLACK_URUKS, 2).setConquestThreshold(100.0f);
        arrspawnListContainer[5] = LOTRBiomeSpawnList.entry(LOTRSpawnList.OLOG_HAI, 2).setConquestThreshold(200.0f);
        npcSpawnList.newFactionList(100).add(arrspawnListContainer);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer2 = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer2[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.ROHIRRIM_WARRIORS, 10);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer2);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer3 = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer3[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GONDOR_SOLDIERS, 10);
        arrspawnListContainer3[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.RANGERS_ITHILIEN, 3);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer3);
        addBiomeVariant(LOTRBiomeVariant.FOREST_LIGHT);
        addBiomeVariant(LOTRBiomeVariant.STEPPE);
        addBiomeVariant(LOTRBiomeVariant.STEPPE_BARREN);
        addBiomeVariant(LOTRBiomeVariant.HILLS);
        addBiomeVariant(LOTRBiomeVariant.HILLS_FOREST);
        addBiomeVariant(LOTRBiomeVariant.DEADFOREST_OAK);
        decorator.setTreeCluster(6, 30);
        decorator.willowPerChunk = 1;
        decorator.flowersPerChunk = 0;
        decorator.doubleFlowersPerChunk = 0;
        decorator.grassPerChunk = 8;
        decorator.dryReedChance = 0.6f;
        decorator.generateWater = true;
        decorator.addTree(LOTRTreeType.OAK, 500);
        decorator.addTree(LOTRTreeType.OAK_LARGE, 100);
        decorator.addTree(LOTRTreeType.OAK_DESERT, 500);
        decorator.addTree(LOTRTreeType.CEDAR, 100);
        decorator.addTree(LOTRTreeType.OAK_DEAD, 200);
        decorator.addTree(LOTRTreeType.CHARRED, 200);
        decorator.clearRandomStructures();
        decorator.addRandomStructure(new LOTRNurnWheatFarmStructure(false), 40);
        decorator.addRandomStructure(new LOTROrcSlaverTowerStructure(false), 200);
        decorator.addRandomStructure(new LOTRSmallStoneRuinStructure(false), 400);
        biomeColors.setGrass(10066237);
        biomeColors.setFoliage(7042874);
        biomeColors.setSky(10526098);
        biomeColors.resetClouds();
        biomeColors.resetFog();
        biomeColors.setWater(8877157);
    }

    @Override
    public void decorate(WorldGenLevel world, RandomSource random, int i, int k) {
        super.decorate(world, random, i, k);
        if (random.nextInt(40) == 0) {
            for (int l = 0; l < 4; ++l) {
                int i1 = i + random.nextInt(16) + 8;
                int k1 = k + random.nextInt(16) + 8;
                nurnBoulderGen.generate(world, random, i1, LOTRWorldGenUtil.getHeightValue(world, i1, k1), k1);
            }
        }
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_NURN;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.MORDOR.getSubregion("nurn");
    }

    @Override
    public LOTRWaypoint.Region getBiomeWaypoints() {
        return LOTRWaypoint.Region.NURN;
    }

    @Override
    public float getTreeIncreaseChance() {
        return 0.25f;
    }

    @Override
    public boolean isGorgoroth() {
        return false;
    }

    @Override
    public int spawnCountMultiplier() {
        return 3;
    }
}
