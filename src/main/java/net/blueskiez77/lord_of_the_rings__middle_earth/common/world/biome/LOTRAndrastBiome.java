package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.achievement.LOTRAchievement;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRChunkTerrain;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenBoulder;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenerator;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.noise.LOTRNoiseGeneratorPerlin;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTREventSpawner;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRGondorObeliskStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRGondorRuinStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRGondorRuinsStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRRuinedGondorTowerStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRSmallStoneRuinStructure;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRBiomeGenAndrast.
 */
public class LOTRAndrastBiome extends LOTRGondorBiome {

    public static LOTRNoiseGeneratorPerlin noiseDirt = new LOTRNoiseGeneratorPerlin(285939985023633003L, 1);

    public static LOTRNoiseGeneratorPerlin noiseStone = new LOTRNoiseGeneratorPerlin(4148990259960304L, 1);

    public LOTRWorldGenerator boulderGen = new LOTRWorldGenBoulder(LOTRLegacyBlocks.vanilla("stone"), 0, 1, 4);

    public LOTRWorldGenerator boulderGenGondor = new LOTRWorldGenBoulder(LOTRLegacyBlocks.mod("rock"), 1, 1, 4);

    public LOTRAndrastBiome(int i, boolean major) {
        super(i, major);
        npcSpawnList.clear();
        clearBiomeVariants();
        variantChance = 0.5f;
        addBiomeVariant(LOTRBiomeVariant.FLOWERS);
        addBiomeVariant(LOTRBiomeVariant.FOREST_LIGHT);
        addBiomeVariant(LOTRBiomeVariant.FOREST);
        addBiomeVariant(LOTRBiomeVariant.HILLS);
        addBiomeVariant(LOTRBiomeVariant.HILLS_FOREST);
        addBiomeVariant(LOTRBiomeVariant.DEADFOREST_OAK);
        addBiomeVariant(LOTRBiomeVariant.SHRUBLAND_OAK);
        addBiomeVariant(LOTRBiomeVariant.SCRUBLAND, 3.0f);
        addBiomeVariant(LOTRBiomeVariant.HILLS_SCRUBLAND);
        addBiomeVariant(LOTRBiomeVariant.WASTELAND, 3.0f);
        decorator.setTreeCluster(10, 30);
        decorator.treesPerChunk = 0;
        decorator.flowersPerChunk = 3;
        decorator.doubleFlowersPerChunk = 1;
        decorator.grassPerChunk = 12;
        decorator.doubleGrassPerChunk = 4;
        decorator.addTree(LOTRTreeType.OAK, 400);
        decorator.addTree(LOTRTreeType.OAK_LARGE, 200);
        decorator.addTree(LOTRTreeType.BIRCH, 50);
        decorator.addTree(LOTRTreeType.BIRCH_LARGE, 20);
        decorator.addTree(LOTRTreeType.BEECH, 50);
        decorator.addTree(LOTRTreeType.BEECH_LARGE, 20);
        decorator.addTree(LOTRTreeType.DARK_OAK, 500);
        decorator.addTree(LOTRTreeType.FIR, 200);
        decorator.addTree(LOTRTreeType.PINE, 200);
        decorator.addTree(LOTRTreeType.SPRUCE, 200);
        decorator.addTree(LOTRTreeType.LARCH, 200);
        decorator.addTree(LOTRTreeType.APPLE, 5);
        decorator.addTree(LOTRTreeType.PEAR, 5);
        decorator.addTree(LOTRTreeType.PLUM, 5);
        decorator.addTree(LOTRTreeType.OAK_SHRUB, 1500);
        registerPlainsFlowers();
        biomeColors.setGrass(10202470);
        decorator.clearVillages();
        decorator.clearRandomStructures();
        decorator.addRandomStructure(new LOTRSmallStoneRuinStructure(false), 400);
        decorator.addRandomStructure(new LOTRGondorRuinsStructure(), 2000);
        decorator.addRandomStructure(new LOTRRuinedGondorTowerStructure(false), 2000);
        decorator.addRandomStructure(new LOTRGondorObeliskStructure(false), 2000);
        decorator.addRandomStructure(new LOTRGondorRuinStructure(false), 1500);
        clearTravellingTraders();
        setBanditChance(LOTREventSpawner.EventChance.BANDIT_UNCOMMON);
        invasionSpawns.clearInvasions();
    }

    @Override
    public void decorate(WorldGenLevel world, RandomSource random, int i, int k) {
        super.decorate(world, random, i, k);
        if (random.nextInt(5) == 0) {
            for (int l = 0; l < 4; ++l) {
                int i1 = i + random.nextInt(16) + 8;
                int k1 = k + random.nextInt(16) + 8;
                if (random.nextBoolean()) {
                    boulderGen.generate(world, random, i1, LOTRWorldGenUtil.getHeightValue(world, i1, k1), k1);
                    continue;
                }
                boulderGenGondor.generate(world, random, i1, LOTRWorldGenUtil.getHeightValue(world, i1, k1), k1);
            }
        }
    }

    @Override
    public void generateBiomeTerrain(long worldSeed, RandomSource random, LOTRChunkTerrain terrain, int i, int k,
                                     double stoneNoise, int height, LOTRBiomeVariant variant) {
        BlockState topBlock = this.topBlock;
        BlockState fillerBlock = this.fillerBlock;
        double d1 = noiseDirt.getValue(i * 0.07, k * 0.07);
        double d2 = noiseDirt.getValue(i * 0.3, k * 0.3);
        double d3 = noiseStone.getValue(i * 0.07, k * 0.07);
        if (d3 + noiseStone.getValue(i * 0.3, k * 0.3) > 1.1) {
            topBlock = LOTRLegacyBlocks.vanilla("stone").state(0);
            fillerBlock = topBlock;
        } else if (d1 + d2 > 0.6) {
            topBlock = LOTRLegacyBlocks.vanilla("dirt").state(1);
        }
        super.generateBiomeTerrain(worldSeed, random, terrain, i, k, stoneNoise, height, variant, topBlock, fillerBlock);
    }

    @Override
    public LOTRAchievement getBiomeAchievement() {
        return LOTRAchievement.ENTER_ANDRAST;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.PUKEL.getSubregion("andrast");
    }

    @Override
    public float getTreeIncreaseChance() {
        return 0.5f;
    }

    @Override
    public int spawnCountMultiplier() {
        return 1;
    }
}
