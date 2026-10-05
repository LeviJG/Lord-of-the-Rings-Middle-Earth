package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public abstract class LOTRSouthronFortWallStructure extends LOTRSouthronStructure {
    public boolean isLong;

    protected LOTRSouthronFortWallStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean canUseRedBricks() {
        return false;
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        setOriginAndRotation(world, i, j, k, rotation, 0);
        setupRandomBlocks(random);
        int xMin = -1;
        int xMax = 1;
        if (isLong) {
            xMin = -2;
            xMax = 2;
        }
        for (int i1 = xMin; i1 <= xMax; ++i1) {
            int j1;
            int j12;
            boolean beam;
            int i2 = Math.abs(i1);
            int k1 = 0;
            findSurface(world, i1, k1);
            beam = i2 % 4 == 2;
            if (beam) {
                for (j12 = 6; (j12 >= 1 || !isOpaque(world, i1, j12, k1)) && getY(j12) >= world.getMinY(); --j12) {
                    setBlockAndMetadata(world, i1, j12, k1, woodBeamBlock, woodBeamMeta);
                    setGrassToDirt(world, i1, j12 - 1, k1);
                }
            } else {
                for (j12 = 5; (j12 >= 1 || !isOpaque(world, i1, j12, k1)) && getY(j12) >= world.getMinY(); --j12) {
                    setBlockAndMetadata(world, i1, j12, k1, plankBlock, plankMeta);
                    setGrassToDirt(world, i1, j12 - 1, k1);
                }
                //noinspection BadOddness
                if (i2 % 2 == 1) {
                    setBlockAndMetadata(world, i1, 5, k1, plankStairBlock, 2);
                } else {
                    setBlockAndMetadata(world, i1, 6, k1, fenceBlock, fenceMeta);
                }
            }
            int k3 = k1 - 1;
            setBlockAndMetadata(world, i1, 2, k3, brickStairBlock, 2);
            for (j1 = 1; (j1 >= 1 || !isOpaque(world, i1, j1, k3)) && getY(j1) >= world.getMinY(); --j1) {
                setBlockAndMetadata(world, i1, j1, k3, brickBlock, brickMeta);
                setGrassToDirt(world, i1, j1 - 1, k3);
            }
            k3 = k1 + 1;
            setBlockAndMetadata(world, i1, 2, k3, brickStairBlock, 3);
            for (j1 = 1; (j1 >= 1 || !isOpaque(world, i1, j1, k3)) && getY(j1) >= world.getMinY(); --j1) {
                setBlockAndMetadata(world, i1, j1, k3, brickBlock, brickMeta);
                setGrassToDirt(world, i1, j1 - 1, k3);
            }
        }
        return true;
    }

    public static class Long extends LOTRSouthronFortWallStructure {
        public Long(boolean flag) {
            super(flag);
            isLong = true;
        }
    }

    public static class Short extends LOTRSouthronFortWallStructure {
        public Short(boolean flag) {
            super(flag);
            isLong = false;
        }
    }

}
