package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.dunland;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

public class LOTRDunlendingCampfireStructure extends LOTRStructureBase {
    public LOTRDunlendingCampfireStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generate(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        int j1;
        int k1;
        int k12;
        int i1;
        if (restrictions && !LOTRLegacyBlocks.vanilla("grass").matches(world.getBlockState(new BlockPos(i, j - 1, k)))) {
            return false;
        }
        --j;
        int rotation = random.nextInt(4);
        if (!restrictions && usingPlayer != null) {
            rotation = usingPlayerRotation();
        }
        switch (rotation) {
            case 0: {
                k += 5;
                break;
            }
            case 1: {
                i -= 5;
                break;
            }
            case 2: {
                k -= 5;
                break;
            }
            case 3: {
                i += 5;
            }
        }
        if (restrictions) {
            for (i1 = i - 5; i1 <= i + 5; ++i1) {
                for (k12 = k - 5; k12 <= k + 5; ++k12) {
                    for (j1 = j + 1; j1 <= j + 2; ++j1) {
                        if (!isOpaqueAt(world, i1, j1, k12)) {
                            continue;
                        }
                        return false;
                    }
                    j1 = world.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, i1, k12) - 1;
                    if (Math.abs(j1 - j) > 2) {
                        return false;
                    }
                    BlockState l = world.getBlockState(new BlockPos(i1, j1, k12));
                    if (LOTRLegacyBlocks.vanilla("grass").matches(l)) {
                        continue;
                    }
                    return false;
                }
            }
        }
        for (i1 = i - 5; i1 <= i + 5; ++i1) {
            for (k12 = k - 5; k12 <= k + 5; ++k12) {
                if (!restrictions) {
                    for (j1 = j + 1; j1 <= j + 2; ++j1) {
                        setBlockAndNotifyAdequately(world, i1, j1, k12, LOTRLegacyBlocks.vanilla("air"), 0);
                    }
                }
                for (j1 = j; j1 >= j - 2; --j1) {
                    if (isOpaqueAt(world, i1, j1 + 1, k12)) {
                        setBlockAndNotifyAdequately(world, i1, j1, k12, LOTRLegacyBlocks.vanilla("dirt"), 0);
                    } else {
                        setBlockAndNotifyAdequately(world, i1, j1, k12, LOTRLegacyBlocks.vanilla("grass"), 0);
                    }
                    setGrassToDirt(world, i1, j1 - 1, k12);
                }
            }
        }
        for (i1 = i - 1; i1 <= i + 1; ++i1) {
            for (k12 = k - 1; k12 <= k + 1; ++k12) {
                setBlockAndNotifyAdequately(world, i1, j, k12, LOTRLegacyBlocks.vanilla("gravel"), 0);
            }
        }
        setBlockAndNotifyAdequately(world, i, j, k, LOTRLegacyBlocks.mod("hearth"), 0);
        setBlockAndNotifyAdequately(world, i, j + 1, k, LOTRLegacyBlocks.vanilla("fire"), 0);
        placeSkullPillar(world, random, i - 2, j + 1, k - 2);
        placeSkullPillar(world, random, i + 2, j + 1, k - 2);
        placeSkullPillar(world, random, i - 2, j + 1, k + 2);
        placeSkullPillar(world, random, i + 2, j + 1, k + 2);
        if (random.nextBoolean()) {
            for (i1 = i - 2; i1 <= i + 2; ++i1) {
                setBlockAndNotifyAdequately(world, i1, j + 1, k + 4, LOTRLegacyBlocks.vanilla("log"), 4);
                setGrassToDirt(world, i1, j, k - 4);
            }
        }
        if (random.nextBoolean()) {
            for (k1 = k - 2; k1 <= k + 2; ++k1) {
                setBlockAndNotifyAdequately(world, i - 4, j + 1, k1, LOTRLegacyBlocks.vanilla("log"), 8);
                setGrassToDirt(world, i - 4, j, k1);
            }
        }
        if (random.nextBoolean()) {
            for (i1 = i - 2; i1 <= i + 2; ++i1) {
                setBlockAndNotifyAdequately(world, i1, j + 1, k - 4, LOTRLegacyBlocks.vanilla("log"), 4);
                setGrassToDirt(world, i1, j, k - 4);
            }
        }
        if (random.nextBoolean()) {
            for (k1 = k - 2; k1 <= k + 2; ++k1) {
                setBlockAndNotifyAdequately(world, i + 4, j + 1, k1, LOTRLegacyBlocks.vanilla("log"), 8);
                setGrassToDirt(world, i + 4, j, k1);
            }
        }
        if (random.nextBoolean()) {
            int chestX = i;
            int chestZ = k;
            int chestMeta = 0;
            int l = random.nextInt(4);
            switch (l) {
                case 0: {
                    chestX = i - 3 + random.nextInt(6);
                    chestZ = k + 3;
                    chestMeta = 3;
                    break;
                }
                case 1: {
                    chestX = i - 3;
                    chestZ = k - 3 + random.nextInt(6);
                    chestMeta = 4;
                    break;
                }
                case 2: {
                    chestX = i - 3 + random.nextInt(6);
                    chestZ = k - 3;
                    chestMeta = 2;
                    break;
                }
                case 3: {
                    chestX = i + 3;
                    chestZ = k - 3 + random.nextInt(6);
                    chestMeta = 5;
                }
            }
            setBlockAndNotifyAdequately(world, chestX, j + 1, chestZ, LOTRLegacyBlocks.mod("chestBasket"), chestMeta);
            LOTRChestContents.fillChest(world, random, new BlockPos(chestX, j + 1, chestZ), LOTRChestContents.DUNLENDING_CAMPFIRE, -1);
        }
        return true;
    }

    public void placeSkullPillar(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        setBlockAndNotifyAdequately(world, i, j, k, LOTRLegacyBlocks.vanilla("cobblestone_wall"), 0);
        placeSkull(world, random, i, j + 1, k);
    }
}
