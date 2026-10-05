package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRNPCRespawnerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.gondor.LOTRGondorTowerGuardEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRBeaconTowerStructure extends LOTRGondorStructure {
    public boolean generateRoom = true;

    public LOTRBeaconTowerStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        return generateWithSetRotationAndHeight(world, random, i, j, k, rotation, random.nextInt(4));
    }

    public boolean generateWithSetRotationAndHeight(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation, int height) {
        int k1;
        int i1;
        int k2;
        int j1;
        int i12;
        int i2;
        int j12;
        int doorBase = j - 1;
        setOriginAndRotation(world, i, j + height, k, rotation, 3);
        doorBase -= getY(0);
        setupRandomBlocks(random);
        if (restrictions) {
            for (i12 = -3; i12 <= 3; ++i12) {
                for (k1 = -3; k1 <= 3; ++k1) {
                    j1 = getTopBlock(world, i12, k1) - 1;
                    if (isSurface(world, i12, j1, k1)) {
                        continue;
                    }
                    return false;
                }
            }
        }
        for (i12 = -2; i12 <= 2; ++i12) {
            for (k1 = -2; k1 <= 2; ++k1) {
                for (j1 = 9; j1 <= 13; ++j1) {
                    setAir(world, i12, j1, k1);
                }
            }
        }
        for (i12 = -2; i12 <= 2; ++i12) {
            for (k1 = -2; k1 <= 2; ++k1) {
                i2 = Math.abs(i12);
                k2 = Math.abs(k1);
                for (j12 = 8; j12 >= doorBase || !isOpaque(world, i12, j12, k1) && getY(j12) >= world.getMinY(); --j12) {
                    if (i2 == 2 && k2 == 2) {
                        setBlockAndMetadata(world, i12, j12, k1, pillarBlock, pillarMeta);
                    } else {
                        setBlockAndMetadata(world, i12, j12, k1, brickBlock, brickMeta);
                    }
                    setGrassToDirt(world, i12, j12 - 1, k1);
                }
                if (i2 == 2 && k2 == 2) {
                    for (j12 = 9; j12 <= 12; ++j12) {
                        setBlockAndMetadata(world, i12, j12, k1, pillarBlock, pillarMeta);
                    }
                    continue;
                }
                if (i2 != 2 && k2 != 2) {
                    continue;
                }
                setBlockAndMetadata(world, i12, 9, k1, fenceBlock, fenceMeta);
            }
        }
        for (i12 = -3; i12 <= 3; ++i12) {
            for (k1 = -3; k1 <= 3; ++k1) {
                i2 = Math.abs(i12);
                k2 = Math.abs(k1);
                if ((i2 != 3 || k2 != 1) && (k2 != 3 || i2 != 1)) {
                    continue;
                }
                for (j12 = 4; j12 >= 1 || !isOpaque(world, i12, j12, k1) && getY(j12) >= world.getMinY(); --j12) {
                    setBlockAndMetadata(world, i12, j12, k1, brickBlock, brickMeta);
                    setGrassToDirt(world, i12, j12 - 1, k1);
                }
            }
        }
        for (int i13 : new int[]{-1, 1}) {
            setBlockAndMetadata(world, i13, 5, -3, brickStairBlock, 2);
            setBlockAndMetadata(world, i13, 5, 3, brickStairBlock, 3);
        }
        int[] i14 = {-1, 1};
        k1 = i14.length;
        for (i2 = 0; i2 < k1; ++i2) {
            int k12 = i14[i2];
            setBlockAndMetadata(world, -3, 5, k12, brickStairBlock, 1);
            setBlockAndMetadata(world, 3, 5, k12, brickStairBlock, 0);
        }
        for (i1 = -1; i1 <= 1; ++i1) {
            for (k1 = -1; k1 <= 1; ++k1) {
                setBlockAndMetadata(world, i1, 8, k1, rockSlabDoubleBlock, rockSlabDoubleMeta);
            }
        }
        setBlockAndMetadata(world, 0, 9, 0, rockBlock, rockMeta);
        setBlockAndMetadata(world, 0, 10, 0, LOTRLegacyBlocks.mod("beacon"), 0);
        setBlockAndMetadata(world, -2, 9, 0, fenceGateBlock, 3);
        int j13 = 8;
        while (!isOpaque(world, -3, j13, 0) && getY(j13) >= world.getMinY()) {
            setBlockAndMetadata(world, -3, j13, 0, LOTRLegacyBlocks.vanilla("ladder"), 5);
            --j13;
        }
        setBlockAndMetadata(world, -2, 12, -1, LOTRLegacyBlocks.vanilla("torch"), 3);
        setBlockAndMetadata(world, 2, 12, -1, LOTRLegacyBlocks.vanilla("torch"), 3);
        setBlockAndMetadata(world, -2, 12, 1, LOTRLegacyBlocks.vanilla("torch"), 4);
        setBlockAndMetadata(world, 2, 12, 1, LOTRLegacyBlocks.vanilla("torch"), 4);
        for (i1 = -3; i1 <= 3; ++i1) {
            for (k1 = -3; k1 <= 3; ++k1) {
                i2 = Math.abs(i1);
                k2 = Math.abs(k1);
                if (i2 == 3 || k2 == 3) {
                    setBlockAndMetadata(world, i1, 13, k1, brickSlabBlock, brickSlabMeta | 8);
                    continue;
                }
                if (i2 == 2 || k2 == 2) {
                    if (i2 == 2 && k2 == 2) {
                        setBlockAndMetadata(world, i1, 13, k1, brickBlock, brickMeta);
                    } else {
                        setBlockAndMetadata(world, i1, 13, k1, brickSlabBlock, brickSlabMeta | 8);
                    }
                    setBlockAndMetadata(world, i1, 14, k1, brickSlabBlock, brickSlabMeta);
                    continue;
                }
                if (i2 != 1 && k2 != 1) {
                    continue;
                }
                setBlockAndMetadata(world, i1, 14, k1, brickBlock, brickMeta);
            }
        }
        int[] i15 = {-2, 2};
        k1 = i15.length;
        for (i2 = 0; i2 < k1; ++i2) {
            int i13;
            i13 = i15[i2];
            for (int k13 : new int[]{-2, 2}) {
                setBlockAndMetadata(world, i13, 13, k13 - 1, brickStairBlock, 6);
                setBlockAndMetadata(world, i13, 13, k13 + 1, brickStairBlock, 7);
                setBlockAndMetadata(world, i13 - 1, 13, k13, brickStairBlock, 5);
                setBlockAndMetadata(world, i13 + 1, 13, k13, brickStairBlock, 4);
            }
        }
        if (generateRoom) {
            setBlockAndMetadata(world, 0, doorBase, -2, brickBlock, brickMeta);
            setBlockAndMetadata(world, 0, doorBase + 1, -2, doorBlock, 1);
            setBlockAndMetadata(world, 0, doorBase + 2, -2, doorBlock, 9);
            for (int i16 = -1; i16 <= 1; ++i16) {
                for (k1 = -1; k1 <= 1; ++k1) {
                    setBlockAndMetadata(world, i16, doorBase, k1, brickBlock, brickMeta);
                    for (j1 = doorBase + 1; j1 <= doorBase + 4; ++j1) {
                        setAir(world, i16, j1, k1);
                    }
                }
            }
            setBlockAndMetadata(world, 0, doorBase + 3, -1, LOTRLegacyBlocks.vanilla("torch"), 3);
            setBlockAndMetadata(world, 1, doorBase + 1, -1, tableBlock, 0);
            placeWallBanner(world, 2, doorBase + 4, -1, bannerType, 3);
            placeChest(world, random, -1, doorBase + 1, -1, LOTRLegacyBlocks.mod("chestLebethron"), 3, LOTRChestContents.GONDOR_FORTRESS_SUPPLIES);
            for (j1 = doorBase + 1; j1 <= doorBase + 4; ++j1) {
                setBlockAndMetadata(world, 1, j1, 1, brickBlock, brickMeta);
                setBlockAndMetadata(world, 1, j1, 0, LOTRLegacyBlocks.vanilla("ladder"), 2);
            }
            setBlockAndMetadata(world, -1, doorBase + 2, 1, brickSlabBlock, brickSlabMeta | 8);
            setBlockAndMetadata(world, 0, doorBase + 2, 1, brickSlabBlock, brickSlabMeta | 8);
            for (int j14 : new int[]{doorBase + 1, doorBase + 3}) {
                setBlockAndMetadata(world, -1, j14, 1, bedBlock, 1);
                setBlockAndMetadata(world, 0, j14, 1, bedBlock, 9);
            }
        }
        int soldiers = 1 + random.nextInt(2);
        for (int l = 0; l < soldiers; ++l) {
            LOTRGondorTowerGuardEntity soldier = create(LOTREntities.GONDOR_TOWER_GUARD, world);
            soldier.spawnRidingHorse = false;
            spawnNPCAndSetHome(soldier, world, -1, 9, 0, 16);
        }
        LOTRNPCRespawnerEntity respawner = create(LOTREntities.NPC_RESPAWNER, world);
        respawner.setSpawnClass(LOTREntities.GONDOR_TOWER_GUARD);
        respawner.setCheckRanges(16, -12, 12, 4);
        respawner.setSpawnRanges(2, -2, 2, 16);
        placeNPCRespawner(respawner, world, 0, 9, 0);
        return true;
    }
}
