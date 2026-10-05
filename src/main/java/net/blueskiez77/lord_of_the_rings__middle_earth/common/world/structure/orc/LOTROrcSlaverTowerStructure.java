package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.orc;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.mordor.LOTRMordorOrcEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.mordor.LOTRMordorOrcSlaverEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

public class LOTROrcSlaverTowerStructure extends LOTRStructureBase {
    public LOTROrcSlaverTowerStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generate(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        int j1;
        int j12;
        int k1;
        int i1;
        if (restrictions && (!LOTRLegacyBlocks.vanilla("grass").matches(world.getBlockState(new BlockPos(i, j - 1, k))) || !(isBiome(world, i, k, "nurn")))) {
            return false;
        }
        int height = 5 + random.nextInt(4);
        j += height;
        int rotation = random.nextInt(4);
        if (!restrictions && usingPlayer != null) {
            rotation = usingPlayerRotation();
        }
        switch (rotation) {
            case 0: {
                ++k;
                break;
            }
            case 1: {
                --i;
                break;
            }
            case 2: {
                --k;
                break;
            }
            case 3: {
                ++i;
            }
        }
        if (restrictions) {
            for (i1 = i - 3; i1 <= i + 3; ++i1) {
                for (k1 = k - 3; k1 <= k + 3; ++k1) {
                    j1 = world.getHeight(Heightmap.Types.WORLD_SURFACE, i1, k1) - 1;
                    BlockState l = world.getBlockState(new BlockPos(i1, j1, k1));
                    if (LOTRLegacyBlocks.vanilla("grass").matches(l)) {
                        continue;
                    }
                    return false;
                }
            }
        }
        for (i1 = i - 3; i1 <= i + 3; ++i1) {
            for (k1 = k - 3; k1 <= k + 3; ++k1) {
                setBlockAndNotifyAdequately(world, i1, j, k1, LOTRLegacyBlocks.mod("planks"), 3);
                setBlockAndNotifyAdequately(world, i1, j + 6, k1, LOTRLegacyBlocks.mod("planks"), 3);
                if (Math.abs(i1 - i) != 3 && Math.abs(k1 - k) != 3) {
                    continue;
                }
                setBlockAndNotifyAdequately(world, i1, j + 1, k1, LOTRLegacyBlocks.mod("fence"), 3);
                setBlockAndNotifyAdequately(world, i1, j + 5, k1, LOTRLegacyBlocks.mod("fence"), 3);
                setBlockAndNotifyAdequately(world, i1, j + 7, k1, LOTRLegacyBlocks.mod("fence"), 3);
            }
        }
        for (i1 = i - 3; i1 <= i + 3; i1 += 6) {
            for (k1 = k - 3; k1 <= k + 3; k1 += 6) {
                for (j1 = j + 5; (j1 >= j || !isOpaqueAt(world, i1, j1, k1)) && j1 >= world.getMinY(); --j1) {
                    setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRLegacyBlocks.mod("wood"), 3);
                    setGrassToDirt(world, i1, j1 - 1, k1);
                }
            }
        }
        for (j12 = j + 2; j12 <= j + 4; ++j12) {
            setBlockAndNotifyAdequately(world, i - 2, j12, k - 3, LOTRLegacyBlocks.mod("fence"), 3);
            setBlockAndNotifyAdequately(world, i - 2, j12, k + 3, LOTRLegacyBlocks.mod("fence"), 3);
            setBlockAndNotifyAdequately(world, i + 2, j12, k - 3, LOTRLegacyBlocks.mod("fence"), 3);
            setBlockAndNotifyAdequately(world, i + 2, j12, k + 3, LOTRLegacyBlocks.mod("fence"), 3);
            setBlockAndNotifyAdequately(world, i - 3, j12, k - 2, LOTRLegacyBlocks.mod("fence"), 3);
            setBlockAndNotifyAdequately(world, i + 3, j12, k - 2, LOTRLegacyBlocks.mod("fence"), 3);
            setBlockAndNotifyAdequately(world, i - 3, j12, k + 2, LOTRLegacyBlocks.mod("fence"), 3);
            setBlockAndNotifyAdequately(world, i + 3, j12, k + 2, LOTRLegacyBlocks.mod("fence"), 3);
        }
        for (j12 = j + 11; (j12 >= j || !isOpaqueAt(world, i, j12, k)) && j12 >= world.getMinY(); --j12) {
            setBlockAndNotifyAdequately(world, i, j12, k, LOTRLegacyBlocks.mod("wood"), 3);
            setGrassToDirt(world, i, j12 - 1, k);
            if (j12 > j + 6) {
                continue;
            }
            setBlockAndNotifyAdequately(world, i, j12, k - 1, LOTRLegacyBlocks.vanilla("ladder"), 2);
        }
        setBlockAndNotifyAdequately(world, i, j + 1, k - 1, LOTRLegacyBlocks.vanilla("trapdoor"), 0);
        setBlockAndNotifyAdequately(world, i, j + 7, k - 1, LOTRLegacyBlocks.vanilla("trapdoor"), 0);
        placeOrcTorch(world, i - 3, j + 8, k - 3);
        placeOrcTorch(world, i - 3, j + 8, k + 3);
        placeOrcTorch(world, i + 3, j + 8, k - 3);
        placeOrcTorch(world, i + 3, j + 8, k + 3);
        setBlockAndNotifyAdequately(world, i, j + 12, k, LOTRLegacyBlocks.mod("fence"), 3);
        setBlockAndNotifyAdequately(world, i, j + 13, k, LOTRLegacyBlocks.mod("fence"), 3);
        setBlockAndNotifyAdequately(world, i, j + 12, k - 1, LOTRLegacyBlocks.mod("fence"), 3);
        setBlockAndNotifyAdequately(world, i, j + 12, k + 1, LOTRLegacyBlocks.mod("fence"), 3);
        setBlockAndNotifyAdequately(world, i - 1, j + 12, k, LOTRLegacyBlocks.mod("fence"), 3);
        setBlockAndNotifyAdequately(world, i + 1, j + 12, k, LOTRLegacyBlocks.mod("fence"), 3);
        placeOrcTorch(world, i, j + 14, k);
        placeOrcTorch(world, i, j + 13, k - 1);
        placeOrcTorch(world, i, j + 13, k + 1);
        placeOrcTorch(world, i - 1, j + 13, k);
        placeOrcTorch(world, i + 1, j + 13, k);
        LOTRMordorOrcSlaverEntity slaver = create(LOTREntities.MORDOR_ORC_SLAVER, world);
        slaver.snapTo(i + 1.5, j + 7, k + 1.5, 0.0f, 0.0f);
        slaver.finalizeSpawn(world, world.getCurrentDifficultyAt(slaver.blockPosition()), EntitySpawnReason.STRUCTURE, null);
        world.addFreshEntity(slaver);
        slaver.setHomeTo(new BlockPos(i, j + 6, k), 12);
        int orcs = 2 + random.nextInt(3);
        for (int l = 0; l < orcs; ++l) {
            LOTRMordorOrcEntity orc = create(LOTREntities.MORDOR_ORC, world);
            orc.snapTo(i + 1.5, j + 1, k + 1.5, 0.0f, 0.0f);
            orc.finalizeSpawn(world, world.getCurrentDifficultyAt(orc.blockPosition()), EntitySpawnReason.STRUCTURE, null);
            orc.isNPCPersistent = true;
            world.addFreshEntity(orc);
            orc.setHomeTo(new BlockPos(i, j + 1, k), 8);
        }
        return true;
    }
}
