package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor;

import java.util.ArrayList;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRGondorTownTreesStructure extends LOTRGondorStructure {
    public LOTRGondorTownTreesStructure(boolean flag) {
        super(flag);
    }

    public static String getRandomTree(RandomSource random) {
        ArrayList<String> treeList = new ArrayList<>();
        treeList.add("CYPRESS");
        return treeList.get(random.nextInt(treeList.size()));
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        int j1;
        int k1;
        int i1;
        setOriginAndRotation(world, i, j, k, rotation, 2);
        setupRandomBlocks(random);
        if (restrictions) {
            for (i1 = -6; i1 <= 6; ++i1) {
                for (k1 = -2; k1 <= 2; ++k1) {
                    int j12 = getTopBlock(world, i1, k1) - 1;
                    if (isSurface(world, i1, j12, k1)) {
                        continue;
                    }
                    return false;
                }
            }
        }
        for (i1 = -6; i1 <= 6; ++i1) {
            for (k1 = -2; k1 <= 2; ++k1) {
                int i2 = Math.abs(i1);
                int k2 = Math.abs(k1);
                for (j1 = 0; (j1 >= 0 || !isOpaque(world, i1, j1, k1)) && getY(j1) >= world.getMinY(); --j1) {
                    setBlockAndMetadata(world, i1, j1, k1, rockSlabDoubleBlock, rockSlabDoubleMeta);
                    setGrassToDirt(world, i1, j1 - 1, k1);
                }
                for (j1 = 1; j1 <= 10; ++j1) {
                    setAir(world, i1, j1, k1);
                }
                if (i2 % 4 != 2 && k2 <= 1) {
                    setBlockAndMetadata(world, i1, 0, k1, LOTRLegacyBlocks.vanilla("grass"), 0);
                }
                if (i2 % 4 != 2 || k2 != 2) {
                    continue;
                }
                setBlockAndMetadata(world, i1, 1, k1, rockWallBlock, rockWallMeta);
                setBlockAndMetadata(world, i1, 2, k1, LOTRLegacyBlocks.vanilla("torch"), 5);
            }
        }
        for (int i12 : new int[]{-4, 0, 4}) {
            j1 = 1;
            int k12 = 0;
            //noinspection StatementWithEmptyBody
            for (int l = 0; l < 16 && !placeTree(world, random, getRandomTree(random), getX(i12, k12), getY(j1), getZ(i12, k12)); ++l) {
            }
        }
        return true;
    }
}
