package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.bree;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRBreeGateStructure extends LOTRBreeStructure {
    public LOTRBreeGateStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        int j1;
        int k1;
        setOriginAndRotation(world, i, j, k, rotation, 1);
        setupRandomBlocks(random);
        if (restrictions) {
            for (int i1 = -4; i1 <= 4; ++i1) {
                for (k1 = 0; k1 <= 0; ++k1) {
                    j1 = getTopBlock(world, i1, k1) - 1;
                    if (isSurface(world, i1, j1, k1)) {
                        continue;
                    }
                    return false;
                }
            }
        }
        for (int i1 : new int[]{-4, 4}) {
            int k12 = 0;
            for (int j12 = 4; (j12 >= 0 || !isOpaque(world, 0, j12, 0)) && getY(j12) >= 0; --j12) {
                setBlockAndMetadata(world, i1, j12, k12, beamBlock, beamMeta);
                setGrassToDirt(world, i1, j12 - 1, k12);
            }
            setBlockAndMetadata(world, i1, 5, k12, fenceBlock, fenceMeta);
            setBlockAndMetadata(world, i1, 6, k12, LOTRLegacyBlocks.vanilla("torch"), 5);
        }
        for (int i1 = -3; i1 <= 3; ++i1) {
            k1 = 0;
            j1 = 0;
            while (!isOpaque(world, i1, j1, k1) && getY(j1) >= 0) {
                placeRandomFloor(world, random, i1, j1, k1);
                setGrassToDirt(world, i1, j1 - 1, k1);
                --j1;
            }
            for (j1 = 1; j1 <= 3; ++j1) {
                setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.mod("gateWoodenCross"), 2);
            }
            setBlockAndMetadata(world, i1, 4, k1, plankSlabBlock, plankSlabMeta);
        }
        return true;
    }
}
