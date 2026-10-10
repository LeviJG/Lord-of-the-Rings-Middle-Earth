package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LOTRWorldGenGnarledOak extends LOTRFeature {
    public LegacyBlock woodBlock = LOTRLegacyBlocks.vanilla("log");
    public int woodMeta;
    public LegacyBlock leafBlock = LOTRLegacyBlocks.vanilla("leaves");
    public int leafMeta;
    public int minHeight = 4;
    public int maxHeight = 9;

    public LOTRWorldGenGnarledOak(boolean flag) {
        super(flag);
    }

    @Override
    protected boolean generateFeature(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        int height = LOTRWorldGenUtil.getRandomIntegerInRange(random, minHeight, maxHeight);
        boolean flag = true;
        if (j >= 1 && height + 1 <= 256) {
            for (int j1 = j; j1 <= j + height + 1; ++j1) {
                int range = 1;
                if (j1 == j) {
                    range = 0;
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
        if (!flag) {
            return false;
        }
        boolean canGrow = true;
        BlockState below = getBlock(world, i, j - 1, k);
        if (!canSustainPlant(world, i, j - 1, k)) {
            canGrow = false;
        }
        if (!canGrow) {
            return false;
        }
        below = getBlock(world, i, j - 1, k);
        onPlantGrow(world, i, j - 1, k);
        for (int j1 = j; j1 < j + height; ++j1) {
            setBlockAndNotifyAdequately(world, i, j1, k, woodBlock, woodMeta);
        }
        generateLeaves(world, random, i, j + height, k);
        int branches = 2 + random.nextInt(3);
        for (int b = 0; b < branches; ++b) {
            float angle = random.nextFloat() * 3.1415927f * 2.0f;
            float cos = Mth.cos(angle);
            float sin = Mth.sin(angle);
            float angleY = random.nextFloat() * 0.6981317007977318f;
            float sinY = Mth.sin(angleY);
            int length = 2 + random.nextInt(3);
            int i1 = i;
            int k1 = k;
            int j1 = j + height - 1 - random.nextInt(3);
            if (j1 < j + 2) {
                j1 = j + 2;
            }
            for (int l = 0; l < length; ++l) {
                if (Math.floor(cos * l) != Math.floor(cos * (l - 1))) {
                    i1 = (int) (i1 + Math.signum(cos));
                }
                if (Math.floor(sin * l) != Math.floor(sin * (l - 1))) {
                    k1 = (int) (k1 + Math.signum(sin));
                }
                if (Math.floor(sinY * l) != Math.floor(sinY * (l - 1))) {
                    j1 = (int) (j1 + Math.signum(sinY));
                }
                if (!isReplaceable(world, i1, j1, k1)) {
                    break;
                }
                setBlockAndNotifyAdequately(world, i1, j1, k1, woodBlock, woodMeta | 0xC);
            }
            generateLeaves(world, random, i1, j1, k1);
        }
        int lastDir = -1;
        for (int j1 = j + 2; j1 < j + height; ++j1) {
            int dir;
            int k1;
            BlockState block;
            int i1;
            if (random.nextInt(3) != 0 || (dir = random.nextInt(4)) == lastDir) {
                continue;
            }
            lastDir = dir;
            int length = 1;
            for (int l = 1; l <= length && (isBlockReplaceable((block = getBlock(world, i1 = i + DIR_OFFSET_X[dir] * l, j1, k1 = k + DIR_OFFSET_Z[dir] * l))) || isLeaves(block)); ++l) {
                if (dir == 0 || dir == 2) {
                    setBlockAndNotifyAdequately(world, i1, j1, k1, woodBlock, woodMeta | 8);
                    continue;
                }
                setBlockAndNotifyAdequately(world, i1, j1, k1, woodBlock, woodMeta | 4);
            }
        }
        for (int i1 = i - 1; i1 <= i + 1; ++i1) {
            for (int k1 = k - 1; k1 <= k + 1; ++k1) {
                if (i1 == i && k1 == k || random.nextInt(4) > 0) {
                    continue;
                }
                int rootY = j + random.nextInt(2);
                int roots = 0;
                while (isBlockReplaceable(getBlock(world, i1, rootY, k1))) {
                    setBlockAndNotifyAdequately(world, i1, rootY, k1, woodBlock, woodMeta | 0xC);
                    onPlantGrow(world, i1, rootY - 1, k1);
                    --rootY;
                    roots++;
                    random.nextInt(3);
                }
            }
        }
        return true;
    }

    public void generateLeaves(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        int leafRange = 3;
        int leafRangeSq = leafRange * leafRange;
        int leafRangeSqLess = (int) ((leafRange - 0.5) * (leafRange - 0.5));
        for (int i1 = i - leafRange; i1 <= i + leafRange; ++i1) {
            for (int j1 = j - leafRange + 1; j1 <= j + leafRange; ++j1) {
                for (int k1 = k - leafRange; k1 <= k + leafRange; ++k1) {
                    BlockState block;
                    int i2 = i1 - i;
                    int j2 = j1 - j;
                    int k2 = k1 - k;
                    int dist = i2 * i2 + j2 * j2 + k2 * k2;
                    if (dist >= leafRangeSqLess && (dist >= leafRangeSq || random.nextInt(3) != 0) || !isBlockReplaceable((block = getBlock(world, i1, j1, k1))) && !isLeaves(block)) {
                        continue;
                    }
                    setBlockAndNotifyAdequately(world, i1, j1, k1, leafBlock, leafMeta);
                }
            }
        }
    }

    public LOTRWorldGenGnarledOak setBlocks(LegacyBlock b1, int m1, LegacyBlock b2, int m2) {
        woodBlock = b1;
        woodMeta = m1;
        leafBlock = b2;
        leafMeta = m2;
        return this;
    }

    public LOTRWorldGenGnarledOak setMinMaxHeight(int min, int max) {
        minHeight = min;
        maxHeight = max;
        return this;
    }
}
