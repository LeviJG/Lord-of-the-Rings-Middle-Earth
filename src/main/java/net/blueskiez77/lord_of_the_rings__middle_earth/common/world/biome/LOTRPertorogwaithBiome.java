package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenBoulder;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenSkullPile;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenerator;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRBiomeSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTREventSpawner;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnEntry;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.halftroll.LOTRHalfTrollHouseStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.halftroll.LOTRHalfTrollWarlordHouseStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRStoneRuinStructure;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

/**
 * LOTRBiomeGenPertorogwaith.
 */
public class LOTRPertorogwaithBiome extends LOTRBiome {

    public LOTRWorldGenerator boulderGen = new LOTRWorldGenBoulder(LOTRLegacyBlocks.vanilla("stone"), 0, 1, 3);

    public LOTRWorldGenerator clayBoulderGen = new LOTRWorldGenBoulder(LOTRLegacyBlocks.vanilla("hardened_clay"), 0, 1, 3);

    public LOTRWorldGenerator deadMoundGen = new LOTRWorldGenBoulder(LOTRLegacyBlocks.mod("wasteBlock"), 0, 1, 3);

    public LOTRPertorogwaithBiome(int i, boolean major) {
        super(i, major);
        spawnableCreatureList.clear();
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.RHINO, 8, 4, 4));
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.GEMSBOK, 4, 4, 4));
        spawnableLOTRAmbientList.clear();
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.HALF_TROLLS, 10);
        npcSpawnList.newFactionList(100).add(arrspawnListContainer);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer2 = new LOTRBiomeSpawnList.SpawnListContainer[3];
        arrspawnListContainer2[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MORDOR_ORCS, 10);
        arrspawnListContainer2[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.BLACK_URUKS, 2).setConquestThreshold(50.0f);
        arrspawnListContainer2[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.OLOG_HAI, 1).setConquestThreshold(200.0f);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer2);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer3 = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer3[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GULF_WARRIORS, 10);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer3);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer4 = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer4[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MORWAITH_WARRIORS, 10);
        arrspawnListContainer4[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MORWAITH, 3);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer4);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer5 = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer5[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.TAURETHRIM_WARRIORS, 10);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer5);
        variantChance = 0.6f;
        addBiomeVariant(LOTRBiomeVariant.FOREST);
        addBiomeVariant(LOTRBiomeVariant.FOREST_LIGHT);
        addBiomeVariant(LOTRBiomeVariant.STEPPE);
        addBiomeVariant(LOTRBiomeVariant.STEPPE_BARREN);
        addBiomeVariant(LOTRBiomeVariant.HILLS);
        addBiomeVariant(LOTRBiomeVariant.HILLS_FOREST);
        addBiomeVariant(LOTRBiomeVariant.DEADFOREST_OAK);
        addBiomeVariant(LOTRBiomeVariant.SHRUBLAND_OAK);
        addBiomeVariant(LOTRBiomeVariant.SCRUBLAND, 3.0f);
        addBiomeVariant(LOTRBiomeVariant.HILLS_SCRUBLAND, 2.0f);
        addBiomeVariant(LOTRBiomeVariant.WASTELAND, 4.0f);
        decorator.grassPerChunk = 10;
        decorator.doubleGrassPerChunk = 4;
        decorator.flowersPerChunk = 0;
        decorator.canePerChunk = 10;
        decorator.addTree(LOTRTreeType.OAK_DESERT, 50);
        decorator.addTree(LOTRTreeType.OAK_DEAD, 100);
        decorator.addTree(LOTRTreeType.ACACIA, 100);
        decorator.addTree(LOTRTreeType.ACACIA_DEAD, 200);
        decorator.addTree(LOTRTreeType.BAOBAB, 10);
        registerHaradFlowers();
        decorator.addRandomStructure(new LOTRHalfTrollHouseStructure(false), 40);
        decorator.addRandomStructure(new LOTRHalfTrollWarlordHouseStructure(false), 200);
        decorator.addRandomStructure(new LOTRStoneRuinStructure.MORDOR(1, 3), 100);
        biomeColors.setSky(8551538);
        biomeColors.setClouds(7500401);
        biomeColors.setFog(7500401);
        biomeColors.setWater(9080439);
        setBanditChance(LOTREventSpawner.EventChance.NEVER);
    }

    @Override
    public boolean canSpawnHostilesInDay() {
        return true;
    }

    @Override
    public void decorate(WorldGenLevel world, RandomSource random, int i, int k) {
        int boulders;
        int i1;
        int k1;
        int l;
        super.decorate(world, random, i, k);
        if (random.nextInt(6) == 0) {
            boulders = 1 + random.nextInt(4);
            for (l = 0; l < boulders; ++l) {
                i1 = i + random.nextInt(16) + 8;
                k1 = k + random.nextInt(16) + 8;
                boulderGen.generate(world, random, i1, LOTRWorldGenUtil.getHeightValue(world, i1, k1), k1);
            }
        }
        if (random.nextInt(12) == 0) {
            boulders = 1 + random.nextInt(4);
            for (l = 0; l < boulders; ++l) {
                i1 = i + random.nextInt(16) + 8;
                k1 = k + random.nextInt(16) + 8;
                clayBoulderGen.generate(world, random, i1, LOTRWorldGenUtil.getHeightValue(world, i1, k1), k1);
            }
        }
        if (random.nextInt(40) == 0) {
            for (int l2 = 0; l2 < 3; ++l2) {
                int i12 = i + random.nextInt(16) + 8;
                int k12 = k + random.nextInt(16) + 8;
                int j1 = LOTRWorldGenUtil.getHeightValue(world, i12, k12);
                deadMoundGen.generate(world, random, i12, j1, k12);
                new LOTRWorldGenSkullPile().generate(world, random, i12, j1, k12);
            }
        }
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_PERTOROGWAITH;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.PERDOROGWAITH.getSubregion("pertorogwaith");
    }

    @Override
    public LOTRWaypoint.Region getBiomeWaypoints() {
        return LOTRWaypoint.Region.PERTOROGWAITH;
    }

    @Override
    public float getChanceToSpawnAnimals() {
        return 0.05f;
    }

    @Override
    public float getTreeIncreaseChance() {
        return 0.25f;
    }

    @Override
    public int spawnCountMultiplier() {
        return 2;
    }
}
