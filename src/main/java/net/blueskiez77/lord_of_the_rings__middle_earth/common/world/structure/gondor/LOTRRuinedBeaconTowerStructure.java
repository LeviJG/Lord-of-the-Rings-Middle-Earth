package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRRuinedBeaconTowerStructure extends LOTRStructureBase2 {
    public LOTRRuinedBeaconTowerStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        int k1;
        int i1;
        int height = 4 + random.nextInt(4);
        setOriginAndRotation(world, i, j + (height + 1), k, rotation, 3);
        for (i1 = -2; i1 <= 2; ++i1) {
            for (k1 = -2; k1 <= 2; ++k1) {
                int i2 = Math.abs(i1);
                int k2 = Math.abs(k1);
                int j1 = 0;
                if (i2 == 2 && k2 < 2 || k2 == 2 && i2 < 2) {
                    j1 -= random.nextInt(4);
                }
                while (!isOpaque(world, i1, j1, k1) && getY(j1) >= world.getMinY()) {
                    if (i2 == 2 && k2 == 2) {
                        setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.mod("pillar"), 6);
                    } else {
                        placeRandomBrick(world, random, i1, j1, k1);
                    }
                    setGrassToDirt(world, i1, j1 - 1, k1);
                    --j1;
                }
            }
        }
        for (i1 = -1; i1 <= 1; ++i1) {
            for (k1 = -1; k1 <= 1; ++k1) {
                setBlockAndMetadata(world, i1, 0, k1, LOTRLegacyBlocks.mod("slabDouble"), 2);
            }
        }
        setBlockAndMetadata(world, 0, 1, 0, LOTRLegacyBlocks.mod("rock"), 1);
        for (int i12 : new int[]{-2, 2}) {
            for (int k12 : new int[]{-2, 2}) {
                int pillarHeight = 1 + random.nextInt(5);
                for (int j1 = 1; j1 <= pillarHeight; ++j1) {
                    setBlockAndMetadata(world, i12, j1, k12, LOTRLegacyBlocks.mod("pillar"), 6);
                }
            }
        }
        return true;
    }

    public void placeRandomBrick(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        if (random.nextInt(5) == 0) {
            setBlockAndMetadata(world, i, j, k, LOTRLegacyBlocks.mod("brick"), 2 + random.nextInt(2));
        } else {
            setBlockAndMetadata(world, i, j, k, LOTRLegacyBlocks.mod("brick"), 1);
        }
    }
}
