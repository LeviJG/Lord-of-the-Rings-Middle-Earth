package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LOTRWorldGenPine extends LOTRFeature {
    public LegacyBlock woodBlock = LOTRLegacyBlocks.mod("wood5");
    public int woodMeta;
    public LegacyBlock leafBlock = LOTRLegacyBlocks.mod("leaves5");
    public int leafMeta;
    public int minHeight = 12;
    public int maxHeight = 24;

    public LOTRWorldGenPine(boolean flag) {
        super(flag);
    }

    @Override
    protected boolean generateFeature(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        BlockState below;
        int height = LOTRWorldGenUtil.getRandomIntegerInRange(random, minHeight, maxHeight);
        boolean flag = true;
        if (j >= 1 && height + 1 <= 256) {
            for (int j1 = j; j1 <= j + height + 1; ++j1) {
                int range = 1;
                if (j1 == j) {
                    range = 0;
                }
                if (j1 >= j + height - 1) {
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
        } else {
            flag = false;
        }
        if (!((below = getBlock(world, i, j - 1, k)) != null && canSustainPlant(world, i, j - 1, k))) {
            flag = false;
        }
        if (!flag) {
            return false;
        }
        onPlantGrow(world, i, j - 1, k);
        setBlockAndNotifyAdequately(world, i, j + height, k, leafBlock, leafMeta);
        generateLeafLayer(world, random, i, j + height - 1, k, 1);
        int leafHeight = j + height - 3;
        int minLeafHeight = j + (int) (height * 0.5f);
        while (leafHeight > minLeafHeight) {
            int r = random.nextInt(3);
            if (r == 0) {
                generateLeafLayer(world, random, i, leafHeight, k, 1);
                leafHeight -= 2;
                continue;
            }
            if (r == 1) {
                leafHeight--;
                generateLeafLayer(world, random, i, leafHeight + 1, k, 1);
                generateLeafLayer(world, random, i, leafHeight, k, 2);
                generateLeafLayer(world, random, i, leafHeight - 1, k, 1);
                leafHeight -= 3;
                continue;
            }
            leafHeight--;
            generateLeafLayer(world, random, i, leafHeight + 1, k, 2);
            generateLeafLayer(world, random, i, leafHeight, k, 3);
            generateLeafLayer(world, random, i, leafHeight - 1, k, 2);
            leafHeight -= 3;
        }
        generateLeafLayer(world, random, i, leafHeight, k, 1);
        int lastDir = -1;
        for (int j1 = j; j1 < j + height; ++j1) {
            int i1;
            int k1;
            int dir;
            setBlockAndNotifyAdequately(world, i, j1, k, woodBlock, woodMeta);
            if (j1 < j + 3 || j1 >= minLeafHeight || random.nextInt(3) != 0 || (dir = random.nextInt(4)) == lastDir) {
                continue;
            }
            lastDir = dir;
            int length = 1;
            for (int l = 1; l <= length && isReplaceable(world, i1 = i + DIR_OFFSET_X[dir] * l, j1, k1 = k + DIR_OFFSET_Z[dir] * l); ++l) {
                if (dir == 0 || dir == 2) {
                    setBlockAndNotifyAdequately(world, i1, j1, k1, woodBlock, woodMeta | 8);
                    continue;
                }
                setBlockAndNotifyAdequately(world, i1, j1, k1, woodBlock, woodMeta | 4);
            }
        }
        return true;
    }

    public void generateLeafLayer(WorldGenLevel world, RandomSource random, int i, int j, int k, int range) {
        for (int i1 = i - range; i1 <= i + range; ++i1) {
            for (int k1 = k - range; k1 <= k + range; ++k1) {
                BlockState block;
                int i2 = Math.abs(i1 - i);
                if (i2 + Math.abs(k1 - k) > range || !isBlockReplaceable((block = getBlock(world, i1, j, k1))) && !isLeaves(block)) {
                    continue;
                }
                setBlockAndNotifyAdequately(world, i1, j, k1, leafBlock, leafMeta);
            }
        }
    }

    public LOTRWorldGenPine setMinMaxHeight(int min, int max) {
        minHeight = min;
        maxHeight = max;
        return this;
    }
}
