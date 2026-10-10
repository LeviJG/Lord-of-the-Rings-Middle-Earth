package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRChunkTerrain;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRVanillaWorldGens;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenDoubleFlower;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenerator;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRRoadType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.noise.LOTRNoiseGeneratorPerlin;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRBiomeSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTREventSpawner;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnEntry;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRHaradObeliskStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRMoredainMercCampStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRStoneRuinStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.village.LOTRVillageGenSouthron;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRBiomeGenNearHaradFertile.
 */
public class LOTRNearHaradFertileBiome extends LOTRBiome {

    public static LOTRNoiseGeneratorPerlin noiseDirt = new LOTRNoiseGeneratorPerlin(12960262626062L, 1);

    public static LOTRNoiseGeneratorPerlin noiseSand = new LOTRNoiseGeneratorPerlin(17860128964L, 1);

    public static LOTRNoiseGeneratorPerlin noiseRedSand = new LOTRNoiseGeneratorPerlin(358960629620L, 1);

    public LOTRNearHaradFertileBiome(int i, boolean major) {
        super(i, major);
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.CAMEL, 6, 4, 4));
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.HORSE, 15, 4, 4));
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.COAST_SOUTHRONS, 20).setSpawnChance(100);
        arrspawnListContainer[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.SOUTHRON_WARRIORS, 15).setSpawnChance(100);
        npcSpawnList.newFactionList(100, 0.0f).add(arrspawnListContainer);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer2 = new LOTRBiomeSpawnList.SpawnListContainer[3];
        arrspawnListContainer2[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.SOUTHRON_WARRIORS, 10);
        arrspawnListContainer2[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.HARNEDOR_WARRIORS, 2);
        arrspawnListContainer2[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.UMBAR_SOLDIERS, 2);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer2);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer3 = new LOTRBiomeSpawnList.SpawnListContainer[4];
        arrspawnListContainer3[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.RANGERS_ITHILIEN, 10);
        arrspawnListContainer3[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GONDOR_SOLDIERS, 5);
        arrspawnListContainer3[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GONDOR_SOLDIERS, 5).setConquestThreshold(50.0f);
        arrspawnListContainer3[3] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GONDOR_SOLDIERS, 5).setConquestThreshold(100.0f);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer3);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer4 = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer4[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.ROHIRRIM_WARRIORS, 10);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer4);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer5 = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer5[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MORDOR_ORCS, 10);
        arrspawnListContainer5[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.BLACK_URUKS, 3).setConquestThreshold(50.0f);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer5);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer6 = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer6[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MORWAITH_WARRIORS, 10);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer6);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer7 = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer7[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.HALF_TROLLS, 10);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer7);
        addBiomeVariant(LOTRBiomeVariant.FLOWERS);
        addBiomeVariant(LOTRBiomeVariant.FOREST);
        addBiomeVariant(LOTRBiomeVariant.FOREST_LIGHT);
        addBiomeVariant(LOTRBiomeVariant.STEPPE);
        addBiomeVariant(LOTRBiomeVariant.STEPPE_BARREN);
        addBiomeVariant(LOTRBiomeVariant.HILLS);
        addBiomeVariant(LOTRBiomeVariant.HILLS_FOREST);
        addBiomeVariant(LOTRBiomeVariant.SHRUBLAND_OAK);
        addBiomeVariant(LOTRBiomeVariant.ORCHARD_ORANGE, 0.2f);
        addBiomeVariant(LOTRBiomeVariant.ORCHARD_LEMON, 0.2f);
        addBiomeVariant(LOTRBiomeVariant.ORCHARD_LIME, 0.2f);
        addBiomeVariant(LOTRBiomeVariant.ORCHARD_OLIVE, 0.2f);
        addBiomeVariant(LOTRBiomeVariant.ORCHARD_ALMOND, 0.2f);
        addBiomeVariant(LOTRBiomeVariant.ORCHARD_PLUM, 0.2f);
        addBiomeVariant(LOTRBiomeVariant.ORCHARD_DATE, 0.2f);
        addBiomeVariant(LOTRBiomeVariant.SCRUBLAND_SAND);
        addBiomeVariant(LOTRBiomeVariant.HILLS_SCRUBLAND_SAND);
        decorator.addOre(new LOTRVanillaWorldGens.Minable(LOTRLegacyBlocks.vanilla("lapis_ore"), 6), 1.0f, 0, 48);
        decorator.grassPerChunk = 10;
        decorator.doubleGrassPerChunk = 2;
        decorator.flowersPerChunk = 3;
        decorator.doubleFlowersPerChunk = 1;
        decorator.deadBushPerChunk = 1;
        decorator.cactiPerChunk = 1;
        decorator.addTree(LOTRTreeType.CEDAR, 800);
        decorator.addTree(LOTRTreeType.OAK_DESERT, 500);
        decorator.addTree(LOTRTreeType.DATE_PALM, 50);
        decorator.addTree(LOTRTreeType.CYPRESS, 400);
        decorator.addTree(LOTRTreeType.CYPRESS_LARGE, 50);
        decorator.addTree(LOTRTreeType.PALM, 100);
        decorator.addTree(LOTRTreeType.LEMON, 5);
        decorator.addTree(LOTRTreeType.ORANGE, 5);
        decorator.addTree(LOTRTreeType.LIME, 5);
        decorator.addTree(LOTRTreeType.OLIVE, 5);
        decorator.addTree(LOTRTreeType.OLIVE_LARGE, 10);
        decorator.addTree(LOTRTreeType.ALMOND, 5);
        decorator.addTree(LOTRTreeType.PLUM, 5);
        registerHaradFlowers();
        biomeColors.setGrass(11914805);
        decorator.addRandomStructure(new LOTRHaradObeliskStructure(false), 3000);
        decorator.addRandomStructure(new LOTRMoredainMercCampStructure(false), 1000);
        decorator.addRandomStructure(new LOTRStoneRuinStructure.NEAR_HARAD(1, 3), 300);
        decorator.addRandomStructure(new LOTRStoneRuinStructure.NUMENOR(1, 3), 4000);
        decorator.addVillage(new LOTRVillageGenSouthron((String) null, 0.6f));
        registerTravellingTrader(LOTREntities.SCRAP_TRADER);
        registerTravellingTrader(LOTREntities.DORWINION_MERCHANT_MAN);
        registerTravellingTrader(LOTREntities.NOMAD_MERCHANT);
        setBanditChance(LOTREventSpawner.EventChance.BANDIT_RARE);
        setBanditEntityClass(LOTREntities.BANDIT_HARAD);
    }

    @Override
    public void generateBiomeTerrain(long worldSeed, RandomSource random, LOTRChunkTerrain terrain, int i, int k,
                                     double stoneNoise, int height, LOTRBiomeVariant variant) {
        BlockState topBlock = this.topBlock;
        BlockState fillerBlock = this.fillerBlock;
        if (hasMixedHaradSoils()) {
            double d1 = noiseDirt.getValue(i * 0.002, k * 0.002);
            double d2 = noiseDirt.getValue(i * 0.07, k * 0.07);
            double d3 = noiseDirt.getValue(i * 0.25, k * 0.25);
            double d4 = noiseSand.getValue(i * 0.002, k * 0.002);
            double d5 = noiseSand.getValue(i * 0.07, k * 0.07);
            double d6 = noiseSand.getValue(i * 0.25, k * 0.25);
            double d7 = noiseRedSand.getValue(i * 0.002, k * 0.002);
            if (d7 + noiseRedSand.getValue(i * 0.07, k * 0.07) + noiseRedSand.getValue(i * 0.25, k * 0.25) > 1.6) {
                topBlock = LOTRLegacyBlocks.vanilla("sand").state(1);
                fillerBlock = topBlock;
            } else if (d4 + d5 + d6 > 0.9) {
                topBlock = LOTRLegacyBlocks.vanilla("sand").state(0);
                fillerBlock = topBlock;
            } else if (d1 + d2 + d3 > 0.4) {
                topBlock = LOTRLegacyBlocks.vanilla("dirt").state(1);
            }
        }
        super.generateBiomeTerrain(worldSeed, random, terrain, i, k, stoneNoise, height, variant, topBlock, fillerBlock);
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_SOUTHRON_COASTS;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.NEAR_HARAD.getSubregion("fertile");
    }

    @Override
    public LOTRWaypoint.Region getBiomeWaypoints() {
        return LOTRWaypoint.Region.SOUTHRON_COASTS;
    }

    @Override
    public float getChanceToSpawnAnimals() {
        return 0.5f;
    }

    @Override
    public LOTRWorldGenerator getRandomWorldGenForDoubleFlower(RandomSource random) {
        LOTRWorldGenDoubleFlower doubleFlowerGen = new LOTRWorldGenDoubleFlower();
        if (random.nextInt(5) == 0) {
            doubleFlowerGen.setFlowerType(3);
        } else {
            doubleFlowerGen.setFlowerType(2);
        }
        return doubleFlowerGen;
    }

    @Override
    public LOTRRoadType getRoadBlock() {
        return LOTRRoadType.HARAD_PATH;
    }

    @Override
    public float getTreeIncreaseChance() {
        return 0.3f;
    }

    public boolean hasMixedHaradSoils() {
        return true;
    }

    @Override
    public int spawnCountMultiplier() {
        return 3;
    }
}
