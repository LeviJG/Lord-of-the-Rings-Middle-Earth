package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.dunland;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.WorldGenLevel;

public abstract class LOTRDunlandStructure extends LOTRStructureBase2 {
    public LegacyBlock floorBlock;
    public int floorMeta;
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
    public LegacyBlock roofSlabBlock;
    public int roofSlabMeta;
    public LegacyBlock roofStairBlock;
    public LegacyBlock barsBlock;
    public int barsMeta;
    public LegacyBlock bedBlock;

    protected LOTRDunlandStructure(boolean flag) {
        super(flag);
    }

    public ItemStack getRandomDunlandWeapon(RandomSource random) {
        ItemStack[] items = {LOTRLegacyItems.vanillaStack("iron_sword", 1, 0), LOTRLegacyItems.modStack("spearIron", 1, 0), LOTRLegacyItems.modStack("daggerIron", 1, 0), LOTRLegacyItems.vanillaStack("stone_sword", 1, 0), LOTRLegacyItems.modStack("spearStone", 1, 0), LOTRLegacyItems.modStack("dunlendingClub", 1, 0), LOTRLegacyItems.modStack("dunlendingTrident", 1, 0)};
        return items[random.nextInt(items.length)].copy();
    }

    public void placeDunlandItemFrame(WorldGenLevel world, RandomSource random, int i, int j, int k, int direction) {
        ItemStack[] items = {LOTRLegacyItems.vanillaStack("bone", 1, 0), LOTRLegacyItems.modStack("fur", 1, 0), LOTRLegacyItems.vanillaStack("flint", 1, 0), LOTRLegacyItems.vanillaStack("iron_sword", 1, 0), LOTRLegacyItems.vanillaStack("stone_sword", 1, 0), LOTRLegacyItems.modStack("spearIron", 1, 0), LOTRLegacyItems.modStack("spearStone", 1, 0), LOTRLegacyItems.vanillaStack("bow", 1, 0), LOTRLegacyItems.vanillaStack("arrow", 1, 0), LOTRLegacyItems.modStack("mug", 1, 0), LOTRLegacyItems.modStack("skullCup", 1, 0)};
        ItemStack item = items[random.nextInt(items.length)].copy();
        spawnItemFrame(world, i, j, k, direction, item);
    }

    @Override
    public void setupRandomBlocks(RandomSource random) {
        int randomFloor = random.nextInt(5);
        switch (randomFloor) {
            case 0:
                floorBlock = LOTRLegacyBlocks.vanilla("cobblestone");
                floorMeta = 0;
                break;
            case 1:
                floorBlock = LOTRLegacyBlocks.vanilla("hardened_clay");
                floorMeta = 0;
                break;
            case 2:
                floorBlock = LOTRLegacyBlocks.vanilla("stained_hardened_clay");
                floorMeta = 7;
                break;
            case 3:
                floorBlock = LOTRLegacyBlocks.vanilla("stained_hardened_clay");
                floorMeta = 12;
                break;
            case 4:
                floorBlock = LOTRLegacyBlocks.vanilla("stained_hardened_clay");
                floorMeta = 15;
                break;
            default:
                break;
        }
        if (random.nextBoolean()) {
            woodBlock = LOTRLegacyBlocks.vanilla("log");
            woodMeta = 1;
            plankBlock = LOTRLegacyBlocks.vanilla("planks");
            plankMeta = 1;
            plankSlabBlock = LOTRLegacyBlocks.vanilla("wooden_slab");
            plankSlabMeta = 1;
            plankStairBlock = LOTRLegacyBlocks.vanilla("spruce_stairs");
            fenceBlock = LOTRLegacyBlocks.vanilla("fence");
            fenceMeta = 1;
            fenceGateBlock = LOTRLegacyBlocks.mod("fenceGateSpruce");
            doorBlock = LOTRLegacyBlocks.mod("doorSpruce");
        } else {
            int randomWood = random.nextInt(2);
            if (randomWood == 0) {
                woodBlock = LOTRLegacyBlocks.vanilla("log");
                woodMeta = 0;
                plankBlock = LOTRLegacyBlocks.vanilla("planks");
                plankMeta = 0;
                plankSlabBlock = LOTRLegacyBlocks.vanilla("wooden_slab");
                plankSlabMeta = 0;
                plankStairBlock = LOTRLegacyBlocks.vanilla("oak_stairs");
                fenceBlock = LOTRLegacyBlocks.vanilla("fence");
                fenceMeta = 0;
                fenceGateBlock = LOTRLegacyBlocks.vanilla("fence_gate");
                doorBlock = LOTRLegacyBlocks.vanilla("wooden_door");
            } else {
                woodBlock = LOTRLegacyBlocks.mod("wood5");
                woodMeta = 0;
                plankBlock = LOTRLegacyBlocks.mod("planks2");
                plankMeta = 4;
                plankSlabBlock = LOTRLegacyBlocks.mod("woodSlabSingle3");
                plankSlabMeta = 4;
                plankStairBlock = LOTRLegacyBlocks.mod("stairsPine");
                fenceBlock = LOTRLegacyBlocks.mod("fence2");
                fenceMeta = 4;
                fenceGateBlock = LOTRLegacyBlocks.mod("fenceGatePine");
                doorBlock = LOTRLegacyBlocks.mod("doorPine");
            }
        }
        roofBlock = LOTRLegacyBlocks.mod("thatch");
        roofMeta = 0;
        roofSlabBlock = LOTRLegacyBlocks.mod("slabSingleThatch");
        roofSlabMeta = 0;
        roofStairBlock = LOTRLegacyBlocks.mod("stairsThatch");
        if (random.nextBoolean()) {
            barsBlock = LOTRLegacyBlocks.vanilla("iron_bars");
        } else {
            barsBlock = LOTRLegacyBlocks.mod("bronzeBars");
        }
        barsMeta = 0;
        bedBlock = random.nextBoolean() ? LOTRLegacyBlocks.mod("furBed") : LOTRLegacyBlocks.mod("strawBed");
    }
}
