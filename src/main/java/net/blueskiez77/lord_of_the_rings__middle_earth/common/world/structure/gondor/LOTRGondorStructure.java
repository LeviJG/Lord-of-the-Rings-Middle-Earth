package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.gondor.LOTRGondorManEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.WorldGenLevel;

public abstract class LOTRGondorStructure extends LOTRStructureBase2 {
    public GondorFiefdom strFief = GondorFiefdom.GONDOR;
    public LegacyBlock rockBlock;
    public int rockMeta;
    public LegacyBlock rockSlabBlock;
    public int rockSlabMeta;
    public LegacyBlock trapdoorBlock;
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
    public LegacyBlock brickMossyBlock;
    public int brickMossyMeta;
    public LegacyBlock brickMossySlabBlock;
    public int brickMossySlabMeta;
    public LegacyBlock brickMossyStairBlock;
    public LegacyBlock brickMossyWallBlock;
    public int brickMossyWallMeta;
    public LegacyBlock brickCrackedBlock;
    public int brickCrackedMeta;
    public LegacyBlock brickCrackedSlabBlock;
    public int brickCrackedSlabMeta;
    public LegacyBlock brickCrackedStairBlock;
    public LegacyBlock brickCrackedWallBlock;
    public int brickCrackedWallMeta;
    public LegacyBlock pillarBlock;
    public int pillarMeta;
    public LegacyBlock brick2Block;
    public int brick2Meta;
    public LegacyBlock brick2SlabBlock;
    public int brick2SlabMeta;
    public LegacyBlock brick2StairBlock;
    public LegacyBlock brick2WallBlock;
    public int brick2WallMeta;
    public LegacyBlock pillar2Block;
    public int pillar2Meta;
    public LegacyBlock cobbleBlock;
    public int cobbleMeta;
    public LegacyBlock cobbleSlabBlock;
    public int cobbleSlabMeta;
    public LegacyBlock cobbleStairBlock;
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
    public LegacyBlock gateBlock;
    public LegacyBlock plateBlock;
    public LegacyBlock cropBlock;
    public int cropMeta;
    public Item seedItem;
    public String bannerType;
    public String bannerType2;
    public LOTRChestContents.Pool chestContents;

    protected LOTRGondorStructure(boolean flag) {
        super(flag);
    }

    public ItemStack getGondorFramedItem(RandomSource random) {
        ItemStack[] items = {LOTRLegacyItems.modStack("helmetGondor", 1, 0), LOTRLegacyItems.modStack("bodyGondor", 1, 0), LOTRLegacyItems.modStack("daggerGondor", 1, 0), LOTRLegacyItems.modStack("spearGondor", 1, 0), LOTRLegacyItems.modStack("gondorBow", 1, 0), LOTRLegacyItems.vanillaStack("arrow", 1, 0), LOTRLegacyItems.vanillaStack("iron_sword", 1, 0), LOTRLegacyItems.vanillaStack("iron_axe", 1, 0), LOTRLegacyItems.modStack("daggerIron", 1, 0), LOTRLegacyItems.modStack("pikeIron", 1, 0), LOTRLegacyItems.modStack("ironCrossbow", 1, 0), LOTRLegacyItems.modStack("goldRing", 1, 0), LOTRLegacyItems.modStack("silverRing", 1, 0)};
        return items[random.nextInt(items.length)].copy();
    }

    @Override
    public void setupRandomBlocks(RandomSource random) {
        rockBlock = LOTRLegacyBlocks.mod("rock");
        rockMeta = 1;
        rockSlabBlock = LOTRLegacyBlocks.mod("slabSingle");
        rockSlabMeta = 2;
        rockSlabDoubleBlock = LOTRLegacyBlocks.mod("slabDouble");
        rockSlabDoubleMeta = 2;
        rockStairBlock = LOTRLegacyBlocks.mod("stairsGondorRock");
        rockWallBlock = LOTRLegacyBlocks.mod("wall");
        rockWallMeta = 2;
        if (strFief == GondorFiefdom.GONDOR || strFief == GondorFiefdom.LEBENNIN || strFief == GondorFiefdom.PELARGIR) {
            brickBlock = LOTRLegacyBlocks.mod("brick");
            brickMeta = 1;
            brickSlabBlock = LOTRLegacyBlocks.mod("slabSingle");
            brickSlabMeta = 3;
            brickStairBlock = LOTRLegacyBlocks.mod("stairsGondorBrick");
            brickWallBlock = LOTRLegacyBlocks.mod("wall");
            brickWallMeta = 3;
            brickMossyBlock = LOTRLegacyBlocks.mod("brick");
            brickMossyMeta = 2;
            brickMossySlabBlock = LOTRLegacyBlocks.mod("slabSingle");
            brickMossySlabMeta = 4;
            brickMossyStairBlock = LOTRLegacyBlocks.mod("stairsGondorBrickMossy");
            brickMossyWallBlock = LOTRLegacyBlocks.mod("wall");
            brickMossyWallMeta = 4;
            brickCrackedBlock = LOTRLegacyBlocks.mod("brick");
            brickCrackedMeta = 3;
            brickCrackedSlabBlock = LOTRLegacyBlocks.mod("slabSingle");
            brickCrackedSlabMeta = 5;
            brickCrackedStairBlock = LOTRLegacyBlocks.mod("stairsGondorBrickCracked");
            brickCrackedWallBlock = LOTRLegacyBlocks.mod("wall");
            brickCrackedWallMeta = 5;
        } else if (strFief == GondorFiefdom.DOL_AMROTH) {
            brickBlock = LOTRLegacyBlocks.mod("brick3");
            brickMeta = 9;
            brickSlabBlock = LOTRLegacyBlocks.mod("slabSingle6");
            brickSlabMeta = 7;
            brickStairBlock = LOTRLegacyBlocks.mod("stairsDolAmrothBrick");
            brickWallBlock = LOTRLegacyBlocks.mod("wall2");
            brickWallMeta = 14;
            brickMossyBlock = brickBlock;
            brickMossyMeta = brickMeta;
            brickMossySlabBlock = brickSlabBlock;
            brickMossySlabMeta = brickSlabMeta;
            brickMossyStairBlock = brickStairBlock;
            brickMossyWallBlock = brickWallBlock;
            brickMossyWallMeta = brickWallMeta;
            brickCrackedBlock = brickBlock;
            brickCrackedMeta = brickMeta;
            brickCrackedSlabBlock = brickSlabBlock;
            brickCrackedSlabMeta = brickSlabMeta;
            brickCrackedStairBlock = brickStairBlock;
            brickCrackedWallBlock = brickWallBlock;
            brickCrackedWallMeta = brickWallMeta;
        } else {
            brickBlock = LOTRLegacyBlocks.mod("brick5");
            brickMeta = 8;
            brickSlabBlock = LOTRLegacyBlocks.mod("slabSingle11");
            brickSlabMeta = 0;
            brickStairBlock = LOTRLegacyBlocks.mod("stairsGondorBrickRustic");
            brickWallBlock = LOTRLegacyBlocks.mod("wall4");
            brickWallMeta = 7;
            brickMossyBlock = LOTRLegacyBlocks.mod("brick5");
            brickMossyMeta = 9;
            brickMossySlabBlock = LOTRLegacyBlocks.mod("slabSingle11");
            brickMossySlabMeta = 1;
            brickMossyStairBlock = LOTRLegacyBlocks.mod("stairsGondorBrickRusticMossy");
            brickMossyWallBlock = LOTRLegacyBlocks.mod("wall4");
            brickMossyWallMeta = 8;
            brickCrackedBlock = LOTRLegacyBlocks.mod("brick5");
            brickCrackedMeta = 10;
            brickCrackedSlabBlock = LOTRLegacyBlocks.mod("slabSingle11");
            brickCrackedSlabMeta = 2;
            brickCrackedStairBlock = LOTRLegacyBlocks.mod("stairsGondorBrickRusticCracked");
            brickCrackedWallBlock = LOTRLegacyBlocks.mod("wall4");
            brickCrackedWallMeta = 9;
        }
        pillarBlock = LOTRLegacyBlocks.mod("pillar");
        pillarMeta = 6;
        if (strFief == GondorFiefdom.GONDOR || strFief == GondorFiefdom.BLACKROOT_VALE) {
            brick2Block = LOTRLegacyBlocks.mod("brick2");
            brick2Meta = 11;
            brick2SlabBlock = LOTRLegacyBlocks.mod("slabSingle5");
            brick2SlabMeta = 3;
            brick2StairBlock = LOTRLegacyBlocks.mod("stairsBlackGondorBrick");
            brick2WallBlock = LOTRLegacyBlocks.mod("wall2");
            brick2WallMeta = 10;
            pillar2Block = LOTRLegacyBlocks.mod("pillar");
            pillar2Meta = 9;
        } else if (strFief == GondorFiefdom.PELARGIR) {
            brick2Block = LOTRLegacyBlocks.mod("whiteSandstone");
            brick2Meta = 0;
            brick2SlabBlock = LOTRLegacyBlocks.mod("slabSingle10");
            brick2SlabMeta = 6;
            brick2StairBlock = LOTRLegacyBlocks.mod("stairsWhiteSandstone");
            brick2WallBlock = LOTRLegacyBlocks.mod("wall3");
            brick2WallMeta = 14;
            pillar2Block = LOTRLegacyBlocks.mod("pillar");
            pillar2Meta = 9;
        } else if (strFief == GondorFiefdom.LAMEDON) {
            brick2Block = LOTRLegacyBlocks.vanilla("cobblestone");
            brick2Meta = 0;
            brick2SlabBlock = LOTRLegacyBlocks.vanilla("stone_slab");
            brick2SlabMeta = 3;
            brick2StairBlock = LOTRLegacyBlocks.vanilla("stone_stairs");
            brick2WallBlock = LOTRLegacyBlocks.vanilla("cobblestone_wall");
            brick2WallMeta = 0;
            pillar2Block = LOTRLegacyBlocks.mod("pillar2");
            pillar2Meta = 2;
        } else if (strFief == GondorFiefdom.PINNATH_GELIN) {
            brick2Block = LOTRLegacyBlocks.mod("clayTileDyed");
            brick2Meta = 13;
            brick2SlabBlock = LOTRLegacyBlocks.mod("slabClayTileDyedSingle2");
            brick2SlabMeta = 5;
            brick2StairBlock = LOTRLegacyBlocks.mod("stairsClayTileDyedGreen");
            brick2WallBlock = LOTRLegacyBlocks.mod("wallClayTileDyed");
            brick2WallMeta = 13;
            pillar2Block = LOTRLegacyBlocks.mod("pillar");
            pillar2Meta = 6;
        } else if (strFief == GondorFiefdom.DOL_AMROTH) {
            brick2Block = LOTRLegacyBlocks.mod("clayTileDyed");
            brick2Meta = 11;
            brick2SlabBlock = LOTRLegacyBlocks.mod("slabClayTileDyedSingle2");
            brick2SlabMeta = 3;
            brick2StairBlock = LOTRLegacyBlocks.mod("stairsClayTileDyedBlue");
            brick2WallBlock = LOTRLegacyBlocks.mod("wallClayTileDyed");
            brick2WallMeta = 11;
            pillar2Block = LOTRLegacyBlocks.mod("pillar");
            pillar2Meta = 6;
        } else {
            brick2Block = LOTRLegacyBlocks.vanilla("stonebrick");
            brick2Meta = 0;
            brick2SlabBlock = LOTRLegacyBlocks.vanilla("stone_slab");
            brick2SlabMeta = 5;
            brick2StairBlock = LOTRLegacyBlocks.vanilla("stone_brick_stairs");
            brick2WallBlock = LOTRLegacyBlocks.mod("wallStoneV");
            brick2WallMeta = 1;
            pillar2Block = LOTRLegacyBlocks.mod("pillar2");
            pillar2Meta = 2;
        }
        cobbleBlock = LOTRLegacyBlocks.vanilla("cobblestone");
        cobbleMeta = 0;
        cobbleSlabBlock = LOTRLegacyBlocks.vanilla("stone_slab");
        cobbleSlabMeta = 3;
        cobbleStairBlock = LOTRLegacyBlocks.vanilla("stone_stairs");
        if (strFief == GondorFiefdom.BLACKROOT_VALE) {
            plankBlock = LOTRLegacyBlocks.vanilla("planks");
            plankMeta = 5;
            plankSlabBlock = LOTRLegacyBlocks.vanilla("wooden_slab");
            plankSlabMeta = 5;
            plankStairBlock = LOTRLegacyBlocks.vanilla("dark_oak_stairs");
            fenceBlock = LOTRLegacyBlocks.vanilla("fence");
            fenceMeta = 5;
            fenceGateBlock = LOTRLegacyBlocks.mod("fenceGateDarkOak");
            woodBeamBlock = LOTRLegacyBlocks.mod("woodBeamV2");
            woodBeamMeta = 1;
            doorBlock = LOTRLegacyBlocks.mod("doorDarkOak");
            trapdoorBlock = LOTRLegacyBlocks.mod("trapdoorDarkOak");
        } else {
            int randomWood = random.nextInt(7);
            switch (randomWood) {
                case 0:
                case 1:
                case 2:
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
                    break;
                case 5:
                    plankBlock = LOTRLegacyBlocks.mod("planks");
                    plankMeta = 8;
                    plankSlabBlock = LOTRLegacyBlocks.mod("woodSlabSingle2");
                    plankSlabMeta = 0;
                    plankStairBlock = LOTRLegacyBlocks.mod("stairsLebethron");
                    fenceBlock = LOTRLegacyBlocks.mod("fence");
                    fenceMeta = 8;
                    fenceGateBlock = LOTRLegacyBlocks.mod("fenceGateLebethron");
                    woodBeamBlock = LOTRLegacyBlocks.mod("woodBeam2");
                    woodBeamMeta = 0;
                    doorBlock = LOTRLegacyBlocks.mod("doorLebethron");
                    trapdoorBlock = LOTRLegacyBlocks.mod("trapdoorLebethron");
                    break;
                case 6:
                    plankBlock = LOTRLegacyBlocks.vanilla("planks");
                    plankMeta = 2;
                    plankSlabBlock = LOTRLegacyBlocks.vanilla("wooden_slab");
                    plankSlabMeta = 2;
                    plankStairBlock = LOTRLegacyBlocks.vanilla("birch_stairs");
                    fenceBlock = LOTRLegacyBlocks.vanilla("fence");
                    fenceMeta = 2;
                    fenceGateBlock = LOTRLegacyBlocks.mod("fenceGateBirch");
                    woodBeamBlock = LOTRLegacyBlocks.mod("woodBeamV1");
                    woodBeamMeta = 2;
                    doorBlock = LOTRLegacyBlocks.mod("doorBirch");
                    trapdoorBlock = LOTRLegacyBlocks.mod("trapdoorBirch");
                    break;
                default:
                    break;
            }
        }
        if (strFief == GondorFiefdom.LOSSARNACH) {
            pillarBlock = woodBeamBlock;
            pillarMeta = woodBeamMeta;
            brick2Block = plankBlock;
            brick2Meta = plankMeta;
            brick2SlabBlock = plankSlabBlock;
            brick2SlabMeta = plankSlabMeta;
            brick2StairBlock = plankStairBlock;
            brick2WallBlock = fenceBlock;
            brick2WallMeta = fenceMeta;
            pillar2Block = woodBeamBlock;
            pillar2Meta = woodBeamMeta;
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
        barsBlock = LOTRLegacyBlocks.vanilla("iron_bars");
        tableBlock = LOTRLegacyBlocks.mod("gondorianTable");
        bedBlock = LOTRLegacyBlocks.mod("strawBed");
        gateBlock = strFief == GondorFiefdom.PINNATH_GELIN || strFief == GondorFiefdom.LOSSARNACH || strFief == GondorFiefdom.LAMEDON ? LOTRLegacyBlocks.mod("gateWooden") : strFief == GondorFiefdom.DOL_AMROTH ? LOTRLegacyBlocks.mod("gateDolAmroth") : LOTRLegacyBlocks.mod("gateGondor");
        plateBlock = strFief == GondorFiefdom.LOSSARNACH || strFief == GondorFiefdom.LAMEDON || strFief == GondorFiefdom.BLACKROOT_VALE ? random.nextBoolean() ? LOTRLegacyBlocks.mod("woodPlateBlock") : LOTRLegacyBlocks.mod("ceramicPlateBlock") : LOTRLegacyBlocks.mod("ceramicPlateBlock");
        if (random.nextBoolean()) {
            cropBlock = LOTRLegacyBlocks.vanilla("wheat");
            cropMeta = 7;
            seedItem = LOTRLegacyItems.vanilla("wheat_seeds", 0);
        } else {
            int randomCrop = random.nextInt(6);
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
                    cropBlock = LOTRLegacyBlocks.mod("cornStalk");
                    cropMeta = 0;
                    seedItem = LOTRLegacyBlocks.mod("cornStalk").state().getBlock().asItem();
                    break;
                case 4:
                    cropBlock = LOTRLegacyBlocks.mod("leekCrop");
                    cropMeta = 7;
                    seedItem = LOTRLegacyItems.mod("leek", 0);
                    break;
                case 5:
                    cropBlock = LOTRLegacyBlocks.mod("turnipCrop");
                    cropMeta = 7;
                    seedItem = LOTRLegacyItems.mod("turnip", 0);
                    break;
                default:
                    break;
            }
        }
        if (strFief == GondorFiefdom.GONDOR) {
            bannerType = "GONDOR";
        } else if (strFief == GondorFiefdom.LOSSARNACH) {
            bannerType = "LOSSARNACH";
        } else if (strFief == GondorFiefdom.LEBENNIN) {
            bannerType = "LEBENNIN";
        } else if (strFief == GondorFiefdom.PELARGIR) {
            bannerType = "PELARGIR";
        } else if (strFief == GondorFiefdom.PINNATH_GELIN) {
            bannerType = "PINNATH_GELIN";
        } else if (strFief == GondorFiefdom.BLACKROOT_VALE) {
            bannerType = "BLACKROOT_VALE";
        } else if (strFief == GondorFiefdom.LAMEDON) {
            bannerType = "LAMEDON";
        } else if (strFief == GondorFiefdom.DOL_AMROTH) {
            bannerType = "DOL_AMROTH";
        }
        bannerType2 = strFief == GondorFiefdom.PELARGIR ? "LEBENNIN" : "GONDOR";
        chestContents = LOTRChestContents.GONDOR_HOUSE;
    }

    public enum GondorFiefdom {
        GONDOR, LOSSARNACH, LEBENNIN, PELARGIR, PINNATH_GELIN, BLACKROOT_VALE, LAMEDON, DOL_AMROTH;

        public LOTRGondorManEntity createCaptain(WorldGenLevel world) {
            if (this == GONDOR) {
                return create(LOTREntities.GONDORIAN_CAPTAIN, world);
            }
            if (this == LOSSARNACH) {
                return create(LOTREntities.LOSSARNACH_CAPTAIN, world);
            }
            if (this == LEBENNIN) {
                return create(LOTREntities.LEBENNIN_CAPTAIN, world);
            }
            if (this == PELARGIR) {
                return create(LOTREntities.PELARGIR_CAPTAIN, world);
            }
            if (this == PINNATH_GELIN) {
                return create(LOTREntities.PINNATH_GELIN_CAPTAIN, world);
            }
            if (this == BLACKROOT_VALE) {
                return create(LOTREntities.BLACKROOT_CAPTAIN, world);
            }
            if (this == LAMEDON) {
                return create(LOTREntities.LAMEDON_CAPTAIN, world);
            }
            if (this == DOL_AMROTH) {
                return create(LOTREntities.DOL_AMROTH_CAPTAIN, world);
            }
            return null;
        }

        public ItemStack[] getFiefdomArmor() {
            if (this == GONDOR) {
                return new ItemStack[]{LOTRLegacyItems.modStack("helmetGondor", 1, 0), LOTRLegacyItems.modStack("bodyGondor", 1, 0), LOTRLegacyItems.modStack("legsGondor", 1, 0), LOTRLegacyItems.modStack("bootsGondor", 1, 0)};
            }
            if (this == LOSSARNACH) {
                return new ItemStack[]{LOTRLegacyItems.modStack("helmetLossarnach", 1, 0), LOTRLegacyItems.modStack("bodyLossarnach", 1, 0), LOTRLegacyItems.modStack("legsLossarnach", 1, 0), LOTRLegacyItems.modStack("bootsLossarnach", 1, 0)};
            }
            if (this == LEBENNIN) {
                return new ItemStack[]{LOTRLegacyItems.modStack("helmetGondor", 1, 0), LOTRLegacyItems.modStack("bodyGondor", 1, 0), LOTRLegacyItems.modStack("legsGondor", 1, 0), LOTRLegacyItems.modStack("bootsGondor", 1, 0)};
            }
            if (this == PELARGIR) {
                return new ItemStack[]{LOTRLegacyItems.modStack("helmetPelargir", 1, 0), LOTRLegacyItems.modStack("bodyPelargir", 1, 0), LOTRLegacyItems.modStack("legsPelargir", 1, 0), LOTRLegacyItems.modStack("bootsPelargir", 1, 0)};
            }
            if (this == PINNATH_GELIN) {
                return new ItemStack[]{LOTRLegacyItems.modStack("helmetPinnathGelin", 1, 0), LOTRLegacyItems.modStack("bodyPinnathGelin", 1, 0), LOTRLegacyItems.modStack("legsPinnathGelin", 1, 0), LOTRLegacyItems.modStack("bootsPinnathGelin", 1, 0)};
            }
            if (this == BLACKROOT_VALE) {
                return new ItemStack[]{LOTRLegacyItems.modStack("helmetBlackroot", 1, 0), LOTRLegacyItems.modStack("bodyBlackroot", 1, 0), LOTRLegacyItems.modStack("legsBlackroot", 1, 0), LOTRLegacyItems.modStack("bootsBlackroot", 1, 0)};
            }
            if (this == LAMEDON) {
                return new ItemStack[]{LOTRLegacyItems.modStack("helmetLamedon", 1, 0), LOTRLegacyItems.modStack("bodyLamedon", 1, 0), LOTRLegacyItems.modStack("legsLamedon", 1, 0), LOTRLegacyItems.modStack("bootsLamedon", 1, 0)};
            }
            if (this == DOL_AMROTH) {
                return new ItemStack[]{LOTRLegacyItems.modStack("helmetDolAmroth", 1, 0), LOTRLegacyItems.modStack("bodyDolAmroth", 1, 0), LOTRLegacyItems.modStack("legsDolAmroth", 1, 0), LOTRLegacyItems.modStack("bootsDolAmroth", 1, 0)};
            }
            return null;
        }

        @SafeVarargs
        private static EntityType<? extends LOTRGondorManEntity>[] types(EntityType<? extends LOTRGondorManEntity>... types) {
            return types;
        }

        public EntityType<? extends LOTRGondorManEntity>[] getLevyClasses() {
            if (this == GONDOR) {
                return types(LOTREntities.GONDOR_LEVYMAN, LOTREntities.GONDOR_SOLDIER);
            }
            if (this == LOSSARNACH) {
                return types(LOTREntities.GONDOR_LEVYMAN, LOTREntities.LOSSARNACH_AXEMAN);
            }
            if (this == LEBENNIN) {
                return types(LOTREntities.LEBENNIN_LEVYMAN, LOTREntities.GONDOR_SOLDIER);
            }
            if (this == PELARGIR) {
                return types(LOTREntities.LEBENNIN_LEVYMAN, LOTREntities.PELARGIR_MARINE);
            }
            if (this == PINNATH_GELIN) {
                return types(LOTREntities.GONDOR_LEVYMAN, LOTREntities.PINNATH_GELIN_SOLDIER);
            }
            if (this == BLACKROOT_VALE) {
                return types(LOTREntities.GONDOR_LEVYMAN, LOTREntities.BLACKROOT_ARCHER);
            }
            if (this == LAMEDON) {
                return types(LOTREntities.LAMEDON_HILLMAN, LOTREntities.LAMEDON_SOLDIER);
            }
            if (this == DOL_AMROTH) {
                return types(LOTREntities.DOL_AMROTH_SOLDIER, LOTREntities.SWAN_KNIGHT);
            }
            return null;
        }

        public EntityType<? extends LOTRGondorManEntity>[] getSoldierClasses() {
            if (this == GONDOR) {
                return types(LOTREntities.GONDOR_SOLDIER, LOTREntities.GONDOR_ARCHER);
            }
            if (this == LOSSARNACH) {
                return types(LOTREntities.LOSSARNACH_AXEMAN, LOTREntities.LOSSARNACH_AXEMAN);
            }
            if (this == LEBENNIN) {
                return types(LOTREntities.LEBENNIN_LEVYMAN, LOTREntities.GONDOR_SOLDIER);
            }
            if (this == PELARGIR) {
                return types(LOTREntities.PELARGIR_MARINE, LOTREntities.PELARGIR_MARINE);
            }
            if (this == PINNATH_GELIN) {
                return types(LOTREntities.PINNATH_GELIN_SOLDIER, LOTREntities.PINNATH_GELIN_SOLDIER);
            }
            if (this == BLACKROOT_VALE) {
                return types(LOTREntities.BLACKROOT_ARCHER, LOTREntities.BLACKROOT_SOLDIER);
            }
            if (this == LAMEDON) {
                return types(LOTREntities.LAMEDON_SOLDIER, LOTREntities.LAMEDON_ARCHER);
            }
            if (this == DOL_AMROTH) {
                return types(LOTREntities.DOL_AMROTH_SOLDIER, LOTREntities.SWAN_KNIGHT);
            }
            return null;
        }
    }

}
