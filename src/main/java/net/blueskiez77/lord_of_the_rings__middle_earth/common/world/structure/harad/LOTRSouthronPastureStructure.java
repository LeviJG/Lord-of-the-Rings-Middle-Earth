package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRSouthronPastureStructure extends LOTRSouthronStructure {
    public LOTRSouthronPastureStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        int j1;
        setOriginAndRotation(world, i, j, k, rotation, 6);
        setupRandomBlocks(random);
        if (restrictions) {
            int minHeight = 0;
            int maxHeight = 0;
            for (int i1 = -4; i1 <= 4; ++i1) {
                for (int k1 = -6; k1 <= 6; ++k1) {
                    j1 = getTopBlock(world, i1, k1) - 1;
                    if (!isSurface(world, i1, j1, k1)) {
                        return false;
                    }
                    if (j1 < minHeight) {
                        minHeight = j1;
                    }
                    if (j1 > maxHeight) {
                        maxHeight = j1;
                    }
                    if (maxHeight - minHeight <= 6) {
                        continue;
                    }
                    return false;
                }
            }
        }
        for (int i1 = -4; i1 <= 4; ++i1) {
            for (int k1 = -6; k1 <= 6; ++k1) {
                int i2 = Math.abs(i1);
                int k2 = Math.abs(k1);
                if (i2 == 4 && k2 == 6) {
                    continue;
                }
                for (j1 = 1; j1 <= 4; ++j1) {
                    setAir(world, i1, j1, k1);
                }
            }
        }
        loadStrScan("southron_pasture");
        associateBlockMetaAlias("STONE", stoneBlock, stoneMeta);
        associateBlockMetaAlias("BRICK", brickBlock, brickMeta);
        associateBlockMetaAlias("BRICK_SLAB", brickSlabBlock, brickSlabMeta);
        associateBlockMetaAlias("FENCE", fenceBlock, fenceMeta);
        associateBlockAlias("FENCE_GATE", fenceGateBlock);
        generateStrScan(world, random, 0, 0, 0);
        int animals = 2 + random.nextInt(4);
        for (int l = 0; l < animals; ++l) {
            Animal animal = LOTRHarnedorPastureStructure.getRandomAnimal(world, random);
            spawnNPCAndSetHome(animal, world, 0, 1, 0, 0);
            animal.clearHome();
        }
        return true;
    }
}
