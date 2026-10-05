package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.gondor.LOTRGondorSoldierEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LOTRGondorTurretStructure extends LOTRStructureBase2 {
    public LOTRGondorTurretStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        int j1;
        int j12;
        int k1;
        int i1;
        setOriginAndRotation(world, i, j, k, rotation, 3);
        if (restrictions) {
            for (i1 = -2; i1 <= 2; ++i1) {
                for (k1 = -2; k1 <= 2; ++k1) {
                    j12 = getTopBlock(world, i1, k1);
                    BlockState block = getBlockState(world, i1, j12 - 1, k1);
                    if (LOTRLegacyBlocks.vanilla("grass").matches(block)) {
                        continue;
                    }
                    return false;
                }
            }
        }
        for (i1 = -2; i1 <= 2; ++i1) {
            for (k1 = -2; k1 <= 2; ++k1) {
                setBlockAndMetadata(world, i1, 0, k1, LOTRLegacyBlocks.mod("slabDouble"), 2);
                j12 = -1;
                while (!isOpaque(world, i1, j12, k1) && getY(j12) >= world.getMinY()) {
                    setBlockAndMetadata(world, i1, j12, k1, LOTRLegacyBlocks.mod("slabDouble"), 2);
                    setGrassToDirt(world, i1, j12 - 1, k1);
                    --j12;
                }
            }
        }
        for (j1 = 1; j1 <= 4; ++j1) {
            for (int i12 = -1; i12 <= 1; ++i12) {
                for (int k12 = -1; k12 <= 1; ++k12) {
                    if (Math.abs(i12) == 1 && Math.abs(k12) == 1) {
                        setBlockAndMetadata(world, i12, j1, k12, LOTRLegacyBlocks.mod("brick"), 5);
                        continue;
                    }
                    setBlockAndMetadata(world, i12, j1, k12, LOTRLegacyBlocks.mod("rock"), 1);
                }
            }
        }
        setBlockAndMetadata(world, -2, 1, -2, LOTRLegacyBlocks.mod("slabDouble"), 2);
        setBlockAndMetadata(world, -2, 1, 2, LOTRLegacyBlocks.mod("slabDouble"), 2);
        setBlockAndMetadata(world, 2, 1, -2, LOTRLegacyBlocks.mod("slabDouble"), 2);
        setBlockAndMetadata(world, 2, 1, 2, LOTRLegacyBlocks.mod("slabDouble"), 2);
        for (j1 = 2; j1 <= 4; ++j1) {
            setBlockAndMetadata(world, -2, j1, -2, LOTRLegacyBlocks.mod("wall"), 2);
            setBlockAndMetadata(world, -2, j1, 2, LOTRLegacyBlocks.mod("wall"), 2);
            setBlockAndMetadata(world, 2, j1, -2, LOTRLegacyBlocks.mod("wall"), 2);
            setBlockAndMetadata(world, 2, j1, 2, LOTRLegacyBlocks.mod("wall"), 2);
        }
        setBlockAndMetadata(world, -2, 5, -2, LOTRLegacyBlocks.vanilla("log"), 0);
        setBlockAndMetadata(world, -2, 5, 2, LOTRLegacyBlocks.vanilla("log"), 0);
        setBlockAndMetadata(world, 2, 5, -2, LOTRLegacyBlocks.vanilla("log"), 0);
        setBlockAndMetadata(world, 2, 5, 2, LOTRLegacyBlocks.vanilla("log"), 0);
        for (int k13 = -1; k13 <= 1; ++k13) {
            setBlockAndMetadata(world, -2, 1, k13, LOTRLegacyBlocks.mod("stairsGondorBrick"), 1);
            setBlockAndMetadata(world, 2, 1, k13, LOTRLegacyBlocks.mod("stairsGondorBrick"), 0);
            setBlockAndMetadata(world, -2, 3, k13, LOTRLegacyBlocks.mod("slabSingle"), 10);
            setBlockAndMetadata(world, -2, 4, k13, LOTRLegacyBlocks.vanilla("log"), 0);
            setBlockAndMetadata(world, -2, 5, k13, LOTRLegacyBlocks.vanilla("log"), 0);
            setBlockAndMetadata(world, 2, 3, k13, LOTRLegacyBlocks.mod("slabSingle"), 10);
            setBlockAndMetadata(world, 2, 4, k13, LOTRLegacyBlocks.vanilla("log"), 0);
            setBlockAndMetadata(world, 2, 5, k13, LOTRLegacyBlocks.vanilla("log"), 0);
        }
        for (i1 = -1; i1 <= 1; ++i1) {
            setBlockAndMetadata(world, i1, 1, 2, LOTRLegacyBlocks.mod("stairsGondorBrick"), 3);
            setBlockAndMetadata(world, i1, 3, -2, LOTRLegacyBlocks.mod("slabSingle"), 10);
            setBlockAndMetadata(world, i1, 4, -2, LOTRLegacyBlocks.vanilla("log"), 0);
            setBlockAndMetadata(world, i1, 5, -2, LOTRLegacyBlocks.vanilla("log"), 0);
            setBlockAndMetadata(world, i1, 3, 2, LOTRLegacyBlocks.mod("slabSingle"), 10);
            setBlockAndMetadata(world, i1, 4, 2, LOTRLegacyBlocks.vanilla("log"), 0);
            setBlockAndMetadata(world, i1, 5, 2, LOTRLegacyBlocks.vanilla("log"), 0);
        }
        for (j1 = 1; j1 <= 4; ++j1) {
            setBlockAndMetadata(world, 0, j1, 0, LOTRLegacyBlocks.vanilla("ladder"), 2);
        }
        setBlockAndMetadata(world, 0, 1, -1, LOTRLegacyBlocks.mod("doorLebethron"), 1);
        setBlockAndMetadata(world, 0, 2, -1, LOTRLegacyBlocks.mod("doorLebethron"), 8);
        setBlockAndMetadata(world, 0, 5, 0, LOTRLegacyBlocks.vanilla("trapdoor"), 0);
        setBlockAndMetadata(world, 0, 5, 1, LOTRLegacyBlocks.mod("slabSingle"), 2);
        placeChest(world, random, 1, 5, 1, LOTRLegacyBlocks.mod("chestLebethron"), 2, LOTRChestContents.GONDOR_FORTRESS_SUPPLIES);
        for (i1 = -2; i1 <= 2; ++i1) {
            for (k1 = -2; k1 <= 2; ++k1) {
                if (Math.abs(i1) != 2 && Math.abs(k1) != 2) {
                    continue;
                }
                setBlockAndMetadata(world, i1, 6, k1, LOTRLegacyBlocks.mod("wall"), 2);
                if (Math.abs(i1) != 2 || Math.abs(k1) != 2) {
                    continue;
                }
                setBlockAndMetadata(world, i1, 7, k1, LOTRLegacyBlocks.vanilla("torch"), 5);
            }
        }
        int soldiers = 1 + random.nextInt(2);
        for (int l = 0; l < soldiers; ++l) {
            LOTRGondorSoldierEntity soldier = random.nextBoolean() ? create(LOTREntities.GONDOR_SOLDIER, world) : create(LOTREntities.GONDOR_ARCHER, world);
            soldier.spawnRidingHorse = false;
            spawnNPCAndSetHome(soldier, world, 0, 6, 0, 8);
        }
        return true;
    }
}
