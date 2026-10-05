package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.rhun;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.rhun.LOTREasterlingEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public abstract class LOTREasterlingMarketStallStructure extends LOTREasterlingStructure {
    public static Class<?>[] allStallTypes = {Blacksmith.class, Lumber.class, Mason.class, Butcher.class, Brewer.class, Fish.class, Baker.class, Hunter.class, Farmer.class, Gold.class};

    protected LOTREasterlingMarketStallStructure(boolean flag) {
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

    public abstract LOTREasterlingEntity createTrader(WorldGenLevel var1);

    public abstract void generateRoof(WorldGenLevel var1, RandomSource var2, int var3, int var4, int var5);

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        int j1;
        int i1;
        setOriginAndRotation(world, i, j, k, rotation, 3);
        setupRandomBlocks(random);
        if (restrictions) {
            int minHeight = 0;
            int maxHeight = 0;
            for (int i12 = -2; i12 <= 2; ++i12) {
                for (int k1 = -2; k1 <= 2; ++k1) {
                    j1 = getTopBlock(world, i12, k1) - 1;
                    if (!isSurface(world, i12, j1, k1)) {
                        return false;
                    }
                    if (j1 < minHeight) {
                        minHeight = j1;
                    }
                    if (j1 > maxHeight) {
                        maxHeight = j1;
                    }
                    if (maxHeight - minHeight <= 5) {
                        continue;
                    }
                    return false;
                }
            }
        }
        for (i1 = -2; i1 <= 2; ++i1) {
            for (int k1 = -2; k1 <= 2; ++k1) {
                int i2 = Math.abs(i1);
                int k2 = Math.abs(k1);
                for (j1 = 0; (j1 >= 0 || !isOpaque(world, i1, j1, k1)) && getY(j1) >= world.getMinY(); --j1) {
                    setBlockAndMetadata(world, i1, j1, k1, brickBlock, brickMeta);
                    setGrassToDirt(world, i1, j1 - 1, k1);
                }
                for (j1 = 1; j1 <= 4; ++j1) {
                    setAir(world, i1, j1, k1);
                }
                if (i2 == 2 && k2 == 2) {
                    for (j1 = 1; j1 <= 3; ++j1) {
                        setBlockAndMetadata(world, i1, j1, k1, woodBeamBlock, woodBeamMeta);
                    }
                } else if (i2 == 2 || k2 == 2) {
                    setBlockAndMetadata(world, i1, 3, k1, LOTRLegacyBlocks.mod("reedBars"), 0);
                }
                generateRoof(world, random, i1, 4, k1);
            }
        }
        for (i1 = -1; i1 <= 1; ++i1) {
            setBlockAndMetadata(world, i1, 1, -2, plankStairBlock, 6);
            setBlockAndMetadata(world, i1, 1, 2, plankStairBlock, 7);
        }
        for (int k1 = -1; k1 <= 1; ++k1) {
            setBlockAndMetadata(world, -2, 1, k1, plankStairBlock, 5);
            setBlockAndMetadata(world, 2, 1, k1, plankStairBlock, 4);
        }
        setBlockAndMetadata(world, -2, 1, 0, fenceGateBlock, 1);
        setBlockAndMetadata(world, -1, 1, 1, LOTRLegacyBlocks.vanilla("crafting_table"), 0);
        setBlockAndMetadata(world, 1, 1, 1, LOTRLegacyBlocks.vanilla("chest"), 2);
        LOTREasterlingEntity trader = createTrader(world);
        spawnNPCAndSetHome(trader, world, 0, 1, 0, 4);
        return true;
    }

    public static class Baker extends LOTREasterlingMarketStallStructure {
        public Baker(boolean flag) {
            super(flag);
        }

        @Override
        public LOTREasterlingEntity createTrader(WorldGenLevel world) {
            return create(LOTREntities.EASTERLING_BAKER, world);
        }

        @Override
        public void generateRoof(WorldGenLevel world, RandomSource random, int i1, int j1, int k1) {
            int k2 = Math.abs(k1);
            if (k2 % 2 == 0) {
                setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("wool"), 1);
            } else {
                setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("wool"), 12);
            }
        }
    }

    public static class Blacksmith extends LOTREasterlingMarketStallStructure {
        public Blacksmith(boolean flag) {
            super(flag);
        }

        @Override
        public LOTREasterlingEntity createTrader(WorldGenLevel world) {
            return create(LOTREntities.EASTERLING_BLACKSMITH, world);
        }

        @Override
        public void generateRoof(WorldGenLevel world, RandomSource random, int i1, int j1, int k1) {
            int i2 = Math.abs(i1);
            if (i2 == Math.abs(k1)) {
                setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("wool"), 15);
            } else {
                setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("wool"), 7);
            }
        }
    }

    public static class Brewer extends LOTREasterlingMarketStallStructure {
        public Brewer(boolean flag) {
            super(flag);
        }

        @Override
        public LOTREasterlingEntity createTrader(WorldGenLevel world) {
            return create(LOTREntities.EASTERLING_BREWER, world);
        }

        @Override
        public void generateRoof(WorldGenLevel world, RandomSource random, int i1, int j1, int k1) {
            int i2 = Math.abs(i1);
            if (i2 % 2 == 0) {
                setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("wool"), 12);
            } else {
                setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("wool"), 0);
            }
        }
    }

    public static class Butcher extends LOTREasterlingMarketStallStructure {
        public Butcher(boolean flag) {
            super(flag);
        }

        @Override
        public LOTREasterlingEntity createTrader(WorldGenLevel world) {
            return create(LOTREntities.EASTERLING_BUTCHER, world);
        }

        @Override
        public void generateRoof(WorldGenLevel world, RandomSource random, int i1, int j1, int k1) {
            int i2 = Math.abs(i1);
            int k2 = Math.abs(k1);
            if (i2 == 2 || k2 == 2) {
                setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("wool"), 6);
            } else if (i2 == 1 || k2 == 1) {
                setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("wool"), 14);
            } else {
                setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("wool"), 0);
            }
        }
    }

    public static class Farmer extends LOTREasterlingMarketStallStructure {
        public Farmer(boolean flag) {
            super(flag);
        }

        @Override
        public LOTREasterlingEntity createTrader(WorldGenLevel world) {
            return create(LOTREntities.EASTERLING_FARMER, world);
        }

        @Override
        public void generateRoof(WorldGenLevel world, RandomSource random, int i1, int j1, int k1) {
            int k2;
            int i2 = Math.abs(i1);
            if (Math.floorMod(i2 + (k2 = Math.abs(k1)), 2) == 0) {
                if (Integer.signum(i1) != -Integer.signum(k1) && i2 + k2 == 2) {
                    setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("wool"), 4);
                } else {
                    setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("wool"), 13);
                }
            } else {
                setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("wool"), 12);
            }
        }
    }

    public static class Fish extends LOTREasterlingMarketStallStructure {
        public Fish(boolean flag) {
            super(flag);
        }

        @Override
        public LOTREasterlingEntity createTrader(WorldGenLevel world) {
            return create(LOTREntities.EASTERLING_FISHMONGER, world);
        }

        @Override
        public void generateRoof(WorldGenLevel world, RandomSource random, int i1, int j1, int k1) {
            int i2 = Math.abs(i1);
            int k2 = Math.abs(k1);
            if (i2 % 2 == 0) {
                if (k2 == 2) {
                    setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("wool"), 0);
                } else {
                    setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("wool"), 3);
                }
            } else {
                setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("wool"), 11);
            }
        }
    }

    public static class Gold extends LOTREasterlingMarketStallStructure {
        public Gold(boolean flag) {
            super(flag);
        }

        @Override
        public LOTREasterlingEntity createTrader(WorldGenLevel world) {
            return create(LOTREntities.EASTERLING_GOLDSMITH, world);
        }

        @Override
        public void generateRoof(WorldGenLevel world, RandomSource random, int i1, int j1, int k1) {
            setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("wool"), 4);
        }
    }

    public static class Hunter extends LOTREasterlingMarketStallStructure {
        public Hunter(boolean flag) {
            super(flag);
        }

        @Override
        public LOTREasterlingEntity createTrader(WorldGenLevel world) {
            return create(LOTREntities.EASTERLING_HUNTER, world);
        }

        @Override
        public void generateRoof(WorldGenLevel world, RandomSource random, int i1, int j1, int k1) {
            if (Math.floorMod(i1, 2) == 0 && Math.floorMod(k1, 2) == 0) {
                setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("wool"), 15);
            } else {
                setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("wool"), 12);
            }
        }
    }

    public static class Lumber extends LOTREasterlingMarketStallStructure {
        public Lumber(boolean flag) {
            super(flag);
        }

        @Override
        public LOTREasterlingEntity createTrader(WorldGenLevel world) {
            return create(LOTREntities.EASTERLING_LUMBERMAN, world);
        }

        @Override
        public void generateRoof(WorldGenLevel world, RandomSource random, int i1, int j1, int k1) {
            int i2 = Math.abs(i1);
            int k2 = Math.abs(k1);
            if ((i2 == 2 || k2 == 2) && Math.floorMod(i2 + k2, 2) == 0) {
                setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("wool"), 13);
            } else {
                setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("wool"), 12);
            }
        }
    }

    public static class Mason extends LOTREasterlingMarketStallStructure {
        public Mason(boolean flag) {
            super(flag);
        }

        @Override
        public LOTREasterlingEntity createTrader(WorldGenLevel world) {
            return create(LOTREntities.EASTERLING_MASON, world);
        }

        @Override
        public void generateRoof(WorldGenLevel world, RandomSource random, int i1, int j1, int k1) {
            int i2 = Math.abs(i1);
            int k2 = Math.abs(k1);
            if (i2 == 2 || k2 == 2 || i2 != 1 && k2 != 1) {
                setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("wool"), 7);
            } else {
                setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("wool"), 14);
            }
        }
    }

}
