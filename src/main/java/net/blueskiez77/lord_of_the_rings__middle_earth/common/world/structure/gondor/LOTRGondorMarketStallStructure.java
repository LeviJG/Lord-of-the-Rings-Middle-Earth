package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.gondor;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.LOTREntities;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.gondor.LOTRGondorManEntity;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRStructureBase2;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

public abstract class LOTRGondorMarketStallStructure extends LOTRGondorStructure {
    public static Class<?>[] allStallTypes = {Greengrocer.class, Lumber.class, Mason.class, Brewer.class, Flowers.class, Butcher.class, Fish.class, Farmer.class, Blacksmith.class, Baker.class};

    protected LOTRGondorMarketStallStructure(boolean flag) {
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

    public abstract LOTRGondorManEntity createTrader(WorldGenLevel var1);

    public abstract void generateRoof(WorldGenLevel var1, RandomSource var2, int var3, int var4, int var5);

    @Override
    public boolean generateWithSetRotation(WorldGenLevel world, RandomSource random, int i, int j, int k, int rotation) {
        int j1;
        setOriginAndRotation(world, i, j, k, rotation, 3);
        setupRandomBlocks(random);
        if (restrictions) {
            int minHeight = 0;
            int maxHeight = 0;
            for (int i1 = -2; i1 <= 2; ++i1) {
                for (int k1 = -2; k1 <= 2; ++k1) {
                    j1 = getTopBlock(world, i1, k1) - 1;
                    if (!isSurface(world, i1, j1, k1)) {
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
        for (int i1 = -2; i1 <= 2; ++i1) {
            for (int k1 = -2; k1 <= 2; ++k1) {
                int i2 = Math.abs(i1);
                int k2 = Math.abs(k1);
                for (j1 = 0; (j1 >= 0 || !isOpaque(world, i1, j1, k1)) && getY(j1) >= 0; --j1) {
                    setBlockAndMetadata(world, i1, j1, k1, brickBlock, brickMeta);
                    setGrassToDirt(world, i1, j1 - 1, k1);
                }
                for (j1 = 1; j1 <= 4; ++j1) {
                    setAir(world, i1, j1, k1);
                }
                if (i2 == 2 && k2 == 2) {
                    for (j1 = 1; j1 <= 3; ++j1) {
                        setBlockAndMetadata(world, i1, j1, k1, fenceBlock, fenceMeta);
                    }
                } else if (i2 == 2 || k2 == 2) {
                    setBlockAndMetadata(world, i1, 1, k1, plankBlock, plankMeta);
                    setBlockAndMetadata(world, i1, 3, k1, fenceBlock, fenceMeta);
                }
                generateRoof(world, random, i1, 4, k1);
            }
        }
        setBlockAndMetadata(world, -2, 1, 0, fenceGateBlock, 1);
        setBlockAndMetadata(world, -1, 1, -1, LOTRLegacyBlocks.vanilla("crafting_table"), 0);
        setBlockAndMetadata(world, 1, 1, -1, LOTRLegacyBlocks.vanilla("chest"), 3);
        LOTRGondorManEntity trader = createTrader(world);
        spawnNPCAndSetHome(trader, world, 0, 1, 0, 4);
        return true;
    }

    public static class Baker extends LOTRGondorMarketStallStructure {
        public Baker(boolean flag) {
            super(flag);
        }

        @Override
        public LOTRGondorManEntity createTrader(WorldGenLevel world) {
            return create(LOTREntities.GONDOR_BAKER, world);
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

    public static class Blacksmith extends LOTRGondorMarketStallStructure {
        public Blacksmith(boolean flag) {
            super(flag);
        }

        @Override
        public LOTRGondorManEntity createTrader(WorldGenLevel world) {
            return create(LOTREntities.GONDOR_BLACKSMITH, world);
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

    public static class Brewer extends LOTRGondorMarketStallStructure {
        public Brewer(boolean flag) {
            super(flag);
        }

        @Override
        public LOTRGondorManEntity createTrader(WorldGenLevel world) {
            return create(LOTREntities.GONDOR_BREWER, world);
        }

        @Override
        public void generateRoof(WorldGenLevel world, RandomSource random, int i1, int j1, int k1) {
            int i2 = Math.abs(i1);
            if (i2 % 2 == 0) {
                setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("wool"), 12);
            } else {
                setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("wool"), 4);
            }
        }
    }

    public static class Butcher extends LOTRGondorMarketStallStructure {
        public Butcher(boolean flag) {
            super(flag);
        }

        @Override
        public LOTRGondorManEntity createTrader(WorldGenLevel world) {
            return create(LOTREntities.GONDOR_BUTCHER, world);
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

    public static class Farmer extends LOTRGondorMarketStallStructure {
        public Farmer(boolean flag) {
            super(flag);
        }

        @Override
        public LOTRGondorManEntity createTrader(WorldGenLevel world) {
            return create(LOTREntities.GONDOR_FARMER, world);
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

    public static class Fish extends LOTRGondorMarketStallStructure {
        public Fish(boolean flag) {
            super(flag);
        }

        @Override
        public LOTRGondorManEntity createTrader(WorldGenLevel world) {
            return create(LOTREntities.GONDOR_FISHMONGER, world);
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

    public static class Flowers extends LOTRGondorMarketStallStructure {
        public Flowers(boolean flag) {
            super(flag);
        }

        @Override
        public LOTRGondorManEntity createTrader(WorldGenLevel world) {
            return create(LOTREntities.GONDOR_FLORIST, world);
        }

        @Override
        public void generateRoof(WorldGenLevel world, RandomSource random, int i1, int j1, int k1) {
            int i2 = Math.abs(i1);
            if (i2 == Math.abs(k1)) {
                setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("wool"), 4);
            } else {
                setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("wool"), 13);
            }
        }
    }

    public static class Greengrocer extends LOTRGondorMarketStallStructure {
        public Greengrocer(boolean flag) {
            super(flag);
        }

        @Override
        public LOTRGondorManEntity createTrader(WorldGenLevel world) {
            return create(LOTREntities.GONDOR_GREENGROCER, world);
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

    public static class Lumber extends LOTRGondorMarketStallStructure {
        public Lumber(boolean flag) {
            super(flag);
        }

        @Override
        public LOTRGondorManEntity createTrader(WorldGenLevel world) {
            return create(LOTREntities.GONDOR_LUMBERMAN, world);
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

    public static class Mason extends LOTRGondorMarketStallStructure {
        public Mason(boolean flag) {
            super(flag);
        }

        @Override
        public LOTRGondorManEntity createTrader(WorldGenLevel world) {
            return create(LOTREntities.GONDOR_MASON, world);
        }

        @Override
        public void generateRoof(WorldGenLevel world, RandomSource random, int i1, int j1, int k1) {
            int i2 = Math.abs(i1);
            int k2 = Math.abs(k1);
            if (i2 == 2 || k2 == 2 || i2 != 1 && k2 != 1) {
                setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("wool"), 7);
            } else {
                setBlockAndMetadata(world, i1, j1, k1, LOTRLegacyBlocks.vanilla("wool"), 8);
            }
        }
    }

}
