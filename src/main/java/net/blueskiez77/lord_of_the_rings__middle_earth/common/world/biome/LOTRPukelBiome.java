package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRChunkTerrain;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenBoulder;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenerator;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.noise.LOTRNoiseGeneratorPerlin;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTREventSpawner;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnEntry;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRBurntHouseStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRRottenHouseStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRRuinedHouseStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRSmallStoneRuinStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRStoneRuinStructure;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRBiomeGenPukel.
 */
public class LOTRPukelBiome extends LOTRBiome {

    public static LOTRNoiseGeneratorPerlin noiseDirt = new LOTRNoiseGeneratorPerlin(285939985023633003L, 1);

    public static LOTRNoiseGeneratorPerlin noiseStone = new LOTRNoiseGeneratorPerlin(4148990259960304L, 1);

    public LOTRWorldGenerator boulderGen = new LOTRWorldGenBoulder(LOTRLegacyBlocks.vanilla("stone"), 0, 1, 4);

    public LOTRPukelBiome(int i, boolean major) {
        super(i, major);
        spawnableCreatureList.add(new LOTRSpawnEntry(EntityTypes.WOLF, 6, 1, 4));
        spawnableCreatureList.add(new LOTRSpawnEntry(LOTREntities.BEAR, 4, 1, 4));
        npcSpawnList.clear();
        clearBiomeVariants();
        variantChance = 0.6f;
        addBiomeVariant(LOTRBiomeVariant.FLOWERS);
        addBiomeVariant(LOTRBiomeVariant.FOREST_LIGHT, 4.0f);
        addBiomeVariant(LOTRBiomeVariant.FOREST, 4.0f);
        addBiomeVariant(LOTRBiomeVariant.HILLS, 2.0f);
        addBiomeVariant(LOTRBiomeVariant.HILLS_FOREST, 3.0f);
        addBiomeVariant(LOTRBiomeVariant.DEADFOREST_OAK, 2.0f);
        addBiomeVariant(LOTRBiomeVariant.DENSEFOREST_OAK, 6.0f);
        addBiomeVariant(LOTRBiomeVariant.DENSEFOREST_DARK_OAK, 6.0f);
        addBiomeVariant(LOTRBiomeVariant.SHRUBLAND_OAK);
        addBiomeVariant(LOTRBiomeVariant.SCRUBLAND);
        addBiomeVariant(LOTRBiomeVariant.HILLS_SCRUBLAND);
        addBiomeVariant(LOTRBiomeVariant.WASTELAND);
        decorator.setTreeCluster(10, 24);
        decorator.treesPerChunk = 1;
        decorator.willowPerChunk = 1;
        decorator.flowersPerChunk = 3;
        decorator.doubleFlowersPerChunk = 1;
        decorator.grassPerChunk = 14;
        decorator.doubleGrassPerChunk = 6;
        decorator.addTree(LOTRTreeType.OAK, 400);
        decorator.addTree(LOTRTreeType.OAK_LARGE, 200);
        decorator.addTree(LOTRTreeType.OAK_PARTY, 50);
        decorator.addTree(LOTRTreeType.BIRCH, 50);
        decorator.addTree(LOTRTreeType.BIRCH_LARGE, 20);
        decorator.addTree(LOTRTreeType.BEECH, 50);
        decorator.addTree(LOTRTreeType.BEECH_LARGE, 20);
        decorator.addTree(LOTRTreeType.CHESTNUT, 200);
        decorator.addTree(LOTRTreeType.CHESTNUT_LARGE, 50);
        decorator.addTree(LOTRTreeType.DARK_OAK, 500);
        decorator.addTree(LOTRTreeType.DARK_OAK_PARTY, 100);
        decorator.addTree(LOTRTreeType.FIR, 200);
        decorator.addTree(LOTRTreeType.PINE, 200);
        decorator.addTree(LOTRTreeType.SPRUCE, 200);
        decorator.addTree(LOTRTreeType.LARCH, 200);
        decorator.addTree(LOTRTreeType.APPLE, 5);
        decorator.addTree(LOTRTreeType.PEAR, 5);
        decorator.addTree(LOTRTreeType.PLUM, 5);
        decorator.addTree(LOTRTreeType.OAK_SHRUB, 300);
        registerPlainsFlowers();
        biomeColors.setGrass(6715192);
        biomeColors.setSky(10927288);
        decorator.clearRandomStructures();
        decorator.addRandomStructure(new LOTRRuinedHouseStructure(false), 2000);
        decorator.addRandomStructure(new LOTRBurntHouseStructure(false), 2000);
        decorator.addRandomStructure(new LOTRRottenHouseStructure(false), 2000);
        decorator.addRandomStructure(new LOTRStoneRuinStructure.STONE(1, 5), 500);
        decorator.addRandomStructure(new LOTRSmallStoneRuinStructure(false), 400);
        clearTravellingTraders();
        setBanditChance(LOTREventSpawner.EventChance.BANDIT_RARE);
        invasionSpawns.clearInvasions();
    }

    @Override
    public void decorate(WorldGenLevel world, RandomSource random, int i, int k) {
        super.decorate(world, random, i, k);
        if (random.nextInt(24) == 0) {
            for (int l = 0; l < 4; ++l) {
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
        double d1 = noiseDirt.getValue(i * 0.06, k * 0.06);
        double d2 = noiseDirt.getValue(i * 0.4, k * 0.4);
        double d3 = noiseStone.getValue(i * 0.06, k * 0.06);
        if (d3 + noiseStone.getValue(i * 0.4, k * 0.4) > 1.3) {
            topBlock = LOTRLegacyBlocks.vanilla("stone").state(0);
            fillerBlock = topBlock;
        } else if (d1 + d2 > 0.7) {
            topBlock = LOTRLegacyBlocks.vanilla("dirt").state(1);
        }
        super.generateBiomeTerrain(worldSeed, random, terrain, i, k, stoneNoise, height, variant, topBlock, fillerBlock);
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_PUKEL;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.PUKEL.getSubregion("pukel");
    }

    @Override
    public LOTRWaypoint.Region getBiomeWaypoints() {
        return LOTRWaypoint.Region.PUKEL;
    }

    @Override
    public float getChanceToSpawnAnimals() {
        return 0.25f;
    }
}
