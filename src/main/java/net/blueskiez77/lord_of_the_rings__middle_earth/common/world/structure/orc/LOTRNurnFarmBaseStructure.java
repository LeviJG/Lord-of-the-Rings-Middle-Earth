package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.orc;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRNPCRespawnerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.mordor.LOTRNurnSlaveEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

public abstract class LOTRNurnFarmBaseStructure extends LOTRStructureBase {
    protected LOTRNurnFarmBaseStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generate(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        int j1;
        int k1;
        int i1;
        if (restrictions && (!LOTRLegacyBlocks.vanilla("grass").matches(world.getBlockState(new BlockPos(i, j - 1, k))) || !(isBiome(world, i, k, "nurn")))) {
            return false;
        }
        --j;
        int rotation = random.nextInt(4);
        if (!restrictions && usingPlayer != null) {
            rotation = usingPlayerRotation();
        }
        switch (rotation) {
            case 0: {
                k += 8;
                break;
            }
            case 1: {
                i -= 8;
                break;
            }
            case 2: {
                k -= 8;
                break;
            }
            case 3: {
                i += 8;
            }
        }
        if (restrictions) {
            for (i1 = i - 8; i1 <= i + 8; ++i1) {
                for (k1 = k - 8; k1 <= k + 8; ++k1) {
                    j1 = world.getHeight(Heightmap.Types.WORLD_SURFACE, i1, k1) - 1;
                    if (Math.abs(j1 - j) > 4) {
                        return false;
                    }
                    BlockState l = world.getBlockState(new BlockPos(i1, j1, k1));
                    if (LOTRLegacyBlocks.vanilla("grass").matches(l)) {
                        continue;
                    }
                    return false;
                }
            }
        }
        for (i1 = i - 7; i1 <= i + 7; ++i1) {
            for (k1 = k - 7; k1 <= k + 7; ++k1) {
                for (j1 = j + 1; j1 <= j + 4; ++j1) {
                    setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("air"), 0);
                }
                for (j1 = j; (j1 == j || !isOpaqueAt(world, i1, j1, k1)) && j1 >= 0; --j1) {
                    setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRLegacyBlocks.mod("brick"), 0);
                    setGrassToDirt(world, i1, j1 - 1, k1);
                }
                if (Math.abs(i1 - i) == 7 || Math.abs(k1 - k) == 7) {
                    setBlockAndNotifyAdequately(world, i1, j + 1, k1, LOTRLegacyBlocks.mod("brick"), 0);
                    setBlockAndNotifyAdequately(world, i1, j + 2, k1, LOTRLegacyBlocks.mod("wall"), 1);
                } else {
                    setBlockAndNotifyAdequately(world, i1, j + 1, k1, LOTRLegacyBlocks.mod("slabSingle"), 1);
                }
                if (Math.abs(i1 - i) != 7 || Math.abs(k1 - k) != 7) {
                    continue;
                }
                placeOrcTorch(world, i1, j + 3, k1);
            }
        }
        switch (rotation) {
            case 0:
                setBlockAndNotifyAdequately(world, i, j + 1, k - 7, LOTRLegacyBlocks.mod("slabSingle"), 1);
                setBlockAndNotifyAdequately(world, i, j + 2, k - 7, LOTRLegacyBlocks.vanilla("air"), 0);
                setBlockAndNotifyAdequately(world, i - 1, j + 3, k - 7, LOTRLegacyBlocks.mod("wall"), 1);
                setBlockAndNotifyAdequately(world, i + 1, j + 3, k - 7, LOTRLegacyBlocks.mod("wall"), 1);
                setBlockAndNotifyAdequately(world, i - 1, j + 4, k - 7, LOTRLegacyBlocks.mod("wall"), 1);
                setBlockAndNotifyAdequately(world, i, j + 4, k - 7, LOTRLegacyBlocks.mod("wall"), 1);
                setBlockAndNotifyAdequately(world, i + 1, j + 4, k - 7, LOTRLegacyBlocks.mod("wall"), 1);
                break;
            case 1:
                setBlockAndNotifyAdequately(world, i + 7, j + 1, k, LOTRLegacyBlocks.mod("slabSingle"), 1);
                setBlockAndNotifyAdequately(world, i + 7, j + 2, k, LOTRLegacyBlocks.vanilla("air"), 0);
                setBlockAndNotifyAdequately(world, i + 7, j + 3, k - 1, LOTRLegacyBlocks.mod("wall"), 1);
                setBlockAndNotifyAdequately(world, i + 7, j + 3, k + 1, LOTRLegacyBlocks.mod("wall"), 1);
                setBlockAndNotifyAdequately(world, i + 7, j + 4, k - 1, LOTRLegacyBlocks.mod("wall"), 1);
                setBlockAndNotifyAdequately(world, i + 7, j + 4, k, LOTRLegacyBlocks.mod("wall"), 1);
                setBlockAndNotifyAdequately(world, i + 7, j + 4, k + 1, LOTRLegacyBlocks.mod("wall"), 1);
                break;
            case 2:
                setBlockAndNotifyAdequately(world, i, j + 1, k + 7, LOTRLegacyBlocks.mod("slabSingle"), 1);
                setBlockAndNotifyAdequately(world, i, j + 2, k + 7, LOTRLegacyBlocks.vanilla("air"), 0);
                setBlockAndNotifyAdequately(world, i - 1, j + 3, k + 7, LOTRLegacyBlocks.mod("wall"), 1);
                setBlockAndNotifyAdequately(world, i + 1, j + 3, k + 7, LOTRLegacyBlocks.mod("wall"), 1);
                setBlockAndNotifyAdequately(world, i - 1, j + 4, k + 7, LOTRLegacyBlocks.mod("wall"), 1);
                setBlockAndNotifyAdequately(world, i, j + 4, k + 7, LOTRLegacyBlocks.mod("wall"), 1);
                setBlockAndNotifyAdequately(world, i + 1, j + 4, k + 7, LOTRLegacyBlocks.mod("wall"), 1);
                break;
            case 3:
                setBlockAndNotifyAdequately(world, i - 7, j + 1, k, LOTRLegacyBlocks.mod("slabSingle"), 1);
                setBlockAndNotifyAdequately(world, i - 7, j + 2, k, LOTRLegacyBlocks.vanilla("air"), 0);
                setBlockAndNotifyAdequately(world, i - 7, j + 3, k - 1, LOTRLegacyBlocks.mod("wall"), 1);
                setBlockAndNotifyAdequately(world, i - 7, j + 3, k + 1, LOTRLegacyBlocks.mod("wall"), 1);
                setBlockAndNotifyAdequately(world, i - 7, j + 4, k - 1, LOTRLegacyBlocks.mod("wall"), 1);
                setBlockAndNotifyAdequately(world, i - 7, j + 4, k, LOTRLegacyBlocks.mod("wall"), 1);
                setBlockAndNotifyAdequately(world, i - 7, j + 4, k + 1, LOTRLegacyBlocks.mod("wall"), 1);
                break;
            default:
                break;
        }
        generateCrops(world, random, i, j, k);
        int slaves = 2 + random.nextInt(4);
        for (int l = 0; l < slaves; ++l) {
            LOTRNurnSlaveEntity slave = create(LOTREntities.NURN_SLAVE, world);
            slave.snapTo(i + 0.5, j + 2, k + 0.5, world.getRandom().nextFloat() * 360.0f, 0.0f);
            slave.finalizeSpawn(world, world.getCurrentDifficultyAt(slave.blockPosition()), EntitySpawnReason.STRUCTURE, null);
            slave.setHomeTo(new BlockPos(i, j, k), 8);
            slave.isNPCPersistent = true;
            world.addFreshEntity(slave);
        }
        LOTRNPCRespawnerEntity respawner = create(LOTREntities.NPC_RESPAWNER, world);
        respawner.setSpawnClass(LOTREntities.NURN_SLAVE);
        respawner.setCheckRanges(12, -8, 8, 8);
        respawner.setSpawnRanges(6, -2, 2, 8);
        placeNPCRespawner(respawner, world, i, j, k);
        return true;
    }

    public abstract void generateCrops(WorldGenLevel var1, RandomSource var2, int var3, int var4, int var5);
}
