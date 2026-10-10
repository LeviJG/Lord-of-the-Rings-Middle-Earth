package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.village;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRNPCRespawnerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNames;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRRoadType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRNPCRespawnerStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRSouthronBarracksStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRSouthronBazaarStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRSouthronFarmStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRSouthronFortCornerStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRSouthronFortGateStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRSouthronFortWallStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRSouthronFortressStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRSouthronHouseStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRSouthronLampStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRSouthronMansionStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRSouthronPastureStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRSouthronSmithyStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRSouthronStablesStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRSouthronStatueStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRSouthronTavernStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRSouthronTowerStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRSouthronTownCornerStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRSouthronTownFlowersStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRSouthronTownGateStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRSouthronTownTreeStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRSouthronTownWallStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRSouthronTrainingStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRSouthronVillageFenceStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRSouthronVillagePostStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRSouthronVillageSignStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRSouthronWellStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.hobbit.LOTRHayBalesStructure;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRVillageGenSouthron extends LOTRVillageGen {
    public LOTRVillageGenSouthron(String biome, float f) {
        super(biome);
        gridScale = 14;
        gridRandomDisplace = 1;
        spawnChance = f;
        villageChunkRadius = 5;
    }

    @Override
    public LOTRVillageGen.AbstractInstance<?> createVillageInstance(long worldSeed, int i, int k, RandomSource random, LocationInfo loc) {
        return new Instance(this, worldSeed, i, k, random, loc);
    }

    public enum VillageType {
        VILLAGE, TOWN, FORT

    }

    public static class Instance extends LOTRVillageGen.AbstractInstance<LOTRVillageGenSouthron> {
        public VillageType villageType;
        public String[] villageName;

        public Instance(LOTRVillageGenSouthron village, long worldSeed, int i, int k, RandomSource random, LocationInfo loc) {
            super(village, worldSeed, i, k, random, loc);
        }

        @Override
        public void addVillageStructures(RandomSource random) {
            if (villageType == VillageType.VILLAGE) {
                setupVillage(random);
            } else if (villageType == VillageType.TOWN) {
                setupTown(random);
            } else if (villageType == VillageType.FORT) {
                setupFort(random);
            }
        }

        public LOTRStructureBase2 getBarracks(RandomSource random) {
            return new LOTRSouthronBarracksStructure(false);
        }

        public LOTRStructureBase2 getBazaar(RandomSource random) {
            return new LOTRSouthronBazaarStructure(false);
        }

        public LOTRStructureBase2 getFlowers(RandomSource random) {
            return new LOTRSouthronTownFlowersStructure(false);
        }

        public LOTRStructureBase2 getFortCorner(RandomSource random) {
            return new LOTRSouthronFortCornerStructure(false);
        }

        public LOTRStructureBase2 getFortGate(RandomSource random) {
            return new LOTRSouthronFortGateStructure(false);
        }

        public LOTRStructureBase2 getFortress(RandomSource random) {
            return new LOTRSouthronFortressStructure(false);
        }

        public LOTRStructureBase2 getFortWallLong(RandomSource random) {
            return new LOTRSouthronFortWallStructure.Long(false);
        }

        public LOTRStructureBase2 getFortWallShort(RandomSource random) {
            return new LOTRSouthronFortWallStructure.Short(false);
        }

        public LOTRStructureBase2 getHouse(RandomSource random) {
            return new LOTRSouthronHouseStructure(false);
        }

        public LOTRStructureBase2 getLamp(RandomSource random) {
            return new LOTRSouthronLampStructure(false);
        }

        public LOTRStructureBase2 getMansion(RandomSource random) {
            return new LOTRSouthronMansionStructure(false);
        }

        @Override
        public LOTRRoadType getPath(RandomSource random, int i, int k) {
            int i1 = Math.abs(i);
            int k1 = Math.abs(k);
            if (villageType == VillageType.VILLAGE) {
                int imn = 2;
                int imx = 14 + random.nextInt(3);
                int kmn = 2;
                int kmx = 14 + random.nextInt(3);
                if (i1 <= imx && k1 <= kmx && (i1 > imn || k1 > kmn)) {
                    return LOTRRoadType.PATH;
                }
                imn = 45 - random.nextInt(3);
                imx = 50 + random.nextInt(3);
                kmn = 45 - random.nextInt(3);
                kmx = 50 + random.nextInt(3);
                if (i1 <= imx && k1 <= kmx && (i1 > imn || k1 > kmn) && (k < 0 || i1 > 7)) {
                    return LOTRRoadType.PATH;
                }
                if (k < 0) {
                    imn = 14;
                    imx = 45;
                    if (i1 + k1 >= imn + imn && i1 + k1 <= imx + imx && Math.abs(i1 - k1) <= (int) (2.5f + random.nextInt(3) * 2.0f)) {
                        return LOTRRoadType.PATH;
                    }
                }
                if (k > 0) {
                    imn = 10;
                    imx = imn + 5 + random.nextInt(3);
                    kmn = 14;
                    kmx = 45;
                    if (k1 >= kmn && k1 <= kmx && i1 >= imn - random.nextInt(3) && i1 <= imx) {
                        return LOTRRoadType.PATH;
                    }
                }
            }
            if (villageType == VillageType.TOWN && i1 <= 72 && k1 <= 42) {
                return LOTRRoadType.HARAD_TOWN;
            }
            if (villageType == VillageType.FORT) {
                if (i1 <= 3 && k >= -45 && k <= -15) {
                    return LOTRRoadType.PATH;
                }
                if (i1 <= 36 && k >= -27 && k <= -20) {
                    return LOTRRoadType.PATH;
                }
                if (i1 >= 29 && i1 <= 36 && k >= -27 && k <= 39 && (k < -7 || k > 7)) {
                    return LOTRRoadType.PATH;
                }
                if (i1 <= 36 && k >= 20 && k <= 27) {
                    return LOTRRoadType.PATH;
                }
            }
            return null;
        }

        public LOTRStructureBase2 getRandomFarm(RandomSource random) {
            if (random.nextBoolean()) {
                return new LOTRSouthronFarmStructure(false);
            }
            return new LOTRSouthronPastureStructure(false);
        }

        public LOTRStructureBase2 getRandomHouse(RandomSource random) {
            if (random.nextInt(6) == 0) {
                return new LOTRSouthronSmithyStructure(false);
            }
            if (random.nextInt(6) == 0) {
                return new LOTRSouthronStablesStructure(false);
            }
            return new LOTRSouthronHouseStructure(false);
        }

        public LOTRSouthronVillageSignStructure getSignpost(RandomSource random) {
            return new LOTRSouthronVillageSignStructure(false);
        }

        public LOTRStructureBase2 getSmithy(RandomSource random) {
            return new LOTRSouthronSmithyStructure(false);
        }

        public LOTRStructureBase2 getStables(RandomSource random) {
            return new LOTRSouthronStablesStructure(false);
        }

        public LOTRStructureBase2 getStatue(RandomSource random) {
            return new LOTRSouthronStatueStructure(false);
        }

        public LOTRStructureBase2 getTavern(RandomSource random) {
            return new LOTRSouthronTavernStructure(false);
        }

        public LOTRStructureBase2 getTower(RandomSource random) {
            return new LOTRSouthronTowerStructure(false);
        }

        public LOTRSouthronTownGateStructure getTownGate(RandomSource random) {
            return new LOTRSouthronTownGateStructure(false);
        }

        public LOTRStructureBase2 getTownWallCorner(RandomSource random) {
            return new LOTRSouthronTownCornerStructure(false);
        }

        public LOTRStructureBase2 getTownWallExtra(RandomSource random) {
            return new LOTRSouthronTownWallStructure.Extra(false);
        }

        public LOTRStructureBase2 getTownWallLong(RandomSource random) {
            return new LOTRSouthronTownWallStructure.Long(false);
        }

        public LOTRStructureBase2 getTownWallShort(RandomSource random) {
            return new LOTRSouthronTownWallStructure.Short(false);
        }

        public LOTRStructureBase2 getTownWallSideMid(RandomSource random) {
            return new LOTRSouthronTownWallStructure.SideMid(false);
        }

        public LOTRStructureBase2 getTraining(RandomSource random) {
            return new LOTRSouthronTrainingStructure(false);
        }

        public LOTRStructureBase2 getTree(RandomSource random) {
            return new LOTRSouthronTownTreeStructure(false);
        }

        public LOTRStructureBase2 getWell(RandomSource random) {
            return new LOTRSouthronWellStructure(false);
        }

        @Override
        public boolean isFlat() {
            return false;
        }

        @Override
        public boolean isVillageSpecificSurface(WorldGenLevel world, int i, int j, int k) {
            if (villageType == VillageType.TOWN) {
                BlockState block = world.getBlockState(new BlockPos(i, j, k));
                return is(block, "brick", 15) || is(block, "brick3", 11) || is(block, "pillar", 5);
            }
            return false;
        }

        public void placeChampionRespawner() {
            addStructure(new LOTRNPCRespawnerStructure(false) {

                @Override
                public void setupRespawner(LOTRNPCRespawnerEntity spawner) {
                    spawner.setSpawnClass(LOTREntities.SOUTHRON_CHAMPION);
                    spawner.setCheckRanges(60, -12, 12, 4);
                    spawner.setSpawnRanges(24, -6, 6, 32);
                }
            }, 0, 0, 0);
        }

        public void setCivilianSpawnClass(LOTRNPCRespawnerEntity spawner) {
            spawner.setSpawnClass(LOTREntities.NEAR_HARADRIM);
        }

        public void setupFort(RandomSource random) {
            int i;
            int r;
            int k;
            int l;
            addStructure(new LOTRNPCRespawnerStructure(false) {

                @Override
                public void setupRespawner(LOTRNPCRespawnerEntity spawner) {
                    setCivilianSpawnClass(spawner);
                    spawner.setCheckRanges(60, -12, 12, 16);
                    spawner.setSpawnRanges(24, -6, 6, 40);
                    spawner.setBlockEnemySpawnRange(60);
                }
            }, 0, 0, 0);
            for (int i1 : new int[]{-25, 25}) {
                for (int k1 : new int[]{-25, 25}) {
                    addStructure(new LOTRNPCRespawnerStructure(false) {

                        @Override
                        public void setupRespawner(LOTRNPCRespawnerEntity spawner) {
                            setWarriorSpawnClasses(spawner);
                            spawner.setCheckRanges(35, -12, 12, 16);
                            spawner.setSpawnRanges(15, -6, 6, 40);
                            spawner.setBlockEnemySpawnRange(35);
                        }
                    }, i1, k1, 0);
                }
            }
            placeChampionRespawner();
            addStructure(getFortress(random), 0, -15, 0, true);
            addStructure(getBarracks(random), -33, -8, 0, true);
            addStructure(getBarracks(random), 32, -8, 0, true);
            addStructure(getTower(random), -43, -36, 2, true);
            addStructure(getTower(random), 43, -36, 2, true);
            addStructure(getTower(random), -43, 36, 0, true);
            addStructure(getTower(random), 43, 36, 0, true);
            for (l = 0; l <= 2; ++l) {
                i = 10 + l * 11;
                k = -28;
                r = 2;
                addStructure(getRandomFarm(random), i, k, r);
                addStructure(getRandomFarm(random), -i, k, r);
            }
            addStructure(getTraining(random), 0, 27, 0, true);
            addStructure(getStables(random), -29, 33, 3, true);
            addStructure(getStables(random), 29, 37, 1, true);
            addStructure(getFortGate(random), 0, -47, 0, true);
            for (l = 0; l <= 9; ++l) {
                i = 8 + l * 4;
                k = -46;
                r = 0;
                addStructure(getFortWallLong(random), -i, k, r, true);
                addStructure(getFortWallLong(random), i, k, r, true);
            }
            for (l = -11; l <= 11; ++l) {
                i = l * 4;
                k = 46;
                r = 2;
                addStructure(getFortWallLong(random), i, k, r, true);
            }
            for (l = -10; l <= 10; ++l) {
                i = -50;
                k = l * 4;
                r = 3;
                addStructure(getFortWallLong(random), i, k, r, true);
                r = 1;
                addStructure(getFortWallLong(random), -i, k, r, true);
            }
            addStructure(getFortCorner(random), -50, -46, 0, true);
            addStructure(getFortCorner(random), 50, -46, 1, true);
            addStructure(getFortCorner(random), -50, 46, 3, true);
            addStructure(getFortCorner(random), 50, 46, 2, true);
        }

        public void setupTown(RandomSource random) {
            int i;
            int r;
            int k;
            int l;
            addStructure(new LOTRNPCRespawnerStructure(false) {

                @Override
                public void setupRespawner(LOTRNPCRespawnerEntity spawner) {
                    setCivilianSpawnClass(spawner);
                    spawner.setCheckRanges(80, -12, 12, 100);
                    spawner.setSpawnRanges(40, -6, 6, 64);
                    spawner.setBlockEnemySpawnRange(60);
                }
            }, 0, 0, 0);
            for (int i1 : new int[]{-30, 30}) {
                for (int k1 : new int[]{-30, 30}) {
                    addStructure(new LOTRNPCRespawnerStructure(false) {

                        @Override
                        public void setupRespawner(LOTRNPCRespawnerEntity spawner) {
                            setWarriorSpawnClasses(spawner);
                            spawner.setCheckRanges(40, -12, 12, 16);
                            spawner.setSpawnRanges(20, -6, 6, 64);
                            spawner.setBlockEnemySpawnRange(60);
                        }
                    }, i1, k1, 0);
                }
            }
            addStructure(getBazaar(random), 1, -2, 0, true);
            addStructure(getLamp(random), 15, -2, 0, true);
            addStructure(getLamp(random), -13, -2, 0, true);
            addStructure(getLamp(random), 15, 18, 0, true);
            addStructure(getLamp(random), -13, 18, 0, true);
            addStructure(getWell(random), -16, 12, 1, true);
            addStructure(getWell(random), -16, 4, 1, true);
            addStructure(getFlowers(random), 18, 13, 3, true);
            addStructure(getFlowers(random), 18, 3, 3, true);
            for (l = 0; l <= 3; ++l) {
                i = -41 + l * 19;
                k = -7;
                r = 2;
                addStructure(getMansion(random), i, k, r, true);
                addStructure(getLamp(random), i + 6, k - 1, r, true);
                i = 24 - l * 19;
                k = 23;
                r = 0;
                addStructure(getMansion(random), i, k, r, true);
                addStructure(getLamp(random), i - 6, k + 1, r, true);
            }
            addStructure(getSmithy(random), -25, 9, 1, true);
            addStructure(getHouse(random), -25, 18, 1, true);
            addStructure(getHouse(random), -25, -2, 1, true);
            addStructure(getTree(random), -45, 8, 1, true);
            addStructure(getHouse(random), -50, 18, 3, true);
            addStructure(getHouse(random), -50, -2, 3, true);
            addStructure(getWell(random), -51, -14, 2, true);
            addStructure(getTree(random), -46, -29, 2, true);
            addStructure(getFlowers(random), -42, -32, 3, true);
            addStructure(getTree(random), -50, 30, 0, true);
            for (l = -3; l <= 3; ++l) {
                i = -56;
                k = -2 + l * 10;
                r = 1;
                addStructure(getHouse(random), i, k, r, true);
            }
            addStructure(getStatue(random), 26, 8, 3, true);
            addStructure(getHouse(random), 26, 18, 3, true);
            addStructure(getHouse(random), 26, -2, 3, true);
            for (l = -3; l <= 2; ++l) {
                i = 52;
                k = 8 + l * 10;
                r = 1;
                addStructure(getHouse(random), i, k, r, true);
            }
            addStructure(getSmithy(random), 41, -33, 3, true);
            for (l = -2; l <= 2; ++l) {
                i = 65;
                k = 3 + l * 14;
                r = 2;
                addStructure(getHouse(random), i, k, r, true);
            }
            addStructure(getWell(random), 57, -19, 2, true);
            addStructure(getLamp(random), 57, -16, 2, true);
            addStructure(getLamp(random), 57, -8, 2, true);
            addStructure(getTree(random), 57, 1, 2, true);
            addStructure(getLamp(random), 57, 4, 2, true);
            addStructure(getLamp(random), 57, 12, 2, true);
            addStructure(getTree(random), 57, 21, 2, true);
            addStructure(getLamp(random), 57, 24, 2, true);
            addStructure(getLamp(random), 57, 32, 2, true);
            for (l = 0; l <= 3; ++l) {
                i = 41 + l * 8;
                k = 34;
                r = 0;
                addStructure(getFlowers(random), i, k, r, true);
            }
            addStructure(getTree(random), 34, 25, 0, true);
            addStructure(getStables(random), -20, -30, 1, true);
            addStructure(getTavern(random), 17, -32, 1, true);
            addStructure(getLamp(random), 19, -28, 1, true);
            addStructure(getLamp(random), 19, -36, 1, true);
            addStructure(getLamp(random), -16, -32, 3, true);
            addStructure(getFlowers(random), 25, -32, 3, true);
            addStructure(getTree(random), 34, -29, 2, true);
            addStructure(getLamp(random), 34, -26, 2, true);
            addStructure(getLamp(random), 34, -18, 2, true);
            addStructure(getTree(random), 34, -9, 2, true);
            addStructure(getTownGate(random).setSignText(villageName), 34, -47, 0, true);
            addStructure(getTownWallCorner(random), 73, -47, 0, true);
            addStructure(getTownWallCorner(random), -77, -43, 3, true);
            addStructure(getTownWallCorner(random), -73, 47, 2, true);
            addStructure(getTownWallCorner(random), 77, 43, 1, true);
            for (l = 0; l <= 6; ++l) {
                i = 68 - l * 4;
                k = -44;
                r = 0;
                if (l % 2 == 0) {
                    addStructure(getTownWallShort(random), i, k, r, true);
                    continue;
                }
                addStructure(getTownWallLong(random), i, k, r, true);
            }
            addStructure(getTownWallExtra(random), 24, -44, 0, true);
            for (l = 0; l <= 22; ++l) {
                i = 20 - l * 4;
                k = -44;
                r = 0;
                if (l % 2 == 0) {
                    addStructure(getTownWallShort(random), i, k, r, true);
                    continue;
                }
                addStructure(getTownWallLong(random), i, k, r, true);
            }
            addStructure(getTownWallSideMid(random), 74, 0, 1, true);
            addStructure(getTownWallSideMid(random), -74, 0, 3, true);
            for (l = 1; l <= 9; ++l) {
                i = 74;
                k = 2 + l * 4;
                if (l % 2 == 1) {
                    addStructure(getTownWallShort(random), i, k, 1, true);
                    addStructure(getTownWallShort(random), i, -k, 1, true);
                    addStructure(getTownWallShort(random), -i, k, 3, true);
                    addStructure(getTownWallShort(random), -i, -k, 3, true);
                    continue;
                }
                addStructure(getTownWallLong(random), i, k, 1, true);
                addStructure(getTownWallLong(random), i, -k, 1, true);
                addStructure(getTownWallLong(random), -i, k, 3, true);
                addStructure(getTownWallLong(random), -i, -k, 3, true);
            }
            for (l = -17; l <= 17; ++l) {
                i = l * 4;
                k = 44;
                r = 2;
                if (Math.floorMod(l, 2) == 1) {
                    addStructure(getTownWallShort(random), i, k, r, true);
                    continue;
                }
                addStructure(getTownWallLong(random), i, k, r, true);
            }
        }

        public void setupVillage(RandomSource random) {
            addStructure(new LOTRNPCRespawnerStructure(false) {

                @Override
                public void setupRespawner(LOTRNPCRespawnerEntity spawner) {
                    setCivilianSpawnClass(spawner);
                    spawner.setCheckRanges(64, -12, 12, 24);
                    spawner.setSpawnRanges(32, -6, 6, 32);
                    spawner.setBlockEnemySpawnRange(64);
                }
            }, 0, 0, 0);
            addStructure(new LOTRNPCRespawnerStructure(false) {

                @Override
                public void setupRespawner(LOTRNPCRespawnerEntity spawner) {
                    setWarriorSpawnClasses(spawner);
                    spawner.setCheckRanges(64, -12, 12, 12);
                    spawner.setSpawnRanges(32, -6, 6, 32);
                    spawner.setBlockEnemySpawnRange(64);
                }
            }, 0, 0, 0);
            addStructure(getWell(random), 0, -2, 0, true);
            addStructure(getSignpost(random).setSignText(villageName), 0, -8, 0, true);
            int rSquareEdge = 17;
            addStructure(getTavern(random), 0, rSquareEdge, 0, true);
            addStructure(getMansion(random), -3, -rSquareEdge, 2, true);
            addStructure(getMansion(random), -rSquareEdge, 3, 1, true);
            addStructure(getMansion(random), rSquareEdge, -3, 3, true);
            int backFenceX = 0;
            int backFenceZ = rSquareEdge + 19;
            int backFenceWidth = 12;
            int sideFenceX = 13;
            int sideFenceZ = rSquareEdge + 11;
            int sideFenceWidth = 8;
            int frontPostZ = sideFenceZ - sideFenceWidth - 1;
            addStructure(new LOTRSouthronVillageFenceStructure(false).setLeftRightExtent(backFenceWidth, backFenceWidth), backFenceX, -backFenceZ, 0);
            addStructure(new LOTRSouthronVillageFenceStructure(false).setLeftRightExtent(sideFenceWidth, sideFenceWidth - 1), -sideFenceX, -sideFenceZ, 1);
            addStructure(new LOTRSouthronVillageFenceStructure(false).setLeftRightExtent(sideFenceWidth - 1, sideFenceWidth), sideFenceX, -sideFenceZ, 3);
            addStructure(new LOTRSouthronVillagePostStructure(false), -sideFenceX, -frontPostZ, 0);
            addStructure(new LOTRSouthronVillagePostStructure(false), sideFenceX, -frontPostZ, 0);
            addStructure(new LOTRSouthronVillagePostStructure(false), -sideFenceX, -backFenceZ, 0);
            addStructure(new LOTRSouthronVillagePostStructure(false), sideFenceX, -backFenceZ, 0);
            addStructure(new LOTRSouthronVillageFenceStructure(false).setLeftRightExtent(backFenceWidth, backFenceWidth), -backFenceZ, backFenceX, 1);
            addStructure(new LOTRSouthronVillageFenceStructure(false).setLeftRightExtent(sideFenceWidth, sideFenceWidth - 1), -sideFenceZ, sideFenceX, 0);
            addStructure(new LOTRSouthronVillageFenceStructure(false).setLeftRightExtent(sideFenceWidth - 1, sideFenceWidth), -sideFenceZ, -sideFenceX, 2);
            addStructure(new LOTRSouthronVillagePostStructure(false), -frontPostZ, sideFenceX, 0);
            addStructure(new LOTRSouthronVillagePostStructure(false), -frontPostZ, -sideFenceX, 0);
            addStructure(new LOTRSouthronVillagePostStructure(false), -backFenceZ, sideFenceX, 0);
            addStructure(new LOTRSouthronVillagePostStructure(false), -backFenceZ, -sideFenceX, 0);
            addStructure(new LOTRSouthronVillageFenceStructure(false).setLeftRightExtent(backFenceWidth, backFenceWidth), backFenceZ, backFenceX, 3);
            addStructure(new LOTRSouthronVillageFenceStructure(false).setLeftRightExtent(sideFenceWidth, sideFenceWidth - 1), sideFenceZ, -sideFenceX, 2);
            addStructure(new LOTRSouthronVillageFenceStructure(false).setLeftRightExtent(sideFenceWidth - 1, sideFenceWidth), sideFenceZ, sideFenceX, 0);
            addStructure(new LOTRSouthronVillagePostStructure(false), frontPostZ, -sideFenceX, 0);
            addStructure(new LOTRSouthronVillagePostStructure(false), frontPostZ, sideFenceX, 0);
            addStructure(new LOTRSouthronVillagePostStructure(false), backFenceZ, -sideFenceX, 0);
            addStructure(new LOTRSouthronVillagePostStructure(false), backFenceZ, sideFenceX, 0);
            int farmRange = 3;
            int farmStep = 14;
            int farmX = 55;
            for (int l = -farmRange; l <= farmRange; ++l) {
                int k = l * farmStep;
                int i = -farmX;
                int r = 1;
                if (random.nextInt(3) == 0) {
                    addStructure(new LOTRHayBalesStructure(false), i, k, r);
                } else {
                    addStructure(getRandomFarm(random), i, k, r);
                }
                i = farmX;
                r = 3;
                if (random.nextInt(3) == 0) {
                    addStructure(new LOTRHayBalesStructure(false), i, k, r);
                    continue;
                }
                addStructure(getRandomFarm(random), i, k, r);
            }
            int houseRange = 3;
            int houseStep = 17;
            int houseZ = 55;
            for (int l = -houseRange; l <= houseRange; ++l) {
                int i = l * houseStep;
                int k = -houseZ;
                int r = 2;
                addStructure(getRandomHouse(random), i, k, r);
                k = houseZ;
                r = 0;
                if (Math.abs(i) < 7) {
                    continue;
                }
                addStructure(getRandomHouse(random), i, k, r);
            }
        }

        @Override
        public void setupVillageProperties(RandomSource random) {
            villageType = random.nextInt(4) == 0 ? VillageType.FORT : random.nextInt(3) == 0 ? VillageType.TOWN : VillageType.VILLAGE;
            villageName = LOTRNames.getHaradVillageName(random);
        }

        public void setWarriorSpawnClasses(LOTRNPCRespawnerEntity spawner) {
            spawner.setSpawnClasses(LOTREntities.NEAR_HARADRIM_WARRIOR, LOTREntities.NEAR_HARADRIM_ARCHER);
        }

    }

}
