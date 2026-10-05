package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.elf;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRFoods;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.elf.LOTRElfEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRHighElfHouseStructure extends LOTRStructureBase2 {
    public LegacyBlock brickBlock;
    public int brickMeta;
    public LegacyBlock brickSlabBlock;
    public int brickSlabMeta;
    public LegacyBlock brickStairBlock;
    public LegacyBlock brickWallBlock;
    public int brickWallMeta;
    public LegacyBlock brickCarvedBlock;
    public int brickCarvedMeta;
    public LegacyBlock pillarBlock;
    public int pillarMeta;
    public LegacyBlock stoneBlock;
    public int stoneMeta;
    public LegacyBlock stoneSlabBlock;
    public int stoneSlabMeta;
    public LegacyBlock roofBlock;
    public int roofMeta;
    public LegacyBlock roofSlabBlock;
    public int roofSlabMeta;
    public LegacyBlock roofStairBlock;
    public LegacyBlock plankBlock;
    public int plankMeta;
    public LegacyBlock plankSlabBlock;
    public int plankSlabMeta;
    public LegacyBlock plankStairBlock;
    public LegacyBlock fenceBlock;
    public int fenceMeta;
    public LegacyBlock leafBlock;
    public int leafMeta;
    public LegacyBlock tableBlock;
    public LegacyBlock bedBlock;
    public LegacyBlock barsBlock;
    public LegacyBlock torchBlock;
    public LegacyBlock chandelierBlock;
    public int chandelierMeta;
    public LegacyBlock plateBlock;
    public String bannerType;
    public LOTRChestContents.Pool chestContents;
    public LegacyBlock trapdoorBlock;

    public LOTRHighElfHouseStructure(boolean flag) {
        super(flag);
    }

    public LOTRElfEntity createElf(WorldGenLevel world) {
        return create(LOTREntities.HIGH_ELF, world);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        int k1;
        int k12;
        int i1;
        int j1;
        int j12;
        int i12;
        int i13;
        int j13;
        int j14;
        int i2;
        int k13;
        int i22;
        int meta;
        setOriginAndRotation(world, i, j, k, rotation, 1);
        setupRandomBlocks(random);
        boolean leafy = random.nextBoolean();
        if (restrictions) {
            int minHeight = 0;
            int maxHeight = 0;
            for (int i14 = -4; i14 <= 4; ++i14) {
                for (int k14 = -1; k14 <= 14; ++k14) {
                    j12 = getTopBlock(world, i14, k14) - 1;
                    if (!isSurface(world, i14, j12, k14)) {
                        return false;
                    }
                    if (j12 < minHeight) {
                        minHeight = j12;
                    }
                    if (j12 > maxHeight) {
                        maxHeight = j12;
                    }
                    if (maxHeight - minHeight <= 6) {
                        continue;
                    }
                    return false;
                }
            }
        }
        for (i12 = -3; i12 <= 3; ++i12) {
            for (k13 = 0; k13 <= 13; ++k13) {
                int j15;
                i2 = Math.abs(i12);
                for (j15 = 0; (j15 >= 0 || !isOpaque(world, i12, j15, k13)) && getY(j15) >= 0; --j15) {
                    setBlockAndMetadata(world, i12, j15, k13, brickBlock, brickMeta);
                    setGrassToDirt(world, i12, j15 - 1, k13);
                }
                for (j15 = 1; j15 <= 12; ++j15) {
                    setAir(world, i12, j15, k13);
                }
                if (i2 <= 2 && k13 >= 1 && k13 <= 12) {
                    setBlockAndMetadata(world, i12, 0, k13, stoneBlock, stoneMeta);
                }
                if (i2 > 2 || k13 != 0) {
                    continue;
                }
                setBlockAndMetadata(world, i12, 0, k13, pillarBlock, pillarMeta);
            }
        }
        for (i12 = -3; i12 <= 3; ++i12) {
            i22 = Math.abs(i12);
            if (i22 % 2 == 1) {
                setBlockAndMetadata(world, i12, 1, 0, pillarBlock, pillarMeta);
                setBlockAndMetadata(world, i12, 2, 0, brickWallBlock, brickWallMeta);
                setBlockAndMetadata(world, i12, 3, 0, brickWallBlock, brickWallMeta);
                setBlockAndMetadata(world, i12, 4, 0, pillarBlock, pillarMeta);
                if (i22 == 1) {
                    setBlockAndMetadata(world, i12, 5, 0, pillarBlock, pillarMeta);
                }
            }
            if (i22 == 0) {
                setBlockAndMetadata(world, i12, 6, 0, pillarBlock, pillarMeta);
                for (j1 = 7; j1 <= 9; ++j1) {
                    setBlockAndMetadata(world, i12, j1, 0, brickWallBlock, brickWallMeta);
                }
                for (j1 = 10; j1 <= 11; ++j1) {
                    setBlockAndMetadata(world, i12, j1, 0, pillarBlock, pillarMeta);
                }
                continue;
            }
            if (i22 > 2) {
                continue;
            }
            setBlockAndMetadata(world, i12, 6, 0, brickWallBlock, brickWallMeta);
        }
        setBlockAndMetadata(world, -2, 5, 0, brickStairBlock, 4);
        setBlockAndMetadata(world, 0, 5, 0, brickStairBlock, 6);
        setBlockAndMetadata(world, 2, 5, 0, brickStairBlock, 5);
        int[] i15 = {-3, 3};
        i22 = i15.length;
        for (j1 = 0; j1 < i22; ++j1) {
            int i16 = i15[j1];
            for (j12 = 1; j12 <= 4; ++j12) {
                setBlockAndMetadata(world, i16, j12, 1, pillarBlock, pillarMeta);
            }
            setBlockAndMetadata(world, i16 + Integer.signum(i16), 4, 1, brickWallBlock, brickWallMeta);
            setBlockAndMetadata(world, i16, 1, 2, brickStairBlock, i16 > 0 ? 4 : 5);
            setBlockAndMetadata(world, i16, 2, 2, brickBlock, brickMeta);
            setBlockAndMetadata(world, i16, 3, 2, brickBlock, brickMeta);
            setBlockAndMetadata(world, i16, 4, 2, plankStairBlock, i16 > 0 ? 0 : 1);
            setBlockAndMetadata(world, i16, 1, 3, brickStairBlock, i16 > 0 ? 0 : 1);
            setBlockAndMetadata(world, i16, 3, 3, barsBlock, 0);
            setBlockAndMetadata(world, i16, 4, 3, plankStairBlock, i16 > 0 ? 4 : 5);
            setBlockAndMetadata(world, i16, 1, 4, brickStairBlock, i16 > 0 ? 4 : 5);
            setBlockAndMetadata(world, i16, 2, 4, brickBlock, brickMeta);
            setBlockAndMetadata(world, i16, 3, 4, brickBlock, brickMeta);
            setBlockAndMetadata(world, i16, 4, 4, plankStairBlock, i16 > 0 ? 0 : 1);
            for (j12 = 1; j12 <= 4; ++j12) {
                setBlockAndMetadata(world, i16, j12, 5, pillarBlock, pillarMeta);
                setBlockAndMetadata(world, i16, j12, 8, pillarBlock, pillarMeta);
            }
            setBlockAndMetadata(world, i16, 4, 6, brickStairBlock, 7);
            setBlockAndMetadata(world, i16, 4, 7, brickStairBlock, 6);
            setBlockAndMetadata(world, i16 + Integer.signum(i16), 4, 5, brickWallBlock, brickWallMeta);
            setBlockAndMetadata(world, i16 + Integer.signum(i16), 4, 8, brickWallBlock, brickWallMeta);
            setBlockAndMetadata(world, i16, 1, 9, brickStairBlock, i16 > 0 ? 4 : 5);
            setBlockAndMetadata(world, i16, 2, 9, brickBlock, brickMeta);
            setBlockAndMetadata(world, i16, 3, 9, brickBlock, brickMeta);
            setBlockAndMetadata(world, i16, 4, 9, plankStairBlock, i16 > 0 ? 0 : 1);
            setBlockAndMetadata(world, i16, 1, 10, brickStairBlock, i16 > 0 ? 0 : 1);
            setBlockAndMetadata(world, i16, 3, 10, barsBlock, 0);
            setBlockAndMetadata(world, i16, 4, 10, plankStairBlock, i16 > 0 ? 4 : 5);
            setBlockAndMetadata(world, i16, 1, 11, brickStairBlock, i16 > 0 ? 4 : 5);
            setBlockAndMetadata(world, i16, 2, 11, brickBlock, brickMeta);
            setBlockAndMetadata(world, i16, 3, 11, brickBlock, brickMeta);
            setBlockAndMetadata(world, i16, 4, 11, plankStairBlock, i16 > 0 ? 0 : 1);
            for (j12 = 1; j12 <= 4; ++j12) {
                setBlockAndMetadata(world, i16, j12, 12, pillarBlock, pillarMeta);
            }
            setBlockAndMetadata(world, i16 + Integer.signum(i16), 4, 12, brickWallBlock, brickWallMeta);
        }
        for (i1 = -2; i1 <= 2; ++i1) {
            i22 = Math.abs(i1);
            switch (i22) {
                case 0:
                    for (j1 = 1; j1 <= 6; ++j1) {
                        setBlockAndMetadata(world, i1, j1, 13, pillarBlock, pillarMeta);
                    }
                    for (j1 = 7; j1 <= 9; ++j1) {
                        setBlockAndMetadata(world, i1, j1, 13, brickWallBlock, brickWallMeta);
                    }
                    for (j1 = 10; j1 <= 11; ++j1) {
                        setBlockAndMetadata(world, i1, j1, 13, pillarBlock, pillarMeta);
                    }
                    break;
                case 1:
                    setBlockAndMetadata(world, i1, 1, 13, brickStairBlock, 7);
                    setBlockAndMetadata(world, i1, 3, 13, barsBlock, 0);
                    setBlockAndMetadata(world, i1, 4, 13, plankStairBlock, 7);
                    break;
                case 2:
                    setBlockAndMetadata(world, i1, 1, 13, brickBlock, brickMeta);
                    setBlockAndMetadata(world, i1, 2, 13, plankBlock, plankMeta);
                    setBlockAndMetadata(world, i1, 3, 13, plankBlock, plankMeta);
                    setBlockAndMetadata(world, i1, 4, 13, plankStairBlock, i1 > 0 ? 0 : 1);
                    break;
                default:
                    break;
            }
            if (i22 < 1 || i22 > 2) {
                continue;
            }
            setBlockAndMetadata(world, i1, 5, 13, stoneSlabBlock, stoneSlabMeta | 8);
            setBlockAndMetadata(world, i1, 6, 13, brickWallBlock, brickWallMeta);
        }
        for (i1 = -2; i1 <= 2; ++i1) {
            for (k13 = 1; k13 <= 12; ++k13) {
                setBlockAndMetadata(world, i1, 5, k13, stoneBlock, stoneMeta);
            }
        }
        for (k12 = 0; k12 <= 13; ++k12) {
            LegacyBlock block = roofBlock;
            meta = roofMeta;
            LegacyBlock slabBlock = roofSlabBlock;
            int slabMeta = roofSlabMeta;
            LegacyBlock stairBlock = roofStairBlock;
            if (k12 == 1 || k12 == 12) {
                block = brickBlock;
                meta = brickMeta;
                slabBlock = brickSlabBlock;
                slabMeta = brickSlabMeta;
                stairBlock = brickStairBlock;
            }
            setBlockAndMetadata(world, -4, 5, k12, stairBlock, 1);
            setBlockAndMetadata(world, -3, 5, k12, stairBlock, 4);
            setBlockAndMetadata(world, -3, 6, k12, block, meta);
            setBlockAndMetadata(world, -3, 7, k12, block, meta);
            setBlockAndMetadata(world, -3, 8, k12, stairBlock, 1);
            setBlockAndMetadata(world, -2, 8, k12, stairBlock, 4);
            setBlockAndMetadata(world, -2, 9, k12, block, meta);
            setBlockAndMetadata(world, -2, 10, k12, stairBlock, 1);
            setBlockAndMetadata(world, -1, 10, k12, stairBlock, 4);
            setBlockAndMetadata(world, 4, 5, k12, stairBlock, 0);
            setBlockAndMetadata(world, 3, 5, k12, stairBlock, 5);
            setBlockAndMetadata(world, 3, 6, k12, block, meta);
            setBlockAndMetadata(world, 3, 7, k12, block, meta);
            setBlockAndMetadata(world, 3, 8, k12, stairBlock, 0);
            setBlockAndMetadata(world, 2, 8, k12, stairBlock, 5);
            setBlockAndMetadata(world, 2, 9, k12, block, meta);
            setBlockAndMetadata(world, 2, 10, k12, stairBlock, 0);
            setBlockAndMetadata(world, 1, 10, k12, stairBlock, 5);
            if (k12 <= 1 || k12 >= 12) {
                setBlockAndMetadata(world, -1, 11, k12, block, meta);
                setBlockAndMetadata(world, -1, 12, k12, stairBlock, 1);
                setBlockAndMetadata(world, 1, 11, k12, block, meta);
                setBlockAndMetadata(world, 1, 12, k12, stairBlock, 0);
                continue;
            }
            if (k12 <= 4 || k12 >= 9) {
                setBlockAndMetadata(world, -1, 11, k12, stairBlock, 1);
                setBlockAndMetadata(world, 1, 11, k12, stairBlock, 0);
                continue;
            }
            if (k12 == 5) {
                setBlockAndMetadata(world, -1, 11, 5, stairBlock, 3);
                setBlockAndMetadata(world, 1, 11, 5, stairBlock, 3);
                continue;
            }
            if (k12 == 8) {
                setBlockAndMetadata(world, -1, 11, 8, stairBlock, 2);
                setBlockAndMetadata(world, 1, 11, 8, stairBlock, 2);
                continue;
            }
            setBlockAndMetadata(world, -1, 11, k12, slabBlock, slabMeta);
            setBlockAndMetadata(world, 1, 11, k12, slabBlock, slabMeta);
        }
        for (k12 = 0; k12 <= 13; ++k12) {
            setBlockAndMetadata(world, 0, 11, k12, brickBlock, brickMeta);
        }
        setBlockAndMetadata(world, 0, 12, -1, brickStairBlock, 6);
        setBlockAndMetadata(world, 0, 13, -1, brickBlock, brickMeta);
        setBlockAndMetadata(world, 0, 14, -1, brickWallBlock, brickWallMeta);
        setBlockAndMetadata(world, 0, 12, 0, brickBlock, brickMeta);
        setBlockAndMetadata(world, 0, 13, 0, brickStairBlock, 3);
        setBlockAndMetadata(world, 0, 12, 1, brickBlock, brickMeta);
        setBlockAndMetadata(world, 0, 13, 1, brickSlabBlock, brickSlabMeta);
        setBlockAndMetadata(world, 0, 12, 2, brickBlock, brickMeta);
        setBlockAndMetadata(world, 0, 12, 3, brickBlock, brickMeta);
        setBlockAndMetadata(world, 0, 12, 4, brickSlabBlock, brickSlabMeta);
        setBlockAndMetadata(world, 0, 12, 5, brickSlabBlock, brickSlabMeta);
        setBlockAndMetadata(world, 0, 12, 8, brickSlabBlock, brickSlabMeta);
        setBlockAndMetadata(world, 0, 12, 9, brickSlabBlock, brickSlabMeta);
        setBlockAndMetadata(world, 0, 12, 10, brickBlock, brickMeta);
        setBlockAndMetadata(world, 0, 12, 11, brickBlock, brickMeta);
        setBlockAndMetadata(world, 0, 12, 12, brickBlock, brickMeta);
        setBlockAndMetadata(world, 0, 13, 12, brickSlabBlock, brickSlabMeta);
        setBlockAndMetadata(world, 0, 12, 13, brickBlock, brickMeta);
        setBlockAndMetadata(world, 0, 13, 13, brickStairBlock, 2);
        setBlockAndMetadata(world, 0, 12, 14, brickStairBlock, 7);
        setBlockAndMetadata(world, 0, 13, 14, brickBlock, brickMeta);
        setBlockAndMetadata(world, 0, 14, 14, brickWallBlock, brickWallMeta);
        setBlockAndMetadata(world, -2, 4, 1, brickStairBlock, 4);
        for (j13 = 1; j13 <= 4; ++j13) {
            setBlockAndMetadata(world, -2, j13, 2, LOTRLegacyBlocks.vanilla("bookshelf"), 0);
        }
        setBlockAndMetadata(world, -2, 1, 3, brickStairBlock, 4);
        placeFlowerPot(world, -2, 2, 3, getRandomFlower(world, random));
        setBlockAndMetadata(world, -2, 4, 3, stoneSlabBlock, stoneSlabMeta | 8);
        setBlockAndMetadata(world, -2, 1, 4, LOTRLegacyBlocks.vanilla("grass"), 0);
        setBlockAndMetadata(world, -1, 1, 4, trapdoorBlock, 6);
        setBlockAndMetadata(world, -2, 1, 5, trapdoorBlock, 5);
        setBlockAndMetadata(world, -2, 2, 4, fenceBlock, fenceMeta);
        setBlockAndMetadata(world, -2, 3, 4, leafBlock, leafMeta);
        setBlockAndMetadata(world, -2, 4, 4, leafBlock, leafMeta);
        setBlockAndMetadata(world, 2, 4, 1, brickStairBlock, 5);
        setBlockAndMetadata(world, 2, 1, 2, brickStairBlock, 5);
        placeMug(world, random, 2, 2, 2, 1, LOTRFoods.ELF_DRINK);
        setBlockAndMetadata(world, 2, 1, 3, tableBlock, 0);
        setBlockAndMetadata(world, 2, 4, 3, stoneSlabBlock, stoneSlabMeta | 8);
        for (j13 = 1; j13 <= 4; ++j13) {
            setBlockAndMetadata(world, 2, j13, 4, LOTRLegacyBlocks.vanilla("bookshelf"), 0);
        }
        for (i1 = -1; i1 <= 0; ++i1) {
            for (k13 = 2; k13 <= 3; ++k13) {
                setBlockAndMetadata(world, i1, 1, k13, LOTRLegacyBlocks.vanilla("carpet"), 3);
            }
        }
        int[] i17 = {5, 8};
        k13 = i17.length;
        for (meta = 0; meta < k13; ++meta) {
            int k15 = i17[meta];
            setBlockAndMetadata(world, -2, 4, k15, brickStairBlock, 4);
            for (int i18 = -1; i18 <= 1; ++i18) {
                setBlockAndMetadata(world, i18, 4, k15, brickSlabBlock, brickSlabMeta | 8);
            }
            setBlockAndMetadata(world, 2, 4, k15, brickStairBlock, 5);
            setBlockAndMetadata(world, -2, 3, k15, torchBlock, 2);
            setBlockAndMetadata(world, 2, 3, k15, torchBlock, 1);
        }
        for (i13 = 0; i13 <= 1; ++i13) {
            for (k13 = 7; k13 <= 8; ++k13) {
                setBlockAndMetadata(world, i13, 1, k13, LOTRLegacyBlocks.vanilla("carpet"), 11);
            }
        }
        setBlockAndMetadata(world, -2, 4, 10, stoneSlabBlock, stoneSlabMeta | 8);
        setBlockAndMetadata(world, 2, 4, 10, stoneSlabBlock, stoneSlabMeta | 8);
        setBlockAndMetadata(world, -2, 4, 12, brickStairBlock, 4);
        setBlockAndMetadata(world, 2, 4, 12, brickStairBlock, 5);
        spawnItemFrame(world, -3, 2, 9, 1, getElfFramedItem(random));
        spawnItemFrame(world, 3, 2, 9, 3, getElfFramedItem(random));
        spawnItemFrame(world, -3, 2, 11, 1, getElfFramedItem(random));
        spawnItemFrame(world, 3, 2, 11, 3, getElfFramedItem(random));
        if (leafy) {
            for (i13 = -2; i13 <= 2; ++i13) {
                for (k13 = 6; k13 <= 7; ++k13) {
                    if (random.nextInt(3) == 0) {
                        continue;
                    }
                    setBlockAndMetadata(world, i13, 4, k13, leafBlock, leafMeta);
                }
            }
        }
        for (i13 = 0; i13 <= 1; ++i13) {
            for (k13 = 9; k13 <= 11; ++k13) {
                setAir(world, i13, 5, k13);
            }
        }
        for (i13 = -1; i13 <= 1; ++i13) {
            for (k13 = 1; k13 <= 9; ++k13) {
                setBlockAndMetadata(world, i13, 10, k13, brickSlabBlock, brickSlabMeta | 8);
            }
            for (k13 = 10; k13 <= 12; ++k13) {
                setBlockAndMetadata(world, i13, 9, k13, brickBlock, brickMeta);
                setBlockAndMetadata(world, i13, 10, k13, brickBlock, brickMeta);
            }
        }
        for (k1 = 9; k1 <= 12; ++k1) {
            for (j14 = 6; j14 <= 8; ++j14) {
                setBlockAndMetadata(world, 2, j14, k1, brickBlock, brickMeta);
            }
        }
        for (k1 = 11; k1 <= 12; ++k1) {
            for (j14 = 6; j14 <= 8; ++j14) {
                setBlockAndMetadata(world, -2, j14, k1, brickBlock, brickMeta);
            }
        }
        setBlockAndMetadata(world, -2, 6, 1, plankStairBlock, 4);
        setBlockAndMetadata(world, -2, 7, 1, brickWallBlock, brickWallMeta);
        setBlockAndMetadata(world, -2, 8, 1, brickBlock, brickMeta);
        setBlockAndMetadata(world, -1, 9, 1, brickWallBlock, brickWallMeta);
        setBlockAndMetadata(world, -1, 10, 1, brickBlock, brickMeta);
        setBlockAndMetadata(world, 2, 6, 1, plankStairBlock, 5);
        setBlockAndMetadata(world, 2, 7, 1, brickWallBlock, brickWallMeta);
        setBlockAndMetadata(world, 2, 8, 1, brickBlock, brickMeta);
        setBlockAndMetadata(world, 1, 9, 1, brickWallBlock, brickWallMeta);
        setBlockAndMetadata(world, 1, 10, 1, brickBlock, brickMeta);
        setBlockAndMetadata(world, 0, 10, 1, brickStairBlock, 7);
        setBlockAndMetadata(world, -1, 10, 2, brickStairBlock, 4);
        setBlockAndMetadata(world, 1, 10, 2, brickStairBlock, 5);
        setBlockAndMetadata(world, 0, 10, 3, brickBlock, brickMeta);
        setBlockAndMetadata(world, 0, 9, 3, chandelierBlock, chandelierMeta);
        setBlockAndMetadata(world, -2, 6, 4, brickWallBlock, brickWallMeta);
        setBlockAndMetadata(world, -2, 7, 4, brickWallBlock, brickWallMeta);
        setBlockAndMetadata(world, -2, 8, 4, brickStairBlock, 4);
        setBlockAndMetadata(world, 2, 6, 4, brickWallBlock, brickWallMeta);
        setBlockAndMetadata(world, 2, 7, 4, brickWallBlock, brickWallMeta);
        setBlockAndMetadata(world, 2, 8, 4, brickStairBlock, 5);
        setBlockAndMetadata(world, 0, 10, 6, brickBlock, brickMeta);
        setBlockAndMetadata(world, 0, 9, 6, chandelierBlock, chandelierMeta);
        setBlockAndMetadata(world, -1, 10, 7, brickStairBlock, 4);
        setBlockAndMetadata(world, 1, 10, 7, brickStairBlock, 5);
        setBlockAndMetadata(world, -1, 10, 8, brickStairBlock, 6);
        setBlockAndMetadata(world, 1, 10, 8, brickStairBlock, 6);
        setBlockAndMetadata(world, -1, 9, 9, brickStairBlock, 4);
        setBlockAndMetadata(world, -1, 10, 9, brickBlock, brickMeta);
        setBlockAndMetadata(world, 1, 9, 9, brickStairBlock, 5);
        setBlockAndMetadata(world, 1, 10, 9, brickBlock, brickMeta);
        setBlockAndMetadata(world, -1, 8, 12, brickStairBlock, 4);
        setBlockAndMetadata(world, 1, 8, 12, brickStairBlock, 5);
        for (int j16 = 1; j16 <= 11; ++j16) {
            setBlockAndMetadata(world, 0, j16, 10, pillarBlock, pillarMeta);
        }
        setBlockAndMetadata(world, 0, 8, 10, brickCarvedBlock, brickCarvedMeta);
        placeWallBanner(world, 0, 3, 10, bannerType, 2);
        setBlockAndMetadata(world, -1, 1, 9, brickSlabBlock, brickSlabMeta);
        setBlockAndMetadata(world, -1, 1, 10, brickSlabBlock, brickSlabMeta | 8);
        setBlockAndMetadata(world, -1, 2, 11, brickSlabBlock, brickSlabMeta);
        setBlockAndMetadata(world, 0, 2, 11, brickSlabBlock, brickSlabMeta | 8);
        setBlockAndMetadata(world, 1, 3, 11, brickSlabBlock, brickSlabMeta);
        setBlockAndMetadata(world, 1, 3, 10, brickSlabBlock, brickSlabMeta | 8);
        setBlockAndMetadata(world, 1, 4, 9, brickSlabBlock, brickSlabMeta);
        setBlockAndMetadata(world, 0, 4, 9, brickSlabBlock, brickSlabMeta | 8);
        setBlockAndMetadata(world, -1, 5, 9, brickSlabBlock, brickSlabMeta);
        for (i13 = 0; i13 <= 2; ++i13) {
            setBlockAndMetadata(world, i13, 6, 8, brickWallBlock, brickWallMeta);
        }
        for (i13 = -1; i13 <= 1; ++i13) {
            for (k13 = 1; k13 <= 7; ++k13) {
                i2 = Math.abs(i13);
                int k2 = Math.abs(k13 - 4);
                if (i2 == 0 && k2 == 0) {
                    setBlockAndMetadata(world, i13, 6, k13, LOTRLegacyBlocks.vanilla("carpet"), 0);
                    continue;
                }
                if (i2 == 0 && k2 <= 2 || i2 == 1 && k2 == 0) {
                    setBlockAndMetadata(world, i13, 6, k13, LOTRLegacyBlocks.vanilla("carpet"), 3);
                    continue;
                }
                if ((i2 != 0 || k2 != 3) && (i2 != 1 || k2 != 1)) {
                    continue;
                }
                setBlockAndMetadata(world, i13, 6, k13, LOTRLegacyBlocks.vanilla("carpet"), 11);
            }
        }
        int[] i19 = {-2, 2};
        k13 = i19.length;
        for (i2 = 0; i2 < k13; ++i2) {
            int i110 = i19[i2];
            setBlockAndMetadata(world, i110, 6, 3, bedBlock, 2);
            setBlockAndMetadata(world, i110, 6, 2, bedBlock, 10);
        }
        setBlockAndMetadata(world, -2, 6, 5, plankStairBlock, 4);
        placeMug(world, random, -2, 7, 5, 3, LOTRFoods.ELF_DRINK);
        setBlockAndMetadata(world, 2, 6, 5, plankStairBlock, 7);
        setBlockAndMetadata(world, 2, 6, 6, plankSlabBlock, plankSlabMeta | 8);
        setBlockAndMetadata(world, 2, 6, 7, plankStairBlock, 6);
        placeChest(world, random, 2, 7, 5, 5, chestContents);
        placePlateWithCertainty(world, random, 2, 7, 6, plateBlock, LOTRFoods.ELF);
        placeBarrel(world, random, 2, 7, 7, 5, LOTRFoods.ELF_DRINK);
        if (leafy) {
            for (int i111 = -4; i111 <= 4; ++i111) {
                for (k13 = 0; k13 <= 13; ++k13) {
                    for (j1 = 5; j1 <= 12; ++j1) {
                        if (Math.abs(i111) < 3 && j1 < 11 || random.nextInt(4) != 0 || !isAir(world, i111, j1, k13) || !isSolidRoofBlock(world, i111, j1 - 1, k13) && !isSolidRoofBlock(world, i111 - 1, j1, k13) && !isSolidRoofBlock(world, i111 + 1, j1, k13)) {
                            continue;
                        }
                        setBlockAndMetadata(world, i111, j1, k13, leafBlock, leafMeta);
                    }
                }
            }
        }
        int elves = 1 + random.nextInt(2);
        for (int l = 0; l < elves; ++l) {
            LOTRElfEntity elf = createElf(world);
            spawnNPCAndSetHome(elf, world, 0, 1, 7, 16);
        }
        return true;
    }

    public ItemStack getElfFramedItem(RandomSource random) {
        ItemStack[] items = {LOTRLegacyItems.modStack("helmetHighElven", 1, 0), LOTRLegacyItems.modStack("bodyHighElven", 1, 0), LOTRLegacyItems.modStack("legsHighElven", 1, 0), LOTRLegacyItems.modStack("bootsHighElven", 1, 0), LOTRLegacyItems.modStack("daggerHighElven", 1, 0), LOTRLegacyItems.modStack("swordHighElven", 1, 0), LOTRLegacyItems.modStack("spearHighElven", 1, 0), LOTRLegacyItems.modStack("longspearHighElven", 1, 0), LOTRLegacyItems.modStack("highElvenBow", 1, 0), LOTRLegacyItems.vanillaStack("arrow", 1, 0), LOTRLegacyItems.vanillaStack("feather", 1, 0), LOTRLegacyItems.modStack("swanFeather", 1, 0), LOTRLegacyItems.modStack("quenditeCrystal", 1, 0), LOTRLegacyItems.modStack("goldRing", 1, 0), LOTRLegacyItems.modStack("silverRing", 1, 0)};
        return items[random.nextInt(items.length)].copy();
    }

    public boolean isSolidRoofBlock(WorldGenLevel world, int i, int j, int k) {
        return getBlockState(world, i, j, k).canOcclude();
    }

    @Override
    public void setupRandomBlocks(RandomSource random) {
        brickBlock = LOTRLegacyBlocks.mod("brick3");
        brickMeta = 2;
        brickSlabBlock = LOTRLegacyBlocks.mod("slabSingle5");
        brickSlabMeta = 5;
        brickStairBlock = LOTRLegacyBlocks.mod("stairsHighElvenBrick");
        brickWallBlock = LOTRLegacyBlocks.mod("wall2");
        brickWallMeta = 11;
        brickCarvedBlock = LOTRLegacyBlocks.mod("brick2");
        brickCarvedMeta = 13;
        pillarBlock = LOTRLegacyBlocks.mod("pillar");
        pillarMeta = 10;
        stoneBlock = LOTRLegacyBlocks.mod("smoothStoneV");
        stoneMeta = 0;
        stoneSlabBlock = LOTRLegacyBlocks.vanilla("stone_slab");
        stoneSlabMeta = 0;
        int randomRoof = random.nextInt(5);
        switch (randomRoof) {
            case 0:
                roofBlock = LOTRLegacyBlocks.mod("clayTileDyed");
                roofMeta = 11;
                roofSlabBlock = LOTRLegacyBlocks.mod("slabClayTileDyedSingle2");
                roofSlabMeta = 3;
                roofStairBlock = LOTRLegacyBlocks.mod("stairsClayTileDyedBlue");
                break;
            case 1:
                roofBlock = LOTRLegacyBlocks.mod("clayTileDyed");
                roofMeta = 3;
                roofSlabBlock = LOTRLegacyBlocks.mod("slabClayTileDyedSingle");
                roofSlabMeta = 3;
                roofStairBlock = LOTRLegacyBlocks.mod("stairsClayTileDyedLightBlue");
                break;
            case 2:
                roofBlock = LOTRLegacyBlocks.mod("clayTileDyed");
                roofMeta = 9;
                roofSlabBlock = LOTRLegacyBlocks.mod("slabClayTileDyedSingle2");
                roofSlabMeta = 1;
                roofStairBlock = LOTRLegacyBlocks.mod("stairsClayTileDyedCyan");
                break;
            case 3:
                roofBlock = LOTRLegacyBlocks.mod("clayTileDyed");
                roofMeta = 8;
                roofSlabBlock = LOTRLegacyBlocks.mod("slabClayTileDyedSingle2");
                roofSlabMeta = 0;
                roofStairBlock = LOTRLegacyBlocks.mod("stairsClayTileDyedLightGray");
                break;
            case 4:
                roofBlock = LOTRLegacyBlocks.mod("clayTileDyed");
                roofMeta = 7;
                roofSlabBlock = LOTRLegacyBlocks.mod("slabClayTileDyedSingle");
                roofSlabMeta = 7;
                roofStairBlock = LOTRLegacyBlocks.mod("stairsClayTileDyedGray");
                break;
            default:
                break;
        }
        int randomWood = random.nextInt(4);
        switch (randomWood) {
            case 0:
                plankBlock = LOTRLegacyBlocks.vanilla("planks");
                plankMeta = 0;
                plankSlabBlock = LOTRLegacyBlocks.vanilla("wooden_slab");
                plankSlabMeta = 0;
                plankStairBlock = LOTRLegacyBlocks.vanilla("oak_stairs");
                fenceBlock = LOTRLegacyBlocks.vanilla("fence");
                fenceMeta = 0;
                trapdoorBlock = LOTRLegacyBlocks.vanilla("trapdoor");
                break;
            case 1:
                plankBlock = LOTRLegacyBlocks.vanilla("planks");
                plankMeta = 2;
                plankSlabBlock = LOTRLegacyBlocks.vanilla("wooden_slab");
                plankSlabMeta = 2;
                plankStairBlock = LOTRLegacyBlocks.vanilla("birch_stairs");
                fenceBlock = LOTRLegacyBlocks.vanilla("fence");
                fenceMeta = 2;
                trapdoorBlock = LOTRLegacyBlocks.mod("trapdoorBirch");
                break;
            case 2:
                plankBlock = LOTRLegacyBlocks.mod("planks");
                plankMeta = 9;
                plankSlabBlock = LOTRLegacyBlocks.mod("woodSlabSingle2");
                plankSlabMeta = 1;
                plankStairBlock = LOTRLegacyBlocks.mod("stairsBeech");
                fenceBlock = LOTRLegacyBlocks.mod("fence");
                fenceMeta = 9;
                trapdoorBlock = LOTRLegacyBlocks.mod("trapdoorBeech");
                break;
            case 3:
                plankBlock = LOTRLegacyBlocks.mod("planks");
                plankMeta = 4;
                plankSlabBlock = LOTRLegacyBlocks.mod("woodSlabSingle");
                plankSlabMeta = 4;
                plankStairBlock = LOTRLegacyBlocks.mod("stairsApple");
                fenceBlock = LOTRLegacyBlocks.mod("fence");
                fenceMeta = 4;
                trapdoorBlock = LOTRLegacyBlocks.mod("trapdoorApple");
                break;
            default:
                break;
        }
        int randomLeaf = random.nextInt(3);
        switch (randomLeaf) {
            case 0:
                leafBlock = LOTRLegacyBlocks.vanilla("leaves");
                leafMeta = 4;
                break;
            case 1:
                leafBlock = LOTRLegacyBlocks.vanilla("leaves");
                leafMeta = 6;
                break;
            case 2:
                leafBlock = LOTRLegacyBlocks.mod("leaves2");
                leafMeta = 5;
                break;
            default:
                break;
        }
        tableBlock = LOTRLegacyBlocks.mod("highElvenTable");
        bedBlock = LOTRLegacyBlocks.mod("highElvenBed");
        barsBlock = LOTRLegacyBlocks.mod("highElfWoodBars");
        torchBlock = LOTRLegacyBlocks.mod("highElvenTorch");
        chandelierBlock = LOTRLegacyBlocks.mod("chandelier");
        chandelierMeta = 10;
        plateBlock = LOTRLegacyBlocks.mod("plateBlock");
        bannerType = "HIGH_ELF";
        chestContents = LOTRChestContents.HIGH_ELVEN_HALL;
    }
}
