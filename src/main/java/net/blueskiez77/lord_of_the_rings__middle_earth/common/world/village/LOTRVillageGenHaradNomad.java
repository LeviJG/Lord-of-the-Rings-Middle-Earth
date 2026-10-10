package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.village;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRNPCRespawnerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRRoadType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRNPCRespawnerStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRNomadBazaarTentStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRNomadChieftainTentStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRNomadTentLargeStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRNomadTentStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.harad.LOTRNomadWellStructure;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRVillageGenHaradNomad extends LOTRVillageGen {
    public LOTRVillageGenHaradNomad(String biome, float f) {
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
        SMALL, BIG

    }

    public static class Instance extends LOTRVillageGen.AbstractInstance<LOTRVillageGenHaradNomad> {
        public VillageType villageType;
        public int numOuterHouses;

        public Instance(LOTRVillageGenHaradNomad village, long worldSeed, int i, int k, RandomSource random, LocationInfo loc) {
            super(village, worldSeed, i, k, random, loc);
        }

        @Override
        public void addVillageStructures(RandomSource random) {
            setupVillage(random);
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

        public void setupVillage(RandomSource random) {
            if (villageType == VillageType.SMALL) {
                addStructure(new LOTRNPCRespawnerStructure(false) {

                    @Override
                    public void setupRespawner(LOTRNPCRespawnerEntity spawner) {
                        spawner.setSpawnClass(LOTREntities.NOMAD);
                        spawner.setCheckRanges(64, -12, 12, 24);
                        spawner.setSpawnRanges(32, -6, 6, 32);
                        spawner.setBlockEnemySpawnRange(40);
                    }
                }, 0, 0, 0);
                addStructure(new LOTRNPCRespawnerStructure(false) {

                    @Override
                    public void setupRespawner(LOTRNPCRespawnerEntity spawner) {
                        spawner.setSpawnClasses(LOTREntities.NOMAD_WARRIOR, LOTREntities.NOMAD_ARCHER);
                        spawner.setCheckRanges(64, -12, 12, 12);
                        spawner.setSpawnRanges(32, -6, 6, 32);
                        spawner.setBlockEnemySpawnRange(40);
                    }
                }, 0, 0, 0);
                addStructure(new LOTRNomadTentLargeStructure(false), 0, -8, 0, true);
            } else if (villageType == VillageType.BIG) {
                addStructure(new LOTRNPCRespawnerStructure(false) {

                    @Override
                    public void setupRespawner(LOTRNPCRespawnerEntity spawner) {
                        spawner.setSpawnClass(LOTREntities.NOMAD);
                        spawner.setCheckRanges(80, -12, 12, 50);
                        spawner.setSpawnRanges(40, -8, 8, 40);
                        spawner.setBlockEnemySpawnRange(60);
                    }
                }, 0, 0, 0);
                addStructure(new LOTRNPCRespawnerStructure(false) {

                    @Override
                    public void setupRespawner(LOTRNPCRespawnerEntity spawner) {
                        spawner.setSpawnClasses(LOTREntities.NOMAD_WARRIOR, LOTREntities.NOMAD_ARCHER);
                        spawner.setCheckRanges(80, -12, 12, 24);
                        spawner.setSpawnRanges(40, -8, 8, 40);
                        spawner.setBlockEnemySpawnRange(60);
                    }
                }, 0, 0, 0);
                addStructure(new LOTRNomadWellStructure(false), 0, 0, 0, true);
                addStructure(new LOTRNomadChieftainTentStructure(false), 0, 14, 0, true);
                addStructure(new LOTRNomadBazaarTentStructure(false), 0, -14, 2, true);
                addStructure(new LOTRNomadTentLargeStructure(false), -14, 0, 1, true);
                addStructure(new LOTRNomadTentLargeStructure(false), 14, 0, 3, true);
            }
            int minOuterSize = 0;
            if (villageType == VillageType.SMALL) {
                minOuterSize = Mth.randomBetweenInclusive(random, 15, 25);
            } else if (villageType == VillageType.BIG) {
                minOuterSize = Mth.randomBetweenInclusive(random, 35, 45);
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
                int l = minOuterSize + random.nextInt(5);
                int i = Math.round(l * cos);
                int k = Math.round(l * sin);
                addStructure(new LOTRNomadTentStructure(false), i, k, r);
            }
        }

        @Override
        public void setupVillageProperties(RandomSource random) {
            if (random.nextInt(3) == 0) {
                villageType = VillageType.BIG;
                numOuterHouses = Mth.randomBetweenInclusive(random, 8, 14);
            } else {
                villageType = VillageType.SMALL;
                numOuterHouses = Mth.randomBetweenInclusive(random, 4, 7);
            }
        }

    }

}
