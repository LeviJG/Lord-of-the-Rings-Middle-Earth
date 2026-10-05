package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.elf;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRFoods;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.elf.LOTRElfEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

public class LOTRHighElvenHallStructure extends LOTRStructureBase {
    public LegacyBlock plankBlock;
    public int plankMeta;
    public LegacyBlock slabBlock;
    public int slabMeta;
    public LegacyBlock stairBlock;
    public LegacyBlock roofBlock;
    public int roofMeta;
    public LegacyBlock roofStairBlock;
    public LegacyBlock tableBlock = LOTRLegacyBlocks.mod("highElvenTable");
    public LegacyBlock plateBlock = LOTRLegacyBlocks.mod("plateBlock");
    public String bannerType = "HIGH_ELF";
    public LOTRChestContents.Pool chestContents = LOTRChestContents.HIGH_ELVEN_HALL;

    public LOTRHighElvenHallStructure(boolean flag) {
        super(flag);
    }

    public LOTRElfEntity createElf(WorldGenLevel world) {
        return create(LOTREntities.HIGH_ELF, world);
    }

    @Override
    public boolean generate(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        int j1;
        int l;
        int i1;
        int k1;
        int i12;
        int k12;
        int j12;
        int i13;
        int i14;
        int k13;
        int k14;
        if (restrictions && !LOTRLegacyBlocks.vanilla("grass").matches(world.getBlockState(new BlockPos(i, j - 1, k)))) {
            return false;
        }
        --j;
        int rotation = random.nextInt(4);
        if (!restrictions && usingPlayer != null) {
            rotation = usingPlayerRotation();
        }
        switch (rotation) {
            case 0: {
                i -= 8;
                ++k;
                break;
            }
            case 1: {
                i -= 16;
                k -= 8;
                break;
            }
            case 2: {
                i -= 7;
                k -= 16;
                break;
            }
            case 3: {
                ++i;
                k -= 7;
            }
        }
        if (restrictions) {
            int minHeight = j + 1;
            int maxHeight = j + 1;
            for (i14 = i - 1; i14 <= i + 16; ++i14) {
                for (k14 = k - 1; k14 <= k + 16; ++k14) {
                    j12 = world.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, i14, k14);
                    BlockState block = world.getBlockState(new BlockPos(i14, j12 - 1, k14));
                    if (!LOTRLegacyBlocks.vanilla("grass").matches(block) && !LOTRLegacyBlocks.vanilla("dirt").matches(block) && !LOTRLegacyBlocks.vanilla("stone").matches(block)) {
                        return false;
                    }
                    if (j12 > maxHeight) {
                        maxHeight = j12;
                    }
                    if (j12 >= minHeight) {
                        continue;
                    }
                    minHeight = j12;
                }
            }
            if (Math.abs(maxHeight - minHeight) > 5) {
                return false;
            }
            int height = j + 1;
            for (i12 = i - 1; i12 <= i + 16; ++i12) {
                for (k1 = k - 1; k1 <= k + 16; ++k1) {
                    int j13;
                    if (i12 != i - 1 && i12 != i + 16 || k1 != k - 1 && k1 != k + 16 || (j13 = world.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, i12, k1)) <= height) {
                        continue;
                    }
                    height = j13;
                }
            }
            j = height - 1;
        }
        int randomWood = random.nextInt(4);
        switch (randomWood) {
            case 0: {
                plankBlock = LOTRLegacyBlocks.vanilla("planks");
                plankMeta = 0;
                slabBlock = LOTRLegacyBlocks.vanilla("wooden_slab");
                slabMeta = 0;
                stairBlock = LOTRLegacyBlocks.vanilla("oak_stairs");
                break;
            }
            case 1: {
                plankBlock = LOTRLegacyBlocks.vanilla("planks");
                plankMeta = 2;
                slabBlock = LOTRLegacyBlocks.vanilla("wooden_slab");
                slabMeta = 2;
                stairBlock = LOTRLegacyBlocks.vanilla("birch_stairs");
                break;
            }
            case 2: {
                plankBlock = LOTRLegacyBlocks.mod("planks");
                plankMeta = 9;
                slabBlock = LOTRLegacyBlocks.mod("woodSlabSingle2");
                slabMeta = 1;
                stairBlock = LOTRLegacyBlocks.mod("stairsBeech");
                break;
            }
            case 3: {
                plankBlock = LOTRLegacyBlocks.mod("planks");
                plankMeta = 4;
                slabBlock = LOTRLegacyBlocks.mod("woodSlabSingle");
                slabMeta = 4;
                stairBlock = LOTRLegacyBlocks.mod("stairsApple");
            }
        }
        int randomRoof = random.nextInt(5);
        switch (randomRoof) {
            case 0:
                roofBlock = LOTRLegacyBlocks.mod("clayTileDyed");
                roofMeta = 11;
                roofStairBlock = LOTRLegacyBlocks.mod("stairsClayTileDyedBlue");
                break;
            case 1:
                roofBlock = LOTRLegacyBlocks.mod("clayTileDyed");
                roofMeta = 3;
                roofStairBlock = LOTRLegacyBlocks.mod("stairsClayTileDyedLightBlue");
                break;
            case 2:
                roofBlock = LOTRLegacyBlocks.mod("clayTileDyed");
                roofMeta = 9;
                roofStairBlock = LOTRLegacyBlocks.mod("stairsClayTileDyedCyan");
                break;
            case 3:
                roofBlock = LOTRLegacyBlocks.mod("clayTileDyed");
                roofMeta = 8;
                roofStairBlock = LOTRLegacyBlocks.mod("stairsClayTileDyedLightGray");
                break;
            case 4:
                roofBlock = LOTRLegacyBlocks.mod("clayTileDyed");
                roofMeta = 7;
                roofStairBlock = LOTRLegacyBlocks.mod("stairsClayTileDyedGray");
                break;
            default:
                break;
        }
        for (i14 = i; i14 <= i + 15; ++i14) {
            for (k14 = k; k14 <= k + 15; ++k14) {
                for (j12 = j; (j12 == j || !isOpaqueAt(world, i14, j12, k14)) && j12 >= 0; --j12) {
                    setBlockAndNotifyAdequately(world, i14, j12, k14, LOTRLegacyBlocks.mod("brick3"), 2);
                    setGrassToDirt(world, i14, j12 - 1, k14);
                }
                for (j12 = j + 1; j12 <= j + 4; ++j12) {
                    setBlockAndNotifyAdequately(world, i14, j12, k14, LOTRLegacyBlocks.vanilla("air"), 0);
                }
                if (i14 < i + 2 || i14 > i + 13 || k14 < k + 2 || k14 > k + 13) {
                    setBlockAndNotifyAdequately(world, i14, j + 5, k14, LOTRLegacyBlocks.mod("brick3"), 2);
                } else {
                    setBlockAndNotifyAdequately(world, i14, j + 5, k14, plankBlock, plankMeta);
                }
                for (j12 = j + 6; j12 <= j + 9; ++j12) {
                    setBlockAndNotifyAdequately(world, i14, j12, k14, LOTRLegacyBlocks.vanilla("air"), 0);
                }
            }
        }
        for (i14 = i + 1; i14 <= i + 14; ++i14) {
            setBlockAndNotifyAdequately(world, i14, j + 6, k, LOTRLegacyBlocks.mod("wall2"), 11);
            setBlockAndNotifyAdequately(world, i14, j + 6, k + 15, LOTRLegacyBlocks.mod("wall2"), 11);
        }
        for (int k15 = k + 1; k15 <= k + 14; ++k15) {
            setBlockAndNotifyAdequately(world, i, j + 6, k15, LOTRLegacyBlocks.mod("brick3"), 2);
            setBlockAndNotifyAdequately(world, i, j + 7, k15, LOTRLegacyBlocks.mod("wall2"), 11);
            setBlockAndNotifyAdequately(world, i + 15, j + 6, k15, LOTRLegacyBlocks.mod("wall2"), 11);
        }
        for (int j14 = j; j14 <= j + 5; j14 += 5) {
            int j2;
            for (k14 = k; k14 <= k + 15; k14 += 15) {
                int i15;
                for (i15 = i; i15 <= i + 15; i15 += 3) {
                    for (j2 = j14 + 1; j2 <= j14 + 4; ++j2) {
                        setBlockAndNotifyAdequately(world, i15, j2, k14, LOTRLegacyBlocks.mod("pillar"), 10);
                    }
                    setBlockAndNotifyAdequately(world, i15, j14 + 5, k14, LOTRLegacyBlocks.mod("brick3"), 2);
                }
                for (i15 = i + 1; i15 <= i + 13; i15 += 3) {
                    setBlockAndNotifyAdequately(world, i15, j14 + 5, k14, LOTRLegacyBlocks.mod("stairsHighElvenBrick"), 5);
                    setBlockAndNotifyAdequately(world, i15 + 1, j14 + 5, k14, LOTRLegacyBlocks.mod("stairsHighElvenBrick"), 4);
                }
            }
            for (i12 = i; i12 <= i + 15; i12 += 15) {
                for (k1 = k + 3; k1 <= k + 12; k1 += 3) {
                    for (j2 = j14 + 1; j2 <= j14 + 4; ++j2) {
                        setBlockAndNotifyAdequately(world, i12, j2, k1, LOTRLegacyBlocks.mod("pillar"), 10);
                    }
                    setBlockAndNotifyAdequately(world, i12, j14 + 5, k1, LOTRLegacyBlocks.mod("brick3"), 2);
                }
                for (k1 = k + 1; k1 <= k + 13; k1 += 3) {
                    setBlockAndNotifyAdequately(world, i12, j14 + 5, k1, LOTRLegacyBlocks.mod("stairsHighElvenBrick"), 7);
                    setBlockAndNotifyAdequately(world, i12, j14 + 5, k1 + 1, LOTRLegacyBlocks.mod("stairsHighElvenBrick"), 6);
                }
            }
            for (i12 = i; i12 <= i + 15; i12 += 3) {
                setBlockAndNotifyAdequately(world, i12, j14 + 4, k + 1, LOTRLegacyBlocks.mod("highElvenTorch"), 3);
                setBlockAndNotifyAdequately(world, i12, j14 + 4, k + 14, LOTRLegacyBlocks.mod("highElvenTorch"), 4);
            }
            for (k14 = k; k14 <= k + 15; k14 += 3) {
                setBlockAndNotifyAdequately(world, i + 1, j14 + 4, k14, LOTRLegacyBlocks.mod("highElvenTorch"), 1);
                setBlockAndNotifyAdequately(world, i + 14, j14 + 4, k14, LOTRLegacyBlocks.mod("highElvenTorch"), 2);
            }
        }
        int roofWidth = 18;
        int roofX = i - 1;
        int roofY = j + 11;
        int roofZ = k - 1;
        while (roofWidth > 2) {
            for (i13 = roofX; i13 < roofX + roofWidth; ++i13) {
                setBlockAndNotifyAdequately(world, i13, roofY, roofZ, roofStairBlock, 2);
                setBlockAndNotifyAdequately(world, i13, roofY, roofZ + roofWidth - 1, roofStairBlock, 3);
            }
            for (k13 = roofZ; k13 < roofZ + roofWidth; ++k13) {
                setBlockAndNotifyAdequately(world, roofX, roofY, k13, roofStairBlock, 0);
                setBlockAndNotifyAdequately(world, roofX + roofWidth - 1, roofY, k13, roofStairBlock, 1);
            }
            for (i13 = roofX + 1; i13 < roofX + roofWidth - 2; ++i13) {
                for (k12 = roofZ + 1; k12 < roofZ + roofWidth - 2; ++k12) {
                    setBlockAndNotifyAdequately(world, i13, roofY, k12, LOTRLegacyBlocks.vanilla("air"), 0);
                }
            }
            for (i13 = roofX + 1; i13 < roofX + roofWidth - 1; ++i13) {
                if (roofWidth > 16) {
                    setBlockAndNotifyAdequately(world, i13, roofY, roofZ + 1, roofBlock, roofMeta);
                    setBlockAndNotifyAdequately(world, i13, roofY, roofZ + roofWidth - 2, roofBlock, roofMeta);
                    continue;
                }
                setBlockAndNotifyAdequately(world, i13, roofY, roofZ + 1, roofStairBlock, 7);
                setBlockAndNotifyAdequately(world, i13, roofY, roofZ + roofWidth - 2, roofStairBlock, 6);
            }
            for (k13 = roofZ + 1; k13 < roofZ + roofWidth - 1; ++k13) {
                if (roofWidth > 16) {
                    setBlockAndNotifyAdequately(world, roofX + 1, roofY, k13, roofBlock, roofMeta);
                    setBlockAndNotifyAdequately(world, roofX + roofWidth - 2, roofY, k13, roofBlock, roofMeta);
                    continue;
                }
                setBlockAndNotifyAdequately(world, roofX + 1, roofY, k13, roofStairBlock, 5);
                setBlockAndNotifyAdequately(world, roofX + roofWidth - 2, roofY, k13, roofStairBlock, 4);
            }
            roofWidth -= 2;
            ++roofX;
            ++roofY;
            ++roofZ;
        }
        for (i13 = roofX; i13 < roofX + roofWidth; ++i13) {
            for (k12 = roofZ; k12 < roofZ + roofWidth; ++k12) {
                setBlockAndNotifyAdequately(world, i13, roofY - 1, k12, LOTRLegacyBlocks.mod("glass"), 0);
            }
        }
        setBlockAndNotifyAdequately(world, i + 2, j + 6, k + 9, LOTRLegacyBlocks.mod("highElvenBed"), 1);
        setBlockAndNotifyAdequately(world, i + 1, j + 6, k + 9, LOTRLegacyBlocks.mod("highElvenBed"), 9);
        setBlockAndNotifyAdequately(world, i + 1, j + 6, k + 10, plankBlock, plankMeta);
        placeFlowerPot(world, i + 1, j + 7, k + 10, getRandomPlant(random));
        setBlockAndNotifyAdequately(world, i + 1, j + 6, k + 8, plankBlock, plankMeta);
        placeFlowerPot(world, i + 1, j + 7, k + 8, getRandomPlant(random));
        setBlockAndNotifyAdequately(world, i + 1, j + 6, k + 6, LOTRLegacyBlocks.vanilla("bookshelf"), 0);
        setBlockAndNotifyAdequately(world, i + 1, j + 6, k + 5, plankBlock, plankMeta);
        setBlockAndNotifyAdequately(world, i + 1, j + 6, k + 4, plankBlock, plankMeta);
        setBlockAndNotifyAdequately(world, i + 1, j + 6, k + 3, LOTRLegacyBlocks.vanilla("bookshelf"), 0);
        setBlockAndNotifyAdequately(world, i + 3, j + 6, k + 4, stairBlock, 0);
        placeMug(world, random, i + 1, j + 7, k + 4, 1, LOTRFoods.ELF_DRINK);
        setBlockAndNotifyAdequately(world, i + 11, j + 6, k + 10, plankBlock, plankMeta);
        setBlockAndNotifyAdequately(world, i + 11, j + 6, k + 11, plankBlock, plankMeta);
        for (k13 = k + 10; k13 <= k + 12; ++k13) {
            setBlockAndNotifyAdequately(world, i + 13, j + 6, k13, stairBlock, 0);
        }
        for (i13 = i + 11; i13 <= i + 13; ++i13) {
            setBlockAndNotifyAdequately(world, i13, j + 6, k + 13, stairBlock, 2);
        }
        for (k13 = k + 5; k13 <= k + 9; ++k13) {
            for (i1 = i + 7; i1 <= i + 10; ++i1) {
                setBlockAndNotifyAdequately(world, i1, j + 5, k13, LOTRLegacyBlocks.vanilla("air"), 0);
            }
        }
        for (k13 = k + 5; k13 <= k + 6; ++k13) {
            for (j1 = j + 1; j1 <= j + 4; ++j1) {
                setBlockAndNotifyAdequately(world, i + 7, j1, k13, LOTRLegacyBlocks.mod("brick3"), 2);
            }
            setBlockAndNotifyAdequately(world, i + 7, j + 5, k13, LOTRLegacyBlocks.mod("stairsHighElvenBrick"), 1);
            for (i1 = i + 8; i1 <= i + 10; ++i1) {
                for (int j15 = j + 1; j15 <= j + 3; ++j15) {
                    setBlockAndNotifyAdequately(world, i1, j15, k13, LOTRLegacyBlocks.mod("brick3"), 2);
                }
            }
            setBlockAndNotifyAdequately(world, i + 8, j + 4, k13, LOTRLegacyBlocks.mod("stairsHighElvenBrick"), 1);
        }
        for (i13 = i + 9; i13 <= i + 10; ++i13) {
            for (j1 = j + 1; j1 <= j + 2; ++j1) {
                setBlockAndNotifyAdequately(world, i13, j1, k + 7, LOTRLegacyBlocks.mod("brick3"), 2);
            }
            setBlockAndNotifyAdequately(world, i13, j + 3, k + 7, LOTRLegacyBlocks.mod("stairsHighElvenBrick"), 3);
            setBlockAndNotifyAdequately(world, i13, j + 1, k + 8, LOTRLegacyBlocks.mod("brick3"), 2);
            setBlockAndNotifyAdequately(world, i13, j + 2, k + 8, LOTRLegacyBlocks.mod("stairsHighElvenBrick"), 3);
            setBlockAndNotifyAdequately(world, i13, j + 1, k + 9, LOTRLegacyBlocks.mod("stairsHighElvenBrick"), 3);
        }
        setBlockAndNotifyAdequately(world, i + 11, j + 1, k + 5, LOTRLegacyBlocks.mod("pillar"), 10);
        setBlockAndNotifyAdequately(world, i + 11, j + 1, k + 6, tableBlock, 0);
        setBlockAndNotifyAdequately(world, i + 11, j + 1, k + 7, LOTRLegacyBlocks.mod("pillar"), 10);
        placeFlowerPot(world, i + 11, j + 2, k + 5, getRandomPlant(random));
        placeFlowerPot(world, i + 11, j + 2, k + 7, getRandomPlant(random));
        setBlockAndNotifyAdequately(world, i + 11, j + 3, k + 6, LOTRLegacyBlocks.mod("highElvenTorch"), 1);
        setBlockAndNotifyAdequately(world, i + 6, j + 3, k + 6, LOTRLegacyBlocks.mod("highElvenTorch"), 2);
        setBlockAndNotifyAdequately(world, i + 8, j + 3, k + 7, LOTRLegacyBlocks.mod("highElvenTorch"), 3);
        setBlockAndNotifyAdequately(world, i + 10, j + 1, k + 4, LOTRLegacyBlocks.mod("pillar"), 10);
        placeBarrel(world, random, i + 10, j + 2, k + 4, 2, LOTRFoods.ELF_DRINK);
        setBlockAndNotifyAdequately(world, i + 7, j + 1, k + 4, LOTRLegacyBlocks.mod("pillar"), 10);
        placeBarrel(world, random, i + 7, j + 2, k + 4, 2, LOTRFoods.ELF_DRINK);
        setBlockAndNotifyAdequately(world, i + 8, j + 1, k + 4, LOTRLegacyBlocks.vanilla("chest"), 0);
        setBlockAndNotifyAdequately(world, i + 9, j + 1, k + 4, LOTRLegacyBlocks.vanilla("chest"), 0);
        LOTRChestContents.fillChest(world, random, new BlockPos(i + 8, j + 1, k + 4), chestContents, -1);
        LOTRChestContents.fillChest(world, random, new BlockPos(i + 9, j + 1, k + 4), chestContents, -1);
        setBlockAndNotifyAdequately(world, i + 8, j + 2, k + 5, LOTRLegacyBlocks.vanilla("furnace"), 2);
        setBlockAndNotifyAdequately(world, i + 9, j + 2, k + 5, LOTRLegacyBlocks.vanilla("furnace"), 2);
        setBlockMetadata(world, i + 8, j + 2, k + 5, 2);
        setBlockMetadata(world, i + 9, j + 2, k + 5, 2);
        setBlockAndNotifyAdequately(world, i + 7, j + 1, k + 7, plankBlock, plankMeta);
        setBlockAndNotifyAdequately(world, i + 8, j + 1, k + 7, plankBlock, plankMeta);
        setBlockAndNotifyAdequately(world, i + 8, j + 1, k + 8, plankBlock, plankMeta);
        placePlateWithCertainty(world, random, i + 7, j + 2, k + 7, plateBlock, LOTRFoods.ELF);
        placePlateWithCertainty(world, random, i + 8, j + 2, k + 7, plateBlock, LOTRFoods.ELF);
        placePlateWithCertainty(world, random, i + 8, j + 2, k + 8, plateBlock, LOTRFoods.ELF);
        for (k13 = k + 6; k13 <= k + 12; ++k13) {
            for (i1 = i + 2; i1 <= i + 4; ++i1) {
                setBlockAndNotifyAdequately(world, i1, j + 1, k13, slabBlock, slabMeta | 8);
            }
        }
        for (k13 = k + 6; k13 <= k + 12; k13 += 3) {
            setBlockAndNotifyAdequately(world, i + 2, j + 1, k13, plankBlock, plankMeta);
            setBlockAndNotifyAdequately(world, i + 4, j + 1, k13, plankBlock, plankMeta);
            setBlockAndNotifyAdequately(world, i + 1, j + 1, k13, stairBlock, 1);
            setBlockAndNotifyAdequately(world, i + 5, j + 1, k13, stairBlock, 0);
        }
        setBlockAndNotifyAdequately(world, i + 3, j + 1, k + 13, stairBlock, 2);
        setBlockAndNotifyAdequately(world, i + 3, j + 1, k + 5, stairBlock, 3);
        for (k13 = k + 6; k13 <= k + 12; k13 += 2) {
            placePlateWithCertainty(world, random, i + 2, j + 2, k13, plateBlock, LOTRFoods.ELF);
            placePlateWithCertainty(world, random, i + 4, j + 2, k13, plateBlock, LOTRFoods.ELF);
        }
        for (k13 = k + 7; k13 <= k + 11; k13 += 2) {
            l = random.nextInt(3);
            switch (l) {
                case 0:
                    setBlockAndNotifyAdequately(world, i + 3, j + 2, k13, LOTRLegacyBlocks.mod("appleCrumble"), 0);
                    break;
                case 1:
                    setBlockAndNotifyAdequately(world, i + 3, j + 2, k13, LOTRLegacyBlocks.mod("cherryPie"), 0);
                    break;
                case 2:
                    setBlockAndNotifyAdequately(world, i + 3, j + 2, k13, LOTRLegacyBlocks.mod("berryPie"), 0);
                    break;
                default:
                    break;
            }
            placeMug(world, random, i + 2, j + 2, k13, 3, LOTRFoods.ELF_DRINK);
            placeMug(world, random, i + 4, j + 2, k13, 1, LOTRFoods.ELF_DRINK);
        }
        placeMug(world, random, i + 3, j + 2, k + 6, 0, LOTRFoods.ELF_DRINK);
        placeMug(world, random, i + 3, j + 2, k + 12, 2, LOTRFoods.ELF_DRINK);
        placeFlowerPot(world, i + 3, j + 2, k + 8, getRandomPlant(random));
        placeFlowerPot(world, i + 3, j + 2, k + 10, getRandomPlant(random));
        for (int j16 = j + 3; j16 <= j + 8; j16 += 5) {
            for (i1 = i + 3; i1 <= i + 12; i1 += 3) {
                placeWallBanner(world, i1, j16, k, 0, bannerType);
                placeWallBanner(world, i1, j16, k + 15, 2, bannerType);
            }
            for (k12 = k + 3; k12 <= k + 12; k12 += 3) {
                placeWallBanner(world, i, j16, k12, 3, bannerType);
                placeWallBanner(world, i + 15, j16, k12, 1, bannerType);
            }
        }
        int elves = 2 + random.nextInt(4);
        for (l = 0; l < elves; ++l) {
            LOTRElfEntity elf = createElf(world);
            elf.snapTo(i + 6, j + 6, k + 6, 0.0f, 0.0f);
            elf.spawnRidingHorse = false;
            elf.finalizeSpawn(world, world.getCurrentDifficultyAt(elf.blockPosition()), EntitySpawnReason.STRUCTURE, null);
            elf.isNPCPersistent = true;
            world.addFreshEntity(elf);
            elf.setHomeTo(new BlockPos(i + 7, j + 3, k + 7), 24);
        }
        return true;
    }

    public ItemStack getRandomPlant(RandomSource random) {
        int l = random.nextInt(5);
        switch (l) {
            case 0: {
                return LOTRLegacyBlocks.vanilla("sapling").stack(1, 0);
            }
            case 1:
            case 2: {
                return LOTRLegacyBlocks.vanilla("sapling").stack(1, 2);
            }
            case 3: {
                return LOTRLegacyBlocks.vanilla("red_flower").stack(1, 0);
            }
            case 4: {
                return LOTRLegacyBlocks.vanilla("yellow_flower").stack(1, 0);
            }
        }
        return LOTRLegacyBlocks.vanilla("sapling").stack(1, 0);
    }
}
