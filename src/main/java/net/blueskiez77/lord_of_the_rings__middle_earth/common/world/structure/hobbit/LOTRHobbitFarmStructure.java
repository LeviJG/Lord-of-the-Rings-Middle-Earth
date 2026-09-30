package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.hobbit;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRHobbitFarmerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRHobbitFarmhandEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LOTRHobbitFarmStructure extends LOTRStructureBase2 {
    public LegacyBlock wood1Block;
    public int wood1Meta;
    public LegacyBlock wood1Stair;
    public LegacyBlock beam1Block;
    public int beam1Meta;
    public LegacyBlock wood2Block;
    public int wood2Meta;
    public LegacyBlock cropBlock;
    public int cropMeta;
    public Item seedItem;

    public LOTRHobbitFarmStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        int j1;
        int j12;
        int k16;
        int i1;
        int i12;
        BlockState block;
        int k12;
        int i13;
        int k13;
        int i14;
        int k14;
        int k15;
        setOriginAndRotation(world, i, j, k, rotation, 6);
        int randomWood = random.nextInt(4);
        switch (randomWood) {
            case 0: {
                wood1Block = LOTRLegacyBlocks.vanilla("planks");
                wood1Meta = 0;
                wood1Stair = LOTRLegacyBlocks.vanilla("oak_stairs");
                beam1Block = LOTRLegacyBlocks.mod("woodBeamV1");
                beam1Meta = 0;
                break;
            }
            case 1: {
                wood1Block = LOTRLegacyBlocks.vanilla("planks");
                wood1Meta = 2;
                wood1Stair = LOTRLegacyBlocks.vanilla("birch_stairs");
                beam1Block = LOTRLegacyBlocks.mod("woodBeamV1");
                beam1Meta = 2;
                break;
            }
            case 2: {
                wood1Block = LOTRLegacyBlocks.mod("planks");
                wood1Meta = 0;
                wood1Stair = LOTRLegacyBlocks.mod("stairsShirePine");
                beam1Block = LOTRLegacyBlocks.mod("woodBeam1");
                beam1Meta = 0;
                break;
            }
            case 3: {
                wood1Block = LOTRLegacyBlocks.mod("planks");
                wood1Meta = 4;
                wood1Stair = LOTRLegacyBlocks.mod("stairsApple");
                beam1Block = LOTRLegacyBlocks.mod("woodBeamFruit");
                beam1Meta = 0;
            }
        }
        int randomWood2 = random.nextInt(2);
        switch (randomWood2) {
            case 0: {
                wood2Block = LOTRLegacyBlocks.vanilla("planks");
                wood2Meta = 1;
                break;
            }
            case 1: {
                wood2Block = LOTRLegacyBlocks.mod("planks");
                wood2Meta = 6;
            }
        }
        int randomCrop = random.nextInt(8);
        switch (randomCrop) {
            case 0: {
                cropBlock = LOTRLegacyBlocks.vanilla("wheat");
                cropMeta = 7;
                seedItem = LOTRLegacyItems.vanilla("wheat_seeds", 0);
                break;
            }
            case 1: {
                cropBlock = LOTRLegacyBlocks.vanilla("carrots");
                cropMeta = 7;
                seedItem = LOTRLegacyItems.vanilla("carrot", 0);
                break;
            }
            case 2: {
                cropBlock = LOTRLegacyBlocks.vanilla("potatoes");
                cropMeta = 7;
                seedItem = LOTRLegacyItems.vanilla("potato", 0);
                break;
            }
            case 3: {
                cropBlock = LOTRLegacyBlocks.mod("lettuceCrop");
                cropMeta = 7;
                seedItem = LOTRLegacyItems.mod("lettuce", 0);
                break;
            }
            case 4: {
                cropBlock = LOTRLegacyBlocks.mod("pipeweedCrop");
                cropMeta = 7;
                seedItem = LOTRLegacyItems.mod("pipeweedSeeds", 0);
                break;
            }
            case 5: {
                cropBlock = LOTRLegacyBlocks.mod("cornStalk");
                cropMeta = 0;
                seedItem = LOTRLegacyBlocks.mod("cornStalk").state().getBlock().asItem();
                break;
            }
            case 6: {
                cropBlock = LOTRLegacyBlocks.mod("leekCrop");
                cropMeta = 7;
                seedItem = LOTRLegacyItems.mod("leek", 0);
                break;
            }
            case 7: {
                cropBlock = LOTRLegacyBlocks.mod("turnipCrop");
                cropMeta = 7;
                seedItem = LOTRLegacyItems.mod("turnip", 0);
            }
        }
        if (restrictions) {
            int minHeight = 1;
            int maxHeight = 1;
            for (i14 = -5; i14 <= 10; ++i14) {
                for (k16 = -7; k16 <= 8; ++k16) {
                    j12 = getTopBlock(world, i14, k16);
                    block = getBlockState(world, i14, j12 - 1, k16);
                    if (!LOTRLegacyBlocks.vanilla("grass").matches(block) && !LOTRLegacyBlocks.vanilla("dirt").matches(block)
                            && !LOTRLegacyBlocks.vanilla("stone").matches(block)) {
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
            if (Math.abs(maxHeight - minHeight) > 6) {
                return false;
            }
        }
        for (int i15 = -5; i15 <= 10; ++i15) {
            for (k13 = -7; k13 <= 8; ++k13) {
                for (j1 = 1; j1 <= 10; ++j1) {
                    setAir(world, i15, j1, k13);
                }
                setBlockAndMetadata(world, i15, 0, k13, LOTRLegacyBlocks.vanilla("grass"), 0);
                setGrassToDirt(world, i15, -1, k13);
                j1 = -1;
                while (!isOpaque(world, i15, j1, k13) && getY(j1) >= 0) {
                    setBlockAndMetadata(world, i15, j1, k13, LOTRLegacyBlocks.vanilla("dirt"), 0);
                    setGrassToDirt(world, i15, j1 - 1, k13);
                    --j1;
                }
            }
        }
        for (k14 = -5; k14 <= 6; ++k14) {
            for (i12 = -5; i12 <= 4; ++i12) {
                if (k14 != -5 && k14 != 6 && i12 != -5 && i12 != 4) {
                    continue;
                }
                for (j1 = 1; j1 <= 5; ++j1) {
                    setBlockAndMetadata(world, i12, j1, k14, wood2Block, wood2Meta);
                    setGrassToDirt(world, i12, j1 - 1, k14);
                }
            }
        }
        for (int stair = 0; stair <= 4; ++stair) {
            int j13 = 5 + stair;
            for (i14 = -5 + stair; i14 <= 4 - stair; ++i14) {
                for (int k17 : new int[]{-5, 6}) {
                    setBlockAndMetadata(world, i14, j13, k17, wood2Block, wood2Meta);
                }
            }
            for (k12 = -6; k12 <= 7; ++k12) {
                setBlockAndMetadata(world, -6 + stair, j13, k12, LOTRLegacyBlocks.mod("stairsThatch"), 1);
                setBlockAndMetadata(world, 5 - stair, j13, k12, LOTRLegacyBlocks.mod("stairsThatch"), 0);
            }
        }
        for (k14 = -4; k14 <= 5; ++k14) {
            for (i12 = -4; i12 <= 3; ++i12) {
                setBlockAndMetadata(world, i12, 5, k14, wood1Block, wood1Meta);
            }
        }
        for (int j14 = 1; j14 <= 5; ++j14) {
            for (int k18 : new int[]{-5, 6}) {
                setBlockAndMetadata(world, -5, j14, k18, beam1Block, beam1Meta);
                setBlockAndMetadata(world, -2, j14, k18, wood1Block, wood1Meta);
                setBlockAndMetadata(world, 1, j14, k18, wood1Block, wood1Meta);
                setBlockAndMetadata(world, 4, j14, k18, beam1Block, beam1Meta);
            }
            int[] i16 = {-5, 4};
            k12 = i16.length;
            for (k16 = 0; k16 < k12; ++k16) {
                i1 = i16[k16];
                setBlockAndMetadata(world, i1, j14, -1, beam1Block, beam1Meta);
                setBlockAndMetadata(world, i1, j14, 2, beam1Block, beam1Meta);
            }
        }
        for (k14 = 0; k14 <= 1; ++k14) {
            int[] i16 = {-5, 4};
            k12 = i16.length;
            for (k16 = 0; k16 < k12; ++k16) {
                i1 = i16[k16];
                setBlockAndMetadata(world, i1, 2, k14, wood1Block, wood1Meta);
                setBlockAndMetadata(world, i1, 4, k14, wood1Block, wood1Meta);
            }
        }
        int[] k19 = {-5, 6};
        i12 = k19.length;
        for (k12 = 0; k12 < i12; ++k12) {
            k16 = k19[k12];
            for (i1 = -1; i1 <= 0; ++i1) {
                setBlockAndMetadata(world, i1, 3, k16, wood1Block, wood1Meta);
                setBlockAndMetadata(world, i1, 5, k16, wood1Block, wood1Meta);
                setBlockAndMetadata(world, i1, 7, k16, LOTRLegacyBlocks.mod("glassPane"), 0);
            }
            for (i1 = -2; i1 <= 1; ++i1) {
                setBlockAndMetadata(world, i1, 0, k16, LOTRLegacyBlocks.vanilla("grass"), 0);
                for (int j15 = 1; j15 <= 3; ++j15) {
                    setBlockAndMetadata(world, i1, j15, k16, LOTRLegacyBlocks.mod("gateWooden"), 2);
                }
            }
        }
        for (i13 = -1; i13 <= 0; ++i13) {
            for (k13 = -6; k13 <= 7; ++k13) {
                setBlockAndMetadata(world, i13, 10, k13, LOTRLegacyBlocks.mod("slabSingleThatch"), 0);
            }
        }
        for (i13 = -3; i13 <= 2; ++i13) {
            setBlockAndMetadata(world, i13, 5, -6, wood1Stair, 6);
            setBlockAndMetadata(world, i13, 5, 7, wood1Stair, 7);
        }
        setBlockAndMetadata(world, -5, 5, -6, wood1Block, wood1Meta);
        setBlockAndMetadata(world, -4, 5, -6, wood1Stair, 4);
        setBlockAndMetadata(world, 3, 5, -6, wood1Stair, 5);
        setBlockAndMetadata(world, 4, 5, -6, wood1Block, wood1Meta);
        setBlockAndMetadata(world, -5, 5, 7, wood1Block, wood1Meta);
        setBlockAndMetadata(world, -4, 5, 7, wood1Stair, 4);
        setBlockAndMetadata(world, 3, 5, 7, wood1Stair, 5);
        setBlockAndMetadata(world, 4, 5, 7, wood1Block, wood1Meta);
        int[] i17 = {-4, 3};
        k13 = i17.length;
        for (k12 = 0; k12 < k13; ++k12) {
            int i18 = i17[k12];
            for (int k110 : new int[]{-1, 2}) {
                setBlockAndMetadata(world, i18, 1, k110, LOTRLegacyBlocks.vanilla("crafting_table"), 0);
                setBlockAndMetadata(world, i18, 2, k110, LOTRLegacyBlocks.vanilla("fence"), 0);
                setBlockAndMetadata(world, i18, 3, k110, LOTRLegacyBlocks.vanilla("fence"), 0);
                setBlockAndMetadata(world, i18, 4, k110, LOTRLegacyBlocks.vanilla("torch"), 5);
            }
        }
        setBlockAndMetadata(world, -4, 1, -4, LOTRLegacyBlocks.vanilla("hay_block"), 0);
        setBlockAndMetadata(world, -4, 2, -4, LOTRLegacyBlocks.vanilla("hay_block"), 0);
        setBlockAndMetadata(world, -3, 1, -4, LOTRLegacyBlocks.vanilla("hay_block"), 0);
        setBlockAndMetadata(world, -4, 1, -3, LOTRLegacyBlocks.vanilla("hay_block"), 0);
        setBlockAndMetadata(world, -4, 1, 5, LOTRLegacyBlocks.vanilla("hay_block"), 0);
        setBlockAndMetadata(world, -4, 2, 5, LOTRLegacyBlocks.vanilla("hay_block"), 0);
        setBlockAndMetadata(world, -3, 1, 5, LOTRLegacyBlocks.vanilla("hay_block"), 0);
        setBlockAndMetadata(world, -4, 1, 4, LOTRLegacyBlocks.vanilla("hay_block"), 0);
        setBlockAndMetadata(world, 3, 1, 5, LOTRLegacyBlocks.vanilla("hay_block"), 0);
        setBlockAndMetadata(world, 3, 2, 5, LOTRLegacyBlocks.vanilla("hay_block"), 0);
        setBlockAndMetadata(world, 2, 1, 5, LOTRLegacyBlocks.vanilla("hay_block"), 0);
        setBlockAndMetadata(world, 3, 1, 4, LOTRLegacyBlocks.vanilla("hay_block"), 0);
        for (int j16 = 1; j16 <= 4; ++j16) {
            setBlockAndMetadata(world, 2, j16, -3, LOTRLegacyBlocks.vanilla("fence"), 0);
        }
        setBlockAndMetadata(world, 1, 1, -4, wood1Stair, 1);
        setBlockAndMetadata(world, 2, 1, -4, wood1Block, wood1Meta);
        setBlockAndMetadata(world, 2, 2, -4, wood1Stair, 1);
        setBlockAndMetadata(world, 3, 1, -4, wood1Block, wood1Meta);
        setBlockAndMetadata(world, 3, 2, -4, wood1Block, wood1Meta);
        setBlockAndMetadata(world, 3, 2, -3, wood1Stair, 7);
        setBlockAndMetadata(world, 3, 3, -3, wood1Stair, 2);
        setBlockAndMetadata(world, 3, 3, -2, wood1Block, wood1Meta);
        setBlockAndMetadata(world, 2, 3, -2, wood1Stair, 5);
        setBlockAndMetadata(world, 2, 4, -2, wood1Stair, 0);
        setBlockAndMetadata(world, 1, 4, -2, wood1Stair, 5);
        setBlockAndMetadata(world, 1, 5, -2, wood1Stair, 0);
        setAir(world, 3, 5, -4);
        setAir(world, 3, 5, -3);
        setAir(world, 3, 5, -2);
        setAir(world, 2, 5, -2);
        for (int i19 = 0; i19 <= 2; ++i19) {
            setBlockAndMetadata(world, i19, 6, -3, LOTRLegacyBlocks.vanilla("fence"), 0);
            setBlockAndMetadata(world, i19, 6, -1, LOTRLegacyBlocks.vanilla("fence"), 0);
        }
        setBlockAndMetadata(world, 2, 6, -4, LOTRLegacyBlocks.vanilla("fence"), 0);
        setBlockAndMetadata(world, 0, 6, -2, LOTRLegacyBlocks.vanilla("fence_gate"), 3);
        for (k15 = -4; k15 <= 5; ++k15) {
            setBlockAndMetadata(world, -4, 6, k15, wood2Block, wood2Meta);
        }
        for (k15 = -1; k15 <= 5; ++k15) {
            setBlockAndMetadata(world, 3, 6, k15, wood2Block, wood2Meta);
        }
        setBlockAndMetadata(world, -2, 7, -4, LOTRLegacyBlocks.vanilla("torch"), 3);
        setBlockAndMetadata(world, 1, 7, -4, LOTRLegacyBlocks.vanilla("torch"), 3);
        setBlockAndMetadata(world, -2, 7, 5, LOTRLegacyBlocks.vanilla("torch"), 4);
        setBlockAndMetadata(world, 1, 7, 5, LOTRLegacyBlocks.vanilla("torch"), 4);
        int carpet = random.nextInt(16);
        setBlockAndMetadata(world, -1, 6, 2, LOTRLegacyBlocks.vanilla("carpet"), carpet);
        setBlockAndMetadata(world, 0, 6, 2, LOTRLegacyBlocks.vanilla("carpet"), carpet);
        setBlockAndMetadata(world, -1, 6, 3, LOTRLegacyBlocks.vanilla("carpet"), carpet);
        setBlockAndMetadata(world, 0, 6, 3, LOTRLegacyBlocks.vanilla("carpet"), carpet);
        for (k13 = 4; k13 <= 5; ++k13) {
            for (j1 = 6; j1 <= 7; ++j1) {
                setBlockAndMetadata(world, -3, j1, k13, LOTRLegacyBlocks.vanilla("bookshelf"), 0);
                setBlockAndMetadata(world, 2, j1, k13, LOTRLegacyBlocks.vanilla("bookshelf"), 0);
            }
        }
        setBlockAndMetadata(world, -3, 6, 0, wood2Block, wood2Meta);
        setBlockAndMetadata(world, -3, 7, 0, LOTRHobbitStructure.getRandomCakeBlock(random), 0);
        setBlockAndMetadata(world, -3, 6, 1, LOTRLegacyBlocks.vanilla("cauldron"), 3);
        setBlockAndMetadata(world, -3, 6, 2, LOTRLegacyBlocks.mod("hobbitOven"), 4);
        setBlockAndMetadata(world, -3, 6, 3, LOTRLegacyBlocks.vanilla("crafting_table"), 0);
        placeChest(world, random, 2, 6, 1, 5, LOTRChestContents.HOBBIT_HOLE_LARDER);
        setBlockAndMetadata(world, 2, 6, 2, LOTRLegacyBlocks.vanilla("bed"), 0);
        setBlockAndMetadata(world, 2, 6, 3, LOTRLegacyBlocks.vanilla("bed"), 8);
        for (i12 = 5; i12 <= 10; ++i12) {
            setBlockAndMetadata(world, i12, 1, -5, LOTRLegacyBlocks.vanilla("fence"), 0);
            setBlockAndMetadata(world, i12, 1, 6, LOTRLegacyBlocks.vanilla("fence"), 0);
        }
        for (k13 = -4; k13 <= 5; ++k13) {
            setBlockAndMetadata(world, 10, 1, k13, LOTRLegacyBlocks.vanilla("fence"), 0);
        }
        setBlockAndMetadata(world, 7, 1, -5, LOTRLegacyBlocks.vanilla("fence_gate"), 0);
        setBlockAndMetadata(world, 5, 2, -5, LOTRLegacyBlocks.vanilla("torch"), 5);
        setBlockAndMetadata(world, 10, 2, -5, LOTRLegacyBlocks.vanilla("torch"), 5);
        setBlockAndMetadata(world, 10, 2, -1, LOTRLegacyBlocks.vanilla("torch"), 5);
        setBlockAndMetadata(world, 10, 2, 2, LOTRLegacyBlocks.vanilla("torch"), 5);
        setBlockAndMetadata(world, 5, 2, 6, LOTRLegacyBlocks.vanilla("torch"), 5);
        setBlockAndMetadata(world, 10, 2, 6, LOTRLegacyBlocks.vanilla("torch"), 5);
        for (i12 = 5; i12 <= 9; ++i12) {
            setBlockAndMetadata(world, i12, 0, -4, LOTRLegacyBlocks.vanilla("gravel"), 0);
            setBlockAndMetadata(world, i12, 0, 5, LOTRLegacyBlocks.vanilla("gravel"), 0);
        }
        for (k13 = -3; k13 <= 4; ++k13) {
            setBlockAndMetadata(world, 4, 0, k13, LOTRLegacyBlocks.vanilla("stonebrick"), 0);
            setBlockAndMetadata(world, 5, 0, k13, LOTRLegacyBlocks.vanilla("water"), 0);
            setBlockAndMetadata(world, 5, 1, k13, LOTRLegacyBlocks.vanilla("stone_slab"), 5);
            setBlockAndMetadata(world, 9, 0, k13, LOTRLegacyBlocks.vanilla("gravel"), 0);
            for (i14 = 6; i14 <= 8; ++i14) {
                setBlockAndMetadata(world, i14, 0, k13, LOTRLegacyBlocks.vanilla("farmland"), 7);
                setBlockAndMetadata(world, i14, 1, k13, cropBlock, cropMeta);
            }
        }
        setBlockAndMetadata(world, 10, 2, 0, LOTRLegacyBlocks.vanilla("fence"), 0);
        setBlockAndMetadata(world, 10, 3, 0, LOTRLegacyBlocks.vanilla("hay_block"), 0);
        setBlockAndMetadata(world, 10, 4, 0, LOTRLegacyBlocks.vanilla("pumpkin"), 1);
        LOTRHobbitFarmerEntity farmer = create(LOTREntities.HOBBIT_FARMER, world);
        spawnNPCAndSetHome(farmer, world, 0, 6, 0, 16);
        int farmhands = 1 + random.nextInt(3);
        for (int l = 0; l < farmhands; ++l) {
            LOTRHobbitFarmhandEntity farmhand = create(LOTREntities.HOBBIT_FARMHAND, world);
            farmhand.seedsItem = seedItem;
            spawnNPCAndSetHome(farmhand, world, 7, 1, 0, 8);
        }
        int animals = 3 + random.nextInt(6);
        for (int l = 0; l < animals; ++l) {
            PathfinderMob animal = create(randomShireAnimal(random), world);
            spawnNPCAndSetHome(animal, world, 0, 1, 0, 8);
        }
        return true;
    }

    /**
     * A creature from the Shire's spawn list (LOTRBiome's domestic animals and
     * LOTRBiomeGenShire's ponies), by weight. The list moves to the Shire
     * biome with D10.
     */
    private static EntityType<? extends PathfinderMob> randomShireAnimal(RandomSource random) {
        int roll = random.nextInt(12 + 10 + 10 + 8 + 5 + 12);
        if ((roll -= 12) < 0) {
            return EntityTypes.SHEEP;
        }
        if ((roll -= 10) < 0) {
            return EntityTypes.PIG;
        }
        if ((roll -= 10) < 0) {
            return EntityTypes.CHICKEN;
        }
        if ((roll -= 8) < 0) {
            return EntityTypes.COW;
        }
        if (roll - 5 < 0) {
            return LOTREntities.DEER;
        }
        return LOTREntities.SHIRE_PONY;
    }
}
