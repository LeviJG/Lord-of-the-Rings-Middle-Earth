package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.elf;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.elf.LOTRElfEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRRuinedEregionForgeStructure extends LOTRHighElvenForgeStructure {
    public LOTRRuinedEregionForgeStructure(boolean flag) {
        super(flag);
        ruined = true;
        roofBlock = LOTRLegacyBlocks.mod("clayTileDyed");
        roofMeta = 8;
        roofStairBlock = LOTRLegacyBlocks.mod("stairsClayTileDyedLightGray");
    }

    @Override
    public LOTRElfEntity getElf(WorldGenLevel world) {
        return null;
    }

    @Override
    public void placeBrick(WorldGenLevel world, int i, int j, int k, RandomSource random) {
        if (random.nextInt(12) == 0) {
            return;
        }
        int l = random.nextInt(3);
        switch (l) {
            case 0: {
                setBlockAndMetadata(world, i, j, k, LOTRLegacyBlocks.mod("brick3"), 2);
                break;
            }
            case 1: {
                setBlockAndMetadata(world, i, j, k, LOTRLegacyBlocks.mod("brick3"), 3);
                break;
            }
            case 2: {
                setBlockAndMetadata(world, i, j, k, LOTRLegacyBlocks.mod("brick3"), 4);
            }
        }
    }

    @Override
    public void placePillar(WorldGenLevel world, int i, int j, int k, RandomSource random) {
        if (random.nextInt(12) == 0) {
            return;
        }
        if (random.nextInt(3) == 0) {
            setBlockAndMetadata(world, i, j, k, LOTRLegacyBlocks.mod("pillar"), 11);
        } else {
            setBlockAndMetadata(world, i, j, k, LOTRLegacyBlocks.mod("pillar"), 10);
        }
    }

    @Override
    public void placeRoof(WorldGenLevel world, int i, int j, int k, RandomSource random) {
        if (random.nextInt(12) == 0) {
            return;
        }
        super.placeRoof(world, i, j, k, random);
    }

    @Override
    public void placeRoofStairs(WorldGenLevel world, int i, int j, int k, int meta, RandomSource random) {
        if (random.nextInt(12) == 0) {
            return;
        }
        super.placeRoofStairs(world, i, j, k, meta, random);
    }

    @Override
    public void placeSlab(WorldGenLevel world, int i, int j, int k, boolean flag, RandomSource random) {
        if (random.nextInt(12) == 0) {
            return;
        }
        int l = random.nextInt(3);
        switch (l) {
            case 0: {
                setBlockAndMetadata(world, i, j, k, LOTRLegacyBlocks.mod("slabSingle5"), 5 | (flag ? 8 : 0));
                break;
            }
            case 1: {
                setBlockAndMetadata(world, i, j, k, LOTRLegacyBlocks.mod("slabSingle5"), 6 | (flag ? 8 : 0));
                break;
            }
            case 2: {
                setBlockAndMetadata(world, i, j, k, LOTRLegacyBlocks.mod("slabSingle5"), 7 | (flag ? 8 : 0));
            }
        }
    }

    @Override
    public void placeStairs(WorldGenLevel world, int i, int j, int k, int meta, RandomSource random) {
        if (random.nextInt(12) == 0) {
            return;
        }
        int l = random.nextInt(3);
        switch (l) {
            case 0: {
                setBlockAndMetadata(world, i, j, k, LOTRLegacyBlocks.mod("stairsHighElvenBrick"), meta);
                break;
            }
            case 1: {
                setBlockAndMetadata(world, i, j, k, LOTRLegacyBlocks.mod("stairsHighElvenBrickMossy"), meta);
                break;
            }
            case 2: {
                setBlockAndMetadata(world, i, j, k, LOTRLegacyBlocks.mod("stairsHighElvenBrickCracked"), meta);
            }
        }
    }

    @Override
    public void placeWall(WorldGenLevel world, int i, int j, int k, RandomSource random) {
        if (random.nextInt(12) == 0) {
            return;
        }
        int l = random.nextInt(3);
        switch (l) {
            case 0: {
                setBlockAndMetadata(world, i, j, k, LOTRLegacyBlocks.mod("wall2"), 11);
                break;
            }
            case 1: {
                setBlockAndMetadata(world, i, j, k, LOTRLegacyBlocks.mod("wall2"), 12);
                break;
            }
            case 2: {
                setBlockAndMetadata(world, i, j, k, LOTRLegacyBlocks.mod("wall2"), 13);
            }
        }
    }
}
