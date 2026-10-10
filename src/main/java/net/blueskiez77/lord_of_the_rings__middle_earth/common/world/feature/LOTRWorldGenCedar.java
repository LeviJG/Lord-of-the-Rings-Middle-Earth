package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LOTRWorldGenCedar extends LOTRFeature {
    public LegacyBlock woodBlock = LOTRLegacyBlocks.mod("wood4");
    public int woodMeta = 2;
    public LegacyBlock leafBlock = LOTRLegacyBlocks.mod("leaves4");
    public int leafMeta = 2;
    public int minHeight = 10;
    public int maxHeight = 16;
    public boolean hangingLeaves;

    public LOTRWorldGenCedar(boolean flag) {
        super(flag);
    }

    @Override
    protected boolean generateFeature(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        BlockState below;
        int canopyMin;
        int j1;
        int height = LOTRWorldGenUtil.getRandomIntegerInRange(random, minHeight, maxHeight);
        boolean flag = true;
        if (j >= 1 && height + 1 <= 256) {
            for (int j12 = j; j12 <= j + height + 1; ++j12) {
                int range = 1;
                if (j12 == j) {
                    range = 0;
                }
                if (j12 >= j + height - 1) {
                    range = 2;
                }
                for (int i1 = i - range; i1 <= i + range && flag; ++i1) {
                    for (int k1 = k - range; k1 <= k + range && flag; ++k1) {
                        if (j12 >= 0 && j12 < 256 && isReplaceable(world, i1, j12, k1)) {
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
        for (j1 = canopyMin = j + height - 2; j1 <= j + height; ++j1) {
            int leafRange = 2 - (j1 - (j + height));
            spawnLeaves(world, random, i, j1, k, leafRange);
            if (j1 != canopyMin) {
                continue;
            }
            for (int i1 = i - 1; i1 <= i + 1; ++i1) {
                for (int k1 = k - 1; k1 <= k + 1; ++k1) {
                    BlockState block;
                    if (i1 != i && k1 != k || !isBlockReplaceable((block = getBlock(world, i1, j1, k1))) && !isLeaves(block)) {
                        continue;
                    }
                    setBlockAndNotifyAdequately(world, i1, j1, k1, woodBlock, woodMeta);
                }
            }
        }
        for (j1 = j + height - 1; j1 > j + height / 2; j1 -= 1 + random.nextInt(3)) {
            int branches = 1 + random.nextInt(3);
            block7:
            for (int l = 0; l < branches; ++l) {
                float angle = random.nextFloat() * 3.1415927f * 2.0f;
                int i1 = i;
                int k1 = k;
                int j2 = j1;
                int length = LOTRWorldGenUtil.getRandomIntegerInRange(random, 4, 7);
                int leafMin = 1 + random.nextInt(2);
                for (int l1 = 0; l1 < length; ++l1) {
                    i1 = i + (int) (0.5f + Mth.cos(angle) * (l1 + 1));
                    BlockState block = getBlock(world, i1, j2 = j1 - 3 + l1 / 2, k1 = k + (int) (0.5f + Mth.sin(angle) * (l1 + 1)));
                    if (!isBlockReplaceable(block) && !isWood(block) && !isLeaves(block)) {
                        continue block7;
                    }
                    setBlockAndNotifyAdequately(world, i1, j2, k1, woodBlock, woodMeta);
                    if (l1 != length - 1 || leafMin < 2) {
                        continue;
                    }
                    for (int i2 = i1 - 1; i2 <= i1 + 1; ++i2) {
                        for (int k2 = k1 - 1; k2 <= k1 + 1; ++k2) {
                            BlockState block1;
                            int j3;
                            if (i2 != i1 && k2 != k1 || !isBlockReplaceable((block1 = getBlock(world, i2, j3 = j2 - 1, k2))) && !isLeaves(block1)) {
                                continue;
                            }
                            setBlockAndNotifyAdequately(world, i2, j3, k2, woodBlock, woodMeta);
                        }
                    }
                }
                for (int j3 = j2 - leafMin; j3 <= j2; ++j3) {
                    int leafRange = 1 - (j3 - j2);
                    spawnLeaves(world, random, i1, j3, k1, leafRange);
                }
            }
        }
        for (j1 = 0; j1 < height; ++j1) {
            setBlockAndNotifyAdequately(world, i, j + j1, k, woodBlock, woodMeta);
        }
        for (int i1 = i - 1; i1 <= i + 1; ++i1) {
            for (int k1 = k - 1; k1 <= k + 1; ++k1) {
                int i2 = i1 - i;
                int k2 = k1 - k;
                if (Math.abs(i2) == Math.abs(k2)) {
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

    public LOTRWorldGenCedar setBlocks(LegacyBlock b1, int m1, LegacyBlock b2, int m2) {
        woodBlock = b1;
        woodMeta = m1;
        leafBlock = b2;
        leafMeta = m2;
        return this;
    }

    public LOTRWorldGenCedar setHangingLeaves() {
        hangingLeaves = true;
        return this;
    }

    public LOTRWorldGenCedar setMinMaxHeight(int min, int max) {
        minHeight = min;
        maxHeight = max;
        return this;
    }

    public void spawnLeaves(WorldGenLevel world, RandomSource random, int i, int j, int k, int leafRange) {
        int leafRangeSq = leafRange * leafRange;
        for (int i1 = i - leafRange; i1 <= i + leafRange; ++i1) {
            for (int k1 = k - leafRange; k1 <= k + leafRange; ++k1) {
                BlockState block;
                int i2 = i1 - i;
                int k2 = k1 - k;
                if (i2 * i2 + k2 * k2 > leafRangeSq || !isBlockReplaceable((block = getBlock(world, i1, j, k1))) && !isLeaves(block)) {
                    continue;
                }
                setBlockAndNotifyAdequately(world, i1, j, k1, leafBlock, leafMeta);
                if (!hangingLeaves || random.nextInt(10) != 0) {
                    continue;
                }
                setBlockAndNotifyAdequately(world, i1, j, k1, woodBlock, woodMeta);
                BlockState block1 = getBlock(world, i1, j + 1, k1);
                if (isBlockReplaceable(block1) || isLeaves(block1)) {
                    setBlockAndNotifyAdequately(world, i1, j + 1, k1, leafBlock, leafMeta);
                }
                int hang = 2 + random.nextInt(3);
                for (int j1 = j - 1; j1 >= j - hang; --j1) {
                    BlockState block2 = getBlock(world, i1, j1, k1);
                    if (!isBlockReplaceable(block2) && !isLeaves(block2)) {
                        continue;
                    }
                    setBlockAndNotifyAdequately(world, i1, j1, k1, leafBlock, leafMeta);
                }
            }
        }
    }
}
