package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;

public abstract class LOTRGulfStructure extends LOTRStructureBase2 {
    public LegacyBlock trapdoorBlock;
    public LegacyBlock brickBlock;
    public int brickMeta;
    public LegacyBlock brickSlabBlock;
    public int brickSlabMeta;
    public LegacyBlock brickStairBlock;
    public LegacyBlock brickWallBlock;
    public int brickWallMeta;
    public LegacyBlock brick2Block;
    public int brick2Meta;
    public LegacyBlock woodBlock;
    public int woodMeta;
    public LegacyBlock plankBlock;
    public int plankMeta;
    public LegacyBlock plankSlabBlock;
    public int plankSlabMeta;
    public LegacyBlock plankStairBlock;
    public LegacyBlock fenceBlock;
    public int fenceMeta;
    public LegacyBlock fenceGateBlock;
    public LegacyBlock doorBlock;
    public LegacyBlock beamBlock;
    public int beamMeta;
    public LegacyBlock plank2Block;
    public int plank2Meta;
    public LegacyBlock plank2SlabBlock;
    public int plank2SlabMeta;
    public LegacyBlock plank2StairBlock;
    public LegacyBlock beam2Block;
    public int beam2Meta;
    public LegacyBlock roofBlock;
    public int roofMeta;
    public LegacyBlock roofSlabBlock;
    public int roofSlabMeta;
    public LegacyBlock roofStairBlock;
    public LegacyBlock flagBlock;
    public int flagMeta;
    public LegacyBlock boneBlock;
    public int boneMeta;
    public LegacyBlock boneWallBlock;
    public int boneWallMeta;
    public LegacyBlock bedBlock;

    protected LOTRGulfStructure(boolean flag) {
        super(flag);
    }

    public boolean canUseRedBrick() {
        return true;
    }

    public ItemStack getRandomGulfWeapon(RandomSource random) {
        ItemStack[] items = {LOTRLegacyItems.modStack("swordGulfHarad", 1, 0), LOTRLegacyItems.modStack("swordGulfHarad", 1, 0), LOTRLegacyItems.modStack("daggerHarad", 1, 0), LOTRLegacyItems.modStack("spearHarad", 1, 0), LOTRLegacyItems.modStack("pikeHarad", 1, 0)};
        return items[random.nextInt(items.length)].copy();
    }

    @Override
    public void setupRandomBlocks(RandomSource random) {
        super.setupRandomBlocks(random);
        if (canUseRedBrick() && random.nextInt(3) == 0) {
            brickBlock = LOTRLegacyBlocks.mod("brick3");
            brickMeta = 13;
            brickSlabBlock = LOTRLegacyBlocks.mod("slabSingle7");
            brickSlabMeta = 2;
            brickStairBlock = LOTRLegacyBlocks.mod("stairsNearHaradBrickRed");
            brickWallBlock = LOTRLegacyBlocks.mod("wall3");
            brickWallMeta = 4;
        } else {
            brickBlock = LOTRLegacyBlocks.mod("brick");
            brickMeta = 15;
            brickSlabBlock = LOTRLegacyBlocks.mod("slabSingle4");
            brickSlabMeta = 0;
            brickStairBlock = LOTRLegacyBlocks.mod("stairsNearHaradBrick");
            brickWallBlock = LOTRLegacyBlocks.mod("wall");
            brickWallMeta = 15;
        }
        brick2Block = LOTRLegacyBlocks.mod("brick3");
        brick2Meta = 13;
        if (random.nextInt(5) == 0) {
            woodBlock = LOTRLegacyBlocks.mod("wood9");
            woodMeta = 0;
            plankBlock = LOTRLegacyBlocks.mod("planks3");
            plankMeta = 4;
            plankSlabBlock = LOTRLegacyBlocks.mod("woodSlabSingle5");
            plankSlabMeta = 4;
            plankStairBlock = LOTRLegacyBlocks.mod("stairsDragon");
            fenceBlock = LOTRLegacyBlocks.mod("fence3");
            fenceMeta = 4;
            fenceGateBlock = LOTRLegacyBlocks.mod("fenceGateDragon");
            doorBlock = LOTRLegacyBlocks.mod("doorDragon");
            beamBlock = LOTRLegacyBlocks.mod("woodBeam9");
            beamMeta = 0;
            trapdoorBlock = LOTRLegacyBlocks.mod("trapdoorDragon");
        } else {
            woodBlock = LOTRLegacyBlocks.mod("wood8");
            woodMeta = 3;
            plankBlock = LOTRLegacyBlocks.mod("planks3");
            plankMeta = 3;
            plankSlabBlock = LOTRLegacyBlocks.mod("woodSlabSingle5");
            plankSlabMeta = 3;
            plankStairBlock = LOTRLegacyBlocks.mod("stairsPalm");
            fenceBlock = LOTRLegacyBlocks.mod("fence3");
            fenceMeta = 3;
            fenceGateBlock = LOTRLegacyBlocks.mod("fenceGatePalm");
            doorBlock = LOTRLegacyBlocks.mod("doorPalm");
            beamBlock = LOTRLegacyBlocks.mod("woodBeam8");
            beamMeta = 3;
            trapdoorBlock = LOTRLegacyBlocks.mod("trapdoorPalm");
        }
        int randomWood2 = random.nextInt(3);
        switch (randomWood2) {
            case 0:
                plank2Block = LOTRLegacyBlocks.vanilla("planks");
                plank2Meta = 4;
                plank2SlabBlock = LOTRLegacyBlocks.vanilla("wooden_slab");
                plank2SlabMeta = 4;
                plank2StairBlock = LOTRLegacyBlocks.vanilla("acacia_stairs");
                beam2Block = LOTRLegacyBlocks.mod("woodBeamV2");
                beam2Meta = 0;
                break;
            case 1:
                plank2Block = LOTRLegacyBlocks.mod("planks");
                plank2Meta = 14;
                plank2SlabBlock = LOTRLegacyBlocks.mod("woodSlabSingle2");
                plank2SlabMeta = 6;
                plank2StairBlock = LOTRLegacyBlocks.mod("stairsDatePalm");
                beam2Block = LOTRLegacyBlocks.mod("woodBeam4");
                beam2Meta = 2;
                break;
            case 2:
                plank2Block = LOTRLegacyBlocks.mod("planks3");
                plank2Meta = 4;
                plank2SlabBlock = LOTRLegacyBlocks.mod("woodSlabSingle5");
                plank2SlabMeta = 4;
                plank2StairBlock = LOTRLegacyBlocks.mod("stairsDragon");
                beam2Block = LOTRLegacyBlocks.mod("woodBeam9");
                beam2Meta = 0;
                break;
            default:
                break;
        }
        roofBlock = LOTRLegacyBlocks.mod("thatch");
        roofMeta = 1;
        roofSlabBlock = LOTRLegacyBlocks.mod("slabSingleThatch");
        roofSlabMeta = 1;
        roofStairBlock = LOTRLegacyBlocks.mod("stairsReed");
        flagBlock = LOTRLegacyBlocks.vanilla("wool");
        flagMeta = 14;
        boneBlock = LOTRLegacyBlocks.mod("boneBlock");
        boneMeta = 0;
        boneWallBlock = LOTRLegacyBlocks.mod("wallBone");
        boneWallMeta = 0;
        bedBlock = LOTRLegacyBlocks.mod("strawBed");
    }
}
