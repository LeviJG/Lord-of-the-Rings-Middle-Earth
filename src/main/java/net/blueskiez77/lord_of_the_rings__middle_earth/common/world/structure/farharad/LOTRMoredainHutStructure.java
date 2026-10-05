package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.farharad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public abstract class LOTRMoredainHutStructure extends LOTRStructureBase2 {
    public LegacyBlock clayBlock = LOTRLegacyBlocks.vanilla("hardened_clay");
    public int clayMeta;
    public LegacyBlock stainedClayBlock = LOTRLegacyBlocks.vanilla("stained_hardened_clay");
    public int stainedClayMeta = 1;
    public LegacyBlock brickBlock = LOTRLegacyBlocks.mod("brick3");
    public int brickMeta = 10;
    public LegacyBlock brickSlabBlock = LOTRLegacyBlocks.mod("slabSingle7");
    public int brickSlabMeta;
    public LegacyBlock plankBlock = LOTRLegacyBlocks.vanilla("planks");
    public int plankMeta = 4;
    public LegacyBlock plankSlabBlock = LOTRLegacyBlocks.vanilla("wooden_slab");
    public int plankSlabMeta = 4;
    public LegacyBlock fenceBlock = LOTRLegacyBlocks.vanilla("fence");
    public int fenceMeta = 4;
    public LegacyBlock thatchBlock = LOTRLegacyBlocks.mod("thatch");
    public int thatchMeta;
    public LegacyBlock thatchSlabBlock = LOTRLegacyBlocks.mod("slabSingleThatch");
    public int thatchSlabMeta;

    protected LOTRMoredainHutStructure(boolean flag) {
        super(flag);
    }

    public void dropFence(WorldGenLevel world, int i, int j, int k) {
        do {
            setBlockAndMetadata(world, i, j, k, fenceBlock, fenceMeta);
            if (isOpaque(world, i, j - 1, k)) {
                break;
            }
            --j;
        } while (true);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        setOriginAndRotation(world, i, j, k, rotation, getOffset());
        if (restrictions) {
            int minHeight = 0;
            int maxHeight = 0;
            int range = getOffset();
            for (int i1 = -range; i1 <= range; ++i1) {
                for (int k1 = -range; k1 <= range; ++k1) {
                    int j1 = getTopBlock(world, i1, k1);
                    BlockState block = getBlockState(world, i1, j1 - 1, k1);
                    if (!LOTRLegacyBlocks.vanilla("grass").matches(block) && !LOTRLegacyBlocks.vanilla("dirt").matches(block) && !LOTRLegacyBlocks.vanilla("sand").matches(block) && !LOTRLegacyBlocks.vanilla("stone").matches(block)) {
                        return false;
                    }
                    if (j1 < minHeight) {
                        minHeight = j1;
                    }
                    if (j1 > maxHeight) {
                        maxHeight = j1;
                    }
                    if (maxHeight - minHeight <= 5) {
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
        for (int j = 0; (j == 0 || !isOpaque(world, i, j, k)) && getY(j) >= world.getMinY(); --j) {
            setBlockAndMetadata(world, i, j, k, clayBlock, clayMeta);
            setGrassToDirt(world, i, j - 1, k);
        }
    }
}
