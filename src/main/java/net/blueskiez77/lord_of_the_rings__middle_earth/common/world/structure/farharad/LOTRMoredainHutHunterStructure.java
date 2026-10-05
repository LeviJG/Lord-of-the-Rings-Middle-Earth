package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.farharad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.farharad.LOTRMoredainWarriorEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRMoredainHutHunterStructure extends LOTRMoredainHutStructure {
    public LOTRMoredainHutHunterStructure(boolean flag) {
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
        for (i1 = -2; i1 <= 2; ++i1) {
            for (k1 = -2; k1 <= 2; ++k1) {
                int j1;
                layFoundation(world, i1, k1);
                for (int j12 = 1; j12 <= 5; ++j12) {
                    setAir(world, i1, j12, k1);
                }
                i2 = Math.abs(i1);
                k2 = Math.abs(k1);
                if (i2 <= 1 && k2 <= 1) {
                    setBlockAndMetadata(world, i1, 0, k1, LOTRLegacyBlocks.mod("redClay"), 0);
                    if (random.nextBoolean()) {
                        setBlockAndMetadata(world, i1, 1, k1, LOTRLegacyBlocks.mod("thatchFloor"), 0);
                    }
                }
                if (i2 == 2 && k2 <= 1 || k2 == 2 && i2 <= 1) {
                    setBlockAndMetadata(world, i1, 1, k1, clayBlock, clayMeta);
                    for (j1 = 2; j1 <= 3; ++j1) {
                        setBlockAndMetadata(world, i1, j1, k1, plankBlock, plankMeta);
                    }
                }
                if (i2 == 2 && k2 == 2) {
                    for (j1 = 1; j1 <= 2; ++j1) {
                        setBlockAndMetadata(world, i1, j1, k1, fenceBlock, fenceMeta);
                    }
                }
                if (i2 != 1 || k2 != 1) {
                    continue;
                }
                setBlockAndMetadata(world, i1, 3, k1, fenceBlock, fenceMeta);
            }
        }
        for (i1 = -2; i1 <= 2; ++i1) {
            for (k1 = -2; k1 <= 2; ++k1) {
                i2 = Math.abs(i1);
                k2 = Math.abs(k1);
                if (i2 == 2 && k2 == 2) {
                    setBlockAndMetadata(world, i1, 3, k1, thatchSlabBlock, thatchSlabMeta | 8);
                }
                if (i2 == 2 && k2 == 0 || k2 == 2 && i2 == 0) {
                    setBlockAndMetadata(world, i1, 4, k1, thatchBlock, thatchMeta);
                }
                if (i2 + k2 == 3) {
                    setBlockAndMetadata(world, i1, 4, k1, thatchSlabBlock, thatchSlabMeta);
                }
                if (i2 == 1 && k2 == 1) {
                    setBlockAndMetadata(world, i1, 4, k1, thatchSlabBlock, thatchSlabMeta | 8);
                }
                if (i2 + k2 != 1) {
                    continue;
                }
                setBlockAndMetadata(world, i1, 5, k1, thatchSlabBlock, thatchSlabMeta);
            }
        }
        setAir(world, 0, 1, -2);
        setAir(world, 0, 2, -2);
        dropFence(world, -1, 2, -3);
        dropFence(world, 1, 2, -3);
        setBlockAndMetadata(world, -1, 3, -3, thatchSlabBlock, thatchSlabMeta);
        setBlockAndMetadata(world, 0, 3, -3, thatchSlabBlock, thatchSlabMeta | 8);
        setBlockAndMetadata(world, 1, 3, -3, thatchSlabBlock, thatchSlabMeta);
        setBlockAndMetadata(world, -1, 1, 0, LOTRLegacyBlocks.mod("strawBed"), 0);
        setBlockAndMetadata(world, -1, 1, 1, LOTRLegacyBlocks.mod("strawBed"), 8);
        placeChest(world, random, 1, 1, 1, LOTRLegacyBlocks.mod("chestBasket"), 2, LOTRChestContents.MOREDAIN_HUT);
        setBlockAndMetadata(world, 0, 3, 1, LOTRLegacyBlocks.vanilla("torch"), 4);
        setBlockAndMetadata(world, 0, 3, -1, LOTRLegacyBlocks.vanilla("torch"), 3);
        LOTRMoredainWarriorEntity moredain = create(LOTREntities.MOREDAIN_WARRIOR, world);
        moredain.spawnRidingHorse = false;
        spawnNPCAndSetHome(moredain, world, 0, 1, 0, 8);
        return true;
    }

    @Override
    public int getOffset() {
        return 3;
    }
}
