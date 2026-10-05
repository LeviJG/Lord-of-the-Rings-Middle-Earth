package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.rhun;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class LOTREasterlingTownWallStructure extends LOTREasterlingStructure {
    public int xMin;
    public int xMax;
    public boolean isCentre;

    public LOTREasterlingTownWallStructure(boolean flag, int x0, int x1, boolean c) {
        super(flag);
        xMin = x0;
        xMax = x1;
        isCentre = c;
    }

    public static LOTREasterlingTownWallStructure Centre(boolean flag) {
        return new LOTREasterlingTownWallStructure(flag, -7, 7, true);
    }

    public static LOTREasterlingTownWallStructure Left(boolean flag) {
        return new LOTREasterlingTownWallStructure(flag, -4, 3, false);
    }

    public static LOTREasterlingTownWallStructure Right(boolean flag) {
        return new LOTREasterlingTownWallStructure(flag, -3, 4, false);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        setOriginAndRotation(world, i, j, k, rotation, 0);
        setupRandomBlocks(random);
        for (int i1 = xMin; i1 <= xMax; ++i1) {
            findSurface(world, i1, 0);
            for (int k1 = -1; k1 <= 1; ++k1) {
                int j1;
                for (j1 = 0; (j1 >= 0 || !isOpaque(world, i1, j1, k1)) && getY(j1) >= world.getMinY(); --j1) {
                    setBlockAndMetadata(world, i1, j1, k1, brickBlock, brickMeta);
                    setGrassToDirt(world, i1, j1 - 1, k1);
                }
                setBlockAndMetadata(world, i1, 1, k1, brickRedBlock, brickRedMeta);
                for (j1 = 2; j1 <= 5; ++j1) {
                    setBlockAndMetadata(world, i1, j1, k1, brickBlock, brickMeta);
                }
                if (k1 != 0) {
                    continue;
                }
                setBlockAndMetadata(world, i1, 5, k1, brickRedBlock, brickRedMeta);
            }
            if (Math.floorMod(i1, 2) == (isCentre ? 1 : 0)) {
                setBlockAndMetadata(world, i1, 5, -2, brickStairBlock, 6);
                setBlockAndMetadata(world, i1, 6, -2, brickBlock, brickMeta);
                setBlockAndMetadata(world, i1, 7, -2, brickStairBlock, 3);
                continue;
            }
            setBlockAndMetadata(world, i1, 6, -2, brickWallBlock, brickWallMeta);
        }
        return true;
    }
}
