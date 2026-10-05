package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRGondorFortWallCornerStructure extends LOTRGondorStructure {
    public LOTRGondorFortWallCornerStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        setOriginAndRotation(world, i, j, k, rotation, 0);
        setupRandomBlocks(random);
        for (int l = -8; l <= 8; ++l) {
            int j1;
            int k1;
            boolean pillar;
            int i1;
            if (l >= 0) {
                i1 = l / 2;
                k1 = -((l + 1) / 2);
            } else {
                i1 = -(Math.abs(l) + 1) / 2;
                k1 = Math.abs(l) / 2;
            }
            findSurface(world, i1, k1);
            pillar = Math.abs(l) == 3;
            if (pillar) {
                for (j1 = 4; (j1 >= 1 || !isOpaque(world, i1, j1, k1)) && getY(j1) >= 0; --j1) {
                    setBlockAndMetadata(world, i1, j1, k1, pillar2Block, pillar2Meta);
                    setGrassToDirt(world, i1, j1 - 1, k1);
                }
            } else {
                for (j1 = 4; (j1 >= 1 || !isOpaque(world, i1, j1, k1)) && getY(j1) >= 0; --j1) {
                    setBlockAndMetadata(world, i1, j1, k1, brickBlock, brickMeta);
                    setGrassToDirt(world, i1, j1 - 1, k1);
                }
            }
            setBlockAndMetadata(world, i1, 5, k1, brick2WallBlock, brick2WallMeta);
            if (pillar) {
                setBlockAndMetadata(world, i1, 6, k1, LOTRLegacyBlocks.vanilla("torch"), 5);
            }
            if (Math.floorMod(l, 2) != 0) {
                continue;
            }
            if (l >= 0) {
                setBlockAndMetadata(world, i1, 4, k1 + 1, rockSlabBlock, rockSlabMeta | 8);
                continue;
            }
            setBlockAndMetadata(world, i1 + 1, 4, k1, rockSlabBlock, rockSlabMeta | 8);
        }
        return true;
    }
}
