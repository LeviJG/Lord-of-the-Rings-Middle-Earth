package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.orc;

import java.util.ArrayList;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.LOTRLegacyWorld;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.orc.LOTROrcEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.item.LOTRChestContents;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.WorldGenLevel;

public class LOTROrcDungeonStructure extends LOTRStructureBase {
    public LOTROrcDungeonStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generate(WorldGenLevel world, RandomSource random, int i, int j, int k) {
        int k1;
        int i1;
        int j1;
        int xSize = random.nextInt(3) + 2;
        int ySize = 3;
        int zSize = random.nextInt(3) + 2;
        int height = 0;
        if (!restrictions && usingPlayer != null) {
            int rotation = usingPlayerRotation();
            switch (rotation) {
                case 0: {
                    k += zSize + 2;
                    break;
                }
                case 1: {
                    i -= xSize + 2;
                    break;
                }
                case 2: {
                    k -= zSize + 2;
                    break;
                }
                case 3: {
                    i += xSize + 2;
                }
            }
        }
        if (restrictions) {
            for (i1 = i - xSize - 1; i1 <= i + xSize + 1; ++i1) {
                for (j1 = j - 1; j1 <= j + ySize + 1; ++j1) {
                    for (k1 = k - zSize - 1; k1 <= k + zSize + 1; ++k1) {
                        boolean solid = LOTRLegacyWorld.isSolid(world.getBlockState(new BlockPos(i1, j1, k1)));
                        if (j1 == j - 1 && !solid || j1 == j + ySize + 1 && !solid) {
                            return false;
                        }
                        if (i1 != i - xSize - 1 && i1 != i + xSize + 1 && k1 != k - zSize - 1 && k1 != k + zSize + 1 || j1 != j || !world.isEmptyBlock(new BlockPos(i1, j1, k1)) || !world.isEmptyBlock(new BlockPos(i1, j1 + 1, k1))) {
                            continue;
                        }
                        ++height;
                    }
                }
            }
        } else {
            height = 3;
        }
        if (height >= 1 && height <= 5) {
            for (i1 = i - xSize - 1; i1 <= i + xSize + 1; ++i1) {
                for (j1 = j + ySize; j1 >= j - 1; --j1) {
                    for (k1 = k - zSize - 1; k1 <= k + zSize + 1; ++k1) {
                        if (i1 != i - xSize - 1 && j1 != j - 1 && k1 != k - zSize - 1 && i1 != i + xSize + 1 && j1 != j + ySize + 1 && k1 != k + zSize + 1) {
                            setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("air"), 0);
                            continue;
                        }
                        if (restrictions && !LOTRLegacyWorld.isSolid(world.getBlockState(new BlockPos(i1, j1, k1)))) {
                            continue;
                        }
                        if (random.nextInt(4) != 0) {
                            setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("stonebrick"), 1 + random.nextInt(2));
                            continue;
                        }
                        setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("stonebrick"), 0);
                    }
                }
            }
            block12:
            for (int chestAttempts = 0; chestAttempts < 2; ++chestAttempts) {
                for (int thisChestAttempts = 0; thisChestAttempts < 3; ++thisChestAttempts) {
                    int k12;
                    int i12 = i + random.nextInt(xSize * 2 + 1) - xSize;
                    if (!world.isEmptyBlock(new BlockPos(i12, j, k12 = k + random.nextInt(zSize * 2 + 1) - zSize))) {
                        continue;
                    }
                    boolean flag = LOTRLegacyWorld.isSolid(world.getBlockState(new BlockPos(i12 - 1, j, k12)));
                    if (LOTRLegacyWorld.isSolid(world.getBlockState(new BlockPos(i12 + 1, j, k12)))) {
                        flag = true;
                    }
                    if (LOTRLegacyWorld.isSolid(world.getBlockState(new BlockPos(i12, j, k12 - 1)))) {
                        flag = true;
                    }
                    if (LOTRLegacyWorld.isSolid(world.getBlockState(new BlockPos(i12, j, k12 + 1)))) {
                        flag = true;
                    }
                    if (!flag) {
                        continue;
                    }
                    setBlockAndNotifyAdequately(world, i12, j, k12, LOTRLegacyBlocks.mod("chestStone"), 0);
                    LOTRChestContents.fillChest(world, random, new BlockPos(i12, j, k12), LOTRChestContents.ORC_DUNGEON, -1);
                    continue block12;
                }
            }
            EntityType<? extends LOTROrcEntity> backupClass = LOTREntities.GUNDABAD_ORC;
            // The original filled this with the orcs the biome here spawns (its NPC spawn
            // list at this spot's variant); the biomes come with D10, so until then every
            // dungeon keeps its fallback, Gundabad orcs.
            ArrayList<EntityType<? extends LOTROrcEntity>> biomeClasses = new ArrayList<>();
            int orcs = Mth.randomBetweenInclusive(random, 3, 6);
            while (orcs > 0) {
                LOTROrcEntity orc;
                EntityType<? extends LOTROrcEntity> orcClass = backupClass;
                if (!biomeClasses.isEmpty()) {
                    orcClass = biomeClasses.get(random.nextInt(biomeClasses.size()));
                }
                if ((orc = create(orcClass, world)).isOrcBombardier()) {
                    continue;
                }
                orc.snapTo(i + 0.5, j + 1, k + 0.5, 0.0f, 0.0f);
                orc.setHomeTo(new BlockPos(i, j + 1, k), 4);
                orc.finalizeSpawn(world, world.getCurrentDifficultyAt(orc.blockPosition()), EntitySpawnReason.STRUCTURE, null);
                orc.isNPCPersistent = true;
                world.addFreshEntity(orc);
                --orcs;
            }
            int pillars = random.nextInt(6);
            block16:
            for (int l = 0; l < pillars; ++l) {
                int j12;
                int i13 = i + random.nextInt(xSize * 2 + 1) - xSize;
                int k13 = k + random.nextInt(zSize * 2 + 1) - zSize;
                if (i13 == i && k13 == k) {
                    continue;
                }
                for (j12 = j + ySize; j12 >= j; --j12) {
                    if (!world.isEmptyBlock(new BlockPos(i13, j12, k13))) {
                        continue block16;
                    }
                }
                for (j12 = j + ySize; j12 >= j; --j12) {
                    if (random.nextInt(4) != 0) {
                        setBlockAndNotifyAdequately(world, i13, j12, k13, LOTRLegacyBlocks.vanilla("stonebrick"), 1 + random.nextInt(2));
                        continue;
                    }
                    setBlockAndNotifyAdequately(world, i13, j12, k13, LOTRLegacyBlocks.vanilla("stonebrick"), 0);
                }
            }
            return true;
        }
        return false;
    }
}
