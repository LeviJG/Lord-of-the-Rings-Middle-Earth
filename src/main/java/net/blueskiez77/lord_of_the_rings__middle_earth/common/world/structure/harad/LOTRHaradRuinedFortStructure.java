package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad;

import java.util.ArrayList;
import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRHaradRuinedFortStructure extends LOTRStructureBase2 {
    public LOTRHaradRuinedFortStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        int j1;
        int k1;
        int i1;
        setOriginAndRotation(world, i, j, k, rotation, 4);
        setupRandomBlocks(random);
        if (restrictions) {
            for (i1 = -7; i1 <= 12; ++i1) {
                for (k1 = -3; k1 <= 4; ++k1) {
                    j1 = getTopBlock(world, i1, k1) - 1;
                    if (j1 >= -4) {
                        continue;
                    }
                    return false;
                }
            }
        }
        if (usingPlayer == null) {
            originY -= 4 + random.nextInt(5);
        }
        for (i1 = -7; i1 <= 12; ++i1) {
            for (k1 = -3; k1 <= 4; ++k1) {
                j1 = -2;
                while (!isOpaque(world, i1, j1, k1) && getY(j1) >= world.getMinY()) {
                    if (random.nextInt(4) == 0) {
                        setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.mod("brick3"), 11);
                    } else {
                        setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.mod("brick"), 15);
                    }
                    setGrassToDirt(world, i1, j1 - 1, k1);
                    --j1;
                }
            }
        }
        loadStrScan("harad_ruined_fort");
        addBlockMetaAliasOption("BRICK", 3, LOTRLegacyBlocks.mod("brick"), 15);
        addBlockMetaAliasOption("BRICK", 1, LOTRLegacyBlocks.mod("brick3"), 11);
        addBlockMetaAliasOption("BRICK_SLAB", 3, LOTRLegacyBlocks.mod("slabSingle4"), 0);
        addBlockMetaAliasOption("BRICK_SLAB", 1, LOTRLegacyBlocks.mod("slabSingle7"), 1);
        addBlockMetaAliasOption("BRICK_SLAB_INV", 3, LOTRLegacyBlocks.mod("slabSingle4"), 8);
        addBlockMetaAliasOption("BRICK_SLAB_INV", 1, LOTRLegacyBlocks.mod("slabSingle7"), 9);
        addBlockAliasOption("BRICK_STAIR", 3, LOTRLegacyBlocks.mod("stairsNearHaradBrick"));
        addBlockAliasOption("BRICK_STAIR", 1, LOTRLegacyBlocks.mod("stairsNearHaradBrickCracked"));
        addBlockMetaAliasOption("BRICK_WALL", 3, LOTRLegacyBlocks.mod("wall"), 15);
        addBlockMetaAliasOption("BRICK_WALL", 1, LOTRLegacyBlocks.mod("wall3"), 3);
        addBlockMetaAliasOption("PILLAR", 4, LOTRLegacyBlocks.mod("pillar"), 5);
        generateStrScan(world, random, 0, 1, 0);
        List<int[]> chestCoords = new ArrayList<>();
        chestCoords.add(new int[]{3, 1, -2});
        chestCoords.add(new int[]{0, 1, -2});
        chestCoords.add(new int[]{-3, 1, -2});
        chestCoords.add(new int[]{-6, 1, -2});
        chestCoords.add(new int[]{8, 1, 0});
        chestCoords.add(new int[]{10, 1, 1});
        chestCoords.add(new int[]{11, 1, 3});
        chestCoords.add(new int[]{8, 1, 3});
        chestCoords.add(new int[]{6, 1, 3});
        chestCoords.add(new int[]{3, 1, 3});
        chestCoords.add(new int[]{0, 1, 3});
        chestCoords.add(new int[]{-3, 1, 3});
        chestCoords.add(new int[]{-6, 1, 3});
        chestCoords.add(new int[]{6, 2, -2});
        chestCoords.add(new int[]{6, 2, 0});
        chestCoords.add(new int[]{6, 6, -2});
        chestCoords.add(new int[]{-6, 6, -2});
        chestCoords.add(new int[]{-1, 6, -1});
        chestCoords.add(new int[]{8, 6, 0});
        chestCoords.add(new int[]{10, 6, 1});
        chestCoords.add(new int[]{0, 6, 1});
        chestCoords.add(new int[]{-2, 6, 1});
        chestCoords.add(new int[]{-6, 6, 1});
        chestCoords.add(new int[]{8, 6, 3});
        chestCoords.add(new int[]{0, 6, 3});
        chestCoords.add(new int[]{-2, 6, 3});
        chestCoords.add(new int[]{-6, 6, 3});
        int chests = 2 + random.nextInt(4);
        while (chestCoords.size() > chests) {
            chestCoords.remove(random.nextInt(chestCoords.size()));
        }
        for (int[] coords : chestCoords) {
            placeChest(world, random, coords[0], coords[1], coords[2], LOTRLegacyBlocks.mod("chestBasket"), Mth.randomBetweenInclusive(random, 2, 4), LOTRChestContents.NEAR_HARAD_PYRAMID);
        }
        return true;
    }
}
