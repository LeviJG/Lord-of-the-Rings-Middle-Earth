package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.rohan;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRFoods;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.rohan.LOTRRohirrimWarriorEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRRohanWatchtowerStructure extends LOTRRohanStructure {
    public LOTRRohanWatchtowerStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        int j1;
        int j12;
        int k1;
        int k12;
        int i1;
        setOriginAndRotation(world, i, j, k, rotation, 0);
        setupRandomBlocks(random);
        int height = 7 + random.nextInt(3);
        originY += height;
        if (restrictions) {
            for (int i12 = -4; i12 <= 4; ++i12) {
                for (k12 = -4; k12 <= 4; ++k12) {
                    j12 = getTopBlock(world, i12, k12) - 1;
                    if (isSurface(world, i12, j12, k12)) {
                        continue;
                    }
                    return false;
                }
            }
        }
        int[] i12 = {-3, 3};
        k12 = i12.length;
        for (j12 = 0; j12 < k12; ++j12) {
            int i13 = i12[j12];
            for (int k13 : new int[]{-3, 3}) {
                int j13 = 3;
                while (!isOpaque(world, i13, j13, k13) && getY(j13) >= 0) {
                    setBlockAndMetadata(world, i13, j13, k13, plank2Block, plank2Meta);
                    setGrassToDirt(world, i13, j13 - 1, k13);
                    --j13;
                }
            }
        }
        for (i1 = -2; i1 <= 2; ++i1) {
            for (k12 = -2; k12 <= 2; ++k12) {
                setBlockAndMetadata(world, i1, 0, k12, plankBlock, plankMeta);
            }
        }
        for (i1 = -3; i1 <= 3; ++i1) {
            for (k12 = -3; k12 <= 3; ++k12) {
                setBlockAndMetadata(world, i1, 4, k12, plankBlock, plankMeta);
            }
        }
        for (i1 = -2; i1 <= 2; ++i1) {
            setBlockAndMetadata(world, i1, 0, -3, logBlock, logMeta | 4);
            setBlockAndMetadata(world, i1, 0, 3, logBlock, logMeta | 4);
            setBlockAndMetadata(world, i1, 4, -3, logBlock, logMeta | 4);
            setBlockAndMetadata(world, i1, 4, 3, logBlock, logMeta | 4);
            setBlockAndMetadata(world, i1, 0, -4, plankStairBlock, 6);
            setBlockAndMetadata(world, i1, 0, 4, plankStairBlock, 7);
            setBlockAndMetadata(world, i1, 1, -4, fenceBlock, fenceMeta);
            setBlockAndMetadata(world, i1, 1, 4, fenceBlock, fenceMeta);
            setBlockAndMetadata(world, i1, 3, -3, fenceBlock, fenceMeta);
            setBlockAndMetadata(world, i1, 3, 3, fenceBlock, fenceMeta);
        }
        for (k1 = -2; k1 <= 2; ++k1) {
            setBlockAndMetadata(world, -3, 0, k1, logBlock, logMeta | 8);
            setBlockAndMetadata(world, 3, 0, k1, logBlock, logMeta | 8);
            setBlockAndMetadata(world, -3, 4, k1, logBlock, logMeta | 8);
            setBlockAndMetadata(world, 3, 4, k1, logBlock, logMeta | 8);
            setBlockAndMetadata(world, -4, 0, k1, plankStairBlock, 5);
            setBlockAndMetadata(world, 4, 0, k1, plankStairBlock, 4);
            setBlockAndMetadata(world, -4, 1, k1, fenceBlock, fenceMeta);
            setBlockAndMetadata(world, 4, 1, k1, fenceBlock, fenceMeta);
            setBlockAndMetadata(world, -3, 3, k1, fenceBlock, fenceMeta);
            setBlockAndMetadata(world, 3, 3, k1, fenceBlock, fenceMeta);
        }
        setBlockAndMetadata(world, -3, 1, -2, fenceBlock, fenceMeta);
        setBlockAndMetadata(world, -3, 1, 2, fenceBlock, fenceMeta);
        setBlockAndMetadata(world, 3, 1, -2, fenceBlock, fenceMeta);
        setBlockAndMetadata(world, 3, 1, 2, fenceBlock, fenceMeta);
        setBlockAndMetadata(world, -2, 1, -3, fenceBlock, fenceMeta);
        setBlockAndMetadata(world, 2, 1, -3, fenceBlock, fenceMeta);
        setBlockAndMetadata(world, -2, 1, 3, fenceBlock, fenceMeta);
        setBlockAndMetadata(world, 2, 1, 3, fenceBlock, fenceMeta);
        setBlockAndMetadata(world, -3, 2, -2, LOTRLegacyBlocks.vanilla("torch"), 3);
        setBlockAndMetadata(world, -3, 2, 2, LOTRLegacyBlocks.vanilla("torch"), 4);
        setBlockAndMetadata(world, 3, 2, -2, LOTRLegacyBlocks.vanilla("torch"), 3);
        setBlockAndMetadata(world, 3, 2, 2, LOTRLegacyBlocks.vanilla("torch"), 4);
        setBlockAndMetadata(world, -2, 2, -3, LOTRLegacyBlocks.vanilla("torch"), 2);
        setBlockAndMetadata(world, 2, 2, -3, LOTRLegacyBlocks.vanilla("torch"), 1);
        setBlockAndMetadata(world, -2, 2, 3, LOTRLegacyBlocks.vanilla("torch"), 2);
        setBlockAndMetadata(world, 2, 2, 3, LOTRLegacyBlocks.vanilla("torch"), 1);
        setBlockAndMetadata(world, -1, 4, 0, logBlock, logMeta | 8);
        setBlockAndMetadata(world, 1, 4, 0, logBlock, logMeta | 8);
        setBlockAndMetadata(world, 0, 4, -1, logBlock, logMeta | 4);
        setBlockAndMetadata(world, 0, 4, 1, logBlock, logMeta | 4);
        for (i1 = -4; i1 <= 4; ++i1) {
            setBlockAndMetadata(world, i1, 4, -4, plankStairBlock, 2);
            setBlockAndMetadata(world, i1, 4, 4, plankStairBlock, 3);
        }
        for (k1 = -4; k1 <= 4; ++k1) {
            setBlockAndMetadata(world, -4, 4, k1, plankStairBlock, 1);
            setBlockAndMetadata(world, 4, 4, k1, plankStairBlock, 0);
        }
        for (j1 = 0; j1 <= 3; ++j1) {
            setBlockAndMetadata(world, 0, j1, 3, plank2Block, plank2Meta);
            setBlockAndMetadata(world, 0, j1, 2, LOTRLegacyBlocks.vanilla("ladder"), 2);
        }
        j1 = -1;
        while (!isOpaque(world, 0, j1, 3) && getY(j1) >= 0) {
            setBlockAndMetadata(world, 0, j1, 3, plank2Block, plank2Meta);
            setGrassToDirt(world, 0, j1 - 1, 3);
            if (!isOpaque(world, 0, j1, 2)) {
                setBlockAndMetadata(world, 0, j1, 2, LOTRLegacyBlocks.vanilla("ladder"), 2);
            }
            --j1;
        }
        placeChest(world, random, -2, 1, 2, 2, LOTRChestContents.ROHAN_WATCHTOWER);
        setBlockAndMetadata(world, 2, 1, 2, tableBlock, 0);
        for (k1 = -2; k1 <= 2; ++k1) {
            int k2 = Math.abs(k1);
            for (int i14 : new int[]{-3, 3}) {
                int j14 = -1;
                while (!isOpaque(world, i14, j14, k1) && getY(j14) >= 0) {
                    if (k2 == 2 && Math.floorMod(j14, 4) == 1 || k2 == 1 && Math.floorMod(j14, 2) == 0 || k2 == 0 && Math.floorMod(j14, 4) == 3) {
                        setBlockAndMetadata(world, i14, j14, k1, logBlock, logMeta);
                        if (k2 == 0) {
                            setBlockAndMetadata(world, i14 - Integer.signum(i14), j14, k1, LOTRLegacyBlocks.vanilla("torch"), i14 > 0 ? 1 : 2);
                        }
                    }
                    --j14;
                }
            }
        }
        int belowTop = getBelowTop(world, 2, -1, 2);
        placeChest(world, random, 2, belowTop, 2, 5, LOTRChestContents.ROHAN_WATCHTOWER);
        belowTop = getBelowTop(world, 2, -1, 0);
        setBlockAndMetadata(world, 2, belowTop, 0, plankBlock, plankMeta);
        setGrassToDirt(world, 2, belowTop - 1, 0);
        placeBarrel(world, random, 2, belowTop + 1, 0, 5, LOTRFoods.ROHAN_DRINK);
        belowTop = getBelowTop(world, -2, -1, 1);
        setBlockAndMetadata(world, -2, belowTop, 1, LOTRLegacyBlocks.vanilla("hay_block"), 0);
        setGrassToDirt(world, -2, belowTop - 1, 1);
        belowTop = getBelowTop(world, -2, -1, 2);
        setBlockAndMetadata(world, -2, belowTop, 2, LOTRLegacyBlocks.vanilla("hay_block"), 0);
        setBlockAndMetadata(world, -2, belowTop + 1, 2, LOTRLegacyBlocks.vanilla("hay_block"), 0);
        setGrassToDirt(world, -2, belowTop - 1, 2);
        belowTop = getBelowTop(world, -1, -1, 2);
        setBlockAndMetadata(world, -1, belowTop, 2, LOTRLegacyBlocks.vanilla("hay_block"), 0);
        setGrassToDirt(world, -1, belowTop - 1, 2);
        int soldiers = 1 + random.nextInt(3);
        for (int l = 0; l < soldiers; ++l) {
            LOTRRohirrimWarriorEntity rohirrim = random.nextInt(3) == 0 ? create(LOTREntities.ROHIRRIM_ARCHER, world) : create(LOTREntities.ROHIRRIM_WARRIOR, world);
            rohirrim.spawnRidingHorse = false;
            spawnNPCAndSetHome(rohirrim, world, 0, 1, 0, 4);
        }
        return true;
    }

    public int getBelowTop(WorldGenLevel world, int i, int j, int k) {
        while (!isOpaque(world, i, j, k) && getY(j) >= 0) {
            --j;
        }
        return j + 1;
    }
}
