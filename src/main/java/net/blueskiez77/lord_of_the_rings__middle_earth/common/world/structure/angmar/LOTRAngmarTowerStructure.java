package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.angmar;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.angmar.LOTRAngmarOrcMercenaryCaptainEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

public class LOTRAngmarTowerStructure extends LOTRStructureBase {
    public LOTRAngmarTowerStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generate(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        int j1;
        int k1;
        int i1;
        int k12;
        int j12;
        if (restrictions) {
            if (!(isBiome(world, i, k, "angmar"))) {
                return false;
            }
            BlockState l = world.getBlockState(new BlockPos(i, j - 1, k));
            if (!LOTRLegacyBlocks.vanilla("grass").matches(l) && !LOTRLegacyBlocks.vanilla("dirt").matches(l) && !LOTRLegacyBlocks.vanilla("stone").matches(l)) {
                return false;
            }
        }
        --j;
        int rotation = random.nextInt(4);
        if (!restrictions && usingPlayer != null) {
            rotation = usingPlayerRotation();
        }
        switch (rotation) {
            case 0: {
                k += 7;
                break;
            }
            case 1: {
                i -= 7;
                break;
            }
            case 2: {
                k -= 7;
                break;
            }
            case 3: {
                i += 7;
            }
        }
        int sections = 2 + random.nextInt(3);
        if (restrictions) {
            for (int i12 = i - 7; i12 <= i + 7; ++i12) {
                for (k12 = k - 7; k12 <= k + 7; ++k12) {
                    j1 = world.getHeight(Heightmap.Types.WORLD_SURFACE, i12, k12) - 1;
                    BlockState block = world.getBlockState(new BlockPos(i12, j1, k12));
                    if (LOTRLegacyBlocks.vanilla("grass").matches(block) || LOTRLegacyBlocks.vanilla("stone").matches(block) || LOTRLegacyBlocks.vanilla("dirt").matches(block) || block.is(BlockTags.LOGS) || block.is(BlockTags.LEAVES)) {
                        continue;
                    }
                    return false;
                }
            }
        }
        for (k1 = k - 2; k1 <= k + 2; ++k1) {
            for (j12 = j; !isOpaqueAt(world, i - 6, j12, k1) && j12 >= 0; --j12) {
                setBlockAndNotifyAdequately(world, i - 6, j12, k1, LOTRLegacyBlocks.mod("brick2"), 0);
                setGrassToDirt(world, i - 6, j12 - 1, k1);
            }
            for (j12 = j; !isOpaqueAt(world, i + 6, j12, k1) && j12 >= 0; --j12) {
                setBlockAndNotifyAdequately(world, i + 6, j12, k1, LOTRLegacyBlocks.mod("brick2"), 0);
                setGrassToDirt(world, i + 6, j12 - 1, k1);
            }
        }
        for (k1 = k - 4; k1 <= k + 4; ++k1) {
            for (j12 = j; !isOpaqueAt(world, i - 5, j12, k1) && j12 >= 0; --j12) {
                setBlockAndNotifyAdequately(world, i - 5, j12, k1, LOTRLegacyBlocks.mod("brick2"), 0);
                setGrassToDirt(world, i - 5, j12 - 1, k1);
            }
            for (j12 = j; !isOpaqueAt(world, i + 5, j12, k1) && j12 >= 0; --j12) {
                setBlockAndNotifyAdequately(world, i + 5, j12, k1, LOTRLegacyBlocks.mod("brick2"), 0);
                setGrassToDirt(world, i + 5, j12 - 1, k1);
            }
        }
        for (k1 = k - 5; k1 <= k + 5; ++k1) {
            for (i1 = i - 4; i1 <= i - 3; ++i1) {
                for (j1 = j; !isOpaqueAt(world, i1, j1, k1) && j1 >= 0; --j1) {
                    setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRLegacyBlocks.mod("brick2"), 0);
                    setGrassToDirt(world, i1, j1 - 1, k1);
                }
            }
            for (i1 = i + 3; i1 <= i + 4; ++i1) {
                for (j1 = j; !isOpaqueAt(world, i1, j1, k1) && j1 >= 0; --j1) {
                    setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRLegacyBlocks.mod("brick2"), 0);
                    setGrassToDirt(world, i1, j1 - 1, k1);
                }
            }
        }
        for (k1 = k - 6; k1 <= k + 6; ++k1) {
            for (i1 = i - 2; i1 <= i + 2; ++i1) {
                for (j1 = j; !isOpaqueAt(world, i1, j1, k1) && j1 >= 0; --j1) {
                    setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRLegacyBlocks.mod("brick2"), 0);
                    setGrassToDirt(world, i1, j1 - 1, k1);
                }
            }
        }
        for (int l = 0; l <= sections; ++l) {
            generateTowerSection(world, random, i, j, k, l, false);
        }
        generateTowerSection(world, random, i, j, k, sections + 1, true);
        LOTRAngmarOrcMercenaryCaptainEntity trader = create(LOTREntities.ANGMAR_ORC_MERCENARY_CAPTAIN, world);
        trader.snapTo(i - 2 + 0.5, j + (sections + 1) * 8 + 1, k + 0.5, world.getRandom().nextFloat() * 360.0f, 0.0f);
        trader.finalizeSpawn(world, world.getCurrentDifficultyAt(trader.blockPosition()), EntitySpawnReason.STRUCTURE, null);
        trader.setHomeTo(new BlockPos(i, j + (sections + 1) * 8, k), 24);
        world.addFreshEntity(trader);
        switch (rotation) {
            case 0: {
                for (i1 = i - 1; i1 <= i + 1; ++i1) {
                    setBlockAndNotifyAdequately(world, i1, j, k - 6, LOTRLegacyBlocks.vanilla("stonebrick"), 0);
                    for (j1 = j + 1; j1 <= j + 4; ++j1) {
                        setBlockAndNotifyAdequately(world, i1, j1, k - 6, LOTRLegacyBlocks.vanilla("air"), 0);
                    }
                }
                setBlockAndNotifyAdequately(world, i, j + 7, k - 6, LOTRLegacyBlocks.mod("brick2"), 0);
                placeWallBanner(world, i, j + 7, k - 6, 2, "ANGMAR");
                break;
            }
            case 1: {
                for (k12 = k - 1; k12 <= k + 1; ++k12) {
                    setBlockAndNotifyAdequately(world, i + 6, j, k12, LOTRLegacyBlocks.vanilla("stonebrick"), 0);
                    for (j1 = j + 1; j1 <= j + 4; ++j1) {
                        setBlockAndNotifyAdequately(world, i + 6, j1, k12, LOTRLegacyBlocks.vanilla("air"), 0);
                    }
                }
                setBlockAndNotifyAdequately(world, i + 6, j + 7, k, LOTRLegacyBlocks.mod("brick2"), 0);
                placeWallBanner(world, i + 6, j + 7, k, 3, "ANGMAR");
                break;
            }
            case 2: {
                for (i1 = i - 1; i1 <= i + 1; ++i1) {
                    setBlockAndNotifyAdequately(world, i1, j, k + 6, LOTRLegacyBlocks.vanilla("stonebrick"), 0);
                    for (j1 = j + 1; j1 <= j + 4; ++j1) {
                        setBlockAndNotifyAdequately(world, i1, j1, k + 6, LOTRLegacyBlocks.vanilla("air"), 0);
                    }
                }
                setBlockAndNotifyAdequately(world, i, j + 7, k + 6, LOTRLegacyBlocks.mod("brick2"), 0);
                placeWallBanner(world, i, j + 7, k + 6, 0, "ANGMAR");
                break;
            }
            case 3: {
                for (k12 = k - 1; k12 <= k + 1; ++k12) {
                    setBlockAndNotifyAdequately(world, i - 6, j, k12, LOTRLegacyBlocks.vanilla("stonebrick"), 0);
                    for (j1 = j + 1; j1 <= j + 4; ++j1) {
                        setBlockAndNotifyAdequately(world, i - 6, j1, k12, LOTRLegacyBlocks.vanilla("air"), 0);
                    }
                }
                setBlockAndNotifyAdequately(world, i - 6, j + 7, k, LOTRLegacyBlocks.mod("brick2"), 0);
                placeWallBanner(world, i - 6, j + 7, k, 1, "ANGMAR");
            }
        }
        int radius = 6;
        for (int l = 0; l < 16; ++l) {
            int k13;
            int j13;
            int i13 = i - random.nextInt(radius * 2) + random.nextInt(radius * 2);
            BlockState id = world.getBlockState(new BlockPos(i13, (j13 = world.getHeight(Heightmap.Types.WORLD_SURFACE, i13, k13 = k - random.nextInt(radius * 2) + random.nextInt(radius * 2))) - 1, k13));
            if (!LOTRLegacyBlocks.vanilla("grass").matches(id) && !LOTRLegacyBlocks.vanilla("dirt").matches(id) && !LOTRLegacyBlocks.vanilla("stone").matches(id)) {
                continue;
            }
            int randomFeature = random.nextInt(4);
            boolean flag = true;
            if (randomFeature == 0) {
                if (!isOpaqueAt(world, i13, j13, k13)) {
                    if (random.nextInt(3) == 0) {
                        setBlockAndNotifyAdequately(world, i13, j13, k13, LOTRLegacyBlocks.mod("slabSingle3"), 4);
                    } else {
                        setBlockAndNotifyAdequately(world, i13, j13, k13, LOTRLegacyBlocks.mod("slabSingle3"), 3);
                    }
                }
            } else {
                int j2;
                for (j2 = j13; j2 < j13 + randomFeature && flag; ++j2) {
                    flag = !isOpaqueAt(world, i13, j2, k13);
                }
                if (flag) {
                    for (j2 = j13; j2 < j13 + randomFeature; ++j2) {
                        if (random.nextBoolean()) {
                            setBlockAndNotifyAdequately(world, i13, j2, k13, LOTRLegacyBlocks.mod("brick2"), 0);
                            continue;
                        }
                        setBlockAndNotifyAdequately(world, i13, j2, k13, LOTRLegacyBlocks.mod("brick2"), 1);
                    }
                }
            }
            if (!LOTRLegacyBlocks.vanilla("dirt").matches(world.getBlockState(new BlockPos(i13, j13 - 1, k13)))) {
                continue;
            }
            setBlockAndNotifyAdequately(world, i13, j13 - 1, k13, LOTRLegacyBlocks.vanilla("dirt"), 0);
        }
        return true;
    }

    public void generateTowerSection(WorldGenLevel world, RandomSource random, int i, int j, int k, int section, boolean isTop) {
        int j1;
        int i1;
        for (j1 = section == 0 ? j : (j += section * 8) + 1; j1 <= (isTop ? j + 10 : j + 8); ++j1) {
            int k1;
            int i12;
            LegacyBlock fillBlock;
            int fillMeta = 0;
            if (j1 == j) {
                fillBlock = LOTRLegacyBlocks.vanilla("stonebrick");
            } else {
                fillBlock = LOTRLegacyBlocks.vanilla("air");
            }
            boolean hasCeiling = j1 == j + 8 && !isTop;
            for (k1 = k - 2; k1 <= k + 2; ++k1) {
                setBlockAndNotifyAdequately(world, i - 5, j1, k1, fillBlock, fillMeta);
                setBlockAndNotifyAdequately(world, i + 5, j1, k1, fillBlock, fillMeta);
                if (hasCeiling && random.nextInt(20) != 0) {
                    setBlockAndNotifyAdequately(world, i - 5, j1, k1, LOTRLegacyBlocks.mod("slabSingle3"), 11);
                }
                if (!hasCeiling || random.nextInt(20) == 0) {
                    continue;
                }
                setBlockAndNotifyAdequately(world, i + 5, j1, k1, LOTRLegacyBlocks.mod("slabSingle3"), 11);
            }
            for (k1 = k - 4; k1 <= k + 4; ++k1) {
                for (i12 = i - 4; i12 <= i - 3; ++i12) {
                    setBlockAndNotifyAdequately(world, i12, j1, k1, fillBlock, fillMeta);
                    if (!hasCeiling || random.nextInt(20) == 0) {
                        continue;
                    }
                    setBlockAndNotifyAdequately(world, i12, j1, k1, LOTRLegacyBlocks.mod("slabSingle3"), 11);
                }
                for (i12 = i + 3; i12 <= i + 4; ++i12) {
                    setBlockAndNotifyAdequately(world, i12, j1, k1, fillBlock, fillMeta);
                    if (!hasCeiling || random.nextInt(20) == 0) {
                        continue;
                    }
                    setBlockAndNotifyAdequately(world, i12, j1, k1, LOTRLegacyBlocks.mod("slabSingle3"), 11);
                }
            }
            for (k1 = k - 5; k1 <= k + 5; ++k1) {
                for (i12 = i - 2; i12 <= i + 2; ++i12) {
                    setBlockAndNotifyAdequately(world, i12, j1, k1, fillBlock, fillMeta);
                    if (!hasCeiling || random.nextInt(20) == 0) {
                        continue;
                    }
                    setBlockAndNotifyAdequately(world, i12, j1, k1, LOTRLegacyBlocks.mod("slabSingle3"), 11);
                }
            }
        }
        for (j1 = j + 1; j1 <= (isTop ? j + 1 : j + 8); ++j1) {
            for (int k1 = k - 2; k1 <= k + 2; ++k1) {
                placeRandomBrick(world, random, i - 6, j1, k1);
                placeRandomBrick(world, random, i + 6, j1, k1);
            }
            for (int i13 = i - 2; i13 <= i + 2; ++i13) {
                placeRandomBrick(world, random, i13, j1, k - 6);
                placeRandomBrick(world, random, i13, j1, k + 6);
            }
            placeRandomBrick(world, random, i - 5, j1, k - 4);
            placeRandomBrick(world, random, i - 5, j1, k - 3);
            placeRandomBrick(world, random, i - 5, j1, k + 3);
            placeRandomBrick(world, random, i - 5, j1, k + 4);
            placeRandomBrick(world, random, i - 4, j1, k - 5);
            placeRandomBrick(world, random, i - 4, j1, k + 5);
            placeRandomBrick(world, random, i - 3, j1, k - 5);
            placeRandomBrick(world, random, i - 3, j1, k + 5);
            placeRandomBrick(world, random, i + 3, j1, k - 5);
            placeRandomBrick(world, random, i + 3, j1, k + 5);
            placeRandomBrick(world, random, i + 4, j1, k - 5);
            placeRandomBrick(world, random, i + 4, j1, k + 5);
            placeRandomBrick(world, random, i + 5, j1, k - 4);
            placeRandomBrick(world, random, i + 5, j1, k - 3);
            placeRandomBrick(world, random, i + 5, j1, k + 3);
            placeRandomBrick(world, random, i + 5, j1, k + 4);
        }
        if (!isTop) {
            for (j1 = j + 2; j1 <= j + 4; ++j1) {
                for (int k1 = k - 1; k1 <= k + 1; ++k1) {
                    if (random.nextInt(3) == 0) {
                        setBlockAndNotifyAdequately(world, i - 6, j1, k1, LOTRLegacyBlocks.vanilla("air"), 0);
                    } else {
                        setBlockAndNotifyAdequately(world, i - 6, j1, k1, LOTRLegacyBlocks.mod("orcSteelBars"), 0);
                    }
                    if (random.nextInt(3) != 0) {
                        setBlockAndNotifyAdequately(world, i + 6, j1, k1, LOTRLegacyBlocks.mod("orcSteelBars"), 0);
                        continue;
                    }
                    setBlockAndNotifyAdequately(world, i + 6, j1, k1, LOTRLegacyBlocks.vanilla("air"), 0);
                }
                for (int i14 = i - 1; i14 <= i + 1; ++i14) {
                    if (random.nextInt(3) == 0) {
                        setBlockAndNotifyAdequately(world, i14, j1, k - 6, LOTRLegacyBlocks.vanilla("air"), 0);
                    } else {
                        setBlockAndNotifyAdequately(world, i14, j1, k - 6, LOTRLegacyBlocks.mod("orcSteelBars"), 0);
                    }
                    if (random.nextInt(3) != 0) {
                        setBlockAndNotifyAdequately(world, i14, j1, k + 6, LOTRLegacyBlocks.mod("orcSteelBars"), 0);
                        continue;
                    }
                    setBlockAndNotifyAdequately(world, i14, j1, k + 6, LOTRLegacyBlocks.vanilla("air"), 0);
                }
            }
            for (i1 = i - 2; i1 <= i + 2; ++i1) {
                for (int k1 = k - 2; k1 <= k + 2; ++k1) {
                    setBlockAndNotifyAdequately(world, i1, j + 8, k1, LOTRLegacyBlocks.vanilla("air"), 0);
                }
            }
            setBlockAndNotifyAdequately(world, i - 2, j + 1, k + 1, LOTRLegacyBlocks.mod("slabSingle3"), 3);
            setBlockAndNotifyAdequately(world, i - 2, j + 1, k + 2, LOTRLegacyBlocks.mod("slabSingle3"), 11);
            setBlockAndNotifyAdequately(world, i - 1, j + 2, k + 2, LOTRLegacyBlocks.mod("slabSingle3"), 3);
            setBlockAndNotifyAdequately(world, i, j + 2, k + 2, LOTRLegacyBlocks.mod("slabSingle3"), 11);
            setBlockAndNotifyAdequately(world, i + 1, j + 3, k + 2, LOTRLegacyBlocks.mod("slabSingle3"), 3);
            setBlockAndNotifyAdequately(world, i + 2, j + 3, k + 2, LOTRLegacyBlocks.mod("slabSingle3"), 11);
            setBlockAndNotifyAdequately(world, i + 2, j + 4, k + 1, LOTRLegacyBlocks.mod("slabSingle3"), 3);
            setBlockAndNotifyAdequately(world, i + 2, j + 4, k, LOTRLegacyBlocks.mod("slabSingle3"), 11);
            setBlockAndNotifyAdequately(world, i + 2, j + 5, k - 1, LOTRLegacyBlocks.mod("slabSingle3"), 3);
            setBlockAndNotifyAdequately(world, i + 2, j + 5, k - 2, LOTRLegacyBlocks.mod("slabSingle3"), 11);
            setBlockAndNotifyAdequately(world, i + 1, j + 6, k - 2, LOTRLegacyBlocks.mod("slabSingle3"), 3);
            setBlockAndNotifyAdequately(world, i, j + 6, k - 2, LOTRLegacyBlocks.mod("slabSingle3"), 11);
            setBlockAndNotifyAdequately(world, i - 1, j + 7, k - 2, LOTRLegacyBlocks.mod("slabSingle3"), 3);
            setBlockAndNotifyAdequately(world, i - 2, j + 7, k - 2, LOTRLegacyBlocks.mod("slabSingle3"), 11);
            setBlockAndNotifyAdequately(world, i - 2, j + 8, k - 1, LOTRLegacyBlocks.mod("slabSingle3"), 3);
            setBlockAndNotifyAdequately(world, i - 2, j + 8, k, LOTRLegacyBlocks.mod("slabSingle3"), 11);
        }
        for (i1 = i - 1; i1 <= i + 1; ++i1) {
            for (int k1 = k - 1; k1 <= k + 1; ++k1) {
                for (int j12 = j + 1; j12 <= (isTop ? j + 3 : j + 8); ++j12) {
                    placeRandomBrick(world, random, i1, j12, k1);
                }
            }
        }
        if (isTop) {
            int j13;
            int top = 4 + random.nextInt(5);
            for (j13 = j + 1; j13 <= j + top; ++j13) {
                for (int k1 = k - 1; k1 <= k + 1; ++k1) {
                    placeRandomBrick(world, random, i - 7, j13, k1);
                    placeRandomBrick(world, random, i + 7, j13, k1);
                }
                for (int i15 = i - 1; i15 <= i + 1; ++i15) {
                    placeRandomBrick(world, random, i15, j13, k - 7);
                    placeRandomBrick(world, random, i15, j13, k + 7);
                }
            }
            for (int k1 = k - 1; k1 <= k + 1; ++k1) {
                placeRandomStairs(world, random, i - 7, j, k1, 4);
                placeRandomStairs(world, random, i - 6, j + 2, k1, 1);
                placeRandomStairs(world, random, i + 7, j, k1, 5);
                placeRandomStairs(world, random, i + 6, j + 2, k1, 0);
            }
            for (int i16 = i - 1; i16 <= i + 1; ++i16) {
                placeRandomStairs(world, random, i16, j, k - 7, 6);
                placeRandomStairs(world, random, i16, j + 2, k - 6, 3);
                placeRandomStairs(world, random, i16, j, k + 7, 7);
                placeRandomStairs(world, random, i16, j + 2, k + 6, 2);
            }
            for (j13 = j; j13 <= j + 4; ++j13) {
                setBlockAndNotifyAdequately(world, i - 5, j13, k - 5, LOTRLegacyBlocks.mod("brick2"), 0);
                setBlockAndNotifyAdequately(world, i - 5, j13, k + 5, LOTRLegacyBlocks.mod("brick2"), 0);
                setBlockAndNotifyAdequately(world, i + 5, j13, k - 5, LOTRLegacyBlocks.mod("brick2"), 0);
                setBlockAndNotifyAdequately(world, i + 5, j13, k + 5, LOTRLegacyBlocks.mod("brick2"), 0);
            }
            placeBanner(world, i - 5, j + 5, k - 5, 0, "ANGMAR");
            placeBanner(world, i - 5, j + 5, k + 5, 0, "ANGMAR");
            placeBanner(world, i + 5, j + 5, k - 5, 0, "ANGMAR");
            placeBanner(world, i + 5, j + 5, k + 5, 0, "ANGMAR");
            placeRandomStairs(world, random, i - 5, j + 2, k - 4, 3);
            placeRandomStairs(world, random, i - 4, j + 2, k - 5, 1);
            placeRandomStairs(world, random, i - 5, j + 2, k + 4, 2);
            placeRandomStairs(world, random, i - 4, j + 2, k + 5, 1);
            placeRandomStairs(world, random, i + 5, j + 2, k - 4, 3);
            placeRandomStairs(world, random, i + 4, j + 2, k - 5, 0);
            placeRandomStairs(world, random, i + 5, j + 2, k + 4, 2);
            placeRandomStairs(world, random, i + 4, j + 2, k + 5, 0);
        }
    }

    public void placeRandomBrick(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        if (random.nextInt(20) == 0) {
            return;
        }
        if (random.nextInt(3) == 0) {
            setBlockAndNotifyAdequately(world, i, j, k, LOTRLegacyBlocks.mod("brick2"), 1);
        } else {
            setBlockAndNotifyAdequately(world, i, j, k, LOTRLegacyBlocks.mod("brick2"), 0);
        }
    }

    public void placeRandomStairs(WorldGenLevel world, RandomSource random, int i, int j, int k, int meta) {
        if (random.nextInt(10) == 0) {
            return;
        }
        if (random.nextInt(3) == 0) {
            setBlockAndNotifyAdequately(world, i, j, k, LOTRLegacyBlocks.mod("stairsAngmarBrickCracked"), meta);
        } else {
            setBlockAndNotifyAdequately(world, i, j, k, LOTRLegacyBlocks.mod("stairsAngmarBrick"), meta);
        }
    }
}
