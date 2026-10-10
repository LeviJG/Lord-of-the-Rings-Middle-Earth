package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.farharad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiomes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRFarHaradSavannahBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

/** LOTRWorldGenMoredainCamp: a hunters' table and two to four hunters' huts, where the savannah is not peopled. */
public class LOTRMoredainCampStructure extends LOTRStructureBase2 {
    public LOTRMoredainCampStructure(boolean flag) {
        super(flag);
    }

    public void attemptHutSpawn(LOTRStructureBase2 structure, WorldGenLevel world, RandomSource random) {
        structure.restrictions = restrictions;
        structure.usingPlayer = usingPlayer;
        for (int l = 0; l < 16; ++l) {
            int x = LOTRWorldGenUtil.getRandomIntegerInRange(random, 4, 8);
            int z = LOTRWorldGenUtil.getRandomIntegerInRange(random, 4, 8);
            int spawnX = getX(x *= random.nextBoolean() ? -1 : 1, z *= random.nextBoolean() ? -1 : 1);
            int spawnZ = getZ(x, z);
            int spawnY = getY(getTopBlock(world, x, z));
            if (generateChild(structure, world, random, spawnX, spawnY, spawnZ, random.nextInt(4))) {
                return;
            }
        }
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        setOriginAndRotation(world, i, j, k, rotation, 0);
        if (restrictions) {
            boolean suitableSpawn = false;
            if (LOTRBiomes.of(world.getBiome(new BlockPos(originX, originY, originZ))) instanceof LOTRFarHaradSavannahBiome) {
                suitableSpawn = !LOTRFarHaradSavannahBiome.isBiomePopulated(originX, originY, originZ);
            }
            if (!suitableSpawn) {
                return false;
            }
            int minHeight = 0;
            int maxHeight = 0;
            int range = 3;
            for (int i1 = -range; i1 <= range; ++i1) {
                for (int k1 = -range; k1 <= range; ++k1) {
                    int j1 = getTopBlock(world, i1, k1);
                    BlockState block = getBlockState(world, i1, j1 - 1, k1);
                    if (!LOTRLegacyBlocks.vanilla("grass").matches(block) && !LOTRLegacyBlocks.vanilla("dirt").matches(block)
                            && !LOTRLegacyBlocks.vanilla("sand").matches(block) && !LOTRLegacyBlocks.vanilla("stone").matches(block)) {
                        return false;
                    }
                    if (j1 < minHeight) {
                        minHeight = j1;
                    }
                    if (j1 > maxHeight) {
                        maxHeight = j1;
                    }
                    if (maxHeight - minHeight > 5) {
                        return false;
                    }
                }
            }
        }
        for (int i1 = -1; i1 <= 1; ++i1) {
            for (int k1 = -1; k1 <= 1; ++k1) {
                for (int j1 = 0; (j1 == 0 || !isOpaque(world, i1, j1, k1)) && getY(j1) >= world.getMinY(); --j1) {
                    setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("hardened_clay"), 0);
                    setGrassToDirt(world, i1, j1 - 1, k1);
                }
                if (i1 == 0 && k1 == 0) {
                    setBlockAndMetadata(world, 0, 1, 0, LOTRLegacyBlocks.mod("moredainTable"), 0);
                    continue;
                }
                setBlockAndMetadata(world, i1, 1, k1, LOTRLegacyBlocks.mod("slabSingle7"), 0);
            }
        }
        int huts = LOTRWorldGenUtil.getRandomIntegerInRange(random, 2, 4);
        for (int l = 0; l < huts; ++l) {
            attemptHutSpawn(new LOTRMoredainHutHunterStructure(notifyChanges), world, random);
        }
        return true;
    }
}
