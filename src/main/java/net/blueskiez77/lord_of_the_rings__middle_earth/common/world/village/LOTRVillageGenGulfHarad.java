package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.village;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRNPCRespawnerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNames;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRRoadType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRNPCRespawnerStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRGulfAltarStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRGulfBazaarStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRGulfFarmStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRGulfHouseStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRGulfPastureStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRGulfPyramidStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRGulfSmithyStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRGulfTavernStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRGulfTotemStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRGulfTowerStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRGulfTownWallStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRGulfVillageLightStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRGulfVillageSignStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRGulfWarCampStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.hobbit.LOTRHayBalesStructure;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRVillageGenGulfHarad extends LOTRVillageGen {
    public LOTRVillageGenGulfHarad(String biome, float f) {
        super(biome);
        gridScale = 14;
        gridRandomDisplace = 1;
        spawnChance = f;
        villageChunkRadius = 6;
    }

    @Override
    public LOTRVillageGen.AbstractInstance<?> createVillageInstance(WorldGenLevel world, int i, int k, RandomSource random, LocationInfo loc) {
        return new Instance(this, world, i, k, random, loc);
    }

    public enum VillageType {
        VILLAGE, TOWN, FORT

    }

    public static class Instance extends LOTRVillageGen.AbstractInstance<LOTRVillageGenGulfHarad> {
        public VillageType villageType;
        public String[] villageName;
        public int numOuterHouses;
        public boolean townWall = true;
        public int rTownTower = 90;

        public Instance(LOTRVillageGenGulfHarad village, WorldGenLevel world, int i, int k, RandomSource random, LocationInfo loc) {
            super(village, world, i, k, random, loc);
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

        @Override
        public LOTRRoadType getPath(RandomSource random, int i, int k) {
            int dSq;
            int i1 = Math.abs(i);
            int k1 = Math.abs(k);
            if (villageType == VillageType.VILLAGE) {
                dSq = i * i + k * k;
                int imn = 16 - random.nextInt(3);
                int imx = 21 + random.nextInt(3);
                if (dSq > imn * imn && dSq < imx * imx) {
                    return LOTRRoadType.PATH;
                }
            }
            if (villageType == VillageType.TOWN) {
                dSq = i * i + k * k;
                if (dSq < 576) {
                    return LOTRRoadType.GULF_HARAD;
                }
                if (k1 <= 3 && i1 <= 74 || i1 <= 3 && k <= 74) {
                    return LOTRRoadType.GULF_HARAD;
                }
            }
            return null;
        }

        public LOTRStructureBase2 getRandomFarm(RandomSource random) {
            if (random.nextBoolean()) {
                return new LOTRGulfFarmStructure(false);
            }
            return new LOTRGulfPastureStructure(false);
        }

        public LOTRStructureBase2 getRandomHouse(RandomSource random) {
            if (random.nextInt(5) == 0) {
                return new LOTRGulfSmithyStructure(false);
            }
            return new LOTRGulfHouseStructure(false);
        }

        @Override
        public boolean isFlat() {
            return false;
        }

        @Override
        public boolean isVillageSpecificSurface(WorldGenLevel world, int i, int j, int k) {
            if (villageType == VillageType.TOWN) {
                BlockState block = world.getBlockState(new BlockPos(i, j, k));
                return is(block, "brick3", 13) || is(block, "brick3", 14);
            }
            return false;
        }

        public void setupFort(RandomSource random) {
            addStructure(new LOTRNPCRespawnerStructure(false) {

                @Override
                public void setupRespawner(LOTRNPCRespawnerEntity spawner) {
                    spawner.setSpawnClass(LOTREntities.GULF_HARADRIM);
                    spawner.setCheckRanges(40, -12, 12, 16);
                    spawner.setSpawnRanges(24, -6, 6, 32);
                    spawner.setBlockEnemySpawnRange(60);
                }
            }, 0, 0, 0);
            addStructure(new LOTRGulfWarCampStructure(false), 0, -15, 0, true);
            int towerX = 36;
            addStructure(new LOTRGulfTowerStructure(false), -towerX, -towerX + 4, 2, true);
            addStructure(new LOTRGulfTowerStructure(false), towerX, -towerX + 4, 2, true);
            addStructure(new LOTRGulfTowerStructure(false), -towerX, towerX - 4, 0, true);
            addStructure(new LOTRGulfTowerStructure(false), towerX, towerX - 4, 0, true);
            for (int l = -1; l <= 1; ++l) {
                int i = l * 16;
                int k = 28;
                addStructure(getRandomFarm(random), i, k, 0);
                addStructure(getRandomFarm(random), -k, i, 1);
                addStructure(getRandomFarm(random), k, i, 3);
            }
        }

        public void setupTown(RandomSource random) {
            addStructure(new LOTRNPCRespawnerStructure(false) {

                @Override
                public void setupRespawner(LOTRNPCRespawnerEntity spawner) {
                    spawner.setSpawnClass(LOTREntities.GULF_HARADRIM);
                    spawner.setCheckRanges(80, -12, 12, 100);
                    spawner.setSpawnRanges(40, -6, 6, 64);
                    spawner.setBlockEnemySpawnRange(60);
                }
            }, 0, 0, 0);
            for (int i1 : new int[]{-40, 40}) {
                for (int k1 : new int[]{-40, 40}) {
                    addStructure(new LOTRNPCRespawnerStructure(false) {

                        @Override
                        public void setupRespawner(LOTRNPCRespawnerEntity spawner) {
                            spawner.setSpawnClasses(LOTREntities.GULF_WARRIOR, LOTREntities.GULF_ARCHER);
                            spawner.setCheckRanges(64, -12, 12, 20);
                            spawner.setSpawnRanges(20, -6, 6, 64);
                            spawner.setBlockEnemySpawnRange(64);
                        }
                    }, i1, k1, 0);
                }
            }
            addStructure(new LOTRGulfPyramidStructure(false), 0, -11, 0, true);
            int lightR = 15;
            addStructure(new LOTRGulfVillageLightStructure(false), -lightR, -lightR, 0, true);
            addStructure(new LOTRGulfVillageLightStructure(false), lightR, -lightR, 0, true);
            addStructure(new LOTRGulfVillageLightStructure(false), -lightR, lightR, 0, true);
            addStructure(new LOTRGulfVillageLightStructure(false), lightR, lightR, 0, true);
            addStructure(new LOTRGulfBazaarStructure(false), -74, 0, 1, true);
            addStructure(new LOTRGulfAltarStructure(false), 74, 0, 3, true);
            addStructure(new LOTRGulfTotemStructure(false), 0, 79, 0, true);
            for (int l = 0; l <= 2; ++l) {
                int i = 5;
                int k = 32 + l * 20;
                addStructure(new LOTRGulfHouseStructure(false), -i, -k, 1, true);
                addStructure(new LOTRGulfHouseStructure(false), i, -k, 3, true);
                addStructure(new LOTRGulfHouseStructure(false), -i, k, 1, true);
                addStructure(new LOTRGulfHouseStructure(false), i, k, 3, true);
                addStructure(new LOTRGulfHouseStructure(false), k, -i, 2, true);
                addStructure(new LOTRGulfHouseStructure(false), k, i, 0, true);
                if (l != 0) {
                    continue;
                }
                addStructure(new LOTRGulfSmithyStructure(false), -k - 6, -i, 2, true);
                addStructure(new LOTRGulfTavernStructure(false), -k - 6, i, 0, true);
            }
            int xzTownTower = (int) (rTownTower / 1.4142135623730951);
            addStructure(new LOTRGulfTowerStructure(false), -xzTownTower, -xzTownTower + 4, 2, true);
            addStructure(new LOTRGulfTowerStructure(false), xzTownTower, -xzTownTower + 4, 2, true);
            addStructure(new LOTRGulfTowerStructure(false), -xzTownTower, xzTownTower - 4, 0, true);
            addStructure(new LOTRGulfTowerStructure(false), xzTownTower, xzTownTower - 4, 0, true);
            int turn = 0;
            int numTurns = 24;
            while (turn <= numTurns) {
                turn++;
                if (turn % 3 == 0) {
                    continue;
                }
                float turnF = (float) turn / numTurns;
                float turnR = (float) Math.toRadians(turnF * 360.0f);
                float sin = Mth.sin(turnR);
                float cos = Mth.cos(turnR);
                int r = 0;
                float turn8 = turnF * 8.0f;
                if (turn8 >= 1.0f && turn8 < 3.0f) {
                    r = 0;
                } else if (turn8 >= 3.0f && turn8 < 5.0f) {
                    r = 1;
                } else if (turn8 >= 5.0f && turn8 < 7.0f) {
                    r = 2;
                } else if (turn8 >= 7.0f || turn8 < 1.0f) {
                    r = 3;
                }
                int l = rTownTower - 6;
                int i = Math.round(l * cos);
                int k = Math.round(l * sin);
                if (random.nextInt(3) == 0) {
                    addStructure(new LOTRHayBalesStructure(false), i, k, r);
                    continue;
                }
                addStructure(getRandomFarm(random), i, k, r);
            }
            addStructure(new LOTRGulfVillageSignStructure(false).setSignText(villageName), -5, -96, 0, true);
            addStructure(new LOTRGulfVillageSignStructure(false).setSignText(villageName), 5, -96, 0, true);
            if (townWall) {
                int rSq = 9604;
                int rMax = 99;
                int rSqMax = rMax * rMax;
                for (int i = -98; i <= 98; ++i) {
                    for (int k = -98; k <= 98; ++k) {
                        int dSq;
                        int i1 = Math.abs(i);
                        if (i1 <= 6 && k < 0 || (dSq = i * i + k * k) < rSq || dSq >= rSqMax) {
                            continue;
                        }
                        LOTRGulfTownWallStructure wall = new LOTRGulfTownWallStructure(false);
                        if (i1 == 7 && k < 0) {
                            wall.setTall();
                        }
                        addStructure(wall, i, k, 0);
                    }
                }
            }
        }

        public void setupVillage(RandomSource random) {
            addStructure(new LOTRNPCRespawnerStructure(false) {

                @Override
                public void setupRespawner(LOTRNPCRespawnerEntity spawner) {
                    spawner.setSpawnClass(LOTREntities.GULF_HARADRIM);
                    spawner.setCheckRanges(64, -12, 12, 24);
                    spawner.setSpawnRanges(32, -6, 6, 32);
                    spawner.setBlockEnemySpawnRange(64);
                }
            }, 0, 0, 0);
            addStructure(new LOTRNPCRespawnerStructure(false) {

                @Override
                public void setupRespawner(LOTRNPCRespawnerEntity spawner) {
                    spawner.setSpawnClasses(LOTREntities.GULF_WARRIOR, LOTREntities.GULF_ARCHER);
                    spawner.setCheckRanges(64, -12, 12, 12);
                    spawner.setSpawnRanges(32, -6, 6, 32);
                    spawner.setBlockEnemySpawnRange(64);
                }
            }, 0, 0, 0);
            addStructure(new LOTRGulfTotemStructure(false), 0, -2, 0, true);
            addStructure(new LOTRGulfTavernStructure(false), 0, 24, 0, true);
            int rSignsInner = 11;
            addStructure(new LOTRGulfVillageSignStructure(false).setSignText(villageName), -rSignsInner, 0, 1, true);
            addStructure(new LOTRGulfVillageSignStructure(false).setSignText(villageName), rSignsInner, 0, 3, true);
            for (int h = 0; h < numOuterHouses; ++h) {
                float turn = (float) h / (numOuterHouses - 1);
                float turnMin = 0.15f;
                float turnMax = 1.0f - turnMin;
                float turnInRange = turnMin + (turnMax - turnMin) * turn;
                float turnCorrected = (turnInRange + 0.25f) % 1.0f;
                float turnR = (float) Math.toRadians(turnCorrected * 360.0f);
                float sin = Mth.sin(turnR);
                float cos = Mth.cos(turnR);
                int r = 0;
                float turn8 = turnCorrected * 8.0f;
                if (turn8 >= 1.0f && turn8 < 3.0f) {
                    r = 0;
                } else if (turn8 >= 3.0f && turn8 < 5.0f) {
                    r = 1;
                } else if (turn8 >= 5.0f && turn8 < 7.0f) {
                    r = 2;
                } else if (turn8 >= 7.0f || turn8 < 1.0f) {
                    r = 3;
                }
                int l = 24;
                int i = Math.round(l * cos);
                int k = Math.round(l * sin);
                addStructure(getRandomHouse(random), i, k, r);
            }
            int numFarms = numOuterHouses * 2;
            float frac = 1.0f / numFarms;
            float turn = 0.0f;
            while (turn < 1.0f) {
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
                int l = 52;
                int i = Math.round(l * cos);
                int k = Math.round(l * sin);
                if (random.nextInt(3) == 0) {
                    addStructure(new LOTRHayBalesStructure(false), i, k, r);
                    continue;
                }
                addStructure(getRandomFarm(random), i, k, r);
            }
        }

        @Override
        public void setupVillageProperties(RandomSource random) {
            villageType = random.nextInt(4) == 0 ? VillageType.FORT : random.nextInt(3) == 0 ? VillageType.TOWN : VillageType.VILLAGE;
            villageName = LOTRNames.getHaradVillageName(random);
            numOuterHouses = Mth.randomBetweenInclusive(random, 5, 8);
        }

    }

}
