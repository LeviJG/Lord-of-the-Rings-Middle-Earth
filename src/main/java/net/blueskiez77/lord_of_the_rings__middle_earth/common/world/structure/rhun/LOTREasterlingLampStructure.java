package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.rhun;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class LOTREasterlingLampStructure extends LOTREasterlingStructure {
    public LOTREasterlingLampStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        int k1;
        int i1;
        setOriginAndRotation(world, i, j, k, rotation, 0);
        setupRandomBlocks(random);
        if (restrictions) {
            for (i1 = -1; i1 <= 1; ++i1) {
                for (k1 = -1; k1 <= 1; ++k1) {
                    int j1 = getTopBlock(world, i1, k1) - 1;
                    if (isSurface(world, i1, j1, k1)) {
                        continue;
                    }
                    return false;
                }
            }
        }
        for (i1 = -1; i1 <= 1; ++i1) {
            for (k1 = -1; k1 <= 1; ++k1) {
                int j1;
                int i2 = Math.abs(i1);
                int k2 = Math.abs(k1);
                for (j1 = 1; j1 <= 6; ++j1) {
                    setAir(world, i1, j1, k1);
                }
                for (j1 = 0; (j1 >= 0 || !isOpaque(world, i1, j1, k1)) && getY(j1) >= world.getMinY(); --j1) {
                    if (i2 == 1 && k2 == 1) {
                        setBlockAndMetadata(world, i1, j1, k1, pillarBlock, pillarMeta);
                    } else {
                        setBlockAndMetadata(world, i1, j1, k1, brickBlock, brickMeta);
                    }
                    setGrassToDirt(world, i1, j1 - 1, k1);
                }
                if (i2 == 0 && k2 == 0) {
                    setBlockAndMetadata(world, i1, 1, k1, brickBlock, brickMeta);
                    setBlockAndMetadata(world, i1, 2, k1, brickBlock, brickMeta);
                }
                if (i2 + k2 != 1) {
                    continue;
                }
                setBlockAndMetadata(world, i1, 1, k1, brickWallBlock, brickWallMeta);
            }
        }
        setBlockAndMetadata(world, 0, 3, 0, LOTRLegacyBlocks.vanilla("glowstone"), 0);
        setBlockAndMetadata(world, 0, 3, -1, trapdoorBlock, 4);
        setBlockAndMetadata(world, 0, 3, 1, trapdoorBlock, 5);
        setBlockAndMetadata(world, -1, 3, 0, trapdoorBlock, 7);
        setBlockAndMetadata(world, 1, 3, 0, trapdoorBlock, 6);
        return true;
    }

    @Override
    public void setupRandomBlocks(RandomSource random) {
        super.setupRandomBlocks(random);
        trapdoorBlock = LOTRLegacyBlocks.mod("trapdoorRedwood");
    }
}
