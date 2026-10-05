package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LOTRNumenorRuinStructure extends LOTRStructureBase2 {
    public LOTRNumenorRuinStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        int k1;
        int i1;
        int width = 3 + random.nextInt(3);
        setOriginAndRotation(world, i, j, k, rotation, width + 1);
        for (i1 = -width; i1 <= width; ++i1) {
            for (k1 = -width; k1 <= width; ++k1) {
                int j1;
                if (Math.abs(i1) == width || Math.abs(k1) == width) {
                    j1 = 0;
                    while (!isOpaque(world, i1, j1, k1) && getY(j1) >= world.getMinY()) {
                        placeRandomBrick(world, random, i1, j1, k1);
                        setGrassToDirt(world, i1, j1 - 1, k1);
                        --j1;
                    }
                    continue;
                }
                setBlockAndMetadata(world, i1, 0, k1, LOTRLegacyBlocks.vanilla("grass"), 0);
                j1 = -1;
                while (!isOpaque(world, i1, j1, k1) && getY(j1) >= world.getMinY()) {
                    setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("dirt"), 0);
                    setGrassToDirt(world, i1, j1 - 1, k1);
                    --j1;
                }
            }
        }
        if (random.nextBoolean()) {
            placeTree(world, random, "OAK_LARGE", originX, originY + 1, originZ);
        } else {
            placeTree(world, random, "BEECH_LARGE", originX, originY + 1, originZ);
        }
        for (i1 = -width; i1 <= width; ++i1) {
            for (k1 = -width; k1 <= width; ++k1) {
                if (Math.abs(i1) != width && Math.abs(k1) != width) {
                    continue;
                }
                int height = width * 2 + random.nextInt(8);
                for (int j1 = 1; j1 < height; ++j1) {
                    placeRandomBrick(world, random, i1, j1, k1);
                }
            }
        }
        setAir(world, 0, 1, -width);
        setAir(world, 0, 2, -width);
        int ruins = 10 + random.nextInt(20);
        for (int l = 0; l < ruins; ++l) {
            int j1;
            int k12;
            int i12 = -width * 2 + random.nextInt(width * 2 + 1);
            BlockState block = getBlockState(world, i12, (j1 = getTopBlock(world, i12, k12 = -width * 2 + random.nextInt(width * 2 + 1))) - 1, k12);
            if (!LOTRLegacyBlocks.vanilla("grass").matches(block) && !LOTRLegacyBlocks.vanilla("dirt").matches(block) && !LOTRLegacyBlocks.vanilla("stone").matches(block)) {
                continue;
            }
            int l1 = random.nextInt(3);
            if (l1 == 0) {
                setBlockAndMetadata(world, i12, j1 - 1, k12, LOTRLegacyBlocks.vanilla("gravel"), 0);
                continue;
            }
            if (l1 == 1) {
                placeRandomBrick(world, random, i12, j1 - 1, k12);
                continue;
            }
            int height = 1 + random.nextInt(3);
            for (int j2 = j1; j2 < j1 + height && !isOpaque(world, i12, j2, k12); ++j2) {
                placeRandomBrick(world, random, i12, j2, k12);
            }
        }
        return true;
    }

    public void placeRandomBrick(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        int l = random.nextInt(5);
        switch (l) {
            case 0:
                setBlockAndMetadata(world, i, j, k, LOTRLegacyBlocks.vanilla("stonebrick"), 0);
                break;
            case 1:
                setBlockAndMetadata(world, i, j, k, LOTRLegacyBlocks.vanilla("stonebrick"), 1);
                break;
            case 2:
                setBlockAndMetadata(world, i, j, k, LOTRLegacyBlocks.vanilla("stonebrick"), 2);
                break;
            case 3:
                setBlockAndMetadata(world, i, j, k, LOTRLegacyBlocks.vanilla("cobblestone"), 0);
                break;
            case 4:
                setBlockAndMetadata(world, i, j, k, LOTRLegacyBlocks.vanilla("mossy_cobblestone"), 0);
                break;
            default:
                break;
        }
    }
}
