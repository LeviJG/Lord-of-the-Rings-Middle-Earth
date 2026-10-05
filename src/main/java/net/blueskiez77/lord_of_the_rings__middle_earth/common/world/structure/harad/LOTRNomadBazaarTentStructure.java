package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRFoods;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRNomadArmourerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRNomadBrewerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRNomadMasonEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRNomadMinerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRNomadBazaarTentStructure extends LOTRNomadStructure {
    public static Class<?>[] stalls = {Mason.class, Brewer.class, Miner.class, Armourer.class};

    public LOTRNomadBazaarTentStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        int j1;
        setOriginAndRotation(world, i, j, k, rotation, 7);
        setupRandomBlocks(random);
        if (restrictions) {
            int minHeight = 0;
            int maxHeight = 0;
            for (int i1 = -14; i1 <= 14; ++i1) {
                for (int k1 = -6; k1 <= 8; ++k1) {
                    j1 = getTopBlock(world, i1, k1) - 1;
                    if (!isSurface(world, i1, j1, k1)) {
                        return false;
                    }
                    if (j1 < minHeight) {
                        minHeight = j1;
                    }
                    if (j1 > maxHeight) {
                        maxHeight = j1;
                    }
                    if (maxHeight - minHeight <= 8) {
                        continue;
                    }
                    return false;
                }
            }
        }
        for (int i1 = -14; i1 <= 14; ++i1) {
            for (int k1 = -6; k1 <= 8; ++k1) {
                if (!isSurface(world, i1, 0, k1)) {
                    laySandBase(world, i1, 0, k1);
                }
                for (j1 = 1; j1 <= 8; ++j1) {
                    setAir(world, i1, j1, k1);
                }
            }
        }
        loadStrScan("nomad_bazaar");
        associateBlockMetaAlias("PLANK", plankBlock, plankMeta);
        associateBlockMetaAlias("PLANK_SLAB", plankSlabBlock, plankSlabMeta);
        associateBlockMetaAlias("PLANK_SLAB_INV", plankSlabBlock, plankSlabMeta | 8);
        associateBlockAlias("PLANK_STAIR", plankStairBlock);
        associateBlockMetaAlias("FENCE", fenceBlock, fenceMeta);
        associateBlockMetaAlias("BEAM", beamBlock, beamMeta);
        associateBlockMetaAlias("TENT", tentBlock, tentMeta);
        associateBlockMetaAlias("TENT2", tent2Block, tent2Meta);
        associateBlockMetaAlias("CARPET", carpetBlock, carpetMeta);
        associateBlockMetaAlias("CARPET2", carpet2Block, carpet2Meta);
        generateStrScan(world, random, 0, 1, 0);
        placeSkull(world, random, -8, 2, -4);
        placeBarrel(world, random, 7, 2, -4, 3, LOTRFoods.NOMAD_DRINK);
        placeBarrel(world, random, 8, 2, -4, 3, LOTRFoods.NOMAD_DRINK);
        placeAnimalJar(world, -7, 2, -4, LOTRLegacyBlocks.mod("butterflyJar"), 0, create(LOTREntities.BUTTERFLY, world));
        placeAnimalJar(world, 9, 1, 5, LOTRLegacyBlocks.mod("birdCageWood"), 0, null);
        placeAnimalJar(world, 4, 3, 2, LOTRLegacyBlocks.mod("birdCageWood"), 0, create(LOTREntities.BIRD, world));
        placeAnimalJar(world, -4, 4, 5, LOTRLegacyBlocks.mod("birdCage"), 2, create(LOTREntities.BIRD, world));
        placeAnimalJar(world, -4, 5, -1, LOTRLegacyBlocks.mod("birdCage"), 0, create(LOTREntities.BIRD, world));
        placeAnimalJar(world, 0, 5, 5, LOTRLegacyBlocks.mod("birdCageWood"), 0, create(LOTREntities.BIRD, world));
        List<Class<?>> stallClasses = new ArrayList<>(Arrays.asList(stalls));
        while (stallClasses.size() > 3) {
            stallClasses.remove(random.nextInt(stallClasses.size()));
        }
        try {
            LOTRStructureBase2 stall0 = (LOTRStructureBase2) stallClasses.get(0).getConstructor(Boolean.TYPE).newInstance(notifyChanges);
            LOTRStructureBase2 stall1 = (LOTRStructureBase2) stallClasses.get(1).getConstructor(Boolean.TYPE).newInstance(notifyChanges);
            LOTRStructureBase2 stall2 = (LOTRStructureBase2) stallClasses.get(2).getConstructor(Boolean.TYPE).newInstance(notifyChanges);
            generateSubstructure(stall0, world, random, -4, 1, 6, 0);
            generateSubstructure(stall1, world, random, 0, 1, 6, 0);
            generateSubstructure(stall2, world, random, 4, 1, 6, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static class Armourer extends LOTRStructureBase2 {
        public Armourer(boolean flag) {
            super(flag);
        }

        @Override
        public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
            setOriginAndRotation(world, i, j, k, rotation, 0);
            setBlockAndMetadata(world, 1, 1, 1, LOTRLegacyBlocks.vanilla("anvil"), 1);
            placeArmorStand(world, 0, 1, 1, 0, new ItemStack[]{LOTRLegacyItems.modStack("helmetMoredainLion", 1, 0), LOTRLegacyItems.modStack("bodyHarnedor", 1, 0), LOTRLegacyItems.modStack("legsNomad", 1, 0), LOTRLegacyItems.modStack("bootsNomad", 1, 0)});
            placeWeaponRack(world, -1, 2, -2, 2, new LOTRNomadBazaarTentStructure(false).getRandomNomadWeapon(random));
            LOTRNomadArmourerEntity trader = create(LOTREntities.NOMAD_ARMOURER, world);
            spawnNPCAndSetHome(trader, world, 0, 1, 0, 4);
            return true;
        }
    }

    public static class Brewer extends LOTRStructureBase2 {
        public Brewer(boolean flag) {
            super(flag);
        }

        @Override
        public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
            setOriginAndRotation(world, i, j, k, rotation, 0);
            setBlockAndMetadata(world, -1, 1, 1, LOTRLegacyBlocks.mod("stairsCedar"), 6);
            setBlockAndMetadata(world, -1, 2, 1, LOTRLegacyBlocks.mod("barrel"), 2);
            setBlockAndMetadata(world, 0, 1, 1, LOTRLegacyBlocks.vanilla("cauldron"), 3);
            setBlockAndMetadata(world, 1, 1, 1, LOTRLegacyBlocks.mod("stairsCedar"), 6);
            setBlockAndMetadata(world, 1, 2, 1, LOTRLegacyBlocks.mod("barrel"), 2);
            placeMug(world, random, -1, 2, -2, 0, LOTRFoods.NOMAD_DRINK);
            placeMug(world, random, 1, 2, -2, 0, LOTRFoods.NOMAD_DRINK);
            LOTRNomadBrewerEntity trader = create(LOTREntities.NOMAD_BREWER, world);
            spawnNPCAndSetHome(trader, world, 0, 1, 0, 4);
            return true;
        }
    }

    public static class Mason extends LOTRStructureBase2 {
        public Mason(boolean flag) {
            super(flag);
        }

        @Override
        public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
            setOriginAndRotation(world, i, j, k, rotation, 0);
            setBlockAndMetadata(world, -1, 1, 1, LOTRLegacyBlocks.mod("redSandstone"), 0);
            setBlockAndMetadata(world, -1, 2, 1, LOTRLegacyBlocks.mod("redSandstone"), 0);
            setBlockAndMetadata(world, -1, 3, 1, LOTRLegacyBlocks.mod("redSandstone"), 0);
            setBlockAndMetadata(world, -1, 1, 0, LOTRLegacyBlocks.vanilla("sandstone"), 0);
            setBlockAndMetadata(world, -1, 2, 0, LOTRLegacyBlocks.vanilla("sandstone"), 0);
            setBlockAndMetadata(world, 0, 1, 1, LOTRLegacyBlocks.mod("brick"), 15);
            setBlockAndMetadata(world, 0, 2, 1, LOTRLegacyBlocks.mod("slabSingle4"), 0);
            setBlockAndMetadata(world, 1, 1, 1, LOTRLegacyBlocks.mod("brick"), 15);
            setBlockAndMetadata(world, 1, 2, 1, LOTRLegacyBlocks.mod("slabSingle4"), 0);
            placeWeaponRack(world, 1, 3, 1, 6, LOTRLegacyItems.modStack("pickaxeBronze", 1, 0));
            LOTRNomadMasonEntity trader = create(LOTREntities.NOMAD_MASON, world);
            spawnNPCAndSetHome(trader, world, 0, 1, 0, 4);
            return true;
        }
    }

    public static class Miner extends LOTRStructureBase2 {
        public Miner(boolean flag) {
            super(flag);
        }

        @Override
        public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
            setOriginAndRotation(world, i, j, k, rotation, 0);
            setBlockAndMetadata(world, -1, 1, 1, LOTRLegacyBlocks.mod("oreCopper"), 0);
            setBlockAndMetadata(world, -1, 2, 1, LOTRLegacyBlocks.mod("oreTin"), 0);
            setBlockAndMetadata(world, 0, 1, 1, LOTRLegacyBlocks.mod("oreCopper"), 0);
            setBlockAndMetadata(world, 1, 1, 1, LOTRLegacyBlocks.mod("oreTin"), 0);
            setBlockAndMetadata(world, 1, 2, 1, LOTRLegacyBlocks.vanilla("lapis_ore"), 0);
            setBlockAndMetadata(world, 1, 1, 0, LOTRLegacyBlocks.vanilla("lapis_ore"), 0);
            placeWeaponRack(world, 0, 2, 1, 6, LOTRLegacyItems.modStack("pickaxeBronze", 1, 0));
            LOTRNomadMinerEntity trader = create(LOTREntities.NOMAD_MINER, world);
            spawnNPCAndSetHome(trader, world, 0, 1, 0, 4);
            return true;
        }
    }

}
