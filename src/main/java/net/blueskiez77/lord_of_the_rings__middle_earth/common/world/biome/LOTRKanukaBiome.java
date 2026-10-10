package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRChunkTerrain;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRWorldChunkManager;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRVanillaWorldGens;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenerator;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.noise.LOTRNoiseGeneratorPerlin;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.spawning.LOTRSpawnEntry;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRBiomeGenKanuka.
 */
public class LOTRKanukaBiome extends LOTRFarHaradBiome {

    public static LOTRNoiseGeneratorPerlin noisePaths1 = new LOTRNoiseGeneratorPerlin(22L, 1);

    public static LOTRNoiseGeneratorPerlin noisePaths2 = new LOTRNoiseGeneratorPerlin(11L, 1);

    public LOTRKanukaBiome(int i, boolean major) {
        super(i, major);
        spawnableLOTRAmbientList.clear();
        spawnableLOTRAmbientList.add(new LOTRSpawnEntry(LOTREntities.BIRD, 10, 4, 4));
        spawnableLOTRAmbientList.add(new LOTRSpawnEntry(LOTREntities.BUTTERFLY, 10, 4, 4));
        addBiomeVariant(LOTRBiomeVariant.FLOWERS);
        addBiomeVariant(LOTRBiomeVariant.HILLS);
        addBiomeVariant(LOTRBiomeVariant.FOREST_LIGHT);
        enablePodzol = false;
        decorator.treesPerChunk = 0;
        decorator.setTreeCluster(8, 3);
        decorator.vinesPerChunk = 0;
        decorator.flowersPerChunk = 3;
        decorator.doubleFlowersPerChunk = 1;
        decorator.grassPerChunk = 4;
        decorator.doubleGrassPerChunk = 1;
        decorator.enableFern = true;
        decorator.melonPerChunk = 0.0f;
        decorator.clearTrees();
        decorator.addTree(LOTRTreeType.KANUKA, 100);
        biomeColors.setGrass(11915563);
    }

    @Override
    public void decorate(WorldGenLevel world, RandomSource random, int i, int k) {
        super.decorate(world, random, i, k);
        for (int count = 0; count < 4; ++count) {
            int k1;
            int i1 = i + random.nextInt(16) + 8;
            int j1 = LOTRWorldGenUtil.getHeightValue(world, i1, k1 = k + random.nextInt(16) + 8);
            if (j1 <= 75) {
                continue;
            }
            decorator.genTree(world, random, i1, j1, k1);
        }
        LOTRBiomeVariant variant = LOTRWorldChunkManager.of(world).getBiomeVariantAt(i + 8, k + 8);
        int grasses = 12;
        grasses = Math.round(grasses * variant.grassFactor);
        for (int l = 0; l < grasses; ++l) {
            int i1 = i + random.nextInt(16) + 8;
            int j1 = random.nextInt(128);
            int k1 = k + random.nextInt(16) + 8;
            if (LOTRWorldGenUtil.getHeightValue(world, i1, k1) <= 75) {
                continue;
            }
            LOTRWorldGenerator grassGen = getRandomWorldGenForGrass(random);
            grassGen.generate(world, random, i1, j1, k1);
        }
        int doubleGrasses = 4;
        doubleGrasses = Math.round(doubleGrasses * variant.grassFactor);
        for (int l = 0; l < doubleGrasses; ++l) {
            int i1 = i + random.nextInt(16) + 8;
            int j1 = random.nextInt(128);
            int k1 = k + random.nextInt(16) + 8;
            if (LOTRWorldGenUtil.getHeightValue(world, i1, k1) <= 75) {
                continue;
            }
            LOTRWorldGenerator grassGen = getRandomWorldGenForDoubleGrass(random);
            grassGen.generate(world, random, i1, j1, k1);
        }
    }

    @Override
    public void generateBiomeTerrain(long worldSeed, RandomSource random, LOTRChunkTerrain terrain, int i, int k,
                                     double stoneNoise, int height, LOTRBiomeVariant variant) {
        BlockState topBlock = this.topBlock;
        BlockState fillerBlock = this.fillerBlock;
        double d1 = noisePaths1.getValue(i * 0.008, k * 0.008);
        double d2 = noisePaths2.getValue(i * 0.008, k * 0.008);
        if (d1 > 0.0 && d1 < 0.1 || d2 > 0.0 && d2 < 0.1) {
            topBlock = LOTRLegacyBlocks.mod("dirtPath").state(1);
        }
        super.generateBiomeTerrain(worldSeed, random, terrain, i, k, stoneNoise, height, variant, topBlock, fillerBlock);
    }

    /** The original set enablePodzol to this about its surface pass. */
    @Override
    public boolean isPodzolEnabled(int height) {
        return height > 75;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.FAR_HARAD.getSubregion("kanuka");
    }

    @Override
    public float getChanceToSpawnAnimals() {
        return super.getChanceToSpawnAnimals() * 0.25f;
    }

    @Override
    public LOTRBiome.GrassBlockAndMeta getRandomGrass(RandomSource random) {
        if (random.nextInt(5) != 0) {
            return new LOTRBiome.GrassBlockAndMeta(LOTRLegacyBlocks.vanilla("tallgrass"), 2);
        }
        return super.getRandomGrass(random);
    }

    @Override
    public LOTRWorldGenerator getRandomWorldGenForDoubleGrass(RandomSource random) {
        LOTRVanillaWorldGens.DoublePlant generator = new LOTRVanillaWorldGens.DoublePlant();
        generator.func_150548_a(3);
        return generator;
    }
}
