package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.dwarf;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.feature.LOTRWorldGenUtil;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

/** LOTRWorldGenDaleVillage: a village tower, and about it a smithy, a bakery and two to five houses. */
public class LOTRDaleVillageStructure extends LOTRStructureBase2 {
    public LOTRDaleVillageStructure(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        setOriginAndRotation(world, i, j, k, rotation, 0);
        setupRandomBlocks(random);
        LOTRDaleVillageTowerStructure tower = new LOTRDaleVillageTowerStructure(notifyChanges);
        tower.restrictions = true;
        int i1 = 0;
        int k1 = -3;
        int j1 = getTopBlock(world, i1, k1);
        int r = 0;
        if (!generateChild(tower, world, random, getX(i1, k1), getY(j1), getZ(i1, k1), (getRotationMode() + r) % 4)) {
            return false;
        }
        int smithies = LOTRWorldGenUtil.getRandomIntegerInRange(random, 0, 1);
        for (int l = 0; l < smithies; ++l) {
            LOTRDaleSmithyStructure smithy = new LOTRDaleSmithyStructure(notifyChanges);
            smithy.restrictions = true;
            tryGenerateHouse(world, random, smithy);
        }
        int bakeries = LOTRWorldGenUtil.getRandomIntegerInRange(random, 0, 1);
        for (int l = 0; l < bakeries; ++l) {
            LOTRDaleBakeryStructure bakery = new LOTRDaleBakeryStructure(notifyChanges);
            bakery.restrictions = true;
            tryGenerateHouse(world, random, bakery);
        }
        int houses = LOTRWorldGenUtil.getRandomIntegerInRange(random, 2, 5);
        for (int l = 0; l < houses; ++l) {
            LOTRDaleHouseStructure house = new LOTRDaleHouseStructure(notifyChanges);
            house.restrictions = true;
            tryGenerateHouse(world, random, house);
        }
        return true;
    }

    public void tryGenerateHouse(WorldGenLevel world, RandomSource random, LOTRStructureBase2 structure) {
        int attempts = 8;
        for (int l = 0; l < attempts; ++l) {
            int i1 = 0;
            int k1 = 0;
            int r = random.nextInt(4);
            switch (r) {
                case 0 -> {
                    i1 = LOTRWorldGenUtil.getRandomIntegerInRange(random, -16, 16);
                    k1 = LOTRWorldGenUtil.getRandomIntegerInRange(random, 7, 10);
                }
                case 1 -> {
                    k1 = LOTRWorldGenUtil.getRandomIntegerInRange(random, -16, 16);
                    i1 = -LOTRWorldGenUtil.getRandomIntegerInRange(random, 7, 10);
                }
                case 2 -> {
                    i1 = LOTRWorldGenUtil.getRandomIntegerInRange(random, -16, 16);
                    k1 = -LOTRWorldGenUtil.getRandomIntegerInRange(random, 7, 10);
                }
                default -> {
                    k1 = LOTRWorldGenUtil.getRandomIntegerInRange(random, -16, 16);
                    i1 = LOTRWorldGenUtil.getRandomIntegerInRange(random, 7, 10);
                }
            }
            int j1 = getTopBlock(world, i1, k1);
            if (generateChild(structure, world, random, getX(i1, k1), getY(j1), getZ(i1, k1), (getRotationMode() + r) % 4)) {
                return;
            }
        }
    }
}
