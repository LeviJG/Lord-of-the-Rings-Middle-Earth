package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRChunkTerrain;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.noise.LOTRNoiseGeneratorPerlin;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRBiomeGenFarHaradArid.
 */
public class LOTRFarHaradAridBiome extends LOTRFarHaradSavannahBiome {

    public static LOTRNoiseGeneratorPerlin noiseDirt = new LOTRNoiseGeneratorPerlin(35952060662L, 1);

    public static LOTRNoiseGeneratorPerlin noiseSand = new LOTRNoiseGeneratorPerlin(5925366672L, 1);

    public LOTRFarHaradAridBiome(int i, boolean major) {
        super(i, major);
        decorator.flowersPerChunk = 1;
        decorator.doubleFlowersPerChunk = 1;
        spawnableLOTRAmbientList.clear();
        biomeColors.setGrass(14073692);
    }

    @Override
    public void generateBiomeTerrain(long worldSeed, RandomSource random, LOTRChunkTerrain terrain, int i, int k,
                                     double stoneNoise, int height, LOTRBiomeVariant variant) {
        BlockState topBlock = this.topBlock;
        BlockState fillerBlock = this.fillerBlock;
        double d1 = noiseDirt.getValue(i * 0.07, k * 0.07);
        double d2 = noiseDirt.getValue(i * 0.15, k * 0.15);
        double d3 = noiseSand.getValue(i * 0.07, k * 0.07);
        if (d3 + noiseSand.getValue(i * 0.15, k * 0.15) > 0.6) {
            topBlock = LOTRLegacyBlocks.vanilla("sand").state(0);
            fillerBlock = topBlock;
        } else if (d1 + d2 > 0.1) {
            topBlock = LOTRLegacyBlocks.vanilla("dirt").state(1);
        }
        super.generateBiomeTerrain(worldSeed, random, terrain, i, k, stoneNoise, height, variant, topBlock, fillerBlock);
    }

    @Override
    public float getChanceToSpawnAnimals() {
        return super.getChanceToSpawnAnimals() * 0.5f;
    }

    @Override
    public LOTRBiome.GrassBlockAndMeta getRandomGrass(RandomSource random) {
        return random.nextBoolean() ? new LOTRBiome.GrassBlockAndMeta(LOTRLegacyBlocks.mod("aridGrass"), 0) : super.getRandomGrass(random);
    }
}
