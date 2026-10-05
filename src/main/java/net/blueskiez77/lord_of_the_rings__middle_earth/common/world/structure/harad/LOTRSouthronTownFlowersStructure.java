package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad;

import net.minecraft.world.level.block.state.BlockState;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;

public class LOTRSouthronTownFlowersStructure extends LOTRSouthronStructure {
    public LOTRSouthronTownFlowersStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        int k1;
        int i1;
        setOriginAndRotation(world, i, j, k, rotation, 1);
        setupRandomBlocks(random);
        ItemStack flower = getRandomFlower(world, random);
        BlockState flowerState = Block.byItem(flower.getItem()).defaultBlockState();
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
                for (j1 = 1; j1 <= 4; ++j1) {
                    setAir(world, i1, j1, k1);
                }
                for (j1 = 0; (j1 >= 0 || !isOpaque(world, i1, j1, k1)) && getY(j1) >= 0; --j1) {
                    setBlockAndMetadata(world, i1, j1, k1, stoneBlock, stoneMeta);
                    setGrassToDirt(world, i1, j1 - 1, k1);
                }
                //noinspection BadOddness
                if ((k1 == 0 || k1 == 3) && i2 % 2 == 1) {
                    setBlockAndMetadata(world, i1, 1, k1, brickSlabBlock, brickSlabMeta);
                }
                if (k1 < 1 || k1 > 2 || i2 > 2) {
                    continue;
                }
                setBlockAndMetadata(world, i1, 0, k1, LOTRLegacyBlocks.vanilla("grass"), 0);
                setBlockState(world, i1, 1, k1, flowerState);
            }
        }
        return true;
    }
}
