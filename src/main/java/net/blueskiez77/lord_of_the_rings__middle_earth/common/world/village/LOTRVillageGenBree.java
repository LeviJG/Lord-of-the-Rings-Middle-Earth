package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.village;

import java.util.ArrayList;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRNPCRespawnerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRRoadType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRWaypoint;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRNPCRespawnerStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.bree.LOTRBreeBarnStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.bree.LOTRBreeGardenStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.bree.LOTRBreeGateStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.bree.LOTRBreeGatehouseStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.bree.LOTRBreeHedgePartStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.bree.LOTRBreeHobbitBurrowStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.bree.LOTRBreeHouseStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.bree.LOTRBreeInnStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.bree.LOTRBreeLampPostStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.bree.LOTRBreeMarketStallStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.bree.LOTRBreeMarketStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.bree.LOTRBreeOfficeStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.bree.LOTRBreeRuffianHouseStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.bree.LOTRBreeSmithyStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.bree.LOTRBreeStableStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.bree.LOTRBreeStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.bree.LOTRBreeWellStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.hobbit.LOTRHayBalesStructure;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRVillageGenBree extends LOTRVillageGen {
    public LOTRVillageGenBree(String biome, float f) {
        super(biome);
        gridScale = 12;
        gridRandomDisplace = 1;
        spawnChance = f;
        villageChunkRadius = 4;
        fixedVillageChunkRadius = 13;
    }

    @Override
    public LOTRVillageGen.AbstractInstance<?> createVillageInstance(long worldSeed, int i, int k, RandomSource random, LocationInfo loc) {
        return new Instance(this, worldSeed, i, k, random, loc);
    }

    public enum VillageType {
        HAMLET, VILLAGE

    }

    public static class Instance extends LOTRVillageGen.AbstractInstance<LOTRVillageGenBree> {
        public static boolean[][] hobbitPathLookup;
        public static int[] hobbitBurrowPathPoints;
        public static int[] hobbitBurrowEndPoints;
        public VillageType villageType;
        public int innerSize;
        public boolean hamletHedge;

        public Instance(LOTRVillageGenBree village, long worldSeed, int i, int k, RandomSource random, LocationInfo loc) {
            super(village, worldSeed, i, k, random, loc);
            if (hobbitPathLookup == null) {
                int l;
                int size = 361;
                hobbitPathLookup = new boolean[size][size];
                int numBurrows = 20;
                ArrayList<Integer> burrowCoords = new ArrayList<>();
                ArrayList<Integer> burrowEndCoords = new ArrayList<>();
                int samples = 300;
                int burrowInterval = samples / numBurrows;
                float[] pathPointsX = new float[samples];
                float[] pathPointsZ = new float[samples];
                int zStart = 50;
                int zEnd = 150;
                float cycles = 1.0f;
                int amp = 80;
                int endpointInterval = samples / Mth.floor(cycles * 4.0f);
                for (l = 0; l < samples; ++l) {
                    float x;
                    float t = (float) l / samples;
                    float z = zStart + (zEnd - zStart) * t;
                    pathPointsX[l] = x = Mth.sin((z - zStart) / (zEnd - zStart) * cycles * 3.1415927f * 2.0f) * amp;
                    pathPointsZ[l] = z;
                    if (l % burrowInterval == 0 && Math.abs(x) <= amp * 0.8f) {
                        burrowCoords.add(Mth.floor(x));
                        burrowCoords.add(Mth.floor(z));
                    }
                    //noinspection BadOddness
                    if (l % endpointInterval != 0 || l / endpointInterval % 2 != 1) {
                        continue;
                    }
                    burrowEndCoords.add(Mth.floor(x));
                    burrowEndCoords.add(Mth.floor(z));
                }
                hobbitBurrowPathPoints = new int[burrowCoords.size()];
                for (l = 0; l < burrowCoords.size(); ++l) {
                    hobbitBurrowPathPoints[l] = burrowCoords.get(l);
                }
                hobbitBurrowEndPoints = new int[burrowEndCoords.size()];
                for (l = 0; l < burrowEndCoords.size(); ++l) {
                    hobbitBurrowEndPoints[l] = burrowEndCoords.get(l);
                }
                int pathWidth = 3;
                for (int z = -180; z <= 180; ++z) {
                    block4:
                    for (int x = -180; x <= 180; ++x) {
                        int xi = x + 180;
                        int zi = z + 180;
                        hobbitPathLookup[zi][xi] = false;
                        float xMid = x + 0.5f;
                        float zMid = z + 0.5f;
                        for (int l2 = 0; l2 < samples; ++l2) {
                            float pathX = pathPointsX[l2];
                            float dx = xMid - pathX;
                            float pathZ = pathPointsZ[l2];
                            float dz = zMid - pathZ;
                            if (dx * dx + dz * dz > pathWidth * pathWidth) {
                                continue;
                            }
                            hobbitPathLookup[zi][xi] = true;
                            continue block4;
                        }
                    }
                }
            }
        }

        @Override
        public void addVillageStructures(RandomSource random) {
            if (villageType == VillageType.HAMLET) {
                setupHamlet(random);
            } else if (villageType == VillageType.VILLAGE) {
                setupVillage(random);
            }
        }

        public LOTRStructureBase2 getHamletHouse(RandomSource random) {
            if (random.nextInt(3) == 0) {
                return new LOTRBreeHobbitBurrowStructure(false);
            }
            if (random.nextInt(8) == 0) {
                return new LOTRBreeRuffianHouseStructure(false);
            }
            return new LOTRBreeHouseStructure(false);
        }

        public LOTRStructureBase2 getHamletHouseOrOther(RandomSource random) {
            if (random.nextInt(3) == 0) {
                float f = random.nextFloat();
                if (f < 0.08f) {
                    return new LOTRBreeBarnStructure(false);
                }
                if (f < 0.16f) {
                    return new LOTRBreeStableStructure(false);
                }
                if (f < 0.4f) {
                    return new LOTRBreeSmithyStructure(false);
                }
                if (f < 0.7f) {
                    return new LOTRBreeOfficeStructure(false);
                }
                return new LOTRBreeInnStructure(false);
            }
            return getHamletHouse(random);
        }

        @Override
        public LOTRRoadType getPath(RandomSource random, int i, int k) {
            int i1 = Math.abs(i);
            int k1 = Math.abs(k);
            if (villageType == VillageType.HAMLET) {
                int dSq = i * i + k * k;
                int imn = innerSize + random.nextInt(3);
                if (dSq < imn * imn) {
                    return LOTRRoadType.PATH;
                }
                if (hamletHedge && k < 0 && k > -(innerSize + 32 + 2) && i1 <= 2 + random.nextInt(3)) {
                    return LOTRRoadType.PATH;
                }
            }
            if (villageType == VillageType.VILLAGE) {
                if (i1 <= 192 && k1 <= 3) {
                    return LOTRRoadType.PAVED_PATH;
                }
                if (k >= -192 && k <= 50 && i1 <= 3) {
                    return LOTRRoadType.PAVED_PATH;
                }
                if (i1 <= 70 && Math.abs(k + 100) <= 3) {
                    return LOTRRoadType.PAVED_PATH;
                }
                if (i >= -180 && i <= 180 && k >= -180 && k <= 180 && hobbitPathLookup[k + 180][i + 180]) {
                    return LOTRRoadType.PAVED_PATH;
                }
            }
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

        public void setupHamlet(RandomSource random) {
            addStructure(new LOTRNPCRespawnerStructure(false) {

                @Override
                public void setupRespawner(LOTRNPCRespawnerEntity spawner) {
                    spawner.setSpawnClass(LOTREntities.BREE_MAN);
                    spawner.setCheckRanges(40, -12, 12, 20);
                    spawner.setSpawnRanges(20, -6, 6, 64);
                    spawner.setBlockEnemySpawnRange(60);
                }
            }, 0, 0, 0);
            addStructure(new LOTRNPCRespawnerStructure(false) {

                @Override
                public void setupRespawner(LOTRNPCRespawnerEntity spawner) {
                    spawner.setSpawnClass(LOTREntities.BREE_HOBBIT);
                    spawner.setCheckRanges(40, -12, 12, 6);
                    spawner.setSpawnRanges(20, -6, 6, 64);
                    spawner.setBlockEnemySpawnRange(60);
                }
            }, 0, 0, 0);
            addStructure(new LOTRNPCRespawnerStructure(false) {

                @Override
                public void setupRespawner(LOTRNPCRespawnerEntity spawner) {
                    spawner.setSpawnClass(LOTREntities.BREE_GUARD);
                    spawner.setCheckRanges(40, -12, 12, 8);
                    spawner.setSpawnRanges(20, -6, 6, 64);
                    spawner.setBlockEnemySpawnRange(60);
                }
            }, 0, 0, 0);
            addStructure(new LOTRBreeWellStructure(false), 0, -2, 0, true);
            int lampX = 9;
            for (int i : new int[]{-lampX, lampX}) {
                for (int k : new int[]{-lampX, lampX}) {
                    addStructure(new LOTRBreeLampPostStructure(false), i, k, 0);
                }
            }
            int rHouse = innerSize + 3;
            if (hamletHedge) {
                addStructure(getHamletHouseOrOther(random), -rHouse, 0, 1);
                addStructure(getHamletHouseOrOther(random), rHouse, 0, 3);
                addStructure(getHamletHouseOrOther(random), 0, rHouse, 0);
                int pathHouseX = 8;
                int pathHouseZ = innerSize + 16;
                addStructure(getHamletHouse(random), -pathHouseX, -pathHouseZ, 1);
                addStructure(getHamletHouse(random), pathHouseX, -pathHouseZ, 3);
            } else {
                addStructure(getHamletHouseOrOther(random), -rHouse, 0, 1);
                addStructure(getHamletHouseOrOther(random), rHouse, 0, 3);
                addStructure(getHamletHouseOrOther(random), 0, rHouse, 0);
                addStructure(getHamletHouseOrOther(random), 0, -rHouse, 2);
            }
            int hayX = innerSize + 16;
            for (int i : new int[]{-hayX, hayX}) {
                for (int k : new int[]{-hayX, hayX}) {
                    if (!random.nextBoolean()) {
                        continue;
                    }
                    addStructure(new LOTRHayBalesStructure(false), i, k, 0);
                }
            }
            LOTRBreeMarketStallStructure[] stalls = LOTRBreeMarketStallStructure.getRandomStalls(random, false, 4);
            int stallX = innerSize + 1;
            if (random.nextInt(6) == 0) {
                addStructure(stalls[0], -stallX + 3, -stallX, 1);
            }
            if (random.nextInt(6) == 0) {
                addStructure(stalls[1], stallX, -stallX + 3, 2);
            }
            if (random.nextInt(6) == 0) {
                addStructure(stalls[2], stallX - 3, stallX, 3);
            }
            if (random.nextInt(6) == 0) {
                addStructure(stalls[3], -stallX, stallX - 3, 0);
            }
            if (hamletHedge) {
                int rHedge = innerSize + 32;
                int rSq = rHedge * rHedge;
                int rMax = rHedge + 2;
                int rSqMax = rMax * rMax;
                for (int i = -rMax; i <= rMax; ++i) {
                    for (int k = -rMax; k <= rMax; ++k) {
                        int dSq;
                        if (Math.abs(i) <= 5 && k < 0 || (dSq = i * i + k * k) < rSq || dSq >= rSqMax) {
                            continue;
                        }
                        addStructure(new LOTRBreeHedgePartStructure(false), i, k, 0);
                    }
                }
            }
        }

        public void setupVillage(RandomSource random) {
            int hobbitZ;
            int l;
            addStructure(new LOTRNPCRespawnerStructure(false) {

                @Override
                public void setupRespawner(LOTRNPCRespawnerEntity spawner) {
                    spawner.setSpawnClass(LOTREntities.BREE_MAN);
                    spawner.setCheckRanges(64, -24, 24, 32);
                    spawner.setSpawnRanges(32, -6, 6, 64);
                    spawner.setBlockEnemySpawnRange(64);
                }
            }, 0, 0, 0);
            addStructure(new LOTRNPCRespawnerStructure(false) {

                @Override
                public void setupRespawner(LOTRNPCRespawnerEntity spawner) {
                    spawner.setSpawnClass(LOTREntities.BREE_MAN);
                    spawner.setCheckRanges(64, -24, 24, 32);
                    spawner.setSpawnRanges(32, -6, 6, 64);
                    spawner.setBlockEnemySpawnRange(64);
                }
            }, -120, 0, 0);
            addStructure(new LOTRNPCRespawnerStructure(false) {

                @Override
                public void setupRespawner(LOTRNPCRespawnerEntity spawner) {
                    spawner.setSpawnClass(LOTREntities.BREE_MAN);
                    spawner.setCheckRanges(64, -24, 24, 32);
                    spawner.setSpawnRanges(32, -6, 6, 64);
                    spawner.setBlockEnemySpawnRange(64);
                }
            }, 120, 0, 0);
            addStructure(new LOTRNPCRespawnerStructure(false) {

                @Override
                public void setupRespawner(LOTRNPCRespawnerEntity spawner) {
                    spawner.setSpawnClass(LOTREntities.BREE_HOBBIT);
                    spawner.setCheckRanges(64, -24, 24, 40);
                    spawner.setSpawnRanges(32, -6, 6, 64);
                    spawner.setBlockEnemySpawnRange(64);
                }
            }, 0, 80, 0);
            addStructure(new LOTRNPCRespawnerStructure(false) {

                @Override
                public void setupRespawner(LOTRNPCRespawnerEntity spawner) {
                    spawner.setSpawnClass(LOTREntities.BREE_GUARD);
                    spawner.setCheckRanges(64, -24, 24, 8);
                    spawner.setSpawnRanges(32, -6, 6, 64);
                    spawner.setBlockEnemySpawnRange(64);
                }
            }, 0, 0, 0);
            addStructure(new LOTRNPCRespawnerStructure(false) {

                @Override
                public void setupRespawner(LOTRNPCRespawnerEntity spawner) {
                    spawner.setSpawnClass(LOTREntities.BREE_GUARD);
                    spawner.setCheckRanges(64, -24, 24, 8);
                    spawner.setSpawnRanges(32, -6, 6, 64);
                    spawner.setBlockEnemySpawnRange(64);
                }
            }, -120, 0, 0);
            addStructure(new LOTRNPCRespawnerStructure(false) {

                @Override
                public void setupRespawner(LOTRNPCRespawnerEntity spawner) {
                    spawner.setSpawnClass(LOTREntities.BREE_GUARD);
                    spawner.setCheckRanges(64, -24, 24, 8);
                    spawner.setSpawnRanges(32, -6, 6, 64);
                    spawner.setBlockEnemySpawnRange(64);
                }
            }, 120, 0, 0);
            addStructure(new LOTRNPCRespawnerStructure(false) {

                @Override
                public void setupRespawner(LOTRNPCRespawnerEntity spawner) {
                    spawner.setSpawnClass(LOTREntities.BREE_GUARD);
                    spawner.setCheckRanges(64, -24, 24, 8);
                    spawner.setSpawnRanges(32, -6, 6, 64);
                    spawner.setBlockEnemySpawnRange(64);
                }
            }, 0, -120, 0);
            LOTRBreeInnStructure inn = new LOTRBreeInnStructure(false);
            if (locationInfo.getAssociatedWaypoint() == LOTRWaypoint.BREE) {
                inn.setPresets(new String[]{"The Prancing", "Pony"}, "Barliman Butterbur", true, false);
            }
            addStructure(inn, 15, 8, 0, true);
            addStructure(new LOTRBreeOfficeStructure(false), -15, 8, 0, true);
            int houses = 9;
            for (int i1 = -houses; i1 <= houses; ++i1) {
                int houseX = i1 * 18;
                int houseZ = 5;
                LOTRBreeStructure house1 = new LOTRBreeHouseStructure(false);
                LOTRBreeStructure house2 = new LOTRBreeHouseStructure(false);
                boolean forceHouse1 = false;
                boolean forceHouse2 = false;
                if (i1 <= -houses + 2 || i1 == houses) {
                    house1 = new LOTRBreeRuffianHouseStructure(false);
                    house2 = new LOTRBreeRuffianHouseStructure(false);
                    if (locationInfo.getAssociatedWaypoint() == LOTRWaypoint.BREE && i1 == -houses) {
                        house1 = new LOTRBreeRuffianHouseStructure(false).setRuffianName("Bill Ferny");
                        forceHouse1 = true;
                    }
                }
                if (Math.abs(i1) < 2) {
                    continue;
                }
                addStructure(house1, houseX, houseZ, 0, forceHouse1);
                if (Math.abs(i1) == 4) {
                    addStructure(new LOTRBreeSmithyStructure(false), houseX, -houseZ, 2);
                } else {
                    addStructure(house2, houseX, -houseZ, 2, forceHouse2);
                }
                int lampX = houseX - Integer.signum(i1) * 9;
                int lampZ = houseZ - 1;
                addStructure(new LOTRBreeLampPostStructure(false), lampX, lampZ, 0);
                addStructure(new LOTRBreeLampPostStructure(false), lampX, -lampZ, 2);
            }
            LOTRBreeMarketStallStructure[] stalls = LOTRBreeMarketStallStructure.getRandomStalls(random, false, 8);
            LOTRBreeMarketStructure market1 = new LOTRBreeMarketStructure(false).setFrontStepsOnly(true);
            LOTRBreeMarketStructure market2 = new LOTRBreeMarketStructure(false).setFrontStepsOnly(true);
            market1.setStalls(stalls[0], stalls[1], stalls[2], stalls[3]);
            market2.setStalls(stalls[4], stalls[5], stalls[6], stalls[7]);
            addStructure(market1, -15, -3, 2, true);
            addStructure(market2, 15, -3, 2, true);
            addStructure(new LOTRBreeWellStructure(false), 5, -32, 3, true);
            addStructure(new LOTRBreeWellStructure(false), -5, -32, 1, true);
            addStructure(new LOTRBreeGardenStructure(false), 6, -42, 3, true);
            addStructure(new LOTRBreeGardenStructure(false), -6, -42, 1, true);
            for (int i1 = 0; i1 <= 5; ++i1) {
                int houseX = 5;
                int houseZ = -64 - i1 * 18;
                if (i1 != 2) {
                    addStructure(new LOTRBreeHouseStructure(false), houseX, houseZ, 3);
                    addStructure(new LOTRBreeHouseStructure(false), -houseX, houseZ, 1);
                }
                int lampX = houseX - 1;
                int lampZ = houseZ - 9;
                addStructure(new LOTRBreeLampPostStructure(false), lampX, lampZ, 3);
                addStructure(new LOTRBreeLampPostStructure(false), -lampX, lampZ, 1);
            }
            addStructure(new LOTRBreeBarnStructure(false), -72, -100, 1, true);
            addStructure(new LOTRBreeBarnStructure(false), 72, -100, 3, true);
            addStructure(new LOTRBreeStableStructure(false), -40, -106, 2, true);
            addStructure(new LOTRBreeStableStructure(false), 40, -106, 2, true);
            addStructure(new LOTRBreeWellStructure(false), -40, -94, 0, true);
            addStructure(new LOTRBreeWellStructure(false), 40, -94, 0, true);
            addStructure(new LOTRBreeWellStructure(false), 5, 28, 3, true);
            addStructure(new LOTRBreeWellStructure(false), -5, 28, 1, true);
            addStructure(new LOTRBreeGardenStructure(false), 6, 38, 3, true);
            addStructure(new LOTRBreeGardenStructure(false), -6, 38, 1, true);
            for (l = 0; l < hobbitBurrowPathPoints.length; l += 2) {
                int hobbitX = hobbitBurrowPathPoints[l];
                hobbitZ = hobbitBurrowPathPoints[l + 1];
                addStructure(new LOTRBreeHobbitBurrowStructure(false), hobbitX, hobbitZ + 6, 0, true);
            }
            for (l = 0; l < hobbitBurrowEndPoints.length; l += 2) {
                int hobbitX = hobbitBurrowEndPoints[l];
                hobbitZ = hobbitBurrowEndPoints[l + 1];
                if (Integer.signum(hobbitX) == -1) {
                    addStructure(new LOTRBreeHobbitBurrowStructure(false), hobbitX - 6, hobbitZ, 1, true);
                    continue;
                }
                addStructure(new LOTRBreeHobbitBurrowStructure(false), hobbitX + 6, hobbitZ, 3, true);
            }
            addStructure(new LOTRBreeGateStructure(false), -182, 0, 3, true);
            addStructure(new LOTRBreeGateStructure(false), 182, 0, 1, true);
            addStructure(new LOTRBreeGatehouseStructure(false).setName(locationInfo.name), 0, -182, 0, true);
            int rHedge = 180;
            int rHedgeSq = rHedge * rHedge;
            int rHedgeMax = rHedge + 3;
            int rHedgeSqMax = rHedgeMax * rHedgeMax;
            for (int i = -rHedgeMax; i <= rHedgeMax; ++i) {
                for (int k = -rHedgeMax; k <= rHedgeMax; ++k) {
                    int dSq = i * i + k * k;
                    if (dSq < rHedgeSq || dSq >= rHedgeSqMax) {
                        continue;
                    }
                    int i1 = Math.abs(i);
                    int k1 = Math.abs(k);
                    if (i1 <= 192 && k1 <= 4 || k >= -192 && k <= 50 && i1 <= 4) {
                        continue;
                    }
                    addStructure(new LOTRBreeHedgePartStructure(false), i, k, 0);
                }
            }
        }

        @Override
        public void setupVillageProperties(RandomSource random) {
            if (locationInfo.isFixedLocation()) {
                villageType = VillageType.VILLAGE;
            } else {
                villageType = VillageType.HAMLET;
                innerSize = Mth.randomBetweenInclusive(random, 12, 14);
                hamletHedge = random.nextBoolean();
            }
        }

    }

}
