package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRChunkTerrain;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenBoulder;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenDoubleFlower;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenerator;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRRoadType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRRoads;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRBiomeSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTREventSpawner;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRInvasions;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnEntry;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnList;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.elf.LOTRDorwinionBathStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.elf.LOTRDorwinionBreweryStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.elf.LOTRDorwinionCampStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.elf.LOTRDorwinionElfHouseStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.elf.LOTRDorwinionGardenStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.elf.LOTRDorwinionHouseStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRStoneRuinStructure;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRBiomeGenDorwinion.
 */
public class LOTRDorwinionBiome extends LOTRBiome {

    public LOTRWorldGenerator boulderGen = new LOTRWorldGenBoulder(LOTRLegacyBlocks.mod("rock"), 5, 1, 2);

    public LOTRBiomeSpawnList vineyardSpawnList = new LOTRBiomeSpawnList(getClass().getName());

    public LOTRDorwinionBiome(int i, boolean major) {
        super(i, major);
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.HORSE, 5, 2, 6));
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.KINE_OF_ARAW, 6, 4, 4));
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.BEAR, 2, 1, 4));
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer = new LOTRBiomeSpawnList.SpawnListContainer[4];
        arrspawnListContainer[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.DORWINION_MEN, 30);
        arrspawnListContainer[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.DORWINION_GUARDS, 10);
        arrspawnListContainer[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.DORWINION_ELVES, 5);
        arrspawnListContainer[3] = LOTRBiomeSpawnList.entry(LOTRSpawnList.DORWINION_ELF_WARRIORS, 2);
        npcSpawnList.newFactionList(100).add(arrspawnListContainer);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer2 = new LOTRBiomeSpawnList.SpawnListContainer[3];
        arrspawnListContainer2[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.DOL_GULDUR_ORCS, 10);
        arrspawnListContainer2[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MIRKWOOD_SPIDERS, 2).setConquestThreshold(50.0f);
        arrspawnListContainer2[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MIRK_TROLLS, 1).setConquestThreshold(200.0f);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer2);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer3 = new LOTRBiomeSpawnList.SpawnListContainer[2];
        arrspawnListContainer3[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GONDOR_SOLDIERS, 10);
        arrspawnListContainer3[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.GONDOR_MEN, 5).setConquestThreshold(100.0f);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer3);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer4 = new LOTRBiomeSpawnList.SpawnListContainer[4];
        arrspawnListContainer4[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MORDOR_ORCS, 10);
        arrspawnListContainer4[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.MORDOR_WARGS, 2);
        arrspawnListContainer4[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.BLACK_URUKS, 2).setConquestThreshold(50.0f);
        arrspawnListContainer4[3] = LOTRBiomeSpawnList.entry(LOTRSpawnList.OLOG_HAI, 1).setConquestThreshold(200.0f);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer4);
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer5 = new LOTRBiomeSpawnList.SpawnListContainer[4];
        arrspawnListContainer5[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.EASTERLING_WARRIORS, 10);
        arrspawnListContainer5[1] = LOTRBiomeSpawnList.entry(LOTRSpawnList.EASTERLING_GOLD_WARRIORS, 1);
        arrspawnListContainer5[2] = LOTRBiomeSpawnList.entry(LOTRSpawnList.EASTERLING_GOLD_WARRIORS, 2).setConquestThreshold(50.0f);
        arrspawnListContainer5[3] = LOTRBiomeSpawnList.entry(LOTRSpawnList.EASTERLINGS, 5).setConquestThreshold(200.0f);
        npcSpawnList.newFactionList(0).add(arrspawnListContainer5);
        npcSpawnList.conquestGainRate = 0.75f;
        LOTRBiomeSpawnList.SpawnListContainer[] arrspawnListContainer6 = new LOTRBiomeSpawnList.SpawnListContainer[1];
        arrspawnListContainer6[0] = LOTRBiomeSpawnList.entry(LOTRSpawnList.DORWINION_VINEYARDS, 10);
        vineyardSpawnList.newFactionList(100).add(arrspawnListContainer6);
        variantChance = 0.3f;
        addBiomeVariantSet(LOTRBiomeVariant.SET_NORMAL_OAK);
        addBiomeVariant(LOTRBiomeVariant.VINEYARD, 8.0f);
        addBiomeVariant(LOTRBiomeVariant.FOREST_BEECH, 0.5f);
        addBiomeVariant(LOTRBiomeVariant.FOREST_BIRCH, 0.5f);
        addBiomeVariant(LOTRBiomeVariant.ORCHARD_OLIVE, 0.5f);
        addBiomeVariant(LOTRBiomeVariant.ORCHARD_APPLE_PEAR, 0.2f);
        addBiomeVariant(LOTRBiomeVariant.ORCHARD_ALMOND, 0.2f);
        addBiomeVariant(LOTRBiomeVariant.ORCHARD_PLUM, 0.2f);
        decorator.setTreeCluster(8, 20);
        decorator.willowPerChunk = 1;
        decorator.flowersPerChunk = 6;
        decorator.doubleFlowersPerChunk = 1;
        decorator.grassPerChunk = 8;
        decorator.doubleGrassPerChunk = 2;
        decorator.addTree(LOTRTreeType.OAK, 200);
        decorator.addTree(LOTRTreeType.OAK_LARGE, 100);
        decorator.addTree(LOTRTreeType.BIRCH, 50);
        decorator.addTree(LOTRTreeType.BIRCH_TALL, 50);
        decorator.addTree(LOTRTreeType.BIRCH_LARGE, 50);
        decorator.addTree(LOTRTreeType.BEECH, 20);
        decorator.addTree(LOTRTreeType.BEECH_LARGE, 20);
        decorator.addTree(LOTRTreeType.CYPRESS, 500);
        decorator.addTree(LOTRTreeType.CYPRESS_LARGE, 50);
        decorator.addTree(LOTRTreeType.OAK_SHRUB, 800);
        decorator.addTree(LOTRTreeType.APPLE, 5);
        decorator.addTree(LOTRTreeType.PEAR, 5);
        decorator.addTree(LOTRTreeType.OLIVE, 20);
        decorator.addTree(LOTRTreeType.OLIVE_LARGE, 20);
        decorator.addTree(LOTRTreeType.ALMOND, 10);
        decorator.addTree(LOTRTreeType.PLUM, 10);
        registerRhunPlainsFlowers();
        biomeColors.setGrass(10538541);
        decorator.addRandomStructure(new LOTRStoneRuinStructure.DORWINION(1, 4), 1000);
        decorator.addRandomStructure(new LOTRDorwinionGardenStructure(false), 300);
        decorator.addRandomStructure(new LOTRDorwinionCampStructure(false), 400);
        decorator.addRandomStructure(new LOTRDorwinionHouseStructure(false), 200);
        decorator.addRandomStructure(new LOTRDorwinionBreweryStructure(false), 500);
        decorator.addRandomStructure(new LOTRDorwinionElfHouseStructure(false), 400);
        decorator.addRandomStructure(new LOTRDorwinionBathStructure(false), 600);
        registerTravellingTrader(LOTREntities.GALADHRIM_TRADER);
        registerTravellingTrader(LOTREntities.NEAR_HARAD_MERCHANT);
        registerTravellingTrader(LOTREntities.IRON_HILLS_MERCHANT);
        registerTravellingTrader(LOTREntities.SCRAP_TRADER);
        registerTravellingTrader(LOTREntities.DALE_MERCHANT);
        setBanditChance(LOTREventSpawner.EventChance.BANDIT_RARE);
        invasionSpawns.addInvasion(LOTRInvasions.DOL_GULDUR, LOTREventSpawner.EventChance.RARE);
        invasionSpawns.addInvasion(LOTRInvasions.MORDOR, LOTREventSpawner.EventChance.RARE);
    }

    @Override
    public void decorate(WorldGenLevel world, RandomSource random, int i, int k) {
        super.decorate(world, random, i, k);
        if (random.nextInt(50) == 0) {
            for (int l = 0; l < 3; ++l) {
                int i1 = i + random.nextInt(16) + 8;
                int k1 = k + random.nextInt(16) + 8;
                boulderGen.generate(world, random, i1, LOTRWorldGenUtil.getHeightValue(world, i1, k1), k1);
            }
        }
    }

    @Override
    public void generateBiomeTerrain(long worldSeed, RandomSource random, LOTRChunkTerrain terrain, int i, int k,
                                     double stoneNoise, int height, LOTRBiomeVariant variant) {
        BlockState[] blocks = terrain.blocks;
        boolean vineyard;
        super.generateBiomeTerrain(worldSeed, random, terrain, i, k, stoneNoise, height, variant, topBlock, fillerBlock);
        int chunkX = i & 0xF;
        int chunkZ = k & 0xF;
        int xzIndex = chunkX * 16 + chunkZ;
        int ySize = LOTRChunkTerrain.HEIGHT;
        vineyard = variant == LOTRBiomeVariant.VINEYARD;
        if (vineyard && !LOTRRoads.isRoadAt(i, k)) {
            for (int j = 128; j >= 0; --j) {
                int index = LOTRChunkTerrain.index(xzIndex, j);
                BlockState above = blocks[index + 1];
                BlockState block = blocks[index];
                if (block == null || !block.isSolidRender() || above != null && above.isAir() == false) {
                    continue;
                }
                int i1 = Mth.positiveModulo(i, 6);
                int i2 = Mth.positiveModulo(i, 24);
                int k1 = Mth.positiveModulo(k, 32);
                int k2 = Mth.positiveModulo(k, 64);
                if ((i1 == 0 || i1 == 5) && k1 != 0) {
                    double d;
                    blocks[index] = LOTRLegacyBlocks.vanilla("farmland").state(0);
                    int h = 2;
                    if (BIOME_TERRAIN_NOISE.getValue(i, k) > 0.0) {
                        ++h;
                    }
                    boolean red = BIOME_TERRAIN_NOISE.getValue(i * (d = 0.01), k * d) > 0.0;
                    BlockState vineBlock = (red ? LOTRLegacyBlocks.mod("grapevineRed") : LOTRLegacyBlocks.mod("grapevineWhite")).state(7);
                    for (int j1 = 1; j1 <= h; ++j1) {
                        blocks[index + j1] = vineBlock;
                    }
                    break;
                }
                if (i1 >= 2 && i1 <= 3) {
                    blocks[index] = LOTRLegacyBlocks.mod("dirtPath").state(0);
                    if (i1 != i2 || (i1 != 2 || k2 != 16) && (i1 != 3 || k2 != 48)) {
                        break;
                    }
                    int h = 3;
                    for (int j1 = 1; j1 <= h; ++j1) {
                        if (j1 == h) {
                            blocks[index + j1] = LOTRLegacyBlocks.vanilla("torch").state(5);
                            continue;
                        }
                        blocks[index + j1] = LOTRLegacyBlocks.mod("fence2").state(10);
                    }
                    break;
                }
                blocks[index] = topBlock;
                break;
            }
        }
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_DORWINION;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.DORWINION.getSubregion("dorwinion");
    }

    @Override
    public LOTRWaypoint.Region getBiomeWaypoints() {
        return LOTRWaypoint.Region.DORWINION;
    }

    @Override
    public float getChanceToSpawnAnimals() {
        return 0.25f;
    }

    @Override
    public LOTRBiomeSpawnList getNPCSpawnList(WorldGenLevel world, RandomSource random, int i, int j, int k, LOTRBiomeVariant variant) {
        if (variant == LOTRBiomeVariant.VINEYARD) {
            return vineyardSpawnList;
        }
        return super.getNPCSpawnList(world, random, i, j, k, variant);
    }

    @Override
    public LOTRWorldGenerator getRandomWorldGenForDoubleFlower(RandomSource random) {
        if (random.nextInt(3) == 0) {
            LOTRWorldGenDoubleFlower doubleFlowerGen = new LOTRWorldGenDoubleFlower();
            doubleFlowerGen.setFlowerType(0);
            return doubleFlowerGen;
        }
        return super.getRandomWorldGenForDoubleFlower(random);
    }

    @Override
    public LOTRRoadType getRoadBlock() {
        return LOTRRoadType.DORWINION;
    }

    @Override
    public boolean hasDomesticAnimals() {
        return true;
    }

    @Override
    public int spawnCountMultiplier() {
        return 3;
    }
}
