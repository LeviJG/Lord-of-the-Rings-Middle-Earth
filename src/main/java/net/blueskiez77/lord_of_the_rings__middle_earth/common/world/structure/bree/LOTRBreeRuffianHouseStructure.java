package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.bree;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRFoods;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNames;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.bree.LOTRBreeRuffianEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LOTRBreeRuffianHouseStructure extends LOTRBreeStructure {
    public String fixedName;

    public LOTRBreeRuffianHouseStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        int k1;
        int i1;
        int j2;
        int j1;
        LOTRBreeRuffianEntity ruffian;
        int i12;
        int randPath;
        int j12;
        int i13;
        int k12;
        setOriginAndRotation(world, i, j, k, rotation, 9);
        setupRandomBlocks(random);
        if (restrictions) {
            for (i13 = -7; i13 <= 8; ++i13) {
                for (k12 = -8; k12 <= 5; ++k12) {
                    j12 = getTopBlock(world, i13, k12) - 1;
                    if (isSurface(world, i13, j12, k12)) {
                        continue;
                    }
                    return false;
                }
            }
        }
        for (i13 = -3; i13 <= 8; ++i13) {
            for (k12 = -5; k12 <= 3; ++k12) {
                for (j12 = 1; j12 <= 8; ++j12) {
                    setAir(world, i13, j12, k12);
                }
            }
        }
        for (i13 = -2; i13 <= 2; ++i13) {
            for (k12 = -8; k12 <= -6; ++k12) {
                for (j12 = 1; j12 <= 8; ++j12) {
                    setAir(world, i13, j12, k12);
                }
            }
        }
        for (i13 = 0; i13 <= 7; ++i13) {
            for (k12 = 3; k12 <= 5; ++k12) {
                for (j12 = 1; j12 <= 8; ++j12) {
                    setAir(world, i13, j12, k12);
                }
            }
        }
        for (i13 = -7; i13 <= -3; ++i13) {
            for (k12 = -4; k12 <= 2; ++k12) {
                for (j12 = 1; j12 <= 8; ++j12) {
                    setAir(world, i13, j12, k12);
                }
            }
        }
        for (i13 = 0; i13 <= 5; ++i13) {
            for (k12 = -4; k12 <= 4; ++k12) {
                for (j12 = -2; j12 <= 0; ++j12) {
                    setAir(world, i13, j12, k12);
                }
            }
        }
        for (i13 = -2; i13 <= -1; ++i13) {
            k12 = 0;
            for (j12 = -2; j12 <= 0; ++j12) {
                setAir(world, i13, j12, k12);
            }
        }
        loadStrScan("bree_ruffian_house");
        associateBlockMetaAlias("BRICK", brickBlock, brickMeta);
        addBlockMetaAliasOption("COBBLE", 3, LOTRLegacyBlocks.vanilla("cobblestone"), 0);
        addBlockMetaAliasOption("COBBLE", 1, LOTRLegacyBlocks.vanilla("mossy_cobblestone"), 0);
        addBlockMetaAliasOption("COBBLE_SLAB_INV", 3, LOTRLegacyBlocks.vanilla("stone_slab"), 11);
        addBlockMetaAliasOption("COBBLE_SLAB_INV", 1, LOTRLegacyBlocks.mod("slabSingleV"), 12);
        addBlockAliasOption("COBBLE_STAIR", 3, LOTRLegacyBlocks.vanilla("stone_stairs"));
        addBlockAliasOption("COBBLE_STAIR", 1, LOTRLegacyBlocks.mod("stairsCobblestoneMossy"));
        addBlockMetaAliasOption("COBBLE_WALL", 3, LOTRLegacyBlocks.vanilla("cobblestone_wall"), 0);
        addBlockMetaAliasOption("COBBLE_WALL", 1, LOTRLegacyBlocks.vanilla("cobblestone_wall"), 1);
        associateBlockMetaAlias("PLANK", plankBlock, plankMeta);
        associateBlockMetaAlias("PLANK_SLAB", plankSlabBlock, plankSlabMeta);
        associateBlockMetaAlias("PLANK_SLAB_INV", plankSlabBlock, plankSlabMeta | 8);
        associateBlockAlias("PLANK_STAIR", plankStairBlock);
        associateBlockMetaAlias("FENCE", fenceBlock, fenceMeta);
        associateBlockAlias("FENCE_GATE", fenceGateBlock);
        associateBlockAlias("DOOR", doorBlock);
        associateBlockAlias("TRAPDOOR", trapdoorBlock);
        associateBlockMetaAlias("BEAM", beamBlock, beamMeta);
        associateBlockMetaAlias("BEAM|4", beamBlock, beamMeta | 4);
        associateBlockMetaAlias("BEAM|8", beamBlock, beamMeta | 8);
        associateBlockMetaAlias("ROOF", roofBlock, roofMeta);
        associateBlockMetaAlias("ROOF_SLAB", roofSlabBlock, roofSlabMeta);
        associateBlockMetaAlias("ROOF_SLAB_INV", roofSlabBlock, roofSlabMeta | 8);
        associateBlockAlias("ROOF_STAIR", roofStairBlock);
        associateBlockMetaAlias("TABLE", tableBlock, 0);
        associateBlockMetaAlias("CARPET", carpetBlock, carpetMeta);
        addBlockMetaAliasOption("THATCH_FLOOR", 1, LOTRLegacyBlocks.mod("thatchFloor"), 0);
        setBlockAliasChance("THATCH_FLOOR", 0.2f);
        addBlockMetaAliasOption("LEAF_FLOOR", 1, LOTRLegacyBlocks.mod("fallenLeaves"), 0);
        setBlockAliasChance("LEAF_FLOOR", 0.3f);
        addBlockMetaAliasOption("WEB", 1, LOTRLegacyBlocks.vanilla("web"), 0);
        setBlockAliasChance("WEB", 0.3f);
        addBlockMetaAliasOption("PATH", 10, LOTRLegacyBlocks.vanilla("grass"), 0);
        addBlockMetaAliasOption("PATH", 10, LOTRLegacyBlocks.vanilla("dirt"), 1);
        addBlockMetaAliasOption("PATH", 10, LOTRLegacyBlocks.mod("dirtPath"), 0);
        addBlockMetaAliasOption("PATH", 5, LOTRLegacyBlocks.vanilla("cobblestone"), 0);
        addBlockMetaAliasOption("PATH", 5, LOTRLegacyBlocks.vanilla("mossy_cobblestone"), 0);
        associateBlockMetaAlias("LEAF", LOTRLegacyBlocks.vanilla("leaves"), 4);
        generateStrScan(world, random, 0, 0, 0);
        for (i1 = 4; i1 <= 6; ++i1) {
            for (int step = 0; step < 12 && !isOpaque(world, i1, j1 = -1 - step, k1 = 5 + step); ++step) {
                randPath = random.nextInt(4);
                switch (randPath) {
                    case 0:
                        setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("grass"), 0);
                        break;
                    case 1:
                        setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("dirt"), 1);
                        break;
                    case 2:
                        setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.mod("dirtPath"), 0);
                        break;
                    case 3:
                        if (random.nextBoolean()) {
                            setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("cobblestone"), 0);
                        } else {
                            setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("mossy_cobblestone"), 0);
                        }
                        break;
                    default:
                        break;
                }
                setGrassToDirt(world, i1, j1 - 1, k1);
                j2 = j1 - 1;
                while (!isOpaque(world, i1, j2, k1) && getY(j2) >= 0) {
                    setBlockAndMetadata(world, i1, j2, k1, LOTRLegacyBlocks.vanilla("dirt"), 0);
                    setGrassToDirt(world, i1, j2 - 1, k1);
                    --j2;
                }
            }
        }
        for (int step = 0; step < 12 && !isOpaque(world, i12 = -5, j1 = -step, k1 = -5 - step); ++step) {
            randPath = random.nextInt(4);
            switch (randPath) {
                case 0:
                    setBlockAndMetadata(world, i12, j1, k1, LOTRLegacyBlocks.vanilla("grass"), 0);
                    break;
                case 1:
                    setBlockAndMetadata(world, i12, j1, k1, LOTRLegacyBlocks.vanilla("dirt"), 1);
                    break;
                case 2:
                    setBlockAndMetadata(world, i12, j1, k1, LOTRLegacyBlocks.mod("dirtPath"), 0);
                    break;
                case 3:
                    if (random.nextBoolean()) {
                        setBlockAndMetadata(world, i12, j1, k1, LOTRLegacyBlocks.vanilla("cobblestone"), 0);
                    } else {
                        setBlockAndMetadata(world, i12, j1, k1, LOTRLegacyBlocks.vanilla("mossy_cobblestone"), 0);
                    }
                    break;
                default:
                    break;
            }
            setGrassToDirt(world, i12, j1 - 1, k1);
            j2 = j1 - 1;
            while (!isOpaque(world, i12, j2, k1) && getY(j2) >= 0) {
                setBlockAndMetadata(world, i12, j2, k1, LOTRLegacyBlocks.vanilla("dirt"), 0);
                setGrassToDirt(world, i12, j2 - 1, k1);
                --j2;
            }
        }
        setBlockAndMetadata(world, 4, -2, -1, bedBlock, 3);
        setBlockAndMetadata(world, 3, -2, -1, bedBlock, 11);
        setBlockAndMetadata(world, 5, -2, 1, bedBlock, 9);
        setBlockAndMetadata(world, 4, -2, 1, bedBlock, 1);
        setBlockAndMetadata(world, 0, 5, 0, bedBlock, 3);
        setBlockAndMetadata(world, -1, 5, 0, bedBlock, 11);
        placePlateWithCertainty(world, random, 1, -1, -4, LOTRLegacyBlocks.mod("ceramicPlateBlock"), LOTRFoods.BREE);
        placeMug(world, random, 0, -1, -4, 0, LOTRFoods.BREE_DRINK);
        placeBarrel(world, random, 5, -2, -4, 5, LOTRFoods.BREE_DRINK);
        placeBarrel(world, random, 4, -2, -3, 2, LOTRFoods.BREE_DRINK);
        placeChest(world, random, 3, -2, -3, 2, LOTRChestContents.BREE_RUFFIAN_PIPEWEED);
        placeChest(world, random, -2, -2, 0, 4, LOTRChestContents.BREE_TREASURE);
        placeChest(world, random, 3, -2, 1, 2, LOTRChestContents.BREE_TREASURE);
        placePlateWithCertainty(world, random, 3, 2, -3, LOTRLegacyBlocks.mod("plateBlock"), LOTRFoods.BREE);
        placeMug(world, random, 3, 2, -2, 3, LOTRFoods.BREE_DRINK);
        placeChest(world, random, -1, 1, 1, 4, LOTRChestContents.BREE_HOUSE);
        placeChest(world, random, 1, 5, 1, 2, LOTRChestContents.BREE_HOUSE);
        for (i1 = -6; i1 <= -3; ++i1) {
            for (int k13 = -3; k13 <= 1; ++k13) {
                j1 = 1;
                BlockState gardenBlock = getBlockState(world, i1, 0, k13);
                if (!LOTRLegacyBlocks.vanilla("grass").matches(gardenBlock) && !LOTRLegacyBlocks.vanilla("dirt").matches(gardenBlock) || random.nextInt(3) != 0) {
                    continue;
                }
                if (random.nextInt(3) == 0) {
                    setBlockAndMetadata(world, i1, j1, k13, LOTRLegacyBlocks.vanilla("double_plant"), 2);
                    setBlockAndMetadata(world, i1, j1 + 1, k13, LOTRLegacyBlocks.vanilla("double_plant"), 10);
                    continue;
                }
                plantTallGrass(world, random, i1, j1, k13);
            }
        }
        ruffian = random.nextInt(3) == 0 ? create(LOTREntities.RUFFIAN_BRUTE, world) : create(LOTREntities.RUFFIAN_SPY, world);
        if (fixedName != null) {
            ruffian.familyInfo.setName(fixedName);
        }
        spawnNPCAndSetHome(ruffian, world, 0, 1, 0, 16);
        placeSign(world, 2, 2, -8, LOTRLegacyBlocks.vanilla("standing_sign"), 9, LOTRNames.getBreeRuffianSign(random));
        return true;
    }

    public LOTRBreeRuffianHouseStructure setRuffianName(String name) {
        fixedName = name;
        return this;
    }
}
