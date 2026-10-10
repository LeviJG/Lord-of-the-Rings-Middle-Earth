package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

/** LOTRWorldGenDoubleFlower: a patch of one of the mod's two-block flowers. */
public class LOTRWorldGenDoubleFlower extends LOTRFeature {

    public int flowerType;

    @Override
    protected boolean generateFeature(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        boolean flag = false;
        BlockState lower = LOTRLegacyBlocks.mod("doubleFlower").state(this.flowerType);
        for (int l = 0; l < 64; ++l) {
            int i1 = i + random.nextInt(8) - random.nextInt(8);
            int j1 = j + random.nextInt(4) - random.nextInt(4);
            int k1 = k + random.nextInt(8) - random.nextInt(8);
            if (!isAirBlock(world, i1, j1, k1) || !placeDoublePlant(world, lower, i1, j1, k1, true)) {
                continue;
            }
            flag = true;
        }
        return flag;
    }

    public void setFlowerType(int i) {
        this.flowerType = i;
    }
}
