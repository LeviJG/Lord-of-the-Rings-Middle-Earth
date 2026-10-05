package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRRangerVillageLightStructure extends LOTRRangerStructure {
    public LOTRRangerVillageLightStructure(boolean flag) {
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
            setBlockAndMetadata(world, 0, j1, 0, logBlock, logMeta);
            setGrassToDirt(world, 0, j1 - 1, 0);
        }
        for (j1 = 1; j1 <= 2; ++j1) {
            setBlockAndMetadata(world, 0, j1, 0, logBlock, logMeta);
        }
        setBlockAndMetadata(world, 0, 3, 0, plankBlock, plankMeta);
        setBlockAndMetadata(world, 0, 4, 0, fenceBlock, fenceMeta);
        setBlockAndMetadata(world, 0, 5, 0, LOTRLegacyBlocks.vanilla("torch"), 5);
        return true;
    }
}
