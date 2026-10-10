package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LOTRWorldGenDragonblood extends LOTRFeature {
    public int minHeight;
    public int maxHeight;
    public int trunkWidth;
    public LegacyBlock woodBlock = LOTRLegacyBlocks.mod("wood9");
    public int woodMeta;
    public LegacyBlock leafBlock = LOTRLegacyBlocks.mod("leaves9");
    public int leafMeta;

    public LOTRWorldGenDragonblood(boolean flag, int i, int j, int k) {
        super(flag);
        minHeight = i;
        maxHeight = j;
        trunkWidth = k;
    }

    @Override
    protected boolean generateFeature(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        int height = LOTRWorldGenUtil.getRandomIntegerInRange(random, minHeight, maxHeight);
        boolean flag = true;
        if (j >= 1 && j + height + 5 <= 256) {
            int k1;
            int i1;
            int j1;
            for (j1 = j; j1 <= j + height + 5; ++j1) {
                int range = trunkWidth + 1;
                if (j1 == j) {
                    range = trunkWidth;
                }
                if (j1 >= j + height + 2) {
                    range = trunkWidth + 2;
                }
                for (int i12 = i - range; i12 <= i + range && flag; ++i12) {
                    for (int k12 = k - range; k12 <= k + range && flag; ++k12) {
                        if (j1 >= 0 && j1 < 256 && isReplaceable(world, i12, j1, k12)) {
                            continue;
                        }
                        flag = false;
                    }
                }
            }
            for (i1 = i - trunkWidth; i1 <= i + trunkWidth && flag; ++i1) {
                for (k1 = k - trunkWidth; k1 <= k + trunkWidth && flag; ++k1) {
                    BlockState block = getBlock(world, i1, j - 1, k1);
                    boolean isSoil = canSustainPlant(world, i1, j - 1, k1);
                    if (isSoil) {
                        continue;
                    }
                    flag = false;
                }
            }
            if (!flag) {
                return false;
            }
            for (i1 = i - trunkWidth; i1 <= i + trunkWidth; ++i1) {
                for (k1 = k - trunkWidth; k1 <= k + trunkWidth; ++k1) {
                    onPlantGrow(world, i1, j - 1, k1);
                }
            }
            for (j1 = 0; j1 < height; ++j1) {
                for (int i13 = i - trunkWidth; i13 <= i + trunkWidth; ++i13) {
                    for (int k13 = k - trunkWidth; k13 <= k + trunkWidth; ++k13) {
                        setBlockAndNotifyAdequately(world, i13, j + j1, k13, woodBlock, woodMeta);
                    }
                }
            }
            if (trunkWidth >= 1) {
                int deg = 0;
                while (deg < 360) {
                    float angle = (float) Math.toRadians(deg += (40 + random.nextInt(30)) / trunkWidth);
                    float cos = Mth.cos(angle);
                    float sin = Mth.sin(angle);
                    float angleY = random.nextFloat() * 0.6981317007977318f;
                    float sinY = Mth.sin(angleY);
                    int length = 3 + random.nextInt(6);
                    int i14 = i;
                    int k14 = k;
                    int j12 = j + height - 1 - random.nextInt(5);
                    for (int l = 0; l < (length *= 1 + random.nextInt(trunkWidth)); ++l) {
                        BlockState block;
                        if (Math.floor(cos * l) != Math.floor(cos * (l - 1))) {
                            i14 = (int) (i14 + Math.signum(cos));
                        }
                        if (Math.floor(sin * l) != Math.floor(sin * (l - 1))) {
                            k14 = (int) (k14 + Math.signum(sin));
                        }
                        if (Math.floor(sinY * l) != Math.floor(sinY * (l - 1))) {
                            j12 = (int) (j12 + Math.signum(sinY));
                        }
                        if (!isBlockReplaceable((block = getBlock(world, i14, j12, k14))) && !isWood(block) && !isLeaves(block)) {
                            break;
                        }
                        setBlockAndNotifyAdequately(world, i14, j12, k14, woodBlock, woodMeta | 0xC);
                    }
                    growLeafCanopy(world, random, i14, j12, k14);
                }
            } else {
                growLeafCanopy(world, random, i, j + height - 1, k);
            }
            for (i1 = i - 1 - trunkWidth; i1 <= i + 1 + trunkWidth; ++i1) {
                for (int k15 = k - 1 - trunkWidth; k15 <= k + 1 + trunkWidth; ++k15) {
                    int i2 = i1 - i;
                    int k2 = k15 - k;
                    if (Math.abs(i2) == Math.abs(k2)) {
                        continue;
                    }
                    int rootY = j + random.nextInt(2 + trunkWidth);
                    int roots = 0;
                    while (isBlockReplaceable(getBlock(world, i1, rootY, k15))) {
                        setBlockAndNotifyAdequately(world, i1, rootY, k15, woodBlock, woodMeta | 0xC);
                        onPlantGrow(world, i1, rootY - 1, k15);
                        --rootY;
                        roots++;
                        random.nextInt(3);
                    }
                }
            }
            return true;
        }
        return false;
    }

    public void growLeafCanopy(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        int j1;
        int leafStart = j + 2;
        int leafTop = j + 4;
        int maxRange = 3;
        for (j1 = leafStart; j1 <= leafTop; ++j1) {
            int j2 = j1 - (leafTop + 1);
            int leafRange = maxRange - j2;
            int leafRangeSq = leafRange * leafRange;
            for (int i1 = i - leafRange; i1 <= i + leafRange; ++i1) {
                for (int k1 = k - leafRange; k1 <= k + leafRange; ++k1) {
                    int k2;
                    boolean grow;
                    int i2 = Math.abs(i1 - i);
                    int dist = i2 * i2 + (k2 = Math.abs(k1 - k)) * k2;
                    grow = dist < leafRangeSq;
                    if (i2 == leafRange - 1 || k2 == leafRange - 1) {
                        grow = grow && random.nextInt(4) > 0;
                    }
                    if (!grow) {
                        continue;
                    }
                    int below = 0;
                    for (int j3 = j1; j3 >= j1 - below; --j3) {
                        BlockState block = getBlock(world, i1, j3, k1);
                        if (!isBlockReplaceable(block) && !isLeaves(block)) {
                            continue;
                        }
                        setBlockAndNotifyAdequately(world, i1, j3, k1, leafBlock, leafMeta);
                    }
                }
            }
        }
        for (j1 = j; j1 <= j + 2; ++j1) {
            for (int i1 = i - maxRange; i1 <= i + maxRange; ++i1) {
                for (int k1 = k - maxRange; k1 <= k + maxRange; ++k1) {
                    BlockState block;
                    int i2 = Math.abs(i1 - i);
                    int k2 = Math.abs(k1 - k);
                    int j2 = j1 - j;
                    if ((i2 != 0 || k2 != 0) && (i2 != k2 || i2 != j2) && (i2 != 0 && k2 != 0 || i2 != j2 + 1 && k2 != j2 + 1) || !isBlockReplaceable((block = getBlock(world, i1, j1, k1))) && !isLeaves(block)) {
                        continue;
                    }
                    setBlockAndNotifyAdequately(world, i1, j1, k1, woodBlock, woodMeta | 0xC);
                }
            }
        }
    }
}
