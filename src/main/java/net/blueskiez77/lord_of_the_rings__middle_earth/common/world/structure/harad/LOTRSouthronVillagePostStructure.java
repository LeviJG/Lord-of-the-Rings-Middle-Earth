package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRSouthronVillagePostStructure extends LOTRSouthronStructure {
    public LOTRSouthronVillagePostStructure(boolean flag) {
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
        for (int j12 = 0; (j12 >= 0 || !isOpaque(world, 0, j12, 0)) && getY(j12) >= 0; --j12) {
            setBlockAndMetadata(world, 0, j12, 0, woodBeamBlock, woodBeamMeta);
            setGrassToDirt(world, 0, j12 - 1, 0);
        }
        setBlockAndMetadata(world, 0, 1, 0, woodBeamBlock, woodBeamMeta);
        setBlockAndMetadata(world, 0, 2, 0, woodBeamBlock, woodBeamMeta);
        setBlockAndMetadata(world, 0, 3, 0, plankSlabBlock, plankSlabMeta);
        return true;
    }

    @Override
    public void setupRandomBlocks(RandomSource random) {
        super.setupRandomBlocks(random);
        plankSlabBlock = LOTRLegacyBlocks.mod("woodSlabSingle3");
        plankSlabMeta = 2;
        woodBeamBlock = LOTRLegacyBlocks.mod("woodBeam4");
        woodBeamMeta = 2;
    }
}
