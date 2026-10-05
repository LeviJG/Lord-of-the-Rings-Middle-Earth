package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRHarnedorPalisadeStructure extends LOTRHarnedorStructure {
    public boolean isTall;

    public LOTRHarnedorPalisadeStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        int k1;
        int i1;
        setOriginAndRotation(world, i, j, k, rotation, 0);
        setupRandomBlocks(random);
        if (restrictions && !isSurface(world, i1 = 0, getTopBlock(world, i1, k1 = 0) - 1, k1)) {
            return false;
        }
        int height = 3 + random.nextInt(2);
        if (isTall) {
            height += 4;
        }
        if (isRuined()) {
            height = Math.max(1, height - 2);
        }
        for (int j12 = height; (j12 >= 0 || !isOpaque(world, 0, j12, 0)) && getY(j12) >= world.getMinY(); --j12) {
            setBlockAndMetadata(world, 0, j12, 0, woodBlock, woodMeta);
            setGrassToDirt(world, 0, j12 - 1, 0);
        }
        if (isTall || random.nextInt(5) == 0) {
            setBlockAndMetadata(world, 0, height + 1, 0, fenceBlock, fenceMeta);
            placeSkull(world, random, 0, height + 2, 0);
        }
        if (!isRuined() && isTall) {
            placeWallBanner(world, 0, height, 0, "NEAR_HARAD", 2);
        }
        return true;
    }

    public void setTall() {
        isTall = true;
    }

    @Override
    public void setupRandomBlocks(RandomSource random) {
        super.setupRandomBlocks(random);
        int randomWood = random.nextInt(3);
        switch (randomWood) {
            case 0:
                woodBlock = LOTRLegacyBlocks.mod("wood4");
                woodMeta = 2;
                break;
            case 1:
                woodBlock = LOTRLegacyBlocks.vanilla("log");
                woodMeta = 0;
                break;
            case 2:
                woodBlock = LOTRLegacyBlocks.mod("wood6");
                woodMeta = 3;
                break;
            default:
                break;
        }
    }
}
