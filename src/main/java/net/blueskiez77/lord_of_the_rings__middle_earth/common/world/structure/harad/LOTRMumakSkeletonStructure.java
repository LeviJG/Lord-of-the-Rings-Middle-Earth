package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRMumakSkeletonStructure extends LOTRStructureBase2 {
    public LegacyBlock boneBlock;
    public int boneMeta;

    public LOTRMumakSkeletonStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        setOriginAndRotation(world, i, j, k, rotation, 0);
        setupRandomBlocks(random);
        if (restrictions) {
            for (int i1 = -3; i1 <= 3; ++i1) {
                for (int k1 = -3; k1 <= 17; ++k1) {
                    int j1 = getTopBlock(world, i1, k1) - 1;
                    if (j1 >= -2) {
                        continue;
                    }
                    return false;
                }
            }
        }
        if (usingPlayer == null) {
            originY -= random.nextInt(6);
        }
        loadStrScan("mumak_skeleton");
        associateBlockMetaAlias("BONE", boneBlock, boneMeta);
        generateStrScan(world, random, 0, 1, 0);
        return true;
    }

    @Override
    public void setupRandomBlocks(RandomSource random) {
        boneBlock = LOTRLegacyBlocks.mod("boneBlock");
        boneMeta = 0;
    }
}
