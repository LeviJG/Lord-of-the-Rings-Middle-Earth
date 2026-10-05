package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.dwarf;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRNPCRespawnerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRFoods;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.dwarf.LOTRBlueDwarfCommanderEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.dwarf.LOTRBlueDwarfWarriorEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

public class LOTRBlueMountainsStrongholdStructure extends LOTRStructureBase {
    public LOTRBlueMountainsStrongholdStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generate(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        LegacyBlock block;
        int i1;
        int i12;
        int k1;
        BlockState below = world.getBlockState(new BlockPos(i, j - 1, k));
        if (restrictions && !LOTRLegacyBlocks.vanilla("grass").matches(below) && !LOTRLegacyBlocks.vanilla("stone").matches(below)
                && !LOTRLegacyBlocks.vanilla("dirt").matches(below) && !LOTRLegacyBlocks.mod("rock").matches(below)
                && !LOTRLegacyBlocks.vanilla("snow").matches(below)) {
            return false;
        }
        --j;
        int rotation = random.nextInt(4);
        if (!restrictions && usingPlayer != null) {
            rotation = usingPlayerRotation();
        }
        switch (rotation) {
            case 0: {
                k += 8;
                break;
            }
            case 1: {
                i -= 8;
                break;
            }
            case 2: {
                k -= 8;
                break;
            }
            case 3: {
                i += 8;
            }
        }
        if (restrictions) {
            int minHeight = j;
            int maxHeight = j;
            for (int i13 = i - 6; i13 <= i + 6; ++i13) {
                for (int k12 = k - 6; k12 <= k + 6; ++k12) {
                    int j1 = world.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, i13, k12) - 1;
                    BlockState block2 = world.getBlockState(new BlockPos(i13, j1, k12));
                    if (!LOTRLegacyBlocks.vanilla("grass").matches(block2) && !LOTRLegacyBlocks.vanilla("stone").matches(block2) && !LOTRLegacyBlocks.vanilla("dirt").matches(block2) && !LOTRLegacyBlocks.mod("rock").matches(block2) && !LOTRLegacyBlocks.vanilla("snow").matches(block2)) {
                        return false;
                    }
                    if (j1 < minHeight) {
                        minHeight = j1;
                    }
                    if (j1 <= maxHeight) {
                        continue;
                    }
                    maxHeight = j1;
                }
            }
            if (maxHeight - minHeight > 10) {
                return false;
            }
        }
        for (k1 = k - 6; k1 <= k + 6; ++k1) {
            for (i12 = i - 6; i12 <= i + 6; ++i12) {
                boolean flag = Math.abs(k1 - k) == 6 && Math.abs(i12 - i) == 6;
                for (int j1 = j + 7; (j1 >= j || !isOpaqueAt(world, i12, j1, k1)) && j1 >= world.getMinY(); --j1) {
                    if (flag) {
                        setBlockAndNotifyAdequately(world, i12, j1, k1, LOTRLegacyBlocks.mod("pillar"), 3);
                    } else {
                        if (Math.abs(i12 - i) < 6 && Math.abs(k1 - k) < 6) {
                            if (j1 >= j + 1 && j1 <= j + 3 || j1 >= j + 4 && j1 <= j + 7) {
                                setAir(world, i12, j1, k1);
                                continue;
                            }
                            if (j1 == j) {
                                setBlockAndNotifyAdequately(world, i12, j1, k1, LOTRLegacyBlocks.vanilla("planks"), 1);
                                continue;
                            }
                        }
                        setBlockAndNotifyAdequately(world, i12, j1, k1, LOTRLegacyBlocks.mod("brick"), 14);
                    }
                    if (j1 > j) {
                        continue;
                    }
                    setGrassToDirt(world, i12, j1 - 1, k1);
                }
            }
        }
        for (i1 = i - 6; i1 <= i + 6; ++i1) {
            setBlockAndNotifyAdequately(world, i1, j + 8, k - 6, LOTRLegacyBlocks.mod("stairsDwarvenBrick"), 2);
            setBlockAndNotifyAdequately(world, i1, j + 8, k + 6, LOTRLegacyBlocks.mod("stairsDwarvenBrick"), 3);
        }
        for (k1 = k - 6; k1 <= k + 6; ++k1) {
            setBlockAndNotifyAdequately(world, i - 6, j + 8, k1, LOTRLegacyBlocks.mod("stairsDwarvenBrick"), 0);
            setBlockAndNotifyAdequately(world, i + 6, j + 8, k1, LOTRLegacyBlocks.mod("stairsDwarvenBrick"), 1);
        }
        for (k1 = k - 5; k1 <= k + 5; ++k1) {
            for (i12 = i - 5; i12 <= i + 5; ++i12) {
                setBlockAndNotifyAdequately(world, i12, j + 4, k1, LOTRLegacyBlocks.mod("slabDouble3"), 0);
                setBlockAndNotifyAdequately(world, i12, j + 8, k1, LOTRLegacyBlocks.mod("slabDouble3"), 0);
                int i2 = Math.abs(i12 - i);
                int k2 = Math.abs(k1 - k);
                int l = -1;
                if (i2 == 5) {
                    l = k2 % 2;
                } else if (k2 == 5) {
                    l = i2 % 2;
                }
                if (l == -1) {
                    continue;
                }
                if (l == 1) {
                    for (int j1 = j + 9; j1 <= j + 11; ++j1) {
                        setBlockAndNotifyAdequately(world, i12, j1, k1, LOTRLegacyBlocks.mod("pillar"), 3);
                    }
                    continue;
                }
                setBlockAndNotifyAdequately(world, i12, j + 9, k1, LOTRLegacyBlocks.mod("wall"), 14);
            }
        }
        for (i1 = i - 5; i1 <= i + 5; ++i1) {
            setBlockAndNotifyAdequately(world, i1, j + 12, k - 5, LOTRLegacyBlocks.mod("stairsDwarvenBrick"), 2);
            setBlockAndNotifyAdequately(world, i1, j + 12, k + 5, LOTRLegacyBlocks.mod("stairsDwarvenBrick"), 3);
        }
        for (k1 = k - 5; k1 <= k + 5; ++k1) {
            setBlockAndNotifyAdequately(world, i - 5, j + 12, k1, LOTRLegacyBlocks.mod("stairsDwarvenBrick"), 0);
            setBlockAndNotifyAdequately(world, i + 5, j + 12, k1, LOTRLegacyBlocks.mod("stairsDwarvenBrick"), 1);
        }
        for (k1 = k - 4; k1 <= k + 4; ++k1) {
            for (i12 = i - 4; i12 <= i + 4; ++i12) {
                setBlockAndNotifyAdequately(world, i12, j + 12, k1, LOTRLegacyBlocks.mod("slabSingle"), 15);
            }
        }
        setBlockAndNotifyAdequately(world, i, j + 7, k, LOTRLegacyBlocks.mod("chandelier"), 11);
        setBlockAndNotifyAdequately(world, i, j + 11, k, LOTRLegacyBlocks.mod("chandelier"), 11);
        setBlockAndNotifyAdequately(world, i, j + 12, k, LOTRLegacyBlocks.mod("brick"), 6);
        switch (rotation) {
            case 0: {
                generateFacingSouth(world, random, i, j, k);
                break;
            }
            case 1: {
                generateFacingWest(world, random, i, j, k);
                break;
            }
            case 2: {
                generateFacingNorth(world, random, i, j, k);
                break;
            }
            case 3: {
                generateFacingEast(world, random, i, j, k);
            }
        }
        spawnDwarfCommander(world, i, j + 9, k);
        for (int l = 0; l < 4; ++l) {
            spawnDwarf(world, i, j + 5, k);
        }
        LOTRNPCRespawnerEntity respawner = create(LOTREntities.NPC_RESPAWNER, world);
        respawner.setSpawnClasses(LOTREntities.BLUE_DWARF_WARRIOR, LOTREntities.BLUE_DWARF_AXE_THROWER);
        respawner.setCheckRanges(8, -8, 16, 8);
        respawner.setSpawnRanges(8, 1, 10, 16);
        placeNPCRespawner(respawner, world, i, j, k);
        return true;
    }

    public void generateFacingEast(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        int j1;
        int j12;
        int j2;
        int k1;
        int k12;
        int k13;
        int i1;
        for (k13 = k - 6; k13 <= k + 6; ++k13) {
            setBlockAndNotifyAdequately(world, i - 7, j + 1, k13, LOTRLegacyBlocks.mod("slabSingle3"), 0);
            setGrassToDirt(world, i - 7, j, k13);
            for (j12 = j; !isOpaqueAt(world, i - 7, j12, k13) && j12 >= world.getMinY(); --j12) {
                setBlockAndNotifyAdequately(world, i - 7, j12, k13, LOTRLegacyBlocks.mod("pillar"), 3);
                setGrassToDirt(world, i - 7, j12 - 1, k13);
            }
        }
        for (j1 = j + 1; j1 <= j + 2; ++j1) {
            setAir(world, i - 6, j1, k);
            setBlockAndNotifyAdequately(world, i - 7, j1, k - 1, LOTRLegacyBlocks.mod("pillar"), 3);
            setBlockAndNotifyAdequately(world, i - 7, j1, k + 1, LOTRLegacyBlocks.mod("pillar"), 3);
        }
        setBlockAndNotifyAdequately(world, i - 7, j, k, LOTRLegacyBlocks.vanilla("planks"), 1);
        setBlockAndNotifyAdequately(world, i - 6, j, k, LOTRLegacyBlocks.vanilla("planks"), 1);
        setBlockAndNotifyAdequately(world, i - 7, j + 1, k, LOTRLegacyBlocks.mod("doorSpruce"), 0);
        setBlockAndNotifyAdequately(world, i - 7, j + 2, k, LOTRLegacyBlocks.mod("doorSpruce"), 8);
        setBlockAndNotifyAdequately(world, i - 7, j + 3, k - 1, LOTRLegacyBlocks.mod("stairsDwarvenBrick"), 2);
        setBlockAndNotifyAdequately(world, i - 7, j + 3, k, LOTRLegacyBlocks.mod("brick3"), 12);
        setBlockAndNotifyAdequately(world, i - 7, j + 3, k + 1, LOTRLegacyBlocks.mod("stairsDwarvenBrick"), 3);
        setBlockAndNotifyAdequately(world, i - 7, j + 4, k, LOTRLegacyBlocks.mod("slabSingle"), 7);
        placeWallBanner(world, i - 6, j + 6, k, 1, "BLUE_MOUNTAINS");
        for (j1 = j + 1; j1 <= j + 3; ++j1) {
            for (i1 = i - 4; i1 <= i - 1; ++i1) {
                for (k12 = k - 5; k12 <= k - 1; ++k12) {
                    setBlockAndNotifyAdequately(world, i1, j1, k12, LOTRLegacyBlocks.mod("brick"), 14);
                }
                for (k12 = k + 1; k12 <= k + 5; ++k12) {
                    setBlockAndNotifyAdequately(world, i1, j1, k12, LOTRLegacyBlocks.mod("brick"), 14);
                }
            }
            for (i1 = i - 2; i1 <= i + 5; ++i1) {
                setBlockAndNotifyAdequately(world, i1, j1, k - 1, LOTRLegacyBlocks.mod("brick"), 14);
                setBlockAndNotifyAdequately(world, i1, j1, k + 1, LOTRLegacyBlocks.mod("brick"), 14);
            }
        }
        setBlockAndNotifyAdequately(world, i + 3, j + 1, k - 1, LOTRLegacyBlocks.mod("doorSpruce"), 1);
        setBlockAndNotifyAdequately(world, i + 3, j + 2, k - 1, LOTRLegacyBlocks.mod("doorSpruce"), 8);
        setBlockAndNotifyAdequately(world, i + 3, j + 1, k + 1, LOTRLegacyBlocks.mod("doorSpruce"), 3);
        setBlockAndNotifyAdequately(world, i + 3, j + 2, k + 1, LOTRLegacyBlocks.mod("doorSpruce"), 8);
        for (k13 = k - 5; k13 <= k + 2; k13 += 7) {
            setBlockAndNotifyAdequately(world, i + 1, j + 1, k13, LOTRLegacyBlocks.mod("dwarvenBed"), 1);
            setBlockAndNotifyAdequately(world, i, j + 1, k13, LOTRLegacyBlocks.mod("dwarvenBed"), 9);
            setBlockAndNotifyAdequately(world, i + 1, j + 1, k13 + 3, LOTRLegacyBlocks.mod("dwarvenBed"), 1);
            setBlockAndNotifyAdequately(world, i, j + 1, k13 + 3, LOTRLegacyBlocks.mod("dwarvenBed"), 9);
            setBlockAndNotifyAdequately(world, i, j + 1, k13 + 1, LOTRLegacyBlocks.vanilla("chest"), 0);
            setBlockAndNotifyAdequately(world, i, j + 1, k13 + 2, LOTRLegacyBlocks.vanilla("chest"), 0);
            LOTRChestContents.fillChest(world, random, new BlockPos(i, j + 1, k13 + 1), LOTRChestContents.DWARF_HOUSE_LARDER, -1);
            LOTRChestContents.fillChest(world, random, new BlockPos(i, j + 1, k13 + 2), LOTRChestContents.BLUE_MOUNTAINS_STRONGHOLD, -1);
            setBlockAndNotifyAdequately(world, i + 3, j + 3, k13 + 1, LOTRLegacyBlocks.mod("chandelier"), 11);
            setBlockAndNotifyAdequately(world, i + 5, j + 1, k13, LOTRLegacyBlocks.vanilla("planks"), 1);
            setBlockAndNotifyAdequately(world, i + 5, j + 1, k13 + 3, LOTRLegacyBlocks.vanilla("planks"), 1);
            placeBarrel(world, random, i + 5, j + 2, k13, 4, LOTRFoods.DWARF_DRINK);
            placeBarrel(world, random, i + 5, j + 2, k13 + 3, 4, LOTRFoods.DWARF_DRINK);
            setBlockAndNotifyAdequately(world, i + 5, j + 1, k13 + 1, LOTRLegacyBlocks.vanilla("furnace"), 0);
            setBlockMetadata(world, i + 5, j + 1, k13 + 1, 4);
            setBlockAndNotifyAdequately(world, i + 5, j + 1, k13 + 2, LOTRLegacyBlocks.vanilla("furnace"), 0);
            setBlockMetadata(world, i + 5, j + 1, k13 + 2, 4);
        }
        setAir(world, i - 5, j + 4, k);
        int stairX = 1;
        for (j12 = j + 1; j12 <= j + 4; ++j12) {
            setAir(world, i - 5, j + 4, k - stairX);
            setAir(world, i - 5, j + 4, k + stairX);
            setBlockAndNotifyAdequately(world, i - 5, j12, k - stairX, LOTRLegacyBlocks.mod("stairsDwarvenBrick"), 3);
            setBlockAndNotifyAdequately(world, i - 5, j12, k + stairX, LOTRLegacyBlocks.mod("stairsDwarvenBrick"), 2);
            for (j2 = j12 - 1; j2 > j; --j2) {
                setBlockAndNotifyAdequately(world, i - 5, j2, k - stairX, LOTRLegacyBlocks.mod("brick"), 6);
                setBlockAndNotifyAdequately(world, i - 5, j2, k + stairX, LOTRLegacyBlocks.mod("brick"), 6);
            }
            ++stairX;
        }
        for (j12 = j + 1; j12 <= j + 3; ++j12) {
            setBlockAndNotifyAdequately(world, i - 5, j12, k - stairX, LOTRLegacyBlocks.mod("brick"), 6);
            setBlockAndNotifyAdequately(world, i - 5, j12, k + stairX, LOTRLegacyBlocks.mod("brick"), 6);
        }
        for (k1 = k - 5; k1 <= k + 5; k1 += 10) {
            setBlockAndNotifyAdequately(world, i - 2, j + 5, k1, LOTRLegacyBlocks.vanilla("planks"), 1);
            setBlockAndNotifyAdequately(world, i - 2, j + 6, k1, LOTRLegacyBlocks.vanilla("wooden_slab"), 1);
            setBlockAndNotifyAdequately(world, i + 2, j + 5, k1, LOTRLegacyBlocks.vanilla("planks"), 1);
            setBlockAndNotifyAdequately(world, i + 2, j + 6, k1, LOTRLegacyBlocks.vanilla("wooden_slab"), 1);
            setBlockAndNotifyAdequately(world, i, j + 5, k1, LOTRLegacyBlocks.mod("blueDwarvenTable"), 0);
            setBlockAndNotifyAdequately(world, i - 1, j + 5, k1, LOTRLegacyBlocks.mod("dwarvenForge"), 0);
            setBlockAndNotifyAdequately(world, i + 1, j + 5, k1, LOTRLegacyBlocks.mod("dwarvenForge"), 0);
        }
        setBlockAndNotifyAdequately(world, i + 6, j + 6, k - 3, LOTRLegacyBlocks.mod("brick3"), 12);
        setBlockAndNotifyAdequately(world, i + 6, j + 6, k + 3, LOTRLegacyBlocks.mod("brick3"), 12);
        stairX = 4;
        for (j12 = j + 5; j12 <= j + 8; ++j12) {
            setAir(world, i - 4, j + 8, k - stairX);
            setAir(world, i - 4, j + 8, k + stairX);
            setBlockAndNotifyAdequately(world, i - 4, j12, k - stairX, LOTRLegacyBlocks.mod("stairsDwarvenBrick"), 2);
            setBlockAndNotifyAdequately(world, i - 4, j12, k + stairX, LOTRLegacyBlocks.mod("stairsDwarvenBrick"), 3);
            for (j2 = j12 - 1; j2 > j + 4; --j2) {
                setBlockAndNotifyAdequately(world, i - 4, j2, k - stairX, LOTRLegacyBlocks.mod("brick"), 6);
                setBlockAndNotifyAdequately(world, i - 4, j2, k + stairX, LOTRLegacyBlocks.mod("brick"), 6);
            }
            --stairX;
        }
        for (j12 = j + 5; j12 <= j + 7; ++j12) {
            setBlockAndNotifyAdequately(world, i - 4, j12, k, LOTRLegacyBlocks.mod("brick"), 6);
        }
        setBlockAndNotifyAdequately(world, i - 4, j + 6, k, LOTRLegacyBlocks.mod("brick3"), 12);
        for (i1 = i + 7; i1 <= i + 8; ++i1) {
            for (k12 = k - 4; k12 <= k + 4; ++k12) {
                placeBalconySection(world, i1, j, k12, false, false);
            }
            placeBalconySection(world, i1, j, k - 5, true, false);
            placeBalconySection(world, i1, j, k + 5, true, false);
        }
        for (k1 = k - 2; k1 <= k + 2; ++k1) {
            placeBalconySection(world, i + 9, j, k1, false, false);
        }
        for (k1 = k - 5; k1 <= k + 5; ++k1) {
            if (Math.abs(k1 - k) < 3) {
                continue;
            }
            placeBalconySection(world, i + 9, j, k1, true, false);
        }
        for (k1 = k - 1; k1 <= k + 1; ++k1) {
            placeBalconySection(world, i + 10, j, k1, false, false);
        }
        for (k1 = k - 3; k1 <= k + 3; ++k1) {
            if (Math.abs(k1 - k) < 2) {
                continue;
            }
            placeBalconySection(world, i + 10, j, k1, true, false);
        }
        for (k1 = k - 2; k1 <= k + 2; ++k1) {
            if (Math.abs(k1 - k) == 0) {
                placeBalconySection(world, i + 11, j, k1, true, true);
                continue;
            }
            placeBalconySection(world, i + 11, j, k1, true, false);
        }
        setBlockAndNotifyAdequately(world, i + 6, j + 4, k, LOTRLegacyBlocks.mod("slabDouble3"), 0);
        setAir(world, i + 6, j + 5, k);
        setAir(world, i + 6, j + 6, k);
        setBlockAndNotifyAdequately(world, i + 6, j, k, LOTRLegacyBlocks.vanilla("planks"), 1);
        setBlockAndNotifyAdequately(world, i + 6, j + 1, k, LOTRLegacyBlocks.mod("doorSpruce"), 2);
        setBlockAndNotifyAdequately(world, i + 6, j + 2, k, LOTRLegacyBlocks.mod("doorSpruce"), 8);
        setBlockAndNotifyAdequately(world, i + 8, j + 3, k, LOTRLegacyBlocks.mod("chandelier"), 11);
        for (j12 = j + 1; j12 <= j + 2; ++j12) {
            for (int i12 = i + 7; i12 <= i + 8; ++i12) {
                placeRandomOre(world, random, i12, j12, k - 4);
                placeRandomOre(world, random, i12, j12, k - 3);
                placeRandomOre(world, random, i12, j12, k + 3);
                placeRandomOre(world, random, i12, j12, k + 4);
            }
            placeRandomOre(world, random, i + 9, j12, k - 2);
            placeRandomOre(world, random, i + 9, j12, k + 2);
            for (k12 = k - 1; k12 <= k + 1; ++k12) {
                placeRandomOre(world, random, i + 10, j12, k12);
            }
        }
        setBlockAndNotifyAdequately(world, i + 3, j + 9, k, LOTRLegacyBlocks.mod("commandTable"), 0);
    }

    public void generateFacingNorth(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        int j1;
        int j12;
        int j2;
        int k1;
        int i1;
        int i12;
        int i13;
        for (i12 = i - 6; i12 <= i + 6; ++i12) {
            setBlockAndNotifyAdequately(world, i12, j + 1, k + 7, LOTRLegacyBlocks.mod("slabSingle3"), 0);
            setGrassToDirt(world, i12, j, k + 7);
            for (j12 = j; !isOpaqueAt(world, i12, j12, k + 7) && j12 >= world.getMinY(); --j12) {
                setBlockAndNotifyAdequately(world, i12, j12, k + 7, LOTRLegacyBlocks.mod("pillar"), 3);
                setGrassToDirt(world, i12, j12 - 1, k + 7);
            }
        }
        for (j1 = j + 1; j1 <= j + 2; ++j1) {
            setAir(world, i, j1, k + 6);
            setBlockAndNotifyAdequately(world, i - 1, j1, k + 7, LOTRLegacyBlocks.mod("pillar"), 3);
            setBlockAndNotifyAdequately(world, i + 1, j1, k + 7, LOTRLegacyBlocks.mod("pillar"), 3);
        }
        setBlockAndNotifyAdequately(world, i, j, k + 7, LOTRLegacyBlocks.vanilla("planks"), 1);
        setBlockAndNotifyAdequately(world, i, j, k + 6, LOTRLegacyBlocks.vanilla("planks"), 1);
        setBlockAndNotifyAdequately(world, i, j + 1, k + 7, LOTRLegacyBlocks.mod("doorSpruce"), 3);
        setBlockAndNotifyAdequately(world, i, j + 2, k + 7, LOTRLegacyBlocks.mod("doorSpruce"), 8);
        setBlockAndNotifyAdequately(world, i - 1, j + 3, k + 7, LOTRLegacyBlocks.mod("stairsDwarvenBrick"), 0);
        setBlockAndNotifyAdequately(world, i, j + 3, k + 7, LOTRLegacyBlocks.mod("brick3"), 12);
        setBlockAndNotifyAdequately(world, i + 1, j + 3, k + 7, LOTRLegacyBlocks.mod("stairsDwarvenBrick"), 1);
        setBlockAndNotifyAdequately(world, i, j + 4, k + 7, LOTRLegacyBlocks.mod("slabSingle"), 7);
        placeWallBanner(world, i, j + 6, k + 6, 0, "BLUE_MOUNTAINS");
        for (j1 = j + 1; j1 <= j + 3; ++j1) {
            for (k1 = k + 4; k1 >= k + 1; --k1) {
                for (i1 = i - 5; i1 <= i - 1; ++i1) {
                    setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRLegacyBlocks.mod("brick"), 14);
                }
                for (i1 = i + 1; i1 <= i + 5; ++i1) {
                    setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRLegacyBlocks.mod("brick"), 14);
                }
            }
            for (k1 = k + 2; k1 >= k - 5; --k1) {
                setBlockAndNotifyAdequately(world, i - 1, j1, k1, LOTRLegacyBlocks.mod("brick"), 14);
                setBlockAndNotifyAdequately(world, i + 1, j1, k1, LOTRLegacyBlocks.mod("brick"), 14);
            }
        }
        setBlockAndNotifyAdequately(world, i - 1, j + 1, k - 3, LOTRLegacyBlocks.mod("doorSpruce"), 0);
        setBlockAndNotifyAdequately(world, i - 1, j + 2, k - 3, LOTRLegacyBlocks.mod("doorSpruce"), 8);
        setBlockAndNotifyAdequately(world, i + 1, j + 1, k - 3, LOTRLegacyBlocks.mod("doorSpruce"), 2);
        setBlockAndNotifyAdequately(world, i + 1, j + 2, k - 3, LOTRLegacyBlocks.mod("doorSpruce"), 8);
        for (i12 = i - 5; i12 <= i + 2; i12 += 7) {
            setBlockAndNotifyAdequately(world, i12, j + 1, k - 1, LOTRLegacyBlocks.mod("dwarvenBed"), 0);
            setBlockAndNotifyAdequately(world, i12, j + 1, k, LOTRLegacyBlocks.mod("dwarvenBed"), 8);
            setBlockAndNotifyAdequately(world, i12 + 3, j + 1, k - 1, LOTRLegacyBlocks.mod("dwarvenBed"), 0);
            setBlockAndNotifyAdequately(world, i12 + 3, j + 1, k, LOTRLegacyBlocks.mod("dwarvenBed"), 8);
            setBlockAndNotifyAdequately(world, i12 + 1, j + 1, k, LOTRLegacyBlocks.vanilla("chest"), 0);
            setBlockAndNotifyAdequately(world, i12 + 2, j + 1, k, LOTRLegacyBlocks.vanilla("chest"), 0);
            LOTRChestContents.fillChest(world, random, new BlockPos(i12 + 1, j + 1, k), LOTRChestContents.DWARF_HOUSE_LARDER, -1);
            LOTRChestContents.fillChest(world, random, new BlockPos(i12 + 2, j + 1, k), LOTRChestContents.BLUE_MOUNTAINS_STRONGHOLD, -1);
            setBlockAndNotifyAdequately(world, i12 + 1, j + 3, k - 3, LOTRLegacyBlocks.mod("chandelier"), 11);
            setBlockAndNotifyAdequately(world, i12, j + 1, k - 5, LOTRLegacyBlocks.vanilla("planks"), 1);
            setBlockAndNotifyAdequately(world, i12 + 3, j + 1, k - 5, LOTRLegacyBlocks.vanilla("planks"), 1);
            placeBarrel(world, random, i12, j + 2, k - 5, 3, LOTRFoods.DWARF_DRINK);
            placeBarrel(world, random, i12 + 3, j + 2, k - 5, 3, LOTRFoods.DWARF_DRINK);
            setBlockAndNotifyAdequately(world, i12 + 1, j + 1, k - 5, LOTRLegacyBlocks.vanilla("furnace"), 0);
            setBlockMetadata(world, i12 + 1, j + 1, k - 5, 3);
            setBlockAndNotifyAdequately(world, i12 + 2, j + 1, k - 5, LOTRLegacyBlocks.vanilla("furnace"), 0);
            setBlockMetadata(world, i12 + 2, j + 1, k - 5, 3);
        }
        setAir(world, i, j + 4, k + 5);
        int stairX = 1;
        for (j12 = j + 1; j12 <= j + 4; ++j12) {
            setAir(world, i - stairX, j + 4, k + 5);
            setAir(world, i + stairX, j + 4, k + 5);
            setBlockAndNotifyAdequately(world, i - stairX, j12, k + 5, LOTRLegacyBlocks.mod("stairsDwarvenBrick"), 1);
            setBlockAndNotifyAdequately(world, i + stairX, j12, k + 5, LOTRLegacyBlocks.mod("stairsDwarvenBrick"), 0);
            for (j2 = j12 - 1; j2 > j; --j2) {
                setBlockAndNotifyAdequately(world, i - stairX, j2, k + 5, LOTRLegacyBlocks.mod("brick"), 6);
                setBlockAndNotifyAdequately(world, i + stairX, j2, k + 5, LOTRLegacyBlocks.mod("brick"), 6);
            }
            ++stairX;
        }
        for (j12 = j + 1; j12 <= j + 3; ++j12) {
            setBlockAndNotifyAdequately(world, i - stairX, j12, k + 5, LOTRLegacyBlocks.mod("brick"), 6);
            setBlockAndNotifyAdequately(world, i + stairX, j12, k + 5, LOTRLegacyBlocks.mod("brick"), 6);
        }
        for (i13 = i - 5; i13 <= i + 5; i13 += 10) {
            setBlockAndNotifyAdequately(world, i13, j + 5, k + 2, LOTRLegacyBlocks.vanilla("planks"), 1);
            setBlockAndNotifyAdequately(world, i13, j + 6, k + 2, LOTRLegacyBlocks.vanilla("wooden_slab"), 1);
            setBlockAndNotifyAdequately(world, i13, j + 5, k - 2, LOTRLegacyBlocks.vanilla("planks"), 1);
            setBlockAndNotifyAdequately(world, i13, j + 6, k - 2, LOTRLegacyBlocks.vanilla("wooden_slab"), 1);
            setBlockAndNotifyAdequately(world, i13, j + 5, k, LOTRLegacyBlocks.mod("blueDwarvenTable"), 0);
            setBlockAndNotifyAdequately(world, i13, j + 5, k + 1, LOTRLegacyBlocks.mod("dwarvenForge"), 0);
            setBlockAndNotifyAdequately(world, i13, j + 5, k - 1, LOTRLegacyBlocks.mod("dwarvenForge"), 0);
        }
        setBlockAndNotifyAdequately(world, i - 3, j + 6, k - 6, LOTRLegacyBlocks.mod("brick3"), 12);
        setBlockAndNotifyAdequately(world, i + 3, j + 6, k - 6, LOTRLegacyBlocks.mod("brick3"), 12);
        stairX = 4;
        for (j12 = j + 5; j12 <= j + 8; ++j12) {
            setAir(world, i - stairX, j + 8, k + 4);
            setAir(world, i + stairX, j + 8, k + 4);
            setBlockAndNotifyAdequately(world, i - stairX, j12, k + 4, LOTRLegacyBlocks.mod("stairsDwarvenBrick"), 0);
            setBlockAndNotifyAdequately(world, i + stairX, j12, k + 4, LOTRLegacyBlocks.mod("stairsDwarvenBrick"), 1);
            for (j2 = j12 - 1; j2 > j + 4; --j2) {
                setBlockAndNotifyAdequately(world, i - stairX, j2, k + 4, LOTRLegacyBlocks.mod("brick"), 6);
                setBlockAndNotifyAdequately(world, i + stairX, j2, k + 4, LOTRLegacyBlocks.mod("brick"), 6);
            }
            --stairX;
        }
        for (j12 = j + 5; j12 <= j + 7; ++j12) {
            setBlockAndNotifyAdequately(world, i, j12, k + 4, LOTRLegacyBlocks.mod("brick"), 6);
        }
        setBlockAndNotifyAdequately(world, i, j + 6, k + 4, LOTRLegacyBlocks.mod("brick3"), 12);
        for (k1 = k - 7; k1 >= k - 8; --k1) {
            for (i1 = i - 4; i1 <= i + 4; ++i1) {
                placeBalconySection(world, i1, j, k1, false, false);
            }
            placeBalconySection(world, i - 5, j, k1, true, false);
            placeBalconySection(world, i + 5, j, k1, true, false);
        }
        for (i13 = i - 2; i13 <= i + 2; ++i13) {
            placeBalconySection(world, i13, j, k - 9, false, false);
        }
        for (i13 = i - 5; i13 <= i + 5; ++i13) {
            if (Math.abs(i13 - i) < 3) {
                continue;
            }
            placeBalconySection(world, i13, j, k - 9, true, false);
        }
        for (i13 = i - 1; i13 <= i + 1; ++i13) {
            placeBalconySection(world, i13, j, k - 10, false, false);
        }
        for (i13 = i - 3; i13 <= i + 3; ++i13) {
            if (Math.abs(i13 - i) < 2) {
                continue;
            }
            placeBalconySection(world, i13, j, k - 10, true, false);
        }
        for (i13 = i - 2; i13 <= i + 2; ++i13) {
            if (Math.abs(i13 - i) == 0) {
                placeBalconySection(world, i13, j, k - 11, true, true);
                continue;
            }
            placeBalconySection(world, i13, j, k - 11, true, false);
        }
        setBlockAndNotifyAdequately(world, i, j + 4, k - 6, LOTRLegacyBlocks.mod("slabDouble3"), 0);
        setAir(world, i, j + 5, k - 6);
        setAir(world, i, j + 6, k - 6);
        setBlockAndNotifyAdequately(world, i, j, k - 6, LOTRLegacyBlocks.vanilla("planks"), 1);
        setBlockAndNotifyAdequately(world, i, j + 1, k - 6, LOTRLegacyBlocks.mod("doorSpruce"), 1);
        setBlockAndNotifyAdequately(world, i, j + 2, k - 6, LOTRLegacyBlocks.mod("doorSpruce"), 8);
        setBlockAndNotifyAdequately(world, i, j + 3, k - 8, LOTRLegacyBlocks.mod("chandelier"), 11);
        for (j12 = j + 1; j12 <= j + 2; ++j12) {
            for (int k12 = k - 7; k12 >= k - 8; --k12) {
                placeRandomOre(world, random, i - 4, j12, k12);
                placeRandomOre(world, random, i - 3, j12, k12);
                placeRandomOre(world, random, i + 3, j12, k12);
                placeRandomOre(world, random, i + 4, j12, k12);
            }
            placeRandomOre(world, random, i - 2, j12, k - 9);
            placeRandomOre(world, random, i + 2, j12, k - 9);
            for (i1 = i - 1; i1 <= i + 1; ++i1) {
                placeRandomOre(world, random, i1, j12, k - 10);
            }
        }
        setBlockAndNotifyAdequately(world, i, j + 9, k - 3, LOTRLegacyBlocks.mod("commandTable"), 0);
    }

    public void generateFacingSouth(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        int j1;
        int j12;
        int j2;
        int k1;
        int i1;
        int i12;
        int i13;
        for (i12 = i - 6; i12 <= i + 6; ++i12) {
            setBlockAndNotifyAdequately(world, i12, j + 1, k - 7, LOTRLegacyBlocks.mod("slabSingle3"), 0);
            setGrassToDirt(world, i12, j, k - 7);
            for (j12 = j; !isOpaqueAt(world, i12, j12, k - 7) && j12 >= world.getMinY(); --j12) {
                setBlockAndNotifyAdequately(world, i12, j12, k - 7, LOTRLegacyBlocks.mod("pillar"), 3);
                setGrassToDirt(world, i12, j12 - 1, k - 7);
            }
        }
        for (j1 = j + 1; j1 <= j + 2; ++j1) {
            setAir(world, i, j1, k - 6);
            setBlockAndNotifyAdequately(world, i - 1, j1, k - 7, LOTRLegacyBlocks.mod("pillar"), 3);
            setBlockAndNotifyAdequately(world, i + 1, j1, k - 7, LOTRLegacyBlocks.mod("pillar"), 3);
        }
        setBlockAndNotifyAdequately(world, i, j, k - 7, LOTRLegacyBlocks.vanilla("planks"), 1);
        setBlockAndNotifyAdequately(world, i, j, k - 6, LOTRLegacyBlocks.vanilla("planks"), 1);
        setBlockAndNotifyAdequately(world, i, j + 1, k - 7, LOTRLegacyBlocks.mod("doorSpruce"), 1);
        setBlockAndNotifyAdequately(world, i, j + 2, k - 7, LOTRLegacyBlocks.mod("doorSpruce"), 8);
        setBlockAndNotifyAdequately(world, i - 1, j + 3, k - 7, LOTRLegacyBlocks.mod("stairsDwarvenBrick"), 0);
        setBlockAndNotifyAdequately(world, i, j + 3, k - 7, LOTRLegacyBlocks.mod("brick3"), 12);
        setBlockAndNotifyAdequately(world, i + 1, j + 3, k - 7, LOTRLegacyBlocks.mod("stairsDwarvenBrick"), 1);
        setBlockAndNotifyAdequately(world, i, j + 4, k - 7, LOTRLegacyBlocks.mod("slabSingle"), 7);
        placeWallBanner(world, i, j + 6, k - 6, 2, "BLUE_MOUNTAINS");
        for (j1 = j + 1; j1 <= j + 3; ++j1) {
            for (k1 = k - 4; k1 <= k - 1; ++k1) {
                for (i1 = i - 5; i1 <= i - 1; ++i1) {
                    setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRLegacyBlocks.mod("brick"), 14);
                }
                for (i1 = i + 1; i1 <= i + 5; ++i1) {
                    setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRLegacyBlocks.mod("brick"), 14);
                }
            }
            for (k1 = k - 2; k1 <= k + 5; ++k1) {
                setBlockAndNotifyAdequately(world, i - 1, j1, k1, LOTRLegacyBlocks.mod("brick"), 14);
                setBlockAndNotifyAdequately(world, i + 1, j1, k1, LOTRLegacyBlocks.mod("brick"), 14);
            }
        }
        setBlockAndNotifyAdequately(world, i - 1, j + 1, k + 3, LOTRLegacyBlocks.mod("doorSpruce"), 0);
        setBlockAndNotifyAdequately(world, i - 1, j + 2, k + 3, LOTRLegacyBlocks.mod("doorSpruce"), 8);
        setBlockAndNotifyAdequately(world, i + 1, j + 1, k + 3, LOTRLegacyBlocks.mod("doorSpruce"), 2);
        setBlockAndNotifyAdequately(world, i + 1, j + 2, k + 3, LOTRLegacyBlocks.mod("doorSpruce"), 8);
        for (i12 = i - 5; i12 <= i + 2; i12 += 7) {
            setBlockAndNotifyAdequately(world, i12, j + 1, k + 1, LOTRLegacyBlocks.mod("dwarvenBed"), 2);
            setBlockAndNotifyAdequately(world, i12, j + 1, k, LOTRLegacyBlocks.mod("dwarvenBed"), 10);
            setBlockAndNotifyAdequately(world, i12 + 3, j + 1, k + 1, LOTRLegacyBlocks.mod("dwarvenBed"), 2);
            setBlockAndNotifyAdequately(world, i12 + 3, j + 1, k, LOTRLegacyBlocks.mod("dwarvenBed"), 10);
            setBlockAndNotifyAdequately(world, i12 + 1, j + 1, k, LOTRLegacyBlocks.vanilla("chest"), 0);
            setBlockAndNotifyAdequately(world, i12 + 2, j + 1, k, LOTRLegacyBlocks.vanilla("chest"), 0);
            LOTRChestContents.fillChest(world, random, new BlockPos(i12 + 1, j + 1, k), LOTRChestContents.DWARF_HOUSE_LARDER, -1);
            LOTRChestContents.fillChest(world, random, new BlockPos(i12 + 2, j + 1, k), LOTRChestContents.BLUE_MOUNTAINS_STRONGHOLD, -1);
            setBlockAndNotifyAdequately(world, i12 + 1, j + 3, k + 3, LOTRLegacyBlocks.mod("chandelier"), 11);
            setBlockAndNotifyAdequately(world, i12, j + 1, k + 5, LOTRLegacyBlocks.vanilla("planks"), 1);
            setBlockAndNotifyAdequately(world, i12 + 3, j + 1, k + 5, LOTRLegacyBlocks.vanilla("planks"), 1);
            placeBarrel(world, random, i12, j + 2, k + 5, 2, LOTRFoods.DWARF_DRINK);
            placeBarrel(world, random, i12 + 3, j + 2, k + 5, 2, LOTRFoods.DWARF_DRINK);
            setBlockAndNotifyAdequately(world, i12 + 1, j + 1, k + 5, LOTRLegacyBlocks.vanilla("furnace"), 0);
            setBlockMetadata(world, i12 + 1, j + 1, k + 5, 2);
            setBlockAndNotifyAdequately(world, i12 + 2, j + 1, k + 5, LOTRLegacyBlocks.vanilla("furnace"), 0);
            setBlockMetadata(world, i12 + 2, j + 1, k + 5, 2);
        }
        setAir(world, i, j + 4, k - 5);
        int stairX = 1;
        for (j12 = j + 1; j12 <= j + 4; ++j12) {
            setAir(world, i - stairX, j + 4, k - 5);
            setAir(world, i + stairX, j + 4, k - 5);
            setBlockAndNotifyAdequately(world, i - stairX, j12, k - 5, LOTRLegacyBlocks.mod("stairsDwarvenBrick"), 1);
            setBlockAndNotifyAdequately(world, i + stairX, j12, k - 5, LOTRLegacyBlocks.mod("stairsDwarvenBrick"), 0);
            for (j2 = j12 - 1; j2 > j; --j2) {
                setBlockAndNotifyAdequately(world, i - stairX, j2, k - 5, LOTRLegacyBlocks.mod("brick"), 6);
                setBlockAndNotifyAdequately(world, i + stairX, j2, k - 5, LOTRLegacyBlocks.mod("brick"), 6);
            }
            ++stairX;
        }
        for (j12 = j + 1; j12 <= j + 3; ++j12) {
            setBlockAndNotifyAdequately(world, i - stairX, j12, k - 5, LOTRLegacyBlocks.mod("brick"), 6);
            setBlockAndNotifyAdequately(world, i + stairX, j12, k - 5, LOTRLegacyBlocks.mod("brick"), 6);
        }
        for (i13 = i - 5; i13 <= i + 5; i13 += 10) {
            setBlockAndNotifyAdequately(world, i13, j + 5, k - 2, LOTRLegacyBlocks.vanilla("planks"), 1);
            setBlockAndNotifyAdequately(world, i13, j + 6, k - 2, LOTRLegacyBlocks.vanilla("wooden_slab"), 1);
            setBlockAndNotifyAdequately(world, i13, j + 5, k + 2, LOTRLegacyBlocks.vanilla("planks"), 1);
            setBlockAndNotifyAdequately(world, i13, j + 6, k + 2, LOTRLegacyBlocks.vanilla("wooden_slab"), 1);
            setBlockAndNotifyAdequately(world, i13, j + 5, k, LOTRLegacyBlocks.mod("blueDwarvenTable"), 0);
            setBlockAndNotifyAdequately(world, i13, j + 5, k - 1, LOTRLegacyBlocks.mod("dwarvenForge"), 0);
            setBlockAndNotifyAdequately(world, i13, j + 5, k + 1, LOTRLegacyBlocks.mod("dwarvenForge"), 0);
        }
        setBlockAndNotifyAdequately(world, i - 3, j + 6, k + 6, LOTRLegacyBlocks.mod("brick3"), 12);
        setBlockAndNotifyAdequately(world, i + 3, j + 6, k + 6, LOTRLegacyBlocks.mod("brick3"), 12);
        stairX = 4;
        for (j12 = j + 5; j12 <= j + 8; ++j12) {
            setAir(world, i - stairX, j + 8, k - 4);
            setAir(world, i + stairX, j + 8, k - 4);
            setBlockAndNotifyAdequately(world, i - stairX, j12, k - 4, LOTRLegacyBlocks.mod("stairsDwarvenBrick"), 0);
            setBlockAndNotifyAdequately(world, i + stairX, j12, k - 4, LOTRLegacyBlocks.mod("stairsDwarvenBrick"), 1);
            for (j2 = j12 - 1; j2 > j + 4; --j2) {
                setBlockAndNotifyAdequately(world, i - stairX, j2, k - 4, LOTRLegacyBlocks.mod("brick"), 6);
                setBlockAndNotifyAdequately(world, i + stairX, j2, k - 4, LOTRLegacyBlocks.mod("brick"), 6);
            }
            --stairX;
        }
        for (j12 = j + 5; j12 <= j + 7; ++j12) {
            setBlockAndNotifyAdequately(world, i, j12, k - 4, LOTRLegacyBlocks.mod("brick"), 6);
        }
        setBlockAndNotifyAdequately(world, i, j + 6, k - 4, LOTRLegacyBlocks.mod("brick3"), 12);
        for (k1 = k + 7; k1 <= k + 8; ++k1) {
            for (i1 = i - 4; i1 <= i + 4; ++i1) {
                placeBalconySection(world, i1, j, k1, false, false);
            }
            placeBalconySection(world, i - 5, j, k1, true, false);
            placeBalconySection(world, i + 5, j, k1, true, false);
        }
        for (i13 = i - 2; i13 <= i + 2; ++i13) {
            placeBalconySection(world, i13, j, k + 9, false, false);
        }
        for (i13 = i - 5; i13 <= i + 5; ++i13) {
            if (Math.abs(i13 - i) < 3) {
                continue;
            }
            placeBalconySection(world, i13, j, k + 9, true, false);
        }
        for (i13 = i - 1; i13 <= i + 1; ++i13) {
            placeBalconySection(world, i13, j, k + 10, false, false);
        }
        for (i13 = i - 3; i13 <= i + 3; ++i13) {
            if (Math.abs(i13 - i) < 2) {
                continue;
            }
            placeBalconySection(world, i13, j, k + 10, true, false);
        }
        for (i13 = i - 2; i13 <= i + 2; ++i13) {
            if (Math.abs(i13 - i) == 0) {
                placeBalconySection(world, i13, j, k + 11, true, true);
                continue;
            }
            placeBalconySection(world, i13, j, k + 11, true, false);
        }
        setBlockAndNotifyAdequately(world, i, j + 4, k + 6, LOTRLegacyBlocks.mod("slabDouble3"), 0);
        setAir(world, i, j + 5, k + 6);
        setAir(world, i, j + 6, k + 6);
        setBlockAndNotifyAdequately(world, i, j, k + 6, LOTRLegacyBlocks.vanilla("planks"), 1);
        setBlockAndNotifyAdequately(world, i, j + 1, k + 6, LOTRLegacyBlocks.mod("doorSpruce"), 3);
        setBlockAndNotifyAdequately(world, i, j + 2, k + 6, LOTRLegacyBlocks.mod("doorSpruce"), 8);
        setBlockAndNotifyAdequately(world, i, j + 3, k + 8, LOTRLegacyBlocks.mod("chandelier"), 11);
        for (j12 = j + 1; j12 <= j + 2; ++j12) {
            for (int k12 = k + 7; k12 <= k + 8; ++k12) {
                placeRandomOre(world, random, i - 4, j12, k12);
                placeRandomOre(world, random, i - 3, j12, k12);
                placeRandomOre(world, random, i + 3, j12, k12);
                placeRandomOre(world, random, i + 4, j12, k12);
            }
            placeRandomOre(world, random, i - 2, j12, k + 9);
            placeRandomOre(world, random, i + 2, j12, k + 9);
            for (i1 = i - 1; i1 <= i + 1; ++i1) {
                placeRandomOre(world, random, i1, j12, k + 10);
            }
        }
        setBlockAndNotifyAdequately(world, i, j + 9, k + 3, LOTRLegacyBlocks.mod("commandTable"), 0);
    }

    public void generateFacingWest(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        int j1;
        int j12;
        int j2;
        int k1;
        int k12;
        int k13;
        int i1;
        for (k13 = k - 6; k13 <= k + 6; ++k13) {
            setBlockAndNotifyAdequately(world, i + 7, j + 1, k13, LOTRLegacyBlocks.mod("slabSingle3"), 0);
            setGrassToDirt(world, i + 7, j, k13);
            for (j12 = j; !isOpaqueAt(world, i + 7, j12, k13) && j12 >= world.getMinY(); --j12) {
                setBlockAndNotifyAdequately(world, i + 7, j12, k13, LOTRLegacyBlocks.mod("pillar"), 3);
                setGrassToDirt(world, i + 7, j12 - 1, k13);
            }
        }
        for (j1 = j + 1; j1 <= j + 2; ++j1) {
            setAir(world, i + 6, j1, k);
            setBlockAndNotifyAdequately(world, i + 7, j1, k - 1, LOTRLegacyBlocks.mod("pillar"), 3);
            setBlockAndNotifyAdequately(world, i + 7, j1, k + 1, LOTRLegacyBlocks.mod("pillar"), 3);
        }
        setBlockAndNotifyAdequately(world, i + 7, j, k, LOTRLegacyBlocks.vanilla("planks"), 1);
        setBlockAndNotifyAdequately(world, i + 6, j, k, LOTRLegacyBlocks.vanilla("planks"), 1);
        setBlockAndNotifyAdequately(world, i + 7, j + 1, k, LOTRLegacyBlocks.mod("doorSpruce"), 2);
        setBlockAndNotifyAdequately(world, i + 7, j + 2, k, LOTRLegacyBlocks.mod("doorSpruce"), 8);
        setBlockAndNotifyAdequately(world, i + 7, j + 3, k - 1, LOTRLegacyBlocks.mod("stairsDwarvenBrick"), 2);
        setBlockAndNotifyAdequately(world, i + 7, j + 3, k, LOTRLegacyBlocks.mod("brick3"), 12);
        setBlockAndNotifyAdequately(world, i + 7, j + 3, k + 1, LOTRLegacyBlocks.mod("stairsDwarvenBrick"), 3);
        setBlockAndNotifyAdequately(world, i + 7, j + 4, k, LOTRLegacyBlocks.mod("slabSingle"), 7);
        placeWallBanner(world, i + 6, j + 6, k, 3, "BLUE_MOUNTAINS");
        for (j1 = j + 1; j1 <= j + 3; ++j1) {
            for (i1 = i + 4; i1 >= i + 1; --i1) {
                for (k12 = k - 5; k12 <= k - 1; ++k12) {
                    setBlockAndNotifyAdequately(world, i1, j1, k12, LOTRLegacyBlocks.mod("brick"), 14);
                }
                for (k12 = k + 1; k12 <= k + 5; ++k12) {
                    setBlockAndNotifyAdequately(world, i1, j1, k12, LOTRLegacyBlocks.mod("brick"), 14);
                }
            }
            for (i1 = i + 2; i1 >= i - 5; --i1) {
                setBlockAndNotifyAdequately(world, i1, j1, k - 1, LOTRLegacyBlocks.mod("brick"), 14);
                setBlockAndNotifyAdequately(world, i1, j1, k + 1, LOTRLegacyBlocks.mod("brick"), 14);
            }
        }
        setBlockAndNotifyAdequately(world, i - 3, j + 1, k - 1, LOTRLegacyBlocks.mod("doorSpruce"), 1);
        setBlockAndNotifyAdequately(world, i - 3, j + 2, k - 1, LOTRLegacyBlocks.mod("doorSpruce"), 8);
        setBlockAndNotifyAdequately(world, i - 3, j + 1, k + 1, LOTRLegacyBlocks.mod("doorSpruce"), 3);
        setBlockAndNotifyAdequately(world, i - 3, j + 2, k + 1, LOTRLegacyBlocks.mod("doorSpruce"), 8);
        for (k13 = k - 5; k13 <= k + 2; k13 += 7) {
            setBlockAndNotifyAdequately(world, i - 1, j + 1, k13, LOTRLegacyBlocks.mod("dwarvenBed"), 3);
            setBlockAndNotifyAdequately(world, i, j + 1, k13, LOTRLegacyBlocks.mod("dwarvenBed"), 11);
            setBlockAndNotifyAdequately(world, i - 1, j + 1, k13 + 3, LOTRLegacyBlocks.mod("dwarvenBed"), 3);
            setBlockAndNotifyAdequately(world, i, j + 1, k13 + 3, LOTRLegacyBlocks.mod("dwarvenBed"), 11);
            setBlockAndNotifyAdequately(world, i, j + 1, k13 + 1, LOTRLegacyBlocks.vanilla("chest"), 0);
            setBlockAndNotifyAdequately(world, i, j + 1, k13 + 2, LOTRLegacyBlocks.vanilla("chest"), 0);
            LOTRChestContents.fillChest(world, random, new BlockPos(i, j + 1, k13 + 1), LOTRChestContents.DWARF_HOUSE_LARDER, -1);
            LOTRChestContents.fillChest(world, random, new BlockPos(i, j + 1, k13 + 2), LOTRChestContents.BLUE_MOUNTAINS_STRONGHOLD, -1);
            setBlockAndNotifyAdequately(world, i - 3, j + 3, k13 + 1, LOTRLegacyBlocks.mod("chandelier"), 11);
            setBlockAndNotifyAdequately(world, i - 5, j + 1, k13, LOTRLegacyBlocks.vanilla("planks"), 1);
            setBlockAndNotifyAdequately(world, i - 5, j + 1, k13 + 3, LOTRLegacyBlocks.vanilla("planks"), 1);
            placeBarrel(world, random, i - 5, j + 2, k13, 5, LOTRFoods.DWARF_DRINK);
            placeBarrel(world, random, i - 5, j + 2, k13 + 3, 5, LOTRFoods.DWARF_DRINK);
            setBlockAndNotifyAdequately(world, i - 5, j + 1, k13 + 1, LOTRLegacyBlocks.vanilla("furnace"), 0);
            setBlockMetadata(world, i - 5, j + 1, k13 + 1, 5);
            setBlockAndNotifyAdequately(world, i - 5, j + 1, k13 + 2, LOTRLegacyBlocks.vanilla("furnace"), 0);
            setBlockMetadata(world, i - 5, j + 1, k13 + 2, 5);
        }
        setAir(world, i + 5, j + 4, k);
        int stairX = 1;
        for (j12 = j + 1; j12 <= j + 4; ++j12) {
            setAir(world, i + 5, j + 4, k - stairX);
            setAir(world, i + 5, j + 4, k + stairX);
            setBlockAndNotifyAdequately(world, i + 5, j12, k - stairX, LOTRLegacyBlocks.mod("stairsDwarvenBrick"), 3);
            setBlockAndNotifyAdequately(world, i + 5, j12, k + stairX, LOTRLegacyBlocks.mod("stairsDwarvenBrick"), 2);
            for (j2 = j12 - 1; j2 > j; --j2) {
                setBlockAndNotifyAdequately(world, i + 5, j2, k - stairX, LOTRLegacyBlocks.mod("brick"), 6);
                setBlockAndNotifyAdequately(world, i + 5, j2, k + stairX, LOTRLegacyBlocks.mod("brick"), 6);
            }
            ++stairX;
        }
        for (j12 = j + 1; j12 <= j + 3; ++j12) {
            setBlockAndNotifyAdequately(world, i + 5, j12, k - stairX, LOTRLegacyBlocks.mod("brick"), 6);
            setBlockAndNotifyAdequately(world, i + 5, j12, k + stairX, LOTRLegacyBlocks.mod("brick"), 6);
        }
        for (k1 = k - 5; k1 <= k + 5; k1 += 10) {
            setBlockAndNotifyAdequately(world, i - 2, j + 5, k1, LOTRLegacyBlocks.vanilla("planks"), 1);
            setBlockAndNotifyAdequately(world, i - 2, j + 6, k1, LOTRLegacyBlocks.vanilla("wooden_slab"), 1);
            setBlockAndNotifyAdequately(world, i + 2, j + 5, k1, LOTRLegacyBlocks.vanilla("planks"), 1);
            setBlockAndNotifyAdequately(world, i + 2, j + 6, k1, LOTRLegacyBlocks.vanilla("wooden_slab"), 1);
            setBlockAndNotifyAdequately(world, i, j + 5, k1, LOTRLegacyBlocks.mod("blueDwarvenTable"), 0);
            setBlockAndNotifyAdequately(world, i - 1, j + 5, k1, LOTRLegacyBlocks.mod("dwarvenForge"), 0);
            setBlockAndNotifyAdequately(world, i + 1, j + 5, k1, LOTRLegacyBlocks.mod("dwarvenForge"), 0);
        }
        setBlockAndNotifyAdequately(world, i - 6, j + 6, k - 3, LOTRLegacyBlocks.mod("brick3"), 12);
        setBlockAndNotifyAdequately(world, i - 6, j + 6, k + 3, LOTRLegacyBlocks.mod("brick3"), 12);
        stairX = 4;
        for (j12 = j + 5; j12 <= j + 8; ++j12) {
            setAir(world, i + 4, j + 8, k - stairX);
            setAir(world, i + 4, j + 8, k + stairX);
            setBlockAndNotifyAdequately(world, i + 4, j12, k - stairX, LOTRLegacyBlocks.mod("stairsDwarvenBrick"), 2);
            setBlockAndNotifyAdequately(world, i + 4, j12, k + stairX, LOTRLegacyBlocks.mod("stairsDwarvenBrick"), 3);
            for (j2 = j12 - 1; j2 > j + 4; --j2) {
                setBlockAndNotifyAdequately(world, i + 4, j2, k - stairX, LOTRLegacyBlocks.mod("brick"), 6);
                setBlockAndNotifyAdequately(world, i + 4, j2, k + stairX, LOTRLegacyBlocks.mod("brick"), 6);
            }
            --stairX;
        }
        for (j12 = j + 5; j12 <= j + 7; ++j12) {
            setBlockAndNotifyAdequately(world, i + 4, j12, k, LOTRLegacyBlocks.mod("brick"), 6);
        }
        setBlockAndNotifyAdequately(world, i + 4, j + 6, k, LOTRLegacyBlocks.mod("brick3"), 12);
        for (i1 = i - 7; i1 >= i - 8; --i1) {
            for (k12 = k - 4; k12 <= k + 4; ++k12) {
                placeBalconySection(world, i1, j, k12, false, false);
            }
            placeBalconySection(world, i1, j, k - 5, true, false);
            placeBalconySection(world, i1, j, k + 5, true, false);
        }
        for (k1 = k - 2; k1 <= k + 2; ++k1) {
            placeBalconySection(world, i - 9, j, k1, false, false);
        }
        for (k1 = k - 5; k1 <= k + 5; ++k1) {
            if (Math.abs(k1 - k) < 3) {
                continue;
            }
            placeBalconySection(world, i - 9, j, k1, true, false);
        }
        for (k1 = k - 1; k1 <= k + 1; ++k1) {
            placeBalconySection(world, i - 10, j, k1, false, false);
        }
        for (k1 = k - 3; k1 <= k + 3; ++k1) {
            if (Math.abs(k1 - k) < 2) {
                continue;
            }
            placeBalconySection(world, i - 10, j, k1, true, false);
        }
        for (k1 = k - 2; k1 <= k + 2; ++k1) {
            if (Math.abs(k1 - k) == 0) {
                placeBalconySection(world, i - 11, j, k1, true, true);
                continue;
            }
            placeBalconySection(world, i - 11, j, k1, true, false);
        }
        setBlockAndNotifyAdequately(world, i - 6, j + 4, k, LOTRLegacyBlocks.mod("slabDouble3"), 0);
        setAir(world, i - 6, j + 5, k);
        setAir(world, i - 6, j + 6, k);
        setBlockAndNotifyAdequately(world, i - 6, j, k, LOTRLegacyBlocks.vanilla("planks"), 1);
        setBlockAndNotifyAdequately(world, i - 6, j + 1, k, LOTRLegacyBlocks.mod("doorSpruce"), 0);
        setBlockAndNotifyAdequately(world, i - 6, j + 2, k, LOTRLegacyBlocks.mod("doorSpruce"), 8);
        setBlockAndNotifyAdequately(world, i - 8, j + 3, k, LOTRLegacyBlocks.mod("chandelier"), 11);
        for (j12 = j + 1; j12 <= j + 2; ++j12) {
            for (int i12 = i - 7; i12 >= i - 8; --i12) {
                placeRandomOre(world, random, i12, j12, k - 4);
                placeRandomOre(world, random, i12, j12, k - 3);
                placeRandomOre(world, random, i12, j12, k + 3);
                placeRandomOre(world, random, i12, j12, k + 4);
            }
            placeRandomOre(world, random, i - 9, j12, k - 2);
            placeRandomOre(world, random, i - 9, j12, k + 2);
            for (k12 = k - 1; k12 <= k + 1; ++k12) {
                placeRandomOre(world, random, i - 10, j12, k12);
            }
        }
        setBlockAndNotifyAdequately(world, i - 3, j + 9, k, LOTRLegacyBlocks.mod("commandTable"), 0);
    }

    public void placeBalconySection(WorldGenLevel world, int i, int j, int k, boolean isEdge, boolean isPillar) {
        if (isEdge) {
            for (int j1 = j + 4; (j1 >= j || !isOpaqueAt(world, i, j1, k)) && j1 >= world.getMinY(); --j1) {
                if (isPillar) {
                    setBlockAndNotifyAdequately(world, i, j1, k, LOTRLegacyBlocks.mod("pillar"), 3);
                } else {
                    setBlockAndNotifyAdequately(world, i, j1, k, LOTRLegacyBlocks.mod("brick"), 14);
                }
                setGrassToDirt(world, i, j1 - 1, k);
            }
            if (isPillar) {
                setBlockAndNotifyAdequately(world, i, j + 4, k, LOTRLegacyBlocks.mod("brick3"), 12);
            }
            setBlockAndNotifyAdequately(world, i, j + 5, k, LOTRLegacyBlocks.mod("brick"), 6);
            setBlockAndNotifyAdequately(world, i, j + 6, k, LOTRLegacyBlocks.mod("wall"), 14);
        } else {
            int j1;
            for (j1 = j - 1; !isOpaqueAt(world, i, j1, k) && j1 >= world.getMinY(); --j1) {
                setBlockAndNotifyAdequately(world, i, j1, k, LOTRLegacyBlocks.mod("brick"), 14);
                setGrassToDirt(world, i, j1 - 1, k);
            }
            setBlockAndNotifyAdequately(world, i, j, k, LOTRLegacyBlocks.vanilla("planks"), 1);
            for (j1 = j + 1; j1 <= j + 6; ++j1) {
                setAir(world, i, j1, k);
            }
            setBlockAndNotifyAdequately(world, i, j + 4, k, LOTRLegacyBlocks.mod("slabDouble3"), 0);
        }
    }

    public void placeRandomOre(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        if (!isOpaqueAt(world, i, j - 1, k) || !random.nextBoolean()) {
            return;
        }
        int l = random.nextInt(5);
        LegacyBlock block = null;
        switch (l) {
            case 0: {
                block = LOTRLegacyBlocks.vanilla("iron_ore");
                break;
            }
            case 1: {
                block = LOTRLegacyBlocks.vanilla("gold_ore");
                break;
            }
            case 2: {
                block = LOTRLegacyBlocks.mod("oreCopper");
                break;
            }
            case 3: {
                block = LOTRLegacyBlocks.mod("oreTin");
                break;
            }
            case 4: {
                block = LOTRLegacyBlocks.mod("oreSilver");
            }
        }
        setBlockAndNotifyAdequately(world, i, j, k, block, 0);
    }

    public void spawnDwarf(WorldGenLevel world, int i, int j, int k) {
        LOTRBlueDwarfWarriorEntity dwarf = world.getRandom().nextInt(3) == 0 ? create(LOTREntities.BLUE_DWARF_AXE_THROWER, world) : create(LOTREntities.BLUE_DWARF_WARRIOR, world);
        dwarf.snapTo(i + 0.5, j, k + 0.5, 0.0f, 0.0f);
        dwarf.finalizeSpawn(world, world.getCurrentDifficultyAt(dwarf.blockPosition()), EntitySpawnReason.STRUCTURE, null);
        dwarf.isNPCPersistent = true;
        dwarf.setHomeTo(new BlockPos(i, j, k), 16);
        world.addFreshEntity(dwarf);
    }

    public void spawnDwarfCommander(WorldGenLevel world, int i, int j, int k) {
        LOTRBlueDwarfCommanderEntity dwarf = create(LOTREntities.BLUE_DWARF_COMMANDER, world);
        dwarf.snapTo(i + 0.5, j, k + 0.5, 0.0f, 0.0f);
        dwarf.finalizeSpawn(world, world.getCurrentDifficultyAt(dwarf.blockPosition()), EntitySpawnReason.STRUCTURE, null);
        dwarf.setHomeTo(new BlockPos(i, j, k), 16);
        world.addFreshEntity(dwarf);
    }
}
