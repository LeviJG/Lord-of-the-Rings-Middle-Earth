package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.variant;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.LOTRChunkTerrain;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRTreeType;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;

/** LOTRBiomeVariantScrubland: patches of bare stone, gravel and sand, scrub and shrubs, the odd dead stump. */
public class LOTRScrublandBiomeVariant extends LOTRBiomeVariant {

    private static final BlockState LEAVES = Blocks.OAK_LEAVES.defaultBlockState().setValue(LeavesBlock.PERSISTENT, true);
    private final BlockState stoneBlock;

    public LOTRScrublandBiomeVariant(int i, String s, BlockState block) {
        super(i, s, VariantScale.LARGE);
        setTemperatureRainfall(0.0f, -0.2f);
        setTrees(0.8f);
        setGrass(0.5f);
        setFlowers(0.5f);
        addTreeTypes(0.6f, LOTRTreeType.OAK_SHRUB, 100);
        this.stoneBlock = block;
        disableVillages();
    }

    @Override
    public void generateVariantTerrain(LOTRChunkTerrain terrain, RandomSource random, int i, int k, int height, LOTRBiome biome) {
        BlockState[] blocks = terrain.blocks;
        int xzIndex = (i & 0xF) * 16 + (k & 0xF);
        double d1 = LOTRBiome.BIOME_TERRAIN_NOISE.getValue(i * 0.005, k * 0.005);
        double d2 = LOTRBiome.BIOME_TERRAIN_NOISE.getValue(i * 0.07, k * 0.07);
        if (d1 + d2 + LOTRBiome.BIOME_TERRAIN_NOISE.getValue(i * 0.3, k * 0.3) > 0.6) {
            int index = LOTRChunkTerrain.index(xzIndex, height);
            if (d1 + d2 > 0.7 && random.nextInt(3) != 0) {
                blocks[index] = Blocks.SAND.defaultBlockState();
            } else if (random.nextInt(5) == 0) {
                blocks[index] = Blocks.GRAVEL.defaultBlockState();
            } else {
                blocks[index] = this.stoneBlock;
            }
            if (random.nextInt(30) == 0) {
                blocks[index + 1] = random.nextInt(3) == 0 ? this.stoneBlock : Blocks.GRAVEL.defaultBlockState();
            }
        }
        d1 = LOTRBiome.BIOME_TERRAIN_NOISE.getValue(i * 0.008, k * 0.008);
        d2 = LOTRBiome.BIOME_TERRAIN_NOISE.getValue(i * 0.05, k * 0.05);
        if (d1 + d2 + LOTRBiome.BIOME_TERRAIN_NOISE.getValue(i * 0.6, k * 0.6) > 0.8 && random.nextInt(3) == 0) {
            int index = LOTRChunkTerrain.index(xzIndex, height);
            if (LOTRChunkTerrain.isOpaque(blocks[index]) && blocks[index + 1].isAir()) {
                blocks[index + 1] = LEAVES;
                if (random.nextInt(5) == 0) {
                    blocks[index + 2] = LEAVES;
                }
            }
        }
        if (random.nextInt(3000) == 0) {
            int index = LOTRChunkTerrain.index(xzIndex, height);
            blocks[index] = biome.fillerBlock;
            int logHeight = 1 + random.nextInt(4);
            for (int j1 = 1; j1 <= logHeight; ++j1) {
                blocks[index + j1] = Blocks.OAK_LOG.defaultBlockState();
            }
        }
    }
}
