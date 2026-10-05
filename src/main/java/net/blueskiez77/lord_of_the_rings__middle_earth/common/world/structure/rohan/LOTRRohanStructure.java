package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.rohan;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract class LOTRRohanStructure extends LOTRStructureBase2 {
    public LegacyBlock rockBlock;
    public int rockMeta;
    public LegacyBlock rockSlabBlock;
    public int rockSlabMeta;
    public LegacyBlock rockSlabDoubleBlock;
    public int rockSlabDoubleMeta;
    public LegacyBlock rockStairBlock;
    public LegacyBlock rockWallBlock;
    public int rockWallMeta;
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
    public LegacyBlock cobbleBlock;
    public int cobbleMeta;
    public LegacyBlock cobbleSlabBlock;
    public int cobbleSlabMeta;
    public LegacyBlock cobbleStairBlock;
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
    public LegacyBlock log2Block;
    public int log2Meta;
    public LegacyBlock plank2Block;
    public int plank2Meta;
    public LegacyBlock plank2SlabBlock;
    public int plank2SlabMeta;
    public LegacyBlock plank2StairBlock;
    public LegacyBlock fence2Block;
    public int fence2Meta;
    public LegacyBlock fenceGate2Block;
    public LegacyBlock woodBeam2Block;
    public int woodBeam2Meta;
    public LegacyBlock woodBeamRohanBlock;
    public int woodBeamRohanMeta;
    public LegacyBlock woodBeamRohanGoldBlock;
    public int woodBeamRohanGoldMeta;
    public LegacyBlock roofBlock;
    public int roofMeta;
    public LegacyBlock roofSlabBlock;
    public int roofSlabMeta;
    public LegacyBlock roofStairBlock;
    public LegacyBlock barsBlock;
    public LegacyBlock tableBlock;
    public LegacyBlock bedBlock;
    public LegacyBlock gateBlock;
    public LegacyBlock plateBlock;
    public LegacyBlock carpetBlock;
    public int carpetMeta;
    public LegacyBlock cropBlock;
    public int cropMeta;
    public Item seedItem;
    public String bannerType;
    public LOTRChestContents.Pool chestContents;

    protected LOTRRohanStructure(boolean flag) {
        super(flag);
    }

    public LegacyBlock getRandomCakeBlock(RandomSource random) {
        int i = random.nextInt(3);
        switch (i) {
            case 0:
                return LOTRLegacyBlocks.mod("appleCrumble");
            case 1:
                return LOTRLegacyBlocks.mod("cherryPie");
            case 2:
                return LOTRLegacyBlocks.mod("berryPie");
            default:
                break;
        }
        return null;
    }

    public ItemStack getRandomRohanWeapon(RandomSource random) {
        ItemStack[] items = {LOTRLegacyItems.modStack("swordRohan", 1, 0), LOTRLegacyItems.modStack("battleaxeRohan", 1, 0), LOTRLegacyItems.modStack("daggerRohan", 1, 0), LOTRLegacyItems.modStack("spearRohan", 1, 0)};
        return items[random.nextInt(items.length)].copy();
    }

    public ItemStack[] getRohanArmourItems() {
        return new ItemStack[]{LOTRLegacyItems.modStack("helmetRohan", 1, 0), LOTRLegacyItems.modStack("bodyRohan", 1, 0), LOTRLegacyItems.modStack("legsRohan", 1, 0), LOTRLegacyItems.modStack("bootsRohan", 1, 0)};
    }

    public ItemStack getRohanFramedItem(RandomSource random) {
        ItemStack[] items = {LOTRLegacyItems.modStack("helmetRohan", 1, 0), LOTRLegacyItems.modStack("bodyRohan", 1, 0), LOTRLegacyItems.modStack("legsRohan", 1, 0), LOTRLegacyItems.modStack("bootsRohan", 1, 0), LOTRLegacyItems.modStack("swordRohan", 1, 0), LOTRLegacyItems.modStack("battleaxeRohan", 1, 0), LOTRLegacyItems.modStack("daggerRohan", 1, 0), LOTRLegacyItems.vanillaStack("wooden_sword", 1, 0), LOTRLegacyItems.vanillaStack("stone_sword", 1, 0), LOTRLegacyItems.modStack("rohanBow", 1, 0), LOTRLegacyItems.vanillaStack("arrow", 1, 0), LOTRLegacyItems.modStack("horn", 1, 0)};
        return items[random.nextInt(items.length)].copy();
    }

    public boolean oneWoodType() {
        return false;
    }

    @Override
    public void setupRandomBlocks(RandomSource random) {
        rockBlock = LOTRLegacyBlocks.mod("rock");
        rockMeta = 2;
        rockSlabBlock = LOTRLegacyBlocks.mod("slabSingle2");
        rockSlabMeta = 1;
        rockSlabDoubleBlock = LOTRLegacyBlocks.mod("slabDouble2");
        rockSlabDoubleMeta = 1;
        rockStairBlock = LOTRLegacyBlocks.mod("stairsRohanRock");
        rockWallBlock = LOTRLegacyBlocks.mod("wall");
        rockWallMeta = 8;
        brickBlock = LOTRLegacyBlocks.mod("brick");
        brickMeta = 4;
        brickSlabBlock = LOTRLegacyBlocks.mod("slabSingle");
        brickSlabMeta = 6;
        brickStairBlock = LOTRLegacyBlocks.mod("stairsRohanBrick");
        brickWallBlock = LOTRLegacyBlocks.mod("wall");
        brickWallMeta = 6;
        brickCarvedBlock = LOTRLegacyBlocks.mod("brick5");
        brickCarvedMeta = 3;
        pillarBlock = LOTRLegacyBlocks.mod("pillar");
        pillarMeta = 8;
        cobbleBlock = LOTRLegacyBlocks.vanilla("cobblestone");
        cobbleMeta = 0;
        cobbleSlabBlock = LOTRLegacyBlocks.vanilla("stone_slab");
        cobbleSlabMeta = 3;
        cobbleStairBlock = LOTRLegacyBlocks.vanilla("stone_stairs");
        int randomWood = random.nextInt(6);
        switch (randomWood) {
            case 0:
            case 1:
            case 2:
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
                break;
            case 3:
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
                break;
            case 4:
                logBlock = LOTRLegacyBlocks.mod("fruitWood");
                logMeta = 0;
                plankBlock = LOTRLegacyBlocks.mod("planks");
                plankMeta = 4;
                plankSlabBlock = LOTRLegacyBlocks.mod("woodSlabSingle");
                plankSlabMeta = 4;
                plankStairBlock = LOTRLegacyBlocks.mod("stairsApple");
                fenceBlock = LOTRLegacyBlocks.mod("fence");
                fenceMeta = 4;
                fenceGateBlock = LOTRLegacyBlocks.mod("fenceGateApple");
                woodBeamBlock = LOTRLegacyBlocks.mod("woodBeamFruit");
                woodBeamMeta = 0;
                doorBlock = LOTRLegacyBlocks.mod("doorApple");
                break;
            case 5:
                logBlock = LOTRLegacyBlocks.mod("wood5");
                logMeta = 0;
                plankBlock = LOTRLegacyBlocks.mod("planks2");
                plankMeta = 4;
                plankSlabBlock = LOTRLegacyBlocks.mod("woodSlabSingle3");
                plankSlabMeta = 4;
                plankStairBlock = LOTRLegacyBlocks.mod("stairsPine");
                fenceBlock = LOTRLegacyBlocks.mod("fence2");
                fenceMeta = 4;
                fenceGateBlock = LOTRLegacyBlocks.mod("fenceGatePine");
                woodBeamBlock = LOTRLegacyBlocks.mod("woodBeam5");
                woodBeamMeta = 0;
                doorBlock = LOTRLegacyBlocks.mod("doorPine");
                break;
            default:
                break;
        }
        int randomWood2 = random.nextInt(4);
        if (randomWood2 == 0 || randomWood2 == 1 || randomWood2 == 2) {
            log2Block = LOTRLegacyBlocks.vanilla("log");
            log2Meta = 1;
            plank2Block = LOTRLegacyBlocks.vanilla("planks");
            plank2Meta = 1;
            plank2SlabBlock = LOTRLegacyBlocks.vanilla("wooden_slab");
            plank2SlabMeta = 1;
            plank2StairBlock = LOTRLegacyBlocks.vanilla("spruce_stairs");
            fence2Block = LOTRLegacyBlocks.vanilla("fence");
            fence2Meta = 1;
            fenceGate2Block = LOTRLegacyBlocks.mod("fenceGateSpruce");
            woodBeam2Block = LOTRLegacyBlocks.mod("woodBeamV1");
        } else {
            log2Block = LOTRLegacyBlocks.mod("wood3");
            log2Meta = 1;
            plank2Block = LOTRLegacyBlocks.mod("planks");
            plank2Meta = 13;
            plank2SlabBlock = LOTRLegacyBlocks.mod("woodSlabSingle2");
            plank2SlabMeta = 5;
            plank2StairBlock = LOTRLegacyBlocks.mod("stairsLarch");
            fence2Block = LOTRLegacyBlocks.mod("fence");
            fence2Meta = 13;
            fenceGate2Block = LOTRLegacyBlocks.mod("fenceGateLarch");
            woodBeam2Block = LOTRLegacyBlocks.mod("woodBeam3");
        }
        woodBeam2Meta = 1;
        if (oneWoodType() && random.nextInt(3) == 0) {
            logBlock = log2Block;
            logMeta = log2Meta;
            plankBlock = plank2Block;
            plankMeta = plank2Meta;
            plankSlabBlock = plank2SlabBlock;
            plankSlabMeta = plank2SlabMeta;
            plankStairBlock = plank2StairBlock;
            fenceBlock = fence2Block;
            fenceMeta = fence2Meta;
            fenceGateBlock = fenceGate2Block;
            woodBeamBlock = woodBeam2Block;
            woodBeamMeta = woodBeam2Meta;
        }
        woodBeamRohanBlock = LOTRLegacyBlocks.mod("woodBeamS");
        woodBeamRohanMeta = 0;
        woodBeamRohanGoldBlock = LOTRLegacyBlocks.mod("woodBeamS");
        woodBeamRohanGoldMeta = 1;
        roofBlock = LOTRLegacyBlocks.mod("thatch");
        roofMeta = 0;
        roofSlabBlock = LOTRLegacyBlocks.mod("slabSingleThatch");
        roofSlabMeta = 0;
        roofStairBlock = LOTRLegacyBlocks.mod("stairsThatch");
        barsBlock = random.nextBoolean() ? LOTRLegacyBlocks.vanilla("iron_bars") : LOTRLegacyBlocks.mod("bronzeBars");
        tableBlock = LOTRLegacyBlocks.mod("rohirricTable");
        bedBlock = LOTRLegacyBlocks.mod("strawBed");
        gateBlock = LOTRLegacyBlocks.mod("gateWooden");
        plateBlock = random.nextBoolean() ? LOTRLegacyBlocks.mod("ceramicPlateBlock") : LOTRLegacyBlocks.mod("woodPlateBlock");
        carpetBlock = LOTRLegacyBlocks.vanilla("carpet");
        carpetMeta = 13;
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
        bannerType = "ROHAN";
        chestContents = LOTRChestContents.ROHAN_HOUSE;
    }
}
