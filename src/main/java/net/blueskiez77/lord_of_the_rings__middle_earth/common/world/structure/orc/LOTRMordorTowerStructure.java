package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.orc;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRNPCRespawnerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRFoods;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.mordor.LOTRMordorOrcMercenaryCaptainEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

public class LOTRMordorTowerStructure extends LOTRStructureBase {
    public LOTRMordorTowerStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generate(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        int j1;
        int k1;
        int i1;
        int j12;
        int k12;
        if (restrictions && !(isBiome(world, i, k, "mordor"))) {
            return false;
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
        int equipmentSection = 1 + random.nextInt(sections);
        if (restrictions) {
            for (int i12 = i - 7; i12 <= i + 7; ++i12) {
                for (k12 = k - 7; k12 <= k + 7; ++k12) {
                    j12 = world.getHeight(Heightmap.Types.WORLD_SURFACE, i12, k12) - 1;
                    BlockState block = world.getBlockState(new BlockPos(i12, j12, k12));
                    if (LOTRLegacyBlocks.mod("mordorDirt").matches(block) || LOTRLegacyBlocks.mod("mordorGravel").matches(block) || block.is(LOTRLegacyBlocks.mod("rock").state(0).getBlock()) || LOTRLegacyBlocks.vanilla("grass").matches(block) || LOTRLegacyBlocks.vanilla("dirt").matches(block)) {
                        continue;
                    }
                    return false;
                }
            }
        }
        for (k1 = k - 2; k1 <= k + 2; ++k1) {
            for (j1 = j; !isOpaqueAt(world, i - 6, j1, k1) && j1 >= 0; --j1) {
                setBlockAndNotifyAdequately(world, i - 6, j1, k1, LOTRLegacyBlocks.mod("brick"), 0);
            }
            for (j1 = j; !isOpaqueAt(world, i + 6, j1, k1) && j1 >= 0; --j1) {
                setBlockAndNotifyAdequately(world, i + 6, j1, k1, LOTRLegacyBlocks.mod("brick"), 0);
            }
        }
        for (k1 = k - 4; k1 <= k + 4; ++k1) {
            for (j1 = j; !isOpaqueAt(world, i - 5, j1, k1) && j1 >= 0; --j1) {
                setBlockAndNotifyAdequately(world, i - 5, j1, k1, LOTRLegacyBlocks.mod("brick"), 0);
            }
            for (j1 = j; !isOpaqueAt(world, i + 5, j1, k1) && j1 >= 0; --j1) {
                setBlockAndNotifyAdequately(world, i + 5, j1, k1, LOTRLegacyBlocks.mod("brick"), 0);
            }
        }
        for (k1 = k - 5; k1 <= k + 5; ++k1) {
            for (i1 = i - 4; i1 <= i - 3; ++i1) {
                for (j12 = j; !isOpaqueAt(world, i1, j12, k1) && j12 >= 0; --j12) {
                    setBlockAndNotifyAdequately(world, i1, j12, k1, LOTRLegacyBlocks.mod("brick"), 0);
                }
            }
            for (i1 = i + 3; i1 <= i + 4; ++i1) {
                for (j12 = j; !isOpaqueAt(world, i1, j12, k1) && j12 >= 0; --j12) {
                    setBlockAndNotifyAdequately(world, i1, j12, k1, LOTRLegacyBlocks.mod("brick"), 0);
                }
            }
        }
        for (k1 = k - 6; k1 <= k + 6; ++k1) {
            for (i1 = i - 2; i1 <= i + 2; ++i1) {
                for (j12 = j; !isOpaqueAt(world, i1, j12, k1) && j12 >= 0; --j12) {
                    setBlockAndNotifyAdequately(world, i1, j12, k1, LOTRLegacyBlocks.mod("brick"), 0);
                }
            }
        }
        for (int l = 0; l <= sections; ++l) {
            generateTowerSection(world, random, i, j, k, l, false, l == equipmentSection);
        }
        generateTowerSection(world, random, i, j, k, sections + 1, true, false);
        LOTRMordorOrcMercenaryCaptainEntity trader = create(LOTREntities.MORDOR_ORC_MERCENARY_CAPTAIN, world);
        trader.snapTo(i + 0.5, j + (sections + 1) * 8 + 1, k - 4 + 0.5, world.getRandom().nextFloat() * 360.0f, 0.0f);
        trader.finalizeSpawn(world, world.getCurrentDifficultyAt(trader.blockPosition()), EntitySpawnReason.STRUCTURE, null);
        trader.setHomeTo(new BlockPos(i, j + (sections + 1) * 8, k), 24);
        world.addFreshEntity(trader);
        switch (rotation) {
            case 0: {
                for (i1 = i - 1; i1 <= i + 1; ++i1) {
                    setBlockAndNotifyAdequately(world, i1, j, k - 6, LOTRLegacyBlocks.mod("slabDouble"), 0);
                    for (j12 = j + 1; j12 <= j + 4; ++j12) {
                        setBlockAndNotifyAdequately(world, i1, j12, k - 6, LOTRLegacyBlocks.mod("gateOrc"), 3);
                    }
                }
                placeWallBanner(world, i, j + 7, k - 6, 2, "MORDOR");
                break;
            }
            case 1: {
                for (k12 = k - 1; k12 <= k + 1; ++k12) {
                    setBlockAndNotifyAdequately(world, i + 6, j, k12, LOTRLegacyBlocks.mod("slabDouble"), 0);
                    for (j12 = j + 1; j12 <= j + 4; ++j12) {
                        setBlockAndNotifyAdequately(world, i + 6, j12, k12, LOTRLegacyBlocks.mod("gateOrc"), 4);
                    }
                }
                placeWallBanner(world, i + 6, j + 7, k, 3, "MORDOR");
                break;
            }
            case 2: {
                for (i1 = i - 1; i1 <= i + 1; ++i1) {
                    setBlockAndNotifyAdequately(world, i1, j, k + 6, LOTRLegacyBlocks.mod("slabDouble"), 0);
                    for (j12 = j + 1; j12 <= j + 4; ++j12) {
                        setBlockAndNotifyAdequately(world, i1, j12, k + 6, LOTRLegacyBlocks.mod("gateOrc"), 2);
                    }
                }
                placeWallBanner(world, i, j + 7, k + 6, 0, "MORDOR");
                break;
            }
            case 3: {
                for (k12 = k - 1; k12 <= k + 1; ++k12) {
                    setBlockAndNotifyAdequately(world, i - 6, j, k12, LOTRLegacyBlocks.mod("slabDouble"), 0);
                    for (j12 = j + 1; j12 <= j + 4; ++j12) {
                        setBlockAndNotifyAdequately(world, i - 6, j12, k12, LOTRLegacyBlocks.mod("gateOrc"), 5);
                    }
                }
                placeWallBanner(world, i - 6, j + 7, k, 1, "MORDOR");
            }
        }
        LOTRNPCRespawnerEntity respawner = create(LOTREntities.NPC_RESPAWNER, world);
        respawner.setSpawnClass(LOTREntities.MORDOR_ORC);
        respawner.setCheckRanges(12, -8, 50, 20);
        respawner.setSpawnRanges(5, 1, 40, 16);
        placeNPCRespawner(respawner, world, i, j, k);
        return true;
    }

    public void generateTowerSection(WorldGenLevel world, RandomSource random, int i, int j, int k, int section, boolean isTop, boolean isEquipmentSection) {
        int j1;
        int i1;
        for (j1 = section == 0 ? j : (j += section * 8) + 1; j1 <= (isTop ? j + 10 : j + 8); ++j1) {
            int i12;
            int k1;
            LegacyBlock fillBlock;
            int fillMeta;
            if (j1 == j) {
                fillBlock = LOTRLegacyBlocks.mod("slabDouble");
                fillMeta = 0;
            } else if (j1 == j + 8 && !isTop) {
                fillBlock = LOTRLegacyBlocks.mod("slabSingle");
                fillMeta = 8;
            } else {
                fillBlock = LOTRLegacyBlocks.vanilla("air");
                fillMeta = 0;
            }
            for (k1 = k - 2; k1 <= k + 2; ++k1) {
                setBlockAndNotifyAdequately(world, i - 5, j1, k1, fillBlock, fillMeta);
                setBlockAndNotifyAdequately(world, i + 5, j1, k1, fillBlock, fillMeta);
            }
            for (k1 = k - 4; k1 <= k + 4; ++k1) {
                for (i12 = i - 4; i12 <= i - 3; ++i12) {
                    setBlockAndNotifyAdequately(world, i12, j1, k1, fillBlock, fillMeta);
                }
                for (i12 = i + 3; i12 <= i + 4; ++i12) {
                    setBlockAndNotifyAdequately(world, i12, j1, k1, fillBlock, fillMeta);
                }
            }
            for (k1 = k - 5; k1 <= k + 5; ++k1) {
                for (i12 = i - 2; i12 <= i + 2; ++i12) {
                    setBlockAndNotifyAdequately(world, i12, j1, k1, fillBlock, fillMeta);
                }
            }
        }
        for (j1 = j + 1; j1 <= (isTop ? j + 1 : j + 8); ++j1) {
            for (int k1 = k - 2; k1 <= k + 2; ++k1) {
                setBlockAndNotifyAdequately(world, i - 6, j1, k1, LOTRLegacyBlocks.mod("brick"), 0);
                setBlockAndNotifyAdequately(world, i + 6, j1, k1, LOTRLegacyBlocks.mod("brick"), 0);
            }
            for (int i13 = i - 2; i13 <= i + 2; ++i13) {
                setBlockAndNotifyAdequately(world, i13, j1, k - 6, LOTRLegacyBlocks.mod("brick"), 0);
                setBlockAndNotifyAdequately(world, i13, j1, k + 6, LOTRLegacyBlocks.mod("brick"), 0);
            }
            setBlockAndNotifyAdequately(world, i - 5, j1, k - 4, LOTRLegacyBlocks.mod("brick"), 0);
            setBlockAndNotifyAdequately(world, i - 5, j1, k - 3, LOTRLegacyBlocks.mod("brick"), 0);
            setBlockAndNotifyAdequately(world, i - 5, j1, k + 3, LOTRLegacyBlocks.mod("brick"), 0);
            setBlockAndNotifyAdequately(world, i - 5, j1, k + 4, LOTRLegacyBlocks.mod("brick"), 0);
            setBlockAndNotifyAdequately(world, i - 4, j1, k - 5, LOTRLegacyBlocks.mod("brick"), 0);
            setBlockAndNotifyAdequately(world, i - 4, j1, k + 5, LOTRLegacyBlocks.mod("brick"), 0);
            setBlockAndNotifyAdequately(world, i - 3, j1, k - 5, LOTRLegacyBlocks.mod("brick"), 0);
            setBlockAndNotifyAdequately(world, i - 3, j1, k + 5, LOTRLegacyBlocks.mod("brick"), 0);
            setBlockAndNotifyAdequately(world, i + 3, j1, k - 5, LOTRLegacyBlocks.mod("brick"), 0);
            setBlockAndNotifyAdequately(world, i + 3, j1, k + 5, LOTRLegacyBlocks.mod("brick"), 0);
            setBlockAndNotifyAdequately(world, i + 4, j1, k - 5, LOTRLegacyBlocks.mod("brick"), 0);
            setBlockAndNotifyAdequately(world, i + 4, j1, k + 5, LOTRLegacyBlocks.mod("brick"), 0);
            setBlockAndNotifyAdequately(world, i + 5, j1, k - 4, LOTRLegacyBlocks.mod("brick"), 0);
            setBlockAndNotifyAdequately(world, i + 5, j1, k - 3, LOTRLegacyBlocks.mod("brick"), 0);
            setBlockAndNotifyAdequately(world, i + 5, j1, k + 3, LOTRLegacyBlocks.mod("brick"), 0);
            setBlockAndNotifyAdequately(world, i + 5, j1, k + 4, LOTRLegacyBlocks.mod("brick"), 0);
        }
        placeOrcTorch(world, i - 5, j + 1, k - 2);
        placeOrcTorch(world, i - 5, j + 1, k + 2);
        placeOrcTorch(world, i + 5, j + 1, k - 2);
        placeOrcTorch(world, i + 5, j + 1, k + 2);
        placeOrcTorch(world, i - 2, j + 1, k - 5);
        placeOrcTorch(world, i + 2, j + 1, k - 5);
        placeOrcTorch(world, i - 2, j + 1, k + 5);
        placeOrcTorch(world, i + 2, j + 1, k + 5);
        if (!isTop) {
            for (j1 = j + 2; j1 <= j + 4; ++j1) {
                for (int k1 = k - 1; k1 <= k + 1; ++k1) {
                    setBlockAndNotifyAdequately(world, i - 6, j1, k1, LOTRLegacyBlocks.mod("orcSteelBars"), 0);
                    setBlockAndNotifyAdequately(world, i + 6, j1, k1, LOTRLegacyBlocks.mod("orcSteelBars"), 0);
                }
                for (int i14 = i - 1; i14 <= i + 1; ++i14) {
                    setBlockAndNotifyAdequately(world, i14, j1, k - 6, LOTRLegacyBlocks.mod("orcSteelBars"), 0);
                    setBlockAndNotifyAdequately(world, i14, j1, k + 6, LOTRLegacyBlocks.mod("orcSteelBars"), 0);
                }
            }
            for (i1 = i - 2; i1 <= i + 2; ++i1) {
                for (int k1 = k - 2; k1 <= k + 2; ++k1) {
                    setBlockAndNotifyAdequately(world, i1, j + 8, k1, LOTRLegacyBlocks.vanilla("air"), 0);
                }
            }
            setBlockAndNotifyAdequately(world, i - 2, j + 1, k + 1, LOTRLegacyBlocks.mod("slabSingle"), 0);
            setBlockAndNotifyAdequately(world, i - 2, j + 1, k + 2, LOTRLegacyBlocks.mod("slabSingle"), 8);
            setBlockAndNotifyAdequately(world, i - 1, j + 2, k + 2, LOTRLegacyBlocks.mod("slabSingle"), 0);
            setBlockAndNotifyAdequately(world, i, j + 2, k + 2, LOTRLegacyBlocks.mod("slabSingle"), 8);
            setBlockAndNotifyAdequately(world, i + 1, j + 3, k + 2, LOTRLegacyBlocks.mod("slabSingle"), 0);
            setBlockAndNotifyAdequately(world, i + 2, j + 3, k + 2, LOTRLegacyBlocks.mod("slabSingle"), 8);
            setBlockAndNotifyAdequately(world, i + 2, j + 4, k + 1, LOTRLegacyBlocks.mod("slabSingle"), 0);
            setBlockAndNotifyAdequately(world, i + 2, j + 4, k, LOTRLegacyBlocks.mod("slabSingle"), 8);
            setBlockAndNotifyAdequately(world, i + 2, j + 5, k - 1, LOTRLegacyBlocks.mod("slabSingle"), 0);
            setBlockAndNotifyAdequately(world, i + 2, j + 5, k - 2, LOTRLegacyBlocks.mod("slabSingle"), 8);
            setBlockAndNotifyAdequately(world, i + 1, j + 6, k - 2, LOTRLegacyBlocks.mod("slabSingle"), 0);
            setBlockAndNotifyAdequately(world, i, j + 6, k - 2, LOTRLegacyBlocks.mod("slabSingle"), 8);
            setBlockAndNotifyAdequately(world, i - 1, j + 7, k - 2, LOTRLegacyBlocks.mod("slabSingle"), 0);
            setBlockAndNotifyAdequately(world, i - 2, j + 7, k - 2, LOTRLegacyBlocks.mod("slabSingle"), 8);
            setBlockAndNotifyAdequately(world, i - 2, j + 8, k - 1, LOTRLegacyBlocks.mod("slabSingle"), 0);
            setBlockAndNotifyAdequately(world, i - 2, j + 8, k, LOTRLegacyBlocks.mod("slabSingle"), 8);
        }
        for (i1 = i - 1; i1 <= i + 1; ++i1) {
            for (int k1 = k - 1; k1 <= k + 1; ++k1) {
                for (int j12 = j + 1; j12 <= (isTop ? j + 3 : j + 8); ++j12) {
                    setBlockAndNotifyAdequately(world, i1, j12, k1, LOTRLegacyBlocks.mod("brick"), 0);
                }
            }
        }
        if (isEquipmentSection) {
            int l = random.nextInt(4);
            switch (l) {
                case 0: {
                    for (int i15 = i - 1; i15 <= i + 1; ++i15) {
                        setBlockAndNotifyAdequately(world, i15, j + 1, k - 5, LOTRLegacyBlocks.mod("orcBomb"), 0);
                        setBlockAndNotifyAdequately(world, i15, j + 1, k + 5, LOTRLegacyBlocks.mod("slabSingle"), 9);
                        placeBarrel(world, random, i15, j + 2, k + 5, 2, LOTRFoods.ORC_DRINK);
                    }
                    break;
                }
                case 1: {
                    for (int k1 = k - 1; k1 <= k + 1; ++k1) {
                        setBlockAndNotifyAdequately(world, i + 5, j + 1, k1, LOTRLegacyBlocks.mod("orcBomb"), 0);
                        setBlockAndNotifyAdequately(world, i - 5, j + 1, k1, LOTRLegacyBlocks.mod("slabSingle"), 9);
                        placeBarrel(world, random, i - 5, j + 2, k1, 5, LOTRFoods.ORC_DRINK);
                    }
                    break;
                }
                case 2: {
                    for (int i16 = i - 1; i16 <= i + 1; ++i16) {
                        setBlockAndNotifyAdequately(world, i16, j + 1, k + 5, LOTRLegacyBlocks.mod("orcBomb"), 0);
                        setBlockAndNotifyAdequately(world, i16, j + 1, k - 5, LOTRLegacyBlocks.mod("slabSingle"), 9);
                        placeBarrel(world, random, i16, j + 2, k - 5, 3, LOTRFoods.ORC_DRINK);
                    }
                    break;
                }
                case 3: {
                    for (int k1 = k - 1; k1 <= k + 1; ++k1) {
                        setBlockAndNotifyAdequately(world, i - 5, j + 1, k1, LOTRLegacyBlocks.mod("orcBomb"), 0);
                        setBlockAndNotifyAdequately(world, i + 5, j + 1, k1, LOTRLegacyBlocks.mod("slabSingle"), 9);
                        placeBarrel(world, random, i + 5, j + 2, k1, 4, LOTRFoods.ORC_DRINK);
                    }
                    break;
                }
            }
        }
        if (isTop) {
            for (j1 = j + 1; j1 <= j + 8; ++j1) {
                for (int k1 = k - 1; k1 <= k + 1; ++k1) {
                    setBlockAndNotifyAdequately(world, i - 7, j1, k1, LOTRLegacyBlocks.mod("brick"), 0);
                    setBlockAndNotifyAdequately(world, i + 7, j1, k1, LOTRLegacyBlocks.mod("brick"), 0);
                }
                for (int i17 = i - 1; i17 <= i + 1; ++i17) {
                    setBlockAndNotifyAdequately(world, i17, j1, k - 7, LOTRLegacyBlocks.mod("brick"), 0);
                    setBlockAndNotifyAdequately(world, i17, j1, k + 7, LOTRLegacyBlocks.mod("brick"), 0);
                }
            }
            for (int k1 = k - 1; k1 <= k + 1; ++k1) {
                setBlockAndNotifyAdequately(world, i - 7, j, k1, LOTRLegacyBlocks.mod("stairsMordorBrick"), 4);
                setBlockAndNotifyAdequately(world, i - 6, j + 2, k1, LOTRLegacyBlocks.mod("stairsMordorBrick"), 1);
                setBlockAndNotifyAdequately(world, i - 7, j + 9, k1, LOTRLegacyBlocks.mod("stairsMordorBrick"), 0);
                setBlockAndNotifyAdequately(world, i - 6, j + 9, k1, LOTRLegacyBlocks.mod("stairsMordorBrick"), 5);
                setBlockAndNotifyAdequately(world, i - 6, j + 10, k1, LOTRLegacyBlocks.mod("stairsMordorBrick"), 0);
                setBlockAndNotifyAdequately(world, i + 7, j, k1, LOTRLegacyBlocks.mod("stairsMordorBrick"), 5);
                setBlockAndNotifyAdequately(world, i + 6, j + 2, k1, LOTRLegacyBlocks.mod("stairsMordorBrick"), 0);
                setBlockAndNotifyAdequately(world, i + 7, j + 9, k1, LOTRLegacyBlocks.mod("stairsMordorBrick"), 1);
                setBlockAndNotifyAdequately(world, i + 6, j + 9, k1, LOTRLegacyBlocks.mod("stairsMordorBrick"), 4);
                setBlockAndNotifyAdequately(world, i + 6, j + 10, k1, LOTRLegacyBlocks.mod("stairsMordorBrick"), 1);
            }
            for (i1 = i - 1; i1 <= i + 1; ++i1) {
                setBlockAndNotifyAdequately(world, i1, j, k - 7, LOTRLegacyBlocks.mod("stairsMordorBrick"), 6);
                setBlockAndNotifyAdequately(world, i1, j + 2, k - 6, LOTRLegacyBlocks.mod("stairsMordorBrick"), 3);
                setBlockAndNotifyAdequately(world, i1, j + 9, k - 7, LOTRLegacyBlocks.mod("stairsMordorBrick"), 2);
                setBlockAndNotifyAdequately(world, i1, j + 9, k - 6, LOTRLegacyBlocks.mod("stairsMordorBrick"), 7);
                setBlockAndNotifyAdequately(world, i1, j + 10, k - 6, LOTRLegacyBlocks.mod("stairsMordorBrick"), 2);
                setBlockAndNotifyAdequately(world, i1, j, k + 7, LOTRLegacyBlocks.mod("stairsMordorBrick"), 7);
                setBlockAndNotifyAdequately(world, i1, j + 2, k + 6, LOTRLegacyBlocks.mod("stairsMordorBrick"), 2);
                setBlockAndNotifyAdequately(world, i1, j + 9, k + 7, LOTRLegacyBlocks.mod("stairsMordorBrick"), 3);
                setBlockAndNotifyAdequately(world, i1, j + 9, k + 6, LOTRLegacyBlocks.mod("stairsMordorBrick"), 6);
                setBlockAndNotifyAdequately(world, i1, j + 10, k + 6, LOTRLegacyBlocks.mod("stairsMordorBrick"), 3);
            }
            for (j1 = j; j1 <= j + 4; ++j1) {
                setBlockAndNotifyAdequately(world, i - 5, j1, k - 5, LOTRLegacyBlocks.mod("brick"), 0);
                setBlockAndNotifyAdequately(world, i - 5, j1, k + 5, LOTRLegacyBlocks.mod("brick"), 0);
                setBlockAndNotifyAdequately(world, i + 5, j1, k - 5, LOTRLegacyBlocks.mod("brick"), 0);
                setBlockAndNotifyAdequately(world, i + 5, j1, k + 5, LOTRLegacyBlocks.mod("brick"), 0);
            }
            placeBanner(world, i - 5, j + 5, k - 5, 0, "MORDOR");
            placeBanner(world, i - 5, j + 5, k + 5, 0, "MORDOR");
            placeBanner(world, i + 5, j + 5, k - 5, 0, "MORDOR");
            placeBanner(world, i + 5, j + 5, k + 5, 0, "MORDOR");
            setBlockAndNotifyAdequately(world, i - 5, j + 2, k - 4, LOTRLegacyBlocks.mod("stairsMordorBrick"), 3);
            setBlockAndNotifyAdequately(world, i - 4, j + 2, k - 5, LOTRLegacyBlocks.mod("stairsMordorBrick"), 1);
            setBlockAndNotifyAdequately(world, i - 5, j + 2, k + 4, LOTRLegacyBlocks.mod("stairsMordorBrick"), 2);
            setBlockAndNotifyAdequately(world, i - 4, j + 2, k + 5, LOTRLegacyBlocks.mod("stairsMordorBrick"), 1);
            setBlockAndNotifyAdequately(world, i + 5, j + 2, k - 4, LOTRLegacyBlocks.mod("stairsMordorBrick"), 3);
            setBlockAndNotifyAdequately(world, i + 4, j + 2, k - 5, LOTRLegacyBlocks.mod("stairsMordorBrick"), 0);
            setBlockAndNotifyAdequately(world, i + 5, j + 2, k + 4, LOTRLegacyBlocks.mod("stairsMordorBrick"), 2);
            setBlockAndNotifyAdequately(world, i + 4, j + 2, k + 5, LOTRLegacyBlocks.mod("stairsMordorBrick"), 0);
        }
    }
}
