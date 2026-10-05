package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;

public abstract class LOTRHarnedorStructure extends LOTRStructureBase2 {
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
    public LegacyBlock roofBlock;
    public int roofMeta;
    public LegacyBlock plank2Block;
    public int plank2Meta;
    public LegacyBlock plank2SlabBlock;
    public int plank2SlabMeta;
    public LegacyBlock plank2StairBlock;
    public LegacyBlock boneBlock;
    public int boneMeta;
    public LegacyBlock bedBlock;
    public LegacyBlock trapdoorBlock;

    protected LOTRHarnedorStructure(boolean flag) {
        super(flag);
    }

    public ItemStack getHarnedorFramedItem(RandomSource random) {
        ItemStack[] items = {LOTRLegacyItems.modStack("helmetHarnedor", 1, 0), LOTRLegacyItems.modStack("bodyHarnedor", 1, 0), LOTRLegacyItems.modStack("legsHarnedor", 1, 0), LOTRLegacyItems.modStack("bootsHarnedor", 1, 0), LOTRLegacyItems.modStack("daggerHarad", 1, 0), LOTRLegacyItems.modStack("swordHarad", 1, 0), LOTRLegacyItems.modStack("spearHarad", 1, 0), LOTRLegacyItems.modStack("pikeHarad", 1, 0), LOTRLegacyItems.modStack("nearHaradBow", 1, 0), LOTRLegacyItems.vanillaStack("arrow", 1, 0), LOTRLegacyItems.vanillaStack("skull", 1, 0), LOTRLegacyItems.vanillaStack("bone", 1, 0), LOTRLegacyItems.modStack("gobletGold", 1, 0), LOTRLegacyItems.modStack("gobletSilver", 1, 0), LOTRLegacyItems.modStack("mug", 1, 0), LOTRLegacyItems.modStack("ceramicMug", 1, 0), LOTRLegacyItems.modStack("goldRing", 1, 0), LOTRLegacyItems.modStack("silverRing", 1, 0), LOTRLegacyBlocks.mod("doubleFlower").stack(1, 2), LOTRLegacyBlocks.mod("doubleFlower").stack(1, 3)};
        return items[random.nextInt(items.length)].copy();
    }

    public ItemStack getRandomHarnedorWeapon(RandomSource random) {
        ItemStack[] items = {LOTRLegacyItems.modStack("swordHarad", 1, 0), LOTRLegacyItems.modStack("daggerHarad", 1, 0), LOTRLegacyItems.modStack("spearHarad", 1, 0), LOTRLegacyItems.modStack("pikeHarad", 1, 0)};
        return items[random.nextInt(items.length)].copy();
    }

    public boolean isRuined() {
        return false;
    }

    @Override
    public void setupRandomBlocks(RandomSource random) {
        super.setupRandomBlocks(random);
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
        doorBlock = LOTRLegacyBlocks.mod("doorCedar");
        trapdoorBlock = LOTRLegacyBlocks.mod("trapdoorCedar");
        int randomWool = random.nextInt(3);
        switch (randomWool) {
            case 0:
                roofBlock = LOTRLegacyBlocks.vanilla("wool");
                roofMeta = 1;
                break;
            case 1:
                roofBlock = LOTRLegacyBlocks.vanilla("wool");
                roofMeta = 12;
                break;
            case 2:
                roofBlock = LOTRLegacyBlocks.vanilla("wool");
                roofMeta = 14;
                break;
            default:
                break;
        }
        int randomFloorWood = random.nextInt(2);
        if (randomFloorWood == 0) {
            plank2Block = LOTRLegacyBlocks.mod("planks2");
            plank2Meta = 11;
            plank2SlabBlock = LOTRLegacyBlocks.mod("woodSlabSingle4");
            plank2SlabMeta = 3;
            plank2StairBlock = LOTRLegacyBlocks.mod("stairsOlive");
        } else {
            plank2Block = LOTRLegacyBlocks.mod("planks3");
            plank2Meta = 0;
            plank2SlabBlock = LOTRLegacyBlocks.mod("woodSlabSingle5");
            plank2SlabMeta = 0;
            plank2StairBlock = LOTRLegacyBlocks.mod("stairsPlum");
        }
        boneBlock = LOTRLegacyBlocks.mod("boneBlock");
        boneMeta = 0;
        bedBlock = LOTRLegacyBlocks.mod("strawBed");
        if (isRuined()) {
            if (random.nextBoolean()) {
                woodBlock = LOTRLegacyBlocks.mod("wood");
                woodMeta = 3;
                plankBlock = LOTRLegacyBlocks.mod("planks");
                plankMeta = 3;
                plankSlabBlock = LOTRLegacyBlocks.mod("woodSlabSingle");
                plankSlabMeta = 3;
                plankStairBlock = LOTRLegacyBlocks.mod("stairsCharred");
                fenceBlock = LOTRLegacyBlocks.mod("fence");
                fenceMeta = 3;
                fenceGateBlock = LOTRLegacyBlocks.mod("fenceGateCharred");
                doorBlock = LOTRLegacyBlocks.mod("doorCharred");
                trapdoorBlock = LOTRLegacyBlocks.mod("trapdoorCharred");
            }
            if (random.nextBoolean()) {
                plank2Block = LOTRLegacyBlocks.mod("planks");
                plank2Meta = 3;
                plank2SlabBlock = LOTRLegacyBlocks.mod("woodSlabSingle");
                plank2SlabMeta = 3;
                plank2StairBlock = LOTRLegacyBlocks.mod("stairsCharred");
            }
        }
    }
}
