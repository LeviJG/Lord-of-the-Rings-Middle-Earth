package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LOTRWorldGenSurfaceGravel extends LOTRFeature {
    @Override
    protected boolean generateFeature(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        LegacyBlock surfBlock;
        int surfMeta;
        int r = LOTRWorldGenUtil.getRandomIntegerInRange(random, 2, 8);
        int chance = LOTRWorldGenUtil.getRandomIntegerInRange(random, 3, 9);
        if (random.nextBoolean()) {
            surfBlock = LOTRLegacyBlocks.vanilla("gravel");
            surfMeta = 0;
        } else {
            surfBlock = LOTRLegacyBlocks.vanilla("dirt");
            surfMeta = 1;
        }
        for (int i1 = -r; i1 <= r; ++i1) {
            for (int k1 = -r; k1 <= r; ++k1) {
                int i2 = i + i1;
                int k2 = k + k1;
                int d = i1 * i1 + k1 * k1;
                if (d >= r * r || random.nextInt(chance) != 0) {
                    continue;
                }
                int j1 = getTopSolidOrLiquidBlock(world, i2, k2) - 1;
                BlockState block = getBlock(world, i2, j1, k2);
                // Material.ground (dirt) or Material.grass.
                if (!isOpaqueCube(block) || !block.is(net.minecraft.tags.BlockTags.DIRT)) {
                    continue;
                }
                setBlock(world, i2, j1, k2, surfBlock, surfMeta, 2);
            }
        }
        return true;
    }
}
