package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.village;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRNPCRespawnerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNames;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRRoadType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRNPCRespawnerStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRHarnedorFarmStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRHarnedorFortStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRHarnedorHouseRuinedStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRHarnedorHouseStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRHarnedorMarketStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRHarnedorPalisadeRuinedStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRHarnedorPalisadeStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRHarnedorPastureStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRHarnedorSmithyStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRHarnedorStablesStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRHarnedorTavernRuinedStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRHarnedorTavernStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRHarnedorTowerStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRHarnedorVillageSignStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRNearHaradTentStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.hobbit.LOTRHayBalesStructure;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRVillageGenHarnedor extends LOTRVillageGen {
    public boolean isRuinedVillage;

    public LOTRVillageGenHarnedor(String biome, float f) {
        super(biome);
        gridScale = 12;
        gridRandomDisplace = 1;
        spawnChance = f;
        villageChunkRadius = 4;
    }

    @Override
    public LOTRVillageGen.AbstractInstance<?> createVillageInstance(WorldGenLevel world, int i, int k, RandomSource random, LocationInfo loc) {
        return new Instance(this, world, i, k, random, loc);
    }

    public LOTRVillageGenHarnedor setRuined() {
        isRuinedVillage = true;
        return this;
    }

    public enum VillageType {
        VILLAGE, FORTRESS

    }

    public static class Instance extends LOTRVillageGen.AbstractInstance<LOTRVillageGenHarnedor> {
        public VillageType villageType;
        public boolean isRuined;
        public String[] villageName;
        public int numOuterHouses;
        public boolean palisade;

        public Instance(LOTRVillageGenHarnedor village, WorldGenLevel world, int i, int k, RandomSource random, LocationInfo loc) {
            super(village, world, i, k, random, loc);
            isRuined = village.isRuinedVillage;
        }

        @Override
        public void addVillageStructures(RandomSource random) {
            if (villageType == VillageType.VILLAGE) {
                setupVillage(random);
            } else {
                setupFortress(random);
            }
        }

        @Override
        public LOTRRoadType getPath(RandomSource random, int i, int k) {
            int i1 = Math.abs(i);
            if (villageType == VillageType.VILLAGE) {
                if (isRuined && random.nextInt(4) == 0) {
                    return null;
                }
                int dSq = i * i + k * k;
                int imn = 17 - random.nextInt(3);
                int imx = 22 + random.nextInt(3);
                if (dSq > imn * imn && dSq < imx * imx) {
                    return LOTRRoadType.PATH;
                }
                if (palisade && k <= -imx && k >= -66 && i1 < 2 + random.nextInt(3)) {
                    return LOTRRoadType.PATH;
                }
            }
            return null;
        }

        public LOTRStructureBase2 getRandomHouse(RandomSource random) {
            if (isRuined) {
                return new LOTRHarnedorHouseRuinedStructure(false);
            }
            if (random.nextInt(5) == 0) {
                return new LOTRHarnedorSmithyStructure(false);
            }
            if (random.nextInt(4) == 0) {
                return new LOTRHarnedorStablesStructure(false);
            }
            return new LOTRHarnedorHouseStructure(false);
        }

        @Override
        public boolean isFlat() {
            return false;
        }

        @Override
        public boolean isVillageSpecificSurface(WorldGenLevel world, int i, int j, int k) {
            return false;
        }

        public void setupFortress(RandomSource random) {
            addStructure(new LOTRNPCRespawnerStructure(false) {

                @Override
                public void setupRespawner(LOTRNPCRespawnerEntity spawner) {
                    spawner.setSpawnClass(LOTREntities.HARNEDHRIM);
                    spawner.setCheckRanges(64, -12, 12, 16);
                    spawner.setSpawnRanges(24, -6, 6, 32);
                    spawner.setBlockEnemySpawnRange(50);
                }
            }, 0, 0, 0);
            addStructure(new LOTRHarnedorFortStructure(false), 0, -12, 0, true);
            addStructure(new LOTRHarnedorTowerStructure(false), -24, -24, 0, true);
            addStructure(new LOTRHarnedorTowerStructure(false), 24, -24, 0, true);
            addStructure(new LOTRHarnedorTowerStructure(false), -24, 24, 2, true);
            addStructure(new LOTRHarnedorTowerStructure(false), 24, 24, 2, true);
            for (int l = -1; l <= 1; ++l) {
                int k = l * 10;
                int i = 24;
                addStructure(new LOTRNearHaradTentStructure(false), -i, k, 1, true);
                addStructure(new LOTRNearHaradTentStructure(false), i, k, 3, true);
            }
            int rSq = 1764;
            int rMax = 43;
            int rSqMax = rMax * rMax;
            for (int i = -42; i <= 42; ++i) {
                for (int k = -42; k <= 42; ++k) {
                    int dSq;
                    int i1 = Math.abs(i);
                    if (i1 <= 4 && k < 0 || (dSq = i * i + k * k) < rSq || dSq >= rSqMax) {
                        continue;
                    }
                    LOTRHarnedorPalisadeStructure palisade = new LOTRHarnedorPalisadeStructure(false);
                    if (i1 == 5 && k < 0) {
                        palisade.setTall();
                    }
                    addStructure(palisade, i, k, 0);
                }
            }
        }

        public void setupVillage(RandomSource random) {
            if (!isRuined) {
                addStructure(new LOTRNPCRespawnerStructure(false) {

                    @Override
                    public void setupRespawner(LOTRNPCRespawnerEntity spawner) {
                        spawner.setSpawnClass(LOTREntities.HARNEDHRIM);
                        spawner.setCheckRanges(64, -12, 12, 24);
                        spawner.setSpawnRanges(32, -6, 6, 32);
                        spawner.setBlockEnemySpawnRange(64);
                    }
                }, 0, 0, 0);
                addStructure(new LOTRNPCRespawnerStructure(false) {

                    @Override
                    public void setupRespawner(LOTRNPCRespawnerEntity spawner) {
                        spawner.setSpawnClasses(LOTREntities.HARNEDOR_WARRIOR, LOTREntities.HARNEDOR_ARCHER);
                        spawner.setCheckRanges(64, -12, 12, 12);
                        spawner.setSpawnRanges(32, -6, 6, 32);
                        spawner.setBlockEnemySpawnRange(64);
                    }
                }, 0, 0, 0);
            }
            if (isRuined) {
                addStructure(new LOTRHarnedorTavernRuinedStructure(false), 3, -7, 0, true);
            } else if (random.nextBoolean()) {
                addStructure(new LOTRHarnedorMarketStructure(false), 0, -8, 0, true);
            } else {
                addStructure(new LOTRHarnedorTavernStructure(false), 3, -7, 0, true);
            }
            float frac = 1.0f / numOuterHouses;
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
                int l = 25;
                int i = Math.round(l * cos);
                int k = Math.round(l * sin);
                if (palisade && k < 0 && Math.abs(i) < 10) {
                    continue;
                }
                addStructure(getRandomHouse(random), i, k, r);
            }
            if (!isRuined) {
                int numFarms = numOuterHouses * 2;
                frac = 1.0f / numFarms;
                turn = 0.0f;
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
                    int l = 45;
                    int i = Math.round(l * cos);
                    int k = Math.round(l * sin);
                    if (palisade && k < 0 && Math.abs(i) < 10) {
                        continue;
                    }
                    if (random.nextInt(3) == 0) {
                        addStructure(new LOTRHayBalesStructure(false), i, k, r);
                        continue;
                    }
                    if (random.nextInt(3) == 0) {
                        addStructure(new LOTRHarnedorPastureStructure(false), i, k, r);
                        continue;
                    }
                    addStructure(new LOTRHarnedorFarmStructure(false), i, k, r);
                }
            }
            if (!isRuined) {
                if (palisade) {
                    addStructure(new LOTRHarnedorVillageSignStructure(false).setSignText(villageName), 5 * (random.nextBoolean() ? 1 : -1), -56, 0, true);
                } else {
                    addStructure(new LOTRHarnedorVillageSignStructure(false).setSignText(villageName), 0, -16, 0, true);
                }
            }
            if (palisade) {
                int rSq = 3721;
                int rMax = 62;
                int rSqMax = rMax * rMax;
                for (int i = -61; i <= 61; ++i) {
                    for (int k = -61; k <= 61; ++k) {
                        int dSq;
                        LOTRHarnedorPalisadeStructure palisade;
                        int i1 = Math.abs(i);
                        if (i1 <= 4 && k < 0 || (dSq = i * i + k * k) < rSq || dSq >= rSqMax) {
                            continue;
                        }
                        if (isRuined) {
                            if (random.nextBoolean()) {
                                continue;
                            }
                            palisade = new LOTRHarnedorPalisadeRuinedStructure(false);
                        } else {
                            palisade = new LOTRHarnedorPalisadeStructure(false);
                        }
                        if (i1 == 5 && k < 0) {
                            palisade.setTall();
                        }
                        addStructure(palisade, i, k, 0);
                    }
                }
            }
        }

        @Override
        public void setupVillageProperties(RandomSource random) {
            villageType = random.nextInt(4) == 0 ? VillageType.FORTRESS : VillageType.VILLAGE;
            villageName = LOTRNames.getHaradVillageName(random);
            numOuterHouses = Mth.randomBetweenInclusive(random, 5, 8);
            palisade = random.nextInt(3) != 0;
        }

    }

}
