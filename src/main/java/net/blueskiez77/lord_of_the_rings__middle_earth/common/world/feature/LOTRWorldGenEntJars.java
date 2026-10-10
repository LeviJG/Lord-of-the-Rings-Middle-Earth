package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

/** LOTRWorldGenEntJars: Ent jars on Fangorn's open grass, some part filled with water. */
public class LOTRWorldGenEntJars extends LOTRFeature {

    @Override
    protected boolean generateFeature(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        for (int l = 0; l < 16; ++l) {
            int i1 = i - random.nextInt(6) + random.nextInt(6);
            int j1 = j - random.nextInt(2) + random.nextInt(2);
            int k1 = k - random.nextInt(6) + random.nextInt(6);
            if (!getBlock(world, i1, j1 - 1, k1).is(net.minecraft.world.level.block.Blocks.GRASS_BLOCK)
                    || getBlock(world, i1, j1, k1).isRedstoneConductor(world, new net.minecraft.core.BlockPos(i1, j1, k1))
                    || getPrecipitationHeight(world, i1, k1) != j1 || !(getBiome(world, i1, k1) instanceof net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRFangornBiome)) {
                continue;
            }
            setBlock(world, i1, j1, k1, LOTRLegacyBlocks.mod("entJar"), 0, 2);
            if (!(world.getBlockEntity(new net.minecraft.core.BlockPos(i1, j1, k1))
                    instanceof net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTREntJarBlockEntity jar)) {
                continue;
            }
            int amount = random.nextInt(net.blueskiez77.lord_of_the_rings__middle_earth.common.blockentity.LOTREntJarBlockEntity.MAX_CAPACITY + 1);
            for (int l1 = 0; l1 < amount; ++l1) {
                jar.fillWithWater();
            }
        }
        return true;
    }
}
