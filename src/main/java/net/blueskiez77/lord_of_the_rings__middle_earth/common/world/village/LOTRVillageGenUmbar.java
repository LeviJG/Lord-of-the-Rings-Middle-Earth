package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.village;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRNPCRespawnerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRSouthronTownGateStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRSouthronVillageSignStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRUmbarBarracksStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRUmbarBazaarStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRUmbarFarmStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRUmbarFortCornerStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRUmbarFortGateStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRUmbarFortWallStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRUmbarFortressStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRUmbarHouseStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRUmbarLampStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRUmbarMansionStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRUmbarPastureStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRUmbarSmithyStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRUmbarStablesStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRUmbarStatueStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRUmbarTavernStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRUmbarTowerStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRUmbarTownCornerStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRUmbarTownFlowersStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRUmbarTownGateStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRUmbarTownTreeStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRUmbarTownWallStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRUmbarTrainingStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRUmbarVillageSignStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRUmbarWellStructure;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRVillageGenUmbar extends LOTRVillageGenSouthron {
    public LOTRVillageGenUmbar(String biome, float f) {
        super(biome, f);
    }

    @Override
    public LOTRVillageGen.AbstractInstance<?> createVillageInstance(WorldGenLevel world, int i, int k, RandomSource random, LocationInfo loc) {
        return new InstanceUmbar(this, world, i, k, random, loc);
    }

    public static class InstanceUmbar extends LOTRVillageGenSouthron.Instance {
        public InstanceUmbar(LOTRVillageGenUmbar village, WorldGenLevel world, int i, int k, RandomSource random, LocationInfo loc) {
            super(village, world, i, k, random, loc);
        }

        @Override
        public LOTRStructureBase2 getBarracks(RandomSource random) {
            return new LOTRUmbarBarracksStructure(false);
        }

        @Override
        public LOTRStructureBase2 getBazaar(RandomSource random) {
            return new LOTRUmbarBazaarStructure(false);
        }

        @Override
        public LOTRStructureBase2 getFlowers(RandomSource random) {
            return new LOTRUmbarTownFlowersStructure(false);
        }

        @Override
        public LOTRStructureBase2 getFortCorner(RandomSource random) {
            return new LOTRUmbarFortCornerStructure(false);
        }

        @Override
        public LOTRStructureBase2 getFortGate(RandomSource random) {
            return new LOTRUmbarFortGateStructure(false);
        }

        @Override
        public LOTRStructureBase2 getFortress(RandomSource random) {
            return new LOTRUmbarFortressStructure(false);
        }

        @Override
        public LOTRStructureBase2 getFortWallLong(RandomSource random) {
            return new LOTRUmbarFortWallStructure.Long(false);
        }

        @Override
        public LOTRStructureBase2 getFortWallShort(RandomSource random) {
            return new LOTRUmbarFortWallStructure.Short(false);
        }

        @Override
        public LOTRStructureBase2 getHouse(RandomSource random) {
            return new LOTRUmbarHouseStructure(false);
        }

        @Override
        public LOTRStructureBase2 getLamp(RandomSource random) {
            return new LOTRUmbarLampStructure(false);
        }

        @Override
        public LOTRStructureBase2 getMansion(RandomSource random) {
            return new LOTRUmbarMansionStructure(false);
        }

        @Override
        public LOTRStructureBase2 getRandomFarm(RandomSource random) {
            if (random.nextBoolean()) {
                return new LOTRUmbarFarmStructure(false);
            }
            return new LOTRUmbarPastureStructure(false);
        }

        @Override
        public LOTRStructureBase2 getRandomHouse(RandomSource random) {
            if (random.nextInt(6) == 0) {
                return new LOTRUmbarSmithyStructure(false);
            }
            if (random.nextInt(6) == 0) {
                return new LOTRUmbarStablesStructure(false);
            }
            return new LOTRUmbarHouseStructure(false);
        }

        @Override
        public LOTRSouthronVillageSignStructure getSignpost(RandomSource random) {
            return new LOTRUmbarVillageSignStructure(false);
        }

        @Override
        public LOTRStructureBase2 getSmithy(RandomSource random) {
            return new LOTRUmbarSmithyStructure(false);
        }

        @Override
        public LOTRStructureBase2 getStables(RandomSource random) {
            return new LOTRUmbarStablesStructure(false);
        }

        @Override
        public LOTRStructureBase2 getStatue(RandomSource random) {
            return new LOTRUmbarStatueStructure(false);
        }

        @Override
        public LOTRStructureBase2 getTavern(RandomSource random) {
            return new LOTRUmbarTavernStructure(false);
        }

        @Override
        public LOTRStructureBase2 getTower(RandomSource random) {
            return new LOTRUmbarTowerStructure(false);
        }

        @Override
        public LOTRSouthronTownGateStructure getTownGate(RandomSource random) {
            return new LOTRUmbarTownGateStructure(false);
        }

        @Override
        public LOTRStructureBase2 getTownWallCorner(RandomSource random) {
            return new LOTRUmbarTownCornerStructure(false);
        }

        @Override
        public LOTRStructureBase2 getTownWallExtra(RandomSource random) {
            return new LOTRUmbarTownWallStructure.Extra(false);
        }

        @Override
        public LOTRStructureBase2 getTownWallLong(RandomSource random) {
            return new LOTRUmbarTownWallStructure.Long(false);
        }

        @Override
        public LOTRStructureBase2 getTownWallShort(RandomSource random) {
            return new LOTRUmbarTownWallStructure.Short(false);
        }

        @Override
        public LOTRStructureBase2 getTownWallSideMid(RandomSource random) {
            return new LOTRUmbarTownWallStructure.SideMid(false);
        }

        @Override
        public LOTRStructureBase2 getTraining(RandomSource random) {
            return new LOTRUmbarTrainingStructure(false);
        }

        @Override
        public LOTRStructureBase2 getTree(RandomSource random) {
            return new LOTRUmbarTownTreeStructure(false);
        }

        @Override
        public LOTRStructureBase2 getWell(RandomSource random) {
            return new LOTRUmbarWellStructure(false);
        }

        @Override
        public void placeChampionRespawner() {
        }

        @Override
        public void setCivilianSpawnClass(LOTRNPCRespawnerEntity spawner) {
            spawner.setSpawnClass(LOTREntities.UMBARIAN);
        }

        @Override
        public void setWarriorSpawnClasses(LOTRNPCRespawnerEntity spawner) {
            spawner.setSpawnClasses(LOTREntities.UMBAR_WARRIOR, LOTREntities.UMBAR_ARCHER);
        }
    }

}
