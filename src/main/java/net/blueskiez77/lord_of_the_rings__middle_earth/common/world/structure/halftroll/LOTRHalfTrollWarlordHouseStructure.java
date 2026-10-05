package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.halftroll;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.halftroll.LOTRHalfTrollWarlordEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LOTRHalfTrollWarlordHouseStructure extends LOTRStructureBase2 {
    public LOTRHalfTrollWarlordHouseStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        int i1;
        int k1;
        int j1;
        int i12;
        int k12;
        int radius = 7;
        int height = 10 + random.nextInt(4);
        setOriginAndRotation(world, i, j, k, rotation, radius + 1);
        if (restrictions) {
            for (i12 = -radius; i12 <= radius; ++i12) {
                for (k12 = -radius; k12 <= radius; ++k12) {
                    j1 = getTopBlock(world, i12, k12);
                    BlockState block = getBlockState(world, i12, j1 - 1, k12);
                    if (LOTRLegacyBlocks.vanilla("grass").matches(block)) {
                        continue;
                    }
                    return false;
                }
            }
        }
        for (i12 = -radius; i12 <= radius; ++i12) {
            for (k12 = -radius; k12 <= radius; ++k12) {
                for (j1 = 0; j1 <= height; ++j1) {
                    double f = (i12 * i12 + k12 * k12) / 4.0 - (8 - j1);
                    if (f >= 8.0) {
                        continue;
                    }
                    if (j1 == 0) {
                        for (int j2 = 0; (j2 == 0 || !isOpaque(world, i12, j2, k12)) && getY(j2) >= 0; --j2) {
                            setBlockAndMetadata(world, i12, j2, k12, LOTRLegacyBlocks.vanilla("hardened_clay"), 0);
                            setGrassToDirt(world, i12, j2 - 1, k12);
                        }
                    }
                    if (f > 0.0) {
                        if (j1 <= 1 || j1 == height - 1) {
                            setBlockAndMetadata(world, i12, j1, k12, LOTRLegacyBlocks.vanilla("stained_hardened_clay"), 12);
                            continue;
                        }
                        setBlockAndMetadata(world, i12, j1, k12, LOTRLegacyBlocks.vanilla("hardened_clay"), 0);
                        continue;
                    }
                    if (j1 == 0) {
                        setBlockAndMetadata(world, i12, 0, k12, LOTRLegacyBlocks.vanilla("cobblestone"), 0);
                        continue;
                    }
                    setAir(world, i12, j1, k12);
                }
            }
        }
        for (i12 = -1; i12 <= 1; ++i12) {
            for (k12 = -radius; k12 <= -radius + 2; ++k12) {
                setBlockAndMetadata(world, i12, 0, k12, LOTRLegacyBlocks.vanilla("cobblestone"), 0);
                for (j1 = 1; j1 <= 3; ++j1) {
                    setAir(world, i12, j1, k12);
                }
            }
            setBlockAndMetadata(world, i12, 4, -radius, LOTRLegacyBlocks.mod("woodSlabSingle"), 3);
        }
        setBlockAndMetadata(world, -2, 2, -radius, LOTRLegacyBlocks.mod("fence"), 3);
        setBlockAndMetadata(world, -2, 3, -radius, LOTRLegacyBlocks.mod("woodSlabSingle"), 3);
        setBlockAndMetadata(world, 2, 2, -radius, LOTRLegacyBlocks.mod("fence"), 3);
        setBlockAndMetadata(world, 2, 3, -radius, LOTRLegacyBlocks.mod("woodSlabSingle"), 3);
        for (i12 = -2; i12 <= 2; ++i12) {
            for (k12 = -2; k12 <= 2; ++k12) {
                int i2 = Math.abs(i12);
                int k2 = Math.abs(k12);
                if (i2 == 2 || k2 == 2 || i2 == 0 && k2 == 0) {
                    for (int j12 = -4; j12 <= 0; ++j12) {
                        setBlockAndMetadata(world, i12, j12, k12, LOTRLegacyBlocks.vanilla("stained_hardened_clay"), 12);
                    }
                    continue;
                }
                if (i2 != 1 && k2 != 1) {
                    continue;
                }
                setBlockAndMetadata(world, i12, -4, k12, LOTRLegacyBlocks.mod("hearth"), 0);
                setBlockAndMetadata(world, i12, -3, k12, LOTRLegacyBlocks.vanilla("fire"), 0);
                setBlockAndMetadata(world, i12, -2, k12, LOTRLegacyBlocks.vanilla("air"), 0);
                setBlockAndMetadata(world, i12, -1, k12, LOTRLegacyBlocks.vanilla("air"), 0);
                setBlockAndMetadata(world, i12, 0, k12, LOTRLegacyBlocks.vanilla("iron_bars"), 0);
            }
        }
        setBlockAndMetadata(world, 0, 0, 0, LOTRLegacyBlocks.vanilla("cobblestone"), 0);
        for (int l = 0; l < 8; ++l) {
            i1 = (3 + (l + 1) / 2 % 2) * (int) Math.pow(-1, l / 4);
            k1 = (3 + (l + 3) / 2 % 2) * (int) Math.pow(-1, (l + 2) / 4);
            setBlockAndMetadata(world, i1, 1, k1, LOTRLegacyBlocks.vanilla("cobblestone"), 0);
            setBlockAndMetadata(world, i1, 2, k1, LOTRLegacyBlocks.mod("fence"), 3);
            setBlockAndMetadata(world, i1, 3, k1, LOTRLegacyBlocks.mod("fence"), 3);
        }
        setBlockAndMetadata(world, -5, 3, 0, LOTRLegacyBlocks.mod("fence"), 3);
        setAir(world, -6, 3, 0);
        setAir(world, -7, 3, 0);
        setBlockAndMetadata(world, 5, 3, 0, LOTRLegacyBlocks.mod("fence"), 3);
        setAir(world, 6, 3, 0);
        setAir(world, 7, 3, 0);
        setBlockAndMetadata(world, 0, 3, 5, LOTRLegacyBlocks.mod("fence"), 3);
        setAir(world, 0, 3, 6);
        setAir(world, 0, 3, 7);
        for (i12 = -4; i12 <= 4; i12 += 8) {
            setBlockAndMetadata(world, i12, 1, -2, LOTRLegacyBlocks.vanilla("stone_slab"), 11);
            setBlockAndMetadata(world, i12, 1, -1, LOTRLegacyBlocks.vanilla("stone_slab"), 11);
            setBlockAndMetadata(world, i12, 1, 1, LOTRLegacyBlocks.vanilla("stone_slab"), 11);
            setBlockAndMetadata(world, i12, 1, 2, LOTRLegacyBlocks.vanilla("stone_slab"), 11);
            setBlockAndMetadata(world, i12 + Integer.signum(i12), 1, 0, LOTRLegacyBlocks.vanilla("stained_hardened_clay"), 12);
            placeChest(world, random, i12, 1, 0, LOTRLegacyBlocks.mod("chestBasket"), 0, LOTRChestContents.HALF_TROLL_HOUSE);
        }
        setBlockAndMetadata(world, -2, 1, 4, LOTRLegacyBlocks.vanilla("crafting_table"), 0);
        setBlockAndMetadata(world, -1, 1, 4, LOTRLegacyBlocks.vanilla("stone_slab"), 11);
        setBlockAndMetadata(world, 0, 1, 4, LOTRLegacyBlocks.vanilla("cobblestone"), 0);
        setBlockAndMetadata(world, 1, 1, 4, LOTRLegacyBlocks.vanilla("stone_slab"), 11);
        setBlockAndMetadata(world, 2, 1, 4, LOTRLegacyBlocks.mod("halfTrollTable"), 0);
        setBlockAndMetadata(world, 0, 1, 3, LOTRLegacyBlocks.mod("commandTable"), 0);
        placeWallBanner(world, 0, 6, -radius + 1, "HALF_TROLL", 2);
        int b = 8;
        for (int l = 0; l < 2; ++l) {
            setBlockAndMetadata(world, 0, b, 0, LOTRLegacyBlocks.mod("fence"), 3);
            --b;
        }
        setBlockAndMetadata(world, 0, b, 0, LOTRLegacyBlocks.mod("woodSlabSingle"), 11);
        placeWallBanner(world, 0, b, 0, "HALF_TROLL", 0);
        placeWallBanner(world, 0, b, 0, "HALF_TROLL", 1);
        placeWallBanner(world, 0, b, 0, "HALF_TROLL", 2);
        placeWallBanner(world, 0, b, 0, "HALF_TROLL", 3);
        for (i1 = -radius; i1 <= radius; ++i1) {
            block16:
            for (k1 = -radius; k1 <= radius; ++k1) {
                if (random.nextInt(10) != 0) {
                    continue;
                }
                for (int j13 = height; j13 > 0; --j13) {
                    if (!isAir(world, i1, j13, k1) || !isOpaque(world, i1, j13 - 1, k1)) {
                        continue;
                    }
                    placeSkull(world, random, i1, j13, k1);
                    continue block16;
                }
            }
        }
        LOTRHalfTrollWarlordEntity halfTroll = create(LOTREntities.HALF_TROLL_WARLORD, world);
        halfTroll.spawnRidingHorse = false;
        spawnNPCAndSetHome(halfTroll, world, 0, 1, 0, 16);
        return true;
    }
}
