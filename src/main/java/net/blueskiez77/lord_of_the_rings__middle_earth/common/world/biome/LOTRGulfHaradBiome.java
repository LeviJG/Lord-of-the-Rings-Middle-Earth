package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRChunkTerrain;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRVanillaWorldGens;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenBoulder;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenDoubleFlower;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
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
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRMumakSkeletonStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRStoneRuinStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.village.LOTRVillageGenGulfHarad;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRBiomeGenGulfHarad.
 */
public class LOTRGulfHaradBiome extends LOTRBiome {

    public static LOTRNoiseGeneratorPerlin noiseDirt = new LOTRNoiseGeneratorPerlin(8359286029006L, 1);

    public static LOTRNoiseGeneratorPerlin noiseSand = new LOTRNoiseGeneratorPerlin(473689270272L, 1);

    public static LOTRNoiseGeneratorPerlin noiseRedSand = new LOTRNoiseGeneratorPerlin(3528569078920702727L, 1);

    public LOTRWorldGenerator boulderGen = new LOTRWorldGenBoulder(LOTRLegacyBlocks.vanilla("stone"), 0, 1, 3);

    public LOTRGulfHaradBiome(int i, boolean major) {
        super(i, major);
        spawnableCreatureList.clear();
        spawnableCreatureList.add(new LOTRSpawnEntry(EntityTypes.SHEEP, 12, 4, 4));
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.WILD_BOAR, 10, 4, 4));
        spawnableCreatureList.add(new LOTRSpawnEntry(EntityTypes.CHICKEN, 8, 4, 4));
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.AUROCHS, 6, 4, 4));
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.WHITE_ORYX, 12, 4, 4));
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.CAMEL, 2, 4, 4));
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.HORSE, 10, 4, 4));
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GULF_HARADRIM, 20).setSpawnChance(100);
        arrspawnListContainer[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GULF_WARRIORS, 5).setSpawnChance(100);
        npcSpawnList.newFactionList(100, 0.0f).add(arrspawnListContainer);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer2 = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer2[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GULF_WARRIORS, 10);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer2);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer3 = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer3[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GONDOR_SOLDIERS, 10);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer3);
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
        decorator.grassPerChunk = 8;
        decorator.doubleGrassPerChunk = 2;
        decorator.flowersPerChunk = 1;
        decorator.doubleFlowersPerChunk = 1;
        decorator.deadBushPerChunk = 1;
        decorator.cactiPerChunk = 1;
        decorator.addTree(LOTRTreeType.PALM, 500);
        decorator.addTree(LOTRTreeType.ACACIA, 300);
        decorator.addTree(LOTRTreeType.OAK_DESERT, 400);
        decorator.addTree(LOTRTreeType.DRAGONBLOOD, 200);
        decorator.addTree(LOTRTreeType.DRAGONBLOOD_LARGE, 10);
        decorator.addTree(LOTRTreeType.DATE_PALM, 50);
        decorator.addTree(LOTRTreeType.LEMON, 5);
        decorator.addTree(LOTRTreeType.ORANGE, 5);
        decorator.addTree(LOTRTreeType.LIME, 5);
        decorator.addTree(LOTRTreeType.OLIVE, 5);
        decorator.addTree(LOTRTreeType.OLIVE_LARGE, 10);
        decorator.addTree(LOTRTreeType.ALMOND, 5);
        decorator.addTree(LOTRTreeType.PLUM, 5);
        registerHaradFlowers();
        decorator.addRandomStructure(new LOTRHaradObeliskStructure(false), 3000);
        decorator.addRandomStructure(new LOTRStoneRuinStructure.NEAR_HARAD(1, 3), 500);
        decorator.addRandomStructure(new LOTRMoredainMercCampStructure(false), 1000);
        decorator.addRandomStructure(new LOTRMumakSkeletonStructure(false), 3000);
        decorator.addVillage(new LOTRVillageGenGulfHarad((String) null, 0.75f));
        registerTravellingTrader(LOTREntities.DORWINION_MERCHANT_MAN);
        registerTravellingTrader(LOTREntities.NOMAD_MERCHANT);
        setBanditChance(LOTREventSpawner.EventChance.BANDIT_RARE);
        setBanditEntityClass(LOTREntities.BANDIT_HARAD);
    }

    @Override
    public void decorate(WorldGenLevel world, RandomSource random, int i, int k) {
        super.decorate(world, random, i, k);
        if (random.nextInt(20) == 0) {
            int boulders = 1 + random.nextInt(3);
            for (int l = 0; l < boulders; ++l) {
                int i1 = i + random.nextInt(16) + 8;
                int k1 = k + random.nextInt(16) + 8;
                int j1 = LOTRWorldGenUtil.getHeightValue(world, i1, k1);
                boulderGen.generate(world, random, i1, j1, k1);
            }
        }
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
            if (d7 + noiseRedSand.getValue(i * 0.07, k * 0.07) + noiseRedSand.getValue(i * 0.25, k * 0.25) > 0.9) {
                topBlock = LOTRLegacyBlocks.vanilla("sand").state(1);
                fillerBlock = topBlock;
            } else if (d4 + d5 + d6 > 1.2) {
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
        return LOTRAchievement.ENTER_GULF_HARAD;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.NEAR_HARAD.getSubregion("gulf");
    }

    @Override
    public LOTRWaypoint.Region getBiomeWaypoints() {
        return LOTRWaypoint.Region.GULF_HARAD;
    }

    @Override
    public float getChanceToSpawnAnimals() {
        return 0.5f;
    }

    @Override
    public LOTRWorldGenerator getRandomWorldGenForDoubleFlower(RandomSource random) {
        LOTRWorldGenDoubleFlower doubleFlowerGen = new LOTRWorldGenDoubleFlower();
        if (random.nextInt(3) == 0) {
            doubleFlowerGen.setFlowerType(2);
        } else {
            doubleFlowerGen.setFlowerType(3);
        }
        return doubleFlowerGen;
    }

    @Override
    public LOTRRoadType getRoadBlock() {
        return LOTRRoadType.GULF_HARAD;
    }

    @Override
    public float getTreeIncreaseChance() {
        return 0.2f;
    }

    public boolean hasMixedHaradSoils() {
        return true;
    }

    @Override
    public int spawnCountMultiplier() {
        return 3;
    }
}
