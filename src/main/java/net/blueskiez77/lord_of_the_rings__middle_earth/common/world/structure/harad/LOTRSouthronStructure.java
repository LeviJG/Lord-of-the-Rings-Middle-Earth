package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.harad.LOTRNearHaradrimBaseEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.WorldGenLevel;

public abstract class LOTRSouthronStructure extends LOTRStructureBase2 {
    public LegacyBlock stoneBlock;
    public int stoneMeta;
    public LegacyBlock stoneStairBlock;
    public LegacyBlock stoneWallBlock;
    public int stoneWallMeta;
    public LegacyBlock brickBlock;
    public int brickMeta;
    public LegacyBlock brickSlabBlock;
    public int brickSlabMeta;
    public LegacyBlock brickStairBlock;
    public LegacyBlock brickWallBlock;
    public int brickWallMeta;
    public LegacyBlock pillarBlock;
    public int pillarMeta;
    public LegacyBlock brick2Block;
    public int brick2Meta;
    public LegacyBlock brick2SlabBlock;
    public int brick2SlabMeta;
    public LegacyBlock brick2StairBlock;
    public LegacyBlock brick2WallBlock;
    public int brick2WallMeta;
    public LegacyBlock brick2CarvedBlock;
    public int brick2CarvedMeta;
    public LegacyBlock pillar2Block;
    public int pillar2Meta;
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
    public LegacyBlock woodBeamBlock;
    public int woodBeamMeta;
    public int woodBeamMeta4;
    public int woodBeamMeta8;
    public LegacyBlock doorBlock;
    public LegacyBlock plank2Block;
    public int plank2Meta;
    public LegacyBlock roofBlock;
    public int roofMeta;
    public LegacyBlock roofSlabBlock;
    public int roofSlabMeta;
    public LegacyBlock roofStairBlock;
    public LegacyBlock gateMetalBlock;
    public LegacyBlock bedBlock;
    public LegacyBlock tableBlock;
    public LegacyBlock trapdoorBlock;
    public LegacyBlock cropBlock;
    public String bannerType;

    protected LOTRSouthronStructure(boolean flag) {
        super(flag);
    }

    public boolean canUseRedBricks() {
        return true;
    }

    public LOTRNearHaradrimBaseEntity createHaradrim(WorldGenLevel world) {
        if (isUmbar()) {
            return create(LOTREntities.UMBARIAN, world);
        }
        return create(LOTREntities.NEAR_HARADRIM, world);
    }

    public boolean forceCedarWood() {
        return false;
    }

    public ItemStack getRandomHaradItem(RandomSource random) {
        ItemStack[] items = {LOTRLegacyItems.modStack("scimitarNearHarad", 1, 0), LOTRLegacyItems.modStack("daggerNearHarad", 1, 0), LOTRLegacyItems.modStack("spearNearHarad", 1, 0), LOTRLegacyItems.modStack("pikeNearHarad", 1, 0), LOTRLegacyItems.modStack("poleaxeNearHarad", 1, 0), LOTRLegacyItems.modStack("maceNearHarad", 1, 0), LOTRLegacyItems.modStack("swordHarad", 1, 0), LOTRLegacyItems.modStack("daggerHarad", 1, 0), LOTRLegacyItems.modStack("spearHarad", 1, 0), LOTRLegacyItems.modStack("pikeHarad", 1, 0), LOTRLegacyItems.vanillaStack("arrow", 1, 0), LOTRLegacyItems.vanillaStack("skull", 1, 0), LOTRLegacyItems.vanillaStack("bone", 1, 0), LOTRLegacyItems.modStack("gobletGold", 1, 0), LOTRLegacyItems.modStack("gobletSilver", 1, 0), LOTRLegacyItems.modStack("gobletCopper", 1, 0), LOTRLegacyItems.modStack("mug", 1, 0), LOTRLegacyItems.modStack("ceramicMug", 1, 0), LOTRLegacyItems.modStack("goldRing", 1, 0), LOTRLegacyItems.modStack("silverRing", 1, 0), LOTRLegacyBlocks.mod("doubleFlower").stack(1, 2), LOTRLegacyBlocks.mod("doubleFlower").stack(1, 3), LOTRLegacyItems.modStack("gemsbokHorn", 1, 0), LOTRLegacyItems.modStack("lionFur", 1, 0)};
        return items[random.nextInt(items.length)].copy();
    }

    public ItemStack getRandomHaradWeapon(RandomSource random) {
        ItemStack[] items = {LOTRLegacyItems.modStack("scimitarNearHarad", 1, 0), LOTRLegacyItems.modStack("daggerNearHarad", 1, 0), LOTRLegacyItems.modStack("spearNearHarad", 1, 0), LOTRLegacyItems.modStack("pikeNearHarad", 1, 0), LOTRLegacyItems.modStack("poleaxeNearHarad", 1, 0), LOTRLegacyItems.modStack("maceNearHarad", 1, 0), LOTRLegacyItems.modStack("swordHarad", 1, 0), LOTRLegacyItems.modStack("daggerHarad", 1, 0), LOTRLegacyItems.modStack("spearHarad", 1, 0), LOTRLegacyItems.modStack("pikeHarad", 1, 0)};
        return items[random.nextInt(items.length)].copy();
    }

    public boolean isUmbar() {
        return false;
    }

    @Override
    public void setupRandomBlocks(RandomSource random) {
        super.setupRandomBlocks(random);
        stoneBlock = LOTRLegacyBlocks.vanilla("sandstone");
        stoneMeta = 0;
        stoneStairBlock = LOTRLegacyBlocks.vanilla("sandstone_stairs");
        stoneWallBlock = LOTRLegacyBlocks.mod("wallStoneV");
        stoneWallMeta = 4;
        if (canUseRedBricks() && random.nextInt(4) == 0) {
            brickBlock = LOTRLegacyBlocks.mod("brick3");
            brickMeta = 13;
            brickSlabBlock = LOTRLegacyBlocks.mod("slabSingle7");
            brickSlabMeta = 2;
            brickStairBlock = LOTRLegacyBlocks.mod("stairsNearHaradBrickRed");
            brickWallBlock = LOTRLegacyBlocks.mod("wall3");
            brickWallMeta = 4;
            pillarBlock = LOTRLegacyBlocks.mod("pillar");
            pillarMeta = 15;
        } else {
            brickBlock = LOTRLegacyBlocks.mod("brick");
            brickMeta = 15;
            brickSlabBlock = LOTRLegacyBlocks.mod("slabSingle4");
            brickSlabMeta = 0;
            brickStairBlock = LOTRLegacyBlocks.mod("stairsNearHaradBrick");
            brickWallBlock = LOTRLegacyBlocks.mod("wall");
            brickWallMeta = 15;
            pillarBlock = LOTRLegacyBlocks.mod("pillar");
            pillarMeta = 5;
        }
        brick2Block = LOTRLegacyBlocks.mod("brick3");
        brick2Meta = 13;
        brick2SlabBlock = LOTRLegacyBlocks.mod("slabSingle7");
        brick2SlabMeta = 2;
        brick2StairBlock = LOTRLegacyBlocks.mod("stairsNearHaradBrickRed");
        brick2WallBlock = LOTRLegacyBlocks.mod("wall3");
        brick2WallMeta = 4;
        brick2CarvedBlock = LOTRLegacyBlocks.mod("brick3");
        brick2CarvedMeta = 15;
        pillar2Block = LOTRLegacyBlocks.mod("pillar");
        pillar2Meta = 15;
        roofBlock = LOTRLegacyBlocks.mod("thatch");
        roofMeta = 1;
        roofSlabBlock = LOTRLegacyBlocks.mod("slabSingleThatch");
        roofSlabMeta = 1;
        roofStairBlock = LOTRLegacyBlocks.mod("stairsReed");
        if (random.nextBoolean() || forceCedarWood()) {
            woodBlock = LOTRLegacyBlocks.mod("wood4");
            woodMeta = 2;
            plankBlock = LOTRLegacyBlocks.mod("planks2");
            plankMeta = 2;
            plankSlabBlock = LOTRLegacyBlocks.mod("woodSlabSingle3");
            plankSlabMeta = 2;
            plankStairBlock = LOTRLegacyBlocks.mod("stairsCedar");
            fenceBlock = LOTRLegacyBlocks.mod("fence2");
            fenceMeta = 2;
            fenceGateBlock = LOTRLegacyBlocks.mod("fenceGateCedar");
            woodBeamBlock = LOTRLegacyBlocks.mod("woodBeam4");
            woodBeamMeta = 2;
            doorBlock = LOTRLegacyBlocks.mod("doorCedar");
            trapdoorBlock = LOTRLegacyBlocks.mod("trapdoorCedar");
        } else {
            int randomWood = random.nextInt(3);
            switch (randomWood) {
                case 0:
                    woodBlock = LOTRLegacyBlocks.mod("wood6");
                    woodMeta = 3;
                    plankBlock = LOTRLegacyBlocks.mod("planks2");
                    plankMeta = 11;
                    plankSlabBlock = LOTRLegacyBlocks.mod("woodSlabSingle4");
                    plankSlabMeta = 3;
                    plankStairBlock = LOTRLegacyBlocks.mod("stairsOlive");
                    fenceBlock = LOTRLegacyBlocks.mod("fence2");
                    fenceMeta = 11;
                    fenceGateBlock = LOTRLegacyBlocks.mod("fenceGateOlive");
                    woodBeamBlock = LOTRLegacyBlocks.mod("woodBeam6");
                    woodBeamMeta = 3;
                    doorBlock = LOTRLegacyBlocks.mod("doorOlive");
                    trapdoorBlock = LOTRLegacyBlocks.mod("trapdoorOlive");
                    break;
                case 1:
                    woodBlock = LOTRLegacyBlocks.mod("wood3");
                    woodMeta = 2;
                    plankBlock = LOTRLegacyBlocks.mod("planks");
                    plankMeta = 14;
                    plankSlabBlock = LOTRLegacyBlocks.mod("woodSlabSingle2");
                    plankSlabMeta = 6;
                    plankStairBlock = LOTRLegacyBlocks.mod("stairsDatePalm");
                    fenceBlock = LOTRLegacyBlocks.mod("fence");
                    fenceMeta = 14;
                    fenceGateBlock = LOTRLegacyBlocks.mod("fenceGateDatePalm");
                    woodBeamBlock = LOTRLegacyBlocks.mod("woodBeam3");
                    woodBeamMeta = 2;
                    doorBlock = LOTRLegacyBlocks.mod("doorDatePalm");
                    trapdoorBlock = LOTRLegacyBlocks.mod("trapdoorDatePalm");
                    break;
                case 2:
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
                    woodBeamBlock = LOTRLegacyBlocks.mod("woodBeam8");
                    woodBeamMeta = 3;
                    doorBlock = LOTRLegacyBlocks.mod("doorPalm");
                    trapdoorBlock = LOTRLegacyBlocks.mod("trapdoorPalm");
                    break;
                default:
                    break;
            }
        }
        woodBeamMeta4 = woodBeamMeta | 4;
        woodBeamMeta8 = woodBeamMeta | 8;
        plank2Block = LOTRLegacyBlocks.mod("planks2");
        plank2Meta = 11;
        gateMetalBlock = LOTRLegacyBlocks.mod("gateBronzeBars");
        bedBlock = LOTRLegacyBlocks.mod("strawBed");
        tableBlock = LOTRLegacyBlocks.mod("nearHaradTable");
        if (random.nextBoolean()) {
            cropBlock = LOTRLegacyBlocks.vanilla("wheat");
        } else {
            int randomCrop = random.nextInt(3);
            switch (randomCrop) {
                case 0:
                case 1:
                    cropBlock = LOTRLegacyBlocks.vanilla("carrots");
                    break;
                case 2:
                    cropBlock = LOTRLegacyBlocks.mod("lettuceCrop");
                    break;
                default:
                    break;
            }
        }
        bannerType = "NEAR_HARAD";
        if (isUmbar()) {
            stoneBlock = LOTRLegacyBlocks.mod("brick2");
            stoneMeta = 11;
            stoneStairBlock = LOTRLegacyBlocks.mod("stairsBlackGondorBrick");
            stoneWallBlock = LOTRLegacyBlocks.mod("wall2");
            stoneWallMeta = 10;
            brickBlock = LOTRLegacyBlocks.mod("brick2");
            brickMeta = 11;
            brickSlabBlock = LOTRLegacyBlocks.mod("slabSingle5");
            brickSlabMeta = 3;
            brickStairBlock = LOTRLegacyBlocks.mod("stairsBlackGondorBrick");
            brickWallBlock = LOTRLegacyBlocks.mod("wall2");
            brickWallMeta = 10;
            pillarBlock = LOTRLegacyBlocks.mod("pillar");
            pillarMeta = 9;
            int randomRoof = random.nextInt(4);
            switch (randomRoof) {
                case 0:
                    roofBlock = LOTRLegacyBlocks.mod("clayTileDyed");
                    roofMeta = 15;
                    roofSlabBlock = LOTRLegacyBlocks.mod("slabClayTileDyedSingle2");
                    roofSlabMeta = 7;
                    roofStairBlock = LOTRLegacyBlocks.mod("stairsClayTileDyedBlack");
                    break;
                case 1:
                    roofBlock = LOTRLegacyBlocks.mod("clayTileDyed");
                    roofMeta = 14;
                    roofSlabBlock = LOTRLegacyBlocks.mod("slabClayTileDyedSingle2");
                    roofSlabMeta = 6;
                    roofStairBlock = LOTRLegacyBlocks.mod("stairsClayTileDyedRed");
                    break;
                case 2:
                    roofBlock = LOTRLegacyBlocks.mod("clayTileDyed");
                    roofMeta = 12;
                    roofSlabBlock = LOTRLegacyBlocks.mod("slabClayTileDyedSingle2");
                    roofSlabMeta = 4;
                    roofStairBlock = LOTRLegacyBlocks.mod("stairsClayTileDyedBrown");
                    break;
                case 3:
                    roofBlock = LOTRLegacyBlocks.mod("clayTileDyed");
                    roofMeta = 7;
                    roofSlabBlock = LOTRLegacyBlocks.mod("slabClayTileDyedSingle");
                    roofSlabMeta = 7;
                    roofStairBlock = LOTRLegacyBlocks.mod("stairsClayTileDyedGray");
                    break;
                default:
                    break;
            }
            brick2Block = roofBlock;
            brick2Meta = roofMeta;
            brick2SlabBlock = roofSlabBlock;
            brick2SlabMeta = roofSlabMeta;
            brick2StairBlock = roofStairBlock;
            plankBlock = LOTRLegacyBlocks.mod("brick6");
            plankMeta = 6;
            plankSlabBlock = LOTRLegacyBlocks.mod("slabSingle13");
            plankSlabMeta = 2;
            plankStairBlock = LOTRLegacyBlocks.mod("stairsUmbarBrick");
            woodBeamBlock = LOTRLegacyBlocks.mod("pillar2");
            woodBeamMeta4 = woodBeamMeta = 10;
            woodBeamMeta8 = woodBeamMeta;
            if (random.nextBoolean() && !forceCedarWood()) {
                fenceBlock = LOTRLegacyBlocks.mod("fence3");
                fenceMeta = 3;
                fenceGateBlock = LOTRLegacyBlocks.mod("fenceGatePalm");
                doorBlock = LOTRLegacyBlocks.mod("doorPalm");
            }
            gateMetalBlock = LOTRLegacyBlocks.mod("gateIronBars");
            tableBlock = LOTRLegacyBlocks.mod("umbarTable");
            bannerType = "UMBAR";
        }
    }
}
