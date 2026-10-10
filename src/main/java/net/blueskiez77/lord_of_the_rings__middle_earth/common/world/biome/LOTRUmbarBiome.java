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
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRCorsairCampStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRMoredainMercCampStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRStoneRuinStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.village.LOTRVillageGenUmbar;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRBiomeGenUmbar.
 */
public class LOTRUmbarBiome extends LOTRBiome {

    public static LOTRNoiseGeneratorPerlin noiseDirt = new LOTRNoiseGeneratorPerlin(7849067306796L, 1);

    public static LOTRNoiseGeneratorPerlin noiseSand = new LOTRNoiseGeneratorPerlin(628602597026L, 1);

    public LOTRUmbarBiome(int i, boolean major) {
        super(i, major);
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.CAMEL, 4, 4, 4));
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.HORSE, 5, 4, 4));
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer = new LOTRBiomeSpawnList.SpawnListContainer[4];
        arrspawnListContainer[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.UMBARIANS, 120).setSpawnChance(100);
        arrspawnListContainer[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.UMBAR_SOLDIERS, 40).setSpawnChance(100);
        arrspawnListContainer[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.CORSAIRS, 40).setSpawnChance(100);
        arrspawnListContainer[3] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GONDOR_RENEGADES, 1).setSpawnChance(100);
        npcSpawnList.newFactionList(100, 0.0f).add(arrspawnListContainer);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer2 = new LOTRBiomeSpawnList.SpawnListContainer[4];
        arrspawnListContainer2[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.UMBAR_SOLDIERS, 50);
        arrspawnListContainer2[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.SOUTHRON_WARRIORS, 10);
        arrspawnListContainer2[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GULF_WARRIORS, 10);
        arrspawnListContainer2[3] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GONDOR_RENEGADES, 1);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer2);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer3 = new LOTRBiomeSpawnList.SpawnListContainer[3];
        arrspawnListContainer3[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GONDOR_SOLDIERS, 10);
        arrspawnListContainer3[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.PELARGIR_SOLDIERS, 5).setConquestThreshold(100.0f);
        arrspawnListContainer3[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.DOL_AMROTH_SOLDIERS, 5).setConquestThreshold(100.0f);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer3);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer4 = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer4[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.ROHIRRIM_WARRIORS, 10);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer4);
        npcSpawnList.conquestGainRate = 0.2f;
        variantChance = 0.3f;
        addBiomeVariant(LOTRBiomeVariant.FLOWERS);
        addBiomeVariant(LOTRBiomeVariant.FOREST);
        addBiomeVariant(LOTRBiomeVariant.FOREST_LIGHT);
        addBiomeVariant(LOTRBiomeVariant.HILLS);
        addBiomeVariant(LOTRBiomeVariant.HILLS_FOREST);
        addBiomeVariant(LOTRBiomeVariant.SHRUBLAND_OAK);
        addBiomeVariant(LOTRBiomeVariant.ORCHARD_ORANGE, 0.1f);
        addBiomeVariant(LOTRBiomeVariant.ORCHARD_LEMON, 0.1f);
        addBiomeVariant(LOTRBiomeVariant.ORCHARD_LIME, 0.1f);
        addBiomeVariant(LOTRBiomeVariant.ORCHARD_OLIVE, 0.1f);
        addBiomeVariant(LOTRBiomeVariant.ORCHARD_ALMOND, 0.1f);
        addBiomeVariant(LOTRBiomeVariant.ORCHARD_PLUM, 0.1f);
        decorator.addOre(new LOTRVanillaWorldGens.Minable(LOTRLegacyBlocks.vanilla("lapis_ore"), 6), 1.0f, 0, 48);
        decorator.grassPerChunk = 6;
        decorator.doubleGrassPerChunk = 1;
        decorator.flowersPerChunk = 3;
        decorator.doubleFlowersPerChunk = 1;
        decorator.addTree(LOTRTreeType.OAK_DESERT, 1000);
        decorator.addTree(LOTRTreeType.CEDAR, 300);
        decorator.addTree(LOTRTreeType.CYPRESS, 500);
        decorator.addTree(LOTRTreeType.CYPRESS_LARGE, 50);
        decorator.addTree(LOTRTreeType.PALM, 100);
        decorator.addTree(LOTRTreeType.DATE_PALM, 5);
        decorator.addTree(LOTRTreeType.LEMON, 2);
        decorator.addTree(LOTRTreeType.ORANGE, 2);
        decorator.addTree(LOTRTreeType.LIME, 2);
        decorator.addTree(LOTRTreeType.OLIVE, 5);
        decorator.addTree(LOTRTreeType.OLIVE_LARGE, 5);
        decorator.addTree(LOTRTreeType.PLUM, 2);
        registerHaradFlowers();
        biomeColors.setGrass(11914805);
        decorator.addRandomStructure(new LOTRMoredainMercCampStructure(false), 1500);
        decorator.addRandomStructure(new LOTRCorsairCampStructure(false), 800);
        decorator.addRandomStructure(new LOTRStoneRuinStructure.UMBAR(1, 3), 800);
        decorator.addRandomStructure(new LOTRStoneRuinStructure.NEAR_HARAD(1, 3), 800);
        decorator.addRandomStructure(new LOTRStoneRuinStructure.NUMENOR(1, 3), 2000);
        decorator.addVillage(new LOTRVillageGenUmbar((String) null, 0.9f));
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
        double d5;
        double d1 = noiseDirt.getValue(i * 0.002, k * 0.002);
        double d2 = noiseDirt.getValue(i * 0.07, k * 0.07);
        double d3 = noiseDirt.getValue(i * 0.25, k * 0.25);
        double d4 = noiseSand.getValue(i * 0.002, k * 0.002);
        d5 = noiseSand.getValue(i * 0.07, k * 0.07);
        if (d4 + d5 + noiseSand.getValue(i * 0.25, k * 0.25) > 1.1) {
            topBlock = LOTRLegacyBlocks.vanilla("sand").state(0);
            fillerBlock = topBlock;
        } else if (d1 + d2 + d3 > 0.6) {
            topBlock = LOTRLegacyBlocks.vanilla("dirt").state(1);
        }
        super.generateBiomeTerrain(worldSeed, random, terrain, i, k, stoneNoise, height, variant, topBlock, fillerBlock);
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_UMBAR;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.NEAR_HARAD.getSubregion("umbar");
    }

    @Override
    public LOTRWaypoint.Region getBiomeWaypoints() {
        return LOTRWaypoint.Region.UMBAR;
    }

    @Override
    public float getChanceToSpawnAnimals() {
        return 0.05f;
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
        return LOTRRoadType.UMBAR;
    }

    @Override
    public float getTreeIncreaseChance() {
        return 0.15f;
    }

    @Override
    public int spawnCountMultiplier() {
        return 2;
    }
}
