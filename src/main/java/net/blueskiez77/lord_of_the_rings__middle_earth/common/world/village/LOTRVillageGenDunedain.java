package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.village;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRNPCRespawnerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRRoadType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRNPCRespawnerStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.hobbit.LOTRHayBalesStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRRangerHouseStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRRangerLodgeStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRRangerSmithyStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRRangerStablesStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRRangerVillageLightStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRRangerVillagePalisadeStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.ranger.LOTRRangerWellStructure;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRVillageGenDunedain extends LOTRVillageGen {
    public LOTRVillageGenDunedain(String biome, float f) {
        super(biome);
        gridScale = 12;
        gridRandomDisplace = 1;
        spawnChance = f;
        villageChunkRadius = 4;
    }

    @Override
    public LOTRVillageGen.AbstractInstance<?> createVillageInstance(long worldSeed, int i, int k, RandomSource random, LocationInfo loc) {
        return new Instance(this, worldSeed, i, k, random, loc);
    }

    public enum VillageType {
        VILLAGE

    }

    public static class Instance extends LOTRVillageGen.AbstractInstance<LOTRVillageGenDunedain> {
        public VillageType villageType;
        public int innerSize;
        public boolean palisade;

        public Instance(LOTRVillageGenDunedain village, long worldSeed, int i, int k, RandomSource random, LocationInfo loc) {
            super(village, worldSeed, i, k, random, loc);
        }

        @Override
        public void addVillageStructures(RandomSource random) {
            if (villageType == VillageType.VILLAGE) {
                setupVillage(random);
            }
        }

        @Override
        public LOTRRoadType getPath(RandomSource random, int i, int k) {
            int i1 = Math.abs(i);
            int k1 = Math.abs(k);
            if (villageType == VillageType.VILLAGE) {
                int dSq = i * i + k * k;
                if (i1 <= 2 && k1 <= 2) {
                    return null;
                }
                int imn = innerSize + random.nextInt(3);
                if (dSq < imn * imn) {
                    return LOTRRoadType.PATH;
                }
                if (palisade && k < 0 && k > -(innerSize + 12 + 16) && i1 <= 2 + random.nextInt(3)) {
                    return LOTRRoadType.PATH;
                }
            }
            return null;
        }

        public LOTRStructureBase2 getRandomHouse(RandomSource random) {
            if (random.nextInt(3) == 0) {
                int i = random.nextInt(3);
                switch (i) {
                    case 0:
                        return new LOTRRangerSmithyStructure(false);
                    case 1:
                        return new LOTRRangerStablesStructure(false);
                    case 2:
                        return new LOTRRangerLodgeStructure(false);
                    default:
                        break;
                }
            }
            return new LOTRRangerHouseStructure(false);
        }

        @Override
        public boolean isFlat() {
            return false;
        }

        @Override
        public boolean isVillageSpecificSurface(WorldGenLevel world, int i, int j, int k) {
            return false;
        }

        public void setupVillage(RandomSource random) {
            addStructure(new LOTRNPCRespawnerStructure(false) {

                @Override
                public void setupRespawner(LOTRNPCRespawnerEntity spawner) {
                    spawner.setSpawnClass(LOTREntities.DUNEDAIN);
                    spawner.setCheckRanges(40, -12, 12, 30);
                    spawner.setSpawnRanges(20, -6, 6, 64);
                    spawner.setBlockEnemySpawnRange(60);
                }
            }, 0, 0, 0);
            addStructure(new LOTRNPCRespawnerStructure(false) {

                @Override
                public void setupRespawner(LOTRNPCRespawnerEntity spawner) {
                    spawner.setSpawnClass(LOTREntities.RANGER_NORTH);
                    spawner.setCheckRanges(40, -12, 12, 12);
                    spawner.setSpawnRanges(20, -6, 6, 64);
                    spawner.setBlockEnemySpawnRange(60);
                }
            }, 0, 0, 0);
            addStructure(new LOTRRangerWellStructure(false), 0, -2, 0, true);
            int lampX = 8;
            for (int i : new int[]{-lampX, lampX}) {
                for (int k : new int[]{-lampX, lampX}) {
                    addStructure(new LOTRRangerVillageLightStructure(false), i, k, 0);
                }
            }
            int houses = 20;
            float frac = 1.0f / houses;
            float turn = 0.0f;
            while (turn < 1.0f) {
                int l;
                int i;
                int k;
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
                if (palisade && sin < 0.0f && Math.abs(cos) <= 0.5f) {
                    continue;
                }
                if (random.nextInt(3) != 0) {
                    l = innerSize + 3;
                    if (random.nextInt(3) == 0) {
                        l += 12;
                    }
                    i = Math.round(l * cos);
                    k = Math.round(l * sin);
                    addStructure(getRandomHouse(random), i, k, r);
                    continue;
                }
                if (random.nextInt(4) != 0) {
                    continue;
                }
                l = innerSize + 5;
                if (random.nextInt(3) == 0) {
                    l += 12;
                }
                i = Math.round(l * cos);
                k = Math.round(l * sin);
                addStructure(new LOTRHayBalesStructure(false), i, k, r);
            }
            if (palisade) {
                int rPalisade = innerSize + 12 + 16;
                int rSq = rPalisade * rPalisade;
                int rMax = rPalisade + 1;
                int rSqMax = rMax * rMax;
                for (int i = -rPalisade; i <= rPalisade; ++i) {
                    for (int k = -rPalisade; k <= rPalisade; ++k) {
                        int dSq;
                        if (Math.abs(i) <= 5 && k < 0 || (dSq = i * i + k * k) < rSq || dSq >= rSqMax) {
                            continue;
                        }
                        addStructure(new LOTRRangerVillagePalisadeStructure(false), i, k, 0);
                    }
                }
            }
        }

        @Override
        public void setupVillageProperties(RandomSource random) {
            villageType = VillageType.VILLAGE;
            innerSize = Mth.randomBetweenInclusive(random, 12, 20);
            palisade = random.nextBoolean();
        }

    }

}
