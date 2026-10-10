package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.farharad;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRBiomes;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.biome.LOTRFarHaradSavannahBiome;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

/** LOTRWorldGenMoredainVillage: three to six huts, some traders' and perhaps a chieftain's, where the savannah is peopled. */
public class LOTRMoredainVillageStructure extends LOTRStructureBase2 {
    public static final int VILLAGE_SIZE = 16;

    public LOTRMoredainVillageStructure(boolean flag) {
        super(flag);
    }

    public void attemptHutSpawn(LOTRStructureBase2 structure, WorldGenLevel world, RandomSource random) {
        structure.restrictions = restrictions;
        structure.usingPlayer = usingPlayer;
        for (int l = 0; l < 16; ++l) {
            int x = LOTRWorldGenUtil.getRandomIntegerInRange(random, -VILLAGE_SIZE, VILLAGE_SIZE);
            int z = LOTRWorldGenUtil.getRandomIntegerInRange(random, -VILLAGE_SIZE, VILLAGE_SIZE);
            int spawnX = getX(x, z);
            int spawnZ = getZ(x, z);
            int spawnY = getY(getTopBlock(world, x, z));
            if (generateChild(structure, world, random, spawnX, spawnY, spawnZ, random.nextInt(4))) {
                return;
            }
        }
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        setOriginAndRotation(world, i, j, k, rotation, usingPlayer != null ? VILLAGE_SIZE + 1 : 0);
        if (restrictions) {
            boolean suitableSpawn = false;
            if (LOTRBiomes.of(world.getBiome(new BlockPos(originX, originY, originZ))) instanceof LOTRFarHaradSavannahBiome) {
                suitableSpawn = LOTRFarHaradSavannahBiome.isBiomePopulated(originX, originY, originZ);
            }
            if (!suitableSpawn) {
                return false;
            }
        }
        int huts = LOTRWorldGenUtil.getRandomIntegerInRange(random, 3, 6);
        int traderHuts = LOTRWorldGenUtil.getRandomIntegerInRange(random, 0, 2);
        int chieftainHuts = LOTRWorldGenUtil.getRandomIntegerInRange(random, 0, 1);
        for (int l = 0; l < chieftainHuts; ++l) {
            attemptHutSpawn(new LOTRMoredainHutChieftainStructure(notifyChanges), world, random);
        }
        for (int l = 0; l < huts; ++l) {
            attemptHutSpawn(new LOTRMoredainHutVillageStructure(notifyChanges), world, random);
        }
        for (int l = 0; l < traderHuts; ++l) {
            attemptHutSpawn(new LOTRMoredainHutTraderStructure(notifyChanges), world, random);
        }
        return true;
    }
}
