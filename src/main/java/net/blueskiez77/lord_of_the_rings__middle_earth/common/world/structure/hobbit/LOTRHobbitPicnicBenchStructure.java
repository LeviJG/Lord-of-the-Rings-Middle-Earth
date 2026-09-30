package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.hobbit;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRFoods;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRHobbitEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRHobbitPicnicBenchStructure extends LOTRStructureBase {
    public LegacyBlock baseBlock;
    public int baseMeta;
    public LegacyBlock stairBlock;
    public LegacyBlock halfBlock;
    public int halfMeta;
    public LegacyBlock plateBlock;

    public LOTRHobbitPicnicBenchStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generate(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        if (restrictions && !(isBiome(world, i, k, "shire"))) {
            return false;
        }
        int randomWood = random.nextInt(4);
        switch (randomWood) {
            case 0: {
                baseBlock = LOTRLegacyBlocks.vanilla("planks");
                baseMeta = 0;
                stairBlock = LOTRLegacyBlocks.vanilla("oak_stairs");
                halfBlock = LOTRLegacyBlocks.vanilla("wooden_slab");
                halfMeta = 0;
                break;
            }
            case 1: {
                baseBlock = LOTRLegacyBlocks.vanilla("planks");
                baseMeta = 1;
                stairBlock = LOTRLegacyBlocks.vanilla("spruce_stairs");
                halfBlock = LOTRLegacyBlocks.vanilla("wooden_slab");
                halfMeta = 1;
                break;
            }
            case 2: {
                baseBlock = LOTRLegacyBlocks.vanilla("planks");
                baseMeta = 2;
                stairBlock = LOTRLegacyBlocks.vanilla("birch_stairs");
                halfBlock = LOTRLegacyBlocks.vanilla("wooden_slab");
                halfMeta = 2;
                break;
            }
            case 3: {
                baseBlock = LOTRLegacyBlocks.mod("planks");
                baseMeta = 0;
                stairBlock = LOTRLegacyBlocks.mod("stairsShirePine");
                halfBlock = LOTRLegacyBlocks.mod("woodSlabSingle");
                halfMeta = 0;
            }
        }
        plateBlock = random.nextBoolean() ? LOTRLegacyBlocks.mod("woodPlateBlock") : LOTRLegacyBlocks.mod("ceramicPlateBlock");
        int rotation = random.nextInt(4);
        if (!restrictions && usingPlayer != null) {
            rotation = usingPlayerRotation();
        }
        switch (rotation) {
            case 0: {
                return generateFacingSouth(world, random, i, j, k);
            }
            case 1: {
                return generateFacingWest(world, random, i, j, k);
            }
            case 2: {
                return generateFacingNorth(world, random, i, j, k);
            }
            case 3: {
                return generateFacingEast(world, random, i, j, k);
            }
        }
        return false;
    }

    public boolean generateFacingEast(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        int k1;
        int i1;
        if (restrictions) {
            for (i1 = i; i1 <= i + 5; ++i1) {
                for (k1 = k - 2; k1 <= k + 3; ++k1) {
                    if (LOTRLegacyBlocks.vanilla("grass").matches(world.getBlockState(new BlockPos(i1, j - 1, k1))) && world.isEmptyBlock(new BlockPos(i1, j, k1)) && world.isEmptyBlock(new BlockPos(i1, j + 1, k1))) {
                        continue;
                    }
                    return false;
                }
            }
        }
        for (i1 = i; i1 <= i + 5; ++i1) {
            for (k1 = k - 2; k1 <= k + 3; ++k1) {
                setBlockAndNotifyAdequately(world, i1, j, k1, LOTRLegacyBlocks.vanilla("air"), 0);
                setBlockAndNotifyAdequately(world, i1, j + 1, k1, LOTRLegacyBlocks.vanilla("air"), 0);
            }
        }
        for (i1 = i; i1 <= i + 5; ++i1) {
            for (k1 = k; k1 <= k + 1; ++k1) {
                if (i1 == i || i1 == i + 5) {
                    setBlockAndNotifyAdequately(world, i1, j, k1, baseBlock, baseMeta);
                } else {
                    setBlockAndNotifyAdequately(world, i1, j, k1, halfBlock, halfMeta | 8);
                }
                placePlate(world, random, i1, j + 1, k1, plateBlock, LOTRFoods.HOBBIT);
            }
            setBlockAndNotifyAdequately(world, i1, j, k - 2, stairBlock, 3);
            setBlockAndNotifyAdequately(world, i1, j, k + 3, stairBlock, 2);
        }
        int hobbits = 2 + random.nextInt(3);
        for (int i12 = 0; i12 < hobbits; ++i12) {
            LOTRHobbitEntity hobbit = create(LOTREntities.HOBBIT, world);
            int hobbitX = i + random.nextInt(6);
            int hobbitZ = k - 1 + random.nextInt(2) * 3;
            hobbit.snapTo(hobbitX + 0.5, j, hobbitZ + 0.5, 0.0f, 0.0f);
            hobbit.setHomeTo(new BlockPos(hobbitX, j, hobbitZ), 16);
            hobbit.finalizeSpawn(world, world.getCurrentDifficultyAt(hobbit.blockPosition()), EntitySpawnReason.STRUCTURE, null);
            hobbit.isNPCPersistent = true;
            world.addFreshEntity(hobbit);
        }
        return true;
    }

    public boolean generateFacingNorth(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        int k1;
        int i1;
        if (restrictions) {
            for (k1 = k; k1 >= k - 5; --k1) {
                for (i1 = i - 2; i1 <= i + 3; ++i1) {
                    if (LOTRLegacyBlocks.vanilla("grass").matches(world.getBlockState(new BlockPos(i1, j - 1, k1))) && world.isEmptyBlock(new BlockPos(i1, j, k1)) && world.isEmptyBlock(new BlockPos(i1, j + 1, k1))) {
                        continue;
                    }
                    return false;
                }
            }
        }
        for (k1 = k; k1 >= k - 5; --k1) {
            for (i1 = i - 2; i1 <= i + 3; ++i1) {
                setBlockAndNotifyAdequately(world, i1, j, k1, LOTRLegacyBlocks.vanilla("air"), 0);
                setBlockAndNotifyAdequately(world, i1, j + 1, k1, LOTRLegacyBlocks.vanilla("air"), 0);
            }
        }
        for (k1 = k; k1 >= k - 5; --k1) {
            for (i1 = i; i1 <= i + 1; ++i1) {
                if (k1 == k || k1 == k - 5) {
                    setBlockAndNotifyAdequately(world, i1, j, k1, baseBlock, baseMeta);
                } else {
                    setBlockAndNotifyAdequately(world, i1, j, k1, halfBlock, halfMeta | 8);
                }
                placePlate(world, random, i1, j + 1, k1, plateBlock, LOTRFoods.HOBBIT);
            }
            setBlockAndNotifyAdequately(world, i - 2, j, k1, stairBlock, 1);
            setBlockAndNotifyAdequately(world, i + 3, j, k1, stairBlock, 0);
        }
        int hobbits = 2 + random.nextInt(3);
        for (i1 = 0; i1 < hobbits; ++i1) {
            LOTRHobbitEntity hobbit = create(LOTREntities.HOBBIT, world);
            int hobbitX = i - 1 + random.nextInt(2) * 3;
            int hobbitZ = k - random.nextInt(6);
            hobbit.snapTo(hobbitX + 0.5, j, hobbitZ + 0.5, 0.0f, 0.0f);
            hobbit.setHomeTo(new BlockPos(hobbitX, j, hobbitZ), 16);
            hobbit.finalizeSpawn(world, world.getCurrentDifficultyAt(hobbit.blockPosition()), EntitySpawnReason.STRUCTURE, null);
            hobbit.isNPCPersistent = true;
            world.addFreshEntity(hobbit);
        }
        return true;
    }

    public boolean generateFacingSouth(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        int k1;
        int i1;
        if (restrictions) {
            for (k1 = k; k1 <= k + 5; ++k1) {
                for (i1 = i + 2; i1 >= i - 3; --i1) {
                    if (LOTRLegacyBlocks.vanilla("grass").matches(world.getBlockState(new BlockPos(i1, j - 1, k1))) && world.isEmptyBlock(new BlockPos(i1, j, k1)) && world.isEmptyBlock(new BlockPos(i1, j + 1, k1))) {
                        continue;
                    }
                    return false;
                }
            }
        }
        for (k1 = k; k1 <= k + 5; ++k1) {
            for (i1 = i + 2; i1 >= i - 3; --i1) {
                setBlockAndNotifyAdequately(world, i1, j, k1, LOTRLegacyBlocks.vanilla("air"), 0);
                setBlockAndNotifyAdequately(world, i1, j + 1, k1, LOTRLegacyBlocks.vanilla("air"), 0);
            }
        }
        for (k1 = k; k1 <= k + 5; ++k1) {
            for (i1 = i; i1 >= i - 1; --i1) {
                if (k1 == k || k1 == k + 5) {
                    setBlockAndNotifyAdequately(world, i1, j, k1, baseBlock, baseMeta);
                } else {
                    setBlockAndNotifyAdequately(world, i1, j, k1, halfBlock, halfMeta | 8);
                }
                placePlate(world, random, i1, j + 1, k1, plateBlock, LOTRFoods.HOBBIT);
            }
            setBlockAndNotifyAdequately(world, i - 3, j, k1, stairBlock, 1);
            setBlockAndNotifyAdequately(world, i + 2, j, k1, stairBlock, 0);
        }
        int hobbits = 2 + random.nextInt(3);
        for (i1 = 0; i1 < hobbits; ++i1) {
            LOTRHobbitEntity hobbit = create(LOTREntities.HOBBIT, world);
            int hobbitX = i + 1 - random.nextInt(2) * 3;
            int hobbitZ = k + random.nextInt(6);
            hobbit.snapTo(hobbitX + 0.5, j, hobbitZ + 0.5, 0.0f, 0.0f);
            hobbit.setHomeTo(new BlockPos(hobbitX, j, hobbitZ), 16);
            hobbit.finalizeSpawn(world, world.getCurrentDifficultyAt(hobbit.blockPosition()), EntitySpawnReason.STRUCTURE, null);
            hobbit.isNPCPersistent = true;
            world.addFreshEntity(hobbit);
        }
        return true;
    }

    public boolean generateFacingWest(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        int k1;
        int i1;
        if (restrictions) {
            for (i1 = i; i1 >= i - 5; --i1) {
                for (k1 = k + 2; k1 >= k - 3; --k1) {
                    if (LOTRLegacyBlocks.vanilla("grass").matches(world.getBlockState(new BlockPos(i1, j - 1, k1))) && world.isEmptyBlock(new BlockPos(i1, j, k1)) && world.isEmptyBlock(new BlockPos(i1, j + 1, k1))) {
                        continue;
                    }
                    return false;
                }
            }
        }
        for (i1 = i; i1 >= i - 5; --i1) {
            for (k1 = k + 2; k1 >= k - 3; --k1) {
                setBlockAndNotifyAdequately(world, i1, j, k1, LOTRLegacyBlocks.vanilla("air"), 0);
                setBlockAndNotifyAdequately(world, i1, j + 1, k1, LOTRLegacyBlocks.vanilla("air"), 0);
            }
        }
        for (i1 = i; i1 >= i - 5; --i1) {
            for (k1 = k; k1 >= k - 1; --k1) {
                if (i1 == i || i1 == i - 5) {
                    setBlockAndNotifyAdequately(world, i1, j, k1, baseBlock, baseMeta);
                } else {
                    setBlockAndNotifyAdequately(world, i1, j, k1, halfBlock, halfMeta | 8);
                }
                placePlate(world, random, i1, j + 1, k1, plateBlock, LOTRFoods.HOBBIT);
            }
            setBlockAndNotifyAdequately(world, i1, j, k - 3, stairBlock, 3);
            setBlockAndNotifyAdequately(world, i1, j, k + 2, stairBlock, 2);
        }
        int hobbits = 2 + random.nextInt(3);
        for (int i12 = 0; i12 < hobbits; ++i12) {
            LOTRHobbitEntity hobbit = create(LOTREntities.HOBBIT, world);
            int hobbitX = i - random.nextInt(6);
            int hobbitZ = k + 1 - random.nextInt(2) * 3;
            hobbit.snapTo(hobbitX + 0.5, j, hobbitZ + 0.5, 0.0f, 0.0f);
            hobbit.setHomeTo(new BlockPos(hobbitX, j, hobbitZ), 16);
            hobbit.finalizeSpawn(world, world.getCurrentDifficultyAt(hobbit.blockPosition()), EntitySpawnReason.STRUCTURE, null);
            hobbit.isNPCPersistent = true;
            world.addFreshEntity(hobbit);
        }
        return true;
    }
}
