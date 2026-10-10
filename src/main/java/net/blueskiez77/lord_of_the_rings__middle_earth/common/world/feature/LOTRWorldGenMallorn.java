package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LOTRWorldGenMallorn extends LOTRFeature {
    public int minHeight = 10;
    public int maxHeight = 14;
    public LegacyBlock woodBlock = LOTRLegacyBlocks.mod("wood");
    public int woodMeta = 1;
    public LegacyBlock leafBlock = LOTRLegacyBlocks.mod("leaves");
    public int leafMeta = 1;

    public LOTRWorldGenMallorn(boolean flag) {
        super(flag);
    }

    @Override
    protected boolean generateFeature(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        int height = LOTRWorldGenUtil.getRandomIntegerInRange(random, minHeight, maxHeight);
        int leafMin = j + (int) (height * 0.6f);
        boolean flag = true;
        if (j >= 1 && j + height + 1 <= 256) {
            for (int j1 = j; j1 <= j + height + 1; ++j1) {
                int range = 1;
                if (j1 == j) {
                    range = 0;
                }
                if (j1 >= leafMin) {
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
            boolean canGrow = true;
            BlockState below = getBlock(world, i, j - 1, k);
            if (!canSustainPlant(world, i, j - 1, k)) {
                canGrow = false;
            }
            if (canGrow) {
                int j1;
                below = getBlock(world, i, j - 1, k);
                onPlantGrow(world, i, j - 1, k);
                int deg = 0;
                for (j1 = j + height; j1 >= leafMin; --j1) {
                    int branches = 1 + random.nextInt(2);
                    for (int b = 0; b < branches; ++b) {
                        float angle = (float) Math.toRadians(deg += 50 + random.nextInt(70));
                        float cos = Mth.cos(angle);
                        float sin = Mth.sin(angle);
                        float angleY = random.nextFloat() * 0.8726646259971648f;
                        float sinY = Mth.sin(angleY);
                        int length = 4 + random.nextInt(6);
                        int i1 = i;
                        int k1 = k;
                        int j2 = j1;
                        for (int l = 0; l < length; ++l) {
                            BlockState block;
                            if (Math.floor(cos * l) != Math.floor(cos * (l - 1))) {
                                i1 = (int) (i1 + Math.signum(cos));
                            }
                            if (Math.floor(sin * l) != Math.floor(sin * (l - 1))) {
                                k1 = (int) (k1 + Math.signum(sin));
                            }
                            if (Math.floor(sinY * l) != Math.floor(sinY * (l - 1))) {
                                j2 = (int) (j2 + Math.signum(sinY));
                            }
                            if (!isBlockReplaceable((block = getBlock(world, i1, j2, k1))) && !isWood(block) && !isLeaves(block)) {
                                break;
                            }
                            setBlockAndNotifyAdequately(world, i1, j2, k1, woodBlock, woodMeta | 0xC);
                        }
                        growLeafCanopy(world, random, i1, j2, k1);
                    }
                }
                for (j1 = j; j1 < j + height; ++j1) {
                    setBlockAndNotifyAdequately(world, i, j1, k, woodBlock, woodMeta);
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
        }
        return false;
    }

    public void growLeafCanopy(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        int leafStart = j - 1;
        int leafTop = j + 2;
        int maxRange = 3 + random.nextInt(2);
        int[] ranges = {-2, 0, -1, -2};
        for (int j1 = leafStart; j1 <= leafTop; ++j1) {
            int leafRange = maxRange + ranges[j1 - leafStart];
            int leafRangeSq = leafRange * leafRange;
            for (int i1 = i - leafRange; i1 <= i + leafRange; ++i1) {
                for (int k1 = k - leafRange; k1 <= k + leafRange; ++k1) {
                    boolean grow;
                    BlockState block;
                    int i2 = Math.abs(i1 - i);
                    int k2 = Math.abs(k1 - k);
                    int j2 = Math.abs(j1 - j);
                    int dSq = i2 * i2 + k2 * k2;
                    int dCh = i2 + j2 + k2;
                    grow = dSq < leafRangeSq && dCh <= 4;
                    if (i2 == leafRange - 1 || k2 == leafRange - 1) {
                        grow = grow && random.nextInt(4) != 0;
                    }
                    if (!grow || !isBlockReplaceable((block = getBlock(world, i1, j1, k1))) && !isLeaves(block)) {
                        continue;
                    }
                    setBlockAndNotifyAdequately(world, i1, j1, k1, leafBlock, leafMeta);
                }
            }
        }
    }

    public LOTRWorldGenMallorn setMinMaxHeight(int min, int max) {
        minHeight = min;
        maxHeight = max;
        return this;
    }
}
