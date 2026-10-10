package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRChunkTerrain;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/** LOTRBiomeVariantWasteland: the ground laid bare to stone and gravel, with coarse dirt between. */
public class LOTRWastelandBiomeVariant extends LOTRBiomeVariant {

    private final BlockState stoneBlock;

    public LOTRWastelandBiomeVariant(int i, String s, BlockState block) {
        super(i, s, VariantScale.LARGE);
        setTemperatureRainfall(0.0f, -0.3f);
        setTrees(0.1f);
        setGrass(0.3f);
        setFlowers(0.3f);
        this.stoneBlock = block;
        disableVillages();
    }

    @Override
    public void generateVariantTerrain(LOTRChunkTerrain terrain, RandomSource random, int i, int k, int height, LOTRBiome biome) {
        BlockState[] blocks = terrain.blocks;
        int index = LOTRChunkTerrain.index((i & 0xF) * 16 + (k & 0xF), height);
        double d1 = LOTRBiome.BIOME_TERRAIN_NOISE.getValue(i * 0.04, k * 0.04);
        double d2 = LOTRBiome.BIOME_TERRAIN_NOISE.getValue(i * 0.3, k * 0.3) * 0.3;
        double d3 = PODZOL_NOISE.getValue(i * 0.04, k * 0.04);
        double d4 = PODZOL_NOISE.getValue(i * 0.3, k * 0.3) * 0.3;
        if (d3 + d4 > 0.5) {
            blocks[index] = Blocks.COARSE_DIRT.defaultBlockState();
        } else if (d1 + d2 > -0.3) {
            blocks[index] = random.nextInt(5) == 0 ? Blocks.GRAVEL.defaultBlockState() : this.stoneBlock;
            if (random.nextInt(50) == 0) {
                blocks[index + 1] = random.nextInt(3) == 0 ? this.stoneBlock : Blocks.GRAVEL.defaultBlockState();
            }
        }
    }
}
