package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRSouthronVillageFenceStructure extends LOTRSouthronStructure {
    public int leftExtent;
    public int rightExtent;

    public LOTRSouthronVillageFenceStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        setOriginAndRotation(world, i, j, k, rotation, 0);
        setupRandomBlocks(random);
        for (int i1 = -leftExtent; i1 <= rightExtent; ++i1) {
            int k1 = 0;
            int j1 = getTopBlock(world, i1, k1) - 1;
            if (!isSurface(world, i1, j1, k1) || isOpaque(world, i1, j1 + 1, k1)) {
                continue;
            }
            setBlockAndMetadata(world, i1, j1 + 1, k1, fenceBlock, fenceMeta);
        }
        return true;
    }

    public LOTRSouthronVillageFenceStructure setLeftRightExtent(int l, int r) {
        leftExtent = l;
        rightExtent = r;
        return this;
    }

    @Override
    public void setupRandomBlocks(RandomSource random) {
        super.setupRandomBlocks(random);
        fenceBlock = LOTRLegacyBlocks.mod("fence2");
        fenceMeta = 2;
    }
}
