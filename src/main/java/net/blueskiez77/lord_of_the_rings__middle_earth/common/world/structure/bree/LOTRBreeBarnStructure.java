package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.bree;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.bree.LOTRBreeFarmerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRBreeBarnStructure extends LOTRBreeStructure {
    public LOTRBreeBarnStructure(boolean flag) {
        super(flag);
    }

    public static Animal getRandomAnimal(WorldGenLevel world, RandomSource random) {
        int animal = random.nextInt(4);
        switch (animal) {
            case 0:
                return create(EntityTypes.COW, world);
            case 1:
                return create(EntityTypes.PIG, world);
            case 2:
                return create(EntityTypes.SHEEP, world);
            case 3:
                return create(EntityTypes.CHICKEN, world);
            default:
                break;
        }
        return null;
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        int i1;
        int j1;
        int j12;
        int k13;
        int i12;
        int j2;
        int step;
        int k12;
        setOriginAndRotation(world, i, j, k, rotation, 8);
        setupRandomBlocks(random);
        if (restrictions) {
            for (i12 = -6; i12 <= 6; ++i12) {
                for (k12 = -9; k12 <= 9; ++k12) {
                    j12 = getTopBlock(world, i12, k12) - 1;
                    if (isSurface(world, i12, j12, k12)) {
                        continue;
                    }
                    return false;
                }
            }
        }
        for (i12 = -5; i12 <= 5; ++i12) {
            for (k12 = -7; k12 <= 7; ++k12) {
                for (j12 = 1; j12 <= 4; ++j12) {
                    setAir(world, i12, j12, k12);
                }
            }
        }
        for (i12 = -6; i12 <= 6; ++i12) {
            for (k12 = -9; k12 <= 9; ++k12) {
                for (j12 = 5; j12 <= 10; ++j12) {
                    setAir(world, i12, j12, k12);
                }
            }
        }
        for (i12 = -1; i12 <= 1; ++i12) {
            for (int k131 : new int[]{-8, 8}) {
                for (int j13 = 1; j13 <= 3; ++j13) {
                    setAir(world, i12, j13, k131);
                }
            }
        }
        loadStrScan("bree_barn");
        associateBlockMetaAlias("BRICK", brickBlock, brickMeta);
        associateBlockMetaAlias("STONE_WALL", stoneWallBlock, stoneWallMeta);
        associateBlockMetaAlias("PLANK", plankBlock, plankMeta);
        associateBlockMetaAlias("PLANK_SLAB", plankSlabBlock, plankSlabMeta);
        associateBlockMetaAlias("PLANK_SLAB_INV", plankSlabBlock, plankSlabMeta | 8);
        associateBlockMetaAlias("FENCE", fenceBlock, fenceMeta);
        associateBlockAlias("FENCE_GATE", fenceGateBlock);
        associateBlockAlias("TRAPDOOR", trapdoorBlock);
        associateBlockMetaAlias("BEAM", beamBlock, beamMeta);
        associateBlockMetaAlias("BEAM|4", beamBlock, beamMeta | 4);
        associateBlockMetaAlias("BEAM|8", beamBlock, beamMeta | 8);
        associateBlockMetaAlias("ROOF", roofBlock, roofMeta);
        associateBlockMetaAlias("ROOF_SLAB", roofSlabBlock, roofSlabMeta);
        associateBlockMetaAlias("ROOF_SLAB_INV", roofSlabBlock, roofSlabMeta | 8);
        associateBlockAlias("ROOF_STAIR", roofStairBlock);
        addBlockMetaAliasOption("THATCH_FLOOR", 1, LOTRLegacyBlocks.mod("thatchFloor"), 0);
        setBlockAliasChance("THATCH_FLOOR", 0.2f);
        addBlockMetaAliasOption("GROUND", 13, LOTRLegacyBlocks.vanilla("grass"), 0);
        addBlockMetaAliasOption("GROUND", 7, LOTRLegacyBlocks.vanilla("cobblestone"), 0);
        associateBlockMetaAlias("LEAF", LOTRLegacyBlocks.vanilla("leaves"), 4);
        generateStrScan(world, random, 0, 0, 0);
        for (i1 = -1; i1 <= 1; ++i1) {
            for (step = 0; step < 12 && !isOpaque(world, i1, j1 = -step, k13 = -8 - step); ++step) {
                setBlockAndMetadata(world, i1, j1, k13, LOTRLegacyBlocks.vanilla("grass"), 0);
                setGrassToDirt(world, i1, j1 - 1, k13);
                j2 = j1 - 1;
                while (!isOpaque(world, i1, j2, k13) && getY(j2) >= world.getMinY()) {
                    setBlockAndMetadata(world, i1, j2, k13, LOTRLegacyBlocks.vanilla("dirt"), 0);
                    setGrassToDirt(world, i1, j2 - 1, k13);
                    --j2;
                }
            }
        }
        for (i1 = -1; i1 <= 1; ++i1) {
            for (step = 0; step < 12 && !isOpaque(world, i1, j1 = -step, k13 = 8 + step); ++step) {
                setBlockAndMetadata(world, i1, j1, k13, LOTRLegacyBlocks.vanilla("grass"), 0);
                setGrassToDirt(world, i1, j1 - 1, k13);
                j2 = j1 - 1;
                while (!isOpaque(world, i1, j2, k13) && getY(j2) >= world.getMinY()) {
                    setBlockAndMetadata(world, i1, j2, k13, LOTRLegacyBlocks.vanilla("dirt"), 0);
                    setGrassToDirt(world, i1, j2 - 1, k13);
                    --j2;
                }
            }
        }
        placeChest(world, random, -4, 1, -6, 4, LOTRChestContents.BREE_HOUSE, 1 + random.nextInt(2));
        placeChest(world, random, -4, 1, -5, 4, LOTRChestContents.BREE_HOUSE, 1 + random.nextInt(2));
        placeChest(world, random, 4, 1, 5, 5, LOTRChestContents.BREE_HOUSE, 1 + random.nextInt(2));
        placeChest(world, random, 4, 1, 6, 5, LOTRChestContents.BREE_HOUSE, 1 + random.nextInt(2));
        placeChest(world, random, -4, 0, -1, 4, LOTRChestContents.BREE_TREASURE);
        placeChest(world, random, 4, 5, -5, 5, LOTRChestContents.BREE_HOUSE, 1 + random.nextInt(2));
        placeChest(world, random, -4, 5, 0, 4, LOTRChestContents.BREE_TREASURE, 1 + random.nextInt(2));
        placeChest(world, random, -4, 5, 6, 4, LOTRChestContents.BREE_TREASURE);
        LOTRBreeFarmerEntity farmer = create(LOTREntities.BREE_FARMER, world);
        spawnNPCAndSetHome(farmer, world, 0, 1, 0, 16);
        spawnAnimal(world, random, -3, 1, -2);
        spawnAnimal(world, random, 3, 1, -2);
        spawnAnimal(world, random, -3, 1, 2);
        spawnAnimal(world, random, 3, 1, 2);
        return true;
    }

    public void spawnAnimal(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        int animals = 2;
        for (int l = 0; l < animals; ++l) {
            Animal animal = getRandomAnimal(world, random);
            spawnNPCAndSetHome(animal, world, i, j, k, 0);
            animal.clearHome();
        }
    }
}
