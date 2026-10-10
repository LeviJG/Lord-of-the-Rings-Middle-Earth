package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.village;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTRNPCRespawnerEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNames;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map.LOTRRoadType;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRNPCRespawnerStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRBlackrootFortressStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRBlackrootWatchtowerStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRDolAmrothStablesStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRDolAmrothWatchtowerStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRGondorBarnStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRGondorBathStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRGondorCottageStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRGondorFortGateStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRGondorFortWallCornerStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRGondorFortWallStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRGondorFortressStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRGondorGatehouseStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRGondorHouseStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRGondorLampPostStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRGondorMarketStallStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRGondorObeliskStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRGondorSmithyStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRGondorStablesStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRGondorStoneHouseStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRGondorStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRGondorTavernStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRGondorTownBenchStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRGondorTownGardenStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRGondorTownTreesStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRGondorTownWallStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRGondorVillageFarmStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRGondorVillageSignStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRGondorWatchtowerStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRGondorWellStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRLamedonFortressStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRLamedonWatchtowerStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRLebenninFortressStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRLebenninWatchtowerStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRLossarnachFortressStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRLossarnachWatchtowerStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRPelargirFortressStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRPelargirWatchtowerStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRPinnathGelinFortressStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor.LOTRPinnathGelinWatchtowerStructure;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.hobbit.LOTRHayBalesStructure;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public class LOTRVillageGenGondor extends LOTRVillageGen {
    public LOTRGondorStructure.GondorFiefdom villageFief;

    public LOTRVillageGenGondor(String biome, LOTRGondorStructure.GondorFiefdom fief, float f) {
        super(biome);
        gridScale = 16;
        gridRandomDisplace = 2;
        spawnChance = f;
        villageChunkRadius = 5;
        villageFief = fief;
    }

    @Override
    public LOTRVillageGen.AbstractInstance<?> createVillageInstance(long worldSeed, int i, int k, RandomSource random, LocationInfo loc) {
        return new Instance(this, worldSeed, i, k, random, loc);
    }

    public enum VillageType {
        VILLAGE, TOWN, FORT

    }

    public static class Instance extends LOTRVillageGen.AbstractInstance<LOTRVillageGenGondor> {
        public VillageType villageType;
        public LOTRGondorStructure.GondorFiefdom villageFief;
        public String[] villageName;

        public Instance(LOTRVillageGenGondor village, long worldSeed, int i, int k, RandomSource random, LocationInfo loc) {
            super(village, worldSeed, i, k, random, loc);
            villageFief = village.villageFief;
        }

        @Override
        public void addStructure(LOTRStructureBase2 structure, int x, int z, int r, boolean force) {
            super.addStructure(structure, x, z, r, force);
            if (structure instanceof LOTRGondorStructure) {
                ((LOTRGondorStructure) structure).strFief = villageFief;
            }
        }

        @Override
        public void addVillageStructures(RandomSource random) {
            if (villageType == VillageType.VILLAGE) {
                setupVillage(random);
            } else if (villageType == VillageType.TOWN) {
                setupTown(random);
            } else if (villageType == VillageType.FORT) {
                setupFortVillage(random);
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
                int omn = 53 - random.nextInt(4);
                int omx = 60 + random.nextInt(4);
                if (dSq > omn * omn && dSq < omx * omx || dSq < 2809 && Math.abs(i1 - k1) <= 2 + random.nextInt(4)) {
                    return LOTRRoadType.PATH;
                }
            }
            if (villageType == VillageType.TOWN && i1 <= 80 && k1 <= 80) {
                return LOTRRoadType.COBBLESTONE;
            }
            if (villageType == VillageType.FORT) {
                if (i1 <= 1 && (k >= 13 || k <= -12) && k1 <= 36) {
                    return LOTRVillageGen.biomeRoadBlock(instanceVillageBiome);
                }
                if (k1 <= 1 && i1 >= 12 && i1 <= 36) {
                    return LOTRVillageGen.biomeRoadBlock(instanceVillageBiome);
                }
                if (k >= 26 && k <= 28 && i1 <= 12) {
                    return LOTRVillageGen.biomeRoadBlock(instanceVillageBiome);
                }
            }
            return null;
        }

        public LOTRStructureBase2 getRandomFarm(RandomSource random) {
            if (random.nextBoolean()) {
                if (random.nextBoolean()) {
                    return new LOTRGondorVillageFarmStructure.Animals(false);
                }
                return new LOTRGondorVillageFarmStructure.Crops(false);
            }
            return new LOTRGondorVillageFarmStructure.Tree(false);
        }

        public LOTRStructureBase2 getRandomHouse(RandomSource random) {
            if (random.nextInt(5) == 0) {
                int i = random.nextInt(3);
                switch (i) {
                    case 0:
                        return new LOTRGondorStablesStructure(false);
                    case 1:
                        return new LOTRGondorSmithyStructure(false);
                    case 2:
                        return new LOTRGondorBarnStructure(false);
                    default:
                        break;
                }
            }
            return new LOTRGondorHouseStructure(false);
        }

        public LOTRStructureBase2 getVillageFortress() {
            if (villageFief == LOTRGondorStructure.GondorFiefdom.GONDOR) {
                return new LOTRGondorFortressStructure(false);
            }
            if (villageFief == LOTRGondorStructure.GondorFiefdom.LOSSARNACH) {
                return new LOTRLossarnachFortressStructure(false);
            }
            if (villageFief == LOTRGondorStructure.GondorFiefdom.LEBENNIN) {
                return new LOTRLebenninFortressStructure(false);
            }
            if (villageFief == LOTRGondorStructure.GondorFiefdom.PELARGIR) {
                return new LOTRPelargirFortressStructure(false);
            }
            if (villageFief == LOTRGondorStructure.GondorFiefdom.PINNATH_GELIN) {
                return new LOTRPinnathGelinFortressStructure(false);
            }
            if (villageFief == LOTRGondorStructure.GondorFiefdom.BLACKROOT_VALE) {
                return new LOTRBlackrootFortressStructure(false);
            }
            if (villageFief == LOTRGondorStructure.GondorFiefdom.LAMEDON) {
                return new LOTRLamedonFortressStructure(false);
            }
            if (villageFief == LOTRGondorStructure.GondorFiefdom.DOL_AMROTH) {
                return new LOTRDolAmrothStablesStructure(false);
            }
            return null;
        }

        public LOTRStructureBase2 getVillageWatchtower() {
            if (villageFief == LOTRGondorStructure.GondorFiefdom.GONDOR) {
                return new LOTRGondorWatchtowerStructure(false);
            }
            if (villageFief == LOTRGondorStructure.GondorFiefdom.LOSSARNACH) {
                return new LOTRLossarnachWatchtowerStructure(false);
            }
            if (villageFief == LOTRGondorStructure.GondorFiefdom.LEBENNIN) {
                return new LOTRLebenninWatchtowerStructure(false);
            }
            if (villageFief == LOTRGondorStructure.GondorFiefdom.PELARGIR) {
                return new LOTRPelargirWatchtowerStructure(false);
            }
            if (villageFief == LOTRGondorStructure.GondorFiefdom.PINNATH_GELIN) {
                return new LOTRPinnathGelinWatchtowerStructure(false);
            }
            if (villageFief == LOTRGondorStructure.GondorFiefdom.BLACKROOT_VALE) {
                return new LOTRBlackrootWatchtowerStructure(false);
            }
            if (villageFief == LOTRGondorStructure.GondorFiefdom.LAMEDON) {
                return new LOTRLamedonWatchtowerStructure(false);
            }
            if (villageFief == LOTRGondorStructure.GondorFiefdom.DOL_AMROTH) {
                return new LOTRDolAmrothWatchtowerStructure(false);
            }
            return null;
        }

        @Override
        public boolean isFlat() {
            return villageType == VillageType.TOWN;
        }

        @Override
        public boolean isVillageSpecificSurface(WorldGenLevel world, int i, int j, int k) {
            BlockState block = world.getBlockState(new BlockPos(i, j, k));
            return villageType == VillageType.TOWN && LOTRLegacyBlocks.vanilla("cobblestone").matches(block);
        }

        public void setupFortVillage(RandomSource random) {
            addStructure(new LOTRNPCRespawnerStructure(false) {

                @Override
                public void setupRespawner(LOTRNPCRespawnerEntity spawner) {
                    spawner.setSpawnClass(LOTREntities.GONDOR_MAN);
                    spawner.setCheckRanges(50, -12, 12, 16);
                    spawner.setSpawnRanges(30, -6, 6, 40);
                    spawner.setBlockEnemySpawnRange(60);
                }
            }, 0, 0, 0);
            for (int i1 : new int[]{-20, 20}) {
                for (int k1 : new int[]{-20, 20}) {
                    addStructure(new LOTRNPCRespawnerStructure(false) {

                        @Override
                        public void setupRespawner(LOTRNPCRespawnerEntity spawner) {
                            spawner.setSpawnClasses(villageFief.getSoldierClasses()[0], villageFief.getSoldierClasses()[1]);
                            spawner.setCheckRanges(20, -12, 12, 16);
                            spawner.setSpawnRanges(20, -6, 6, 40);
                            spawner.setBlockEnemySpawnRange(40);
                        }
                    }, i1, k1, 0);
                }
            }
            addStructure(getVillageFortress(), 0, 12, 2, true);
            addStructure(new LOTRGondorFortGateStructure(false), 0, -37, 0, true);
            addStructure(new LOTRGondorFortWallStructure.Right(false), -11, -37, 0, true);
            addStructure(new LOTRGondorFortWallStructure.Left(false), 11, -37, 0, true);
            addStructure(getVillageWatchtower(), -23, -33, 2, true);
            addStructure(getVillageWatchtower(), 23, -33, 2, true);
            addStructure(new LOTRGondorFortGateStructure(false), -37, 0, 3, true);
            addStructure(new LOTRGondorFortWallStructure.Left(false), -37, -11, 3, true);
            addStructure(new LOTRGondorFortWallStructure.Right(false), -37, 11, 3, true);
            addStructure(getVillageWatchtower(), -33, -23, 1, true);
            addStructure(getVillageWatchtower(), -33, 23, 1, true);
            addStructure(new LOTRGondorFortGateStructure(false), 0, 37, 2, true);
            addStructure(new LOTRGondorFortWallStructure.Left(false), -11, 37, 2, true);
            addStructure(new LOTRGondorFortWallStructure.Right(false), 11, 37, 2, true);
            addStructure(getVillageWatchtower(), -23, 33, 0, true);
            addStructure(getVillageWatchtower(), 23, 33, 0, true);
            addStructure(new LOTRGondorFortGateStructure(false), 37, 0, 1, true);
            addStructure(new LOTRGondorFortWallStructure.Right(false), 37, -11, 1, true);
            addStructure(new LOTRGondorFortWallStructure.Left(false), 37, 11, 1, true);
            addStructure(getVillageWatchtower(), 33, -23, 3, true);
            addStructure(getVillageWatchtower(), 33, 23, 3, true);
            addStructure(new LOTRGondorFortWallCornerStructure(false), -30, -30, 3);
            addStructure(new LOTRGondorFortWallCornerStructure(false), -30, 30, 2);
            addStructure(new LOTRGondorFortWallCornerStructure(false), 30, 30, 1);
            addStructure(new LOTRGondorFortWallCornerStructure(false), 30, -30, 0);
            addStructure(new LOTRGondorStablesStructure(false), -24, 2, 0);
            addStructure(new LOTRGondorStablesStructure(false), -24, -2, 2);
            addStructure(new LOTRGondorSmithyStructure(false), 24, 1, 0);
            addStructure(new LOTRGondorSmithyStructure(false), 24, -1, 2);
            addStructure(new LOTRGondorStoneHouseStructure(false), -3, -25, 1);
            addStructure(new LOTRGondorStoneHouseStructure(false), 3, -25, 3);
            addStructure(new LOTRGondorVillageFarmStructure.Crops(false), -18, -21, 1);
            addStructure(new LOTRGondorVillageFarmStructure.Crops(false), 18, -21, 3);
            addStructure(new LOTRGondorWellStructure(false), -12, 27, 1);
            addStructure(new LOTRGondorWellStructure(false), 12, 27, 3);
        }

        public void setupTown(RandomSource random) {
            int l;
            int wallX;
            boolean outerTavern = random.nextBoolean();
            addStructure(new LOTRNPCRespawnerStructure(false) {

                @Override
                public void setupRespawner(LOTRNPCRespawnerEntity spawner) {
                    spawner.setSpawnClass(LOTREntities.GONDOR_MAN);
                    spawner.setCheckRanges(80, -12, 12, 100);
                    spawner.setSpawnRanges(60, -6, 6, 64);
                    spawner.setBlockEnemySpawnRange(60);
                }
            }, 0, 0, 0);
            for (int i1 : new int[]{-40, 40}) {
                int[] arrn = {-40, 40};
                int n = arrn.length;
                for (int k1 : arrn) {
                    addStructure(new LOTRNPCRespawnerStructure(false) {

                        @Override
                        public void setupRespawner(LOTRNPCRespawnerEntity spawner) {
                            spawner.setSpawnClasses(villageFief.getLevyClasses()[0], villageFief.getLevyClasses()[1]);
                            spawner.setCheckRanges(40, -12, 12, 16);
                            spawner.setSpawnRanges(20, -6, 6, 64);
                            spawner.setBlockEnemySpawnRange(60);
                        }
                    }, i1, k1, 0);
                }
            }
            addStructure(new LOTRGondorWellStructure(false), 0, -4, 0, true);
            int stallPos = 12;
            for (int k1 = -1; k1 <= 1; ++k1) {
                int k2 = k1 * stallPos;
                if (random.nextInt(3) != 0) {
                    addStructure(LOTRGondorMarketStallStructure.getRandomStall(random, false), -stallPos + 3, k2, 1, true);
                }
                if (random.nextInt(3) == 0) {
                    continue;
                }
                addStructure(LOTRGondorMarketStallStructure.getRandomStall(random, false), stallPos - 3, k2, 3, true);
            }
            if (random.nextInt(3) != 0) {
                addStructure(LOTRGondorMarketStallStructure.getRandomStall(random, false), 0, stallPos - 3, 0, true);
            }
            if (random.nextInt(3) != 0) {
                addStructure(LOTRGondorMarketStallStructure.getRandomStall(random, false), 0, -stallPos + 3, 2, true);
            }
            int flowerX = 12;
            int flowerZ = 18;
            for (int i1 : new int[]{-flowerX, flowerX}) {
                addStructure(new LOTRGondorTownGardenStructure(false), i1, flowerZ, 0, true);
                addStructure(new LOTRGondorTownGardenStructure(false), i1, -flowerZ, 2, true);
                addStructure(new LOTRGondorTownGardenStructure(false), -flowerZ, i1, 1, true);
                addStructure(new LOTRGondorTownGardenStructure(false), flowerZ, i1, 3, true);
            }
            int lampZ = 21;
            for (int i1 : new int[]{-1, 1}) {
                int lampX = i1 * 6;
                addStructure(new LOTRGondorLampPostStructure(false), lampX, lampZ, 0, true);
                addStructure(new LOTRGondorLampPostStructure(false), lampX, -lampZ, 2, true);
                if (i1 != -1) {
                    addStructure(new LOTRGondorLampPostStructure(false), -lampZ, lampX, 1, true);
                }
                addStructure(new LOTRGondorLampPostStructure(false), lampZ, lampX, 3, true);
            }
            int houseX = 24;
            for (int k1 = -1; k1 <= 1; ++k1) {
                int houseZ = k1 * 12;
                if (k1 == 1) {
                    addStructure(new LOTRGondorStoneHouseStructure(false), -houseX, houseZ, 1, true);
                    addStructure(new LOTRGondorStoneHouseStructure(false), houseX, houseZ, 3, true);
                }
                if (k1 == 0) {
                    continue;
                }
                addStructure(new LOTRGondorStoneHouseStructure(false), houseZ, houseX, 0, true);
                addStructure(new LOTRGondorStoneHouseStructure(false), houseZ, -houseX, 2, true);
            }
            addStructure(new LOTRGondorSmithyStructure(false), 0, -26, 2, true);
            addStructure(new LOTRGondorObeliskStructure(false), 0, 27, 0, true);
            addStructure(new LOTRGondorTavernStructure(false), -houseX, -5, 1, true);
            addStructure(new LOTRGondorTownTreesStructure(false), -47, -13, 2, true);
            addStructure(new LOTRGondorTownTreesStructure(false), -47, 1, 0, true);
            for (int i1 : new int[]{-43, -51}) {
                addStructure(new LOTRGondorTownBenchStructure(false), i1, -9, 2, true);
                addStructure(new LOTRGondorTownBenchStructure(false), i1, -3, 0, true);
            }
            addStructure(new LOTRGondorBathStructure(false), houseX + 2, -6, 3, true);
            addStructure(new LOTRGondorTownGardenStructure(false), 51, -13, 2, true);
            addStructure(new LOTRGondorTownGardenStructure(false), 51, 1, 0, true);
            addStructure(new LOTRGondorTownGardenStructure(false), 52, -6, 3, true);
            int wellX = 22;
            int wellZ = 31;
            for (int i1 : new int[]{-wellX, wellX}) {
                addStructure(new LOTRGondorWellStructure(false), i1, -wellZ, 2, true);
                addStructure(new LOTRGondorWellStructure(false), i1, wellZ, 0, true);
                addStructure(new LOTRGondorWellStructure(false), -wellZ, i1, 1, true);
                addStructure(new LOTRGondorWellStructure(false), wellZ, i1, 3, true);
            }
            houseX = 54;
            for (int k1 = -2; k1 <= 2; ++k1) {
                int houseZ = k1 * 12;
                if (k1 == -2 || k1 >= 1) {
                    addStructure(new LOTRGondorStoneHouseStructure(false), -houseX, houseZ, 3, true);
                    addStructure(new LOTRGondorStoneHouseStructure(false), houseX, houseZ, 1, true);
                }
                addStructure(new LOTRGondorStoneHouseStructure(false), houseZ, houseX, 2, true);
                addStructure(new LOTRGondorStoneHouseStructure(false), houseZ, -houseX, 0, true);
            }
            int treeX = 47;
            int treeZ = 35;
            for (int i1 : new int[]{-treeX, treeX}) {
                addStructure(new LOTRGondorTownTreesStructure(false), i1, -treeZ, 0, true);
                addStructure(new LOTRGondorTownTreesStructure(false), i1, treeZ, 2, true);
                addStructure(new LOTRGondorTownTreesStructure(false), -treeZ, i1, 3, true);
                addStructure(new LOTRGondorTownTreesStructure(false), treeZ, i1, 1, true);
            }
            houseX = 64;
            int lampX = 59;
            for (int k1 = -4; k1 <= 4; ++k1) {
                boolean treepiece;
                int houseZ = k1 * 12;
                treepiece = Math.floorMod(k1, 2) == 1;
                if (treepiece) {
                    addStructure(new LOTRGondorVillageFarmStructure.Tree(false), -houseX - 2, houseZ, 1, true);
                    addStructure(new LOTRGondorVillageFarmStructure.Tree(false), houseX + 2, houseZ, 3, true);
                } else {
                    addStructure(new LOTRGondorStoneHouseStructure(false), -houseX, houseZ, 1, true);
                    addStructure(new LOTRGondorStoneHouseStructure(false), houseX, houseZ, 3, true);
                }
                if (treepiece) {
                    addStructure(new LOTRGondorVillageFarmStructure.Tree(false), houseZ, -houseX - 2, 2, true);
                } else {
                    addStructure(new LOTRGondorStoneHouseStructure(false), houseZ, -houseX, 2, true);
                }
                if (Math.abs(k1) >= 2 && (!outerTavern || k1 <= 2)) {
                    if (treepiece) {
                        addStructure(new LOTRGondorVillageFarmStructure.Tree(false), houseZ, houseX + 2, 0, true);
                    } else {
                        addStructure(new LOTRGondorStoneHouseStructure(false), houseZ, houseX, 0, true);
                    }
                }
                addStructure(new LOTRGondorLampPostStructure(false), -lampX, houseZ, 1, true);
                addStructure(new LOTRGondorLampPostStructure(false), lampX, houseZ, 3, true);
                addStructure(new LOTRGondorLampPostStructure(false), houseZ, lampX, 0, true);
                addStructure(new LOTRGondorLampPostStructure(false), houseZ, -lampX, 2, true);
            }
            if (outerTavern) {
                addStructure(new LOTRGondorTavernStructure(false), 44, houseX, 0, true);
            }
            int gardenX = 42;
            int gardenZ = 48;
            addStructure(new LOTRGondorVillageFarmStructure.Tree(false), -gardenX, -gardenZ, 1, true);
            addStructure(new LOTRGondorVillageFarmStructure.Tree(false), -gardenX, gardenZ, 1, true);
            addStructure(new LOTRGondorVillageFarmStructure.Tree(false), gardenX, -gardenZ, 3, true);
            addStructure(new LOTRGondorVillageFarmStructure.Tree(false), gardenX, gardenZ, 3, true);
            int obeliskX = 62;
            int obeliskZ = 66;
            addStructure(new LOTRGondorObeliskStructure(false), -obeliskX, -obeliskZ, 1, true);
            addStructure(new LOTRGondorObeliskStructure(false), -obeliskX, obeliskZ, 1, true);
            addStructure(new LOTRGondorObeliskStructure(false), obeliskX, -obeliskZ, 3, true);
            addStructure(new LOTRGondorObeliskStructure(false), obeliskX, obeliskZ, 3, true);
            wellX = 64;
            wellZ = 57;
            addStructure(new LOTRGondorWellStructure(false), -wellX, -wellZ, 1, true);
            addStructure(new LOTRGondorWellStructure(false), -wellX, wellZ, 1, true);
            addStructure(new LOTRGondorWellStructure(false), wellX, -wellZ, 3, true);
            addStructure(new LOTRGondorWellStructure(false), wellX, wellZ, 3, true);
            addStructure(new LOTRGondorWellStructure(false), -wellZ, -wellX, 2, true);
            addStructure(new LOTRGondorWellStructure(false), wellZ, -wellX, 2, true);
            addStructure(new LOTRGondorWellStructure(false), -wellZ, wellX, 0, true);
            addStructure(new LOTRGondorWellStructure(false), wellZ, wellX, 0, true);
            treeX = 75;
            treeZ = 61;
            addStructure(new LOTRGondorTownTreesStructure(false), -treeX, -treeZ, 1, true);
            addStructure(new LOTRGondorTownTreesStructure(false), -treeX, treeZ, 1, true);
            addStructure(new LOTRGondorTownTreesStructure(false), treeX, -treeZ, 3, true);
            addStructure(new LOTRGondorTownTreesStructure(false), treeX, treeZ, 3, true);
            addStructure(new LOTRGondorTownTreesStructure(false), -treeZ, -treeX, 2, true);
            addStructure(new LOTRGondorTownTreesStructure(false), treeZ, -treeX, 2, true);
            addStructure(new LOTRGondorTownTreesStructure(false), -treeZ, treeX, 0, true);
            addStructure(new LOTRGondorTownTreesStructure(false), treeZ, treeX, 0, true);
            addStructure(new LOTRGondorTownTreesStructure(false), -14, 71, 1, true);
            addStructure(new LOTRGondorTownTreesStructure(false), 14, 71, 3, true);
            for (int k1 : new int[]{67, 75}) {
                addStructure(new LOTRGondorTownBenchStructure(false), -10, k1, 1, true);
                addStructure(new LOTRGondorTownBenchStructure(false), 10, k1, 3, true);
            }
            addStructure(new LOTRGondorGatehouseStructure(false).setSignText(villageName), 0, 84, 2, true);
            addStructure(new LOTRGondorLampPostStructure(false), -4, 73, 0, true);
            addStructure(new LOTRGondorLampPostStructure(false), 4, 73, 0, true);
            int towerX = 78;
            int towerZ = 74;
            for (int i1 : new int[]{-towerX, towerX}) {
                addStructure(getVillageWatchtower(), i1, -towerZ, 2, true);
                addStructure(getVillageWatchtower(), i1, towerZ, 0, true);
            }
            int wallZ = 82;
            int wallEndX = 76;
            for (l = 0; l <= 3; ++l) {
                wallX = 12 + l * 16;
                addStructure(LOTRGondorTownWallStructure.Left(false), -wallX, wallZ, 2, true);
                addStructure(LOTRGondorTownWallStructure.Right(false), wallX, wallZ, 2, true);
            }
            addStructure(LOTRGondorTownWallStructure.LeftEndShort(false), -wallEndX, wallZ, 2, true);
            addStructure(LOTRGondorTownWallStructure.RightEndShort(false), wallEndX, wallZ, 2, true);
            addStructure(LOTRGondorTownWallStructure.Centre(false), -wallZ, 0, 3, true);
            addStructure(LOTRGondorTownWallStructure.Centre(false), wallZ, 0, 1, true);
            addStructure(LOTRGondorTownWallStructure.Centre(false), 0, -wallZ, 0, true);
            for (l = 0; l <= 3; ++l) {
                wallX = 12 + l * 16;
                addStructure(LOTRGondorTownWallStructure.Left(false), -wallZ, -wallX, 3, true);
                addStructure(LOTRGondorTownWallStructure.Right(false), -wallZ, wallX, 3, true);
                addStructure(LOTRGondorTownWallStructure.Left(false), wallZ, wallX, 1, true);
                addStructure(LOTRGondorTownWallStructure.Right(false), wallZ, -wallX, 1, true);
                addStructure(LOTRGondorTownWallStructure.Left(false), wallX, -wallZ, 0, true);
                addStructure(LOTRGondorTownWallStructure.Right(false), -wallX, -wallZ, 0, true);
            }
            addStructure(LOTRGondorTownWallStructure.LeftEnd(false), -wallZ, -wallEndX, 3, true);
            addStructure(LOTRGondorTownWallStructure.RightEnd(false), -wallZ, wallEndX, 3, true);
            addStructure(LOTRGondorTownWallStructure.LeftEnd(false), wallZ, wallEndX, 1, true);
            addStructure(LOTRGondorTownWallStructure.RightEnd(false), wallZ, -wallEndX, 1, true);
            addStructure(LOTRGondorTownWallStructure.LeftEndShort(false), wallEndX, -wallZ, 0, true);
            addStructure(LOTRGondorTownWallStructure.RightEndShort(false), -wallEndX, -wallZ, 0, true);
        }

        public void setupVillage(RandomSource random) {
            addStructure(new LOTRGondorWellStructure(false), 0, -4, 0, true);
            addStructure(new LOTRNPCRespawnerStructure(false) {

                @Override
                public void setupRespawner(LOTRNPCRespawnerEntity spawner) {
                    spawner.setSpawnClass(LOTREntities.GONDOR_MAN);
                    spawner.setCheckRanges(40, -12, 12, 40);
                    spawner.setSpawnRanges(20, -6, 6, 64);
                    spawner.setBlockEnemySpawnRange(60);
                }
            }, 0, 0, 0);
            addStructure(new LOTRNPCRespawnerStructure(false) {

                @Override
                public void setupRespawner(LOTRNPCRespawnerEntity spawner) {
                    spawner.setSpawnClass(villageFief.getLevyClasses()[0]);
                    spawner.setCheckRanges(40, -12, 12, 16);
                    spawner.setSpawnRanges(20, -6, 6, 64);
                    spawner.setBlockEnemySpawnRange(60);
                }
            }, 0, 0, 0);
            addStructure(new LOTRGondorCottageStructure(false), -21, 0, 1);
            addStructure(new LOTRGondorCottageStructure(false), 0, -21, 2);
            addStructure(new LOTRGondorCottageStructure(false), 21, 0, 3);
            addStructure(new LOTRGondorTavernStructure(false), 0, 21, 0);
            if (random.nextBoolean()) {
                if (random.nextInt(3) == 0) {
                    addStructure(LOTRGondorMarketStallStructure.getRandomStall(random, false), -9, -12, 1);
                }
                if (random.nextInt(3) == 0) {
                    addStructure(LOTRGondorMarketStallStructure.getRandomStall(random, false), 9, -12, 3);
                }
                if (random.nextInt(3) == 0) {
                    addStructure(LOTRGondorMarketStallStructure.getRandomStall(random, false), -9, 12, 1);
                }
                if (random.nextInt(3) == 0) {
                    addStructure(LOTRGondorMarketStallStructure.getRandomStall(random, false), 9, 12, 3);
                }
            }
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
                if (random.nextBoolean()) {
                    l = 61;
                    i = Math.round(l * cos);
                    k = Math.round(l * sin);
                    addStructure(getRandomHouse(random), i, k, r);
                    continue;
                }
                if (random.nextInt(3) == 0) {
                    continue;
                }
                l = 65;
                i = Math.round(l * cos);
                k = Math.round(l * sin);
                addStructure(new LOTRHayBalesStructure(false), i, k, r);
            }
            int signPos = Math.round(50.0f * Mth.cos(0.7853981633974483f));
            int signDisp = Math.round(7.0f * Mth.cos(0.7853981633974483f));
            addStructure(new LOTRGondorVillageSignStructure(false).setSignText(villageName), -signPos, -signPos + signDisp, 1);
            addStructure(new LOTRGondorVillageSignStructure(false).setSignText(villageName), signPos, -signPos + signDisp, 3);
            addStructure(new LOTRGondorVillageSignStructure(false).setSignText(villageName), -signPos, signPos - signDisp, 1);
            addStructure(new LOTRGondorVillageSignStructure(false).setSignText(villageName), signPos, signPos - signDisp, 3);
            int farmX = 38;
            int farmZ = 17;
            int farmSize = 6;
            if (random.nextBoolean()) {
                addStructure(getRandomFarm(random), -farmX + farmSize, -farmZ, 1);
            }
            if (random.nextBoolean()) {
                addStructure(getRandomFarm(random), -farmZ + farmSize, -farmX, 1);
            }
            if (random.nextBoolean()) {
                addStructure(getRandomFarm(random), farmX - farmSize, -farmZ, 3);
            }
            if (random.nextBoolean()) {
                addStructure(getRandomFarm(random), farmZ - farmSize, -farmX, 3);
            }
            if (random.nextBoolean()) {
                addStructure(getRandomFarm(random), -farmX + farmSize, farmZ, 1);
            }
            if (random.nextBoolean()) {
                addStructure(getRandomFarm(random), farmX - farmSize, farmZ, 3);
            }
        }

        @Override
        public void setupVillageProperties(RandomSource random) {
            villageName = LOTRNames.getGondorVillageName(random);
            villageType = random.nextInt(4) == 0 ? VillageType.FORT : random.nextInt(4) == 0 ? VillageType.TOWN : VillageType.VILLAGE;
        }

    }

}
