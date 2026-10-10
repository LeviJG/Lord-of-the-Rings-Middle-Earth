package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRChunkTerrain;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.noise.LOTRNoiseGeneratorPerlin;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRBiomeGenNearHaradHills.
 */
public class LOTRNearHaradHillsBiome extends LOTRNearHaradBiome {

    public static LOTRNoiseGeneratorPerlin noiseSandstone = new LOTRNoiseGeneratorPerlin(8906820602062L, 1);

    public static LOTRNoiseGeneratorPerlin noiseStone = new LOTRNoiseGeneratorPerlin(583062262026L, 1);

    public LOTRNearHaradHillsBiome(int i, boolean major) {
        super(i, major);
        enableRain = true;
        clearBiomeVariants();
        addBiomeVariant(LOTRBiomeVariant.DEADFOREST_OAK);
        addBiomeVariant(LOTRBiomeVariant.SCRUBLAND_SAND);
    }

    @Override
    public void generateBiomeTerrain(long worldSeed, RandomSource random, LOTRChunkTerrain terrain, int i, int k,
                                     double stoneNoise, int height, LOTRBiomeVariant variant) {
        BlockState topBlock = this.topBlock;
        BlockState fillerBlock = this.fillerBlock;
        double d1 = noiseSandstone.getValue(i * 0.09, k * 0.09);
        double d2 = noiseSandstone.getValue(i * 0.4, k * 0.4);
        double d3 = noiseStone.getValue(i * 0.09, k * 0.09);
        if (d3 + noiseStone.getValue(i * 0.4, k * 0.4) > 0.6) {
            topBlock = LOTRLegacyBlocks.vanilla("stone").state(0);
            fillerBlock = topBlock;
        } else if (d1 + d2 > 0.2) {
            topBlock = LOTRLegacyBlocks.vanilla("sandstone").state(0);
            fillerBlock = topBlock;
        }
        super.generateBiomeTerrain(worldSeed, random, terrain, i, k, stoneNoise, height, variant, topBlock, fillerBlock);
    }

    @Override
    public float getTreeIncreaseChance() {
        return 0.01f;
    }
}
