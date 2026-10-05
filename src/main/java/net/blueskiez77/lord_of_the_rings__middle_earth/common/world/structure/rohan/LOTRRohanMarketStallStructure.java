package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.rohan;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.rohan.LOTRRohanManEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public abstract class LOTRRohanMarketStallStructure extends LOTRRohanStructure {
    public static Class<?>[] allStallTypes = {Blacksmith.class, Farmer.class, Lumber.class, Builder.class, Brewer.class, Butcher.class, Fish.class, Baker.class, Orcharder.class};

    protected LOTRRohanMarketStallStructure(boolean flag) {
        super(flag);
    }

    public static LOTRStructureBase2 getRandomStall(RandomSource random, boolean flag) {
        try {
            Class<?> cls = allStallTypes[random.nextInt(allStallTypes.length)];
            return (LOTRStructureBase2) cls.getConstructor(Boolean.TYPE).newInstance(flag);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public abstract LOTRRohanManEntity createTrader(WorldGenLevel var1);

    public abstract void generateRoof(WorldGenLevel var1, RandomSource var2, int var3, int var4, int var5);

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        int k1;
        int i1;
        setOriginAndRotation(world, i, j, k, rotation, 3);
        setupRandomBlocks(random);
        if (restrictions) {
            for (i1 = -2; i1 <= 2; ++i1) {
                for (k1 = -2; k1 <= 2; ++k1) {
                    int j1 = getTopBlock(world, i1, k1) - 1;
                    if (isSurface(world, i1, j1, k1)) {
                        continue;
                    }
                    return false;
                }
            }
        }
        for (i1 = -2; i1 <= 2; ++i1) {
            for (k1 = -2; k1 <= 2; ++k1) {
                int j1;
                int i2 = Math.abs(i1);
                int k2 = Math.abs(k1);
                for (j1 = 0; (j1 >= 0 || !isOpaque(world, i1, j1, k1)) && getY(j1) >= 0; --j1) {
                    setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.mod("dirtPath"), 0);
                    setGrassToDirt(world, i1, j1 - 1, k1);
                }
                for (j1 = 1; j1 <= 4; ++j1) {
                    setAir(world, i1, j1, k1);
                }
                if (i2 == 2 && k2 == 2) {
                    if (k1 < 0) {
                        for (j1 = 1; j1 <= 4; ++j1) {
                            setBlockAndMetadata(world, i1, j1, k1, fenceBlock, fenceMeta);
                        }
                        continue;
                    }
                    for (j1 = 1; j1 <= 3; ++j1) {
                        setBlockAndMetadata(world, i1, j1, k1, fenceBlock, fenceMeta);
                    }
                    continue;
                }
                int j2 = 4;
                if (k1 == 2 || k1 == 1 && i2 == 2) {
                    j2 = 3;
                }
                generateRoof(world, random, i1, j2, k1);
            }
        }
        setBlockAndMetadata(world, -1, 1, -2, plankStairBlock, 4);
        setBlockAndMetadata(world, 0, 1, -2, plankStairBlock, 6);
        setBlockAndMetadata(world, 1, 1, -2, plankStairBlock, 5);
        setBlockAndMetadata(world, -1, 1, 2, plankStairBlock, 4);
        setBlockAndMetadata(world, 0, 1, 2, plankStairBlock, 7);
        setBlockAndMetadata(world, 1, 1, 2, plankStairBlock, 5);
        setBlockAndMetadata(world, 2, 1, -1, plankStairBlock, 7);
        setBlockAndMetadata(world, 2, 1, 0, plankStairBlock, 4);
        setBlockAndMetadata(world, 2, 1, 1, plankStairBlock, 6);
        setBlockAndMetadata(world, -2, 1, -1, plankBlock, plankMeta);
        setBlockAndMetadata(world, -2, 1, 0, fenceGateBlock, 1);
        setBlockAndMetadata(world, -2, 1, 1, plankBlock, plankMeta);
        for (i1 = -1; i1 <= 1; ++i1) {
            setBlockAndMetadata(world, i1, 1, 1, plank2StairBlock, 6);
            setBlockAndMetadata(world, i1, 3, 1, plankSlabBlock, plankSlabMeta | 8);
        }
        for (int k12 = -1; k12 <= 0; ++k12) {
            setBlockAndMetadata(world, -2, 3, k12, plankSlabBlock, plankSlabMeta | 8);
            setBlockAndMetadata(world, 2, 3, k12, plankSlabBlock, plankSlabMeta | 8);
        }
        setBlockAndMetadata(world, 1, 1, -1, LOTRLegacyBlocks.vanilla("chest"), 3);
        for (i1 = -1; i1 <= 1; ++i1) {
            setBlockAndMetadata(world, i1, 3, -2, fenceBlock, fenceMeta);
        }
        LOTRRohanManEntity trader = createTrader(world);
        spawnNPCAndSetHome(trader, world, 0, 1, 0, 4);
        return true;
    }

    public static class Baker extends LOTRRohanMarketStallStructure {
        public Baker(boolean flag) {
            super(flag);
        }

        @Override
        public LOTRRohanManEntity createTrader(WorldGenLevel world) {
            return create(LOTREntities.ROHAN_BAKER, world);
        }

        @Override
        public void generateRoof(WorldGenLevel world, RandomSource random, int i1, int j1, int k1) {
            int i2 = Math.abs(i1);
            if (i2 % 2 == 0) {
                setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("wool"), 12);
            } else {
                setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("wool"), 1);
            }
        }
    }

    public static class Blacksmith extends LOTRRohanMarketStallStructure {
        public Blacksmith(boolean flag) {
            super(flag);
        }

        @Override
        public LOTRRohanManEntity createTrader(WorldGenLevel world) {
            return create(LOTREntities.ROHAN_BLACKSMITH, world);
        }

        @Override
        public void generateRoof(WorldGenLevel world, RandomSource random, int i1, int j1, int k1) {
            int i2 = Math.abs(i1);
            if (i2 + Math.abs(k1) >= 3) {
                setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("wool"), 7);
            } else {
                setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("wool"), 15);
            }
        }
    }

    public static class Brewer extends LOTRRohanMarketStallStructure {
        public Brewer(boolean flag) {
            super(flag);
        }

        @Override
        public LOTRRohanManEntity createTrader(WorldGenLevel world) {
            return create(LOTREntities.ROHAN_BREWER, world);
        }

        @Override
        public void generateRoof(WorldGenLevel world, RandomSource random, int i1, int j1, int k1) {
            int i2 = Math.abs(i1);
            //noinspection BadOddness
            if (i2 % 2 == 1) {
                setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("wool"), 12);
            } else {
                setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("wool"), 4);
            }
        }
    }

    public static class Builder extends LOTRRohanMarketStallStructure {
        public Builder(boolean flag) {
            super(flag);
        }

        @Override
        public LOTRRohanManEntity createTrader(WorldGenLevel world) {
            return create(LOTREntities.ROHAN_BUILDER, world);
        }

        @Override
        public void generateRoof(WorldGenLevel world, RandomSource random, int i1, int j1, int k1) {
            int i2 = Math.abs(i1);
            int k2 = Math.abs(k1);
            if (k2 % 2 == 0 && i2 % 2 == k2 / 2 % 2) {
                setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("wool"), 12);
            } else {
                setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("wool"), 7);
            }
        }
    }

    public static class Butcher extends LOTRRohanMarketStallStructure {
        public Butcher(boolean flag) {
            super(flag);
        }

        @Override
        public LOTRRohanManEntity createTrader(WorldGenLevel world) {
            return create(LOTREntities.ROHAN_BUTCHER, world);
        }

        @Override
        public void generateRoof(WorldGenLevel world, RandomSource random, int i1, int j1, int k1) {
            if (random.nextInt(3) == 0) {
                setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("wool"), 14);
            } else {
                setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("wool"), 6);
            }
        }
    }

    public static class Farmer extends LOTRRohanMarketStallStructure {
        public Farmer(boolean flag) {
            super(flag);
        }

        @Override
        public LOTRRohanManEntity createTrader(WorldGenLevel world) {
            return create(LOTREntities.ROHAN_FARMER, world);
        }

        @Override
        public void generateRoof(WorldGenLevel world, RandomSource random, int i1, int j1, int k1) {
            if (random.nextInt(3) == 0) {
                setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("wool"), 0);
            } else {
                setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("wool"), 8);
            }
        }
    }

    public static class Fish extends LOTRRohanMarketStallStructure {
        public Fish(boolean flag) {
            super(flag);
        }

        @Override
        public LOTRRohanManEntity createTrader(WorldGenLevel world) {
            return create(LOTREntities.ROHAN_FISHMONGER, world);
        }

        @Override
        public void generateRoof(WorldGenLevel world, RandomSource random, int i1, int j1, int k1) {
            int i2 = Math.abs(i1);
            int k2 = Math.abs(k1);
            //noinspection BadOddness
            if (k2 % 2 == 1) {
                setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("wool"), 11);
            } else if (i2 % 2 == k2 / 2 % 2) {
                setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("wool"), 0);
            } else {
                setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("wool"), 3);
            }
        }
    }

    public static class Lumber extends LOTRRohanMarketStallStructure {
        public Lumber(boolean flag) {
            super(flag);
        }

        @Override
        public LOTRRohanManEntity createTrader(WorldGenLevel world) {
            return create(LOTREntities.ROHAN_LUMBERMAN, world);
        }

        @Override
        public void generateRoof(WorldGenLevel world, RandomSource random, int i1, int j1, int k1) {
            int i2 = Math.abs(i1);
            if (i2 + Math.abs(k1) >= 3) {
                setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("wool"), 13);
            } else {
                setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("wool"), 12);
            }
        }
    }

    public static class Orcharder extends LOTRRohanMarketStallStructure {
        public Orcharder(boolean flag) {
            super(flag);
        }

        @Override
        public LOTRRohanManEntity createTrader(WorldGenLevel world) {
            return create(LOTREntities.ROHAN_ORCHARDER, world);
        }

        @Override
        public void generateRoof(WorldGenLevel world, RandomSource random, int i1, int j1, int k1) {
            int i2 = Math.abs(i1);
            if (Math.floorMod(i2 + Math.abs(k1), 2) == 0) {
                setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("wool"), 14);
            } else {
                setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("wool"), 5);
            }
        }
    }

}
