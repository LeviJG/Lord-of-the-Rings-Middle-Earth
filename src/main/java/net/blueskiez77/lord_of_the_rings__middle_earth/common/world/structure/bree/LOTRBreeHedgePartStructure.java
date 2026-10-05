package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.bree;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRBreeHedgePartStructure extends LOTRBreeStructure {
    public boolean grassOnly;

    public LOTRBreeHedgePartStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        int i1;
        int j1;
        int k1;
        setOriginAndRotation(world, i, j, k, rotation, 0);
        setupRandomBlocks(random);
        if (restrictions && (!isSurface(world, i1 = 0, j1 = getTopBlock(world, i1, k1 = 0) - 1, k1) || grassOnly && !LOTRLegacyBlocks.vanilla("grass").matches(getBlockState(world, i1, j1, k1)))) {
            return false;
        }
        int j12 = 0;
        while (!isOpaque(world, 0, j12, 0) && getY(j12) >= 0) {
            setBlockAndMetadata(world, 0, j12, 0, LOTRLegacyBlocks.mod("dirtPath"), 0);
            setGrassToDirt(world, 0, j12 - 1, 0);
            --j12;
        }
        boolean hasBeams = random.nextInt(4) == 0;
        int height = 3 + random.nextInt(2);
        for (j1 = 1; j1 <= height; ++j1) {
            if (hasBeams && j1 <= 2) {
                setBlockAndMetadata(world, 0, j1, 0, beamBlock, beamMeta);
                setGrassToDirt(world, 0, j1 - 1, 0);
                continue;
            }
            if (random.nextInt(4) == 0) {
                setBlockAndMetadata(world, 0, j1, 0, fenceBlock, fenceMeta);
                continue;
            }
            int randLeaf = random.nextInt(4);
            switch (randLeaf) {
                case 0:
                    setBlockAndMetadata(world, 0, j1, 0, LOTRLegacyBlocks.vanilla("leaves"), 4);
                    continue;
                case 1:
                    setBlockAndMetadata(world, 0, j1, 0, LOTRLegacyBlocks.mod("leaves2"), 5);
                    continue;
                case 2:
                    setBlockAndMetadata(world, 0, j1, 0, LOTRLegacyBlocks.mod("leaves4"), 4);
                    continue;
                default:
                    break;
            }
            setBlockAndMetadata(world, 0, j1, 0, LOTRLegacyBlocks.mod("leaves7"), 4);
        }
        return true;
    }

    public LOTRBreeHedgePartStructure setGrassOnly() {
        grassOnly = true;
        return this;
    }

    @Override
    public void setupRandomBlocks(RandomSource random) {
        super.setupRandomBlocks(random);
        fenceBlock = LOTRLegacyBlocks.vanilla("fence");
        fenceMeta = 0;
        beamBlock = LOTRLegacyBlocks.mod("woodBeamV1");
        beamMeta = 0;
    }
}
