package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRNPCRespawnerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.animal.LOTRCamelEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRWorldGenNearHaradDesertCamp: a little sandstone well between two tents, camels tethered
 * about it, and Harnedor warriors kept up by a respawner.
 */
public class LOTRNearHaradDesertCampStructure extends LOTRStructureBase2 {
    public LOTRNearHaradDesertCampStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        setOriginAndRotation(world, i, j, k, rotation, 0);
        int highestHeight = 0;
        for (int i1 = -1; i1 <= 1; ++i1) {
            for (int k1 = -1; k1 <= 1; ++k1) {
                int j1 = getTopBlock(world, i1, k1);
                BlockState block = getBlockState(world, i1, j1 - 1, k1);
                if (!LOTRLegacyBlocks.vanilla("sand").matches(block) && !LOTRLegacyBlocks.vanilla("dirt").matches(block)
                        && !LOTRLegacyBlocks.vanilla("grass").matches(block)) {
                    return false;
                }
                if (j1 > highestHeight) {
                    highestHeight = j1;
                }
            }
        }
        boolean flag;
        int x = -2 + random.nextInt(5);
        int z = 4 + random.nextInt(4);
        int y = getTopBlock(world, x, z);
        flag = generateChild(new LOTRNearHaradTentStructure(notifyChanges), world, random, getX(x, z), getY(y), getZ(x, z), getRotationMode());
        x = -2 + random.nextInt(5);
        z = -4 - random.nextInt(4);
        y = getTopBlock(world, x, z);
        flag = generateChild(new LOTRNearHaradTentStructure(notifyChanges), world, random, getX(x, z), getY(y), getZ(x, z), (getRotationMode() + 2) % 4) || flag;
        if (!flag) {
            return false;
        }
        for (int i1 = -1; i1 <= 1; ++i1) {
            for (int k1 = -1; k1 <= 1; ++k1) {
                int j1 = highestHeight - 1;
                while (!isOpaque(world, i1, j1, k1) && getY(j1) >= world.getMinY()) {
                    setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("sandstone"), 0);
                    setGrassToDirt(world, i1, j1 - 1, k1);
                    --j1;
                }
            }
            setBlockAndMetadata(world, i1, highestHeight, -1, LOTRLegacyBlocks.mod("stairsNearHaradBrick"), 2);
            setBlockAndMetadata(world, i1, highestHeight, 1, LOTRLegacyBlocks.mod("stairsNearHaradBrick"), 3);
        }
        setBlockAndMetadata(world, -1, highestHeight, 0, LOTRLegacyBlocks.mod("stairsNearHaradBrick"), 1);
        setBlockAndMetadata(world, 1, highestHeight, 0, LOTRLegacyBlocks.mod("stairsNearHaradBrick"), 0);
        setBlockAndMetadata(world, 0, highestHeight, 0, LOTRLegacyBlocks.vanilla("water"), 0);
        int camels = 1 + random.nextInt(4);
        for (int l = 0; l < camels; ++l) {
            int camelX = random.nextBoolean() ? -3 - random.nextInt(3) : 3 + random.nextInt(3);
            int camelZ = -3 + random.nextInt(7);
            int camelY = getTopBlock(world, camelX, camelZ);
            if (!LOTRLegacyBlocks.vanilla("sand").matches(getBlockState(world, camelX, camelY - 1, camelZ))
                    || !isAir(world, camelX, camelY, camelZ) || !isAir(world, camelX, camelY + 1, camelZ)) {
                continue;
            }
            LOTRCamelEntity camel = create(LOTREntities.CAMEL, world);
            setBlockAndMetadata(world, camelX, camelY, camelZ, LOTRLegacyBlocks.mod("fence2"), 2);
            setBlockAndMetadata(world, camelX, camelY + 1, camelZ, LOTRLegacyBlocks.mod("fence2"), 2);
            spawnNPCAndSetHome(camel, world, camelX, camelY, camelZ, 0);
            camel.clearHome();
            camel.saddleMountForWorldGen();
            if (random.nextBoolean()) {
                camel.setChest(true);
            }
            leashEntityTo(camel, world, camelX, camelY, camelZ);
        }
        LOTRNPCRespawnerEntity respawner = create(LOTREntities.NPC_RESPAWNER, world);
        respawner.setSpawnClass(LOTREntities.HARNEDOR_WARRIOR);
        respawner.setCheckRanges(20, -12, 12, 4);
        respawner.setSpawnRanges(8, -4, 4, 16);
        placeNPCRespawner(respawner, world, 0, 0, 0);
        return true;
    }
}
