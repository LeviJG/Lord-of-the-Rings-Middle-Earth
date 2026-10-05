package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRNPCRespawnerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LOTRRangerWatchtowerStructure extends LOTRStructureBase2 {
    public LegacyBlock woodBlock;
    public int woodMeta;
    public LegacyBlock plankBlock;
    public int plankMeta;
    public LegacyBlock fenceBlock;
    public int fenceMeta;
    public LegacyBlock stairBlock;
    public LegacyBlock trapdoorBlock;

    public LOTRRangerWatchtowerStructure(boolean flag) {
        super(flag);
    }

    public void generateSupportPillar(WorldGenLevel world, int i, int j, int k) {
        int j1 = j;
        while (!isOpaque(world, i, j1, k) && getY(j1) >= world.getMinY()) {
            setBlockAndMetadata(world, i, j1, k, woodBlock, woodMeta);
            setGrassToDirt(world, i, j1 - 1, i);
            --j1;
        }
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        int j1;
        int i1;
        int k1;
        int randomWood;
        int i12;
        setOriginAndRotation(world, i, j, k, rotation, 0);
        if (restrictions) {
            for (int i13 = -4; i13 <= 4; ++i13) {
                for (int k12 = -4; k12 <= 4; ++k12) {
                    int j12 = getTopBlock(world, i13, k12);
                    BlockState block = getBlockState(world, i13, j12 - 1, k12);
                    if (LOTRLegacyBlocks.vanilla("grass").matches(block)) {
                        continue;
                    }
                    return false;
                }
            }
        }
        randomWood = random.nextInt(4);
        switch (randomWood) {
            case 0:
                woodBlock = LOTRLegacyBlocks.vanilla("log");
                woodMeta = 0;
                plankBlock = LOTRLegacyBlocks.vanilla("planks");
                plankMeta = 0;
                fenceBlock = LOTRLegacyBlocks.vanilla("fence");
                fenceMeta = 0;
                stairBlock = LOTRLegacyBlocks.vanilla("oak_stairs");
                trapdoorBlock = LOTRLegacyBlocks.vanilla("trapdoor");
                break;
            case 1:
                woodBlock = LOTRLegacyBlocks.vanilla("log");
                woodMeta = 1;
                plankBlock = LOTRLegacyBlocks.vanilla("planks");
                plankMeta = 1;
                fenceBlock = LOTRLegacyBlocks.vanilla("fence");
                fenceMeta = 0;
                stairBlock = LOTRLegacyBlocks.vanilla("spruce_stairs");
                trapdoorBlock = LOTRLegacyBlocks.mod("trapdoorSpruce");
                break;
            case 2:
                woodBlock = LOTRLegacyBlocks.mod("wood2");
                woodMeta = 1;
                plankBlock = LOTRLegacyBlocks.mod("planks");
                plankMeta = 9;
                fenceBlock = LOTRLegacyBlocks.mod("fence");
                fenceMeta = 9;
                stairBlock = LOTRLegacyBlocks.mod("stairsBeech");
                trapdoorBlock = LOTRLegacyBlocks.mod("trapdoorBeech");
                break;
            case 3:
                woodBlock = LOTRLegacyBlocks.mod("wood3");
                woodMeta = 0;
                plankBlock = LOTRLegacyBlocks.mod("planks");
                plankMeta = 12;
                fenceBlock = LOTRLegacyBlocks.mod("fence");
                fenceMeta = 12;
                stairBlock = LOTRLegacyBlocks.mod("stairsMaple");
                trapdoorBlock = LOTRLegacyBlocks.mod("trapdoorMaple");
                break;
            default:
                break;
        }
        generateSupportPillar(world, -3, 4, -3);
        generateSupportPillar(world, -3, 4, 3);
        generateSupportPillar(world, 3, 4, -3);
        generateSupportPillar(world, 3, 4, 3);
        for (i12 = -2; i12 <= 2; ++i12) {
            for (k1 = -2; k1 <= 2; ++k1) {
                for (int j13 = 5; j13 <= 19; ++j13) {
                    setAir(world, i12, j13, k1);
                }
            }
        }
        for (j1 = 6; j1 <= 19; ++j1) {
            setBlockAndMetadata(world, -2, j1, -2, woodBlock, woodMeta);
            setBlockAndMetadata(world, -2, j1, 2, woodBlock, woodMeta);
            setBlockAndMetadata(world, 2, j1, -2, woodBlock, woodMeta);
            setBlockAndMetadata(world, 2, j1, 2, woodBlock, woodMeta);
        }
        for (j1 = 5; j1 <= 10; j1 += 5) {
            for (i1 = -3; i1 <= 3; ++i1) {
                for (int k13 = -3; k13 <= 3; ++k13) {
                    setBlockAndMetadata(world, i1, j1, k13, plankBlock, plankMeta);
                }
            }
            for (i1 = -4; i1 <= 4; ++i1) {
                setBlockAndMetadata(world, i1, j1, -4, stairBlock, 2);
                setBlockAndMetadata(world, i1, j1, 4, stairBlock, 3);
            }
            for (k1 = -3; k1 <= 3; ++k1) {
                setBlockAndMetadata(world, -4, j1, k1, stairBlock, 1);
                setBlockAndMetadata(world, 4, j1, k1, stairBlock, 0);
            }
            for (i1 = -2; i1 <= 2; ++i1) {
                setBlockAndMetadata(world, i1, j1 + 1, -3, fenceBlock, fenceMeta);
                setBlockAndMetadata(world, i1, j1 + 1, 3, fenceBlock, fenceMeta);
            }
            for (k1 = -2; k1 <= 2; ++k1) {
                setBlockAndMetadata(world, -3, j1 + 1, k1, fenceBlock, fenceMeta);
                setBlockAndMetadata(world, 3, j1 + 1, k1, fenceBlock, fenceMeta);
            }
            setBlockAndMetadata(world, 0, j1 + 2, -3, LOTRLegacyBlocks.vanilla("torch"), 5);
            setBlockAndMetadata(world, 0, j1 + 2, 3, LOTRLegacyBlocks.vanilla("torch"), 5);
            setBlockAndMetadata(world, -3, j1 + 2, 0, LOTRLegacyBlocks.vanilla("torch"), 5);
            setBlockAndMetadata(world, 3, j1 + 2, 0, LOTRLegacyBlocks.vanilla("torch"), 5);
            spawnNPCAndSetHome(create(LOTREntities.RANGER_NORTH, world), world, -1, j1 + 1, 0, 8);
        }
        for (i12 = -2; i12 <= 2; ++i12) {
            for (k1 = -2; k1 <= 2; ++k1) {
                int i2 = Math.abs(i12);
                int k2 = Math.abs(k1);
                if (i2 >= 2 && k2 >= 2) {
                    continue;
                }
                setBlockAndMetadata(world, i12, 15, k1, plankBlock, plankMeta);
                if ((i2 >= 2 || k2 != 2) && i2 != 2) {
                    continue;
                }
                setBlockAndMetadata(world, i12, 16, k1, fenceBlock, fenceMeta);
            }
        }
        setGrassToDirt(world, 0, 0, 0);
        for (j1 = 1; j1 <= 25; ++j1) {
            setBlockAndMetadata(world, 0, j1, 0, woodBlock, woodMeta);
            if (j1 > 15) {
                continue;
            }
            setBlockAndMetadata(world, 0, j1, -1, LOTRLegacyBlocks.vanilla("ladder"), 2);
        }
        setBlockAndMetadata(world, 0, 6, -1, trapdoorBlock, 0);
        setBlockAndMetadata(world, 0, 11, -1, trapdoorBlock, 0);
        setBlockAndMetadata(world, 0, 17, -2, LOTRLegacyBlocks.vanilla("torch"), 5);
        setBlockAndMetadata(world, 0, 17, 2, LOTRLegacyBlocks.vanilla("torch"), 5);
        setBlockAndMetadata(world, -2, 17, 0, LOTRLegacyBlocks.vanilla("torch"), 5);
        setBlockAndMetadata(world, 2, 17, 0, LOTRLegacyBlocks.vanilla("torch"), 5);
        placeChest(world, random, 0, 16, 1, 0, LOTRChestContents.RANGER_TENT);
        setBlockAndMetadata(world, 0, 11, 1, LOTRLegacyBlocks.mod("rangerTable"), 0);
        for (j1 = 17; j1 <= 18; ++j1) {
            setBlockAndMetadata(world, -2, j1, -2, fenceBlock, fenceMeta);
            setBlockAndMetadata(world, -2, j1, 2, fenceBlock, fenceMeta);
            setBlockAndMetadata(world, 2, j1, -2, fenceBlock, fenceMeta);
            setBlockAndMetadata(world, 2, j1, 2, fenceBlock, fenceMeta);
        }
        for (int step = 0; step <= 1; ++step) {
            for (i1 = -2 + step; i1 <= 2 - step; ++i1) {
                setBlockAndMetadata(world, i1, 20 + step, -2 + step, stairBlock, 2);
                setBlockAndMetadata(world, i1, 20 + step, 2 - step, stairBlock, 3);
            }
            for (k1 = -1 + step; k1 <= 1 - step; ++k1) {
                setBlockAndMetadata(world, -2 + step, 20 + step, k1, stairBlock, 1);
                setBlockAndMetadata(world, 2 - step, 20 + step, k1, stairBlock, 0);
            }
        }
        placeWallBanner(world, -2, 15, 0, "RANGER_NORTH", 3);
        placeWallBanner(world, 2, 15, 0, "RANGER_NORTH", 1);
        placeWallBanner(world, 0, 15, -2, "RANGER_NORTH", 2);
        placeWallBanner(world, 0, 15, 2, "RANGER_NORTH", 0);
        for (j1 = 24; j1 <= 25; ++j1) {
            setBlockAndMetadata(world, 1, j1, 0, LOTRLegacyBlocks.vanilla("wool"), 13);
            setBlockAndMetadata(world, 2, j1, 1, LOTRLegacyBlocks.vanilla("wool"), 13);
            setBlockAndMetadata(world, 2, j1, 2, LOTRLegacyBlocks.vanilla("wool"), 13);
            setBlockAndMetadata(world, 3, j1, 3, LOTRLegacyBlocks.vanilla("wool"), 13);
        }
        LOTRNPCRespawnerEntity respawner = create(LOTREntities.NPC_RESPAWNER, world);
        respawner.setSpawnClass(LOTREntities.RANGER_NORTH);
        respawner.setCheckRanges(24, -12, 20, 8);
        respawner.setSpawnRanges(4, -4, 4, 16);
        placeNPCRespawner(respawner, world, 0, 0, 0);
        return true;
    }
}
