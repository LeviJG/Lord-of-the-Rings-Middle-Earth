package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.rhun;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract class LOTREasterlingStructure extends LOTRStructureBase2 {
    public LegacyBlock brickBlock;
    public int brickMeta;
    public LegacyBlock brickSlabBlock;
    public int brickSlabMeta;
    public LegacyBlock brickStairBlock;
    public LegacyBlock brickWallBlock;
    public int brickWallMeta;
    public LegacyBlock brickCarvedBlock;
    public int brickCarvedMeta;
    public LegacyBlock brickFloweryBlock;
    public int brickFloweryMeta;
    public LegacyBlock brickFlowerySlabBlock;
    public int brickFlowerySlabMeta;
    public LegacyBlock brickGoldBlock;
    public int brickGoldMeta;
    public LegacyBlock brickRedBlock;
    public int brickRedMeta;
    public LegacyBlock brickRedSlabBlock;
    public int brickRedSlabMeta;
    public LegacyBlock brickRedStairBlock;
    public LegacyBlock brickRedWallBlock;
    public int brickRedWallMeta;
    public LegacyBlock brickRedCarvedBlock;
    public int brickRedCarvedMeta;
    public LegacyBlock pillarBlock;
    public int pillarMeta;
    public LegacyBlock pillarRedBlock;
    public int pillarRedMeta;
    public LegacyBlock logBlock;
    public int logMeta;
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
    public LegacyBlock doorBlock;
    public LegacyBlock roofBlock;
    public int roofMeta;
    public LegacyBlock roofSlabBlock;
    public int roofSlabMeta;
    public LegacyBlock roofStairBlock;
    public LegacyBlock roofWallBlock;
    public int roofWallMeta;
    public LegacyBlock barsBlock;
    public LegacyBlock tableBlock;
    public LegacyBlock gateBlock;
    public LegacyBlock bedBlock;
    public LegacyBlock plateBlock;
    public LegacyBlock cropBlock;
    public int cropMeta;
    public Item seedItem;
    public LegacyBlock trapdoorBlock;
    public String bannerType;
    public LOTRChestContents.Pool chestContents;

    protected LOTREasterlingStructure(boolean flag) {
        super(flag);
    }

    public ItemStack getEasterlingFramedItem(RandomSource random) {
        ItemStack[] items = {LOTRLegacyItems.modStack("helmetRhun", 1, 0), LOTRLegacyItems.modStack("bodyRhun", 1, 0), LOTRLegacyItems.modStack("legsRhun", 1, 0), LOTRLegacyItems.modStack("bootsRhun", 1, 0), LOTRLegacyItems.modStack("helmetRhunGold", 1, 0), LOTRLegacyItems.modStack("bodyRhunGold", 1, 0), LOTRLegacyItems.modStack("legsRhunGold", 1, 0), LOTRLegacyItems.modStack("bootsRhunGold", 1, 0), LOTRLegacyItems.modStack("daggerRhun", 1, 0), LOTRLegacyItems.modStack("swordRhun", 1, 0), LOTRLegacyItems.modStack("battleaxeRhun", 1, 0), LOTRLegacyItems.modStack("spearRhun", 1, 0), LOTRLegacyItems.modStack("rhunBow", 1, 0), LOTRLegacyItems.vanillaStack("arrow", 1, 0), LOTRLegacyItems.vanillaStack("skull", 1, 0), LOTRLegacyItems.vanillaStack("bone", 1, 0), LOTRLegacyItems.modStack("gobletGold", 1, 0), LOTRLegacyItems.modStack("gobletSilver", 1, 0), LOTRLegacyItems.modStack("mug", 1, 0), LOTRLegacyItems.modStack("goldRing", 1, 0)};
        return items[random.nextInt(items.length)].copy();
    }

    public ItemStack getEasterlingWeaponItem(RandomSource random) {
        ItemStack[] items = {LOTRLegacyItems.modStack("swordRhun", 1, 0), LOTRLegacyItems.modStack("daggerRhun", 1, 0), LOTRLegacyItems.modStack("daggerRhunPoisoned", 1, 0), LOTRLegacyItems.modStack("spearRhun", 1, 0), LOTRLegacyItems.modStack("battleaxeRhun", 1, 0), LOTRLegacyItems.modStack("polearmRhun", 1, 0), LOTRLegacyItems.modStack("pikeRhun", 1, 0)};
        return items[random.nextInt(items.length)].copy();
    }

    @Override
    public void setupRandomBlocks(RandomSource random) {
        brickBlock = LOTRLegacyBlocks.mod("brick5");
        brickMeta = 11;
        brickSlabBlock = LOTRLegacyBlocks.mod("slabSingle12");
        brickSlabMeta = 0;
        brickStairBlock = LOTRLegacyBlocks.mod("stairsRhunBrick");
        brickWallBlock = LOTRLegacyBlocks.mod("wall3");
        brickWallMeta = 15;
        brickCarvedBlock = LOTRLegacyBlocks.mod("brick5");
        brickCarvedMeta = 12;
        brickFloweryBlock = LOTRLegacyBlocks.mod("brick5");
        brickFloweryMeta = 15;
        brickFlowerySlabBlock = LOTRLegacyBlocks.mod("slabSingle12");
        brickFlowerySlabMeta = 3;
        brickGoldBlock = LOTRLegacyBlocks.mod("brick6");
        brickGoldMeta = 0;
        brickRedBlock = LOTRLegacyBlocks.mod("brick6");
        brickRedMeta = 1;
        brickRedSlabBlock = LOTRLegacyBlocks.mod("slabSingle12");
        brickRedSlabMeta = 5;
        brickRedStairBlock = LOTRLegacyBlocks.mod("stairsRhunBrickRed");
        brickRedWallBlock = LOTRLegacyBlocks.mod("wall4");
        brickRedWallMeta = 13;
        brickRedCarvedBlock = LOTRLegacyBlocks.mod("brick6");
        brickRedCarvedMeta = 2;
        pillarBlock = LOTRLegacyBlocks.mod("pillar2");
        pillarMeta = 8;
        pillarRedBlock = LOTRLegacyBlocks.mod("pillar2");
        pillarRedMeta = 9;
        if (random.nextBoolean()) {
            logBlock = LOTRLegacyBlocks.mod("wood8");
            logMeta = 1;
            plankBlock = LOTRLegacyBlocks.mod("planks3");
            plankMeta = 1;
            plankSlabBlock = LOTRLegacyBlocks.mod("woodSlabSingle5");
            plankSlabMeta = 1;
            plankStairBlock = LOTRLegacyBlocks.mod("stairsRedwood");
            fenceBlock = LOTRLegacyBlocks.mod("fence3");
            fenceMeta = 1;
            fenceGateBlock = LOTRLegacyBlocks.mod("fenceGateRedwood");
            woodBeamBlock = LOTRLegacyBlocks.mod("woodBeam8");
            woodBeamMeta = 1;
            doorBlock = LOTRLegacyBlocks.mod("doorRedwood");
            trapdoorBlock = LOTRLegacyBlocks.mod("trapdoorRedwood");
        } else {
            int randomWood = random.nextInt(4);
            switch (randomWood) {
                case 0:
                    logBlock = LOTRLegacyBlocks.vanilla("log");
                    logMeta = 0;
                    plankBlock = LOTRLegacyBlocks.vanilla("planks");
                    plankMeta = 0;
                    plankSlabBlock = LOTRLegacyBlocks.vanilla("wooden_slab");
                    plankSlabMeta = 0;
                    plankStairBlock = LOTRLegacyBlocks.vanilla("oak_stairs");
                    fenceBlock = LOTRLegacyBlocks.vanilla("fence");
                    fenceMeta = 0;
                    fenceGateBlock = LOTRLegacyBlocks.vanilla("fence_gate");
                    woodBeamBlock = LOTRLegacyBlocks.mod("woodBeamV1");
                    woodBeamMeta = 0;
                    doorBlock = LOTRLegacyBlocks.vanilla("wooden_door");
                    trapdoorBlock = LOTRLegacyBlocks.vanilla("trapdoor");
                    break;
                case 1:
                    logBlock = LOTRLegacyBlocks.mod("wood2");
                    logMeta = 1;
                    plankBlock = LOTRLegacyBlocks.mod("planks");
                    plankMeta = 9;
                    plankSlabBlock = LOTRLegacyBlocks.mod("woodSlabSingle2");
                    plankSlabMeta = 1;
                    plankStairBlock = LOTRLegacyBlocks.mod("stairsBeech");
                    fenceBlock = LOTRLegacyBlocks.mod("fence");
                    fenceMeta = 9;
                    fenceGateBlock = LOTRLegacyBlocks.mod("fenceGateBeech");
                    woodBeamBlock = LOTRLegacyBlocks.mod("woodBeam2");
                    woodBeamMeta = 1;
                    doorBlock = LOTRLegacyBlocks.mod("doorBeech");
                    trapdoorBlock = LOTRLegacyBlocks.mod("trapdoorBeech");
                    break;
                case 2:
                    logBlock = LOTRLegacyBlocks.mod("wood6");
                    logMeta = 2;
                    plankBlock = LOTRLegacyBlocks.mod("planks2");
                    plankMeta = 10;
                    plankSlabBlock = LOTRLegacyBlocks.mod("woodSlabSingle4");
                    plankSlabMeta = 2;
                    plankStairBlock = LOTRLegacyBlocks.mod("stairsCypress");
                    fenceBlock = LOTRLegacyBlocks.mod("fence2");
                    fenceMeta = 10;
                    fenceGateBlock = LOTRLegacyBlocks.mod("fenceGateCypress");
                    woodBeamBlock = LOTRLegacyBlocks.mod("woodBeam6");
                    woodBeamMeta = 2;
                    doorBlock = LOTRLegacyBlocks.mod("doorCypress");
                    trapdoorBlock = LOTRLegacyBlocks.mod("trapdoorCypress");
                    break;
                case 3:
                    logBlock = LOTRLegacyBlocks.mod("wood6");
                    logMeta = 3;
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
                default:
                    break;
            }
        }
        if (useTownBlocks()) {
            if (random.nextBoolean()) {
                roofBlock = LOTRLegacyBlocks.mod("clayTileDyed");
                roofMeta = 14;
                roofSlabBlock = LOTRLegacyBlocks.mod("slabClayTileDyedSingle2");
                roofSlabMeta = 6;
                roofStairBlock = LOTRLegacyBlocks.mod("stairsClayTileDyedRed");
                roofWallBlock = LOTRLegacyBlocks.mod("wallClayTileDyed");
                roofWallMeta = 14;
            } else {
                int randomClay = random.nextInt(2);
                if (randomClay == 0) {
                    roofBlock = LOTRLegacyBlocks.mod("clayTileDyed");
                    roofMeta = 12;
                    roofSlabBlock = LOTRLegacyBlocks.mod("slabClayTileDyedSingle2");
                    roofSlabMeta = 4;
                    roofStairBlock = LOTRLegacyBlocks.mod("stairsClayTileDyedBrown");
                    roofWallBlock = LOTRLegacyBlocks.mod("wallClayTileDyed");
                    roofWallMeta = 12;
                } else {
                    roofBlock = LOTRLegacyBlocks.mod("clayTileDyed");
                    roofMeta = 1;
                    roofSlabBlock = LOTRLegacyBlocks.mod("slabClayTileDyedSingle");
                    roofSlabMeta = 1;
                    roofStairBlock = LOTRLegacyBlocks.mod("stairsClayTileDyedOrange");
                    roofWallBlock = LOTRLegacyBlocks.mod("wallClayTileDyed");
                    roofWallMeta = 1;
                }
            }
        } else {
            roofBlock = LOTRLegacyBlocks.mod("thatch");
            roofMeta = 0;
            roofSlabBlock = LOTRLegacyBlocks.mod("slabSingleThatch");
            roofSlabMeta = 0;
            roofStairBlock = LOTRLegacyBlocks.mod("stairsThatch");
            roofWallBlock = fenceBlock;
            roofWallMeta = fenceMeta;
        }
        barsBlock = random.nextBoolean() ? LOTRLegacyBlocks.vanilla("iron_bars") : LOTRLegacyBlocks.mod("bronzeBars");
        tableBlock = LOTRLegacyBlocks.mod("rhunTable");
        gateBlock = LOTRLegacyBlocks.mod("gateRhun");
        bedBlock = useTownBlocks() ? LOTRLegacyBlocks.vanilla("bed") : LOTRLegacyBlocks.mod("strawBed");
        plateBlock = useTownBlocks() ? LOTRLegacyBlocks.mod("ceramicPlateBlock") : random.nextBoolean() ? LOTRLegacyBlocks.mod("ceramicPlateBlock") : LOTRLegacyBlocks.mod("woodPlateBlock");
        if (random.nextBoolean()) {
            cropBlock = LOTRLegacyBlocks.vanilla("wheat");
            cropMeta = 7;
            seedItem = LOTRLegacyItems.vanilla("wheat_seeds", 0);
        } else {
            int randomCrop = random.nextInt(5);
            switch (randomCrop) {
                case 0:
                    cropBlock = LOTRLegacyBlocks.vanilla("carrots");
                    cropMeta = 7;
                    seedItem = LOTRLegacyItems.vanilla("carrot", 0);
                    break;
                case 1:
                    cropBlock = LOTRLegacyBlocks.vanilla("potatoes");
                    cropMeta = 7;
                    seedItem = LOTRLegacyItems.vanilla("potato", 0);
                    break;
                case 2:
                    cropBlock = LOTRLegacyBlocks.mod("lettuceCrop");
                    cropMeta = 7;
                    seedItem = LOTRLegacyItems.mod("lettuce", 0);
                    break;
                case 3:
                    cropBlock = LOTRLegacyBlocks.mod("leekCrop");
                    cropMeta = 7;
                    seedItem = LOTRLegacyItems.mod("leek", 0);
                    break;
                case 4:
                    cropBlock = LOTRLegacyBlocks.mod("turnipCrop");
                    cropMeta = 7;
                    seedItem = LOTRLegacyItems.mod("turnip", 0);
                    break;
                default:
                    break;
            }
        }
        bannerType = "RHUN";
        chestContents = LOTRChestContents.EASTERLING_HOUSE;
    }

    public boolean useTownBlocks() {
        return false;
    }
}
