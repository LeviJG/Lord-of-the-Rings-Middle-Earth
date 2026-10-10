package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LOTRWorldGenWaterPlant extends LOTRFeature {
    public LegacyBlock plant;

    public LOTRWorldGenWaterPlant(LegacyBlock block) {
        plant = block;
    }

    @Override
    protected boolean generateFeature(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        for (int l = 0; l < 32; ++l) {
            int j1;
            int k1;
            int i1 = i + random.nextInt(4) - random.nextInt(4);
            if (!isAirBlock(world, i1, j1 = j, k1 = k + random.nextInt(4) - random.nextInt(4)) || !canBlockStay(plant, world, i1, j1, k1)) {
                continue;
            }
            setBlock(world, i1, j1, k1, plant, 0, 2);
        }
        return true;
    }
}
