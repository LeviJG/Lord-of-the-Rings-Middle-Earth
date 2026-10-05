package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.elf;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRNPCRespawnerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRFoods;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.dorwinion.LOTRDorwinionCaptainEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.elf.LOTRDorwinionElfCaptainEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LOTRDorwinionCaptainTentStructure extends LOTRDorwinionTentStructure {
    public LOTRDorwinionCaptainTentStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        int i2;
        int k2;
        int k1;
        int j1;
        int i1;
        setOriginAndRotation(world, i, j, k, rotation, 7);
        setupRandomBlocks(random);
        if (restrictions) {
            int minHeight = 0;
            int maxHeight = 0;
            int maxEdgeHeight = 0;
            for (int i12 = -15; i12 <= 15; ++i12) {
                for (int k12 = -15; k12 <= 15; ++k12) {
                    int j12 = getTopBlock(world, i12, k12);
                    BlockState block = getBlockState(world, i12, j12 - 1, k12);
                    if (!LOTRLegacyBlocks.vanilla("grass").matches(block)) {
                        return false;
                    }
                    if (j12 < minHeight) {
                        minHeight = j12;
                    }
                    if (j12 > maxHeight) {
                        maxHeight = j12;
                    }
                    if (maxHeight - minHeight > 6) {
                        return false;
                    }
                    if (Math.abs(i12) != 8 && Math.abs(k12) != 8 || j12 <= maxEdgeHeight) {
                        continue;
                    }
                    maxEdgeHeight = j12;
                }
            }
            originY = getY(maxEdgeHeight);
        }
        int r = 35;
        int h = 7;
        for (i1 = -r; i1 <= r; ++i1) {
            for (k1 = -r; k1 <= r; ++k1) {
                i2 = Math.abs(i1);
                k2 = Math.abs(k1);
                boolean within = false;
                for (j1 = 0; j1 >= -h; --j1) {
                    int j2 = j1 + r - 3;
                    int d = i2 * i2 + j2 * j2 + k2 * k2;
                    if (d >= r * r) {
                        continue;
                    }
                    boolean grass = !isOpaque(world, i1, j1 + 1, k1);
                    setBlockAndMetadata(world, i1, j1, k1, grass ? LOTRLegacyBlocks.vanilla("grass") : LOTRLegacyBlocks.vanilla("dirt"), 0);
                    setGrassToDirt(world, i1, j1 - 1, k1);
                    within = true;
                }
                if (!within) {
                    continue;
                }
                j1 = -h - 1;
                while (!isOpaque(world, i1, j1, k1) && getY(j1) >= world.getMinY()) {
                    setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("dirt"), 0);
                    setGrassToDirt(world, i1, j1 - 1, k1);
                    --j1;
                }
            }
        }
        for (i1 = -6; i1 <= 6; ++i1) {
            for (k1 = -6; k1 <= 6; ++k1) {
                i2 = Math.abs(i1);
                k2 = Math.abs(k1);
                boolean inside = i2 <= 3 && k2 <= 3;
                if (i2 == 4 && k2 <= 3 || k2 == 4 && i2 <= 3) {
                    inside = true;
                }
                if (i2 == 5 && k2 <= 2 || k2 == 5 && i2 <= 2) {
                    inside = true;
                }
                if (inside) {
                    setBlockAndMetadata(world, i1, 0, k1, floorBlock, floorMeta);
                    for (j1 = 1; j1 <= 4; ++j1) {
                        setAir(world, i1, j1, k1);
                    }
                }
                if (i2 == 6 && k2 == 2 || k2 == 6 && i2 == 2 || i2 == 4 && k2 == 4) {
                    setGrassToDirt(world, i1, 0, k1);
                    for (j1 = 1; j1 <= 3; ++j1) {
                        setBlockAndMetadata(world, i1, j1, k1, woodBeamBlock, woodBeamMeta);
                    }
                    setBlockAndMetadata(world, i1, 4, k1, clay2SlabBlock, clay2SlabMeta);
                }
                if (i2 == 5 && k2 == 3 || k2 == 5 && i2 == 3) {
                    setGrassToDirt(world, i1, 0, k1);
                    setBlockAndMetadata(world, i1, 1, k1, wool1Block, wool1Meta);
                    setBlockAndMetadata(world, i1, 2, k1, wool2Block, wool2Meta);
                    setBlockAndMetadata(world, i1, 3, k1, wool1Block, wool1Meta);
                    setBlockAndMetadata(world, i1, 4, k1, clay2SlabBlock, clay2SlabMeta);
                }
                if (i2 == 5 && k2 == 4 || k2 == 5 && i2 == 4) {
                    for (j1 = 1; j1 <= 2; ++j1) {
                        setBlockAndMetadata(world, i1, j1, k1, fenceBlock, fenceMeta);
                    }
                    setBlockAndMetadata(world, i1, 3, k1, clay1Block, clay1Meta);
                }
                if (i2 == 6 && k2 == 3 || k2 == 6 && i2 == 3) {
                    setBlockAndMetadata(world, i1, 3, k1, clay1SlabBlock, clay1SlabMeta | 8);
                }
                if (i2 == 6 && k2 <= 1 || k2 == 6 && i2 <= 1) {
                    setBlockAndMetadata(world, i1, 0, k1, floorBlock, floorMeta);
                    int gateMeta = LOTRStructureBase2.DIRECTION_TO_FACING[LOTRStructureBase2.getMovementDirection(i1, k1)];
                    for (int j13 = 1; j13 <= 3; ++j13) {
                        setBlockAndMetadata(world, i1, j13, k1, LOTRLegacyBlocks.mod("gateWooden"), gateMeta);
                    }
                    setBlockAndMetadata(world, i1, 4, k1, clay2SlabBlock, clay2SlabMeta);
                }
                if (i2 == 5 && k2 == 2 || k2 == 5 && i2 == 2) {
                    setBlockAndMetadata(world, i1, 1, k1, plankBlock, plankMeta);
                    setBlockAndMetadata(world, i1, 2, k1, fenceBlock, fenceMeta);
                    setBlockAndMetadata(world, i1, 3, k1, fenceBlock, fenceMeta);
                    setBlockAndMetadata(world, i1, 4, k1, clay1Block, clay1Meta);
                }
                if (i2 == 5 && k2 <= 1 || k2 == 5 && i2 <= 1) {
                    setBlockAndMetadata(world, i1, 4, k1, clay1SlabBlock, clay1SlabMeta | 8);
                }
                if (i2 == 4 && k2 == 3 || k2 == 4 && i2 == 3) {
                    setBlockAndMetadata(world, i1, 4, k1, clay1SlabBlock, clay1SlabMeta | 8);
                }
                if (i2 == 4 && k2 <= 2 || k2 == 4 && i2 <= 2 || i2 == 3 && k2 == 3) {
                    setBlockAndMetadata(world, i1, 5, k1, clay2SlabBlock, clay2SlabMeta);
                }
                if (i2 == 3 && k2 <= 2 || k2 == 3 && i2 <= 2 || i2 == 2 && k2 == 2) {
                    setBlockAndMetadata(world, i1, 5, k1, clay1SlabBlock, clay1SlabMeta);
                }
                if (i2 == 2 && k2 == 1 || k2 == 2 && i2 == 1) {
                    setBlockAndMetadata(world, i1, 5, k1, clay2SlabBlock, clay2SlabMeta);
                }
                if (i2 == 0 && k2 == 2 || k2 == 0 && i2 == 2 || i2 == 1 && k2 == 1) {
                    setBlockAndMetadata(world, i1, 5, k1, clay2Block, clay2Meta);
                }
                if (i2 == 0 && k2 == 1 || k2 == 0 && i2 == 1 || i2 == 0 && k2 == 0) {
                    setBlockAndMetadata(world, i1, 5, k1, LOTRLegacyBlocks.mod("silverBars"), 0);
                }
                if (i2 == 2 && k2 <= 1 || k2 == 2 && i2 <= 1 || i2 <= 1 && k2 <= 1) {
                    setBlockAndMetadata(world, i1, 0, k1, plankBlock, plankMeta);
                }
                if ((i2 != 2 || k2 != 0) && (k2 != 2 || i2 != 0)) {
                    continue;
                }
                for (j1 = 1; j1 <= 4; ++j1) {
                    setBlockAndMetadata(world, i1, j1, k1, woodBeamBlock, woodBeamMeta);
                }
            }
        }
        setBlockAndMetadata(world, 0, 1, 0, LOTRLegacyBlocks.mod("commandTable"), 0);
        setBlockAndMetadata(world, 0, 3, -3, LOTRLegacyBlocks.vanilla("torch"), 4);
        setBlockAndMetadata(world, 0, 3, 3, LOTRLegacyBlocks.vanilla("torch"), 3);
        setBlockAndMetadata(world, -3, 3, 0, LOTRLegacyBlocks.vanilla("torch"), 1);
        setBlockAndMetadata(world, 3, 3, 0, LOTRLegacyBlocks.vanilla("torch"), 2);
        setBlockAndMetadata(world, -3, 1, -4, plankBlock, plankMeta);
        setBlockAndMetadata(world, -3, 1, -3, plankBlock, plankMeta);
        setBlockAndMetadata(world, -4, 1, -3, plankBlock, plankMeta);
        setBlockAndMetadata(world, -3, 1, -2, plankSlabBlock, plankSlabMeta);
        setBlockAndMetadata(world, -4, 1, -2, plankSlabBlock, plankSlabMeta);
        setBlockAndMetadata(world, -3, 2, -3, LOTRLegacyBlocks.vanilla("bed"), 2);
        setBlockAndMetadata(world, -3, 2, -4, LOTRLegacyBlocks.vanilla("bed"), 10);
        setBlockAndMetadata(world, 3, 1, -4, LOTRLegacyBlocks.vanilla("crafting_table"), 0);
        setBlockAndMetadata(world, 4, 1, -3, LOTRLegacyBlocks.mod("dorwinionTable"), 0);
        placeChest(world, random, -4, 1, 3, 2, LOTRChestContents.DORWINION_CAMP);
        placeChest(world, random, -3, 1, 4, 4, LOTRChestContents.DORWINION_CAMP);
        setBlockAndMetadata(world, 2, 1, 4, plankSlabBlock, plankSlabMeta | 8);
        placeMug(world, random, 2, 2, 4, 1, LOTRFoods.DORWINION_DRINK);
        setBlockAndMetadata(world, 3, 1, 3, plankSlabBlock, plankSlabMeta | 8);
        placeMug(world, random, 3, 2, 3, 0, LOTRFoods.DORWINION_DRINK);
        setBlockAndMetadata(world, 4, 1, 2, plankSlabBlock, plankSlabMeta | 8);
        placeMug(world, random, 4, 2, 2, 1, LOTRFoods.DORWINION_DRINK);
        setBlockAndMetadata(world, 3, 1, 4, plankBlock, plankMeta);
        placeBarrel(world, random, 3, 2, 4, 5, LOTRFoods.DORWINION_DRINK);
        setBlockAndMetadata(world, 4, 1, 3, plankBlock, plankMeta);
        placeBarrel(world, random, 4, 2, 3, 2, LOTRFoods.DORWINION_DRINK);
        for (int i13 : new int[]{-2, 2}) {
            placeWallBanner(world, i13, 3, -6, "DORWINION", 2);
            placeWallBanner(world, i13, 3, 6, "DORWINION", 0);
        }
        for (int k13 : new int[]{-2, 2}) {
            placeWallBanner(world, -6, 3, k13, "DORWINION", 3);
            placeWallBanner(world, 6, 3, k13, "DORWINION", 1);
        }
        LOTRDorwinionCaptainEntity captain = create(LOTREntities.DORWINION_CAPTAIN, world);
        spawnNPCAndSetHome(captain, world, 0, 1, -1, 16);
        if (random.nextInt(3) == 0) {
            LOTRDorwinionElfCaptainEntity elfCaptain = create(LOTREntities.DORWINION_ELF_CAPTAIN, world);
            spawnNPCAndSetHome(elfCaptain, world, 0, 1, -1, 16);
        }
        LOTRNPCRespawnerEntity respawner = create(LOTREntities.NPC_RESPAWNER, world);
        respawner.setSpawnClasses(LOTREntities.DORWINION_GUARD, LOTREntities.DORWINION_CROSSBOWER);
        respawner.setCheckRanges(24, -12, 12, 12);
        respawner.setSpawnRanges(12, -2, 2, 16);
        placeNPCRespawner(respawner, world, 0, 0, 0);
        return true;
    }
}
