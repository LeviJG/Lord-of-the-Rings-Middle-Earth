package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.farharad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.farharad.LOTRMoredainEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRMoredainHutVillageStructure extends LOTRMoredainHutStructure {
    public LegacyBlock bedBlock;

    public LOTRMoredainHutVillageStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        int k2;
        int k1;
        int i2;
        int i1;
        if (!super.generateWithSetRotation(world, random, i, j, k, rotation)) {
            return false;
        }
        bedBlock = random.nextBoolean() ? LOTRLegacyBlocks.mod("strawBed") : LOTRLegacyBlocks.mod("lionBed");
        for (i1 = -3; i1 <= 3; ++i1) {
            for (k1 = -3; k1 <= 3; ++k1) {
                int j1;
                layFoundation(world, i1, k1);
                for (int j12 = 1; j12 <= 6; ++j12) {
                    setAir(world, i1, j12, k1);
                }
                i2 = Math.abs(i1);
                k2 = Math.abs(k1);
                if (i2 <= 2 && k2 <= 2) {
                    setBlockAndMetadata(world, i1, 0, k1, LOTRLegacyBlocks.mod("redClay"), 0);
                    if (random.nextBoolean()) {
                        setBlockAndMetadata(world, i1, 1, k1, LOTRLegacyBlocks.mod("thatchFloor"), 0);
                    }
                }
                if (i2 == 3 || k2 == 3) {
                    setBlockAndMetadata(world, i1, 1, k1, clayBlock, clayMeta);
                }
                if (i2 == 3 && k2 <= 2 || k2 == 3 && i2 <= 2) {
                    for (j1 = 2; j1 <= 4; ++j1) {
                        setBlockAndMetadata(world, i1, j1, k1, brickBlock, brickMeta);
                    }
                }
                if (i2 == 3 && k2 == 3) {
                    for (j1 = 2; j1 <= 3; ++j1) {
                        setBlockAndMetadata(world, i1, j1, k1, fenceBlock, fenceMeta);
                    }
                }
                if (i2 != 2 || k2 != 2) {
                    continue;
                }
                for (j1 = 1; j1 <= 4; ++j1) {
                    setBlockAndMetadata(world, i1, j1, k1, fenceBlock, fenceMeta);
                }
            }
        }
        for (i1 = -4; i1 <= 4; ++i1) {
            for (k1 = -4; k1 <= 4; ++k1) {
                i2 = Math.abs(i1);
                k2 = Math.abs(k1);
                if (i2 == 4 && k2 <= 2 || k2 == 4 && i2 <= 2) {
                    setBlockAndMetadata(world, i1, 4, k1, thatchSlabBlock, thatchSlabMeta | 8);
                }
                if (i2 == 3 && k2 == 3) {
                    setBlockAndMetadata(world, i1, 4, k1, thatchSlabBlock, thatchSlabMeta | 8);
                }
                if (i2 == 4 && k2 == 0 || k2 == 4 && i2 == 0) {
                    setBlockAndMetadata(world, i1, 4, k1, thatchBlock, thatchMeta);
                    setBlockAndMetadata(world, i1, 5, k1, thatchSlabBlock, thatchSlabMeta);
                }
                if (i2 == 3 && k2 <= 2 || k2 == 3 && i2 <= 2) {
                    setBlockAndMetadata(world, i1, 5, k1, thatchSlabBlock, thatchSlabMeta);
                }
                if (i2 == 3 && k2 == 0 || k2 == 3 && i2 == 0) {
                    setBlockAndMetadata(world, i1, 5, k1, thatchBlock, thatchMeta);
                }
                if (i2 == 2 && k2 <= 2 || k2 == 2 && i2 <= 2) {
                    setBlockAndMetadata(world, i1, 5, k1, thatchSlabBlock, thatchSlabMeta | 8);
                }
                if (i2 + k2 == 2) {
                    setBlockAndMetadata(world, i1, 6, k1, thatchSlabBlock, thatchSlabMeta);
                }
                if (i2 + k2 == 1) {
                    setBlockAndMetadata(world, i1, 6, k1, thatchBlock, thatchMeta);
                }
                if (i2 <= 2 && k2 <= 2 && i2 + k2 >= 3) {
                    setBlockAndMetadata(world, i1, 5, k1, thatchBlock, thatchMeta);
                }
                if (i2 != 1 || k2 != 1) {
                    continue;
                }
                setBlockAndMetadata(world, i1, 5, k1, thatchSlabBlock, thatchSlabMeta | 8);
            }
        }
        setAir(world, 0, 1, -3);
        setAir(world, 0, 2, -3);
        setBlockAndMetadata(world, 0, 3, -3, plankSlabBlock, plankSlabMeta | 8);
        setBlockAndMetadata(world, -1, 4, -4, thatchBlock, thatchMeta);
        setBlockAndMetadata(world, 1, 4, -4, thatchBlock, thatchMeta);
        dropFence(world, -1, 3, -4);
        dropFence(world, 1, 3, -4);
        setBlockAndMetadata(world, -3, 2, 0, plankSlabBlock, plankSlabMeta);
        setBlockAndMetadata(world, -3, 3, 0, plankSlabBlock, plankSlabMeta | 8);
        setBlockAndMetadata(world, 0, 2, 3, plankSlabBlock, plankSlabMeta);
        setBlockAndMetadata(world, 0, 3, 3, plankSlabBlock, plankSlabMeta | 8);
        setBlockAndMetadata(world, 3, 2, 0, plankSlabBlock, plankSlabMeta);
        setBlockAndMetadata(world, 3, 3, 0, plankSlabBlock, plankSlabMeta | 8);
        setBlockAndMetadata(world, -2, 1, 2, plankBlock, plankMeta);
        setBlockAndMetadata(world, -1, 1, 2, plankBlock, plankMeta);
        setBlockAndMetadata(world, 2, 1, 2, LOTRLegacyBlocks.vanilla("crafting_table"), 0);
        setBlockAndMetadata(world, 2, 1, 1, plankBlock, plankMeta);
        placeChest(world, random, 2, 1, 0, LOTRLegacyBlocks.mod("chestBasket"), 5, LOTRChestContents.MOREDAIN_HUT);
        setBlockAndMetadata(world, 2, 1, -1, plankBlock, plankMeta);
        setBlockAndMetadata(world, 2, 1, -2, LOTRLegacyBlocks.mod("moredainTable"), 0);
        setBlockAndMetadata(world, -1, 1, -1, bedBlock, 3);
        setBlockAndMetadata(world, -2, 1, -1, bedBlock, 11);
        setBlockAndMetadata(world, -1, 1, 1, bedBlock, 3);
        setBlockAndMetadata(world, -2, 1, 1, bedBlock, 11);
        setBlockAndMetadata(world, -2, 2, -4, LOTRLegacyBlocks.vanilla("torch"), 4);
        setBlockAndMetadata(world, 2, 2, -4, LOTRLegacyBlocks.vanilla("torch"), 4);
        setBlockAndMetadata(world, 0, 4, 2, LOTRLegacyBlocks.vanilla("torch"), 4);
        setBlockAndMetadata(world, 0, 4, -2, LOTRLegacyBlocks.vanilla("torch"), 3);
        LOTRMoredainEntity moredain = create(LOTREntities.MOREDAIN, world);
        spawnNPCAndSetHome(moredain, world, 0, 1, 0, 8);
        return true;
    }

    @Override
    public int getOffset() {
        return 4;
    }
}
