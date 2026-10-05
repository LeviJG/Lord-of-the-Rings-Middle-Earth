package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRGondorLampPostStructure extends LOTRGondorStructure {
    public LOTRGondorLampPostStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        int j1;
        int k1;
        int i1;
        setOriginAndRotation(world, i, j, k, rotation, 0);
        setupRandomBlocks(random);
        if (restrictions && !isSurface(world, i1 = 0, getTopBlock(world, i1, k1 = 0) - 1, k1)) {
            return false;
        }
        for (j1 = 0; (j1 >= 0 || !isOpaque(world, 0, j1, 0)) && getY(j1) >= 0; --j1) {
            setBlockAndMetadata(world, 0, j1, 0, brickBlock, brickMeta);
            setGrassToDirt(world, 0, j1 - 1, 0);
        }
        setBlockAndMetadata(world, 0, 1, 0, pillarBlock, pillarMeta);
        for (j1 = 2; j1 <= 4; ++j1) {
            setBlockAndMetadata(world, 0, j1, 0, rockWallBlock, rockWallMeta);
        }
        setBlockAndMetadata(world, 0, 5, 0, rockSlabDoubleBlock, rockSlabDoubleMeta);
        setBlockAndMetadata(world, -1, 5, 0, LOTRLegacyBlocks.vanilla("torch"), 1);
        setBlockAndMetadata(world, 1, 5, 0, LOTRLegacyBlocks.vanilla("torch"), 2);
        setBlockAndMetadata(world, 0, 5, -1, LOTRLegacyBlocks.vanilla("torch"), 4);
        setBlockAndMetadata(world, 0, 5, 1, LOTRLegacyBlocks.vanilla("torch"), 3);
        return true;
    }
}
