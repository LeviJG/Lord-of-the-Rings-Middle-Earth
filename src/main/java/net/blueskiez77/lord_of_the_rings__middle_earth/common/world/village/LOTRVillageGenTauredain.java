package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.village;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRRoadType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.farharad.LOTRTauredainChieftainPyramidStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.farharad.LOTRTauredainHouseLargeStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.farharad.LOTRTauredainHouseSimpleStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.farharad.LOTRTauredainHouseStiltsStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.farharad.LOTRTauredainSmithyStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.farharad.LOTRTauredainVillageFarmStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.farharad.LOTRTauredainVillageTreeStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.farharad.LOTRTauredainWatchtowerStructure;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRVillageGenTauredain extends LOTRVillageGen {
    public LOTRVillageGenTauredain(String biome, float f) {
        super(biome);
        gridScale = 10;
        gridRandomDisplace = 1;
        spawnChance = f;
        villageChunkRadius = 3;
    }

    @Override
    public LOTRVillageGen.AbstractInstance<?> createVillageInstance(long worldSeed, int i, int k, RandomSource random, LocationInfo loc) {
        return new Instance(this, worldSeed, i, k, random, loc);
    }

    public static class Instance extends LOTRVillageGen.AbstractInstance<LOTRVillageGenTauredain> {
        public Instance(LOTRVillageGenTauredain village, long worldSeed, int i, int k, RandomSource random, LocationInfo loc) {
            super(village, worldSeed, i, k, random, loc);
        }

        @Override
        public void addVillageStructures(RandomSource random) {
            int smithyPos = random.nextInt(4);
            addStructure(new LOTRTauredainChieftainPyramidStructure(false), 0, -11, 0, true);
            addStructure(new LOTRTauredainVillageTreeStructure(false), 0, -16, 2);
            addStructure(new LOTRTauredainVillageFarmStructure(false), -16, -19, 2);
            addStructure(new LOTRTauredainVillageFarmStructure(false), 16, -19, 2);
            addStructure(new LOTRTauredainHouseStiltsStructure(false), 0, 15, 0);
            addStructure(new LOTRTauredainVillageFarmStructure(false), -16, 19, 0);
            addStructure(new LOTRTauredainVillageFarmStructure(false), 16, 19, 0);
            addStructure(new LOTRTauredainHouseLargeStructure(false), -20, 0, 1);
            addStructure(new LOTRTauredainHouseLargeStructure(false), 20, 1, 3);
            addStructure(new LOTRTauredainHouseSimpleStructure(false), -15, -36, 0);
            addStructure(new LOTRTauredainHouseSimpleStructure(false), 15, -36, 0);
            if (smithyPos == 0) {
                addStructure(new LOTRTauredainSmithyStructure(false), -22, -13, 1);
            } else {
                addStructure(new LOTRTauredainHouseSimpleStructure(false), -32, -22, 3);
                addStructure(new LOTRTauredainHouseSimpleStructure(false), -32, -12, 3);
            }
            if (smithyPos == 1) {
                addStructure(new LOTRTauredainSmithyStructure(false), -22, 14, 1);
            } else {
                addStructure(new LOTRTauredainHouseSimpleStructure(false), -32, 13, 3);
                addStructure(new LOTRTauredainHouseSimpleStructure(false), -32, 23, 3);
            }
            if (smithyPos == 2) {
                addStructure(new LOTRTauredainSmithyStructure(false), 22, -13, 3);
            } else {
                addStructure(new LOTRTauredainHouseSimpleStructure(false), 32, -22, 1);
                addStructure(new LOTRTauredainHouseSimpleStructure(false), 32, -12, 1);
            }
            if (smithyPos == 3) {
                addStructure(new LOTRTauredainSmithyStructure(false), 22, 14, 3);
            } else {
                addStructure(new LOTRTauredainHouseSimpleStructure(false), 32, 13, 1);
                addStructure(new LOTRTauredainHouseSimpleStructure(false), 32, 23, 1);
            }
            addStructure(new LOTRTauredainHouseSimpleStructure(false), -15, 36, 2);
            addStructure(new LOTRTauredainHouseSimpleStructure(false), 0, 37, 2);
            addStructure(new LOTRTauredainHouseSimpleStructure(false), 15, 36, 2);
            addStructure(new LOTRTauredainWatchtowerStructure(false), -26, -36, 0);
            addStructure(new LOTRTauredainWatchtowerStructure(false), 26, -36, 0);
            addStructure(new LOTRTauredainWatchtowerStructure(false), -26, 37, 2);
            addStructure(new LOTRTauredainWatchtowerStructure(false), 26, 37, 2);
        }

        @Override
        public LOTRRoadType getPath(RandomSource random, int i, int k) {
            return null;
        }

        @Override
        public boolean isFlat() {
            return false;
        }

        @Override
        public boolean isVillageSpecificSurface(WorldGenLevel world, int i, int j, int k) {
            return false;
        }

        @Override
        public void setupVillageProperties(RandomSource random) {
        }
    }

}
