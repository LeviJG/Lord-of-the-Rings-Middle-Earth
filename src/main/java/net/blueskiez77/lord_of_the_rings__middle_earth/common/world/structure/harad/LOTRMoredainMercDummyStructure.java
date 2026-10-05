package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRMoredainMercDummyStructure extends LOTRStructureBase2 {
    public LOTRMoredainMercDummyStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        setOriginAndRotation(world, i, j, k, rotation, 0);
        setupRandomBlocks(random);
        if (restrictions) {
            for (int i1 = -1; i1 <= 1; ++i1) {
                for (int k1 = -1; k1 <= 1; ++k1) {
                    int j1 = getTopBlock(world, i1, k1) - 1;
                    if (j1 != getTopBlock(world, 0, 0) - 1 || !isSurface(world, i1, j1, k1)) {
                        return false;
                    }
                    for (int j2 = j1 + 1; j2 <= j1 + 3; ++j2) {
                        if (!isOpaque(world, i1, j2, k1)) {
                            continue;
                        }
                        return false;
                    }
                }
            }
        }
        setBlockAndMetadata(world, 0, 1, 0, LOTRLegacyBlocks.mod("fence2"), 2);
        setBlockAndMetadata(world, 0, 2, 0, LOTRLegacyBlocks.vanilla("wool"), 12);
        placeSkull(world, random, 0, 3, 0);
        setBlockAndMetadata(world, -1, 2, 0, LOTRLegacyBlocks.vanilla("lever"), 1);
        setBlockAndMetadata(world, 1, 2, 0, LOTRLegacyBlocks.vanilla("lever"), 2);
        return true;
    }
}
