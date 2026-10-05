package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.farharad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.WorldGenLevel;

public abstract class LOTRTauredainHouseStructure extends LOTRStructureBase2 {
    public LegacyBlock brickBlock;
    public int brickMeta;
    public LegacyBlock brickSlabBlock;
    public int brickSlabMeta;
    public LegacyBlock brickStairBlock;
    public LegacyBlock brickWallBlock;
    public int brickWallMeta;
    public LegacyBlock floorBlock;
    public int floorMeta;
    public LegacyBlock woodBlock;
    public int woodMeta;
    public LegacyBlock plankBlock;
    public int plankMeta;
    public LegacyBlock plankSlabBlock;
    public int plankSlabMeta;
    public LegacyBlock plankStairBlock;
    public LegacyBlock fenceBlock;
    public int fenceMeta;
    public LegacyBlock fenceGateBlock;
    public LegacyBlock doorBlock;
    public LegacyBlock thatchBlock;
    public int thatchMeta;
    public LegacyBlock thatchSlabBlock;
    public int thatchSlabMeta;
    public LegacyBlock thatchStairBlock;
    public LegacyBlock bedBlock;
    public LegacyBlock plateBlock;

    protected LOTRTauredainHouseStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        setOriginAndRotation(world, i, j, k, rotation, getOffset());
        setupRandomBlocks(random);
        if (restrictions) {
            int minHeight = 0;
            int maxHeight = 0;
            int range = getOffset();
            for (int i1 = -range; i1 <= range; ++i1) {
                for (int k1 = -range; k1 <= range; ++k1) {
                    int j1 = getTopBlock(world, i1, k1) - 1;
                    if (!isSurface(world, i1, j1, k1)) {
                        return false;
                    }
                    if (j1 < minHeight) {
                        minHeight = j1;
                    }
                    if (j1 > maxHeight) {
                        maxHeight = j1;
                    }
                    if (maxHeight - minHeight <= 6) {
                        continue;
                    }
                    return false;
                }
            }
        }
        return true;
    }

    public abstract int getOffset();

    public void layFoundation(WorldGenLevel world, int i, int k) {
        for (int j = 0; (j == 0 || !isOpaque(world, i, j, k)) && getY(j) >= 0; --j) {
            setBlockAndMetadata(world, i, j, k, brickBlock, brickMeta);
            setGrassToDirt(world, i, j - 1, k);
        }
    }

    public void placeTauredainFlowerPot(WorldGenLevel world, int i, int j, int k, RandomSource random) {
        ItemStack plant = null;
        if (random.nextInt(3) == 0) {
            plant = getRandomFlower(world, random);
        } else {
            int l = random.nextInt(6);
            switch (l) {
                case 0:
                    plant = LOTRLegacyBlocks.vanilla("sapling").stack(1, 3);
                    break;
                case 1:
                    plant = LOTRLegacyBlocks.mod("sapling6").stack(1, 0);
                    break;
                case 2:
                    plant = LOTRLegacyBlocks.mod("fruitSapling").stack(1, 3);
                    break;
                case 3:
                    plant = LOTRLegacyBlocks.vanilla("tallgrass").stack(1, 2);
                    break;
                case 4:
                    plant = LOTRLegacyBlocks.vanilla("tallgrass").stack(1, 1);
                    break;
                case 5:
                    plant = LOTRLegacyBlocks.mod("tallGrass").stack(1, 5);
                    break;
                default:
                    break;
            }
        }
        placeFlowerPot(world, i, j, k, plant);
    }

    public void placeTauredainTorch(WorldGenLevel world, int i, int j, int k) {
        setBlockAndMetadata(world, i, j, k, LOTRLegacyBlocks.mod("tauredainDoubleTorch"), 0);
        setBlockAndMetadata(world, i, j + 1, k, LOTRLegacyBlocks.mod("tauredainDoubleTorch"), 1);
    }

    @Override
    public void setupRandomBlocks(RandomSource random) {
        if (useStoneBrick()) {
            brickBlock = LOTRLegacyBlocks.mod("brick4");
            brickMeta = 0;
            brickSlabBlock = LOTRLegacyBlocks.mod("slabSingle8");
            brickSlabMeta = 0;
            brickStairBlock = LOTRLegacyBlocks.mod("stairsTauredainBrick");
            brickWallBlock = LOTRLegacyBlocks.mod("wall4");
            brickWallMeta = 0;
        } else {
            brickBlock = LOTRLegacyBlocks.mod("brick5");
            brickMeta = 0;
            brickSlabBlock = LOTRLegacyBlocks.mod("slabSingle9");
            brickSlabMeta = 5;
            brickStairBlock = LOTRLegacyBlocks.mod("stairsMudBrick");
            brickWallBlock = LOTRLegacyBlocks.mod("wall3");
            brickWallMeta = 8;
        }
        if (random.nextBoolean()) {
            floorBlock = LOTRLegacyBlocks.vanilla("stained_hardened_clay");
            floorMeta = 7;
        } else {
            floorBlock = LOTRLegacyBlocks.vanilla("stained_hardened_clay");
            floorMeta = 12;
        }
        if (random.nextInt(3) == 0) {
            woodBlock = LOTRLegacyBlocks.mod("wood6");
            woodMeta = 0;
            plankBlock = LOTRLegacyBlocks.mod("planks2");
            plankMeta = 8;
            plankSlabBlock = LOTRLegacyBlocks.mod("woodSlabSingle4");
            plankSlabMeta = 0;
            plankStairBlock = LOTRLegacyBlocks.mod("stairsMahogany");
            doorBlock = LOTRLegacyBlocks.mod("doorMahogany");
            fenceBlock = LOTRLegacyBlocks.mod("fence2");
            fenceMeta = 8;
            fenceGateBlock = LOTRLegacyBlocks.mod("fenceGateMahogany");
        } else {
            woodBlock = LOTRLegacyBlocks.vanilla("log");
            woodMeta = 3;
            plankBlock = LOTRLegacyBlocks.vanilla("planks");
            plankMeta = 3;
            plankSlabBlock = LOTRLegacyBlocks.vanilla("wooden_slab");
            plankSlabMeta = 3;
            plankStairBlock = LOTRLegacyBlocks.vanilla("jungle_stairs");
            doorBlock = LOTRLegacyBlocks.mod("doorJungle");
            fenceBlock = LOTRLegacyBlocks.vanilla("fence");
            fenceMeta = 3;
            fenceGateBlock = LOTRLegacyBlocks.mod("fenceGateJungle");
        }
        thatchBlock = LOTRLegacyBlocks.mod("thatch");
        thatchMeta = 1;
        thatchSlabBlock = LOTRLegacyBlocks.mod("slabSingleThatch");
        thatchSlabMeta = 1;
        thatchStairBlock = LOTRLegacyBlocks.mod("stairsReed");
        bedBlock = LOTRLegacyBlocks.mod("strawBed");
        plateBlock = LOTRLegacyBlocks.mod("woodPlateBlock");
    }

    public boolean useStoneBrick() {
        return false;
    }
}
