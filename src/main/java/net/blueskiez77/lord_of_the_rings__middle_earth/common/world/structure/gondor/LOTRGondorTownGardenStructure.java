package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;

public class LOTRGondorTownGardenStructure extends LOTRGondorStructure {
    public LOTRGondorTownGardenStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        int k1;
        int i1;
        setOriginAndRotation(world, i, j, k, rotation, 0);
        setupRandomBlocks(random);
        if (restrictions) {
            for (i1 = -3; i1 <= 3; ++i1) {
                for (k1 = 0; k1 <= 3; ++k1) {
                    int j1 = getTopBlock(world, i1, k1) - 1;
                    if (isSurface(world, i1, j1, k1)) {
                        continue;
                    }
                    return false;
                }
            }
        }
        for (i1 = -3; i1 <= 3; ++i1) {
            for (k1 = 0; k1 <= 3; ++k1) {
                int j1;
                int i2 = Math.abs(i1);
                for (j1 = 0; (j1 >= 0 || !isOpaque(world, i1, j1, k1)) && getY(j1) >= world.getMinY(); --j1) {
                    setBlockAndMetadata(world, i1, j1, k1, rockSlabDoubleBlock, rockSlabDoubleMeta);
                    setGrassToDirt(world, i1, j1 - 1, k1);
                }
                for (j1 = 1; j1 <= 3; ++j1) {
                    setAir(world, i1, j1, k1);
                }
                if (i2 <= 2 && k1 >= 1 && k1 <= 2) {
                    setBlockAndMetadata(world, i1, 0, k1, LOTRLegacyBlocks.vanilla("grass"), 0);
                }
                if (i2 != 3 || k1 != 0 && k1 != 3) {
                    continue;
                }
                setBlockAndMetadata(world, i1, 1, k1, rockWallBlock, rockWallMeta);
                setBlockAndMetadata(world, i1, 2, k1, LOTRLegacyBlocks.vanilla("torch"), 5);
            }
        }
        for (int k12 = 1; k12 <= 2; ++k12) {
            ItemStack flower = getRandomFlower(world, random);
            for (int i12 = -2; i12 <= 2; ++i12) {
                setBlockState(world, i12, 1, k12, Block.byItem(flower.getItem()).defaultBlockState());
            }
        }
        return true;
    }
}
