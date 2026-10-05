package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.elf;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.dorwinion.LOTRDorwinionGuardEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRDorwinionTentStructure extends LOTRStructureBase2 {
    public LegacyBlock woodBeamBlock;
    public int woodBeamMeta;
    public LegacyBlock plankBlock;
    public int plankMeta;
    public LegacyBlock plankSlabBlock;
    public int plankSlabMeta;
    public LegacyBlock plankStairBlock;
    public LegacyBlock fenceBlock;
    public int fenceMeta;
    public LegacyBlock floorBlock;
    public int floorMeta;
    public LegacyBlock wool1Block;
    public int wool1Meta;
    public LegacyBlock clay1Block;
    public int clay1Meta;
    public LegacyBlock clay1SlabBlock;
    public int clay1SlabMeta;
    public LegacyBlock clay1StairBlock;
    public LegacyBlock wool2Block;
    public int wool2Meta;
    public LegacyBlock clay2Block;
    public int clay2Meta;
    public LegacyBlock clay2SlabBlock;
    public int clay2SlabMeta;
    public LegacyBlock clay2StairBlock;

    public LOTRDorwinionTentStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        int k1;
        int i1;
        setOriginAndRotation(world, i, j, k, rotation, 3);
        setupRandomBlocks(random);
        if (restrictions) {
            for (i1 = -2; i1 <= 2; ++i1) {
                for (k1 = -3; k1 <= 3; ++k1) {
                    int j1 = getTopBlock(world, i1, k1) - 1;
                    if (isSurface(world, i1, j1, k1)) {
                        continue;
                    }
                    return false;
                }
            }
        }
        for (i1 = -2; i1 <= 2; ++i1) {
            for (k1 = -2; k1 <= 2; ++k1) {
                int j1;
                int i2 = Math.abs(i1);
                int k2 = Math.abs(k1);
                for (j1 = 0; (j1 == 0 || !isOpaque(world, i1, j1, k1)) && getY(j1) >= world.getMinY(); --j1) {
                    setBlockAndMetadata(world, i1, j1, k1, floorBlock, floorMeta);
                    setGrassToDirt(world, i1, j1 - 1, k1);
                }
                for (j1 = 1; j1 <= 4; ++j1) {
                    setAir(world, i1, j1, k1);
                }
                if (i2 == 2 && k2 == 2) {
                    for (j1 = 1; j1 <= 2; ++j1) {
                        setBlockAndMetadata(world, i1, j1, k1, woodBeamBlock, woodBeamMeta);
                    }
                } else if (i2 == 2) {
                    for (j1 = 1; j1 <= 2; ++j1) {
                        if (k1 % 2 == 0) {
                            setBlockAndMetadata(world, i1, j1, k1, wool1Block, wool1Meta);
                            continue;
                        }
                        setBlockAndMetadata(world, i1, j1, k1, wool2Block, wool2Meta);
                    }
                }
                if (i2 != 0 || k2 != 2) {
                    continue;
                }
                for (j1 = 1; j1 <= 3; ++j1) {
                    setBlockAndMetadata(world, i1, j1, k1, fenceBlock, fenceMeta);
                }
            }
        }
        for (int k12 = -2; k12 <= 2; ++k12) {
            if (k12 % 2 == 0) {
                setBlockAndMetadata(world, -2, 3, k12, clay1StairBlock, 1);
                setBlockAndMetadata(world, 2, 3, k12, clay1StairBlock, 0);
                setBlockAndMetadata(world, -1, 3, k12, clay1StairBlock, 4);
                setBlockAndMetadata(world, 1, 3, k12, clay1StairBlock, 5);
                setBlockAndMetadata(world, -1, 4, k12, clay1StairBlock, 1);
                setBlockAndMetadata(world, 1, 4, k12, clay1StairBlock, 0);
                setBlockAndMetadata(world, 0, 4, k12, clay1Block, clay1Meta);
                setBlockAndMetadata(world, 0, 5, k12, clay1SlabBlock, clay1SlabMeta);
                continue;
            }
            setBlockAndMetadata(world, -2, 3, k12, clay2StairBlock, 1);
            setBlockAndMetadata(world, 2, 3, k12, clay2StairBlock, 0);
            setBlockAndMetadata(world, -1, 3, k12, clay2StairBlock, 4);
            setBlockAndMetadata(world, 1, 3, k12, clay2StairBlock, 5);
            setBlockAndMetadata(world, -1, 4, k12, clay2StairBlock, 1);
            setBlockAndMetadata(world, 1, 4, k12, clay2StairBlock, 0);
            setBlockAndMetadata(world, 0, 4, k12, clay2Block, clay2Meta);
            setBlockAndMetadata(world, 0, 5, k12, clay2SlabBlock, clay2SlabMeta);
        }
        if (random.nextBoolean()) {
            placeChest(world, random, -1, 1, 0, 4, LOTRChestContents.DORWINION_CAMP);
            setBlockAndMetadata(world, 1, 2, 0, LOTRLegacyBlocks.vanilla("torch"), 1);
        } else {
            placeChest(world, random, 1, 1, 0, 4, LOTRChestContents.DORWINION_CAMP);
            setBlockAndMetadata(world, -1, 2, 0, LOTRLegacyBlocks.vanilla("torch"), 2);
        }
        LOTRDorwinionGuardEntity guard = create(LOTREntities.DORWINION_GUARD, world);
        spawnNPCAndSetHome(guard, world, 0, 1, 0, 16);
        return true;
    }

    @Override
    public void setupRandomBlocks(RandomSource random) {
        int randomWood = random.nextInt(3);
        switch (randomWood) {
            case 0:
                woodBeamBlock = LOTRLegacyBlocks.mod("woodBeamV1");
                woodBeamMeta = 0;
                plankBlock = LOTRLegacyBlocks.vanilla("planks");
                plankMeta = 0;
                plankSlabBlock = LOTRLegacyBlocks.vanilla("wooden_slab");
                plankSlabMeta = 0;
                plankStairBlock = LOTRLegacyBlocks.vanilla("oak_stairs");
                fenceBlock = LOTRLegacyBlocks.vanilla("fence");
                fenceMeta = 0;
                break;
            case 1:
                woodBeamBlock = LOTRLegacyBlocks.mod("woodBeam6");
                woodBeamMeta = 2;
                plankBlock = LOTRLegacyBlocks.mod("planks2");
                plankMeta = 10;
                plankSlabBlock = LOTRLegacyBlocks.mod("woodSlabSingle4");
                plankSlabMeta = 2;
                plankStairBlock = LOTRLegacyBlocks.mod("stairsCypress");
                fenceBlock = LOTRLegacyBlocks.mod("fence2");
                fenceMeta = 10;
                break;
            case 2:
                woodBeamBlock = LOTRLegacyBlocks.mod("woodBeam6");
                woodBeamMeta = 3;
                plankBlock = LOTRLegacyBlocks.mod("planks2");
                plankMeta = 11;
                plankSlabBlock = LOTRLegacyBlocks.mod("woodSlabSingle4");
                plankSlabMeta = 3;
                plankStairBlock = LOTRLegacyBlocks.mod("stairsOlive");
                fenceBlock = LOTRLegacyBlocks.mod("fence2");
                fenceMeta = 11;
                break;
            default:
                break;
        }
        int randomFloor = random.nextInt(4);
        switch (randomFloor) {
            case 0:
                floorBlock = LOTRLegacyBlocks.vanilla("stained_hardened_clay");
                floorMeta = 2;
                break;
            case 1:
                floorBlock = LOTRLegacyBlocks.vanilla("stained_hardened_clay");
                floorMeta = 3;
                break;
            case 2:
                floorBlock = LOTRLegacyBlocks.vanilla("stained_hardened_clay");
                floorMeta = 14;
                break;
            case 3:
                floorBlock = LOTRLegacyBlocks.vanilla("stained_hardened_clay");
                floorMeta = 10;
                break;
            default:
                break;
        }
        int randomWool1 = random.nextInt(3);
        switch (randomWool1) {
            case 0:
                wool1Block = LOTRLegacyBlocks.vanilla("wool");
                wool1Meta = 10;
                clay1Block = LOTRLegacyBlocks.mod("clayTileDyed");
                clay1Meta = 10;
                clay1SlabBlock = LOTRLegacyBlocks.mod("slabClayTileDyedSingle2");
                clay1SlabMeta = 2;
                clay1StairBlock = LOTRLegacyBlocks.mod("stairsClayTileDyedPurple");
                break;
            case 1:
                wool1Block = LOTRLegacyBlocks.vanilla("wool");
                wool1Meta = 2;
                clay1Block = LOTRLegacyBlocks.mod("clayTileDyed");
                clay1Meta = 2;
                clay1SlabBlock = LOTRLegacyBlocks.mod("slabClayTileDyedSingle");
                clay1SlabMeta = 2;
                clay1StairBlock = LOTRLegacyBlocks.mod("stairsClayTileDyedMagenta");
                break;
            case 2:
                wool1Block = LOTRLegacyBlocks.vanilla("wool");
                wool1Meta = 14;
                clay1Block = LOTRLegacyBlocks.mod("clayTileDyed");
                clay1Meta = 14;
                clay1SlabBlock = LOTRLegacyBlocks.mod("slabClayTileDyedSingle2");
                clay1SlabMeta = 6;
                clay1StairBlock = LOTRLegacyBlocks.mod("stairsClayTileDyedRed");
                break;
            default:
                break;
        }
        int randomWool2 = random.nextInt(2);
        if (randomWool2 == 0) {
            wool2Block = LOTRLegacyBlocks.vanilla("wool");
            wool2Meta = 4;
            clay2Block = LOTRLegacyBlocks.mod("clayTileDyed");
            clay2Meta = 4;
            clay2SlabBlock = LOTRLegacyBlocks.mod("slabClayTileDyedSingle");
            clay2SlabMeta = 4;
            clay2StairBlock = LOTRLegacyBlocks.mod("stairsClayTileDyedYellow");
        } else {
            wool2Block = LOTRLegacyBlocks.vanilla("wool");
            wool2Meta = 0;
            clay2Block = LOTRLegacyBlocks.mod("clayTileDyed");
            clay2Meta = 0;
            clay2SlabBlock = LOTRLegacyBlocks.mod("slabClayTileDyedSingle");
            clay2SlabMeta = 0;
            clay2StairBlock = LOTRLegacyBlocks.mod("stairsClayTileDyedWhite");
        }
    }
}
