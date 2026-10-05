package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.bree;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LOTRBreeGardenStructure extends LOTRBreeStructure {
    public LOTRBreeGardenStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        int i1;
        int j1;
        int k1;
        setOriginAndRotation(world, i, j, k, rotation, 8);
        setupRandomBlocks(random);
        if (restrictions) {
            for (i1 = -6; i1 <= 6; ++i1) {
                for (k1 = -3; k1 <= 3; ++k1) {
                    j1 = getTopBlock(world, i1, k1) - 1;
                    if (isSurface(world, i1, j1, k1)) {
                        continue;
                    }
                    return false;
                }
            }
            for (i1 = -3; i1 <= 3; ++i1) {
                for (k1 = -8; k1 <= 4; ++k1) {
                    j1 = getTopBlock(world, i1, k1) - 1;
                    if (isSurface(world, i1, j1, k1)) {
                        continue;
                    }
                    return false;
                }
            }
        }
        for (i1 = -6; i1 <= 6; ++i1) {
            for (k1 = -3; k1 <= 3; ++k1) {
                for (j1 = 1; j1 <= 5; ++j1) {
                    setAir(world, i1, j1, k1);
                }
            }
        }
        for (i1 = -3; i1 <= 3; ++i1) {
            k1 = -4;
            for (j1 = 1; j1 <= 5; ++j1) {
                setAir(world, i1, j1, k1);
            }
        }
        for (i1 = -2; i1 <= 2; ++i1) {
            for (k1 = -8; k1 <= -5; ++k1) {
                for (j1 = 1; j1 <= 5; ++j1) {
                    setAir(world, i1, j1, k1);
                }
            }
        }
        loadStrScan("bree_garden");
        addBlockMetaAliasOption("COBBLE", 3, LOTRLegacyBlocks.vanilla("cobblestone"), 0);
        addBlockMetaAliasOption("COBBLE", 1, LOTRLegacyBlocks.vanilla("mossy_cobblestone"), 0);
        addBlockAliasOption("COBBLE_STAIR", 3, LOTRLegacyBlocks.vanilla("stone_stairs"));
        addBlockAliasOption("COBBLE_STAIR", 1, LOTRLegacyBlocks.mod("stairsCobblestoneMossy"));
        addBlockMetaAliasOption("COBBLE_WALL", 3, LOTRLegacyBlocks.vanilla("cobblestone_wall"), 0);
        addBlockMetaAliasOption("COBBLE_WALL", 1, LOTRLegacyBlocks.vanilla("cobblestone_wall"), 1);
        associateBlockMetaAlias("FENCE", fenceBlock, fenceMeta);
        associateBlockAlias("FENCE_GATE", fenceGateBlock);
        addBlockMetaAliasOption("LEAF", 1, LOTRLegacyBlocks.vanilla("leaves"), 4);
        addBlockMetaAliasOption("LEAF", 1, LOTRLegacyBlocks.mod("leaves4"), 4);
        addBlockMetaAliasOption("LEAF", 1, LOTRLegacyBlocks.mod("leaves2"), 5);
        addBlockMetaAliasOption("LEAF", 1, LOTRLegacyBlocks.mod("leaves7"), 4);
        addBlockMetaAliasOption("PATH", 14, LOTRLegacyBlocks.vanilla("grass"), 0);
        addBlockMetaAliasOption("PATH", 3, LOTRLegacyBlocks.vanilla("cobblestone"), 0);
        addBlockMetaAliasOption("PATH", 3, LOTRLegacyBlocks.vanilla("cobblestone"), 1);
        generateStrScan(world, random, 0, 0, 0);
        for (i1 = -5; i1 <= 5; ++i1) {
            for (k1 = -3; k1 <= 2; ++k1) {
                j1 = 1;
                BlockState below = getBlockState(world, i1, 0, k1);
                if (!LOTRLegacyBlocks.vanilla("grass").matches(below) || !isAir(world, i1, j1, k1) || random.nextInt(5) != 0) {
                    continue;
                }
                plantFlower(world, random, i1, j1, k1);
            }
        }
        return true;
    }
}
