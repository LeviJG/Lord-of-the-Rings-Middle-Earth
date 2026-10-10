package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRChunkTerrain;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant.LOTRBiomeVariant;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRVanillaWorldGens;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.noise.LOTRNoiseGeneratorPerlin;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRBiomeGenRhunLandHills.
 */
public class LOTRRhunLandHillsBiome extends LOTRRhunLandBiome {

    public static LOTRNoiseGeneratorPerlin noiseStone = new LOTRNoiseGeneratorPerlin(528592609698295062L, 1);

    public static LOTRNoiseGeneratorPerlin noiseSand = new LOTRNoiseGeneratorPerlin(23849150950915615L, 1);

    public LOTRRhunLandHillsBiome(int i, boolean major) {
        super(i, major);
        npcSpawnList.clear();
        clearBiomeVariants();
        addBiomeVariantSet(LOTRBiomeVariant.SET_MOUNTAINS);
        decorator.addOre(new LOTRVanillaWorldGens.Minable(LOTRLegacyBlocks.vanilla("gold_ore"), 4), 2.0f, 0, 48);
        decorator.resetTreeCluster();
        decorator.flowersPerChunk = 2;
        decorator.doubleFlowersPerChunk = 0;
        decorator.grassPerChunk = 5;
        decorator.doubleGrassPerChunk = 1;
        decorator.clearVillages();
        biomeColors.resetGrass();
    }

    @Override
    public void generateBiomeTerrain(long worldSeed, RandomSource random, LOTRChunkTerrain terrain, int i, int k,
                                     double stoneNoise, int height, LOTRBiomeVariant variant) {
        BlockState topBlock = this.topBlock;
        BlockState fillerBlock = this.fillerBlock;
        double d1 = noiseStone.getValue(i * 0.09, k * 0.09);
        double d2 = noiseStone.getValue(i * 0.4, k * 0.4);
        double d3 = noiseSand.getValue(i * 0.09, k * 0.09);
        if (d3 + noiseSand.getValue(i * 0.4, k * 0.4) > 1.1) {
            topBlock = LOTRLegacyBlocks.vanilla("sand").state(0);
            fillerBlock = topBlock;
        } else if (d1 + d2 > 0.4) {
            topBlock = LOTRLegacyBlocks.vanilla("stone").state(0);
            fillerBlock = topBlock;
        }
        super.generateBiomeTerrain(worldSeed, random, terrain, i, k, stoneNoise, height, variant, topBlock, fillerBlock);
    }

    @Override
    public float getTreeIncreaseChance() {
        return 0.25f;
    }
}
