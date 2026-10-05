package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;

public abstract class LOTRCorsairStructure extends LOTRStructureBase2 {
    public LegacyBlock brickBlock;
    public int brickMeta;
    public LegacyBlock brickSlabBlock;
    public int brickSlabMeta;
    public LegacyBlock brickStairBlock;
    public LegacyBlock brickWallBlock;
    public int brickWallMeta;
    public LegacyBlock pillarBlock;
    public int pillarMeta;
    public LegacyBlock pillarSlabBlock;
    public int pillarSlabMeta;
    public LegacyBlock plankBlock;
    public int plankMeta;
    public LegacyBlock plankSlabBlock;
    public int plankSlabMeta;
    public LegacyBlock plankStairBlock;
    public LegacyBlock fenceBlock;
    public int fenceMeta;
    public LegacyBlock fenceGateBlock;

    protected LOTRCorsairStructure(boolean flag) {
        super(flag);
    }

    public ItemStack getRandomCorsairWeapon(RandomSource random) {
        ItemStack[] items = {LOTRLegacyItems.modStack("swordCorsair", 1, 0), LOTRLegacyItems.modStack("daggerCorsair", 1, 0), LOTRLegacyItems.modStack("spearCorsair", 1, 0), LOTRLegacyItems.modStack("battleaxeCorsair", 1, 0), LOTRLegacyItems.modStack("nearHaradBow", 1, 0)};
        return items[random.nextInt(items.length)].copy();
    }

    @Override
    public void setupRandomBlocks(RandomSource random) {
        super.setupRandomBlocks(random);
        brickBlock = LOTRLegacyBlocks.mod("brick6");
        brickMeta = 6;
        brickSlabBlock = LOTRLegacyBlocks.mod("slabSingle13");
        brickSlabMeta = 2;
        brickStairBlock = LOTRLegacyBlocks.mod("stairsUmbarBrick");
        brickWallBlock = LOTRLegacyBlocks.mod("wall5");
        brickWallMeta = 0;
        pillarBlock = LOTRLegacyBlocks.mod("pillar2");
        pillarMeta = 10;
        pillarSlabBlock = LOTRLegacyBlocks.mod("slabSingle13");
        pillarSlabMeta = 4;
        int randomWood = random.nextInt(2);
        if (randomWood == 0) {
            plankBlock = LOTRLegacyBlocks.mod("planks3");
            plankMeta = 3;
            plankSlabBlock = LOTRLegacyBlocks.mod("woodSlabSingle5");
            plankSlabMeta = 3;
            plankStairBlock = LOTRLegacyBlocks.mod("stairsPalm");
            fenceBlock = LOTRLegacyBlocks.mod("fence3");
            fenceMeta = 3;
            fenceGateBlock = LOTRLegacyBlocks.mod("fenceGatePalm");
        } else {
            plankBlock = LOTRLegacyBlocks.mod("planks2");
            plankMeta = 2;
            plankSlabBlock = LOTRLegacyBlocks.mod("woodSlabSingle3");
            plankSlabMeta = 2;
            plankStairBlock = LOTRLegacyBlocks.mod("stairsCedar");
            fenceBlock = LOTRLegacyBlocks.mod("fence2");
            fenceMeta = 2;
            fenceGateBlock = LOTRLegacyBlocks.mod("fenceGateCedar");
        }
    }
}
