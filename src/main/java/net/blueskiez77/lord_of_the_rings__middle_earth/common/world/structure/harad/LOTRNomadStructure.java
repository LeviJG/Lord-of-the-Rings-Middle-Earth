package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyItems;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.WorldGenLevel;

public abstract class LOTRNomadStructure extends LOTRStructureBase2 {
    public LegacyBlock tentBlock;
    public int tentMeta;
    public LegacyBlock tent2Block;
    public int tent2Meta;
    public LegacyBlock carpetBlock;
    public int carpetMeta;
    public LegacyBlock carpet2Block;
    public int carpet2Meta;
    public LegacyBlock plankBlock;
    public int plankMeta;
    public LegacyBlock plankSlabBlock;
    public int plankSlabMeta;
    public LegacyBlock plankStairBlock;
    public LegacyBlock fenceBlock;
    public int fenceMeta;
    public LegacyBlock fenceGateBlock;
    public LegacyBlock beamBlock;
    public int beamMeta;
    public LegacyBlock bedBlock;
    public LegacyBlock trapdoorBlock;

    protected LOTRNomadStructure(boolean flag) {
        super(flag);
    }

    public ItemStack getRandomNomadWeapon(RandomSource random) {
        ItemStack[] items = {LOTRLegacyItems.modStack("swordHarad", 1, 0), LOTRLegacyItems.modStack("daggerHarad", 1, 0), LOTRLegacyItems.modStack("spearHarad", 1, 0), LOTRLegacyItems.modStack("pikeHarad", 1, 0)};
        return items[random.nextInt(items.length)].copy();
    }

    public ItemStack getRandomUmbarWeapon(RandomSource random) {
        ItemStack[] items = {LOTRLegacyItems.modStack("scimitarNearHarad", 1, 0), LOTRLegacyItems.modStack("spearNearHarad", 1, 0), LOTRLegacyItems.modStack("pikeNearHarad", 1, 0), LOTRLegacyItems.modStack("poleaxeNearHarad", 1, 0), LOTRLegacyItems.modStack("maceNearHarad", 1, 0)};
        return items[random.nextInt(items.length)].copy();
    }

    public void laySandBase(WorldGenLevel world, int i, int j, int k) {
        setBlockAndMetadata(world, i, j, k, LOTRLegacyBlocks.vanilla("sand"), 0);
        int j1 = j - 1;
        while (getY(j1) >= 0 && !isOpaque(world, i, j1, k)) {
            if (isOpaque(world, i, j1 - 1, k)) {
                setBlockAndMetadata(world, i, j1, k, LOTRLegacyBlocks.vanilla("sandstone"), 0);
            } else {
                setBlockAndMetadata(world, i, j1, k, LOTRLegacyBlocks.vanilla("sand"), 0);
            }
            setGrassToDirt(world, i, j1 - 1, k);
            --j1;
        }
    }

    @Override
    public void setupRandomBlocks(RandomSource random) {
        super.setupRandomBlocks(random);
        tentBlock = LOTRLegacyBlocks.vanilla("wool");
        tentMeta = 0;
        tent2Block = LOTRLegacyBlocks.vanilla("wool");
        tent2Meta = 12;
        carpetBlock = LOTRLegacyBlocks.vanilla("carpet");
        carpetMeta = 0;
        carpet2Block = LOTRLegacyBlocks.vanilla("carpet");
        carpet2Meta = 12;
        int randomWood = random.nextInt(3);
        switch (randomWood) {
            case 0:
                plankBlock = LOTRLegacyBlocks.vanilla("planks");
                plankMeta = 0;
                plankSlabBlock = LOTRLegacyBlocks.vanilla("wooden_slab");
                plankSlabMeta = 0;
                plankStairBlock = LOTRLegacyBlocks.vanilla("oak_stairs");
                fenceBlock = LOTRLegacyBlocks.vanilla("fence");
                fenceMeta = 0;
                fenceGateBlock = LOTRLegacyBlocks.vanilla("fence_gate");
                beamBlock = LOTRLegacyBlocks.mod("woodBeamV1");
                beamMeta = 0;
                trapdoorBlock = LOTRLegacyBlocks.vanilla("trapdoor");
                break;
            case 1:
                plankBlock = LOTRLegacyBlocks.mod("planks2");
                plankMeta = 2;
                plankSlabBlock = LOTRLegacyBlocks.mod("woodSlabSingle3");
                plankSlabMeta = 2;
                plankStairBlock = LOTRLegacyBlocks.mod("stairsCedar");
                fenceBlock = LOTRLegacyBlocks.mod("fence2");
                fenceMeta = 2;
                fenceGateBlock = LOTRLegacyBlocks.mod("fenceGateCedar");
                beamBlock = LOTRLegacyBlocks.mod("woodBeam4");
                beamMeta = 2;
                trapdoorBlock = LOTRLegacyBlocks.mod("trapdoorCedar");
                break;
            case 2:
                plankBlock = LOTRLegacyBlocks.mod("planks");
                plankMeta = 14;
                plankSlabBlock = LOTRLegacyBlocks.mod("woodSlabSingle2");
                plankSlabMeta = 6;
                plankStairBlock = LOTRLegacyBlocks.mod("stairsDatePalm");
                fenceBlock = LOTRLegacyBlocks.mod("fence");
                fenceMeta = 14;
                fenceGateBlock = LOTRLegacyBlocks.mod("fenceGateDatePalm");
                trapdoorBlock = LOTRLegacyBlocks.mod("trapdoorDatePalm");
                beamBlock = LOTRLegacyBlocks.mod("woodBeam3");
                beamMeta = 2;
                break;
            default:
                break;
        }
        bedBlock = LOTRLegacyBlocks.mod("strawBed");
    }
}
