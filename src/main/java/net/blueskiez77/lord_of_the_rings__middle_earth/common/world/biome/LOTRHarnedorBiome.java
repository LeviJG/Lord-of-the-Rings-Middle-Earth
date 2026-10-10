package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRChunkTerrain;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRVanillaWorldGens;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenBoulder;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenerator;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRRoadType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.noise.LOTRNoiseGeneratorPerlin;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRBiomeSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTREventSpawner;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRInvasions;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnEntry;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRHarnedorTowerStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRMoredainMercCampStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRMumakSkeletonStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRNearHaradDesertCampStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRSmallStoneRuinStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.village.LOTRVillageGenHarnedor;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRBiomeGenHarnedor.
 */
public class LOTRHarnedorBiome extends LOTRBiome {

    public static LOTRNoiseGeneratorPerlin noiseDirt = new LOTRNoiseGeneratorPerlin(3869098386927266L, 1);

    public static LOTRNoiseGeneratorPerlin noiseSand = new LOTRNoiseGeneratorPerlin(92726978206783582L, 1);

    public LOTRWorldGenerator boulderGen = new LOTRWorldGenBoulder(LOTRLegacyBlocks.vanilla("stone"), 0, 1, 2);

    public LOTRHarnedorBiome(int i, boolean major) {
        super(i, major);
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.HORSE, 10, 4, 4));
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.HARNEDHRIM, 20).setSpawnChance(100);
        arrspawnListContainer[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.HARNEDOR_WARRIORS, 30).setSpawnChance(100);
        npcSpawnList.newFactionList(100, 0.0f).add(arrspawnListContainer);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer2 = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer2[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.HARNEDOR_WARRIORS, 10);
        arrspawnListContainer2[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.SOUTHRON_WARRIORS, 2);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer2);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer3 = new LOTRBiomeSpawnList.SpawnListContainer[5];
        arrspawnListContainer3[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.RANGERS_ITHILIEN, 10);
        arrspawnListContainer3[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GONDOR_SOLDIERS, 5);
        arrspawnListContainer3[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GONDOR_SOLDIERS, 5).setConquestThreshold(50.0f);
        arrspawnListContainer3[3] = LOTRBiomeSpawnList.entry(LOTRSpawnList.LOSSARNACH_SOLDIERS, 2).setConquestThreshold(50.0f);
        arrspawnListContainer3[4] = LOTRBiomeSpawnList.entry(LOTRSpawnList.LEBENNIN_SOLDIERS, 2).setConquestThreshold(50.0f);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer3);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer4 = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer4[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.ROHIRRIM_WARRIORS, 10);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer4);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer5 = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer5[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MORDOR_ORCS, 10);
        arrspawnListContainer5[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.BLACK_URUKS, 3).setConquestThreshold(50.0f);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer5);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer6 = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer6[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.EASTERLING_WARRIORS, 10);
        arrspawnListContainer6[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.EASTERLING_GOLD_WARRIORS, 2).setConquestThreshold(50.0f);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer6);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer7 = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer7[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MORWAITH_WARRIORS, 10);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer7);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer8 = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer8[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.HALF_TROLLS, 10);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer8);
        addBiomeVariant(LOTRBiomeVariant.FOREST);
        addBiomeVariant(LOTRBiomeVariant.FOREST_LIGHT);
        addBiomeVariant(LOTRBiomeVariant.STEPPE);
        addBiomeVariant(LOTRBiomeVariant.STEPPE_BARREN);
        addBiomeVariant(LOTRBiomeVariant.HILLS);
        addBiomeVariant(LOTRBiomeVariant.HILLS_FOREST);
        addBiomeVariant(LOTRBiomeVariant.SHRUBLAND_OAK, 2.0f);
        addBiomeVariant(LOTRBiomeVariant.SCRUBLAND_SAND, 2.0f);
        addBiomeVariant(LOTRBiomeVariant.HILLS_SCRUBLAND_SAND);
        addBiomeVariant(LOTRBiomeVariant.WASTELAND_SAND);
        addBiomeVariant(LOTRBiomeVariant.DEADFOREST_OAK);
        decorator.addOre(new LOTRVanillaWorldGens.Minable(LOTRLegacyBlocks.vanilla("lapis_ore"), 6), 1.0f, 0, 48);
        decorator.setTreeCluster(3, 30);
        decorator.grassPerChunk = 8;
        decorator.doubleGrassPerChunk = 1;
        decorator.flowersPerChunk = 3;
        decorator.cactiPerChunk = 1;
        decorator.deadBushPerChunk = 1;
        decorator.addTree(LOTRTreeType.OAK_DESERT, 1000);
        decorator.addTree(LOTRTreeType.CEDAR, 250);
        decorator.addTree(LOTRTreeType.LEMON, 5);
        decorator.addTree(LOTRTreeType.ORANGE, 5);
        decorator.addTree(LOTRTreeType.LIME, 5);
        decorator.addTree(LOTRTreeType.OLIVE, 5);
        decorator.addTree(LOTRTreeType.OLIVE_LARGE, 5);
        decorator.addTree(LOTRTreeType.ALMOND, 5);
        decorator.addTree(LOTRTreeType.PLUM, 5);
        registerHaradFlowers();
        biomeColors.setGrass(14538086);
        decorator.addRandomStructure(new LOTRNearHaradDesertCampStructure(false), 3000);
        decorator.addRandomStructure(new LOTRSmallStoneRuinStructure(false), 600);
        decorator.addRandomStructure(new LOTRHarnedorTowerStructure(false), 800);
        decorator.addRandomStructure(new LOTRMoredainMercCampStructure(false), 2000);
        decorator.addRandomStructure(new LOTRMumakSkeletonStructure(false), 6000);
        decorator.addVillage(new LOTRVillageGenHarnedor((String) null, 0.75f));
        registerTravellingTrader(LOTREntities.SCRAP_TRADER);
        registerTravellingTrader(LOTREntities.DORWINION_MERCHANT_MAN);
        registerTravellingTrader(LOTREntities.NOMAD_MERCHANT);
        setBanditChance(LOTREventSpawner.EventChance.BANDIT_UNCOMMON);
        setBanditEntityClass(LOTREntities.BANDIT_HARAD);
        invasionSpawns.addInvasion(LOTRInvasions.GONDOR, LOTREventSpawner.EventChance.RARE);
        invasionSpawns.addInvasion(LOTRInvasions.GONDOR_ITHILIEN, LOTREventSpawner.EventChance.UNCOMMON);
        invasionSpawns.addInvasion(LOTRInvasions.NEAR_HARAD_HARNEDOR, LOTREventSpawner.EventChance.UNCOMMON);
        invasionSpawns.addInvasion(LOTRInvasions.NEAR_HARAD_COAST, LOTREventSpawner.EventChance.RARE);
        invasionSpawns.addInvasion(LOTRInvasions.NEAR_HARAD_CORSAIR, LOTREventSpawner.EventChance.RARE);
    }

    @Override
    public void decorate(WorldGenLevel world, RandomSource random, int i, int k) {
        super.decorate(world, random, i, k);
        if (random.nextInt(12) == 0) {
            int boulders = 1 + random.nextInt(4);
            for (int l = 0; l < boulders; ++l) {
                int i1 = i + random.nextInt(16) + 8;
                int k1 = k + random.nextInt(16) + 8;
                boulderGen.generate(world, random, i1, LOTRWorldGenUtil.getHeightValue(world, i1, k1), k1);
            }
        }
    }

    @Override
    public void generateBiomeTerrain(long worldSeed, RandomSource random, LOTRChunkTerrain terrain, int i, int k,
                                     double stoneNoise, int height, LOTRBiomeVariant variant) {
        BlockState topBlock = this.topBlock;
        BlockState fillerBlock = this.fillerBlock;
        double d1 = noiseDirt.getValue(i * 0.09, k * 0.09);
        double d2 = noiseDirt.getValue(i * 0.4, k * 0.4);
        double d3 = noiseSand.getValue(i * 0.09, k * 0.09);
        if (d3 + noiseSand.getValue(i * 0.4, k * 0.4) > 0.6) {
            topBlock = LOTRLegacyBlocks.vanilla("sand").state(0);
            fillerBlock = topBlock;
        } else if (d1 + d2 > 0.2) {
            topBlock = LOTRLegacyBlocks.vanilla("dirt").state(1);
            fillerBlock = topBlock;
        }
        super.generateBiomeTerrain(worldSeed, random, terrain, i, k, stoneNoise, height, variant, topBlock, fillerBlock);
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_HARNEDOR;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.NEAR_HARAD.getSubregion("harnedor");
    }

    @Override
    public LOTRWaypoint.Region getBiomeWaypoints() {
        return LOTRWaypoint.Region.HARNEDOR;
    }

    @Override
    public float getChanceToSpawnAnimals() {
        return 0.25f;
    }

    @Override
    public LOTRBiome.GrassBlockAndMeta getRandomGrass(RandomSource random) {
        if (random.nextBoolean()) {
            return new LOTRBiome.GrassBlockAndMeta(LOTRLegacyBlocks.mod("aridGrass"), 0);
        }
        return super.getRandomGrass(random);
    }

    @Override
    public LOTRRoadType getRoadBlock() {
        return LOTRRoadType.HARAD_PATH;
    }

    @Override
    public int spawnCountMultiplier() {
        return 3;
    }
}
