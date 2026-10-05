package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRSouthronStatueStructure extends LOTRSouthronStructure {
    public LOTRSouthronStatueStructure(boolean flag) {
        super(flag);
    }

    public void assocStatueBlocks() {
        associateBlockMetaAlias("STONE", stoneBlock, stoneMeta);
        associateBlockMetaAlias("BRICK", brickBlock, brickMeta);
        associateBlockMetaAlias("BRICK_SLAB", brickSlabBlock, brickSlabMeta);
        associateBlockMetaAlias("BRICK_SLAB_INV", brickSlabBlock, brickSlabMeta | 8);
        associateBlockAlias("BRICK_STAIR", brickStairBlock);
        associateBlockMetaAlias("BRICK_WALL", brickWallBlock, brickWallMeta);
        associateBlockMetaAlias("PILLAR", pillarBlock, pillarMeta);
        associateBlockMetaAlias("BRICK2", brick2Block, brick2Meta);
        associateBlockMetaAlias("BRICK2_SLAB", brick2SlabBlock, brick2SlabMeta);
        associateBlockMetaAlias("BRICK2_SLAB_INV", brick2SlabBlock, brick2SlabMeta | 8);
        associateBlockAlias("BRICK2_STAIR", brick2StairBlock);
        associateBlockMetaAlias("BRICK2_WALL", brick2WallBlock, brick2WallMeta);
        associateBlockMetaAlias("BRICK2_CARVED", brick2CarvedBlock, brick2CarvedMeta);
        associateBlockMetaAlias("PILLAR2", pillar2Block, pillar2Meta);
    }

    @Override
    public boolean canUseRedBricks() {
        return false;
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        int k1;
        int i1;
        setOriginAndRotation(world, i, j, k, rotation, 6);
        setupRandomBlocks(random);
        if (restrictions) {
            for (i1 = -5; i1 <= 5; ++i1) {
                for (k1 = -5; k1 <= 5; ++k1) {
                    int j1 = getTopBlock(world, i1, k1) - 1;
                    if (isSurface(world, i1, j1, k1)) {
                        continue;
                    }
                    return false;
                }
            }
        }
        for (i1 = -5; i1 <= 5; ++i1) {
            for (k1 = -5; k1 <= 5; ++k1) {
                int i2 = Math.abs(i1);
                int k2 = Math.abs(k1);
                if (i2 == 5 && k2 == 5) {
                    continue;
                }
                for (int j1 = 1; j1 <= 10; ++j1) {
                    setAir(world, i1, j1, k1);
                }
            }
        }
        loadStrScan("southron_statue_base");
        assocStatueBlocks();
        generateStrScan(world, random, 0, 0, 0);
        String statue = getRandomStatueStrscan(random);
        loadStrScan(statue);
        assocStatueBlocks();
        generateStrScan(world, random, 0, 4, 0);
        return true;
    }

    public String getRandomStatueStrscan(RandomSource random) {
        String[] statues = {"mumak", "bird", "snake"};
        return "southron_statue_" + statues[random.nextInt(statues.length)];
    }

    @Override
    public void setupRandomBlocks(RandomSource random) {
        super.setupRandomBlocks(random);
        if (isUmbar()) {
            brick2Block = LOTRLegacyBlocks.mod("brick6");
            brick2Meta = 6;
            brick2SlabBlock = LOTRLegacyBlocks.mod("slabSingle13");
            brick2SlabMeta = 2;
            brick2StairBlock = LOTRLegacyBlocks.mod("stairsUmbarBrick");
            brick2WallBlock = LOTRLegacyBlocks.mod("wall5");
            brick2WallMeta = 0;
            brick2CarvedBlock = LOTRLegacyBlocks.mod("brick6");
            brick2CarvedMeta = 8;
            pillar2Block = LOTRLegacyBlocks.mod("pillar2");
            pillar2Meta = 10;
        }
    }
}
