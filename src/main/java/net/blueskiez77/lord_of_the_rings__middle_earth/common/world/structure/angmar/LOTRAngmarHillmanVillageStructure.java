package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.angmar;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

/** LOTRWorldGenAngmarHillmanVillage: three to six hillman houses, and sometimes a chieftain's, scattered about. */
public class LOTRAngmarHillmanVillageStructure extends LOTRStructureBase2 {
    public static final int VILLAGE_SIZE = 16;

    public LOTRAngmarHillmanVillageStructure(boolean flag) {
        super(flag);
    }

    public void attemptHouseSpawn(LOTRStructureBase2 structure, WorldGenLevel world, RandomSource random) {
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
        setOriginAndRotation(world, i, j, k, rotation, 0);
        int houses = LOTRWorldGenUtil.getRandomIntegerInRange(random, 3, 6);
        int chiefainHouses = LOTRWorldGenUtil.getRandomIntegerInRange(random, 0, 1);
        for (int l = 0; l < chiefainHouses; ++l) {
            attemptHouseSpawn(new LOTRAngmarHillmanChieftainHouseStructure(notifyChanges), world, random);
        }
        for (int l = 0; l < houses; ++l) {
            attemptHouseSpawn(new LOTRAngmarHillmanHouseStructure(notifyChanges), world, random);
        }
        return true;
    }
}
