package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.feature;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.elf.LOTRElfHouseStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.elf.LOTRElfLordHouseStructure;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LOTRMallornExtremeStructure extends LOTRStructureBase {
    public static int HEIGHT_MIN = 35;
    public static int HEIGHT_MAX = 70;
    public static int BOUGH_ANGLE_INTERVAL_MIN = 10;
    public static int BOUGH_ANGLE_INTERVAL_MAX = 30;
    public static int BOUGH_LENGTH_MIN = 15;
    public static int BOUGH_LENGTH_MAX = 25;
    public static float BOUGH_THICKNESS_FACTOR = 0.03f;
    public static float BOUGH_BASE_HEIGHT_MIN = 0.9f;
    public static float BOUGH_BASE_HEIGHT_MAX = 1.0f;
    public static int BOUGH_HEIGHT_MIN = 7;
    public static int BOUGH_HEIGHT_MAX = 10;
    public static int BRANCH_LENGTH_MIN = 8;
    public static int BRANCH_LENGTH_MAX = 10;
    public static int BRANCH_HEIGHT_MIN = 6;
    public static int BRANCH_HEIGHT_MAX = 8;
    public static float HOUSE_HEIGHT_MIN = 0.4f;
    public static float HOUSE_HEIGHT_MAX = 0.7f;
    public static float HOUSE_CHANCE = 0.7f;
    public static float HOUSE_ELFLORD_CHANCE = 0.15f;
    public boolean notify;
    public boolean saplingGrowth;

    public LOTRMallornExtremeStructure(boolean flag) {
        super(flag);
        notify = flag;
    }

    @Override
    public boolean generate(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        return generateAndReturnHeight(world, random, i, j, k, false) > 0;
    }

    public int generateAndReturnHeight(WorldGenLevel world, RandomSource random, int i, int j, int k, boolean forceGeneration) {
        int height = Mth.randomBetweenInclusive(random, HEIGHT_MIN, HEIGHT_MAX);
        int trunkWidth = 2;
        boolean flag = true;
        if (j >= world.getMinY() + 1 && j + height + 5 <= world.getMaxY() + 1 || forceGeneration) {
            int i1;
            int k1;
            for (int j1 = j; j1 <= j + 1 + height; ++j1) {
                int range = trunkWidth;
                if (j1 == j) {
                    range = 0;
                }
                if (j1 >= j + 1 + height - 2) {
                    range = trunkWidth + 1;
                }
                for (int i2 = i - range; i2 <= i + range && flag; ++i2) {
                    for (int k2 = k - range; k2 <= k + range && flag; ++k2) {
                        if (j1 >= world.getMinY() && j1 <= world.getMaxY()) {
                            BlockState block = world.getBlockState(new BlockPos(i2, j1, k2));
                            if (forceGeneration || isReplaceable(world, i2, j1, k2) || LOTRLegacyBlocks.mod("quenditeGrass").matches(block)) {
                                continue;
                            }
                            flag = false;
                            continue;
                        }
                        if (forceGeneration) {
                            continue;
                        }
                        flag = false;
                    }
                }
            }
            if (!flag && !forceGeneration) {
                return 0;
            }
            if (!forceGeneration) {
                for (i1 = i - trunkWidth; i1 <= i + trunkWidth; ++i1) {
                    for (k1 = k - trunkWidth; k1 <= k + trunkWidth; ++k1) {
                        BlockState block = world.getBlockState(new BlockPos(i1, j - 1, k1));
                        boolean correctBlock = false;
                        if (saplingGrowth) {
                            if (LOTRLegacyBlocks.mod("quenditeGrass").matches(block)) {
                                correctBlock = true;
                            }
                        } else if (block.is(BlockTags.DIRT)) {
                            correctBlock = true;
                        }
                        if (correctBlock) {
                            continue;
                        }
                        return 0;
                    }
                }
            }
            for (i1 = i - trunkWidth; i1 <= i + trunkWidth; ++i1) {
                for (k1 = k - trunkWidth; k1 <= k + trunkWidth; ++k1) {
                    if (!forceGeneration) {
                        setGrassToDirt(world, i1, j - 1, k1);
                    }
                    for (int j1 = 0; j1 < height; ++j1) {
                        BlockState block = world.getBlockState(new BlockPos(i1, j + j1, k1));
                        if (!block.canBeReplaced() && !block.is(BlockTags.LEAVES)) {
                            continue;
                        }
                        setBlockAndNotifyAdequately(world, i1, j + j1, k1, LOTRLegacyBlocks.mod("wood"), 1);
                    }
                }
            }
            int angle = 0;
            while (angle < 360) {
                float angleR = (float) Math.toRadians(angle += Mth.randomBetweenInclusive(random, BOUGH_ANGLE_INTERVAL_MIN, BOUGH_ANGLE_INTERVAL_MAX));
                float sin = Mth.sin(angleR);
                float cos = Mth.cos(angleR);
                int boughLength = Mth.randomBetweenInclusive(random, BOUGH_LENGTH_MIN, BOUGH_LENGTH_MAX);
                int boughThickness = Math.round(boughLength * BOUGH_THICKNESS_FACTOR);
                int boughBaseHeight = j + Mth.floor(height * Mth.randomBetween(random, BOUGH_BASE_HEIGHT_MIN, BOUGH_BASE_HEIGHT_MAX));
                int boughHeight = Mth.randomBetweenInclusive(random, BOUGH_HEIGHT_MIN, BOUGH_HEIGHT_MAX);
                for (int l = 0; l < boughLength; ++l) {
                    int i12 = i + Math.round(sin * l);
                    int k12 = k + Math.round(cos * l);
                    int j1 = boughBaseHeight + Math.round((float) l / boughLength * boughHeight);
                    int range = boughThickness - Math.round((float) l / boughLength * boughThickness * 0.5f);
                    for (int i2 = i12 - range; i2 <= i12 + range; ++i2) {
                        for (int j2 = j1 - range; j2 <= j1 + range; ++j2) {
                            for (int k2 = k12 - range; k2 <= k12 + range; ++k2) {
                                BlockState block = world.getBlockState(new BlockPos(i2, j2, k2));
                                if (!block.canBeReplaced() && !block.is(BlockTags.LEAVES)) {
                                    continue;
                                }
                                setBlockAndNotifyAdequately(world, i2, j2, k2, LOTRLegacyBlocks.mod("wood"), 13);
                            }
                        }
                    }
                    if (l != boughLength - 1) {
                        continue;
                    }
                    int branches = Mth.randomBetweenInclusive(random, 8, 16);
                    for (int b = 0; b < branches; ++b) {
                        float branch_angle = random.nextFloat() * 2.0f * 3.1415927f;
                        float branch_sin = Mth.sin(branch_angle);
                        float branch_cos = Mth.cos(branch_angle);
                        int branchLength = Mth.randomBetweenInclusive(random, BRANCH_LENGTH_MIN, BRANCH_LENGTH_MAX);
                        int branchHeight = Mth.randomBetweenInclusive(random, BRANCH_HEIGHT_MIN, BRANCH_HEIGHT_MAX);
                        for (int b1 = 0; b1 < branchLength; ++b1) {
                            int i2 = i12 + Math.round(branch_sin * b1);
                            int k2 = k12 + Math.round(branch_cos * b1);
                            int j2 = j1 + Math.round((float) b1 / branchLength * branchHeight);
                            BlockState block = world.getBlockState(new BlockPos(i2, j2, k2));
                            if (block.canBeReplaced() || block.is(BlockTags.LEAVES)) {
                                setBlockAndNotifyAdequately(world, i2, j2, k2, LOTRLegacyBlocks.mod("wood"), 13);
                            }
                            if (b1 != branchLength - 1) {
                                continue;
                            }
                            spawnLeafCluster(world, random, i2, j2, k2, 3);
                        }
                    }
                }
            }
            for (int j1 = j + (int) (height * BOUGH_BASE_HEIGHT_MIN); j1 > j + (int) (height * 0.67f); j1 -= 1 + random.nextInt(3)) {
                int branches = 1 + random.nextInt(5);
                for (int b = 0; b < branches; ++b) {
                    float branchAngle = random.nextFloat() * 3.1415927f * 2.0f;
                    int i13 = i + (int) (1.5f + Mth.cos(branchAngle) * 4.0f);
                    int k13 = k + (int) (1.5f + Mth.sin(branchAngle) * 4.0f);
                    int j2 = j1;
                    int length = Mth.randomBetweenInclusive(random, 10, 20);
                    for (int l = 0; l < length && isReplaceable(world, i13 = i + (int) (1.5f + Mth.cos(branchAngle) * l), j2 = j1 - 3 + l / 2, k13 = k + (int) (1.5f + Mth.sin(branchAngle) * l)); ++l) {
                        setBlockAndNotifyAdequately(world, i13, j2, k13, LOTRLegacyBlocks.mod("wood"), 13);
                    }
                    spawnLeafLayer(world, random, i13, j2 + 1, k13, 2);
                    spawnLeafLayer(world, random, i13, j2, k13, 3);
                    spawnLeafLayer(world, random, i13, j2 - 1, k13, 1);
                }
            }
            int roots = Mth.randomBetweenInclusive(random, 6, 10);
            for (int l = 0; l < roots; ++l) {
                int i14 = i;
                int j1 = j + 1 + random.nextInt(5);
                int k14 = k;
                int xDirection = 0;
                int zDirection = 0;
                int rootLength = 1 + random.nextInt(4);
                if (random.nextBoolean()) {
                    if (random.nextBoolean()) {
                        i14 -= trunkWidth + 1;
                        xDirection = -1;
                    } else {
                        i14 += trunkWidth + 1;
                        xDirection = 1;
                    }
                    k14 -= trunkWidth + 1;
                    k14 += random.nextInt(trunkWidth * 2 + 2);
                } else {
                    if (random.nextBoolean()) {
                        k14 -= trunkWidth + 1;
                        zDirection = -1;
                    } else {
                        k14 += trunkWidth + 1;
                        zDirection = 1;
                    }
                    i14 -= trunkWidth + 1;
                    i14 += random.nextInt(trunkWidth * 2 + 2);
                }
                for (int l1 = 0; l1 < rootLength; ++l1) {
                    int rootBlocks = 0;
                    int j2 = j1;
                    while (!isOpaqueAt(world, i14, j2, k14)) {
                        setBlockAndNotifyAdequately(world, i14, j2, k14, LOTRLegacyBlocks.mod("wood"), 13);
                        setGrassToDirt(world, i14, j2 - 1, k14);
                        rootBlocks++;
                        if (rootBlocks > 5) {
                            break;
                        }
                        --j2;
                    }
                    --j1;
                    if (!random.nextBoolean()) {
                        continue;
                    }
                    if (xDirection == -1) {
                        --i14;
                        continue;
                    }
                    if (xDirection == 1) {
                        ++i14;
                        continue;
                    }
                    if (zDirection == -1) {
                        --k14;
                        continue;
                    }
                    ++k14;
                }
            }
            if (!saplingGrowth && !notify && !forceGeneration && random.nextFloat() < HOUSE_CHANCE) {
                int houseHeight = Mth.floor(height * Mth.randomBetween(random, HOUSE_HEIGHT_MIN, HOUSE_HEIGHT_MAX));
                boolean isElfLordTree = random.nextFloat() < HOUSE_ELFLORD_CHANCE;
                boolean spawnedElfLord = false;
                if (isElfLordTree) {
                    LOTRElfLordHouseStructure house = new LOTRElfLordHouseStructure(true);
                    house.restrictions = false;
                    spawnedElfLord = house.generate(world, random, i, j + houseHeight, k);
                }
                if (!isElfLordTree || !spawnedElfLord) {
                    LOTRElfHouseStructure house = new LOTRElfHouseStructure(true);
                    house.restrictions = false;
                    house.generate(world, random, i, j + houseHeight, k);
                }
            }
            return height;
        }
        return 0;
    }

    public LOTRMallornExtremeStructure setSaplingGrowth() {
        saplingGrowth = true;
        return this;
    }

    public void spawnLeafCluster(WorldGenLevel world, RandomSource random, int i, int j, int k, int leafRange) {
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
                    if (dist >= leafRangeSqLess && (dist >= leafRangeSq || random.nextInt(3) != 0) || !(block = world.getBlockState(new BlockPos(i1, j1, k1))).canBeReplaced() && !block.is(BlockTags.LEAVES)) {
                        continue;
                    }
                    setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRLegacyBlocks.mod("leaves"), 1);
                }
            }
        }
    }

    public void spawnLeafLayer(WorldGenLevel world, RandomSource random, int i, int j, int k, int leafRange) {
        int leafRangeSq = leafRange * leafRange;
        for (int i1 = i - leafRange; i1 <= i + leafRange; ++i1) {
            for (int k1 = k - leafRange; k1 <= k + leafRange; ++k1) {
                BlockState block;
                int i2 = i1 - i;
                int k2 = k1 - k;
                int dist = i2 * i2 + k2 * k2;
                if (dist > leafRangeSq || !(block = world.getBlockState(new BlockPos(i1, j, k1))).canBeReplaced() && !block.is(BlockTags.LEAVES)) {
                    continue;
                }
                setBlockAndNotifyAdequately(world, i1, j, k1, LOTRLegacyBlocks.mod("leaves"), 1);
            }
        }
    }
}
