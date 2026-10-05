package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.halftroll;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.halftroll.LOTRHalfTrollEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LOTRHalfTrollHouseStructure extends LOTRStructureBase2 {
    public LOTRHalfTrollHouseStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        int j1;
        int i1;
        int k1;
        int radius = 5;
        int height = 6 + random.nextInt(4);
        setOriginAndRotation(world, i, j, k, rotation, radius + 1);
        if (restrictions) {
            for (i1 = -radius; i1 <= radius; ++i1) {
                for (k1 = -radius; k1 <= radius; ++k1) {
                    j1 = getTopBlock(world, i1, k1);
                    BlockState block = getBlockState(world, i1, j1 - 1, k1);
                    if (LOTRLegacyBlocks.vanilla("grass").matches(block)) {
                        continue;
                    }
                    return false;
                }
            }
        }
        for (i1 = -radius; i1 <= radius; ++i1) {
            for (k1 = -radius; k1 <= radius; ++k1) {
                for (j1 = 0; j1 <= height; ++j1) {
                    double f = (i1 * i1 + k1 * k1) / 2.0 - (8 - j1);
                    if (f >= 8.0) {
                        continue;
                    }
                    if (j1 == 0) {
                        for (int j2 = 0; (j2 == 0 || !isOpaque(world, i1, j2, k1)) && getY(j2) >= 0; --j2) {
                            setBlockAndMetadata(world, i1, j2, k1, LOTRLegacyBlocks.vanilla("hardened_clay"), 0);
                            setGrassToDirt(world, i1, j2 - 1, k1);
                        }
                    }
                    if (f > 0.0) {
                        if (j1 <= 1 || j1 == height - 1) {
                            setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("stained_hardened_clay"), 12);
                            continue;
                        }
                        setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("hardened_clay"), 0);
                        continue;
                    }
                    if (j1 == 0) {
                        setBlockAndMetadata(world, i1, 0, k1, LOTRLegacyBlocks.vanilla("cobblestone"), 0);
                        continue;
                    }
                    setAir(world, i1, j1, k1);
                }
            }
        }
        for (i1 = -1; i1 <= 1; ++i1) {
            for (k1 = -radius; k1 <= -radius + 1; ++k1) {
                setBlockAndMetadata(world, i1, 0, k1, LOTRLegacyBlocks.vanilla("cobblestone"), 0);
                for (j1 = 1; j1 <= 3; ++j1) {
                    setAir(world, i1, j1, k1);
                }
            }
            setBlockAndMetadata(world, i1, 4, -radius, LOTRLegacyBlocks.mod("woodSlabSingle"), 3);
        }
        setBlockAndMetadata(world, -2, 2, -radius, LOTRLegacyBlocks.mod("fence"), 3);
        setBlockAndMetadata(world, -2, 3, -radius, LOTRLegacyBlocks.mod("woodSlabSingle"), 3);
        setBlockAndMetadata(world, 2, 2, -radius, LOTRLegacyBlocks.mod("fence"), 3);
        setBlockAndMetadata(world, 2, 3, -radius, LOTRLegacyBlocks.mod("woodSlabSingle"), 3);
        for (i1 = -2; i1 <= 2; ++i1) {
            for (k1 = -2; k1 <= 2; ++k1) {
                int i2 = Math.abs(i1);
                int k2 = Math.abs(k1);
                if (i2 == 2 || k2 == 2 || i2 == 0 && k2 == 0) {
                    for (int j12 = -4; j12 <= 0; ++j12) {
                        setBlockAndMetadata(world, i1, j12, k1, LOTRLegacyBlocks.vanilla("stained_hardened_clay"), 12);
                    }
                    continue;
                }
                if (i2 != 1 && k2 != 1) {
                    continue;
                }
                setBlockAndMetadata(world, i1, -4, k1, LOTRLegacyBlocks.mod("hearth"), 0);
                setBlockAndMetadata(world, i1, -3, k1, LOTRLegacyBlocks.vanilla("fire"), 0);
                setBlockAndMetadata(world, i1, -2, k1, LOTRLegacyBlocks.vanilla("air"), 0);
                setBlockAndMetadata(world, i1, -1, k1, LOTRLegacyBlocks.vanilla("air"), 0);
                setBlockAndMetadata(world, i1, 0, k1, LOTRLegacyBlocks.vanilla("iron_bars"), 0);
            }
        }
        setBlockAndMetadata(world, 0, 0, 0, LOTRLegacyBlocks.vanilla("cobblestone"), 0);
        for (int l = 0; l < 8; ++l) {
            int i12 = (2 + (l + 1) / 2 % 2) * (int) Math.pow(-1, l / 4);
            int k12 = (2 + (l + 3) / 2 % 2) * (int) Math.pow(-1, (l + 2) / 4);
            setBlockAndMetadata(world, i12, 1, k12, LOTRLegacyBlocks.vanilla("cobblestone"), 0);
            setBlockAndMetadata(world, i12, 2, k12, LOTRLegacyBlocks.mod("fence"), 3);
            setBlockAndMetadata(world, i12, 3, k12, LOTRLegacyBlocks.mod("fence"), 3);
        }
        setBlockAndMetadata(world, -4, 3, 0, LOTRLegacyBlocks.mod("fence"), 3);
        setAir(world, -5, 3, 0);
        setBlockAndMetadata(world, 4, 3, 0, LOTRLegacyBlocks.mod("fence"), 3);
        setAir(world, 5, 3, 0);
        setBlockAndMetadata(world, 0, 3, 4, LOTRLegacyBlocks.mod("fence"), 3);
        setAir(world, 0, 3, 5);
        for (i1 = -3; i1 <= 3; i1 += 6) {
            setBlockAndMetadata(world, i1, 1, -1, LOTRLegacyBlocks.vanilla("stone_slab"), 11);
            setBlockAndMetadata(world, i1, 1, 1, LOTRLegacyBlocks.vanilla("stone_slab"), 11);
            placeChest(world, random, i1, 1, 0, LOTRLegacyBlocks.mod("chestBasket"), 0, LOTRChestContents.HALF_TROLL_HOUSE);
        }
        setBlockAndMetadata(world, -1, 1, 3, LOTRLegacyBlocks.vanilla("crafting_table"), 0);
        setBlockAndMetadata(world, 0, 1, 3, LOTRLegacyBlocks.vanilla("stone_slab"), 11);
        setBlockAndMetadata(world, 1, 1, 3, LOTRLegacyBlocks.mod("halfTrollTable"), 0);
        LOTRHalfTrollEntity halfTroll = create(LOTREntities.HALF_TROLL, world);
        spawnNPCAndSetHome(halfTroll, world, 0, 1, 0, 16);
        return true;
    }
}
