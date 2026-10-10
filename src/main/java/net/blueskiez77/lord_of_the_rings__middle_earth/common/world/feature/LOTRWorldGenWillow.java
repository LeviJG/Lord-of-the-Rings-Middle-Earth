package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature;

import java.util.ArrayList;
import java.util.Collection;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LOTRWorldGenWillow extends LOTRFeature {
    public LegacyBlock woodBlock = LOTRLegacyBlocks.mod("wood6");
    public int woodMeta = 1;
    public LegacyBlock leafBlock = LOTRLegacyBlocks.mod("leaves6");
    public int leafMeta = 1;
    public int minHeight = 8;
    public int maxHeight = 13;
    public boolean needsWater;

    public LOTRWorldGenWillow(boolean flag) {
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
        if (needsWater && !hasWaterNearby(world, random, i, j, k)) {
            return false;
        }
        onPlantGrow(world, i, j - 1, k);
        Collection<net.minecraft.core.BlockPos> vineGrows = new ArrayList<>();
        int angle = 0;

        while (angle < 360) {
            float angleR = (float) Math.toRadians(angle += 30 + random.nextInt(30));
            float sin = Mth.sin(angleR);
            float cos = Mth.cos(angleR);

            int base = j + height - 3 - random.nextInt(3);
            int length = 2 + random.nextInt(4);
            int i1 = i;
            int j1 = base;
            int k1 = k;

            for (int l = 0; l < length; ++l) {
                j1 = updateJ1(j1, random);
                i1 = updateI1(i1, cos, random);
                k1 = updateK1(k1, sin, random);
                setBlockAndNotifyAdequately(world, i1, j1, k1, woodBlock, woodMeta);
            }

            spawnLeafCluster(world, random, i1, j1, k1);
            vineGrows.add(new net.minecraft.core.BlockPos(i1, j1, k1));
        }

        for (int j1 = 0; j1 < height; ++j1) {
            setBlockAndNotifyAdequately(world, i, j + j1, k, woodBlock, woodMeta);
            if (j1 == height - 1) {
                spawnLeafCluster(world, random, i, j + j1, k);
                vineGrows.add(new net.minecraft.core.BlockPos(i, j + j1, k));
            }
        }

        for (int i1 = i - 1; i1 <= i + 1; ++i1) {
            for (int k1 = k - 1; k1 <= k + 1; ++k1) {
                if (Math.abs(i1 - i) != Math.abs(k1 - k)) {
                    generateRoots(world, random, i1, k1, j);
                }
            }
        }

        for (net.minecraft.core.BlockPos coords : vineGrows) {
            spawnVineCluster(world, random, coords.getX(), coords.getY(), coords.getZ());
        }

        return true;
    }
    private boolean hasWaterNearby(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        int attempts = 4;
        for (int l = 0; l < attempts; ++l) {
            int xOffset = random.nextInt(13) - 6;
            int zOffset = random.nextInt(13) - 6;
            int yOffset = random.nextInt(7) - 4;
            int i1 = i + xOffset;
            int j1 = j + yOffset;
            int k1 = k + zOffset;
            int chunkX = i1 >> 4;
            int chunkZ = k1 >> 4;
            if (world.hasChunk(chunkX, chunkZ)) {
                BlockState block = getBlock(world, i1, j1, k1);
                if (LOTRLegacyBlocks.vanilla("water").matches(block) || LOTRLegacyBlocks.vanilla("flowing_water").matches(block)) {
                    return true;
                }
            }
        }
        return false;
    }
    private int updateJ1(int j1, RandomSource random) {
        if (j1 > 0 && (j1 % 4 == 0 || random.nextInt(3) == 0)) {
            return j1 + 1;
        }
        return j1;
    }

    private int updateI1(int i1, float cos, RandomSource random) {
        if (random.nextFloat() < Math.abs(cos)) {
            return (int) (i1 + Math.signum(cos));
        }
        return i1;
    }

    private int updateK1(int k1, float sin, RandomSource random) {
        if (random.nextFloat() < Math.abs(sin)) {
            return (int) (k1 + Math.signum(sin));
        }
        return k1;
    }

    private void generateRoots(WorldGenLevel world, RandomSource random, int i1, int k1, int j) {
        int rootY = j + 1 + random.nextInt(2);
        while (isBlockReplaceable(getBlock(world, i1, rootY, k1))) {
            setBlockAndNotifyAdequately(world, i1, rootY, k1, woodBlock, woodMeta | 0xC);
            onPlantGrow(world, i1, rootY - 1, k1);
            rootY--;
            random.nextInt(3);
        }
    }

    public void growVines(WorldGenLevel world, RandomSource random, int i, int j, int k, int meta) {
        setBlockAndNotifyAdequately(world, i, j, k, LOTRLegacyBlocks.mod("willowVines"), meta);
        int vines = 0;
        while ((getBlock(world, i, --j, k)).isAir() && vines < 2 + random.nextInt(4)) {
            setBlockAndNotifyAdequately(world, i, j, k, LOTRLegacyBlocks.mod("willowVines"), meta);
            ++vines;
        }
    }

    public LOTRWorldGenWillow setNeedsWater() {
        needsWater = true;
        return this;
    }

    public void spawnLeafCluster(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        int leafRange = 3;
        int leafRangeSq = leafRange * leafRange;
        int leafRangeSqLess = (int) ((leafRange - 0.5) * (leafRange - 0.5));
        for (int i1 = i - leafRange; i1 <= i + leafRange; ++i1) {
            for (int j1 = j - leafRange; j1 <= j + leafRange; ++j1) {
                for (int k1 = k - leafRange; k1 <= k + leafRange; ++k1) {
                    BlockState block;
                    int i2 = i1 - i;
                    int j2 = j1 - j;
                    int k2 = k1 - k;
                    int dist = i2 * i2 + j2 * j2 + k2 * k2;
                    int taxicab = Math.abs(i2) + Math.abs(j2) + Math.abs(k2);
                    if (dist >= leafRangeSqLess && (dist >= leafRangeSq || random.nextInt(3) != 0) || taxicab > 4 || !isBlockReplaceable((block = getBlock(world, i1, j1, k1))) && !isLeaves(block)) {
                        continue;
                    }
                    setBlockAndNotifyAdequately(world, i1, j1, k1, leafBlock, leafMeta);
                }
            }
        }
    }

    public void spawnVineCluster(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        int leafRange = 3;
        int leafRangeSq = leafRange * leafRange;
        for (int i1 = i - leafRange; i1 <= i + leafRange; ++i1) {
            for (int j1 = j - leafRange; j1 <= j + leafRange; ++j1) {
                for (int k1 = k - leafRange; k1 <= k + leafRange; ++k1) {
                    int i2 = i1 - i;
                    int j2 = j1 - j;
                    int k2 = k1 - k;
                    int dist = i2 * i2 + j2 * j2 + k2 * k2;
                    if (dist >= leafRangeSq) {
                        continue;
                    }
                    BlockState block = getBlock(world, i1, j1, k1);
                    if (block.getBlock() != leafBlock.state(leafMeta).getBlock()) {
                        continue;
                    }
                    int vineChance = 2;
                    if (random.nextInt(vineChance) == 0 && (getBlock(world, i1 - 1, j1, k1)).isAir()) {
                        growVines(world, random, i1 - 1, j1, k1, 8);
                    }
                    if (random.nextInt(vineChance) == 0 && (getBlock(world, i1 + 1, j1, k1)).isAir()) {
                        growVines(world, random, i1 + 1, j1, k1, 2);
                    }
                    if (random.nextInt(vineChance) == 0 && (getBlock(world, i1, j1, k1 - 1)).isAir()) {
                        growVines(world, random, i1, j1, k1 - 1, 1);
                    }
                    if (random.nextInt(vineChance) != 0 || !(getBlock(world, i1, j1, k1 + 1)).isAir()) {
                        continue;
                    }
                    growVines(world, random, i1, j1, k1 + 1, 4);
                }
            }
        }
    }
}
