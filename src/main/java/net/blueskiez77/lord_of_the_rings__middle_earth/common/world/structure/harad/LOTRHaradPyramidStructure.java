package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LOTRHaradPyramidStructure extends LOTRStructureBase2 {
    public LOTRHaradPyramidStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        int pyramidRadius = 27;
        setOriginAndRotation(world, i, j, k, rotation, usingPlayer != null ? pyramidRadius : 0);
        setupRandomBlocks(random);
        if (restrictions) {
            for (int i1 = -pyramidRadius; i1 <= pyramidRadius; ++i1) {
                for (int k1 = -pyramidRadius; k1 <= pyramidRadius; ++k1) {
                    int j1 = getTopBlock(world, i1, k1) - 1;
                    BlockState block = getBlockState(world, i1, j1, k1);
                    if (isSurface(world, i1, j1, k1) || LOTRLegacyBlocks.vanilla("stone").matches(block) || LOTRLegacyBlocks.vanilla("sandstone").matches(block) || LOTRLegacyBlocks.mod("redSandstone").matches(block)) {
                        continue;
                    }
                    return false;
                }
            }
        }
        originY += Mth.randomBetweenInclusive(random, -2, 4);
        loadStrScan("harad_pyramid");
        addBlockMetaAliasOption("BRICK", 3, LOTRLegacyBlocks.mod("brick"), 15);
        addBlockMetaAliasOption("BRICK", 1, LOTRLegacyBlocks.mod("brick3"), 11);
        addBlockMetaAliasOption("BRICK_MAYBE", 4, LOTRLegacyBlocks.vanilla("air"), 0);
        addBlockMetaAliasOption("BRICK_MAYBE", 3, LOTRLegacyBlocks.mod("brick"), 15);
        addBlockMetaAliasOption("BRICK_MAYBE", 1, LOTRLegacyBlocks.mod("brick3"), 11);
        addBlockMetaAliasOption("BRICK_SLAB", 3, LOTRLegacyBlocks.mod("slabSingle4"), 0);
        addBlockMetaAliasOption("BRICK_SLAB", 1, LOTRLegacyBlocks.mod("slabSingle7"), 1);
        addBlockAliasOption("BRICK_STAIR", 3, LOTRLegacyBlocks.mod("stairsNearHaradBrick"));
        addBlockAliasOption("BRICK_STAIR", 1, LOTRLegacyBlocks.mod("stairsNearHaradBrickCracked"));
        addBlockMetaAliasOption("BRICK_WALL", 3, LOTRLegacyBlocks.mod("wall"), 15);
        addBlockMetaAliasOption("BRICK_WALL", 1, LOTRLegacyBlocks.mod("wall3"), 3);
        addBlockMetaAliasOption("PILLAR", 4, LOTRLegacyBlocks.mod("pillar"), 5);
        addBlockMetaAliasOption("PILLAR_SLAB", 4, LOTRLegacyBlocks.mod("slabSingle4"), 7);
        addBlockMetaAliasOption("BRICK2", 3, LOTRLegacyBlocks.mod("brick3"), 13);
        addBlockMetaAliasOption("BRICK2", 1, LOTRLegacyBlocks.mod("brick3"), 14);
        addBlockMetaAliasOption("BRICK2_SLAB", 3, LOTRLegacyBlocks.mod("slabSingle7"), 2);
        addBlockMetaAliasOption("BRICK2_SLAB", 1, LOTRLegacyBlocks.mod("slabSingle7"), 3);
        addBlockAliasOption("BRICK2_STAIR", 3, LOTRLegacyBlocks.mod("stairsNearHaradBrickRed"));
        addBlockAliasOption("BRICK2_STAIR", 1, LOTRLegacyBlocks.mod("stairsNearHaradBrickRedCracked"));
        addBlockMetaAliasOption("TUNNEL", 5, LOTRLegacyBlocks.vanilla("sand"), 0);
        addBlockMetaAliasOption("TUNNEL", 5, LOTRLegacyBlocks.vanilla("air"), 0);
        addBlockMetaAliasOption("ROOF", 4, LOTRLegacyBlocks.vanilla("sand"), 1);
        addBlockMetaAliasOption("ROOF", 4, LOTRLegacyBlocks.mod("redSandstone"), 0);
        addBlockMetaAliasOption("ROOF", 2, LOTRLegacyBlocks.mod("brick3"), 13);
        addBlockMetaAliasOption("ROOF", 2, LOTRLegacyBlocks.mod("brick3"), 14);
        generateStrScan(world, random, 0, 0, 0);
        placePyramidChest(world, random, -4, -6, 3, 2);
        placePyramidChest(world, random, 0, -6, 3, 2);
        placePyramidChest(world, random, 4, -6, 3, 2);
        placePyramidChest(world, random, -5, -5, -7, 4);
        placePyramidChest(world, random, -3, -5, -7, 5);
        placePyramidChest(world, random, 3, -5, -7, 4);
        placePyramidChest(world, random, 5, -5, -7, 5);
        placePyramidChest(world, random, -4, -5, -5, 2);
        placePyramidChest(world, random, 4, -5, -5, 2);
        placeSpawnerChest(world, random, 0, -6, 15, LOTRLegacyBlocks.mod("spawnerChestAncientHarad"), 2, LOTREntities.HARAD_PYRAMID_WRAITH, LOTRChestContents.NEAR_HARAD_PYRAMID, 12);
        placeMobSpawner(world, 0, -2, 15, LOTREntities.DESERT_SCORPION);
        placeMobSpawner(world, -12, -2, -12, LOTREntities.DESERT_SCORPION);
        placeMobSpawner(world, 12, -2, -12, LOTREntities.DESERT_SCORPION);
        placeMobSpawner(world, 0, 8, 0, LOTREntities.DESERT_SCORPION);
        placePyramidChest(world, random, -12, -1, -12, 2, true);
        placePyramidChest(world, random, 12, -1, -12, 2, true);
        placePyramidChest(world, random, 0, 9, 0, 2, true);
        return true;
    }

    public void placePyramidChest(WorldGenLevel world, RandomSource random, int i, int j, int k, int meta) {
        placePyramidChest(world, random, i, j, k, meta, random.nextBoolean());
    }

    public void placePyramidChest(WorldGenLevel world, RandomSource random, int i, int j, int k, int meta, boolean trap) {
        int amount = Mth.randomBetweenInclusive(random, 3, 5);
        if (trap) {
            placeSpawnerChest(world, random, i, j, k, LOTRLegacyBlocks.mod("spawnerChestStone"), meta, LOTREntities.HARAD_PYRAMID_WRAITH, LOTRChestContents.NEAR_HARAD_PYRAMID, amount);
        } else {
            placeChest(world, random, i, j, k, LOTRLegacyBlocks.mod("chestStone"), meta, LOTRChestContents.NEAR_HARAD_PYRAMID, amount);
        }
    }
}
