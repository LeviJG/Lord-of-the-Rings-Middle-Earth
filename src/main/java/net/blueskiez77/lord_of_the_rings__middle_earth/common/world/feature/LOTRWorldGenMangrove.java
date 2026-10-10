package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LOTRWorldGenMangrove extends LOTRFeature {
    public LegacyBlock woodID = LOTRLegacyBlocks.mod("wood3");
    public int woodMeta = 3;
    public LegacyBlock leafID = LOTRLegacyBlocks.mod("leaves3");
    public int leafMeta = 3;

    public LOTRWorldGenMangrove(boolean flag) {
        super(flag);
    }

    @Override
    protected boolean generateFeature(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        int height = 6 + random.nextInt(5);
        boolean flag = true;
        if (j >= 1 && j + height + 1 <= 256) {
            boolean canGrow;
            for (int j1 = j; j1 <= j + 1 + height; ++j1) {
                int range = 1;
                if (j1 == j) {
                    range = 0;
                }
                if (j1 >= j + 1 + height - 2) {
                    range = 2;
                }
                for (int i1 = i - range; i1 <= i + range && flag; ++i1) {
                    for (int k1 = k - range; k1 <= k + range && flag; ++k1) {
                        if (j1 >= 0 && j1 < 256 && isReplaceable(world, i1, j1, k1)) {
                            continue;
                        }
                        flag = false;
                    }
                }
            }
            if (!flag) {
                return false;
            }
            BlockState below = getBlock(world, i, j - 1, k);
            canGrow = canSustainPlant(world, i, j - 1, k) || canSustainPlant(world, i, j - 1, k);
            if (canGrow) {
                int j1;
                onPlantGrow(world, i, j - 1, k);
                int leafStart = 3;
                int leafRangeMin = 0;
                int leafRangeFactor = 2;
                for (j1 = j - leafStart + height; j1 <= j + height; ++j1) {
                    int j2 = j1 - (j + height);
                    int leafRange = leafRangeMin + 1 - j2 / leafRangeFactor;
                    for (int i1 = i - leafRange; i1 <= i + leafRange; ++i1) {
                        int i2 = i1 - i;
                        for (int k1 = k - leafRange; k1 <= k + leafRange; ++k1) {
                            int k2 = k1 - k;
                            BlockState block = getBlock(world, i1, j1, k1);
                            if (Math.abs(i2) == leafRange && Math.abs(k2) == leafRange && (random.nextInt(2) == 0 || j2 == 0) || !canBeReplacedByLeaves(block)) {
                                continue;
                            }
                            setBlockAndNotifyAdequately(world, i1, j1, k1, leafID, leafMeta);
                            if (random.nextInt(8) == 0 && (getBlock(world, i1 - 1, j1, k1)).isAir()) {
                                growVines(world, random, i1 - 1, j1, k1, 8);
                            }
                            if (random.nextInt(8) == 0 && (getBlock(world, i1 + 1, j1, k1)).isAir()) {
                                growVines(world, random, i1 + 1, j1, k1, 2);
                            }
                            if (random.nextInt(8) == 0 && (getBlock(world, i1, j1, k1 - 1)).isAir()) {
                                growVines(world, random, i1, j1, k1 - 1, 1);
                            }
                            if (random.nextInt(8) != 0 || !(getBlock(world, i1, j1, k1 + 1)).isAir()) {
                                continue;
                            }
                            growVines(world, random, i1, j1, k1 + 1, 4);
                        }
                    }
                }
                for (j1 = 0; j1 < height; ++j1) {
                    BlockState block = getBlock(world, i, j + j1, k);
                    if (!isBlockReplaceable(block) && !isLeaves(block)) {
                        continue;
                    }
                    setBlockAndNotifyAdequately(world, i, j + j1, k, woodID, woodMeta);
                }
                for (int i1 = i - 1; i1 <= i + 1; ++i1) {
                    for (int k1 = k - 1; k1 <= k + 1; ++k1) {
                        int i2 = i1 - i;
                        int k2 = k1 - k;
                        if (Math.abs(i2) == Math.abs(k2)) {
                            continue;
                        }
                        int rootX = i1;
                        int rootY = j + 1 + random.nextInt(3);
                        int rootZ = k1;
                        int xWay = Integer.signum(i2);
                        int zWay = Integer.signum(k2);
                        int roots = 0;
                        while (isBlockReplaceable(getBlock(world, rootX, rootY, k1))) {
                            setBlockAndNotifyAdequately(world, rootX, rootY, rootZ, woodID, woodMeta | 0xC);
                            onPlantGrow(world, rootX, rootY - 1, rootZ);
                            --rootY;
                            if (random.nextInt(3) > 0) {
                                rootX += xWay;
                                rootZ += zWay;
                            }
                            roots++;
                            random.nextInt(3);
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }

    public void growVines(WorldGenLevel world, RandomSource random, int i, int j, int k, int meta) {
        setBlockAndNotifyAdequately(world, i, j, k, LOTRLegacyBlocks.vanilla("vine"), meta);
        int vines = 0;
        while ((getBlock(world, i, --j, k)).isAir() && vines < 2 + random.nextInt(3)) {
            setBlockAndNotifyAdequately(world, i, j, k, LOTRLegacyBlocks.vanilla("vine"), meta);
            ++vines;
        }
    }
}
