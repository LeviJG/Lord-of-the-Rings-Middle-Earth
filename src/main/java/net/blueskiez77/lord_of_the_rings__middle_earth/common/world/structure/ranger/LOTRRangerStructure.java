package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract class LOTRRangerStructure extends LOTRStructureBase2 {
    public LegacyBlock brickBlock;
    public int brickMeta;
    public LegacyBlock brickSlabBlock;
    public int brickSlabMeta;
    public LegacyBlock brickStairBlock;
    public LegacyBlock brickWallBlock;
    public int brickWallMeta;
    public LegacyBlock brickCarvedBlock;
    public int brickCarvedMeta;
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
    public LegacyBlock wallBlock;
    public int wallMeta;
    public LegacyBlock roofBlock;
    public int roofMeta;
    public LegacyBlock roofSlabBlock;
    public int roofSlabMeta;
    public LegacyBlock roofStairBlock;
    public LegacyBlock barsBlock;
    public LegacyBlock tableBlock;
    public LegacyBlock bedBlock;
    public LegacyBlock plateBlock;
    public LegacyBlock cropBlock;
    public int cropMeta;
    public Item seedItem;
    public LegacyBlock trapdoorBlock;
    public String bannerType;
    public LOTRChestContents.Pool chestContentsHouse;
    public LOTRChestContents.Pool chestContentsRanger;

    protected LOTRRangerStructure(boolean flag) {
        super(flag);
    }

    public ItemStack getRangerFramedItem(RandomSource random) {
        ItemStack[] items = {LOTRLegacyItems.modStack("helmetRanger", 1, 0), LOTRLegacyItems.modStack("bodyRanger", 1, 0), LOTRLegacyItems.modStack("legsRanger", 1, 0), LOTRLegacyItems.modStack("bootsRanger", 1, 0), LOTRLegacyItems.modStack("daggerIron", 1, 0), LOTRLegacyItems.modStack("daggerBronze", 1, 0), LOTRLegacyItems.modStack("rangerBow", 1, 0), LOTRLegacyItems.vanillaStack("bow", 1, 0), LOTRLegacyItems.vanillaStack("arrow", 1, 0)};
        return items[random.nextInt(items.length)].copy();
    }

    @Override
    public void setupRandomBlocks(RandomSource random) {
        if (random.nextInt(3) == 0) {
            brickBlock = LOTRLegacyBlocks.mod("brick2");
            brickMeta = 3;
            brickSlabBlock = LOTRLegacyBlocks.mod("slabSingle4");
            brickSlabMeta = 1;
            brickStairBlock = LOTRLegacyBlocks.mod("stairsArnorBrick");
            brickWallBlock = LOTRLegacyBlocks.mod("wall2");
            brickWallMeta = 4;
            brickCarvedBlock = LOTRLegacyBlocks.mod("brick2");
            brickCarvedMeta = 6;
        } else {
            brickBlock = LOTRLegacyBlocks.vanilla("stonebrick");
            brickMeta = 0;
            brickSlabBlock = LOTRLegacyBlocks.vanilla("stone_slab");
            brickSlabMeta = 5;
            brickStairBlock = LOTRLegacyBlocks.vanilla("stone_brick_stairs");
            brickWallBlock = LOTRLegacyBlocks.mod("wallStoneV");
            brickWallMeta = 1;
            brickCarvedBlock = LOTRLegacyBlocks.vanilla("stonebrick");
            brickCarvedMeta = 3;
        }
        cobbleBlock = LOTRLegacyBlocks.vanilla("cobblestone");
        cobbleMeta = 0;
        cobbleSlabBlock = LOTRLegacyBlocks.vanilla("stone_slab");
        cobbleSlabMeta = 3;
        cobbleStairBlock = LOTRLegacyBlocks.vanilla("stone_stairs");
        int randomWood = random.nextInt(7);
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
                trapdoorBlock = LOTRLegacyBlocks.vanilla("trapdoor");
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
                trapdoorBlock = LOTRLegacyBlocks.mod("trapdoorBeech");
                break;
            case 4:
                logBlock = LOTRLegacyBlocks.vanilla("log");
                logMeta = 1;
                plankBlock = LOTRLegacyBlocks.vanilla("planks");
                plankMeta = 1;
                plankSlabBlock = LOTRLegacyBlocks.vanilla("wooden_slab");
                plankSlabMeta = 1;
                plankStairBlock = LOTRLegacyBlocks.vanilla("spruce_stairs");
                fenceBlock = LOTRLegacyBlocks.vanilla("fence");
                fenceMeta = 1;
                fenceGateBlock = LOTRLegacyBlocks.mod("fenceGateSpruce");
                woodBeamBlock = LOTRLegacyBlocks.mod("woodBeamV1");
                woodBeamMeta = 1;
                doorBlock = LOTRLegacyBlocks.mod("doorSpruce");
                trapdoorBlock = LOTRLegacyBlocks.mod("trapdoorSpruce");
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
                trapdoorBlock = LOTRLegacyBlocks.mod("trapdoorPine");
                break;
            case 6:
                logBlock = LOTRLegacyBlocks.mod("wood4");
                logMeta = 0;
                plankBlock = LOTRLegacyBlocks.mod("planks2");
                plankMeta = 0;
                plankSlabBlock = LOTRLegacyBlocks.mod("woodSlabSingle3");
                plankSlabMeta = 0;
                plankStairBlock = LOTRLegacyBlocks.mod("stairsChestnut");
                fenceBlock = LOTRLegacyBlocks.mod("fence2");
                fenceMeta = 0;
                fenceGateBlock = LOTRLegacyBlocks.mod("fenceGateChestnut");
                woodBeamBlock = LOTRLegacyBlocks.mod("woodBeam4");
                woodBeamMeta = 0;
                doorBlock = LOTRLegacyBlocks.mod("doorChestnut");
                trapdoorBlock = LOTRLegacyBlocks.mod("trapdoorChestnut");
                break;
            default:
                break;
        }
        if (random.nextBoolean()) {
            wallBlock = LOTRLegacyBlocks.mod("daub");
            wallMeta = 0;
        } else {
            wallBlock = plankBlock;
            wallMeta = plankMeta;
        }
        roofBlock = LOTRLegacyBlocks.mod("thatch");
        roofMeta = 0;
        roofSlabBlock = LOTRLegacyBlocks.mod("slabSingleThatch");
        roofSlabMeta = 0;
        roofStairBlock = LOTRLegacyBlocks.mod("stairsThatch");
        barsBlock = random.nextBoolean() ? LOTRLegacyBlocks.vanilla("iron_bars") : LOTRLegacyBlocks.mod("bronzeBars");
        tableBlock = LOTRLegacyBlocks.mod("rangerTable");
        bedBlock = LOTRLegacyBlocks.mod("strawBed");
        plateBlock = random.nextBoolean() ? LOTRLegacyBlocks.mod("woodPlateBlock") : LOTRLegacyBlocks.mod("ceramicPlateBlock");
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
        bannerType = "RANGER_NORTH";
        chestContentsHouse = LOTRChestContents.RANGER_HOUSE;
        chestContentsRanger = LOTRChestContents.RANGER_TENT;
    }
}
