package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.village;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRNPCRespawnerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNames;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRRoadType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRNPCRespawnerStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.hobbit.LOTRHayBalesStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.rohan.LOTRMeadHallStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.rohan.LOTRRohanBarnStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.rohan.LOTRRohanFortCornerStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.rohan.LOTRRohanFortWallStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.rohan.LOTRRohanFortressStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.rohan.LOTRRohanGatehouseStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.rohan.LOTRRohanHouseStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.rohan.LOTRRohanMarketStallStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.rohan.LOTRRohanSmithyStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.rohan.LOTRRohanStablesStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.rohan.LOTRRohanVillageFarmStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.rohan.LOTRRohanVillageGardenStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.rohan.LOTRRohanVillagePalisadeStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.rohan.LOTRRohanVillagePastureStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.rohan.LOTRRohanVillageSignStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.rohan.LOTRRohanWatchtowerStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.rohan.LOTRRohanWellStructure;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRVillageGenRohan extends LOTRVillageGen {
    public LOTRVillageGenRohan(String biome, float f) {
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
        VILLAGE, FORT

    }

    public static class Instance extends LOTRVillageGen.AbstractInstance<LOTRVillageGenRohan> {
        public VillageType villageType;
        public String[] villageName;
        public boolean palisade;

        public Instance(LOTRVillageGenRohan village, long worldSeed, int i, int k, RandomSource random, LocationInfo loc) {
            super(village, worldSeed, i, k, random, loc);
        }

        @Override
        public void addVillageStructures(RandomSource random) {
            if (villageType == VillageType.VILLAGE) {
                setupVillage(random);
            } else if (villageType == VillageType.FORT) {
                setupFort(random);
            }
        }

        @Override
        public LOTRRoadType getPath(RandomSource random, int i, int k) {
            int i1 = Math.abs(i);
            int k1 = Math.abs(k);
            if (villageType == VillageType.VILLAGE) {
                int dSq = i * i + k * k;
                int imn = 20 + random.nextInt(4);
                if (dSq < imn * imn) {
                    return LOTRRoadType.PATH;
                }
                int omn = 50 - random.nextInt(4);
                int omx = 56 + random.nextInt(4);
                if (dSq > omn * omn && dSq < omx * omx || dSq < 2500 && Math.abs(i1 - k1) <= 2 + random.nextInt(4)) {
                    return LOTRRoadType.PATH;
                }
                if (palisade && k < -56 && k > -81 && i1 <= 2 + random.nextInt(4)) {
                    return LOTRRoadType.PATH;
                }
            }
            if (villageType == VillageType.FORT) {
                if (k <= -14 && k >= -49 && i1 <= 2) {
                    return LOTRRoadType.ROHAN;
                }
                if (k <= -14 && k >= -17 && i1 <= 37) {
                    return LOTRRoadType.PATH;
                }
                if (k >= -14 && k <= 20 && i1 >= 19 && i1 <= 22) {
                    return LOTRRoadType.PATH;
                }
                if (k >= 20 && k <= 23 && i1 <= 37) {
                    return LOTRRoadType.PATH;
                }
            }
            return null;
        }

        public LOTRStructureBase2 getRandomFarm(RandomSource random) {
            if (random.nextInt(3) == 0) {
                return new LOTRRohanVillagePastureStructure(false);
            }
            return new LOTRRohanVillageFarmStructure(false);
        }

        public LOTRStructureBase2 getRandomHouse(RandomSource random) {
            if (random.nextInt(4) == 0) {
                int i = random.nextInt(3);
                switch (i) {
                    case 0:
                        return new LOTRRohanSmithyStructure(false);
                    case 1:
                        return new LOTRRohanStablesStructure(false);
                    case 2:
                        return new LOTRRohanBarnStructure(false);
                    default:
                        break;
                }
            }
            return new LOTRRohanHouseStructure(false);
        }

        @Override
        public boolean isFlat() {
            return false;
        }

        @Override
        public boolean isVillageSpecificSurface(WorldGenLevel world, int i, int j, int k) {
            return false;
        }

        public void setupFort(RandomSource random) {
            int farmX;
            int wallX;
            int l;
            int l2;
            int wallZ;
            addStructure(new LOTRRohanFortressStructure(false), 0, -13, 0, true);
            addStructure(new LOTRNPCRespawnerStructure(false) {

                @Override
                public void setupRespawner(LOTRNPCRespawnerEntity spawner) {
                    spawner.setSpawnClasses(LOTREntities.ROHIRRIM_WARRIOR, LOTREntities.ROHIRRIM_ARCHER);
                    spawner.setCheckRanges(40, -12, 12, 30);
                    spawner.setSpawnRanges(32, -6, 6, 64);
                    spawner.setBlockEnemySpawnRange(60);
                }
            }, 0, 0, 0);
            addStructure(new LOTRRohanGatehouseStructure(false), 0, -53, 0, true);
            int towerX = 46;
            for (int i1 : new int[]{-towerX, towerX}) {
                addStructure(new LOTRRohanWatchtowerStructure(false), i1, -towerX, 0, true);
                addStructure(new LOTRRohanWatchtowerStructure(false), i1, towerX, 2, true);
            }
            for (int i1 : new int[]{-35, 35}) {
                addStructure(new LOTRRohanStablesStructure(false), i1, -14, 0, true);
            }
            int farmZ = -20;
            for (l = 0; l <= 1; ++l) {
                farmX = 30 - l * 12;
                addStructure(new LOTRRohanVillageFarmStructure(false), -farmX, farmZ, 2);
                addStructure(new LOTRRohanVillageFarmStructure(false), farmX, farmZ, 2);
            }
            farmZ = 26;
            for (l = -2; l <= 2; ++l) {
                farmX = l * 12;
                addStructure(new LOTRRohanVillageFarmStructure(false), -farmX, farmZ, 0);
                addStructure(new LOTRRohanVillageFarmStructure(false), farmX, farmZ, 0);
            }
            for (int i1 : new int[]{-51, 51}) {
                for (int k1 : new int[]{-51, 51}) {
                    addStructure(new LOTRRohanFortCornerStructure(false), i1, k1, 0, true);
                }
            }
            for (l2 = 0; l2 <= 4; ++l2) {
                wallX = 13 + l2 * 8;
                wallZ = -51;
                addStructure(new LOTRRohanFortWallStructure(false, -3, 4), -wallX, wallZ, 0, true);
                addStructure(new LOTRRohanFortWallStructure(false, -4, 3), wallX, wallZ, 0, true);
            }
            for (l2 = -5; l2 <= 5; ++l2) {
                wallX = l2 * 9;
                wallZ = 51;
                addStructure(new LOTRRohanFortWallStructure(false), wallX, wallZ, 2, true);
                addStructure(new LOTRRohanFortWallStructure(false), -wallZ, wallX, 3, true);
                addStructure(new LOTRRohanFortWallStructure(false), wallZ, wallX, 1, true);
            }
        }

        public void setupVillage(RandomSource random) {
            addStructure(new LOTRMeadHallStructure(false), 0, 2, 0, true);
            addStructure(new LOTRNPCRespawnerStructure(false) {

                @Override
                public void setupRespawner(LOTRNPCRespawnerEntity spawner) {
                    spawner.setSpawnClass(LOTREntities.ROHAN_MAN);
                    spawner.setCheckRanges(40, -12, 12, 40);
                    spawner.setSpawnRanges(20, -6, 6, 64);
                    spawner.setBlockEnemySpawnRange(60);
                }
            }, 0, 0, 0);
            addStructure(new LOTRNPCRespawnerStructure(false) {

                @Override
                public void setupRespawner(LOTRNPCRespawnerEntity spawner) {
                    spawner.setSpawnClass(LOTREntities.ROHIRRIM_WARRIOR);
                    spawner.setCheckRanges(40, -12, 12, 16);
                    spawner.setSpawnRanges(20, -6, 6, 64);
                    spawner.setBlockEnemySpawnRange(60);
                }
            }, 0, 0, 0);
            int houses = 20;
            float frac = 1.0f / houses;
            float turn = 0.0f;
            while (turn < 1.0f) {
                int k;
                int l;
                int i;
                float turnR = (float) Math.toRadians((turn += frac) * 360.0f);
                float sin = Mth.sin(turnR);
                float cos = Mth.cos(turnR);
                int r = 0;
                float turn8 = turn * 8.0f;
                if (turn8 >= 1.0f && turn8 < 3.0f) {
                    r = 0;
                } else if (turn8 >= 3.0f && turn8 < 5.0f) {
                    r = 1;
                } else if (turn8 >= 5.0f && turn8 < 7.0f) {
                    r = 2;
                } else if (turn8 >= 7.0f || turn8 < 1.0f) {
                    r = 3;
                }
                if (palisade && sin < 0.0f && Math.abs(cos) <= 0.25f) {
                    continue;
                }
                if (random.nextBoolean()) {
                    l = 57;
                    i = Math.round(l * cos);
                    k = Math.round(l * sin);
                    addStructure(getRandomHouse(random), i, k, r);
                    continue;
                }
                if (random.nextInt(3) == 0) {
                    continue;
                }
                l = 61;
                i = Math.round(l * cos);
                k = Math.round(l * sin);
                addStructure(new LOTRHayBalesStructure(false), i, k, r);
            }
            int farmX = 25;
            for (int k = -1; k <= 1; ++k) {
                int farmZ = k * 14;
                addStructure(getRandomFarm(random), -farmX, farmZ, 1);
                addStructure(getRandomFarm(random), farmX, farmZ, 3);
            }
            int gardenX = 14;
            for (int k = 0; k <= 2; ++k) {
                int gardenZ = 24 + k * 8;
                addStructure(new LOTRRohanVillageGardenStructure(false), -gardenX, gardenZ, 3);
                addStructure(new LOTRRohanVillageGardenStructure(false), gardenX, gardenZ, 1);
            }
            int gardenZ = 41;
            for (int i = -1; i <= 1; ++i) {
                gardenX = i * 6;
                if (i == 0) {
                    continue;
                }
                addStructure(new LOTRRohanVillageGardenStructure(false), gardenX, gardenZ, 0);
            }
            addStructure(new LOTRRohanWellStructure(false), 0, -23, 2, true);
            addStructure(new LOTRRohanVillageSignStructure(false).setSignText(villageName), 0, -11, 0, true);
            if (random.nextBoolean()) {
                int marketX = 8;
                for (int k = 0; k <= 1; ++k) {
                    int marketZ = 25 + k * 10;
                    if (random.nextBoolean()) {
                        addStructure(LOTRRohanMarketStallStructure.getRandomStall(random, false), -marketX, -marketZ, 1);
                    }
                    if (!random.nextBoolean()) {
                        continue;
                    }
                    addStructure(LOTRRohanMarketStallStructure.getRandomStall(random, false), marketX, -marketZ, 3);
                }
            }
            if (palisade) {
                int rPalisade = 81;
                int rSq = rPalisade * rPalisade;
                int rMax = rPalisade + 1;
                int rSqMax = rMax * rMax;
                for (int i = -rPalisade; i <= rPalisade; ++i) {
                    for (int k = -rPalisade; k <= rPalisade; ++k) {
                        int dSq;
                        if (Math.abs(i) <= 9 && k < 0 || (dSq = i * i + k * k) < rSq || dSq >= rSqMax) {
                            continue;
                        }
                        addStructure(new LOTRRohanVillagePalisadeStructure(false), i, k, 0);
                    }
                }
                addStructure(new LOTRRohanGatehouseStructure(false), 0, -rPalisade - 2, 0);
            }
        }

        @Override
        public void setupVillageProperties(RandomSource random) {
            villageName = LOTRNames.getRohanVillageName(random);
            villageType = random.nextInt(3) == 0 ? VillageType.FORT : VillageType.VILLAGE;
            palisade = random.nextBoolean();
        }

    }

}
