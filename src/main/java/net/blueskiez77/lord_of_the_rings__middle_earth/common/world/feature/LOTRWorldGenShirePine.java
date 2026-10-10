package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LOTRWorldGenShirePine extends LOTRFeature {
    public LegacyBlock woodBlock = LOTRLegacyBlocks.mod("wood");
    public int woodMeta;
    public LegacyBlock leafBlock = LOTRLegacyBlocks.mod("leaves");
    public int leafMeta;
    public int minHeight = 10;
    public int maxHeight = 20;

    public LOTRWorldGenShirePine(boolean flag) {
        super(flag);
    }

    @Override
    protected boolean generateFeature(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        int height = LOTRWorldGenUtil.getRandomIntegerInRange(random, minHeight, maxHeight);
        int leafHeight = 6 + random.nextInt(4);
        int minLeafHeight = j + height - leafHeight;
        int maxLeafWidth = 2 + random.nextInt(2);
        boolean flag = true;
        if (j >= 1 && j + height + 1 <= 256) {
            for (int j1 = j; j1 <= j + 1 + height && flag; ++j1) {
                int checkRange;
                checkRange = j1 < minLeafHeight ? 0 : maxLeafWidth;
                for (int i1 = i - checkRange; i1 <= i + checkRange && flag; ++i1) {
                    for (int k1 = k - checkRange; k1 <= k + checkRange && flag; ++k1) {
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
            if (canSustainPlant(world, i, j - 1, k)) {
                onPlantGrow(world, i, j - 1, k);
                int leafWidth = random.nextInt(2);
                int leafWidthLimit = 1;
                int nextLeafWidth = 0;
                for (int j1 = j + height; j1 >= minLeafHeight; --j1) {
                    for (int i1 = i - leafWidth; i1 <= i + leafWidth; ++i1) {
                        for (int k1 = k - leafWidth; k1 <= k + leafWidth; ++k1) {
                            BlockState block;
                            int i2 = i1 - i;
                            int k2 = k1 - k;
                            if (leafWidth > 0 && Math.abs(i2) == leafWidth && Math.abs(k2) == leafWidth || !isBlockReplaceable((block = getBlock(world, i1, j1, k1))) && !isLeaves(block)) {
                                continue;
                            }
                            setBlockAndNotifyAdequately(world, i1, j1, k1, leafBlock, leafMeta);
                        }
                    }
                    if (leafWidth >= leafWidthLimit) {
                        leafWidth = nextLeafWidth;
                        nextLeafWidth = 1;
                        leafWidthLimit++;
                        if (leafWidthLimit <= maxLeafWidth) {
                            continue;
                        }
                        leafWidthLimit = maxLeafWidth;
                        continue;
                    }
                    ++leafWidth;
                }
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
        }
        return false;
    }
}
