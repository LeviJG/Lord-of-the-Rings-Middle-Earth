package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRSouthronLampStructure extends LOTRSouthronStructure {
    public LOTRSouthronLampStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        int k1;
        int i1;
        setOriginAndRotation(world, i, j, k, rotation, 0);
        setupRandomBlocks(random);
        if (restrictions && !isSurface(world, i1 = 0, getTopBlock(world, i1, k1 = 0) - 1, k1)) {
            return false;
        }
        int j12 = 0;
        while (!isOpaque(world, 0, j12, 0) && getY(j12) >= world.getMinY()) {
            setBlockAndMetadata(world, 0, j12, 0, stoneBlock, stoneMeta);
            setGrassToDirt(world, 0, j12 - 1, 0);
            --j12;
        }
        setBlockAndMetadata(world, 0, 1, 0, stoneWallBlock, stoneWallMeta);
        setBlockAndMetadata(world, 0, 2, 0, brickWallBlock, brickWallMeta);
        setBlockAndMetadata(world, 0, 3, 0, brickWallBlock, brickWallMeta);
        setBlockAndMetadata(world, 0, 4, 0, fenceBlock, fenceMeta);
        setBlockAndMetadata(world, 0, 5, 0, LOTRLegacyBlocks.vanilla("torch"), 5);
        return true;
    }
}
