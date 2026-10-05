package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal.LOTRHorseEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRNearHaradrimBaseEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRSouthronStablesStructure extends LOTRSouthronStructure {
    public LOTRSouthronStablesStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        int j1;
        setOriginAndRotation(world, i, j, k, rotation, 8);
        setupRandomBlocks(random);
        if (restrictions) {
            int minHeight = 0;
            int maxHeight = 0;
            for (int i1 = -4; i1 <= 8; ++i1) {
                for (int k1 = -7; k1 <= 7; ++k1) {
                    j1 = getTopBlock(world, i1, k1) - 1;
                    if (!isSurface(world, i1, j1, k1)) {
                        return false;
                    }
                    if (j1 < minHeight) {
                        minHeight = j1;
                    }
                    if (j1 > maxHeight) {
                        maxHeight = j1;
                    }
                    if (maxHeight - minHeight <= 8) {
                        continue;
                    }
                    return false;
                }
            }
        }
        for (int i1 = -4; i1 <= 8; ++i1) {
            for (int k1 = -7; k1 <= 7; ++k1) {
                int i2 = Math.abs(i1);
                int k2 = Math.abs(k1);
                if (i2 <= 4 || i1 >= 5 && k2 <= 6) {
                    for (j1 = 0; (j1 >= 0 || !isOpaque(world, i1, j1, k1)) && getY(j1) >= world.getMinY(); --j1) {
                        setBlockAndMetadata(world, i1, j1, k1, stoneBlock, stoneMeta);
                        setGrassToDirt(world, i1, j1 - 1, k1);
                    }
                    for (j1 = 1; j1 <= 8; ++j1) {
                        setAir(world, i1, j1, k1);
                    }
                }
                if ((i2 > 3 || k2 > 6) && (i1 < 4 || i1 > 7 || k2 > 5)) {
                    continue;
                }
                random.nextInt(2);
                if (random.nextBoolean()) {
                    setBlockAndMetadata(world, i1, 0, k1, LOTRLegacyBlocks.vanilla("sand"), 0);
                } else {
                    setBlockAndMetadata(world, i1, 0, k1, LOTRLegacyBlocks.mod("dirtPath"), 0);
                }
                if (random.nextInt(4) != 0) {
                    continue;
                }
                setBlockAndMetadata(world, i1, 1, k1, LOTRLegacyBlocks.mod("thatchFloor"), 0);
            }
        }
        loadStrScan("southron_stable");
        associateBlockMetaAlias("STONE", stoneBlock, stoneMeta);
        associateBlockMetaAlias("BRICK", brickBlock, brickMeta);
        associateBlockMetaAlias("BRICK_SLAB", brickSlabBlock, brickSlabMeta);
        associateBlockMetaAlias("BRICK_SLAB_INV", brickSlabBlock, brickSlabMeta | 8);
        associateBlockAlias("BRICK_STAIR", brickStairBlock);
        associateBlockMetaAlias("PILLAR", pillarBlock, pillarMeta);
        associateBlockMetaAlias("PLANK", plankBlock, plankMeta);
        associateBlockMetaAlias("PLANK_SLAB", plankSlabBlock, plankSlabMeta);
        associateBlockMetaAlias("PLANK_SLAB_INV", plankSlabBlock, plankSlabMeta | 8);
        associateBlockAlias("PLANK_STAIR", plankStairBlock);
        associateBlockMetaAlias("FENCE", fenceBlock, fenceMeta);
        associateBlockAlias("FENCE_GATE", fenceGateBlock);
        associateBlockMetaAlias("BEAM", woodBeamBlock, woodBeamMeta);
        associateBlockMetaAlias("BEAM|4", woodBeamBlock, woodBeamMeta4);
        associateBlockMetaAlias("BEAM|8", woodBeamBlock, woodBeamMeta8);
        associateBlockMetaAlias("ROOF", roofBlock, roofMeta);
        associateBlockMetaAlias("ROOF_SLAB", roofSlabBlock, roofSlabMeta);
        associateBlockMetaAlias("ROOF_SLAB_INV", roofSlabBlock, roofSlabMeta | 8);
        associateBlockAlias("ROOF_STAIR", roofStairBlock);
        generateStrScan(world, random, 0, 1, 0);
        placeChest(world, random, -3, 1, 6, LOTRLegacyBlocks.mod("chestBasket"), 2, LOTRChestContents.NEAR_HARAD_HOUSE);
        int numHaradrim = 1 + random.nextInt(2);
        for (int l = 0; l < numHaradrim; ++l) {
            LOTRNearHaradrimBaseEntity haradrim = createHaradrim(world);
            spawnNPCAndSetHome(haradrim, world, 0, 1, 0, 8);
        }
        for (int k1 : new int[]{-4, 0, 4}) {
            int i1 = 5;
            int j12 = 1;
            LOTRHorseEntity horse = create(LOTREntities.HORSE, world);
            spawnNPCAndSetHome(horse, world, i1, j12, k1, 0);
            horse.saddleMountForWorldGen();
            horse.clearHome();
        }
        return true;
    }
}
