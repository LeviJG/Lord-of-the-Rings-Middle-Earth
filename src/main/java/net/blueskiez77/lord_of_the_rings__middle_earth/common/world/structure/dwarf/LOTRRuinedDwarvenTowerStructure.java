package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.dwarf;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNPCEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRRuinedDwarvenTowerStructure extends LOTRDwarvenTowerStructure {
    public boolean isGundabad;

    public LOTRRuinedDwarvenTowerStructure(boolean flag) {
        super(flag);
        ruined = true;
        glowBrickBlock = brickBlock;
        glowBrickMeta = brickMeta;
    }

    @Override
    public LOTRNPCEntity getCommanderNPC(WorldGenLevel world) {
        if (isGundabad) {
            return create(LOTREntities.GUNDABAD_ORC_MERCENARY_CAPTAIN, world);
        }
        return null;
    }

    @Override
    public void placeBrick(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        if (random.nextInt(4) == 0) {
            setBlockAndMetadata(world, i, j, k, LOTRLegacyBlocks.mod("brick4"), 5);
        } else {
            super.placeBrick(world, random, i, j, k);
        }
    }

    @Override
    public void placeBrickSlab(WorldGenLevel world, RandomSource random, int i, int j, int k, boolean flip) {
        if (random.nextInt(4) == 0) {
            setBlockAndMetadata(world, i, j, k, LOTRLegacyBlocks.mod("slabSingle7"), 6 | (flip ? 8 : 0));
        } else {
            super.placeBrickSlab(world, random, i, j, k, flip);
        }
    }

    @Override
    public void placeBrickStair(WorldGenLevel world, RandomSource random, int i, int j, int k, int meta) {
        if (random.nextInt(4) == 0) {
            setBlockAndMetadata(world, i, j, k, LOTRLegacyBlocks.mod("stairsDwarvenBrickCracked"), meta);
        } else {
            super.placeBrickStair(world, random, i, j, k, meta);
        }
    }

    @Override
    public void placeBrickWall(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        if (random.nextInt(4) == 0) {
            setBlockAndMetadata(world, i, j, k, LOTRLegacyBlocks.mod("wall4"), 5);
        } else {
            super.placeBrickWall(world, random, i, j, k);
        }
    }

    @Override
    public void placePillar(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        if (random.nextInt(4) == 0) {
            setBlockAndMetadata(world, i, j, k, LOTRLegacyBlocks.mod("pillar2"), 0);
        } else {
            super.placePillar(world, random, i, j, k);
        }
    }

    @Override
    public void setupRandomBlocks(RandomSource random) {
        super.setupRandomBlocks(random);
        if (random.nextInt(3) == 0) {
            plankBlock = LOTRLegacyBlocks.mod("planks");
            plankMeta = 3;
            plankSlabBlock = LOTRLegacyBlocks.mod("woodSlabSingle");
            plankSlabMeta = 3;
        }
        if (random.nextInt(4) == 0) {
            barsBlock = LOTRLegacyBlocks.vanilla("air");
        } else {
            int randomBars = random.nextInt(4);
            switch (randomBars) {
                case 0:
                    barsBlock = LOTRLegacyBlocks.mod("dwarfBars");
                    break;
                case 1:
                    barsBlock = LOTRLegacyBlocks.mod("orcSteelBars");
                    break;
                case 2:
                    barsBlock = LOTRLegacyBlocks.vanilla("iron_bars");
                    break;
                case 3:
                    barsBlock = LOTRLegacyBlocks.mod("bronzeBars");
                    break;
                default:
                    break;
            }
        }
        isGundabad = random.nextInt(3) == 0;
        if (isGundabad) {
            gateBlock = LOTRLegacyBlocks.mod("gateOrc");
            tableBlock = LOTRLegacyBlocks.mod("gundabadTable");
            forgeBlock = LOTRLegacyBlocks.mod("orcForge");
            bannerType = "GUNDABAD";
            chestContents = LOTRChestContents.GUNDABAD_TENT;
        } else {
            gateBlock = LOTRLegacyBlocks.mod("gateDwarven");
            tableBlock = LOTRLegacyBlocks.mod("dwarvenTable");
            forgeBlock = LOTRLegacyBlocks.mod("dwarvenForge");
            bannerType = null;
            chestContents = LOTRChestContents.DWARVEN_TOWER;
        }
    }
}
