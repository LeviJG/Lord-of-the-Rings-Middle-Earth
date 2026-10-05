package net.blueskiez77.lord_of_the_rings__middle_earth.common.world.map;

import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.world.structure.LOTRLegacyBlocks.LegacyBlock;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

/**
 * LOTRRoadType: what a road or village path is made of -- for each kind, the
 * block for the top (or a slab, where the path steps down) and the one under
 * it, often a random mix -- and, for the roads across the map, how much of it
 * is left (repair) and whether flowers grow in it. The bridges' blocks
 * (BridgeType) are here too.
 *
 * <p>The original passed each call the biome as well; none of the kinds used
 * it, so it is left out. The roads themselves come with the world (D10); the
 * villages' paths use these now.
 */
public abstract class LOTRRoadType {
    public static LOTRRoadType PATH = new LOTRRoadType() {

        @Override
        public RoadBlock getBlock(RandomSource rand, boolean top, boolean slab) {
            if (slab) {
                float f = rand.nextFloat();
                if (f < 0.5f) {
                    return new RoadBlock(LOTRLegacyBlocks.mod("slabSingleDirt"), 1);
                }
                if (f < 0.8f) {
                    return new RoadBlock(LOTRLegacyBlocks.mod("slabSingleDirt"), 0);
                }
                return new RoadBlock(LOTRLegacyBlocks.mod("slabSingleGravel"), 0);
            }
            if (top) {
                float f = rand.nextFloat();
                if (f < 0.5f) {
                    return new RoadBlock(LOTRLegacyBlocks.mod("dirtPath"), 0);
                }
                if (f < 0.8f) {
                    return new RoadBlock(LOTRLegacyBlocks.vanilla("dirt"), 1);
                }
                return new RoadBlock(LOTRLegacyBlocks.vanilla("gravel"), 0);
            }
            return new RoadBlock(LOTRLegacyBlocks.mod("dirtPath"), 0);
        }
    };
    public static LOTRRoadType PAVED_PATH = new LOTRRoadType() {

        @Override
        public RoadBlock getBlock(RandomSource rand, boolean top, boolean slab) {
            if (slab) {
                float f = rand.nextFloat();
                if (f < 0.5f) {
                    return new RoadBlock(LOTRLegacyBlocks.mod("slabSingleDirt"), 1);
                }
                if (f < 0.8f) {
                    return new RoadBlock(LOTRLegacyBlocks.vanilla("stone_slab"), 3);
                }
                return new RoadBlock(LOTRLegacyBlocks.mod("slabSingleGravel"), 0);
            }
            if (top) {
                float f = rand.nextFloat();
                if (f < 0.5f) {
                    return new RoadBlock(LOTRLegacyBlocks.mod("dirtPath"), 0);
                }
                if (f < 0.8f) {
                    return new RoadBlock(LOTRLegacyBlocks.vanilla("cobblestone"), 0);
                }
                return new RoadBlock(LOTRLegacyBlocks.vanilla("gravel"), 0);
            }
            return new RoadBlock(LOTRLegacyBlocks.mod("dirtPath"), 0);
        }
    };
    public static LOTRRoadType COBBLESTONE = new LOTRRoadType() {

        @Override
        public RoadBlock getBlock(RandomSource rand, boolean top, boolean slab) {
            if (slab) {
                return new RoadBlock(LOTRLegacyBlocks.vanilla("stone_slab"), 3);
            }
            return new RoadBlock(LOTRLegacyBlocks.vanilla("cobblestone"), 0);
        }
    };
    public static LOTRRoadType DIRT = new LOTRRoadType() {

        @Override
        public RoadBlock getBlock(RandomSource rand, boolean top, boolean slab) {
            if (slab) {
                return new RoadBlock(LOTRLegacyBlocks.mod("slabSingleDirt"), 0);
            }
            return new RoadBlock(LOTRLegacyBlocks.vanilla("dirt"), 1);
        }
    };
    public static LOTRRoadType GALADHRIM = new LOTRRoadType() {

        @Override
        public RoadBlock getBlock(RandomSource rand, boolean top, boolean slab) {
            if (slab) {
                return new RoadBlock(LOTRLegacyBlocks.mod("slabSingle2"), 3);
            }
            return new RoadBlock(LOTRLegacyBlocks.mod("brick"), 11);
        }
    };
    public static LOTRRoadType GALADHRIM_RUINED = new LOTRRoadType() {

        @Override
        public RoadBlock getBlock(RandomSource rand, boolean top, boolean slab) {
            if (slab) {
                if (rand.nextInt(4) == 0) {
                    return rand.nextBoolean() ? new RoadBlock(LOTRLegacyBlocks.mod("slabSingle2"), 4) : new RoadBlock(LOTRLegacyBlocks.mod("slabSingle2"), 5);
                }
                return new RoadBlock(LOTRLegacyBlocks.mod("slabSingle2"), 3);
            }
            if (rand.nextInt(4) == 0) {
                return rand.nextBoolean() ? new RoadBlock(LOTRLegacyBlocks.mod("brick"), 12) : new RoadBlock(LOTRLegacyBlocks.mod("brick"), 13);
            }
            return new RoadBlock(LOTRLegacyBlocks.mod("brick"), 11);
        }
    };
    public static LOTRRoadType HIGH_ELVEN = new LOTRRoadType() {

        @Override
        public RoadBlock getBlock(RandomSource rand, boolean top, boolean slab) {
            if (slab) {
                return new RoadBlock(LOTRLegacyBlocks.mod("slabSingle5"), 5);
            }
            return new RoadBlock(LOTRLegacyBlocks.mod("brick3"), 2);
        }
    };
    public static LOTRRoadType HIGH_ELVEN_RUINED = new LOTRRoadType() {

        @Override
        public RoadBlock getBlock(RandomSource rand, boolean top, boolean slab) {
            if (slab) {
                if (rand.nextInt(4) == 0) {
                    return rand.nextBoolean() ? new RoadBlock(LOTRLegacyBlocks.mod("slabSingle5"), 6) : new RoadBlock(LOTRLegacyBlocks.mod("slabSingle5"), 7);
                }
                return new RoadBlock(LOTRLegacyBlocks.mod("slabSingle5"), 5);
            }
            if (rand.nextInt(4) == 0) {
                return rand.nextBoolean() ? new RoadBlock(LOTRLegacyBlocks.mod("brick3"), 3) : new RoadBlock(LOTRLegacyBlocks.mod("brick3"), 4);
            }
            return new RoadBlock(LOTRLegacyBlocks.mod("brick3"), 2);
        }
    };
    public static LOTRRoadType WOOD_ELVEN = new LOTRRoadType() {

        @Override
        public RoadBlock getBlock(RandomSource rand, boolean top, boolean slab) {
            if (slab) {
                return new RoadBlock(LOTRLegacyBlocks.mod("slabSingle6"), 2);
            }
            return new RoadBlock(LOTRLegacyBlocks.mod("brick3"), 5);
        }
    };
    public static LOTRRoadType WOOD_ELVEN_RUINED = new LOTRRoadType() {

        @Override
        public RoadBlock getBlock(RandomSource rand, boolean top, boolean slab) {
            if (slab) {
                if (rand.nextInt(4) == 0) {
                    return rand.nextBoolean() ? new RoadBlock(LOTRLegacyBlocks.mod("slabSingle6"), 3) : new RoadBlock(LOTRLegacyBlocks.mod("slabSingle6"), 4);
                }
                return new RoadBlock(LOTRLegacyBlocks.mod("slabSingle6"), 2);
            }
            if (rand.nextInt(4) == 0) {
                return rand.nextBoolean() ? new RoadBlock(LOTRLegacyBlocks.mod("brick3"), 6) : new RoadBlock(LOTRLegacyBlocks.mod("brick3"), 7);
            }
            return new RoadBlock(LOTRLegacyBlocks.mod("brick3"), 5);
        }
    };
    public static LOTRRoadType ARNOR = new LOTRRoadType() {

        @Override
        public RoadBlock getBlock(RandomSource rand, boolean top, boolean slab) {
            if (slab) {
                if (rand.nextInt(4) == 0) {
                    return rand.nextBoolean() ? new RoadBlock(LOTRLegacyBlocks.mod("slabSingle4"), 2) : new RoadBlock(LOTRLegacyBlocks.mod("slabSingle4"), 3);
                }
                return new RoadBlock(LOTRLegacyBlocks.mod("slabSingle4"), 1);
            }
            if (rand.nextInt(4) == 0) {
                return rand.nextBoolean() ? new RoadBlock(LOTRLegacyBlocks.mod("brick2"), 4) : new RoadBlock(LOTRLegacyBlocks.mod("brick2"), 5);
            }
            return new RoadBlock(LOTRLegacyBlocks.mod("brick2"), 3);
        }
    };
    public static LOTRRoadType GONDOR = new LOTRRoadType() {

        @Override
        public RoadBlock getBlock(RandomSource rand, boolean top, boolean slab) {
            if (slab) {
                return new RoadBlock(LOTRLegacyBlocks.mod("slabSingle"), 3);
            }
            return new RoadBlock(LOTRLegacyBlocks.mod("brick"), 1);
        }
    };
    public static LOTRRoadType GONDOR_MIX = new LOTRRoadType() {

        @Override
        public RoadBlock getBlock(RandomSource rand, boolean top, boolean slab) {
            if (slab) {
                if (rand.nextInt(8) == 0) {
                    return new RoadBlock(LOTRLegacyBlocks.mod("slabSingle"), 2);
                }
                if (rand.nextInt(8) == 0) {
                    return rand.nextBoolean() ? new RoadBlock(LOTRLegacyBlocks.mod("slabSingle"), 4) : new RoadBlock(LOTRLegacyBlocks.mod("slabSingle"), 5);
                }
                return new RoadBlock(LOTRLegacyBlocks.mod("slabSingle"), 3);
            }
            if (rand.nextInt(8) == 0) {
                return new RoadBlock(LOTRLegacyBlocks.mod("slabDouble"), 2);
            }
            if (rand.nextInt(8) == 0) {
                return rand.nextBoolean() ? new RoadBlock(LOTRLegacyBlocks.mod("brick"), 2) : new RoadBlock(LOTRLegacyBlocks.mod("brick"), 3);
            }
            return new RoadBlock(LOTRLegacyBlocks.mod("brick"), 1);
        }
    };
    public static LOTRRoadType DOL_AMROTH = new LOTRRoadType() {

        @Override
        public RoadBlock getBlock(RandomSource rand, boolean top, boolean slab) {
            if (slab) {
                return new RoadBlock(LOTRLegacyBlocks.mod("slabSingle6"), 7);
            }
            return new RoadBlock(LOTRLegacyBlocks.mod("brick3"), 9);
        }
    };
    public static LOTRRoadType ROHAN = new LOTRRoadType() {

        @Override
        public RoadBlock getBlock(RandomSource rand, boolean top, boolean slab) {
            if (slab) {
                return new RoadBlock(LOTRLegacyBlocks.mod("slabSingle"), 6);
            }
            return new RoadBlock(LOTRLegacyBlocks.mod("brick"), 4);
        }
    };
    public static LOTRRoadType ROHAN_MIX = new LOTRRoadType() {

        @Override
        public RoadBlock getBlock(RandomSource rand, boolean top, boolean slab) {
            if (slab) {
                if (rand.nextInt(3) == 0) {
                    return rand.nextBoolean() ? new RoadBlock(LOTRLegacyBlocks.mod("slabSingleDirt"), 0) : new RoadBlock(LOTRLegacyBlocks.mod("slabSingleDirt"), 1);
                }
                return rand.nextBoolean() ? new RoadBlock(LOTRLegacyBlocks.mod("slabSingle"), 6) : new RoadBlock(LOTRLegacyBlocks.mod("slabSingle11"), 4);
            }
            if (rand.nextInt(3) == 0) {
                return rand.nextBoolean() ? new RoadBlock(LOTRLegacyBlocks.vanilla("dirt"), 1) : new RoadBlock(LOTRLegacyBlocks.mod("dirtPath"), 0);
            }
            return rand.nextBoolean() ? new RoadBlock(LOTRLegacyBlocks.mod("brick"), 4) : new RoadBlock(LOTRLegacyBlocks.mod("rock"), 2);
        }
    };
    public static LOTRRoadType DWARVEN = new LOTRRoadType() {

        @Override
        public RoadBlock getBlock(RandomSource rand, boolean top, boolean slab) {
            if (slab) {
                return new RoadBlock(LOTRLegacyBlocks.mod("slabSingle"), 7);
            }
            return new RoadBlock(LOTRLegacyBlocks.mod("brick"), 6);
        }
    };
    public static LOTRRoadType DALE = new LOTRRoadType() {

        @Override
        public RoadBlock getBlock(RandomSource rand, boolean top, boolean slab) {
            if (slab) {
                return new RoadBlock(LOTRLegacyBlocks.mod("slabSingle9"), 6);
            }
            return new RoadBlock(LOTRLegacyBlocks.mod("brick5"), 1);
        }
    };
    public static LOTRRoadType HARAD = new LOTRRoadType() {

        @Override
        public RoadBlock getBlock(RandomSource rand, boolean top, boolean slab) {
            if (slab) {
                return new RoadBlock(LOTRLegacyBlocks.mod("slabSingle4"), 0);
            }
            return new RoadBlock(LOTRLegacyBlocks.mod("brick"), 15);
        }
    };
    public static LOTRRoadType HARAD_PATH = new LOTRRoadType() {

        @Override
        public RoadBlock getBlock(RandomSource rand, boolean top, boolean slab) {
            if (slab) {
                float f = rand.nextFloat();
                if (f < 0.33f) {
                    if (rand.nextInt(4) == 0) {
                        return new RoadBlock(LOTRLegacyBlocks.mod("slabSingle7"), 1);
                    }
                    return new RoadBlock(LOTRLegacyBlocks.mod("slabSingle4"), 0);
                }
                if (f < 0.67f) {
                    return new RoadBlock(LOTRLegacyBlocks.mod("slabSingleSand"), 0);
                }
                return new RoadBlock(LOTRLegacyBlocks.mod("slabSingleDirt"), 1);
            }
            float f = rand.nextFloat();
            if (f < 0.33f) {
                if (rand.nextInt(4) == 0) {
                    return new RoadBlock(LOTRLegacyBlocks.mod("brick3"), 11);
                }
                return new RoadBlock(LOTRLegacyBlocks.mod("brick"), 15);
            }
            if (f < 0.67f) {
                return top ? new RoadBlock(LOTRLegacyBlocks.vanilla("sand"), 0) : new RoadBlock(LOTRLegacyBlocks.vanilla("sandstone"), 0);
            }
            return new RoadBlock(LOTRLegacyBlocks.mod("dirtPath"), 0);
        }
    };
    public static LOTRRoadType HARAD_TOWN = new LOTRRoadType() {

        @Override
        public RoadBlock getBlock(RandomSource rand, boolean top, boolean slab) {
            if (slab) {
                float f = rand.nextFloat();
                if (f < 0.17f) {
                    return new RoadBlock(LOTRLegacyBlocks.mod("slabSingleDirt"), 0);
                }
                if (f < 0.33f) {
                    return new RoadBlock(LOTRLegacyBlocks.mod("slabSingleDirt"), 1);
                }
                if (f < 0.5f) {
                    return new RoadBlock(LOTRLegacyBlocks.mod("slabSingleSand"), 0);
                }
                if (f < 0.67f) {
                    return new RoadBlock(LOTRLegacyBlocks.mod("slabSingle4"), 0);
                }
                if (f < 0.83f) {
                    return new RoadBlock(LOTRLegacyBlocks.mod("slabSingle7"), 1);
                }
                return new RoadBlock(LOTRLegacyBlocks.mod("slabSingle4"), 7);
            }
            float f = rand.nextFloat();
            if (f < 0.17f) {
                return new RoadBlock(LOTRLegacyBlocks.vanilla("dirt"), 1);
            }
            if (f < 0.33f) {
                return new RoadBlock(LOTRLegacyBlocks.mod("dirtPath"), 0);
            }
            if (f < 0.5f) {
                return top ? new RoadBlock(LOTRLegacyBlocks.vanilla("sand"), 0) : new RoadBlock(LOTRLegacyBlocks.vanilla("sandstone"), 0);
            }
            if (f < 0.67f) {
                return new RoadBlock(LOTRLegacyBlocks.mod("brick"), 15);
            }
            if (f < 0.83f) {
                return new RoadBlock(LOTRLegacyBlocks.mod("brick3"), 11);
            }
            return new RoadBlock(LOTRLegacyBlocks.mod("pillar"), 5);
        }
    };
    public static LOTRRoadType UMBAR = new LOTRRoadType() {

        @Override
        public RoadBlock getBlock(RandomSource rand, boolean top, boolean slab) {
            if (slab) {
                return new RoadBlock(LOTRLegacyBlocks.mod("slabSingle13"), 2);
            }
            return new RoadBlock(LOTRLegacyBlocks.mod("brick6"), 6);
        }
    };
    public static LOTRRoadType GULF_HARAD = new LOTRRoadType() {

        @Override
        public RoadBlock getBlock(RandomSource rand, boolean top, boolean slab) {
            if (slab) {
                float f = rand.nextFloat();
                if (f < 0.25f) {
                    return new RoadBlock(LOTRLegacyBlocks.mod("slabSingleDirt"), 0);
                }
                if (f < 0.5f) {
                    return new RoadBlock(LOTRLegacyBlocks.mod("slabSingleSand"), 1);
                }
                if (f < 0.75f) {
                    return new RoadBlock(LOTRLegacyBlocks.mod("slabSingle7"), 2);
                }
                return new RoadBlock(LOTRLegacyBlocks.mod("slabSingle7"), 3);
            }
            float f = rand.nextFloat();
            if (f < 0.25f) {
                return new RoadBlock(LOTRLegacyBlocks.vanilla("dirt"), 1);
            }
            if (f < 0.5f) {
                return top ? new RoadBlock(LOTRLegacyBlocks.vanilla("sand"), 1) : new RoadBlock(LOTRLegacyBlocks.mod("redSandstone"), 0);
            }
            if (f < 0.75f) {
                return new RoadBlock(LOTRLegacyBlocks.mod("brick3"), 13);
            }
            return new RoadBlock(LOTRLegacyBlocks.mod("brick3"), 14);
        }
    };
    public static LOTRRoadType TAUREDAIN = new LOTRRoadType() {

        @Override
        public RoadBlock getBlock(RandomSource rand, boolean top, boolean slab) {
            if (slab) {
                if (rand.nextInt(4) == 0) {
                    if (rand.nextBoolean()) {
                        return new RoadBlock(LOTRLegacyBlocks.mod("slabSingle8"), 1);
                    }
                    return new RoadBlock(LOTRLegacyBlocks.mod("slabSingle8"), 2);
                }
                return new RoadBlock(LOTRLegacyBlocks.mod("slabSingle8"), 0);
            }
            if (rand.nextInt(4) == 0) {
                if (rand.nextBoolean()) {
                    return new RoadBlock(LOTRLegacyBlocks.mod("brick4"), 1);
                }
                return new RoadBlock(LOTRLegacyBlocks.mod("brick4"), 2);
            }
            return new RoadBlock(LOTRLegacyBlocks.mod("brick4"), 0);
        }
    };
    public static LOTRRoadType MORDOR = new LOTRRoadType() {

        @Override
        public RoadBlock getBlock(RandomSource rand, boolean top, boolean slab) {
            if (slab) {
                return new RoadBlock(LOTRLegacyBlocks.mod("slabSingleDirt"), 3);
            }
            return new RoadBlock(LOTRLegacyBlocks.mod("mordorDirt"), 0);
        }
    };
    public static LOTRRoadType DORWINION = new LOTRRoadType() {

        @Override
        public RoadBlock getBlock(RandomSource rand, boolean top, boolean slab) {
            if (slab) {
                return new RoadBlock(LOTRLegacyBlocks.mod("slabSingle9"), 7);
            }
            return new RoadBlock(LOTRLegacyBlocks.mod("brick5"), 2);
        }
    };
    public static LOTRRoadType RHUN = new LOTRRoadType() {

        @Override
        public RoadBlock getBlock(RandomSource rand, boolean top, boolean slab) {
            if (slab) {
                if (rand.nextInt(8) == 0) {
                    return rand.nextBoolean() ? new RoadBlock(LOTRLegacyBlocks.mod("slabSingle12"), 1) : new RoadBlock(LOTRLegacyBlocks.mod("slabSingle12"), 2);
                }
                return new RoadBlock(LOTRLegacyBlocks.mod("slabSingle12"), 0);
            }
            if (rand.nextInt(8) == 0) {
                return rand.nextBoolean() ? new RoadBlock(LOTRLegacyBlocks.mod("brick5"), 13) : new RoadBlock(LOTRLegacyBlocks.mod("brick5"), 14);
            }
            return new RoadBlock(LOTRLegacyBlocks.mod("brick5"), 11);
        }
    };

    public abstract RoadBlock getBlock(RandomSource rand, boolean top, boolean slab);

    public float getRepair() {
        return 1.0f;
    }

    public LOTRRoadType setRepair(float f) {
        LOTRRoadType baseRoad = this;
        return new LOTRRoadType() {

            @Override
            public RoadBlock getBlock(RandomSource rand, boolean top, boolean slab) {
                return baseRoad.getBlock(rand, top, slab);
            }

            @Override
            public float getRepair() {
                return f;
            }

            @Override
            public boolean hasFlowers() {
                return baseRoad.hasFlowers();
            }
        };
    }

    public boolean hasFlowers() {
        return false;
    }

    public LOTRRoadType setHasFlowers(boolean flag) {
        LOTRRoadType baseRoad = this;
        return new LOTRRoadType() {

            @Override
            public RoadBlock getBlock(RandomSource rand, boolean top, boolean slab) {
                return baseRoad.getBlock(rand, top, slab);
            }

            @Override
            public float getRepair() {
                return baseRoad.getRepair();
            }

            @Override
            public boolean hasFlowers() {
                return flag;
            }
        };
    }

    public abstract static class BridgeType {
        public static BridgeType DEFAULT = new BridgeType() {

            @Override
            public RoadBlock getBlock(RandomSource rand, boolean slab) {
                if (slab) {
                    return new RoadBlock(LOTRLegacyBlocks.vanilla("wooden_slab"), 0);
                }
                return new RoadBlock(LOTRLegacyBlocks.vanilla("planks"), 0);
            }

            @Override
            public RoadBlock getEdge(RandomSource rand) {
                return new RoadBlock(LOTRLegacyBlocks.mod("woodBeamV1"), 0);
            }

            @Override
            public RoadBlock getFence(RandomSource rand) {
                return new RoadBlock(LOTRLegacyBlocks.vanilla("fence"), 0);
            }
        };
        public static BridgeType MIRKWOOD = new BridgeType() {

            @Override
            public RoadBlock getBlock(RandomSource rand, boolean slab) {
                if (slab) {
                    return new RoadBlock(LOTRLegacyBlocks.mod("woodSlabSingle"), 2);
                }
                return new RoadBlock(LOTRLegacyBlocks.mod("planks"), 2);
            }

            @Override
            public RoadBlock getEdge(RandomSource rand) {
                return new RoadBlock(LOTRLegacyBlocks.mod("woodBeam1"), 2);
            }

            @Override
            public RoadBlock getFence(RandomSource rand) {
                return new RoadBlock(LOTRLegacyBlocks.mod("fence"), 2);
            }
        };
        public static BridgeType CHARRED = new BridgeType() {

            @Override
            public RoadBlock getBlock(RandomSource rand, boolean slab) {
                if (slab) {
                    return new RoadBlock(LOTRLegacyBlocks.mod("woodSlabSingle"), 3);
                }
                return new RoadBlock(LOTRLegacyBlocks.mod("planks"), 3);
            }

            @Override
            public RoadBlock getEdge(RandomSource rand) {
                return new RoadBlock(LOTRLegacyBlocks.mod("woodBeam1"), 3);
            }

            @Override
            public RoadBlock getFence(RandomSource rand) {
                return new RoadBlock(LOTRLegacyBlocks.mod("fence"), 3);
            }
        };

        public abstract RoadBlock getBlock(RandomSource rand, boolean slab);

        public abstract RoadBlock getEdge(RandomSource rand);

        public abstract RoadBlock getFence(RandomSource rand);

    }

    /** A block and the 1.7.10 metadata it was placed with. */
    public record RoadBlock(LegacyBlock block, int meta) {
        public BlockState state() {
            return this.block.state(this.meta);
        }
    }
}

