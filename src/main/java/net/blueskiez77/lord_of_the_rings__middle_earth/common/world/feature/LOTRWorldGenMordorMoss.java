package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LOTRWorldGenMordorMoss extends LOTRFeature {
    @Override
    protected boolean generateFeature(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        int numberOfMoss = 32 + random.nextInt(80);
        float f = random.nextFloat() * 3.1415927f;
        double d = i + 8 + Mth.sin(f) * numberOfMoss / 8.0f;
        double d1 = i + 8 - Mth.sin(f) * numberOfMoss / 8.0f;
        double d2 = k + 8 + Mth.cos(f) * numberOfMoss / 8.0f;
        double d3 = k + 8 - Mth.cos(f) * numberOfMoss / 8.0f;
        for (int l = 0; l <= numberOfMoss; ++l) {
            double d5 = d + (d1 - d) * l / numberOfMoss;
            double d6 = d2 + (d3 - d2) * l / numberOfMoss;
            double d7 = random.nextDouble() * numberOfMoss / 16.0;
            double d8 = (Mth.sin(l * 3.1415927f / numberOfMoss) + 1.0f) * d7 + 1.0;
            int i1 = Mth.floor(d5 - d8 / 2.0);
            int k1 = Mth.floor(d6 - d8 / 2.0);
            int i2 = Mth.floor(d5 + d8 / 2.0);
            int k2 = Mth.floor(d6 + d8 / 2.0);
            for (int i3 = i1; i3 <= i2; ++i3) {
                double d9 = (i3 + 0.5 - d5) / (d8 / 2.0);
                if (d9 * d9 >= 1.0) {
                    continue;
                }
                for (int k3 = k1; k3 <= k2; ++k3) {
                    double d10;
                    int j1 = getHeightValue(world, i3, k3);
                    if (j1 != j || d9 * d9 + (d10 = (k3 + 0.5 - d6) / (d8 / 2.0)) * d10 >= 1.0 || !net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRMordorBiome.isSurfaceMordorBlock(world, i3, j1 - 1, k3) || !isAirBlock(world, i3, j1, k3)) {
                        continue;
                    }
                    setBlock(world, i3, j1, k3, LOTRLegacyBlocks.mod("mordorMoss"), 0, 2);
                }
            }
        }
        return true;
    }
}
