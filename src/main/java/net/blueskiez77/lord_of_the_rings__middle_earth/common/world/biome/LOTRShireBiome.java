package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTROrchardBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRVanillaWorldGens;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenClover;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRBiomeSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTREventSpawner;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnEntry;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.hobbit.LOTRHobbitBurrowStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.hobbit.LOTRHobbitFarmStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.hobbit.LOTRHobbitHoleStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.hobbit.LOTRHobbitHouseStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.hobbit.LOTRHobbitPicnicBenchStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.hobbit.LOTRHobbitTavernStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.hobbit.LOTRHobbitWindmillStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRStoneRuinStructure;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

/**
 * LOTRBiomeGenShire.
 */
public class LOTRShireBiome extends LOTRBiome {

    public LOTRBiomeSpawnList orcharderSpawnList = new LOTRBiomeSpawnList(getClass().getName());

    public LOTRShireBiome(int i, boolean major) {
        super(i, major);
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.SHIRE_PONY, 12, 2, 6));
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.HOBBITS, 10);
        npcSpawnList.newFactionList(100).add(arrspawnListContainer);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer2 = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer2[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GUNDABAD_ORCS, 10);
        arrspawnListContainer2[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GUNDABAD_WARGS, 3).setConquestThreshold(100.0f);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer2);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer3 = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer3[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.ANGMAR_ORCS, 10);
        arrspawnListContainer3[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.ANGMAR_WARGS, 3).setConquestThreshold(100.0f);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer3);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer4 = new LOTRBiomeSpawnList.SpawnListContainer[3];
        arrspawnListContainer4[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.RUFFIANS, 10);
        arrspawnListContainer4[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.ISENGARD_SNAGA, 10).setConquestThreshold(25.0f);
        arrspawnListContainer4[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.URUK_HAI, 5).setConquestThreshold(25.0f);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer4);
        npcSpawnList.conquestGainRate = 0.2f;
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer5 = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer5[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.HOBBITS_ORCHARD);
        orcharderSpawnList.newFactionList(100).add(arrspawnListContainer5);
        variantChance = 0.25f;
        addBiomeVariant(LOTRBiomeVariant.FLOWERS);
        addBiomeVariant(LOTRBiomeVariant.FOREST);
        addBiomeVariant(LOTRBiomeVariant.FOREST_LIGHT);
        addBiomeVariant(LOTRBiomeVariant.HILLS);
        addBiomeVariant(LOTRBiomeVariant.HILLS_FOREST);
        addBiomeVariant(LOTRBiomeVariant.FOREST_BIRCH, 0.5f);
        addBiomeVariant(LOTRBiomeVariant.FOREST_ASPEN, 0.5f);
        addBiomeVariant(LOTRBiomeVariant.ORCHARD_SHIRE, 1.0f);
        addBiomeVariant(LOTRBiomeVariant.ORCHARD_PLUM, 0.3f);
        decorator.willowPerChunk = 1;
        decorator.flowersPerChunk = 3;
        decorator.doubleFlowersPerChunk = 1;
        decorator.grassPerChunk = 6;
        decorator.generateLava = false;
        decorator.addTree(LOTRTreeType.OAK, 1000);
        decorator.addTree(LOTRTreeType.OAK_LARGE, 400);
        decorator.addTree(LOTRTreeType.OAK_PARTY, 10);
        decorator.addTree(LOTRTreeType.CHESTNUT, 250);
        decorator.addTree(LOTRTreeType.CHESTNUT_LARGE, 100);
        decorator.addTree(LOTRTreeType.BIRCH, 25);
        decorator.addTree(LOTRTreeType.BIRCH_LARGE, 10);
        decorator.addTree(LOTRTreeType.ASPEN, 50);
        decorator.addTree(LOTRTreeType.ASPEN_LARGE, 10);
        decorator.addTree(LOTRTreeType.APPLE, 5);
        decorator.addTree(LOTRTreeType.PEAR, 5);
        decorator.addTree(LOTRTreeType.CHERRY, 2);
        decorator.addTree(LOTRTreeType.PLUM, 5);
        registerPlainsFlowers();
        biomeColors.setGrass(8111137);
        if (hasShireStructures()) {
            if (getClass() == LOTRShireBiome.class) {
                decorator.addRandomStructure(new LOTRHobbitHoleStructure(false), 90);
                decorator.addRandomStructure(new LOTRHobbitBurrowStructure(false), 45);
                decorator.addRandomStructure(new LOTRHobbitHouseStructure(false), 45);
                decorator.addRandomStructure(new LOTRHobbitTavernStructure(false), 70);
                decorator.addRandomStructure(new LOTRHobbitWindmillStructure(false), 600);
                decorator.addRandomStructure(new LOTRHobbitFarmStructure(false), 500);
            }
            decorator.addRandomStructure(new LOTRHobbitPicnicBenchStructure(false), 40);
            decorator.addRandomStructure(new LOTRStoneRuinStructure.STONE(1, 4), 1500);
            decorator.addRandomStructure(new LOTRStoneRuinStructure.ARNOR(1, 4), 1500);
        }
        registerTravellingTrader(LOTREntities.GALADHRIM_TRADER);
        registerTravellingTrader(LOTREntities.BLUE_DWARF_MERCHANT);
        registerTravellingTrader(LOTREntities.IRON_HILLS_MERCHANT);
        registerTravellingTrader(LOTREntities.SCRAP_TRADER);
        registerTravellingTrader(LOTREntities.DALE_MERCHANT);
        registerTravellingTrader(LOTREntities.RIVENDELL_TRADER);
        setBanditChance(LOTREventSpawner.EventChance.NEVER);
    }

    @Override
    public void decorate(WorldGenLevel world, RandomSource random, int i, int k) {
        int k1;
        int j1;
        int i1;
        super.decorate(world, random, i, k);
        for (int l = 0; l < decorator.grassPerChunk / 2; ++l) {
            int i12 = i + random.nextInt(16) + 8;
            int j12 = random.nextInt(128);
            int k12 = k + random.nextInt(16) + 8;
            new LOTRWorldGenClover().generate(world, random, i12, j12, k12);
        }
        if (random.nextInt(6) == 0) {
            i1 = i + random.nextInt(16) + 8;
            j1 = random.nextInt(128);
            k1 = k + random.nextInt(16) + 8;
            new LOTRVanillaWorldGens.Flowers(LOTRLegacyBlocks.mod("pipeweedPlant")).generate(world, random, i1, j1, k1);
        }
        if (decorator.doubleFlowersPerChunk > 0 && random.nextInt(6) == 0) {
            i1 = i + random.nextInt(16) + 8;
            j1 = random.nextInt(128);
            k1 = k + random.nextInt(16) + 8;
            LOTRVanillaWorldGens.DoublePlant doubleFlowerGen = new LOTRVanillaWorldGens.DoublePlant();
            doubleFlowerGen.func_150548_a(0);
            doubleFlowerGen.generate(world, random, i1, j1, k1);
        }
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.SHIRE.getSubregion("shire");
    }

    @Override
    public LOTRWaypoint.Region getBiomeWaypoints() {
        return LOTRWaypoint.Region.SHIRE;
    }

    @Override
    public LOTRBiomeSpawnList getNPCSpawnList(WorldGenLevel world, RandomSource random, int i, int j, int k, LOTRBiomeVariant variant) {
        if (variant instanceof LOTROrchardBiomeVariant && random.nextFloat() < 0.3f) {
            return orcharderSpawnList;
        }
        return super.getNPCSpawnList(world, random, i, j, k, variant);
    }

    @Override
    public float getTreeIncreaseChance() {
        return 0.5f;
    }

    @Override
    public boolean hasDomesticAnimals() {
        return true;
    }

    public boolean hasShireStructures() {
        return true;
    }

    @Override
    public int spawnCountMultiplier() {
        return 3;
    }
}
