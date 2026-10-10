package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

/** LOTRWorldGenBiomeFlowers: a scatter of one flower -- the one given, or one of the biome's own. */
public class LOTRWorldGenBiomeFlowers extends LOTRFeature {

    private final @org.jspecify.annotations.Nullable BlockState specifiedBlock;

    public LOTRWorldGenBiomeFlowers() {
        this.specifiedBlock = null;
    }

    public LOTRWorldGenBiomeFlowers(LegacyBlock block, int meta) {
        this.specifiedBlock = block.state(meta);
    }

    @Override
    protected boolean generateFeature(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        BlockState block = this.specifiedBlock;
        if (block == null) {
            LOTRBiome biome = getBiome(world, i, k);
            LOTRBiome.FlowerEntry flower = biome == null ? null : biome.getRandomFlower(world, random, i, j, k);
            if (flower == null) {
                return false;
            }
            block = flower.state();
        }
        for (int l = 0; l < 64; ++l) {
            int i1 = i + random.nextInt(8) - random.nextInt(8);
            int j1 = j + random.nextInt(4) - random.nextInt(4);
            int k1 = k + random.nextInt(8) - random.nextInt(8);
            if (!isAirBlock(world, i1, j1, k1) || !canBlockStay(block, world, i1, j1, k1)) {
                continue;
            }
            setBlock(world, i1, j1, k1, block, 2);
        }
        return true;
    }
}
