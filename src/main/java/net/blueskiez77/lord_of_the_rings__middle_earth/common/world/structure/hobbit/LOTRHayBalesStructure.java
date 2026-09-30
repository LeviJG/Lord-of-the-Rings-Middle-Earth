package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.hobbit;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LOTRHayBalesStructure extends LOTRStructureBase2 {
    public LOTRHayBalesStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        setOriginAndRotation(world, i, j, k, rotation, 0);
        setupRandomBlocks(random);
        int width = 1 + random.nextInt(3);
        int size = 4 + width * width * (2 + random.nextInt(3));
        block0:
        for (int l = 0; l < size; ++l) {
            int r = Mth.randomBetweenInclusive(random, 0, width * width);
            int dist = (int) Math.round(Math.sqrt(r));
            float angle = 6.2831855f * random.nextFloat();
            int i1 = Math.round(Mth.cos(angle) * dist);
            int k1 = Math.round(Mth.sin(angle) * dist);
            for (int j1 = 12; j1 >= -12; --j1) {
                if (!isSurface(world, i1, j1 - 1, k1) && !LOTRLegacyBlocks.vanilla("hay_block").matches(getBlockState(world, i1, j1 - 1, k1))) {
                    continue;
                }
                BlockState block = getBlockState(world, i1, j1, k1);
                if (!isAir(world, i1, j1, k1) && !isReplaceable(world, i1, j1, k1) && !isPlant(block)) {
                    continue;
                }
                setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("hay_block"), 0);
                setGrassToDirt(world, i1, j1 - 1, k1);
                continue block0;
            }
        }
        return true;
    }
}
